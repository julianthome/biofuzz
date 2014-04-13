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


public class SchoolMateInputSpec implements BioFuzzInputSpecIface {

	
	public SchoolMateInputSpec() {
		;
	}

	public InputSpecification getInputSpec() {
		InputSpecification input = new InputSpecification();
		Form contactForm0 = new Form();
		contactForm0.field("username").setValues("test");
		contactForm0.field("password").setValues("test");
		input.setValuesInForm(contactForm0).beforeClickElement("button").withAttribute("value", "Login");
		return input;
	}

	
	public List<BioFuzzFieldInputSequence> getInputSequences() {
		
		List<BioFuzzFieldInputSequence> liseq = new Vector<BioFuzzFieldInputSequence> ();
		
		BioFuzzFieldInputSequence iseq0 = new BioFuzzFieldInputSequence("//FORM[@name='loginForm']","//FORM[@name='loginForm']");
		
		BioFuzzFieldInput i00 = new BioFuzzFieldInput(BioFuzzAction.GO_TO_URL, "http://localhost/schoolmate");
		BioFuzzFieldInput i01 = new BioFuzzFieldInput("//FORM[@name='login']//INPUT[@name='username']",BioFuzzAction.TEXT_INPUT, "marie");
		BioFuzzFieldInput i02 = new BioFuzzFieldInput("//FORM[@name='login']//INPUT[@name='password']",BioFuzzAction.TEXT_INPUT, "test");
		BioFuzzFieldInput i03 = new BioFuzzFieldInput("//FORM[@name='login']//INPUT[@type='submit']",BioFuzzAction.CLICK);

		
		iseq0.add(i00);
		iseq0.add(i01);
		iseq0.add(i02);
		iseq0.add(i03);

		
		BioFuzzFieldInputSequence iseq1 = new BioFuzzFieldInputSequence("//FORM[@name='loginForm']","//FORM[@name='loginForm']");
		
		BioFuzzFieldInput i10 = new BioFuzzFieldInput(BioFuzzAction.GO_TO_URL, "http://localhost/schoolmate");
		BioFuzzFieldInput i11 = new BioFuzzFieldInput("//FORM[@name='login']//INPUT[@name='username']",BioFuzzAction.TEXT_INPUT, "marie");
		BioFuzzFieldInput i12 = new BioFuzzFieldInput("//FORM[@name='login']//INPUT[@name='password']",BioFuzzAction.TEXT_INPUT, "test");
		BioFuzzFieldInput i13 = new BioFuzzFieldInput("//FORM[@name='login']//INPUT[@type='submit']",BioFuzzAction.CLICK);
		BioFuzzFieldInput i14 = new BioFuzzFieldInput("//FORM[@name='classes']//SELECT[@name='semester']//OPTION[@value='1']",BioFuzzAction.CLICK);
		BioFuzzFieldInput i15 = new BioFuzzFieldInput("//TABLE[@class='dynamiclist']//A",BioFuzzAction.CLICK);
		
		iseq1.add(i10);
		iseq1.add(i11);
		iseq1.add(i12);
		iseq1.add(i13);
		iseq1.add(i14);
		iseq1.add(i15);
		
		
		BioFuzzFieldInputSequence iseq2 = new BioFuzzFieldInputSequence("//FORM[@name='loginForm']","//FORM[@name='loginForm']");
		
		BioFuzzFieldInput i20 = new BioFuzzFieldInput(BioFuzzAction.GO_TO_URL, "http://localhost/schoolmate");
		BioFuzzFieldInput i21 = new BioFuzzFieldInput("//FORM[@name='login']//INPUT[@name='username']",BioFuzzAction.TEXT_INPUT, "egon");
		BioFuzzFieldInput i22 = new BioFuzzFieldInput("//FORM[@name='login']//INPUT[@name='password']",BioFuzzAction.TEXT_INPUT, "test");
		BioFuzzFieldInput i23 = new BioFuzzFieldInput("//FORM[@name='login']//INPUT[@type='submit']",BioFuzzAction.CLICK);
		BioFuzzFieldInput i24 = new BioFuzzFieldInput("//FORM[@name='classes']//SELECT[@name='semester']//OPTION[@value='1']",BioFuzzAction.CLICK);
		BioFuzzFieldInput i25 = new BioFuzzFieldInput("//TABLE[@class='dynamiclist']//A",BioFuzzAction.CLICK);
		BioFuzzFieldInput i26 = new BioFuzzFieldInput("//FORM[@name='classes']//INPUT[@value='Update']",BioFuzzAction.CLICK);

		
		iseq2.add(i20);
		iseq2.add(i21);
		iseq2.add(i22);
		iseq2.add(i23);
		iseq2.add(i24);
		iseq2.add(i25);
		iseq2.add(i26);
		
		
		
		BioFuzzFieldInputSequence iseq3 = new BioFuzzFieldInputSequence("//FORM[@name='loginForm']","//FORM[@name='loginForm']");
		
		BioFuzzFieldInput i30 = new BioFuzzFieldInput(BioFuzzAction.GO_TO_URL, "http://localhost/schoolmate");
		BioFuzzFieldInput i31 = new BioFuzzFieldInput("//FORM[@name='login']//INPUT[@name='username']",BioFuzzAction.TEXT_INPUT, "test");
		BioFuzzFieldInput i32 = new BioFuzzFieldInput("//FORM[@name='login']//INPUT[@name='password']",BioFuzzAction.TEXT_INPUT, "test");
		BioFuzzFieldInput i33 = new BioFuzzFieldInput("//FORM[@name='login']//INPUT[@type='submit']",BioFuzzAction.CLICK);
		BioFuzzFieldInput i34 = new BioFuzzFieldInput("//FORM[@name='admin']//A[1]",BioFuzzAction.CLICK);
		BioFuzzFieldInput i35 = new BioFuzzFieldInput("//FORM[@name='info']//INPUT[@type='button']",BioFuzzAction.CLICK);

		
		iseq3.add(i30);
		iseq3.add(i31);
		iseq3.add(i32);
		iseq3.add(i33);
		iseq3.add(i34);
		iseq3.add(i35);

		
		
		liseq.add(iseq0);
		liseq.add(iseq1);
		liseq.add(iseq2);
		liseq.add(iseq3);

		return liseq;
	}
	
	@Override
	public String boilDownDom(Document dom, Eventable ev) {
		return null;
	}

	@Override
	public BioFuzzSpecType getIdtInputSpec(BioFuzzFieldInput i) {
		return BioFuzzSpecType.FLD_NMOD;
	}


} 