package com.imooc.o2o.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.imooc.o2o.dao.ProductDao;
import com.imooc.o2o.dao.ProductImgDao;
import com.imooc.o2o.dto.ImageHolder;
import com.imooc.o2o.dto.ProductExecution;
import com.imooc.o2o.entity.Product;
import com.imooc.o2o.entity.ProductImg;
import com.imooc.o2o.enums.ProductStateEnum;
import com.imooc.o2o.exception.ProductOperationException;
import com.imooc.o2o.service.ProductService;
import com.imooc.o2o.util.ImageUtil;
import com.imooc.o2o.util.PageCalculator;
import com.imooc.o2o.util.PathUtil;
@Service
public class ProductServiceImpl implements ProductService {
	
	@Autowired
	private ProductDao productDao;
	@Autowired
	private ProductImgDao productImgDao;
	@Override
	@Transactional
	//1.处理缩略图，获取缩略图相对路径并赋值给product
	//2.往tb_product写入商品信息，获取productId
	//3.结合productId批量处理商品详情图
	//4.将商品详情图列表批量插入tb_product_img中
	public ProductExecution addProduct(Product product, ImageHolder thumbnail, List<ImageHolder> productImgHolderList)
			throws ProductOperationException {
		//空值判断
		if(product!=null&&product.getShop()!=null&&product.getShop().getShopId()!=null) {
			//给商品设置默认属性
			product.setCreateTime(new Date());
			product.setLastEditTime(new Date());
			//设置上架状态为1
			product.setEnableStatus(1);
			if(thumbnail!=null) {
				addThumbnail(product,thumbnail);
			}
			try {
				//创建商品信息
				int effectNum=productDao.insertProduct(product);
				if(effectNum<=0) {
					throw new ProductOperationException("创建商品失败");
				}
			}catch(ProductOperationException e) {
				throw new ProductOperationException("创建商品失败"+e.getMessage());
			}
			//若商品详情图不为空则添加
			if(productImgHolderList!=null&&productImgHolderList.size()>0) {
				addProductImgList(product,productImgHolderList);
			}
			return new ProductExecution(ProductStateEnum.SUCCESS, product);
		}else {
			//传参为空返回错误信息
			return new ProductExecution(ProductStateEnum.EMPTY);
		}
	}
	
	/**
	 * 批量添加图片
	 * @param product
	 * @param productImgHolderList
	 */
	private void addProductImgList(Product product, List<ImageHolder> productImgHolderList) {
		// 获取图片路径，直接存放到相应店铺的文件夹下
		String dest=PathUtil.getShopImagePath(product.getShop().getShopId());
		List<ProductImg>productImgList = new ArrayList<>();
		for(ImageHolder productImgHolder:productImgHolderList) {
			String imgAddr = ImageUtil.generateNormalImg(productImgHolder, dest);
			ProductImg productImg = new ProductImg();
			productImg.setImgAddr(imgAddr);
			productImg.setCreateTime(new Date());
			productImg.setProductId(product.getProductId());
			productImgList.add(productImg);
		}
		if(productImgList.size()>0) {
			try {
				int effectNum=productImgDao.batchInsertProductImg(productImgList);
				if(effectNum<=0) {
					throw new ProductOperationException("创建商品详情图片失败");
				}
			}catch(ProductOperationException e) {
				throw new ProductOperationException("创建商品详情图片失败"+e.getMessage());
			}
		}
	}
	
	private void deleteProductImgList(long productId) {
		//根据productId获取原有的图片
		List<ProductImg>productImgList = productImgDao.queryProductImgList(productId);
		//删除原来的图片
		for(ProductImg productImg : productImgList) {
			ImageUtil.deleteFileOrPath(productImg.getImgAddr());
		}
		//删除数据库里原有的图片信息
		productImgDao.deleteProductImgByProductId(productId);
	}
	/**
	 * 添加缩略图 
	 * @param product
	 * @param thumbnail
	 */
	private void addThumbnail(Product product, ImageHolder thumbnail) {
		System.out.println(thumbnail.getImageName());
		String dest=PathUtil.getShopImagePath(product.getShop().getShopId());
		String thumbnailAddr = ImageUtil.generateThumbnail(thumbnail, dest);
		product.setImgAddr(thumbnailAddr);
		
	}

	@Override
	public Product getProductById(long productId) {
		return productDao.quaryProductById(productId);
	}

	@Override
	@Transactional
	//1.若缩略图参数有值，则处理缩略图
	//若原先存在缩略图，则先删除再添加新图，之后获取缩略图相对路径，并赋给product
	//2.若商品详情图列表参数有值，对商品详情图片列表进行同样的操作
	//------重点：不仅要删除数据库里面的信息，还要删除本地的图片以及目录---------
	//3.将tb_product_img下面的该商品原先的商品详情图全部删除
	//4.更新tb_product的信息
	public ProductExecution modifyProduct(Product product, ImageHolder thumbnail, List<ImageHolder> productImgHolderList)
			throws ProductOperationException {
		if(product!=null&&product.getShop()!=null&&product.getShop().getShopId()!=null) {
			product.setLastEditTime(new Date());
			if(thumbnail!=null) {
				//先获取原有信息，如果存在则删除掉
				Product tempProduct = productDao.quaryProductById(product.getProductId());
				if(tempProduct.getImgAddr()!=null) {
					ImageUtil.deleteFileOrPath(tempProduct.getImgAddr());
				}
				addThumbnail(product,thumbnail);
			}
			//如果传入参数中有商品详情图，则将原先的删除，再添加新的
			if(productImgHolderList!=null&&productImgHolderList.size()>0) {
				productImgDao.deleteProductImgByProductId(product.getProductId());
				deleteProductImgList(product.getProductId());
				addProductImgList(product,productImgHolderList);
			}
			try {
				//更新商品信息
				int effectNum = productDao.updateProduct(product);
				if(effectNum<=0) {
					throw new ProductOperationException("更新商品信息失败");
				}
				return new ProductExecution(ProductStateEnum.SUCCESS,product);
			}catch(Exception e){
				throw new ProductOperationException("更新商品信息失败"+e.getMessage());
			}
		}else {
			return new ProductExecution(ProductStateEnum.EMPTY);
		}
		
	}

	@Override
	public ProductExecution getProductList(Product productCondition, int pageIndex, int pageSize) {
		int rowIndex=PageCalculator.calculateRowIndex(pageIndex, pageSize);
		List<Product> productList= productDao.queryProductList(productCondition, rowIndex, pageSize);
		int count = productDao.queryProductCount(productCondition);
		ProductExecution pe=new ProductExecution();
		if(productList!=null) {
			pe.setProductList(productList);
			pe.setCount(count);
		}else {
			pe.setState(ProductStateEnum.INNER_ERROR.getState());
		}
		return pe;
	}

}
