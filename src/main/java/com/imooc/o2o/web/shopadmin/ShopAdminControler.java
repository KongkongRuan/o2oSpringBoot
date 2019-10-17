package com.imooc.o2o.web.shopadmin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
//通过springMVC的转发加上头和尾
//按照spring-web.xml中的3.视图解析器加上头和尾转发
@Controller
@RequestMapping(value="shopadmin",method= {RequestMethod.GET})
public class ShopAdminControler {
	@RequestMapping(value="/shopoperation")
	public String shopOperation() {
		return "shop/shopoperation";
	}
	@RequestMapping(value="/shoplist")
	public String shopList() {
		return "shop/shoplist";
	}
	@RequestMapping(value="/shopmanagement")
	public String shopManagement() {
		return "shop/shopmanagement";
	}
	@RequestMapping(value="/productcategorymanagement")
	public String productCategoryManagement() {
		return "shop/productcategorymanagement";
	}
	//商品操作
	@RequestMapping(value="/productoperation")
	public String productOperation() {
		return "shop/productoperation";
	}
	//商品管理
	@RequestMapping(value="/productmanagement")
	public String productManagement() {
		return "shop/productmanagement";
	}
}
