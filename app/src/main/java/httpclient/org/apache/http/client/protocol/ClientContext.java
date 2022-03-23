package httpclient.org.apache.http.client.protocol;

/*
 * ====================================================================
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */


/**
 * client side HTTP protocol processing.
 *
 * @since 4.0
 *
 * @deprecated (4.3) use {@link HttpClientContext}.
 */
@Deprecated
public interface ClientContext {

    /**
     * object that represents the actual connection route.
     *
     * @since 4.3
     */
    public static final String ROUTE   = "http.route";

    /**
     * object that represents the actual protocol scheme registry.
     */
    public static final String SCHEME_REGISTRY   = "http.scheme-registry";

    /**
     */
    public static final String COOKIESPEC_REGISTRY   = "http.cookiespec-registry";

    /**
     * object that represents the actual cookie specification.
     */
    public static final String COOKIE_SPEC           = "http.cookie-spec";

    /**
     * object that represents the actual details of the origin server.
     */
    public static final String COOKIE_ORIGIN         = "http.cookie-origin";

    /**
     * object that represents the actual cookie store.
     */
    public static final String COOKIE_STORE          = "http.cookie-store";

    /**
     * object that represents the actual credentials provider.
     */
    public static final String CREDS_PROVIDER        = "http.auth.credentials-provider";

    /**
     * that represents the auth scheme cache.
     */
    public static final String AUTH_CACHE            = "http.auth.auth-cache";

    /**
     * object that represents the actual target authentication state.
     */
    public static final String TARGET_AUTH_STATE     = "http.auth.target-scope";

    /**
     * object that represents the actual proxy authentication state.
     */
    public static final String PROXY_AUTH_STATE      = "http.auth.proxy-scope";

    public static final String AUTH_SCHEME_PREF      = "http.auth.scheme-pref";

    /**
     * Attribute name of a {@link java.lang.Object} object that represents
     * the actual user identity such as user {@link java.security.Principal}.
     */
    public static final String USER_TOKEN            = "http.user-token";

    /**
     */
    public static final String AUTHSCHEME_REGISTRY   = "http.authscheme-registry";

    public static final String SOCKET_FACTORY_REGISTRY = "http.socket-factory-registry";

    /**
     * represents the actual request configuration.
     *
     * @since 4.3
     */
    public static final String REQUEST_CONFIG = "http.request-config";

}
