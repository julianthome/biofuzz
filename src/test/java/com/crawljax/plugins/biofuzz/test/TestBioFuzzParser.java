package com.crawljax.plugins.biofuzz.test;
import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.biofuzztk.cfg.BioFuzzAttackCfg;
import org.biofuzztk.cfg.BioFuzzAttackCfgMgr;
import org.biofuzztk.cfg.BioFuzzAttackTag;
import org.biofuzztk.cfg.BioFuzzAttackTag.TagType;
import org.biofuzztk.components.BioFuzzMgr;
import org.biofuzztk.components.BioFuzzTracer;
import org.biofuzztk.components.BioFuzzTracer.BioFuzzQuery;
import org.biofuzztk.components.BioFuzzTracer.TraceType;
import org.biofuzztk.components.parser.BioFuzzParserConfig;
import org.biofuzztk.components.tokenizer.BioFuzzSQLTokenizer;
import org.biofuzztk.ptree.BioFuzzParseNode;
import org.biofuzztk.ptree.BioFuzzParseTree;


public class TestBioFuzzParser {
	private static BioFuzzMgr mgr;
	final static Logger logger = LoggerFactory.getLogger(TestBioFuzzPVG.class);

	static List<BioFuzzParseTree> tLst0 = null;
	@Test
	public void test() {
		mgr = new BioFuzzMgr("src/main/resources/cfg.xml", new BioFuzzSQLTokenizer());
		assert(mgr != null);
		
		BioFuzzAttackCfgMgr m = mgr.getAtackCfgMgr();
		assert(m!= null);
		
		BioFuzzAttackCfg acfg = m.getAttackCfgByKey("tab_name");
		assert(acfg != null);
		
		BioFuzzAttackCfg colcfg = m.getAttackCfgByKey("col_name");
		assert(acfg != null);
		
		
		//acfg.appendAtag(new BioFuzzAttackTag(null,"schoolinfo",TagType.TOK_TERMINAL));
		//colcfg.appendAtag(new BioFuzzAttackTag(null,"phonenumber",TagType.TOK_TERMINAL));
		//colcfg.appendAtag(new BioFuzzAttackTag(null,"sitemessage",TagType.TOK_TERMINAL));
		
		//colcfg.appendAtag(new BioFuzzAttackTag(null,"schoolname",TagType.TERMINAL));
		logger.debug(acfg.toString());
		
		//logger.debug(">> Tree creation");
		//tLst0 = mgr.buildTrees("SELECT * from tab_user WHERE (( a = b ) AND ( c = d))");SELECT * FROM history WHERE gameID = ZONPW UNION ALL Select * from name"
		
		String s = "UPDATE schoolinfo SET schoolname = \"School Name\", " +
				"address = '1,Street', phonenumber = '52365895', " +
				"sitetext = '', sitemessage = '', numsemesters = '0', numperiods = '0', " +
				"apoint = '0.0', bpoint = '0.0', cpoint = '0.0', dpoint = '0.0', " +
				"fpoint = '0.0' where schoolname = 'School Name' LIMIT 1";
		
//		logger.debug(mgr.toString());
//		String s = "SELECT * FROM history WHERE gameID=598162";
		
		String q = "UPDATE schoolinfo SET schoolname = \"684775\", " +
				"address = '1,Street', phonenumber = '52365895', sitetext = '', " +
				"sitemessage = '', numsemesters = '0', numperiods = '0', apoint = '0.0', " +
				"bpoint = '1.0', cpoint = '0.0', dpoint = '1.0', fpoint = '526828";
		
		String p = "UPDATE schoolinfo SET schoolname = \"684775\", address = '1,Street'," +
				" phonenumber = '52365895', sitetext = '', sitemessage = '', numsemesters = '817959', numperiods = '344396";
		
		String l = "Build Individual for UPDATE schoolinfo SET schoolname = \"684775\", " +
				"address = '1,Street', phonenumber = '52365895', sitetext = '', sitemessage = '', numsemesters = '817959', numperiods = '689328', apoint = '0.0', bpoint = '1.0', cpoint = '0.0', dpoint = '1.0', fpoint = '099412";
		
		String v ="UPDATE schoolinfo SET";
		
		String z ="UPDATE schoolinfo SET schoolname = \" 904995 CONCAT(CHAR(34)) WHERE 808 =";
		
		String y = "UPDATE schoolinfo SET schoolname = \"056424 CONCAT(CHAR(34)) WHERE ( \", " +
				"address = '473765', phonenumber = '52365895', sitetext = '', sitemessage = '', " +
				"numsemesters = '483648', numperiods = '0', apoint = '1.0', bpoint = '0.0', cpoint = '1.0', dpoint = '0.0', " +
				"fpoint = '0.0' where schoolname = '056424 CONCAT(CHAR(34)) WHERE ( ' LIMIT 1";
		
		String x = "SELECT * FROM games WHERE (gameMessage = 'playerInvited' AND " +
				"((whitePlayer = 1 AND messageFrom = 'white') OR (blackPlayer = 1 AND messageFrom = 'black'))) " +
				"OR (gameMessage = 'inviteDeclined' AND ((whitePlayer = 1 AND messageFrom = 'black') OR (blackPlayer = 1 " +
				"AND messageFrom = 'white')))  ORDER BY dateCreated";
		
		String qq = "INSERT INTO stats (`ip`, `date`, `page`, `month`, `day`, `year`) VALUES ('127.0.0.1', '09/29/2013', ') ', '09', '29', '2013')";
		
		String pp = "select * from users where " +
				"md5(concat(username,md5_pass,'Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) " +
				"Ubuntu Chromium/28.0.1500.71 Chrome/28.0.1500.71 Safari/537.362013-09')) = '3526c8c7af9073aa7300e125a698e7e4'";
		
		String oo = "INSERT INTO group_list (domain_id, group_name, group_header, group_footer,  group_parent_id)" +
"		                           VALUES ('0', 'a','a','a','0')";

		String tt = "DELETE FROM posts WHERE id = 1 OR 1 = 1";
	
		
		
		for(int k = 0; k < 1; k++) {
			tLst0 = mgr.buildTrees(tt);
		}
		
		assert(tLst0 != null);

		logger.debug("matches: " + tLst0.size());
		
		for(BioFuzzParseTree t : tLst0) {
			logger.debug("SIZE" + tLst0.size());
			assert(t != null);
			logger.debug(t.toString());
			break;
		}
		
		
//		BioFuzzTracer tracer = new BioFuzzTracer();
//		
//		BioFuzzQuery qColumns = new BioFuzzQuery() {
//			public Boolean condition(BioFuzzParseNode node) {
//				return node.getAtagType() == TagType.NON_TERMINAL && node.getAtagName().equals("col_name");
//			}
//		};
//		
//		BioFuzzQuery term = new BioFuzzQuery() {
//			public Boolean condition(BioFuzzParseNode node) {
//				return node.getAtagType() == TagType.TERMINAL || node.getAtagType() == TagType.REGEXP;
//			}
//		};
//		
//	
//		
//		List<BioFuzzParseNode> node = tracer.doTrace(tLst0.get(0), qColumns, TraceType.DFS);
//		List<BioFuzzParseNode> res = new Vector<BioFuzzParseNode>();
//		
//		for(BioFuzzParseNode nod : node) {
//			tracer.doTraceSubNodes(nod, term,TraceType.DFS);
//		}
//		
//		
		
		
//		logger.debug("SIZE" + tLst0.size());
//
//		BioFuzzParseTree t0 = tLst0.get(0);
//
//		this.mgr.extend(t0);
//		
//		this.mgr.mutate(t0);
//		
//		logger.debug(t0.toString());
//		
		
	}

}
