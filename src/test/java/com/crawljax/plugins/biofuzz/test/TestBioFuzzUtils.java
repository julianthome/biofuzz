package com.crawljax.plugins.biofuzz.test;

import org.apache.commons.lang.RandomStringUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.crawljax.plugins.biofuzz.utils.BioFuzzUtils;

public class TestBioFuzzUtils {

	final static Logger logger = LoggerFactory.getLogger(TestBioFuzzUtils.class);
	
	public String [] t (String s) {
		s = s.replaceAll(" +", "\n");
		
		logger.debug(s);
		s = s.replaceAll(",", "\n,\n");
		s = s.replaceAll("=", "\n=\n");
		s = s.replaceAll(">", "\n>\n");
		s = s.replaceAll("<", "\n<\n");
		s = s.replaceAll("\\(", "\n\\(\n");
		s = s.replaceAll("\\)", "\n\\)\n");
		
		
		s = s.replaceAll("\'", "\n\'\n");
		
		s = s.replaceAll("\n<\n+>\n", "\n<>\n");

		s = s.replaceAll("[Nn][Oo][Ww][ \n]*\\([ \n]*\\)", "\nNOW\\(\\)\n");
		
		
		s = s.replaceAll("< *>", "\n<>\n");
		s = s.replaceAll("\n[Oo][Rr][Dd][Ee][Re]\n[Bb][Yy]\n", "\nORDER BY\n");
		//s = s.replaceAll(" ", "\n");
		s = s + "\n$";
		
		return s.split("\n");
	}
	
	@Test
	public void test() {
		assert(BioFuzzUtils.getStrDist("hello", "hallo") == 1);
		assert(BioFuzzUtils.getStrDist("aa", "bb") == 2);
		assert(BioFuzzUtils.getStrDist("that is a test", "") == 14);
		assert(BioFuzzUtils.getStrDist("", "") == 0);
		assert(BioFuzzUtils.getStrDist("aaa", "aaa") == 0);
		
		String a = RandomStringUtils.randomAlphanumeric(32000);
		String b = RandomStringUtils.randomAlphanumeric(32000);
		 
		
		logger.debug("a: " + a);
		logger.debug("b: " + b);
		
		//logger.debug("Dist: " + BioFuzzUtils.getNormalziedStrDist(a, b));
		logger.debug("Dist1: " + BioFuzzUtils.getNormalziedStrDist("aaa", "aaa"));
		logger.debug("Dist2: " + BioFuzzUtils.getNormalziedStrDist("aaaa", "aabb"));
		
		String[] q = t("Hallo welt das ist ein Test <> ");
		
		for(String m : q) {
			logger.debug("M: " + m);
		}
		
	}

}
