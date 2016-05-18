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

    public enum CassandraDirection {
	IN, OUT, BI_DIRECTIONAL;
    }

    private final Session session;

    private CassandraGraphClient() {
	Cluster cluster = Cluster.builder()
		.withClusterName("myCluster")
		.addContactPoint("127.0.0.1")
		.build();
	session = cluster.connect();

    }

    public static CassandraGraphClient getInstance() {
	return new CassandraGraphClient();
    }

    public void createVertex(String id, String label, Map<String, Object> properties) {
	List<Statement> statements = new ArrayList<>();
	properties.put("", ""); // add empty key value
	properties.entrySet().stream().forEach(property -> {
	    Statement s = QueryBuilder.insertInto(VERTEX_TABLE).using().value("id", id).value("label", label).value(property.getKey(), property.getValue());
	    statements.add(s);
	});
	statements.forEach(s -> session.execute(s));
    }

    public Optional<CassandraVertex> getVertex(String id) {
	Statement s = QueryBuilder.select().from(VERTEX_TABLE).where(eq("id", id));
	ResultSet result = session.execute(s);
	List<Row> rows = result.all();
	if (!rows.isEmpty()) {
	    String label = rows.get(0).getString("label");
	    return Optional.ofNullable(new CassandraVertex(id, label));
	}
	return Optional.empty();
    }

    public Optional<CassandraEdge> getEdge(CassandraVertex vertex, CassandraDirection direction , String label, CassandraVertex VertexOut) {
	Statement s = QueryBuilder.select().from(EDGE_TABLE).where(eq("vin", vertex.id())).and(eq("vout", VertexOut.id()));
	ResultSet result = session.execute(s);
	List<Row> rows = result.all();
	if (!rows.isEmpty()) {
//	    String label = rows.get(0).getString("label");
	    return Optional.ofNullable(new CassandraEdge(vertex, VertexOut, label));
	}
	return Optional.empty();
    }

}
