package com.cassandra.graph.entity;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.tinkerpop.gremlin.structure.Direction;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.Property;
import org.apache.tinkerpop.gremlin.structure.Vertex;

/**
 *
 * @author rhuneau
 */
public class CassandraEdge implements Edge {

    private final Vertex vertex, vertexOut;

    private final String label;
    
    

    public CassandraEdge(Vertex vertexIn, Vertex vertexOut, String label) {
	this.vertex = vertexIn;
	this.vertexOut = vertexOut;
	this.label = label;

    }

    public CassandraEdge(Vertex vertexIn, Vertex vertexOut, String label, Map<String, Object> asMap) {
	throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Iterator<Vertex> vertices(Direction direction) {
	List<Vertex> vertices = Collections.EMPTY_LIST;
	switch (direction) {
	    case BOTH:
		vertices = Arrays.asList(vertexOut, vertex);
		break;
	    case IN:
		vertices = Arrays.asList(vertex);
		break;
	    case OUT:
		vertices = Arrays.asList(vertexOut);
		break;
	}
	return vertices.iterator();
    }

    @Override
    public <V> Iterator<Property<V>> properties(String... propertyKeys) {
	throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object id() {
	return vertex.id();
    }

    @Override
    public String label() {
	return label;
    }

    @Override
    public Graph graph() {
	throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public <V> Property<V> property(String key, V value) {
	throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove() {
	throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
