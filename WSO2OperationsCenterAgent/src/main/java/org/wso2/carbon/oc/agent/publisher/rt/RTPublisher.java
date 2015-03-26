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

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.wso2.carbon.oc.agent.internal.OCAgentUtils;
import org.wso2.carbon.oc.agent.message.OCMessage;
import org.wso2.carbon.oc.agent.message.OCMessageConstants;
import org.wso2.carbon.oc.agent.model.OCPublisherConfiguration;
import org.wso2.carbon.oc.agent.publisher.OCDataPublisher;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Allows to publish real time data to oc web app
 * This is the default publisher
 */
public class RTPublisher implements OCDataPublisher {

    private String serverId;
    private static final String REGISTRATION_PATH = "/servers";
    private static final String SYNCHRONIZATION_PATH = "/servers/";
    private static final String CONTENT_TYPE = "application/json";
    private static final String CHARACTER_SET = "UTF-8";

    private static final Log logger = LogFactory.getLog(RTPublisher.class);
    private ObjectMapper objectMapper = new ObjectMapper();
    private HttpClient httpClient;
    private boolean isRegistered = false;
    private String ocUrl;
    private long interval;

    @Override
    public void init(OCPublisherConfiguration ocPublisherConfiguration) {
        // get set config
        Map<String, String> configMap = ocPublisherConfiguration.getOcPublisherProperties().getPropertyMap();

        String username = configMap.get(RTConstants.USERNAME);
        String password = configMap.get(RTConstants.PASSWORD);
        this.ocUrl = configMap.get(RTConstants.REPORT_URL);

        this.interval = Long.parseLong(configMap.get(RTConstants.INTERVAL));

        if (StringUtils.isBlank(this.ocUrl)) {
            throw new IllegalArgumentException("Operations Center URL is unspecified.");
        }
        this.httpClient = new HttpClient(new MultiThreadedHttpConnectionManager());
        this.httpClient.getState().setCredentials(AuthScope.ANY,
                new UsernamePasswordCredentials(username,
                        password));
        this.httpClient.getParams().setAuthenticationPreemptive(true);
        logger.info("RTPublisher init done");


    }

    public void publish(OCMessage ocMessage) {
        logger.debug("======real-time===========reporting");

        if (!isRegistered) {
            register(ocMessage);
        } else {
            sync(ocMessage);
        }
    }

    /**
     * send the real time registration message
     *
     * @param ocMessage - all oc data
     */
    private void register(OCMessage ocMessage) {

        String jsonString = RTMessageUtil.getRegistrationRequestMessage(ocMessage);

        //request check
        String responseBody = null;
        try {
            responseBody =
                    sendPostRequest(ocUrl + REGISTRATION_PATH, jsonString, HttpStatus.SC_CREATED);
        } catch (IOException e) {
            logger.error("RTPublisher connection down while registering: ", e);
            isRegistered = false;
        }

        //response check
        if (responseBody != null && responseBody.length() > 0) {
            Map<String, String> regResMap;
            try {

                regResMap = objectMapper
                        .readValue(responseBody, new TypeReference<HashMap<String, String>>() {
                        });
                if (regResMap != null) {
                    serverId = regResMap.get(OCMessageConstants.SERVER_ID);
                }


                isRegistered = true;
            } catch (IOException e) {
                logger.error("Failed to read values from RegistrationResponse", e);
                isRegistered = false;
            }
        }
    }

    /**
     * send the real time synchronization message
     *
     * @param ocMessage - all oc data
     */
    private void sync(OCMessage ocMessage) {

        String jsonString = RTMessageUtil.getSynchronizationRequestMessage(ocMessage);
        String responseBody = null;

        //request check
        try {
            responseBody =
                    sendPutRequest(ocUrl + SYNCHRONIZATION_PATH + serverId, jsonString, HttpStatus.SC_OK);
        } catch (IOException e) {
            logger.error("RTPublisher connection down while sync messaging: ", e);
            isRegistered = false;
        }

        //response check
        if (responseBody != null && responseBody.length() > 0) {
            List<String> synResMap;
            try {
                synResMap = objectMapper
                        .readValue(responseBody, new TypeReference<List<String>>() {
                        });

                isRegistered = true;
            } catch (IOException e) {
                logger.error("Failed to read values from SynchronizationResponse", e);
                isRegistered = false;
                return;
            }

            if (synResMap != null) {
                for (String command : synResMap) {
                    OCAgentUtils.performAction(command);
                    logger.debug("Executing command. [Command:" + command + "]");
                }

            } else {
                logger.error("Unable receive JSON synchronization response.");
            }
        }
    }

    /**
     * @param url      String - operations center url
     * @param request  String - json string request message
     * @param expected int - expected http status code
     * @return String - response json string
     * @throws IOException - sever connect problem
     */
    public String sendPostRequest(String url, String request, int expected) throws IOException {
        PostMethod postMethod = new PostMethod(url);
        try {
            RequestEntity entity = new StringRequestEntity(request, CONTENT_TYPE, CHARACTER_SET);
            postMethod.setRequestEntity(entity);
            if (logger.isTraceEnabled()) {
                logger.trace("Sending POST request. " + request);
            }

            int statusCode = httpClient.executeMethod(postMethod);
            if (statusCode == expected) {
                String responseBody = postMethod.getResponseBodyAsString();
                if (logger.isTraceEnabled()) {
                    logger.trace("Response received. " + responseBody);
                }
                return responseBody;
            } else {
                logger.debug("Request failed with status Code : " + statusCode);
            }
        } catch (UnsupportedEncodingException e) {
            logger.error("Failed to register with Operations Center", e);
        } finally {
            postMethod.releaseConnection();
        }
        return null;
    }

    /**
     * @param url      String - operations center url
     * @param request  String - json string request message
     * @param expected int - expected http status code
     * @return String - response json string
     * @throws IOException - sever connect problem
     */
    public String sendPutRequest(String url, String request, int expected) throws IOException {
        PutMethod putMethod = new PutMethod(url);
        try {
            RequestEntity entity = new StringRequestEntity(request, CONTENT_TYPE, CHARACTER_SET);
            putMethod.setRequestEntity(entity);
            if (logger.isTraceEnabled()) {
                logger.trace("Sending PUT request. " + request);
            }

            int statusCode = httpClient.executeMethod(putMethod);
            if (statusCode == expected) {
                String responseBody = putMethod.getResponseBodyAsString();
                if (logger.isTraceEnabled()) {
                    logger.trace("Response received. " + responseBody);
                }
                return responseBody;
            } else {
                logger.debug("Request failed with status Code : " + statusCode);
                throw new IOException();
            }

        } catch (UnsupportedEncodingException e) {
            logger.error("Failed to sync data with Operations Center", e);
        } finally {
            putMethod.releaseConnection();
        }
        return null;
    }

    public long getInterval() {
        return interval;
    }
}
