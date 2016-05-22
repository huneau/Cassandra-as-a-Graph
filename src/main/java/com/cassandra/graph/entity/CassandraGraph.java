package com.cassandra.graph.entity;

import com.cassandra.graph.client.CassandraGraphClient;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.configuration.Configuration;
import org.apache.tinkerpop.gremlin.process.computer.GraphComputer;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.Transaction;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.structure.util.ElementHelper;

/**
 *
 * @author rhuneau
 */
public class CassandraGraph implements Graph {

    private static final CassandraGraphClient CLIENT = CassandraGraphClient.getInstance();

    @Override
    public Vertex addVertex(Object... keyValues) {
	ElementHelper.legalPropertyKeyValueArray(keyValues);
	return new CassandraVertex(ElementHelper.asMap(keyValues));
    }

    @Override
    public void close() throws Exception {
	CLIENT.close();
    }

    @Override
    public <C extends GraphComputer> C compute(Class<C> graphComputerClass) throws IllegalArgumentException {
	throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public GraphComputer compute() throws IllegalArgumentException {
	throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Configuration configuration() {
	throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Iterator<Edge> edges(Object... edgeIds) {
	if (edgeIds.length == 0) {
	    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}
	List<Edge> edges = new ArrayList<>();
	Arrays.asList(edgeIds).stream().forEach(id -> {
	    CassandraVertex vertex = CLIENT.getVertex(id.toString()).get();
	    edges.addAll(CLIENT.getEdge(vertex, CassandraGraphClient.CassandraDirection.CONNECT));
	});
	return edges.iterator();
    }

    @Override
    public Transaction tx() {
	throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Variables variables() {
	throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Iterator<Vertex> vertices(Object... vertexIds) {
	if (vertexIds.length == 0) {
	    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}
	List<Vertex> vertices = new ArrayList<>();
	Arrays.asList(vertexIds).stream().forEach(id -> vertices.add(CLIENT.getVertex(id.toString()).get()));
	return vertices.iterator();
    }

    @Override
    public Features features() {
	return new CassandraFeatures();
    }

    /**
     * Reason :
     * {@link GraphFactory#open(org.apache.commons.configuration.Configuration)}
     *
     * @param c
     * @return
     */
    public static Graph open(Configuration c) {
	return new CassandraGraph();
    }

}
