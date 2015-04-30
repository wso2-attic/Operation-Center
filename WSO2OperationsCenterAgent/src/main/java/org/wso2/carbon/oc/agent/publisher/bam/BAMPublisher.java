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
import org.wso2.carbon.databridge.agent.thrift.DataPublisher;
import org.wso2.carbon.databridge.agent.thrift.exception.AgentException;
import org.wso2.carbon.databridge.commons.exception.*;
import org.wso2.carbon.oc.agent.beans.OCMessage;
import org.wso2.carbon.oc.agent.model.OCPublisherConfiguration;
import org.wso2.carbon.oc.agent.publisher.OCDataPublisher;
import org.wso2.carbon.utils.CarbonUtils;

import java.net.MalformedURLException;
import java.util.Map;

/**
 * Allows publish data to bam as payload data stream
 */
public class BAMPublisher implements OCDataPublisher {

    private static final Log logger = LogFactory.getLog(BAMPublisher.class);
    private static DataPublisher dataPublisher = null;
    //config attributes
    private String username;
    private String password;
    private String defaultHostName;
    private String thriftPort;
    private long interval;
    private boolean isRegistered = false;

    @Override
    public void init(OCPublisherConfiguration ocPublisherConfiguration) {
        //load xml config
        Map<String, String> configMap = ocPublisherConfiguration.getOcPublisherProperties().getPropertyMap();

        this.username = configMap.get(BAMConstants.USERNAME);
        this.password = configMap.get(BAMConstants.PASSWORD);
        this.defaultHostName = configMap.get(BAMConstants.REPORT_HOST_NAME);
        this.thriftPort = configMap.get(BAMConstants.THRIFT_PORT);
        this.interval = Long.parseLong(configMap.get(BAMConstants.INTERVAL));

        try {
            synchronized (BAMPublisher.class) {
                if (dataPublisher == null) {
                    setTrustStoreParams();
                    dataPublisher = new DataPublisher("tcp://" + defaultHostName + ":" + thriftPort,
                            username, password);
                }

            }

        } catch (MalformedURLException e) {
            logger.error(e.getMessage(), e);
        } catch (AgentException e) {
            logger.error("BAMPublisher connection down", e);
        } catch (AuthenticationException e) {
            logger.error(e.getMessage(), e);
        } catch (TransportException e) {
            logger.error(e.getMessage(), e);
        }
        logger.info("BAMPublisher init done");
    }

    /**
     * @param streamDef - stream definition json string event > payload
     * @return String - unique generated stream id
     */
    private String getStreamId(String streamDef) throws AgentException {
        String streamId = null;

        try {
            streamId = dataPublisher.defineStream(streamDef);
        } catch (MalformedStreamDefinitionException e) {
            logger.error(e.getMessage(), e);
        } catch (StreamDefinitionException e) {
            logger.error(e.getMessage(), e);
        } catch (DifferentStreamDefinitionAlreadyDefinedException e) {
            logger.error(e.getMessage(), e);
        }

        return streamId;
    }


    private void setTrustStoreParams() {
        System.setProperty("javax.net.ssl.trustStore",
                CarbonUtils.getCarbonHome() + "/repository/resources/security" +
                        "/client-truststore.jks");
        System.setProperty("javax.net.ssl.trustStorePassword", "wso2carbon");
    }

    @Override
    public void publish(OCMessage ocMessage) {

		/*logger.debug("==========wso2-bam==========reporting");
        try {

			if (!isRegistered) {
				dataPublisher.publish(getStreamId(BAMMessageUtil.getRegisterStreamDef(ocMessage)), null, null,
				                      BAMMessageUtil
						                      .getBAMRegistrationRequestMessage(ocMessage));
				isRegistered = true;
			} else {
				dataPublisher.publish(getStreamId(BAMMessageUtil.getSynchronizeStreamDef(ocMessage)), null, null,
				                      BAMMessageUtil
						                      .getBAMSynchronizationRequestMessage(ocMessage));
			}

		} catch (AgentException e) {
			logger.error("BAMPublisher connection down", e);
		}*/

        //		logger.info(getSynchronizeStreamDef(dataExtractor));
        //		logger.info(getRegisterStreamDef(dataExtractor));
    }

    /**
     * stop bam publisher
     */
    public void stop() {
        dataPublisher.stop();
    }

    @Override
    public long getInterval() {
        return interval;
    }
}
