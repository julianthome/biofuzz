package com.crawljax.plugins.biofuzz.core.components;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.channels.FileChannel;

import mx4j.log.Logger;


public class BioFuzzLogger {

	private static BioFuzzLogger inst = null;
	private static String fname = null;

	public static BioFuzzLogger getInstance(String fname) {
		if (inst == null) {
			inst = new BioFuzzLogger(fname);
		} 
		return inst;
	}

	private BioFuzzLogger(String f) {
		fname = f;
	}
	
	public String cut() {
		String s = read();
		reset();
		return s;
	}
	
	public void reset() {
		//write("");
		try {
			FileOutputStream fos = new FileOutputStream(fname, true);
			FileChannel chan = fos.getChannel();
			chan.truncate(0);
			chan.close();
			fos.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public void write(String s) {
		FileWriter fw = null;
		BufferedWriter bw = null;
		try {
			fw = new FileWriter(fname);
			bw = new BufferedWriter(fw);
			bw.write(s);
			bw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fw != null) {
				try {
					fw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	public String read() {
		String s = "";
		FileReader fr = null;
		BufferedReader br = null;
		String line = "";
		try {
			fr = new FileReader(fname);
			
			if(fr.ready() == false)
				return "";
			
			br = new BufferedReader(fr);
			
			while ((line = br.readLine()) != null) {
				
				
				if (line != null && !line.matches("([0-9]+) .*")) {
					line = line.trim();					
					s += " " + line;
				} else {
					if(s.length() > 0)
						s+="\n";
					s += line;
				}
			}
		} catch (FileNotFoundException e) {
			throw new RuntimeException("File not found");
		} catch (IOException e) {
			throw new RuntimeException("IO Error occured");
		} finally {
			if (fr != null) {
				try {
					fr.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return s;
	}


}
