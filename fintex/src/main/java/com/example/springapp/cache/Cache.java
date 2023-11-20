package com.example.springapp.cache;

public interface Cache {
    public void remove(Object key);
    public void update(Object key, Object value);
}
