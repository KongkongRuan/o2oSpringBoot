package com.imooc.o2o.enums;

public enum ProductStateEnum {
	SUCCESS(1,"添加成功"),INNER_ERROR(-1001,"操作失败"),EMPTY(-1002,"传入的商品为空");
	private int state;
	private String stateInfo;
	private  ProductStateEnum(int state,String stateInfo){
		this.state=state;
		this.stateInfo=stateInfo;
	}
	public int getState() {
		return state;
	}
	public String getStateInfo() {
		return stateInfo;
	}
	public static ProductStateEnum stateOf(int index) {
		for(ProductStateEnum state:values()) {
			if(state.getState()==index) {
				return state;
			}
		}
		return null;
	}
}
