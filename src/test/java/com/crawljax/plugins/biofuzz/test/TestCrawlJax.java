package com.crawljax.plugins.biofuzz.test;

import com.crawljax.plugins.biofuzz.studies.BioFuzzWebchessArdilla;
import org.junit.Test;

public class TestCrawlJax {

	@Test
	public void test() {

		System.setProperty("webdriver.gecko.driver",
				"/Users/julian/Downloads/geckodriver");

		BioFuzzWebchessArdilla.main(null);
	}

}
