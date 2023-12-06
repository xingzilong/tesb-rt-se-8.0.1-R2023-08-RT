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

import java.util.Collections;

import org.apache.camel.EndpointInject;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

public class TalendComponentParamTest extends CamelTestSupport {

    @EndpointInject(uri = "mock:result")
    protected MockEndpoint resultEndpoint;

    @Test
    public void testJobWithoutContext() throws Exception {
        resultEndpoint.expectedBodiesReceived("propagateHeader=false");
        sendBody("direct:test", "propagateHeader=false");
        resultEndpoint.assertIsSatisfied();
    }

    @Test
    public void testJobWithContext() throws Exception {
        resultEndpoint.expectedBodiesReceived("--context=Default");
        sendBody("direct:test", "context=Default&propagateHeader=false");
        resultEndpoint.assertIsSatisfied();
    }

    @Test
    public void testJobWithParamFromHeaders() throws Exception {
        context.setUseBreadcrumb(false);
        resultEndpoint.expectedBodiesReceived("--context_param header=value");
        sendBody("direct:test", null, Collections.singletonMap("header", (Object) "value"));
        resultEndpoint.assertIsSatisfied();
    }

    @Test
    public void testJobParamFromContext() throws Exception {
        resultEndpoint.expectedBodiesReceived("--context_param property=context");
        context.getGlobalOptions().put("property", "context");
        sendBody("direct:test", "propagateHeader=false");
        resultEndpoint.assertIsSatisfied();
    }

    @Test
    public void testJobParamFromEndpoint() throws Exception {
        resultEndpoint.expectedBodiesReceived("--context_param property=endpoint");
        sendBody("direct:test", "propagateHeader=false&endpointProperties.property=endpoint");
        resultEndpoint.assertIsSatisfied();
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            public void configure() {
                from("direct:test")
                    .recipientList(simple("talend://org.talend.camel.TestJob?${body}"))
                    .to("mock:result");
            }
        };
    }
}
