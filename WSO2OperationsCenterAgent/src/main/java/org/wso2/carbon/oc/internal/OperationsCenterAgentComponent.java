package org.wso2.carbon.oc.internal;

import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wso2.carbon.base.api.ServerConfigurationService;
import org.wso2.carbon.oc.services.OperationsCenterAgentService;
import org.wso2.carbon.server.admin.common.IServerAdmin;
import org.wso2.carbon.utils.ConfigurationContextService;

/**
 * Created by jayanga on 4/18/14.
 *
 * The Declarative Service Component for AdminService, ServerConfigurationService and ConfigurationContextService
 *
 * @scr.component name="org.wso2.carbon.oc.internal.OperationsCenterAgentComponent"
 *                immediate="true"
 * @scr.reference name="org.wso2.carbon.server.admin.common"
 *                interface="org.wso2.carbon.server.admin.common.IServerAdmin"
 *                cardinality="1..1" policy="dynamic"
 *                bind="setServerAdminService"
 *                unbind="unsetServerAdminService"
 * @scr.reference name="server.configuration"
 *                interface="org.wso2.carbon.base.api.ServerConfigurationService"
 *                cardinality="1..1" policy="dynamic"
 *                bind="setServerConfigurationService"
 *                unbind="unsetServerConfigurationService"
 * @scr.reference name="config.context.service"
 *                interface="org.wso2.carbon.utils.ConfigurationContextService"
 *                cardinality="1..1" policy="dynamic"
 *                bind="setConfigurationContextService"
 *                unbind="unsetConfigurationContextService"
 */
public class OperationsCenterAgentComponent {
    private static Logger logger = LoggerFactory.getLogger(OperationsCenterAgentComponent.class);

    private IServerAdmin serverAdmin;
    private ServerConfigurationService serverConfigurationService;
    private ConfigurationContextService configurationContextService;
    private OperationsCenterAgent operationsCenterAgent;

    protected void unsetServerAdminService(IServerAdmin serverAdmin) {
        logger.info("Un-setting ServerAdmin from OperationsCenterAgentComponent");
        this.serverAdmin = null;
    }

    protected void setServerAdminService(IServerAdmin serverAdmin) {
        logger.info("Setting ServerAdmin to OperationsCenterAgentComponent");
        this.serverAdmin = serverAdmin;
    }

    protected void unsetServerConfigurationService(ServerConfigurationService serverConfigurationService) {
        logger.info("Un-setting ServerConfigurationService from OperationsCenterAgentComponent");
        this.serverConfigurationService = null;
    }

    protected void setServerConfigurationService(ServerConfigurationService serverConfigurationService) {
        logger.info("Setting ServerConfigurationService to OperationsCenterAgentComponent");
        this.serverConfigurationService = serverConfigurationService;
    }

    protected void unsetConfigurationContextService(ConfigurationContextService configurationContextService) {
        logger.info("Un-setting ConfigurationContextService from OperationsCenterAgentComponent");
        this.configurationContextService = null;
    }

    protected void setConfigurationContextService(ConfigurationContextService configurationContextService) {
        logger.info("Setting ConfigurationContextService to OperationsCenterAgentComponent");
        this.configurationContextService = configurationContextService;
    }

    public OperationsCenterAgent getOperationsCenterAgent() {
        return operationsCenterAgent;
    }

    public void setOperationsCenterAgent(OperationsCenterAgent operationsCenterAgent) {
        this.operationsCenterAgent = operationsCenterAgent;
    }

    protected void activate(ComponentContext componentContext){
        logger.info("Activating OperationsCenterAgentComponent");

        operationsCenterAgent = new OperationsCenterAgent(this);
        operationsCenterAgent.start();

        OperationsCenterAgentService.setOperationsCenterAgentComponent(this);

        logger.info("OperationsCenterAgentComponent activation complete");
    }

    protected void deactivate(ComponentContext componentContext){
        logger.info("Deactivating OperationsCenterAgentComponent");
    }

    public IServerAdmin getServerAdmin() {
        if (serverAdmin == null){
            logger.debug("ServerAdmin is null");
        }
        return serverAdmin;
    }

    public ServerConfigurationService getServerConfigurationService() {
        if (serverConfigurationService == null){
            logger.debug("ServerConfigurationService is null");
        }
        return serverConfigurationService;
    }

    public ConfigurationContextService getConfigurationContextService() {
        if (configurationContextService == null){
            logger.debug("ConfigurationContextService is null");
        }
        return configurationContextService;
    }
}
