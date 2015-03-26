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
package org.wso2.carbon.oc.agent.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@XmlRootElement(name = "Properties")
public class OCPublisherProperties {
	@XmlElement(name = "Property")
	//abstract class wont work concrete class
	private List<OCPublisherProperty> pList;

	public List<OCPublisherProperty> getPropertyList() {
		return pList;
	}

	public void setPropertyList(List<OCPublisherProperty> propertyList) {
		this.pList = propertyList;
	}

	public Map<String, String> getPropertyMap() {
		Map<String, String> map = new HashMap<String, String>();

		List<OCPublisherProperty> propList = this.getPropertyList();
		for (OCPublisherProperty p : propList) {
			map.put(p.getName(), p.getValue());
		}

		return map;
	}
}
