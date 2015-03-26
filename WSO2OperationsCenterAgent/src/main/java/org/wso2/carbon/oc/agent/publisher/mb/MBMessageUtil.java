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

package org.wso2.carbon.oc.agent.publisher.mb;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.map.ObjectMapper;
import org.wso2.carbon.oc.agent.message.OCMessage;
import org.wso2.carbon.oc.agent.message.OCMessageConstants;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * MB utility
 */
public class MBMessageUtil {
    private static final Log logger = LogFactory.getLog(MBMessageUtil.class);
    private static ObjectMapper objectMapper = new ObjectMapper(); // for json conversion

    private MBMessageUtil() {
    }

    /**
     * This method builds static event payload message
     *
     * @param ocMessage OCMessage - all server data
     * @return String - json string
     */
    static String getRegistrationPayload(OCMessage ocMessage) {

        //root map
        Map<String, Map<String, Map<String, Object>>> root =
                new HashMap<String, Map<String, Map<String, Object>>>();
        //event map
        Map<String, Map<String, Object>> event = new HashMap<String, Map<String, Object>>();
        //payload map
        Map<String, Object> payload = new HashMap<String, Object>();


        payload.put(OCMessageConstants.SYSTEM_LOCAL_IP,
                ocMessage.getLocalIp());
        payload.put(OCMessageConstants.SERVER_NAME, ocMessage.getServerName());
        payload.put(OCMessageConstants.SERVER_VERSION, ocMessage.getServerVersion());
        payload.put(OCMessageConstants.SERVER_DOMAIN, ocMessage.getDomain());
        payload.put(OCMessageConstants.SERVER_SUBDOMAIN,
                ocMessage.getSubDomain());
        payload.put(OCMessageConstants.SERVER_ADMIN_SERVICE_URL,
                ocMessage.getAdminServiceUrl());
        payload.put(OCMessageConstants.SERVER_START_TIME,
                ocMessage.getServerStartTime());
        payload.put(OCMessageConstants.SYSTEM_OS, ocMessage.getOs());
        payload.put(OCMessageConstants.SYSTEM_TOTAL_MEMORY,
                ocMessage.getTotalMemory());
        payload.put(OCMessageConstants.SYSTEM_CPU_COUNT,
                ocMessage.getCpuCount());
        payload.put(OCMessageConstants.SYSTEM_CPU_SPEED,
                ocMessage.getCpuSpeed());
        payload.put(OCMessageConstants.SERVER_TIMESTAMP,
                ocMessage.getTimestamp());
        payload.put(OCMessageConstants.SERVER_PATCHES, ocMessage.getPatches());

        event.put("payload", payload);
        root.put("event", event);

        String message = null;

        try {
            message = objectMapper.writeValueAsString(root);

        } catch (IOException e) {
            logger.error("Failed to get JSON String from ocSynchronizationRequest", e);
        }
        return message;
    }

    /**
     * This method builds dynamic event payload message
     *
     * @param ocMessage OCMessage - all server data
     * @return String - json string
     */
    static String getSynchronizationPayload(OCMessage ocMessage) {

        //root map
        Map<String, Map<String, Map<String, Object>>> root =
                new HashMap<String, Map<String, Map<String, Object>>>();
        //event map
        Map<String, Map<String, Object>> event = new HashMap<String, Map<String, Object>>();
        //payload map
        Map<String, Object> payload = new HashMap<String, Object>();

        payload.put(OCMessageConstants.SERVER_TENANTS, ocMessage.getTenants());
        payload.put(OCMessageConstants.SERVER_TIMESTAMP,
                ocMessage.getTimestamp());
        payload.put(OCMessageConstants.SYSTEM_LOAD_AVERAGE,
                ocMessage.getSystemLoadAverage());
        payload.put(OCMessageConstants.SERVER_THREAD_COUNT,
                ocMessage.getThreadCount());
        payload.put(OCMessageConstants.SERVER_UPTIME, ocMessage.getServerUpTime());
        payload.put(OCMessageConstants.SERVER_ADMIN_SERVICE_URL,
                ocMessage.getAdminServiceUrl());
        payload.put(OCMessageConstants.SYSTEM_USER_CPU_USAGE,
                ocMessage.getUserCpuUsage());
        payload.put(OCMessageConstants.SYSTEM_SYSTEM_CPU_USAGE,
                ocMessage.getSystemCpuUsage());
        payload.put(OCMessageConstants.SYSTEM_IDLE_CPU_USAGE,
                ocMessage.getIdleCpuUsage());
        payload.put(OCMessageConstants.SYSTEM_FREE_MEMORY,
                ocMessage.getFreeMemory());

        event.put("payload", payload);
        root.put("event", event);

        String message = null;

        try {
            message = objectMapper.writeValueAsString(root);

        } catch (IOException e) {
            logger.error("Failed to get JSON String from ocSynchronizationRequest", e);
        }

        return message;
    }

}