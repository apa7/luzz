package com.uilzzw.proxy;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import com.uilzzw.common.ConstantUtils;

public class OutputOrInputProxy {

	/**
	 * 
	 * 将获取到的ip地址,端口,协议的集合写入到文件
	 * 
	 * @param ipAddrList
	 *            IP集合
	 * @param filePath
	 *            需要保存的路径(最后一层目录带有无\均可)
	 * @param isSaveIpAddr
	 *            是否保存IP地址
	 * @param isSavePort
	 *            是否保存端口
	 * @param isSaveProtocol
	 *            是否保存协议
	 * @return
	 */
	public static boolean writeContentToFile(List<HashMap<String, String>> ipAddrList, String filePath,
			boolean isSaveIpAddr, boolean isSavePort, boolean isSaveProtocol) {
		String direc = filePath;
		ConstantUtils.getLogger().info("Directory Path is: [" + direc + "]");
		if (ipAddrList == null || StringUtils.isBlank(filePath))
			return false;
		if (!isSaveIpAddr && !isSavePort && !isSaveProtocol)
			return false;
		if (StringUtils.isBlank(filePath)) {
			ConstantUtils.getLogger().info("Don't choose the directory;We use the default directory");
			direc = ConstantUtils.getUserHome();
			ConstantUtils.getLogger().info("Directory Path is: [" + direc + "]");
			filePath = ConstantUtils.getUserHome() + "\\" + ConstantUtils.FILE_NAME;
			ConstantUtils.getLogger().info("proxy.txt absolute path is: [" + filePath + "]");
		} else {
			if(filePath.endsWith("\\")){
				filePath+=ConstantUtils.FILE_NAME;
			}else{
				filePath += "\\" + ConstantUtils.FILE_NAME;
			}
			ConstantUtils.getLogger().info("proxy.txt absolute path is: [" + filePath + "]");
		}
		File directory = new File(direc);
		if (!directory.exists()) {
			directory.mkdir();
			ConstantUtils.getLogger().info("Create directory===" + direc + "===successfull;");
		}
		File proxyTxt = new File(filePath);
		if (!proxyTxt.exists()) {
			try {
				boolean createNewFile = proxyTxt.createNewFile();
				if (createNewFile) {
					ConstantUtils.getLogger().info("Create File===" + proxyTxt.toString() + "===successfull;");
					FileWriter fw = new FileWriter(proxyTxt, true);
					contentToFile(ipAddrList, isSaveIpAddr, isSavePort, isSaveProtocol, fw);
					return true;
				} else {
					ConstantUtils.getLogger().error("Create File===" + proxyTxt.toString() + "===failed;");
					return false;
				}
			} catch (IOException e) {
				ConstantUtils.getLogger().error("Create File===" + proxyTxt.toString() + "===failed;", e);
				return false;
			}
		} else if (proxyTxt.exists()) {
			try {
				FileWriter fw = new FileWriter(proxyTxt, true);
				contentToFile(ipAddrList, isSaveIpAddr, isSavePort, isSaveProtocol, fw);
				return true;
			} catch (IOException e) {
				ConstantUtils.getLogger().error("Copy To File===" + proxyTxt.toString() + "===failed;", e);
				return false;
			}
		}
		return false;
	}

	/**
	 * 数据写入文本文档
	 * 
	 * @param ipAddrList
	 * @param isSaveIpAddr
	 * @param isSavePort
	 * @param isSaveProtocol
	 * @param fw
	 * @throws IOException
	 */
	private static void contentToFile(List<HashMap<String, String>> ipAddrList, boolean isSaveIpAddr,
			boolean isSavePort, boolean isSaveProtocol, FileWriter fw) {
		ConstantUtils.getLogger().info("=======Starting Copy To File=======");
		BufferedWriter bw = new BufferedWriter(fw);
		for (int i = 0; i < ipAddrList.size(); i++) {
			HashMap<String, String> ipElem = ipAddrList.get(i);
			StringBuffer ipSB = new StringBuffer();
			if (isSaveIpAddr) {
				String ip = ipElem.get("ipAddr");
				ipSB.append(ip);
			}
			if (isSavePort) {
				String port = ipElem.get("port");
				ipSB.append(":").append(port);
			}
			if (isSaveProtocol) {
				String protocol = ipElem.get("protocol");
				ipSB.append(",").append(protocol);
			}
			ConstantUtils.getLogger().info("IP Address Info:" + ipSB.toString());
			StringBuffer buffer = ipSB.append("\r\n");
			String ipElemToString = buffer.toString();
			try {
				bw.append(ipElemToString);
			} catch (IOException e) {
				ConstantUtils.getLogger().error("Failed to append string" + ipElemToString, e);
				e.printStackTrace();
			}
		}
		ConstantUtils.getLogger().info("=========End Copy=========");
		try {
			bw.close();
		} catch (IOException e) {
			ConstantUtils.getLogger().error("Failed closd BufferedWriter", e);
			e.printStackTrace();
		}
	}

	/**
	 * 从指定的proxy文件读取内容
	 * 
	 * @param filePath
	 *            The file named proxy.txt's absolute path
	 * @return IP-Address,Port,Protocol
	 */
	public static List<Map<String, String>> readProxyFromFile(String filePath) {
		if (StringUtils.isBlank(filePath))
			return null;
		ConstantUtils.getLogger().info("The file path is::[" + filePath + "]");
		File proxy = new File(filePath);
		BufferedReader br = null;
		List<Map<String, String>> ipList = new ArrayList<Map<String, String>>();
		if (proxy.exists()) {
			try {
				br = new BufferedReader(new FileReader(proxy));
				String tempString = null;
				while ((tempString = br.readLine()) != null) {
					Map<String, String> ipMap = new HashMap<String, String>();
					if (StringUtils.isBlank(tempString))
						continue;
					String[] record = null;
					boolean boocomma = tempString.contains(",");
					boolean boocolon = tempString.contains(":");
					if (boocomma && boocolon) {
						record = tempString.split(",");
						if (record != null && record.length > 0) {
							if (record[0].contains(":")) {
								ipMap.put("ipAddr", record[0].split(":")[0]);
								ipMap.put("port", record[0].split(":")[1]);
							}
							ipMap.put("protocol", record[1]);
						}
					}
					if (!boocomma && boocolon) {
						record = tempString.split(":");
						if (record != null && record.length > 0) {
							ipMap.put("ipAddr", record[0]);
							ipMap.put("port", record[1]);
						}
					}
					ipList.add(ipMap);
				}
				ConstantUtils.getLogger().info("Read from file success;The IP-List==" + ipList.toString());
				br.close();
				return ipList;
			} catch (FileNotFoundException e) {
				ConstantUtils.getLogger().error("File is not found;Please check it!");
				e.printStackTrace();
			} catch (IOException e) {
				ConstantUtils.getLogger().error("Close BufferedReader Failure!", e);
				e.printStackTrace();
			} finally {
				if (br != null) {
					try {
						br.close();
					} catch (IOException e) {
						ConstantUtils.getLogger().error("Close BufferedReader Failure", e);
						e.printStackTrace();
					}
				}
			}
		}
		return ipList;
	}
}
