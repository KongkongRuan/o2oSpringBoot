package com.imooc.o2o.config.web;

import javax.servlet.ServletException;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.google.code.kaptcha.servlet.KaptchaServlet;
import com.imooc.o2o.interceptor.ShopadminInterceptor;
import com.imooc.o2o.interceptor.SuperadminInterceptor;
/**
 * 开启Mvc，自动注入Spring容器。WebMvcConfigurerAdapter：配置视图解析器
 * 当一个类实现了（ApplicationContextAware）这个接口后，这个类就可以方便获得ApplicationContext中所有的bean
 * @author 三月
 *
 */
@SuppressWarnings("deprecation")
@Configuration
@EnableWebMvc
//等价于<mvc:annotation-driven />
public class MvcConfiguration extends WebMvcConfigurerAdapter implements ApplicationContextAware {
	//Spring容器
	private ApplicationContext applicationContext;
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext=applicationContext;
	}
	/**
	 * 静态资源配置
	 * 
	 */
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		//registry.addResourceHandler("/resources/**").addResourceLocations("classpath:/resources/");	
		registry.addResourceHandler("/upload/**").addResourceLocations("file:/home/o2o/upload/");
		//registry.addResourceHandler("/upload/**").addResourceLocations("file:E:\\projectdev\\upload\\");
	}
	/**
	 * 定义默认的请求处理器
	 */
	@Override
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
		configurer.enable();
	}
	/**
	 * 定义视图解析器
	 */
	@Bean(name="viewResolver")
	public ViewResolver createViewResolver() {
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		//设置Spring容器
		viewResolver.setApplicationContext(this.applicationContext);
		viewResolver.setPrefix("/WEB-INF/html/");
		viewResolver.setSuffix(".html");
		return viewResolver;
	}
	/**
	 * 文件上传解析器
	 */
	@Bean(name="multipartResolver")
	public CommonsMultipartResolver createMultipartResolver() {
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
		multipartResolver.setDefaultEncoding("utf-8");
		multipartResolver.setMaxUploadSize(20971520);
		multipartResolver.setMaxInMemorySize(20971520);
		return multipartResolver;
	}
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		String superadminInterceptPath="/superadmin/**";
		String shopadminInterceptPath="/shopadmin/**";
		//注册第一个拦截器
		InterceptorRegistration logIR1 = registry.addInterceptor(new SuperadminInterceptor());
		//配置拦截路径
		logIR1.addPathPatterns(superadminInterceptPath);
		//配置不拦截的路径
		//logIR1.excludePathPatterns("/");
		//第二个
		InterceptorRegistration logIR2 = registry.addInterceptor(new ShopadminInterceptor());
		//配置第二个拦截器的拦截路径
		logIR2.addPathPatterns(shopadminInterceptPath);
	}
	




	@Value("${kaptcha.border}")
	private String border;
	@Value("${kaptcha.textproducer.font.color}")
	private String fcolor;
	@Value("${kaptcha.image.width}")
	private String width;
	@Value("${kaptcha.textproducer.char.string}")
	private String cString;
	@Value("${kaptcha.image.height}")
	private String height;
	@Value("${kaptcha.textproducer.font.size}")
	private String fsize;
	@Value("${kaptcha.noise.impl}")
	private String nimpl;
	@Value("${kaptcha.textproducer.char.length}")
	private String clength;
	@Value("${kaptcha.textproducer.font.names}")
	private String fnames;
	/**
	 * 把web.xml对Kaptcha验证码的配置引入到这里
	 */
	@Bean
	public ServletRegistrationBean servletRegistrationBean() throws ServletException{
		ServletRegistrationBean servlet = new ServletRegistrationBean(new KaptchaServlet(),"/Kaptcha");
		servlet.addInitParameter("kaptcha.border", border);
		servlet.addInitParameter("kaptcha.textproducer.font.color", fcolor);
		servlet.addInitParameter("kaptcha.image.width", width);
		servlet.addInitParameter("kaptcha.textproducer.char.string", cString);
		servlet.addInitParameter("kaptcha.image.height", height);
		servlet.addInitParameter("kaptcha.textproducer.font.size", fsize);
		servlet.addInitParameter("kaptcha.noise.impl", nimpl);
		servlet.addInitParameter("kaptcha.textproducer.char.length", clength);
		servlet.addInitParameter("kaptcha.textproducer.font.names", fnames);
		return servlet;
	}
}
