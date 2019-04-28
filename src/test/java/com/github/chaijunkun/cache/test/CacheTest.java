package com.github.chaijunkun.cache.test;

import java.util.concurrent.TimeUnit;

import org.junit.Test;

import com.github.chaijunkun.cache.ExpireNotifyCache;
import com.github.chaijunkun.cache.test.callback.CustomTimeoutCallback;

/**
 * 缓存单元测试
 * @author chaijunkun
 * @since 2017年1月6日
 */
public class CacheTest {
	
	@Test
	public void doTest() throws InterruptedException{
		ExpireNotifyCache<String, String> cache = new ExpireNotifyCache<String, String>();
		cache.setTimeoutCallback(new CustomTimeoutCallback());
		cache.put("cache1", "value1", 2, TimeUnit.SECONDS);
		Thread.sleep(100000L);
	}

}
