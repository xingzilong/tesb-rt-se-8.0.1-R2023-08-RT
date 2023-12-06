/*
 * #%L
 * Service Activity Monitoring :: Agent
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
package org.talend.esb.sam.agent.feature;

import org.apache.cxf.feature.Feature;

/**
 * the public interface of SAM agent
 *
 */
public interface EventFeature extends Feature {

    String SAM_PROPERTIES = "esb.sam.properties";

	/**
	 * If store the content of Message or not
	 * @param logMessageContent
	 */
	void setLogMessageContent(boolean logMessageContent);

	/**
	 * Set the maximum content length
	 * @param maxContentLength
	 */
	void setMaxContentLength(int maxContentLength);

	/**
	 * If enforce transfering MessageID (only for SOAP message)
	 * @param enforceMessageIDTransfer
	 */
	void setEnforceMessageIDTransfer(boolean enforceMessageIDTransfer);

	/**
	 * If message content logging behaviour can be locally overridden
	 * @param logMessageContentOverride
	 */
	void setLogMessageContentOverride(boolean logMessageContentOverride);

}
