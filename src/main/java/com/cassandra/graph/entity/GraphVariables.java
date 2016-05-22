package com.cassandra.graph.entity;

import com.cassandra.graph.client.CassandraGraphClient;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.apache.tinkerpop.gremlin.structure.Graph;

/**
 *
 * @author rhuneau
 */
public class GraphVariables implements Graph.Variables {
    
    private static final CassandraGraphClient CLIENT = CassandraGraphClient.getInstance();
    private static final Map<String, String> PROPERTIES = CLIENT.loadVariable();

    @Override
    public Set<String> keys() {
	return PROPERTIES.keySet();
    }

    @Override
    public <R> Optional<R> get(String key) {
	return Optional.ofNullable((R) PROPERTIES.get(key));
    }

    @Override
    public void set(String key, Object value) {
	PROPERTIES.put(key, value.toString());
	CLIENT.setVariable(key, value.toString());
    }

    @Override
    public void remove(String key) {
	PROPERTIES.remove(key);
	CLIENT.deleteVariable(key);
    }
    
}
