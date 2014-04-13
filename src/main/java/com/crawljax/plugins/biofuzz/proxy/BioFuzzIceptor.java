package com.crawljax.plugins.biofuzz.proxy;

import org.owasp.webscarab.httpclient.HTTPClient;
import org.owasp.webscarab.model.Request;
import org.owasp.webscarab.plugin.proxy.ProxyPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import com.crawljax.plugins.biofuzz.proxy.buffer.BioFuzzConvBuffer;
import com.crawljax.plugins.biofuzz.proxy.buffer.BioFuzzConvFilter;
import com.crawljax.plugins.biofuzz.proxy.buffer.BioFuzzConvPair;

public class BioFuzzIceptor extends ProxyPlugin {


	private static final Logger logger = LoggerFactory.getLogger(BioFuzzIceptor.class);


	private BioFuzzConvBuffer buffer = null;
	private boolean rec;

	
	public BioFuzzIceptor(){
		this.buffer = new BioFuzzConvBuffer();
		this.rec = false;
		assert(buffer != null);
	}

	public  BioFuzzConvBuffer getRequestBuffer() {
		return this.buffer;
	}

	public  BioFuzzConvBuffer moveRequestBuffer() {
		BioFuzzConvBuffer buf = new BioFuzzConvBuffer(this.buffer);
		this.buffer.clear();
		return buf;
	}

	public  void flushRequestBuffer() {
		logger.debug("Flush Request Buffer ...");
		if (rec == false) {
			this.buffer.clear();
			logger.debug("... done");
		}
		
	}

	public  BioFuzzConvPair getLatestConversation () {
		return this.buffer.getLatest();
	}

	public  void bufferConversation(BioFuzzConvPair pair) {
		logger.debug("++++++++++ add to buf");
		if(this.rec == true)
			this.buffer.add(pair);
	}

	public  void startRec() {
		logger.debug("Start recording ...");
		this.buffer.clear();
		this.rec = true;
	}

	public  void stopRec() {
		this.rec = false;
	}

	public  boolean isRecording() {
		return this.rec;
	}

	public  BioFuzzConvBuffer getBufferCopy() {
		BioFuzzConvBuffer buf = new BioFuzzConvBuffer(this.buffer);
		return buf;
	}

	public  void setFilter(BioFuzzConvFilter filter) {
		logger.debug("set filter for iceptor");
		this.buffer.setFilter(filter);
	}

	@Override
	public String getPluginName() {
		return "BioFuzzProxyAddon";
	}

	@Override
	public HTTPClient getProxyPlugin(HTTPClient in) {
		return new BioFuzzProxyPlugin(in, this);
	}


}
