/**
 * Copyright (C) 2012 The Android Open Source Project
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
package android.content.pm;


/**
 * Represents a {@code KeySet} that has been declared in the AndroidManifest.xml
 * file for the application.  A {@code KeySet} can be used explicitly to
 * represent a trust relationship with other applications on the device.
 *
 * @unknown 
 */
public class KeySet implements android.os.Parcelable {
    private android.os.IBinder token;

    /**
     *
     *
     * @unknown 
     */
    public KeySet(android.os.IBinder token) {
        if (token == null) {
            throw new java.lang.NullPointerException("null value for KeySet IBinder token");
        }
        this.token = token;
    }

    /**
     *
     *
     * @unknown 
     */
    public android.os.IBinder getToken() {
        return token;
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (o instanceof android.content.pm.KeySet) {
            android.content.pm.KeySet ks = ((android.content.pm.KeySet) (o));
            return token == ks.token;
        }
        return false;
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public int hashCode() {
        return token.hashCode();
    }

    /**
     * Implement Parcelable
     *
     * @unknown 
     */
    @android.annotation.NonNull
    public static final android.os.Parcelable.Creator<android.content.pm.KeySet> CREATOR = new android.os.Parcelable.Creator<android.content.pm.KeySet>() {
        /**
         * Create a KeySet from a Parcel
         *
         * @param in
         * 		The parcel containing the KeySet
         */
        public android.content.pm.KeySet createFromParcel(android.os.Parcel source) {
            return readFromParcel(source);
        }

        /**
         * Create an array of null KeySets
         */
        public android.content.pm.KeySet[] newArray(int size) {
            return new android.content.pm.KeySet[size];
        }
    };

    /**
     *
     *
     * @unknown 
     */
    private static android.content.pm.KeySet readFromParcel(android.os.Parcel in) {
        android.os.IBinder token = in.readStrongBinder();
        return new android.content.pm.KeySet(token);
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public void writeToParcel(android.os.Parcel out, int flags) {
        out.writeStrongBinder(token);
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public int describeContents() {
        return 0;
    }
}

