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

package httpcorenio.org.apache.http.nio.protocol;

import java.io.IOException;

import httpcore.org.apache.http.HttpEntity;
import httpcore.org.apache.http.HttpResponse;
import httpcore.org.apache.http.protocol.HTTP;
import httpcorenio.org.apache.http.nio.ContentEncoder;
import httpcorenio.org.apache.http.nio.IOControl;
import httpcorenio.org.apache.http.nio.entity.EntityAsyncContentProducer;
import httpcorenio.org.apache.http.nio.entity.HttpAsyncContentProducer;
import httpcore.org.apache.http.protocol.HttpContext;

/**
 * This class changed from package private to public in version 4.4.8.
 *
 * @since 4.4.8
 */
public class ErrorResponseProducer implements HttpAsyncResponseProducer {

    private final HttpResponse response;
    private final HttpEntity entity;
    private final HttpAsyncContentProducer contentProducer;
    private final boolean keepAlive;

    public ErrorResponseProducer(
            final HttpResponse response,
            final HttpEntity entity,
            final boolean keepAlive) {
        super();
        this.response = response;
        this.entity = entity;
        if (entity instanceof HttpAsyncContentProducer) {
            this.contentProducer = (HttpAsyncContentProducer) entity;
        } else {
            this.contentProducer = new EntityAsyncContentProducer(entity);
        }
        this.keepAlive = keepAlive;
    }

    @Override
    public HttpResponse generateResponse() {
        if (this.keepAlive) {
            response.addHeader(HTTP.CONN_DIRECTIVE, HTTP.CONN_KEEP_ALIVE);
        } else {
            response.addHeader(HTTP.CONN_DIRECTIVE, HTTP.CONN_CLOSE);
        }
        response.setEntity(this.entity);
        return response;
    }

    @Override
    public void produceContent(
            final ContentEncoder encoder, final IOControl ioControl) throws IOException {
        this.contentProducer.produceContent(encoder, ioControl);
    }

    @Override
    public void responseCompleted(final HttpContext context) {
    }

    @Override
    public void failed(final Exception ex) {
    }

    @Override
    public void close() throws IOException {
        this.contentProducer.close();
    }

}
