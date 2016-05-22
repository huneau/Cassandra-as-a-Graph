package com.cassandra.graph.entity;

import org.apache.tinkerpop.gremlin.structure.Graph;

/**
 *
 * @author rhuneau
 */
public class CassandraFeatures implements Graph.Features {

    @Override
    public EdgeFeatures edge() {
	return new CassandraEdgeFeatures();
    }

    @Override
    public GraphFeatures graph() {
	return new CassandraGraphFeatures();
    }

    @Override
    public VertexFeatures vertex() {
	return new CassandraVertexFeatures();
    }

    public class CassandraEdgeFeatures extends CassandraElementFeatures implements EdgeFeatures {

	@Override
	public EdgePropertyFeatures properties() {
	    return new CassandraEdgePropertyFeatures();
	}

    }

    public class CassandraGraphFeatures implements GraphFeatures {

    }

    public class CassandraVertexFeatures extends CassandraElementFeatures implements VertexFeatures {

	@Override
	public boolean supportsMetaProperties() {
	    return false;
	}

	@Override
	public boolean supportsMultiProperties() {
	    return false;
	}

	@Override
	public VertexPropertyFeatures properties() {
	    return new CassandraVertexPropertyFeatures();
	}

    }

    public class CassandraEdgePropertyFeatures extends CassandraDataTypeFeatures implements EdgePropertyFeatures {

    }

    public class CassandraVertexPropertyFeatures extends CassandraDataTypeFeatures implements VertexPropertyFeatures {

	@Override
	public boolean supportsUserSuppliedIds() {
	    return false;
	}

    }

    public class CassandraDataTypeFeatures implements DataTypeFeatures {

	@Override
	public boolean supportsBooleanArrayValues() {
	    return false;
	}

	@Override
	public boolean supportsBooleanValues() {
	    return false;
	}

	@Override
	public boolean supportsByteValues() {
	    return false;
	}

	@Override
	public boolean supportsDoubleValues() {
	    return false;
	}

	@Override
	public boolean supportsFloatValues() {
	    return false;
	}

	@Override
	public boolean supportsIntegerValues() {
	    return false;
	}

	@Override
	public boolean supportsLongValues() {
	    return false;
	}

	@Override
	public boolean supportsMapValues() {
	    return false;
	}

	@Override
	public boolean supportsMixedListValues() {
	    return false;
	}

	@Override
	public boolean supportsByteArrayValues() {
	    return false;
	}

	@Override
	public boolean supportsDoubleArrayValues() {
	    return false;
	}

	@Override
	public boolean supportsFloatArrayValues() {
	    return false;
	}

	@Override
	public boolean supportsIntegerArrayValues() {
	    return false;
	}

	@Override
	public boolean supportsStringArrayValues() {
	    return false;
	}

	@Override
	public boolean supportsLongArrayValues() {
	    return false;
	}

	@Override
	public boolean supportsSerializableValues() {
	    return false;
	}

	@Override
	public boolean supportsUniformListValues() {
	    return false;
	}

    }

    public class CassandraElementFeatures implements ElementFeatures {

	@Override
	public boolean supportsNumericIds() {
	    return false;
	}

	@Override
	public boolean supportsUuidIds() {
	    return false;
	}

	@Override
	public boolean supportsAnyIds() {
	    return false;
	}

	@Override
	public boolean supportsCustomIds() {
	    return false;
	}

    }

}
