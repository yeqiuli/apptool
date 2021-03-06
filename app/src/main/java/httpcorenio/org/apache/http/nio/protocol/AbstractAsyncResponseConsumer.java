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
import java.util.concurrent.atomic.AtomicBoolean;

import httpcore.org.apache.http.HttpEntity;
import httpcore.org.apache.http.HttpException;
import httpcore.org.apache.http.HttpResponse;
import httpcore.org.apache.http.entity.ContentType;
import httpcorenio.org.apache.http.nio.ContentDecoder;
import httpcorenio.org.apache.http.nio.IOControl;
import httpcore.org.apache.http.protocol.HttpContext;

/**
 * Abstract {@link HttpAsyncResponseConsumer} implementation that relieves its
 * subclasses from having to manage internal state and provides a number of protected
 * event methods that they need to implement.
 *
 * @since 4.2
 */
public abstract class AbstractAsyncResponseConsumer<T> implements HttpAsyncResponseConsumer<T> {

    private final AtomicBoolean completed;

    private volatile T result;
    private volatile Exception ex;

    public AbstractAsyncResponseConsumer() {
        super();
        this.completed = new AtomicBoolean(false);
    }

    /**
     * Invoked when a HTTP response message is received. Please note
     * that the {@link #onContentReceived(ContentDecoder, IOControl)} method
     * will be invoked only if the response messages has a content entity
     * enclosed.
     *
     * @param response HTTP response message.
     * @throws HttpException in case of HTTP protocol violation
     * @throws IOException in case of an I/O error
     */
    protected abstract void onResponseReceived(
            HttpResponse response) throws HttpException, IOException;

    /**
     * Invoked to process a chunk of content from the {@link ContentDecoder}.
     * The {@link IOControl} interface can be used to suspend input events
     * if the consumer is temporarily unable to consume more content.
     * <p>
     * The consumer can use the {@link ContentDecoder#isCompleted()} method
     * to find out whether or not the message content has been fully consumed.
     *
     * @param decoder content decoder.
     * @param ioControl I/O control of the underlying connection.
     * @throws IOException in case of an I/O error
     */
    protected abstract void onContentReceived(
            ContentDecoder decoder, IOControl ioControl) throws IOException;

    /**
     * Invoked if the response message encloses a content entity.
     *
     * @param entity HTTP entity
     * @param contentType expected content type.
     * @throws IOException in case of an I/O error
     */
    protected abstract void onEntityEnclosed(
            HttpEntity entity, ContentType contentType) throws IOException;

    /**
     * Invoked to generate a result object from the received HTTP response
     * message.
     *
     * @param context HTTP context.
     * @return result of the response processing.
     * @throws Exception in case of an abnormal termination.
     */
    protected abstract T buildResult(HttpContext context) throws Exception;

    /**
     * Invoked to release all system resources currently allocated.
     */
    protected abstract void releaseResources();

    /**
     * Invoked when the consumer is being closed.
     * @throws IOException may be thrown by subclassses
     *
     * @since 4.3
     */
    protected void onClose() throws IOException {
    }

    /**
     * @since 4.4
     */
    protected ContentType getContentType(final HttpEntity entity) {
        return entity != null ? ContentType.getOrDefault(entity) : null;
    }

    /**
     * Use {@link #onResponseReceived(HttpResponse)} instead.
     */
    @Override
    public final void responseReceived(
            final HttpResponse response) throws IOException, HttpException {
        onResponseReceived(response);
        final HttpEntity entity = response.getEntity();
        if (entity != null) {
            onEntityEnclosed(entity, getContentType(entity));
        }
    }

    /**
     * Use {@link #onContentReceived(ContentDecoder, IOControl)} instead.
     */
    @Override
    public final void consumeContent(
            final ContentDecoder decoder, final IOControl ioControl) throws IOException {
        onContentReceived(decoder, ioControl);
    }

    /**
     * Use {@link #buildResult(HttpContext)} instead.
     */
    @Override
    public final void responseCompleted(final HttpContext context) {
        if (this.completed.compareAndSet(false, true)) {
            try {
                this.result = buildResult(context);
            } catch (final Exception ex) {
                this.ex = ex;
            } finally {
                releaseResources();
            }
        }
    }

    @Override
    public final boolean cancel() {
        if (this.completed.compareAndSet(false, true)) {
            releaseResources();
            return true;
        }
        return false;
    }

    @Override
    public final void failed(final Exception ex) {
        if (this.completed.compareAndSet(false, true)) {
            this.ex = ex;
            releaseResources();
        }
    }

    @Override
    public final void close() throws IOException {
        if (this.completed.compareAndSet(false, true)) {
            releaseResources();
            onClose();
        }
    }

    @Override
    public Exception getException() {
        return this.ex;
    }

    @Override
    public T getResult() {
        return this.result;
    }

    @Override
    public boolean isDone() {
        return this.completed.get();
    }

}
