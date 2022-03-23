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

package httpclient.org.apache.http.client.protocol;

import httpclient.org.apache.http.auth.AuthSchemeProvider;
import httpclient.org.apache.http.auth.AuthState;
import httpclient.org.apache.http.client.AuthCache;
import httpclient.org.apache.http.client.CookieStore;
import httpclient.org.apache.http.client.CredentialsProvider;
import httpclient.org.apache.http.client.config.RequestConfig;
import httpclient.org.apache.http.conn.routing.HttpRoute;
import httpclient.org.apache.http.conn.routing.RouteInfo;
import httpclient.org.apache.http.cookie.CookieOrigin;
import httpclient.org.apache.http.cookie.CookieSpec;
import httpclient.org.apache.http.cookie.CookieSpecProvider;
import httpcore.org.apache.http.annotation.NotThreadSafe;
import httpcore.org.apache.http.config.Lookup;
import httpcore.org.apache.http.protocol.BasicHttpContext;
import httpcore.org.apache.http.protocol.HttpContext;
import httpcore.org.apache.http.protocol.HttpCoreContext;


import java.net.URI;
import java.util.List;

/**
 * Adaptor class that provides convenience type safe setters and getters
 * for common {@link HttpContext} attributes used in the course
 * of HTTP request execution.
 * <p>httpClient上下文中包含的基本要素
 * <li>路由</li>
 * <li>重定向位置列表</li>
 * <li>cookieSpec注册</li>
 * <li>cookieSpec（cookie管理策略）</li>
 * <li>Cookie源服务器明细</li>
 * <li>Cookie仓库</li>
 * <li>认证凭据provider</li>
 * <li>认证缓存</li>
 * <li>目标主机认证状态</li>
 * <li>代理认证状态</li>
 * <li>用户token</li>
 * <li>认证方案注册</li>
 * <li>请求配置</li>
 * </p>
 *
 * @since 4.3
 */
@NotThreadSafe
public class HttpClientContext extends HttpCoreContext {

    /**
     * object that represents the actual connection route.
     */
    public static final String HTTP_ROUTE = "http.route";

    /**
     * Attribute name of a {@link List} object that represents a collection of all
     * redirect locations received in the process of request execution.
     */
    public static final String REDIRECT_LOCATIONS = "http.protocol.redirect-locations";

    /**
     * the actual {@link CookieSpecProvider} registry.
     */
    public static final String COOKIESPEC_REGISTRY = "http.cookiespec-registry";

    /**
     * Attribute name of a {@link CookieSpec}
     * object that represents the actual cookie specification.
     */
    public static final String COOKIE_SPEC = "http.cookie-spec";

    /**
     * Attribute name of a {@link CookieOrigin}
     * object that represents the actual details of the origin server.
     */
    public static final String COOKIE_ORIGIN = "http.cookie-origin";

    /**
     * Attribute name of a {@link CookieStore}
     * object that represents the actual cookie store.
     */
    public static final String COOKIE_STORE = "http.cookie-store";

    /**
     * Attribute name of a {@link CredentialsProvider}
     * object that represents the actual credentials provider.
     */
    public static final String CREDS_PROVIDER = "http.auth.credentials-provider";

    /**
     * Attribute name of a {@link AuthCache} object
     * that represents the auth scheme cache.
     */
    public static final String AUTH_CACHE = "http.auth.auth-cache";

    /**
     * object that represents the actual target authentication state.
     */
    public static final String TARGET_AUTH_STATE = "http.auth.target-scope";

    /**
     * object that represents the actual proxy authentication state.
     */
    public static final String PROXY_AUTH_STATE = "http.auth.proxy-scope";

    /**
     * Attribute name of a {@link Object} object that represents
     * the actual user identity such as user {@link java.security.Principal}.
     */
    public static final String USER_TOKEN = "http.user-token";

    /**
     * the actual {@link AuthSchemeProvider} registry.
     */
    public static final String AUTHSCHEME_REGISTRY = "http.authscheme-registry";

    /**
     * represents the actual request configuration.
     */
    public static final String REQUEST_CONFIG = "http.request-config";

    public static HttpClientContext adapt(final HttpContext context) {
        if (context instanceof HttpClientContext) {
            return (HttpClientContext) context;
        } else {
            return new HttpClientContext(context);
        }
    }

    public static HttpClientContext create() {
        return new HttpClientContext(new BasicHttpContext());
    }

    public HttpClientContext(final HttpContext context) {
        super(context);
    }

    public HttpClientContext() {
        super();
    }

    public RouteInfo getHttpRoute() {
        return getAttribute(HTTP_ROUTE, HttpRoute.class);
    }

    @SuppressWarnings("unchecked")
    public List<URI> getRedirectLocations() {
        return getAttribute(REDIRECT_LOCATIONS, List.class);
    }

    public CookieStore getCookieStore() {
        return getAttribute(COOKIE_STORE, CookieStore.class);
    }

    public void setCookieStore(final CookieStore cookieStore) {
        setAttribute(COOKIE_STORE, cookieStore);
    }

    public CookieSpec getCookieSpec() {
        return getAttribute(COOKIE_SPEC, CookieSpec.class);
    }

    public CookieOrigin getCookieOrigin() {
        return getAttribute(COOKIE_ORIGIN, CookieOrigin.class);
    }

    @SuppressWarnings("unchecked")
    private <T> Lookup<T> getLookup(final String name, final Class<T> clazz) {
        return getAttribute(name, Lookup.class);
    }

    public Lookup<CookieSpecProvider> getCookieSpecRegistry() {
        return getLookup(COOKIESPEC_REGISTRY, CookieSpecProvider.class);
    }

    public void setCookieSpecRegistry(final Lookup<CookieSpecProvider> lookup) {
        setAttribute(COOKIESPEC_REGISTRY, lookup);
    }

    public Lookup<AuthSchemeProvider> getAuthSchemeRegistry() {
        return getLookup(AUTHSCHEME_REGISTRY, AuthSchemeProvider.class);
    }

    public void setAuthSchemeRegistry(final Lookup<AuthSchemeProvider> lookup) {
        setAttribute(AUTHSCHEME_REGISTRY, lookup);
    }

    public CredentialsProvider getCredentialsProvider() {
        return getAttribute(CREDS_PROVIDER, CredentialsProvider.class);
    }

    public void setCredentialsProvider(final CredentialsProvider credentialsProvider) {
        setAttribute(CREDS_PROVIDER, credentialsProvider);
    }

    public AuthCache getAuthCache() {
        return getAttribute(AUTH_CACHE, AuthCache.class);
    }

    public void setAuthCache(final AuthCache authCache) {
        setAttribute(AUTH_CACHE, authCache);
    }

    public AuthState getTargetAuthState() {
        return getAttribute(TARGET_AUTH_STATE, AuthState.class);
    }

    public AuthState getProxyAuthState() {
        return getAttribute(PROXY_AUTH_STATE, AuthState.class);
    }

    public <T> T getUserToken(final Class<T> clazz) {
        return getAttribute(USER_TOKEN, clazz);
    }

    public Object getUserToken() {
        return getAttribute(USER_TOKEN);
    }

    public void setUserToken(final Object obj) {
        setAttribute(USER_TOKEN, obj);
    }

    public RequestConfig getRequestConfig() {
        final RequestConfig config = getAttribute(REQUEST_CONFIG, RequestConfig.class);
        return config != null ? config : RequestConfig.DEFAULT;
    }

    public void setRequestConfig(final RequestConfig config) {
        setAttribute(REQUEST_CONFIG, config);
    }

}
