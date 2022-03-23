package httpclient.org.apache.http.impl.cookie;

import httpcore.org.apache.http.annotation.Contract;
import httpcore.org.apache.http.annotation.ThreadingBehavior;

@Contract(threading = ThreadingBehavior.SAFE)
@Deprecated
public class BestMatchSpec extends DefaultCookieSpec {

    public BestMatchSpec(final String[] datepatterns, final boolean oneHeader) {
        super(datepatterns, oneHeader);
    }

    public BestMatchSpec() {
        this(null, false);
    }

    @Override
    public String toString() {
        return "best-match";
    }

}
