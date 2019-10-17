package com.imooc.o2o.service;


import com.imooc.o2o.dto.PersonInfoExecution;
import com.imooc.o2o.entity.PersonInfo;


public interface PersonInfoService {

	/**
	 * 
	 * @param userId
	 * @return
	 */
	PersonInfo getPersonInfoById(Long userId);
	
	/**
	 * 分页获取用户信息
	 * @param personInfoCondition
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	public PersonInfoExecution getPersonInfoList(PersonInfo personInfoCondition,int pageIndex,int pageSize);
}
