package com.uilzzw.proxy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import com.uilzzw.common.ConstantUtils;

public class SpiderProxy {

	/**
	 * 获取西刺代理指定url页面的IP和端口号集合
	 * 
	 * @param url
	 * @param userAgent
	 * @return
	 */
	public static List<HashMap<String, String>> getXiciProxy(String url, String userAgent) {
		if (StringUtils.isBlank(url))
			return null;
		if (StringUtils.isBlank(userAgent))
			return null;
		ConstantUtils.getLogger().info("ProxyWebSite=[" + url + "]");
		ConstantUtils.getLogger().info("User-Agent=[" + userAgent + "]");
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpGet nnGet = new HttpGet(url);
		// 设置头消息
		nnGet.addHeader("User-Agent", userAgent);
		try {
			CloseableHttpResponse response = httpClient.execute(nnGet);
			String htmlText = EntityUtils.toString(response.getEntity());
			Document document = Jsoup.parse(htmlText);
			// Element ipList = document.getElementById("ip_list");
			List<HashMap<String, String>> ipAddrList = new ArrayList<HashMap<String, String>>();
			// 解析table获取IP地址端口和协议
			Elements trs = document.select("table").select("tr");
			for (int i = 0; i < trs.size(); i++) {
				if (i == 0)
					continue;
				Elements tds = trs.get(i).select("td");
				HashMap<String, String> ipAddrm = new HashMap<String, String>();
				for (int j = 0; j < tds.size(); j++) {
					// 1:IP地址;2:PORT;5:PROTOCOL
					if (j == 1 || j == 2 || j == 5) {
						if (j == 5) {
							String protocol = tds.get(j).text();
							ipAddrm.put("protocol", protocol);
						}
						if (j == 1) {
							String ipAddr = tds.get(j).text();
							ipAddrm.put("ipAddr", ipAddr);
						}
						if (j == 2) {
							String port = tds.get(j).text();
							ipAddrm.put("port", port);
						}
					}
				}
				ipAddrList.add(ipAddrm);
			}
			ConstantUtils.getLogger().info("IP-list=" + ipAddrList.toString());
			return ipAddrList;
		} catch (ClientProtocolException e) {
			ConstantUtils.getLogger().error("Get IP LIST ERROR", e);
			// e.printStackTrace();
		} catch (IOException e) {
			ConstantUtils.getLogger().error("Get IP LIST ERROR", e);
			// e.printStackTrace();
		}
		return null;
	}

}
