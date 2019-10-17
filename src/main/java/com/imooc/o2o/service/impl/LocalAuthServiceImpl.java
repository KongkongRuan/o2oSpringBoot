package com.imooc.o2o.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.imooc.o2o.dao.LocalAuthDao;
import com.imooc.o2o.dao.PersonInfoDao;
import com.imooc.o2o.dto.LocalAuthExecution;
import com.imooc.o2o.entity.LocalAuth;
import com.imooc.o2o.entity.PersonInfo;
import com.imooc.o2o.enums.LocalAuthStateEnum;
import com.imooc.o2o.exception.LocalAuthOperationException;
import com.imooc.o2o.service.LocalAuthService;
@Service
public class LocalAuthServiceImpl implements LocalAuthService {

	@Autowired
	LocalAuthDao localAuthDao;
	@Autowired
	PersonInfoDao personInfoDao;
	/**
	 * 注册
	 */
	@Override
	@Transactional
	public LocalAuthExecution register(LocalAuth localAuth)throws LocalAuthOperationException {
		if(localAuth==null||localAuth.getUsername()==null) {
			return new LocalAuthExecution(LocalAuthStateEnum.NULL_AUTH_INFO);
		}
		try {
			localAuth.setCreateTime(new Date());
			//如果本地帐号夹带着用户信息，则正常注册，否则报错
			if(localAuth.getPersonInfo()!=null&&localAuth.getPersonInfo().getUserId()==null) {
				//创建用户
				try {
					localAuth.getPersonInfo().setCreateTime(new Date());
					localAuth.getPersonInfo().setLastEditTime(new Date());
					localAuth.getPersonInfo().setEnableStatus(1);
					PersonInfo personInfo = localAuth.getPersonInfo();
					int effectedNum = personInfoDao
							.insertPersonInfo(personInfo);
					localAuth.setPersonInfo(personInfo);
					if(effectedNum<=0) {
						throw new LocalAuthOperationException("添加用户信息失败");
					}
				}catch(Exception e) {
					throw new LocalAuthOperationException("insertPersonInfo error: "
							+ e.getMessage());
				}
			}
			//创建本地账户
			int effectedNum = localAuthDao.insertLocalAuth(localAuth);
			if(effectedNum<=0) {
				throw new LocalAuthOperationException("账号创建失败");
			}else {
				return new LocalAuthExecution(LocalAuthStateEnum.SUCCESS,localAuth);
			}
		}catch(Exception e) {
			throw new LocalAuthOperationException("insertLocalAuth error: "
					+ e.getMessage());
		}
		
		
	}

	@Override
	public LocalAuth getLocalInfoByUserName(String username) {
		return localAuthDao.queryLocalInfoByUserName(username);
	}

	@Override
	public int countLocalInfoByUserName(String username) {
		
		return localAuthDao.countLocalInfoByUserName(username);
	}
	/**
	 * 修改密码
	 */
	@Override
	public LocalAuthExecution modifiPassword(String username, String oldPassword,String newPassword) {
		if(username==null||oldPassword==null||newPassword==null) {
			return new LocalAuthExecution(LocalAuthStateEnum.NULL_AUTH_INFO);
		}
		try {
			LocalAuth localAuth=localAuthDao.queryLocalInfoByUserName(username);
			if(!localAuth.getpassword().equals(oldPassword)) {
				return new LocalAuthExecution(LocalAuthStateEnum.ERROR_OLD_PWD);
			}
			int effectedNum = localAuthDao.updatePassword(username, newPassword);
			if(effectedNum<=0) {
				throw new LocalAuthOperationException("修改密码失败");
			}else {
				return new LocalAuthExecution(LocalAuthStateEnum.SUCCESS);
			}
		}catch(Exception e) {
			throw new LocalAuthOperationException("modifiPassword error: "
					+ e.getMessage());
		}
		
	}
	/**
	 * 重置密码
	 */
	@Override
	public LocalAuthExecution resetPassword(String username, String email, String newPassword) {
		if(username==null||email==null||newPassword==null) {
			return new LocalAuthExecution(LocalAuthStateEnum.NULL_AUTH_INFO);
		}
		try {
			LocalAuth localAuth = localAuthDao.queryLocalInfoByUserName(username);
			String contrastEmail =localAuth.getPersonInfo().getEmail();
			if(!contrastEmail.equals(email)) {
				return new LocalAuthExecution(LocalAuthStateEnum.ERROR_EMAIL);
			}
			int effectedNum = localAuthDao.updatePassword(username, newPassword);
			if(effectedNum<=0) {
				throw new LocalAuthOperationException("重置密码失败");
			}else {
				return new LocalAuthExecution(LocalAuthStateEnum.SUCCESS);
			}
		}catch(Exception e) {
			throw new LocalAuthOperationException("resetPassword error: "
					+ e.getMessage());
		}
	}

}
