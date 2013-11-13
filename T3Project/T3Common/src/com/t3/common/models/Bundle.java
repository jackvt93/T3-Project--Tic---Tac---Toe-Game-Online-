package com.t3.common.models;

import java.io.Serializable;
import java.util.HashMap;

/**
 * This class using for put and get a bundle data(scalar), for transfer between client/server
 * @author Luan Vu
 *
 */
public class Bundle implements Serializable {
	private static final long serialVersionUID = 1L;

	private HashMap<String, Object> bundle;

	public Bundle() {
		bundle = new HashMap<String, Object>();
	}

	public void putBoolean(String key, boolean value) {
		bundle.put(key, value);
	}

	public void putBooleanArray(String key, boolean[] value) {
		bundle.put(key, value);
	}

	public void putByte(String key, byte value) {
		bundle.put(key, value);
	}

	public void putByteArray(String key, byte[] value) {
		bundle.put(key, value);
	}

	public void putChar(String key, char value) {
		bundle.put(key, value);
	}

	public void putCharArray(String key, char[] value) {
		bundle.put(key, value);
	}
	
	public void putDouble(String key, double value) {
		bundle.put(key, value);
	}
	
	public void putDoubleArray(String key, double[] value) {
		bundle.put(key, value);
	}
	
	public void putFloat(String key, float value) {
		bundle.put(key, value);
	}
	
	public void putFloatArray(String key, float[] value) {
		bundle.put(key, value);
	}
	
	public void putInt(String key, int value) {
		bundle.put(key, value);
	}
	
	public void putIntArray(String key, int[] value) {
		bundle.put(key, value);
	}
	
	public void putLong(String key, long value) {
		bundle.put(key, value);
		
	}
	
	public void putLongArray(String key, long[] value) {
		bundle.put(key, value);
	}
	
	public void putShort(String key, short value) {
		bundle.put(key, value);
	}
	
	public void putShortArray(String key, short[] value) {
		bundle.put(key, value);
	}
	
	public void putString(String key, String value) {
		bundle.put(key, value);
	}
	public void putStringArray(String key, String[] value) {
		bundle.put(key, value);
	}
	
	public boolean getBoolean(String key) {
		return (boolean) bundle.get(key);
	}

	public boolean[] getBooleanArray(String key) {
		return (boolean[]) bundle.get(key);
	}

	public byte getByte(String key) {
		return (byte) bundle.get(key);
	}

	public byte[] getByteArray(String key) {
		return (byte[]) bundle.get(key);
	}

	public char getChar(String key) {
		return (char) bundle.get(key);
	}

	public char[] getCharArray(String key) {
		return (char[]) bundle.get(key);
	}
	
	public double getDouble(String key) {
		return (double) bundle.get(key);
	}
	
	public double[] getDoubleArray(String key) {
		return (double[]) bundle.get(key);
	}
	
	public float getFloat(String key) {
		return (float) bundle.get(key);
	}
	
	public float[] getFloatArray(String key) {
		return (float[]) bundle.get(key);
	}
	
	public int getInt(String key) {
		return (int) bundle.get(key);
	}
	
	public int[] getIntArray(String key) {
		return (int[]) bundle.get(key);
	}
	
	public long getLong(String key) {
		return (long) bundle.get(key);
	}
	
	public long[] getLongArray(String key) {
		return (long[]) bundle.get(key);
	}
	
	public short getShort(String key) {
		return (short) bundle.get(key);
	}
	
	public short[] getShortArray(String key) {
		return (short[]) bundle.get(key);
	}
	
	public String getString(String key) {
		return (String) bundle.get(key);
	}
	public String[] getStringArray(String key) {
		return (String[])bundle.get(key);
	}
}
