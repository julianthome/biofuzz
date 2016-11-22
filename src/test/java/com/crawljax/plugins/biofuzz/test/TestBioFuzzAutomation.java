package com.crawljax.plugins.biofuzz.test;

import com.crawljax.plugins.biofuzz.core.components.BioFuzzAutomation;
import com.crawljax.plugins.biofuzz.utils.BioFuzzUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

public class TestBioFuzzAutomation {
	
	private BioFuzzAutomation bauto = new BioFuzzAutomation();
	final static Logger logger = LoggerFactory.getLogger(TestBioFuzzAutomation.class);

	@Test
	public void test() {
		File fXmlFile = new File("./src/test/resources/page");
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		Document doc = null;
		try {
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			doc = dBuilder.parse(fXmlFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}

		assert(doc != null);
		NodeList list = bauto.xpathRealQuery(doc, "//form[@name='PersonalInfo']//input[@type='text' or @type='password']");
		Node nod = null;
		for(int i = 0; i < list.getLength(); i++) {
			nod = list.item(i);
			logger.debug("Node Name:"  + nod.getNodeName());
		}
		
		
		String sDoc = BioFuzzUtils.domToString(doc);
		logger.debug(sDoc);
		Document nDoc = null;
		try {
			nDoc = BioFuzzUtils.stringToDom(sDoc);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			assert(false);
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}

		list = bauto.xpathRealQuery(nDoc, "//form[@name='PersonalInfo']//input[@type='text' or @type='password']");
		nod = null;
		for(int i = 0; i < list.getLength(); i++) {
			nod = list.item(i);
			logger.debug("Node Name:"  + nod.getNodeName());
		}
	}
	

	@Test
	public void testStringParsing() {
		String xmls = null;
		try {
			xmls = BioFuzzUtils.readFile("./src/test/resources/page");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		NodeList list = bauto.xpathRealQuery(xmls, "//form[@name='PersonalInfo']//input[@type='text' or @type='password']");
		Node nod = null;
		for(int i = 0; i < list.getLength(); i++) {
			nod = list.item(i);
			logger.debug("Node Name:"  + nod.getNodeName());
		}
	}

}
