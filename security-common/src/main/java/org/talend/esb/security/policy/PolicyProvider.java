/*
 * #%L
 * Talend :: ESB :: Job :: Controller
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
package org.talend.esb.security.policy;

import org.apache.cxf.Bus;
import org.apache.neethi.Policy;

public interface PolicyProvider {

    // policy id
    String ID_POLICY_USERNAME_TOKEN = "org.talend.esb.job.token.policy";
    String ID_POLICY_SAML_TOKEN = "org.talend.esb.job.saml.policy";
    String ID_POLICY_SAML_AUTHZ = "org.talend.esb.job.saml.authz.policy";
    String ID_POLICY_SAML_TOKEN_CRYPTO = "org.talend.esb.job.saml.crypto.policy";
    String ID_POLICY_SAML_AUTHZ_CRYPTO = "org.talend.esb.job.saml.authz.crypto.policy";

    Policy getUsernamePolicy(Bus cxf);

    Policy getSAMLPolicy(Bus cxf);

    Policy getSAMLAuthzPolicy(Bus cxf);

    Policy getSAMLCryptoPolicy(Bus cxf);

    Policy getSAMLAuthzCryptoPolicy(Bus cxf);

    void register(Bus cxf);

}
