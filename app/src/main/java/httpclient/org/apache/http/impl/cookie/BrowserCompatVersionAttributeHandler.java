package httpclient.org.apache.http.impl.cookie;

import httpclient.org.apache.http.cookie.ClientCookie;
import httpclient.org.apache.http.cookie.CommonCookieAttributeHandler;
import httpclient.org.apache.http.cookie.MalformedCookieException;
import httpclient.org.apache.http.cookie.SetCookie;
import httpcore.org.apache.http.annotation.Contract;
import httpcore.org.apache.http.annotation.ThreadingBehavior;
import httpcore.org.apache.http.util.Args;

@Deprecated
@Contract(threading = ThreadingBehavior.IMMUTABLE)
public class BrowserCompatVersionAttributeHandler extends
        AbstractCookieAttributeHandler implements CommonCookieAttributeHandler {

    public BrowserCompatVersionAttributeHandler() {
        super();
    }

    /**
     * Parse cookie version attribute.
     */
    @Override
    public void parse(final SetCookie cookie, final String value)
            throws MalformedCookieException {
        Args.notNull(cookie, "Cookie");
        if (value == null) {
            throw new MalformedCookieException("Missing value for version attribute");
        }
        int version = 0;
        try {
            version = Integer.parseInt(value);
        } catch (final NumberFormatException e) {
            // Just ignore invalid versions
        }
        cookie.setVersion(version);
    }

    @Override
    public String getAttributeName() {
        return ClientCookie.VERSION_ATTR;
    }

}
