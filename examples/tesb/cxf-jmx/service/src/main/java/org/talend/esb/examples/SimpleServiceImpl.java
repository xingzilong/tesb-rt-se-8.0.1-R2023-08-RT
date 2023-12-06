/*
 * #%L
 * CXF :: Example :: SimpleService :: TESB container
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

package org.talend.esb.examples;

import javax.jws.WebService;

@WebService
public class SimpleServiceImpl implements SimpleService {

	@Override
	public String sayHi(String name) {

		if (name.equals("Joe"))
			throw new RuntimeException("Incorrect name");

		return "Hi " + name + "!";
	}

	@Override
	public int doubleIt(int arg) {
		return arg * 2;
	}

}
