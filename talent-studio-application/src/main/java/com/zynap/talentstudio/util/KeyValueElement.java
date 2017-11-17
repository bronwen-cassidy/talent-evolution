package com.zynap.talentstudio.util;

import lombok.Getter;

/**
 *  Represents a key value pair without the overhead of a map
 */

@Getter
public class KeyValueElement<E,T> {
	
	private E key;
	private T value;

	public KeyValueElement(E key, T value) {
		this.key = key;
		this.value = value;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		KeyValueElement<?, ?> that = (KeyValueElement<?, ?>) o;

		if (key != null ? !key.equals(that.key) : that.key != null) return false;
		return value != null ? value.equals(that.value) : that.value == null;
	}

	@Override
	public int hashCode() {
		int result = key != null ? key.hashCode() : 0;
		result = 31 * result + (value != null ? value.hashCode() : 0);
		return result;
	}
}
