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
 * RSA private key (instance of {@link PrivateKey} and {@link RSAKey}) backed by keystore.
 *
 * @unknown 
 */
public class AndroidKeyStoreRSAPrivateKey extends android.security.keystore.AndroidKeyStorePrivateKey implements java.security.interfaces.RSAKey {
    private final java.math.BigInteger mModulus;

    public AndroidKeyStoreRSAPrivateKey(java.lang.String alias, int uid, java.math.BigInteger modulus) {
        super(alias, uid, android.security.keystore.KeyProperties.KEY_ALGORITHM_RSA);
        mModulus = modulus;
    }

    @java.lang.Override
    public java.math.BigInteger getModulus() {
        return mModulus;
    }
}

