package com.imooc.o2o.enums;

public enum PersonInfoStateEnum {
	QUERYERROR(-1, "查询信息输入有误"), SUCCESS(0, "操作成功");
	private int state;

	private String stateInfo;
	private PersonInfoStateEnum(int state,String stateInfo) {
		this.state=state;
		this.stateInfo=stateInfo;
	}
	public static PersonInfoStateEnum stateOf(int index) {
		for (PersonInfoStateEnum state : values()) {
			if (state.getState() == index) {
				return state;
			}
		}
		return null;
	}
	public int getState() {
		return state;
	}
	public String getStateInfo() {
		return stateInfo;
	}
}
