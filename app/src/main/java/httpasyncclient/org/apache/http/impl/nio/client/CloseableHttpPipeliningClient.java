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

import httpasyncclient.org.apache.http.nio.client.HttpPipeliningClient;
import httpasyncclient.org.apache.http.nio.client.methods.HttpAsyncMethods;
import httpclient.org.apache.http.client.protocol.HttpClientContext;
import httpcore.org.apache.http.HttpHost;
import httpcore.org.apache.http.HttpRequest;
import httpcore.org.apache.http.HttpResponse;
import httpcore.org.apache.http.annotation.Contract;
import httpcore.org.apache.http.annotation.ThreadingBehavior;
import httpcore.org.apache.http.concurrent.FutureCallback;
import httpcore.org.apache.http.protocol.HttpContext;
import httpcore.org.apache.http.util.Args;
import httpcorenio.org.apache.http.nio.protocol.HttpAsyncRequestProducer;
import httpcorenio.org.apache.http.nio.protocol.HttpAsyncResponseConsumer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

/**
 * Base implementation of {@link HttpPipeliningClient} that also
 * implements {@link java.io.Closeable}.
 *
 * @since 4.1
 */
@Contract(threading = ThreadingBehavior.SAFE)
public abstract class CloseableHttpPipeliningClient
        extends CloseableHttpAsyncClient implements HttpPipeliningClient {

    @Override
    public <T> Future<List<T>> execute(
            final HttpHost target,
            final List<? extends HttpAsyncRequestProducer> requestProducers,
            final List<? extends HttpAsyncResponseConsumer<T>> responseConsumers,
            final FutureCallback<List<T>> callback) {
        return execute(target, requestProducers, responseConsumers, HttpClientContext.create(), callback);
    }

    @Override
    public Future<List<HttpResponse>> execute(
            final HttpHost target,
            final List<HttpRequest> requests,
            final FutureCallback<List<HttpResponse>> callback) {
        return execute(target, requests, HttpClientContext.create(), callback);
    }

    @Override
    public Future<List<HttpResponse>> execute(
            final HttpHost target,
            final List<HttpRequest> requests,
            final HttpContext context,
            final FutureCallback<List<HttpResponse>> callback) {
        Args.notEmpty(requests, "HTTP request list");
        final List<HttpAsyncRequestProducer> requestProducers = new ArrayList<HttpAsyncRequestProducer>(
                requests.size());
        final List<HttpAsyncResponseConsumer<HttpResponse>> responseConsumers = new ArrayList<HttpAsyncResponseConsumer<HttpResponse>>(
                requests.size());
        for (int i = 0; i < requests.size(); i++) {
            final HttpRequest request = requests.get(i);
            requestProducers.add(HttpAsyncMethods.create(target, request));
            responseConsumers.add(HttpAsyncMethods.createConsumer());
        }
        return execute(target, requestProducers, responseConsumers, context, callback);
    }

}
