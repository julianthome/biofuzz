package com.crawljax.plugins.biofuzz.test;

import static org.junit.Assert.*;

import java.io.IOException;

import org.apache.commons.lang.RandomStringUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.codahale.metrics.MetricRegistry;
import com.crawljax.browser.EmbeddedBrowser;
import com.crawljax.browser.WebDriverBrowserBuilder;
import com.crawljax.core.configuration.CrawljaxConfiguration;
import com.crawljax.core.configuration.CrawljaxConfiguration.CrawljaxConfigurationBuilder;
import com.crawljax.core.plugin.Plugins;
import com.crawljax.core.state.Identification;
import com.crawljax.core.state.Identification.How;
import com.crawljax.plugins.biofuzz.configuration.FaqForgeInputSpec;
import com.crawljax.plugins.biofuzz.configuration.WebChessInputSpec;
import com.crawljax.plugins.biofuzz.core.components.BioFuzzAutomation;
import com.crawljax.plugins.biofuzz.input.BioFuzzFieldInput;
import com.crawljax.plugins.biofuzz.input.BioFuzzFieldInput.BioFuzzAction;
import com.crawljax.plugins.biofuzz.input.BioFuzzFieldInputSequence;
import com.crawljax.plugins.biofuzz.input.BioFuzzInputSpecIface;
import com.crawljax.plugins.biofuzz.input.BioFuzzInputSpecIface.BioFuzzSpecType;
import com.crawljax.plugins.biofuzz.utils.BioFuzzUtils;
import com.crawljax.util.DomUtils;

public class TestBioFuzzIdt {
	
	private BioFuzzAutomation bauto = new BioFuzzAutomation();
	private NodeList iBoxes = null;
	private BioFuzzFieldInputSequence iseq = null;
	private EmbeddedBrowser browser = null;
	private BioFuzzFieldInput eventTrigger = null;
	
	final static Logger logger = LoggerFactory.getLogger(TestBioFuzzIdt.class);
	
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
		CrawljaxConfigurationBuilder builder = CrawljaxConfiguration.builderFor("http://localhost/faqforge");
		CrawljaxConfiguration config = builder.build();
		Plugins plugins = new Plugins(config, new MetricRegistry());
		WebDriverBrowserBuilder wdev = new WebDriverBrowserBuilder(config, plugins);
		
		this.browser = wdev.get();
		
		BioFuzzInputSpecIface iface = new FaqForgeInputSpec();
		
		iseq = iface.getInputSequences().get(0);
		
		goToStartStateIseq();
		
		logger.debug(browser.getUnStrippedDom());
		
		Node nod = null;
		
		try {
			iBoxes = getInputBoxes(DomUtils.asDocument(browser.getStrippedDom()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assert(this.iBoxes != null);
		
		for(int i = 0; i < this.iBoxes.getLength(); i++) {

			nod = this.iBoxes.item(i);
			
			if (nod != null) {
				// generate xpath for a specific node
				String path = BioFuzzUtils.doGetXPathFromNode(nod);
				
				logger.debug("Path:" + path);
				
				String type = null;
				// get 'type' attribute
				if(nod.hasAttributes() == true) {
					if(nod.getAttributes().getNamedItem("type") != null) {
						type = nod.getAttributes().getNamedItem("type").getNodeValue();
					}
				}
					
				if(type != null && (type.equals("submit") || type.equals("button") )) {
					// input field is a button
					this.eventTrigger = new BioFuzzFieldInput(path,BioFuzzAction.CLICK,RandomStringUtils.randomAlphanumeric(5).toUpperCase());
					
				} else {

					// input field is an inputbox
					BioFuzzFieldInput in = new BioFuzzFieldInput(path,BioFuzzAction.TEXT_INPUT,RandomStringUtils.randomAlphanumeric(5).toUpperCase());
					
					// have a look at input specification
					boolean ret = this.bauto.executeInput(this.browser, in);
					//this.browser.input(identification, text)
					assert(ret == true);
				}
			} 
			
		}
		
	}

}
