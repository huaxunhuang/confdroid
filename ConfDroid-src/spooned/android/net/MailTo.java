/**
 * Copyright (C) 2008 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package android.net;


/**
 * MailTo URL parser
 *
 * This class parses a mailto scheme URL and then can be queried for
 * the parsed parameters. This implements RFC 2368.
 */
public class MailTo {
    public static final java.lang.String MAILTO_SCHEME = "mailto:";

    // All the parsed content is added to the headers.
    private java.util.HashMap<java.lang.String, java.lang.String> mHeaders;

    // Well known headers
    private static final java.lang.String TO = "to";

    private static final java.lang.String BODY = "body";

    private static final java.lang.String CC = "cc";

    private static final java.lang.String SUBJECT = "subject";

    /**
     * Test to see if the given string is a mailto URL
     *
     * @param url
     * 		string to be tested
     * @return true if the string is a mailto URL
     */
    public static boolean isMailTo(java.lang.String url) {
        if ((url != null) && url.startsWith(android.net.MailTo.MAILTO_SCHEME)) {
            return true;
        }
        return false;
    }

    /**
     * Parse and decode a mailto scheme string. This parser implements
     * RFC 2368. The returned object can be queried for the parsed parameters.
     *
     * @param url
     * 		String containing a mailto URL
     * @return MailTo object
     * @exception ParseException
     * 		if the scheme is not a mailto URL
     */
    public static android.net.MailTo parse(java.lang.String url) throws android.net.ParseException {
        if (url == null) {
            throw new java.lang.NullPointerException();
        }
        if (!android.net.MailTo.isMailTo(url)) {
            throw new android.net.ParseException("Not a mailto scheme");
        }
        // Strip the scheme as the Uri parser can't cope with it.
        java.lang.String noScheme = url.substring(android.net.MailTo.MAILTO_SCHEME.length());
        android.net.Uri email = android.net.Uri.parse(noScheme);
        android.net.MailTo m = new android.net.MailTo();
        // Parse out the query parameters
        java.lang.String query = email.getQuery();
        if (query != null) {
            java.lang.String[] queries = query.split("&");
            for (java.lang.String q : queries) {
                java.lang.String[] nameval = q.split("=");
                if (nameval.length == 0) {
                    continue;
                }
                // insert the headers with the name in lowercase so that
                // we can easily find common headers
                m.mHeaders.put(android.net.Uri.decode(nameval[0]).toLowerCase(java.util.Locale.ROOT), nameval.length > 1 ? android.net.Uri.decode(nameval[1]) : null);
            }
        }
        // Address can be specified in both the headers and just after the
        // mailto line. Join the two together.
        java.lang.String address = email.getPath();
        if (address != null) {
            java.lang.String addr = m.getTo();
            if (addr != null) {
                address += ", " + addr;
            }
            m.mHeaders.put(android.net.MailTo.TO, address);
        }
        return m;
    }

    /**
     * Retrieve the To address line from the parsed mailto URL. This could be
     * several email address that are comma-space delimited.
     * If no To line was specified, then null is return
     *
     * @return comma delimited email addresses or null
     */
    public java.lang.String getTo() {
        return mHeaders.get(android.net.MailTo.TO);
    }

    /**
     * Retrieve the CC address line from the parsed mailto URL. This could be
     * several email address that are comma-space delimited.
     * If no CC line was specified, then null is return
     *
     * @return comma delimited email addresses or null
     */
    public java.lang.String getCc() {
        return mHeaders.get(android.net.MailTo.CC);
    }

    /**
     * Retrieve the subject line from the parsed mailto URL.
     * If no subject line was specified, then null is return
     *
     * @return subject or null
     */
    public java.lang.String getSubject() {
        return mHeaders.get(android.net.MailTo.SUBJECT);
    }

    /**
     * Retrieve the body line from the parsed mailto URL.
     * If no body line was specified, then null is return
     *
     * @return body or null
     */
    public java.lang.String getBody() {
        return mHeaders.get(android.net.MailTo.BODY);
    }

    /**
     * Retrieve all the parsed email headers from the mailto URL
     *
     * @return map containing all parsed values
     */
    public java.util.Map<java.lang.String, java.lang.String> getHeaders() {
        return mHeaders;
    }

    @java.lang.Override
    public java.lang.String toString() {
        java.lang.StringBuilder sb = new java.lang.StringBuilder(android.net.MailTo.MAILTO_SCHEME);
        sb.append('?');
        for (java.util.Map.Entry<java.lang.String, java.lang.String> header : mHeaders.entrySet()) {
            sb.append(android.net.Uri.encode(header.getKey()));
            sb.append('=');
            sb.append(android.net.Uri.encode(header.getValue()));
            sb.append('&');
        }
        return sb.toString();
    }

    /**
     * Private constructor. The only way to build a Mailto object is through
     * the parse() method.
     */
    private MailTo() {
        mHeaders = new java.util.HashMap<java.lang.String, java.lang.String>();
    }
}

