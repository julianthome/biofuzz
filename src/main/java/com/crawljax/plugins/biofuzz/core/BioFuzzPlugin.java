package com.crawljax.plugins.biofuzz.core;


import java.util.List;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.crawljax.core.CrawlSession;
import com.crawljax.core.CrawlerContext;
import com.crawljax.core.ExitNotifier.ExitStatus;
import com.crawljax.core.configuration.CrawljaxConfiguration;
import com.crawljax.core.plugin.OnNewStatePlugin;
import com.crawljax.core.plugin.PostCrawlingPlugin;
import com.crawljax.core.plugin.PreCrawlingPlugin;
import com.crawljax.core.state.Eventable;
import com.crawljax.core.state.StateFlowGraph;
import com.crawljax.core.state.StateVertex;

import com.crawljax.plugins.biofuzz.core.components.BioFuzzLogger;
import com.crawljax.plugins.biofuzz.core.topology.BioFuzzWorld;
import com.crawljax.plugins.biofuzz.input.BioFuzzFieldInputSequence;
import com.crawljax.plugins.biofuzz.input.BioFuzzInputSpecIface;
import com.crawljax.plugins.biofuzz.proxy.BioFuzzProxy;
import com.crawljax.plugins.biofuzz.proxy.BioFuzzProxyMgr;
import com.crawljax.plugins.biofuzz.proxy.buffer.BioFuzzParamFilter;
import com.crawljax.plugins.biofuzz.utils.BioFuzzFileLogger;
import com.google.common.collect.ImmutableSet;

public class BioFuzzPlugin implements OnNewStatePlugin,
PreCrawlingPlugin, PostCrawlingPlugin {

	private static final Logger log = LoggerFactory
			.getLogger(BioFuzzPlugin.class);
	
	private static BioFuzzInputSpecIface ispec = null;
	private static BioFuzzWorld bworld = null;
	private static BioFuzzFileLogger flog = null;
	private static BioFuzzPluginConfig config = null;
	private static final BioFuzzLogger bflog = BioFuzzLogger.getInstance("/tmp/logger.log");
	private static BioFuzzProxyMgr pMgr = null;
	private  BioFuzzProxy proxy = null;

	private static long globStart = System.nanoTime();


	public BioFuzzPlugin(BioFuzzInputSpecIface nispec, BioFuzzPluginConfig conf) {
		
		ispec = nispec;
		bworld = new BioFuzzWorld(conf.getFscal());	


		//buttonSet = new HashSet<String>();
		flog = new BioFuzzFileLogger("/tmp/biofuzz/", "main.log");
		pMgr = BioFuzzProxyMgr.getInstance("127.0.0.1", 8084);

		config = conf;
		
		assert(pMgr != null);
		this.proxy = pMgr.createAndGetProxy();
		this.proxy.setFilter(new BioFuzzParamFilter());
		assert(proxy != null);
		logMsg("start");
	
	}

	
	private void logMsg(String s) {
		log.debug(s);
		flog.write(s);
	}
	
	
	@Override
	public void onNewState(CrawlerContext context, StateVertex vertex) {
		
		proxy.stopRec();
		logMsg("On new State triggered ");
		
		ImmutableSet<Eventable> elist = null;
		StateFlowGraph sfg = context.getSession().getStateFlowGraph();
		elist = sfg.getIncomingClickable(vertex);
		
		for(Eventable ev : elist) {
			if(proxy.getRequestBuffer().getSize() > 0) {
				logMsg("Form data was exchanged - create instance for " + ev.getIdentification().toString());
				//bufEvent.put(ev, iceptor.getBufferCopy());
				createBioFuzzInstanceForState(context, ev);
				// OK we can start a BioFuzzInstance
			}
		}
		
		proxy.startRec();
	}


	private void createBioFuzzInstanceForState(CrawlerContext context, Eventable transition) {


		BioFuzzInstance bsr = null;

		if ( transition != null ) {
			bsr = new BioFuzzInstance(context, transition, ispec, bworld, config);
		} else {
			bsr = new BioFuzzInstance(context, ispec, bworld, config);
		}
	
		if(bsr != null)
			BioFuzzThreadPool.getInstance().registerThread(bsr);
	}
	
	private void createBioFuzzInstancesForInputSeq(String url) {

		List<BioFuzzFieldInputSequence> isec = null;
		
		if((isec = ispec.getInputSequences()) == null || isec.size() <= 0)
			return;
		
		
		BioFuzzInstance bsr = null;

		for(BioFuzzFieldInputSequence seq : isec) {
			bsr = new BioFuzzInstance(url, seq, ispec, bworld, config);
		
		
			if(bsr != null) {
				BioFuzzThreadPool.getInstance().registerThread(bsr);
			}
		}
	}



	@Override
	public void preCrawling(CrawljaxConfiguration config)
			throws RuntimeException {

		proxy.startRec();
		// reset logger
		bflog.reset();
	}

	@Override
	public void postCrawling(CrawlSession session, ExitStatus exitReason) {
		
		
		logMsg(this.proxy.getRequestBuffer().toString());
		
		createBioFuzzInstancesForInputSeq(session.getConfig().getUrl().toString());
		
		BioFuzzLogger bflog = BioFuzzLogger.getInstance("/tmp/logger.log");
		bflog.reset();
		
		long start = System.nanoTime();
		
		if(BioFuzzThreadPool.getInstance().submitAll() == false) {
			logMsg("Error submitting all BioFuzz instances");
		}
		
		long end = System.nanoTime();
		
		// compute the global fitness
		bworld.computeFitness();
		
		logMsg(bworld.toString());
		
		long phase2Time = TimeUnit.SECONDS.convert(end-start, TimeUnit.NANOSECONDS);
		long overallTime = TimeUnit.SECONDS.convert(end-globStart, TimeUnit.NANOSECONDS);
		
		int states = session.getStateFlowGraph().getNumberOfStates();
		
		logMsg("Phase 2 Elapsed Time in seconds: " + phase2Time);
		logMsg("Overall Time in seconds: " + overallTime);
		logMsg("# Crawljax states: " + states);
		logMsg("# Instances started: " + BioFuzzThreadPool.getInstance().getInstanceCnt());
		
		
		
		this.proxy.stop();
	}
	
	public BioFuzzProxy getProxy() {
		return this.proxy;
	}
	
	public void startProxy() {
		this.proxy.start();
	}
	
	
	public void stopProxy() {
		
		this.proxy.stop();
	}

		

}
