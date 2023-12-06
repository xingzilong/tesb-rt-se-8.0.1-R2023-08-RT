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
package org.talend.esb.servicelocator.client.internal;

import static org.easymock.EasyMock.*;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.talend.esb.servicelocator.TestContent.createContent;
import static org.talend.esb.servicelocator.TestValues.ENDPOINT_1;
import static org.talend.esb.servicelocator.TestValues.ENDPOINT_2;
import static org.talend.esb.servicelocator.TestValues.PROPERTIES_1;
import static org.talend.esb.servicelocator.TestValues.SERVICE_QNAME_1;
import static org.talend.esb.servicelocator.TestValues.SERVICE_QNAME_2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.namespace.QName;

import org.junit.Assert;
import org.easymock.Capture;
import org.easymock.CaptureType;
import org.easymock.EasyMockSupport;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.talend.esb.servicelocator.client.EndpointNotFoundException;
import org.talend.esb.servicelocator.client.ServiceLocatorException;
import org.talend.esb.servicelocator.client.WrongArgumentException;

public class ServiceLocatorImplTest extends EasyMockSupport {

    private ServiceLocatorBackend backend;

    private RootNode rootNode;

    private ServiceNode serviceNode;

    private EndpointNode endpointNode;

    public static void ignore(String txt) {
    }

    @Before
    public void setUp() throws Exception {
        backend = createMock(ServiceLocatorBackend.class);
        rootNode = createMock(RootNode.class);
        serviceNode = createMock(ServiceNode.class);
        endpointNode = createMock(EndpointNode.class);
        Logger LOG = Logger.getLogger(ServiceLocatorImpl.class.getName());
        LOG.setLevel(Level.FINE);
    }

    @Test
    public void connect() throws Exception {
        expect(backend.connect()).andReturn(rootNode);
        replayAll();

        ServiceLocatorImpl slc = new ServiceLocatorImpl();
        slc.setBackend(backend);
        slc.connect();

        verifyAll();
    }

    @Test
    public void connectFailing() throws Exception {
        expect(backend.connect()).andThrow(new ServiceLocatorException());

        replayAll();

        ServiceLocatorImpl slc = new ServiceLocatorImpl();
        slc.setBackend(backend);

        try {
            slc.connect();
            fail("A ServiceLocatorException should have been thrown.");
        } catch (ServiceLocatorException e) {
            ignore("Expected exception");
        }

        verifyAll();
    }

    @Test
    public void failureWhenRegisteringService() throws Exception {
        expect(backend.connect()).andReturn(rootNode);
        expect(rootNode.getServiceNode(SERVICE_QNAME_1)).andReturn(serviceNode);
        serviceNode.ensureExists();
        expectLastCall().andThrow(new ServiceLocatorException());

        replayAll();

        ServiceLocatorImpl slc = new ServiceLocatorImpl();
        slc.setBackend(backend);

        try {
            slc.register(SERVICE_QNAME_1, ENDPOINT_1);
            fail("A ServiceLocatorException should have been thrown.");
        } catch (ServiceLocatorException e) {
            ignore("Expected exception");
        }

        verifyAll();
    }

    @Test
    public void removeEndpoint() throws Exception {
        expect(backend.connect()).andReturn(rootNode);
        expect(rootNode.getServiceNode(SERVICE_QNAME_1)).andReturn(serviceNode);
        expect(serviceNode.getEndPoint(ENDPOINT_1)).andReturn(endpointNode);
        endpointNode.ensureRemoved();
        replayAll();

        ServiceLocatorImpl slc = new ServiceLocatorImpl();
        slc.setBackend(backend);
        slc.removeEndpoint(SERVICE_QNAME_1, ENDPOINT_1);

        verifyAll();
    }

    @Test
    public void removeEndpointFails() throws Exception {
        expect(backend.connect()).andReturn(rootNode);
        expect(rootNode.getServiceNode(SERVICE_QNAME_1)).andReturn(serviceNode);
        expect(serviceNode.getEndPoint(ENDPOINT_1)).andReturn(endpointNode);
        endpointNode.ensureRemoved();
        expectLastCall().andThrow(new ServiceLocatorException());
        replayAll();

        ServiceLocatorImpl slc = new ServiceLocatorImpl();
        slc.setBackend(backend);
        try {
            slc.removeEndpoint(SERVICE_QNAME_1, ENDPOINT_1);
            fail("A ServiceLocatorException should have been thrown.");
        } catch (ServiceLocatorException e) {
            ignore("Expected exception");
        }
        verifyAll();
    }

    @Test
    public void lookupServiceKnownEndpointIsLive() throws Exception {
        expect(backend.connect()).andReturn(rootNode);
        expect(rootNode.getServiceNode(SERVICE_QNAME_1)).andReturn(serviceNode);
        expect(serviceNode.exists()).andReturn(true);
        expect(serviceNode.getEndPoints()).andReturn(Arrays.asList(endpointNode));
        expect(endpointNode.isLive()).andReturn(true);
        expect(endpointNode.getEndpointName()).andStubReturn(ENDPOINT_1);
        //expect(endpointNode.getEndpointName()).andReturn(ENDPOINT_1);
        expect(endpointNode.getContent()).andStubReturn(createContent(PROPERTIES_1));

        replayAll();

        ServiceLocatorImpl slc = new ServiceLocatorImpl();
        slc.setBackend(backend);

        List<String> endpoints = slc.lookup(SERVICE_QNAME_1);

        assertThat(endpoints, hasItem(ENDPOINT_1));
        verifyAll();
    }

    @Ignore
    @Test
    public void lookupServiceKnownEndpointsAvailableWithProperties() throws Exception {
        /*
         * SLPropertiesMatcher matcher = new SLPropertiesMatcher();
         * matcher.addAssertion(NAME_1, VALUE_2);
         *
         * pathExists(SERVICE_PATH_1); getChildren(SERVICE_PATH_1,
         * ENDPOINT_NODE_1, ENDPOINT_NODE_2);
         *
         * pathExists(ENDPOINT_STATUS_PATH_11); getData(ENDPOINT_PATH_11,
         * createContent(PROPERTIES_1));
         *
         * pathExists(ENDPOINT_STATUS_PATH_12); getData(ENDPOINT_PATH_12,
         * createContent(PROPERTIES_2));
         *
         * replayAll();
         *
         * ServiceLocatorImpl slc = createServiceLocatorSuccess(); List<String>
         * endpoints = slc.lookup(SERVICE_QNAME_1, matcher);
         *
         * assertThat(endpoints, containsInAnyOrder(ENDPOINT_1)); verifyAll();
         */
    }

    @Test
    public void lookupServiceNotKnown() throws Exception {
        expect(backend.connect()).andReturn(rootNode);
        expect(rootNode.getServiceNode(SERVICE_QNAME_1)).andReturn(serviceNode);
        expect(serviceNode.exists()).andReturn(false);

        replayAll();

        ServiceLocatorImpl slc = new ServiceLocatorImpl();
        slc.setBackend(backend);

        List<String> endpoints = slc.lookup(SERVICE_QNAME_1);

        assertThat(endpoints, empty());
        verifyAll();
    }

    @Test
    public void getServicesSuccessful() throws Exception {
        expect(backend.connect()).andReturn(rootNode);
        expect(rootNode.getServiceNames()).andReturn(Arrays.asList(SERVICE_QNAME_1, SERVICE_QNAME_2));
        replayAll();

        ServiceLocatorImpl slc = new ServiceLocatorImpl();
        slc.setBackend(backend);
        List<QName> services = slc.getServices();

        assertThat(services, containsInAnyOrder(SERVICE_QNAME_1, SERVICE_QNAME_2));
        verifyAll();
    }

    @Test
    public void failureWhenGettingServices() throws Exception {
        expect(backend.connect()).andReturn(rootNode);
        expect(rootNode.getServiceNames()).andThrow(new ServiceLocatorException());
        replayAll();

        ServiceLocatorImpl slc = new ServiceLocatorImpl();
        slc.setBackend(backend);

        try {
            slc.getServices();
            fail("A ServiceLocatorException should have been thrown.");
        } catch (ServiceLocatorException e) {
            ignore("Expected exception");
        }
        verifyAll();
    }

    @Test
    public void getEndpointNamesServiceExists() throws Exception {
        expect(backend.connect()).andReturn(rootNode);
        expect(rootNode.getServiceNode(SERVICE_QNAME_1)).andReturn(serviceNode);
        expect(serviceNode.exists()).andReturn(true);
        expect(serviceNode.getEndpointNames()).andReturn(Arrays.asList(ENDPOINT_1, ENDPOINT_2));

        replayAll();

        ServiceLocatorImpl slc = new ServiceLocatorImpl();
        slc.setBackend(backend);

        List<String> endpoints = slc.getEndpointNames(SERVICE_QNAME_1);

        assertThat(endpoints, containsInAnyOrder(ENDPOINT_1, ENDPOINT_2));
        verifyAll();
    }

    @Test
    public void getEndpointNamesServiceExistsNot() throws Exception {
        expect(backend.connect()).andReturn(rootNode);
        expect(rootNode.getServiceNode(SERVICE_QNAME_1)).andReturn(serviceNode);
        expect(serviceNode.exists()).andReturn(false);

        replayAll();

        ServiceLocatorImpl slc = new ServiceLocatorImpl();
        slc.setBackend(backend);

        List<String> endpoints = slc.getEndpointNames(SERVICE_QNAME_1);

        assertThat(endpoints, empty());
        verifyAll();
    }

    @Test
    public void updateTimetolive() throws Exception {
        final int ttl = 95;

        Capture<Date> dateCap = Capture.newInstance(CaptureType.LAST);

        expect(backend.connect()).andReturn(rootNode);
        expect(rootNode.getServiceNode(SERVICE_QNAME_1)).andReturn(serviceNode);
        expect(serviceNode.getEndPoint(ENDPOINT_1)).andReturn(endpointNode);
        expect(endpointNode.exists()).andReturn(true);
        endpointNode.setLive(true);
        endpointNode.setExpiryTime(capture(dateCap), eq(true));

        replayAll();

        ServiceLocatorImpl slc = new ServiceLocatorImpl();
        slc.setBackend(backend);

        Date startD = new Date();

        slc.updateTimetolive(SERVICE_QNAME_1, ENDPOINT_1, ttl);

        verifyAll();

        assertNotNull(dateCap.getValue());
        assertTrue(dateCap.getValue().after(startD));
        assertTrue(dateCap.getValue().before(new Date(startD.getTime() + (ttl + 5) * 1000)));
    }

    @Test
    public void updateTimetoliveWrongArgument() throws Exception {
        ServiceLocatorImpl slc = new ServiceLocatorImpl();
        slc.setBackend(backend);

        try {
            slc.updateTimetolive(SERVICE_QNAME_1, ENDPOINT_1, -3);
            fail();
        } catch (WrongArgumentException e) {
            // pass
        }

        try {
            slc.updateTimetolive(SERVICE_QNAME_1, ENDPOINT_1, 0);
            fail();
        } catch (WrongArgumentException e) {
            // pass
        }
    }

    @Test
    public void updateTimetoliveEndpointNotFound() throws Exception {
        final int ttl = 95;

        expect(backend.connect()).andReturn(rootNode);
        expect(rootNode.getServiceNode(SERVICE_QNAME_1)).andReturn(serviceNode);
        expect(serviceNode.getEndPoint(ENDPOINT_1)).andReturn(endpointNode);
        expect(endpointNode.exists()).andReturn(false);

        replayAll();

        ServiceLocatorImpl slc = new ServiceLocatorImpl();
        slc.setBackend(backend);

        try {
            slc.updateTimetolive(SERVICE_QNAME_1, ENDPOINT_1, ttl);
            fail();
        } catch (EndpointNotFoundException e) {
            // pass
        }

        verifyAll();
    }

    @Test
    public void scheduleAndPerformCollection() throws Exception {
        final int ttl = 95;

        List<String> l = new ArrayList<String>();
        l.add("superEndpointName");

        Capture<Date> dateCap = Capture.newInstance(CaptureType.LAST);

        expect(backend.connect()).andStubReturn(rootNode);
        expect(rootNode.getServiceNode(SERVICE_QNAME_1)).andStubReturn(serviceNode);
        expect(serviceNode.getEndPoint(ENDPOINT_1)).andStubReturn(endpointNode);
        expect(serviceNode.exists()).andStubReturn(true);
        expect(serviceNode.getEndpointNames()).andStubReturn(l);
        expect(endpointNode.exists()).andReturn(true);
        endpointNode.setLive(true);
        endpointNode.setExpiryTime(capture(dateCap), eq(true));

        replayAll();

        ServiceLocatorImpl slc = new ServiceLocatorImpl();
        slc.setBackend(backend);

        Date startD = new Date();

        slc.updateTimetolive(SERVICE_QNAME_1, ENDPOINT_1, 1);
        verifyAll();

        List<String> eps = slc.getEndpointNames(SERVICE_QNAME_1);
        Assert.assertNotNull(eps);
        Assert.assertEquals(1, eps.size());



    }
}
