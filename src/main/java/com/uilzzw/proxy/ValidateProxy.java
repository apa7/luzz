package com.uilzzw.proxy;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import com.uilzzw.common.ConstantUtils;

public class ValidateProxy {

	public static boolean validationProxy(String ipAddress, int port, String protocol, String validateSite,
			String assertString) {
		if (StringUtils.isBlank(ipAddress))
			return false;
		if (StringUtils.isBlank(protocol))
			return false;
		if (port <= 0 || port > 65535)
			return false;
		if (StringUtils.isBlank(validateSite))
			validateSite = ConstantUtils.VALIDATION_SITE;
		if (validateSite.contains("https://"))
			validateSite = validateSite.replace("https://", "http://");

		CloseableHttpClient httpClient = HttpClients.createDefault();
		// Add proxy
		HttpHost proxy = new HttpHost(ipAddress, port, protocol);
		RequestConfig proxyConfig = RequestConfig.custom().setProxy(proxy).build();
		HttpGet httpGet = new HttpGet(validateSite);
		httpGet.setConfig(proxyConfig);
		try {
			CloseableHttpResponse response = httpClient.execute(proxy, httpGet);
			String htmlText = EntityUtils.toString(response.getEntity(), "UTF-8");
			Document document = Jsoup.parse(htmlText);
			Elements byTags = document.getElementsByTag("title");
			for (Element element : byTags) {
				// default is baidu.com
				if (element.text().trim().contains(assertString)) {
					ConstantUtils.getLogger().info("Proxy [" + ipAddress + ":" + port + "," + protocol + "] can used");
					return true;
				}
			}
			httpClient.close();
		} catch (Exception e) {
			ConstantUtils.getLogger().error("Proxy can't used", e);
			return false;
		}
		return false;
	}
}
