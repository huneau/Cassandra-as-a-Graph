package com.cassandra.graph.entity;

import com.cassandra.graph.client.CassandraGraphClient;
import java.util.ArrayList;
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
import org.apache.tinkerpop.gremlin.structure.util.ElementHelper;

/**
 *
 * @author rhuneau
 */
public class CassandraEdge implements Edge {

    private final Vertex fromVertex, toVertex;
    private final Map<String, Object> properties;
    private final String label;

    private static final CassandraGraphClient CLIENT = CassandraGraphClient.getInstance();

    public CassandraEdge(Vertex fromVertex, Vertex toVertex, String label, Map<String, Object> asMap) {
	CLIENT.createEdge(fromVertex.id().toString(), label, toVertex.id().toString(), asMap);
	this.properties = asMap;
	this.label = label;
	this.fromVertex = fromVertex;
	this.toVertex = toVertex;
    }

    public CassandraEdge(Vertex from, String to, String label, Map<String, Object> asMap) {
	CLIENT.getEdge((CassandraVertex) from, CassandraGraphClient.CassandraDirection.CONNECT, label, to);
	this.fromVertex = from;
	this.toVertex = CLIENT.getVertex(to).get();
	this.label = label;
	this.properties = asMap;
    }

    @Override
    public Iterator<Vertex> vertices(Direction direction) {
	List<Vertex> vertices = Collections.EMPTY_LIST;
	switch (direction) {
	    case BOTH:
		vertices = Arrays.asList(toVertex, fromVertex);
		break;
	    case IN:
		vertices = Arrays.asList(fromVertex);
		break;
	    case OUT:
		vertices = Arrays.asList(toVertex);
		break;
	}
	return vertices.iterator();
    }

    @Override
    public <V> Iterator<Property<V>> properties(String... propertyKeys) {
	List<Property<V>> edgeProperties = new ArrayList<>();
	if (propertyKeys.length == 0) {
	    properties.entrySet().forEach(entry -> {
		edgeProperties.add((Property<V>) new CassandraEdgeProperty(this, entry.getKey(), (String) entry.getValue()));
	    });
	} else {
	    Arrays.asList(propertyKeys).stream().forEach(key -> {
		if (properties.containsKey(key)) {
		    CassandraEdgeProperty property = new CassandraEdgeProperty(this, key, label);
		    edgeProperties.add((Property<V>) property);
		}
	    });
	}
	return edgeProperties.iterator();
    }

    @Override
    public Object id() {
	return fromVertex.id();
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
	ElementHelper.validateProperty(key, value);
	properties.put(key, value);
	CLIENT.addEdgeProperty(this, key, value);
	return (Property<V>) new CassandraEdgeProperty(this, key, (String) value);
    }

    @Override
    public void remove() {
	CLIENT.deleteEdge(this);
    }

    public Vertex from() {
	return fromVertex;
    }

    public Vertex to() {
	return fromVertex;
    }

}
