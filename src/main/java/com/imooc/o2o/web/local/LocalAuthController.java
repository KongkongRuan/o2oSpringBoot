package com.imooc.o2o.web.local;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

//通过springMVC的转发加上头和尾
//按照spring-web.xml中的3.视图解析器加上头和尾转发
@Controller
@RequestMapping(value="local",method= {RequestMethod.GET})
public class LocalAuthController {
	@RequestMapping(value="/register")
	public String register() {
		return "local/register";
	}
	@RequestMapping(value="/login")
	public String login() {
		return "local/login";
	}
	@RequestMapping(value="/modifipassword")
	public String modifiPassword() {
		return "local/modifipassword";
	}
	@RequestMapping(value="/resetpassword")
	public String resetPassword() {
		return "local/resetpassword";
	}
}
