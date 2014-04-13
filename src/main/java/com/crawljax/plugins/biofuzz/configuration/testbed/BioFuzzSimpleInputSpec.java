package com.crawljax.plugins.biofuzz.configuration.testbed;


import java.util.List;
import java.util.Vector;

import org.w3c.dom.Document;

import com.crawljax.core.configuration.InputSpecification;
import com.crawljax.core.state.Eventable;
import com.crawljax.plugins.biofuzz.input.BioFuzzFieldInput;
import com.crawljax.plugins.biofuzz.input.BioFuzzFieldInputSequence;
import com.crawljax.plugins.biofuzz.input.BioFuzzInputSpecIface;
import com.crawljax.plugins.biofuzz.input.BioFuzzFieldInput.BioFuzzAction;

public class BioFuzzSimpleInputSpec implements BioFuzzInputSpecIface {

	public BioFuzzSimpleInputSpec() {
		;
	}

	public InputSpecification getInputSpec() {
		return null;
	}

	
	public List<BioFuzzFieldInputSequence> getInputSequences() {
		List<BioFuzzFieldInputSequence> liseq = new Vector<BioFuzzFieldInputSequence> ();
		
		BioFuzzFieldInputSequence iseq0 = new BioFuzzFieldInputSequence("","");
		
		BioFuzzFieldInput i00 = new BioFuzzFieldInput(BioFuzzAction.GO_TO_URL, "http://localhost/biofuzz-testbed/");
		BioFuzzFieldInput i01 = new BioFuzzFieldInput("/html/body/table[2]/tbody/tr[2]/td[1]/select/option[1]",BioFuzzAction.CLICK);
		BioFuzzFieldInput i02 = new BioFuzzFieldInput("/html/body/table[2]/tbody/tr[2]/td[2]/select/option[1]",BioFuzzAction.CLICK);
		BioFuzzFieldInput i03 = new BioFuzzFieldInput("/html/body/table[2]/tbody/tr[2]/td[3]/select/option[1]",BioFuzzAction.CLICK);
		BioFuzzFieldInput i04 = new BioFuzzFieldInput("/html/body/table[2]/tbody/tr[2]/td[4]/select/option[1]",BioFuzzAction.CLICK);
		BioFuzzFieldInput i05 = new BioFuzzFieldInput("/html/body/table[2]/tbody/tr[3]/td[3]/input[1]",BioFuzzAction.CLICK);
		
		iseq0.add(i00);
		iseq0.add(i01);
		iseq0.add(i02);
		iseq0.add(i03);
		iseq0.add(i04);
		iseq0.add(i05);

		
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