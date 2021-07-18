/**
 * Copyright (C) 2006 The Android Open Source Project
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
package android.net.http;


/**
 * SSL certificate info (certificate details) class
 */
public class SslCertificate {
    /**
     * SimpleDateFormat pattern for an ISO 8601 date
     */
    private static java.lang.String ISO_8601_DATE_FORMAT = "yyyy-MM-dd HH:mm:ssZ";

    /**
     * Name of the entity this certificate is issued to
     */
    private final android.net.http.SslCertificate.DName mIssuedTo;

    /**
     * Name of the entity this certificate is issued by
     */
    private final android.net.http.SslCertificate.DName mIssuedBy;

    /**
     * Not-before date from the validity period
     */
    private final java.util.Date mValidNotBefore;

    /**
     * Not-after date from the validity period
     */
    private final java.util.Date mValidNotAfter;

    /**
     * The original source certificate, if available.
     *
     * TODO If deprecated constructors are removed, this should always
     * be available, and saveState and restoreState can be simplified
     * to be unconditional.
     */
    private final java.security.cert.X509Certificate mX509Certificate;

    /**
     * Bundle key names
     */
    private static final java.lang.String ISSUED_TO = "issued-to";

    private static final java.lang.String ISSUED_BY = "issued-by";

    private static final java.lang.String VALID_NOT_BEFORE = "valid-not-before";

    private static final java.lang.String VALID_NOT_AFTER = "valid-not-after";

    private static final java.lang.String X509_CERTIFICATE = "x509-certificate";

    /**
     * Saves the certificate state to a bundle
     *
     * @param certificate
     * 		The SSL certificate to store
     * @return A bundle with the certificate stored in it or null if fails
     */
    public static android.os.Bundle saveState(android.net.http.SslCertificate certificate) {
        if (certificate == null) {
            return null;
        }
        android.os.Bundle bundle = new android.os.Bundle();
        bundle.putString(android.net.http.SslCertificate.ISSUED_TO, certificate.getIssuedTo().getDName());
        bundle.putString(android.net.http.SslCertificate.ISSUED_BY, certificate.getIssuedBy().getDName());
        bundle.putString(android.net.http.SslCertificate.VALID_NOT_BEFORE, certificate.getValidNotBefore());
        bundle.putString(android.net.http.SslCertificate.VALID_NOT_AFTER, certificate.getValidNotAfter());
        java.security.cert.X509Certificate x509Certificate = certificate.mX509Certificate;
        if (x509Certificate != null) {
            try {
                bundle.putByteArray(android.net.http.SslCertificate.X509_CERTIFICATE, x509Certificate.getEncoded());
            } catch (java.security.cert.CertificateEncodingException ignored) {
            }
        }
        return bundle;
    }

    /**
     * Restores the certificate stored in the bundle
     *
     * @param bundle
     * 		The bundle with the certificate state stored in it
     * @return The SSL certificate stored in the bundle or null if fails
     */
    public static android.net.http.SslCertificate restoreState(android.os.Bundle bundle) {
        if (bundle == null) {
            return null;
        }
        java.security.cert.X509Certificate x509Certificate;
        byte[] bytes = bundle.getByteArray(android.net.http.SslCertificate.X509_CERTIFICATE);
        if (bytes == null) {
            x509Certificate = null;
        } else {
            try {
                java.security.cert.CertificateFactory certFactory = java.security.cert.CertificateFactory.getInstance("X.509");
                java.security.cert.Certificate cert = certFactory.generateCertificate(new java.io.ByteArrayInputStream(bytes));
                x509Certificate = ((java.security.cert.X509Certificate) (cert));
            } catch (java.security.cert.CertificateException e) {
                x509Certificate = null;
            }
        }
        return new android.net.http.SslCertificate(bundle.getString(android.net.http.SslCertificate.ISSUED_TO), bundle.getString(android.net.http.SslCertificate.ISSUED_BY), android.net.http.SslCertificate.parseDate(bundle.getString(android.net.http.SslCertificate.VALID_NOT_BEFORE)), android.net.http.SslCertificate.parseDate(bundle.getString(android.net.http.SslCertificate.VALID_NOT_AFTER)), x509Certificate);
    }

    /**
     * Creates a new SSL certificate object
     *
     * @param issuedTo
     * 		The entity this certificate is issued to
     * @param issuedBy
     * 		The entity that issued this certificate
     * @param validNotBefore
     * 		The not-before date from the certificate
     * 		validity period in ISO 8601 format
     * @param validNotAfter
     * 		The not-after date from the certificate
     * 		validity period in ISO 8601 format
     * @deprecated Use {@link #SslCertificate(X509Certificate)}
     */
    @java.lang.Deprecated
    public SslCertificate(java.lang.String issuedTo, java.lang.String issuedBy, java.lang.String validNotBefore, java.lang.String validNotAfter) {
        this(issuedTo, issuedBy, android.net.http.SslCertificate.parseDate(validNotBefore), android.net.http.SslCertificate.parseDate(validNotAfter), null);
    }

    /**
     * Creates a new SSL certificate object
     *
     * @param issuedTo
     * 		The entity this certificate is issued to
     * @param issuedBy
     * 		The entity that issued this certificate
     * @param validNotBefore
     * 		The not-before date from the certificate validity period
     * @param validNotAfter
     * 		The not-after date from the certificate validity period
     * @deprecated Use {@link #SslCertificate(X509Certificate)}
     */
    @java.lang.Deprecated
    public SslCertificate(java.lang.String issuedTo, java.lang.String issuedBy, java.util.Date validNotBefore, java.util.Date validNotAfter) {
        this(issuedTo, issuedBy, validNotBefore, validNotAfter, null);
    }

    /**
     * Creates a new SSL certificate object from an X509 certificate
     *
     * @param certificate
     * 		X509 certificate
     */
    public SslCertificate(java.security.cert.X509Certificate certificate) {
        this(certificate.getSubjectDN().getName(), certificate.getIssuerDN().getName(), certificate.getNotBefore(), certificate.getNotAfter(), certificate);
    }

    private SslCertificate(java.lang.String issuedTo, java.lang.String issuedBy, java.util.Date validNotBefore, java.util.Date validNotAfter, java.security.cert.X509Certificate x509Certificate) {
        mIssuedTo = new android.net.http.SslCertificate.DName(issuedTo);
        mIssuedBy = new android.net.http.SslCertificate.DName(issuedBy);
        mValidNotBefore = android.net.http.SslCertificate.cloneDate(validNotBefore);
        mValidNotAfter = android.net.http.SslCertificate.cloneDate(validNotAfter);
        mX509Certificate = x509Certificate;
    }

    /**
     *
     *
     * @return Not-before date from the certificate validity period or
    "" if none has been set
     */
    public java.util.Date getValidNotBeforeDate() {
        return android.net.http.SslCertificate.cloneDate(mValidNotBefore);
    }

    /**
     *
     *
     * @return Not-before date from the certificate validity period in
    ISO 8601 format or "" if none has been set
     * @deprecated Use {@link #getValidNotBeforeDate()}
     */
    @java.lang.Deprecated
    public java.lang.String getValidNotBefore() {
        return android.net.http.SslCertificate.formatDate(mValidNotBefore);
    }

    /**
     *
     *
     * @return Not-after date from the certificate validity period or
    "" if none has been set
     */
    public java.util.Date getValidNotAfterDate() {
        return android.net.http.SslCertificate.cloneDate(mValidNotAfter);
    }

    /**
     *
     *
     * @return Not-after date from the certificate validity period in
    ISO 8601 format or "" if none has been set
     * @deprecated Use {@link #getValidNotAfterDate()}
     */
    @java.lang.Deprecated
    public java.lang.String getValidNotAfter() {
        return android.net.http.SslCertificate.formatDate(mValidNotAfter);
    }

    /**
     *
     *
     * @return Issued-to distinguished name or null if none has been set
     */
    public android.net.http.SslCertificate.DName getIssuedTo() {
        return mIssuedTo;
    }

    /**
     *
     *
     * @return Issued-by distinguished name or null if none has been set
     */
    public android.net.http.SslCertificate.DName getIssuedBy() {
        return mIssuedBy;
    }

    /**
     * Convenience for UI presentation, not intended as public API.
     */
    private static java.lang.String getSerialNumber(java.security.cert.X509Certificate x509Certificate) {
        if (x509Certificate == null) {
            return "";
        }
        java.math.BigInteger serialNumber = x509Certificate.getSerialNumber();
        if (serialNumber == null) {
            return "";
        }
        return android.net.http.SslCertificate.fingerprint(serialNumber.toByteArray());
    }

    /**
     * Convenience for UI presentation, not intended as public API.
     */
    private static java.lang.String getDigest(java.security.cert.X509Certificate x509Certificate, java.lang.String algorithm) {
        if (x509Certificate == null) {
            return "";
        }
        try {
            byte[] bytes = x509Certificate.getEncoded();
            java.security.MessageDigest md = java.security.MessageDigest.getInstance(algorithm);
            byte[] digest = md.digest(bytes);
            return android.net.http.SslCertificate.fingerprint(digest);
        } catch (java.security.cert.CertificateEncodingException ignored) {
            return "";
        } catch (java.security.NoSuchAlgorithmException ignored) {
            return "";
        }
    }

    private static final java.lang.String fingerprint(byte[] bytes) {
        if (bytes == null) {
            return "";
        }
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            byte b = bytes[i];
            com.android.internal.util.HexDump.appendByteAsHex(sb, b, true);
            if ((i + 1) != bytes.length) {
                sb.append(':');
            }
        }
        return sb.toString();
    }

    /**
     *
     *
     * @return A string representation of this certificate for debugging
     */
    public java.lang.String toString() {
        return (((("Issued to: " + mIssuedTo.getDName()) + ";\n") + "Issued by: ") + mIssuedBy.getDName()) + ";\n";
    }

    /**
     * Parse an ISO 8601 date converting ParseExceptions to a null result;
     */
    private static java.util.Date parseDate(java.lang.String string) {
        try {
            return new java.text.SimpleDateFormat(android.net.http.SslCertificate.ISO_8601_DATE_FORMAT).parse(string);
        } catch (java.text.ParseException e) {
            return null;
        }
    }

    /**
     * Format a date as an ISO 8601 string, return "" for a null date
     */
    private static java.lang.String formatDate(java.util.Date date) {
        if (date == null) {
            return "";
        }
        return new java.text.SimpleDateFormat(android.net.http.SslCertificate.ISO_8601_DATE_FORMAT).format(date);
    }

    /**
     * Clone a possibly null Date
     */
    private static java.util.Date cloneDate(java.util.Date date) {
        if (date == null) {
            return null;
        }
        return ((java.util.Date) (date.clone()));
    }

    /**
     * A distinguished name helper class: a 3-tuple of:
     * <ul>
     *   <li>the most specific common name (CN)</li>
     *   <li>the most specific organization (O)</li>
     *   <li>the most specific organizational unit (OU)</li>
     * <ul>
     */
    public class DName {
        /**
         * Distinguished name (normally includes CN, O, and OU names)
         */
        private java.lang.String mDName;

        /**
         * Common-name (CN) component of the name
         */
        private java.lang.String mCName;

        /**
         * Organization (O) component of the name
         */
        private java.lang.String mOName;

        /**
         * Organizational Unit (OU) component of the name
         */
        private java.lang.String mUName;

        /**
         * Creates a new {@code DName} from a string. The attributes
         * are assumed to come in most significant to least
         * significant order which is true of human readable values
         * returned by methods such as {@code X500Principal.getName()}.
         * Be aware that the underlying sources of distinguished names
         * such as instances of {@code X509Certificate} are encoded in
         * least significant to most significant order, so make sure
         * the value passed here has the expected ordering of
         * attributes.
         */
        public DName(java.lang.String dName) {
            if (dName != null) {
                mDName = dName;
                try {
                    com.android.org.bouncycastle.asn1.x509.X509Name x509Name = new com.android.org.bouncycastle.asn1.x509.X509Name(dName);
                    java.util.Vector val = x509Name.getValues();
                    java.util.Vector oid = x509Name.getOIDs();
                    for (int i = 0; i < oid.size(); i++) {
                        if (oid.elementAt(i).equals(X509Name.CN)) {
                            if (mCName == null) {
                                mCName = ((java.lang.String) (val.elementAt(i)));
                            }
                            continue;
                        }
                        if (oid.elementAt(i).equals(X509Name.O)) {
                            if (mOName == null) {
                                mOName = ((java.lang.String) (val.elementAt(i)));
                                continue;
                            }
                        }
                        if (oid.elementAt(i).equals(X509Name.OU)) {
                            if (mUName == null) {
                                mUName = ((java.lang.String) (val.elementAt(i)));
                                continue;
                            }
                        }
                    }
                } catch (java.lang.IllegalArgumentException ex) {
                    // thrown if there is an error parsing the string
                }
            }
        }

        /**
         *
         *
         * @return The distinguished name (normally includes CN, O, and OU names)
         */
        public java.lang.String getDName() {
            return mDName != null ? mDName : "";
        }

        /**
         *
         *
         * @return The most specific Common-name (CN) component of this name
         */
        public java.lang.String getCName() {
            return mCName != null ? mCName : "";
        }

        /**
         *
         *
         * @return The most specific Organization (O) component of this name
         */
        public java.lang.String getOName() {
            return mOName != null ? mOName : "";
        }

        /**
         *
         *
         * @return The most specific Organizational Unit (OU) component of this name
         */
        public java.lang.String getUName() {
            return mUName != null ? mUName : "";
        }
    }

    /**
     * Inflates the SSL certificate view (helper method).
     *
     * @return The resultant certificate view with issued-to, issued-by,
    issued-on, expires-on, and possibly other fields set.
     * @unknown Used by Browser and Settings
     */
    public android.view.View inflateCertificateView(android.content.Context context) {
        android.view.LayoutInflater factory = android.view.LayoutInflater.from(context);
        android.view.View certificateView = factory.inflate(com.android.internal.R.layout.ssl_certificate, null);
        // issued to:
        android.net.http.SslCertificate.DName issuedTo = getIssuedTo();
        if (issuedTo != null) {
            ((android.widget.TextView) (certificateView.findViewById(com.android.internal.R.id.to_common))).setText(issuedTo.getCName());
            ((android.widget.TextView) (certificateView.findViewById(com.android.internal.R.id.to_org))).setText(issuedTo.getOName());
            ((android.widget.TextView) (certificateView.findViewById(com.android.internal.R.id.to_org_unit))).setText(issuedTo.getUName());
        }
        // serial number:
        ((android.widget.TextView) (certificateView.findViewById(com.android.internal.R.id.serial_number))).setText(android.net.http.SslCertificate.getSerialNumber(mX509Certificate));
        // issued by:
        android.net.http.SslCertificate.DName issuedBy = getIssuedBy();
        if (issuedBy != null) {
            ((android.widget.TextView) (certificateView.findViewById(com.android.internal.R.id.by_common))).setText(issuedBy.getCName());
            ((android.widget.TextView) (certificateView.findViewById(com.android.internal.R.id.by_org))).setText(issuedBy.getOName());
            ((android.widget.TextView) (certificateView.findViewById(com.android.internal.R.id.by_org_unit))).setText(issuedBy.getUName());
        }
        // issued on:
        java.lang.String issuedOn = formatCertificateDate(context, getValidNotBeforeDate());
        ((android.widget.TextView) (certificateView.findViewById(com.android.internal.R.id.issued_on))).setText(issuedOn);
        // expires on:
        java.lang.String expiresOn = formatCertificateDate(context, getValidNotAfterDate());
        ((android.widget.TextView) (certificateView.findViewById(com.android.internal.R.id.expires_on))).setText(expiresOn);
        // fingerprints:
        ((android.widget.TextView) (certificateView.findViewById(com.android.internal.R.id.sha256_fingerprint))).setText(android.net.http.SslCertificate.getDigest(mX509Certificate, "SHA256"));
        ((android.widget.TextView) (certificateView.findViewById(com.android.internal.R.id.sha1_fingerprint))).setText(android.net.http.SslCertificate.getDigest(mX509Certificate, "SHA1"));
        return certificateView;
    }

    /**
     * Formats the certificate date to a properly localized date string.
     *
     * @return Properly localized version of the certificate date string and
    the "" if it fails to localize.
     */
    private java.lang.String formatCertificateDate(android.content.Context context, java.util.Date certificateDate) {
        if (certificateDate == null) {
            return "";
        }
        return android.text.format.DateFormat.getDateFormat(context).format(certificateDate);
    }
}

