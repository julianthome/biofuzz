package com.crawljax.plugins.biofuzz.core;

import java.util.List;
import java.util.Vector;

import com.codahale.metrics.MetricRegistry;
import com.crawljax.browser.EmbeddedBrowser;
import com.crawljax.browser.WebDriverBrowserBuilder;
import com.crawljax.core.configuration.CrawljaxConfiguration;
import com.crawljax.core.plugin.Plugins;
import com.crawljax.plugins.biofuzz.input.BioFuzzInputSpecIface;
import com.crawljax.plugins.biofuzz.proxy.BioFuzzProxy;
import com.crawljax.plugins.biofuzz.utils.BioFuzzUtils;
import com.google.common.collect.ImmutableList;

public class BioFuzzBrowserMgr {

	private WebDriverBrowserBuilder builder = null;

	private List<EmbeddedBrowser> browsers = null;
	
	private CrawljaxConfiguration config = null;
	
	public BioFuzzBrowserMgr(CrawljaxConfiguration config) {
		Plugins plugins = new Plugins(config, new MetricRegistry());
		builder = new WebDriverBrowserBuilder(config, plugins);
		browsers = new Vector<EmbeddedBrowser>();
	}
	
	public BioFuzzBrowserMgr(String url, BioFuzzInputSpecIface ispec, BioFuzzProxy proxy) {
		CrawljaxConfiguration config = BioFuzzUtils.getConfigFor(url, ispec, proxy);
		Plugins plugins = new Plugins(config, new MetricRegistry());
		builder = new WebDriverBrowserBuilder(config, plugins);
		browsers = new Vector<EmbeddedBrowser>();
	}
	
	public BioFuzzBrowserMgr(String url, BioFuzzProxy proxy) {
		CrawljaxConfiguration config = BioFuzzUtils.getConfigFor(url,proxy);
		Plugins plugins = new Plugins(config, new MetricRegistry());
		builder = new WebDriverBrowserBuilder(config, plugins);
		browsers = new Vector<EmbeddedBrowser>();
	}
	
	public BioFuzzBrowserMgr(String url, BioFuzzInputSpecIface ispec, String proxyURL, int proxyPort) {
		CrawljaxConfiguration config = BioFuzzUtils.getConfigFor(url, ispec, proxyURL, proxyPort);
		Plugins plugins = new Plugins(config, new MetricRegistry());
		builder = new WebDriverBrowserBuilder(config, plugins);
		browsers = new Vector<EmbeddedBrowser>();
	}
	
	public BioFuzzBrowserMgr(String url, String proxyURL, int proxyPort) {
		CrawljaxConfiguration config = BioFuzzUtils.getConfigFor(url, proxyURL, proxyPort);
		Plugins plugins = new Plugins(config, new MetricRegistry());
		builder = new WebDriverBrowserBuilder(config, plugins);
		browsers = new Vector<EmbeddedBrowser>();
	}
	
	
	public EmbeddedBrowser getBrowser() {
		
		EmbeddedBrowser browser = builder.get();
		
		browsers.add(browser);
		
		return browser;
	}
	
	public CrawljaxConfiguration getConfig() {
		return this.config;
	}
	
	public void killAllBrowsers() {
		for(EmbeddedBrowser b : this.browsers) {
			b.close();
		}
	}
	
	
}
