package com.kyoo.pixel.utils;

import java.lang.ref.SoftReference;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public final class Cache<K, V> {

  private static final int DEFAULT_CACHE_SIZE = 1000;
  private final Queue<K> keys;
  private final ConcurrentHashMap<K, SoftReference<V>> cache;
  private final int cacheSize;

  public Cache() {
    this(DEFAULT_CACHE_SIZE);
  }

  public Cache(int cacheSize) {
    this.cacheSize = cacheSize;
    cache = new ConcurrentHashMap<>();
    keys = new LinkedList<>();
  }

  public V get(K key, Function<K, V> f) {
    if (cache.containsKey(key)) {
      return cache.get(key).get();
    }
    V value = f.apply(key);
    if (cache.size() >= cacheSize) {
      K lastKey = keys.poll();
      cache.remove(lastKey);
    }
    keys.add(key);
    cache.put(key, new SoftReference<>(value));
    return value;
  }
}
