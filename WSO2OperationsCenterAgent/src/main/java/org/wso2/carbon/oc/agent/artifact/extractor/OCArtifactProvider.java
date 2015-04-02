package org.wso2.carbon.oc.agent.artifact.extractor;

import java.util.List;

public interface OCArtifactProvider {
	public List<OCArtifactProvider> getArtifacts();

	public String getArtifactType();
}
