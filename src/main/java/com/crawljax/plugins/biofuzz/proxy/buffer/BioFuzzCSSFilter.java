package com.crawljax.plugins.biofuzz.proxy.buffer;

import org.owasp.webscarab.model.Request;

public class BioFuzzCSSFilter implements BioFuzzConvFilter {
	@Override
	public boolean filter(BioFuzzConvPair cpair) {
		
		assert(cpair != null);

		Request req = cpair.getRequest();
		
		if(req != null) {
			String ctype = req.getHeader("Content-Type");
			if(ctype != null && (ctype.contains("css"))) {
				byte[] content = req.getContent();
				if(content != null && content.length > 0) {	
					return true;
				}
			}
		}
	return false;
	}

	@Override
	public FilterType getType() {
		return FilterType.BLACKLIST;
	}
}
