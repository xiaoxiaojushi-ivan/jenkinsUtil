package com.ndpmedia.comm.vo;

public class BaseResultVO {
	// 返回码，0表示成功，非0表示失败
	private int flag;

	// 返回消息，成功为“success”，失败为具体失败信
	private String msg = "success";

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}


	

}
