package com.uilzzw.proxytest;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.uilzzw.common.ConstantUtils;
import com.uilzzw.proxy.OutputOrInputProxy;
import com.uilzzw.utils.CommonUtil;

public class ValidateIpAndPortThreadTest {
   
	//ConfigFileLoad   从配置文件取值
	static int POOL_SIZE  =  2;//DataType.getAsInt(ConfigFileLoad.getConfContent("miaosha.properties", "POOL_SIZE"));
	static int MAX_THREADS  = 2;// DataType.getAsInt(ConfigFileLoad.getConfContent("miaosha.properties", "MAX_THREADS"));

    static int threadPoolNum = getThreadPoolNum();
    static Future<?> future = null;
    
    private static int getThreadPoolNum(){
        int preThreadPoolNum = Runtime.getRuntime().availableProcessors() * POOL_SIZE;
        int ThreadPoolNum = preThreadPoolNum > MAX_THREADS ? MAX_THREADS : preThreadPoolNum;
        return ThreadPoolNum;
     }	
    private static ExecutorService EXECUTORSERVICE = null;
    public ExecutorService getExecutorService(){
	    return EXECUTORSERVICE;
	}
   
    private synchronized static  ExecutorService initExecutor(){
   	 if(EXECUTORSERVICE==null){
   		//int threadPoolNum = threadPoolNum;
   		EXECUTORSERVICE = Executors.newFixedThreadPool(100);
   	 }
   	return EXECUTORSERVICE;
    }
	public static void main(String[] args) {
		String filePath = ConstantUtils.getUserHome() + "\\" + ConstantUtils.FILE_NAME;
		List<Map<String, String>> list = OutputOrInputProxy.readProxyFromFile(filePath);
		List<List<Map<String,String>>> split = CommonUtil.split(list, 100);
		for (List<Map<String, String>> list2 : split) {
			doValid(list2);
		}
		EXECUTORSERVICE.shutdown();
	}
	private static void doValid( List<Map<String, String>> list2) {
		for (Object obj : list2) {
			initExecutor();
			Map map = (Map) obj;
			String ipAddr = (String) map.get("ipAddr");
			String port = (String) map.get("port");
			IpPortValidRunable ir = new IpPortValidRunable(ipAddr,port);
			EXECUTORSERVICE.execute(ir);
		}
	}
}
