/**
 * Copyright (C) 2007 The Android Open Source Project
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
 * Immutable URI reference. A URI reference includes a URI and a fragment, the
 * component of the URI following a '#'. Builds and parses URI references
 * which conform to
 * <a href="http://www.faqs.org/rfcs/rfc2396.html">RFC 2396</a>.
 *
 * <p>In the interest of performance, this class performs little to no
 * validation. Behavior is undefined for invalid input. This class is very
 * forgiving--in the face of invalid input, it will return garbage
 * rather than throw an exception unless otherwise specified.
 */
public abstract class Uri implements android.os.Parcelable , java.lang.Comparable<android.net.Uri> {
    /* This class aims to do as little up front work as possible. To accomplish
    that, we vary the implementation depending on what the user passes in.
    For example, we have one implementation if the user passes in a
    URI string (StringUri) and another if the user passes in the
    individual components (OpaqueUri).

    Concurrency notes*: Like any truly immutable object, this class is safe
    for concurrent use. This class uses a caching pattern in some places where
    it doesn't use volatile or synchronized. This is safe to do with ints
    because getting or setting an int is atomic. It's safe to do with a String
    because the internal fields are final and the memory model guarantees other
    threads won't see a partially initialized instance. We are not guaranteed
    that some threads will immediately see changes from other threads on
    certain platforms, but we don't mind if those threads reconstruct the
    cached result. As a result, we get thread safe caching with no concurrency
    overhead, which means the most common case, access from a single thread,
    is as fast as possible.

    From the Java Language spec.:

    "17.5 Final Field Semantics

    ... when the object is seen by another thread, that thread will always
    see the correctly constructed version of that object's final fields.
    It will also see versions of any object or array referenced by
    those final fields that are at least as up-to-date as the final fields
    are."

    In that same vein, all non-transient fields within Uri
    implementations should be final and immutable so as to ensure true
    immutability for clients even when they don't use proper concurrency
    control.

    For reference, from RFC 2396:

    "4.3. Parsing a URI Reference

    A URI reference is typically parsed according to the four main
    components and fragment identifier in order to determine what
    components are present and whether the reference is relative or
    absolute.  The individual components are then parsed for their
    subparts and, if not opaque, to verify their validity.

    Although the BNF defines what is allowed in each component, it is
    ambiguous in terms of differentiating between an authority component
    and a path component that begins with two slash characters.  The
    greedy algorithm is used for disambiguation: the left-most matching
    rule soaks up as much of the URI reference string as it is capable of
    matching.  In other words, the authority component wins."

    The "four main components" of a hierarchical URI consist of
    <scheme>://<authority><path>?<query>
     */
    /**
     * Log tag.
     */
    private static final java.lang.String LOG = android.net.Uri.class.getSimpleName();

    /**
     * NOTE: EMPTY accesses this field during its own initialization, so this
     * field *must* be initialized first, or else EMPTY will see a null value!
     *
     * Placeholder for strings which haven't been cached. This enables us
     * to cache null. We intentionally create a new String instance so we can
     * compare its identity and there is no chance we will confuse it with
     * user data.
     */
    @java.lang.SuppressWarnings("RedundantStringConstructorCall")
    private static final java.lang.String NOT_CACHED = new java.lang.String("NOT CACHED");

    /**
     * The empty URI, equivalent to "".
     */
    public static final android.net.Uri EMPTY = new android.net.Uri.HierarchicalUri(null, android.net.Uri.Part.NULL, android.net.Uri.PathPart.EMPTY, android.net.Uri.Part.NULL, android.net.Uri.Part.NULL);

    /**
     * Prevents external subclassing.
     */
    private Uri() {
    }

    /**
     * Returns true if this URI is hierarchical like "http://google.com".
     * Absolute URIs are hierarchical if the scheme-specific part starts with
     * a '/'. Relative URIs are always hierarchical.
     */
    public abstract boolean isHierarchical();

    /**
     * Returns true if this URI is opaque like "mailto:nobody@google.com". The
     * scheme-specific part of an opaque URI cannot start with a '/'.
     */
    public boolean isOpaque() {
        return !isHierarchical();
    }

    /**
     * Returns true if this URI is relative, i.e.&nbsp;if it doesn't contain an
     * explicit scheme.
     *
     * @return true if this URI is relative, false if it's absolute
     */
    public abstract boolean isRelative();

    /**
     * Returns true if this URI is absolute, i.e.&nbsp;if it contains an
     * explicit scheme.
     *
     * @return true if this URI is absolute, false if it's relative
     */
    public boolean isAbsolute() {
        return !isRelative();
    }

    /**
     * Gets the scheme of this URI. Example: "http"
     *
     * @return the scheme or null if this is a relative URI
     */
    public abstract java.lang.String getScheme();

    /**
     * Gets the scheme-specific part of this URI, i.e.&nbsp;everything between
     * the scheme separator ':' and the fragment separator '#'. If this is a
     * relative URI, this method returns the entire URI. Decodes escaped octets.
     *
     * <p>Example: "//www.google.com/search?q=android"
     *
     * @return the decoded scheme-specific-part
     */
    public abstract java.lang.String getSchemeSpecificPart();

    /**
     * Gets the scheme-specific part of this URI, i.e.&nbsp;everything between
     * the scheme separator ':' and the fragment separator '#'. If this is a
     * relative URI, this method returns the entire URI. Leaves escaped octets
     * intact.
     *
     * <p>Example: "//www.google.com/search?q=android"
     *
     * @return the decoded scheme-specific-part
     */
    public abstract java.lang.String getEncodedSchemeSpecificPart();

    /**
     * Gets the decoded authority part of this URI. For
     * server addresses, the authority is structured as follows:
     * {@code [ userinfo '@' ] host [ ':' port ]}
     *
     * <p>Examples: "google.com", "bob@google.com:80"
     *
     * @return the authority for this URI or null if not present
     */
    public abstract java.lang.String getAuthority();

    /**
     * Gets the encoded authority part of this URI. For
     * server addresses, the authority is structured as follows:
     * {@code [ userinfo '@' ] host [ ':' port ]}
     *
     * <p>Examples: "google.com", "bob@google.com:80"
     *
     * @return the authority for this URI or null if not present
     */
    public abstract java.lang.String getEncodedAuthority();

    /**
     * Gets the decoded user information from the authority.
     * For example, if the authority is "nobody@google.com", this method will
     * return "nobody".
     *
     * @return the user info for this URI or null if not present
     */
    public abstract java.lang.String getUserInfo();

    /**
     * Gets the encoded user information from the authority.
     * For example, if the authority is "nobody@google.com", this method will
     * return "nobody".
     *
     * @return the user info for this URI or null if not present
     */
    public abstract java.lang.String getEncodedUserInfo();

    /**
     * Gets the encoded host from the authority for this URI. For example,
     * if the authority is "bob@google.com", this method will return
     * "google.com".
     *
     * @return the host for this URI or null if not present
     */
    public abstract java.lang.String getHost();

    /**
     * Gets the port from the authority for this URI. For example,
     * if the authority is "google.com:80", this method will return 80.
     *
     * @return the port for this URI or -1 if invalid or not present
     */
    public abstract int getPort();

    /**
     * Gets the decoded path.
     *
     * @return the decoded path, or null if this is not a hierarchical URI
    (like "mailto:nobody@google.com") or the URI is invalid
     */
    public abstract java.lang.String getPath();

    /**
     * Gets the encoded path.
     *
     * @return the encoded path, or null if this is not a hierarchical URI
    (like "mailto:nobody@google.com") or the URI is invalid
     */
    public abstract java.lang.String getEncodedPath();

    /**
     * Gets the decoded query component from this URI. The query comes after
     * the query separator ('?') and before the fragment separator ('#'). This
     * method would return "q=android" for
     * "http://www.google.com/search?q=android".
     *
     * @return the decoded query or null if there isn't one
     */
    public abstract java.lang.String getQuery();

    /**
     * Gets the encoded query component from this URI. The query comes after
     * the query separator ('?') and before the fragment separator ('#'). This
     * method would return "q=android" for
     * "http://www.google.com/search?q=android".
     *
     * @return the encoded query or null if there isn't one
     */
    public abstract java.lang.String getEncodedQuery();

    /**
     * Gets the decoded fragment part of this URI, everything after the '#'.
     *
     * @return the decoded fragment or null if there isn't one
     */
    public abstract java.lang.String getFragment();

    /**
     * Gets the encoded fragment part of this URI, everything after the '#'.
     *
     * @return the encoded fragment or null if there isn't one
     */
    public abstract java.lang.String getEncodedFragment();

    /**
     * Gets the decoded path segments.
     *
     * @return decoded path segments, each without a leading or trailing '/'
     */
    public abstract java.util.List<java.lang.String> getPathSegments();

    /**
     * Gets the decoded last segment in the path.
     *
     * @return the decoded last segment or null if the path is empty
     */
    public abstract java.lang.String getLastPathSegment();

    /**
     * Compares this Uri to another object for equality. Returns true if the
     * encoded string representations of this Uri and the given Uri are
     * equal. Case counts. Paths are not normalized. If one Uri specifies a
     * default port explicitly and the other leaves it implicit, they will not
     * be considered equal.
     */
    public boolean equals(java.lang.Object o) {
        if (!(o instanceof android.net.Uri)) {
            return false;
        }
        android.net.Uri other = ((android.net.Uri) (o));
        return toString().equals(other.toString());
    }

    /**
     * Hashes the encoded string represention of this Uri consistently with
     * {@link #equals(Object)}.
     */
    public int hashCode() {
        return toString().hashCode();
    }

    /**
     * Compares the string representation of this Uri with that of
     * another.
     */
    public int compareTo(android.net.Uri other) {
        return toString().compareTo(other.toString());
    }

    /**
     * Returns the encoded string representation of this URI.
     * Example: "http://google.com/"
     */
    public abstract java.lang.String toString();

    /**
     * Return a string representation of the URI that is safe to print
     * to logs and other places where PII should be avoided.
     *
     * @unknown 
     */
    public java.lang.String toSafeString() {
        java.lang.String scheme = getScheme();
        java.lang.String ssp = getSchemeSpecificPart();
        if (scheme != null) {
            if ((((scheme.equalsIgnoreCase("tel") || scheme.equalsIgnoreCase("sip")) || scheme.equalsIgnoreCase("sms")) || scheme.equalsIgnoreCase("smsto")) || scheme.equalsIgnoreCase("mailto")) {
                java.lang.StringBuilder builder = new java.lang.StringBuilder(64);
                builder.append(scheme);
                builder.append(':');
                if (ssp != null) {
                    for (int i = 0; i < ssp.length(); i++) {
                        char c = ssp.charAt(i);
                        if (((c == '-') || (c == '@')) || (c == '.')) {
                            builder.append(c);
                        } else {
                            builder.append('x');
                        }
                    }
                }
                return builder.toString();
            } else
                if ((scheme.equalsIgnoreCase("http") || scheme.equalsIgnoreCase("https")) || scheme.equalsIgnoreCase("ftp")) {
                    ssp = (("//" + (getHost() != null ? getHost() : "")) + (getPort() != (-1) ? ":" + getPort() : "")) + "/...";
                }

        }
        // Not a sensitive scheme, but let's still be conservative about
        // the data we include -- only the ssp, not the query params or
        // fragment, because those can often have sensitive info.
        java.lang.StringBuilder builder = new java.lang.StringBuilder(64);
        if (scheme != null) {
            builder.append(scheme);
            builder.append(':');
        }
        if (ssp != null) {
            builder.append(ssp);
        }
        return builder.toString();
    }

    /**
     * Constructs a new builder, copying the attributes from this Uri.
     */
    public abstract android.net.Uri.Builder buildUpon();

    /**
     * Index of a component which was not found.
     */
    private static final int NOT_FOUND = -1;

    /**
     * Placeholder value for an index which hasn't been calculated yet.
     */
    private static final int NOT_CALCULATED = -2;

    /**
     * Error message presented when a user tries to treat an opaque URI as
     * hierarchical.
     */
    private static final java.lang.String NOT_HIERARCHICAL = "This isn't a hierarchical URI.";

    /**
     * Default encoding.
     */
    private static final java.lang.String DEFAULT_ENCODING = "UTF-8";

    /**
     * Creates a Uri which parses the given encoded URI string.
     *
     * @param uriString
     * 		an RFC 2396-compliant, encoded URI
     * @throws NullPointerException
     * 		if uriString is null
     * @return Uri for this given uri string
     */
    public static android.net.Uri parse(java.lang.String uriString) {
        return new android.net.Uri.StringUri(uriString);
    }

    /**
     * Creates a Uri from a file. The URI has the form
     * "file://<absolute path>". Encodes path characters with the exception of
     * '/'.
     *
     * <p>Example: "file:///tmp/android.txt"
     *
     * @throws NullPointerException
     * 		if file is null
     * @return a Uri for the given file
     */
    public static android.net.Uri fromFile(java.io.File file) {
        if (file == null) {
            throw new java.lang.NullPointerException("file");
        }
        android.net.Uri.PathPart path = android.net.Uri.PathPart.fromDecoded(file.getAbsolutePath());
        return new android.net.Uri.HierarchicalUri("file", android.net.Uri.Part.EMPTY, path, android.net.Uri.Part.NULL, android.net.Uri.Part.NULL);
    }

    /**
     * An implementation which wraps a String URI. This URI can be opaque or
     * hierarchical, but we extend AbstractHierarchicalUri in case we need
     * the hierarchical functionality.
     */
    private static class StringUri extends android.net.Uri.AbstractHierarchicalUri {
        /**
         * Used in parcelling.
         */
        static final int TYPE_ID = 1;

        /**
         * URI string representation.
         */
        private final java.lang.String uriString;

        private StringUri(java.lang.String uriString) {
            if (uriString == null) {
                throw new java.lang.NullPointerException("uriString");
            }
            this.uriString = uriString;
        }

        static android.net.Uri readFrom(android.os.Parcel parcel) {
            return new android.net.Uri.StringUri(parcel.readString());
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(android.os.Parcel parcel, int flags) {
            parcel.writeInt(android.net.Uri.StringUri.TYPE_ID);
            parcel.writeString(uriString);
        }

        /**
         * Cached scheme separator index.
         */
        private volatile int cachedSsi = android.net.Uri.NOT_CALCULATED;

        /**
         * Finds the first ':'. Returns -1 if none found.
         */
        private int findSchemeSeparator() {
            return cachedSsi == android.net.Uri.NOT_CALCULATED ? cachedSsi = uriString.indexOf(':') : cachedSsi;
        }

        /**
         * Cached fragment separator index.
         */
        private volatile int cachedFsi = android.net.Uri.NOT_CALCULATED;

        /**
         * Finds the first '#'. Returns -1 if none found.
         */
        private int findFragmentSeparator() {
            return cachedFsi == android.net.Uri.NOT_CALCULATED ? cachedFsi = uriString.indexOf('#', findSchemeSeparator()) : cachedFsi;
        }

        public boolean isHierarchical() {
            int ssi = findSchemeSeparator();
            if (ssi == android.net.Uri.NOT_FOUND) {
                // All relative URIs are hierarchical.
                return true;
            }
            if (uriString.length() == (ssi + 1)) {
                // No ssp.
                return false;
            }
            // If the ssp starts with a '/', this is hierarchical.
            return uriString.charAt(ssi + 1) == '/';
        }

        public boolean isRelative() {
            // Note: We return true if the index is 0
            return findSchemeSeparator() == android.net.Uri.NOT_FOUND;
        }

        private volatile java.lang.String scheme = android.net.Uri.NOT_CACHED;

        public java.lang.String getScheme() {
            @java.lang.SuppressWarnings("StringEquality")
            boolean cached = scheme != android.net.Uri.NOT_CACHED;
            return cached ? scheme : (scheme = parseScheme());
        }

        private java.lang.String parseScheme() {
            int ssi = findSchemeSeparator();
            return ssi == android.net.Uri.NOT_FOUND ? null : uriString.substring(0, ssi);
        }

        private android.net.Uri.Part ssp;

        private android.net.Uri.Part getSsp() {
            return ssp == null ? ssp = android.net.Uri.Part.fromEncoded(parseSsp()) : ssp;
        }

        public java.lang.String getEncodedSchemeSpecificPart() {
            return getSsp().getEncoded();
        }

        public java.lang.String getSchemeSpecificPart() {
            return getSsp().getDecoded();
        }

        private java.lang.String parseSsp() {
            int ssi = findSchemeSeparator();
            int fsi = findFragmentSeparator();
            // Return everything between ssi and fsi.
            return fsi == android.net.Uri.NOT_FOUND ? uriString.substring(ssi + 1) : uriString.substring(ssi + 1, fsi);
        }

        private android.net.Uri.Part authority;

        private android.net.Uri.Part getAuthorityPart() {
            if (authority == null) {
                java.lang.String encodedAuthority = android.net.Uri.StringUri.parseAuthority(this.uriString, findSchemeSeparator());
                return authority = android.net.Uri.Part.fromEncoded(encodedAuthority);
            }
            return authority;
        }

        public java.lang.String getEncodedAuthority() {
            return getAuthorityPart().getEncoded();
        }

        public java.lang.String getAuthority() {
            return getAuthorityPart().getDecoded();
        }

        private android.net.Uri.PathPart path;

        private android.net.Uri.PathPart getPathPart() {
            return path == null ? path = android.net.Uri.PathPart.fromEncoded(parsePath()) : path;
        }

        public java.lang.String getPath() {
            return getPathPart().getDecoded();
        }

        public java.lang.String getEncodedPath() {
            return getPathPart().getEncoded();
        }

        public java.util.List<java.lang.String> getPathSegments() {
            return getPathPart().getPathSegments();
        }

        private java.lang.String parsePath() {
            java.lang.String uriString = this.uriString;
            int ssi = findSchemeSeparator();
            // If the URI is absolute.
            if (ssi > (-1)) {
                // Is there anything after the ':'?
                boolean schemeOnly = (ssi + 1) == uriString.length();
                if (schemeOnly) {
                    // Opaque URI.
                    return null;
                }
                // A '/' after the ':' means this is hierarchical.
                if (uriString.charAt(ssi + 1) != '/') {
                    // Opaque URI.
                    return null;
                }
            } else {
                // All relative URIs are hierarchical.
            }
            return android.net.Uri.StringUri.parsePath(uriString, ssi);
        }

        private android.net.Uri.Part query;

        private android.net.Uri.Part getQueryPart() {
            return query == null ? query = android.net.Uri.Part.fromEncoded(parseQuery()) : query;
        }

        public java.lang.String getEncodedQuery() {
            return getQueryPart().getEncoded();
        }

        private java.lang.String parseQuery() {
            // It doesn't make sense to cache this index. We only ever
            // calculate it once.
            int qsi = uriString.indexOf('?', findSchemeSeparator());
            if (qsi == android.net.Uri.NOT_FOUND) {
                return null;
            }
            int fsi = findFragmentSeparator();
            if (fsi == android.net.Uri.NOT_FOUND) {
                return uriString.substring(qsi + 1);
            }
            if (fsi < qsi) {
                // Invalid.
                return null;
            }
            return uriString.substring(qsi + 1, fsi);
        }

        public java.lang.String getQuery() {
            return getQueryPart().getDecoded();
        }

        private android.net.Uri.Part fragment;

        private android.net.Uri.Part getFragmentPart() {
            return fragment == null ? fragment = android.net.Uri.Part.fromEncoded(parseFragment()) : fragment;
        }

        public java.lang.String getEncodedFragment() {
            return getFragmentPart().getEncoded();
        }

        private java.lang.String parseFragment() {
            int fsi = findFragmentSeparator();
            return fsi == android.net.Uri.NOT_FOUND ? null : uriString.substring(fsi + 1);
        }

        public java.lang.String getFragment() {
            return getFragmentPart().getDecoded();
        }

        public java.lang.String toString() {
            return uriString;
        }

        /**
         * Parses an authority out of the given URI string.
         *
         * @param uriString
         * 		URI string
         * @param ssi
         * 		scheme separator index, -1 for a relative URI
         * @return the authority or null if none is found
         */
        static java.lang.String parseAuthority(java.lang.String uriString, int ssi) {
            int length = uriString.length();
            // If "//" follows the scheme separator, we have an authority.
            if (((length > (ssi + 2)) && (uriString.charAt(ssi + 1) == '/')) && (uriString.charAt(ssi + 2) == '/')) {
                // We have an authority.
                // Look for the start of the path, query, or fragment, or the
                // end of the string.
                int end = ssi + 3;
                LOOP : while (end < length) {
                    switch (uriString.charAt(end)) {
                        case '/' :
                            // Start of path
                        case '?' :
                            // Start of query
                        case '#' :
                            // Start of fragment
                            break LOOP;
                    }
                    end++;
                } 
                return uriString.substring(ssi + 3, end);
            } else {
                return null;
            }
        }

        /**
         * Parses a path out of this given URI string.
         *
         * @param uriString
         * 		URI string
         * @param ssi
         * 		scheme separator index, -1 for a relative URI
         * @return the path
         */
        static java.lang.String parsePath(java.lang.String uriString, int ssi) {
            int length = uriString.length();
            // Find start of path.
            int pathStart;
            if (((length > (ssi + 2)) && (uriString.charAt(ssi + 1) == '/')) && (uriString.charAt(ssi + 2) == '/')) {
                // Skip over authority to path.
                pathStart = ssi + 3;
                LOOP : while (pathStart < length) {
                    switch (uriString.charAt(pathStart)) {
                        case '?' :
                            // Start of query
                        case '#' :
                            // Start of fragment
                            return "";// Empty path.

                        case '/' :
                            // Start of path!
                            break LOOP;
                    }
                    pathStart++;
                } 
            } else {
                // Path starts immediately after scheme separator.
                pathStart = ssi + 1;
            }
            // Find end of path.
            int pathEnd = pathStart;
            LOOP : while (pathEnd < length) {
                switch (uriString.charAt(pathEnd)) {
                    case '?' :
                        // Start of query
                    case '#' :
                        // Start of fragment
                        break LOOP;
                }
                pathEnd++;
            } 
            return uriString.substring(pathStart, pathEnd);
        }

        public android.net.Uri.Builder buildUpon() {
            if (isHierarchical()) {
                return new android.net.Uri.Builder().scheme(getScheme()).authority(getAuthorityPart()).path(getPathPart()).query(getQueryPart()).fragment(getFragmentPart());
            } else {
                return new android.net.Uri.Builder().scheme(getScheme()).opaquePart(getSsp()).fragment(getFragmentPart());
            }
        }
    }

    /**
     * Creates an opaque Uri from the given components. Encodes the ssp
     * which means this method cannot be used to create hierarchical URIs.
     *
     * @param scheme
     * 		of the URI
     * @param ssp
     * 		scheme-specific-part, everything between the
     * 		scheme separator (':') and the fragment separator ('#'), which will
     * 		get encoded
     * @param fragment
     * 		fragment, everything after the '#', null if undefined,
     * 		will get encoded
     * @throws NullPointerException
     * 		if scheme or ssp is null
     * @return Uri composed of the given scheme, ssp, and fragment
     * @see Builder if you don't want the ssp and fragment to be encoded
     */
    public static android.net.Uri fromParts(java.lang.String scheme, java.lang.String ssp, java.lang.String fragment) {
        if (scheme == null) {
            throw new java.lang.NullPointerException("scheme");
        }
        if (ssp == null) {
            throw new java.lang.NullPointerException("ssp");
        }
        return new android.net.Uri.OpaqueUri(scheme, android.net.Uri.Part.fromDecoded(ssp), android.net.Uri.Part.fromDecoded(fragment));
    }

    /**
     * Opaque URI.
     */
    private static class OpaqueUri extends android.net.Uri {
        /**
         * Used in parcelling.
         */
        static final int TYPE_ID = 2;

        private final java.lang.String scheme;

        private final android.net.Uri.Part ssp;

        private final android.net.Uri.Part fragment;

        private OpaqueUri(java.lang.String scheme, android.net.Uri.Part ssp, android.net.Uri.Part fragment) {
            this.scheme = scheme;
            this.ssp = ssp;
            this.fragment = (fragment == null) ? android.net.Uri.Part.NULL : fragment;
        }

        static android.net.Uri readFrom(android.os.Parcel parcel) {
            return new android.net.Uri.OpaqueUri(parcel.readString(), android.net.Uri.Part.readFrom(parcel), android.net.Uri.Part.readFrom(parcel));
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(android.os.Parcel parcel, int flags) {
            parcel.writeInt(android.net.Uri.OpaqueUri.TYPE_ID);
            parcel.writeString(scheme);
            ssp.writeTo(parcel);
            fragment.writeTo(parcel);
        }

        public boolean isHierarchical() {
            return false;
        }

        public boolean isRelative() {
            return scheme == null;
        }

        public java.lang.String getScheme() {
            return this.scheme;
        }

        public java.lang.String getEncodedSchemeSpecificPart() {
            return ssp.getEncoded();
        }

        public java.lang.String getSchemeSpecificPart() {
            return ssp.getDecoded();
        }

        public java.lang.String getAuthority() {
            return null;
        }

        public java.lang.String getEncodedAuthority() {
            return null;
        }

        public java.lang.String getPath() {
            return null;
        }

        public java.lang.String getEncodedPath() {
            return null;
        }

        public java.lang.String getQuery() {
            return null;
        }

        public java.lang.String getEncodedQuery() {
            return null;
        }

        public java.lang.String getFragment() {
            return fragment.getDecoded();
        }

        public java.lang.String getEncodedFragment() {
            return fragment.getEncoded();
        }

        public java.util.List<java.lang.String> getPathSegments() {
            return java.util.Collections.emptyList();
        }

        public java.lang.String getLastPathSegment() {
            return null;
        }

        public java.lang.String getUserInfo() {
            return null;
        }

        public java.lang.String getEncodedUserInfo() {
            return null;
        }

        public java.lang.String getHost() {
            return null;
        }

        public int getPort() {
            return -1;
        }

        private volatile java.lang.String cachedString = android.net.Uri.NOT_CACHED;

        public java.lang.String toString() {
            @java.lang.SuppressWarnings("StringEquality")
            boolean cached = cachedString != android.net.Uri.NOT_CACHED;
            if (cached) {
                return cachedString;
            }
            java.lang.StringBuilder sb = new java.lang.StringBuilder();
            sb.append(scheme).append(':');
            sb.append(getEncodedSchemeSpecificPart());
            if (!fragment.isEmpty()) {
                sb.append('#').append(fragment.getEncoded());
            }
            return cachedString = sb.toString();
        }

        public android.net.Uri.Builder buildUpon() {
            return new android.net.Uri.Builder().scheme(this.scheme).opaquePart(this.ssp).fragment(this.fragment);
        }
    }

    /**
     * Wrapper for path segment array.
     */
    static class PathSegments extends java.util.AbstractList<java.lang.String> implements java.util.RandomAccess {
        static final android.net.Uri.PathSegments EMPTY = new android.net.Uri.PathSegments(null, 0);

        final java.lang.String[] segments;

        final int size;

        PathSegments(java.lang.String[] segments, int size) {
            this.segments = segments;
            this.size = size;
        }

        public java.lang.String get(int index) {
            if (index >= size) {
                throw new java.lang.IndexOutOfBoundsException();
            }
            return segments[index];
        }

        public int size() {
            return this.size;
        }
    }

    /**
     * Builds PathSegments.
     */
    static class PathSegmentsBuilder {
        java.lang.String[] segments;

        int size = 0;

        void add(java.lang.String segment) {
            if (segments == null) {
                segments = new java.lang.String[4];
            } else
                if ((size + 1) == segments.length) {
                    java.lang.String[] expanded = new java.lang.String[segments.length * 2];
                    java.lang.System.arraycopy(segments, 0, expanded, 0, segments.length);
                    segments = expanded;
                }

            segments[size++] = segment;
        }

        android.net.Uri.PathSegments build() {
            if (segments == null) {
                return android.net.Uri.PathSegments.EMPTY;
            }
            try {
                return new android.net.Uri.PathSegments(segments, size);
            } finally {
                // Makes sure this doesn't get reused.
                segments = null;
            }
        }
    }

    /**
     * Support for hierarchical URIs.
     */
    private static abstract class AbstractHierarchicalUri extends android.net.Uri {
        public java.lang.String getLastPathSegment() {
            // TODO: If we haven't parsed all of the segments already, just
            // grab the last one directly so we only allocate one string.
            java.util.List<java.lang.String> segments = getPathSegments();
            int size = segments.size();
            if (size == 0) {
                return null;
            }
            return segments.get(size - 1);
        }

        private android.net.Uri.Part userInfo;

        private android.net.Uri.Part getUserInfoPart() {
            return userInfo == null ? userInfo = android.net.Uri.Part.fromEncoded(parseUserInfo()) : userInfo;
        }

        public final java.lang.String getEncodedUserInfo() {
            return getUserInfoPart().getEncoded();
        }

        private java.lang.String parseUserInfo() {
            java.lang.String authority = getEncodedAuthority();
            if (authority == null) {
                return null;
            }
            int end = authority.indexOf('@');
            return end == android.net.Uri.NOT_FOUND ? null : authority.substring(0, end);
        }

        public java.lang.String getUserInfo() {
            return getUserInfoPart().getDecoded();
        }

        private volatile java.lang.String host = android.net.Uri.NOT_CACHED;

        public java.lang.String getHost() {
            @java.lang.SuppressWarnings("StringEquality")
            boolean cached = host != android.net.Uri.NOT_CACHED;
            return cached ? host : (host = parseHost());
        }

        private java.lang.String parseHost() {
            java.lang.String authority = getEncodedAuthority();
            if (authority == null) {
                return null;
            }
            // Parse out user info and then port.
            int userInfoSeparator = authority.indexOf('@');
            int portSeparator = authority.indexOf(':', userInfoSeparator);
            java.lang.String encodedHost = (portSeparator == android.net.Uri.NOT_FOUND) ? authority.substring(userInfoSeparator + 1) : authority.substring(userInfoSeparator + 1, portSeparator);
            return android.net.Uri.decode(encodedHost);
        }

        private volatile int port = android.net.Uri.NOT_CALCULATED;

        public int getPort() {
            return port == android.net.Uri.NOT_CALCULATED ? port = parsePort() : port;
        }

        private int parsePort() {
            java.lang.String authority = getEncodedAuthority();
            if (authority == null) {
                return -1;
            }
            // Make sure we look for the port separtor *after* the user info
            // separator. We have URLs with a ':' in the user info.
            int userInfoSeparator = authority.indexOf('@');
            int portSeparator = authority.indexOf(':', userInfoSeparator);
            if (portSeparator == android.net.Uri.NOT_FOUND) {
                return -1;
            }
            java.lang.String portString = android.net.Uri.decode(authority.substring(portSeparator + 1));
            try {
                return java.lang.Integer.parseInt(portString);
            } catch (java.lang.NumberFormatException e) {
                android.util.Log.w(android.net.Uri.LOG, "Error parsing port string.", e);
                return -1;
            }
        }
    }

    /**
     * Hierarchical Uri.
     */
    private static class HierarchicalUri extends android.net.Uri.AbstractHierarchicalUri {
        /**
         * Used in parcelling.
         */
        static final int TYPE_ID = 3;

        private final java.lang.String scheme;// can be null


        private final android.net.Uri.Part authority;

        private final android.net.Uri.PathPart path;

        private final android.net.Uri.Part query;

        private final android.net.Uri.Part fragment;

        private HierarchicalUri(java.lang.String scheme, android.net.Uri.Part authority, android.net.Uri.PathPart path, android.net.Uri.Part query, android.net.Uri.Part fragment) {
            this.scheme = scheme;
            this.authority = android.net.Uri.Part.nonNull(authority);
            this.path = (path == null) ? android.net.Uri.PathPart.NULL : path;
            this.query = android.net.Uri.Part.nonNull(query);
            this.fragment = android.net.Uri.Part.nonNull(fragment);
        }

        static android.net.Uri readFrom(android.os.Parcel parcel) {
            return new android.net.Uri.HierarchicalUri(parcel.readString(), android.net.Uri.Part.readFrom(parcel), android.net.Uri.PathPart.readFrom(parcel), android.net.Uri.Part.readFrom(parcel), android.net.Uri.Part.readFrom(parcel));
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(android.os.Parcel parcel, int flags) {
            parcel.writeInt(android.net.Uri.HierarchicalUri.TYPE_ID);
            parcel.writeString(scheme);
            authority.writeTo(parcel);
            path.writeTo(parcel);
            query.writeTo(parcel);
            fragment.writeTo(parcel);
        }

        public boolean isHierarchical() {
            return true;
        }

        public boolean isRelative() {
            return scheme == null;
        }

        public java.lang.String getScheme() {
            return scheme;
        }

        private android.net.Uri.Part ssp;

        private android.net.Uri.Part getSsp() {
            return ssp == null ? ssp = android.net.Uri.Part.fromEncoded(makeSchemeSpecificPart()) : ssp;
        }

        public java.lang.String getEncodedSchemeSpecificPart() {
            return getSsp().getEncoded();
        }

        public java.lang.String getSchemeSpecificPart() {
            return getSsp().getDecoded();
        }

        /**
         * Creates the encoded scheme-specific part from its sub parts.
         */
        private java.lang.String makeSchemeSpecificPart() {
            java.lang.StringBuilder builder = new java.lang.StringBuilder();
            appendSspTo(builder);
            return builder.toString();
        }

        private void appendSspTo(java.lang.StringBuilder builder) {
            java.lang.String encodedAuthority = authority.getEncoded();
            if (encodedAuthority != null) {
                // Even if the authority is "", we still want to append "//".
                builder.append("//").append(encodedAuthority);
            }
            java.lang.String encodedPath = path.getEncoded();
            if (encodedPath != null) {
                builder.append(encodedPath);
            }
            if (!query.isEmpty()) {
                builder.append('?').append(query.getEncoded());
            }
        }

        public java.lang.String getAuthority() {
            return this.authority.getDecoded();
        }

        public java.lang.String getEncodedAuthority() {
            return this.authority.getEncoded();
        }

        public java.lang.String getEncodedPath() {
            return this.path.getEncoded();
        }

        public java.lang.String getPath() {
            return this.path.getDecoded();
        }

        public java.lang.String getQuery() {
            return this.query.getDecoded();
        }

        public java.lang.String getEncodedQuery() {
            return this.query.getEncoded();
        }

        public java.lang.String getFragment() {
            return this.fragment.getDecoded();
        }

        public java.lang.String getEncodedFragment() {
            return this.fragment.getEncoded();
        }

        public java.util.List<java.lang.String> getPathSegments() {
            return this.path.getPathSegments();
        }

        private volatile java.lang.String uriString = android.net.Uri.NOT_CACHED;

        @java.lang.Override
        public java.lang.String toString() {
            @java.lang.SuppressWarnings("StringEquality")
            boolean cached = uriString != android.net.Uri.NOT_CACHED;
            return cached ? uriString : (uriString = makeUriString());
        }

        private java.lang.String makeUriString() {
            java.lang.StringBuilder builder = new java.lang.StringBuilder();
            if (scheme != null) {
                builder.append(scheme).append(':');
            }
            appendSspTo(builder);
            if (!fragment.isEmpty()) {
                builder.append('#').append(fragment.getEncoded());
            }
            return builder.toString();
        }

        public android.net.Uri.Builder buildUpon() {
            return new android.net.Uri.Builder().scheme(scheme).authority(authority).path(path).query(query).fragment(fragment);
        }
    }

    /**
     * Helper class for building or manipulating URI references. Not safe for
     * concurrent use.
     *
     * <p>An absolute hierarchical URI reference follows the pattern:
     * {@code <scheme>://<authority><absolute path>?<query>#<fragment>}
     *
     * <p>Relative URI references (which are always hierarchical) follow one
     * of two patterns: {@code <relative or absolute path>?<query>#<fragment>}
     * or {@code //<authority><absolute path>?<query>#<fragment>}
     *
     * <p>An opaque URI follows this pattern:
     * {@code <scheme>:<opaque part>#<fragment>}
     *
     * <p>Use {@link Uri#buildUpon()} to obtain a builder representing an existing URI.
     */
    public static final class Builder {
        private java.lang.String scheme;

        private android.net.Uri.Part opaquePart;

        private android.net.Uri.Part authority;

        private android.net.Uri.PathPart path;

        private android.net.Uri.Part query;

        private android.net.Uri.Part fragment;

        /**
         * Constructs a new Builder.
         */
        public Builder() {
        }

        /**
         * Sets the scheme.
         *
         * @param scheme
         * 		name or {@code null} if this is a relative Uri
         */
        public android.net.Uri.Builder scheme(java.lang.String scheme) {
            this.scheme = scheme;
            return this;
        }

        android.net.Uri.Builder opaquePart(android.net.Uri.Part opaquePart) {
            this.opaquePart = opaquePart;
            return this;
        }

        /**
         * Encodes and sets the given opaque scheme-specific-part.
         *
         * @param opaquePart
         * 		decoded opaque part
         */
        public android.net.Uri.Builder opaquePart(java.lang.String opaquePart) {
            return opaquePart(android.net.Uri.Part.fromDecoded(opaquePart));
        }

        /**
         * Sets the previously encoded opaque scheme-specific-part.
         *
         * @param opaquePart
         * 		encoded opaque part
         */
        public android.net.Uri.Builder encodedOpaquePart(java.lang.String opaquePart) {
            return opaquePart(android.net.Uri.Part.fromEncoded(opaquePart));
        }

        android.net.Uri.Builder authority(android.net.Uri.Part authority) {
            // This URI will be hierarchical.
            this.opaquePart = null;
            this.authority = authority;
            return this;
        }

        /**
         * Encodes and sets the authority.
         */
        public android.net.Uri.Builder authority(java.lang.String authority) {
            return authority(android.net.Uri.Part.fromDecoded(authority));
        }

        /**
         * Sets the previously encoded authority.
         */
        public android.net.Uri.Builder encodedAuthority(java.lang.String authority) {
            return authority(android.net.Uri.Part.fromEncoded(authority));
        }

        android.net.Uri.Builder path(android.net.Uri.PathPart path) {
            // This URI will be hierarchical.
            this.opaquePart = null;
            this.path = path;
            return this;
        }

        /**
         * Sets the path. Leaves '/' characters intact but encodes others as
         * necessary.
         *
         * <p>If the path is not null and doesn't start with a '/', and if
         * you specify a scheme and/or authority, the builder will prepend the
         * given path with a '/'.
         */
        public android.net.Uri.Builder path(java.lang.String path) {
            return path(android.net.Uri.PathPart.fromDecoded(path));
        }

        /**
         * Sets the previously encoded path.
         *
         * <p>If the path is not null and doesn't start with a '/', and if
         * you specify a scheme and/or authority, the builder will prepend the
         * given path with a '/'.
         */
        public android.net.Uri.Builder encodedPath(java.lang.String path) {
            return path(android.net.Uri.PathPart.fromEncoded(path));
        }

        /**
         * Encodes the given segment and appends it to the path.
         */
        public android.net.Uri.Builder appendPath(java.lang.String newSegment) {
            return path(android.net.Uri.PathPart.appendDecodedSegment(path, newSegment));
        }

        /**
         * Appends the given segment to the path.
         */
        public android.net.Uri.Builder appendEncodedPath(java.lang.String newSegment) {
            return path(android.net.Uri.PathPart.appendEncodedSegment(path, newSegment));
        }

        android.net.Uri.Builder query(android.net.Uri.Part query) {
            // This URI will be hierarchical.
            this.opaquePart = null;
            this.query = query;
            return this;
        }

        /**
         * Encodes and sets the query.
         */
        public android.net.Uri.Builder query(java.lang.String query) {
            return query(android.net.Uri.Part.fromDecoded(query));
        }

        /**
         * Sets the previously encoded query.
         */
        public android.net.Uri.Builder encodedQuery(java.lang.String query) {
            return query(android.net.Uri.Part.fromEncoded(query));
        }

        android.net.Uri.Builder fragment(android.net.Uri.Part fragment) {
            this.fragment = fragment;
            return this;
        }

        /**
         * Encodes and sets the fragment.
         */
        public android.net.Uri.Builder fragment(java.lang.String fragment) {
            return fragment(android.net.Uri.Part.fromDecoded(fragment));
        }

        /**
         * Sets the previously encoded fragment.
         */
        public android.net.Uri.Builder encodedFragment(java.lang.String fragment) {
            return fragment(android.net.Uri.Part.fromEncoded(fragment));
        }

        /**
         * Encodes the key and value and then appends the parameter to the
         * query string.
         *
         * @param key
         * 		which will be encoded
         * @param value
         * 		which will be encoded
         */
        public android.net.Uri.Builder appendQueryParameter(java.lang.String key, java.lang.String value) {
            // This URI will be hierarchical.
            this.opaquePart = null;
            java.lang.String encodedParameter = (android.net.Uri.encode(key, null) + "=") + android.net.Uri.encode(value, null);
            if (query == null) {
                query = android.net.Uri.Part.fromEncoded(encodedParameter);
                return this;
            }
            java.lang.String oldQuery = query.getEncoded();
            if ((oldQuery == null) || (oldQuery.length() == 0)) {
                query = android.net.Uri.Part.fromEncoded(encodedParameter);
            } else {
                query = android.net.Uri.Part.fromEncoded((oldQuery + "&") + encodedParameter);
            }
            return this;
        }

        /**
         * Clears the the previously set query.
         */
        public android.net.Uri.Builder clearQuery() {
            return query(((android.net.Uri.Part) (null)));
        }

        /**
         * Constructs a Uri with the current attributes.
         *
         * @throws UnsupportedOperationException
         * 		if the URI is opaque and the
         * 		scheme is null
         */
        public android.net.Uri build() {
            if (opaquePart != null) {
                if (this.scheme == null) {
                    throw new java.lang.UnsupportedOperationException("An opaque URI must have a scheme.");
                }
                return new android.net.Uri.OpaqueUri(scheme, opaquePart, fragment);
            } else {
                // Hierarchical URIs should not return null for getPath().
                android.net.Uri.PathPart path = this.path;
                if ((path == null) || (path == android.net.Uri.PathPart.NULL)) {
                    path = android.net.Uri.PathPart.EMPTY;
                } else {
                    // If we have a scheme and/or authority, the path must
                    // be absolute. Prepend it with a '/' if necessary.
                    if (hasSchemeOrAuthority()) {
                        path = android.net.Uri.PathPart.makeAbsolute(path);
                    }
                }
                return new android.net.Uri.HierarchicalUri(scheme, authority, path, query, fragment);
            }
        }

        private boolean hasSchemeOrAuthority() {
            return (scheme != null) || ((authority != null) && (authority != android.net.Uri.Part.NULL));
        }

        @java.lang.Override
        public java.lang.String toString() {
            return build().toString();
        }
    }

    /**
     * Returns a set of the unique names of all query parameters. Iterating
     * over the set will return the names in order of their first occurrence.
     *
     * @throws UnsupportedOperationException
     * 		if this isn't a hierarchical URI
     * @return a set of decoded names
     */
    public java.util.Set<java.lang.String> getQueryParameterNames() {
        if (isOpaque()) {
            throw new java.lang.UnsupportedOperationException(android.net.Uri.NOT_HIERARCHICAL);
        }
        java.lang.String query = getEncodedQuery();
        if (query == null) {
            return java.util.Collections.emptySet();
        }
        java.util.Set<java.lang.String> names = new java.util.LinkedHashSet<java.lang.String>();
        int start = 0;
        do {
            int next = query.indexOf('&', start);
            int end = (next == (-1)) ? query.length() : next;
            int separator = query.indexOf('=', start);
            if ((separator > end) || (separator == (-1))) {
                separator = end;
            }
            java.lang.String name = query.substring(start, separator);
            names.add(android.net.Uri.decode(name));
            // Move start to end of name.
            start = end + 1;
        } while (start < query.length() );
        return java.util.Collections.unmodifiableSet(names);
    }

    /**
     * Searches the query string for parameter values with the given key.
     *
     * @param key
     * 		which will be encoded
     * @throws UnsupportedOperationException
     * 		if this isn't a hierarchical URI
     * @throws NullPointerException
     * 		if key is null
     * @return a list of decoded values
     */
    public java.util.List<java.lang.String> getQueryParameters(java.lang.String key) {
        if (isOpaque()) {
            throw new java.lang.UnsupportedOperationException(android.net.Uri.NOT_HIERARCHICAL);
        }
        if (key == null) {
            throw new java.lang.NullPointerException("key");
        }
        java.lang.String query = getEncodedQuery();
        if (query == null) {
            return java.util.Collections.emptyList();
        }
        java.lang.String encodedKey;
        try {
            encodedKey = java.net.URLEncoder.encode(key, android.net.Uri.DEFAULT_ENCODING);
        } catch (java.io.UnsupportedEncodingException e) {
            throw new java.lang.AssertionError(e);
        }
        java.util.ArrayList<java.lang.String> values = new java.util.ArrayList<java.lang.String>();
        int start = 0;
        do {
            int nextAmpersand = query.indexOf('&', start);
            int end = (nextAmpersand != (-1)) ? nextAmpersand : query.length();
            int separator = query.indexOf('=', start);
            if ((separator > end) || (separator == (-1))) {
                separator = end;
            }
            if (((separator - start) == encodedKey.length()) && query.regionMatches(start, encodedKey, 0, encodedKey.length())) {
                if (separator == end) {
                    values.add("");
                } else {
                    values.add(android.net.Uri.decode(query.substring(separator + 1, end)));
                }
            }
            // Move start to end of name.
            if (nextAmpersand != (-1)) {
                start = nextAmpersand + 1;
            } else {
                break;
            }
        } while (true );
        return java.util.Collections.unmodifiableList(values);
    }

    /**
     * Searches the query string for the first value with the given key.
     *
     * <p><strong>Warning:</strong> Prior to Jelly Bean, this decoded
     * the '+' character as '+' rather than ' '.
     *
     * @param key
     * 		which will be encoded
     * @throws UnsupportedOperationException
     * 		if this isn't a hierarchical URI
     * @throws NullPointerException
     * 		if key is null
     * @return the decoded value or null if no parameter is found
     */
    public java.lang.String getQueryParameter(java.lang.String key) {
        if (isOpaque()) {
            throw new java.lang.UnsupportedOperationException(android.net.Uri.NOT_HIERARCHICAL);
        }
        if (key == null) {
            throw new java.lang.NullPointerException("key");
        }
        final java.lang.String query = getEncodedQuery();
        if (query == null) {
            return null;
        }
        final java.lang.String encodedKey = android.net.Uri.encode(key, null);
        final int length = query.length();
        int start = 0;
        do {
            int nextAmpersand = query.indexOf('&', start);
            int end = (nextAmpersand != (-1)) ? nextAmpersand : length;
            int separator = query.indexOf('=', start);
            if ((separator > end) || (separator == (-1))) {
                separator = end;
            }
            if (((separator - start) == encodedKey.length()) && query.regionMatches(start, encodedKey, 0, encodedKey.length())) {
                if (separator == end) {
                    return "";
                } else {
                    java.lang.String encodedValue = query.substring(separator + 1, end);
                    return libcore.net.UriCodec.decode(encodedValue, true, java.nio.charset.StandardCharsets.UTF_8, false);
                }
            }
            // Move start to end of name.
            if (nextAmpersand != (-1)) {
                start = nextAmpersand + 1;
            } else {
                break;
            }
        } while (true );
        return null;
    }

    /**
     * Searches the query string for the first value with the given key and interprets it
     * as a boolean value. "false" and "0" are interpreted as <code>false</code>, everything
     * else is interpreted as <code>true</code>.
     *
     * @param key
     * 		which will be decoded
     * @param defaultValue
     * 		the default value to return if there is no query parameter for key
     * @return the boolean interpretation of the query parameter key
     */
    public boolean getBooleanQueryParameter(java.lang.String key, boolean defaultValue) {
        java.lang.String flag = getQueryParameter(key);
        if (flag == null) {
            return defaultValue;
        }
        flag = flag.toLowerCase(java.util.Locale.ROOT);
        return (!"false".equals(flag)) && (!"0".equals(flag));
    }

    /**
     * Return an equivalent URI with a lowercase scheme component.
     * This aligns the Uri with Android best practices for
     * intent filtering.
     *
     * <p>For example, "HTTP://www.android.com" becomes
     * "http://www.android.com"
     *
     * <p>All URIs received from outside Android (such as user input,
     * or external sources like Bluetooth, NFC, or the Internet) should
     * be normalized before they are used to create an Intent.
     *
     * <p class="note">This method does <em>not</em> validate bad URI's,
     * or 'fix' poorly formatted URI's - so do not use it for input validation.
     * A Uri will always be returned, even if the Uri is badly formatted to
     * begin with and a scheme component cannot be found.
     *
     * @return normalized Uri (never null)
     * @see {@link android.content.Intent#setData}
     * @see {@link android.content.Intent#setDataAndNormalize}
     */
    public android.net.Uri normalizeScheme() {
        java.lang.String scheme = getScheme();
        if (scheme == null)
            return this;
        // give up

        java.lang.String lowerScheme = scheme.toLowerCase(java.util.Locale.ROOT);
        if (scheme.equals(lowerScheme))
            return this;
        // no change

        return buildUpon().scheme(lowerScheme).build();
    }

    /**
     * Identifies a null parcelled Uri.
     */
    private static final int NULL_TYPE_ID = 0;

    /**
     * Reads Uris from Parcels.
     */
    public static final android.os.Parcelable.Creator<android.net.Uri> CREATOR = new android.os.Parcelable.Creator<android.net.Uri>() {
        public android.net.Uri createFromParcel(android.os.Parcel in) {
            int type = in.readInt();
            switch (type) {
                case android.net.Uri.NULL_TYPE_ID :
                    return null;
                case android.net.Uri.StringUri.TYPE_ID :
                    return android.net.Uri.StringUri.readFrom(in);
                case android.net.Uri.OpaqueUri.TYPE_ID :
                    return android.net.Uri.OpaqueUri.readFrom(in);
                case android.net.Uri.HierarchicalUri.TYPE_ID :
                    return android.net.Uri.HierarchicalUri.readFrom(in);
            }
            throw new java.lang.IllegalArgumentException("Unknown URI type: " + type);
        }

        public android.net.Uri[] newArray(int size) {
            return new android.net.Uri[size];
        }
    };

    /**
     * Writes a Uri to a Parcel.
     *
     * @param out
     * 		parcel to write to
     * @param uri
     * 		to write, can be null
     */
    public static void writeToParcel(android.os.Parcel out, android.net.Uri uri) {
        if (uri == null) {
            out.writeInt(android.net.Uri.NULL_TYPE_ID);
        } else {
            uri.writeToParcel(out, 0);
        }
    }

    private static final char[] HEX_DIGITS = "0123456789ABCDEF".toCharArray();

    /**
     * Encodes characters in the given string as '%'-escaped octets
     * using the UTF-8 scheme. Leaves letters ("A-Z", "a-z"), numbers
     * ("0-9"), and unreserved characters ("_-!.~'()*") intact. Encodes
     * all other characters.
     *
     * @param s
     * 		string to encode
     * @return an encoded version of s suitable for use as a URI component,
    or null if s is null
     */
    public static java.lang.String encode(java.lang.String s) {
        return android.net.Uri.encode(s, null);
    }

    /**
     * Encodes characters in the given string as '%'-escaped octets
     * using the UTF-8 scheme. Leaves letters ("A-Z", "a-z"), numbers
     * ("0-9"), and unreserved characters ("_-!.~'()*") intact. Encodes
     * all other characters with the exception of those specified in the
     * allow argument.
     *
     * @param s
     * 		string to encode
     * @param allow
     * 		set of additional characters to allow in the encoded form,
     * 		null if no characters should be skipped
     * @return an encoded version of s suitable for use as a URI component,
    or null if s is null
     */
    public static java.lang.String encode(java.lang.String s, java.lang.String allow) {
        if (s == null) {
            return null;
        }
        // Lazily-initialized buffers.
        java.lang.StringBuilder encoded = null;
        int oldLength = s.length();
        // This loop alternates between copying over allowed characters and
        // encoding in chunks. This results in fewer method calls and
        // allocations than encoding one character at a time.
        int current = 0;
        while (current < oldLength) {
            // Start in "copying" mode where we copy over allowed chars.
            // Find the next character which needs to be encoded.
            int nextToEncode = current;
            while ((nextToEncode < oldLength) && android.net.Uri.isAllowed(s.charAt(nextToEncode), allow)) {
                nextToEncode++;
            } 
            // If there's nothing more to encode...
            if (nextToEncode == oldLength) {
                if (current == 0) {
                    // We didn't need to encode anything!
                    return s;
                } else {
                    // Presumably, we've already done some encoding.
                    encoded.append(s, current, oldLength);
                    return encoded.toString();
                }
            }
            if (encoded == null) {
                encoded = new java.lang.StringBuilder();
            }
            if (nextToEncode > current) {
                // Append allowed characters leading up to this point.
                encoded.append(s, current, nextToEncode);
            } else {
                // assert nextToEncode == current
            }
            // Switch to "encoding" mode.
            // Find the next allowed character.
            current = nextToEncode;
            int nextAllowed = current + 1;
            while ((nextAllowed < oldLength) && (!android.net.Uri.isAllowed(s.charAt(nextAllowed), allow))) {
                nextAllowed++;
            } 
            // Convert the substring to bytes and encode the bytes as
            // '%'-escaped octets.
            java.lang.String toEncode = s.substring(current, nextAllowed);
            try {
                byte[] bytes = toEncode.getBytes(android.net.Uri.DEFAULT_ENCODING);
                int bytesLength = bytes.length;
                for (int i = 0; i < bytesLength; i++) {
                    encoded.append('%');
                    encoded.append(android.net.Uri.HEX_DIGITS[(bytes[i] & 0xf0) >> 4]);
                    encoded.append(android.net.Uri.HEX_DIGITS[bytes[i] & 0xf]);
                }
            } catch (java.io.UnsupportedEncodingException e) {
                throw new java.lang.AssertionError(e);
            }
            current = nextAllowed;
        } 
        // Encoded could still be null at this point if s is empty.
        return encoded == null ? s : encoded.toString();
    }

    /**
     * Returns true if the given character is allowed.
     *
     * @param c
     * 		character to check
     * @param allow
     * 		characters to allow
     * @return true if the character is allowed or false if it should be
    encoded
     */
    private static boolean isAllowed(char c, java.lang.String allow) {
        return (((((c >= 'A') && (c <= 'Z')) || ((c >= 'a') && (c <= 'z'))) || ((c >= '0') && (c <= '9'))) || ("_-!.~'()*".indexOf(c) != android.net.Uri.NOT_FOUND)) || ((allow != null) && (allow.indexOf(c) != android.net.Uri.NOT_FOUND));
    }

    /**
     * Decodes '%'-escaped octets in the given string using the UTF-8 scheme.
     * Replaces invalid octets with the unicode replacement character
     * ("\\uFFFD").
     *
     * @param s
     * 		encoded string to decode
     * @return the given string with escaped octets decoded, or null if
    s is null
     */
    public static java.lang.String decode(java.lang.String s) {
        if (s == null) {
            return null;
        }
        return libcore.net.UriCodec.decode(s, false, java.nio.charset.StandardCharsets.UTF_8, false);
    }

    /**
     * Support for part implementations.
     */
    static abstract class AbstractPart {
        /**
         * Enum which indicates which representation of a given part we have.
         */
        static class Representation {
            static final int BOTH = 0;

            static final int ENCODED = 1;

            static final int DECODED = 2;
        }

        volatile java.lang.String encoded;

        volatile java.lang.String decoded;

        AbstractPart(java.lang.String encoded, java.lang.String decoded) {
            this.encoded = encoded;
            this.decoded = decoded;
        }

        abstract java.lang.String getEncoded();

        final java.lang.String getDecoded() {
            @java.lang.SuppressWarnings("StringEquality")
            boolean hasDecoded = decoded != android.net.Uri.NOT_CACHED;
            return hasDecoded ? decoded : (decoded = android.net.Uri.decode(encoded));
        }

        final void writeTo(android.os.Parcel parcel) {
            @java.lang.SuppressWarnings("StringEquality")
            boolean hasEncoded = encoded != android.net.Uri.NOT_CACHED;
            @java.lang.SuppressWarnings("StringEquality")
            boolean hasDecoded = decoded != android.net.Uri.NOT_CACHED;
            if (hasEncoded && hasDecoded) {
                parcel.writeInt(android.net.Uri.AbstractPart.Representation.BOTH);
                parcel.writeString(encoded);
                parcel.writeString(decoded);
            } else
                if (hasEncoded) {
                    parcel.writeInt(android.net.Uri.AbstractPart.Representation.ENCODED);
                    parcel.writeString(encoded);
                } else
                    if (hasDecoded) {
                        parcel.writeInt(android.net.Uri.AbstractPart.Representation.DECODED);
                        parcel.writeString(decoded);
                    } else {
                        throw new java.lang.IllegalArgumentException("Neither encoded nor decoded");
                    }


        }
    }

    /**
     * Immutable wrapper of encoded and decoded versions of a URI part. Lazily
     * creates the encoded or decoded version from the other.
     */
    static class Part extends android.net.Uri.AbstractPart {
        /**
         * A part with null values.
         */
        static final android.net.Uri.Part NULL = new android.net.Uri.Part.EmptyPart(null);

        /**
         * A part with empty strings for values.
         */
        static final android.net.Uri.Part EMPTY = new android.net.Uri.Part.EmptyPart("");

        private Part(java.lang.String encoded, java.lang.String decoded) {
            super(encoded, decoded);
        }

        boolean isEmpty() {
            return false;
        }

        java.lang.String getEncoded() {
            @java.lang.SuppressWarnings("StringEquality")
            boolean hasEncoded = encoded != android.net.Uri.NOT_CACHED;
            return hasEncoded ? encoded : (encoded = android.net.Uri.encode(decoded));
        }

        static android.net.Uri.Part readFrom(android.os.Parcel parcel) {
            int representation = parcel.readInt();
            switch (representation) {
                case android.net.Uri.AbstractPart.Representation.BOTH :
                    return android.net.Uri.Part.from(parcel.readString(), parcel.readString());
                case android.net.Uri.AbstractPart.Representation.ENCODED :
                    return android.net.Uri.Part.fromEncoded(parcel.readString());
                case android.net.Uri.AbstractPart.Representation.DECODED :
                    return android.net.Uri.Part.fromDecoded(parcel.readString());
                default :
                    throw new java.lang.IllegalArgumentException("Unknown representation: " + representation);
            }
        }

        /**
         * Returns given part or {@link #NULL} if the given part is null.
         */
        static android.net.Uri.Part nonNull(android.net.Uri.Part part) {
            return part == null ? android.net.Uri.Part.NULL : part;
        }

        /**
         * Creates a part from the encoded string.
         *
         * @param encoded
         * 		part string
         */
        static android.net.Uri.Part fromEncoded(java.lang.String encoded) {
            return android.net.Uri.Part.from(encoded, android.net.Uri.NOT_CACHED);
        }

        /**
         * Creates a part from the decoded string.
         *
         * @param decoded
         * 		part string
         */
        static android.net.Uri.Part fromDecoded(java.lang.String decoded) {
            return android.net.Uri.Part.from(android.net.Uri.NOT_CACHED, decoded);
        }

        /**
         * Creates a part from the encoded and decoded strings.
         *
         * @param encoded
         * 		part string
         * @param decoded
         * 		part string
         */
        static android.net.Uri.Part from(java.lang.String encoded, java.lang.String decoded) {
            // We have to check both encoded and decoded in case one is
            // NOT_CACHED.
            if (encoded == null) {
                return android.net.Uri.Part.NULL;
            }
            if (encoded.length() == 0) {
                return android.net.Uri.Part.EMPTY;
            }
            if (decoded == null) {
                return android.net.Uri.Part.NULL;
            }
            if (decoded.length() == 0) {
                return android.net.Uri.Part.EMPTY;
            }
            return new android.net.Uri.Part(encoded, decoded);
        }

        private static class EmptyPart extends android.net.Uri.Part {
            public EmptyPart(java.lang.String value) {
                super(value, value);
            }

            @java.lang.Override
            boolean isEmpty() {
                return true;
            }
        }
    }

    /**
     * Immutable wrapper of encoded and decoded versions of a path part. Lazily
     * creates the encoded or decoded version from the other.
     */
    static class PathPart extends android.net.Uri.AbstractPart {
        /**
         * A part with null values.
         */
        static final android.net.Uri.PathPart NULL = new android.net.Uri.PathPart(null, null);

        /**
         * A part with empty strings for values.
         */
        static final android.net.Uri.PathPart EMPTY = new android.net.Uri.PathPart("", "");

        private PathPart(java.lang.String encoded, java.lang.String decoded) {
            super(encoded, decoded);
        }

        java.lang.String getEncoded() {
            @java.lang.SuppressWarnings("StringEquality")
            boolean hasEncoded = encoded != android.net.Uri.NOT_CACHED;
            // Don't encode '/'.
            return hasEncoded ? encoded : (encoded = android.net.Uri.encode(decoded, "/"));
        }

        /**
         * Cached path segments. This doesn't need to be volatile--we don't
         * care if other threads see the result.
         */
        private android.net.Uri.PathSegments pathSegments;

        /**
         * Gets the individual path segments. Parses them if necessary.
         *
         * @return parsed path segments or null if this isn't a hierarchical
        URI
         */
        android.net.Uri.PathSegments getPathSegments() {
            if (pathSegments != null) {
                return pathSegments;
            }
            java.lang.String path = getEncoded();
            if (path == null) {
                return pathSegments = android.net.Uri.PathSegments.EMPTY;
            }
            android.net.Uri.PathSegmentsBuilder segmentBuilder = new android.net.Uri.PathSegmentsBuilder();
            int previous = 0;
            int current;
            while ((current = path.indexOf('/', previous)) > (-1)) {
                // This check keeps us from adding a segment if the path starts
                // '/' and an empty segment for "//".
                if (previous < current) {
                    java.lang.String decodedSegment = android.net.Uri.decode(path.substring(previous, current));
                    segmentBuilder.add(decodedSegment);
                }
                previous = current + 1;
            } 
            // Add in the final path segment.
            if (previous < path.length()) {
                segmentBuilder.add(android.net.Uri.decode(path.substring(previous)));
            }
            return pathSegments = segmentBuilder.build();
        }

        static android.net.Uri.PathPart appendEncodedSegment(android.net.Uri.PathPart oldPart, java.lang.String newSegment) {
            // If there is no old path, should we make the new path relative
            // or absolute? I pick absolute.
            if (oldPart == null) {
                // No old path.
                return android.net.Uri.PathPart.fromEncoded("/" + newSegment);
            }
            java.lang.String oldPath = oldPart.getEncoded();
            if (oldPath == null) {
                oldPath = "";
            }
            int oldPathLength = oldPath.length();
            java.lang.String newPath;
            if (oldPathLength == 0) {
                // No old path.
                newPath = "/" + newSegment;
            } else
                if (oldPath.charAt(oldPathLength - 1) == '/') {
                    newPath = oldPath + newSegment;
                } else {
                    newPath = (oldPath + "/") + newSegment;
                }

            return android.net.Uri.PathPart.fromEncoded(newPath);
        }

        static android.net.Uri.PathPart appendDecodedSegment(android.net.Uri.PathPart oldPart, java.lang.String decoded) {
            java.lang.String encoded = android.net.Uri.encode(decoded);
            // TODO: Should we reuse old PathSegments? Probably not.
            return android.net.Uri.PathPart.appendEncodedSegment(oldPart, encoded);
        }

        static android.net.Uri.PathPart readFrom(android.os.Parcel parcel) {
            int representation = parcel.readInt();
            switch (representation) {
                case android.net.Uri.AbstractPart.Representation.BOTH :
                    return android.net.Uri.PathPart.from(parcel.readString(), parcel.readString());
                case android.net.Uri.AbstractPart.Representation.ENCODED :
                    return android.net.Uri.PathPart.fromEncoded(parcel.readString());
                case android.net.Uri.AbstractPart.Representation.DECODED :
                    return android.net.Uri.PathPart.fromDecoded(parcel.readString());
                default :
                    throw new java.lang.IllegalArgumentException("Bad representation: " + representation);
            }
        }

        /**
         * Creates a path from the encoded string.
         *
         * @param encoded
         * 		part string
         */
        static android.net.Uri.PathPart fromEncoded(java.lang.String encoded) {
            return android.net.Uri.PathPart.from(encoded, android.net.Uri.NOT_CACHED);
        }

        /**
         * Creates a path from the decoded string.
         *
         * @param decoded
         * 		part string
         */
        static android.net.Uri.PathPart fromDecoded(java.lang.String decoded) {
            return android.net.Uri.PathPart.from(android.net.Uri.NOT_CACHED, decoded);
        }

        /**
         * Creates a path from the encoded and decoded strings.
         *
         * @param encoded
         * 		part string
         * @param decoded
         * 		part string
         */
        static android.net.Uri.PathPart from(java.lang.String encoded, java.lang.String decoded) {
            if (encoded == null) {
                return android.net.Uri.PathPart.NULL;
            }
            if (encoded.length() == 0) {
                return android.net.Uri.PathPart.EMPTY;
            }
            return new android.net.Uri.PathPart(encoded, decoded);
        }

        /**
         * Prepends path values with "/" if they're present, not empty, and
         * they don't already start with "/".
         */
        static android.net.Uri.PathPart makeAbsolute(android.net.Uri.PathPart oldPart) {
            @java.lang.SuppressWarnings("StringEquality")
            boolean encodedCached = oldPart.encoded != android.net.Uri.NOT_CACHED;
            // We don't care which version we use, and we don't want to force
            // unneccessary encoding/decoding.
            java.lang.String oldPath = (encodedCached) ? oldPart.encoded : oldPart.decoded;
            if (((oldPath == null) || (oldPath.length() == 0)) || oldPath.startsWith("/")) {
                return oldPart;
            }
            // Prepend encoded string if present.
            java.lang.String newEncoded = (encodedCached) ? "/" + oldPart.encoded : android.net.Uri.NOT_CACHED;
            // Prepend decoded string if present.
            @java.lang.SuppressWarnings("StringEquality")
            boolean decodedCached = oldPart.decoded != android.net.Uri.NOT_CACHED;
            java.lang.String newDecoded = (decodedCached) ? "/" + oldPart.decoded : android.net.Uri.NOT_CACHED;
            return new android.net.Uri.PathPart(newEncoded, newDecoded);
        }
    }

    /**
     * Creates a new Uri by appending an already-encoded path segment to a
     * base Uri.
     *
     * @param baseUri
     * 		Uri to append path segment to
     * @param pathSegment
     * 		encoded path segment to append
     * @return a new Uri based on baseUri with the given segment appended to
    the path
     * @throws NullPointerException
     * 		if baseUri is null
     */
    public static android.net.Uri withAppendedPath(android.net.Uri baseUri, java.lang.String pathSegment) {
        android.net.Uri.Builder builder = baseUri.buildUpon();
        builder = builder.appendEncodedPath(pathSegment);
        return builder.build();
    }

    /**
     * If this {@link Uri} is {@code file://}, then resolve and return its
     * canonical path. Also fixes legacy emulated storage paths so they are
     * usable across user boundaries. Should always be called from the app
     * process before sending elsewhere.
     *
     * @unknown 
     */
    public android.net.Uri getCanonicalUri() {
        if ("file".equals(getScheme())) {
            final java.lang.String canonicalPath;
            try {
                canonicalPath = new java.io.File(getPath()).getCanonicalPath();
            } catch (java.io.IOException e) {
                return this;
            }
            if (android.os.Environment.isExternalStorageEmulated()) {
                final java.lang.String legacyPath = android.os.Environment.getLegacyExternalStorageDirectory().toString();
                // Splice in user-specific path when legacy path is found
                if (canonicalPath.startsWith(legacyPath)) {
                    return android.net.Uri.fromFile(new java.io.File(android.os.Environment.getExternalStorageDirectory().toString(), canonicalPath.substring(legacyPath.length() + 1)));
                }
            }
            return android.net.Uri.fromFile(new java.io.File(canonicalPath));
        } else {
            return this;
        }
    }

    /**
     * If this is a {@code file://} Uri, it will be reported to
     * {@link StrictMode}.
     *
     * @unknown 
     */
    public void checkFileUriExposed(java.lang.String location) {
        if ("file".equals(getScheme()) && (!getPath().startsWith("/system/"))) {
            android.os.StrictMode.onFileUriExposed(this, location);
        }
    }

    /**
     * Test if this is a path prefix match against the given Uri. Verifies that
     * scheme, authority, and atomic path segments match.
     *
     * @unknown 
     */
    public boolean isPathPrefixMatch(android.net.Uri prefix) {
        if (!java.util.Objects.equals(getScheme(), prefix.getScheme()))
            return false;

        if (!java.util.Objects.equals(getAuthority(), prefix.getAuthority()))
            return false;

        java.util.List<java.lang.String> seg = getPathSegments();
        java.util.List<java.lang.String> prefixSeg = prefix.getPathSegments();
        final int prefixSize = prefixSeg.size();
        if (seg.size() < prefixSize)
            return false;

        for (int i = 0; i < prefixSize; i++) {
            if (!java.util.Objects.equals(seg.get(i), prefixSeg.get(i))) {
                return false;
            }
        }
        return true;
    }
}

