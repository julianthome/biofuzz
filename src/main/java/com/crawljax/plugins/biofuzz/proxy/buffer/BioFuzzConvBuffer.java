package com.crawljax.plugins.biofuzz.proxy.buffer;


import java.util.List;
import java.util.Vector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.crawljax.plugins.biofuzz.proxy.buffer.BioFuzzConvFilter.FilterType;


public class BioFuzzConvBuffer {

	private static final Logger logger = LoggerFactory.getLogger(BioFuzzConvBuffer.class);

	private List<BioFuzzConvPair> buffer = null;
	private int ptr = 0;
	private BioFuzzConvFilter filter = null;

	public BioFuzzConvBuffer() {
		this.filter = null;
		this.buffer = new Vector<BioFuzzConvPair>();
	}
	
	public BioFuzzConvBuffer(BioFuzzConvBuffer buf) {
		this.buffer = new Vector<BioFuzzConvPair>();
		this.filter = buf.filter;
		assert(this.buffer != null);
		

		for(BioFuzzConvPair cpair : buf.buffer) {
			this.buffer.add(new BioFuzzConvPair(cpair));
		}
	}
	
	public void clear() {
		this.buffer.clear();
	}
	
	public void add(BioFuzzConvPair npair) {
		
		if(this.filter == null) {
			logger.debug("No filter Specified - log everything");
			this.buffer.add(npair);
		}
		if(this.filter != null) {
			if(this.filter.getType() == FilterType.WHITELIST) {
				logger.debug("Log with whitelist");
				if(filter.filter(npair)) {
					this.buffer.add(npair);
				}
			} else if (this.filter.getType() == FilterType.BLACKLIST) {
				logger.debug("Log with blacklist");
				if(!filter.filter(npair)) {
					this.buffer.add(npair);
				}
			}
		} 
		logger.debug("... PTR: " + this.ptr);
	}
	
	public int getSize() {
		return this.buffer.size();
	}
	
	public void setFilter(BioFuzzConvFilter filter) {
		this.filter = filter;
	}
	
	public BioFuzzConvPair getLatest() {
		return this.buffer.get(this.buffer.size()-1);
	}
	
	public BioFuzzConvPair get(int idx) {
		assert(idx >=0 && idx < this.buffer.size());
		return this.buffer.get(idx);
	}
	
	public String toString() {

		String s = "BioFuzzConversation ------------------------START\n";
		synchronized(this) {
			for(BioFuzzConvPair pair : this.buffer) {
				s += pair.toString();
			}
		}
		s += "BioFuzzConversation ------------------------END\n";
		
		return s;
	}
	
	public BioFuzzConvPair[] toArray () {
		return (BioFuzzConvPair[])this.buffer.toArray();
	}

}
