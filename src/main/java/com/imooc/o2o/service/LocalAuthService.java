package com.imooc.o2o.service;

import com.imooc.o2o.dto.LocalAuthExecution;
import com.imooc.o2o.entity.LocalAuth;
import com.imooc.o2o.exception.LocalAuthOperationException;

public interface LocalAuthService {
	/**
	 * 注册
	 * @param localAuth
	 * @return
	 * @throws LocalAuthOperationException
	 */
	public LocalAuthExecution register(LocalAuth localAuth) throws LocalAuthOperationException;
	/**
	 * 通过用户名得到账户
	 * @param username
	 * @return
	 */
	public LocalAuth getLocalInfoByUserName(String username);
	/**
	 * 查找该用户名是否存在
	 * @param username
	 * @return
	 */
	public int countLocalInfoByUserName(String username);
	/**
	 * 修改密码
	 * @param username
	 * @return
	 */
	public LocalAuthExecution modifiPassword(String username,String oldPassword,String newPassword);
	/**
	 * 重置密码
	 * @param username
	 * @param email
	 * @param newPassword
	 * @return
	 */
	public LocalAuthExecution resetPassword(String username,String email,String newPassword);
}
