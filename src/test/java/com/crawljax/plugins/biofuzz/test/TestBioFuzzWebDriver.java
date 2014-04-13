package com.crawljax.plugins.biofuzz.test;

import java.io.File;
import java.io.IOException;

import org.junit.Test;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import org.owasp.webscarab.httpclient.HTTPClient;
import org.owasp.webscarab.model.Preferences;
import org.owasp.webscarab.model.Request;
import org.owasp.webscarab.model.Response;
import org.owasp.webscarab.model.StoreException;
import org.owasp.webscarab.plugin.Framework;
import org.owasp.webscarab.plugin.proxy.ManualEdit;
import org.owasp.webscarab.plugin.proxy.Proxy;

import com.codahale.metrics.MetricRegistry;
import com.crawljax.browser.EmbeddedBrowser;
import com.crawljax.browser.WebDriverBrowserBuilder;
import com.crawljax.core.configuration.BrowserConfiguration;
import com.crawljax.core.configuration.CrawljaxConfiguration;
import com.crawljax.core.configuration.CrawljaxConfiguration.CrawljaxConfigurationBuilder;
import com.crawljax.core.configuration.ProxyConfiguration;
import com.crawljax.core.plugin.Plugins;
import com.crawljax.plugins.biofuzz.core.components.BioFuzzAutomation;
import com.crawljax.plugins.biofuzz.input.BioFuzzFieldInput;
import com.crawljax.plugins.biofuzz.input.BioFuzzFieldInputSequence;

import com.crawljax.util.DomUtils;


public class TestBioFuzzWebDriver {
	
	private BioFuzzAutomation bauto = new BioFuzzAutomation();
	private NodeList iBoxes = null;
	private BioFuzzFieldInputSequence iseq = null;
	private EmbeddedBrowser browser = null;
	private BioFuzzFieldInput eventTrigger = null;
	
	//final static Logger logger = LoggerFactory.getLogger(TestBioFuzzWebDriver.class);

	
	@Test
	public void test() {

		Framework framework = new Framework();
		
		Preferences.setPreference("Proxy.listeners",
		        "localhost:80");
		

		
        Proxy proxy = new Proxy(framework);

		ManualEdit me = new ManualEdit();
        proxy.addPlugin(me);
        proxy.run();
		
//		CrawljaxConfigurationBuilder builder = CrawljaxConfiguration.builderFor("localhost");
//		builder.setProxyConfig(ProxyConfiguration.manualProxyOn("127.0.0.1", 8084));
//		
//
//		CrawljaxConfiguration config = builder.build();
//		Plugins plugins = new Plugins(config, new MetricRegistry());
//		WebDriverBrowserBuilder wdev = new WebDriverBrowserBuilder(config, plugins);
//		
//
//		
//		this.browser = wdev.get(); 
//
//		
//		InputSequence iseq = new InputSequence("//FORM[@name='loginForm']","//FORM[@name='loginForm']");
//		
//		Input i0 = new Input(InputType.GO_TO_URL, "http://localhost/webchess9");
//		Input i1 = new Input("//FORM//INPUT[@name='txtNick']",InputType.TEXT_INPUT, "webchess");
//		Input i2 = new Input("//FORM//INPUT[@name='pwdPassword']",InputType.TEXT_INPUT, "webchess");
//		Input i3 = new Input("//FORM//INPUT[@type='submit']",InputType.CLICK);
//		Input i4 = new Input("//FORM[@name='preferences']//INPUT[@name='txtReload']",InputType.TEXT_INPUT, "100");
//		Input i5 = new Input("//FORM[@name='preferences']//INPUT[@value='Update Preferences']",InputType.CLICK);
//		
//		//Input i1 = new Input("//FORM//INPUT[@value='New Account']",InputType.CLICK);
//		//Input i2 = new Input("//BODY//FORM[@action='mainmenu.php' and @method='post' and @name='userdata']//INPUT[@name='btnCancel' and @type='button' and @value='Cancel']",InputType.CLICK);
//		
//		iseq.add(i0);
//		iseq.add(i1);
//		iseq.add(i2);
//		iseq.add(i3);
//		iseq.add(i4);
//		iseq.add(i5);
//		//iseq.add(i2);
//		
//	
//		for(int i = 0; i < iseq.getSize(); i++) {
//			Input in = iseq.get(i);
//			logger.debug("Executing input " + in.toString());
//			boolean ret = this.bauto.executeInput(browser, in);
//			if(!ret) {
//				logger.debug("Didn't work out");
//			}
//		}
//		
//		
		proxy.stop();
		
	}

}
