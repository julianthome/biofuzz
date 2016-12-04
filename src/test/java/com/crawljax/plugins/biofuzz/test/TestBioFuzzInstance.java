package com.crawljax.plugins.biofuzz.test;

import java.util.List;
import java.util.Vector;
import java.util.concurrent.CyclicBarrier;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.crawljax.plugins.biofuzz.configuration.ElemataInputSpec;
import com.crawljax.plugins.biofuzz.configuration.FaqForgeInputSpec;
import com.crawljax.plugins.biofuzz.configuration.GeccBBInputSpec;
import com.crawljax.plugins.biofuzz.configuration.PHPAddressbookInputSpec;
import com.crawljax.plugins.biofuzz.configuration.SchoolMateInputSpec;
import com.crawljax.plugins.biofuzz.configuration.WebChessArdillaInputSpec;
import com.crawljax.plugins.biofuzz.core.BioFuzzFitness.BioFuzzFitnessScalar;
import com.crawljax.plugins.biofuzz.core.BioFuzzInstance;
import com.crawljax.plugins.biofuzz.core.BioFuzzPlugin;
import com.crawljax.plugins.biofuzz.core.BioFuzzPluginConfig;
import com.crawljax.plugins.biofuzz.core.BioFuzzThreadPool;
import com.crawljax.plugins.biofuzz.input.BioFuzzFieldInput;
import com.crawljax.plugins.biofuzz.input.BioFuzzFieldInputSequence;
import com.crawljax.plugins.biofuzz.input.BioFuzzFieldInput.BioFuzzAction;
import com.crawljax.plugins.biofuzz.proxy.BioFuzzProxy;
import com.crawljax.plugins.biofuzz.proxy.BioFuzzProxyMgr;
import com.crawljax.plugins.biofuzz.proxy.buffer.BioFuzzParamFilter;
import com.crawljax.plugins.biofuzz.core.topology.BioFuzzWorld;



public class TestBioFuzzInstance {
	
	private static final Logger log = LoggerFactory
			.getLogger(TestBioFuzzInstance.class);
	
	@Test
	public void test1() {
		
		WebChessArdillaInputSpec ispec = new WebChessArdillaInputSpec();
		
		BioFuzzPluginConfig bfCfg = new BioFuzzPluginConfig();
		
		bfCfg.setTribes(1);
		bfCfg.setMaxIter(2);
		bfCfg.setCrossoverCycle(2);
		bfCfg.setCrossoverCnt(2);
		bfCfg.setCrossoverOffset(5);
		bfCfg.setParserMatchMax(2);
		bfCfg.setFscal(new BioFuzzFitnessScalar(1,2,2,3));

		
		// Create an initial proxy
		//BioFuzzProxyMgr pMgr = BioFuzzProxyMgr.getInstance("127.0.0.1", 8084);

		//BioFuzzProxy proxy = pMgr.createAndGetProxy();
		//proxy.setFilter(new BioFuzzParamFilter());
		
		bfCfg.setFscal(new BioFuzzFitnessScalar(1,2,2,3));

		BioFuzzPlugin plugin = new BioFuzzPlugin(ispec,bfCfg);
		
		//List<BioFuzzFieldInputSequence> liseq = ispec.getInputSequences();
		
		BioFuzzWorld world = new BioFuzzWorld(new BioFuzzFitnessScalar(1,1,1,1));
		
		BioFuzzThreadPool pool = BioFuzzThreadPool.getInstance();

		BioFuzzInstance inst0 = new BioFuzzInstance("http://localhost/webchess",ispec.getInputSequences().get(0),ispec,world,bfCfg);

	
		inst0.phase0();
		inst0.phase1();
		
		log.info("Phase 2 ===================");
		
		inst0.phase2();
		
		//pool.registerThread(inst0);

		//proxy.startRec();
		
		//try {
		//	pool.submitAll();
		//} catch (Exception e) {
			// TODO Auto-generated catch block
		//	e.printStackTrace();
		//}
		//proxy.stopRec();
		
	}
}
