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

package org.wso2.carbon.oc.agent.internal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wso2.carbon.oc.agent.model.OCConfiguration;
import org.wso2.carbon.oc.agent.model.OCPublishers;
import org.wso2.carbon.server.admin.service.ServerAdmin;
import org.wso2.carbon.utils.CarbonUtils;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

/*
    Allows to invoke commands in server restart, shutdown
*/

public class OCAgentUtils {
	private static final String FORCE_SHUTDOWN = "FORCE_SHUTDOWN";
	private static final String FORCE_RESTART = "FORCE_RESTART";
	private static final String GRACEFUL_SHUTDOWN = "GRACEFUL_SHUTDOWN";
	private static final String GRACEFUL_RESTART = "GRACEFUL_RESTART";
    private static final Log logger = LogFactory.getLog(OCAgentUtils.class);

	private OCAgentUtils() {
	}

	/**
	 * Extract all enabled publisher info
	 * @return Publishers - get all publisher objects
	 */
	//handle NPE
	public static OCPublishers getOcPublishers() {
		OCPublishers publishers = null;
		try {
			JAXBContext context = JAXBContext.newInstance(OCConfiguration.class);
			Unmarshaller um = context.createUnmarshaller();
			//TODO
			OCConfiguration oc = (OCConfiguration) um.unmarshal(new FileReader(
					CarbonUtils.getCarbonConfigDirPath() + File.separator +
					OCAgentConstants.OC_XML));
			publishers = oc.getOcPublishers();
		} catch (JAXBException e) {
			logger.error(e.getMessage(), e);
		} catch (FileNotFoundException e) {
			logger.error(OCAgentConstants.OC_XML + " is missing in this path", e);
		}

		return publishers;
	}

	/**
	 * @param command "RESTART", "SHUTDOWN" etc..
	 */

	public static void performAction(String command) {
		ServerAdmin serverAdmin =
				(ServerAdmin) OCAgentDataHolder.getInstance().getServerAdmin();
		if (serverAdmin != null) {
			try {
				if (FORCE_RESTART.equals(command)) {
					serverAdmin.restart();
				} else if (GRACEFUL_RESTART.equals(command)) {
					serverAdmin.restartGracefully();
				} else if (FORCE_SHUTDOWN.equals(command)) {
					serverAdmin.shutdown();
				} else if (GRACEFUL_SHUTDOWN.equals(command)) {
					serverAdmin.shutdownGracefully();
				} else {
					logger.debug("Unknown command received. [" + command + "]");
				}
			} catch (Exception e) {
				logger.error("Failed while executing command. [" + command + "]", e);
			}
		} else {
			logger.error("Unable to perform action, ServerAdmin is null");
		}
	}
}
