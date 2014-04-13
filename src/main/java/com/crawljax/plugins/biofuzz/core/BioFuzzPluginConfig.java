package com.crawljax.plugins.biofuzz.core;

import com.crawljax.plugins.biofuzz.core.BioFuzzFitness.BioFuzzFitnessScalar;

public class BioFuzzPluginConfig {

	private BioFuzzFitnessScalar fscal;
	
	private int maxIter;
	private int crossoverCycle;
	private int crossoverCnt;
	private int crossoverOffset;
	private int tribes;
	private int parserMatchMax;
	
	private boolean applyMutation;
	private boolean applyCrossover;
	
	public BioFuzzPluginConfig() {
		this(null,true,true,0,0,0,0,0,0);
	}

	public int getParserMatchMax() {
		return parserMatchMax;
	}


	public boolean getApplyMutation() {
		return applyMutation;
	}


	public void setApplyMutation(boolean applyMutation) {
		this.applyMutation = applyMutation;
	}


	public boolean getApplyCrossover() {
		return applyCrossover;
	}


	public void setApplyCrossover(boolean applyCrossover) {
		this.applyCrossover = applyCrossover;
	}

	public void setParserMatchMax(int parserMatchMax) {
		this.parserMatchMax = parserMatchMax;
	}


	BioFuzzPluginConfig(BioFuzzFitnessScalar fscal, 
			boolean applyMutation,
			boolean applyCrossover,
			int maxIter, 
			int crossoverCycle, 
			int crossoverCnt, 
			int crossoverOffset, 
			int tribes, 
			int parserMatchMax) {
		
		this.applyCrossover = applyCrossover;
		this.applyMutation = applyMutation;
		
		this.maxIter = maxIter;
		this.crossoverCycle = crossoverCycle;
		this.tribes = tribes;
		this.crossoverCnt = crossoverCnt;
		this.crossoverOffset = crossoverOffset;
		this.fscal = fscal;
		this.parserMatchMax = parserMatchMax;
	}
	
	
	public int getCrossoverOffset() {
		return crossoverOffset;
	}

	public void setCrossoverOffset(int crossoverOffset) {
		this.crossoverOffset = crossoverOffset;
	}

	public BioFuzzFitnessScalar getFscal() {
		return fscal;
	}

	public void setFscal(BioFuzzFitnessScalar fscal) {
		this.fscal = fscal;
	}

	public int getMaxIter() {
		return maxIter;
	}

	public void setMaxIter(int maxIter) {
		this.maxIter = maxIter;
	}


	public int getCrossoverCycle() {
		return crossoverCycle;
	}

	public void setCrossoverCycle(int crossoverCycle) {
		this.crossoverCycle = crossoverCycle;
	}

	public int getCrossoverCnt() {
		return crossoverCnt;
	}

	public void setCrossoverCnt(int crossoverCnt) {
		this.crossoverCnt = crossoverCnt;
	}

	public int getTribes() {
		return tribes;
	}

	public void setTribes(int tribes) {
		this.tribes = tribes;
	}


}
