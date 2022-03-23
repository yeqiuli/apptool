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
package httpcorenio.org.apache.http.impl.nio.pool;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import httpcore.org.apache.http.HttpHost;
import httpcore.org.apache.http.annotation.ThreadingBehavior;
import httpcore.org.apache.http.annotation.Contract;
import httpcore.org.apache.http.concurrent.FutureCallback;
import httpcore.org.apache.http.config.ConnectionConfig;
import httpcorenio.org.apache.http.nio.NHttpClientConnection;
import httpcorenio.org.apache.http.nio.pool.AbstractNIOConnPool;
import httpcorenio.org.apache.http.nio.pool.NIOConnFactory;
import httpcorenio.org.apache.http.nio.pool.SocketAddressResolver;
import httpcorenio.org.apache.http.nio.reactor.ConnectingIOReactor;
import httpcore.org.apache.http.params.CoreConnectionPNames;
import httpcore.org.apache.http.params.HttpParams;
import httpcore.org.apache.http.util.Args;

/**
 * represents a pool of non-blocking {@link NHttpClientConnection} connections
 * identified by an {@link HttpHost} instance. Please note this pool
 * implementation does not support complex routes via a proxy cannot
 * differentiate between direct and proxied connections.
 *
 * @see HttpHost
 * @since 4.2
 */
@SuppressWarnings("deprecation")
@Contract(threading = ThreadingBehavior.SAFE)
public class BasicNIOConnPool extends AbstractNIOConnPool<HttpHost, NHttpClientConnection, BasicNIOPoolEntry> {

    private static final AtomicLong COUNTER = new AtomicLong();

    private final int connectTimeout;

    static class BasicAddressResolver implements SocketAddressResolver<HttpHost> {

        @Override
        public SocketAddress resolveLocalAddress(final HttpHost host) {
            return null;
        }

        @Override
        public SocketAddress resolveRemoteAddress(final HttpHost host) {
            final String hostname = host.getHostName();
            int port = host.getPort();
            if (port == -1) {
                if (host.getSchemeName().equalsIgnoreCase("http")) {
                    port = 80;
                } else if (host.getSchemeName().equalsIgnoreCase("https")) {
                    port = 443;
                }
            }
            return new InetSocketAddress(hostname, port);
        }

    }

    /**
     * @deprecated (4.3) use {@link BasicNIOConnPool#BasicNIOConnPool(ConnectingIOReactor, NIOConnFactory, int)}
     */
    @Deprecated
    public BasicNIOConnPool(
            final ConnectingIOReactor ioReactor,
            final NIOConnFactory<HttpHost, NHttpClientConnection> connFactory,
            final HttpParams params) {
        super(ioReactor, connFactory, 2, 20);
        Args.notNull(params, "HTTP parameters");
        this.connectTimeout = params.getIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 0);
    }

    /**
     * @deprecated (4.3) use {@link BasicNIOConnPool#BasicNIOConnPool(ConnectingIOReactor,
     * ConnectionConfig)}
     */
    @Deprecated
    public BasicNIOConnPool(
            final ConnectingIOReactor ioReactor, final HttpParams params) {
        this(ioReactor, new BasicNIOConnFactory(params), params);
    }

    /**
     * @since 4.3
     */
    public BasicNIOConnPool(
            final ConnectingIOReactor ioReactor,
            final NIOConnFactory<HttpHost, NHttpClientConnection> connFactory,
            final int connectTimeout) {
        super(ioReactor, connFactory, new BasicAddressResolver(), 2, 20);
        this.connectTimeout = connectTimeout;
    }

    /**
     * @since 4.3
     */
    public BasicNIOConnPool(
            final ConnectingIOReactor ioReactor,
            final int connectTimeout,
            final ConnectionConfig config) {
        this(ioReactor, new BasicNIOConnFactory(config), connectTimeout);
    }

    /**
     * @since 4.3
     */
    public BasicNIOConnPool(
            final ConnectingIOReactor ioReactor,
            final ConnectionConfig config) {
        this(ioReactor, new BasicNIOConnFactory(config), 0);
    }

    /**
     * @since 4.3
     */
    public BasicNIOConnPool(final ConnectingIOReactor ioReactor) {
        this(ioReactor, new BasicNIOConnFactory(ConnectionConfig.DEFAULT), 0);
    }

    /**
     * @deprecated (4.3) use {@link SocketAddressResolver}
     */
    @Deprecated
    @Override
    protected SocketAddress resolveRemoteAddress(final HttpHost host) {
        return new InetSocketAddress(host.getHostName(), host.getPort());
    }

    /**
     * @deprecated (4.3) use {@link SocketAddressResolver}
     */
    @Deprecated
    @Override
    protected SocketAddress resolveLocalAddress(final HttpHost host) {
        return null;
    }

    @Override
    protected BasicNIOPoolEntry createEntry(final HttpHost host, final NHttpClientConnection conn) {
        final BasicNIOPoolEntry entry = new BasicNIOPoolEntry(
                Long.toString(COUNTER.getAndIncrement()), host, conn);
        entry.setSocketTimeout(conn.getSocketTimeout());
        return entry;
    }

    @Override
    public Future<BasicNIOPoolEntry> lease(
            final HttpHost route,
            final Object state,
            final FutureCallback<BasicNIOPoolEntry> callback) {
        return super.lease(route, state,
                this.connectTimeout, TimeUnit.MILLISECONDS, callback);
    }

    @Override
    public Future<BasicNIOPoolEntry> lease(
            final HttpHost route,
            final Object state) {
        return super.lease(route, state,
                this.connectTimeout, TimeUnit.MILLISECONDS, null);
    }

    @Override
    protected void onLease(final BasicNIOPoolEntry entry) {
        final NHttpClientConnection conn = entry.getConnection();
        conn.setSocketTimeout(entry.getSocketTimeout());
    }

    @Override
    protected void onRelease(final BasicNIOPoolEntry entry) {
        final NHttpClientConnection conn = entry.getConnection();
        entry.setSocketTimeout(conn.getSocketTimeout());
        conn.setSocketTimeout(0);
    }

}
