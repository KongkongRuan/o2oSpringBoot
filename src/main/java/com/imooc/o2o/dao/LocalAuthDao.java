package com.imooc.o2o.dao;


import org.apache.ibatis.annotations.Param;

import com.imooc.o2o.entity.LocalAuth;

public interface LocalAuthDao {
	
	/**
	 * 注册
	 * @param localAuth
	 */
	int insertLocalAuth(LocalAuth localAuth);
	
	/**
	 * 
	 * @param username
	 * @param passworld
	 * @return
	 */
	LocalAuth  queryLocalInfoByUserName(String username);
	/**
	 * 
	 * @param username
	 * @return
	 */
	int countLocalInfoByUserName(String username);
	/**
	 * 通过用户名更改密码
	 * @param username
	 * @param password
	 * @return
	 */
	int updatePassword(@Param("username") String username,@Param("newPassword")  String newPassword);
}
