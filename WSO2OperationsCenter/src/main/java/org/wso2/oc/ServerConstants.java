/*
 * Copyright 2004,2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
