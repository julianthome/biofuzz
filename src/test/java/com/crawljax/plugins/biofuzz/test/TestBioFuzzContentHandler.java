package com.crawljax.plugins.biofuzz.test;

import com.crawljax.browser.EmbeddedBrowser;
import com.crawljax.plugins.biofuzz.core.BioFuzzBrowserMgr;
import com.crawljax.plugins.biofuzz.core.components.BioFuzzAutomation;
import com.crawljax.plugins.biofuzz.input.BioFuzzContentHandler;
import com.crawljax.plugins.biofuzz.input.BioFuzzFieldInput;
import com.crawljax.plugins.biofuzz.input.BioFuzzFieldInput.BioFuzzAction;
import com.crawljax.plugins.biofuzz.input.BioFuzzFieldInputSequence;
import com.crawljax.plugins.biofuzz.input.BioFuzzParamTuple;
import com.crawljax.plugins.biofuzz.proxy.BioFuzzClient;
import com.crawljax.plugins.biofuzz.proxy.BioFuzzProxy;
import com.crawljax.plugins.biofuzz.proxy.BioFuzzProxyMgr;
import com.crawljax.plugins.biofuzz.proxy.BioFuzzProxyMgr.BioFuzzCSPair;
import com.crawljax.plugins.biofuzz.proxy.buffer.BioFuzzConvBuffer;
import com.crawljax.plugins.biofuzz.proxy.buffer.BioFuzzConvPair;
import com.crawljax.plugins.biofuzz.proxy.buffer.BioFuzzParamFilter;
import com.crawljax.plugins.biofuzz.utils.BioFuzzUtils;
import org.junit.Test;
import org.owasp.webscarab.model.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class TestBioFuzzContentHandler {
	
	private BioFuzzAutomation bauto = new BioFuzzAutomation();
	final static Logger logger = LoggerFactory.getLogger(TestBioFuzzAutomation.class);

	
	@Test
	public void test() {

		System.setProperty("webdriver.gecko.driver",
				"/home/biofuzz/files/geckodriver");
		//BioFuzzBrowserMgr mgr = new BioFuzzBrowserMgr("http://localhost/webchess9", "127.0.0.1", 8084);

		BioFuzzProxyMgr pmgr = BioFuzzProxyMgr.getInstance("127.0.0.1", 8084);
		
		
		BioFuzzCSPair cpair = pmgr.createAndGetCSPair();
		
		BioFuzzProxy proxy = cpair.getProxy();
		
		BioFuzzClient client = cpair.getClient();
		
		BioFuzzBrowserMgr bmgr = new BioFuzzBrowserMgr
				("http://localhost/webchess/index.php", proxy);
		proxy.setFilter(new BioFuzzParamFilter());
		

		
		EmbeddedBrowser browser1 = bmgr.getBrowser();

		BioFuzzFieldInputSequence iseq = new BioFuzzFieldInputSequence("//FORM[@name='loginForm']","//FORM[@name='loginForm']");


		BioFuzzFieldInput i0 = new BioFuzzFieldInput(BioFuzzAction.GO_TO_URL, "http://localhost:8081/webchess/index.php");
		BioFuzzFieldInput i1 = new BioFuzzFieldInput("//FORM//INPUT[@name='txtNick']",BioFuzzAction.TEXT_INPUT, "webchess");
		BioFuzzFieldInput i2 = new BioFuzzFieldInput("//FORM//INPUT[@name='pwdPassword']",BioFuzzAction.TEXT_INPUT, "webchess");
		BioFuzzFieldInput i3 = new BioFuzzFieldInput("//FORM//INPUT[@type='submit']",BioFuzzAction.CLICK);
		//Input i4 = new Input("//A[@href='javascript:loadEndedGame(2)']",InputType.CLICK);
		


		iseq.add(i0);
		iseq.add(i1);
		iseq.add(i2);
		iseq.add(i3);
		//iseq.add(i4);
		
		logger.debug("===================================================================================================1B");
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
		
		logger.debug("===================================================================================================1E");
		
		browser1.close();

		BioFuzzConvBuffer buf = proxy.getRequestBufferCopy();
		assert(buf != null);
		logger.debug(buf.toString());
		
		BioFuzzContentHandler handler = new BioFuzzContentHandler();
		
		BioFuzzConvPair conv = buf.getLatest();
		
		Request req = conv.getRequest();
		
		handler.addContentFromRequest(req );
		
		logger.debug(handler.toString());
		
		BioFuzzParamTuple t = handler.get(0);
		t.submit("' OR 1=1--");
		logger.debug(handler.toString());
		
		Request nreq = BioFuzzUtils.fuzzAndGetRequest(req, handler.getContent());
		
		logger.debug("New Request: \n" + nreq.toString());
		proxy.start();
		try {
			client.fetchResponse(nreq);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.debug(e.getMessage());
		}
		proxy.stop();
		handler.rollback();
		logger.debug(handler.toString());
		
	}
}
