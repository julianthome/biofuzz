  package com.crawljax.plugins.biofuzz.core;

import java.util.ArrayList;

public class BioFuzzFitness {

	public enum FitnessCriterion {
		NODE_CNT("Node Count"),
		DOM_LDIST("Dom Levenshtein Distance"),
		LOGGER_LDIST("Logger Levenshtein Distance"),
		CHECKPOINT_CNT("Checkpoint Count");
		
		private String desc;
		public static int amnt = 4;
		
		FitnessCriterion(String desc) {
			this.desc = desc;
		}
		
		@Override
		public String toString() {
			return this.desc;
		}
	};
	
	public static class BioFuzzFitnessVector {
		private double [] fv = null;
		
		public BioFuzzFitnessVector(double nodeCnt, double domLdist, double loggerLdist, double cpointCnt) {
			fv = new double[FitnessCriterion.amnt];
			fv[FitnessCriterion.NODE_CNT.ordinal()] = nodeCnt;
			fv[FitnessCriterion.DOM_LDIST.ordinal()] = domLdist;
			fv[FitnessCriterion.LOGGER_LDIST.ordinal()] = loggerLdist;
			fv[FitnessCriterion.CHECKPOINT_CNT.ordinal()] = cpointCnt;
		}
	
		public void setFvalForCriterion(FitnessCriterion crit, double fval) {
			fv[crit.ordinal()] = fval;
		}
		
		public double getFvalForCriterion(FitnessCriterion crit) {
			return fv[crit.ordinal()];
		}
		
		public double getFvalByIdx(int idx) {
			assert(idx < fv.length);
			return fv[idx];
		}
		
		public int getLength() {
			return this.fv.length;
		}
		
		public String toString() {
			return "FitnessVector:: NodeCnt(" + fv[FitnessCriterion.NODE_CNT.ordinal()] + 
					"); DomLdist(" + fv[FitnessCriterion.DOM_LDIST.ordinal()] + 
					"); LoggerLdist(" + fv[FitnessCriterion.LOGGER_LDIST.ordinal()] +
					"); CpointCnt(" + fv[FitnessCriterion.CHECKPOINT_CNT.ordinal()] + 
					")";
		}
	};
	
	public static class BioFuzzFitnessScalar {
		
		private int [] sv = null;
		
		public BioFuzzFitnessScalar(int sNodeCnt, int sDomLdist, int sLoggerLdist, int cpointCnt) {
			sv = new int[FitnessCriterion.amnt];
			sv[FitnessCriterion.NODE_CNT.ordinal()] = sNodeCnt;
			sv[FitnessCriterion.DOM_LDIST.ordinal()] = sDomLdist;
			sv[FitnessCriterion.LOGGER_LDIST.ordinal()] = sLoggerLdist;
			sv[FitnessCriterion.CHECKPOINT_CNT.ordinal()] = cpointCnt;
		}
		
		public void setScalForCriterion(FitnessCriterion crit, int scal) {
			sv[crit.ordinal()] = scal;
		}
		
		public double getScalForCriterion(FitnessCriterion crit) {
			return sv[crit.ordinal()];
		}
		
		public int getLength() {
			return this.sv.length;
		}
		
		public double getFvalByIdx(int idx) {
			assert(idx < sv.length);
			return sv[idx];
		}
		
		public String toString() {
			return "FitnessScalar:: NodeCnt(" + sv[FitnessCriterion.NODE_CNT.ordinal()] + 
					"); DomLdist(" + sv[FitnessCriterion.DOM_LDIST.ordinal()] + 
					"); LoggerLdist(" + sv[FitnessCriterion.LOGGER_LDIST.ordinal()] +
					"); CpointCnt(" + sv[FitnessCriterion.CHECKPOINT_CNT.ordinal()] +		
					")";
		}
	};
	
	
	public static double computeFitness(BioFuzzFitnessVector fv, BioFuzzFitnessScalar sv) {

		assert(fv.getLength() == sv.getLength());
		
		double fitness = 0.0;
		int nf = 0;
		
		for(int i = 0; i < fv.getLength(); i++) {
			nf += sv.getFvalByIdx(i);
			fitness += fv.getFvalByIdx(i) * sv.getFvalByIdx(i);
		}
		
		return fitness/nf;
		
	}
}
