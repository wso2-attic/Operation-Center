package org.wso2.carbon.oc.internal;

import org.apache.axiom.om.OMElement;
import org.apache.axis2.clustering.ClusteringAgent;
import org.apache.axis2.description.Parameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wso2.carbon.base.api.ServerConfigurationService;
import org.wso2.carbon.server.admin.service.ServerAdmin;
import org.wso2.carbon.utils.ConfigurationContextService;

import java.util.Iterator;

/**
 * Created by jayanga on 4/18/14.
 */
public class ServerDataExtractor {
    private static Logger logger = LoggerFactory.getLogger(ServerDataExtractor.class);

    private OperationsCenterAgentComponent operationsCenterAgentComponent;

    private static final String serverNameTag = "Name";
    private static final String operationsCenterURLTag = "OperationsCenterURL";
    private static final String webContextRootTag = "WebContextRoot";
    private static final String clusterDomainTag = "domain";
    private static final String clusteringAgentPropertiesTag = "properties";

    private static final String localIpProperty = "carbon.local.ip";
    private static final String httpsPortProperty = "mgt.transport.https.port";

    private static final String clusterPropertyAttributeName = "name";
    private static final String clusterPropertyAttributeValue = "value";
    private static final String clusterSubDomainAttribute = "subDomain";

    public ServerDataExtractor(OperationsCenterAgentComponent operationsCenterAgentComponent){
        this.operationsCenterAgentComponent = operationsCenterAgentComponent;
    }

    public String getServerStartTime(){
        ServerAdmin serverAdmin = (ServerAdmin) operationsCenterAgentComponent.getServerAdmin();
        if (serverAdmin != null){
            try {
                return serverAdmin.getServerData().getServerStartTime();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    public String getServerStartUpDuration(){
        ServerAdmin serverAdmin = (ServerAdmin) operationsCenterAgentComponent.getServerAdmin();
        if (serverAdmin != null){
            try {
                return serverAdmin.getServerData().getServerStartUpDuration();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    public String getServerName(){
        ServerConfigurationService serverConfigurationService = operationsCenterAgentComponent.getServerConfigurationService();
        if (serverConfigurationService != null){
            return  serverConfigurationService.getFirstProperty(serverNameTag);
        }
        return "";
    }

    public String getOperationsCenterURL(){
        ServerConfigurationService serverConfigurationService = operationsCenterAgentComponent.getServerConfigurationService();
        if (serverConfigurationService != null){
            return  serverConfigurationService.getFirstProperty(operationsCenterURLTag);
        }
        return "";
    }

    public String getServerIP(){
        String localIP = System.getProperty(localIpProperty);

        if (localIP != null){
            return  localIP;
        }
        return "";
    }

    public String getAdminServicePort(){
        String httpsPort = System.getProperty(httpsPortProperty);

        if (httpsPort != null){
            return  httpsPort;
        }
        return "";
    }

    public String getAdminServiceURL(){
        ServerConfigurationService serverConfigurationService = operationsCenterAgentComponent.getServerConfigurationService();
        if (serverConfigurationService != null){
            String webContextRoot = serverConfigurationService.getFirstProperty(webContextRootTag);
            String localIP = System.getProperty(localIpProperty);
            String httpsPort = System.getProperty(httpsPortProperty);

            if (!webContextRoot.endsWith("/")){
                webContextRoot += "/";
            }

            if (localIP != null && httpsPort != null){
                return  "https://" + localIP + ":" + httpsPort + webContextRoot;
            }
        }
        return "";
    }

    public String getServerDomain(){
        ConfigurationContextService configurationContextService = operationsCenterAgentComponent.getConfigurationContextService();
        if (configurationContextService != null){
            ClusteringAgent clusteringAgent = configurationContextService.getServerConfigContext().getAxisConfiguration().getClusteringAgent();
            if (clusteringAgent != null){
                Parameter domainParameter = clusteringAgent.getParameter(clusterDomainTag);
                if (domainParameter != null){
                    return (String)domainParameter.getValue();
                }
            }else{
                logger.debug("ClusteringAgent is null. Running with no clustering");
            }
        }
        return "";
    }

    public String getServerSubDomain(){
        ConfigurationContextService configurationContextService = operationsCenterAgentComponent.getConfigurationContextService();
        if (configurationContextService != null){
            ClusteringAgent clusteringAgent = configurationContextService.getServerConfigContext().getAxisConfiguration().getClusteringAgent();
            if (clusteringAgent != null){
                Parameter propertiesParameter = clusteringAgent.getParameter(clusteringAgentPropertiesTag);
                if (propertiesParameter != null){
                    OMElement omElement = propertiesParameter.getParameterElement();
                    Iterator<OMElement> childElements = omElement.getChildElements();
                    while (childElements.hasNext()) {
                        OMElement childElement = (OMElement) childElements.next();
                        if (childElement != null){
                            String propertyAttributeValue = childElement.getAttributeValue(childElement.resolveQName(clusterPropertyAttributeName));
                            if (propertyAttributeValue != null && propertyAttributeValue.equalsIgnoreCase(clusterSubDomainAttribute)){
                                return (childElement.getAttributeValue(childElement.resolveQName(clusterPropertyAttributeValue)));
                            }
                        }
                    }
                }
            }else{
                logger.debug("ClusteringAgent is null. Running with no clustering");
            }
        }
        return "";
    }

    public String getServerStatus(){
        ServerAdmin serverAdmin = (ServerAdmin) operationsCenterAgentComponent.getServerAdmin();
        if (serverAdmin != null){
            try {
                return serverAdmin.getServerStatus();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "UNKNOWN";
    }
}
