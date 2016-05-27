package com.uilzzw.proxytest;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;

import com.uilzzw.common.ConstantUtils;
import com.uilzzw.proxy.OutputOrInputProxy;
import com.uilzzw.proxy.ValidateProxy;
import com.uilzzw.utils.CommonUtils;

public class ReadAndValidate {
    public static void main(String[] args) {
	//new a threadPool
	ScheduledExecutorService threadPool = CommonUtils.createThreadPool("ValidationPool", 100);
	String filePath = ConstantUtils.getUserHome() + "\\" + ConstantUtils.FILE_NAME;
	List<Map<String, String>> list = OutputOrInputProxy.readProxyFromFile(filePath);
	for (Map<String, String> map : list) {
	    String ipAddr = map.get("ipAddr");
	    String port = map.get("port");
	    String protocol = map.get("protocol");
	    ValidateProxy vProxy=new ValidateProxy(ipAddr, Integer.parseInt(port), protocol, ConstantUtils.VALIDATION_SITE, ConstantUtils.ASSERT);
	    threadPool.execute(vProxy);
	}
	threadPool.shutdown();
    }


}
