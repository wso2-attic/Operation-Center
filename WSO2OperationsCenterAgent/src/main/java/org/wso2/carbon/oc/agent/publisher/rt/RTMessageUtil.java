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

package org.wso2.carbon.oc.agent.publisher.rt;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.map.ObjectMapper;
import org.wso2.carbon.oc.agent.message.OCMessage;
import org.wso2.carbon.oc.agent.message.OCMessageConstants;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Real time message utility.
 */
public class RTMessageUtil {
    private static final Log logger = LogFactory.getLog(RTMessageUtil.class);
    private static ObjectMapper objectMapper = new ObjectMapper();  // for json conversion

    private RTMessageUtil() {
    }

    /**
     * This method builds dynamic message
     *
     * @param ocMessage OCMessage  - all server data keys can found @ OCAgentConstants
     * @return String - json string
     */
    static String getSynchronizationRequestMessage(OCMessage ocMessage) {

        Map<String, Object> syncDataMap = new HashMap<String, Object>();

        syncDataMap.put(OCMessageConstants.SERVER_DOMAIN,
                ocMessage.getDomain());
        syncDataMap.put(OCMessageConstants.SERVER_TENANTS,
                ocMessage.getTenants());
        syncDataMap.put(OCMessageConstants.SERVER_TIMESTAMP,
                ocMessage.getTimestamp());
        syncDataMap.put(OCMessageConstants.SYSTEM_LOAD_AVERAGE,
                ocMessage.getSystemLoadAverage());
        syncDataMap.put(OCMessageConstants.SERVER_THREAD_COUNT,
                ocMessage.getThreadCount());
        syncDataMap.put(OCMessageConstants.SERVER_UPTIME,
                ocMessage.getServerUpTime());
        syncDataMap.put(OCMessageConstants.SERVER_ADMIN_SERVICE_URL,
                ocMessage.getAdminServiceUrl());
        syncDataMap.put(OCMessageConstants.SYSTEM_USER_CPU_USAGE,
                ocMessage.getUserCpuUsage());
        syncDataMap.put(OCMessageConstants.SYSTEM_SYSTEM_CPU_USAGE,
                ocMessage.getSystemCpuUsage());
        syncDataMap.put(OCMessageConstants.SYSTEM_IDLE_CPU_USAGE,
                ocMessage.getIdleCpuUsage());
        syncDataMap.put(OCMessageConstants.SYSTEM_FREE_MEMORY,
                ocMessage.getFreeMemory());
         syncDataMap.put(OCMessageConstants.ARTIFACTS,ocMessage.getArtifacts());
        String message = null;

        try {
            message = objectMapper
                    .writeValueAsString(syncDataMap);

        } catch (IOException e) {
            logger.error("Failed to get JSON String from ocSynchronizationRequest", e);
        }
        return message;
    }

    /**
     * This method builds static message for registration
     *
     * @param ocMessage OCMessage  - all server data keys can found @ OCAgentConstants
     * @return String - json string
     */
    static String getRegistrationRequestMessage(OCMessage ocMessage) {
        Map<String, Object> regDataMap = new HashMap<String, Object>();

        regDataMap.put(OCMessageConstants.SYSTEM_LOCAL_IP,
                ocMessage.getLocalIp());
        regDataMap.put(OCMessageConstants.SERVER_NAME, ocMessage.getServerName());
        regDataMap.put(OCMessageConstants.SERVER_VERSION, ocMessage.getServerVersion());
        regDataMap.put(OCMessageConstants.SERVER_DOMAIN, ocMessage.getDomain());
        regDataMap.put(OCMessageConstants.SERVER_SUBDOMAIN,
                ocMessage.getSubDomain());
        regDataMap.put(OCMessageConstants.SERVER_ADMIN_SERVICE_URL,
                ocMessage.getAdminServiceUrl());
        regDataMap.put(OCMessageConstants.SERVER_START_TIME,
                ocMessage.getServerStartTime());
        regDataMap.put(OCMessageConstants.SYSTEM_OS, ocMessage.getOs());
        regDataMap.put(OCMessageConstants.SYSTEM_TOTAL_MEMORY,
                ocMessage.getTotalMemory());
        regDataMap.put(OCMessageConstants.SYSTEM_CPU_COUNT,
                ocMessage.getCpuCount());
        regDataMap.put(OCMessageConstants.SYSTEM_CPU_SPEED,
                ocMessage.getCpuSpeed());
        regDataMap.put(OCMessageConstants.SERVER_TIMESTAMP,
                ocMessage.getTimestamp());
        regDataMap.put(OCMessageConstants.SERVER_PATCHES, ocMessage.getPatches());

        String message = null;

        try {
            message = objectMapper
                    .writeValueAsString(regDataMap);

        } catch (IOException e) {
            logger.error("Failed to get JSON String from ocRegistrationRequest", e);
        }
        return message;
    }

}
