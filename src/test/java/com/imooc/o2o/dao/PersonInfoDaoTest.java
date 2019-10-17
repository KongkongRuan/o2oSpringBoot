package com.imooc.o2o.dao;

import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.List;

import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.imooc.o2o.entity.PersonInfo;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(SpringRunner.class)
@SpringBootTest
public class PersonInfoDaoTest {
	@Autowired
	private PersonInfoDao personInfoDao;
	@Ignore
	@Test
	public void testAInsertPersonInfo() throws Exception {
		PersonInfo personInfo = new PersonInfo();
		personInfo.setName("我爱你");
		personInfo.setGender("女");
		personInfo.setUserType(1);
		personInfo.setCreateTime(new Date());
		personInfo.setLastEditTime(new Date());
		personInfo.setEnableStatus(1);
		int effectedNum = personInfoDao.insertPersonInfo(personInfo);
		assertEquals(1, effectedNum);
	}
	@Ignore
	@Test
	public void testBQueryPersonInfoList() throws Exception {
		long userId=1;
		PersonInfo person = personInfoDao.queryPersonInfoById(userId);
		System.out.println(person.getName());

	}
	@Test
	public void testCqueryPersonInfoList() throws Exception{
		PersonInfo personInfoCondition = new PersonInfo();
		personInfoCondition.setName("测试");
		List<PersonInfo> personInfoList = personInfoDao.queryPersonInfoList(personInfoCondition, 0, 3);
		int effectedNum = personInfoDao.queryPersonInfoCount(personInfoCondition);
		assertEquals(6, effectedNum);
		System.out.println("--------------"+personInfoList.get(0).getName()+"--------------");
	}
	
}
