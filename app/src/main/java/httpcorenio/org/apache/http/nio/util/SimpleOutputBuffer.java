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
package httpcorenio.org.apache.http.nio.util;

import java.io.IOException;

import httpcorenio.org.apache.http.nio.ContentEncoder;

/**
 * Basic implementation of the {@link ContentOutputBuffer} interface.
 * <p>
 * This class is not thread safe.
 *
 * @since 4.0
 */
public class SimpleOutputBuffer extends ExpandableBuffer implements ContentOutputBuffer {

    private boolean endOfStream;

    public SimpleOutputBuffer(final int bufferSize, final ByteBufferAllocator allocator) {
        super(bufferSize, allocator);
        this.endOfStream = false;
    }

    /**
     * @since 4.3
     */
    public SimpleOutputBuffer(final int bufferSize) {
        this(bufferSize, HeapByteBufferAllocator.INSTANCE);
    }

    @Override
    public int produceContent(final ContentEncoder encoder) throws IOException {
        setOutputMode();
        final int bytesWritten = encoder.write(this.buffer);
        if (!hasData() && this.endOfStream) {
            encoder.complete();
        }
        return bytesWritten;
    }

    @Override
    public void write(final byte[] b, final int off, final int len) throws IOException {
        if (b == null) {
            return;
        }
        if (this.endOfStream) {
            return;
        }
        setInputMode();
        ensureCapacity(this.buffer.position() + len);
        this.buffer.put(b, off, len);
    }

    public void write(final byte[] b) throws IOException {
        if (b == null) {
            return;
        }
        if (this.endOfStream) {
            return;
        }
        write(b, 0, b.length);
    }

    @Override
    public void write(final int b) throws IOException {
        if (this.endOfStream) {
            return;
        }
        setInputMode();
        ensureCapacity(this.capacity() + 1);
        this.buffer.put((byte)b);
    }

    @Override
    public void reset() {
        super.clear();
        this.endOfStream = false;
    }

    @Override
    public void flush() {
        // do nothing.
    }

    @Override
    public void writeCompleted() {
        this.endOfStream = true;
    }

    public void shutdown() {
        this.endOfStream = true;
    }

}
