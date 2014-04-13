package com.crawljax.plugins.biofuzz.proxy;

import java.io.File;
import java.io.IOException;

import org.owasp.webscarab.model.Preferences;
import org.owasp.webscarab.model.Request;
import org.owasp.webscarab.model.StoreException;
import org.owasp.webscarab.plugin.Framework;
import org.owasp.webscarab.plugin.proxy.Proxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

import com.crawljax.core.configuration.ProxyConfiguration;
import com.crawljax.plugins.biofuzz.proxy.buffer.BioFuzzConvBuffer;
import com.crawljax.plugins.biofuzz.proxy.buffer.BioFuzzConvFilter;


public class BioFuzzProxy {

	
	private static final Logger logger = LoggerFactory.getLogger(BioFuzzProxy.class);
	private ProxyConfiguration config = null;
	private BioFuzzIceptor iceptor = null;
	private boolean running = false;
	private Proxy proxy = null;

	public BioFuzzProxy(ProxyConfiguration config) {
		SLF4JBridgeHandler.removeHandlersForRootLogger();
		SLF4JBridgeHandler.install();
		this.config = config;
		this.iceptor = new BioFuzzIceptor();
		this.running = false;
		
		this.proxy = createProxy(this.config);
		
	}
	
	private Proxy createProxy(ProxyConfiguration config) {
		Framework framework = new Framework();

		/* set listening port before creating the object to avoid warnings */
		Preferences.setPreference("Proxy.listeners",
		        config.getHostname() + ":" + config.getPort());

		Proxy p = new Proxy(framework);
		
		assert(this.iceptor != null);
		p.addPlugin(this.iceptor);

		try {
			framework.setSession("FileSystem", new File("conversationRecords"), "");
		} catch (StoreException e) {
			throw new RuntimeException("Cannot create BioFuzzProxy");
		}
		
		return p;
		
	}
	
	public void start(){
		// create new proxy - this is just to prevent locking (access to buffer)
		//this.proxy = createProxy(this.config);
		logger.debug("Start proxy ...");
		assert(this.proxy.isRunning() == false);
		this.proxy.run();
	}
	
	public void stop() {
		this.proxy.stop();
	}
	
	public boolean isRunning() {
		return this.proxy.isRunning();
	}
	
	public String getHost() {
		return config.getHostname();
	}
	
	public int getPort() {
		return config.getPort();
	}
	
	public void setFilter(BioFuzzConvFilter filter) {

		assert(isRunning() == false);
		if(isRunning() == false) {
			//this.proxy = createProxy(this.config);
			this.iceptor.setFilter(filter);
		}
	}

	
	public void startRec() {
		try {
			this.proxy.flush();
		} catch (StoreException e) {
			throw new RuntimeException(e.getMessage());
		}
		
		if(isRunning() == false) {
			start();
		} 
		
		assert(isRunning() == true);
		
		if(isRunning() == true) {
			this.iceptor.startRec();
		}
	}
	
	public void stopRec() {	
		assert(this.proxy.isRunning() == true);
		if(isRunning() == false) {
			this.iceptor.stopRec();
		}	
	}	
	
	public BioFuzzConvBuffer getRequestBuffer() {
		return this.iceptor.getRequestBuffer();
	}
	
	public BioFuzzConvBuffer getRequestBufferCopy() {
		return this.iceptor.getBufferCopy();
	}

}
