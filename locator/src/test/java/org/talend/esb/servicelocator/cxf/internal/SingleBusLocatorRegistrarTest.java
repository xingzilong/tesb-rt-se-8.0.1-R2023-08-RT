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

import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.expect;
import static org.junit.Assert.fail;
import static org.talend.esb.servicelocator.TestValues.ENDPOINT_1;
import static org.talend.esb.servicelocator.TestValues.ENDPOINT_2;
import static org.talend.esb.servicelocator.TestValues.PREFIXES_1;
import static org.talend.esb.servicelocator.TestValues.PREFIX_1;
import static org.talend.esb.servicelocator.TestValues.SERVICE_QNAME_1;
import static org.talend.esb.servicelocator.TestValues.SERVICE_QNAME_2;
import static org.talend.esb.servicelocator.cxf.internal.CXFTestStubs.REL_SERVER_1;
import static org.talend.esb.servicelocator.cxf.internal.CXFTestStubs.SERVER_1;
import static org.talend.esb.servicelocator.cxf.internal.CXFTestStubs.SERVER_2;

import java.util.Collections;
import java.util.List;

import org.apache.cxf.Bus;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.endpoint.ServerLifeCycleListener;
import org.apache.cxf.endpoint.ServerLifeCycleManager;
import org.apache.cxf.endpoint.ServerRegistry;
import org.apache.cxf.ws.policy.PolicyEngine;
import org.easymock.Capture;
import org.easymock.CaptureType;
import org.easymock.EasyMockSupport;
import org.junit.Test;
import org.talend.esb.servicelocator.client.ServiceLocator;
import org.talend.esb.servicelocator.client.ServiceLocator.PostConnectAction;

public class SingleBusLocatorRegistrarTest extends EasyMockSupport {

    private ServiceLocator sl = createMock(ServiceLocator.class);

    @Test
    public void postConnectActionRegistered() {
        sl.addPostConnectAction((PostConnectAction) anyObject());
        Bus bus = createMock(Bus.class);
        expect(bus.getExtension(ServerLifeCycleManager.class)).andStubReturn(null);
        replayAll();

        SingleBusLocatorRegistrar locatorRegistrar = new SingleBusLocatorRegistrar(bus);
        locatorRegistrar.setServiceLocator(sl);

        verifyAll();
    }

    @Test
    public void registerEndpoint() throws Exception {
        CXFEndpointProvider endpoint = new CXFEndpointProvider(SERVICE_QNAME_1, ENDPOINT_1, null);
        sl.addPostConnectAction((PostConnectAction) anyObject());
        sl.register(endpoint);
        Bus bus = createMock(Bus.class);
        expect(bus.getExtension(ServerLifeCycleManager.class)).andStubReturn(null);
        replayAll();

        SingleBusLocatorRegistrar locatorRegistrar = new SingleBusLocatorRegistrar(bus);
        locatorRegistrar.setServiceLocator(sl);

        locatorRegistrar.registerServer(SERVER_1);

        verifyAll();
    }

    @Test
    public void registerEndpointWithRelativeAddressWhenPrefixSet() throws Exception {
        CXFEndpointProvider endpoint = new CXFEndpointProvider(SERVICE_QNAME_1, ENDPOINT_1, null);

        sl.addPostConnectAction((PostConnectAction) anyObject());
        sl.register(endpoint);
        Bus bus = createMock(Bus.class);
        expect(bus.getExtension(ServerLifeCycleManager.class)).andStubReturn(null);
        replayAll();

        SingleBusLocatorRegistrar locatorRegistrar = new SingleBusLocatorRegistrar(bus);
        locatorRegistrar.setEndpointPrefix(PREFIX_1);
        locatorRegistrar.setServiceLocator(sl);

        locatorRegistrar.registerServer(REL_SERVER_1);

        verifyAll();
    }

    @Test
    public void registerEndpointWithAbsAddressWhenPrefixSet() throws Exception {
        CXFEndpointProvider endpoint = new CXFEndpointProvider(SERVICE_QNAME_1, ENDPOINT_1, null);

        sl.addPostConnectAction((PostConnectAction) anyObject());
        sl.register(endpoint);
        Bus bus = createMock(Bus.class);
        expect(bus.getExtension(ServerLifeCycleManager.class)).andStubReturn(null);
        replayAll();

        SingleBusLocatorRegistrar locatorRegistrar = new SingleBusLocatorRegistrar(bus);
        locatorRegistrar.setEndpointPrefix(PREFIX_1);
        locatorRegistrar.setServiceLocator(sl);

        locatorRegistrar.registerServer(SERVER_1);

        verifyAll();
    }

    @Test
    public void registerEndpointWithRelativeAddressWhenPrefixesSet() throws Exception {
        CXFEndpointProvider endpoint = new CXFEndpointProvider(SERVICE_QNAME_1, ENDPOINT_1, null);

        sl.addPostConnectAction((PostConnectAction) anyObject());
        sl.register(endpoint);
        Bus bus = createMock(Bus.class);
        expect(bus.getExtension(ServerLifeCycleManager.class)).andStubReturn(null);
        expect(bus.getExtension(PolicyEngine.class)).andStubReturn(null);
        replayAll();

        SingleBusLocatorRegistrar locatorRegistrar = new SingleBusLocatorRegistrar(bus);
		locatorRegistrar.setEndpointPrefixes(PREFIXES_1);
        locatorRegistrar.setServiceLocator(sl);

        locatorRegistrar.registerServer(REL_SERVER_1);

        verifyAll();
    }

    @Test
    public void registerEndpointLocatorNull() throws Exception {
        Bus bus = createMock(Bus.class);
        expect(bus.getExtension(ServerLifeCycleManager.class)).andStubReturn(null);
        replayAll();

        SingleBusLocatorRegistrar locatorRegistrar = new SingleBusLocatorRegistrar(bus);

        try {
            locatorRegistrar.registerServer(SERVER_1);
            fail("An IllegalStateException should have been thrown.");
        } catch (IllegalStateException e) {

        }

        verifyAll();
    }

    @Test
    public void registerServerLifeCycleListener() {
        ServerLifeCycleManager slcm = createMock(ServerLifeCycleManager.class);
        slcm.registerListener((ServerLifeCycleListener) anyObject());
        Bus bus = createMock(Bus.class);
        expect(bus.getExtension(ServerLifeCycleManager.class)).andStubReturn(slcm);
        replayAll();

        new SingleBusLocatorRegistrar(bus);

        verifyAll();
    }

    @Test
    public void startListenForServer() throws Exception {
        CXFEndpointProvider endpoint = new CXFEndpointProvider(SERVICE_QNAME_1, ENDPOINT_1, null);
        Bus bus = createMock(Bus.class);

        Capture<ServerLifeCycleListener> slclCapture = addServerLifeCycleManager(bus);

        List<Server> servers = Collections.emptyList();
        addRegisteredServers(bus, servers);

        sl.addPostConnectAction((PostConnectAction) anyObject());
        sl.register(endpoint);

        replayAll();

        SingleBusLocatorRegistrar locatorRegistrar = new SingleBusLocatorRegistrar(bus);
        locatorRegistrar.setServiceLocator(sl);
        locatorRegistrar.startListenForServers();

        ServerLifeCycleListener listener = slclCapture.getValue();
        listener.startServer(SERVER_1);

        verifyAll();
    }

    @Test
    public void ignoreStartedServersIfNotStartListenForServerCalled() throws Exception {
        Bus bus = createMock(Bus.class);

        Capture<ServerLifeCycleListener> slclCapture = addServerLifeCycleManager(bus);

        List<Server> servers = Collections.emptyList();
        addRegisteredServers(bus, servers);

        sl.addPostConnectAction((PostConnectAction) anyObject());

        replayAll();

        SingleBusLocatorRegistrar locatorRegistrar = new SingleBusLocatorRegistrar(bus);
        locatorRegistrar.setServiceLocator(sl);

        ServerLifeCycleListener listener = slclCapture.getValue();
        listener.startServer(SERVER_1);

        verifyAll();
    }

    @Test
    public void serverStopsThenEndpointUnregistered() throws Exception {
        CXFEndpointProvider endpoint = new CXFEndpointProvider(SERVICE_QNAME_1, ENDPOINT_1, null);

        sl.addPostConnectAction((PostConnectAction) anyObject());
        sl.register(endpoint);
        sl.unregister(endpoint);

        Bus bus = createMock(Bus.class);
        expect(bus.getExtension(ServerLifeCycleManager.class)).andStubReturn(null);

        replayAll();

        SingleBusLocatorRegistrar locatorRegistrar = new SingleBusLocatorRegistrar(bus);
        locatorRegistrar.setServiceLocator(sl);

        locatorRegistrar.registerServer(SERVER_1);
        locatorRegistrar.stopServer(SERVER_1);

        verifyAll();
    }

    @Test
    public void serverStopsIfNotRegisteredBeforeDoNothing() throws Exception {
        sl.addPostConnectAction((PostConnectAction) anyObject());

        Bus bus = createMock(Bus.class);
        expect(bus.getExtension(ServerLifeCycleManager.class)).andStubReturn(null);

        replayAll();

        SingleBusLocatorRegistrar locatorRegistrar = new SingleBusLocatorRegistrar(bus);
        locatorRegistrar.setServiceLocator(sl);

        locatorRegistrar.stopServer(SERVER_1);

        verifyAll();
    }

    @Test
    public void processReregisterAllEndpoints() throws Exception {
        CXFEndpointProvider endpoint1 = new CXFEndpointProvider(SERVICE_QNAME_1, ENDPOINT_1, null);
        CXFEndpointProvider endpoint2 = new CXFEndpointProvider(SERVICE_QNAME_2, ENDPOINT_2, null);


        sl.addPostConnectAction((PostConnectAction) anyObject());

        sl.register(endpoint1);
        sl.register(endpoint2);
        sl.register(endpoint1);
        sl.register(endpoint2);

        Bus bus = createMock(Bus.class);
        expect(bus.getExtension(ServerLifeCycleManager.class)).andStubReturn(null);

        replayAll();

        SingleBusLocatorRegistrar locatorRegistrar = new SingleBusLocatorRegistrar(bus);
        locatorRegistrar.setServiceLocator(sl);

        locatorRegistrar.registerServer(SERVER_1);
        locatorRegistrar.registerServer(SERVER_2);

        locatorRegistrar.process(sl);

        verifyAll();
    }

    private Capture<ServerLifeCycleListener> addServerLifeCycleManager(Bus bus) {
        Capture<ServerLifeCycleListener> slclCapture = Capture.newInstance(CaptureType.LAST);

        ServerLifeCycleManager slcm = createMock(ServerLifeCycleManager.class);
        slcm.registerListener(capture(slclCapture));

        expect(bus.getExtension(ServerLifeCycleManager.class)).andStubReturn(slcm);

        return slclCapture;
    }

    private Bus addRegisteredServers(Bus bus, List<Server> registeredServers) {
        ServerRegistry sr = createMock(ServerRegistry.class);
        expect(sr.getServers()).andStubReturn(registeredServers);

        expect(bus.getExtension(ServerRegistry.class)).andStubReturn(sr);
        return null;
    }
}
