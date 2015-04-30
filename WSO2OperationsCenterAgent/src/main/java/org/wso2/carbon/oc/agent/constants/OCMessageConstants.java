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

package org.wso2.carbon.oc.agent.constants;

/**
 * This holds oc constants key values
 */
public class OCMessageConstants {

	//all system level keys of data map
	public static final String SYSTEM_LOCAL_IP = "ip";
	public static final String SYSTEM_OS = "os";
	public static final String SYSTEM_TOTAL_MEMORY = "totalMemory";
	public static final String SYSTEM_CPU_COUNT = "cpuCount";
	public static final String SYSTEM_CPU_SPEED = "cpuSpeed";
	public static final String SYSTEM_FREE_MEMORY = "freeMemory";
	public static final String SYSTEM_IDLE_CPU_USAGE = "idleCpuUsage";
	public static final String SYSTEM_USER_CPU_USAGE = "userCpuUsage";
	public static final String SYSTEM_SYSTEM_CPU_USAGE = "systemCpuUsage";
	public static final String SYSTEM_LOAD_AVERAGE = "systemLoadAverage";
	//all server level keys of data map
	public static final String SERVER_NAME = "serverName";
	public static final String SERVER_VERSION = "serverVersion";
	public static final String SERVER_DOMAIN = "domain";
	public static final String SERVER_SUBDOMAIN = "subDomain";
	public static final String SERVER_START_TIME = "startTime";
	public static final String SERVER_ADMIN_SERVICE_URL = "adminServiceUrl";
	public static final String SERVER_UPTIME = "serverUpTime";
	public static final String SERVER_THREAD_COUNT = "threadCount";
	public static final String SERVER_TENANTS = "tenants";
	public static final String SERVER_TIMESTAMP = "timestamp";
	public static final String SERVER_PATCHES = "patches";
	public static final String SERVER_ID = "serverId";
	public static final String ARTIFACTS = "artifacts";

	private OCMessageConstants() {
	}
}
