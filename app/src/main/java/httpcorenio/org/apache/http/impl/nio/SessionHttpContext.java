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

package httpcorenio.org.apache.http.impl.nio;


import httpcore.org.apache.http.protocol.HttpContext;
import httpcorenio.org.apache.http.nio.reactor.IOSession;

class SessionHttpContext implements HttpContext {

    private final IOSession ioSession;

    public SessionHttpContext(final IOSession ioSession) {
        super();
        this.ioSession = ioSession;
    }

    @Override
    public Object getAttribute(final String id) {
        return this.ioSession.getAttribute(id);
    }

    @Override
    public Object removeAttribute(final String id) {
        return this.ioSession.removeAttribute(id);
    }

    @Override
    public void setAttribute(final String id, final Object obj) {
        this.ioSession.setAttribute(id, obj);
    }

    /**
     * @since 4.4.7
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("[ioSession=");
        sb.append(ioSession);
        sb.append("]");
        return sb.toString();
    }

}
