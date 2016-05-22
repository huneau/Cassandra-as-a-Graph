package com.cassandra.graph.entity;

import com.cassandra.graph.client.CassandraGraphClient;
import com.cassandra.graph.client.CassandraGraphClient.CassandraDirection;
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

    private final String id;
    private final String label;
    private final Map<String, Object> properties;

    private static final CassandraGraphClient CLIENT = CassandraGraphClient.getInstance();

    public CassandraVertex(String id, String label, Map<String, Object> properties) {
	this.id = id;
	this.label = label;
	this.properties = properties;
    }

    public CassandraVertex(Map<String, Object> properties) {
	id = properties.containsKey(T.id) ? (String) properties.get(T.id) : UUID.randomUUID().toString();
	properties.remove(T.id);

	label = properties.containsKey(T.label) ? (String) properties.get(T.label) : Vertex.DEFAULT_LABEL;
	properties.remove(T.label);

	CLIENT.createVertex(id, label, properties);
	this.properties = properties;
    }

    @Override
    public Edge addEdge(String label, Vertex inVertex, Object... keyValues) {
	ElementHelper.legalPropertyKeyValueArray(keyValues);
	return new CassandraEdge(this, inVertex, label, ElementHelper.asMap(keyValues));
    }

    @Override
    public Iterator<Edge> edges(Direction direction, String... edgeLabels) {
	List<Edge> edges = Collections.EMPTY_LIST;
	CassandraDirection d;
	switch (direction) {
	    case BOTH:
		d = CassandraDirection.BOTH;
		break;
	    case IN:
		d = CassandraDirection.UNCONNECT;
		break;
	    case OUT:
		d = CassandraDirection.CONNECT;
		break;
	    default:
		d = CassandraDirection.CONNECT;
	}

	CassandraDirection dFinal = d;
	Arrays.asList(edgeLabels).stream().forEach(edgeLabel -> {
	    List<CassandraEdge> es = CLIENT.getNeighbours(this, dFinal, edgeLabel);
	    edges.addAll(es);
	});
	return edges.iterator();

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
	List<VertexProperty<V>> vertexProperties = new ArrayList<>();;
	if (propertyKeys.length == 0) {
	    properties.entrySet().forEach(entry -> {
		vertexProperties.add((VertexProperty<V>) new CassandraVertexProperty(this, entry.getKey(), (String) entry.getValue()));
	    });
	} else {
	    Arrays.asList(propertyKeys).stream().forEach(key -> {
		if (properties.containsKey(key)) {
		    vertexProperties.add((VertexProperty<V>) new CassandraVertexProperty(this, key, (String) properties.get(key)));
		}
	    });
	}
	return vertexProperties.iterator();
    }

    @Override
    public <V> VertexProperty<V> property(VertexProperty.Cardinality cardinality, String key, V value, Object... keyValues) {
	if (cardinality != VertexProperty.Cardinality.list) {
	    throw new UnsupportedOperationException("Not supported yet.");
	}
	CLIENT.addVertexProperty(this, key, value, Arrays.asList(keyValues));
	properties.put(key, value);
	return (VertexProperty<V>) new CassandraVertexProperty(this, key, (String) value);
    }

    @Override
    public void remove() {
	CLIENT.deleteVertex(this);
    }

    @Override
    public Iterator<Vertex> vertices(Direction direction, String... edgeLabels) {
	List<Vertex> vertices = new ArrayList<>();
	edges(direction, edgeLabels).forEachRemaining(edge -> {
	    Vertex v = edge.outVertex();
	    vertices.add(v);
	});
	return vertices.iterator();
    }

}
