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
package httpasyncclient.org.apache.http.impl.nio.conn;

import httplog.org.apache.commons.logging.Log;

import java.nio.ByteBuffer;

class Wire {

    private final Log log;
    private final String id;

    public Wire(final Log log, final String id) {
        super();
        this.log = log;
        this.id = id;
    }

    private void wire(final String header, final byte[] b, final int pos, final int off) {
        final StringBuilder buffer = new StringBuilder();
        for (int i = 0; i < off; i++) {
            final int ch = b[pos + i];
            if (ch == 13) {
                buffer.append("[\\r]");
            } else if (ch == 10) {
                    buffer.append("[\\n]\"");
                    buffer.insert(0, "\"");
                    buffer.insert(0, header);
                    this.log.debug(this.id + " " + buffer.toString());
                    buffer.setLength(0);
            } else if ((ch < 32) || (ch > 127)) {
                buffer.append("[0x");
                buffer.append(Integer.toHexString(ch));
                buffer.append("]");
            } else {
                buffer.append((char) ch);
            }
        }
        if (buffer.length() > 0) {
            buffer.append('\"');
            buffer.insert(0, '\"');
            buffer.insert(0, header);
            this.log.debug(this.id + " " + buffer.toString());
        }
    }


    public boolean isEnabled() {
        return this.log.isDebugEnabled();
    }

    public void output(final byte[] b, final int pos, final int off) {
        wire(">> ", b, pos, off);
    }

    public void input(final byte[] b, final int pos, final int off) {
        wire("<< ", b, pos, off);
    }

    public void output(final byte[] b) {
        output(b, 0, b.length);
    }

    public void input(final byte[] b) {
        input(b, 0, b.length);
    }

    public void output(final int b) {
        output(new byte[] {(byte) b});
    }

    public void input(final int b) {
        input(new byte[] {(byte) b});
    }

    public void output(final ByteBuffer b) {
        if (b.hasArray()) {
            output(b.array(), b.arrayOffset() + b.position(), b.remaining());
        } else {
            final byte[] tmp = new byte[b.remaining()];
            b.get(tmp);
            output(tmp);
        }
    }

    public void input(final ByteBuffer b) {
        if (b.hasArray()) {
            input(b.array(), b.arrayOffset() + b.position(), b.remaining());
        } else {
            final byte[] tmp = new byte[b.remaining()];
            b.get(tmp);
            input(tmp);
        }
    }

}
