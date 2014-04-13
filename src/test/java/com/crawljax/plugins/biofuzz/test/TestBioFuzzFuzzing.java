package com.crawljax.plugins.biofuzz.test;

import java.io.IOException;
import java.util.List;
import java.util.Vector;

import org.junit.Test;

import org.owasp.webscarab.httpclient.HTTPClient;
import org.owasp.webscarab.model.Request;
import org.owasp.webscarab.model.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import com.crawljax.browser.EmbeddedBrowser;

import com.crawljax.plugins.biofuzz.core.BioFuzzBrowserMgr;
import com.crawljax.plugins.biofuzz.core.components.BioFuzzAutomation;
import com.crawljax.plugins.biofuzz.input.BioFuzzContent;
import com.crawljax.plugins.biofuzz.input.BioFuzzFieldInput;
import com.crawljax.plugins.biofuzz.input.BioFuzzParamTuple;
import com.crawljax.plugins.biofuzz.input.BioFuzzFieldInput.BioFuzzAction;
import com.crawljax.plugins.biofuzz.input.BioFuzzFieldInputSequence;
import com.crawljax.plugins.biofuzz.proxy.BioFuzzProxy;
import com.crawljax.plugins.biofuzz.proxy.BioFuzzProxyMgr;
import com.crawljax.plugins.biofuzz.proxy.buffer.BioFuzzConvBuffer;
import com.crawljax.plugins.biofuzz.proxy.buffer.BioFuzzConvPair;
import com.crawljax.plugins.biofuzz.proxy.buffer.BioFuzzParamFilter;
import com.crawljax.plugins.biofuzz.utils.BioFuzzUtils;


public class TestBioFuzzFuzzing {

	final static Logger logger = LoggerFactory.getLogger(TestBioFuzzFuzzing.class);
	private BioFuzzAutomation bauto = new BioFuzzAutomation();
	@Test
	public void test() {

		//BioFuzzBrowserMgr mgr = new BioFuzzBrowserMgr("http://localhost/webchess9", "127.0.0.1", 8084);

		BioFuzzProxyMgr pmgr = BioFuzzProxyMgr.getInstance("127.0.0.1", 8084);
		BioFuzzProxy proxy = pmgr.createAndGetProxy();
		
		
		BioFuzzBrowserMgr bmgr = new BioFuzzBrowserMgr("http://localhost/webchess9", proxy);

		proxy.setFilter(new BioFuzzParamFilter());

		EmbeddedBrowser browser1 = bmgr.getBrowser();

		BioFuzzFieldInputSequence iseq = new BioFuzzFieldInputSequence("//FORM[@name='loginForm']","//FORM[@name='loginForm']");


		BioFuzzFieldInput i0 = new BioFuzzFieldInput(BioFuzzAction.GO_TO_URL, "http://localhost/webchess9");
		BioFuzzFieldInput i1 = new BioFuzzFieldInput("//FORM//INPUT[@name='txtNick']",BioFuzzAction.TEXT_INPUT, "webchess&");
		BioFuzzFieldInput i2 = new BioFuzzFieldInput("//FORM//INPUT[@name='pwdPassword']",BioFuzzAction.TEXT_INPUT, "webchess");
		BioFuzzFieldInput i3 = new BioFuzzFieldInput("//FORM//INPUT[@type='submit']",BioFuzzAction.CLICK);
		//Input i4 = new Input("//A[@href='javascript:loadEndedGame(2)']",InputType.CLICK);
		


		iseq.add(i0);
		iseq.add(i1);
		iseq.add(i2);
		iseq.add(i3);
		//iseq.add(i4);

		List<BioFuzzContent> lcont = new Vector<BioFuzzContent>();
		
	    proxy.startRec();
		for(int i = 0; i < iseq.getSize(); i++) {
			BioFuzzFieldInput in = iseq.get(i);
			logger.debug("Executing input " + in.toString());
			boolean ret = this.bauto.executeInput(browser1, in);
			if(!ret) {
				logger.debug("Didn't work out");
				break;
			}
		}
		proxy.stopRec();
		
		BioFuzzConvBuffer buf = proxy.getRequestBuffer();
		
		for(int i = 0; i < buf.getSize(); i++) {
			BioFuzzConvPair cpair = buf.get(i);
			Request req = cpair.getRequest();
			lcont.add(BioFuzzUtils.createAndGetContent(req));	
		}
		
		logger.debug("Show the output");
		for(BioFuzzContent c : lcont) {
			for(int i = 0; i < c.getSize(); i++) {
				BioFuzzParamTuple cpair = c.get(i);
				
				logger.debug("Key:" + cpair.getKey() + " Value: " + cpair.getValue());
			}
		}
	}

}
