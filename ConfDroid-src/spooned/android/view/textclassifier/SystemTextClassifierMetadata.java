/**
 * Copyright (C) 2020 The Android Open Source Project
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
package android.view.textclassifier;


/**
 * SystemTextClassifier specific information.
 * <p>
 * This contains information requires for the TextClassificationManagerService to process the
 * requests from the application, e.g. user id, calling package name and etc. Centrialize the data
 * into this class helps to extend the scalability if we want to add new fields.
 *
 * @unknown 
 */
@com.android.internal.annotations.VisibleForTesting(visibility = com.android.internal.annotations.VisibleForTesting.Visibility.PACKAGE)
public final class SystemTextClassifierMetadata implements android.os.Parcelable {
    /* The name of the package that sent the TC request. */
    @android.annotation.NonNull
    private final java.lang.String mCallingPackageName;

    /* The id of the user that sent the TC request. */
    @android.annotation.UserIdInt
    private final int mUserId;

    /* Whether to use the default text classifier to handle the request. */
    private final boolean mUseDefaultTextClassifier;

    public SystemTextClassifierMetadata(@android.annotation.NonNull
    java.lang.String packageName, @android.annotation.UserIdInt
    int userId, boolean useDefaultTextClassifier) {
        java.util.Objects.requireNonNull(packageName);
        mCallingPackageName = packageName;
        mUserId = userId;
        mUseDefaultTextClassifier = useDefaultTextClassifier;
    }

    /**
     * Returns the id of the user that sent the TC request.
     */
    @android.annotation.UserIdInt
    public int getUserId() {
        return mUserId;
    }

    /**
     * Returns the name of the package that sent the TC request.
     * This returns {@code null} if no calling package name is set.
     */
    @android.annotation.NonNull
    public java.lang.String getCallingPackageName() {
        return mCallingPackageName;
    }

    /**
     * Returns whether to use the default text classifier to handle TC request.
     */
    public boolean useDefaultTextClassifier() {
        return mUseDefaultTextClassifier;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return java.lang.String.format(java.util.Locale.US, "SystemTextClassifierMetadata {callingPackageName=%s, userId=%d, " + "useDefaultTextClassifier=%b}", mCallingPackageName, mUserId, mUseDefaultTextClassifier);
    }

    private static android.view.textclassifier.SystemTextClassifierMetadata readFromParcel(android.os.Parcel in) {
        final java.lang.String packageName = in.readString();
        final int userId = in.readInt();
        final boolean useDefaultTextClassifier = in.readBoolean();
        return new android.view.textclassifier.SystemTextClassifierMetadata(packageName, userId, useDefaultTextClassifier);
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeString(mCallingPackageName);
        dest.writeInt(mUserId);
        dest.writeBoolean(mUseDefaultTextClassifier);
    }

    @android.annotation.NonNull
    public static final android.view.textclassifier.Creator<android.view.textclassifier.SystemTextClassifierMetadata> CREATOR = new android.view.textclassifier.Creator<android.view.textclassifier.SystemTextClassifierMetadata>() {
        @java.lang.Override
        public android.view.textclassifier.SystemTextClassifierMetadata createFromParcel(android.os.Parcel in) {
            return android.view.textclassifier.SystemTextClassifierMetadata.readFromParcel(in);
        }

        @java.lang.Override
        public android.view.textclassifier.SystemTextClassifierMetadata[] newArray(int size) {
            return new android.view.textclassifier.SystemTextClassifierMetadata[size];
        }
    };
}

