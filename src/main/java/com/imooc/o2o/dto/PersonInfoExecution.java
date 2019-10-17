package com.imooc.o2o.dto;

import java.util.List;

import com.imooc.o2o.entity.PersonInfo;
import com.imooc.o2o.enums.PersonInfoStateEnum;

public class PersonInfoExecution {
	// 结果状态
	private int state;

	// 状态标识
	private String stateInfo;
	
	private PersonInfo personInfo;
	
	private int count;
	private List<PersonInfo> personInfoList;
	public PersonInfoExecution(){
		
	}
	//失败时调用的构造器
	public PersonInfoExecution(PersonInfoStateEnum stateEnum){
		this.state=stateEnum.getState();
		this.stateInfo=stateEnum.getStateInfo();	
	}
	//成功时调用的构造器
	public PersonInfoExecution(PersonInfoStateEnum stateEnum,PersonInfo personInfo){
		this.state=stateEnum.getState();
		this.stateInfo=stateEnum.getStateInfo();
		this.personInfo=personInfo;
	}
	//成功时调用的构造器
	public PersonInfoExecution(PersonInfoStateEnum stateEnum,List<PersonInfo> personInfoList,int count){
		this.state=stateEnum.getState();
		this.stateInfo=stateEnum.getStateInfo();
		this.personInfoList=personInfoList;
		this.count=count;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public String getStateInfo() {
		return stateInfo;
	}
	public void setStateInfo(String stateInfo) {
		this.stateInfo = stateInfo;
	}
	
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public PersonInfo getPersonInfo() {
		return personInfo;
	}
	public void setPersonInfo(PersonInfo personInfo) {
		this.personInfo = personInfo;
	}
	public List<PersonInfo> getPersonInfoList() {
		return personInfoList;
	}
	public void setPersonInfoList(List<PersonInfo> personInfoList) {
		this.personInfoList = personInfoList;
	}
}
