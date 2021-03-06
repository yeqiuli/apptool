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

import java.io.IOException;

import httpcore.org.apache.http.HttpHost;
import httpcore.org.apache.http.annotation.Contract;
import httpcore.org.apache.http.annotation.ThreadingBehavior;
import httpcorenio.org.apache.http.nio.NHttpClientConnection;
import httpcore.org.apache.http.pool.PoolEntry;

/**
 * A basic {@link PoolEntry} implementation that represents an entry
 * in a pool of non-blocking {@link NHttpClientConnection}s identified by
 * an {@link HttpHost} instance.
 *
 * @see HttpHost
 * @since 4.2
 */
@Contract(threading = ThreadingBehavior.SAFE)
public class BasicNIOPoolEntry extends PoolEntry<HttpHost, NHttpClientConnection> {

    private volatile int socketTimeout;

    public BasicNIOPoolEntry(final String id, final HttpHost route, final NHttpClientConnection conn) {
        super(id, route, conn);
    }

    @Override
    public void close() {
        try {
            final NHttpClientConnection connection = getConnection();
            try {
                final int socketTimeout = connection.getSocketTimeout();
                if (socketTimeout <= 0 || socketTimeout > 1000) {
                    connection.setSocketTimeout(1000);
                }
                connection.close();
            } catch (final IOException ex) {
                connection.shutdown();
            }
        } catch (final IOException ignore) {
        }
    }

    @Override
    public boolean isClosed() {
        return !getConnection().isOpen();
    }

    int getSocketTimeout() {
        return socketTimeout;
    }

    void setSocketTimeout(final int socketTimeout) {
        this.socketTimeout = socketTimeout;
    }

}
