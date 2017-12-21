/*
 * $Header: ${}
 * $Revision: ${}
 * $Date: 26-Mar-2007
 *
 * Copyright (c) 1999-2006 Bronwen Cassidy.  All rights reserved.
 */
package com.zynap.talentstudio.web;

import java.io.Serializable;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 26-Mar-2007 13:04:39
 */
public class Trio<K, V, T> implements Serializable {

	public Trio() {}
	
    public Trio(K key, V value, T type) {
        this.key = key;
        this.value = value;
    }

	public K getKey() {
		return key;
	}

	public void setKey(K key) {
		this.key = key;
	}

	public V getValue() {
		return value;
	}

	public void setValue(V value) {
		this.value = value;
	}

	public T getType() {
		return type;
	}

	public void setType(T type) {
		this.type = type;
	}

	private K key;
	private V value;
	private T type;
}
