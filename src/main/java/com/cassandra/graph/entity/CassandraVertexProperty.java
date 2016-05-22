package com.cassandra.graph.entity;

import java.util.Iterator;
import java.util.NoSuchElementException;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.Property;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.structure.VertexProperty;

/**
 *
 * @author rhuneau
 */
public class CassandraVertexProperty implements VertexProperty<String>{
    
    private final String key;
    private final String value;
    private final Vertex parent;

    public CassandraVertexProperty(Vertex parent, String key, String value ) {
	this.key = key;
	this.value = value;
	this.parent = parent;
    }
    
    

    @Override
    public Vertex element() {
	return parent;
    }

    @Override
    public <U> Iterator<Property<U>> properties(String... propertyKeys) {
	throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
    public void remove() {
	throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object id() {
	throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String label() {
	throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Graph graph() {
	throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public <V> Property<V> property(String key, V value) {
	throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
