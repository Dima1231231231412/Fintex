package com.example.springapp.cache;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.WeakHashMap;

@Component
public class WeatherCache<T> implements Cache {
    @Value("${cache.course.size}")
    private int maxSize;

    public final WeakHashMap<Object, LruListNode<T>> lrucache = new WeakHashMap<>(maxSize, 0.75f);
    public final LinkedList<LruListNode<T>> lrulist = new LinkedList<>();

    public final LinkedHashMap<Object, Integer> accessCount = new LinkedHashMap<>(maxSize, 0.75f, true);

    @Override
    public void remove(Object key) {
        lrulist.remove(lrucache.get(key));
        lrucache.remove(key);
    }

    @Override
    public void update(Object key, Object value) {
        if (lrucache.size() >= maxSize) {
            evictLeastRecentlyUsed();
        }
        LruListNode<T> node = new LruListNode<>((T) value);
        lrucache.put(key, node);
        lrulist.addFirst(node);
        accessCount.put(key, 1);
    }

    private void evictLeastRecentlyUsed() {
        Object leastUsedLocation = accessCount.entrySet().stream()
                .min(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);

        if (leastUsedLocation != null) {
            lrucache.remove(leastUsedLocation);
            accessCount.remove(leastUsedLocation);
        }
    }
}
