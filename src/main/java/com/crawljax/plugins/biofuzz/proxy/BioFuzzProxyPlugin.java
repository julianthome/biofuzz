package com.crawljax.plugins.biofuzz.proxy;

import java.io.IOException;

import org.owasp.webscarab.httpclient.HTTPClient;
import org.owasp.webscarab.model.Request;
import org.owasp.webscarab.model.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.crawljax.plugins.biofuzz.proxy.buffer.BioFuzzConvPair;

public class BioFuzzProxyPlugin implements HTTPClient {

	private HTTPClient client = null;
	private BioFuzzIceptor addon = null;

	private static final Logger logger = LoggerFactory.getLogger(BioFuzzProxyPlugin.class);
	
	
	public BioFuzzProxyPlugin(HTTPClient in, BioFuzzIceptor addon ) {
		this.client = in;
		this.addon = addon;
	}

	@Override
	public Response fetchResponse(Request request) throws IOException {
		assert(request != null);
		Response response = null;
		if(request != null && request.getURL() != null) {
			response = this.client.fetchResponse(request);
			logger.debug("+++++++++++++++++++++ " + new String(request.toString()));
		}
		
		if(request != null && response != null) {
			this.addon.bufferConversation(new BioFuzzConvPair(request, response));
		}
		
		return response;
	}
}