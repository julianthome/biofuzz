package com.crawljax.plugins.biofuzz.input;

import java.util.List;
import java.util.Vector;

public class BioFuzzFieldInputSequence {
	List<BioFuzzFieldInput> iseq;
	private String formPath;
	private String submitPath;
	
	public BioFuzzFieldInputSequence(String formPath, String submitPath) {
		this.iseq = new Vector<BioFuzzFieldInput>();
		this.formPath = formPath;
		this.submitPath = submitPath;
	}
	public BioFuzzFieldInputSequence() {
		this.iseq = new Vector<BioFuzzFieldInput>();
	}
	
	public void add(BioFuzzFieldInput in) {
		this.iseq.add(in);
	}
	
	public BioFuzzFieldInput get(int idx) {
		return iseq.get(idx);
	}
	
	public int getSize() {
		return this.iseq.size();
	}
	
	public String getFormPath() {
		return this.formPath;
	}
	
	public String getSubmitPath() {
		return this.submitPath;
	}
	
}
