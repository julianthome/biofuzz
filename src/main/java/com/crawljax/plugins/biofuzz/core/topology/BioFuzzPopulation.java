package com.crawljax.plugins.biofuzz.core.topology;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.Vector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import org.biofuzztk.cfg.BioFuzzAttackCfg;
import org.biofuzztk.cfg.BioFuzzAttackCfgMgr;
import org.biofuzztk.cfg.BioFuzzAttackTag;
import org.biofuzztk.cfg.BioFuzzAttackTag.TagType;
import org.biofuzztk.ptree.BioFuzzParseTree;
import org.biofuzztk.ptree.BioFuzzTokLst;
import org.biofuzztk.components.BioFuzzMgr;


import com.crawljax.plugins.biofuzz.core.BioFuzzDBSchema;
import com.crawljax.plugins.biofuzz.core.BioFuzzFitness.BioFuzzFitnessScalar;
import com.crawljax.plugins.biofuzz.core.BioFuzzFitness.FitnessCriterion;
import com.crawljax.plugins.biofuzz.core.topology.BioFuzzIndividual.State;
import com.crawljax.plugins.biofuzz.input.BioFuzzContentHandler;

import com.crawljax.plugins.biofuzz.utils.BioFuzzFileLogger;
import com.crawljax.plugins.biofuzz.utils.BioFuzzUtils;



public class BioFuzzPopulation {
	

	private static final Logger log = LoggerFactory
			.getLogger(BioFuzzPopulation.class);
	private List<BioFuzzTribe> tribes = null;
	private List<BioFuzzIndividual> individuals = null;
	private BioFuzzWorld world = null;
	private BioFuzzMgr mgr = null;
	private BioFuzzFitnessScalar fscal = null;
	private int ntSum = 0;
	@SuppressWarnings("unused")
	private int cpSum = 0;
	private double avgFitness = 0.0;
	private static BioFuzzFileLogger flog = new BioFuzzFileLogger("/tmp/biofuzz/","pop.log");

	// Sorting
	
	
	public BioFuzzPopulation(BioFuzzWorld world) {
		assert(world != null);
		
		this.tribes = new Vector<BioFuzzTribe>();
		this.individuals = new Vector<BioFuzzIndividual>();
		this.world = world;
		// get manager from world in order to perform operations
		//this.mgr = this.world.getMgr();
		// @TODO: find a better solution for that - for now every pop gets its own mgr
		this.mgr = new BioFuzzMgr("cfg.xml");
		this.fscal = this.world.getFscal();
		this.ntSum = 0;
		this.cpSum = 0;

	}

	
	public void addDBInfo(BioFuzzDBSchema schema) {
		
		BioFuzzAttackCfgMgr m = mgr.getAtackCfgMgr();
		assert(m!= null);
		
		BioFuzzAttackCfg tabcfg = m.getAttackCfgByKey("tab_name");
		assert(tabcfg != null);
		
		BioFuzzAttackCfg colcfg = m.getAttackCfgByKey("col_name");
		assert(tabcfg != null);
		
		Set<String> tabs = schema.getTables();
		Set<String> cols = schema.getColumns();
		
		for(String tab : tabs) {
			tabcfg.appendAtag(new BioFuzzAttackTag(null,tab,TagType.TOK_TERMINAL));
		}
		
		for(String col : cols) {
			colcfg.appendAtag(new BioFuzzAttackTag(null,col,TagType.TOK_TERMINAL));
		}
		
		log.debug("Grammar extended");
		
		log.debug(tabcfg.toString());
		log.debug(colcfg.toString());
		
		
	}	
	
	public BioFuzzFitnessScalar getFscal() {
		return this.fscal;
	}
	
	public void addTribe(BioFuzzTribe tribe) {
		this.tribes.add(tribe);
	}
	
	public BioFuzzTribe createAndGetTribe(BioFuzzContentHandler handler) {
	
		BioFuzzTribe tribe = new BioFuzzTribe(this, this.mgr, new BioFuzzContentHandler(handler));
		this.tribes.add(tribe);
		return tribe;
	}
	
	public BioFuzzIndividual getIndividualByIdx(int idx) {
		assert(idx < this.individuals.size());
		
		return this.individuals.get(idx);
	}
	
	public void registerIndividual(BioFuzzIndividual ind) {
		this.individuals.add(ind);
		this.world.registerIndividual(ind);
	}
	
	public void unregisterIndividual(BioFuzzIndividual ind) {
		this.individuals.remove(ind);
		this.world.unregisterIndividual(ind);
	}
	
	public BioFuzzMgr getMgr() {
		return this.mgr;
	}
	
	public int getSize() {
		return this.individuals.size();
	}
	
	public void computeFitness() {
		log.debug("compute Fitness");
		this.avgFitness = 0.0;
		for(BioFuzzIndividual ind : this.individuals) {
			ind.computeFitness();
			this.avgFitness += ind.getFitness();
		}
		
		if(this.individuals.size() > 0) {
			this.avgFitness = this.avgFitness/(double)this.individuals.size();
		} else {
			this.avgFitness = 0.0;
		}
		log.debug("rank individuals");
		rank();
	}
	
	
	public void checkIndividualState(BioFuzzIndividual ind) {
		
		if(ind.getState() == State.IMMIGRANT) {
			ind.setState(State.IMMATURE);
		}
		
		BioFuzzTokLst tokLst = ind.getTree().getTokLst();
		String s = tokLst.get(tokLst.getSize()-2);
		
		// if the last token added is a comment and we have a checkpoint, there is nothing to do anymore
		if ( s.equals("--") || s.equals("#") ) {
			ind.setState(State.MATURE);
		}
	}
	
	public void checkForCheckpoints(BioFuzzIndividual ind, int iter) {
		
		this.mgr.validate(ind.getTree());
		
		if(ind.getTree().getVal() == true && ind.hasChanged()) {
			double nc = ind.getFvalForCriterion(FitnessCriterion.NODE_CNT);
			double effort = 1 - nc;
			//double cp = nc * (effort + 1.0);
			double cp = BioFuzzUtils.max(nc, effort);
			ind.addCheckpoint(cp);
			
		}
	}
	
	public void evolveIndividual(BioFuzzIndividual ind) {
		assert(ind != null);
		log.debug("Population: evolve individual");	
		flog.write("evolve individual");
		this.mgr.extend(ind.getTree());

	}
	
	public void mutateIndividual(BioFuzzIndividual ind) {
		assert(ind != null);
		flog.write("mutate individual");
		this.mgr.mutate(ind.getTree());
	}
	
	public BioFuzzIndividual crossOver(BioFuzzIndividual a, BioFuzzIndividual b) {
		log.debug("Population crossover");
		assert(a != null);
		assert(b != null);
		assert(a.getTree() != null);
		assert(b.getTree() != null);
		
		flog.write("crossover");
		this.mgr.validate(a.getTree());
		this.mgr.validate(b.getTree());
		log.debug("Validation done");

		
		BioFuzzParseTree t = this.mgr.crossover(a.getTree(),b.getTree());
		
		log.debug("after crossover");
		
		if(t == null) {
			log.debug("tree is null");
			return null;
		}
		
		//BioFuzzProtocolInput in = new BioFuzzProtocolInput(a.getInput());
		BioFuzzIndividual newInd = new BioFuzzIndividual(a.getTribe(),a.getInput(),a.getId(),a.getQuery(),a.getQueryPfx(),a.getExpectedDom(),t);
		log.debug("New individual created");
		assert(newInd != null);
		this.mgr.validate(newInd.getTree());
		
		
		Integer [] cps = a.getTree().getTokLst().getCheckpoints();
		int barrier = a.getTree().getPfxBarrier();
		
		// add checkpoints that are still valid
		for(int cp : cps) {
			if(cp <= barrier) {
				newInd.getTree().getTokLst().addCheckpoint(cp);
			}
		}
		newInd.setPfxBarrier(a.getPfxBarrier());
		
		return newInd;
	}
	
	public BioFuzzTribe getTribeByIdx(int idx) {
		if(idx >= 0 && idx < this.tribes.size())
			return this.tribes.get(idx);
		
		return null;
	}
	
	public int getTribeCnt() {
		return this.tribes.size();
	}
	
	public int getIndividualCnt() {
		return this.individuals.size();
	}
	
	public void rank() {
		Collections.sort(this.individuals);
	}
	
	public List<BioFuzzIndividual> getWorst(int amt) {
		
		List<BioFuzzIndividual> ind = new Vector<BioFuzzIndividual>();
		
		if(amt > this.individuals.size()) {
			return null;
		}
		
		// if size of list is smaller than amt
		int limit = this.individuals.size()/2 < amt ? this.individuals.size()/2 : amt;
		
		for(int i = this.individuals.size()-1; i > this.individuals.size()-1-limit;i--) {
			ind.add(this.individuals.get(i));
		}
		
		long seed = System.nanoTime();
		
		Collections.shuffle(ind, new Random(seed));
		
		return ind;
	}
	
	
	public List<BioFuzzIndividual> getBest(int amt) {
		
		List<BioFuzzIndividual> ind = new Vector<BioFuzzIndividual>();
		
		if(amt > this.individuals.size()) {
			return null;
		}
		
		// if size of list is smaller than amt
		int limit = this.individuals.size()/2 < amt ? this.individuals.size()/2 : amt;
		
		for(int i = 0; i < limit ; i++) {
			ind.add(this.individuals.get(i));
		}
		
		long seed = System.nanoTime();
		
		Collections.shuffle(ind, new Random(seed));
		
		return ind;
	}
	
	public BioFuzzIndividual getWorst() {
		if(this.individuals.size() > 0) {
			return this.individuals.get(this.individuals.size()-1);
		}
		return null;
	}
	
	public BioFuzzIndividual getBest() {
		if(this.individuals.size() > 0) {
			return this.individuals.get(0);
		}
		
		return null;
	}
	
	public String toString() {
		
		assert(this.individuals != null);
		
		String s = "[[BioFuzzPopulation: ......................................BEGIN]]\n";
		s += "#Generated NTs: " + this.ntSum + "\n";
		for(BioFuzzIndividual ind : this.individuals) {
			s += "\n";
			s += "Prefix: " + ind.getQueryPfx() + "\n";
			s += ind.getTree().getTokLst().toString() + "\n";
			s += ind.getFvect() + "\n";
			s += ind.getFscal() + "\n";
			s += "FitnessValue: " + ind.getFitness() + "\n";
			s += "Generated Tokens: " + ind.getTermCnt() + "\n";
			s += "Checkpoints: " + ind.getCheckpointCnt() + "\n";
			s += "Pfx Barrier: " + ind.getTree().getPfxBarrier() + "\n";
		}
		s += "Avg Fitness: " + this.avgFitness + "\n";
		s += "#Individuals: " + this.individuals.size() + "\n";
		s += "#Tribes: " + this.tribes.size() + "\n";
		s += "[[BioFuzzPopulation: ......................................END]]\n";
		return s;
	}

}