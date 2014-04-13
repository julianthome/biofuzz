package com.crawljax.plugins.biofuzz.core.topology;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;


import org.biofuzztk.ptree.BioFuzzParseTree;
import org.biofuzztk.ptree.BioFuzzTokLst;

import com.crawljax.plugins.biofuzz.core.BioFuzzFitness;
import com.crawljax.plugins.biofuzz.core.BioFuzzFitness.BioFuzzFitnessScalar;
import com.crawljax.plugins.biofuzz.core.BioFuzzFitness.BioFuzzFitnessVector;
import com.crawljax.plugins.biofuzz.core.BioFuzzFitness.FitnessCriterion;
import com.crawljax.plugins.biofuzz.input.BioFuzzProtocolInput;
import com.crawljax.plugins.biofuzz.utils.BioFuzzUtils;

public class BioFuzzIndividual implements Comparable<Object>{
	
	public enum State {
		IMMIGRANT("IMMIGRANT"),
		IMMATURE("IMMATURE"),
		MATURE("MATURE");
		
		private String desc;
		State(String desc) {
			this.desc = desc;
		}
		@Override
		public String toString() {
			return this.desc;
		}
	};
	
	private String expectedDom;
	private String id;
	private String query;
	private String patQuery;
	private String queryPfx;
	private BioFuzzProtocolInput input;
	private ArrayList <Double>cp_i;
	private int pfxBarrier;
	@SuppressWarnings("unused")
	private boolean isImmigrant;
	private double fitness;
	private BioFuzzParseTree tree = null;
	private BioFuzzTribe tribe = null;
	private State state;
	
	private BioFuzzFitness.BioFuzzFitnessVector fvect = null;
	private BioFuzzFitnessScalar fscal = null;
	
	public BioFuzzProtocolInput getInput() {
		return input;
	}
	public void setInput(BioFuzzProtocolInput input) {
		this.input = input;
	}
	public BioFuzzIndividual(BioFuzzTribe tribe, BioFuzzProtocolInput in, String id, String query, String pfx, String expectedDom, BioFuzzParseTree tree) {
		assert(tree != null);
		this.tribe = tribe;
		this.input = in;
		this.id = id;
		this.query = query;
		this.queryPfx = pfx;
		this.tree = tree;
		this.pfxBarrier = this.tree.getTokLstLen()-2;
		// set prefix barrier such that only tokens after
		// the one (specified by the idx) are useable
		// for crossover
		this.tree.setPfxBarrier(this.pfxBarrier);
		this.expectedDom = expectedDom;
		this.fscal = this.tribe.getFscal();
		this.fvect = new BioFuzzFitnessVector(0.0,0.0,0.0,0.0);
		this.fvect.setFvalForCriterion(FitnessCriterion.LOGGER_LDIST, 1.0);
		this.fitness = 0.0;
		this.patQuery = query;
		this.state = State.IMMATURE;
		
		this.patQuery = Pattern.quote(this.patQuery);
		
		// Just a heuristic to generalize the pattern a little bit in the case of multiple 
		HashMap<String,List<String>> tinfo = BioFuzzUtils.getTableInfo(query);
		if(tinfo != null) {
			for(String key : tinfo.keySet()) {
				List<String> vals = tinfo.get(key);
				if(vals.size() > 1) {
					for(String pat : vals) {
						if(!pat.contains(this.id)) {
							this.patQuery = this.patQuery.replace(pat, "\\E.*\\Q");
						}
					}
				}
			}
		}
		
		
		this.cp_i = new ArrayList<Double>();
		this.patQuery = this.patQuery.replace(id, "\\E(.*)\\Q");
		//this.patQuery = this.patQuery.replaceAll("'[A-Za-z0-9 ()]+'", "\\\\E'[A-Za-z0-9 ()]+'\\\\Q");
	}
	
	public void setState(State state) {
		this.state = state;
	}
	
	public State getState() {
		return this.state;
	}
	
	public void setPfxBarrier(int pfxBarrier) {
		assert(pfxBarrier > 0 && pfxBarrier <= this.tree.getTokLstLen()-2);
		this.tree.setPfxBarrier(pfxBarrier);
		this.pfxBarrier = pfxBarrier;
	}
	
	public int getPfxBarrier() {
		return this.pfxBarrier;
	}
	
	public String getPatQuery() {
		return patQuery;
	}
	public void setPatQuery(String patQuery) {
		this.patQuery = patQuery;
	}
	
	public void addCheckpoint(double cp) {
		this.tree.addCheckpoint(tree.getTokLstLen()-2);
		this.cp_i.add(cp);
	}
	
	public double getCPAvg() {
		
		if(this.cp_i.size() <= 0)
			return 0.0;
		
		
		double sum = 0.0;
		
		for(double cp : this.cp_i) {
			sum += cp;
		}
		return sum/(this.cp_i.size() * 1.0);
	}
	
	public boolean hasChanged() {
		return this.pfxBarrier < this.tree.getTokLstLen()-2;
	}
	
	public double getFvalForCriterion(FitnessCriterion crit) {
		return this.fvect.getFvalForCriterion(crit);
	}
	
	public BioFuzzFitnessScalar getFscal() {
		return this.fscal;
	}
	
	public double getFitness() {
		return this.fitness;
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public int getLtok() {
		return pfxBarrier;
	}
	
	public void setLtok(int ltok) {
		this.pfxBarrier = ltok;
	}
	
	public String getQuery() {
		return query;
	}
	
	public void setQuery(String query) {
		this.query = query;
	}
	
	public String getQueryPfx() {
		return queryPfx;
	}
	
	public void setQueryPfx(String queryPfx) {
		this.queryPfx = queryPfx;
	}
	
	public BioFuzzParseTree getTree() {
		return tree;
	}
	
	public void setTree(BioFuzzParseTree tree) {
		this.tree = tree;
	}
	
	public String getSuffix() {
		BioFuzzTokLst tLst = this.tree.getTokLst();
		String suffix = "";
		
		for(int i = this.pfxBarrier; i < tLst.getLength()-1;i++) {
			suffix += tLst.get(i) + " ";
		}
		
		return suffix;
	}

	
	public int getTermCnt() {
		BioFuzzTokLst tLst = this.tree.getTokLst();

		assert(tLst != null);
		assert(this.pfxBarrier < (tLst.getLength()-1));
		
		return (tLst.getLength()-1) - this.pfxBarrier - 1;
	}
	
	public int getCheckpointCnt() {
		BioFuzzTokLst tLst = this.tree.getTokLst();
		return tLst.getCheckpointCnt();
	}
	
	public String getExpectedDom() {
		return this.expectedDom;
	}
	
	public void setFitness(FitnessCriterion crit, double fval) {
		this.fvect.setFvalForCriterion(crit, fval);
	}
	
	public void computeFitness() {
		this.fitness = BioFuzzFitness.computeFitness(this.fvect, this.fscal);
	}
	
	public String toString() {

		
		String s = "";
		
		s += "BioFuzzInidviudal: ......................BEGIN\n";
		s += "  Query: " + this.query + "\n";
		s += "  QueryPfx: " + this.queryPfx + "\n";
		s += "  Input: " + this.input + "\n";
		s += "  Id: " + this.id + "\n";
		//s += "  Tree: " + this.tree + "\n";
		s += "  TokLst: " + this.tree.getTokLst().toString() + "\n";
		s += "  Suffix: " + getSuffix() + "\n";
		s += "  Fvect: " + this.fvect + "\n";
		s += "  Fscal: " + this.fscal + "\n";
		s += "  Fitness: " + this.fitness + "\n";
		s += "  PfxBarrier: " + this.tree.getPfxBarrier() + "\n";
		s += "  State: " + this.state + "\n";
		s += "BioFuzzInidviudal: ......................END\n";
		
		return s;
				
	}

	public void submit() {
		assert(this.input != null);
		this.input.setInputValue(getSuffix());
		this.input.submit();
	}
	
	public void rollback() {
		this.input.rollback();
	}
	
    public int compareTo(Object compareObject)
    {

        BioFuzzIndividual ind = (BioFuzzIndividual)compareObject;
        
        if(this.getFitness() > ind.getFitness()) {
            return -1;
        }

        if(this.getFitness() < ind.getFitness()) {
            return 1;
        }
        
        return 0;
    }
	
	public BioFuzzFitnessVector getFvect() {
		return this.fvect;
	}
	
	public BioFuzzTribe getTribe() {
		return this.tribe;
	}
	
	public void register() {
		assert(this.tribe != null);
		
		this.tribe.registerIndividual(this);
	}
	
	public void unregister() {
		assert(this.tribe != null);
		
		this.tribe.unregisterIndividual(this);
	}

}
