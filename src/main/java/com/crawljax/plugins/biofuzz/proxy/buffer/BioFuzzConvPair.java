package com.crawljax.plugins.biofuzz.proxy.buffer;

import org.owasp.webscarab.model.Request;
import org.owasp.webscarab.model.Response;

public class BioFuzzConvPair {
	private Request req;
	private Response res;
	
	public BioFuzzConvPair (Request req, Response res) {
		this.req = req;
		this.res = res;
	}
	
	public BioFuzzConvPair (BioFuzzConvPair cpair) {
		
		if(cpair.getRequest() != null) {
			this.req = new Request(cpair.getRequest());
		} else {
			this.req = null;
		}
		
		if(cpair.getResponse() != null) {
			this.res = new Response(cpair.getResponse());
		} else {
			this.res = null;
		}
		
	}
	
	
	public Response getResponse() {
		return this.res;
	}
	
	public Request getRequest() {
		return this.req;
	}
	
	public String toString() {
		String s = "";
		
		s += "Conversation Pair ---------------------------------BEGIN\n";
		if(this.req != null) {
			s += "Request: ...............\n" + this.req.toString() + "\n";
			s += "...................................................\n";
		}
//		if(this.res != null) {
//			s += "Response: ...............\n" + this.res.toString() + "\n";
//			s += "...................................................\n";
//			s += "Conversation Pair ---------------------------------END\n";
//		}
		return s;
	}
	
}