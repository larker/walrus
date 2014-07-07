package org.j4.tools;

public class CmsResponce {
	
	/**
	 * 是否成功
	 */
	private boolean success;
	/**
	 * 返回消息
	 */
	private Object msg;
	/**
	 * 其他对象
	 */
	private Object o;
	
	public CmsResponce() {
		super();
	}
	public CmsResponce(boolean success, Object msg) {
		super();
		this.success = success;
		this.msg = msg;
	}
	public CmsResponce(boolean success, Object msg, Object o) {
		super();
		this.success = success;
		this.msg = msg;
		this.o = o;
	}
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public Object getMsg() {
		return msg;
	}
	public void setMsg(Object msg) {
		this.msg = msg;
	}
	public Object getO() {
		return o;
	}
	public void setO(Object o) {
		this.o = o;
	}
}
