package com.crawljax.plugins.biofuzz.test;

import com.crawljax.browser.EmbeddedBrowser;
import com.crawljax.plugins.biofuzz.core.BioFuzzBrowserMgr;
import com.crawljax.plugins.biofuzz.core.components.BioFuzzAutomation;
import com.crawljax.plugins.biofuzz.input.BioFuzzFieldInput;
import com.crawljax.plugins.biofuzz.input.BioFuzzFieldInput.BioFuzzAction;
import com.crawljax.plugins.biofuzz.input.BioFuzzFieldInputSequence;
import com.crawljax.plugins.biofuzz.proxy.BioFuzzClient;
import com.crawljax.plugins.biofuzz.proxy.BioFuzzProxy;
import com.crawljax.plugins.biofuzz.proxy.BioFuzzProxyMgr;
import com.crawljax.plugins.biofuzz.proxy.BioFuzzProxyMgr.BioFuzzCSPair;
import com.crawljax.plugins.biofuzz.proxy.buffer.BioFuzzConvBuffer;
import com.crawljax.plugins.biofuzz.proxy.buffer.BioFuzzParamFilter;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestBioFuzzRequestInterception {

	final static Logger logger = LoggerFactory.getLogger(TestBioFuzzRequestInterception.class);
	private BioFuzzAutomation bauto = new BioFuzzAutomation();
	@Test
	public void test() {

		//BioFuzzBrowserMgr mgr = new BioFuzzBrowserMgr("http://localhost/webchess9", "127.0.0.1", 8084);

		BioFuzzProxyMgr pmgr = BioFuzzProxyMgr.getInstance("127.0.0.1", 8084);
		
		BioFuzzCSPair cpair = pmgr.createAndGetCSPair();
		
		BioFuzzProxy proxy = cpair.getProxy();
		
		
		BioFuzzBrowserMgr bmgr = new BioFuzzBrowserMgr("http://localhost/webchess9", proxy);
		proxy.setFilter(new BioFuzzParamFilter());

		
		EmbeddedBrowser browser1 = bmgr.getBrowser();

		BioFuzzFieldInputSequence iseq3 = new BioFuzzFieldInputSequence("//FORM[@name='loginForm']","//FORM[@name='loginForm']");
		
		BioFuzzFieldInput i00 = new BioFuzzFieldInput(BioFuzzAction.GO_TO_URL, "http://localhost/schoolmate");
		BioFuzzFieldInput i01 = new BioFuzzFieldInput("//FORM[@name='login']//INPUT[@name='username']",BioFuzzAction.TEXT_INPUT, "marie");
		BioFuzzFieldInput i02 = new BioFuzzFieldInput("//FORM[@name='login']//INPUT[@name='password']",BioFuzzAction.TEXT_INPUT, "test");
		BioFuzzFieldInput i03 = new BioFuzzFieldInput("//FORM[@name='login']//INPUT[@type='submit']",BioFuzzAction.CLICK);
		
		iseq3.add(i00);
		iseq3.add(i01);
		iseq3.add(i02);
		iseq3.add(i03);


		
		
		logger.debug("===================================================================================================1B");
	    proxy.startRec();
		for(int i = 0; i < iseq3.getSize(); i++) {
			BioFuzzFieldInput in = iseq3.get(i);
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
		
//		
//		
		BioFuzzClient cl = cpair.getClient();
//		
//		assert(proxy.getRequestBuffer().getSize() > 0);
//		proxy.startRec();
//		assert(proxy.getRequestBuffer().getSize() <= 0);
//		logger.debug("Start replaying stuff");
//		
//		assert(cl.replay(buf, 0, buf.getSize()-1) == true);
//		
//		proxy.stopRec();
//		
//		assert(proxy.getRequestBuffer().getSize() > 0);
		
//		for(int i = 0; i < buf.getSize(); i++) {
//			BioFuzzConvPair pair = buf.get(i);
//			
//			Request req = pair.getRequest();
//			if(req == null)
//				continue;
//			
//			if(i == buf.getSize()-1) {
//				logger.debug("ST----------------------------------------------------------------------");
//
//				String s = "schoolname=School Name&schooladdress=1%2CStreet&schoolphone=52365895&numsemesters=0&numperiods=0&apoint=0.0&bpoint=1.0&cpoint=0.0&dpoint=1.0&fpoint=568585&sitetext=&sitemessage=&infoupdate=1&page2=1&logout=&page=1";
//				
//				req.setContent(s.getBytes());
//				
//				//logger.debug("Send Request: " + i + " --- " + req);
//				//logger.debug("URL " + req.getURL());
//				logger.debug("EN----------------------------------------------------------------------");
//			}
//				
//			try{
//				
//				Response res = cl.fetchResponse(req);
//				
//				logger.debug("Response: " + res);
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
	
	}

}
