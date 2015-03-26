package org.wso2.carbon.identity.authenticator.oc.internal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.ComponentContext;
import org.wso2.carbon.CarbonConstants;
import org.wso2.carbon.core.services.authentication.CarbonServerAuthenticator;
import org.wso2.carbon.identity.authenticator.oc.OperationsCenterSSLAuthenticator;
import org.wso2.carbon.user.core.service.RealmService;

import java.util.Hashtable;

/**
 * @scr.component name=
 *                "mutualssl.OperationsCenterSSLAuthenticatorServiceComponent"
 *                immediate="true"
 * @scr.reference name="user.realmservice.default"
 *                interface="org.wso2.carbon.user.core.service.RealmService"
 *                cardinality="1..1" policy="dynamic"
 *                bind="setRealmService"
 *                unbind="unsetRealmService"
 */
public class OperationsCenterSSLAuthenticatorServiceComponent {
    private static final Log log = LogFactory.getLog(OperationsCenterSSLAuthenticatorServiceComponent.class);

    private static RealmService realmService = null;
    private static BundleContext bundleContext = null;

    protected void activate(ComponentContext componentContext) {
        OperationsCenterSSLAuthenticator operationsCenterSSLAuthenticator = new OperationsCenterSSLAuthenticator();
        OperationsCenterSSLAuthenticatorServiceComponent.setBundleContext(componentContext.getBundleContext());

        Hashtable<String, String> properties = new Hashtable<String, String>();
        properties.put(CarbonConstants.AUTHENTICATOR_TYPE, operationsCenterSSLAuthenticator.getAuthenticatorName());

        componentContext.getBundleContext().registerService(CarbonServerAuthenticator.class.getName(),
                operationsCenterSSLAuthenticator, properties);

        if (log.isDebugEnabled()) {
            log.debug("OperationsCenterSSLAuthenticatorServiceComponent is activated.");
        }
    }

    protected void deactivate(ComponentContext componentContext) {
        if (log.isDebugEnabled()) {
            log.debug("OperationsCenterSSLAuthenticatorServiceComponent is deactivated");
        }
    }

    public RealmService getRealmService() {
        return realmService;
    }

    public void setRealmService(RealmService realmService) {
        if (log.isDebugEnabled()) {
            log.debug("Setting realmService for OperationsCenterSSLAuthenticatorServiceComponent.");
        }
        OperationsCenterSSLAuthenticatorServiceComponent.realmService = realmService;
    }

    public void unsetRealmService(RealmService realmService) {
        if (log.isDebugEnabled()) {
            log.debug("Un-setting realmService for OperationsCenterSSLAuthenticatorServiceComponent.");
        }
        OperationsCenterSSLAuthenticatorServiceComponent.realmService = null;
    }

    public BundleContext getBundleContext() {
        return bundleContext;
    }

    public static void setBundleContext(BundleContext bundleContext) {
        OperationsCenterSSLAuthenticatorServiceComponent.bundleContext = bundleContext;
    }
}
