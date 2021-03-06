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

package httpasyncclient.org.apache.http.impl.nio.client;

import httpasyncclient.org.apache.http.impl.nio.conn.PoolingNHttpClientConnectionManager;
import httpasyncclient.org.apache.http.nio.conn.NHttpClientConnectionManager;
import httpclient.org.apache.http.client.protocol.RequestAddCookies;
import httpclient.org.apache.http.client.protocol.RequestClientConnControl;
import httpclient.org.apache.http.client.protocol.ResponseProcessCookies;
import httpclient.org.apache.http.conn.ConnectionKeepAliveStrategy;
import httpclient.org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import httpcore.org.apache.http.ConnectionReuseStrategy;
import httpcore.org.apache.http.impl.DefaultConnectionReuseStrategy;
import httpcore.org.apache.http.protocol.*;
import httpcore.org.apache.http.util.VersionInfo;
import httpcorenio.org.apache.http.impl.nio.reactor.IOReactorConfig;
import httpcorenio.org.apache.http.nio.NHttpClientEventHandler;
import httpcorenio.org.apache.http.nio.protocol.HttpAsyncRequestExecutor;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * Builder for {@link   MinimalHttpAsyncClient} instances.
 *
 * @since 4.1
 */
class MinimalHttpAsyncClientBuilder {

    private NHttpClientConnectionManager connManager;
    private boolean connManagerShared;
    private ConnectionReuseStrategy reuseStrategy;
    private ConnectionKeepAliveStrategy keepAliveStrategy;
    private String userAgent;
    private ThreadFactory threadFactory;
    private boolean cookieManagementDisabled;

    public static MinimalHttpAsyncClientBuilder create() {
        return new MinimalHttpAsyncClientBuilder();
    }

    protected MinimalHttpAsyncClientBuilder() {
        super();
    }

    public final MinimalHttpAsyncClientBuilder setConnectionManager(
            final NHttpClientConnectionManager connManager) {
        this.connManager = connManager;
        return this;
    }

    public final MinimalHttpAsyncClientBuilder setConnectionManagerShared(
            final boolean shared) {
        this.connManagerShared = shared;
        return this;
    }

    public final MinimalHttpAsyncClientBuilder setConnectionReuseStrategy(
            final ConnectionReuseStrategy reuseStrategy) {
        this.reuseStrategy = reuseStrategy;
        return this;
    }

    public final MinimalHttpAsyncClientBuilder setKeepAliveStrategy(
            final ConnectionKeepAliveStrategy keepAliveStrategy) {
        this.keepAliveStrategy = keepAliveStrategy;
        return this;
    }

    public final MinimalHttpAsyncClientBuilder setUserAgent(final String userAgent) {
        this.userAgent = userAgent;
        return this;
    }

    public final MinimalHttpAsyncClientBuilder setThreadFactory(final ThreadFactory threadFactory) {
        this.threadFactory = threadFactory;
        return this;
    }

    public final MinimalHttpAsyncClientBuilder disableCookieManagement() {
        cookieManagementDisabled = true;
        return this;
    }

    public MinimalHttpAsyncClient build() {

        NHttpClientConnectionManager connManager = this.connManager;
        if (connManager == null) {
            connManager = new PoolingNHttpClientConnectionManager(IOReactorUtils.create(IOReactorConfig.DEFAULT,
                    threadFactory));
        }
        ConnectionReuseStrategy reuseStrategy = this.reuseStrategy;
        if (reuseStrategy == null) {
            reuseStrategy = DefaultConnectionReuseStrategy.INSTANCE;
        }
        ConnectionKeepAliveStrategy keepAliveStrategy = this.keepAliveStrategy;
        if (keepAliveStrategy == null) {
            keepAliveStrategy = DefaultConnectionKeepAliveStrategy.INSTANCE;
        }
        String userAgent = this.userAgent;
        if (userAgent == null) {
            userAgent = VersionInfo.getUserAgent(
                    "Apache-HttpAsyncClient", "org.apache.http.nio.client", getClass());
        }
        final HttpProcessorBuilder b = HttpProcessorBuilder.create();
        b.addAll(
                new RequestContent(),
                new RequestTargetHost(),
                new RequestClientConnControl(),
                new RequestUserAgent(userAgent));
        if (!cookieManagementDisabled) {
            b.add(new RequestAddCookies());
            b.add(new ResponseProcessCookies());
        }
        final HttpProcessor httpprocessor = b.build();

        ThreadFactory threadFactory = null;
        NHttpClientEventHandler eventHandler = null;
        if (!this.connManagerShared) {
            threadFactory = this.threadFactory;
            if (threadFactory == null) {
                threadFactory = Executors.defaultThreadFactory();
            }
            eventHandler = new HttpAsyncRequestExecutor();
        }
        return new MinimalHttpAsyncClient(
                connManager,
                threadFactory,
                eventHandler,
                httpprocessor,
                reuseStrategy,
                keepAliveStrategy);
    }

}
