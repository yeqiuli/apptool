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
package httpasyncclient.org.apache.http.nio.client.methods;

import httpclient.org.apache.http.client.methods.*;
import httpclient.org.apache.http.client.utils.URIUtils;
import httpcore.org.apache.http.HttpEntityEnclosingRequest;
import httpcore.org.apache.http.HttpHost;
import httpcore.org.apache.http.HttpRequest;
import httpcore.org.apache.http.HttpResponse;
import httpcore.org.apache.http.entity.ContentType;
import httpcore.org.apache.http.util.Args;
import httpcorenio.org.apache.http.nio.entity.HttpAsyncContentProducer;
import httpcorenio.org.apache.http.nio.entity.NByteArrayEntity;
import httpcorenio.org.apache.http.nio.entity.NStringEntity;
import httpcorenio.org.apache.http.nio.protocol.BasicAsyncRequestProducer;
import httpcorenio.org.apache.http.nio.protocol.BasicAsyncResponseConsumer;
import httpcorenio.org.apache.http.nio.protocol.HttpAsyncRequestProducer;
import httpcorenio.org.apache.http.nio.protocol.HttpAsyncResponseConsumer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.net.URI;

/**
 * Factory methods for asynchronous request producers and response consumers.
 *
 * @since 4.0
 */
public final class HttpAsyncMethods {

    /**
     * Creates asynchronous request generator for the given request message.
     *
     * @param target  request target.
     * @param request request message.
     * @return asynchronous request generator
     */
    public static HttpAsyncRequestProducer create(final HttpHost target, final HttpRequest request) {
        Args.notNull(target, "HTTP host");
        Args.notNull(request, "HTTP request");
        return new RequestProducerImpl(target, request);
    }

    /**
     * Creates asynchronous request generator for the given request message.
     *
     * @param request request message.
     * @return asynchronous request generator
     */
    public static HttpAsyncRequestProducer create(final HttpUriRequest request) {
        Args.notNull(request, "HTTP request");
        final HttpHost target = URIUtils.extractHost(request.getURI());
        return new RequestProducerImpl(target, request);
    }

    /**
     * Creates asynchronous {@code GET} request generator.
     *
     * @param requestURI request URI.
     * @return asynchronous request generator
     */
    public static HttpAsyncRequestProducer createGet(final URI requestURI) {
        return create(new HttpGet(requestURI));
    }

    /**
     * Creates asynchronous {@code GET} request generator.
     *
     * @param requestURI request URI.
     * @return asynchronous request generator
     */
    public static HttpAsyncRequestProducer createGet(final String requestURI) {
        return create(new HttpGet(URI.create(requestURI)));
    }

    /**
     * Creates asynchronous {@code HEAD} request generator.
     *
     * @param requestURI request URI.
     * @return asynchronous request generator
     */
    public static HttpAsyncRequestProducer createHead(final URI requestURI) {
        return create(new HttpHead(requestURI));
    }

    /**
     * Creates asynchronous {@code HEAD} request generator.
     *
     * @param requestURI request URI.
     * @return asynchronous request generator
     */
    public static HttpAsyncRequestProducer createHead(final String requestURI) {
        return create(new HttpHead(URI.create(requestURI)));
    }

    /**
     * Creates asynchronous {@code DELETE} request generator.
     *
     * @param requestURI request URI.
     * @return asynchronous request generator
     */
    public static HttpAsyncRequestProducer createDelete(final URI requestURI) {
        return create(new HttpDelete(requestURI));
    }

    /**
     * Creates asynchronous {@code DELETE} request generator.
     *
     * @param requestURI request URI.
     * @return asynchronous request generator
     */
    public static HttpAsyncRequestProducer createDelete(final String requestURI) {
        return create(new HttpDelete(URI.create(requestURI)));
    }

    /**
     * Creates asynchronous {@code OPTIONS} request generator.
     *
     * @param requestURI request URI.
     * @return asynchronous request generator
     */
    public static HttpAsyncRequestProducer createOptions(final URI requestURI) {
        return create(new HttpOptions(requestURI));
    }

    /**
     * Creates asynchronous {@code OPTIONS} request generator.
     *
     * @param requestURI request URI.
     * @return asynchronous request generator
     */
    public static HttpAsyncRequestProducer createOptions(final String requestURI) {
        return create(new HttpOptions(URI.create(requestURI)));
    }

    /**
     * Creates asynchronous {@code TRACE} request generator.
     *
     * @param requestURI request URI.
     * @return asynchronous request generator
     */
    public static HttpAsyncRequestProducer createTrace(final URI requestURI) {
        return create(new HttpTrace(requestURI));
    }

    /**
     * Creates asynchronous {@code TRACE} request generator.
     *
     * @param requestURI request URI.
     * @return asynchronous request generator
     */
    public static HttpAsyncRequestProducer createTrace(final String requestURI) {
        return create(new HttpTrace(URI.create(requestURI)));
    }

    /**
     * Creates asynchronous {@code POST} request generator.
     *
     * @param requestURI  request URI.
     * @param content     request content.
     * @param contentType request contentType.
     * @return asynchronous request generator
     */
    public static HttpAsyncRequestProducer createPost(
            final URI requestURI,
            final String content,
            final ContentType contentType) throws UnsupportedEncodingException {
        final HttpPost httppost = new HttpPost(requestURI);
        final NStringEntity entity = new NStringEntity(content, contentType);
        httppost.setEntity(entity);
        final HttpHost target = URIUtils.extractHost(requestURI);
        return new RequestProducerImpl(target, httppost, entity);
    }

    /**
     * Creates asynchronous {@code POST} request generator.
     *
     * @param requestURI  request URI.
     * @param content     request content.
     * @param contentType request contentType.
     * @return asynchronous request generator
     */
    public static HttpAsyncRequestProducer createPost(
            final String requestURI,
            final String content,
            final ContentType contentType) throws UnsupportedEncodingException {
        return createPost(URI.create(requestURI), content, contentType);
    }




    /**
     * Creates asynchronous {@code PUT} request generator.
     *
     * @param requestURI  request URI.
     * @param content     request content.
     * @param contentType request contentType.
     * @return asynchronous request generator
     */
    public static HttpAsyncRequestProducer createPut(
            final URI requestURI,
            final String content,
            final ContentType contentType) throws UnsupportedEncodingException {
        final HttpPut httpput = new HttpPut(requestURI);
        final NStringEntity entity = new NStringEntity(content, contentType);
        httpput.setEntity(entity);
        final HttpHost target = URIUtils.extractHost(requestURI);
        return new RequestProducerImpl(target, httpput, entity);
    }

    /**
     * Creates asynchronous {@code PUT} request generator.
     *
     * @param requestURI  request URI.
     * @param content     request content.
     * @param contentType request contentType.
     * @return asynchronous request generator
     */
    public static HttpAsyncRequestProducer createPut(
            final String requestURI,
            final String content,
            final ContentType contentType) throws UnsupportedEncodingException {
        return createPut(URI.create(requestURI), content, contentType);
    }

    /**
     * Creates asynchronous {@code PUT} request generator.
     *
     * @param requestURI  request URI.
     * @param content     request content.
     * @param contentType request contentType.
     * @return asynchronous request generator
     */
    public static HttpAsyncRequestProducer createPut(
            final URI requestURI,
            final byte[] content,
            final ContentType contentType) {
        final HttpPut httpput = new HttpPut(requestURI);
        final NByteArrayEntity entity = new NByteArrayEntity(content, contentType);
        httpput.setEntity(entity);
        final HttpHost target = URIUtils.extractHost(requestURI);
        return new RequestProducerImpl(target, httpput, entity);
    }

    /**
     * Creates asynchronous {@code PUT} request generator.
     *
     * @param requestURI  request URI.
     * @param content     request content.
     * @param contentType request contentType.
     * @return asynchronous request generator
     */
    public static HttpAsyncRequestProducer createPut(
            final String requestURI,
            final byte[] content,
            final ContentType contentType) {
        return createPut(URI.create(requestURI), content, contentType);
    }

    /**
     * Creates asynchronous zero-copy {@code POST} request generator.
     *
     * @param requestURI  request URI.
     * @param content     request content.
     * @param contentType request contentType.
     * @return asynchronous request generator
     */
    public static HttpAsyncRequestProducer createZeroCopyPost(
            final URI requestURI,
            final File content,
            final ContentType contentType) throws FileNotFoundException {
        return new ZeroCopyPost(requestURI, content, contentType);
    }

    /**
     * Creates asynchronous zero-copy {@code POST} request generator.
     *
     * @param requestURI  request URI.
     * @param content     request content.
     * @param contentType request contentType.
     * @return asynchronous request generator
     */
    public static HttpAsyncRequestProducer createZeroCopyPost(
            final String requestURI,
            final File content,
            final ContentType contentType) throws FileNotFoundException {
        return new ZeroCopyPost(URI.create(requestURI), content, contentType);
    }

    /**
     * Creates asynchronous zero-copy {@code PUT} request generator.
     *
     * @param requestURI  request URI.
     * @param content     request content.
     * @param contentType request contentType.
     * @return asynchronous request generator
     */
    public static HttpAsyncRequestProducer createZeroCopyPut(
            final URI requestURI,
            final File content,
            final ContentType contentType) throws FileNotFoundException {
        return new ZeroCopyPut(requestURI, content, contentType);
    }

    /**
     * Creates asynchronous zero-copy {@code PUT} request generator.
     *
     * @param requestURI  request URI.
     * @param content     request content.
     * @param contentType request contentType.
     * @return asynchronous request generator
     */
    public static HttpAsyncRequestProducer createZeroCopyPut(
            final String requestURI,
            final File content,
            final ContentType contentType) throws FileNotFoundException {
        return new ZeroCopyPut(URI.create(requestURI), content, contentType);
    }

    /**
     * Creates basic response consumer that will buffer response content in memory.
     *
     * @return asynchronous response consumer.
     */
    public static HttpAsyncResponseConsumer<HttpResponse> createConsumer() {
        return new BasicAsyncResponseConsumer();
    }

    /**
     * Creates zero-copy response consumer that will stream response content
     * directly to the given file.
     *
     * @return asynchronous response consumer.
     */
    public static HttpAsyncResponseConsumer<HttpResponse> createZeroCopyConsumer(
            final File file) throws FileNotFoundException {
        return new ZeroCopyConsumer<HttpResponse>(file) {

            @Override
            protected HttpResponse process(
                    final HttpResponse response,
                    final File file,
                    final ContentType contentType) {
                return response;
            }

        };
    }

    static class RequestProducerImpl extends BasicAsyncRequestProducer {

        protected RequestProducerImpl(
                final HttpHost target,
                final HttpEntityEnclosingRequest request,
                final HttpAsyncContentProducer producer) {
            super(target, request, producer);
        }

        public RequestProducerImpl(final HttpHost target, final HttpRequest request) {
            super(target, request);
        }

    }

}
