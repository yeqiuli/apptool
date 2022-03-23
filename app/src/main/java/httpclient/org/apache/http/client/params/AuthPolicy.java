package httpclient.org.apache.http.client.params;
@Deprecated
public final class AuthPolicy {

    private AuthPolicy() {
        super();
    }

    /**
     * The NTLM scheme is a proprietary Microsoft Windows Authentication
     * protocol (considered to be the most secure among currently supported
     * authentication schemes).
     */
    public static final String NTLM = "NTLM";

    /**
     * Digest authentication scheme as defined in RFC2617.
     */
    public static final String DIGEST = "Digest";

    /**
     * Basic authentication scheme as defined in RFC2617 (considered inherently
     * insecure, but most widely supported)
     */
    public static final String BASIC = "Basic";

    /**
     * SPNEGO Authentication scheme.
     *
     * @since 4.1
     */
    public static final String SPNEGO = "Negotiate";

    /**
     * Kerberos Authentication scheme.
     *
     * @since 4.2
     */
    public static final String KERBEROS = "Kerberos";

}
