/**
 * 
 */
package com.online.exams.system.webapp.exception;

/**
 * 所有业务异常，该异常会被controller的拦截器统一拦截，并将msg返回给前端，因此，只有业务问题才抛出该异常，并且msg是友好的
 * 
 * @author tanxianwen 2015年1月20日
 */
public class BusinessException extends RuntimeException {

	/**
	 * 默认是1，与返回前端的普通错误一致
	 */
	private int code = 1;

	/**
	 * 
	 */
	private static final long serialVersionUID = 814964852033852606L;

	public BusinessException() {
		super();
	}

	public BusinessException(String msg) {
		super(msg);
	}

	public BusinessException(int code, String msg) {
		super(msg);
		this.code = code;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}
}
