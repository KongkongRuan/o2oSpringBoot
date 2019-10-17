package com.imooc.o2o.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.imooc.o2o.dao.ShopDao;
import com.imooc.o2o.dto.ImageHolder;
import com.imooc.o2o.dto.ShopExecution;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.enums.ShopStateEnum;
import com.imooc.o2o.exception.ShopOperationException;
import com.imooc.o2o.service.ShopService;
import com.imooc.o2o.util.ImageUtil;
import com.imooc.o2o.util.PageCalculator;
import com.imooc.o2o.util.PathUtil;

@Service
public class ShopServiceImpl implements ShopService {
	@Autowired
	private ShopDao shopDao;

	@Transactional
	public ShopExecution addShop(Shop shop, ImageHolder shopImgInputStream) {
		// 空值判断
		if (shop == null) {
			return new ShopExecution(ShopStateEnum.NULL_SHOP);
		}
		try {
			// 给店铺赋初始值
			shop.setEnableStatus(0);
			shop.setCreateTime(new Date());
			shop.setLastEditTime(new Date());
			// 添加店铺信息
			int effectedNum = shopDao.insertShop(shop);
			if (effectedNum <= 0) {
				// 抛出RuntimeException事务才会终止并且回滚而Exception则不会回滚
				throw new RuntimeException("店铺创建失败");
			} else {
				System.out.println("创建店铺成功");
				if (shopImgInputStream != null) {
					// 存储图片
					System.out.println("图片路径有效");
					try {
						addShopImg(shop, shopImgInputStream);
						System.out.println("设置当前图片地址成功");
					} catch (Exception e) {
						throw new RuntimeException("addShopImg error:" + e.getMessage());
					}
					// 更新店铺的图片地址
					effectedNum = shopDao.updateShop(shop);
					System.out.println("更新店铺成功");
					if (effectedNum <= 0) {
						throw new RuntimeException("更新图片地址失败");
					}
				}
			}
		} catch (Exception e) {
			throw new RuntimeException("addShop error:" + e.getMessage());
		}
		return new ShopExecution(ShopStateEnum.CHECK, shop);
	}

	private void addShopImg(Shop shop, ImageHolder shopImgInputStream) {
		// 获取shop图片目录的相对值路径
		String dest = PathUtil.getShopImagePath(shop.getShopId());
		String shopImgAddr = ImageUtil.generateThumbnail(shopImgInputStream, dest);
		shop.setShopImg(shopImgAddr);
	}

	public Shop getByShopId(Long shopId) {
		return shopDao.queryByShopId(shopId);
	}

	public ShopExecution modifyShop(Shop shop, ImageHolder shopImgInputStream)
			throws ShopOperationException {
		if (shop == null || shop.getShopId() == null) {
			return new ShopExecution(ShopStateEnum.NULL_SHOP);
		} else {
			// 1.判断是否需要处理图片
			try {
				if (shopImgInputStream != null && shopImgInputStream.getImageName() != null && !shopImgInputStream.getImageName().equals("")) {
					
					Shop tempShop = shopDao.queryByShopId(shop.getShopId());
					if (tempShop.getShopImg() != null) {
						
						ImageUtil.deleteFileOrPath(tempShop.getShopImg());
						
					}
						addShopImg(shop, shopImgInputStream);
						
					
				}
				// 2.更新店铺信息
				shop.setLastEditTime(new Date());
				int effectedNum = shopDao.updateShop(shop);
				if (effectedNum <= 0) {
					return new ShopExecution(ShopStateEnum.INNER_ERROR);
				} else {
					shop = shopDao.queryByShopId(shop.getShopId());
					return new ShopExecution(ShopStateEnum.SUCCESS, shop);
				}
			} catch (Exception e) {
				new ShopOperationException("modifyShop error:" + e.getMessage());
			}
		}
		return null;

	}
	public ShopExecution getShopList(Shop shopCondition, int pageIndex, int pageSize) {
		int rowIndex=PageCalculator.calculateRowIndex(pageIndex, pageSize);
		List<Shop> shopList=shopDao.queryShopList(shopCondition, rowIndex, pageSize);
		int count = shopDao.queryShopCount(shopCondition);
		ShopExecution se=new ShopExecution();
		if(shopList!=null) {
			se.setState(ShopStateEnum.SUCCESS.getState());
			se.setShopList(shopList);
			se.setCount(count);
		}else {
			se.setState(ShopStateEnum.INNER_ERROR.getState());
		}
		return se;
	}

	@Override
	public ShopExecution modifyShopEnableStatus(Shop shop) throws ShopOperationException {
		try {
		if(shop!=null && shop.getShopId()!=null && shop.getEnableStatus()!=null) {
			shop.setLastEditTime(new Date());
			int effectedNum =shopDao.updateShop(shop);
			if (effectedNum <= 0) {
				return new ShopExecution(ShopStateEnum.INNER_ERROR);
			} else {
				shop = shopDao.queryByShopId(shop.getShopId());
				return new ShopExecution(ShopStateEnum.SUCCESS, shop);
			}
		}else {
			return new ShopExecution(ShopStateEnum.INNER_ERROR);
		}
		}catch(Exception e) {
			new ShopOperationException("modifyShopEnableStatus error:" + e.getMessage());
		}
		return null;
	}

}
