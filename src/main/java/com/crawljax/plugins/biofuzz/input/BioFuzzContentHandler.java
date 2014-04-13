package com.crawljax.plugins.biofuzz.input;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.owasp.webscarab.model.HttpUrl;
import org.owasp.webscarab.model.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.crawljax.plugins.biofuzz.input.BioFuzzContent.ContentType;
import com.crawljax.plugins.biofuzz.utils.BioFuzzUtils;

import flex.messaging.util.URLEncoder;


public class BioFuzzContentHandler {

	final static Pattern paramPat = Pattern.compile("(\\w*)=(.*)",Pattern.CASE_INSENSITIVE);
	final static Logger logger = LoggerFactory.getLogger(BioFuzzUtils.class);
	
	private BioFuzzContent orig = null;
	private BioFuzzContent safe = null;
	private Set<Integer> changedKeys = null;
	private List<Integer> injectable = null;
	
	public BioFuzzContentHandler(BioFuzzContent content) {
		this.orig = content;
		this.safe = new BioFuzzContent(this,this.orig);
		this.changedKeys = new HashSet<Integer>();
		this.injectable = new Vector<Integer>();
	}

	public BioFuzzContentHandler() {
		this.orig = null;
		this.safe = null;
		this.changedKeys = new HashSet<Integer>();
		this.injectable = new Vector<Integer>();
	}
	
	public BioFuzzContentHandler(BioFuzzContentHandler handler) {
		this.orig = new BioFuzzContent(this,handler.orig);
		this.safe = new BioFuzzContent(this,handler.safe);
		this.changedKeys = new HashSet<Integer>();
		this.injectable = new Vector<Integer>();
		
	}
	
	
	public void addFields(Set<BioFuzzParamTuple> s) {
		for(BioFuzzParamTuple tup : s) {
			addContent(tup.getKey(), tup.getValue());
		}
	}
	
	public BioFuzzParamTuple addContent(String key, String value) {
		this.safe.registerAndGetParamTuple(key, value);
		return this.orig.registerAndGetParamTuple(key, value);
	}
	
    public boolean addContentFromString(String params, ContentType type) {
    	assert(params != null);
    	
		
		String[] tuples = params.split("&");
		
		logger.debug(" tuple list " + tuples[0]);
		
		
		this.orig = new BioFuzzContent(this, type);
		
		for(int i = 0; i < tuples.length; i++) {
			assert(tuples[i] != null);

			Matcher match = paramPat.matcher(tuples[i]);
			
			//BioFuzzParamTuple ptup = new BioFuzzParamTuple(match.replaceAll("$1"), match.replaceAll("$2"));
			String key = match.replaceAll("$1");
			String val =  match.replaceAll("$2");
			BioFuzzParamTuple ptup = this.orig.registerAndGetParamTuple(key, val);
		}
		
		
		this.safe = new BioFuzzContent(this,this.orig);
		return true;
	}
	
    public boolean addContentFromRequest(Request req) {
    	assert(req != null);
    	
    	logger.debug("Request " + req.toString());
    	
    	ContentType type = ContentType.UNDEF;
    	
    	String method = req.getMethod();
    	
    	String params = null;
    	
    	if(method == null) {
    		return false;
    	}
    	
    	if(method.equals("GET")) {
    		type = ContentType.GET;
    		
    		HttpUrl url = req.getURL();
    		
    		if(url != null) {
    			params = url.getParameters();    			
    		}
    		
    		if(params.startsWith("?")) {
    			if(params.length() > 1) {
    				params = params.substring(1, params.length()-1);
    			} else {
    				params = "dummy=0";
    			}
    		}
    		
    	} else if(method.equals("POST")) {
    		type = ContentType.POST;
        	byte[] content = req.getContent();
 
    		if(content != null && content.length >= 0) {
    			params = new String(content);
    			logger.debug("Content to string " + params);
    		}
    		
    	} else {
    		return false;
    	}
    	
    	return this.addContentFromString(params, type);
	}
	
    public BioFuzzParamTuple get(int idx) {
    	assert(this.orig.getSize() > 0 && idx < this.orig.getSize());
    	return this.orig.get(idx);
    }
    
    public void submit(int idx, String value) {
    	logger.debug("submit idx:" + idx + " value:" + value);
    	BioFuzzParamTuple tup = this.orig.get(idx);
    	assert(tup != null);
    	tup.setValue(URLEncoder.encode(value));
    	this.changedKeys.add(idx);
    }
    
    public void rollback() {
    	for(Integer key : this.changedKeys) {
    		this.orig.get(key).setValue(this.safe.get(key).getValue());
    	}
    	this.changedKeys.clear();
    }
    
    public int getSize() {
    	assert(this.orig != null);
    	assert(this.safe != null);
    	assert(this.orig.getSize() == this.safe.getSize());
    	
    	return this.orig.getSize();
    }
    
    public BioFuzzContent getContent() {
    	return this.orig;
    }
    
    public void setInjectable(int idx) {
    	if(!this.injectable.contains(idx))
    		this.injectable.add(idx);
    }
    
    public boolean isInjectable(int idx) {
    	return this.injectable.contains(idx);
    }
    
    
    public Set<BioFuzzParamTuple> getConstParams() {
    	Set <BioFuzzParamTuple> constParams = new HashSet<BioFuzzParamTuple>();
    	
    	for(int i = 0; i < this.orig.getSize(); i++) {
    		if(this.injectable.contains(i))
    			continue;
    		
    		BioFuzzParamTuple tup = this.orig.get(i);
    		constParams.add(new BioFuzzParamTuple(tup.getKey(),tup.getValue()));
    	}
    	
    	return constParams;
    }
    
    public Set<BioFuzzParamTuple> getAllParams() {
    	Set <BioFuzzParamTuple> allParams = new HashSet<BioFuzzParamTuple>();
    	
    	if(this.orig != null) {
	    	for(int i = 0; i < this.orig.getSize(); i++) {
	    		BioFuzzParamTuple tup = this.orig.get(i);
	    		allParams.add(new BioFuzzParamTuple(tup.getKey(),tup.getValue()));
	    	}
    	}
    	
    	return allParams;
  
    }
    
    public String toString() {
    	String s = "Content Handler -----------------------------------BEGIN\n";
    	s+="Original:\n";
    	s+= this.orig.toString()+"\n";
    	s+="Safe:\n";
    	s+= this.safe.toString()+"\n";
    	s+="Set of changes :"  +  this.changedKeys.toString()+"\n";
    	s+="Injectable Params: " + this.injectable.toString()+"\n";
    	s+="Content Handler -----------------------------------END\n";
    	return s;
    }
    
}
