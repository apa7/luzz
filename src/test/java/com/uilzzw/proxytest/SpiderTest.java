package com.uilzzw.proxytest;

import java.util.HashMap;
import java.util.List;
import com.uilzzw.common.ConstantUtils;
import com.uilzzw.common.UserAgent;
import com.uilzzw.proxy.OutputOrInputProxy;
import com.uilzzw.proxy.SpiderProxy;
import com.uilzzw.utils.ProxyWebSite;


public class SpiderTest {

	public static void main(String[] args) {
		String userAgent = UserAgent.GOOGLECHROME_WINDOWS;
		for (int i = 1; i <= 10; i++) {
			String url = ProxyWebSite.NT_PROXY + i;
			List<HashMap<String, String>> xiciProxy = SpiderProxy.getXiciProxy(url, userAgent);
			ConstantUtils.getLogger().info(xiciProxy.toString());
			OutputOrInputProxy.writeContentToFile(xiciProxy, null, true, true, true);
		}
	}
}
