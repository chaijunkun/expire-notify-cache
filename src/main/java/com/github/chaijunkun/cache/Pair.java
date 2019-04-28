package com.github.chaijunkun.cache;


/** 
 * 缓存元素,一旦初始化不可修改
 * @author chaijunkun
 * @since 2015年1月27日 
 */
public class Pair<K, V> {

	private final K key;

    private final V value;
    
    public Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }

	public K getKey() {
		return key;
	}

	public V getValue() {
		return value;
	}
    
}
