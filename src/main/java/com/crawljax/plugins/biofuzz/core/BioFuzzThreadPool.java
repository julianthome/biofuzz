package com.crawljax.plugins.biofuzz.core;


import java.util.List;
import java.util.Vector;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.crawljax.plugins.biofuzz.utils.BioFuzzFileLogger;


public class BioFuzzThreadPool {

	private static List<BioFuzzInstance> threadQueue0 = null;
	private static List<BioFuzzInstance> threadQueue1 = null;
	private static List<BioFuzzInstance> threadQueue2 = null;
	private static BioFuzzThreadPool pool = null;
	
	private static BioFuzzFileLogger flog = new BioFuzzFileLogger("/tmp/biofuzz/","tpool.log");
	
	private void logMsg(String s) {
		this.flog.write(s);
		logger.debug(s);
	}
	
	private static final Logger logger = LoggerFactory
			.getLogger(BioFuzzInstance.class);

	
	private BioFuzzThreadPool() {

		threadQueue0 = new Vector<BioFuzzInstance>();
		threadQueue1 = new Vector<BioFuzzInstance>();
		threadQueue2 = new Vector<BioFuzzInstance>();
	}
	

	public static BioFuzzThreadPool getInstance() {
		if (pool == null) {
			pool = new BioFuzzThreadPool();
		} 
		return pool;
	};
	
	public void registerThread(BioFuzzInstance thread) {
		threadQueue0.add(thread);
	}
	
	public int getInstanceCnt() {
		return threadQueue0.size();
	}
	
	public boolean submitAll() {
		logger.debug("submit all");
		// Execute the first phase
		for(int i = 0; i < threadQueue0.size(); i++) {
			BioFuzzInstance inst = threadQueue0.get(i);
			boolean ret = false;
			if(ret = inst.phase0()) {
				logMsg("Phase 0 for instance " + i + " successful");
				threadQueue1.add(inst);
			} else {
				logMsg("Phase 0 for instance " + i + " was not successful");
			}
			
		}

		// Execution of phase 1
		for(int i = 0; i < threadQueue1.size(); i++) {
			BioFuzzInstance inst = threadQueue1.get(i);
			if(inst.phase1()) {
				logMsg("Phase 1 for instance " + i + " successful");
				threadQueue2.add(inst);
			} else {
				logMsg("Phase 1 for instance " + i + " was not successful");
				threadQueue2.add(inst);
			}
		}
	
		// Execution of phase 2
		for(int i = 0; i < threadQueue2.size(); i++) {
			BioFuzzInstance inst = threadQueue2.get(i);
			if(inst.phase2()) {
				logMsg("Phase 2 for instance " + i + " successful");
			} else {
				logMsg("Phase 2 for instance " + i + " was not successful");
			}
		}
		
		
		return true;
	}
	
	
}