package com.crawljax.plugins.biofuzz.test;

import static org.junit.Assert.*;

import java.util.List;

import org.biofuzztk.components.BioFuzzMgr;
import org.biofuzztk.components.tokenizer.BioFuzzSQLTokenizer;
import org.biofuzztk.ptree.BioFuzzParseTree;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class TestBioFuzzValidator {

	private static BioFuzzMgr mgr;
	final static Logger logger = LoggerFactory.getLogger(TestBioFuzzTracer.class);

	static List<BioFuzzParseTree> tLst0 = null;
	static List<BioFuzzParseTree> tLst1 = null;
	static List<BioFuzzParseTree> tLst2 = null;
	static List<BioFuzzParseTree> tLst3 = null;
	static List<BioFuzzParseTree> tLst4 = null;
	
	@Test
	public void testParser() {
		mgr = new BioFuzzMgr("cfg.xml", new BioFuzzSQLTokenizer());
		
		assert(mgr != null);
		
		logger.debug(">> Tree 0 creation");
		tLst0 = mgr.buildTrees("SELECT from taba WHERE 1 = 1");
		assert(tLst0 != null && tLst0.size() > 0);
		tLst1 = mgr.buildTrees("SELECT from taba WHERE 1 = 1 UNION ALL SELECT * FROM users; INSERT INTO");
		assert(tLst1 != null && tLst1.size() > 0);
		tLst2 = mgr.buildTrees("UPDATE tab_name SET t = 5 WHERE x = y");
		assert(tLst2 != null && tLst2.size() > 0);
		
		
		BioFuzzParseTree tree0 = tLst0.get(0);
		BioFuzzParseTree tree1 = tLst1.get(0);
		BioFuzzParseTree tree2 = tLst2.get(0);
		
		mgr.validate(tree0);
		mgr.validate(tree1);
		mgr.validate(tree2);
		
		assert(tree0.getVal() == true);
		assert(tree1.getVal() == false);
		assert(tree2.getVal() == true);
	

	}

}
