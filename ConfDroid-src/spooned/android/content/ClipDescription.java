/**
 * Copyright (c) 2010, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package android.content;


/**
 * Meta-data describing the contents of a {@link ClipData}.  Provides enough
 * information to know if you can handle the ClipData, but not the data
 * itself.
 *
 * <div class="special reference">
 * <h3>Developer Guides</h3>
 * <p>For more information about using the clipboard framework, read the
 * <a href="{@docRoot }guide/topics/clipboard/copy-paste.html">Copy and Paste</a>
 * developer guide.</p>
 * </div>
 */
public class ClipDescription implements android.os.Parcelable {
    /**
     * The MIME type for a clip holding plain text.
     */
    public static final java.lang.String MIMETYPE_TEXT_PLAIN = "text/plain";

    /**
     * The MIME type for a clip holding HTML text.
     */
    public static final java.lang.String MIMETYPE_TEXT_HTML = "text/html";

    /**
     * The MIME type for a clip holding one or more URIs.  This should be
     * used for URIs that are meaningful to a user (such as an http: URI).
     * It should <em>not</em> be used for a content: URI that references some
     * other piece of data; in that case the MIME type should be the type
     * of the referenced data.
     */
    public static final java.lang.String MIMETYPE_TEXT_URILIST = "text/uri-list";

    /**
     * The MIME type for a clip holding an Intent.
     */
    public static final java.lang.String MIMETYPE_TEXT_INTENT = "text/vnd.android.intent";

    /**
     * The name of the extra used to define a component name when copying/dragging
     * an app icon from Launcher.
     * <p>
     * Type: String
     * </p>
     * <p>
     * Use {@link ComponentName#unflattenFromString(String)}
     * and {@link ComponentName#flattenToString()} to convert the extra value
     * to/from {@link ComponentName}.
     * </p>
     *
     * @unknown 
     */
    public static final java.lang.String EXTRA_TARGET_COMPONENT_NAME = "android.content.extra.TARGET_COMPONENT_NAME";

    /**
     * The name of the extra used to define a user serial number when copying/dragging
     * an app icon from Launcher.
     * <p>
     * Type: long
     * </p>
     *
     * @unknown 
     */
    public static final java.lang.String EXTRA_USER_SERIAL_NUMBER = "android.content.extra.USER_SERIAL_NUMBER";

    final java.lang.CharSequence mLabel;

    private final java.util.ArrayList<java.lang.String> mMimeTypes;

    private android.os.PersistableBundle mExtras;

    private long mTimeStamp;

    /**
     * Create a new clip.
     *
     * @param label
     * 		Label to show to the user describing this clip.
     * @param mimeTypes
     * 		An array of MIME types this data is available as.
     */
    public ClipDescription(java.lang.CharSequence label, java.lang.String[] mimeTypes) {
        if (mimeTypes == null) {
            throw new java.lang.NullPointerException("mimeTypes is null");
        }
        mLabel = label;
        mMimeTypes = new java.util.ArrayList<java.lang.String>(java.util.Arrays.asList(mimeTypes));
    }

    /**
     * Create a copy of a ClipDescription.
     */
    public ClipDescription(android.content.ClipDescription o) {
        mLabel = o.mLabel;
        mMimeTypes = new java.util.ArrayList<java.lang.String>(o.mMimeTypes);
        mTimeStamp = o.mTimeStamp;
    }

    /**
     * Helper to compare two MIME types, where one may be a pattern.
     *
     * @param concreteType
     * 		A fully-specified MIME type.
     * @param desiredType
     * 		A desired MIME type that may be a pattern such as *&#47;*.
     * @return Returns true if the two MIME types match.
     */
    public static boolean compareMimeTypes(java.lang.String concreteType, java.lang.String desiredType) {
        final int typeLength = desiredType.length();
        if ((typeLength == 3) && desiredType.equals("*/*")) {
            return true;
        }
        final int slashpos = desiredType.indexOf('/');
        if (slashpos > 0) {
            if ((typeLength == (slashpos + 2)) && (desiredType.charAt(slashpos + 1) == '*')) {
                if (desiredType.regionMatches(0, concreteType, 0, slashpos + 1)) {
                    return true;
                }
            } else
                if (desiredType.equals(concreteType)) {
                    return true;
                }

        }
        return false;
    }

    /**
     * Used for setting the timestamp at which the associated {@link ClipData} is copied to
     * global clipboard.
     *
     * @param timeStamp
     * 		at which the associated {@link ClipData} is copied to clipboard in
     * 		{@link System#currentTimeMillis()} time base.
     * @unknown 
     */
    public void setTimestamp(long timeStamp) {
        mTimeStamp = timeStamp;
    }

    /**
     * Return the timestamp at which the associated {@link ClipData} is copied to global clipboard
     * in the {@link System#currentTimeMillis()} time base.
     *
     * @return timestamp at which the associated {@link ClipData} is copied to global clipboard
    or {@code 0} if it is not copied to clipboard.
     */
    public long getTimestamp() {
        return mTimeStamp;
    }

    /**
     * Return the label for this clip.
     */
    public java.lang.CharSequence getLabel() {
        return mLabel;
    }

    /**
     * Check whether the clip description contains the given MIME type.
     *
     * @param mimeType
     * 		The desired MIME type.  May be a pattern.
     * @return Returns true if one of the MIME types in the clip description
    matches the desired MIME type, else false.
     */
    public boolean hasMimeType(java.lang.String mimeType) {
        final int size = mMimeTypes.size();
        for (int i = 0; i < size; i++) {
            if (android.content.ClipDescription.compareMimeTypes(mMimeTypes.get(i), mimeType)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Filter the clip description MIME types by the given MIME type.  Returns
     * all MIME types in the clip that match the given MIME type.
     *
     * @param mimeType
     * 		The desired MIME type.  May be a pattern.
     * @return Returns an array of all matching MIME types.  If there are no
    matching MIME types, null is returned.
     */
    public java.lang.String[] filterMimeTypes(java.lang.String mimeType) {
        java.util.ArrayList<java.lang.String> array = null;
        final int size = mMimeTypes.size();
        for (int i = 0; i < size; i++) {
            if (android.content.ClipDescription.compareMimeTypes(mMimeTypes.get(i), mimeType)) {
                if (array == null) {
                    array = new java.util.ArrayList<java.lang.String>();
                }
                array.add(mMimeTypes.get(i));
            }
        }
        if (array == null) {
            return null;
        }
        java.lang.String[] rawArray = new java.lang.String[array.size()];
        array.toArray(rawArray);
        return rawArray;
    }

    /**
     * Return the number of MIME types the clip is available in.
     */
    public int getMimeTypeCount() {
        return mMimeTypes.size();
    }

    /**
     * Return one of the possible clip MIME types.
     */
    public java.lang.String getMimeType(int index) {
        return mMimeTypes.get(index);
    }

    /**
     * Add MIME types to the clip description.
     */
    void addMimeTypes(java.lang.String[] mimeTypes) {
        for (int i = 0; i != mimeTypes.length; i++) {
            final java.lang.String mimeType = mimeTypes[i];
            if (!mMimeTypes.contains(mimeType)) {
                mMimeTypes.add(mimeType);
            }
        }
    }

    /**
     * Retrieve extended data from the clip description.
     *
     * @return the bundle containing extended data previously set with
    {@link #setExtras(PersistableBundle)}, or null if no extras have been set.
     * @see #setExtras(PersistableBundle)
     */
    public android.os.PersistableBundle getExtras() {
        return mExtras;
    }

    /**
     * Add extended data to the clip description.
     *
     * @see #getExtras()
     */
    public void setExtras(android.os.PersistableBundle extras) {
        mExtras = new android.os.PersistableBundle(extras);
    }

    /**
     *
     *
     * @unknown 
     */
    public void validate() {
        if (mMimeTypes == null) {
            throw new java.lang.NullPointerException("null mime types");
        }
        final int size = mMimeTypes.size();
        if (size <= 0) {
            throw new java.lang.IllegalArgumentException("must have at least 1 mime type");
        }
        for (int i = 0; i < size; i++) {
            if (mMimeTypes.get(i) == null) {
                throw new java.lang.NullPointerException(("mime type at " + i) + " is null");
            }
        }
    }

    @java.lang.Override
    public java.lang.String toString() {
        java.lang.StringBuilder b = new java.lang.StringBuilder(128);
        b.append("ClipDescription { ");
        toShortString(b);
        b.append(" }");
        return b.toString();
    }

    /**
     *
     *
     * @unknown 
     */
    public boolean toShortString(java.lang.StringBuilder b) {
        boolean first = !toShortStringTypesOnly(b);
        if (mLabel != null) {
            if (!first) {
                b.append(' ');
            }
            first = false;
            b.append('"');
            b.append(mLabel);
            b.append('"');
        }
        if (mExtras != null) {
            if (!first) {
                b.append(' ');
            }
            first = false;
            b.append(mExtras.toString());
        }
        if (mTimeStamp > 0) {
            if (!first) {
                b.append(' ');
            }
            first = false;
            b.append('<');
            b.append(android.util.TimeUtils.logTimeOfDay(mTimeStamp));
            b.append('>');
        }
        return !first;
    }

    /**
     *
     *
     * @unknown 
     */
    public boolean toShortStringTypesOnly(java.lang.StringBuilder b) {
        boolean first = true;
        final int size = mMimeTypes.size();
        for (int i = 0; i < size; i++) {
            if (!first) {
                b.append(' ');
            }
            first = false;
            b.append(mMimeTypes.get(i));
        }
        return !first;
    }

    /**
     *
     *
     * @unknown 
     */
    public void writeToProto(android.util.proto.ProtoOutputStream proto, long fieldId) {
        final long token = proto.start(fieldId);
        final int size = mMimeTypes.size();
        for (int i = 0; i < size; i++) {
            proto.write(ClipDescriptionProto.MIME_TYPES, mMimeTypes.get(i));
        }
        if (mLabel != null) {
            proto.write(ClipDescriptionProto.LABEL, mLabel.toString());
        }
        if (mExtras != null) {
            mExtras.writeToProto(proto, ClipDescriptionProto.EXTRAS);
        }
        if (mTimeStamp > 0) {
            proto.write(ClipDescriptionProto.TIMESTAMP_MS, mTimeStamp);
        }
        proto.end(token);
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        android.text.TextUtils.writeToParcel(mLabel, dest, flags);
        dest.writeStringList(mMimeTypes);
        dest.writePersistableBundle(mExtras);
        dest.writeLong(mTimeStamp);
    }

    ClipDescription(android.os.Parcel in) {
        mLabel = TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(in);
        mMimeTypes = in.createStringArrayList();
        mExtras = in.readPersistableBundle();
        mTimeStamp = in.readLong();
    }

    @android.annotation.NonNull
    public static final android.os.Parcelable.Creator<android.content.ClipDescription> CREATOR = new android.os.Parcelable.Creator<android.content.ClipDescription>() {
        public android.content.ClipDescription createFromParcel(android.os.Parcel source) {
            return new android.content.ClipDescription(source);
        }

        public android.content.ClipDescription[] newArray(int size) {
            return new android.content.ClipDescription[size];
        }
    };
}

