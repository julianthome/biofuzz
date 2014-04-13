package com.crawljax.plugins.biofuzz.studies;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.crawljax.core.CrawljaxRunner;
import com.crawljax.core.configuration.CrawljaxConfiguration;
import com.crawljax.core.configuration.CrawljaxConfiguration.CrawljaxConfigurationBuilder;
import com.crawljax.plugins.biofuzz.configuration.ElemataInputSpec;
import com.crawljax.plugins.biofuzz.configuration.SchoolMateInputSpec;
import com.crawljax.plugins.biofuzz.configuration.WebChessArdillaInputSpec;
import com.crawljax.plugins.biofuzz.core.BioFuzzPlugin;
import com.crawljax.plugins.biofuzz.core.BioFuzzPluginConfig;
import com.crawljax.plugins.biofuzz.core.BioFuzzFitness.BioFuzzFitnessScalar;
import com.crawljax.plugins.biofuzz.input.BioFuzzInputSpecIface;
import com.crawljax.plugins.biofuzz.utils.BioFuzzUtils;


public class BioFuzzElemata {
	
	private static final Logger logger = LoggerFactory.getLogger(BioFuzzElemata.class);
	private static BioFuzzInputSpecIface ispec = null;

	public static void main(String[] args) {
		
		ispec = new ElemataInputSpec();

		BioFuzzPluginConfig bfCfg = new BioFuzzPluginConfig();
		
		bfCfg.setTribes(10);
		bfCfg.setMaxIter(20);
		bfCfg.setCrossoverCycle(2);
		bfCfg.setCrossoverCnt(2);
		bfCfg.setCrossoverOffset(5);
		bfCfg.setParserMatchMax(2);
		
		bfCfg.setFscal(new BioFuzzFitnessScalar(1,2,2,3));


		BioFuzzPlugin plugin = new BioFuzzPlugin(ispec,bfCfg);
		
		CrawljaxConfigurationBuilder builder = BioFuzzUtils.getConfigBuilderFor("http://localhost/elemata/admin/login.php", ispec, plugin);
		builder.crawlRules().crawlFrames(true);
		builder.crawlRules().clickDefaultElements();

		builder.crawlRules().clickOnce(true);
		
		CrawljaxConfiguration conf = builder.build();
		
		
		
		logger.debug(conf.toString());
		
		plugin.startProxy();

		CrawljaxRunner crawljax =
				new CrawljaxRunner(conf);
		
		crawljax.call(); 
		
		plugin.stopProxy();
		
	}

	

	

}
