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
 * {@link PublicKey} backed by Android Keystore.
 *
 * @unknown 
 */
public class AndroidKeyStorePublicKey extends android.security.keystore.AndroidKeyStoreKey implements java.security.PublicKey {
    private final byte[] mEncoded;

    public AndroidKeyStorePublicKey(java.lang.String alias, int uid, java.lang.String algorithm, byte[] x509EncodedForm) {
        super(alias, uid, algorithm);
        mEncoded = android.security.keystore.ArrayUtils.cloneIfNotEmpty(x509EncodedForm);
    }

    @java.lang.Override
    public java.lang.String getFormat() {
        return "X.509";
    }

    @java.lang.Override
    public byte[] getEncoded() {
        return android.security.keystore.ArrayUtils.cloneIfNotEmpty(mEncoded);
    }

    @java.lang.Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = (prime * result) + java.util.Arrays.hashCode(mEncoded);
        return result;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        android.security.keystore.AndroidKeyStorePublicKey other = ((android.security.keystore.AndroidKeyStorePublicKey) (obj));
        if (!java.util.Arrays.equals(mEncoded, other.mEncoded)) {
            return false;
        }
        return true;
    }
}

