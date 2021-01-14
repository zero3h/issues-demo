package com.demo.exception;

import org.springframework.security.core.AuthenticationException;

public class CustomAuthenticationException extends AuthenticationException implements IException {

	private static final long serialVersionUID = 1L;

	private int errCode = -1;

	public CustomAuthenticationException(String msg) {
		super(msg);
	}

	/**
	 * @param msg
	 * @param errCode
	 */
	public CustomAuthenticationException(String msg, int errCode) {
		super(msg);
		this.errCode = errCode;
	}
	

	/**
	 * Returns the {@link #errCode}.
	 * 
	 * @return the errCode.
	 */
	public int getErrCode() {
		return errCode;
	}

}
