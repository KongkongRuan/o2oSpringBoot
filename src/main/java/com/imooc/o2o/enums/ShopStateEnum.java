package com.imooc.o2o.enums;

public enum ShopStateEnum {
	//第一行默认调用了构造器
	CHECK(0,"审核中"),OFFLINK(-1,"非法店铺"),SUCCESS(1,"操作成功"),
	PASS(2,"通过认证"),INNER_ERROR(-1001,"系统内部错误"),NULL_SHOPID(-1002,"ShopId为空"),NULL_SHOP(-1003,"Shop信息为空");
	private int state;
	private String stateInfo;
	//构造器需定义成私有的，这样就不能在别处申明此类的对象了
	private ShopStateEnum(int state,String stateInfo) {
		this.state = state;
		this.stateInfo=stateInfo;
	}
	/**
	 * 依据传入的state返回相应的enum值
	 */
	public static ShopStateEnum stateOf(int state) {
		for(ShopStateEnum stateEnum:values()) {
			if(stateEnum.getState()==state) {
				return stateEnum;
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
