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

package httpclient.org.apache.http.impl.conn;

import httpcore.org.apache.http.HttpResponse;
import httpcore.org.apache.http.HttpResponseFactory;
import httpcore.org.apache.http.annotation.Immutable;
import httpcore.org.apache.http.config.MessageConstraints;
import httpcore.org.apache.http.impl.DefaultHttpResponseFactory;
import httpcore.org.apache.http.io.HttpMessageParser;
import httpcore.org.apache.http.io.HttpMessageParserFactory;
import httpcore.org.apache.http.io.SessionInputBuffer;
import httpcore.org.apache.http.message.BasicLineParser;
import httpcore.org.apache.http.message.LineParser;

/**
 * Default factory for response message parsers.
 *
 * @since 4.3
 */
@Immutable
public class DefaultHttpResponseParserFactory implements HttpMessageParserFactory<HttpResponse> {

    public static final DefaultHttpResponseParserFactory INSTANCE = new DefaultHttpResponseParserFactory();

    /**
     * http消息行解析器，可以处理requestLine headerLine  statusLine
     */
    private final LineParser lineParser;
    /**
     * 响应对象的创建工厂
     */
    private final HttpResponseFactory responseFactory;

    public DefaultHttpResponseParserFactory(
            final LineParser lineParser,
            final HttpResponseFactory responseFactory) {
        super();
        this.lineParser = lineParser != null ? lineParser : BasicLineParser.INSTANCE;
        this.responseFactory = responseFactory != null ? responseFactory
                : DefaultHttpResponseFactory.INSTANCE;
    }

    public DefaultHttpResponseParserFactory(
            final HttpResponseFactory responseFactory) {
        this(null, responseFactory);
    }

    public DefaultHttpResponseParserFactory() {
        this(null, null);
    }

    @Override
    public HttpMessageParser<HttpResponse> create(final SessionInputBuffer buffer,
                                                  final MessageConstraints constraints) {
        return new DefaultHttpResponseParser(buffer, lineParser, responseFactory, constraints);
    }

}
