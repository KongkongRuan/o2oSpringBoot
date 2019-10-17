package com.imooc.o2o.config.dao;

import java.beans.PropertyVetoException;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.imooc.o2o.util.DESUtil;
import com.mchange.v2.c3p0.ComboPooledDataSource;
/**
 * 配置DateSource到ioc容器里面
 * @author 三月
 *
 */
@Configuration
//配置mybatis mapper的扫描路径
@MapperScan("com.imooc.o2o.dao")
public class DateSourceConfiguration { 
	//使用@Value标签把application.properties的变量值注入到对应的变量中
	@Value("${jdbc.driver}")
	private String jdbcDriver;
	@Value("${jdbc.url}")
	private String jdbcUrl;
	@Value("${jdbc.username}")
	private String jdbcUsername;
	@Value("${jdbc.password}")
	private String jdbcPassword;
	
	@Bean(name="dataSource")
	public ComboPooledDataSource createDataSource() throws PropertyVetoException {
		//生成datasource实例
		ComboPooledDataSource dataSource = new ComboPooledDataSource();
		//跟配置文件一样设置以下信息
		//驱动
		dataSource.setDriverClass(jdbcDriver);
		//数据库连接
		dataSource.setJdbcUrl(jdbcUrl);
		//设置用户名
		dataSource.setUser(DESUtil.getDecryptString(jdbcUsername));
		//设置密码
		dataSource.setPassword(DESUtil.getDecryptString(jdbcPassword));
		//配置C3P0连接池的私有属性
		//连接池的最大线程数
		dataSource.setMaxPoolSize(30);
		//最小线程数
		dataSource.setMinPoolSize(10);
		//关闭连接后不自动commit
		dataSource.setAutoCommitOnClose(false);
		//获取连接超时时间
		dataSource.setCheckoutTimeout(10000);
		//当获取连接失败重试次数
		dataSource.setAcquireRetryAttempts(2);
		return dataSource;
	}
	
	
	
	
	
	
	
	
	
	
	
	
}
