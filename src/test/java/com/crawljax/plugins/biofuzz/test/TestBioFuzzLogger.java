package com.crawljax.plugins.biofuzz.test;




import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestBioFuzzLogger {
	
	final static Logger logger = LoggerFactory.getLogger(TestBioFuzzLogger.class);

	



	@Test
	public void test() {
		//BioFuzzLogger log = BioFuzzLogger.getInstance("/tmp/logger.log");

		String s = "0 SELECT aperc, bperc, cperc, dperc, coursename FROM courses WHERE courseid = 1";
		
		s = s.replaceAll("[0-9]+ (.*)", "$1");
		
		String exp = "\\QINSERT INTO forum VALUES('', '\\E(.*)\\Q', '\\E.*\\Q', 'IzyMuxeJ', ' ylRJTbCf', '8987')\\E";
		String orig  = "INSERT INTO forum VALUES('', '839679 ' ', '1380638792', 'IzyMuxeJ', ' ylRJTbCf', '8987')";

		
		Pattern pat = Pattern.compile(exp,Pattern.MULTILINE);
		Matcher valMat = pat.matcher(orig);
		
		
		if(valMat.find()) {
			logger.debug(valMat.group(2));
			logger.debug("OK");
		} else {
			logger.debug("NOK");
		}
		
		
		logger.debug(s);
	}
}
