package httpclient.org.apache.http.client;

import java.io.IOException;

import httpcore.org.apache.http.HttpException;
import httpcore.org.apache.http.HttpHost;
import httpcore.org.apache.http.HttpRequest;
import httpcore.org.apache.http.HttpResponse;
import httpcore.org.apache.http.protocol.HttpContext;

@Deprecated
public interface RequestDirector {


    /**
     * Executes a request.
     * <p>
     * <b>Note:</b> For the time being, a new director is instantiated for each request.
     * This is the same behavior as for {@code HttpMethodDirector}
     * in HttpClient 3.
     * </p>
     *
     * @param target    the target host for the request.
     *                  Implementations may accept {@code null}
     *                  if they can still determine a route, for example
     *                  to a default target or by inspecting the request.
     * @param request   the request to execute
     * @param context   the context for executing the request
     *
     * @return  the final response to the request.
     *          This is never an intermediate response with status code 1xx.
     *
     * @throws HttpException            in case of a problem
     * @throws IOException              in case of an IO problem
     *                                     or if the connection was aborted
     */
    HttpResponse execute(HttpHost target, HttpRequest request, HttpContext context)
            throws HttpException, IOException;

}
