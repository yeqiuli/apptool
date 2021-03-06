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

package httpcorenio.org.apache.http.impl.nio.codecs;

import httpcore.org.apache.http.HttpResponse;
import httpcore.org.apache.http.HttpResponseFactory;
import httpcore.org.apache.http.annotation.Contract;
import httpcore.org.apache.http.annotation.ThreadingBehavior;
import httpcore.org.apache.http.config.MessageConstraints;
import httpcore.org.apache.http.impl.DefaultHttpResponseFactory;
import httpcore.org.apache.http.message.BasicLineParser;
import httpcore.org.apache.http.message.LineParser;
import httpcorenio.org.apache.http.nio.NHttpMessageParser;
import httpcorenio.org.apache.http.nio.NHttpMessageParserFactory;
import httpcorenio.org.apache.http.nio.reactor.SessionInputBuffer;

/**
 * Default factory for response message parsers.
 *
 * @since 4.3
 */
@Contract(threading = ThreadingBehavior.IMMUTABLE_CONDITIONAL)
public class DefaultHttpResponseParserFactory implements NHttpMessageParserFactory<HttpResponse> {

    public static final DefaultHttpResponseParserFactory INSTANCE = new DefaultHttpResponseParserFactory();

    private final LineParser lineParser;
    private final HttpResponseFactory responseFactory;

    public DefaultHttpResponseParserFactory(final LineParser lineParser,
            final HttpResponseFactory responseFactory) {
        super();
        this.lineParser = lineParser != null ? lineParser : BasicLineParser.INSTANCE;
        this.responseFactory = responseFactory != null ? responseFactory
                : DefaultHttpResponseFactory.INSTANCE;
    }

    public DefaultHttpResponseParserFactory() {
        this(null, null);
    }

    @Override
    public NHttpMessageParser<HttpResponse> create(final SessionInputBuffer buffer,
            final MessageConstraints constraints) {
        return new DefaultHttpResponseParser(buffer, lineParser, responseFactory, constraints);
    }

}
