package com.crawljax.plugins.biofuzz.configuration;


import java.util.List;
import java.util.Vector;

import org.w3c.dom.Document;

import com.crawljax.core.configuration.InputSpecification;
import com.crawljax.core.state.Eventable;
import com.crawljax.plugins.biofuzz.input.BioFuzzFieldInput;
import com.crawljax.plugins.biofuzz.input.BioFuzzFieldInputSequence;
import com.crawljax.plugins.biofuzz.input.BioFuzzInputSpecIface;
import com.crawljax.plugins.biofuzz.input.BioFuzzFieldInput.BioFuzzAction;

public class GeccBBInputSpec implements BioFuzzInputSpecIface {

	public GeccBBInputSpec() {
		;
	}

	public InputSpecification getInputSpec() {
		return null;
	}

	
	public List<BioFuzzFieldInputSequence> getInputSequences() {
		List<BioFuzzFieldInputSequence> liseq = new Vector<BioFuzzFieldInputSequence> ();
		
		BioFuzzFieldInputSequence iseq0 = new BioFuzzFieldInputSequence("//FORM[@name='loginForm']","//FORM[@name='loginForm']");
		
		BioFuzzFieldInput i00 = new BioFuzzFieldInput(BioFuzzAction.GO_TO_URL, "http://localhost/geccBB_filter/");
		BioFuzzFieldInput i01 = new BioFuzzFieldInput("//*[@id='cornice']/ul/li[1]/a[2]",BioFuzzAction.CLICK);

		iseq0.add(i00);
		iseq0.add(i01);
		
		liseq.add(iseq0);
		return liseq;
	}
	
	@Override
	public String boilDownDom(Document doc, Eventable ev) {
		return null;
	}

	@Override
	public BioFuzzSpecType getIdtInputSpec(BioFuzzFieldInput i) {
		return BioFuzzSpecType.FLD_NMOD;
	}


} 