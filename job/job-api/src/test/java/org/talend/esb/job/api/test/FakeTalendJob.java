/*
 * #%L
 * Talend :: ESB :: Job :: API
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
package org.talend.esb.job.api.test;

import routines.system.api.TalendJob;

public class FakeTalendJob implements TalendJob {

	public String[][] runJob(String[] args) {
		System.out.println("TalendJob: Here it goes, the Fake Talend Job, it will take ages to execute");
		System.out.println("TalendJob: done!");
		return new String[0][0];
	}

	public int runJobInTOS(String[] args) {
		runJob(new String[0]);
		return 0;
	}

}
