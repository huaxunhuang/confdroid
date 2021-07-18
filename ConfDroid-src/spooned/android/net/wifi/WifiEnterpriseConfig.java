/**
 * Copyright (C) 2013 The Android Open Source Project
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
package android.net.wifi;


/**
 * Enterprise configuration details for Wi-Fi. Stores details about the EAP method
 * and any associated credentials.
 */
public class WifiEnterpriseConfig implements android.os.Parcelable {
    /**
     *
     *
     * @unknown 
     */
    public static final java.lang.String EMPTY_VALUE = "NULL";

    /**
     *
     *
     * @unknown 
     */
    public static final java.lang.String EAP_KEY = "eap";

    /**
     *
     *
     * @unknown 
     */
    public static final java.lang.String PHASE2_KEY = "phase2";

    /**
     *
     *
     * @unknown 
     */
    public static final java.lang.String IDENTITY_KEY = "identity";

    /**
     *
     *
     * @unknown 
     */
    public static final java.lang.String ANON_IDENTITY_KEY = "anonymous_identity";

    /**
     *
     *
     * @unknown 
     */
    public static final java.lang.String PASSWORD_KEY = "password";

    /**
     *
     *
     * @unknown 
     */
    public static final java.lang.String SUBJECT_MATCH_KEY = "subject_match";

    /**
     *
     *
     * @unknown 
     */
    public static final java.lang.String ALTSUBJECT_MATCH_KEY = "altsubject_match";

    /**
     *
     *
     * @unknown 
     */
    public static final java.lang.String DOM_SUFFIX_MATCH_KEY = "domain_suffix_match";

    /**
     *
     *
     * @unknown 
     */
    public static final java.lang.String OPP_KEY_CACHING = "proactive_key_caching";

    /**
     * String representing the keystore OpenSSL ENGINE's ID.
     *
     * @unknown 
     */
    public static final java.lang.String ENGINE_ID_KEYSTORE = "keystore";

    /**
     * String representing the keystore URI used for wpa_supplicant.
     *
     * @unknown 
     */
    public static final java.lang.String KEYSTORE_URI = "keystore://";

    /**
     * String representing the keystore URI used for wpa_supplicant,
     * Unlike #KEYSTORE_URI, this supports a list of space-delimited aliases
     *
     * @unknown 
     */
    public static final java.lang.String KEYSTORES_URI = "keystores://";

    /**
     * String to set the engine value to when it should be enabled.
     *
     * @unknown 
     */
    public static final java.lang.String ENGINE_ENABLE = "1";

    /**
     * String to set the engine value to when it should be disabled.
     *
     * @unknown 
     */
    public static final java.lang.String ENGINE_DISABLE = "0";

    /**
     *
     *
     * @unknown 
     */
    public static final java.lang.String CA_CERT_PREFIX = android.net.wifi.WifiEnterpriseConfig.KEYSTORE_URI + android.security.Credentials.CA_CERTIFICATE;

    /**
     *
     *
     * @unknown 
     */
    public static final java.lang.String CLIENT_CERT_PREFIX = android.net.wifi.WifiEnterpriseConfig.KEYSTORE_URI + android.security.Credentials.USER_CERTIFICATE;

    /**
     *
     *
     * @unknown 
     */
    public static final java.lang.String CLIENT_CERT_KEY = "client_cert";

    /**
     *
     *
     * @unknown 
     */
    public static final java.lang.String CA_CERT_KEY = "ca_cert";

    /**
     *
     *
     * @unknown 
     */
    public static final java.lang.String CA_PATH_KEY = "ca_path";

    /**
     *
     *
     * @unknown 
     */
    public static final java.lang.String ENGINE_KEY = "engine";

    /**
     *
     *
     * @unknown 
     */
    public static final java.lang.String ENGINE_ID_KEY = "engine_id";

    /**
     *
     *
     * @unknown 
     */
    public static final java.lang.String PRIVATE_KEY_ID_KEY = "key_id";

    /**
     *
     *
     * @unknown 
     */
    public static final java.lang.String REALM_KEY = "realm";

    /**
     *
     *
     * @unknown 
     */
    public static final java.lang.String PLMN_KEY = "plmn";

    /**
     *
     *
     * @unknown 
     */
    public static final java.lang.String CA_CERT_ALIAS_DELIMITER = " ";

    // Fields to copy verbatim from wpa_supplicant.
    private static final java.lang.String[] SUPPLICANT_CONFIG_KEYS = new java.lang.String[]{ android.net.wifi.WifiEnterpriseConfig.IDENTITY_KEY, android.net.wifi.WifiEnterpriseConfig.ANON_IDENTITY_KEY, android.net.wifi.WifiEnterpriseConfig.PASSWORD_KEY, android.net.wifi.WifiEnterpriseConfig.CLIENT_CERT_KEY, android.net.wifi.WifiEnterpriseConfig.CA_CERT_KEY, android.net.wifi.WifiEnterpriseConfig.SUBJECT_MATCH_KEY, android.net.wifi.WifiEnterpriseConfig.ENGINE_KEY, android.net.wifi.WifiEnterpriseConfig.ENGINE_ID_KEY, android.net.wifi.WifiEnterpriseConfig.PRIVATE_KEY_ID_KEY, android.net.wifi.WifiEnterpriseConfig.ALTSUBJECT_MATCH_KEY, android.net.wifi.WifiEnterpriseConfig.DOM_SUFFIX_MATCH_KEY, android.net.wifi.WifiEnterpriseConfig.CA_PATH_KEY };

    private java.util.HashMap<java.lang.String, java.lang.String> mFields = new java.util.HashMap<java.lang.String, java.lang.String>();

    private java.security.cert.X509Certificate[] mCaCerts;

    private java.security.PrivateKey mClientPrivateKey;

    private java.security.cert.X509Certificate mClientCertificate;

    private int mEapMethod = android.net.wifi.WifiEnterpriseConfig.Eap.NONE;

    private int mPhase2Method = android.net.wifi.WifiEnterpriseConfig.Phase2.NONE;

    private static final java.lang.String TAG = "WifiEnterpriseConfig";

    public WifiEnterpriseConfig() {
        // Do not set defaults so that the enterprise fields that are not changed
        // by API are not changed underneath
        // This is essential because an app may not have all fields like password
        // available. It allows modification of subset of fields.
    }

    /**
     * Copy constructor
     */
    public WifiEnterpriseConfig(android.net.wifi.WifiEnterpriseConfig source) {
        for (java.lang.String key : source.mFields.keySet()) {
            mFields.put(key, source.mFields.get(key));
        }
        mEapMethod = source.mEapMethod;
        mPhase2Method = source.mPhase2Method;
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeInt(mFields.size());
        for (java.util.Map.Entry<java.lang.String, java.lang.String> entry : mFields.entrySet()) {
            dest.writeString(entry.getKey());
            dest.writeString(entry.getValue());
        }
        dest.writeInt(mEapMethod);
        dest.writeInt(mPhase2Method);
        writeCertificates(dest, mCaCerts);
        if (mClientPrivateKey != null) {
            java.lang.String algorithm = mClientPrivateKey.getAlgorithm();
            byte[] userKeyBytes = mClientPrivateKey.getEncoded();
            dest.writeInt(userKeyBytes.length);
            dest.writeByteArray(userKeyBytes);
            dest.writeString(algorithm);
        } else {
            dest.writeInt(0);
        }
        writeCertificate(dest, mClientCertificate);
    }

    private void writeCertificates(android.os.Parcel dest, java.security.cert.X509Certificate[] cert) {
        if ((cert != null) && (cert.length != 0)) {
            dest.writeInt(cert.length);
            for (int i = 0; i < cert.length; i++) {
                writeCertificate(dest, cert[i]);
            }
        } else {
            dest.writeInt(0);
        }
    }

    private void writeCertificate(android.os.Parcel dest, java.security.cert.X509Certificate cert) {
        if (cert != null) {
            try {
                byte[] certBytes = cert.getEncoded();
                dest.writeInt(certBytes.length);
                dest.writeByteArray(certBytes);
            } catch (java.security.cert.CertificateEncodingException e) {
                dest.writeInt(0);
            }
        } else {
            dest.writeInt(0);
        }
    }

    public static final android.os.Parcelable.Creator<android.net.wifi.WifiEnterpriseConfig> CREATOR = new android.os.Parcelable.Creator<android.net.wifi.WifiEnterpriseConfig>() {
        public android.net.wifi.WifiEnterpriseConfig createFromParcel(android.os.Parcel in) {
            android.net.wifi.WifiEnterpriseConfig enterpriseConfig = new android.net.wifi.WifiEnterpriseConfig();
            int count = in.readInt();
            for (int i = 0; i < count; i++) {
                java.lang.String key = in.readString();
                java.lang.String value = in.readString();
                enterpriseConfig.mFields.put(key, value);
            }
            enterpriseConfig.mEapMethod = in.readInt();
            enterpriseConfig.mPhase2Method = in.readInt();
            enterpriseConfig.mCaCerts = readCertificates(in);
            java.security.PrivateKey userKey = null;
            int len = in.readInt();
            if (len > 0) {
                try {
                    byte[] bytes = new byte[len];
                    in.readByteArray(bytes);
                    java.lang.String algorithm = in.readString();
                    java.security.KeyFactory keyFactory = java.security.KeyFactory.getInstance(algorithm);
                    userKey = keyFactory.generatePrivate(new java.security.spec.PKCS8EncodedKeySpec(bytes));
                } catch (java.security.NoSuchAlgorithmException e) {
                    userKey = null;
                } catch (java.security.spec.InvalidKeySpecException e) {
                    userKey = null;
                }
            }
            enterpriseConfig.mClientPrivateKey = userKey;
            enterpriseConfig.mClientCertificate = readCertificate(in);
            return enterpriseConfig;
        }

        private java.security.cert.X509Certificate[] readCertificates(android.os.Parcel in) {
            java.security.cert.X509Certificate[] certs = null;
            int len = in.readInt();
            if (len > 0) {
                certs = new java.security.cert.X509Certificate[len];
                for (int i = 0; i < len; i++) {
                    certs[i] = readCertificate(in);
                }
            }
            return certs;
        }

        private java.security.cert.X509Certificate readCertificate(android.os.Parcel in) {
            java.security.cert.X509Certificate cert = null;
            int len = in.readInt();
            if (len > 0) {
                try {
                    byte[] bytes = new byte[len];
                    in.readByteArray(bytes);
                    java.security.cert.CertificateFactory cFactory = java.security.cert.CertificateFactory.getInstance("X.509");
                    cert = ((java.security.cert.X509Certificate) (cFactory.generateCertificate(new java.io.ByteArrayInputStream(bytes))));
                } catch (java.security.cert.CertificateException e) {
                    cert = null;
                }
            }
            return cert;
        }

        public android.net.wifi.WifiEnterpriseConfig[] newArray(int size) {
            return new android.net.wifi.WifiEnterpriseConfig[size];
        }
    };

    /**
     * The Extensible Authentication Protocol method used
     */
    public static final class Eap {
        /**
         * No EAP method used. Represents an empty config
         */
        public static final int NONE = -1;

        /**
         * Protected EAP
         */
        public static final int PEAP = 0;

        /**
         * EAP-Transport Layer Security
         */
        public static final int TLS = 1;

        /**
         * EAP-Tunneled Transport Layer Security
         */
        public static final int TTLS = 2;

        /**
         * EAP-Password
         */
        public static final int PWD = 3;

        /**
         * EAP-Subscriber Identity Module
         */
        public static final int SIM = 4;

        /**
         * EAP-Authentication and Key Agreement
         */
        public static final int AKA = 5;

        /**
         * EAP-Authentication and Key Agreement Prime
         */
        public static final int AKA_PRIME = 6;

        /**
         * Hotspot 2.0 r2 OSEN
         */
        public static final int UNAUTH_TLS = 7;

        /**
         *
         *
         * @unknown 
         */
        public static final java.lang.String[] strings = new java.lang.String[]{ "PEAP", "TLS", "TTLS", "PWD", "SIM", "AKA", "AKA'", "WFA-UNAUTH-TLS" };

        /**
         * Prevent initialization
         */
        private Eap() {
        }
    }

    /**
     * The inner authentication method used
     */
    public static final class Phase2 {
        public static final int NONE = 0;

        /**
         * Password Authentication Protocol
         */
        public static final int PAP = 1;

        /**
         * Microsoft Challenge Handshake Authentication Protocol
         */
        public static final int MSCHAP = 2;

        /**
         * Microsoft Challenge Handshake Authentication Protocol v2
         */
        public static final int MSCHAPV2 = 3;

        /**
         * Generic Token Card
         */
        public static final int GTC = 4;

        private static final java.lang.String AUTH_PREFIX = "auth=";

        private static final java.lang.String AUTHEAP_PREFIX = "autheap=";

        /**
         *
         *
         * @unknown 
         */
        public static final java.lang.String[] strings = new java.lang.String[]{ android.net.wifi.WifiEnterpriseConfig.EMPTY_VALUE, "PAP", "MSCHAP", "MSCHAPV2", "GTC" };

        /**
         * Prevent initialization
         */
        private Phase2() {
        }
    }

    // Loader and saver interfaces for exchanging data with wpa_supplicant.
    // TODO: Decouple this object (which is just a placeholder of the configuration)
    // from the implementation that knows what wpa_supplicant wants.
    /**
     * Interface used for retrieving supplicant configuration from WifiEnterpriseConfig
     *
     * @unknown 
     */
    public interface SupplicantSaver {
        /**
         * Set a value within wpa_supplicant configuration
         *
         * @param key
         * 		index to set within wpa_supplciant
         * @param value
         * 		the value for the key
         * @return true if successful; false otherwise
         */
        boolean saveValue(java.lang.String key, java.lang.String value);
    }

    /**
     * Interface used for populating a WifiEnterpriseConfig from supplicant configuration
     *
     * @unknown 
     */
    public interface SupplicantLoader {
        /**
         * Returns a value within wpa_supplicant configuration
         *
         * @param key
         * 		index to set within wpa_supplciant
         * @return string value if successful; null otherwise
         */
        java.lang.String loadValue(java.lang.String key);
    }

    /**
     * Internal use only; supply field values to wpa_supplicant config.  The configuration
     * process aborts on the first failed call on {@code saver}.
     *
     * @param saver
     * 		proxy for setting configuration in wpa_supplciant
     * @return whether the save succeeded on all attempts
     * @unknown 
     */
    public boolean saveToSupplicant(android.net.wifi.WifiEnterpriseConfig.SupplicantSaver saver) {
        if (!isEapMethodValid()) {
            return false;
        }
        // wpa_supplicant can update the anonymous identity for these kinds of networks after
        // framework reads them, so make sure the framework doesn't try to overwrite them.
        boolean shouldNotWriteAnonIdentity = ((mEapMethod == android.net.wifi.WifiEnterpriseConfig.Eap.SIM) || (mEapMethod == android.net.wifi.WifiEnterpriseConfig.Eap.AKA)) || (mEapMethod == android.net.wifi.WifiEnterpriseConfig.Eap.AKA_PRIME);
        for (java.lang.String key : mFields.keySet()) {
            if (shouldNotWriteAnonIdentity && android.net.wifi.WifiEnterpriseConfig.ANON_IDENTITY_KEY.equals(key)) {
                continue;
            }
            if (!saver.saveValue(key, mFields.get(key))) {
                return false;
            }
        }
        if (!saver.saveValue(android.net.wifi.WifiEnterpriseConfig.EAP_KEY, android.net.wifi.WifiEnterpriseConfig.Eap.strings[mEapMethod])) {
            return false;
        }
        if ((mEapMethod != android.net.wifi.WifiEnterpriseConfig.Eap.TLS) && (mPhase2Method != android.net.wifi.WifiEnterpriseConfig.Phase2.NONE)) {
            boolean is_autheap = (mEapMethod == android.net.wifi.WifiEnterpriseConfig.Eap.TTLS) && (mPhase2Method == android.net.wifi.WifiEnterpriseConfig.Phase2.GTC);
            java.lang.String prefix = (is_autheap) ? android.net.wifi.WifiEnterpriseConfig.Phase2.AUTHEAP_PREFIX : android.net.wifi.WifiEnterpriseConfig.Phase2.AUTH_PREFIX;
            java.lang.String value = convertToQuotedString(prefix + android.net.wifi.WifiEnterpriseConfig.Phase2.strings[mPhase2Method]);
            return saver.saveValue(android.net.wifi.WifiEnterpriseConfig.PHASE2_KEY, value);
        } else
            if (mPhase2Method == android.net.wifi.WifiEnterpriseConfig.Phase2.NONE) {
                // By default, send a null phase 2 to clear old configuration values.
                return saver.saveValue(android.net.wifi.WifiEnterpriseConfig.PHASE2_KEY, null);
            } else {
                android.util.Log.e(android.net.wifi.WifiEnterpriseConfig.TAG, "WiFi enterprise configuration is invalid as it supplies a " + "phase 2 method but the phase1 method does not support it.");
                return false;
            }

    }

    /**
     * Internal use only; retrieve configuration from wpa_supplicant config.
     *
     * @param loader
     * 		proxy for retrieving configuration keys from wpa_supplicant
     * @unknown 
     */
    public void loadFromSupplicant(android.net.wifi.WifiEnterpriseConfig.SupplicantLoader loader) {
        for (java.lang.String key : android.net.wifi.WifiEnterpriseConfig.SUPPLICANT_CONFIG_KEYS) {
            java.lang.String value = loader.loadValue(key);
            if (value == null) {
                mFields.put(key, android.net.wifi.WifiEnterpriseConfig.EMPTY_VALUE);
            } else {
                mFields.put(key, value);
            }
        }
        java.lang.String eapMethod = loader.loadValue(android.net.wifi.WifiEnterpriseConfig.EAP_KEY);
        mEapMethod = getStringIndex(android.net.wifi.WifiEnterpriseConfig.Eap.strings, eapMethod, android.net.wifi.WifiEnterpriseConfig.Eap.NONE);
        java.lang.String phase2Method = removeDoubleQuotes(loader.loadValue(android.net.wifi.WifiEnterpriseConfig.PHASE2_KEY));
        // Remove "auth=" or "autheap=" prefix.
        if (phase2Method.startsWith(android.net.wifi.WifiEnterpriseConfig.Phase2.AUTH_PREFIX)) {
            phase2Method = phase2Method.substring(android.net.wifi.WifiEnterpriseConfig.Phase2.AUTH_PREFIX.length());
        } else
            if (phase2Method.startsWith(android.net.wifi.WifiEnterpriseConfig.Phase2.AUTHEAP_PREFIX)) {
                phase2Method = phase2Method.substring(android.net.wifi.WifiEnterpriseConfig.Phase2.AUTHEAP_PREFIX.length());
            }

        mPhase2Method = getStringIndex(android.net.wifi.WifiEnterpriseConfig.Phase2.strings, phase2Method, android.net.wifi.WifiEnterpriseConfig.Phase2.NONE);
    }

    /**
     * Set the EAP authentication method.
     *
     * @param eapMethod
     * 		is one {@link Eap#PEAP}, {@link Eap#TLS}, {@link Eap#TTLS} or
     * 		{@link Eap#PWD}
     * @throws IllegalArgumentException
     * 		on an invalid eap method
     */
    public void setEapMethod(int eapMethod) {
        switch (eapMethod) {
            /**
             * Valid methods
             */
            case android.net.wifi.WifiEnterpriseConfig.Eap.TLS :
            case android.net.wifi.WifiEnterpriseConfig.Eap.UNAUTH_TLS :
                setPhase2Method(android.net.wifi.WifiEnterpriseConfig.Phase2.NONE);
                /* fall through */
            case android.net.wifi.WifiEnterpriseConfig.Eap.PEAP :
            case android.net.wifi.WifiEnterpriseConfig.Eap.PWD :
            case android.net.wifi.WifiEnterpriseConfig.Eap.TTLS :
            case android.net.wifi.WifiEnterpriseConfig.Eap.SIM :
            case android.net.wifi.WifiEnterpriseConfig.Eap.AKA :
            case android.net.wifi.WifiEnterpriseConfig.Eap.AKA_PRIME :
                mEapMethod = eapMethod;
                mFields.put(android.net.wifi.WifiEnterpriseConfig.OPP_KEY_CACHING, "1");
                break;
            default :
                throw new java.lang.IllegalArgumentException("Unknown EAP method");
        }
    }

    /**
     * Get the eap method.
     *
     * @return eap method configured
     */
    public int getEapMethod() {
        return mEapMethod;
    }

    /**
     * Set Phase 2 authentication method. Sets the inner authentication method to be used in
     * phase 2 after setting up a secure channel
     *
     * @param phase2Method
     * 		is the inner authentication method and can be one of {@link Phase2#NONE},
     * 		{@link Phase2#PAP}, {@link Phase2#MSCHAP}, {@link Phase2#MSCHAPV2},
     * 		{@link Phase2#GTC}
     * @throws IllegalArgumentException
     * 		on an invalid phase2 method
     */
    public void setPhase2Method(int phase2Method) {
        switch (phase2Method) {
            case android.net.wifi.WifiEnterpriseConfig.Phase2.NONE :
            case android.net.wifi.WifiEnterpriseConfig.Phase2.PAP :
            case android.net.wifi.WifiEnterpriseConfig.Phase2.MSCHAP :
            case android.net.wifi.WifiEnterpriseConfig.Phase2.MSCHAPV2 :
            case android.net.wifi.WifiEnterpriseConfig.Phase2.GTC :
                mPhase2Method = phase2Method;
                break;
            default :
                throw new java.lang.IllegalArgumentException("Unknown Phase 2 method");
        }
    }

    /**
     * Get the phase 2 authentication method.
     *
     * @return a phase 2 method defined at {@link Phase2}
     */
    public int getPhase2Method() {
        return mPhase2Method;
    }

    /**
     * Set the identity
     *
     * @param identity
     * 		
     */
    public void setIdentity(java.lang.String identity) {
        setFieldValue(android.net.wifi.WifiEnterpriseConfig.IDENTITY_KEY, identity, "");
    }

    /**
     * Get the identity
     *
     * @return the identity
     */
    public java.lang.String getIdentity() {
        return getFieldValue(android.net.wifi.WifiEnterpriseConfig.IDENTITY_KEY, "");
    }

    /**
     * Set anonymous identity. This is used as the unencrypted identity with
     * certain EAP types
     *
     * @param anonymousIdentity
     * 		the anonymous identity
     */
    public void setAnonymousIdentity(java.lang.String anonymousIdentity) {
        setFieldValue(android.net.wifi.WifiEnterpriseConfig.ANON_IDENTITY_KEY, anonymousIdentity, "");
    }

    /**
     * Get the anonymous identity
     *
     * @return anonymous identity
     */
    public java.lang.String getAnonymousIdentity() {
        return getFieldValue(android.net.wifi.WifiEnterpriseConfig.ANON_IDENTITY_KEY, "");
    }

    /**
     * Set the password.
     *
     * @param password
     * 		the password
     */
    public void setPassword(java.lang.String password) {
        setFieldValue(android.net.wifi.WifiEnterpriseConfig.PASSWORD_KEY, password, "");
    }

    /**
     * Get the password.
     *
     * Returns locally set password value. For networks fetched from
     * framework, returns "*".
     */
    public java.lang.String getPassword() {
        return getFieldValue(android.net.wifi.WifiEnterpriseConfig.PASSWORD_KEY, "");
    }

    /**
     * Encode a CA certificate alias so it does not contain illegal character.
     *
     * @unknown 
     */
    public static java.lang.String encodeCaCertificateAlias(java.lang.String alias) {
        byte[] bytes = alias.getBytes(java.nio.charset.StandardCharsets.UTF_8);
        java.lang.StringBuilder sb = new java.lang.StringBuilder(bytes.length * 2);
        for (byte o : bytes) {
            sb.append(java.lang.String.format("%02x", o & 0xff));
        }
        return sb.toString();
    }

    /**
     * Decode a previously-encoded CA certificate alias.
     *
     * @unknown 
     */
    public static java.lang.String decodeCaCertificateAlias(java.lang.String alias) {
        byte[] data = new byte[alias.length() >> 1];
        for (int n = 0, position = 0; n < alias.length(); n += 2 , position++) {
            data[position] = ((byte) (java.lang.Integer.parseInt(alias.substring(n, n + 2), 16)));
        }
        try {
            return new java.lang.String(data, java.nio.charset.StandardCharsets.UTF_8);
        } catch (java.lang.NumberFormatException e) {
            e.printStackTrace();
            return alias;
        }
    }

    /**
     * Set CA certificate alias.
     *
     * <p> See the {@link android.security.KeyChain} for details on installing or choosing
     * a certificate
     * </p>
     *
     * @param alias
     * 		identifies the certificate
     * @unknown 
     */
    public void setCaCertificateAlias(java.lang.String alias) {
        setFieldValue(android.net.wifi.WifiEnterpriseConfig.CA_CERT_KEY, alias, android.net.wifi.WifiEnterpriseConfig.CA_CERT_PREFIX);
    }

    /**
     * Set CA certificate aliases. When creating installing the corresponding certificate to
     * the keystore, please use alias encoded by {@link #encodeCaCertificateAlias(String)}.
     *
     * <p> See the {@link android.security.KeyChain} for details on installing or choosing
     * a certificate.
     * </p>
     *
     * @param aliases
     * 		identifies the certificate
     * @unknown 
     */
    public void setCaCertificateAliases(@android.annotation.Nullable
    java.lang.String[] aliases) {
        if (aliases == null) {
            setFieldValue(android.net.wifi.WifiEnterpriseConfig.CA_CERT_KEY, null, android.net.wifi.WifiEnterpriseConfig.CA_CERT_PREFIX);
        } else
            if (aliases.length == 1) {
                // Backwards compatibility: use the original cert prefix if setting only one alias.
                setCaCertificateAlias(aliases[0]);
            } else {
                // Use KEYSTORES_URI which supports multiple aliases.
                java.lang.StringBuilder sb = new java.lang.StringBuilder();
                for (int i = 0; i < aliases.length; i++) {
                    if (i > 0) {
                        sb.append(android.net.wifi.WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
                    }
                    sb.append(android.net.wifi.WifiEnterpriseConfig.encodeCaCertificateAlias(android.security.Credentials.CA_CERTIFICATE + aliases[i]));
                }
                setFieldValue(android.net.wifi.WifiEnterpriseConfig.CA_CERT_KEY, sb.toString(), android.net.wifi.WifiEnterpriseConfig.KEYSTORES_URI);
            }

    }

    /**
     * Get CA certificate alias
     *
     * @return alias to the CA certificate
     * @unknown 
     */
    public java.lang.String getCaCertificateAlias() {
        return getFieldValue(android.net.wifi.WifiEnterpriseConfig.CA_CERT_KEY, android.net.wifi.WifiEnterpriseConfig.CA_CERT_PREFIX);
    }

    /**
     * Get CA certificate aliases
     *
     * @return alias to the CA certificate
     * @unknown 
     */
    @android.annotation.Nullable
    public java.lang.String[] getCaCertificateAliases() {
        java.lang.String value = getFieldValue(android.net.wifi.WifiEnterpriseConfig.CA_CERT_KEY, "");
        if (value.startsWith(android.net.wifi.WifiEnterpriseConfig.CA_CERT_PREFIX)) {
            // Backwards compatibility: parse the original alias prefix.
            return new java.lang.String[]{ getFieldValue(android.net.wifi.WifiEnterpriseConfig.CA_CERT_KEY, android.net.wifi.WifiEnterpriseConfig.CA_CERT_PREFIX) };
        } else
            if (value.startsWith(android.net.wifi.WifiEnterpriseConfig.KEYSTORES_URI)) {
                java.lang.String values = value.substring(android.net.wifi.WifiEnterpriseConfig.KEYSTORES_URI.length());
                java.lang.String[] aliases = android.text.TextUtils.split(values, android.net.wifi.WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
                for (int i = 0; i < aliases.length; i++) {
                    aliases[i] = android.net.wifi.WifiEnterpriseConfig.decodeCaCertificateAlias(aliases[i]);
                    if (aliases[i].startsWith(android.security.Credentials.CA_CERTIFICATE)) {
                        aliases[i] = aliases[i].substring(android.security.Credentials.CA_CERTIFICATE.length());
                    }
                }
                return aliases.length != 0 ? aliases : null;
            } else {
                return android.text.TextUtils.isEmpty(value) ? null : new java.lang.String[]{ value };
            }

    }

    /**
     * Specify a X.509 certificate that identifies the server.
     *
     * <p>A default name is automatically assigned to the certificate and used
     * with this configuration. The framework takes care of installing the
     * certificate when the config is saved and removing the certificate when
     * the config is removed.
     *
     * @param cert
     * 		X.509 CA certificate
     * @throws IllegalArgumentException
     * 		if not a CA certificate
     */
    public void setCaCertificate(@android.annotation.Nullable
    java.security.cert.X509Certificate cert) {
        if (cert != null) {
            if (cert.getBasicConstraints() >= 0) {
                mCaCerts = new java.security.cert.X509Certificate[]{ cert };
            } else {
                throw new java.lang.IllegalArgumentException("Not a CA certificate");
            }
        } else {
            mCaCerts = null;
        }
    }

    /**
     * Get CA certificate. If multiple CA certificates are configured previously,
     * return the first one.
     *
     * @return X.509 CA certificate
     */
    @android.annotation.Nullable
    public java.security.cert.X509Certificate getCaCertificate() {
        if ((mCaCerts != null) && (mCaCerts.length > 0)) {
            return mCaCerts[0];
        } else {
            return null;
        }
    }

    /**
     * Specify a list of X.509 certificates that identifies the server. The validation
     * passes if the CA of server certificate matches one of the given certificates.
     *
     * <p>Default names are automatically assigned to the certificates and used
     * with this configuration. The framework takes care of installing the
     * certificates when the config is saved and removing the certificates when
     * the config is removed.
     *
     * @param certs
     * 		X.509 CA certificates
     * @throws IllegalArgumentException
     * 		if any of the provided certificates is
     * 		not a CA certificate
     */
    public void setCaCertificates(@android.annotation.Nullable
    java.security.cert.X509Certificate[] certs) {
        if (certs != null) {
            java.security.cert.X509Certificate[] newCerts = new java.security.cert.X509Certificate[certs.length];
            for (int i = 0; i < certs.length; i++) {
                if (certs[i].getBasicConstraints() >= 0) {
                    newCerts[i] = certs[i];
                } else {
                    throw new java.lang.IllegalArgumentException("Not a CA certificate");
                }
            }
            mCaCerts = newCerts;
        } else {
            mCaCerts = null;
        }
    }

    /**
     * Get CA certificates.
     */
    @android.annotation.Nullable
    public java.security.cert.X509Certificate[] getCaCertificates() {
        if ((mCaCerts != null) && (mCaCerts.length > 0)) {
            return mCaCerts;
        } else {
            return null;
        }
    }

    /**
     *
     *
     * @unknown 
     */
    public void resetCaCertificate() {
        mCaCerts = null;
    }

    /**
     * Set the ca_path directive on wpa_supplicant.
     *
     * From wpa_supplicant documentation:
     *
     * Directory path for CA certificate files (PEM). This path may contain
     * multiple CA certificates in OpenSSL format. Common use for this is to
     * point to system trusted CA list which is often installed into directory
     * like /etc/ssl/certs. If configured, these certificates are added to the
     * list of trusted CAs. ca_cert may also be included in that case, but it is
     * not required.
     *
     * @param domain
     * 		The path for CA certificate files
     * @unknown 
     */
    public void setCaPath(java.lang.String path) {
        setFieldValue(android.net.wifi.WifiEnterpriseConfig.CA_PATH_KEY, path);
    }

    /**
     * Get the domain_suffix_match value. See setDomSuffixMatch.
     *
     * @return The path for CA certificate files.
     * @unknown 
     */
    public java.lang.String getCaPath() {
        return getFieldValue(android.net.wifi.WifiEnterpriseConfig.CA_PATH_KEY, "");
    }

    /**
     * Set Client certificate alias.
     *
     * <p> See the {@link android.security.KeyChain} for details on installing or choosing
     * a certificate
     * </p>
     *
     * @param alias
     * 		identifies the certificate
     * @unknown 
     */
    public void setClientCertificateAlias(java.lang.String alias) {
        setFieldValue(android.net.wifi.WifiEnterpriseConfig.CLIENT_CERT_KEY, alias, android.net.wifi.WifiEnterpriseConfig.CLIENT_CERT_PREFIX);
        setFieldValue(android.net.wifi.WifiEnterpriseConfig.PRIVATE_KEY_ID_KEY, alias, android.security.Credentials.USER_PRIVATE_KEY);
        // Also, set engine parameters
        if (android.text.TextUtils.isEmpty(alias)) {
            mFields.put(android.net.wifi.WifiEnterpriseConfig.ENGINE_KEY, android.net.wifi.WifiEnterpriseConfig.ENGINE_DISABLE);
            mFields.put(android.net.wifi.WifiEnterpriseConfig.ENGINE_ID_KEY, android.net.wifi.WifiEnterpriseConfig.EMPTY_VALUE);
        } else {
            mFields.put(android.net.wifi.WifiEnterpriseConfig.ENGINE_KEY, android.net.wifi.WifiEnterpriseConfig.ENGINE_ENABLE);
            mFields.put(android.net.wifi.WifiEnterpriseConfig.ENGINE_ID_KEY, convertToQuotedString(android.net.wifi.WifiEnterpriseConfig.ENGINE_ID_KEYSTORE));
        }
    }

    /**
     * Get client certificate alias
     *
     * @return alias to the client certificate
     * @unknown 
     */
    public java.lang.String getClientCertificateAlias() {
        return getFieldValue(android.net.wifi.WifiEnterpriseConfig.CLIENT_CERT_KEY, android.net.wifi.WifiEnterpriseConfig.CLIENT_CERT_PREFIX);
    }

    /**
     * Specify a private key and client certificate for client authorization.
     *
     * <p>A default name is automatically assigned to the key entry and used
     * with this configuration.  The framework takes care of installing the
     * key entry when the config is saved and removing the key entry when
     * the config is removed.
     *
     * @param privateKey
     * 		
     * @param clientCertificate
     * 		
     * @throws IllegalArgumentException
     * 		for an invalid key or certificate.
     */
    public void setClientKeyEntry(java.security.PrivateKey privateKey, java.security.cert.X509Certificate clientCertificate) {
        if (clientCertificate != null) {
            if (clientCertificate.getBasicConstraints() != (-1)) {
                throw new java.lang.IllegalArgumentException("Cannot be a CA certificate");
            }
            if (privateKey == null) {
                throw new java.lang.IllegalArgumentException("Client cert without a private key");
            }
            if (privateKey.getEncoded() == null) {
                throw new java.lang.IllegalArgumentException("Private key cannot be encoded");
            }
        }
        mClientPrivateKey = privateKey;
        mClientCertificate = clientCertificate;
    }

    /**
     * Get client certificate
     *
     * @return X.509 client certificate
     */
    public java.security.cert.X509Certificate getClientCertificate() {
        return mClientCertificate;
    }

    /**
     *
     *
     * @unknown 
     */
    public void resetClientKeyEntry() {
        mClientPrivateKey = null;
        mClientCertificate = null;
    }

    /**
     *
     *
     * @unknown 
     */
    public java.security.PrivateKey getClientPrivateKey() {
        return mClientPrivateKey;
    }

    /**
     * Set subject match (deprecated). This is the substring to be matched against the subject of
     * the authentication server certificate.
     *
     * @param subjectMatch
     * 		substring to be matched
     * @deprecated in favor of altSubjectMatch
     */
    public void setSubjectMatch(java.lang.String subjectMatch) {
        setFieldValue(android.net.wifi.WifiEnterpriseConfig.SUBJECT_MATCH_KEY, subjectMatch, "");
    }

    /**
     * Get subject match (deprecated)
     *
     * @return the subject match string
     * @deprecated in favor of altSubjectMatch
     */
    public java.lang.String getSubjectMatch() {
        return getFieldValue(android.net.wifi.WifiEnterpriseConfig.SUBJECT_MATCH_KEY, "");
    }

    /**
     * Set alternate subject match. This is the substring to be matched against the
     * alternate subject of the authentication server certificate.
     *
     * @param altSubjectMatch
     * 		substring to be matched, for example
     * 		DNS:server.example.com;EMAIL:server@example.com
     */
    public void setAltSubjectMatch(java.lang.String altSubjectMatch) {
        setFieldValue(android.net.wifi.WifiEnterpriseConfig.ALTSUBJECT_MATCH_KEY, altSubjectMatch, "");
    }

    /**
     * Get alternate subject match
     *
     * @return the alternate subject match string
     */
    public java.lang.String getAltSubjectMatch() {
        return getFieldValue(android.net.wifi.WifiEnterpriseConfig.ALTSUBJECT_MATCH_KEY, "");
    }

    /**
     * Set the domain_suffix_match directive on wpa_supplicant. This is the parameter to use
     * for Hotspot 2.0 defined matching of AAA server certs per WFA HS2.0 spec, section 7.3.3.2,
     * second paragraph.
     *
     * From wpa_supplicant documentation:
     * Constraint for server domain name. If set, this FQDN is used as a suffix match requirement
     * for the AAAserver certificate in SubjectAltName dNSName element(s). If a matching dNSName is
     * found, this constraint is met. If no dNSName values are present, this constraint is matched
     * against SubjectName CN using same suffix match comparison.
     * Suffix match here means that the host/domain name is compared one label at a time starting
     * from the top-level domain and all the labels in domain_suffix_match shall be included in the
     * certificate. The certificate may include additional sub-level labels in addition to the
     * required labels.
     * For example, domain_suffix_match=example.com would match test.example.com but would not
     * match test-example.com.
     *
     * @param domain
     * 		The domain value
     */
    public void setDomainSuffixMatch(java.lang.String domain) {
        setFieldValue(android.net.wifi.WifiEnterpriseConfig.DOM_SUFFIX_MATCH_KEY, domain);
    }

    /**
     * Get the domain_suffix_match value. See setDomSuffixMatch.
     *
     * @return The domain value.
     */
    public java.lang.String getDomainSuffixMatch() {
        return getFieldValue(android.net.wifi.WifiEnterpriseConfig.DOM_SUFFIX_MATCH_KEY, "");
    }

    /**
     * Set realm for passpoint credential; realm identifies a set of networks where your
     * passpoint credential can be used
     *
     * @param realm
     * 		the realm
     */
    public void setRealm(java.lang.String realm) {
        setFieldValue(android.net.wifi.WifiEnterpriseConfig.REALM_KEY, realm, "");
    }

    /**
     * Get realm for passpoint credential; see {@link #setRealm(String)} for more information
     *
     * @return the realm
     */
    public java.lang.String getRealm() {
        return getFieldValue(android.net.wifi.WifiEnterpriseConfig.REALM_KEY, "");
    }

    /**
     * Set plmn (Public Land Mobile Network) of the provider of passpoint credential
     *
     * @param plmn
     * 		the plmn value derived from mcc (mobile country code) & mnc (mobile network code)
     */
    public void setPlmn(java.lang.String plmn) {
        setFieldValue(android.net.wifi.WifiEnterpriseConfig.PLMN_KEY, plmn, "");
    }

    /**
     * Get plmn (Public Land Mobile Network) for passpoint credential; see {@link #setPlmn
     * (String)} for more information
     *
     * @return the plmn
     */
    public java.lang.String getPlmn() {
        return getFieldValue(android.net.wifi.WifiEnterpriseConfig.PLMN_KEY, "");
    }

    /**
     * See {@link WifiConfiguration#getKeyIdForCredentials} @hide
     */
    public java.lang.String getKeyId(android.net.wifi.WifiEnterpriseConfig current) {
        // If EAP method is not initialized, use current config details
        if (mEapMethod == android.net.wifi.WifiEnterpriseConfig.Eap.NONE) {
            return current != null ? current.getKeyId(null) : android.net.wifi.WifiEnterpriseConfig.EMPTY_VALUE;
        }
        if (!isEapMethodValid()) {
            return android.net.wifi.WifiEnterpriseConfig.EMPTY_VALUE;
        }
        return (android.net.wifi.WifiEnterpriseConfig.Eap.strings[mEapMethod] + "_") + android.net.wifi.WifiEnterpriseConfig.Phase2.strings[mPhase2Method];
    }

    private java.lang.String removeDoubleQuotes(java.lang.String string) {
        if (android.text.TextUtils.isEmpty(string))
            return "";

        int length = string.length();
        if (((length > 1) && (string.charAt(0) == '"')) && (string.charAt(length - 1) == '"')) {
            return string.substring(1, length - 1);
        }
        return string;
    }

    private java.lang.String convertToQuotedString(java.lang.String string) {
        return ("\"" + string) + "\"";
    }

    /**
     * Returns the index at which the toBeFound string is found in the array.
     *
     * @param arr
     * 		array of strings
     * @param toBeFound
     * 		string to be found
     * @param defaultIndex
     * 		default index to be returned when string is not found
     * @return the index into array
     */
    private int getStringIndex(java.lang.String[] arr, java.lang.String toBeFound, int defaultIndex) {
        if (android.text.TextUtils.isEmpty(toBeFound))
            return defaultIndex;

        for (int i = 0; i < arr.length; i++) {
            if (toBeFound.equals(arr[i]))
                return i;

        }
        return defaultIndex;
    }

    /**
     * Returns the field value for the key.
     *
     * @param key
     * 		into the hash
     * @param prefix
     * 		is the prefix that the value may have
     * @return value
     * @unknown 
     */
    public java.lang.String getFieldValue(java.lang.String key, java.lang.String prefix) {
        // TODO: Should raise an exception if |key| is EAP_KEY or PHASE2_KEY since
        // neither of these keys should be retrieved in this manner.
        java.lang.String value = mFields.get(key);
        // Uninitialized or known to be empty after reading from supplicant
        if (android.text.TextUtils.isEmpty(value) || android.net.wifi.WifiEnterpriseConfig.EMPTY_VALUE.equals(value))
            return "";

        value = removeDoubleQuotes(value);
        if (value.startsWith(prefix)) {
            return value.substring(prefix.length());
        } else {
            return value;
        }
    }

    /**
     * Set a value with an optional prefix at key
     *
     * @param key
     * 		into the hash
     * @param value
     * 		to be set
     * @param prefix
     * 		an optional value to be prefixed to actual value
     * @unknown 
     */
    public void setFieldValue(java.lang.String key, java.lang.String value, java.lang.String prefix) {
        // TODO: Should raise an exception if |key| is EAP_KEY or PHASE2_KEY since
        // neither of these keys should be set in this manner.
        if (android.text.TextUtils.isEmpty(value)) {
            mFields.put(key, android.net.wifi.WifiEnterpriseConfig.EMPTY_VALUE);
        } else {
            mFields.put(key, convertToQuotedString(prefix + value));
        }
    }

    /**
     * Set a value with an optional prefix at key
     *
     * @param key
     * 		into the hash
     * @param value
     * 		to be set
     * @param prefix
     * 		an optional value to be prefixed to actual value
     * @unknown 
     */
    public void setFieldValue(java.lang.String key, java.lang.String value) {
        // TODO: Should raise an exception if |key| is EAP_KEY or PHASE2_KEY since
        // neither of these keys should be set in this manner.
        if (android.text.TextUtils.isEmpty(value)) {
            mFields.put(key, android.net.wifi.WifiEnterpriseConfig.EMPTY_VALUE);
        } else {
            mFields.put(key, convertToQuotedString(value));
        }
    }

    @java.lang.Override
    public java.lang.String toString() {
        java.lang.StringBuffer sb = new java.lang.StringBuffer();
        for (java.lang.String key : mFields.keySet()) {
            // Don't display password in toString().
            java.lang.String value = (android.net.wifi.WifiEnterpriseConfig.PASSWORD_KEY.equals(key)) ? "<removed>" : mFields.get(key);
            sb.append(key).append(" ").append(value).append("\n");
        }
        return sb.toString();
    }

    /**
     * Returns whether the EAP method data is valid, i.e., whether mEapMethod and mPhase2Method
     * are valid indices into {@code Eap.strings[]} and {@code Phase2.strings[]} respectively.
     */
    private boolean isEapMethodValid() {
        if (mEapMethod == android.net.wifi.WifiEnterpriseConfig.Eap.NONE) {
            android.util.Log.e(android.net.wifi.WifiEnterpriseConfig.TAG, "WiFi enterprise configuration is invalid as it supplies no EAP method.");
            return false;
        }
        if ((mEapMethod < 0) || (mEapMethod >= android.net.wifi.WifiEnterpriseConfig.Eap.strings.length)) {
            android.util.Log.e(android.net.wifi.WifiEnterpriseConfig.TAG, "mEapMethod is invald for WiFi enterprise configuration: " + mEapMethod);
            return false;
        }
        if ((mPhase2Method < 0) || (mPhase2Method >= android.net.wifi.WifiEnterpriseConfig.Phase2.strings.length)) {
            android.util.Log.e(android.net.wifi.WifiEnterpriseConfig.TAG, "mPhase2Method is invald for WiFi enterprise configuration: " + mPhase2Method);
            return false;
        }
        return true;
    }
}

