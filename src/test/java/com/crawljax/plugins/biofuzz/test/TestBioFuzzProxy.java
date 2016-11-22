package com.crawljax.plugins.biofuzz.test;


import com.codahale.metrics.MetricRegistry;
import com.crawljax.browser.EmbeddedBrowser;
import com.crawljax.browser.WebDriverBrowserBuilder;
import com.crawljax.core.configuration.CrawljaxConfiguration;
import com.crawljax.core.configuration.CrawljaxConfiguration.CrawljaxConfigurationBuilder;
import com.crawljax.core.configuration.ProxyConfiguration;
import com.crawljax.core.plugin.Plugins;
import com.crawljax.plugins.biofuzz.core.components.BioFuzzAutomation;
import com.crawljax.plugins.biofuzz.input.BioFuzzFieldInput;
import com.crawljax.plugins.biofuzz.input.BioFuzzFieldInput.BioFuzzAction;
import com.crawljax.plugins.biofuzz.input.BioFuzzFieldInputSequence;
import com.crawljax.util.DomUtils;
import net.lightbody.bmp.core.har.*;
import net.lightbody.bmp.proxy.ProxyServer;
import net.lightbody.bmp.proxy.http.BrowserMobHttpRequest;
import net.lightbody.bmp.proxy.http.RequestInterceptor;
import org.junit.Test;
import org.openqa.selenium.Proxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.List;

public class TestBioFuzzProxy {
	
	private BioFuzzAutomation bauto = new BioFuzzAutomation();
	private NodeList iBoxes = null;
	private BioFuzzFieldInputSequence iseq = null;
	private EmbeddedBrowser browser = null;
	private BioFuzzFieldInput eventTrigger = null;
	
	final static Logger logger = LoggerFactory.getLogger(TestBioFuzzProxy.class);
	static HarLog hlog = null;
	static ProxyServer server = null;
	
	private NodeList getInputBoxes(Document dom) {
		return bauto.xpathRealQuery(dom,"//FORM//INPUT[@type='text' or @type='password' or @type='submit']|//FORM//TEXTAREA");
	}
	
	private void goToStartStateIseq() {
		Document dom = null;

		for(int i = 0; i < this.iseq.getSize(); i++) {
			BioFuzzFieldInput in = this.iseq.get(i);
			this.bauto.executeInput(this.browser, in);
		}
			
		// final state reached ... search for input boxes if not already done

		try {
			dom = DomUtils.asDocument(browser.getStrippedDom());
		} catch (IOException e) {
			e.printStackTrace();
		}
		assert(dom != null);
		this.iBoxes = getInputBoxes(dom);
	}
	
	@Test
	public void test() {
		
		server = new ProxyServer(8888);
		try {
			server.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			assert(false);
		}
		Proxy proxy = null;
		// get the Selenium proxy object
		try {
			proxy = server.seleniumProxy();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		server.setCaptureContent(true);
		server.setCaptureHeaders(true);
		server.setCaptureBinaryContent(true);
		assert(proxy != null);
		// configure it as a desired capability
		//DesiredCapabilities capabilities = new DesiredCapabilities();
		//capabilities.setCapability(CapabilityType.PROXY, proxy);
		
		
		CrawljaxConfigurationBuilder builder = CrawljaxConfiguration.builderFor("http://localhost/webchess9");
		builder.setProxyConfig(ProxyConfiguration.manualProxyOn("127.0.0.1", 8888));
		
		
		CrawljaxConfiguration config = builder.build();
		Plugins plugins = new Plugins(config, new MetricRegistry());
		WebDriverBrowserBuilder wdev = new WebDriverBrowserBuilder(config, plugins);
		
		// create a new HAR with the label "yahoo.com"
		//server.newHar("http://localhost/webchess9");
		//server.newPage("http://localhost/webchess9");
		
	    //server.remapHost("http://localhost/webchess9", "127.0.0.1");
		server.newHar("http://localhost/webchess9");
		
		//Har har = server.getHar();
		
		//hlog = har.getLog();
		
		
	    
//	    server.addRequestInterceptor(new HttpRequestInterceptor() {
//
//            public void process(HttpRequest request, HttpContext context)
//                    throws HttpException, IOException {
//            	
//            	Header [] hl = request.getAllHeaders();
//
//            	logger.debug("Context: " + context.toString());
//            	
//            	logger.debug(">A-----------------------------------------\n");
//            	
//            	for(Header h : hl) {
//            		logger.debug(h.getName() + " " + h.getValue());
//            		HeaderElement[] e = h.getElements();
//            		
//            		for(HeaderElement l : e) {
//            			logger.debug("Element:" + l.toString());
//            		}
//            		
//            		
//            	}
//            	
//            
//            	logger.debug("REQUEST: " + request.getRequestLine().toString());
//            	
//            	HttpParams pars = request.getParams();
//            	
//            	if(pars.getParameter("cmd") != null) {
//            		logger.debug("YAHOOO");
//            	}
//            	
//                logger.debug("<V-----------------------------------------\n");
//            }
//        });
	    
	    
/*		server.addRequestInterceptor(new RequestInterceptor() {
		    @Override
		    public void process(BrowserMobHttpRequest r) {
		    	assert(r != null);		    	
		    	
		        logger.debug("Intercepted\n");
		        logger.debug("---------------------------------------------<>\n");
		        r.getProxyRequest().getParameterStringArrayMap();
		        HttpRequest req = r.getProxyRequest();
		        req.setAttribute("txtNick", "test");
		        r.setRequestBody("hhhhh");
		        //req.getParameters();
		        assert(req != null);
		        logger.debug("CTYPE: " + req.getContentType());
		        logger.debug("DOT: " + req.getDotVersion());
		        logger.debug("QUERY :" + req.getHost());
		        logger.debug("PATH: " + req.getPath());
		        logger.debug("PATH: " + req.getRequestLine());
		        logger.debug("KKK: " + req.getURI());
		        logger.debug("::::" + req.toString());
		        logger.debug("<>---------------------------------------------\n");
		    }
		});*/
		
		server.addRequestInterceptor(new RequestInterceptor() {
	    @Override
	    public void process(BrowserMobHttpRequest r) {
	    	logger.debug("---------------------------------");
			logger.debug("REQ: " + r.toString());
			logger.debug("---------------------------------");
	    }
	});
	
		//server.
		
		this.browser = wdev.get(); 
		// get the HAR data
		//Har har = server.getHar();
		
		BioFuzzFieldInputSequence iseq = new BioFuzzFieldInputSequence("//FORM[@name='loginForm']","//FORM[@name='loginForm']");
		
		BioFuzzFieldInput i0 = new BioFuzzFieldInput(BioFuzzAction.GO_TO_URL, "http://localhost/webchess9");
		BioFuzzFieldInput i1 = new BioFuzzFieldInput("//FORM//INPUT[@name='txtNick']",BioFuzzAction.TEXT_INPUT, "webchess");
		BioFuzzFieldInput i2 = new BioFuzzFieldInput("//FORM//INPUT[@name='pwdPassword']",BioFuzzAction.TEXT_INPUT, "webchess");
		BioFuzzFieldInput i3 = new BioFuzzFieldInput("//FORM//INPUT[@type='submit']",BioFuzzAction.CLICK);
		BioFuzzFieldInput i4 = new BioFuzzFieldInput("//FORM[@name='preferences']//INPUT[@name='txtReload']",BioFuzzAction.TEXT_INPUT, "100");
		BioFuzzFieldInput i5 = new BioFuzzFieldInput("//FORM[@name='preferences']//INPUT[@value='Update Preferences']",BioFuzzAction.CLICK);
		
		//Input i1 = new Input("//FORM//INPUT[@value='New Account']",InputType.CLICK);
		//Input i2 = new Input("//BODY//FORM[@action='mainmenu.php' and @method='post' and @name='userdata']//INPUT[@name='btnCancel' and @type='button' and @value='Cancel']",InputType.CLICK);
		
		iseq.add(i0);
		iseq.add(i1);
		iseq.add(i2);
		iseq.add(i3);
		iseq.add(i4);
		iseq.add(i5);
		//iseq.add(i2);
		
	
		for(int i = 0; i < iseq.getSize(); i++) {
			BioFuzzFieldInput in = iseq.get(i);
			logger.debug("Executing input " + in.toString());
			boolean ret = this.bauto.executeInput(browser, in);
			if(!ret) {
				logger.debug("Didn't work out");
			}
		}
		
		
		logger.debug("START -------------------------------------------");
		Har har = server.getHar();
    	HarLog hlog = null;
    	if(har != null)
	    	hlog = har.getLog();
    	else
    		logger.debug("hentry is null");
    	
    	
    	for(HarEntry e : hlog.getEntries()) {
    		HarRequest req = e.getRequest();
    		HarPostData hpd = null;
    		if(req != null) {
    			hpd = req.getPostData();
    			if(hpd != null) {
    				List<HarPostDataParam> hl = hpd.getParams();
    				for(HarPostDataParam p : hl) {
    					logger.debug(">>"  + p.getName() + " : " + p.getValue());
    				}
    			}
    		}
    
    	}

    	logger.debug("END -------------------------------------------");
    	
		try {
			server.stop();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.debug(e.getMessage());
			assert(false);
		}

		
	}

}
