package com.uilzzw.proxytest;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import com.uilzzw.beans.ProxyEntity;
import com.uilzzw.common.ConstantUtils;
import com.uilzzw.proxy.OutputOrInputProxy;
import com.uilzzw.proxy.ValidationTask;
import com.uilzzw.utils.CommonUtils;

public class ReadAndValidate {
	public static void main(String[] args) {
		String filePath = ConstantUtils.getUserHome() + "\\" + ConstantUtils.FILE_NAME;
		List<Map<String, String>> list = OutputOrInputProxy.readProxyFromFile(filePath);
		// new a threadPool
		ScheduledExecutorService threadPool = CommonUtils.createThreadPool("ValidationPool", 100);
		List<ProxyEntity> proxy = new ValidationTask().getCanUsedProxy(list, threadPool);
		System.out.println(proxy);
	}
}
