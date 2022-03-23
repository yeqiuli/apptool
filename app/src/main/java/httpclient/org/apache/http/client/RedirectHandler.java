package httpclient.org.apache.http.client;

import java.net.URI;

import httpcore.org.apache.http.HttpResponse;
import httpcore.org.apache.http.ProtocolException;
import httpcore.org.apache.http.protocol.HttpContext;

@Deprecated
public interface RedirectHandler {

    /**
     * Determines if a request should be redirected to a new location
     * given the response from the target server.
     *
     * @param response the response received from the target server
     * @param context the context for the request execution
     *
     * @return {@code true} if the request should be redirected, {@code false}
     * otherwise
     */
    boolean isRedirectRequested(HttpResponse response, HttpContext context);

    /**
     * Determines the location request is expected to be redirected to
     * given the response from the target server and the current request
     * execution context.
     *
     * @param response the response received from the target server
     * @param context the context for the request execution
     *
     * @return redirect URI
     */
    URI getLocationURI(HttpResponse response, HttpContext context)
            throws ProtocolException;

}
