package com.example.springapp.cache;

public class Node<T> {
    public final T value;
    public Node<T> nextNode;
    public Node<T> previousNode;

    public Node(T value) {
        this.value = value;
        this.nextNode = null;
        this.previousNode = null;
    }
}
