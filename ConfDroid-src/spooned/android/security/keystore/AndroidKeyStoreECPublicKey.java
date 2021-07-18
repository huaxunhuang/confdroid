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
 * {@link ECPublicKey} backed by keystore.
 *
 * @unknown 
 */
public class AndroidKeyStoreECPublicKey extends android.security.keystore.AndroidKeyStorePublicKey implements java.security.interfaces.ECPublicKey {
    private final java.security.spec.ECParameterSpec mParams;

    private final java.security.spec.ECPoint mW;

    public AndroidKeyStoreECPublicKey(java.lang.String alias, int uid, byte[] x509EncodedForm, java.security.spec.ECParameterSpec params, java.security.spec.ECPoint w) {
        super(alias, uid, android.security.keystore.KeyProperties.KEY_ALGORITHM_EC, x509EncodedForm);
        mParams = params;
        mW = w;
    }

    public AndroidKeyStoreECPublicKey(java.lang.String alias, int uid, java.security.interfaces.ECPublicKey info) {
        this(alias, uid, info.getEncoded(), info.getParams(), info.getW());
        if (!"X.509".equalsIgnoreCase(info.getFormat())) {
            throw new java.lang.IllegalArgumentException("Unsupported key export format: " + info.getFormat());
        }
    }

    @java.lang.Override
    public java.security.spec.ECParameterSpec getParams() {
        return mParams;
    }

    @java.lang.Override
    public java.security.spec.ECPoint getW() {
        return mW;
    }
}

