package com.uilzzw.beans;

public class ProxyEntity {
	private String ipAddress;
	private int port;
	private String protocol;
	// 0-Can used;1-Can't used;2-Don't Validate;3-Deleted;4-Died;5-Alived;
	private int status;

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

}
