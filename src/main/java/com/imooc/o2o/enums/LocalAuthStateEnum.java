package com.imooc.o2o.enums;

public enum LocalAuthStateEnum {
	LOGINFAIL(-1, "用户名输入有误"), SUCCESS(0, "操作成功"), NULL_AUTH_INFO(-1006,
			"输入信息为空"),ERROR_OLD_PWD(-1007,"输入旧密码错误"),ERROR_EMAIL(-1008,"输入邮箱错误");
	private int state;

	private String stateInfo;
	
	private LocalAuthStateEnum(int state, String stateInfo) {
		this.state = state;
		this.stateInfo = stateInfo;
	}
	public static LocalAuthStateEnum stateOf(int index) {
		for (LocalAuthStateEnum state : values()) {
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
