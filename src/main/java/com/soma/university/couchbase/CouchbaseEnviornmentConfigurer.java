package com.soma.university.couchbase;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.couchbase.config.CouchbaseConfigurer;
import org.springframework.stereotype.Component;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.cluster.ClusterInfo;
import com.couchbase.client.java.env.CouchbaseEnvironment;
import com.couchbase.client.java.env.DefaultCouchbaseEnvironment;

@Component
public class CouchbaseEnviornmentConfigurer implements CouchbaseConfigurer {

	@Value("${university.databaseName}")
	private String universityBucketName = "university";

	@Autowired
	private CouchbaseClusterConnector couchbaseClusterConnector;
	
	private static final long CONNECTION_TIMEOUT = 60000l;

	@Override
	public CouchbaseEnvironment couchbaseEnvironment() throws Exception {
		return DefaultCouchbaseEnvironment.builder().connectTimeout(CONNECTION_TIMEOUT).kvTimeout(CONNECTION_TIMEOUT)
				.queryTimeout(CONNECTION_TIMEOUT).viewTimeout(CONNECTION_TIMEOUT).build();
	}

	@Override
	public Cluster couchbaseCluster() throws Exception {
		return couchbaseClusterConnector.getCluster();
	}

	@Override
	public ClusterInfo couchbaseClusterInfo() throws Exception {
		return couchbaseCluster().clusterManager().info();
	}

	@Override
	public synchronized Bucket couchbaseClient() throws Exception {
		return getBucket(universityBucketName);
	}
	
	private Bucket getBucket(String bucketName) throws Exception {
		return couchbaseCluster().openBucket(bucketName); 
	}

}
