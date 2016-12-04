package com.crawljax.plugins.biofuzz.core.topology;


import com.crawljax.plugins.biofuzz.core.BioFuzzDBSchema;
import com.crawljax.plugins.biofuzz.core.BioFuzzFitness.BioFuzzFitnessScalar;
import com.crawljax.plugins.biofuzz.input.BioFuzzParamTuple;
import com.crawljax.plugins.biofuzz.utils.BioFuzzFileLogger;
import org.biofuzztk.cfg.BioFuzzAttackTag.TagType;
import org.biofuzztk.components.BioFuzzMgr;
import org.biofuzztk.components.BioFuzzTracer.BioFuzzQuery;
import org.biofuzztk.components.BioFuzzTracer.TraceType;
import org.biofuzztk.components.tokenizer.BioFuzzSQLTokenizer;
import org.biofuzztk.ptree.BioFuzzParseNode;
import org.biofuzztk.ptree.BioFuzzParseTree;

import java.util.*;

public class BioFuzzWorld {

	private BioFuzzMgr mgr;
	private static List<BioFuzzPopulation> pops = null;
	private static List<BioFuzzIndividual> individuals = null;
	private static int TREE_ANALYSE_TRESH = 1;

	private BioFuzzFitnessScalar fscal = null;
	
	private static double avgFitness = 0.0;
	private BioFuzzDBSchema dbSchema = null;
	private static Set<BioFuzzParamTuple> globConstParams = null;
	private BioFuzzFileLogger flog = null;
	
	
	public BioFuzzWorld(BioFuzzFitnessScalar fvect) {
		this.mgr = new BioFuzzMgr("src/main/resources/cfg.xml", new BioFuzzSQLTokenizer());
		this.fscal = fvect;
		pops = new Vector<BioFuzzPopulation>();
		dbSchema = new BioFuzzDBSchema();
		this.flog = new BioFuzzFileLogger("/tmp/biofuzz/","world.log");
		individuals = new Vector<BioFuzzIndividual>();
		globConstParams = new HashSet<BioFuzzParamTuple>();

	}

	public void addParams(Set<BioFuzzParamTuple> s) {
		if(s != null)
			globConstParams.addAll(s);
	}
	
	public void showParams() {
		
		logMsg("Show global params");
		for(BioFuzzParamTuple t : globConstParams) {
			logMsg(">> " + t.toString());
		}
	}
	
	public Set<BioFuzzParamTuple> getDiffParams(Set<BioFuzzParamTuple> s) {
		
		Set <BioFuzzParamTuple>globCp = new HashSet<BioFuzzParamTuple>(globConstParams);
		
		globCp.removeAll(s);
		
		return globCp;
		
	}
	
	private List<BioFuzzParseNode> getNonTerminals(List<BioFuzzParseNode> nts) {
		//logMsg("addToDBschmea");

		List<BioFuzzParseNode> ret = new Vector<BioFuzzParseNode>();

		BioFuzzQuery q = new BioFuzzQuery() {
			public Boolean condition(BioFuzzParseNode node) {
				return node.getAtagType() == TagType.TERMINAL || node.getAtagType() == TagType.REGEXP;
			}
		};

		if(nts != null && nts.size() > 0) {
			for(BioFuzzParseNode nt : nts) {
				// find all terminal subnodes of nonterminal
				ret.addAll(this.mgr.traceSubNodes(nt, q, TraceType.DFS));
			}
		}

		if(ret == null || ret.size() <=0 )
			return null;

		return ret;

	}

	public void registerIndividual(BioFuzzIndividual ind) {
		individuals.add(ind);
	}
	
	public void unregisterIndividual(BioFuzzIndividual ind) {
		individuals.remove(ind);
	}


	public void rank() {
		Collections.sort(individuals);
	}

	private void checkTreeForDBInfo(BioFuzzParseTree tree) {
		//logMsg("check for db info");

		BioFuzzQuery qTables = new BioFuzzQuery() {
			public Boolean condition(BioFuzzParseNode node) {
				return node.getAtagType() == TagType.NON_TERMINAL && node.getAtagName().equals("tab_name") &&
						node.getChildCnt() == 1;
			}
		};


		BioFuzzQuery qColumns = new BioFuzzQuery() {
			public Boolean condition(BioFuzzParseNode node) {
				return node.getAtagType() == TagType.NON_TERMINAL && node.getAtagName().equals("col_label") &&
						node.getChildCnt() == 1;
			}
		};


		List<BioFuzzParseNode> tabNTs = this.mgr.trace(tree,qTables, TraceType.DFS);
		List<BioFuzzParseNode> colNTs = this.mgr.trace(tree,qColumns, TraceType.DFS);
		
		List<BioFuzzParseNode> tabTs = null;
		List<BioFuzzParseNode> colTs = null;
		

		if(tabNTs != null && tabNTs.size() > 0) {
			tabTs = getNonTerminals(tabNTs);
		}
		
		if(colNTs != null && colNTs.size() > 0) {
			colTs = getNonTerminals(colNTs);
		}

	
		if(tabTs != null && tabTs.size() > 0 && tabTs != null && tabTs.size() > 0) {
			
			
			String tab = tree.getTokByNode(tabTs.get(0));
			//logMsg("add Table " + tab);
			dbSchema.addTable(tab);
			
			if(colTs != null && colTs.size() > 0) {
				for(BioFuzzParseNode t : colTs) {
						//logMsg("add col " + tree.getTokByNode(t) + " to table " + tab);
						dbSchema.addColumnToTable(tab, tree.getTokByNode(t));
				}
			}
			
		}



	}

	public void analyzeLog(String[] stmts) {
		assert(stmts != null);

		for(String s : stmts) {
			
			assert(s != null);
			
			s = s.replaceAll("[0-9]+ (.*)", "$1");
			logMsg("build tree for " + s);
			List<BioFuzzParseTree> trees = this.mgr.buildTrees(s);
			int cyc = 0;
			if(trees != null && trees.size() > 0) {
				for(BioFuzzParseTree t : trees) {
					
						checkTreeForDBInfo(t);
						cyc = cyc +1;
						
						if (cyc > TREE_ANALYSE_TRESH) {
							break;
						} {
							logMsg("ok " + cyc);
						}
				}
			} else {
				logMsg("couldn't build");
			}
		}
		
		//logMsg("Schema: ---");
		//logMsg(dbSchema.toString());
	}


	public BioFuzzDBSchema getDBSchema() {
		return new BioFuzzDBSchema(this.dbSchema);
	}
	
	private void logMsg(String s) {
		flog.write(s);
	}

	public BioFuzzFitnessScalar getFscal() {
		return this.fscal;
	}

	public BioFuzzPopulation createPopulation() {

		BioFuzzPopulation pop = null;
		pop = new BioFuzzPopulation(this);
		pops.add(pop); 
	
		return pop;
	}

	public BioFuzzPopulation getPopulationByIdx(int idx) {
		if(idx > 0 && idx < pops.size()) {
			return pops.get(idx);
		} 
		return null;
	}
	
	public void computeFitness() {
		logMsg("compute Fitness");

		avgFitness = 0.0;
		for(BioFuzzIndividual ind : individuals) {
			avgFitness += ind.getFitness();
		}
		
		avgFitness = avgFitness/(double)individuals.size();
		rank();
	}
	
	private int getSuccessfulInstances() {
		int succ = 0;
		for(BioFuzzPopulation pop : pops) {
			if (pop.getIndividualCnt() > 0) {
				succ += 1;
			}
		}
		return succ;
	}
	
	public String toString() {

		String s = "";

		s += "[[BioFuzzWorld: ......................................BEGIN]]\n";

		
		for(BioFuzzIndividual ind : individuals) {
			s += "[BioFuzzIndividual: ......................................BEGIN]\n";
			s += "Prefix: " + ind.getQueryPfx() + "\n";
			s += ind.getTree().getTokLst().toString() + "\n";
			s += ind.getFvect() + "\n";
			s += ind.getFscal() + "\n";
			s += "FitnessValue: " + ind.getFitness() + "\n";
			s += "Generated Tokens: " + ind.getTermCnt() + "\n";
			s += "Checkpoints: " + ind.getCheckpointCnt() + "\n";
			s += "Pfx Barrier: " + ind.getTree().getPfxBarrier() + "\n";
			s += ind.getInput().getInputKey() + "\n";
			s += "[BioFuzzIndividual: ......................................END]\n";
		}
		
		
		s += "Avg Fitness: " + avgFitness + "\n";
		s += "# Individuals: " + individuals.size() + "\n";
		s += "# Populations: " + pops.size() + "\n";
		s += "# Fields detected: " + globConstParams.size() + "\n";
		s += "# Successful instances: " + getSuccessfulInstances()  + "\n";
		s += globConstParams.toString() + "\n";
		s += dbSchema.toString() + "\n";
		
		s += "[BioFuzzWorld: ......................................END]]\n";
		return s;
	}

}
