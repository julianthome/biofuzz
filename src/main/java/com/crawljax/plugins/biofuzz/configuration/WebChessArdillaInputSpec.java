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


public class WebChessArdillaInputSpec implements BioFuzzInputSpecIface {

	
	public WebChessArdillaInputSpec() {
		;
	}

	public InputSpecification getInputSpec() {
		
		InputSpecification input = new InputSpecification();
		
		Form contactForm0 = new Form();
		contactForm0.field("txtnick").setValues("webchess");
		contactForm0.field("pwdPassword").setValues("webchess");
		input.setValuesInForm(contactForm0).beforeClickElement("button").withAttribute("value", "Login");
		
		
		Form contactForm1 = new Form();
		contactForm1.field("pwdOldPassword").setValues("webchess");
		contactForm1.field("pwdPassword2").setValues("webchess");
		input.setValuesInForm(contactForm1).beforeClickElement("button").withAttribute("value", "Update Personal Info");
		
		Form contactForm2 = new Form();
		contactForm2.field("txtReload").setValues("1234");
		input.setValuesInForm(contactForm2).beforeClickElement("button").withAttribute("name", "txtReload");
		
		
		Form contactForm3 = new Form();
		contactForm3.field("txtFirstName").setValues(RandomStringUtils.randomAlphanumeric(5).toUpperCase());
		contactForm3.field("txtLastName").setValues(RandomStringUtils.randomAlphanumeric(5).toUpperCase());
		contactForm3.field("txtNick").setValues(RandomStringUtils.randomAlphanumeric(5).toUpperCase());
		
		input.setValuesInForm(contactForm3).beforeClickElement("button").withAttribute("name", "btnCreate");

		return input;
	}

	
	public List<BioFuzzFieldInputSequence> getInputSequences() {
		
		List<BioFuzzFieldInputSequence> liseq = new  Vector<BioFuzzFieldInputSequence>();
		
		BioFuzzFieldInputSequence iseq = new BioFuzzFieldInputSequence("//FORM[@name='loginForm']","//FORM[@name='loginForm']");


		BioFuzzFieldInput i0 = new BioFuzzFieldInput(BioFuzzAction.GO_TO_URL, "http://localhost/webchess9");
		BioFuzzFieldInput i1 = new BioFuzzFieldInput("//FORM//INPUT[@name='txtNick']",BioFuzzAction.TEXT_INPUT, "webchess");
		BioFuzzFieldInput i2 = new BioFuzzFieldInput("//FORM//INPUT[@name='pwdPassword']",BioFuzzAction.TEXT_INPUT, "webchess");
		BioFuzzFieldInput i3 = new BioFuzzFieldInput("//FORM//INPUT[@type='submit']",BioFuzzAction.CLICK);
		BioFuzzFieldInput i4 = new BioFuzzFieldInput("//FORM[@name='existingGames']//INPUT[@type='radio'][1]",BioFuzzAction.CLICK);
		BioFuzzFieldInput i5 = new BioFuzzFieldInput("//FORM[@name='existingGames']//A[1]",BioFuzzAction.CLICK);
		BioFuzzFieldInput i6 = new BioFuzzFieldInput("//FORM//INPUT[@name='pwdPassword']",BioFuzzAction.TEXT_INPUT, "webchess");
		BioFuzzFieldInput i7 = new BioFuzzFieldInput("//FORM//INPUT[@type='submit']",BioFuzzAction.CLICK);
		
		iseq.add(i0);
		iseq.add(i1);
		iseq.add(i2);
		iseq.add(i3);
		iseq.add(i4);
		iseq.add(i5);
		iseq.add(i6);
		iseq.add(i7);
		
		liseq.add(iseq);
		
		return liseq;

	}
	
	@Override
	public String boilDownDom(Document doc, Eventable ev) {
		return null;
	}

	@Override
	public BioFuzzSpecType getIdtInputSpec(BioFuzzFieldInput i) {
		// ATTENTION: be sure that no input is the prefix of another one !!!
		
		for(String elementId: i.getElementIds()) {
		
			if(elementId.matches(".*/FORM\\[.*@name=\\'PersonalInfo\\'.*\\].*/INPUT.*\\[.*@name=\\'pwdOldPassword\\'.*\\]")) {
				i.setInputValue("webchess");
				return BioFuzzSpecType.FLD_MOD;
			}
			
			// Mergeing two fields to one input
			if(elementId.matches(".*/FORM\\[.*@name=\\'PersonalInfo\\'.*\\].*/INPUT.*\\[.*@name=\\'pwdPassword\\'.*\\]")) {
				if(i.getElementIds().size() == 1) {
					i.addElementId(i.getElementId(0).replace("pwdPassword", "pwdPassword2"));
				}
				
				i.setInputValue("webchess");
				return BioFuzzSpecType.FLD_MOD;
			}
			
			// two individuals for asdt will be created
			if(elementId.matches(".*/FORM\\[.*@name=\\'PersonalInfo\\'.*\\].*/INPUT.*\\[.*@name=\\'pwdPassword2\\'.*\\]")) {
				// this id constraint is already met by the previous one
				i.setInputValue("webchess");
				return BioFuzzSpecType.FLD_DEP;
			}
			
			// Mergeing two fields to one input
			if(elementId.matches(".*/FORM\\[.*@name=\\'userdata\\'.*\\].*/INPUT.*\\[.*@name=\\'pwdPassword\\'.*\\]")) {
				if(i.getElementIds().size() == 1) {
					i.addElementId(i.getElementId(0).replace("pwdPassword", "pwdPassword2"));
				}
				
				i.setInputValue("webchess");
				return BioFuzzSpecType.FLD_MOD;
			}
			
			// two individuals for asdt will be created
			if(elementId.matches(".*/FORM\\[.*@name=\\'userdata\\'.*\\].*/INPUT.*\\[.*@name=\\'pwdPassword2\\'.*\\]")) {
				// this id constraint is already met by the previous one
				i.setInputValue("webchess");
				return BioFuzzSpecType.FLD_DEP;
			}
			
			
			if(elementId.matches(".*/FORM\\[.*@name=\\'userdata\\'.*\\].*/INPUT.*\\[.*@name=\\'txtReload\\'.*\\]")) {
				i.setInputValue("1234");
				return BioFuzzSpecType.FLD_MOD;
			}
			
			if(elementId.matches(".*/FORM\\[.*@name=\\'preferences\\'.*\\].*/INPUT.*\\[.*@name=\\'txtReload\\'.*\\]")) {
				i.setInputValue("1234");
				return BioFuzzSpecType.FLD_MOD;
			}
			
			if(elementId.matches(".*/FORM\\[.*@name=\\'userdata\\'.*\\].*/INPUT.*\\[.*@name=\\'txtNick\\'.*\\]")) {
				i.setInputValue(RandomStringUtils.randomAlphanumeric(5).toUpperCase());
				return BioFuzzSpecType.FLD_MOD;
			}
			
		}
		return BioFuzzSpecType.FLD_NMOD;
	}


} 