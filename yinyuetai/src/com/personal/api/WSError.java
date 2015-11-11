package com.personal.api;

public class WSError extends Throwable {

	private static final long serialVersionUID = 1L;

	private String message;

	private String msgCode;

	public WSError(String message) {
		this.message = message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setMessage(String message, String msgCode) {
		this.message = message;
		this.msgCode = msgCode;
	}

	public String getMessage() {
		return message;
	}

	public String getMsgCode() {
		return msgCode;
	}

	public void setMsgCode(String msgCode) {
		this.msgCode = msgCode;
	}

	public WSError() {
	}
}
