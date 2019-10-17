package com.imooc.o2o.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.imooc.o2o.entity.PersonInfo;

public class ShopadminInterceptor extends HandlerInterceptorAdapter {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		PersonInfo personInfo=(PersonInfo)request.getSession().getAttribute("user");
		if(personInfo==null||personInfo.getUserId()==null||personInfo.getUserType()!=2) {
			request.getRequestDispatcher("/local/login").forward(request, response);
			return false;
		}else {
			return true;
		}
	}
}
