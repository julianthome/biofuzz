package com.crawljax.plugins.biofuzz.configuration;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Vector;

import org.apache.commons.lang.RandomStringUtils;
import org.w3c.dom.Document;
import com.crawljax.core.configuration.Form;
import com.crawljax.core.configuration.InputSpecification;
import com.crawljax.core.state.Eventable;
import com.crawljax.plugins.biofuzz.input.BioFuzzFieldInput;
import com.crawljax.plugins.biofuzz.input.BioFuzzFieldInputSequence;
import com.crawljax.plugins.biofuzz.input.BioFuzzInputSpecIface;
import com.crawljax.plugins.biofuzz.input.BioFuzzFieldInput.BioFuzzAction;


public class PHPAddressbookInputSpec implements BioFuzzInputSpecIface {

	
	public PHPAddressbookInputSpec() {
		;
	}

	public InputSpecification getInputSpec() {
		
		InputSpecification input = new InputSpecification();
		
		Form contactForm0 = new Form();
		contactForm0.field("user").setValues("admin@host.de");
		contactForm0.field("pass").setValues("admin");
		input.setValuesInForm(contactForm0).beforeClickElement("button").withAttribute("value", "Login");
		
		
		return input;
	}

	
	public List<BioFuzzFieldInputSequence> getInputSequences() {
		List<BioFuzzFieldInputSequence> liseq = new Vector<BioFuzzFieldInputSequence> ();
		
		BioFuzzFieldInputSequence iseq0 = new BioFuzzFieldInputSequence();
		BioFuzzFieldInputSequence iseq1 = new BioFuzzFieldInputSequence();
		BioFuzzFieldInputSequence iseq2 = new BioFuzzFieldInputSequence();
		BioFuzzFieldInputSequence iseq3 = new BioFuzzFieldInputSequence();
		
		// LOGIN
		BioFuzzFieldInput i00 = new BioFuzzFieldInput(BioFuzzAction.GO_TO_URL, "http://localhost/addressbook/index.php");
		BioFuzzFieldInput i01 = new BioFuzzFieldInput("//BODY//FORM//INPUT[1]",BioFuzzAction.TEXT_INPUT, "admin@host.de");
		BioFuzzFieldInput i02 = new BioFuzzFieldInput("//BODY//FORM//INPUT[2]",BioFuzzAction.TEXT_INPUT, "admin");
		BioFuzzFieldInput i03 = new BioFuzzFieldInput("//BODY//FORM//INPUT[3]",BioFuzzAction.CLICK);
		// Go to home
		BioFuzzFieldInput i04 = new BioFuzzFieldInput("//*[@id='nav']/ul/li[1]/a",BioFuzzAction.CLICK);
		// Edit address book entry
		BioFuzzFieldInput i05 = new BioFuzzFieldInput("//*[@id='maintable']/tbody/tr[4]/td[8]/a/img",BioFuzzAction.CLICK);
		BioFuzzFieldInput i06 = new BioFuzzFieldInput("//*[@id='content']/form[1]/input[1]",BioFuzzAction.CLICK);


		// Login
		iseq3.add(i00);iseq3.add(i01);iseq3.add(i02);iseq3.add(i03);
		
		// Go to home
		iseq0.add(i00);iseq0.add(i01);iseq0.add(i02);iseq0.add(i03);
		iseq0.add(i04);
		// Edit address
		iseq1.add(i00);iseq1.add(i01);iseq1.add(i02);iseq1.add(i03);
		iseq1.add(i04);iseq1.add(i05);
		// Delete
		iseq2.add(i00);iseq2.add(i01);iseq2.add(i02);iseq2.add(i03);
		iseq2.add(i04);iseq2.add(i05);iseq2.add(i06);

		
		
		BioFuzzFieldInputSequence iseq4 = new BioFuzzFieldInputSequence();
		// add new
		BioFuzzFieldInput i10 = new BioFuzzFieldInput("//*[@id='nav']/ul/li[2]/a",BioFuzzAction.CLICK);
		BioFuzzFieldInput i11 = new BioFuzzFieldInput("//*[@id='content']/form/textarea",BioFuzzAction.TEXT_INPUT, "test");
		BioFuzzFieldInput i12 = new BioFuzzFieldInput("//*[@id='content']/form/input[2]",BioFuzzAction.CLICK);
		
		iseq4.add(i00);iseq4.add(i01);iseq4.add(i02);iseq4.add(i03);
		iseq4.add(i10);iseq4.add(i11);iseq4.add(i12);
		
		
		BioFuzzFieldInputSequence iseq5 = new BioFuzzFieldInputSequence();
		// new group
		BioFuzzFieldInput i20 = new BioFuzzFieldInput("//*[@id='nav']/ul/li[3]/a",BioFuzzAction.CLICK);
		BioFuzzFieldInput i21 = new BioFuzzFieldInput("//*[@id='content']/form/input[1]",BioFuzzAction.CLICK);
		BioFuzzFieldInput i22 = new BioFuzzFieldInput("//*[@id='content']/form/input[1]",BioFuzzAction.TEXT_INPUT, "a");
		BioFuzzFieldInput i23 = new BioFuzzFieldInput("//*[@id='content']/form/textarea[1]",BioFuzzAction.TEXT_INPUT, "b");
		BioFuzzFieldInput i24 = new BioFuzzFieldInput("//*[@id='content']/form/textarea[2]",BioFuzzAction.TEXT_INPUT, "c");
		BioFuzzFieldInput i25 = new BioFuzzFieldInput("//*[@id='content']/form/textarea[2]",BioFuzzAction.TEXT_INPUT, "d");
		BioFuzzFieldInput i26 = new BioFuzzFieldInput("//*[@id='content']/form/input[2]",BioFuzzAction.CLICK);

		iseq5.add(i00);iseq5.add(i01);iseq5.add(i02);iseq5.add(i03);
		iseq5.add(i20);iseq5.add(i21);iseq5.add(i22);iseq5.add(i23);
		iseq5.add(i24);iseq5.add(i25);iseq5.add(i26);
		
		
		// next birtdays
		BioFuzzFieldInputSequence iseq6 = new BioFuzzFieldInputSequence();
		BioFuzzFieldInput i30 = new BioFuzzFieldInput("//*[@id='nav']/ul/li[3]/a",BioFuzzAction.CLICK);
		iseq6.add(i00);iseq6.add(i01);iseq6.add(i02);iseq6.add(i03);
		iseq6.add(i30);
		
		
		// print all
		BioFuzzFieldInputSequence iseq7 = new BioFuzzFieldInputSequence();
		BioFuzzFieldInput i40 = new BioFuzzFieldInput("//*[@id='nav']/ul/li[4]/a",BioFuzzAction.CLICK);
		iseq7.add(i00);iseq7.add(i01);iseq7.add(i02);iseq7.add(i03);
		iseq7.add(i30);iseq7.add(i40);
		
		
		// print phones
		BioFuzzFieldInputSequence iseq8 = new BioFuzzFieldInputSequence();
		BioFuzzFieldInput i50 = new BioFuzzFieldInput("//*[@id='nav']/ul/li[5]/a",BioFuzzAction.CLICK);
		iseq8.add(i00);iseq8.add(i01);iseq8.add(i02);iseq8.add(i03);
		iseq8.add(i30);iseq8.add(i50);
		
		// map
		BioFuzzFieldInputSequence iseq9 = new BioFuzzFieldInputSequence();
		BioFuzzFieldInput i60 = new BioFuzzFieldInput("//*[@id='nav']/ul/li[6]/a",BioFuzzAction.CLICK);
		iseq9.add(i00);iseq9.add(i01);iseq9.add(i02);iseq9.add(i03);
		iseq9.add(i30);iseq9.add(i60);
		

		// export
		BioFuzzFieldInputSequence iseq10 = new BioFuzzFieldInputSequence();
		BioFuzzFieldInput i70 = new BioFuzzFieldInput("//*[@id='nav']/ul/li[7]/a",BioFuzzAction.CLICK);
		iseq10.add(i00);iseq10.add(i01);iseq10.add(i02);iseq10.add(i03);
		iseq10.add(i30);iseq10.add(i70);
		
		
		// import
		BioFuzzFieldInputSequence iseq11 = new BioFuzzFieldInputSequence();
		BioFuzzFieldInputSequence iseq12 = new BioFuzzFieldInputSequence();
		BioFuzzFieldInput i80 = new BioFuzzFieldInput("//*[@id='nav']/ul/li[8]/a",BioFuzzAction.CLICK);
		BioFuzzFieldInput i81 = new BioFuzzFieldInput("//*[@id='content']/form/input[3]",BioFuzzAction.CLICK);
		iseq11.add(i00);iseq11.add(i01);iseq11.add(i02);iseq11.add(i03);
		iseq11.add(i30);iseq11.add(i80);
		iseq12.add(i00);iseq12.add(i01);iseq12.add(i02);iseq12.add(i03);
		iseq12.add(i30);iseq12.add(i80);iseq12.add(i81);
		
		
		// import
		BioFuzzFieldInputSequence iseq13 = new BioFuzzFieldInputSequence();
		BioFuzzFieldInput i90 = new BioFuzzFieldInput("//*[@id='nav']/ul/li[3]/a",BioFuzzAction.CLICK);
		BioFuzzFieldInput i91 = new BioFuzzFieldInput("//*[@id='content']/form/input[4]",BioFuzzAction.CLICK);
		BioFuzzFieldInput i92 = new BioFuzzFieldInput("//*[@id='content']/form/input[3]",BioFuzzAction.CLICK);
		BioFuzzFieldInput i93 = new BioFuzzFieldInput("//*[@id='content']/form/input[3]",BioFuzzAction.CLICK);
		iseq13.add(i00);iseq13.add(i01);iseq13.add(i02);iseq13.add(i03);
		iseq13.add(i90);iseq13.add(i91);iseq13.add(i92);iseq13.add(i93);

		
		liseq.add(iseq0);
		liseq.add(iseq1);
		liseq.add(iseq2);
		liseq.add(iseq3);
		liseq.add(iseq4);
		liseq.add(iseq5);
		liseq.add(iseq6);
		liseq.add(iseq7);
		liseq.add(iseq8);
		liseq.add(iseq9);
		liseq.add(iseq10);
		liseq.add(iseq11);
		liseq.add(iseq12);
		liseq.add(iseq13);
		
		
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