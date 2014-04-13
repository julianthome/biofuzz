package com.crawljax.plugins.biofuzz.input;


public class BioFuzzProtocolInput implements BioFuzzInput {

	private BioFuzzParamTuple tup = null;
	private String val;
	
	public BioFuzzProtocolInput(BioFuzzParamTuple tup, String val) {
		this.tup = tup;
		this.val = val;
	}
	
	public BioFuzzProtocolInput(BioFuzzContent cont, BioFuzzProtocolInput in) {
		this.tup = new BioFuzzParamTuple(cont, in.tup);
		this.val = in.val;
	}
	
//	public BioFuzzProtocolInput(BioFuzzProtocolInput in) {
//		this.tup = in.tup;
//		this.val = in.val;
//	}
	
	@Override
	public String getInputValue() {
		return this.val;
	}

	@Override
	public void setInputValue(String val) {
		this.val = val;
	}
	
	public String getInputKey() {
		assert(this.tup != null);
		return this.tup.getKey();
	}
	
	public int getIdx() {
		return this.tup.getIdx();
	}
	
	public void submit() {
		assert(this.tup != null);
		this.tup.submit(this.val);
	}
	
	public void setInjectable() {
		this.tup.setInjectable();
	}
	
	
	public boolean isInjectable() {
		return this.tup.isInjectable();
	}
	
	
	public void rollback() {
		this.tup.rollback();
	}
	
	public String toString() {
		String s = "BioFuzzInput: (>>)";
		s += this.tup.toString() + " [" + val + "] ";
		s += "<<";
		
		return s;
	}

}
