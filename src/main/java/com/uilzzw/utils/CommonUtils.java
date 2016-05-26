package com.uilzzw.utils;

import java.util.ArrayList;
import java.util.List;

public class CommonUtils {

	/**
	 * 将一个list集合拆分为指定个数的集合
	 * 
	 * @param targetList
	 *            目标集合
	 * @param count
	 *            要拆分的个数
	 * @return 结果集
	 */
	public static <T> List<List<T>> split(List<T> targetList, int count) {
		if (targetList == null || count < 1)
			return null;
		List<List<T>> result = new ArrayList<List<T>>();
		int size = targetList.size();
		// 数据量不足count指定的大小
		if (size <= count) {
			result.add(targetList);
		} else {
			int front = size / count;
			int last = size % count;
			// 前面front个集合，每个大小都是count个元素
			for (int i = 0; i < front; i++) {
				List<T> itemList = new ArrayList<T>();
				for (int j = 0; j < count; j++) {
					itemList.add(targetList.get(i * count + j));
				}
				result.add(itemList);
			}
			// last的进行处理
			if (last > 0) {
				List<T> itemList = new ArrayList<T>();
				for (int i = 0; i < last; i++) {
					itemList.add(targetList.get(front * count + i));
				}
				result.add(itemList);
			}
		}
		return result;
	}
}
