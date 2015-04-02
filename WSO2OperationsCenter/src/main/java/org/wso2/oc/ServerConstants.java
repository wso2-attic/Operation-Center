package org.wso2.oc;

public class ServerConstants {

	public static final String FORCE_SHUTDOWN = "FORCE_SHUTDOWN";
	public static final String FORCE_RESTART = "FORCE_RESTART";
	public static final String GRACEFUL_SHUTDOWN = "GRACEFUL_SHUTDOWN";
	public static final String GRACEFUL_RESTART = "GRACEFUL_RESTART";

	public static final String NODE_DOWN = "NODE_DOWN";
	public static final String NODE_RUNNING = "RUNNING";
	public static final String NODE_NOT_REPORTING = "NOT_REPORTING";

	public static final String CLUSTER_DOWN = "CLUSTER_DOWN";
	public static final String CLUSTER_RUNNING = "RUNNING";

	public static final int NODE_DOWN_TIME_INTERVAL = 60000;
	public static final int NODE_NOT_REPORTING_TIME_INTERVAL = 11000;

	private ServerConstants() {
	}
}
