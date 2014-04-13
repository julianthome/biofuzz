package com.crawljax.plugins.biofuzz.test;

import java.util.List;
import java.util.Vector;
import java.util.concurrent.CyclicBarrier;

import org.junit.Test;

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

	//@Test
	public void test0() {
		
		SchoolMateInputSpec ispec = new SchoolMateInputSpec();
		
		BioFuzzPluginConfig bfCfg = new BioFuzzPluginConfig();
		
		bfCfg.setTribes(3);
		bfCfg.setMaxIter(20);
		bfCfg.setCrossoverCycle(2);
		bfCfg.setCrossoverCnt(2);
		bfCfg.setCrossoverOffset(5);
		bfCfg.setParserMatchMax(2);
		
		// Create an initial proxy
		BioFuzzProxyMgr pMgr = BioFuzzProxyMgr.getInstance("127.0.0.1", 8084);

		BioFuzzProxy proxy = pMgr.createAndGetProxy();
		proxy.setFilter(new BioFuzzParamFilter());
		
		bfCfg.setFscal(new BioFuzzFitnessScalar(1,1,1,1));

		BioFuzzPlugin plugin = new BioFuzzPlugin(ispec,bfCfg);
		
		List<BioFuzzFieldInputSequence> liseq = ispec.getInputSequences();
		
		BioFuzzWorld world = new BioFuzzWorld(new BioFuzzFitnessScalar(1,1,1,1));
		
		BioFuzzThreadPool pool = BioFuzzThreadPool.getInstance();

		BioFuzzInstance inst0 = new BioFuzzInstance("http://localhost/schoolmate",liseq.get(0),ispec,world,bfCfg);
		BioFuzzInstance inst1 = new BioFuzzInstance("http://localhost/schoolmate",liseq.get(1),ispec,world,bfCfg);
		BioFuzzInstance inst2 = new BioFuzzInstance("http://localhost/schoolmate",liseq.get(2),ispec,world,bfCfg);
		BioFuzzInstance inst3 = new BioFuzzInstance("http://localhost/schoolmate",liseq.get(3),ispec,world,bfCfg);
		
		pool.registerThread(inst0);
		pool.registerThread(inst1);
		pool.registerThread(inst2);		
		pool.registerThread(inst3);
		//pool.registerThread(inst0);
//		pool.registerThread(inst1);
//		pool.registerThread(inst2);

		
		try {
			pool.submitAll();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	
	@Test
	public void test1() {
		
		ElemataInputSpec ispec = new ElemataInputSpec();
		
		BioFuzzPluginConfig bfCfg = new BioFuzzPluginConfig();
		
		bfCfg.setTribes(10);
		bfCfg.setMaxIter(20);
		bfCfg.setCrossoverCycle(2);
		bfCfg.setCrossoverCnt(2);
		bfCfg.setCrossoverOffset(5);
		bfCfg.setParserMatchMax(2);
		bfCfg.setFscal(new BioFuzzFitnessScalar(1,2,2,3));

		
		// Create an initial proxy
		BioFuzzProxyMgr pMgr = BioFuzzProxyMgr.getInstance("127.0.0.1", 8084);

		BioFuzzProxy proxy = pMgr.createAndGetProxy();
		proxy.setFilter(new BioFuzzParamFilter());
		
		bfCfg.setFscal(new BioFuzzFitnessScalar(1,2,2,3));

		BioFuzzPlugin plugin = new BioFuzzPlugin(ispec,bfCfg);
		
		List<BioFuzzFieldInputSequence> liseq = ispec.getInputSequences();
		
		BioFuzzWorld world = new BioFuzzWorld(new BioFuzzFitnessScalar(1,1,1,1));
		
		BioFuzzThreadPool pool = BioFuzzThreadPool.getInstance();

		BioFuzzInstance inst0 = new BioFuzzInstance("http://localhost/elemata/login.php",ispec.getInputSequences().get(0),ispec,world,bfCfg);

		
		pool.registerThread(inst0);

		proxy.startRec();
		
		try {
			pool.submitAll();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		proxy.stopRec();
		
	}
}
