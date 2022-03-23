package httpclient.org.apache.http.impl.cookie;


import java.util.Collection;

import httpclient.org.apache.http.cookie.CookieSpec;
import httpclient.org.apache.http.cookie.CookieSpecFactory;
import httpclient.org.apache.http.cookie.CookieSpecProvider;
import httpclient.org.apache.http.cookie.params.CookieSpecPNames;
import httpcore.org.apache.http.annotation.Contract;
import httpcore.org.apache.http.annotation.ThreadingBehavior;
import httpcore.org.apache.http.params.HttpParams;
import httpcore.org.apache.http.protocol.HttpContext;

@Contract(threading = ThreadingBehavior.IMMUTABLE)
@Deprecated
public class BrowserCompatSpecFactory implements CookieSpecFactory, CookieSpecProvider {

    public enum SecurityLevel {
        SECURITYLEVEL_DEFAULT,
        SECURITYLEVEL_IE_MEDIUM
    }

    private final SecurityLevel securityLevel;
    private final CookieSpec cookieSpec;

    public BrowserCompatSpecFactory(final String[] datepatterns, final SecurityLevel securityLevel) {
        super();
        this.securityLevel = securityLevel;
        this.cookieSpec = new BrowserCompatSpec(datepatterns, securityLevel);
    }

    public BrowserCompatSpecFactory(final String[] datepatterns) {
        this(null, SecurityLevel.SECURITYLEVEL_DEFAULT);
    }

    public BrowserCompatSpecFactory() {
        this(null, SecurityLevel.SECURITYLEVEL_DEFAULT);
    }

    @Override
    public CookieSpec newInstance(final HttpParams params) {
        if (params != null) {

            String[] patterns = null;
            final Collection<?> param = (Collection<?>) params.getParameter(
                    CookieSpecPNames.DATE_PATTERNS);
            if (param != null) {
                patterns = new String[param.size()];
                patterns = param.toArray(patterns);
            }
            return new BrowserCompatSpec(patterns, securityLevel);
        } else {
            return new BrowserCompatSpec(null, securityLevel);
        }
    }

    @Override
    public CookieSpec create(final HttpContext context) {
        return this.cookieSpec;
    }

}
