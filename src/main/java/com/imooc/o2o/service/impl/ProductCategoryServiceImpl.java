package com.imooc.o2o.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.imooc.o2o.dao.ProductCategoryDao;
import com.imooc.o2o.dao.ProductDao;
import com.imooc.o2o.dto.ProductCategoryExecution;
import com.imooc.o2o.entity.ProductCategory;
import com.imooc.o2o.enums.ProductCategoryStateEnum;
import com.imooc.o2o.exception.ProductCategoryOperationException;


@Service
public class ProductCategoryServiceImpl implements com.imooc.o2o.service.ProductCategoryService {
	@Autowired
	private ProductCategoryDao  productCategoryDao;
	
	@Autowired
	private ProductDao productDao;
	public List<ProductCategory> getProductCategoryList(Long shopId) {
		return productCategoryDao.queryProductCategory(shopId);
	}

	@Override
	public ProductCategoryExecution batchAddProductCategory(List<ProductCategory> productCategoryList)
			throws ProductCategoryOperationException {
		
		if(productCategoryList!=null&&productCategoryList.size()>0) {
			try {
				int effectedNum = productCategoryDao.batchInsertProductCategory(productCategoryList);
				if(effectedNum>0) {
					return new ProductCategoryExecution(ProductCategoryStateEnum.SUCCESS,productCategoryList);
				}else {
					return new ProductCategoryExecution(ProductCategoryStateEnum.INNER_ERROR);
				}
			}catch(Exception e) {
				throw new ProductCategoryOperationException("batchInsertProductCategory erro"+e.getMessage());
			}
		}else {
			return new ProductCategoryExecution(ProductCategoryStateEnum.EMPTY_LIST);
		}
	}

	
	
	public ProductCategoryExecution deleteProductCategory(long productCategoryId, long shopId)
			throws ProductCategoryOperationException {
		//解除tb_product里的商品与该productCategoryId的关联
		try {
			int effectedNum = productDao.updateProductCategoryToNull(productCategoryId);
			if(effectedNum<=0) {
				throw new ProductCategoryOperationException("商品类别删除失败！");
			}
		}catch(Exception e) {
			throw new ProductCategoryOperationException("deleteProductCategory error"+e.getMessage());
		}
		//删除该productcategory
		try {
			int effectedNum = productCategoryDao.deleteProductCategory(productCategoryId, shopId);
			if(effectedNum<=0) {
				throw new ProductCategoryOperationException("商品类别删除失败！");
			}else {
				return new ProductCategoryExecution(ProductCategoryStateEnum.SUCCESS);
			}
		}catch(Exception e) {
			throw new ProductCategoryOperationException("deleteProductCategory error"+e.getMessage());
		}
	}
}
