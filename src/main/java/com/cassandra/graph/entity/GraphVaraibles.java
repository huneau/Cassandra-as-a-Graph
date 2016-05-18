package com.cassandra.graph.entity;

import java.util.Optional;
import java.util.Set;
import org.apache.tinkerpop.gremlin.structure.Graph;

/**
 *
 * @author rhuneau
 */
public class GraphVaraibles implements Graph.Variables {

    @Override
    public Set<String> keys() {
	throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public <R> Optional<R> get(String key) {
	throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void set(String key, Object value) {
	throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(String key) {
	throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
