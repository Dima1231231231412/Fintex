package com.example.springapp.cache;

import org.springframework.beans.factory.annotation.Value;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class Cache<T> implements CacheDao<T> {
    @Value("${cache.course.size}")
    private int size;
    public final Map<Object, Node<T>> keysMap;
    public Node<T> linkedList;

    public Cache() {
        keysMap = new HashMap<>(size);
        linkedList = null;
    }

    public void moveNodeToHead(Node<T> node) {
        if (node == linkedList) {
            return;
        }
        if (node.nextNode != null) {
            node.nextNode.previousNode = node.previousNode;
        }
        if (node.previousNode != null) {
            node.previousNode.nextNode = node.nextNode;
        }
        node.previousNode = null;
        node.nextNode = linkedList;
        if (linkedList != null) {
            linkedList.previousNode = node;
        }
        linkedList = node;
    }

    public void addNodeToHead(Node<T> node) {
        if (linkedList == null) {
            linkedList = node;
        } else {
            node.previousNode = linkedList;
            linkedList = node;
            linkedList.nextNode = node;
        }
    }

    public void removeTailNode() {
        // Поиск начального узла двусвязанного списка
        while (linkedList.previousNode != null) {
            linkedList = linkedList.previousNode;
        }
        Node<T> firstPreviousNode = linkedList;
        //Получение ключа для мапы начального узла
        String firstPreviousLocation = (String) getKey(keysMap,firstPreviousNode);
        keysMap.remove(firstPreviousLocation);
        linkedList = null;
    }

    @Override
    public T get(Object key, Supplier<T> supplier) {
        if (keysMap.containsKey(key)) {
            Node<T> node = keysMap.get(key);
            moveNodeToHead(node);
            return node.value;
        } else {
            T value = supplier.get();
            Node<T> newNode = new Node<>(value);
            keysMap.put(key, newNode);
            addNodeToHead(newNode);
            if (keysMap.size() > size) {
                removeTailNode();
            }
            return value;
        }
    }

    public static <K, V> K getKey(Map<K, V> map, V value)
    {
        for (Map.Entry<K, V> entry: map.entrySet())
        {
            if (value.equals(entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }
}
