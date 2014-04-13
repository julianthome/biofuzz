package com.crawljax.plugins.biofuzz.test;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.biofuzztk.components.BioFuzzMgr;
import org.biofuzztk.components.tokenizer.BioFuzzSQLTokenizer;
import org.biofuzztk.ptree.BioFuzzParseTree;
import com.crawljax.plugins.biofuzz.utils.BioFuzzUtils;

public class TestBioFuzzModifier {

	
	private static BioFuzzMgr mgr;
	final static Logger logger = LoggerFactory.getLogger(TestBioFuzzModifier.class);

	static List<BioFuzzParseTree> tLst0 = null;
	static List<BioFuzzParseTree> tLst1 = null;
	static List<BioFuzzParseTree> tLst2 = null;
	static List<BioFuzzParseTree> tLst3 = null;
	static List<BioFuzzParseTree> tLst4 = null;
	
	@BeforeClass
	public static void testParser() {
		mgr = new BioFuzzMgr("cfg.xml", new BioFuzzSQLTokenizer());
		assert(mgr != null);
		
		logger.debug(">> Tree 0 creation");
		tLst0 = mgr.buildTrees("SELECT * from taba WHERE a = b UNION ALL SELECT * FROM test WHERE 'a' = 'b");
		assert(tLst0 != null);
		logger.debug("List length: " + tLst0.size());
		
		logger.debug(">> Tree 1 creation");
		tLst1 = mgr.buildTrees("SELECT * from taba,tabb,tabc WHERE 1 = 1 OR name > 'hallo'");
		assert(tLst1!= null);
		logger.debug("List length: " + tLst1.size());
		
		
		logger.debug(">> Tree 2 creation");
		tLst2 = mgr.buildTrees("SELECT name,address,location from tab_c WHERE 1 = 2");
		assert(tLst2 != null);
		logger.debug("List length: " + tLst2.size());
		logger.debug(tLst2.toString());
		
		logger.debug(">> Tree 3 creation");
		tLst3 = mgr.buildTrees("SELECT name,address,location FROM tab_b WHERE usr = 'hans");
		assert(tLst3 != null);
		logger.debug("List length: " + tLst3.size());
		
		
		logger.debug(">> Tree 4 creation");
		tLst4 = mgr.buildTrees("SELECT * FROM players WHERE nick = '278570'");
		
		mgr.validate(tLst4.get(0));
		
		logger.debug(tLst4.get(0).toString());
		for(int i = 0; i < 15; i++) {
			mgr.extend(tLst4.get(0));
			logger.debug(tLst4.get(0).getTokLst().toString());
		}
		
		
		
		
		//mgr.validate(tLst4.get(0),false);
		//logger.debug(tLst4.get(0).toString());
		assert(tLst4 != null);

	}
	
	//@Test
	public void testCrossOver() {
		
		logger.debug(">> Test Crossover BEGIN");
		
		BioFuzzParseTree treeA = tLst0.get(0);
		
		assert(treeA != null);
		mgr.validate(treeA);
		logger.debug("A:" + treeA.getTokLst().toString());
		assert(treeA.getRootNode().getVal() == true);
		
		BioFuzzParseTree treeB = tLst1.get(0);
		assert(treeB != null);
		mgr.validate(treeB);
		logger.debug("B:" + treeB.getTokLst().toString());
		assert(treeB.getRootNode().getVal() == true);
		
		
		BioFuzzParseTree treeC = tLst2.get(0);
		assert(treeB != null);
		mgr.validate(treeC);
		logger.debug("C:" + treeC.getTokLst().toString());
		assert(treeC.getRootNode().getVal() == true);
		
		
		logger.debug(treeA.toString());
		logger.debug(treeB.toString());
		int i = 0;
		
		BioFuzzParseTree treeAB = null;
		while(i ++ < 200) {
			treeAB = mgr.crossover(treeA, treeB);
			assert(treeAB != null);
			mgr.validate(treeAB);
			logger.debug("AB: " + treeAB.getTokLst().toString());
			logger.debug(treeAB.toString());
			assert(treeAB.getVal() == true);
		}
		
		BioFuzzParseTree treeABA = mgr.crossover(treeAB, treeA);
		assert(treeC != null);
		mgr.validate(treeABA);
		logger.debug("ABA: " + treeABA.getTokLst().toString());
		logger.debug(treeABA.toString());
		assert(treeABA.getRootNode().getVal() == true);
		

		
		logger.debug("Check with prefix now");
		treeA.setPfxBarrier(6);
		BioFuzzParseTree treeABpfx = mgr.crossover(treeA, treeB);
		assert(treeABpfx != null);
		mgr.validate(treeABpfx);
		logger.debug("ABpfx: " + treeABpfx.getTokLst().toString());
		logger.debug(treeABpfx.toString());
		assert(treeABpfx.getRootNode().getVal() == true);

		

		BioFuzzParseTree treeAC = mgr.crossover(treeA, treeC);
		assert(treeAC != null);
		mgr.validate(treeAC);
		logger.debug("AC: " + treeAC.getTokLst().toString());
		logger.debug(treeAC.toString());
		assert(treeAC.getRootNode().getVal() == true);
		logger.debug(mgr.toString());
		logger.debug(">> Test Crossover END");
		
	}
	
	@Test
	public void testMutation() {
		
		logger.debug(">> Test Mutation BEGIN");
		
		BioFuzzParseTree treeA = tLst0.get(0);
		assert(treeA != null);
		mgr.validate(treeA);
		logger.debug(treeA.toString());
		
		BioFuzzParseTree treeB = tLst1.get(0);
		assert(treeB != null);
		mgr.validate(treeB);
		logger.debug(treeB.toString());
		
		BioFuzzParseTree treeC = tLst2.get(0);
		assert(treeB != null);
		mgr.validate(treeC);
		//logger.debug(treeC.toString());
		
		BioFuzzParseTree treeD = tLst3.get(0);
		assert(treeB != null);
		mgr.validate(treeD);
		assert(treeD.getVal() == false);
		
		String sA0 = treeA.getTokLst().getStrFromTokens();
		logger.debug("sA0: " + sA0);
		String sB0 = treeB.getTokLst().getStrFromTokens();
		logger.debug("sB0: " + sB0);
		String sC0 = treeC.getTokLst().getStrFromTokens();
		logger.debug("sC0: " + sC0);
		String sD0 = treeC.getTokLst().getStrFromTokens();
		logger.debug("sD0: " + sD0);
		
		Boolean a = mgr.mutate(treeA);
		logger.debug("Mutation: " + a + "for treeA");
		Boolean b = mgr.mutate(treeB);
		logger.debug("Mutation: " + b + "for treeA");
		Boolean c = mgr.mutate(treeC);
		logger.debug("Mutation: " + c + "for treeA");
		Boolean d = mgr.mutate(treeD);
		logger.debug("Mutation: " + c + "for treeA");
		
		String sA1 = treeA.getTokLst().getStrFromTokens();
		logger.debug("sA1: " + sA1);
		String sB1 = treeB.getTokLst().getStrFromTokens();
		logger.debug("sB1: " + sB1);
		String sC1 = treeC.getTokLst().getStrFromTokens();
		logger.debug("sC1: " + sC1);
		String sD1 = treeD.getTokLst().getStrFromTokens();
		logger.debug("sD1: " + sD1);
		
		if (a)
			assert(BioFuzzUtils.getStrDist(sA0, sA1) > 0);
		if (b)
			assert(BioFuzzUtils.getStrDist(sB0, sB1) > 0);
		if (c)
			assert(BioFuzzUtils.getStrDist(sC0, sC1) > 0);
		if (d)
			assert(BioFuzzUtils.getStrDist(sD0, sD1) > 0);
		
		
		
		logger.debug(">> Test Mutation END");
		
	}

}
