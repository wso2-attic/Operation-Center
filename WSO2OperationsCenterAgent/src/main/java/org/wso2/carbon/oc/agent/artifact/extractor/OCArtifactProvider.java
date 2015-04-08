package org.wso2.carbon.oc.agent.artifact.extractor;

import org.wso2.carbon.oc.agent.beans.OCArtifact;

import java.util.List;

public interface OCArtifactProvider {
	/**
	 * get artifact details from the bundles
	 * @return
	 */
	public List<OCArtifact> getArtifacts();
	/**
	 * get the type of deployed artifact
	 * @return
	 */
	public String getArtifactType();
}
