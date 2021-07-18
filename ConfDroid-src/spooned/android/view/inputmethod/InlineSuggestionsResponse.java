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
 * This class represents an inline suggestion response. See {@link InlineSuggestion} for more
 * information.
 */
// @formatter:on
// End of generated code
@com.android.internal.util.DataClass(genEqualsHashCode = true, genToString = true, genHiddenConstructor = true)
public final class InlineSuggestionsResponse implements android.os.Parcelable {
    @android.annotation.NonNull
    private final java.util.List<android.view.inputmethod.InlineSuggestion> mInlineSuggestions;

    /**
     * Creates a new {@link InlineSuggestionsResponse}, for testing purpose.
     *
     * @unknown 
     */
    @android.annotation.TestApi
    @android.annotation.NonNull
    public static android.view.inputmethod.InlineSuggestionsResponse newInlineSuggestionsResponse(@android.annotation.NonNull
    java.util.List<android.view.inputmethod.InlineSuggestion> inlineSuggestions) {
        return new android.view.inputmethod.InlineSuggestionsResponse(inlineSuggestions);
    }

    // Code below generated by codegen v1.0.14.
    // 
    // DO NOT MODIFY!
    // CHECKSTYLE:OFF Generated code
    // 
    // To regenerate run:
    // $ codegen $ANDROID_BUILD_TOP/frameworks/base/core/java/android/view/inputmethod/InlineSuggestionsResponse.java
    // 
    // To exclude the generated code from IntelliJ auto-formatting enable (one-time):
    // Settings > Editor > Code Style > Formatter Control
    // @formatter:off
    /**
     * Creates a new InlineSuggestionsResponse.
     *
     * @unknown 
     */
    @com.android.internal.util.DataClass.Generated.Member
    public InlineSuggestionsResponse(@android.annotation.NonNull
    java.util.List<android.view.inputmethod.InlineSuggestion> inlineSuggestions) {
        this.mInlineSuggestions = inlineSuggestions;
        com.android.internal.util.AnnotationValidations.validate(android.annotation.NonNull.class, null, mInlineSuggestions);
        // onConstructed(); // You can define this method to get a callback
    }

    @com.android.internal.util.DataClass.Generated.Member
    @android.annotation.NonNull
    public java.util.List<android.view.inputmethod.InlineSuggestion> getInlineSuggestions() {
        return mInlineSuggestions;
    }

    @java.lang.Override
    @com.android.internal.util.DataClass.Generated.Member
    public java.lang.String toString() {
        // You can override field toString logic by defining methods like:
        // String fieldNameToString() { ... }
        return (("InlineSuggestionsResponse { " + "inlineSuggestions = ") + mInlineSuggestions) + " }";
    }

    @java.lang.Override
    @com.android.internal.util.DataClass.Generated.Member
    public boolean equals(@android.annotation.Nullable
    java.lang.Object o) {
        // You can override field equality logic by defining either of the methods like:
        // boolean fieldNameEquals(InlineSuggestionsResponse other) { ... }
        // boolean fieldNameEquals(FieldType otherValue) { ... }
        if (this == o)
            return true;

        if ((o == null) || (getClass() != o.getClass()))
            return false;

        @java.lang.SuppressWarnings("unchecked")
        android.view.inputmethod.InlineSuggestionsResponse that = ((android.view.inputmethod.InlineSuggestionsResponse) (o));
        // noinspection PointlessBooleanExpression
        return true && java.util.Objects.equals(mInlineSuggestions, that.mInlineSuggestions);
    }

    @java.lang.Override
    @com.android.internal.util.DataClass.Generated.Member
    public int hashCode() {
        // You can override field hashCode logic by defining methods like:
        // int fieldNameHashCode() { ... }
        int _hash = 1;
        _hash = (31 * _hash) + java.util.Objects.hashCode(mInlineSuggestions);
        return _hash;
    }

    @java.lang.Override
    @com.android.internal.util.DataClass.Generated.Member
    public void writeToParcel(@android.annotation.NonNull
    android.os.Parcel dest, int flags) {
        // You can override field parcelling by defining methods like:
        // void parcelFieldName(Parcel dest, int flags) { ... }
        dest.writeParcelableList(mInlineSuggestions, flags);
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
    InlineSuggestionsResponse(@android.annotation.NonNull
    android.os.Parcel in) {
        // You can override field unparcelling by defining methods like:
        // static FieldType unparcelFieldName(Parcel in) { ... }
        java.util.List<android.view.inputmethod.InlineSuggestion> inlineSuggestions = new java.util.ArrayList<>();
        in.readParcelableList(inlineSuggestions, android.view.inputmethod.InlineSuggestion.class.getClassLoader());
        this.mInlineSuggestions = inlineSuggestions;
        com.android.internal.util.AnnotationValidations.validate(android.annotation.NonNull.class, null, mInlineSuggestions);
        // onConstructed(); // You can define this method to get a callback
    }

    @com.android.internal.util.DataClass.Generated.Member
    @android.annotation.NonNull
    public static final android.os.Parcelable.Creator<android.view.inputmethod.InlineSuggestionsResponse> CREATOR = new android.os.Parcelable.Creator<android.view.inputmethod.InlineSuggestionsResponse>() {
        @java.lang.Override
        public android.view.inputmethod.InlineSuggestionsResponse[] newArray(int size) {
            return new android.view.inputmethod.InlineSuggestionsResponse[size];
        }

        @java.lang.Override
        public android.view.inputmethod.InlineSuggestionsResponse createFromParcel(@android.annotation.NonNull
        android.os.Parcel in) {
            return new android.view.inputmethod.InlineSuggestionsResponse(in);
        }
    };

    @DataClass.Generated(time = 1578972149519L, codegenVersion = "1.0.14", sourceFile = "frameworks/base/core/java/android/view/inputmethod/InlineSuggestionsResponse.java", inputSignatures = "private final @android.annotation.NonNull java.util.List<android.view.inputmethod.InlineSuggestion> mInlineSuggestions\npublic static @android.annotation.TestApi @android.annotation.NonNull android.view.inputmethod.InlineSuggestionsResponse newInlineSuggestionsResponse(java.util.List<android.view.inputmethod.InlineSuggestion>)\nclass InlineSuggestionsResponse extends java.lang.Object implements [android.os.Parcelable]\n@com.android.internal.util.DataClass(genEqualsHashCode=true, genToString=true, genHiddenConstructor=true)")
    @java.lang.Deprecated
    private void __metadata() {
    }
}

