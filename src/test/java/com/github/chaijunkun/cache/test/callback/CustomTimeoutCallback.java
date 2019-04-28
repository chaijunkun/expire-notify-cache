package com.github.chaijunkun.cache.test.callback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.chaijunkun.cache.Pair;
import com.github.chaijunkun.cache.TimeoutCallback;

/** 
 * 自定义超时回调
 * @author chaijunkun
 * @since 2015年1月28日 
 */
public class CustomTimeoutCallback implements TimeoutCallback<String, String> {

	private static Logger log = LoggerFactory.getLogger(CustomTimeoutCallback.class);
	
	@Override
	public void onTimeout(Pair<String, String> pair) throws Exception {
		log.warn("cache expired, key:{}, value:{}", pair.getKey(), pair.getValue());
	}

}
