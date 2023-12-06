/*
 * #%L
 * Talend ESB :: Camel Talend Job Component
 * %%
 * Copyright (c) 2006-2021 Talend Inc. - www.talend.com
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

package org.talend.camel;

import java.util.HashMap;
import java.util.Map;

import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;
import org.apache.camel.support.DefaultComponent;
import org.apache.camel.util.PropertiesHelper;

/**
 * <p>
 * Represents the component that manages {@link TalendEndpoint}.
 * </p>
 */
public class TalendComponent extends DefaultComponent {

    public TalendComponent() {
        super();
    }

    public TalendComponent(CamelContext context) {
        super(context);
    }

    protected Endpoint createEndpoint(String uri, String remaining, Map<String, Object> parameters)
        throws Exception {
        final TalendEndpoint endpoint = new TalendEndpoint(uri, remaining, this);
        endpoint.setStickyJob(isTrue(parameters.remove("sticky")));
        // extract the properties.xxx and set them as properties
        Map<String, Object> properties =
                PropertiesHelper.extractProperties(parameters, "endpointProperties.");
        if (properties != null) {
            Map<String, String> endpointProperties = new HashMap<>(properties.size());
            for (Map.Entry<String, Object> entry : properties.entrySet()) {
                endpointProperties.put(entry.getKey(), entry.getValue().toString());
            }
            endpoint.setEndpointProperties(endpointProperties);
        }
        setProperties(endpoint, parameters);
        return endpoint;
    }

    private static boolean isTrue(Object value) {
    	if (value == null) {
    		return false;
    	}
    	if (value instanceof Boolean) {
    		return ((Boolean) value).booleanValue();
    	}
    	if (value instanceof String) {
    		return "true".equalsIgnoreCase((String) value);
    	}
    	return false;
    }
}
