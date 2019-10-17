package com.imooc.o2o.exception;

public class WechatAuthOperationException extends RuntimeException{


    /**
	 * 
	 */
	private static final long serialVersionUID = 365495560514635947L;

	public WechatAuthOperationException(String msg) {
        super(msg);
    }
}