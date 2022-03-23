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

package httpclient.org.apache.http.impl.cookie;

import httpclient.org.apache.http.cookie.CookieSpec;
import httpclient.org.apache.http.cookie.CookieSpecProvider;
import httpcore.org.apache.http.annotation.Immutable;
import httpcore.org.apache.http.protocol.HttpContext;

/**
 * {@link CookieSpecProvider} implementation that ignores all cookies.
 *
 * @since 4.4
 */
@Immutable
public class IgnoreSpecProvider implements CookieSpecProvider {

    private volatile CookieSpec cookieSpec;

    public IgnoreSpecProvider() {
        super();
    }

    @Override
    public CookieSpec create(final HttpContext context) {
        if (cookieSpec == null) {
            synchronized (this) {
                if (cookieSpec == null) {
                    this.cookieSpec = new IgnoreSpec();
                }
            }
        }
        return this.cookieSpec;
    }

}
