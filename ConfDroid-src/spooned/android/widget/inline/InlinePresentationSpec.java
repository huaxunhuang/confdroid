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
package android.widget.inline;


/**
 * This class represents the presentation specification by which an inline suggestion
 * should abide when constructing its UI. Since suggestions are inlined in a
 * host application while provided by another source, they need to be consistent
 * with the host's look at feel to allow building smooth and integrated UIs.
 */
// @formatter:on
// End of generated code
@com.android.internal.util.DataClass(genEqualsHashCode = true, genToString = true, genBuilder = true)
public final class InlinePresentationSpec implements android.os.Parcelable {
    /**
     * The minimal size of the suggestion.
     */
    @android.annotation.NonNull
    private final android.util.Size mMinSize;

    /**
     * The maximal size of the suggestion.
     */
    @android.annotation.NonNull
    private final android.util.Size mMaxSize;

    /**
     * The extras encoding the UI style information. Defaults to {@code Bundle.Empty} in which case
     * the default system UI style will be used.
     *
     * <p>Note: There should be no remote objects in the bundle, all included remote objects will
     * be removed from the bundle before transmission.</p>
     */
    @android.annotation.NonNull
    private final android.os.Bundle mStyle;

    @android.annotation.NonNull
    private static android.os.Bundle defaultStyle() {
        return android.os.Bundle.EMPTY;
    }

    private boolean styleEquals(@android.annotation.NonNull
    android.os.Bundle style) {
        return com.android.internal.widget.InlinePresentationStyleUtils.bundleEquals(mStyle, style);
    }

    /**
     * Removes the remote objects from the {@code mStyle}.
     *
     * @unknown 
     */
    public void filterContentTypes() {
        com.android.internal.widget.InlinePresentationStyleUtils.filterContentTypes(mStyle);
    }

    /**
     *
     *
     * @unknown 
     */
    @com.android.internal.util.DataClass.Suppress({ "setMaxSize", "setMinSize" })
    static abstract class BaseBuilder {}

    // Code below generated by codegen v1.0.15.
    // 
    // DO NOT MODIFY!
    // CHECKSTYLE:OFF Generated code
    // 
    // To regenerate run:
    // $ codegen $ANDROID_BUILD_TOP/frameworks/base/core/java/android/widget/inline/InlinePresentationSpec.java
    // 
    // To exclude the generated code from IntelliJ auto-formatting enable (one-time):
    // Settings > Editor > Code Style > Formatter Control
    // @formatter:off
    /* package-private */
    @com.android.internal.util.DataClass.Generated.Member
    InlinePresentationSpec(@android.annotation.NonNull
    android.util.Size minSize, @android.annotation.NonNull
    android.util.Size maxSize, @android.annotation.NonNull
    android.os.Bundle style) {
        this.mMinSize = minSize;
        com.android.internal.util.AnnotationValidations.validate(android.annotation.NonNull.class, null, mMinSize);
        this.mMaxSize = maxSize;
        com.android.internal.util.AnnotationValidations.validate(android.annotation.NonNull.class, null, mMaxSize);
        this.mStyle = style;
        com.android.internal.util.AnnotationValidations.validate(android.annotation.NonNull.class, null, mStyle);
        // onConstructed(); // You can define this method to get a callback
    }

    /**
     * The minimal size of the suggestion.
     */
    @com.android.internal.util.DataClass.Generated.Member
    @android.annotation.NonNull
    public android.util.Size getMinSize() {
        return mMinSize;
    }

    /**
     * The maximal size of the suggestion.
     */
    @com.android.internal.util.DataClass.Generated.Member
    @android.annotation.NonNull
    public android.util.Size getMaxSize() {
        return mMaxSize;
    }

    /**
     * The extras encoding the UI style information. Defaults to {@code Bundle.Empty} in which case
     * the default system UI style will be used.
     *
     * <p>Note: There should be no remote objects in the bundle, all included remote objects will
     * be removed from the bundle before transmission.</p>
     */
    @com.android.internal.util.DataClass.Generated.Member
    @android.annotation.NonNull
    public android.os.Bundle getStyle() {
        return mStyle;
    }

    @java.lang.Override
    @com.android.internal.util.DataClass.Generated.Member
    public java.lang.String toString() {
        // You can override field toString logic by defining methods like:
        // String fieldNameToString() { ... }
        return (((((((("InlinePresentationSpec { " + "minSize = ") + mMinSize) + ", ") + "maxSize = ") + mMaxSize) + ", ") + "style = ") + mStyle) + " }";
    }

    @java.lang.Override
    @com.android.internal.util.DataClass.Generated.Member
    public boolean equals(@android.annotation.Nullable
    java.lang.Object o) {
        // You can override field equality logic by defining either of the methods like:
        // boolean fieldNameEquals(InlinePresentationSpec other) { ... }
        // boolean fieldNameEquals(FieldType otherValue) { ... }
        if (this == o)
            return true;

        if ((o == null) || (getClass() != o.getClass()))
            return false;

        @java.lang.SuppressWarnings("unchecked")
        android.widget.inline.InlinePresentationSpec that = ((android.widget.inline.InlinePresentationSpec) (o));
        // noinspection PointlessBooleanExpression
        return ((true && java.util.Objects.equals(mMinSize, that.mMinSize)) && java.util.Objects.equals(mMaxSize, that.mMaxSize)) && styleEquals(that.mStyle);
    }

    @java.lang.Override
    @com.android.internal.util.DataClass.Generated.Member
    public int hashCode() {
        // You can override field hashCode logic by defining methods like:
        // int fieldNameHashCode() { ... }
        int _hash = 1;
        _hash = (31 * _hash) + java.util.Objects.hashCode(mMinSize);
        _hash = (31 * _hash) + java.util.Objects.hashCode(mMaxSize);
        _hash = (31 * _hash) + java.util.Objects.hashCode(mStyle);
        return _hash;
    }

    @java.lang.Override
    @com.android.internal.util.DataClass.Generated.Member
    public void writeToParcel(@android.annotation.NonNull
    android.os.Parcel dest, int flags) {
        // You can override field parcelling by defining methods like:
        // void parcelFieldName(Parcel dest, int flags) { ... }
        dest.writeSize(mMinSize);
        dest.writeSize(mMaxSize);
        dest.writeBundle(mStyle);
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
    InlinePresentationSpec(@android.annotation.NonNull
    android.os.Parcel in) {
        // You can override field unparcelling by defining methods like:
        // static FieldType unparcelFieldName(Parcel in) { ... }
        android.util.Size minSize = ((android.util.Size) (in.readSize()));
        android.util.Size maxSize = ((android.util.Size) (in.readSize()));
        android.os.Bundle style = in.readBundle();
        this.mMinSize = minSize;
        com.android.internal.util.AnnotationValidations.validate(android.annotation.NonNull.class, null, mMinSize);
        this.mMaxSize = maxSize;
        com.android.internal.util.AnnotationValidations.validate(android.annotation.NonNull.class, null, mMaxSize);
        this.mStyle = style;
        com.android.internal.util.AnnotationValidations.validate(android.annotation.NonNull.class, null, mStyle);
        // onConstructed(); // You can define this method to get a callback
    }

    @com.android.internal.util.DataClass.Generated.Member
    @android.annotation.NonNull
    public static final android.os.Parcelable.Creator<android.widget.inline.InlinePresentationSpec> CREATOR = new android.os.Parcelable.Creator<android.widget.inline.InlinePresentationSpec>() {
        @java.lang.Override
        public android.widget.inline.InlinePresentationSpec[] newArray(int size) {
            return new android.widget.inline.InlinePresentationSpec[size];
        }

        @java.lang.Override
        public android.widget.inline.InlinePresentationSpec createFromParcel(@android.annotation.NonNull
        android.os.Parcel in) {
            return new android.widget.inline.InlinePresentationSpec(in);
        }
    };

    /**
     * A builder for {@link InlinePresentationSpec}
     */
    @java.lang.SuppressWarnings("WeakerAccess")
    @com.android.internal.util.DataClass.Generated.Member
    public static final class Builder extends android.widget.inline.InlinePresentationSpec.BaseBuilder {
        @android.annotation.NonNull
        private android.util.Size mMinSize;

        @android.annotation.NonNull
        private android.util.Size mMaxSize;

        @android.annotation.NonNull
        private android.os.Bundle mStyle;

        private long mBuilderFieldsSet = 0L;

        /**
         * Creates a new Builder.
         *
         * @param minSize
         * 		The minimal size of the suggestion.
         * @param maxSize
         * 		The maximal size of the suggestion.
         */
        public Builder(@android.annotation.NonNull
        android.util.Size minSize, @android.annotation.NonNull
        android.util.Size maxSize) {
            mMinSize = minSize;
            com.android.internal.util.AnnotationValidations.validate(android.annotation.NonNull.class, null, mMinSize);
            mMaxSize = maxSize;
            com.android.internal.util.AnnotationValidations.validate(android.annotation.NonNull.class, null, mMaxSize);
        }

        /**
         * The extras encoding the UI style information. Defaults to {@code Bundle.Empty} in which case
         * the default system UI style will be used.
         *
         * <p>Note: There should be no remote objects in the bundle, all included remote objects will
         * be removed from the bundle before transmission.</p>
         */
        @com.android.internal.util.DataClass.Generated.Member
        @android.annotation.NonNull
        public android.widget.inline.InlinePresentationSpec.Builder setStyle(@android.annotation.NonNull
        android.os.Bundle value) {
            checkNotUsed();
            mBuilderFieldsSet |= 0x4;
            mStyle = value;
            return this;
        }

        /**
         * Builds the instance. This builder should not be touched after calling this!
         */
        @android.annotation.NonNull
        public android.widget.inline.InlinePresentationSpec build() {
            checkNotUsed();
            mBuilderFieldsSet |= 0x8;// Mark builder used

            if ((mBuilderFieldsSet & 0x4) == 0) {
                mStyle = android.widget.inline.InlinePresentationSpec.defaultStyle();
            }
            android.widget.inline.InlinePresentationSpec o = new android.widget.inline.InlinePresentationSpec(mMinSize, mMaxSize, mStyle);
            return o;
        }

        private void checkNotUsed() {
            if ((mBuilderFieldsSet & 0x8) != 0) {
                throw new java.lang.IllegalStateException("This Builder should not be reused. Use a new Builder instance instead");
            }
        }
    }

    @DataClass.Generated(time = 1588109681295L, codegenVersion = "1.0.15", sourceFile = "frameworks/base/core/java/android/widget/inline/InlinePresentationSpec.java", inputSignatures = "private final @android.annotation.NonNull android.util.Size mMinSize\nprivate final @android.annotation.NonNull android.util.Size mMaxSize\nprivate final @android.annotation.NonNull android.os.Bundle mStyle\nprivate static @android.annotation.NonNull android.os.Bundle defaultStyle()\nprivate  boolean styleEquals(android.os.Bundle)\npublic  void filterContentTypes()\nclass InlinePresentationSpec extends java.lang.Object implements [android.os.Parcelable]\n@com.android.internal.util.DataClass(genEqualsHashCode=true, genToString=true, genBuilder=true)\nclass BaseBuilder extends java.lang.Object implements []")
    @java.lang.Deprecated
    private void __metadata() {
    }
}

