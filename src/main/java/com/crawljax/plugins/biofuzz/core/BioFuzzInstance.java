package com.crawljax.plugins.biofuzz.core;

import com.crawljax.browser.EmbeddedBrowser;
import com.crawljax.core.CrawlerContext;
import com.crawljax.core.state.Eventable;
import com.crawljax.core.state.StateFlowGraph;
import com.crawljax.core.state.StateVertex;
import com.crawljax.forms.FormInput;
import com.crawljax.forms.InputValue;
import com.crawljax.plugins.biofuzz.core.BioFuzzFitness.BioFuzzFitnessScalar;
import com.crawljax.plugins.biofuzz.core.BioFuzzFitness.FitnessCriterion;
import com.crawljax.plugins.biofuzz.core.components.BioFuzzAutomation;
import com.crawljax.plugins.biofuzz.core.components.BioFuzzLogger;
import com.crawljax.plugins.biofuzz.core.topology.BioFuzzIndividual;
import com.crawljax.plugins.biofuzz.core.topology.BioFuzzIndividual.State;
import com.crawljax.plugins.biofuzz.core.topology.BioFuzzPopulation;
import com.crawljax.plugins.biofuzz.core.topology.BioFuzzTribe;
import com.crawljax.plugins.biofuzz.core.topology.BioFuzzWorld;
import com.crawljax.plugins.biofuzz.input.*;
import com.crawljax.plugins.biofuzz.proxy.BioFuzzClient;
import com.crawljax.plugins.biofuzz.proxy.BioFuzzProxy;
import com.crawljax.plugins.biofuzz.proxy.BioFuzzProxyMgr;
import com.crawljax.plugins.biofuzz.proxy.BioFuzzProxyMgr.BioFuzzCSPair;
import com.crawljax.plugins.biofuzz.proxy.buffer.BioFuzzConvBuffer;
import com.crawljax.plugins.biofuzz.proxy.buffer.BioFuzzConvPair;
import com.crawljax.plugins.biofuzz.proxy.buffer.BioFuzzParamFilter;
import com.crawljax.plugins.biofuzz.utils.BioFuzzFileLogger;
import com.crawljax.plugins.biofuzz.utils.BioFuzzUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.owasp.webscarab.model.Request;
import org.owasp.webscarab.model.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.Set;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class BioFuzzInstance {


	private static final Logger log = LoggerFactory
			.getLogger(BioFuzzInstance.class);

	private static final BioFuzzLogger bflog = BioFuzzLogger.getInstance("/tmp/logger.log");




	private StateFlowGraph sfg = null;
	private List<Eventable> evl = null;
	private StateVertex target = null;
	private String url = null;

	private BioFuzzFileLogger flog = null;
	private Eventable transition = null;
	private CrawlerContext context = null; 
	private BioFuzzAutomation bauto = null;
 

	private BioFuzzInputSpecIface ispec = null;
	private BioFuzzWorld bworld = null;
	private BioFuzzPopulation bpop = null;


	private int maxIter = 0;
	private int tribeCnt = 0;
	private int crossoverCycle = 0;
	private int crossoverCount = 0;
	private int crossoverOffset = 0;
	private int parserMatchMax = 0;
	private boolean applyCrossover = false;
	private boolean applyMutation = false;

	private BioFuzzFieldInputSequence iseq = null;


	private BioFuzzFitnessScalar fvect = null;


	private BioFuzzCSPair cpair = null;
	private BioFuzzClient client = null;
	private BioFuzzProxy proxy = null;

	private BioFuzzBrowserMgr bmgr = null;

	private List<BioFuzzProtocolInput> initialInputs = null;


	private BioFuzzConvBuffer replayBuf = null;

	private List<BioFuzzTribe> tribes = null;

	private BioFuzzContentHandler contHandler = null;


	public BioFuzzInstance(CrawlerContext context,
			Eventable transition, BioFuzzInputSpecIface ispec, BioFuzzWorld world, BioFuzzPluginConfig conf) {

		this(context,ispec,world, conf);

		this.transition = transition;
		this.sfg = context.getSession().getStateFlowGraph();
		this.target = transition.getTargetStateVertex();
		this.evl = sfg.getShortestPath(context.getSession().getInitialState(), this.target);
	}

	public BioFuzzInstance(CrawlerContext context,
			BioFuzzInputSpecIface ispec, BioFuzzWorld world, BioFuzzPluginConfig conf) {
		System.setProperty("webdriver.gecko.driver",
				"/home/biofuzz/files/geckodriver");
		this.flog = new BioFuzzFileLogger("/tmp/biofuzz/",RandomStringUtils.randomAlphanumeric(5).toUpperCase());

		this.bauto = new BioFuzzAutomation();
		this.ispec = ispec;
		this.bworld = world;

		this.fvect = this.bworld.getFscal();
		this.bpop = this.bworld.createPopulation();

		assert(this.bpop != null);

		this.sfg = context.getSession().getStateFlowGraph();
		this.target = this.sfg.getInitialState();
		this.evl = sfg.getShortestPath(context.getSession().getInitialState(), this.target);


		this.iseq = null;
		this.transition = null;
		this.context = context;

		this.cpair = BioFuzzProxyMgr.getInstance().createAndGetCSPair();

		assert(this.cpair != null);
		this.proxy = this.cpair.getProxy();
		// only look at POST and GET protocols
		this.proxy.setFilter(new BioFuzzParamFilter());

		assert(this.proxy != null);
		this.client = this.cpair.getClient();

		this.bmgr = new BioFuzzBrowserMgr(context.getConfig().getUrl().toString(), this.ispec, this.proxy);
		this.initialInputs = new Vector<BioFuzzProtocolInput>();

		this.tribes = new Vector<BioFuzzTribe>();
		this.url = null;

		this.maxIter = conf.getMaxIter();
		this.tribeCnt = conf.getTribes();
		this.crossoverCycle = conf.getCrossoverCycle();
		this.crossoverCount = conf.getCrossoverCnt();
		this.crossoverOffset = conf.getCrossoverOffset();
		this.parserMatchMax = conf.getParserMatchMax();
		this.applyCrossover = conf.getApplyCrossover();
		this.applyMutation = conf.getApplyMutation();

	}


	public BioFuzzInstance(String url, BioFuzzFieldInputSequence iseq,
			BioFuzzInputSpecIface ispec, BioFuzzWorld world, BioFuzzPluginConfig conf) {

		System.setProperty("webdriver.gecko.driver",
				"/home/biofuzz/files/geckodriver");
		this.iseq = iseq;
		this.sfg = null;
		this.bauto = new BioFuzzAutomation();		

		this.flog = new BioFuzzFileLogger("/tmp/biofuzz/",RandomStringUtils.randomAlphanumeric(5).toUpperCase());

		//this.initialInputs = new Vector<Input>();
		this.ispec = ispec;
		this.bworld = world;


		this.bpop = this.bworld.createPopulation();

		assert(this.bpop != null);

		this.cpair = BioFuzzProxyMgr.getInstance().createAndGetCSPair();

		assert(this.cpair != null);
		this.proxy = this.cpair.getProxy();
		// only look at POST and GET protocols
		this.proxy.setFilter(new BioFuzzParamFilter());

		assert(this.proxy != null);
		this.client = this.cpair.getClient();

		this.fvect = world.getFscal();

		this.bmgr = new BioFuzzBrowserMgr(url, this.ispec, this.proxy);
		this.initialInputs = new Vector<BioFuzzProtocolInput>();
		this.tribes = new Vector<BioFuzzTribe>();

		this.url = url;

		this.maxIter = conf.getMaxIter();
		this.tribeCnt = conf.getTribes();
		this.crossoverCycle = conf.getCrossoverCycle();
		this.crossoverCount = conf.getCrossoverCnt();
		this.crossoverOffset = conf.getCrossoverOffset();
		this.parserMatchMax = conf.getParserMatchMax();
		this.applyCrossover = conf.getApplyCrossover();
		this.applyMutation = conf.getApplyMutation();
	}


	public BioFuzzFitnessScalar getFvect() {
		return this.fvect;
	}


	private void analyzeLog() {

		String res = null;

		res = bflog.cut();

		if(res != null && res.length() > 0) {

			this.bworld.analyzeLog(res.split("\n"));

		}
	}

	private boolean goToTargetStateWithBrowser() {

		// both of these functions are requiring sync since they
		// are triggering SQL statements

		logMsg("go to target state with browser");
		boolean ret = false;
		EmbeddedBrowser b = this.bmgr.getBrowser();

		assert this.proxy != null;
		
		this.proxy.startRec();
		if(this.sfg == null && this.iseq != null) {
			ret = goToStartStateIseq(b);
		} else if (this.sfg != null) {
			ret = goToTargetStateSfg(b);
		}
		this.proxy.stopRec();
		b.close();

		return ret;
	}

	private boolean goToStartStateIseq(EmbeddedBrowser b) {
		assert(b != null);

		logMsg("go to start state Iseq");

		// Keep lock ... the logger is logging while we are replaying stuff

		for(int i = 0; i < this.iseq.getSize(); i++) {
			BioFuzzFieldInput in = this.iseq.get(i);
			if(this.bauto.executeInput(b, in) == false) {
				logMsg("Cannot execute input " + in.toString() + " ... abort");
				return false;
			}

			analyzeLog();

		}
		return true;
	}

	private boolean goToTargetStateSfg(EmbeddedBrowser b) {
		assert(b != null);
		logMsg("go to start state Sfg");

		// First go to URL and then fire all events
		try {
			URL url = null;

			if (transition == null) {
				url = context.getConfig().getUrl();
				logMsg("go to url (no transition): " + url.toString());
			} else {

				url = new URL(this.sfg.getInitialState().getUrl());
				logMsg("go to url: " + url.toString());
			}
			b.goToUrl(url);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			return false;
		}
		for (Eventable ev: this.evl) {
			logMsg("Replay Event: " + ev.getId());
			try {
				for(FormInput input : ev.getRelatedFormInputs()) {
					for (InputValue tup : input.getInputValues()) {

						if(b.input(input.getIdentification(), tup.getValue()) == false) {
							logMsg("Couldn't execute input " + tup.getValue() + " in " + input.getIdentification().toString());
							return false;
						}
					}
				}
				if(b.fireEventAndWait(ev) == false) {
					logMsg("There went something wrong");
					return false;
				}

				analyzeLog();

			} catch (Exception e) {
				logMsg("Cannot Execute chain in browser: " + e.getMessage());
				b.close();
				this.proxy.stopRec();
				return false;
			}
		}

		return true;
	}


	private void logMsg(String s) {
		log.debug(s);
		flog.write(s);
	}

	private synchronized String[] submitAndRead(Request req) {
		String stmts = "";

		
		bflog.reset();

		this.proxy.startRec();
		Response res = null;
		try {
			res = this.client.fetchResponse(req);
		} catch (IOException e) {
			logMsg("Something went wrong trying to execute request" + e.getMessage());
			proxy.stopRec();
			return null;
		}

		this.proxy.stopRec();

		assert(this.proxy.getRequestBuffer().getSize() >= 0);

		if(this.proxy.getRequestBuffer().getSize() <= 0) {
			logMsg("Request buffer empty");
			return null;
		}

		logMsg("reading start");
		stmts = bflog.cut();
		logMsg("reading end");

		
		logMsg("got stmtms " + stmts);
		if(stmts == null) {
			logMsg("shouldn't happen");
			return null;
		}

		if (stmts.length() > 0){
			return stmts.split("\n");
		} else {
			logMsg("length is not ok");
			return null;
		}
		//}

	}


	private String getLatestResponseContent() {

		assert(this.proxy != null);

		BioFuzzConvBuffer buf = proxy.getRequestBufferCopy();

		assert(buf != null);

		if(buf == null) {
			logMsg("Buffer is null");
			return null;
		}

		BioFuzzConvPair cpair = buf.getLatest();

		assert(cpair != null);

		if(cpair == null) {
			logMsg("last cpair is null");
			return null;
		}

		Response resp = cpair.getResponse();

		assert(resp != null);

		if(resp == null) {
			logMsg("resonse is null");
			return null;
		}

		byte[] content = resp.getContent();

		assert(content != null);

		if(content == null) {
			logMsg("cannot extract content");
			return null;
		}

		return new String(content);

	}


	private void measureDistance(BioFuzzIndividual ind) {

		String exp = ind.getQuery();
		exp = exp.replace(ind.getId(),ind.getSuffix());


		ind.setFitness(FitnessCriterion.LOGGER_LDIST, 0.0);

	}

	private boolean measureDistance(BioFuzzIndividual ind, String orig, int iter) {

		boolean match = false;

		String sdbRet = orig.replaceFirst("([0-9]+) .*","$1");
		int dbRet = Integer.valueOf(sdbRet);

		logMsg("Database return code is " + dbRet);

		orig = orig.replaceAll("[0-9]+ (.*)", "$1");

		//String exp = ind.getQuery();

		String exp = ind.getPatQuery();
		logMsg("exp is : " + exp);
		logMsg("orig is: " + orig);

		Pattern pat = Pattern.compile(exp,Pattern.MULTILINE);
		Matcher valMat = pat.matcher(orig);

		if(valMat.find()) {

			// first inner group is the injected value
			String injAfter = valMat.group(1);
			String injBefore = ind.getSuffix();
			match = true;

			logMsg("found stmt: " + orig);

			int ldist = BioFuzzUtils.getStrDist(injBefore, injAfter);

			double lfit = 1.0/(double)(1.0+ldist);


			ind.setFitness(FitnessCriterion.LOGGER_LDIST, lfit);

			if(lfit == 1.0 && dbRet == 0) {
				this.bpop.checkForCheckpoints(ind, iter);
				ind.setFitness(FitnessCriterion.CHECKPOINT_CNT, ind.getCPAvg());
			}

		} else {
			logMsg("nothing found");
			ind.setFitness(FitnessCriterion.LOGGER_LDIST, 0.0);
		}

		return match;			
	}

	private boolean evalIndividual(BioFuzzIndividual ind, String [] sqlOut, int iter) {

		logMsg("eval individual \n");

		if(sqlOut == null || sqlOut.length <= 0) {
			logMsg("Couldn't read anything from logger");
			// special distance measure useful if nothing is logged
			measureDistance(ind);
			return true;
		}

		logMsg("get normalized Dom");
		String newDom = getLatestResponseContent();


		if(newDom == null) {
			logMsg("couldn't obtain latest response content");
			return false;
		} else {
			newDom =  BioFuzzUtils.getNormalizedDom(newDom);
			logMsg("got normalized dom");
			//logMsg("newDom: " + newDom);
		}


		double dfit = BioFuzzUtils.getNormalziedStrDist(ind.getExpectedDom(),newDom);

		ind.setFitness(FitnessCriterion.DOM_LDIST, dfit);

		updateNC(ind, iter);

		// Associate a query with the given input
		boolean found = false;
	
		
		for (String stmt : sqlOut) {
			logMsg("measure dist " + stmt);
			if(measureDistance(ind, stmt, iter) == true) {
				found = true;
				break;
			}
		}



		if(!found)
			logMsg("nothing found");

		return found;
	}

	private void updateNC(BioFuzzIndividual ind, int iter) {
		double nc = (ind.getTermCnt() * 1.0)/(iter * 1.0);
		ind.setFitness(FitnessCriterion.NODE_CNT, nc);
	}
	
	private boolean goToTargetStateWithClient(BioFuzzClient c) {
		assert(this.replayBuf != null);
		assert(this.replayBuf.getSize() > 0);

		logMsg("got to target state with HTTP Client");
		logMsg("buf lenght is " + this.replayBuf.getSize());

		assert(proxy.isRunning());

		if(this.replayBuf.getSize() > 1) {
			if(c.replay(this.replayBuf, 0, this.replayBuf.getSize()-2) != true) {
				logMsg("Cannot Replay buffer");
				return false;
			}
		} else {
			logMsg("Nothing to replay ... buffer size is 1");
		}

		return true;
	}

	private String [] runAttack(BioFuzzClient c, BioFuzzContentHandler chandler) {
		String [] sqlOut = null;
		logMsg("run attack");
		//synchronized(bflogLock) {

		// this function requires sync - since it triggers db queries
		if(goToTargetStateWithClient(c) != true) {
			logMsg("Client cannot go to target state");
			return null;
		}

		// IMPORTANT: just use the replay buffer here
		BioFuzzConvPair conv = this.replayBuf.getLatest();

		assert(conv != null);
		if(conv == null) {
			return null;
		}
		Request last = conv.getRequest();

		assert(last != null);
		if(last == null) {
			return null;
		}

		Request newReq = BioFuzzUtils.fuzzAndGetRequest(last, chandler.getContent());
		if(newReq != null) {
			logMsg("new Request is " + newReq.toString());

			logMsg("Execute request and read log");

			// this function requires sync - since it triggers db queries
			sqlOut = submitAndRead(newReq);
		}
		//}
		return sqlOut;

	}

	private boolean genInitialInputs() {

		assert(this.replayBuf != null);

		if(this.replayBuf == null) {
			logMsg("couldn't create replay buffer");
			return false;
		}

		if(this.replayBuf.getSize() <= 0) {
			logMsg("replay buffer is empty");
			return false;
		}

		// get the latest request
		BioFuzzConvPair cpair = this.replayBuf.getLatest();

		logMsg("Take latest cpair");
		Request req = cpair.getRequest();
		assert(req != null);

		// create initial content handler
		if(req != null) {
			logMsg("Create and get Content");
			// Transform request into content object (we are just interested in the Content field or the URL)
			this.contHandler = new BioFuzzContentHandler();
			this.contHandler.addContentFromRequest(req);
		} 

		assert(this.contHandler != null);
		if(contHandler == null) {
			logMsg("Couldn't create biofuzz content");
			return false;
		}

		for(int i = 0; i < this.contHandler.getSize(); i++) {
			BioFuzzProtocolInput in = new BioFuzzProtocolInput(contHandler.get(i),
					RandomStringUtils.randomNumeric(6));
			this.initialInputs.add(in);
		}

		assert(this.bpop != null);
		assert(this.tribes != null);

		return (this.initialInputs.size() > 0);

	}

	public boolean getFieldAssoc(BioFuzzTribe tribe, boolean constParams) {
		String[] sqlOut = null;
		List<String> queries = new Vector<String>();
		boolean ret = false;
		boolean checked = false;
		for(BioFuzzProtocolInput in : this.initialInputs) {
			queries.clear();
			logMsg("Input: " + in.toString());
			// move input value into content (content is managed by the global content handler)
			in.submit();
			// use global content handler here - tribes will get a copy
			if((sqlOut = runAttack(this.client, this.contHandler)) != null) {


				if(!checked ){
					this.bworld.analyzeLog(sqlOut);
					checked = true;
				}


				logMsg("Found input: " + in.getInputValue() + " as db input");

				for(String stmt : sqlOut) {
					logMsg("STMT ---"  + stmt);
					if(stmt.contains(in.getInputValue())) {
						String sstmt = stmt.replaceAll("[0-9]+ (.*)", "$1");
						queries.add(sstmt);
						logMsg(">>>> stmt:" + sstmt);
					}
				}
			} 

			if(queries.size() > 0 ) {
				logMsg("create individual for " + in.getInputValue());

				// hightlight input as influencial on the output 

				if(constParams == true && in.isInjectable() == true) {
					logMsg("constParams ... no impact");
					continue;
				}

				in.setInjectable();

				String cont = getLatestResponseContent();
				cont = BioFuzzUtils.getNormalizedDom(cont);
				if(cont == null) {
					logMsg("couldn't get latest content - don't create individual");
					continue;
				}


				if(tribe.createIndividual(in, queries, cont, this.parserMatchMax) == true) {
					ret = true;

					logMsg("Individual created");
				} else {
					logMsg("Was not able to create individual - check your grammar");
				}
			}

			// rollback in.submit()
			in.rollback();
		}
		return ret;
	}

	public boolean doGetFieldAssoc() {

		assert(this.client != null);
		assert(this.initialInputs != null && this.initialInputs.size() > 0);
		boolean ret = false;
		logMsg("run Attack");

		for(int ind = 0; ind < this.tribeCnt; ind++) {

			BioFuzzTribe tribe = this.bpop.createAndGetTribe(this.contHandler);

			ret = getFieldAssoc(tribe, false);
		}

		return ret;
	}


	private void learnGrammar() {
		BioFuzzDBSchema schema = null;
		schema = this.bworld.getDBSchema();
		this.bpop.addDBInfo(schema);
	}
	
	
	private boolean runAttackForIndividual(BioFuzzIndividual ind, int iter) {
		// submit new suffix to the content
		logMsg("submit");
		ind.submit();

		String[] sqlOut = runAttack(this.client, ind.getTribe().getContentHandler());

		BioFuzzConvBuffer buf = this.proxy.getRequestBufferCopy();

		assert(buf != null);

		if(buf.getSize() <= 0) {
			logMsg("Buffer is empty after running attack in phase 2 - shouldn't happen");
			return false;
		}

		// evaluate Individual
		if(evalIndividual(ind, sqlOut, iter) == false) {
			logMsg("Couldn't evaluate individual " + ind.getId());
			return false;
		}

		ind.rollback();
		
		this.bpop.checkIndividualState(ind);
		return true;
	}



	public boolean phase0() {

		if(this.transition != null && this.target != null) {
			logMsg("Transition: " + this.transition.getIdentification().toString());
			logMsg("Target: " + this.target.getUrl());
		}

		if(this.url != null) {
			logMsg("URL: " + this.url);
		}

		if( goToTargetStateWithBrowser() == false ) {
			logMsg("couldn't reach target state ... abort");
			return false;
		} else {
			logMsg("target state reached");
		}

		// store deep copy of captured information in this variable
		this.replayBuf = this.proxy.getRequestBufferCopy();

		logMsg("Iceptor");
		logMsg(this.replayBuf.toString());

		if(this.replayBuf.getSize() <= 0) {
			logMsg("No Requests have been recorded ... nothing to do anymore");
			logMsg("finished !!");
			return false;
		}

		logMsg("Individual\n");

		if(genInitialInputs() == false) {
			logMsg("Couldn't generate initial inputs ... return");
			return false;
		}

		if(this.contHandler == null) {
			logMsg("Something went wrong trying to generate Content datastructure to keep protocol parameters");
			return false;
		}

		// create intitial individuals
		if(doGetFieldAssoc() == false) {
			logMsg("no field association detected");
			return false;
		}

		this.bworld.addParams(this.contHandler.getAllParams());

		return true;

	}

	public boolean phase1() {



		if(this.contHandler == null) {
			logMsg("No content handler in last phase created - abort");
			return false;
		}

		Set<BioFuzzParamTuple> res = null;

		assert(this.bworld != null);


		res = this.bworld.getDiffParams(this.contHandler.getAllParams());

		if(res != null && !res.isEmpty()) {
			logMsg("Determine impact of const fields");

			int start = this.contHandler.getSize();

			this.contHandler.addFields(res);

			// add new input
			for(int i = start; i < this.contHandler.getSize(); i++) {
				BioFuzzProtocolInput in = new BioFuzzProtocolInput(contHandler.get(i),
						RandomStringUtils.randomNumeric(6));
				this.initialInputs.add(in);
			}

			logMsg("new const handler : " + this.contHandler.toString());

			// one additional tribe containing all optional parameters
			BioFuzzTribe tribe = this.bpop.createAndGetTribe(this.contHandler);

			if(getFieldAssoc(tribe, true) == false) {
				logMsg("no const field impact detected");
				return false;
			} else {
				logMsg("const field impact detected");
			}
		}

		// learn new grammar information - just important for generation
		learnGrammar();

		logMsg("Popsize 2: " + this.bpop.getTribeCnt());
		return true;
	}


	public boolean phase2() {

		for(int iter = 1; iter < this.maxIter; iter ++) {

			for(int k = 0; k < this.bpop.getSize(); k++) {

				//BioFuzzTribe tribe = null;
				BioFuzzIndividual ind = this.bpop.getIndividualByIdx(k);
				
				if(ind.getState() == State.MATURE) {
					updateNC(ind, iter);
					continue;
				}

				assert(ind != null);
				assert(this.bpop != null);

				if(ind.getState() == State.IMMATURE) {

					if(ind.getFvalForCriterion(FitnessCriterion.LOGGER_LDIST) == 1.0 ) {
						
						logMsg("Evolve Individual");
						this.bpop.evolveIndividual(ind);

					} else {

						logMsg("Cannot evolve Individual: " + ind.getFvalForCriterion(FitnessCriterion.LOGGER_LDIST));

						if(this.applyMutation == true) {
							logMsg("apply mutation");
							this.bpop.mutateIndividual(ind);
						}
					}
				}

				runAttackForIndividual(ind, iter);
			}


			if(this.applyCrossover) {
				logMsg("apply Crossover");
				List<BioFuzzIndividual> ret = performCrossover(iter);
				if(ret != null && ret.size() > 0) {
					for(BioFuzzIndividual nind : ret) {
						// you have to evaluate the newly created individuals
						runAttackForIndividual(nind, iter);
					}
				}
			}

		}

		logMsg("ComputeFitness");
		this.bpop.computeFitness();

		logMsg(this.bpop.toString());


		logMsg("Fitness computed");
		return true;
	}

	private List<BioFuzzIndividual> performCrossover( int cycle ) {

		
		// rank the whole population
		this.bpop.computeFitness();

		List<BioFuzzIndividual> list = new Vector<BioFuzzIndividual>();
		
		if(cycle >= this.crossoverOffset && this.crossoverCount > 0 && this.crossoverCycle > 0 && 
				(cycle%this.crossoverCycle) == 0) {

			List<BioFuzzIndividual> best = this.bpop.getBest(this.crossoverCount);
			List<BioFuzzIndividual> worst = this.bpop.getWorst(this.crossoverCount);

			if(best == null || worst == null) {
				logMsg("popsize too small");
				return null;
			}

			assert(best.size() == worst.size());

			if(best == null || worst == null || 
					best.size() == 0 || worst.size() == 0
					|| best.size() != worst.size()) {
				logMsg("that should not happen");
				return null;
			}

			BioFuzzIndividual worstInd = null;
			BioFuzzIndividual bestInd = null;
			BioFuzzIndividual res = null;

			assert(worst.size() == best.size());
			for(int i = 0; i <  best.size(); i++) {
				logMsg("new round");
				bestInd = best.get(i);
				assert(bestInd != null);
				worstInd = worst.get(i);
				assert(worstInd != null);

				res = this.bpop.crossOver(worstInd, bestInd);

				if(res == null) {
					logMsg("Couldn't perform crossover");
					continue;
				}

				logMsg("crossover performed --------------------------BEGIN");
				logMsg("a: " + worstInd.toString());
				logMsg("b: " + bestInd.toString());
				logMsg("c: " + res.toString());
				logMsg("crossover performed -------------------------END");

				res.register();
				list.add(res);
				
				res.setState(State.IMMIGRANT);
				worstInd.unregister();
				logMsg("added");


				// be on the safe side
				worstInd = null;
				bestInd = null;
				res = null;
			}


			// On top of that - apply real crossover and create elite individuals
//			if(this.bpop.getIndividualCnt() > 1) {
//				BioFuzzIndividual best0 = this.bpop.getIndividualByIdx(0);
//				BioFuzzIndividual best1 = null;
//				for(int i = 1; i < this.bpop.getIndividualCnt(); i++) {
//					
//					BioFuzzIndividual tmp = this.bpop.getIndividualByIdx(i);
//					
//					if(tmp.getSuffix().equals(best0.getSuffix()) == false ) {
//						best1 = this.bpop.getIndividualByIdx(i);
//						break;
//					}
//				}
//				
//				if(best0 != null && best1 != null) {
//					
//					
//					BioFuzzIndividual child0 = this.bpop.crossOver(best0, best1);
//					BioFuzzIndividual child1 = this.bpop.crossOver(best1, best0);
//					
//					logMsg("crossover performed --------------------------BEGIN");
//					logMsg("best0: " + best0.toString());
//					logMsg("best1: " + best1.toString());
//					logMsg("child0: " + child0.toString());
//					logMsg("child1: " + child0.toString());
//					logMsg("crossover performed -------------------------END");
//					
//					
//					if(child0 != null) {
//						child0.register();
//						child0.setState(State.IMMIGRANT);
//						list.add(child0);
//					}
//					if(child1 != null) {
//						child1.register();
//						child1.setState(State.IMMIGRANT);
//						list.add(child1);
//					}
//				}
//
//			}

			logMsg("finished");


		}

		logMsg("leave");

		return list;
	}

	public List<Eventable> getEvl() {
		return this.evl;
	}


}

