package com.github.chaijunkun.cache;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** 
 * 调度超时专用缓存
 * @author chaijunkun
 * @since 2015年1月28日 
 */
public class ExpireNotifyCache<K, V> {
	
	private static final Logger log = LoggerFactory.getLogger(ExpireNotifyCache.class);

	private ConcurrentMap<K, V> cacheObjMap = new ConcurrentHashMap<K, V>();

	private DelayQueue<DelayItem<Pair<K, V>>> delayQueue = new DelayQueue<DelayItem<Pair<K, V>>>();

	private Thread daemonThread;
	
	/** 缓存过期后的回调接口 */
	private TimeoutCallback<K, V> timeoutCallback;
	
	public TimeoutCallback<K, V> getTimeoutCallback() {
		return timeoutCallback;
	}

	public void setTimeoutCallback(TimeoutCallback<K, V> timeoutCallback) {
		this.timeoutCallback = timeoutCallback;
	}

	public ExpireNotifyCache() {
		Runnable daemonTask = new Runnable() {
			public void run() {
				timeoutCheck();
			}
		};
		daemonThread = new Thread(daemonTask);
		daemonThread.setDaemon(true);
		daemonThread.setName("Schedule_Cache_Daemon");
		daemonThread.start();
	}

	private void timeoutCheck() {
		log.info("schedule cache service started.");
		for (;;) {
			try {
				DelayItem<Pair<K, V>> delayItem = delayQueue.take();
				if (delayItem != null) {
					//当有缓存超时时delayItem不为空,调用回调方法并从map中删除缓存
					Pair<K, V> pair = delayItem.getItem();
					if (timeoutCallback!=null){
						try {
							timeoutCallback.onTimeout(pair);
						} catch (Exception e) {
							throw new RuntimeException(e);
						}
					}
					cacheObjMap.remove(pair.getKey(), pair.getValue()); // compare and remove
				}
			} catch (InterruptedException e) {
				log.error(e.getMessage(), e);
				break;
			}
		}
		log.info("schedule cache service stopped.");
	}

	/**
	 * 添加缓存对象
	 * @param key 缓存key
	 * @param value 缓存value
	 * @param time 过期时间
	 * @param unit 过期时间单位
	 */
	public void put(K key, V value, long time, TimeUnit unit) {
		V oldValue = cacheObjMap.put(key, value);
		if (oldValue != null){
			delayQueue.remove(key);
		}
		long nanoTime = TimeUnit.NANOSECONDS.convert(time, unit);
		delayQueue.put(new DelayItem<Pair<K, V>>(new Pair<K, V>(key, value), nanoTime));
	}

	/**
	 * 从缓存中获取值
	 * @param key 缓存key
	 * @return 对应的缓存value
	 */
	public V get(K key) {
		return cacheObjMap.get(key);
	}

}
