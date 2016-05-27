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

public class ValidateProxy implements Runnable{
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

    private boolean validationProxy(String ipAddress, int port, String protocol, String validateSite,
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

    @Override
    public void run() {
	System.out.println(Thread.currentThread().getName()+" is running");
	boolean boo = validationProxy(getIpAddress(), getPort(), getProtocol(), getVilateSite(), getAssertString());
	System.out.println(boo);
    }
}
