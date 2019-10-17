package com.imooc.o2o.web.frontend;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.imooc.o2o.dto.ProductExecution;
import com.imooc.o2o.entity.Product;
import com.imooc.o2o.entity.ProductCategory;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.service.ProductCategoryService;
import com.imooc.o2o.service.ProductService;
import com.imooc.o2o.service.ShopService;
import com.imooc.o2o.util.HttpServletRequestUtil;

@Controller
@RequestMapping("/frontend")
public class ShopDetailController {
	@Autowired
	private ShopService shopService;
	
	@Autowired
	private ProductService productService;
	@Autowired
	private ProductCategoryService productCategoryService;
	/**
	 * 获取店铺信息以及该店铺下面的商品类别列表
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/listshopdetailpageinfo")
	@ResponseBody
	private Map<String,Object> listShopDetailPageInfo(HttpServletRequest request){
		Map<String,Object>modelMap = new HashMap<String,Object>();
		//获取前台传过来的shopId
		long shopId = HttpServletRequestUtil.getLong(request, "shopId");
		Shop shop = null;
		List<ProductCategory> productCategoryList = null;
		if(shopId!=-1) {
			//获取店铺Id为shopId的店铺信息
			shop = shopService.getByShopId(shopId);
			//获取店铺下面的商品类别列表
			productCategoryList = productCategoryService.getProductCategoryList(shopId);
			modelMap.put("shop", shop);
			modelMap.put("productCategoryList", productCategoryList);
			modelMap.put("success", true);
		}else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "empty shopId");
		}
		return modelMap;
	}
	
	/**
	 * 根据查询条件分页列出该店铺下的所有商品
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/listproductsbyshop")
	@ResponseBody
	private Map<String,Object> listProductsByShop(HttpServletRequest request){
		Map<String,Object>modelMap = new HashMap<String,Object>();
		int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
		int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
		long shopId = HttpServletRequestUtil.getLong(request, "shopId");
		if((pageIndex>-1)&&(pageSize>-1)&&(shopId>-1L)) {
			//尝试获取商品类别id
			long productCategoryId = HttpServletRequestUtil.getLong(request, "productCategoryId");
			//尝试获取模糊查找商品名
			String productName = HttpServletRequestUtil.getString(request, "productName");
			//组合查询条件
			Product productCondition = compactProductCondition4Search(shopId, productCategoryId, productName);
			ProductExecution pe = productService.getProductList(productCondition, pageIndex, pageSize);
			modelMap.put("productList", pe.getProductList());
			modelMap.put("count", pe.getCount());
			modelMap.put("success", true);
		}else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "empty pageIndex or pageSize or shopId");
		}
		return modelMap;
	}
	private Product compactProductCondition4Search(long shopId,long productCategoryId,String productName) {
		Product productCondition = new Product();
		Shop shop = new Shop();
		shop.setShopId(shopId);
		productCondition.setShop(shop);
		if(productCategoryId!=-1L) {
			ProductCategory productCategory = new ProductCategory();
			productCategory.setProductCategoryId(productCategoryId);
			productCondition.setProductCategory(productCategory);
		}
		if(productName!=null) {
			productCondition.setProductName(productName);
		}
		productCondition.setEnableStatus(1);
		return productCondition;
	}
}
