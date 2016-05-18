package com.cassandra.graph.entity;

import com.cassandra.graph.client.CassandraGraphClient;
import java.util.*;
import org.apache.tinkerpop.gremlin.structure.Direction;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.T;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.structure.VertexProperty;
import org.apache.tinkerpop.gremlin.structure.util.ElementHelper;

/**
 *
 * @author rhuneau
 */
public class CassandraVertex implements Vertex {

    private String id;
    private String label;

    public CassandraVertex(String id, String label) {
	this.id = id;
	this.label = label;
    }

    public CassandraVertex(Map<String, Object> properties) {
	id = properties.containsKey(T.id) ? (String) properties.get(T.id) : UUID.randomUUID().toString();
	properties.remove(T.id);

	label = properties.containsKey(T.label) ? (String) properties.get(T.label) : Vertex.DEFAULT_LABEL;
	properties.remove(T.label);

	CassandraGraphClient.getInstance().createVertex(id, label, properties);
    }

    public CassandraVertex() {
	this.id = UUID.randomUUID().toString();
    }

    @Override
    public Edge addEdge(String label, Vertex inVertex, Object... keyValues) {
	ElementHelper.legalPropertyKeyValueArray(keyValues);
	return new CassandraEdge(this, inVertex, label, ElementHelper.asMap(keyValues));
    }

    @Override
    public Iterator<Edge> edges(Direction direction, String... edgeLabels) {
	throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Graph graph() {
	throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object id() {
	return id;
    }

    @Override
    public String label() {
	return label;
    }

    @Override
    public <V> Iterator<VertexProperty<V>> properties(String... propertyKeys) {
	throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public <V> VertexProperty<V> property(VertexProperty.Cardinality cardinality, String key, V value, Object... keyValues) {
	throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove() {
	throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Iterator<Vertex> vertices(Direction direction, String... edgeLabels) {
	throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
