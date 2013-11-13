package com.t3.common.models;

import java.io.Serializable;

/**
 * This class contain data, using to exchange between server and client
 * @author Luan Vu
 * @see PackageKind
 */
public class Package implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	/** Kind of package for determine action  */
	private int kind;
	
	/** 
	 * Main data of package,
	 *  using almost action/result package kind 
	 */
	private Object data = null;
	
	/** Extra data of package */
	private Object extras = null;
	
	/** Default constructor */
	public Package(Object data, int kind) {
		this.data = data;
		this.kind = kind;
	}

	/** Constructor with extra data */
	public Package(Object data, Object extras, int kind) {
		this(data, kind);
		this.extras = extras;
	}

	public Object getData() {
		return data;
	}

	public Object getExtras() {
		return extras;
	}

	public int getKind() {
		return kind;
	}

}
