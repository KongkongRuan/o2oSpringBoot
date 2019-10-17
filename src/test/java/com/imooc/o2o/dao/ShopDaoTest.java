package com.imooc.o2o.dao;

import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.imooc.o2o.entity.Area;
import com.imooc.o2o.entity.PersonInfo;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.entity.ShopCategory;
@RunWith(SpringRunner.class)
@SpringBootTest
public class ShopDaoTest {

    @Autowired
    private ShopDao shopDao;
    @Test
    public void testQueryShopListAndCount() {
    	Shop shopCondition = new Shop();
    	ShopCategory childCategory = new ShopCategory();
    	ShopCategory parentCategory = new ShopCategory();
    	parentCategory.setShopCategoryId(1L);
    	childCategory.setParent(parentCategory);
    	shopCondition.setShopCategory(childCategory);
    	List<Shop>shopList= shopDao.queryShopList(shopCondition, 0, 4);
    	int count=shopDao.queryShopCount(shopCondition);
    	System.out.println("获取到的店铺列表总数为："+shopList.size());
    	System.out.println("店铺列表总数为："+count);
    }
    
    
    
    @Test
    @Ignore
    public void testQueryByShopId() {
    	long shopId=1;
    	Shop shop=shopDao.queryByShopId(shopId);
    	System.out.println("区域id："+shop.getArea().getAreaId());
    	System.out.println("区域名字："+shop.getArea().getAreaName());
    }
    
    
    
    @Test
    @Ignore
    public void testInsertShop() {
        Shop shop = new Shop();
        PersonInfo owner = new PersonInfo();
        Area area = new Area();
        ShopCategory shopCategory = new ShopCategory();
        owner.setUserId(1L);
        area.setAreaId(2);
        shopCategory.setShopCategoryId(1L);
        shop.setOwner(owner);
        shop.setArea(area);
        shop.setShopCategory(shopCategory);
        shop.setShopName("测试的店铺");
        shop.setShopDesc("test");
        shop.setShopAddr("test");
        shop.setPhone("test");
        shop.setShopImg("test");
        shop.setPriority(0);
        shop.setCreateTime(new Date());
        shop.setEnableStatus(1);
        shop.setAdvice("审核中");
        int effectedNum = shopDao.insertShop(shop);
        assertEquals(1, effectedNum);
    }
    @Test
    @Ignore
    public void testUpdateShop() {
        Shop shop = new Shop();
        shop.setShopId(1L);
        shop.setShopDesc("测试描述5");
        shop.setShopAddr("测试地址5");
        shop.setLastEditTime(new Date());
        int effectedNum = shopDao.updateShop(shop);
        assertEquals(1, effectedNum);
    }
    
}

