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
import org.wso2.carbon.oc.agent.beans.OCMessage;
import org.wso2.carbon.oc.agent.model.OCPublisherConfiguration;
import org.wso2.carbon.oc.agent.publisher.OCDataPublisher;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Map;
import java.util.Properties;

/**
 * Allows publish data to constants broker endpoint
 */
public class MBPublisher implements OCDataPublisher {

    // mb queue names
    private static final String REG_QUEUE = "RegisterRequest";
    private static final String SYNC_QUEUE = "UpdateRequest";
    // mb conf
    private static final String QPID_ICF =
            "org.wso2.andes.jndi.PropertiesFileInitialContextFactory";
    private static final String CF_NAME_PREFIX = "connectionfactory.";
    private static final String QUEUE_NAME_PREFIX = "queue.";
    private static final String CF_NAME = "qpidConnectionfactory";
    private static final Log logger = LogFactory.getLog(MBPublisher.class);
    private static String CARBON_CLIENT_ID = "carbon";
    private static String CARBON_VIRTUAL_HOST_NAME = "carbon";

    // load from carbon.xml
    private String defaultHostName;
    private String defaultPort;
    private String username;
    private String password;
    private long interval;

    private boolean isRegistered = false;

    @Override
    public void init(OCPublisherConfiguration ocPublisherConfiguration) {
        //get set config
        Map<String, String> configMap = ocPublisherConfiguration.getOcPublisherProperties().getPropertyMap();

        this.username = configMap.get(MBConstants.USERNAME);
        this.password = configMap.get(MBConstants.PASSWORD);
        this.defaultHostName =
                configMap.get(MBConstants.REPORT_HOST_NAME);
        this.defaultPort = configMap.get(MBConstants.REPORT_PORT);

        this.interval =
                Long.parseLong(configMap.get(MBConstants.INTERVAL));
        logger.info("MBPublisher init done");
    }

    /**
     * @param queueName   - String mb queue name
     * @param jsonMessage - String mb queue constants json string
     */
    public void sendMessages(String queueName, String jsonMessage) {

        try {
            Properties properties = new Properties();
            properties.put(Context.INITIAL_CONTEXT_FACTORY, QPID_ICF);
            properties.put(CF_NAME_PREFIX + CF_NAME, getTCPConnectionURL(username, password));
            properties.put(QUEUE_NAME_PREFIX + queueName, queueName);
            InitialContext ctx = new InitialContext(properties);
            // lookup connection factory
            QueueConnectionFactory connFactory = (QueueConnectionFactory) ctx.lookup(CF_NAME);
            QueueConnection queueConnection = connFactory.createQueueConnection();
            queueConnection.start();
            QueueSession queueSession =
                    queueConnection.createQueueSession(false, QueueSession.AUTO_ACKNOWLEDGE);
            // send constants
            Queue queue = (Queue) ctx.lookup(queueName);
            // create the constants to send

            TextMessage textMessage = queueSession.createTextMessage(jsonMessage);
            QueueSender queueSender = queueSession.createSender(queue);
            queueSender.send(textMessage);
            queueSender.close();
            queueSession.close();
            queueConnection.close();
        } catch (JMSException e) {
            logger.error("MBPublisher connection down", e);
        } catch (NamingException e) {
            logger.error("Naming error", e);
        }

    }

    @Override
    public void publish(OCMessage ocMessage) {
        if(logger.isDebugEnabled()) {
            logger.debug("======wso2-mb===========reporting");
        }
        if (!isRegistered) {
            sendMessages(REG_QUEUE, MBMessageUtil.getRegistrationPayload(ocMessage));
            isRegistered = true;
        } else {
            sendMessages(SYNC_QUEUE, MBMessageUtil.getSynchronizationPayload(ocMessage));
        }

    }

    @Override
    public long getInterval() {
        return interval;
    }

    /**
     * @param username
     * @param password
     * @return String - conn url
     */
    public String getTCPConnectionURL(String username, String password) {
        // amqp://{username}:{password}@carbon/carbon?brokerlist='tcp://{hostname}:{port}'
        return new StringBuffer()
                .append("amqp://").append(username).append(":").append(password)
                .append("@").append(CARBON_CLIENT_ID)
                .append("/").append(CARBON_VIRTUAL_HOST_NAME)
                .append("?brokerlist='tcp://").append(defaultHostName).append(":")
                .append(defaultPort).append("'")
                .toString();
    }
}
