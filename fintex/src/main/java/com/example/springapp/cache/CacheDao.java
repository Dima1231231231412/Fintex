package com.example.springapp.cache;

import java.util.function.Supplier;

public interface CacheDao<T>{
    T get(Object key, Supplier<T> supplier);
}
