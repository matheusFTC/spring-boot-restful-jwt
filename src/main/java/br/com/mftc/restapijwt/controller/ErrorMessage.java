package br.com.mftc.restapijwt.controller;

import java.io.Serializable;

public class ErrorMessage implements Serializable {

	private static final long serialVersionUID = -2741977249552748326L;

	private Integer errorCode;
	
	private String message;

	public ErrorMessage(Integer errorCode, String message) {
		super();
		this.errorCode = errorCode;
		this.message = message;
	}

	public Integer getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(Integer errorCode) {
		this.errorCode = errorCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}
