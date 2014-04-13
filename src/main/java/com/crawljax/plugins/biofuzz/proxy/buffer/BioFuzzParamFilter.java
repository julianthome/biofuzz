package com.crawljax.plugins.biofuzz.proxy.buffer;

import org.owasp.webscarab.model.Request;

public class BioFuzzParamFilter implements BioFuzzConvFilter {
	
	@Override
	public boolean filter(BioFuzzConvPair cpair) {
		
		assert(cpair != null);

		Request req = cpair.getRequest();
		
		if(req != null) {
			String method = req.getMethod();
			
			if(method != null && method.matches("POST")) {
				String ctype = req.getHeader("Content-Type");
				if(ctype != null && (ctype.contains("application/x-www-form-urlencoded"))) {
					byte[] content = req.getContent();
					if(content != null && content.length > 0) {	
						return true;
					}
				}
			} else if(method != null && method.matches("GET")) {
				if(req.getURL() != null) {
					String p = req.getURL().getParameters();
					if(p != null && p.length() > 0) {
						return true;
					}
				}
			}
		}

	return false;
	}

	@Override
	public FilterType getType() {
		return FilterType.WHITELIST;
	}
}
