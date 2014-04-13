package com.crawljax.plugins.biofuzz.configuration;


import java.util.List;
import java.util.Vector;

import org.w3c.dom.Document;
import com.crawljax.core.configuration.Form;
import com.crawljax.core.configuration.InputSpecification;
import com.crawljax.core.state.Eventable;
import com.crawljax.plugins.biofuzz.input.BioFuzzFieldInput;
import com.crawljax.plugins.biofuzz.input.BioFuzzFieldInput.BioFuzzAction;
import com.crawljax.plugins.biofuzz.input.BioFuzzFieldInputSequence;
import com.crawljax.plugins.biofuzz.input.BioFuzzInputSpecIface;



public class WebChessInputSpec implements BioFuzzInputSpecIface {
	
	public WebChessInputSpec() {
		;
	}

	public InputSpecification getInputSpec() {
		InputSpecification input = new InputSpecification();
		Form contactForm0 = new Form();
		contactForm0.field("txtnick").setValues("webchess");
		contactForm0.field("pwdPassword").setValues("webchess");

		input.setValuesInForm(contactForm0).beforeClickElement("button").withAttribute("value", "Login");
		return input;
	}

	
	public List<BioFuzzFieldInputSequence> getInputSequences() {
		
		List<BioFuzzFieldInputSequence> liseq = new  Vector<BioFuzzFieldInputSequence>();
		
		BioFuzzFieldInputSequence iseq = new BioFuzzFieldInputSequence("//FORM[@name='loginForm']","//FORM[@name='loginForm']");
		
		BioFuzzFieldInput i0 = new BioFuzzFieldInput(BioFuzzAction.GO_TO_URL, "http://localhost/webchess");
		BioFuzzFieldInput i1 = new BioFuzzFieldInput("//FORM[@name='loginForm']//INPUT[@id='txtNick']",BioFuzzAction.TEXT_INPUT, "webchess");
		BioFuzzFieldInput i2 = new BioFuzzFieldInput("//FORM[@name='loginForm']//INPUT[@id='pwdPassword']",BioFuzzAction.TEXT_INPUT, "webchess");
		BioFuzzFieldInput i3 = new BioFuzzFieldInput("//FORM[@name='loginForm']//INPUT[@type='submit']",BioFuzzAction.CLICK);
		BioFuzzFieldInput i4 = new BioFuzzFieldInput(BioFuzzAction.GO_TO_URL, "http://localhost/webchess/sendmessage.php");
		
		iseq.add(i0);
		iseq.add(i1);
		iseq.add(i2);
		iseq.add(i3);
		iseq.add(i4);
		
		liseq.add(iseq);
		
		return liseq;
		
	}
	
	@Override
	public String boilDownDom(Document doc, Eventable ev) {
		
		String evId = ev.getElement().getAttributeOrNull("href");
		String evLogin = ev.getElement().getAttributeOrNull("name");
		
		if(evLogin != null && evLogin.equals("login")) {
			return null;
			//return bauto.xpathRealQuery(doc,"//FORM[@name='existingGames']//INPUT[@type='text' or @type='password' or @type='submit']");
		}
		
		if (evId == null){
			return null;
			//return bauto.xpathRealQuery(doc,"//FORM//INPUT[@type='text' or @type='password' or @type='submit']");
		}
		
		if (evId.equals("#continuegame")) {
			return null;
		} 
		if (evId.equals("#invitiations")) {
			return null;
		} 
		if (evId.equals("#messages")) {
			return null;
		} 
		if (evId.equals("#challenge")) {
			return null;
		} 
		if (evId.equals("#viewgame")) {
			return null;
		} 
		if (evId.equals("#preferences")) {
			//return bauto.xpathRealQuery(doc,"//FORM[@name='userdata']//INPUT[@type='text' or @type='password' or @type='submit']");
			//return null;
			return "//FORM[@name='userdata']";
		} 
		if (evId.equals("#personalinfo")) {
			//return bauto.xpathRealQuery(doc,"//FORM[@name='PersonalInfo']//INPUT[@type='text' or @type='password' or @type='button']");
			return null;
		} 
		
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
			
			
			if(elementId.matches(".*/FORM\\[.*@name=\\'userdata\\'.*\\].*/INPUT.*\\[.*@name=\\'txtReload\\'.*\\]")) {
				i.setInputValue("1234");
				return BioFuzzSpecType.FLD_MOD;
			}
		}
		return BioFuzzSpecType.FLD_NMOD;
	}


} 