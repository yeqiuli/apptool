package httpcore.org.apache.http.impl.io;


import java.io.IOException;

import httpcore.org.apache.http.HttpRequest;
import httpcore.org.apache.http.io.SessionOutputBuffer;
import httpcore.org.apache.http.message.LineFormatter;
import httpcore.org.apache.http.params.HttpParams;

/**
 * HTTP request writer that serializes its output to an instance
 * of {@link SessionOutputBuffer}.
 *
 * @since 4.0
 *
 * @deprecated (4.3) use {@link DefaultHttpRequestWriter}
 */
@Deprecated
public class HttpRequestWriter extends AbstractMessageWriter<HttpRequest> {

    public HttpRequestWriter(final SessionOutputBuffer buffer,
                             final LineFormatter formatter,
                             final HttpParams params) {
        super(buffer, formatter, params);
    }

    @Override
    protected void writeHeadLine(final HttpRequest message) throws IOException {
        lineFormatter.formatRequestLine(this.lineBuf, message.getRequestLine());
        this.sessionBuffer.writeLine(this.lineBuf);
    }

}
