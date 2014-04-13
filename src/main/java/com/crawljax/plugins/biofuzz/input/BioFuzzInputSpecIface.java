package com.crawljax.plugins.biofuzz.input;

import java.util.List;
import java.util.Vector;

import org.w3c.dom.Document;

import com.crawljax.core.configuration.InputSpecification;
import com.crawljax.core.state.Eventable;


public interface BioFuzzInputSpecIface {

	public enum BioFuzzSpecType {
		FLD_MOD("FLD_MOD"),
		FLD_NMOD("FLD_NMOD"),
		FLD_DEP("FLD_DEP");
		
		private String desc;
		
		BioFuzzSpecType(String desc) {
			this.desc = desc;
		}
		
		@Override
		public String toString() {
			return this.desc;
		}
	};

	public abstract String boilDownDom (Document dom, Eventable ev);
	public abstract InputSpecification getInputSpec();
	public abstract List<BioFuzzFieldInputSequence> getInputSequences();
	public abstract BioFuzzSpecType getIdtInputSpec(BioFuzzFieldInput i);

}
