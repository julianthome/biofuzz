package com.crawljax.plugins.biofuzz.core;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Vector;

public class BioFuzzDBSchema {
	
	private SortedMap<String, List<String>> dbStruct = null;
	private Set<String> tables = null;
	private Set<String> columns = null;
	
	public BioFuzzDBSchema () {
		this.dbStruct = new TreeMap<String, List<String>>();
		this.tables = new HashSet<String>();
		this.columns = new HashSet<String>();
	}
	
	public BioFuzzDBSchema(BioFuzzDBSchema schema) {
		this();
		this.dbStruct.putAll(schema.dbStruct);
		this.tables.addAll(schema.tables);
		this.columns.addAll(schema.columns);
		
	}
	
	public boolean tablePresent(String tab) {
		return dbStruct.containsKey(tab);
	}
	
	public Set<String> getTables() {
		return this.tables;
	}
	
	public Set<String> getColumns() {
		return this.columns;
	}

	public boolean addTable(String tab) {
		
		if(!tablePresent(tab)) {
			this.tables.add(tab);
			dbStruct.put(tab, new Vector<String>());
			return true;
		}
		return false;
	}
	
	public int getSize() {
		return dbStruct.size();
	}
	

	public boolean addColumnToTable(String tab, String col) {
		
		if(!tablePresent(tab))
			return false;
		
		List<String> cols = ((List<String>) dbStruct.get(tab));
		
		assert(cols != null);
		
		if(cols.contains(col))
			return false;
		
		this.columns.add(col);
		cols.add(col);
		return true;
	}
	
	public String toString() {
		String s = "inferred db schema:-----------------------------BEGIN\n";
		for(String tab : this.dbStruct.keySet()) {
			List<String> cols = this.dbStruct.get(tab);
			s += "table: " + tab + " ( ";
			for (String col : cols) {
				s += col + " ";
			}
			s += ")\n";
		}
		s += "inferred db schema:-----------------------------END\n";
		return s;
	}

}
