package com.imooc.o2o.dao;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


import com.imooc.o2o.entity.LocalAuth;
import com.imooc.o2o.entity.PersonInfo;


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(SpringRunner.class)
@SpringBootTest
public class LocalAuthDaoTest  {
	@Autowired
	LocalAuthDao localAuthDao;
	
	
	@Test
	public void TestAInsertLocalAuth() {
		LocalAuth localAuth = new LocalAuth();
		PersonInfo personInfo = new PersonInfo();
		personInfo.setUserId(1L);
		localAuth.setPersonInfo(personInfo);
		localAuth.setUsername("test3");
		localAuth.setpassword("123456");
		localAuth.setCreateTime(new Date());
		int effectedNum = localAuthDao.insertLocalAuth(localAuth);
		assertEquals(effectedNum, 1);
	}
	@Test
	public void TestBqueryLocalInfoByUserName() {
		
		LocalAuth localAuth1 = localAuthDao.queryLocalInfoByUserName("test3");
		assertEquals(localAuth1.getpassword(), "123456");
	}
	@Test
	public void TestCcountLocalInfoByUserName() {
		
		int effectedNum = localAuthDao.countLocalInfoByUserName("test3");
		assertEquals(effectedNum,1);
	}
	@Test
	public void TestDupdatePassword() {
		int effectedNum = localAuthDao.updatePassword("test3", "1234567");
		assertEquals(effectedNum,1);
	}
}
