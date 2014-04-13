package com.crawljax.plugins.biofuzz.input;

import flex.messaging.util.URLEncoder;


public class BioFuzzParamTuple {
	
	private String key;
	private String value;
	private int idx = 0;
	private BioFuzzContentHandler handler = null;
	private BioFuzzContent cont = null;

	
	public BioFuzzParamTuple(String key, String value) {
		this.key = key;
		this.value = value;
		this.handler = null;
		this.cont = null;
		this.idx = -1;
	}
	
	public BioFuzzParamTuple(BioFuzzContent cont, int idx, String key, String value) {
		this.key = key;
		//this.value = URLEncoder.encode(value);
		this.value=value;
		this.cont = cont;
		this.handler = cont.getHandler();
		this.idx = idx;
	}
	
	public BioFuzzParamTuple(BioFuzzContent cont, BioFuzzParamTuple tup) {
		this.key = new String(tup.getKey());
		this.value = new String(tup.getValue());
		this.idx = tup.idx;
		this.cont = cont;
		
		this.handler = cont.getHandler();
	}

	public String toString() {
		return this.key + "=" + this.value;
	}

	public String getKey() {
		return key;
	}
	
	public boolean hasContent() {
		return this.cont != null;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		//this.value = URLEncoder.encode(value);
		this.value = value;
	}
	
	public void submit(String value) {
		assert(this.handler != null);
		this.handler.submit(this.idx, value);
	}
	
	public void setInjectable() {
		this.handler.setInjectable(this.idx);
	}
	
	public boolean isInjectable() {
		return this.handler.isInjectable(this.idx);
	}
	
	public void rollback() {
		this.handler.rollback();
	}
	
	public int getIdx() {
		return this.idx;
	}

	@Override
	public boolean equals(Object obj) {
		return obj != null && obj instanceof BioFuzzParamTuple && 
				((BioFuzzParamTuple)obj).getKey().equals(this.getKey());
	}
	
	@Override
	public int hashCode() {
		return this.getKey().hashCode();
	}
}