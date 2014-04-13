package com.crawljax.plugins.biofuzz.utils;


import java.io.IOException;

import java.util.logging.FileHandler;  
import java.util.logging.LogRecord;
import java.util.logging.Logger;  
import java.util.logging.SimpleFormatter;  

public class BioFuzzFileLogger extends SimpleFormatter {

	private Logger logger = null;
    private FileHandler fh = null;  
      
    public BioFuzzFileLogger(String logDir, String logFileName) {
    	logger = Logger.getLogger(logDir + logFileName);
    	try {  
            fh = new FileHandler(logDir + logFileName);  
            logger.addHandler(fh);  
            fh.setFormatter(this);  
        } catch (SecurityException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        
    }

    public void write(String logMessage) {
    	logger.info(logMessage);
    }
    
    public String format(LogRecord record) {
    	return record.getMessage() + "\r\n";
    }
   
}
