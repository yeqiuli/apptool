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

package httpcorenio.org.apache.http.nio.entity;

import java.io.Closeable;
import java.io.IOException;

import httpcorenio.org.apache.http.nio.ContentEncoder;
import httpcorenio.org.apache.http.nio.IOControl;

/**
 * {@code HttpAsyncContentProducer} is a callback interface whose methods
 * get invoked to stream out message content to a non-blocking HTTP connection.
 *
 * @since 4.2
 */
public interface HttpAsyncContentProducer extends Closeable {

    /**
     * Invoked to write out a chunk of content to the {@link ContentEncoder}.
     * The {@link IOControl} interface can be used to suspend output event
     * notifications if the entity is temporarily unable to produce more content.
     * <p>
     * When all content is finished, the producer <b>MUST</b> call
     * {@link ContentEncoder#complete()}. Failure to do so may cause the entity
     * to be incorrectly delimited.
     * <p>
     * Please note that the {@link ContentEncoder} object is not thread-safe and
     * should only be used within the context of this method call.
     * The {@link IOControl} object can be shared and used on other thread
     * to resume output event notifications when more content is made available.
     *
     * @param encoder   content encoder.
     * @param ioControl I/O control of the underlying connection.
     */
    void produceContent(ContentEncoder encoder, IOControl ioControl) throws IOException;

    /**
     * Determines whether or not this producer is capable of producing
     * its content more than once. Repeatable content producers are expected
     * to be able to recreate their content even after having been closed.
     */
    boolean isRepeatable();

}
