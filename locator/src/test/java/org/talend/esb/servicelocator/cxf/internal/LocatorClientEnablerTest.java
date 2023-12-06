/*
 * #%L
 * Service Locator Client for CXF
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
package org.talend.esb.servicelocator.cxf.internal;

import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import org.apache.cxf.clustering.FailoverStrategy;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.endpoint.ConduitSelector;
import org.apache.cxf.endpoint.Endpoint;
import org.easymock.Capture;
import org.easymock.CaptureType;
import org.junit.Before;
import org.junit.Test;
import org.talend.esb.servicelocator.client.ServiceLocator;

public class LocatorClientEnablerTest {

    private ConduitSelector conduitSelector = createMock(ConduitSelector.class);

    private Endpoint endpoint = createMock(Endpoint.class);

    private LocatorSelectionStrategyMap locatorSelectionStrategyMap;

    @Before
    public void setUp() {
        locatorSelectionStrategyMap = new LocatorSelectionStrategyMap();
        locatorSelectionStrategyMap.init();

        expect(conduitSelector.getEndpoint()).andStubReturn(endpoint);
        replay(conduitSelector);
    }

    @Test
    public void enableClient() {
        ServiceLocator sl = createMock(ServiceLocator.class);

        Capture<LocatorTargetSelector> capturedSelector = Capture.newInstance(CaptureType.LAST);

        Client client = createMock(Client.class);
        expect(client.getConduitSelector()).andStubReturn(conduitSelector);
        client.setConduitSelector(capture(capturedSelector));
        replay(client);

        LocatorClientEnabler clientRegistrar = new LocatorClientEnabler();
        clientRegistrar.setServiceLocator(sl);
        clientRegistrar.setLocatorSelectionStrategyMap(locatorSelectionStrategyMap);
        // clientRegistrar.setLocatorSelectionStrategy("defaultSelectionStrategy");
        clientRegistrar.setDefaultLocatorSelectionStrategy("evenDistributionSelectionStrategy");
        clientRegistrar.enable(client);

        LocatorTargetSelector selector = capturedSelector.getValue();
        assertEquals(endpoint, selector.getEndpoint());

        FailoverStrategy strategy = selector.getStrategy();
        assertThat(strategy, instanceOf(LocatorSelectionStrategy.class));

        verify(client);
    }

}
