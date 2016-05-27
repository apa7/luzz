package com.uilzzw.proxytest;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.uilzzw.common.ConstantUtils;
import com.uilzzw.proxy.ValidateProxy;

public class IpPortValidRunable implements Runnable{

	private static final Logger LOG = Logger.getLogger(IpPortValidRunable.class);
	private List list;
	private String ipAddr;
	private String port;
	
	public IpPortValidRunable(List list){
        this.list = list;
    }
	
	
	public IpPortValidRunable(String ipAddr, String port) {
		this.ipAddr = ipAddr;
		this.port = port;
	}


	@Override
	public void run() {
		boolean validationProxy = ValidateProxy.validationProxy(ipAddr, Integer.parseInt(port), "http", ConstantUtils.VALIDATION_SITE, ConstantUtils.ASSERT);
		/*do something*/
	}


}
