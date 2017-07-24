package com.meelin.core.exception;

/**
 * 控制层异常信息
 * @author ZhangHaiLiang
 * @date 2015年9月15日
 */
public class ControllerException extends RuntimeException {

	private static final long serialVersionUID = -3577393080786753629L;

	public ControllerException() {
		super();
	}

	public ControllerException(String message, Throwable cause) {
		super(message, cause);
	}

	public ControllerException(String message) {
		super(message);
	}

	public ControllerException(Throwable cause) {
		super(cause);
	}

}
