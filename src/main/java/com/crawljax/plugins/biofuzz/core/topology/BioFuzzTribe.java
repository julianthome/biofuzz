package com.crawljax.plugins.biofuzz.core.topology;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Vector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.biofuzztk.components.BioFuzzMgr;
import org.biofuzztk.ptree.BioFuzzParseTree;


import com.crawljax.plugins.biofuzz.core.BioFuzzFitness.BioFuzzFitnessScalar;

import com.crawljax.plugins.biofuzz.input.BioFuzzContentHandler;

import com.crawljax.plugins.biofuzz.input.BioFuzzProtocolInput;


public class BioFuzzTribe {	
	
	private List<BioFuzzIndividual> population = null;
	private BioFuzzMgr mgr = null;
	private BioFuzzPopulation bpop = null;
	private BioFuzzFitnessScalar fscal = null;
	private BioFuzzContentHandler handler = null;
	//private static BioFuzzFileLogger flog = null;
	
	private static final Logger log = LoggerFactory
			.getLogger(BioFuzzTribe.class);
	
	public BioFuzzTribe(BioFuzzPopulation pop, BioFuzzMgr mgr, BioFuzzContentHandler cont) {
		this.population = new Vector<BioFuzzIndividual>();
		this.mgr = mgr;
		this.bpop = pop;
		this.fscal = pop.getFscal();
		this.handler = cont;
		//flog = new BioFuzzFileLogger("/tmp/biofuzz/","tribe.log");
	}
	
	public BioFuzzContentHandler getContentHandler() {
		return this.handler;
	}
	
	private void logMsg(String s) {
		log.debug(s);
		//flog.write(s);
	}
	
	public BioFuzzFitnessScalar getFscal() {
		return this.fscal;
	}
	
	public int getSize() {
		return this.population.size();
	}

	public BioFuzzIndividual getIndividualByIdx(int idx) {
		assert(idx >= 0 && idx < this.population.size());
		
		return this.population.get(idx);
	}
	
	// create Individual
	public boolean createIndividual(BioFuzzProtocolInput in, List<String> queries, String expectedDom, int parserMatchMax) {
		
		int indCnt = 0;
		
		for(String query : queries) {
			// use random input value to split string into chunks
			
			String [] queryTmp = null;
			
			if(query == null) {
				logMsg("Query is null");
				continue;
			}
			
			
			logMsg("query is: " + query);
			
			logMsg("split with " + in.getInputValue());
			
			if(query.length() > 0 && query.contains(in.getInputValue())) {
				queryTmp = query.split(in.getInputValue());
			} else {
				logMsg("Nothing to split");
				continue;
			}
			
			if(queryTmp.length <= 0) {
				logMsg("query has length 0");
				continue;
			}
			
			
			if (queryTmp.length > 2) {
				// if we're here one single inputbox's value is used multiple times within
				// one query --> we have to create multiple individuals
				
				for(int k = queryTmp.length - 2; k >= 0; k--) {
					String q = "";
					for (int t = 0; t <= k; t++) {
						q += queryTmp[t];
						//if(t != k-1)
						q += in.getInputValue().trim();
					}
					
					// There might be multiple rules that are leading to the same prefix
					// Create an individual for every case
					
					List<BioFuzzParseTree> ltree = null;
					
					logMsg("build tree for: " + q);
					
					assert(q.length() > 0);
					
					if(q.length() <= 0) {
						logMsg("cannot create individual for string with length = 0");
						return false;
					}
					
					ltree = this.mgr.buildTrees(q);

					// no match found
					if(ltree == null || ltree.size() <= 0) {
						logMsg("Couldn't build tree for " + q);
						return false;
					}
					
					indCnt += createIndividual(ltree, in, query, q, expectedDom, parserMatchMax);
				}

			} else {
				String q = queryTmp[0] + in.getInputValue().trim();
				logMsg("Build Individual for " + q + " ...");
				List<BioFuzzParseTree> ltree = this.mgr.buildTrees(q);
				logMsg("... done");
				
				// no match found
				if(ltree == null || ltree.size() <= 0) {
					return false;
				}
				
				indCnt += createIndividual(ltree, in, query, q, expectedDom, parserMatchMax);
			}
		}
		
		return indCnt > 0;
		
	}
	
	private int createIndividual( List <BioFuzzParseTree> tlist, BioFuzzProtocolInput in, String query, String pfx, 
			String expDom, int parserMatchMax) {
		
		assert(tlist != null && tlist.size() > 0);
		
		int indCnt = 0;
		
		if(parserMatchMax > tlist.size()) {
			for(BioFuzzParseTree t:tlist) {
				assert(t != null);
				// associate new input with tribe content handler
				BioFuzzProtocolInput nin = new BioFuzzProtocolInput(this.handler.get(in.getIdx()), in.getInputValue());
				// Important - use tribe content handler here instead of the global one
				assert(handler != null);
				BioFuzzIndividual ind = new BioFuzzIndividual(this, nin, nin.getInputValue(),query,pfx,expDom,t);
				this.registerIndividual(ind);
				indCnt ++;
			}
		} else {

			Random ridx = new Random();
			int idx = 0;
			for(int pcnt = 0; pcnt < parserMatchMax; pcnt ++) {
				idx = ridx.nextInt(tlist.size());
				BioFuzzParseTree tree = tlist.remove(idx);
				assert(tree != null);
				
				// associate new input with tribe content handler
				BioFuzzProtocolInput nin = new BioFuzzProtocolInput(this.handler.get(in.getIdx()), in.getInputValue());
				// Important - use tribe content handler here instead of the global one
				assert(handler != null);
				
				BioFuzzIndividual ind = new BioFuzzIndividual(this, nin, nin.getInputValue(),query,pfx,expDom,tree);
				this.registerIndividual(ind);
				indCnt ++;
			}
			
		}
		
		return indCnt;
		
		
	}
	
	public void registerIndividual(BioFuzzIndividual in) {
		this.population.add(in);
		this.bpop.registerIndividual(in);
	}
	
	public void unregisterIndividual(BioFuzzIndividual in) {
		this.population.remove(in);
		this.bpop.unregisterIndividual(in);
	}
	
	public void rank() {
		Collections.sort(this.population);
	}
	
	public String toString() {
		String s = "[BioFuzzTribe: ......................................BEGIN]\n";
		for(BioFuzzIndividual ind : this.population) {
			//s += ind.toString() + "\n";
			s += ind.getTree().getTokLst().toString() + " " + "Fitness: " + ind.getFitness() + "\n";
		}
		s += "Propsize: " + this.population.size() + '\n';
		s += "Original Content " + this.handler.toString() + "\n";
		s += "[BioFuzzTribe: ......................................END]\n";
		return s;
	}
}
