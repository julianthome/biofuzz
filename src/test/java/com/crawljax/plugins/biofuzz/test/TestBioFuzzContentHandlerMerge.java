package com.crawljax.plugins.biofuzz.test;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.owasp.webscarab.model.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.crawljax.plugins.biofuzz.input.BioFuzzContent;
import com.crawljax.plugins.biofuzz.input.BioFuzzContent.ContentType;
import com.crawljax.plugins.biofuzz.input.BioFuzzContentHandler;
import com.crawljax.plugins.biofuzz.input.BioFuzzParamTuple;

public class TestBioFuzzContentHandlerMerge {
	
	final static Logger logger = LoggerFactory.getLogger(TestBioFuzzContentHandlerMerge.class);
	
	@Test
	public void test() {
		
		logger.debug("Test Content Handler");
		
		BioFuzzContentHandler handler0 = new BioFuzzContentHandler();
		handler0.addContentFromString("semester=1&page2=1&logout=&page=4&selectclass=1&studentid=1&update=1", ContentType.POST);
		
		BioFuzzContentHandler handler1 = new BioFuzzContentHandler();
		handler1.addContentFromString("semester=1&page2=1&page=4&selectclass=1&studentid=1", ContentType.POST);
		
		logger.debug(handler0.toString());
		
		handler0.submit(2, "test");
		handler0.submit(3, "0000");
		logger.debug(handler0.toString());
		
		handler0.rollback();
		
		logger.debug(handler0.toString());
		logger.debug(handler1.toString());

		handler0.setInjectable(0);
		handler1.setInjectable(1);
		handler1.setInjectable(2);
		
		logger.debug(handler0.toString());
		logger.debug(handler1.toString());

		
	}
	
	
}
