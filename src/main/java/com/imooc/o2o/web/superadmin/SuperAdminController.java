package com.imooc.o2o.web.superadmin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
//通过springMVC的转发加上头和尾
//按照spring-web.xml中的3.视图解析器加上头和尾转发
@Controller
@RequestMapping(value="superadmin",method= {RequestMethod.GET})
public class SuperAdminController {
	@RequestMapping(value="/getshoplist")
	public String adminGetShopList() {
		return "superadmin/shopenablestatus";
	}
	@RequestMapping(value="/getuserlist")
	public String adminGetUserList() {
		return "superadmin/personmanagement";
	}
}
