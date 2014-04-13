package com.crawljax.plugins.biofuzz.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class BioFuzzLevenshtein {
	final static Logger logger = LoggerFactory.getLogger(BioFuzzLevenshtein.class);


	private BioFuzzLevenshtein(String s1, String s2) {
	}


	public static int getLdist(String s1, String s2) {
		//s1 = s1.toLowerCase();
		//s2 = s2.toLowerCase();

		int [] dist = new int[s2.length() + 1];
		for (int i = 0; i <= s1.length(); i++) {
			int lastValue = i;
			for (int j = 0; j <= s2.length(); j++) {
				if (i == 0)
					dist[j] = j;
				else {
					if (j > 0) {
						int newValue = dist[j - 1];
						if (s1.charAt(i - 1) != s2.charAt(j - 1))
							newValue = Math.min(Math.min(newValue, lastValue), dist[j]) + 1;
						dist[j - 1] = lastValue;
						lastValue = newValue;
					}
				}
			}
			if (i > 0)
				dist[s2.length()] = lastValue;
		}
		return dist[s2.length()];
	}

	
	public static double getNormalizedLdist(String s1, String s2) {
		//s1 = s1.toLowerCase();
		//s2 = s2.toLowerCase();

		double ldist = getLdist(s1,s2);
		
		double mlen = s1.length() > s2.length() ? s1.length() : s2.length();
		
		assert(mlen != 0.0);
		
		return ldist/mlen;
		
	}


}
