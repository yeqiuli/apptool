package httpclient.org.apache.http.impl.cookie;


import httpclient.org.apache.http.cookie.CookieSpec;
import httpclient.org.apache.http.cookie.CookieSpecFactory;
import httpclient.org.apache.http.cookie.CookieSpecProvider;
import httpcore.org.apache.http.annotation.Contract;
import httpcore.org.apache.http.annotation.ThreadingBehavior;
import httpcore.org.apache.http.params.HttpParams;
import httpcore.org.apache.http.protocol.HttpContext;

@Contract(threading = ThreadingBehavior.IMMUTABLE)
@Deprecated
public class IgnoreSpecFactory implements CookieSpecFactory, CookieSpecProvider {

    public IgnoreSpecFactory() {
        super();
    }

    @Override
    public CookieSpec newInstance(final HttpParams params) {
        return new IgnoreSpec();
    }

    @Override
    public CookieSpec create(final HttpContext context) {
        return new IgnoreSpec();
    }

}
