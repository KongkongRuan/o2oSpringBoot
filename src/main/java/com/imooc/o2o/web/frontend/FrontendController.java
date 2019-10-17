package com.imooc.o2o.web.frontend;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

//通过springMVC的转发加上头和尾
//按照spring-web.xml中的3.视图解析器加上头和尾转发
@Controller
@RequestMapping("/frontend")
public class FrontendController {
	/**
	 * 商铺首页路由转发
	 * @return
	 */
	@RequestMapping(value="/index",method=RequestMethod.GET)
	private String index() {
		return "frontend/index";
	}
	/**
	 * 商铺列表路由转发
	 * @return
	 */
	@RequestMapping(value="/shoplist",method=RequestMethod.GET)
	private String shoplist() {
		return "frontend/shoplist";
	}
	/**
	 * 商铺详情路由转发
	 * @return
	 */
	@RequestMapping(value="/shopdetail",method=RequestMethod.GET)
	private String shopdetail() {
		return "frontend/shopdetail";
	}
	/**
	 * 商品详情的路由转发
	 * @return
	 */
	@RequestMapping(value="/productdetail",method=RequestMethod.GET)
	private String productdetail() {
		return "frontend/productdetail";
	}
}
