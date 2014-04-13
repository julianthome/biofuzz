package com.crawljax.plugins.biofuzz.input;

import java.util.List;
import java.util.Vector;

public class BioFuzzFieldInput implements BioFuzzInput {

	BioFuzzAction type;
	String inputValue;
	List<String> elementIds;

	public enum BioFuzzAction {
		CLICK("CLICK"),
		TEXT_INPUT("TEXT_INPUT"),
		GO_TO_URL("GO_TO_URL");
		
		private String desc;
		
		BioFuzzAction(String desc) {
			this.desc = desc;
		}
		
		@Override
		public String toString() {
			return this.desc;
		}
	};
	
	public BioFuzzFieldInput(BioFuzzFieldInput in) {
		this.type = in.type;
		this.inputValue = in.inputValue;
		this.elementIds = new Vector<String>();
		this.elementIds.addAll(in.elementIds);
	}
	
	public BioFuzzFieldInput(String elementId, BioFuzzAction type, String inputValue) {
		this.type = type;
		this.inputValue = inputValue;
		this.elementIds = new Vector<String>();
		this.elementIds.add(elementId);
	}		
	
	public BioFuzzFieldInput(String elementId, BioFuzzAction type) {
		this.type = type;
		this.inputValue = "";
		this.elementIds = new Vector<String>();
		this.elementIds.add(elementId);
	}
	
	public BioFuzzFieldInput(BioFuzzAction type, String inputValue) {
		this.type = type;
		this.inputValue = inputValue;
	}
	
	public BioFuzzAction getAction() {
		return this.type;
	}
	
	public String getInputValue() {
		return this.inputValue;
	}
	
	public List<String> getElementIds() {
		return this.elementIds;
	}
	
	public String getElementId( int x ) {
		assert(x >= 0 && x < this.elementIds.size());
		return this.elementIds.get(x);
	}
	
	public void setInputValue(String i) {
		this.inputValue = i;
	}
	
	public void addElementId(String elementId) {
		this.elementIds.add(elementId);
	}
	
	public String toString() {
		
		String s = "";
		
		s += "Input: ................. \n";
		s += "  ElementIds: " + this.elementIds + "\n";
		s += "  InputValue: " + this.inputValue + "\n";
		s += "  Type: " + this.type + "\n";
		
		s += "........................ \n";
		return s;
	}
	
}
