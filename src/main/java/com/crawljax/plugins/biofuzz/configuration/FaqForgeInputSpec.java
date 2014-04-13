package com.crawljax.plugins.biofuzz.configuration;

import java.util.List;
import java.util.Vector;

import org.w3c.dom.Document;
import com.crawljax.core.configuration.Form;
import com.crawljax.core.configuration.InputSpecification;
import com.crawljax.core.state.Eventable;
import com.crawljax.plugins.biofuzz.input.BioFuzzFieldInput;
import com.crawljax.plugins.biofuzz.input.BioFuzzFieldInputSequence;
import com.crawljax.plugins.biofuzz.input.BioFuzzInputSpecIface;
import com.crawljax.plugins.biofuzz.input.BioFuzzFieldInput.BioFuzzAction;


public class FaqForgeInputSpec implements BioFuzzInputSpecIface {

	
	public FaqForgeInputSpec() {
		;
	}

	public InputSpecification getInputSpec() {
		InputSpecification input = new InputSpecification();
		Form contactForm0 = new Form();
		contactForm0.field("formuser").setValues("admin");
		contactForm0.field("formpassword").setValues("admin");
		input.setValuesInForm(contactForm0).beforeClickElement("button").withAttribute("value", "Login to FaqForge Admin Center");
		return input;
	}

	
	public List<BioFuzzFieldInputSequence> getInputSequences() {
//		List<BioFuzzFieldInputSequence> liseq = new  Vector<BioFuzzFieldInputSequence>();
//		
//		BioFuzzFieldInputSequence iseq = new BioFuzzFieldInputSequence("//FORM[@name='loginForm']","//FORM[@name='loginForm']");
//		
//		BioFuzzFieldInput i0 = new BioFuzzFieldInput(BioFuzzAction.GO_TO_URL, "http://localhost/faqforge/");
//		BioFuzzFieldInput i1 = new BioFuzzFieldInput("//TABLE//SMALL//A[1]",BioFuzzAction.CLICK);
//		
//		iseq.add(i0);
//		iseq.add(i1);
//
//		liseq.add(iseq);
//		
//		return liseq;
		return null;
		
	}
	
	@Override
	public String boilDownDom(Document doc, Eventable ev) {
		return null;
	}

	@Override
	public BioFuzzSpecType getIdtInputSpec(BioFuzzFieldInput i) {
		for(String elementId: i.getElementIds()) {
			
			if(elementId.matches(".*/FORM\\[.*@name=\\'adminLoginForm\\'.*\\].*/INPUT.*\\[.*@name=\\'formuser\\'.*\\]")) {
				i.setInputValue("admin");
				return BioFuzzSpecType.FLD_MOD;
			}
			
			
			if(elementId.matches(".*/FORM\\[.*@name=\\'adminLoginForm\\'.*\\].*/INPUT.*\\[.*@name=\\'formpassword\\'.*\\]")) {
				i.setInputValue("admin");
				return BioFuzzSpecType.FLD_MOD;
			}
			
		}
		return BioFuzzSpecType.FLD_NMOD;
	}


} 