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
package android.os;


/**
 * A mapping from String keys to values of various types. The set of types
 * supported by this class is purposefully restricted to simple objects that can
 * safely be persisted to and restored from disk.
 *
 * @see Bundle
 */
public final class PersistableBundle extends android.os.BaseBundle implements android.os.Parcelable , com.android.internal.util.XmlUtils.WriteMapCallback , java.lang.Cloneable {
    private static final java.lang.String TAG_PERSISTABLEMAP = "pbundle_as_map";

    public static final android.os.PersistableBundle EMPTY;

    static {
        EMPTY = new android.os.PersistableBundle();
        android.os.PersistableBundle.EMPTY.mMap = android.util.ArrayMap.EMPTY;
    }

    /**
     *
     *
     * @unknown 
     */
    public static boolean isValidType(java.lang.Object value) {
        return (((((((((((value instanceof java.lang.Integer) || (value instanceof java.lang.Long)) || (value instanceof java.lang.Double)) || (value instanceof java.lang.String)) || (value instanceof int[])) || (value instanceof long[])) || (value instanceof double[])) || (value instanceof java.lang.String[])) || (value instanceof android.os.PersistableBundle)) || (value == null)) || (value instanceof java.lang.Boolean)) || (value instanceof boolean[]);
    }

    /**
     * Constructs a new, empty PersistableBundle.
     */
    public PersistableBundle() {
        super();
        mFlags = android.os.BaseBundle.FLAG_DEFUSABLE;
    }

    /**
     * Constructs a new, empty PersistableBundle sized to hold the given number of
     * elements. The PersistableBundle will grow as needed.
     *
     * @param capacity
     * 		the initial capacity of the PersistableBundle
     */
    public PersistableBundle(int capacity) {
        super(capacity);
        mFlags = android.os.BaseBundle.FLAG_DEFUSABLE;
    }

    /**
     * Constructs a PersistableBundle containing a copy of the mappings from the given
     * PersistableBundle.
     *
     * @param b
     * 		a PersistableBundle to be copied.
     */
    public PersistableBundle(android.os.PersistableBundle b) {
        super(b);
        mFlags = b.mFlags;
    }

    /**
     * Constructs a PersistableBundle from a Bundle.
     *
     * @param b
     * 		a Bundle to be copied.
     * @throws IllegalArgumentException
     * 		if any element of {@code b} cannot be persisted.
     * @unknown 
     */
    public PersistableBundle(android.os.Bundle b) {
        this(b.getMap());
    }

    /**
     * Constructs a PersistableBundle containing the mappings passed in.
     *
     * @param map
     * 		a Map containing only those items that can be persisted.
     * @throws IllegalArgumentException
     * 		if any element of #map cannot be persisted.
     */
    private PersistableBundle(android.util.ArrayMap<java.lang.String, java.lang.Object> map) {
        super();
        mFlags = android.os.BaseBundle.FLAG_DEFUSABLE;
        // First stuff everything in.
        putAll(map);
        // Now verify each item throwing an exception if there is a violation.
        final int N = mMap.size();
        for (int i = 0; i < N; i++) {
            java.lang.Object value = mMap.valueAt(i);
            if (value instanceof android.util.ArrayMap) {
                // Fix up any Maps by replacing them with PersistableBundles.
                mMap.setValueAt(i, new android.os.PersistableBundle(((android.util.ArrayMap<java.lang.String, java.lang.Object>) (value))));
            } else
                if (value instanceof android.os.Bundle) {
                    mMap.setValueAt(i, new android.os.PersistableBundle(((android.os.Bundle) (value))));
                } else
                    if (!android.os.PersistableBundle.isValidType(value)) {
                        throw new java.lang.IllegalArgumentException((("Bad value in PersistableBundle key=" + mMap.keyAt(i)) + " value=") + value);
                    }


        }
    }

    /* package */
    PersistableBundle(android.os.Parcel parcelledData, int length) {
        super(parcelledData, length);
        mFlags = android.os.BaseBundle.FLAG_DEFUSABLE;
    }

    /**
     * Make a PersistableBundle for a single key/value pair.
     *
     * @unknown 
     */
    public static android.os.PersistableBundle forPair(java.lang.String key, java.lang.String value) {
        android.os.PersistableBundle b = new android.os.PersistableBundle(1);
        b.putString(key, value);
        return b;
    }

    /**
     * Clones the current PersistableBundle. The internal map is cloned, but the keys and
     * values to which it refers are copied by reference.
     */
    @java.lang.Override
    public java.lang.Object clone() {
        return new android.os.PersistableBundle(this);
    }

    /**
     * Inserts a PersistableBundle value into the mapping of this Bundle, replacing
     * any existing value for the given key.  Either key or value may be null.
     *
     * @param key
     * 		a String, or null
     * @param value
     * 		a Bundle object, or null
     */
    public void putPersistableBundle(@android.annotation.Nullable
    java.lang.String key, @android.annotation.Nullable
    android.os.PersistableBundle value) {
        unparcel();
        mMap.put(key, value);
    }

    /**
     * Returns the value associated with the given key, or null if
     * no mapping of the desired type exists for the given key or a null
     * value is explicitly associated with the key.
     *
     * @param key
     * 		a String, or null
     * @return a Bundle value, or null
     */
    @android.annotation.Nullable
    public android.os.PersistableBundle getPersistableBundle(@android.annotation.Nullable
    java.lang.String key) {
        unparcel();
        java.lang.Object o = mMap.get(key);
        if (o == null) {
            return null;
        }
        try {
            return ((android.os.PersistableBundle) (o));
        } catch (java.lang.ClassCastException e) {
            typeWarning(key, o, "Bundle", e);
            return null;
        }
    }

    public static final android.os.Parcelable.Creator<android.os.PersistableBundle> CREATOR = new android.os.Parcelable.Creator<android.os.PersistableBundle>() {
        @java.lang.Override
        public android.os.PersistableBundle createFromParcel(android.os.Parcel in) {
            return in.readPersistableBundle();
        }

        @java.lang.Override
        public android.os.PersistableBundle[] newArray(int size) {
            return new android.os.PersistableBundle[size];
        }
    };

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public void writeUnknownObject(java.lang.Object v, java.lang.String name, org.xmlpull.v1.XmlSerializer out) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        if (v instanceof android.os.PersistableBundle) {
            out.startTag(null, android.os.PersistableBundle.TAG_PERSISTABLEMAP);
            out.attribute(null, "name", name);
            ((android.os.PersistableBundle) (v)).saveToXml(out);
            out.endTag(null, android.os.PersistableBundle.TAG_PERSISTABLEMAP);
        } else {
            throw new org.xmlpull.v1.XmlPullParserException("Unknown Object o=" + v);
        }
    }

    /**
     *
     *
     * @unknown 
     */
    public void saveToXml(org.xmlpull.v1.XmlSerializer out) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        unparcel();
        com.android.internal.util.XmlUtils.writeMapXml(mMap, out, this);
    }

    /**
     *
     *
     * @unknown 
     */
    static class MyReadMapCallback implements com.android.internal.util.XmlUtils.ReadMapCallback {
        @java.lang.Override
        public java.lang.Object readThisUnknownObjectXml(org.xmlpull.v1.XmlPullParser in, java.lang.String tag) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
            if (android.os.PersistableBundle.TAG_PERSISTABLEMAP.equals(tag)) {
                return android.os.PersistableBundle.restoreFromXml(in);
            }
            throw new org.xmlpull.v1.XmlPullParserException("Unknown tag=" + tag);
        }
    }

    /**
     * Report the nature of this Parcelable's contents
     */
    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    /**
     * Writes the PersistableBundle contents to a Parcel, typically in order for
     * it to be passed through an IBinder connection.
     *
     * @param parcel
     * 		The parcel to copy this bundle to.
     */
    @java.lang.Override
    public void writeToParcel(android.os.Parcel parcel, int flags) {
        final boolean oldAllowFds = parcel.pushAllowFds(false);
        try {
            writeToParcelInner(parcel, flags);
        } finally {
            parcel.restoreAllowFds(oldAllowFds);
        }
    }

    /**
     *
     *
     * @unknown 
     */
    public static android.os.PersistableBundle restoreFromXml(org.xmlpull.v1.XmlPullParser in) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        final int outerDepth = in.getDepth();
        final java.lang.String startTag = in.getName();
        final java.lang.String[] tagName = new java.lang.String[1];
        int event;
        while (((event = in.next()) != org.xmlpull.v1.XmlPullParser.END_DOCUMENT) && ((event != org.xmlpull.v1.XmlPullParser.END_TAG) || (in.getDepth() < outerDepth))) {
            if (event == org.xmlpull.v1.XmlPullParser.START_TAG) {
                return new android.os.PersistableBundle(((android.util.ArrayMap<java.lang.String, java.lang.Object>) (com.android.internal.util.XmlUtils.readThisArrayMapXml(in, startTag, tagName, new android.os.PersistableBundle.MyReadMapCallback()))));
            }
        } 
        return android.os.PersistableBundle.EMPTY;
    }

    @java.lang.Override
    public synchronized java.lang.String toString() {
        if (mParcelledData != null) {
            if (isEmptyParcel()) {
                return "PersistableBundle[EMPTY_PARCEL]";
            } else {
                return ("PersistableBundle[mParcelledData.dataSize=" + mParcelledData.dataSize()) + "]";
            }
        }
        return ("PersistableBundle[" + mMap.toString()) + "]";
    }
}

