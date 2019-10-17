package com.imooc.o2o.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.imooc.o2o.entity.PersonInfo;

public interface PersonInfoDao {
	/**
	 * 通过用户Id查询用户
	 * @param userId
	 * @return
	 */
	PersonInfo queryPersonInfoById(long userId);
	/**
	 * 添加用户信息
	 * @param wechatAuth
	 * @return
	 */
	int insertPersonInfo(PersonInfo personInfo);
	/**
	 * 分页查询用户(主要是提供给超级管理员使用)
	 * @param PersonInfoCondition
	 * @param rowIndex
	 * @param pageSize
	 * @return
	 */
	List<PersonInfo>queryPersonInfoList(@Param("personInfoCondition")PersonInfo personInfoCondition,@Param("rowIndex")int rowIndex,
			@Param("pageSize")int pageSize);
	/**
	 * 查询总数
	 * @param personInfoCondition
	 * @return
	 */
	int queryPersonInfoCount(@Param("personInfoCondition")PersonInfo personInfoCondition);
}
