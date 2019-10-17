package com.imooc.o2o.dao;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import com.imooc.o2o.entity.ProductCategory;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductCategoryDaoTest{
	@Autowired
	private ProductCategoryDao productCategoryDao;
	
	
	@Test
	public void testBQueryProductCategory() {
		List<ProductCategory> productCategoryList=productCategoryDao.queryProductCategory(1L);
		assertEquals(10,productCategoryList.size());
		System.out.println(productCategoryList.get(0).getProductCategoryName());
	}
	@Test
	public void testABatchInsertProductCategory() {
		ProductCategory productCategory1 = new ProductCategory();
		productCategory1.setProductCategoryName("商品类别4");
		productCategory1.setPriority(10);
		productCategory1.setCreateTime( new Date());
		productCategory1.setShopId(1L);
		ProductCategory productCategory2 = new ProductCategory();
		productCategory2.setProductCategoryName("商品类别5");
		productCategory2.setPriority(30);
		productCategory2.setCreateTime( new Date());
		productCategory2.setShopId(1L);
		List<ProductCategory>productCategoryList = new ArrayList<ProductCategory>();
		productCategoryList.add(productCategory1);
		productCategoryList.add(productCategory2);
		int effectedNum =productCategoryDao.batchInsertProductCategory(productCategoryList);
		assertEquals(2, effectedNum);
	}
	@Test
	public void testCDeleteProductCategory() {
		List<ProductCategory> productCategoryList=productCategoryDao.queryProductCategory(1L);
		for(ProductCategory pc:productCategoryList) {
			if(pc.getProductCategoryName().equals("商品类别4")||pc.getProductCategoryName().equals("商品类别5")) {
				System.out.println("ProductCategoryId为"+pc.getProductCategoryId());
				int effectedNum =productCategoryDao.deleteProductCategory(pc.getProductCategoryId(), pc.getShopId());
				assertEquals(1, effectedNum);
			}
		}
	}
}
