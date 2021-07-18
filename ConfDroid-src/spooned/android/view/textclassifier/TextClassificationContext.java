/**
 * Copyright (C) 2018 The Android Open Source Project
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
 * A representation of the context in which text classification would be performed.
 *
 * @see TextClassificationManager#createTextClassificationSession(TextClassificationContext)
 */
public final class TextClassificationContext implements android.os.Parcelable {
    private final java.lang.String mPackageName;

    private final java.lang.String mWidgetType;

    @android.annotation.Nullable
    private final java.lang.String mWidgetVersion;

    private TextClassificationContext(java.lang.String packageName, java.lang.String widgetType, java.lang.String widgetVersion) {
        mPackageName = com.android.internal.util.Preconditions.checkNotNull(packageName);
        mWidgetType = com.android.internal.util.Preconditions.checkNotNull(widgetType);
        mWidgetVersion = widgetVersion;
    }

    /**
     * Returns the package name for the calling package.
     */
    @android.annotation.NonNull
    public java.lang.String getPackageName() {
        return mPackageName;
    }

    /**
     * Returns the widget type for this classification context.
     */
    @android.annotation.NonNull
    @android.view.textclassifier.TextClassifier.WidgetType
    public java.lang.String getWidgetType() {
        return mWidgetType;
    }

    /**
     * Returns a custom version string for the widget type.
     *
     * @see #getWidgetType()
     */
    @android.annotation.Nullable
    public java.lang.String getWidgetVersion() {
        return mWidgetVersion;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return java.lang.String.format(java.util.Locale.US, "TextClassificationContext{" + "packageName=%s, widgetType=%s, widgetVersion=%s}", mPackageName, mWidgetType, mWidgetVersion);
    }

    /**
     * A builder for building a TextClassification context.
     */
    public static final class Builder {
        private final java.lang.String mPackageName;

        private final java.lang.String mWidgetType;

        @android.annotation.Nullable
        private java.lang.String mWidgetVersion;

        /**
         * Initializes a new builder for text classification context objects.
         *
         * @param packageName
         * 		the name of the calling package
         * @param widgetType
         * 		the type of widget e.g. {@link TextClassifier#WIDGET_TYPE_TEXTVIEW}
         * @return this builder
         */
        public Builder(@android.annotation.NonNull
        java.lang.String packageName, @android.annotation.NonNull
        @android.view.textclassifier.TextClassifier.WidgetType
        java.lang.String widgetType) {
            mPackageName = com.android.internal.util.Preconditions.checkNotNull(packageName);
            mWidgetType = com.android.internal.util.Preconditions.checkNotNull(widgetType);
        }

        /**
         * Sets an optional custom version string for the widget type.
         *
         * @return this builder
         */
        public android.view.textclassifier.TextClassificationContext.Builder setWidgetVersion(@android.annotation.Nullable
        java.lang.String widgetVersion) {
            mWidgetVersion = widgetVersion;
            return this;
        }

        /**
         * Builds the text classification context object.
         *
         * @return the built TextClassificationContext object
         */
        @android.annotation.NonNull
        public android.view.textclassifier.TextClassificationContext build() {
            return new android.view.textclassifier.TextClassificationContext(mPackageName, mWidgetType, mWidgetVersion);
        }
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel parcel, int flags) {
        parcel.writeString(mPackageName);
        parcel.writeString(mWidgetType);
        parcel.writeString(mWidgetVersion);
    }

    private TextClassificationContext(android.os.Parcel in) {
        mPackageName = in.readString();
        mWidgetType = in.readString();
        mWidgetVersion = in.readString();
    }

    @android.annotation.NonNull
    public static final android.os.Parcelable.Creator<android.view.textclassifier.TextClassificationContext> CREATOR = new android.os.Parcelable.Creator<android.view.textclassifier.TextClassificationContext>() {
        @java.lang.Override
        public android.view.textclassifier.TextClassificationContext createFromParcel(android.os.Parcel parcel) {
            return new android.view.textclassifier.TextClassificationContext(parcel);
        }

        @java.lang.Override
        public android.view.textclassifier.TextClassificationContext[] newArray(int size) {
            return new android.view.textclassifier.TextClassificationContext[size];
        }
    };
}

