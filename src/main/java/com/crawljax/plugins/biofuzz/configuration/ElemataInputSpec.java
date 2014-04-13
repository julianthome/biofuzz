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


public class ElemataInputSpec implements BioFuzzInputSpecIface {

	
	public ElemataInputSpec() {
		;
	}

	public InputSpecification getInputSpec() {
		InputSpecification input = new InputSpecification();
		Form contactForm0 = new Form();
		contactForm0.field("username").setValues("admin");
		contactForm0.field("password").setValues("admin");
		input.setValuesInForm(contactForm0).beforeClickElement("button").withAttribute("value", "Login");
		return input;
	}

	
	public List<BioFuzzFieldInputSequence> getInputSequences() {
		List<BioFuzzFieldInputSequence> liseq = new Vector<BioFuzzFieldInputSequence> ();
		
		BioFuzzFieldInputSequence iseq0 = new BioFuzzFieldInputSequence("//FORM[@name='loginForm']","//FORM[@name='loginForm']");
		
		BioFuzzFieldInput i00 = new BioFuzzFieldInput(BioFuzzAction.GO_TO_URL, "http://localhost/elemata/admin/login.php");
		BioFuzzFieldInput i01 = new BioFuzzFieldInput("//*[@id='username']",BioFuzzAction.TEXT_INPUT, "admin");
		BioFuzzFieldInput i02 = new BioFuzzFieldInput("//*[@id='password']",BioFuzzAction.TEXT_INPUT, "admin");
		BioFuzzFieldInput i03 = new BioFuzzFieldInput("//*[@id='button']",BioFuzzAction.CLICK, "admin");
		BioFuzzFieldInput i04 = new BioFuzzFieldInput("/html/body/div/div[2]/div[1]/ul/a[3]",BioFuzzAction.CLICK);
		BioFuzzFieldInput i05 = new BioFuzzFieldInput("/html/body/div/div[3]/div/div/table/tbody/tr[1]/td[5]/div/a",BioFuzzAction.CLICK);
		BioFuzzFieldInput i06 = new BioFuzzFieldInput("/html/body/div/div[3]/div/p[2]/a[1]",BioFuzzAction.CLICK);
		
		iseq0.add(i00);
		iseq0.add(i01);
		iseq0.add(i02);
		iseq0.add(i03);
		iseq0.add(i04);
		iseq0.add(i05);
		iseq0.add(i06);
		liseq.add(iseq0);
		
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