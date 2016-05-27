package com.uilzzw.proxytest;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
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
		List<List<Map<String,String>>> split = CommonUtils.split(list, 125);

		System.out.println(split.size());
		
		
		//ExecutorService threadPool = CommonUtils.createThreadPool("ValidationPool");
		for (int i = 0; i < split.size(); i++) {
			//List<ProxyEntity> list2 = new ValidationTask().getCanUsedProxy(split.get(i),threadPool);
			List<ProxyEntity> list2 = new ValidationTask(CommonUtils.createThreadPool("ValidationPool")).getCanUsedProxy(split.get(i));
			System.out.println(list2);
		}
	}
}
