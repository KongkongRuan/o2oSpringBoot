package com.imooc.o2o.service.impl;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.imooc.o2o.dao.PersonInfoDao;
import com.imooc.o2o.dto.PersonInfoExecution;
import com.imooc.o2o.entity.PersonInfo;
import com.imooc.o2o.enums.PersonInfoStateEnum;
import com.imooc.o2o.service.PersonInfoService;
import com.imooc.o2o.util.PageCalculator;

@Service
public class PersonInfoServiceImpl implements PersonInfoService {
	@Autowired
	private PersonInfoDao personInfoDao;

	@Override
	public PersonInfo getPersonInfoById(Long userId) {
		return personInfoDao.queryPersonInfoById(userId);
	}

	@Override
	public PersonInfoExecution getPersonInfoList(PersonInfo personInfoCondition, int pageIndex, int pageSize) {
		int rowIndex=PageCalculator.calculateRowIndex(pageIndex, pageSize);
		List<PersonInfo> personInfoList = personInfoDao.queryPersonInfoList(personInfoCondition, rowIndex, pageSize);
		int count = personInfoDao.queryPersonInfoCount(personInfoCondition);
		if(personInfoList!=null) {
			return new PersonInfoExecution(PersonInfoStateEnum.SUCCESS, personInfoList,count);
		}else {
			return new PersonInfoExecution(PersonInfoStateEnum.QUERYERROR);
		} 
	}


}
