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
package org.talend.esb.job.controller.internal;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.cxf.Bus;
import org.apache.cxf.configuration.security.AuthorizationPolicy;
import org.apache.cxf.transport.http.auth.HttpAuthHeader;
import org.apache.cxf.ws.security.SecurityConstants;
import org.apache.cxf.ws.security.trust.STSClient;
import org.apache.neethi.Policy;
import org.apache.wss4j.common.crypto.Crypto;
import org.talend.esb.job.controller.ESBEndpointConstants.EsbSecurity;
import org.talend.esb.security.saml.STSClientCreator;
// import org.talend.esb.security.saml.SAMLRESTUtils;
import org.talend.esb.security.saml.STSClientUtils;
import org.talend.esb.security.saml.WSPasswordCallbackHandler;

public class SecurityArguments {

    private final EsbSecurity esbSecurity;
    private final Policy policy;
    private final String username;
    private final String password;
    private final String alias;
    private final Map<String, Object> clientProperties;
    private final String roleName;
    private final Object securityToken;
    private final Crypto cryptoProvider;
    private final STSClientCreator stsClientCreator;

    public SecurityArguments(final EsbSecurity esbSecurity,
            final Policy policy,
            String username,
            String password,
            String alias,
            Map<String, Object> clientProperties,
            String roleName,
            Object securityToken,
            Crypto cryptoProvider,
            STSClientCreator stsClientCreator) {
        this.esbSecurity = esbSecurity;
        this.policy = policy;
        this.username = username;
        this.password = password;
        this.alias = alias;
        this.clientProperties = clientProperties;
        this.roleName = roleName;
        this.securityToken = securityToken;
        this.cryptoProvider = cryptoProvider;
        this.stsClientCreator = stsClientCreator;
    }

    public EsbSecurity getEsbSecurity() {
        return esbSecurity;
    }

    public Policy getPolicy() {
        return policy;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getAlias() {
        return alias;
    }

    public Map<String, Object> getClientProperties() {
        return clientProperties;
    }

    public String getRoleName() {
        return roleName;
    }

    public Object getSecurityToken() {
        return securityToken;
    }

    public Crypto getCryptoProvider() {
        return cryptoProvider;
    }

    public STSClientCreator getStsClientCreator() {
        return stsClientCreator;
    }

    public AuthorizationPolicy buildAuthorizationPolicy() {
        AuthorizationPolicy authzPolicy = null;
        if (EsbSecurity.BASIC == esbSecurity) {
            authzPolicy = new AuthorizationPolicy();
            authzPolicy.setUserName(username);
            authzPolicy.setPassword(password);
            authzPolicy.setAuthorizationType(HttpAuthHeader.AUTH_TYPE_BASIC);
        } else if (EsbSecurity.DIGEST == esbSecurity) {
            authzPolicy = new AuthorizationPolicy();
            authzPolicy.setUserName(username);
            authzPolicy.setPassword(password);
            authzPolicy.setAuthorizationType(HttpAuthHeader.AUTH_TYPE_DIGEST);
        }
        return authzPolicy;
    }

    public Map<String, Object> buildClientConfig(final Bus bus, boolean useServiceRegistry, String encryptionUsername) {
        Map<String, Object> clientConfig = new HashMap<String, Object>();

        if (EsbSecurity.TOKEN == esbSecurity || useServiceRegistry) {
            clientConfig.put(SecurityConstants.USERNAME, username);
            clientConfig.put(SecurityConstants.PASSWORD, password);
        }

        if (EsbSecurity.SAML == esbSecurity || useServiceRegistry) {
            final STSClient stsClient = configureSTSClient(bus);
            clientConfig.put(SecurityConstants.STS_CLIENT, stsClient);

            for (Map.Entry<String, Object> entry : clientProperties.entrySet()) {
                if (SecurityConstants.ALL_PROPERTIES.contains(entry.getKey())) {
                    clientConfig.put(entry.getKey(), processFileURI(entry.getValue()));
                }
            }
            if (null == alias) {
                String sigUser = (String) clientProperties.get(SecurityConstants.SIGNATURE_USERNAME);
                if (sigUser == null) {
                    sigUser = (String) clientProperties.get("ws-" + SecurityConstants.SIGNATURE_USERNAME);
                }
                clientConfig.put(SecurityConstants.CALLBACK_HANDLER,
                        new WSPasswordCallbackHandler(sigUser,
                                (String) clientProperties.get(SecurityConstants.SIGNATURE_PASSWORD)));
            } else {
                clientConfig.put(SecurityConstants.SIGNATURE_USERNAME, alias);
                clientConfig.put(SecurityConstants.CALLBACK_HANDLER, new WSPasswordCallbackHandler(alias, password));
            }
            if (null != cryptoProvider) {
                clientConfig.put(SecurityConstants.ENCRYPT_CRYPTO, cryptoProvider);
                Object encryptUsername = clientConfig.get(SecurityConstants.ENCRYPT_USERNAME);
                if (encryptUsername == null) {
                    encryptUsername = clientProperties.get("ws-" + SecurityConstants.ENCRYPT_USERNAME);
                }
                if (encryptUsername == null || encryptUsername.toString().isEmpty()) {
                    clientConfig.put(SecurityConstants.ENCRYPT_USERNAME, encryptionUsername);
                }
            }
        }
        return clientConfig;
    }

    private STSClient configureSTSClient(final Bus bus) {
        final STSClient stsClient;
        if (null == alias) {
            stsClient = stsClientCreator.newSTSClient(bus, username, password);
        } else {
            stsClient = stsClientCreator.newSTSX509Client(bus, alias);
        }

        if (null != roleName && roleName.length() != 0) {
            STSClientUtils.applyAuthorization(stsClient, roleName);
        }
        if (null != securityToken) {
            stsClient.setOnBehalfOf(securityToken);
        }
        return stsClient;
    }

    private static Object processFileURI(Object fileURI) {
        if (fileURI instanceof String) {
            String fileURIName = (String) fileURI;
            if (fileURIName.startsWith("file:")) {
                try {
                    return new URL(fileURIName);
                } catch (MalformedURLException e) {
                    // return input value
                }
            }
        }
        return fileURI;
    }

}
