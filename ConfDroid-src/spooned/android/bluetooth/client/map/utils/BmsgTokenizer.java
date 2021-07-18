/**
 * Copyright (C) 2014 The Android Open Source Project
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
package android.bluetooth.client.map.utils;


public final class BmsgTokenizer {
    private final java.lang.String mStr;

    private final java.util.regex.Matcher mMatcher;

    private int mPos = 0;

    private final int mOffset;

    public static class Property {
        public final java.lang.String name;

        public final java.lang.String value;

        public Property(java.lang.String name, java.lang.String value) {
            if ((name == null) || (value == null)) {
                throw new java.lang.IllegalArgumentException();
            }
            this.name = name;
            this.value = value;
            android.util.Log.v("BMSG >> ", toString());
        }

        @java.lang.Override
        public java.lang.String toString() {
            return (name + ":") + value;
        }

        @java.lang.Override
        public boolean equals(java.lang.Object o) {
            return ((o instanceof android.bluetooth.client.map.utils.BmsgTokenizer.Property) && ((android.bluetooth.client.map.utils.BmsgTokenizer.Property) (o)).name.equals(name)) && ((android.bluetooth.client.map.utils.BmsgTokenizer.Property) (o)).value.equals(value);
        }
    }

    public BmsgTokenizer(java.lang.String str) {
        this(str, 0);
    }

    public BmsgTokenizer(java.lang.String str, int offset) {
        mStr = str;
        mOffset = offset;
        mMatcher = java.util.regex.Pattern.compile("(([^:]*):(.*))?\r\n").matcher(str);
        mPos = mMatcher.regionStart();
    }

    public android.bluetooth.client.map.utils.BmsgTokenizer.Property next(boolean alwaysReturn) throws java.text.ParseException {
        boolean found = false;
        do {
            mMatcher.region(mPos, mMatcher.regionEnd());
            if (!mMatcher.lookingAt()) {
                if (alwaysReturn) {
                    return null;
                }
                throw new java.text.ParseException("Property or empty line expected", pos());
            }
            mPos = mMatcher.end();
            if (mMatcher.group(1) != null) {
                found = true;
            }
        } while (!found );
        return new android.bluetooth.client.map.utils.BmsgTokenizer.Property(mMatcher.group(2), mMatcher.group(3));
    }

    public android.bluetooth.client.map.utils.BmsgTokenizer.Property next() throws java.text.ParseException {
        return next(false);
    }

    public java.lang.String remaining() {
        return mStr.substring(mPos);
    }

    public int pos() {
        return mPos + mOffset;
    }
}

