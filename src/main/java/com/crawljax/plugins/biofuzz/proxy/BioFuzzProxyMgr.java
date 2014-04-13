package com.crawljax.plugins.biofuzz.proxy;

import java.util.List;
import java.util.Vector;

import com.crawljax.core.configuration.ProxyConfiguration;
import com.crawljax.plugins.biofuzz.proxy.buffer.BioFuzzConvFilter;

public class BioFuzzProxyMgr {
	
	private static List <BioFuzzProxy> proxies = null;

	private static BioFuzzProxyMgr mgr = null;
	private static int port = 0;
	private static final int defaultPort = 8084;
	private static final String defaultHost = "127.0.0.1";
	private static String host = "";
	
	public class BioFuzzCSPair {
		private BioFuzzProxy proxy = null;
		private BioFuzzClient client = null;
		
		public BioFuzzCSPair(BioFuzzProxy server,BioFuzzClient client) {
			this.proxy = server;
			this.client = client;
		}
		
		public BioFuzzProxy getProxy() {
			return this.proxy;
		}
		
		public BioFuzzClient getClient() {
			return this.client;
		}
		
	}
	
	
	private BioFuzzProxyMgr(String hst, int initPort) {
		proxies = new Vector<BioFuzzProxy>();

		port = initPort;
		host = hst;
	}
	
	
	public static synchronized BioFuzzProxyMgr getInstance(String host, int initPort) {
		
		if(mgr == null) {
			mgr = new BioFuzzProxyMgr(host, initPort);
		} 
		return mgr;
	}
	
	public static synchronized BioFuzzProxyMgr getInstance() {
		
		if(mgr == null) {
			mgr = new BioFuzzProxyMgr(defaultHost, defaultPort);
		} 
		return mgr;
	}
	
	
	public synchronized BioFuzzProxy createAndGetProxy() {
		BioFuzzProxy proxy = new BioFuzzProxy(ProxyConfiguration.manualProxyOn(host, port++));
	
		proxies.add(proxy);
		
		return proxy;
	}
	
	public synchronized BioFuzzClient createAndGetClientForProxy(BioFuzzProxy proxy){
		//URLFetcher uf = new URLFetcher();
		//uf.setHttpProxy(host, proxy.getPort());
		//clients.add(uf);
		//assert(uf != null);
		return new BioFuzzClient(host,proxy.getPort());
	}
	
	public synchronized BioFuzzCSPair createAndGetCSPair(){
		BioFuzzProxy server = createAndGetProxy();
		BioFuzzClient client = createAndGetClientForProxy(server);
		
		BioFuzzCSPair pair = new BioFuzzCSPair(server,client);
		
		return pair;
		
	}
	

	public synchronized void killAllProxies() {
		
		for (BioFuzzProxy proxy : proxies) {
			if(proxy.isRunning()) {
				proxy.stop();
			}
		}
		
		proxies.clear();
	}
}
