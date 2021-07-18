/**
 * Copyright (C) 2008 The Android Open Source Project
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
package android.text;


/**
 * Annotations are simple key-value pairs that are preserved across
 * TextView save/restore cycles and can be used to keep application-specific
 * data that needs to be maintained for regions of text.
 */
public class Annotation implements android.text.ParcelableSpan {
    private final java.lang.String mKey;

    private final java.lang.String mValue;

    public Annotation(java.lang.String key, java.lang.String value) {
        mKey = key;
        mValue = value;
    }

    public Annotation(android.os.Parcel src) {
        mKey = src.readString();
        mValue = src.readString();
    }

    public int getSpanTypeId() {
        return getSpanTypeIdInternal();
    }

    /**
     *
     *
     * @unknown 
     */
    public int getSpanTypeIdInternal() {
        return android.text.TextUtils.ANNOTATION;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(android.os.Parcel dest, int flags) {
        writeToParcelInternal(dest, flags);
    }

    /**
     *
     *
     * @unknown 
     */
    public void writeToParcelInternal(android.os.Parcel dest, int flags) {
        dest.writeString(mKey);
        dest.writeString(mValue);
    }

    public java.lang.String getKey() {
        return mKey;
    }

    public java.lang.String getValue() {
        return mValue;
    }
}

