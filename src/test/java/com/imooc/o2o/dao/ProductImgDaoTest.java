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

import com.imooc.o2o.entity.ProductImg;
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductImgDaoTest{
	@Autowired
	private ProductImgDao productImgDao;
	
	@Test
	
	public void testABatchInsertProductImg() throws Exception{
		//productId为1的商品里添加两个详情图片记录
		ProductImg productImg1= new ProductImg();
		productImg1.setCreateTime(new Date());
		productImg1.setImgAddr("图片1路径");
		productImg1.setImgDesc("图片1描述");
		productImg1.setPriority(1);
		productImg1.setProductId(1L);
		ProductImg productImg2= new ProductImg();
		productImg2.setCreateTime(new Date());
		productImg2.setImgAddr("图片2路径");
		productImg2.setImgDesc("图片2描述");
		productImg2.setPriority(1);
		productImg2.setProductId(1L);
		
		List<ProductImg>productImgList = new ArrayList<ProductImg>();
		productImgList.add(productImg1);
		productImgList.add(productImg2);
		
		int effectedNum = productImgDao.batchInsertProductImg(productImgList);
		assertEquals(effectedNum,2 );
	}
	
	@Test
	public void testBQueryProductImgList() {
		List<ProductImg>productImgList = productImgDao.queryProductImgList(1L);
		assertEquals(2, productImgList.size());
	}
	
	@Test
	public void testCDeleteProductImgByProductId()throws Exception{
		long productId = 1;
		int effectedNum = productImgDao.deleteProductImgByProductId(productId);
		assertEquals(2, effectedNum);
	}
	
	
	 
	
}
