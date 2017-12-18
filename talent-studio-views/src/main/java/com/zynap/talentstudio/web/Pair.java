/*
 * $Header: ${}
 * $Revision: ${}
 * $Date: 26-Mar-2007
 *
 * Copyright (c) 1999-2006 Bronwen Cassidy.  All rights reserved.
 */
package com.zynap.talentstudio.web;

import com.zynap.talentstudio.organisation.OrganisationUnit;
import com.zynap.talentstudio.organisation.positions.Position;
import com.zynap.talentstudio.organisation.subjects.Subject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 26-Mar-2007 13:04:39
 */
public class Pair<K, V> implements Serializable {

	public Pair() {}
	
    public Pair(K key, V value) {
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

	private K key;
	private V value;
}
