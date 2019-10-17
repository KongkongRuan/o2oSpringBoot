package com.imooc.o2o.service;


import com.imooc.o2o.dto.ImageHolder;
import com.imooc.o2o.dto.ShopExecution;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.exception.ShopOperationException;

public interface ShopService {
	/**
	 * 根据 shopCondition分页返回相应店铺列表
	 * @param shopCondition
	 * @param rowIndex
	 * @param pageSize
	 * @return
	 */
	public ShopExecution getShopList(Shop shopCondition,int pageIndex,int pageSize);
	
	/**
	 * 通过店铺id获取店铺信息
	 * @param shopId
	 * @return
	 */
	Shop getByShopId(Long shopId);
	/**
	 * 更新店铺信息，包括对图片的处理
	 * @param shop
	 * @param shopImgInputStream
	 * @param fileName
	 * @return
	 * @throws ShopOperationException
	 */
	ShopExecution modifyShop(Shop shop,ImageHolder shopImgInputStream)throws ShopOperationException;
	/**
	 * 注册店铺信息
	 * @param shop
	 * @param shopImgInputStream
	 * @param fileName
	 * @return
	 * @throws ShopOperationException
	 */
	ShopExecution addShop(Shop shop,ImageHolder shopImgInputStream);
	
	/**
	 * 修改店铺状态
	 * @param shop
	 * @return
	 * @throws ShopOperationException
	 */
	ShopExecution modifyShopEnableStatus(Shop shop)throws ShopOperationException;
}
