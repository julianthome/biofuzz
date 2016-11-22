package com.crawljax.plugins.biofuzz.core.components;

import com.crawljax.browser.EmbeddedBrowser;
import com.crawljax.core.CrawljaxException;
import com.crawljax.core.state.Eventable;
import com.crawljax.core.state.Eventable.EventType;
import com.crawljax.core.state.Identification;
import com.crawljax.core.state.Identification.How;
import com.crawljax.core.state.StateVertex;
import com.crawljax.plugins.biofuzz.input.BioFuzzFieldInput;
import com.crawljax.plugins.biofuzz.utils.BioFuzzUtils;
import com.crawljax.util.DomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.xpath.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class BioFuzzAutomation {

	private static final Logger log = LoggerFactory
			.getLogger(BioFuzzAutomation.class);
	
	public boolean xpathQuery(Document doc, String query) {
		log.debug("xpathQuery(Document doc, String query)");
		log.debug("path: " + query);
		
		XPathFactory xpathFact = XPathFactory.newInstance();
		XPath xpath = xpathFact.newXPath();
		boolean ret = false;
		try {
			ret = (xpath.evaluate(query, doc) == null) ? false : true;
		} catch (XPathExpressionException e) {
			log.debug("exception");
			log.debug(e.getMessage());
			return false;
		}
		
		if(ret == false) {
			log.debug("ret is false");
			log.debug(BioFuzzUtils.domToString(doc));
			
		}
		
		return ret;
	}
	
	public NodeList xpathRealQuery(Document doc, String query) {
		log.debug("xpathRealQuery(Document doc, String query)");
		XPathFactory xpathFact = XPathFactory.newInstance();
		XPath xpath = xpathFact.newXPath();
		XPathExpression expr = null;
		NodeList res = null;
		try {
			expr = xpath.compile(query);
			Object result = expr.evaluate(doc, XPathConstants.NODESET);
			res = (NodeList) result;
		} catch (XPathExpressionException e) {
			log.debug(e.getMessage());
			return null;
		}
		return res;
	}
	
	
	public NodeList xpathRealQuery(String doc, String query) {
		log.debug("xpathRealQuery(String doc, String query)");
		XPathFactory xpathFact = XPathFactory.newInstance();
		XPath xpath = xpathFact.newXPath();
		XPathExpression expr = null;
		NodeList res = null;
		try {
			expr = xpath.compile(query);
			Object result = expr.evaluate(doc, XPathConstants.STRING);
			res = (NodeList) result;
		} catch (XPathExpressionException e) {
			log.debug(e.getMessage());
			return null;
		}
		return res;
	}
	
	private boolean xpathInput(EmbeddedBrowser browser, StateVertex vertex, String path, String value) {
		log.debug("xpathInput(EmbeddedBrowser browser, StateVertex vertex, String path, String value)");
		Document doc = null;
		try {
			doc = vertex.getDocument();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assert(doc != null);
		if (xpathQuery(doc, path)) {
			Identification id = new Identification();
			id.setHow(How.xpath);
			id.setValue(path);
			try {
				return browser.input(id, value);
			} catch (CrawljaxException e) {
				log.debug(e.getMessage());
				return false;
			}
		}
		
		return false;
	}
	
	private boolean xpathInput(EmbeddedBrowser browser, String path, String value) {
		log.debug("xpathInput(EmbeddedBrowser browser, String path, String value)");
		assert(browser != null);
		Document doc = null;

		try {
			doc = DomUtils.asDocument(browser.getStrippedDom());
		} catch (IOException e) {
			log.debug(e.getMessage());
			return false;
		}

		assert(doc != null);
		if (xpathQuery(doc, path)) {
			Identification id = new Identification();
			id.setHow(How.xpath);
			id.setValue(path);
			try {
				log.debug("Browser input\n");
				
				return browser.input(id, value);
			} catch (CrawljaxException e) {
				log.debug(e.getMessage());
				return false;
			}
		}
		
		return false;
	}
	
	
	public boolean xpathEvent(EmbeddedBrowser browser, StateVertex vertex, String path, EventType t) {
		log.debug("xpathEvent(EmbeddedBrowser browser, StateVertex vertex, String path, EventType t)");
		log.debug("path: " + path);
		Document doc = null;
		try {
			doc = vertex.getDocument();
		} catch (IOException e) {
			log.debug("exception");
			log.debug(e.getMessage());
			log.debug(e.getCause().toString());
			return false;
		}
		assert(doc != null);
		if (xpathQuery(doc, path)) {
			Identification id = new Identification();
			id.setHow(How.xpath);
			id.setValue(path);
			Eventable ev = new Eventable();
			ev.setEventType(t);
			ev.setIdentification(id);
			
			return fireEvent(ev, browser);
		}
		return false;
	}
	
	public boolean xpathEvent(EmbeddedBrowser browser, String path, EventType t) {
		log.debug("xpathEvent without start\n");
		Document doc = null;
		assert(browser != null);
		try {
			doc = DomUtils.asDocument(browser.getStrippedDom());
		} catch (IOException e) {
			log.debug(e.getMessage());
			return false;
		}


		assert(doc != null);
		if (xpathQuery(doc, path)) {
			Identification id = new Identification();
			id.setHow(How.xpath);
			id.setValue(path);
			Eventable ev = new Eventable();
			ev.setEventType(t);
			ev.setIdentification(id);
			
			return fireEvent(ev, browser);
		}
		return false;
	}
	
	public boolean executeInput(EmbeddedBrowser browser,StateVertex vertex, BioFuzzFieldInput in) {
		log.debug("executeInput(EmbeddedBrowser browser,StateVertex vertex,Input in) ");
		log.debug("Input: " + in.toString());
		assert(browser != null);
		assert(vertex != null);
		assert(in != null);
		switch(in.getAction()) {
		case TEXT_INPUT:
			boolean ret = true;
			for(String elementId : in.getElementIds()) {
				ret = ret & xpathInput(browser, vertex, elementId, in.getInputValue());
			}
			return ret;
		case CLICK:
			return xpathEvent(browser, vertex, in.getElementId(0), EventType.click);
		case GO_TO_URL:
			try {
				browser.goToUrl(new URI(in.getInputValue()));
				return true;
			} catch (URISyntaxException e) {
				log.debug(e.getMessage());
				return false;
			}
		}
		
		return false;
	}
	
	public boolean executeInput(EmbeddedBrowser browser,BioFuzzFieldInput in) {
		log.debug("executeInput(EmbeddedBrowser browser,Input in)");
		assert(browser != null);
		assert(in != null);
		
		switch(in.getAction()) {
		case TEXT_INPUT:
			log.debug("text input");
			boolean ret = true;
			for(String elementId : in.getElementIds()) {
				log.debug("execute input for" + elementId);
				ret = ret & xpathInput(browser, elementId, in.getInputValue());
			}
			return ret;
		case CLICK:
			log.debug("click");
			assert(in.getElementIds().size() > 0);
			return xpathEvent(browser,in.getElementId(0), EventType.click);
		case GO_TO_URL:
			log.debug("go to url");
			try {
				browser.goToUrl(new URI(in.getInputValue()));
				return true;
			} catch (URISyntaxException e) {
				log.debug(e.getMessage());
				return false;
			} 
		default:
			break;
		}
		
		return false;
	}
	
	// Based on the fireEvent Method in the crawljax core
    private boolean fireEvent(Eventable eventable, EmbeddedBrowser browser) {
    	log.debug("fireEvent(Eventable eventable, EmbeddedBrowser browser)");
        Eventable eventToFire = eventable;

        boolean isFired = false;
        try {
                isFired = browser.fireEventAndWait(eventToFire);
        //} catch (ElementNotVisibleException | NoSuchElementException e) {
         //       return false;
        } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return false;
        }

        if (isFired) {
                return true;
        } else {
                return false; // no event fired
        }
    }	


}
