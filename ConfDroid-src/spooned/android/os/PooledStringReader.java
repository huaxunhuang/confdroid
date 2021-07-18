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
package android.os;


/**
 * Helper class for reading pooling strings from a Parcel.  It must be used
 * in conjunction with {@link android.os.PooledStringWriter}.  This really needs
 * to be pushed in to Parcel itself, but doing that is...  complicated.
 *
 * @unknown 
 */
public class PooledStringReader {
    private final android.os.Parcel mIn;

    /**
     * The pool of strings we have collected so far.
     */
    private final java.lang.String[] mPool;

    public PooledStringReader(android.os.Parcel in) {
        mIn = in;
        final int size = in.readInt();
        mPool = new java.lang.String[size];
    }

    public int getStringCount() {
        return mPool.length;
    }

    public java.lang.String readString() {
        int idx = mIn.readInt();
        if (idx >= 0) {
            return mPool[idx];
        } else {
            idx = (-idx) - 1;
            java.lang.String str = mIn.readString();
            mPool[idx] = str;
            return str;
        }
    }
}

