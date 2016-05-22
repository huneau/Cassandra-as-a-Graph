package com.cassandra.graph.client;

import com.cassandra.graph.entity.CassandraEdge;
import com.cassandra.graph.entity.CassandraVertex;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.Statement;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import static com.datastax.driver.core.querybuilder.QueryBuilder.*;
import java.util.*;

/**
 *
 * @author rhuneau
 */
public class CassandraGraphClient {

    public static final String VERTEX_TABLE = "vertex";
    public static final String EDGE_TABLE = "edge";
    public static final String GRAPH_VARIABLE = "graph";

    private final Session session;

    private CassandraGraphClient() {
	Cluster cluster = Cluster.builder()
		.addContactPoint("127.0.0.1")
		.build();
	session = cluster.connect("Mykeyspace");

    }

    public static CassandraGraphClient getInstance() {
	return new CassandraGraphClient();
    }

    public void close() {
	session.close();
    }

    ////////////////////////////////////////////////////////////////////////////
    //////////////////////////// VERTEX ////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    public void createVertex(String id, String label, Map<String, Object> properties) {
	List<Statement> statements = new ArrayList<>();
	properties.put("", ""); // add empty key value
	properties.entrySet().stream().forEach(property -> {
	    Statement s = QueryBuilder.insertInto(VERTEX_TABLE).using().
		    value("id", id).
		    value("label", label).
		    value("key", property.getKey()).
		    value("value", property.getValue());
	    statements.add(s);
	});
	statements.forEach(s -> session.execute(s));
    }

    public Optional<CassandraVertex> getVertex(String id) {
	Statement s = QueryBuilder.select().all().from(VERTEX_TABLE).where(eq("id", id));
	ResultSet result = session.execute(s);
	List<Row> rows = result.all();
	Map<String, Object> properties = new HashMap<>();
	if (!rows.isEmpty()) {
	    rows.stream().forEach(row -> {
		String key = row.getString("key");
		String value = row.getString("value");
		if (!key.isEmpty()) {
		    properties.put(key, value);
		}
	    });
	    
	    String label = rows.get(0).getString("label");
	    return Optional.ofNullable(new CassandraVertex(id, label,  properties ));
	}
	return Optional.empty();
    }

    public void deleteVertex(CassandraVertex vertex) {
	Statement s = QueryBuilder.delete().all().from(VERTEX_TABLE).where(eq("id", vertex.id()));
	session.execute(s);
    }

    public void addVertexProperty(CassandraVertex vertex, String key, Object value, List<Object> subProperties) {
	Statement s = QueryBuilder.insertInto(VERTEX_TABLE).using().
		value("id", vertex.id()).
		value("label", vertex.label()).
		value("key", key).
		value("value", value).
		value("list", subProperties);
	session.execute(s);
    }

    ////////////////////////////////////////////////////////////////////////////
    //////////////////////////// EDGE //////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    public void createEdge(String fromVertex, String label, String toVertex, Map<String, Object> properties) {
	List<Statement> statements = new ArrayList<>();
	properties.put("", ""); // add empty key value
	properties.entrySet().stream().forEach(property -> {
	    Statement s1 = QueryBuilder.insertInto(EDGE_TABLE).using().
		    value("from", fromVertex).
		    value("connected", CassandraDirection.CONNECT.get()).
		    value("label", label).
		    value("to", toVertex).
		    value(property.getKey(), property.getValue());
	    Statement s2 = QueryBuilder.insertInto(EDGE_TABLE).using().
		    value("from", toVertex).
		    value("connected", CassandraDirection.UNCONNECT.get()).
		    value("label", label).
		    value("to", fromVertex).
		    value(property.getKey(), property.getValue());
	    statements.add(s1);
	    statements.add(s2);
	});
	statements.forEach(s -> session.execute(s));
    }

    public Optional<CassandraEdge> getEdge(CassandraVertex fromVertex, CassandraDirection direction, String label, String toVertex) {
	Statement s = QueryBuilder.select().all().from(EDGE_TABLE).
		where(eq("from", fromVertex.id())).
		and(eq("connected", direction.get())).
		and(eq("label", label)).
		and(eq("to", toVertex));
	ResultSet result = session.execute(s);
	List<Row> rows = result.all();
	Map<String, Object> properties = new HashMap<>();
	if (!rows.isEmpty()) {
	    rows.stream().forEach(row -> {
		String key = row.getString("key");
		String value = row.getString("value");
		if (!key.isEmpty()) {
		    properties.put(key, value);
		}
	    });

	    return Optional.ofNullable(new CassandraEdge(fromVertex, toVertex, label, properties));
	}
	return Optional.empty();
    }

    public List<CassandraEdge> getEdge(CassandraVertex fromVertex, CassandraDirection direction) {
	Map<String, Map<String, Object>> neighbours = new HashMap<>();
	Map<String, String> neighboursLabel = new HashMap<>();
	Statement s = QueryBuilder.select().all().from(EDGE_TABLE).
		where(eq("from", fromVertex.id())).
		and(eq("connected", direction.get()));
	ResultSet result = session.execute(s);
	List<Row> rows = result.all();
	if (!rows.isEmpty()) {
	    rows.stream().forEach(row -> {
		String label = row.getString("labels");
		String toVertex = row.getString("to");
		String key = row.getString("key");
		String value = row.getString("value");
		neighboursLabel.put(toVertex, label);
		if (!key.isEmpty()) {
		    Map<String, Object> properties = neighbours.get(toVertex);
		    if (properties == null) {
			properties = new HashMap<>();
			neighbours.put(toVertex, properties);
		    }
		    properties.put(key, value);
		}
	    });

	    List<CassandraEdge> es = new ArrayList<>();
	    neighbours.entrySet().forEach(neighbour -> {
		//TODO : remove the getvertex below
		CassandraEdge ce = new CassandraEdge(fromVertex, //from
			neighbour.getKey(), //to
			neighboursLabel.get(neighbour.getKey()), //label
			neighbour.getValue());//properties
		es.add(ce);
	    });

	    return es;
	}
	return Collections.EMPTY_LIST;
    }

    public List<CassandraEdge> getNeighbours(CassandraVertex fromVertex, CassandraDirection direction, String label) {
	Map<String, Map<String, Object>> neighbours = new HashMap<>();
	Statement s = QueryBuilder.select().all().from(EDGE_TABLE).
		where(eq("from", fromVertex)).
		and(eq("connected", direction.get())).
		and(eq("label", label));
	ResultSet result = session.execute(s);
	List<Row> rows = result.all();
	if (!rows.isEmpty()) {
	    rows.stream().forEach(row -> {
		String toVertex = row.getString("to");
		String key = row.getString("key");
		String value = row.getString("value");
		if (!key.isEmpty()) {
		    Map<String, Object> properties = neighbours.get(toVertex);
		    if (properties == null) {
			properties = new HashMap<>();
			neighbours.put(toVertex, properties);
		    }
		    properties.put(key, value);
		}
	    });

	    List<CassandraEdge> es = new ArrayList<>();
	    neighbours.entrySet().forEach(neighbour -> {
		CassandraEdge ce = new CassandraEdge(fromVertex, neighbour.getKey(), label, neighbour.getValue());
		es.add(ce);
	    });

	    return es;
	}
	return Collections.EMPTY_LIST;
    }

    public void deleteEdge(CassandraEdge edge) {
	Statement s1 = QueryBuilder.delete().all().from(EDGE_TABLE).
		where(eq("from", edge.from().id())).
		and(eq("connected", CassandraDirection.CONNECT.get())).
		and(eq("label", edge.label())).
		and(eq("to", edge.to().id()));
	Statement s2 = QueryBuilder.delete().all().from(EDGE_TABLE).
		where(eq("from", edge.to().id())).
		and(eq("connected", CassandraDirection.UNCONNECT.get())).
		and(eq("label", edge.label())).
		and(eq("to", edge.from().id()));
	session.execute(s1);
	session.execute(s2);
    }

    public void addEdgeProperty(CassandraEdge edge, String key, Object value) {
	Statement s = QueryBuilder.insertInto(VERTEX_TABLE).using().
		value("from", edge.from().id()).
		value("connected", CassandraDirection.CONNECT.get()).
		value("label", edge.label()).
		value("to", edge.to().id()).
		value("key", key).
		value("value", value);
	session.execute(s);
    }
    
    public void deleteEdgeProperty(CassandraEdge edge, String key){
	Statement s = QueryBuilder.delete().all().from(EDGE_TABLE).
		where(eq("from", edge.from().id())).
		and(eq("connected", CassandraDirection.CONNECT.get())).
		and(eq("label", edge.label())).
		and(eq("to", edge.to().id())).
		and(eq("key", key));
	session.execute(s);
    }

    ////////////////////////////////////////////////////////////////////////////
    ///////////////////////////// GRAPH ////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    public void setVariable(String key, String value) {
	Statement s = QueryBuilder.insertInto(GRAPH_VARIABLE).using().
		value("key", key).
		value("value", value);
	session.execute(s);

    }
    
    public Map<String,String> loadVariable(){
	Statement s = QueryBuilder.select().all().from(GRAPH_VARIABLE);
	List<Row> rows = session.execute(s).all();
	if(!rows.isEmpty()){
	    Map<String,String> properties = new HashMap<>();
	    rows.stream().forEach(row->{
		properties.put(row.getString("key"),row.getString("value"));
	    });
	    return properties;
	}
	return Collections.EMPTY_MAP;
    }
    
    public void deleteVariable(String key){
	Statement s = QueryBuilder.delete().all().from(GRAPH_VARIABLE).where(eq("key", key));
	session.execute(s);
    }

    public enum CassandraDirection {
	CONNECT(0), UNCONNECT(1), BOTH(2);

	private final int value;

	private CassandraDirection(int val) {
	    this.value = val;
	}

	public int get() {
	    return value;
	}
    }

}
