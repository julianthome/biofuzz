package com.crawljax.plugins.biofuzz.test;

import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class TestBioFuzzIndividualComparison {


	final static Logger logger = LoggerFactory.getLogger(TestBioFuzzIndividualComparison.class);
	
	private boolean match() {

		
		String rexp = "\\QUPDATE schoolinfo SET schoolname = \"\\E(.*)\\Q\", address = '1,Street', phonenumber = " +
				"'52365895', sitetext = '', sitemessage = '', numsemesters = '0', numperiods = '0', " +
				"apoint = '0.0', bpoint = '0.0', cpoint = '0.0', dpoint = '0.0', fpoint = '0.0' where schoolname = '387849 CONCAT(CHAR(34)) ' LIMIT 1 \\E|(.*)";


		String terms = "\n(.*=.*)\n";
		
		String orig = "UPDATE schoolinfo SET schoolname = \"alter \", " +
				"address = '1,Street', phonenumber = '52365895', sitetext = '', " +
				"sitemessage = '', numsemesters = '0', numperiods = '0', " +
				"apoint = '0.0', bpoint = '0.0', cpoint = '0.0', dpoint = '0.0', " +
				"fpoint = '0.0' where schoolname = '387849 CONCAT(CHAR(34)) ' LIMIT 1 ";
		
		HashMap<String,List<String>> pat = new HashMap<String,List<String>>();
		
		String termSplit = rexp.replaceAll(" *([^ ,=]* *= *['\"][^'\"=]*['\"])[ *,?]", "\t$1\n");
		termSplit = termSplit.replaceAll("[^\t]*\t(.*)\n[^\n]*", "$1\n");
		
		String [] cols = termSplit.split("\n");
		
		for(String col : cols) {
			String key = col.replaceAll("([^=]*)=.*", "$1");
			logger.debug("col: " + col);
			if(pat.containsKey(key)) {
				pat.get(key).add(col);
			} else {
				List <String> v = new Vector<String>();
				v.add(col);
				pat.put(key,v);
			}
		}
		
		for(String key : pat.keySet()) {
			List<String> vals = pat.get(key);
			logger.debug("Key: " + key);
			for(String val : vals) {
				logger.debug(" -- vals: " + val); 
			}
		}
		
		//logger.debug(termSplit);
		
		Pattern grp = Pattern.compile(rexp,Pattern.MULTILINE);
		
		Matcher grpMatch = grp.matcher(orig);
		grpMatch.find();
		MatchResult r = grpMatch.toMatchResult();
		

		for(int i = 0; i < grpMatch.groupCount(); i++) {
			logger.debug("Group: " + i + " " +  grpMatch.group(i));
		}
		
		
		Pattern trm = Pattern.compile(terms,Pattern.MULTILINE);
		
		Matcher trms = trm.matcher(termSplit);
		trms.find();
		MatchResult trmR = trms.toMatchResult();
		

//		for(int i = 0; i < grpMatch.groupCount(); i++) {
//			logger.debug("Group: " + i + " " +  grpMatch.group(i));
//		}
//		
//		for(int i = 0; i < trmR.groupCount(); i++) {
//			logger.debug("trm: " + i + " " +  trmR.group(i));
//		}
//		
			
		return orig.matches(rexp);
		
	}
	
	
	@Test
	public void test() {
		assert(match() == true);
		
	}

}
