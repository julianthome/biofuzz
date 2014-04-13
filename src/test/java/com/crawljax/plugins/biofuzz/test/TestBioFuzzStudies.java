package com.crawljax.plugins.biofuzz.test;

import java.util.List;
import java.util.Vector;

import org.biofuzztk.cfg.BioFuzzAttackTag.TagType;
import org.biofuzztk.components.BioFuzzMgr;
import org.biofuzztk.components.BioFuzzTracer.BioFuzzQuery;
import org.biofuzztk.components.BioFuzzTracer.TraceType;
import org.biofuzztk.ptree.BioFuzzParseNode;
import org.biofuzztk.ptree.BioFuzzParseTree;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class TestBioFuzzStudies {
	final static Logger logger = LoggerFactory.getLogger(TestBioFuzzStudies.class);
		
	static List<BioFuzzParseTree> tLst0 = null;
	static List<BioFuzzParseTree> tLst1 = null;
	static List<BioFuzzParseTree> tLst2 = null;
	static List<BioFuzzParseTree> tLst3 = null;
	static List<BioFuzzParseTree> tLst4 = null;
	static List<BioFuzzParseTree> tLst5 = null;
	static List<BioFuzzParseTree> tLst6 = null;
	static List<BioFuzzParseTree> tLst7 = null;
	static List<BioFuzzParseTree> tLst8 = null;
	static List<BioFuzzParseTree> tLst9 = null;
	static List<BioFuzzParseTree> tLst10 = null;
	static List<BioFuzzParseTree> tLst11 = null;
	static List<BioFuzzParseTree> tLst12 = null;
	static List<BioFuzzParseTree> tLst13 = null;
	static List<BioFuzzParseTree> tLst14 = null;
	static List<BioFuzzParseTree> tLst15 = null;
	static List<BioFuzzParseTree> tLst16 = null;
	static List<BioFuzzParseTree> tLst17 = null;
	static BioFuzzMgr mgr = null;
	
	
	//@Test
	public void testCreat() {
		mgr = new BioFuzzMgr("cfg.xml");
		assert(mgr != null);
		
		logger.debug(">> Tree creation");
		tLst0 = mgr.buildTrees("UPDATE preferences SET value = 1234 WHERE playerID = 1 AND preference = 'autoreload'");
		assert(tLst0 != null);
		
		for(BioFuzzParseTree tree: tLst0) {
			logger.debug(tree.toString());
		}
		
		tLst1 = mgr.buildTrees("SELECT * FROM players WHERE nick = '5CZRF' AND password = 'TYSOC'");
		assert(tLst1 != null);
		for(BioFuzzParseTree tree: tLst1) {
			logger.debug(tree.toString());
		}
		
		logger.debug(mgr.toString());
		
		tLst2 = mgr.buildTrees("UPDATE players SET firstName = 'WAC2O', lastName = 'KK9FF', password = 'webchess1' WHERE playerID = 1");
		assert(tLst2 != null);
		for(BioFuzzParseTree tree: tLst2) {
			logger.debug(tree.toString());
		}
		
		tLst3 = mgr.buildTrees("INSERT INTO communication (gameID,fromID,toID,title,text,postDate,expireDate,ack,commType) VALUES ( NULL , 1 , 2, 'hallo', 'test', NOW( ) , NULL , '0', '0' );");
		assert(tLst3 != null);
		for(BioFuzzParseTree tree: tLst3) {
			logger.debug(tree.toString());
		}
		
		tLst4 = mgr.buildTrees("INSERT INTO preferences (playerID, preference, value) VALUES (35, 'autoreload', 1234)");
		assert(tLst4 != null);
		for(BioFuzzParseTree tree: tLst4) {
			logger.debug(tree.toString());
		}
		
		
		tLst5 = mgr.buildTrees("UPDATE preferences SET value = 1234 WHERE playerID = 1 AND preference = 'autoreload'");
		assert(tLst5 != null);
		for(BioFuzzParseTree tree: tLst5) {
			logger.debug(tree.toString());
		}
		
		tLst6 = mgr.buildTrees("INSERT INTO forum VALUES('', 'KTHR0', '1378197350', 'SG6H1', ' LNYCH', '')");
		assert(tLst6 != null);
		for(BioFuzzParseTree tree: tLst6) {
			logger.debug(tree.toString());
		}


		tLst7 = mgr.buildTrees("SELECT gameID FROM games WHERE lastMove < '2013-08-21'");
		assert(tLst7 != null);
		for(BioFuzzParseTree tree: tLst7) {
			logger.debug(tree.toString());
		}
		
	
		BioFuzzQuery q0 = new BioFuzzQuery() {
			public Boolean condition(BioFuzzParseNode node) {
				return node.getAtagType() == TagType.NON_TERMINAL && node.getAtagName().equals("tab_name");
			}
		};
		

		BioFuzzQuery q1 = new BioFuzzQuery() {
			public Boolean condition(BioFuzzParseNode node) {
				return node.getAtagType() == TagType.TERMINAL || node.getAtagType() == TagType.REGEXP;
			}
		};
		
		List<BioFuzzParseNode> l = mgr.trace(tLst7.get(0), q0,TraceType.BFS);
		assert(l != null);
		assert(l.size() > 0);
		
		for(BioFuzzParseNode nt : l) {
			logger.debug("Found Non-Terminal: " + nt.getAtagName());
			List<BioFuzzParseNode> p = mgr.traceSubNodes(nt, q1,TraceType.BFS);
			assert(p != null);
			assert(p.size() > 0);
			
			for(BioFuzzParseNode t : p) {
				logger.debug(tLst7.get(0).getTokByIdx(t.getTokIdx()));
			}
			
		}

		logger.debug(mgr.toString());
		tLst8 = mgr.buildTrees("SELECT * from Faq WHERE parent_id = 4 ORDER BY list_order");
		assert(tLst8 != null);
		for(BioFuzzParseTree tree: tLst8) {
			logger.debug(tree.toString());
		}
		
		logger.debug(mgr.toString());
		tLst9 = mgr.buildTrees("SELECT * FROM forum WHERE rispostadel='' ORDER BY id DESC");
		assert(tLst9 != null);
		for(BioFuzzParseTree tree: tLst9) {
			logger.debug(tree.toString());
		}	
		
		logger.debug(mgr.toString());
		tLst10 = mgr.buildTrees("SELECT * FROM games WHERE (gameMessage = 'playerInvited' AND " +
				"((whitePlayer = 1 AND messageFrom = 'white') OR (blackPlayer = 1 AND messageFrom = 'black'))) " +
				"OR (gameMessage = 'inviteDeclined' AND ((whitePlayer = 1 AND messageFrom = 'black') OR (blackPlayer = 1 " +
				"AND messageFrom = 'white')))  ORDER BY dateCreated");
		assert(tLst10 != null);
		for(BioFuzzParseTree tree: tLst10) {
			logger.debug(tree.toString());
		}
		
		
		
		logger.debug(mgr.toString());
		tLst10 = mgr.buildTrees("SELECT * FROM games WHERE (gameMessage = 'playerInvited' AND " +
				"((whitePlayer = 1 AND messageFrom = 'white') OR (blackPlayer = 1 AND messageFrom = 'black'))) " +
				"OR (gameMessage = 'inviteDeclined' AND ((whitePlayer = 1 AND messageFrom = 'black') OR (blackPlayer = 1 " +
				"AND messageFrom = 'white')))  ORDER BY dateCreated");
		assert(tLst10 != null);
		for(BioFuzzParseTree tree: tLst10) {
			logger.debug(tree.toString());
		}
		
		logger.debug(mgr.toString());
		tLst11 = mgr.buildTrees("SELECT * FROM games WHERE gameMessage = 'playerInvited' " +
				"AND ((whitePlayer = 1 AND messageFrom = 'black') OR " +
				"(blackPlayer = 1 AND messageFrom = 'white')) ORDER BY dateCreated");
		assert(tLst11 != null);
		for(BioFuzzParseTree tree: tLst11) {
			logger.debug(tree.toString());
		}
		
		
		tLst12 = mgr.buildTrees("SELECT * FROM games WHERE (gameMessage <> '' " +
				"AND gameMessage <> 'playerInvited' AND gameMessage <> 'inviteDeclined') " +
				"AND (whitePlayer = 1 OR blackPlayer = 1) ORDER BY lastMove DESC");
		assert(tLst12 != null);
		for(BioFuzzParseTree tree: tLst12) {
			logger.debug(tree.toString());
		}
		
		BioFuzzQuery q2 = new BioFuzzQuery() {
			public Boolean condition(BioFuzzParseNode node) {
				return node.getAtagType() == TagType.NON_TERMINAL && node.getAtagName().equals("where_exp");
			}
		};
		

		BioFuzzQuery q3 = new BioFuzzQuery() {
			public Boolean condition(BioFuzzParseNode node) {
				return node.getAtagType() == TagType.NON_TERMINAL && node.getAtagName().equals("col_name");
			}
		};
		
		BioFuzzQuery q4 = new BioFuzzQuery() {
			public Boolean condition(BioFuzzParseNode node) {
				return node.getAtagType() == TagType.NON_TERMINAL && node.getAtagName().equals("col_val");
			}
		};
		
		BioFuzzQuery q5 = new BioFuzzQuery() {
			public Boolean condition(BioFuzzParseNode node) {
				return node.getAtagType() == TagType.TERMINAL || node.getAtagType() == TagType.REGEXP;
			}
		};
		List<BioFuzzQuery> qlistColName = new Vector<BioFuzzQuery>();
		
		qlistColName.add(q3);
		qlistColName.add(q5);

		List<BioFuzzParseNode> where_res = mgr.trace(tLst12.get(0), q2,TraceType.BFS);
		assert(where_res != null);
		assert(where_res.size() > 0);
		
		List<BioFuzzParseNode> colName_res = mgr.traceAll(tLst12.get(0), qlistColName,TraceType.BFS);
		List<BioFuzzParseNode> colVal_res = mgr.trace(tLst12.get(0), q4,TraceType.BFS);
		
//		logger.debug("output");
//		for(BioFuzzParseNode t : colName_res) {
//			logger.debug(">> ColNames: " + t.getTok());
//		}
//		
//		for(BioFuzzParseNode t : colVal_res) {
//			logger.debug("<< ColVals: " + t.getTokens());
//		}
//		logger.debug("done");	
		
		tLst13 = mgr.buildTrees("INSERT INTO games (whitePlayer, blackPlayer, gameMessage, messageFrom, dateCreated, lastMove) VALUES (1, 3, 'playerInvited', 'white', NOW(), NOW())");
		assert(tLst13 != null);
		for(BioFuzzParseTree tree: tLst13) {
			logger.debug(tree.toString());
		}
		
		tLst14 = mgr.buildTrees("SELECT * FROM history WHERE gameID = RD58I ( A < ' '");
		assert(tLst14 == null);
		
		tLst15 = mgr.buildTrees("SELECT * FROM history WHERE gameID = 123");
		assert(tLst15 != null);
		for(BioFuzzParseTree tree: tLst15) {
			logger.debug(tree.toString());
		}
		
		String s0 = "SELECT gameID FROM games WHERE gameMessage = 'playerInvited' " +
				"AND ((messageFrom = 'white' AND whitePlayer = 1 AND blackPlayer = 45192) " +
				"OR (messageFrom = 'black' AND whitePlayer = 45192 AND blackPlayer = 1))";
		
		String s1 = "INSERT INTO games " +
				"(whitePlayer, blackPlayer, gameMessage, messageFrom, dateCreated, lastMove) " +
				"VALUES (45192, 1, 'playerInvited', 'black', NOW(), NOW())";
		
		String s2 = "SELECT gameID FROM games WHERE gameMessage = 'playerInvited' " +
				"AND ((messageFrom = 'white' AND whitePlayer = 1 AND " +
				"blackPlayer = 50278) OR (messageFrom = 'black' AND whitePlayer = 50278";
		
		String s3 = "SELECT * FROM games WHERE a = b OR 1=1 #";
		
		String s4 = "select curPiece,curColor,replaced " +
				"from history where replaced > ' ' and gameID = '406691' ORDER";
		
		String s5 = "select curPiece,curColor,replaced " +
				"from history where replaced > ZONPW and gameID = '406691' ORDER BY";
		
		String s6 = "select curPiece,curColor,replaced from history where " +
				"replaced > ' ' and gameID='8' order by curColor desc, replaced desc";
		
		tLst16 = mgr.buildTrees(s6);

		logger.debug(mgr.toString());
		assert(tLst16 != null);
		for(BioFuzzParseTree tree: tLst16) {
			
			mgr.validate(tree);
			logger.debug(tree.toString());
			//logger.debug(tLst15.get(0).toString());
			//mgr.extend(tree);
			//BioFuzzParseTree test = mgr.crossover(tree, tLst15.get(0));
			//logger.debug(test.getTokLst().toString());
		}
		
		tLst17 = mgr.buildTrees("SELECT * FROM history WHERE gameID = 'ZONPW' UNION ALL Select * from name");
		assert(tLst17 != null);
		for(BioFuzzParseTree tree: tLst17) {
			logger.debug(tree.toString());
		}

	}
	
	@Test
	public void testEvol() {
		
		logger.debug(">> Tree creation");
		mgr = new BioFuzzMgr("cfg.xml");
		assert(mgr != null);
		tLst3 = mgr.buildTrees("UPDATE players SET firstName = 'WAC2O', lastName = 'KK9FF', password = '");
		assert(tLst3 != null);
		
		
		for(BioFuzzParseTree tree: tLst3) {
			logger.debug(tree.toString());
			int iter = 0;
			mgr.validate(tree);
			mgr.extend(tree);
			
			while(iter < 40) {
				logger.debug(tree.toString());
				mgr.validate(tree);
				mgr.extend(tree);
				iter ++;
			}
			logger.debug(tree.toString());
		}
		
		

		
	}

}
