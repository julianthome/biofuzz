package com.crawljax.plugins.biofuzz.proxy;

import java.io.IOException;

import org.owasp.webscarab.httpclient.URLFetcher;
import org.owasp.webscarab.model.Request;
import org.owasp.webscarab.model.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.crawljax.plugins.biofuzz.proxy.buffer.BioFuzzConvBuffer;
import com.crawljax.plugins.biofuzz.proxy.buffer.BioFuzzConvPair;

public class BioFuzzClient extends URLFetcher {	
	
	private static final Logger logger = LoggerFactory.getLogger(BioFuzzClient.class);
	
	public BioFuzzClient(String host, int port) {
		super();
		setHttpProxy(host,port);
		
	}

	@Override
	public Response fetchResponse(Request arg0) throws IOException {
		return super.fetchResponse(arg0);
	}

	
	public boolean replay(BioFuzzConvBuffer buf, int from, int to) {
		logger.debug("replay");
		assert(to >= from);
		
		for(int i = from; i <= to; i++ ) {
			BioFuzzConvPair cpair = buf.get(i);
			Request req = cpair.getRequest();
			
			
			try {
				logger.debug("fetch response for " + req.toString());
				if(fetchResponse(req) == null) {
					return false;
				}
			} catch (IOException e) {
				logger.debug(e.getMessage());
				return false;
			}
			
		}
		
		return true;
		
	}
	
}
