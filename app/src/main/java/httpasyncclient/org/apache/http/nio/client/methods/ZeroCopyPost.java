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

import httpclient.org.apache.http.client.methods.HttpPost;
import httpcore.org.apache.http.HttpEntity;
import httpcore.org.apache.http.HttpEntityEnclosingRequest;
import httpcore.org.apache.http.entity.ContentType;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URI;

/**
 * {@link org.apache.http.nio.protocol.HttpAsyncRequestProducer} implementation
 * that generates an HTTP {@code POST} request enclosing content of a file.
 * The request content will be streamed out directly from the underlying file
 * without an intermediate in-memory buffer.
 *
 * @since 4.0
 */
public class ZeroCopyPost extends BaseZeroCopyRequestProducer {

    public ZeroCopyPost(
            final URI requestURI,
            final File content,
            final ContentType contentType) throws FileNotFoundException {
        super(requestURI, content, contentType);
    }

    public ZeroCopyPost(
            final String requestURI,
            final File content,
            final ContentType contentType) throws FileNotFoundException {
        super(URI.create(requestURI), content, contentType);
    }

    @Override
    protected HttpEntityEnclosingRequest createRequest(final URI requestURI, final HttpEntity entity) {
        final HttpPost httppost = new HttpPost(requestURI);
        httppost.setEntity(entity);
        return httppost;
    }

}
