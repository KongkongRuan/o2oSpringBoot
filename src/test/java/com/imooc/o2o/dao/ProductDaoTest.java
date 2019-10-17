package com.imooc.o2o.dao;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.imooc.o2o.entity.Product;
import com.imooc.o2o.entity.ProductCategory;
import com.imooc.o2o.entity.ProductImg;
import com.imooc.o2o.entity.Shop;
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductDaoTest  {
	@Autowired
	private ProductImgDao productImgDao;
	@Autowired
	private ProductDao productDao;
	@Test
	@Ignore
	public void testAInsertProduct()throws Exception{
		Shop shop1=new Shop();
		shop1.setShopId(1L);
		ProductCategory pc1=new ProductCategory();
		pc1.setProductCategoryId(1L);
		//初始化三个商品实例并添加进shopId为1的店铺里
		//同时商品类别Id也为1
		Product product1 = new Product();
		product1.setCreateTime(new Date());
		product1.setEnableStatus(1);
		product1.setImgAddr("商品1图片地址");
		product1.setLastEditTime(new Date());
		product1.setNormalPrice("22");
		product1.setPriority(1);
		product1.setProductCategory(pc1);
		product1.setProductDesc("商品1简介");
		product1.setProductName("商品1");
		product1.setPromotionPrice("33");
		product1.setShop(shop1);
		
		Product product2 = new Product();
		product2.setCreateTime(new Date());
		product2.setEnableStatus(2);
		product2.setImgAddr("商品2图片地址");
		product2.setLastEditTime(new Date());
		product2.setNormalPrice("22");
		product2.setPriority(2);
		product2.setProductCategory(pc1);
		product2.setProductDesc("商品2简介");
		product2.setProductName("商品2");
		product2.setPromotionPrice("33");
		product2.setShop(shop1);
		
		Product product3 = new Product();
		product3.setCreateTime(new Date());
		product3.setEnableStatus(3);
		product3.setImgAddr("商品1图片地址");
		product3.setLastEditTime(new Date());
		product3.setNormalPrice("22");
		product3.setPriority(3);
		product3.setProductCategory(pc1);
		product3.setProductDesc("商品3简介");
		product3.setProductName("商品3");
		product3.setPromotionPrice("33");
		product3.setShop(shop1);
		
		int effectNum = productDao.insertProduct(product1);
		assertEquals(effectNum, 1);
		
		effectNum = productDao.insertProduct(product2);
		assertEquals(effectNum, 1);
		
		effectNum = productDao.insertProduct(product3);
		assertEquals(effectNum, 1);
	}
	
	@Test
	public void testEUpdateProductCategoryToNull() {
		int effectedNum = productDao.updateProductCategoryToNull(1);
		assertEquals(2, effectedNum);
	}
	
	@Test
	@Ignore
	public void testBQueryProductList()throws Exception{
		Product productCondition = new Product();
		List<Product> productList = productDao.queryProductList(productCondition,0,3);
		assertEquals(3, productList.size());
		//查询商品总数
		int count = productDao.queryProductCount(productCondition);
		assertEquals(4, count);
		//测试模糊查询
		productCondition.setProductName("正式");
		List<Product> productList2 = productDao.queryProductList(productCondition,0,2);
		assertEquals(2, productList2.size());
		count = productDao.queryProductCount(productCondition);
		assertEquals(3, count);
	}
	
	@Test
	@Ignore
	public void testCQueryProductByProductId()throws Exception{
		long productId = 65;
		//初始化两个商品详情图实例作为productId为1的商品下的详情图片
		//批量插入到商品详情图表中
		ProductImg productImg1 = new ProductImg();
		productImg1.setImgAddr("图片1");
		productImg1.setImgDesc("测试图片1");
		productImg1.setCreateTime(new Date());
		productImg1.setPriority(1);
		productImg1.setProductId(productId);
		ProductImg productImg2 = new ProductImg();
		productImg2.setImgAddr("图片2");
		productImg2.setImgDesc("测试图片2");
		productImg2.setCreateTime(new Date());
		productImg2.setPriority(1);
		productImg2.setProductId(productId);
		List<ProductImg>productImgList = new ArrayList<ProductImg>();
		productImgList.add(productImg1);
		productImgList.add(productImg2);
		int effectedNum = productImgDao.batchInsertProductImg(productImgList);
		assertEquals(2, effectedNum);
		//查询productId为1的商品信息并校验返回的详情图实例列表size是否为2
		Product product =productDao.quaryProductById(productId);
		assertEquals(2, product.getProductImgList().size());
		//删除新增的两个商品详情图实例
		effectedNum = productImgDao.deleteProductImgByProductId(productId);
		assertEquals(2, effectedNum);
	}
	@Ignore
	@Test
	public void testDupdateProduct()throws Exception{
		Product product = new Product();
		ProductCategory pc = new ProductCategory();
		Shop shop = new Shop();
		shop.setShopId(1L);
		pc.setProductCategoryId(2L);
		product.setProductCategory(pc);
		product.setShop(shop);
		product.setProductName("第一个产品");
		product.setProductId(1L);
		//修改productId为1的商品名称
		//以及商品类别并校验影响的行数是否为1
		int effectedNum = productDao.updateProduct(product);
		assertEquals(1, effectedNum);
	}
}
