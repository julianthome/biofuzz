package com.crawljax.plugins.biofuzz.test;

import java.util.Iterator;
import java.util.List;


import org.biofuzztk.components.BioFuzzMgr;
import org.biofuzztk.components.tokenizer.BioFuzzSQLTokenizer;
import org.biofuzztk.ptree.BioFuzzParseTree;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestBioFuzzPVG {

	private static BioFuzzMgr mgr;
	final static Logger logger = LoggerFactory.getLogger(TestBioFuzzPVG.class);

	static List<BioFuzzParseTree> tLst0 = null;
	static List<BioFuzzParseTree> tLst1 = null;
	static List<BioFuzzParseTree> tLst2 = null;
	static List<BioFuzzParseTree> tLst3 = null;
	static List<BioFuzzParseTree> tLst4 = null;
	
	@BeforeClass
	public static void testParser() {
		mgr = new BioFuzzMgr("cfg.xml", new BioFuzzSQLTokenizer());
		assert(mgr != null);
		
		logger.debug(">> Tree creation");
		tLst0 = mgr.buildTrees("SELECT * from tab_user");
		assert(tLst0 != null);
		logger.debug("List length: " + tLst0.size());
		
		tLst1 = mgr.buildTrees("SELECT * from tab WHERE 1 = 1");
		assert(tLst1 != null);
		logger.debug("List length: " + tLst1.size());
		assert(tLst1.size() > 0);
		
		//tLst2 = mgr.buildTrees("SELECT * FROM history WHERE gameID = ZONPW");
		logger.debug(mgr.toString());
		tLst2 = mgr.buildTrees("SELECT nick FROM players,games WHERE playerID=blackPlayer AND gameID=204398");
		assert(tLst2 != null);
		logger.debug("List length: " + tLst2.size());
		assert(tLst2.size() > 0);
		
		
		logger.debug(mgr.toString());
		tLst3 = mgr.buildTrees("INSERT INTO tab_test VALUES('a',4)");
		assert(tLst3 != null);
		logger.debug("List length: " + tLst3.size());
		assert(tLst3.size() > 0);
		
		logger.debug(mgr.toString());
		tLst4 = mgr.buildTrees("SELECT * from tab WHERE 1 = 1");
		assert(tLst4 != null);
		logger.debug("List length: " + tLst4.size());
		assert(tLst4.size() > 0);
		
		
		
	}
	
	//@Test
	public void testValidatorGenerator() {
		assert(tLst0 != null);
		Iterator<BioFuzzParseTree> iter = tLst0.iterator();
		
		while(iter.hasNext()) {
			logger.debug(">> Iterate over children");
			BioFuzzParseTree tree = iter.next();
			logger.debug(">> Set prefix barrier");
			tree.setPfxBarrierToLastTok();
			logger.debug(">> Validate tree");
			mgr.validate(tree);
			
			logger.debug(">> Extend and revalidate tree");
			
			int len0 = tree.getTokLstLen();
			mgr.extend(tree);
			//assert(tree.getVal() == true);
			
			// tree is not extended since it was already valid
			//assert(tree.getTokLstLen() == len0);
			// validation in non-strict mode - look randomly how 
			// root element is invalid now
			//assert(tree.getVal() == false);
			mgr.extend(tree);
			mgr.validate(tree);
			int len1 = tree.getTokLstLen();
			assert(len1 >= len0);
			logger.debug(tree.toString());
		}
		
	}
	
	@Test
	public void testGenerator0() {
		assert(tLst1 != null);
		Iterator<BioFuzzParseTree> iter = tLst1.iterator();
		
		while(iter.hasNext()) {
			logger.debug(">> Iterate over children");
			BioFuzzParseTree tree = iter.next();
			
			mgr.validate(tree);
			logger.debug(tree.toString());
			//assert(!tree.getVal());
			int len0 = tree.getTokLstLen();
			mgr.extend(tree);
			int len1 = tree.getTokLstLen();
			assert(len1 >= len0);
			//assert(!tree.getVal());
			
			mgr.validate(tree);
			mgr.extend(tree);
			int len2 = tree.getTokLstLen();
			//assert(!tree.getVal());
			assert(len2 >= len1);
			
			mgr.validate(tree);
			mgr.extend(tree);
			int len3 = tree.getTokLstLen();
			//assert(tree.getVal());
			assert(len3 >= len2);
			
			//mgr.validate(xtree);
			BioFuzzParseTree a = tLst2.get(0);
			a.setPfxBarrier(0);
			
			logger.debug(a.toString());
			
			BioFuzzParseTree xtree = mgr.crossover(tLst3.get(0), tLst4.get(0));
			//logger.debug(xtree.toString());
			//logger.debug(mgr.toString());
		}
		
	}
	
	
	//@Test
	public void testGenerator1() {
		assert(tLst2 != null);
		Iterator<BioFuzzParseTree> iter = tLst2.iterator();
		
		logger.debug(mgr.toString());
		

		
		while(iter.hasNext()) {
			
			BioFuzzParseTree tree = iter.next();
			
			
			for(int i = 0; i < 2; i++) {
				logger.debug("00000000000000000000000000000000000000000000000000");
				mgr.extend(tree);
				logger.debug("00000000000000000000000000000000000000000000000000");
			}
			logger.debug("tree " + tree.getTokLst().toString());
			
		}
		
		iter = tLst2.iterator();
		
		
		while(iter.hasNext()) {
			BioFuzzParseTree tree = iter.next();
			logger.debug("tree " + tree.getTokLst().toString());
		}
		
		
//		iter = tLst2.iterator();
//		
//		
//		while(iter.hasNext()) {
//			BioFuzzParseTree tree = iter.next();
//			logger.debug("tree " + tree.toString());
//		}
		
	}

}
