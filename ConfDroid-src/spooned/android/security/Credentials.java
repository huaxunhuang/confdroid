/**
 * Copyright (C) 2009 The Android Open Source Project
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
package android.security;


/**
 * {@hide }
 */
public class Credentials {
    private static final java.lang.String LOGTAG = "Credentials";

    public static final java.lang.String INSTALL_ACTION = "android.credentials.INSTALL";

    public static final java.lang.String INSTALL_AS_USER_ACTION = "android.credentials.INSTALL_AS_USER";

    public static final java.lang.String UNLOCK_ACTION = "com.android.credentials.UNLOCK";

    /**
     * Key prefix for CA certificates.
     */
    public static final java.lang.String CA_CERTIFICATE = "CACERT_";

    /**
     * Key prefix for user certificates.
     */
    public static final java.lang.String USER_CERTIFICATE = "USRCERT_";

    /**
     * Key prefix for user private keys.
     */
    public static final java.lang.String USER_PRIVATE_KEY = "USRPKEY_";

    /**
     * Key prefix for user secret keys.
     */
    public static final java.lang.String USER_SECRET_KEY = "USRSKEY_";

    /**
     * Key prefix for VPN.
     */
    public static final java.lang.String VPN = "VPN_";

    /**
     * Key prefix for WIFI.
     */
    public static final java.lang.String WIFI = "WIFI_";

    /**
     * Key containing suffix of lockdown VPN profile.
     */
    public static final java.lang.String LOCKDOWN_VPN = "LOCKDOWN_VPN";

    /**
     * Data type for public keys.
     */
    public static final java.lang.String EXTRA_PUBLIC_KEY = "KEY";

    /**
     * Data type for private keys.
     */
    public static final java.lang.String EXTRA_PRIVATE_KEY = "PKEY";

    // historically used by Android
    public static final java.lang.String EXTENSION_CRT = ".crt";

    public static final java.lang.String EXTENSION_P12 = ".p12";

    // commonly used on Windows
    public static final java.lang.String EXTENSION_CER = ".cer";

    public static final java.lang.String EXTENSION_PFX = ".pfx";

    /**
     * Intent extra: install the certificate bundle as this UID instead of
     * system.
     */
    public static final java.lang.String EXTRA_INSTALL_AS_UID = "install_as_uid";

    /**
     * Intent extra: name for the user's private key.
     */
    public static final java.lang.String EXTRA_USER_PRIVATE_KEY_NAME = "user_private_key_name";

    /**
     * Intent extra: data for the user's private key in PEM-encoded PKCS#8.
     */
    public static final java.lang.String EXTRA_USER_PRIVATE_KEY_DATA = "user_private_key_data";

    /**
     * Intent extra: name for the user's certificate.
     */
    public static final java.lang.String EXTRA_USER_CERTIFICATE_NAME = "user_certificate_name";

    /**
     * Intent extra: data for the user's certificate in PEM-encoded X.509.
     */
    public static final java.lang.String EXTRA_USER_CERTIFICATE_DATA = "user_certificate_data";

    /**
     * Intent extra: name for CA certificate chain
     */
    public static final java.lang.String EXTRA_CA_CERTIFICATES_NAME = "ca_certificates_name";

    /**
     * Intent extra: data for CA certificate chain in PEM-encoded X.509.
     */
    public static final java.lang.String EXTRA_CA_CERTIFICATES_DATA = "ca_certificates_data";

    /**
     * Convert objects to a PEM format which is used for
     * CA_CERTIFICATE and USER_CERTIFICATE entries.
     */
    public static byte[] convertToPem(java.security.cert.Certificate... objects) throws java.io.IOException, java.security.cert.CertificateEncodingException {
        java.io.ByteArrayOutputStream bao = new java.io.ByteArrayOutputStream();
        java.io.Writer writer = new java.io.OutputStreamWriter(bao, java.nio.charset.StandardCharsets.US_ASCII);
        com.android.org.bouncycastle.util.io.pem.PemWriter pw = new com.android.org.bouncycastle.util.io.pem.PemWriter(writer);
        for (java.security.cert.Certificate o : objects) {
            pw.writeObject(new com.android.org.bouncycastle.util.io.pem.PemObject("CERTIFICATE", o.getEncoded()));
        }
        pw.close();
        return bao.toByteArray();
    }

    /**
     * Convert objects from PEM format, which is used for
     * CA_CERTIFICATE and USER_CERTIFICATE entries.
     */
    public static java.util.List<java.security.cert.X509Certificate> convertFromPem(byte[] bytes) throws java.io.IOException, java.security.cert.CertificateException {
        java.io.ByteArrayInputStream bai = new java.io.ByteArrayInputStream(bytes);
        java.io.Reader reader = new java.io.InputStreamReader(bai, java.nio.charset.StandardCharsets.US_ASCII);
        com.android.org.bouncycastle.util.io.pem.PemReader pr = new com.android.org.bouncycastle.util.io.pem.PemReader(reader);
        try {
            java.security.cert.CertificateFactory cf = java.security.cert.CertificateFactory.getInstance("X509");
            java.util.List<java.security.cert.X509Certificate> result = new java.util.ArrayList<java.security.cert.X509Certificate>();
            com.android.org.bouncycastle.util.io.pem.PemObject o;
            while ((o = pr.readPemObject()) != null) {
                if (o.getType().equals("CERTIFICATE")) {
                    java.security.cert.Certificate c = cf.generateCertificate(new java.io.ByteArrayInputStream(o.getContent()));
                    result.add(((java.security.cert.X509Certificate) (c)));
                } else {
                    throw new java.lang.IllegalArgumentException("Unknown type " + o.getType());
                }
            } 
            return result;
        } finally {
            pr.close();
        }
    }

    private static android.security.Credentials singleton;

    public static android.security.Credentials getInstance() {
        if (android.security.Credentials.singleton == null) {
            android.security.Credentials.singleton = new android.security.Credentials();
        }
        return android.security.Credentials.singleton;
    }

    public void unlock(android.content.Context context) {
        try {
            android.content.Intent intent = new android.content.Intent(android.security.Credentials.UNLOCK_ACTION);
            context.startActivity(intent);
        } catch (android.content.ActivityNotFoundException e) {
            android.util.Log.w(android.security.Credentials.LOGTAG, e.toString());
        }
    }

    public void install(android.content.Context context) {
        try {
            android.content.Intent intent = android.security.KeyChain.createInstallIntent();
            context.startActivity(intent);
        } catch (android.content.ActivityNotFoundException e) {
            android.util.Log.w(android.security.Credentials.LOGTAG, e.toString());
        }
    }

    public void install(android.content.Context context, java.security.KeyPair pair) {
        try {
            android.content.Intent intent = android.security.KeyChain.createInstallIntent();
            intent.putExtra(android.security.Credentials.EXTRA_PRIVATE_KEY, pair.getPrivate().getEncoded());
            intent.putExtra(android.security.Credentials.EXTRA_PUBLIC_KEY, pair.getPublic().getEncoded());
            context.startActivity(intent);
        } catch (android.content.ActivityNotFoundException e) {
            android.util.Log.w(android.security.Credentials.LOGTAG, e.toString());
        }
    }

    public void install(android.content.Context context, java.lang.String type, byte[] value) {
        try {
            android.content.Intent intent = android.security.KeyChain.createInstallIntent();
            intent.putExtra(type, value);
            context.startActivity(intent);
        } catch (android.content.ActivityNotFoundException e) {
            android.util.Log.w(android.security.Credentials.LOGTAG, e.toString());
        }
    }

    /**
     * Delete all types (private key, user certificate, CA certificate) for a
     * particular {@code alias}. All three can exist for any given alias.
     * Returns {@code true} if the alias no longer contains any types.
     */
    public static boolean deleteAllTypesForAlias(android.security.KeyStore keystore, java.lang.String alias) {
        return android.security.Credentials.deleteAllTypesForAlias(keystore, alias, android.security.KeyStore.UID_SELF);
    }

    /**
     * Delete all types (private key, user certificate, CA certificate) for a
     * particular {@code alias}. All three can exist for any given alias.
     * Returns {@code true} if the alias no longer contains any types.
     */
    public static boolean deleteAllTypesForAlias(android.security.KeyStore keystore, java.lang.String alias, int uid) {
        /* Make sure every type is deleted. There can be all three types, so
        don't use a conditional here.
         */
        return (android.security.Credentials.deletePrivateKeyTypeForAlias(keystore, alias, uid) & android.security.Credentials.deleteSecretKeyTypeForAlias(keystore, alias, uid)) & android.security.Credentials.deleteCertificateTypesForAlias(keystore, alias, uid);
    }

    /**
     * Delete certificate types (user certificate, CA certificate) for a
     * particular {@code alias}. Both can exist for any given alias.
     * Returns {@code true} if the alias no longer contains either type.
     */
    public static boolean deleteCertificateTypesForAlias(android.security.KeyStore keystore, java.lang.String alias) {
        return android.security.Credentials.deleteCertificateTypesForAlias(keystore, alias, android.security.KeyStore.UID_SELF);
    }

    /**
     * Delete certificate types (user certificate, CA certificate) for a
     * particular {@code alias}. Both can exist for any given alias.
     * Returns {@code true} if the alias no longer contains either type.
     */
    public static boolean deleteCertificateTypesForAlias(android.security.KeyStore keystore, java.lang.String alias, int uid) {
        /* Make sure every certificate type is deleted. There can be two types,
        so don't use a conditional here.
         */
        return keystore.delete(android.security.Credentials.USER_CERTIFICATE + alias, uid) & keystore.delete(android.security.Credentials.CA_CERTIFICATE + alias, uid);
    }

    /**
     * Delete private key for a particular {@code alias}.
     * Returns {@code true} if the entry no longer exists.
     */
    static boolean deletePrivateKeyTypeForAlias(android.security.KeyStore keystore, java.lang.String alias) {
        return android.security.Credentials.deletePrivateKeyTypeForAlias(keystore, alias, android.security.KeyStore.UID_SELF);
    }

    /**
     * Delete private key for a particular {@code alias}.
     * Returns {@code true} if the entry no longer exists.
     */
    static boolean deletePrivateKeyTypeForAlias(android.security.KeyStore keystore, java.lang.String alias, int uid) {
        return keystore.delete(android.security.Credentials.USER_PRIVATE_KEY + alias, uid);
    }

    /**
     * Delete secret key for a particular {@code alias}.
     * Returns {@code true} if the entry no longer exists.
     */
    public static boolean deleteSecretKeyTypeForAlias(android.security.KeyStore keystore, java.lang.String alias) {
        return android.security.Credentials.deleteSecretKeyTypeForAlias(keystore, alias, android.security.KeyStore.UID_SELF);
    }

    /**
     * Delete secret key for a particular {@code alias}.
     * Returns {@code true} if the entry no longer exists.
     */
    public static boolean deleteSecretKeyTypeForAlias(android.security.KeyStore keystore, java.lang.String alias, int uid) {
        return keystore.delete(android.security.Credentials.USER_SECRET_KEY + alias, uid);
    }
}

