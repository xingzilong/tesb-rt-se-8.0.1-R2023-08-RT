/*
 * #%L
 * Locator Demo Server
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

package demo.service;

import javax.jws.WebService;

import demo.common.Greeter_Https;


@WebService(targetNamespace = "http://talend.org/esb/examples/", serviceName = "Greeter_HttpsService")
public class Greeter_HttpsImpl implements Greeter_Https {

	public String greetMe_Https(String me) {
		System.out.println("Executing operation greetMe_Https");
		System.out.println("Message received: " + me + "\n");
		return "Hello " + me;
	}
}
