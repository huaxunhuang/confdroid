/**
 * Copyright (C) 2015 The Android Open Source Project
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
package android.security.keystore;


/**
 * {@link RSAPublicKey} backed by Android Keystore.
 *
 * @unknown 
 */
public class AndroidKeyStoreRSAPublicKey extends android.security.keystore.AndroidKeyStorePublicKey implements java.security.interfaces.RSAPublicKey {
    private final java.math.BigInteger mModulus;

    private final java.math.BigInteger mPublicExponent;

    public AndroidKeyStoreRSAPublicKey(java.lang.String alias, int uid, byte[] x509EncodedForm, java.math.BigInteger modulus, java.math.BigInteger publicExponent) {
        super(alias, uid, android.security.keystore.KeyProperties.KEY_ALGORITHM_RSA, x509EncodedForm);
        mModulus = modulus;
        mPublicExponent = publicExponent;
    }

    public AndroidKeyStoreRSAPublicKey(java.lang.String alias, int uid, java.security.interfaces.RSAPublicKey info) {
        this(alias, uid, info.getEncoded(), info.getModulus(), info.getPublicExponent());
        if (!"X.509".equalsIgnoreCase(info.getFormat())) {
            throw new java.lang.IllegalArgumentException("Unsupported key export format: " + info.getFormat());
        }
    }

    @java.lang.Override
    public java.math.BigInteger getModulus() {
        return mModulus;
    }

    @java.lang.Override
    public java.math.BigInteger getPublicExponent() {
        return mPublicExponent;
    }
}

