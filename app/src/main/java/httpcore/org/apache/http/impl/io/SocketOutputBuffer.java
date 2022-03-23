package httpcore.org.apache.http.impl.io;


import java.io.IOException;
import java.net.Socket;

import httpcore.org.apache.http.params.HttpParams;
import httpcore.org.apache.http.util.Args;

/**
 * bound to a {@link Socket}.
 *
 * @since 4.0
 *
 * @deprecated (4.3) use {@link SessionOutputBufferImpl}
 */
@Deprecated
public class SocketOutputBuffer extends AbstractSessionOutputBuffer {

    /**
     * Creates an instance of this class.
     *
     * @param socket the socket to write data to.
     * @param bufferSize the size of the internal buffer. If this number is less
     *   than {@code 0} it is set to the value of
     *   {@link Socket#getSendBufferSize()}. If resultant number is less
     *   than {@code 1024} it is set to {@code 1024}.
     * @param params HTTP parameters.
     */
    public SocketOutputBuffer(
            final Socket socket,
            final int bufferSize,
            final HttpParams params) throws IOException {
        super();
        Args.notNull(socket, "Socket");
        int n = bufferSize;
        if (n < 0) {
            n = socket.getSendBufferSize();
        }
        if (n < 1024) {
            n = 1024;
        }
        init(socket.getOutputStream(), n, params);
    }

}
