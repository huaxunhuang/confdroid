/**
 * Copyright (C) 2009 The Android Open Source Project
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
package android.content;


/**
 * Contains the result of the application of a {@link ContentProviderOperation}. It is guaranteed
 * to have exactly one of {@link #uri} or {@link #count} set.
 */
public class ContentProviderResult implements android.os.Parcelable {
    public final android.net.Uri uri;

    public final java.lang.Integer count;

    /**
     * {@hide }
     */
    public final java.lang.String failure;

    public ContentProviderResult(android.net.Uri uri) {
        this(com.android.internal.util.Preconditions.checkNotNull(uri), null, null);
    }

    public ContentProviderResult(int count) {
        this(null, count, null);
    }

    /**
     * {@hide }
     */
    public ContentProviderResult(java.lang.String failure) {
        this(null, null, failure);
    }

    /**
     * {@hide }
     */
    public ContentProviderResult(android.net.Uri uri, java.lang.Integer count, java.lang.String failure) {
        this.uri = uri;
        this.count = count;
        this.failure = failure;
    }

    public ContentProviderResult(android.os.Parcel source) {
        if (source.readInt() != 0) {
            uri = Uri.CREATOR.createFromParcel(source);
        } else {
            uri = null;
        }
        if (source.readInt() != 0) {
            count = source.readInt();
        } else {
            count = null;
        }
        if (source.readInt() != 0) {
            failure = source.readString();
        } else {
            failure = null;
        }
    }

    /**
     *
     *
     * @unknown 
     */
    public ContentProviderResult(android.content.ContentProviderResult cpr, int userId) {
        uri = android.content.ContentProvider.maybeAddUserId(cpr.uri, userId);
        count = cpr.count;
        failure = cpr.failure;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        if (uri != null) {
            dest.writeInt(1);
            uri.writeToParcel(dest, flags);
        } else {
            dest.writeInt(0);
        }
        if (count != null) {
            dest.writeInt(1);
            dest.writeInt(count);
        } else {
            dest.writeInt(0);
        }
        if (failure != null) {
            dest.writeInt(1);
            dest.writeString(failure);
        } else {
            dest.writeInt(0);
        }
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @android.annotation.NonNull
    public static final android.content.Creator<android.content.ContentProviderResult> CREATOR = new android.content.Creator<android.content.ContentProviderResult>() {
        @java.lang.Override
        public android.content.ContentProviderResult createFromParcel(android.os.Parcel source) {
            return new android.content.ContentProviderResult(source);
        }

        @java.lang.Override
        public android.content.ContentProviderResult[] newArray(int size) {
            return new android.content.ContentProviderResult[size];
        }
    };

    @java.lang.Override
    public java.lang.String toString() {
        final java.lang.StringBuilder sb = new java.lang.StringBuilder("ContentProviderResult(");
        if (uri != null) {
            sb.append(("uri=" + uri) + " ");
        }
        if (count != null) {
            sb.append(("count=" + count) + " ");
        }
        if (uri != null) {
            sb.append(("failure=" + failure) + " ");
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append(")");
        return sb.toString();
    }
}

