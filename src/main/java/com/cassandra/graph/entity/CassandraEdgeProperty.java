/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cassandra.graph.entity;

import java.util.NoSuchElementException;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Element;
import org.apache.tinkerpop.gremlin.structure.Property;

/**
 *
 * @author rhuneau
 */
public class CassandraEdgeProperty implements Property<String>{
    
    private final String key;
    private final String value;
    private final Edge parent;

    public CassandraEdgeProperty(Edge parent, String key, String value) {
	this.key = key;
	this.value = value;
	this.parent = parent;
    }
    
    

    @Override
    public String key() {
	return key;
    }

    @Override
    public String value() throws NoSuchElementException {
	return value;
    }

    @Override
    public boolean isPresent() {
	throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Element element() {
	return parent;
    }

    @Override
    public void remove() {
	throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
