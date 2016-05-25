package com.uilzzw.proxytest;

import java.util.List;
import java.util.Map;

import com.uilzzw.common.ConstantUtils;
import com.uilzzw.proxy.OutputOrInputProxy;
import com.uilzzw.utils.ProxyConstants;

public class ReadProxyTest {
	public static void main(String[] args) {
		String filePath = ConstantUtils.getUserHome() + "\\" + ProxyConstants.FILE_NAME;
		List<Map<String, String>> list = OutputOrInputProxy.readProxyFromFile(filePath);
		System.out.println(list.size());
	}
}
