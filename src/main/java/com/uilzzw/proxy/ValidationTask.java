package com.uilzzw.proxy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import com.uilzzw.beans.ProxyEntity;
import com.uilzzw.common.ConstantUtils;

public class ValidationTask {
	private ExecutorService threadPool;
	
	
	public ExecutorService getThreadPool() {
		return threadPool;
	}

	public void setThreadPool(ExecutorService threadPool) {
		this.threadPool = threadPool;
	}

	public ValidationTask() {
		super();
	}

	public ValidationTask(ExecutorService threadPool) {
		super();
		this.threadPool = threadPool;
	}

	/**
	 * 
	 * @param list
	 *            从文件读取到的代理list
	 * @param threadPool
	 *            线程池
	 * @return
	 */
	public List<ProxyEntity> getCanUsedProxy(List<Map<String, String>> list, ExecutorService threadPool) {
		if (list == null || list.size() == 0 || threadPool == null)
			return null;
		List<Future<ProxyEntity>> proxyList = new ArrayList<Future<ProxyEntity>>();
		for (Map<String, String> map : list) {
			String ipAddr = map.get("ipAddr");
			String port = map.get("port");
			String protocol = map.get("protocol");
			ValidateProxy task = new ValidateProxy(ipAddr, Integer.parseInt(port), protocol,
					ConstantUtils.VALIDATION_SITE, ConstantUtils.ASSERT);
			Future<ProxyEntity> future = threadPool.submit(task);
			proxyList.add(future);
		}
		List<ProxyEntity> canUsedList = new ArrayList<ProxyEntity>();
		for (Future<ProxyEntity> future : proxyList) {
			try {
				ProxyEntity entity = future.get();
				if (entity.getStatus() == 1)
					continue;
				canUsedList.add(entity);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		}
		//threadPool.shutdown();
		return canUsedList;
	}
	
	/**
	 * 
	 * @param list
	 *            从文件读取到的代理list
	 * @param threadPool
	 *            线程池
	 * @return
	 */
	public List<ProxyEntity> getCanUsedProxy(List<Map<String, String>> list) {
		if (list == null || list.size() == 0 || threadPool == null)
			return null;
		List<Future<ProxyEntity>> proxyList = new ArrayList<Future<ProxyEntity>>();
		for (Map<String, String> map : list) {
			String ipAddr = map.get("ipAddr");
			String port = map.get("port");
			String protocol = map.get("protocol");
			ValidateProxy task = new ValidateProxy(ipAddr, Integer.parseInt(port), protocol,
					ConstantUtils.VALIDATION_SITE, ConstantUtils.ASSERT);
			Future<ProxyEntity> future = threadPool.submit(task);
			proxyList.add(future);
		}
		List<ProxyEntity> canUsedList = new ArrayList<ProxyEntity>();
		for (Future<ProxyEntity> future : proxyList) {
			try {
				ProxyEntity entity = future.get();
				if (entity.getStatus() == 1)
					continue;
				canUsedList.add(entity);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		}
		//threadPool.shutdown();
		return canUsedList;
	}
}
