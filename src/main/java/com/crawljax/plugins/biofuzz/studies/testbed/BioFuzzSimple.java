package com.crawljax.plugins.biofuzz.studies.testbed;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.crawljax.core.CrawljaxRunner;
import com.crawljax.core.configuration.CrawljaxConfiguration;
import com.crawljax.core.configuration.CrawljaxConfiguration.CrawljaxConfigurationBuilder;
import com.crawljax.plugins.biofuzz.configuration.ElemataInputSpec;
import com.crawljax.plugins.biofuzz.configuration.SchoolMateInputSpec;
import com.crawljax.plugins.biofuzz.configuration.WebChessArdillaInputSpec;
import com.crawljax.plugins.biofuzz.configuration.testbed.BioFuzzSimpleInputSpec;
import com.crawljax.plugins.biofuzz.core.BioFuzzPlugin;
import com.crawljax.plugins.biofuzz.core.BioFuzzPluginConfig;
import com.crawljax.plugins.biofuzz.core.BioFuzzFitness.BioFuzzFitnessScalar;
import com.crawljax.plugins.biofuzz.input.BioFuzzInputSpecIface;
import com.crawljax.plugins.biofuzz.utils.BioFuzzUtils;


public class BioFuzzSimple {
	
	private static final Logger logger = LoggerFactory.getLogger(BioFuzzSimple.class);
	private static BioFuzzInputSpecIface ispec = null;

	public static void main(String[] args) {
		
		ispec = new BioFuzzSimpleInputSpec();

		BioFuzzPluginConfig bfCfg = new BioFuzzPluginConfig();
		
		bfCfg.setTribes(10);
		bfCfg.setMaxIter(100);
		bfCfg.setCrossoverCycle(3);
		bfCfg.setCrossoverCnt(8);
		bfCfg.setCrossoverOffset(10);
		bfCfg.setParserMatchMax(3);
		bfCfg.setApplyCrossover(true);
		bfCfg.setApplyMutation(false);
		
		bfCfg.setFscal(new BioFuzzFitnessScalar(1,2,2,3));


		BioFuzzPlugin plugin = new BioFuzzPlugin(ispec,bfCfg);
		
		CrawljaxConfigurationBuilder builder = BioFuzzUtils.getConfigBuilderFor("http://localhost/biofuzz-testbed/", ispec, plugin);
		builder.crawlRules().click("");
		CrawljaxConfiguration conf = builder.build();
		
		
		
		logger.debug(conf.toString());
		
		plugin.startProxy();

		CrawljaxRunner crawljax =
				new CrawljaxRunner(conf);
		
		crawljax.call(); 
		
		plugin.stopProxy();
		
	}

	

	

}
