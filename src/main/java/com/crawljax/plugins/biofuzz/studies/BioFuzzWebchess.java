package com.crawljax.plugins.biofuzz.studies;

import com.crawljax.core.CrawljaxRunner;
import com.crawljax.core.configuration.CrawljaxConfiguration;
import com.crawljax.core.configuration.ProxyConfiguration;
import com.crawljax.core.configuration.CrawljaxConfiguration.CrawljaxConfigurationBuilder;
import com.crawljax.plugins.biofuzz.configuration.WebChessArdillaInputSpec;
import com.crawljax.plugins.biofuzz.configuration.WebChessInputSpec;
import com.crawljax.plugins.biofuzz.core.BioFuzzPlugin;
import com.crawljax.plugins.biofuzz.core.BioFuzzPluginConfig;
import com.crawljax.plugins.biofuzz.core.BioFuzzFitness.BioFuzzFitnessScalar;
import com.crawljax.plugins.biofuzz.input.BioFuzzInputSpecIface;


public class BioFuzzWebchess {
	

	private static BioFuzzInputSpecIface ispec = null;

	public static void main(String[] args) {
		CrawljaxConfigurationBuilder builder = CrawljaxConfiguration.builderFor("http://localhost/webchess9");
		builder.crawlRules().insertRandomDataInInputForms(false);
		
		ispec = new WebChessArdillaInputSpec();
		builder.crawlRules().setInputSpec(ispec.getInputSpec());
		//builder.crawlRules().click("input").withAttribute("type", "submit");
		builder.crawlRules().dontClick("a").underXPath("//FORM[@name='preferences']");
		
		builder.setMaximumStates(20);
		builder.setMaximumDepth(10);
		//CrawljaxConfiguration config = builder.build();
		BioFuzzPluginConfig bfCfg = new BioFuzzPluginConfig();
		
		bfCfg.setTribes(4);
		bfCfg.setMaxIter(10);
		bfCfg.setCrossoverCycle(2);
		bfCfg.setCrossoverCnt(2);
		bfCfg.setCrossoverOffset(4);
		bfCfg.setFscal(new BioFuzzFitnessScalar(1,1,1,1));

		BioFuzzPlugin plugin = new BioFuzzPlugin(ispec,bfCfg);
		plugin.startProxy();

			
		builder.setProxyConfig(ProxyConfiguration.manualProxyOn(plugin.getProxy().getHost(), plugin.getProxy().getPort()));
		
		CrawljaxRunner crawljax =
				new CrawljaxRunner(builder.build());
		
		crawljax.call(); 
		
		
	}
	

	

}
