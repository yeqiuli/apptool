package httpclient.org.apache.http.cookie;

import httpcore.org.apache.http.params.HttpParams;

@Deprecated
public interface CookieSpecFactory {

    /**
     * Creates an instance of {@link CookieSpec} using given HTTP parameters.
     *
     * @param params HTTP parameters.
     *
     * @return cookie spec.
     */
    CookieSpec newInstance(HttpParams params);

}
