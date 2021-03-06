/*
 * ====================================================================
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

package httpclient.org.apache.http.impl.cookie;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import httpclient.org.apache.http.client.utils.DateUtils;
import httpclient.org.apache.http.cookie.ClientCookie;
import httpclient.org.apache.http.cookie.CommonCookieAttributeHandler;
import httpclient.org.apache.http.cookie.Cookie;
import httpclient.org.apache.http.cookie.CookieOrigin;
import httpclient.org.apache.http.cookie.CookiePathComparator;
import httpclient.org.apache.http.cookie.CookieRestrictionViolationException;
import httpclient.org.apache.http.cookie.MalformedCookieException;
import httpclient.org.apache.http.cookie.SM;
import httpcore.org.apache.http.Header;
import httpcore.org.apache.http.HeaderElement;
import httpcore.org.apache.http.annotation.Obsolete;
import httpcore.org.apache.http.annotation.ThreadSafe;
import httpcore.org.apache.http.message.BufferedHeader;
import httpcore.org.apache.http.util.Args;
import httpcore.org.apache.http.util.CharArrayBuffer;

/**
 */
@Obsolete
@ThreadSafe
public class RFC2109Spec extends CookieSpecBase {

    final static String[] DATE_PATTERNS = {
        DateUtils.PATTERN_RFC1123,
        DateUtils.PATTERN_RFC1036,
        DateUtils.PATTERN_ASCTIME
    };

    private final boolean oneHeader;

    /** Default constructor */
    public RFC2109Spec(final String[] datepatterns, final boolean oneHeader) {
        super(new RFC2109VersionHandler(),
                new BasicPathHandler(),
                new RFC2109DomainHandler(),
                new BasicMaxAgeHandler(),
                new BasicSecureHandler(),
                new BasicCommentHandler(),
                new BasicExpiresHandler(
                        datepatterns != null ? datepatterns.clone() : DATE_PATTERNS));
        this.oneHeader = oneHeader;
    }

    /** Default constructor */
    public RFC2109Spec() {
        this(null, false);
    }

    protected RFC2109Spec(final boolean oneHeader,
                          final CommonCookieAttributeHandler... handlers) {
        super(handlers);
        this.oneHeader = oneHeader;
    }

    @Override
    public List<Cookie> parse(final Header header, final CookieOrigin origin)
            throws MalformedCookieException {
        Args.notNull(header, "Header");
        Args.notNull(origin, "Cookie origin");
        if (!header.getName().equalsIgnoreCase(SM.SET_COOKIE)) {
            throw new MalformedCookieException("Unrecognized cookie header '"
                    + header.toString() + "'");
        }
        final HeaderElement[] elems = header.getElements();
        return parse(elems, origin);
    }

    @Override
    public void validate(final Cookie cookie, final CookieOrigin origin)
            throws MalformedCookieException {
        Args.notNull(cookie, "Cookie");
        final String name = cookie.getName();
        if (name.indexOf(' ') != -1) {
            throw new CookieRestrictionViolationException("Cookie name may not contain blanks");
        }
        if (name.startsWith("$")) {
            throw new CookieRestrictionViolationException("Cookie name may not start with $");
        }
        super.validate(cookie, origin);
    }

    @Override
    public List<Header> formatCookies(final List<Cookie> cookies) {
        Args.notEmpty(cookies, "List of cookies");
        List<Cookie> cookieList;
        if (cookies.size() > 1) {
            // Create a mutable copy and sort the copy.
            cookieList = new ArrayList<Cookie>(cookies);
            Collections.sort(cookieList, CookiePathComparator.INSTANCE);
        } else {
            cookieList = cookies;
        }
        if (this.oneHeader) {
            return doFormatOneHeader(cookieList);
        } else {
            return doFormatManyHeaders(cookieList);
        }
    }

    private List<Header> doFormatOneHeader(final List<Cookie> cookies) {
        int version = Integer.MAX_VALUE;
        // Pick the lowest common denominator
        for (final Cookie cookie : cookies) {
            if (cookie.getVersion() < version) {
                version = cookie.getVersion();
            }
        }
        final CharArrayBuffer buffer = new CharArrayBuffer(40 * cookies.size());
        buffer.append(SM.COOKIE);
        buffer.append(": ");
        buffer.append("$Version=");
        buffer.append(Integer.toString(version));
        for (final Cookie cooky : cookies) {
            buffer.append("; ");
            final Cookie cookie = cooky;
            formatCookieAsVer(buffer, cookie, version);
        }
        final List<Header> headers = new ArrayList<Header>(1);
        headers.add(new BufferedHeader(buffer));
        return headers;
    }

    private List<Header> doFormatManyHeaders(final List<Cookie> cookies) {
        final List<Header> headers = new ArrayList<Header>(cookies.size());
        for (final Cookie cookie : cookies) {
            final int version = cookie.getVersion();
            final CharArrayBuffer buffer = new CharArrayBuffer(40);
            buffer.append("Cookie: ");
            buffer.append("$Version=");
            buffer.append(Integer.toString(version));
            buffer.append("; ");
            formatCookieAsVer(buffer, cookie, version);
            headers.add(new BufferedHeader(buffer));
        }
        return headers;
    }

    /**
     * Return a name/value string suitable for sending in a {@code "Cookie"}
     * header as defined in RFC 2109 for backward compatibility with cookie
     * version 0
     * @param buffer The char array buffer to use for output
     * @param name The cookie name
     * @param value The cookie value
     * @param version The cookie version
     */
    protected void formatParamAsVer(final CharArrayBuffer buffer,
            final String name, final String value, final int version) {
        buffer.append(name);
        buffer.append("=");
        if (value != null) {
            if (version > 0) {
                buffer.append('\"');
                buffer.append(value);
                buffer.append('\"');
            } else {
                buffer.append(value);
            }
        }
    }

    /**
     * Return a string suitable for sending in a {@code "Cookie"} header
     * as defined in RFC 2109 for backward compatibility with cookie version 0
     * @param buffer The char array buffer to use for output
     * @param cookie The {@link Cookie} to be formatted as string
     * @param version The version to use.
     */
    protected void formatCookieAsVer(final CharArrayBuffer buffer,
            final Cookie cookie, final int version) {
        formatParamAsVer(buffer, cookie.getName(), cookie.getValue(), version);
        if (cookie.getPath() != null) {
            if (cookie instanceof ClientCookie
                    && ((ClientCookie) cookie).containsAttribute(ClientCookie.PATH_ATTR)) {
                buffer.append("; ");
                formatParamAsVer(buffer, "$Path", cookie.getPath(), version);
            }
        }
        if (cookie.getDomain() != null) {
            if (cookie instanceof ClientCookie
                    && ((ClientCookie) cookie).containsAttribute(ClientCookie.DOMAIN_ATTR)) {
                buffer.append("; ");
                formatParamAsVer(buffer, "$Domain", cookie.getDomain(), version);
            }
        }
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public Header getVersionHeader() {
        return null;
    }

    @Override
    public String toString() {
        return "rfc2109";
    }

}
