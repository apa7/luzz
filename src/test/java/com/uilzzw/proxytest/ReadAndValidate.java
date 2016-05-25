package com.uilzzw.proxytest;

import java.util.List;
import java.util.Map;

import com.uilzzw.common.ConstantUtils;
import com.uilzzw.proxy.OutputOrInputProxy;
import com.uilzzw.proxy.ValidateProxy;

public class ReadAndValidate {
	public static void main(String[] args) {
		String filePath = ConstantUtils.getUserHome() + "\\" + ConstantUtils.FILE_NAME;
		List<Map<String, String>> list = OutputOrInputProxy.readProxyFromFile(filePath);
		for (Map<String, String> map : list) {
			String ipAddr = map.get("ipAddr");
			String port = map.get("port");
			String protocol = map.get("protocol");
			boolean validationProxy = ValidateProxy.validationProxy(ipAddr, Integer.parseInt(port), protocol, ConstantUtils.VALIDATION_SITE, ConstantUtils.ASSERT);
			System.out.println(validationProxy);
		}
	}
}
