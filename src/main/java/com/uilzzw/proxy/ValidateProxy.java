package com.uilzzw.proxy;

import java.util.concurrent.Callable;

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

import com.uilzzw.beans.ProxyEntity;
import com.uilzzw.common.ConstantUtils;

public class ValidateProxy implements Callable<ProxyEntity> {
	private String ipAddress;
	private int port;
	private String protocol;
	private String vilateSite;
	private String assertString;

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

	public String getVilateSite() {
		return vilateSite;
	}

	public void setVilateSite(String vilateSite) {
		this.vilateSite = vilateSite;
	}

	public String getAssertString() {
		return assertString;
	}

	public void setAssertString(String assertString) {
		this.assertString = assertString;
	}

	public ValidateProxy() {
		super();
	}

	public ValidateProxy(String ipAddress, int port, String protocol, String vilateSite, String assertString) {
		super();
		this.ipAddress = ipAddress;
		this.port = port;
		this.protocol = protocol;
		this.vilateSite = vilateSite;
		this.assertString = assertString;
	}

	/**
	 * Validate Proxy can be used or not;
	 * 
	 * @param ipAddress
	 *            IP地址
	 * @param port
	 *            端口
	 * @param protocol
	 *            协议
	 * @param validateSite
	 *            验证网址
	 * @param assertString
	 *            断言关键字
	 * @return true or false
	 */
	private boolean validationProxy(String ipAddress, int port, String protocol, String validateSite,
			String assertString) {
		if (StringUtils.isBlank(ipAddress) || StringUtils.isBlank(protocol) || port <= 0 || port > 65535)
			return false;

		validateSite = StringUtils.isBlank(validateSite) ? ConstantUtils.VALIDATION_SITE : validateSite;
		validateSite = validateSite.contains("https://") ? validateSite.replace("https://", "http://") : validateSite;

		CloseableHttpClient httpClient = HttpClients.createDefault();
		// Add proxy
		HttpHost proxy = new HttpHost(ipAddress, port, protocol);
		// set Timeout
		RequestConfig proxyConfig = RequestConfig.custom().setProxy(proxy).setSocketTimeout(3000)
				.setConnectionRequestTimeout(3000).setConnectTimeout(3000).build();
		HttpGet httpGet = new HttpGet(validateSite);
		httpGet.setConfig(proxyConfig);
		ConstantUtils.getLogger().info("Validating proxy now,Please wait a moment.");
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

	/**
	 * like Interface Runnable run();
	 * 
	 * return ProxyEntity;
	 */
	@Override
	public ProxyEntity call() throws Exception {
		ConstantUtils.getLogger().info(Thread.currentThread().getName() + " is running;");
		boolean boo = validationProxy(getIpAddress(), getPort(), getProtocol(), getVilateSite(), getAssertString());
		// 0-Can used;1-Can't used;2-Don't Validate;3-Deleted;4-Died;5-Alived;
		return boo ? new ProxyEntity(getIpAddress(), getPort(), protocol, 0) : new ProxyEntity(getIpAddress(),
				getPort(), getProtocol(), 1);
	}
}
