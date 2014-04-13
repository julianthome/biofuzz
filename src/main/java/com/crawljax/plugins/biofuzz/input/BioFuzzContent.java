package com.crawljax.plugins.biofuzz.input;


import java.util.List;
import java.util.Vector;

import flex.messaging.util.URLDecoder;
import flex.messaging.util.URLEncoder;

public class BioFuzzContent {
	
	private List<BioFuzzParamTuple>paramTuples;
	private ContentType type;
	private BioFuzzContentHandler handler = null;
	
	public BioFuzzContent(BioFuzzContentHandler handler, ContentType type) {
		this.paramTuples = new Vector<BioFuzzParamTuple>();
		this.type = type;
		this.handler = handler;
	}
	
	public BioFuzzContent(BioFuzzContentHandler handler, List <BioFuzzParamTuple> paramTuples, ContentType type) {
		this.paramTuples = paramTuples;
		this.type = type;
		
		this.handler = handler;
	}
	
	public BioFuzzContent(List <BioFuzzParamTuple> paramTuples, ContentType type) {
		this.paramTuples = paramTuples;
		this.type = type;
		
		//this.handler = handler;
	}
	
	public BioFuzzContent(ContentType type) {
		this.paramTuples = new Vector<BioFuzzParamTuple>();
		this.type = ContentType.UNDEF;
		//this.handler = handler;
	}
	
	public BioFuzzContent(BioFuzzContentHandler handler, BioFuzzContent c) {
		
		this.paramTuples = new Vector<BioFuzzParamTuple>();

		this.handler = handler;
		this.type = c.type;
		
		for(BioFuzzParamTuple tup : c.paramTuples) {
			this.paramTuples.add(new BioFuzzParamTuple(this,this.paramTuples.size(),tup.getKey(),tup.getValue()));
		}

		
	}
	
	public String toString () {
		
		String s = (type == ContentType.GET ? "?" : "" );
		for(int i = 0; i < paramTuples.size(); i++) {
			s += paramTuples.get(i).toString();
			
			if(i != paramTuples.size()-1) {
				s+= "&";
			}
		}
		return s;
	}
	
	public int getSize() {
		return this.paramTuples.size();
	}
	
	public BioFuzzParamTuple get(int idx) {
		assert(idx >= 0);
		assert(idx < this.paramTuples.size());
		
		if(idx >= 0 && idx < this.paramTuples.size())
			return this.paramTuples.get(idx);
		
		return null;
	}
	
	public ContentType getContentType() {
		return this.type;
	}
	
	public BioFuzzParamTuple registerAndGetParamTuple(String key, String value) {
		
		BioFuzzParamTuple ptup = null;
		if(this.type == ContentType.GET) {
			ptup = new BioFuzzParamTuple(this, this.paramTuples.size(),  key, value);
		} else {
			ptup = new BioFuzzParamTuple(this, this.paramTuples.size(),  key, URLDecoder.decode(value));
		}
		this.paramTuples.add(ptup);
		return ptup;
	}
	
	public BioFuzzContentHandler getHandler() {
		return this.handler;
	}

	public boolean hasHandler() {
		return this.handler != null;
	}
	
	public enum ContentType {
		UNDEF("UNDEF"),
		POST("POST"),
		GET("GET");
		
		private String desc;
		
		ContentType(String desc) {
			this.desc = desc;
		}
		
		@Override
		public String toString() {
			return this.desc;
		}
	};
}
