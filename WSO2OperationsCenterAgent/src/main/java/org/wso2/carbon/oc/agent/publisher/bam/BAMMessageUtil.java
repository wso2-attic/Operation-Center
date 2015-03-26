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

package org.wso2.carbon.oc.agent.publisher.bam;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.map.ObjectMapper;
import org.wso2.carbon.oc.agent.message.OCMessage;
import org.wso2.carbon.oc.agent.message.OCMessageConstants;

import java.io.IOException;
import java.util.*;

/**
 * BAM message utility
 */
public class BAMMessageUtil {
    private static final Log logger = LogFactory.getLog(BAMMessageUtil.class);
    private static ObjectMapper objectMapper = new ObjectMapper();  // for json conversion
    private static String bamRegisterPayloadDef;// BAM registration message payload definition
    private static String bamSyncPayloadDef;// BAM synchronization message payload definition

    //stream names
    private static final String REGISTER_STREAM = "RegisterStream";
    private static final String SYNC_STREAM = "SyncStream";


    private BAMMessageUtil() {
    }

    /**
     * @return String - register message stream definition json
     */
    static String getRegisterStreamDef(OCMessage ocMessage) {
        return "{" +
                "  'name':'" + REGISTER_STREAM + "'," +
                "  'description': 'Storing OC server register request'," +
                "  'tags':['update', 'request', 'up_request']," +
                "  'metaData':[" +
                "               " +
                "  ]," +
                "  'payloadData':[" +
                BAMMessageUtil
                        .getBAMRegPayloadDef(BAMMessageUtil.getBAMRegistrationDataMap(ocMessage)) +
                "  ]" +
                "}";
    }

    /**
     * @return String - synchronize message stream definition json
     */
    static String getSynchronizeStreamDef(OCMessage ocMessage) {
        return "{" +
                "  'name':'" + SYNC_STREAM + "'," +
                "  'description': 'Storing OC server update request'," +
                "  'tags':['update', 'request', 'up_request']," +
                "  'metaData':[" +
                "               " +
                "  ]," +
                "  'payloadData':[" +
                BAMMessageUtil.getBAMSyncPayloadDef(
                        BAMMessageUtil.getBAMSynchronizationDataMap(ocMessage)) +
                "  ]" +
                "}";
    }

    /**
     * This builds the final BAM registration json message
     *
     * @param dataMap - all oc data map
     * @return object array of individual values
     */
    static Object[] getBAMRegistrationRequestMessage(Map<String, Object> dataMap) {

        Map<String, Object> map = dataMap;
        List<Object> objList = new ArrayList<Object>();
        String keySequence[] = {
                OCMessageConstants.SYSTEM_OS, OCMessageConstants.SYSTEM_CPU_COUNT,
                OCMessageConstants.SYSTEM_CPU_SPEED, OCMessageConstants.SYSTEM_TOTAL_MEMORY,
                OCMessageConstants.SERVER_SUBDOMAIN, OCMessageConstants.SERVER_VERSION,
                OCMessageConstants.SERVER_NAME, OCMessageConstants.SERVER_ADMIN_SERVICE_URL,
                OCMessageConstants.SYSTEM_LOCAL_IP, OCMessageConstants.SERVER_START_TIME,
                OCMessageConstants.SERVER_TIMESTAMP, OCMessageConstants.SERVER_PATCHES,
                OCMessageConstants.SERVER_DOMAIN};

        for (String key : keySequence) {
            if (OCMessageConstants.SERVER_PATCHES.equals(key)) {
                objList.add(" ");
                continue;
            }
            Object temp = map.get(key);
            if (isNumber(temp.toString())) {
                objList.add(Double.parseDouble(temp.toString()));
            } else {
                objList.add(temp.toString());
            }
        }
        return objList.toArray();
    }


    /**
     * This builds the final BAM synchronize json message
     *
     * @param dataMap - all oc data map
     * @return object array of individual values
     */
    static Object[] getBAMSynchronizationRequestMessage(Map<String, Object> dataMap) {

        Map<String, Object> map = dataMap;
        List<Object> objList = new ArrayList<Object>();
        String keySequence[] = {
                OCMessageConstants.SERVER_TIMESTAMP, OCMessageConstants.SYSTEM_USER_CPU_USAGE,
                OCMessageConstants.SYSTEM_IDLE_CPU_USAGE, OCMessageConstants.SYSTEM_LOAD_AVERAGE,
                OCMessageConstants.SERVER_TENANTS, OCMessageConstants.SERVER_THREAD_COUNT,
                OCMessageConstants.SYSTEM_FREE_MEMORY, OCMessageConstants.SYSTEM_SYSTEM_CPU_USAGE,
                OCMessageConstants.SERVER_UPTIME, OCMessageConstants.SERVER_ADMIN_SERVICE_URL};

        for (String key : keySequence) {
            if (OCMessageConstants.SERVER_TENANTS.equals(key)) {
                objList.add(" ");
                continue;
            }
            Object temp = map.get(key);
            if (isNumber(temp.toString())) {
                objList.add(Double.parseDouble(temp.toString()));
            } else {
                objList.add(temp.toString());
            }
        }

        return objList.toArray();
    }

    private static String getBAMPayloadDef(Map<String, Object> syncDataMap) {
        Iterator i = syncDataMap.entrySet().iterator();

        List<Map<String, Object>> resultMapList = new ArrayList<Map<String, Object>>();

        while (i.hasNext()) {
            Map<String, Object> tempMap = new HashMap<String, Object>();
            Map.Entry<String, Object> entry = (Map.Entry<String, Object>) i.next();
            String key = entry.getKey();
            Object value = entry.getValue();
            String dataType = null;

            if (isNumber(value.toString())) {
                dataType = "DOUBLE";
            } else {
                dataType = "STRING";
            }

            tempMap.put("name", key);
            tempMap.put("type", dataType);
            resultMapList.add(tempMap);
        }

        String jsonStr = null;
        try {
            jsonStr = objectMapper.writeValueAsString(resultMapList);
        } catch (IOException e) {
            logger.error("Cannot convert the payload definition", e);
        }
        jsonStr = jsonStr.replaceAll("\\[|\\]", "");

        return jsonStr;
    }

    /**
     * This builds the registration message from all oc data map
     *
     * @param ocMessage - all oc data map
     * @return Map<String, Object> - filtered static oc data map
     */
    private static Map<String, Object> getBAMRegistrationDataMap(OCMessage ocMessage) {

        Map<String, Object> regDataMap = new HashMap<String, Object>();

        regDataMap.put(OCMessageConstants.SYSTEM_LOCAL_IP,
                ocMessage.getLocalIp());
        regDataMap.put(OCMessageConstants.SERVER_NAME, ocMessage.getServerName());
        regDataMap
                .put(OCMessageConstants.SERVER_VERSION, ocMessage.getServerVersion());
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
        regDataMap
                .put(OCMessageConstants.SERVER_PATCHES, ocMessage.getPatches());

        return regDataMap;
    }

    /**
     * This builds the synchronize message from all oc data map
     *
     * @param ocMessage - all oc data map
     * @return Map<String, Object> - filtered dynamic oc data map
     */
    private static Map<String, Object> getBAMSynchronizationDataMap(OCMessage ocMessage) {
        Map<String, Object> syncDataMap = new HashMap<String, Object>();

        syncDataMap.put(OCMessageConstants.SERVER_TENANTS, ocMessage.getTenants());
        syncDataMap.put(OCMessageConstants.SERVER_TIMESTAMP,
                ocMessage.getTimestamp());
        syncDataMap.put(OCMessageConstants.SYSTEM_LOAD_AVERAGE,
                ocMessage.getSystemLoadAverage());
        syncDataMap.put(OCMessageConstants.SERVER_THREAD_COUNT,
                ocMessage.getThreadCount());
        syncDataMap.put(OCMessageConstants.SERVER_UPTIME, ocMessage.getServerUpTime());
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


        return syncDataMap;
    }

    /**
     * This builds the registration payload structure
     *
     * @param regDataMap - registration data map
     * @return String - json
     */
    private static String getBAMRegPayloadDef(Map<String, Object> regDataMap) {
        if (bamRegisterPayloadDef == null) {
            bamRegisterPayloadDef = getBAMPayloadDef(regDataMap);
        }
        return bamRegisterPayloadDef;
    }


    /**
     * This builds the synchronize payload structure
     *
     * @param syncDataMap - synchronize data map
     * @return String - json
     */
    private static String getBAMSyncPayloadDef(Map<String, Object> syncDataMap) {
        if (bamSyncPayloadDef == null) {
            bamSyncPayloadDef = getBAMPayloadDef(syncDataMap);
        }
        return bamSyncPayloadDef;
    }

    /**
     * This is a helper method to identify the
     * basic data types [STRING, DOUBLE]
     *
     * @param s - String expected number
     * @return boolean - num / !num
     */
    private static boolean isNumber(String s) {
        boolean result = false;
        try {
            double d = Double.parseDouble(s);
            result = true;
        } catch (NumberFormatException e) {
            return result;
        }
        return result;
    }


}
