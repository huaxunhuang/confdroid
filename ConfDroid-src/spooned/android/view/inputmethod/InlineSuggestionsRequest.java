/**
 * Copyright (C) 2019 The Android Open Source Project
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
package android.view.inputmethod;


/**
 * This class represents an inline suggestion request made by one app to get suggestions from the
 * other source. See {@link InlineSuggestion} for more information.
 */
// @formatter:on
// End of generated code
@com.android.internal.util.DataClass(genEqualsHashCode = true, genToString = true, genBuilder = true)
public final class InlineSuggestionsRequest implements android.os.Parcelable {
    /**
     * Constant used to indicate not putting a cap on the number of suggestions to return.
     */
    public static final int SUGGESTION_COUNT_UNLIMITED = java.lang.Integer.MAX_VALUE;

    /**
     * Max number of suggestions expected from the response. It must be a positive value.
     * Defaults to {@code SUGGESTION_COUNT_UNLIMITED} if not set.
     */
    private final int mMaxSuggestionCount;

    /**
     * The {@link InlinePresentationSpec} for each suggestion in the response. If the max suggestion
     * count is larger than the number of specs in the list, then the last spec is used for the
     * remainder of the suggestions. The list should not be empty.
     */
    @android.annotation.NonNull
    private final java.util.List<android.widget.inline.InlinePresentationSpec> mInlinePresentationSpecs;

    /**
     * The package name of the app that requests for the inline suggestions and will host the
     * embedded suggestion views. The app does not have to set the value for the field because
     * it'll be set by the system for safety reasons.
     */
    @android.annotation.NonNull
    private java.lang.String mHostPackageName;

    /**
     * The IME provided locales for the request. If non-empty, the inline suggestions should
     * return languages from the supported locales. If not provided, it'll default to system locale.
     */
    @android.annotation.NonNull
    private android.os.LocaleList mSupportedLocales;

    /**
     * The extras state propagated from the IME to pass extra data.
     *
     * <p>Note: There should be no remote objects in the bundle, all included remote objects will
     * be removed from the bundle before transmission.</p>
     */
    @android.annotation.NonNull
    private android.os.Bundle mExtras;

    /**
     * The host input token of the IME that made the request. This will be set by the system for
     * safety reasons.
     *
     * @unknown 
     */
    @android.annotation.Nullable
    private android.os.IBinder mHostInputToken;

    /**
     * The host display id of the IME that made the request. This will be set by the system for
     * safety reasons.
     *
     * @unknown 
     */
    private int mHostDisplayId;

    /**
     *
     *
     * @unknown 
     * @see {@link #mHostInputToken}.
     */
    public void setHostInputToken(android.os.IBinder hostInputToken) {
        mHostInputToken = hostInputToken;
    }

    private boolean extrasEquals(@android.annotation.NonNull
    android.os.Bundle extras) {
        return com.android.internal.widget.InlinePresentationStyleUtils.bundleEquals(mExtras, extras);
    }

    // TODO(b/149609075): remove once IBinder parcelling is natively supported
    private void parcelHostInputToken(@android.annotation.NonNull
    android.os.Parcel parcel, int flags) {
        parcel.writeStrongBinder(mHostInputToken);
    }

    // TODO(b/149609075): remove once IBinder parcelling is natively supported
    @android.annotation.Nullable
    private android.os.IBinder unparcelHostInputToken(android.os.Parcel parcel) {
        return parcel.readStrongBinder();
    }

    /**
     *
     *
     * @unknown 
     * @see {@link #mHostDisplayId}.
     */
    public void setHostDisplayId(int hostDisplayId) {
        mHostDisplayId = hostDisplayId;
    }

    private void onConstructed() {
        com.android.internal.util.Preconditions.checkState(!mInlinePresentationSpecs.isEmpty());
        com.android.internal.util.Preconditions.checkState(mMaxSuggestionCount >= mInlinePresentationSpecs.size());
    }

    /**
     * Removes the remote objects from the bundles within the {@Code mExtras} and the
     * {@code mInlinePresentationSpecs}.
     *
     * @unknown 
     */
    public void filterContentTypes() {
        com.android.internal.widget.InlinePresentationStyleUtils.filterContentTypes(mExtras);
        for (int i = 0; i < mInlinePresentationSpecs.size(); i++) {
            mInlinePresentationSpecs.get(i).filterContentTypes();
        }
    }

    private static int defaultMaxSuggestionCount() {
        return android.view.inputmethod.InlineSuggestionsRequest.SUGGESTION_COUNT_UNLIMITED;
    }

    private static java.lang.String defaultHostPackageName() {
        return android.app.ActivityThread.currentPackageName();
    }

    private static android.os.LocaleList defaultSupportedLocales() {
        return android.os.LocaleList.getDefault();
    }

    @android.annotation.Nullable
    private static android.os.IBinder defaultHostInputToken() {
        return null;
    }

    @android.annotation.Nullable
    private static int defaultHostDisplayId() {
        return android.view.Display.INVALID_DISPLAY;
    }

    @android.annotation.NonNull
    private static android.os.Bundle defaultExtras() {
        return android.os.Bundle.EMPTY;
    }

    /**
     *
     *
     * @unknown 
     */
    static abstract class BaseBuilder {
        abstract android.view.inputmethod.InlineSuggestionsRequest.Builder setInlinePresentationSpecs(@android.annotation.NonNull
        java.util.List<android.widget.inline.InlinePresentationSpec> specs);

        abstract android.view.inputmethod.InlineSuggestionsRequest.Builder setHostPackageName(@android.annotation.Nullable
        java.lang.String value);

        abstract android.view.inputmethod.InlineSuggestionsRequest.Builder setHostInputToken(android.os.IBinder hostInputToken);

        abstract android.view.inputmethod.InlineSuggestionsRequest.Builder setHostDisplayId(int value);
    }

    // Code below generated by codegen v1.0.15.
    // 
    // DO NOT MODIFY!
    // CHECKSTYLE:OFF Generated code
    // 
    // To regenerate run:
    // $ codegen $ANDROID_BUILD_TOP/frameworks/base/core/java/android/view/inputmethod/InlineSuggestionsRequest.java
    // 
    // To exclude the generated code from IntelliJ auto-formatting enable (one-time):
    // Settings > Editor > Code Style > Formatter Control
    // @formatter:off
    /* package-private */
    @com.android.internal.util.DataClass.Generated.Member
    InlineSuggestionsRequest(int maxSuggestionCount, @android.annotation.NonNull
    java.util.List<android.widget.inline.InlinePresentationSpec> inlinePresentationSpecs, @android.annotation.NonNull
    java.lang.String hostPackageName, @android.annotation.NonNull
    android.os.LocaleList supportedLocales, @android.annotation.NonNull
    android.os.Bundle extras, @android.annotation.Nullable
    android.os.IBinder hostInputToken, int hostDisplayId) {
        this.mMaxSuggestionCount = maxSuggestionCount;
        this.mInlinePresentationSpecs = inlinePresentationSpecs;
        com.android.internal.util.AnnotationValidations.validate(android.annotation.NonNull.class, null, mInlinePresentationSpecs);
        this.mHostPackageName = hostPackageName;
        com.android.internal.util.AnnotationValidations.validate(android.annotation.NonNull.class, null, mHostPackageName);
        this.mSupportedLocales = supportedLocales;
        com.android.internal.util.AnnotationValidations.validate(android.annotation.NonNull.class, null, mSupportedLocales);
        this.mExtras = extras;
        com.android.internal.util.AnnotationValidations.validate(android.annotation.NonNull.class, null, mExtras);
        this.mHostInputToken = hostInputToken;
        this.mHostDisplayId = hostDisplayId;
        onConstructed();
    }

    /**
     * Max number of suggestions expected from the response. It must be a positive value.
     * Defaults to {@code SUGGESTION_COUNT_UNLIMITED} if not set.
     */
    @com.android.internal.util.DataClass.Generated.Member
    public int getMaxSuggestionCount() {
        return mMaxSuggestionCount;
    }

    /**
     * The {@link InlinePresentationSpec} for each suggestion in the response. If the max suggestion
     * count is larger than the number of specs in the list, then the last spec is used for the
     * remainder of the suggestions. The list should not be empty.
     */
    @com.android.internal.util.DataClass.Generated.Member
    @android.annotation.NonNull
    public java.util.List<android.widget.inline.InlinePresentationSpec> getInlinePresentationSpecs() {
        return mInlinePresentationSpecs;
    }

    /**
     * The package name of the app that requests for the inline suggestions and will host the
     * embedded suggestion views. The app does not have to set the value for the field because
     * it'll be set by the system for safety reasons.
     */
    @com.android.internal.util.DataClass.Generated.Member
    @android.annotation.NonNull
    public java.lang.String getHostPackageName() {
        return mHostPackageName;
    }

    /**
     * The IME provided locales for the request. If non-empty, the inline suggestions should
     * return languages from the supported locales. If not provided, it'll default to system locale.
     */
    @com.android.internal.util.DataClass.Generated.Member
    @android.annotation.NonNull
    public android.os.LocaleList getSupportedLocales() {
        return mSupportedLocales;
    }

    /**
     * The extras state propagated from the IME to pass extra data.
     *
     * <p>Note: There should be no remote objects in the bundle, all included remote objects will
     * be removed from the bundle before transmission.</p>
     */
    @com.android.internal.util.DataClass.Generated.Member
    @android.annotation.NonNull
    public android.os.Bundle getExtras() {
        return mExtras;
    }

    /**
     * The host input token of the IME that made the request. This will be set by the system for
     * safety reasons.
     *
     * @unknown 
     */
    @com.android.internal.util.DataClass.Generated.Member
    @android.annotation.Nullable
    public android.os.IBinder getHostInputToken() {
        return mHostInputToken;
    }

    /**
     * The host display id of the IME that made the request. This will be set by the system for
     * safety reasons.
     *
     * @unknown 
     */
    @com.android.internal.util.DataClass.Generated.Member
    public int getHostDisplayId() {
        return mHostDisplayId;
    }

    @java.lang.Override
    @com.android.internal.util.DataClass.Generated.Member
    public java.lang.String toString() {
        // You can override field toString logic by defining methods like:
        // String fieldNameToString() { ... }
        return (((((((((((((((((((("InlineSuggestionsRequest { " + "maxSuggestionCount = ") + mMaxSuggestionCount) + ", ") + "inlinePresentationSpecs = ") + mInlinePresentationSpecs) + ", ") + "hostPackageName = ") + mHostPackageName) + ", ") + "supportedLocales = ") + mSupportedLocales) + ", ") + "extras = ") + mExtras) + ", ") + "hostInputToken = ") + mHostInputToken) + ", ") + "hostDisplayId = ") + mHostDisplayId) + " }";
    }

    @java.lang.Override
    @com.android.internal.util.DataClass.Generated.Member
    public boolean equals(@android.annotation.Nullable
    java.lang.Object o) {
        // You can override field equality logic by defining either of the methods like:
        // boolean fieldNameEquals(InlineSuggestionsRequest other) { ... }
        // boolean fieldNameEquals(FieldType otherValue) { ... }
        if (this == o)
            return true;

        if ((o == null) || (getClass() != o.getClass()))
            return false;

        @java.lang.SuppressWarnings("unchecked")
        android.view.inputmethod.InlineSuggestionsRequest that = ((android.view.inputmethod.InlineSuggestionsRequest) (o));
        // noinspection PointlessBooleanExpression
        return ((((((true && (mMaxSuggestionCount == that.mMaxSuggestionCount)) && java.util.Objects.equals(mInlinePresentationSpecs, that.mInlinePresentationSpecs)) && java.util.Objects.equals(mHostPackageName, that.mHostPackageName)) && java.util.Objects.equals(mSupportedLocales, that.mSupportedLocales)) && extrasEquals(that.mExtras)) && java.util.Objects.equals(mHostInputToken, that.mHostInputToken)) && (mHostDisplayId == that.mHostDisplayId);
    }

    @java.lang.Override
    @com.android.internal.util.DataClass.Generated.Member
    public int hashCode() {
        // You can override field hashCode logic by defining methods like:
        // int fieldNameHashCode() { ... }
        int _hash = 1;
        _hash = (31 * _hash) + mMaxSuggestionCount;
        _hash = (31 * _hash) + java.util.Objects.hashCode(mInlinePresentationSpecs);
        _hash = (31 * _hash) + java.util.Objects.hashCode(mHostPackageName);
        _hash = (31 * _hash) + java.util.Objects.hashCode(mSupportedLocales);
        _hash = (31 * _hash) + java.util.Objects.hashCode(mExtras);
        _hash = (31 * _hash) + java.util.Objects.hashCode(mHostInputToken);
        _hash = (31 * _hash) + mHostDisplayId;
        return _hash;
    }

    @java.lang.Override
    @com.android.internal.util.DataClass.Generated.Member
    public void writeToParcel(@android.annotation.NonNull
    android.os.Parcel dest, int flags) {
        // You can override field parcelling by defining methods like:
        // void parcelFieldName(Parcel dest, int flags) { ... }
        byte flg = 0;
        if (mHostInputToken != null)
            flg |= 0x20;

        dest.writeByte(flg);
        dest.writeInt(mMaxSuggestionCount);
        dest.writeParcelableList(mInlinePresentationSpecs, flags);
        dest.writeString(mHostPackageName);
        dest.writeTypedObject(mSupportedLocales, flags);
        dest.writeBundle(mExtras);
        parcelHostInputToken(dest, flags);
        dest.writeInt(mHostDisplayId);
    }

    @java.lang.Override
    @com.android.internal.util.DataClass.Generated.Member
    public int describeContents() {
        return 0;
    }

    /**
     *
     *
     * @unknown 
     */
    /* package-private */
    @java.lang.SuppressWarnings({ "unchecked", "RedundantCast" })
    @com.android.internal.util.DataClass.Generated.Member
    InlineSuggestionsRequest(@android.annotation.NonNull
    android.os.Parcel in) {
        // You can override field unparcelling by defining methods like:
        // static FieldType unparcelFieldName(Parcel in) { ... }
        byte flg = in.readByte();
        int maxSuggestionCount = in.readInt();
        java.util.List<android.widget.inline.InlinePresentationSpec> inlinePresentationSpecs = new java.util.ArrayList<>();
        in.readParcelableList(inlinePresentationSpecs, android.widget.inline.InlinePresentationSpec.class.getClassLoader());
        java.lang.String hostPackageName = in.readString();
        android.os.LocaleList supportedLocales = ((android.os.LocaleList) (in.readTypedObject(LocaleList.CREATOR)));
        android.os.Bundle extras = in.readBundle();
        android.os.IBinder hostInputToken = unparcelHostInputToken(in);
        int hostDisplayId = in.readInt();
        this.mMaxSuggestionCount = maxSuggestionCount;
        this.mInlinePresentationSpecs = inlinePresentationSpecs;
        com.android.internal.util.AnnotationValidations.validate(android.annotation.NonNull.class, null, mInlinePresentationSpecs);
        this.mHostPackageName = hostPackageName;
        com.android.internal.util.AnnotationValidations.validate(android.annotation.NonNull.class, null, mHostPackageName);
        this.mSupportedLocales = supportedLocales;
        com.android.internal.util.AnnotationValidations.validate(android.annotation.NonNull.class, null, mSupportedLocales);
        this.mExtras = extras;
        com.android.internal.util.AnnotationValidations.validate(android.annotation.NonNull.class, null, mExtras);
        this.mHostInputToken = hostInputToken;
        this.mHostDisplayId = hostDisplayId;
        onConstructed();
    }

    @com.android.internal.util.DataClass.Generated.Member
    @android.annotation.NonNull
    public static final android.os.Parcelable.Creator<android.view.inputmethod.InlineSuggestionsRequest> CREATOR = new android.os.Parcelable.Creator<android.view.inputmethod.InlineSuggestionsRequest>() {
        @java.lang.Override
        public android.view.inputmethod.InlineSuggestionsRequest[] newArray(int size) {
            return new android.view.inputmethod.InlineSuggestionsRequest[size];
        }

        @java.lang.Override
        public android.view.inputmethod.InlineSuggestionsRequest createFromParcel(@android.annotation.NonNull
        android.os.Parcel in) {
            return new android.view.inputmethod.InlineSuggestionsRequest(in);
        }
    };

    /**
     * A builder for {@link InlineSuggestionsRequest}
     */
    @java.lang.SuppressWarnings("WeakerAccess")
    @com.android.internal.util.DataClass.Generated.Member
    public static final class Builder extends android.view.inputmethod.InlineSuggestionsRequest.BaseBuilder {
        private int mMaxSuggestionCount;

        @android.annotation.NonNull
        private java.util.List<android.widget.inline.InlinePresentationSpec> mInlinePresentationSpecs;

        @android.annotation.NonNull
        private java.lang.String mHostPackageName;

        @android.annotation.NonNull
        private android.os.LocaleList mSupportedLocales;

        @android.annotation.NonNull
        private android.os.Bundle mExtras;

        @android.annotation.Nullable
        private android.os.IBinder mHostInputToken;

        private int mHostDisplayId;

        private long mBuilderFieldsSet = 0L;

        /**
         * Creates a new Builder.
         *
         * @param inlinePresentationSpecs
         * 		The {@link InlinePresentationSpec} for each suggestion in the response. If the max suggestion
         * 		count is larger than the number of specs in the list, then the last spec is used for the
         * 		remainder of the suggestions. The list should not be empty.
         */
        public Builder(@android.annotation.NonNull
        java.util.List<android.widget.inline.InlinePresentationSpec> inlinePresentationSpecs) {
            mInlinePresentationSpecs = inlinePresentationSpecs;
            com.android.internal.util.AnnotationValidations.validate(android.annotation.NonNull.class, null, mInlinePresentationSpecs);
        }

        /**
         * Max number of suggestions expected from the response. It must be a positive value.
         * Defaults to {@code SUGGESTION_COUNT_UNLIMITED} if not set.
         */
        @com.android.internal.util.DataClass.Generated.Member
        @android.annotation.NonNull
        public android.view.inputmethod.InlineSuggestionsRequest.Builder setMaxSuggestionCount(int value) {
            checkNotUsed();
            mBuilderFieldsSet |= 0x1;
            mMaxSuggestionCount = value;
            return this;
        }

        /**
         * The {@link InlinePresentationSpec} for each suggestion in the response. If the max suggestion
         * count is larger than the number of specs in the list, then the last spec is used for the
         * remainder of the suggestions. The list should not be empty.
         */
        @com.android.internal.util.DataClass.Generated.Member
        @android.annotation.NonNull
        public android.view.inputmethod.InlineSuggestionsRequest.Builder setInlinePresentationSpecs(@android.annotation.NonNull
        java.util.List<android.widget.inline.InlinePresentationSpec> value) {
            checkNotUsed();
            mBuilderFieldsSet |= 0x2;
            mInlinePresentationSpecs = value;
            return this;
        }

        /**
         *
         *
         * @see #setInlinePresentationSpecs
         */
        @com.android.internal.util.DataClass.Generated.Member
        @android.annotation.NonNull
        public android.view.inputmethod.InlineSuggestionsRequest.Builder addInlinePresentationSpecs(@android.annotation.NonNull
        android.widget.inline.InlinePresentationSpec value) {
            // You can refine this method's name by providing item's singular name, e.g.:
            // @DataClass.PluralOf("item")) mItems = ...
            if (mInlinePresentationSpecs == null)
                setInlinePresentationSpecs(new java.util.ArrayList<>());

            mInlinePresentationSpecs.add(value);
            return this;
        }

        /**
         * The package name of the app that requests for the inline suggestions and will host the
         * embedded suggestion views. The app does not have to set the value for the field because
         * it'll be set by the system for safety reasons.
         */
        @com.android.internal.util.DataClass.Generated.Member
        @java.lang.Override
        @android.annotation.NonNull
        android.view.inputmethod.InlineSuggestionsRequest.Builder setHostPackageName(@android.annotation.NonNull
        java.lang.String value) {
            checkNotUsed();
            mBuilderFieldsSet |= 0x4;
            mHostPackageName = value;
            return this;
        }

        /**
         * The IME provided locales for the request. If non-empty, the inline suggestions should
         * return languages from the supported locales. If not provided, it'll default to system locale.
         */
        @com.android.internal.util.DataClass.Generated.Member
        @android.annotation.NonNull
        public android.view.inputmethod.InlineSuggestionsRequest.Builder setSupportedLocales(@android.annotation.NonNull
        android.os.LocaleList value) {
            checkNotUsed();
            mBuilderFieldsSet |= 0x8;
            mSupportedLocales = value;
            return this;
        }

        /**
         * The extras state propagated from the IME to pass extra data.
         *
         * <p>Note: There should be no remote objects in the bundle, all included remote objects will
         * be removed from the bundle before transmission.</p>
         */
        @com.android.internal.util.DataClass.Generated.Member
        @android.annotation.NonNull
        public android.view.inputmethod.InlineSuggestionsRequest.Builder setExtras(@android.annotation.NonNull
        android.os.Bundle value) {
            checkNotUsed();
            mBuilderFieldsSet |= 0x10;
            mExtras = value;
            return this;
        }

        /**
         * The host input token of the IME that made the request. This will be set by the system for
         * safety reasons.
         *
         * @unknown 
         */
        @com.android.internal.util.DataClass.Generated.Member
        @java.lang.Override
        @android.annotation.NonNull
        android.view.inputmethod.InlineSuggestionsRequest.Builder setHostInputToken(@android.annotation.NonNull
        android.os.IBinder value) {
            checkNotUsed();
            mBuilderFieldsSet |= 0x20;
            mHostInputToken = value;
            return this;
        }

        /**
         * The host display id of the IME that made the request. This will be set by the system for
         * safety reasons.
         *
         * @unknown 
         */
        @com.android.internal.util.DataClass.Generated.Member
        @java.lang.Override
        @android.annotation.NonNull
        android.view.inputmethod.InlineSuggestionsRequest.Builder setHostDisplayId(int value) {
            checkNotUsed();
            mBuilderFieldsSet |= 0x40;
            mHostDisplayId = value;
            return this;
        }

        /**
         * Builds the instance. This builder should not be touched after calling this!
         */
        @android.annotation.NonNull
        public android.view.inputmethod.InlineSuggestionsRequest build() {
            checkNotUsed();
            mBuilderFieldsSet |= 0x80;// Mark builder used

            if ((mBuilderFieldsSet & 0x1) == 0) {
                mMaxSuggestionCount = android.view.inputmethod.InlineSuggestionsRequest.defaultMaxSuggestionCount();
            }
            if ((mBuilderFieldsSet & 0x4) == 0) {
                mHostPackageName = android.view.inputmethod.InlineSuggestionsRequest.defaultHostPackageName();
            }
            if ((mBuilderFieldsSet & 0x8) == 0) {
                mSupportedLocales = android.view.inputmethod.InlineSuggestionsRequest.defaultSupportedLocales();
            }
            if ((mBuilderFieldsSet & 0x10) == 0) {
                mExtras = android.view.inputmethod.InlineSuggestionsRequest.defaultExtras();
            }
            if ((mBuilderFieldsSet & 0x20) == 0) {
                mHostInputToken = android.view.inputmethod.InlineSuggestionsRequest.defaultHostInputToken();
            }
            if ((mBuilderFieldsSet & 0x40) == 0) {
                mHostDisplayId = android.view.inputmethod.InlineSuggestionsRequest.defaultHostDisplayId();
            }
            android.view.inputmethod.InlineSuggestionsRequest o = new android.view.inputmethod.InlineSuggestionsRequest(mMaxSuggestionCount, mInlinePresentationSpecs, mHostPackageName, mSupportedLocales, mExtras, mHostInputToken, mHostDisplayId);
            return o;
        }

        private void checkNotUsed() {
            if ((mBuilderFieldsSet & 0x80) != 0) {
                throw new java.lang.IllegalStateException("This Builder should not be reused. Use a new Builder instance instead");
            }
        }
    }

    @DataClass.Generated(time = 1588109685838L, codegenVersion = "1.0.15", sourceFile = "frameworks/base/core/java/android/view/inputmethod/InlineSuggestionsRequest.java", inputSignatures = "public static final  int SUGGESTION_COUNT_UNLIMITED\nprivate final  int mMaxSuggestionCount\nprivate final @android.annotation.NonNull java.util.List<android.widget.inline.InlinePresentationSpec> mInlinePresentationSpecs\nprivate @android.annotation.NonNull java.lang.String mHostPackageName\nprivate @android.annotation.NonNull android.os.LocaleList mSupportedLocales\nprivate @android.annotation.NonNull android.os.Bundle mExtras\nprivate @android.annotation.Nullable android.os.IBinder mHostInputToken\nprivate  int mHostDisplayId\npublic  void setHostInputToken(android.os.IBinder)\nprivate  boolean extrasEquals(android.os.Bundle)\nprivate  void parcelHostInputToken(android.os.Parcel,int)\nprivate @android.annotation.Nullable android.os.IBinder unparcelHostInputToken(android.os.Parcel)\npublic  void setHostDisplayId(int)\nprivate  void onConstructed()\npublic  void filterContentTypes()\nprivate static  int defaultMaxSuggestionCount()\nprivate static  java.lang.String defaultHostPackageName()\nprivate static  android.os.LocaleList defaultSupportedLocales()\nprivate static @android.annotation.Nullable android.os.IBinder defaultHostInputToken()\nprivate static @android.annotation.Nullable int defaultHostDisplayId()\nprivate static @android.annotation.NonNull android.os.Bundle defaultExtras()\nclass InlineSuggestionsRequest extends java.lang.Object implements [android.os.Parcelable]\n@com.android.internal.util.DataClass(genEqualsHashCode=true, genToString=true, genBuilder=true)\nabstract  android.view.inputmethod.InlineSuggestionsRequest.Builder setInlinePresentationSpecs(java.util.List<android.widget.inline.InlinePresentationSpec>)\nabstract  android.view.inputmethod.InlineSuggestionsRequest.Builder setHostPackageName(java.lang.String)\nabstract  android.view.inputmethod.InlineSuggestionsRequest.Builder setHostInputToken(android.os.IBinder)\nabstract  android.view.inputmethod.InlineSuggestionsRequest.Builder setHostDisplayId(int)\nclass BaseBuilder extends java.lang.Object implements []")
    @java.lang.Deprecated
    private void __metadata() {
    }
}

