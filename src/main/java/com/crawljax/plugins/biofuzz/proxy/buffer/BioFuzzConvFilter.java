package com.crawljax.plugins.biofuzz.proxy.buffer;


public interface BioFuzzConvFilter {
	
	public enum FilterType {
		UNDEF("UNDEF"),
		WHITELIST("WHITELIST"),
		BLACKLIST("BLACKLIST");
		
		private String desc;
		FilterType(String desc) {
			this.desc = desc;
		}
		
		@Override
		public String toString() {
			return this.desc;
		}
	};

	public boolean filter(BioFuzzConvPair cpair);
	
	public FilterType getType();
}