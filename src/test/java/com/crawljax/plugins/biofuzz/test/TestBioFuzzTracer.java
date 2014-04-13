package com.crawljax.plugins.biofuzz.test;

import static org.junit.Assert.*;

import java.util.List;

import org.biofuzztk.cfg.BioFuzzAttackTag.TagType;
import org.biofuzztk.components.BioFuzzMgr;
import org.biofuzztk.components.BioFuzzTracer.BioFuzzQuery;
import org.biofuzztk.components.BioFuzzTracer.TraceType;
import org.biofuzztk.components.tokenizer.BioFuzzSQLTokenizer;
import org.biofuzztk.ptree.BioFuzzParseNode;
import org.biofuzztk.ptree.BioFuzzParseTree;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class TestBioFuzzTracer {
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
		assert(tLst0 != null);
		logger.debug("List length: " + tLst0.size());
		
		logger.debug(tLst0.get(0).toString());
		
		
		BioFuzzQuery qTerm = new BioFuzzQuery() {
			public Boolean condition(BioFuzzParseNode node) {
				return node.getAtagType() != TagType.NON_TERMINAL && node.getAtagType() != TagType.ROOT;
			}
		};
		
		List<BioFuzzParseNode> l = mgr.trace(tLst0.get(0), qTerm, TraceType.DFS);
		
		assert(l != null);
		
		for(BioFuzzParseNode n : l) {
			logger.debug("DFS " + n.getAtagName());
		}
		
		logger.debug(" ------ ");
		
		l = mgr.trace(tLst0.get(0), qTerm, TraceType.BFS);
		
		assert(l != null);
		
		for(BioFuzzParseNode n : l) {
			logger.debug("BFS " + n.getAtagName());
		}
		

	}

}
