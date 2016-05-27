package com.uilzzw.beans;

import java.util.Date;

public class ProxyEntity {
	private String ipAddress;
	private int port;
	private String protocol;
	// 0-Can used;1-Can't used;2-Don't Validate;3-Deleted;4-Died;5-Alived;
	private int status;
	private Date createTime;
	private Date validateTime;
	private Date updateTime;
	
	

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getValidateTime() {
		return validateTime;
	}

	public void setValidateTime(Date validateTime) {
		this.validateTime = validateTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public ProxyEntity() {
		super();
	}

	public ProxyEntity(String ipAddress, int port, String protocol, int status) {
		super();
		this.ipAddress = ipAddress;
		this.port = port;
		this.protocol = protocol;
		this.status = status;
	}
	
	public ProxyEntity(String ipAddress, int port, String protocol, int status, Date validateTime) {
		super();
		this.ipAddress = ipAddress;
		this.port = port;
		this.protocol = protocol;
		this.status = status;
		this.validateTime = validateTime;
	}

	public ProxyEntity(String ipAddress, int port, String protocol, int status, Date createTime, Date validateTime,
			Date updateTime) {
		super();
		this.ipAddress = ipAddress;
		this.port = port;
		this.protocol = protocol;
		this.status = status;
		this.createTime = createTime;
		this.validateTime = validateTime;
		this.updateTime = updateTime;
	}

}
