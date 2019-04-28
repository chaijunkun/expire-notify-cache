package com.github.chaijunkun.cache;

/** 
 * 元素超时回调接口
 * @author chaijunkun
 * @since 2015年1月28日 
 */
public interface TimeoutCallback<K, V> {
	
	/**
	 * 超时时回调的方法
	 * @param pair 超时的K缓存对
	 * @throws Exception 回调时产生的异常
	 */
	public void onTimeout(Pair<K, V> pair) throws Exception;

}
