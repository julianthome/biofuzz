package com.crawljax.plugins.biofuzz.studies;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.crawljax.condition.crawlcondition.CrawlCondition;
import com.crawljax.core.CrawljaxRunner;
import com.crawljax.core.configuration.CrawljaxConfiguration;
import com.crawljax.core.configuration.CrawljaxConfiguration.CrawljaxConfigurationBuilder;
import com.crawljax.plugins.biofuzz.configuration.ElemataInputSpec;
import com.crawljax.plugins.biofuzz.configuration.PHPAddressbookInputSpec;
import com.crawljax.plugins.biofuzz.configuration.SchoolMateInputSpec;
import com.crawljax.plugins.biofuzz.configuration.WebChessArdillaInputSpec;
import com.crawljax.plugins.biofuzz.core.BioFuzzPlugin;
import com.crawljax.plugins.biofuzz.core.BioFuzzPluginConfig;
import com.crawljax.plugins.biofuzz.core.BioFuzzFitness.BioFuzzFitnessScalar;
import com.crawljax.plugins.biofuzz.input.BioFuzzInputSpecIface;
import com.crawljax.plugins.biofuzz.utils.BioFuzzUtils;


public class BioFuzzPHPAddressbook {
	
	private static final Logger logger = LoggerFactory.getLogger(BioFuzzPHPAddressbook.class);
	private static BioFuzzInputSpecIface ispec = null;

	public static void main(String[] args) {
		
		ispec = new PHPAddressbookInputSpec();

		BioFuzzPluginConfig bfCfg = new BioFuzzPluginConfig();
		
		bfCfg.setTribes(10);
		bfCfg.setMaxIter(20);
		bfCfg.setCrossoverCycle(2);
		bfCfg.setCrossoverCnt(2);
		bfCfg.setCrossoverOffset(5);
		bfCfg.setParserMatchMax(2);
		
		bfCfg.setFscal(new BioFuzzFitnessScalar(1,2,2,3));

		BioFuzzPlugin plugin = new BioFuzzPlugin(ispec,bfCfg);
		
		CrawljaxConfigurationBuilder builder = BioFuzzUtils.getConfigBuilderFor("http://localhost/addressbook/", ispec, plugin);
		
//		builder.crawlRules().clickOnce(true);
//		builder.crawlRules().click("a").underXPath("//FORM[@name='dummy']");

		CrawljaxConfiguration conf = builder.build();
		
		
		plugin.startProxy();

		CrawljaxRunner crawljax =
				new CrawljaxRunner(conf);
		
		crawljax.call(); 
		
		plugin.stopProxy();
		
	}

	

	

}
