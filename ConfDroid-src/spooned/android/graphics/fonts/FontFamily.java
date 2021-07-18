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
package android.graphics.fonts;


/**
 * A font family class can be used for creating Typeface.
 *
 * <p>
 * A font family is a bundle of fonts for drawing text in various styles.
 * For example, you can bundle regular style font and bold style font into a single font family,
 * then system will select the correct style font from family for drawing.
 *
 * <pre>
 *  FontFamily family = new FontFamily.Builder(new Font.Builder("regular.ttf").build())
 *      .addFont(new Font.Builder("bold.ttf").build()).build();
 *  Typeface typeface = new Typeface.Builder2(family).build();
 *
 *  SpannableStringBuilder ssb = new SpannableStringBuilder("Hello, World.");
 *  ssb.setSpan(new StyleSpan(Typeface.Bold), 6, 12, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
 *
 *  textView.setTypeface(typeface);
 *  textView.setText(ssb);
 * </pre>
 *
 * In this example, "Hello, " is drawn with "regular.ttf", and "World." is drawn with "bold.ttf".
 *
 * If there is no font exactly matches with the text style, the system will select the closest font.
 * </p>
 */
public final class FontFamily {
    private static final java.lang.String TAG = "FontFamily";

    /**
     * A builder class for creating new FontFamily.
     */
    public static final class Builder {
        private static final libcore.util.NativeAllocationRegistry sFamilyRegistory = libcore.util.NativeAllocationRegistry.createMalloced(android.graphics.fonts.FontFamily.class.getClassLoader(), android.graphics.fonts.FontFamily.Builder.nGetReleaseNativeFamily());

        private final java.util.ArrayList<android.graphics.fonts.Font> mFonts = new java.util.ArrayList<>();

        private final java.util.HashSet<java.lang.Integer> mStyleHashSet = new java.util.HashSet<>();

        /**
         * Constructs a builder.
         *
         * @param font
         * 		a font
         */
        public Builder(@android.annotation.NonNull
        android.graphics.fonts.Font font) {
            com.android.internal.util.Preconditions.checkNotNull(font, "font can not be null");
            mStyleHashSet.add(android.graphics.fonts.FontFamily.Builder.makeStyleIdentifier(font));
            mFonts.add(font);
        }

        /**
         * Adds different style font to the builder.
         *
         * System will select the font if the text style is closest to the font.
         * If the same style font is already added to the builder, this method will fail with
         * {@link IllegalArgumentException}.
         *
         * Note that system assumes all fonts bundled in FontFamily have the same coverage for the
         * code points. For example, regular style font and bold style font must have the same code
         * point coverage, otherwise some character may be shown as tofu.
         *
         * @param font
         * 		a font
         * @return this builder
         */
        @android.annotation.NonNull
        public android.graphics.fonts.FontFamily.Builder addFont(@android.annotation.NonNull
        android.graphics.fonts.Font font) {
            com.android.internal.util.Preconditions.checkNotNull(font, "font can not be null");
            if (!mStyleHashSet.add(android.graphics.fonts.FontFamily.Builder.makeStyleIdentifier(font))) {
                throw new java.lang.IllegalArgumentException(font + " has already been added");
            }
            mFonts.add(font);
            return this;
        }

        /**
         * Build the font family
         *
         * @return a font family
         */
        @android.annotation.NonNull
        public android.graphics.fonts.FontFamily build() {
            return /* isCustomFallback */
            build("", FontConfig.Family.VARIANT_DEFAULT, true);
        }

        /**
         *
         *
         * @unknown 
         */
        @android.annotation.NonNull
        public android.graphics.fonts.FontFamily build(@android.annotation.NonNull
        java.lang.String langTags, int variant, boolean isCustomFallback) {
            final long builderPtr = android.graphics.fonts.FontFamily.Builder.nInitBuilder();
            for (int i = 0; i < mFonts.size(); ++i) {
                android.graphics.fonts.FontFamily.Builder.nAddFont(builderPtr, mFonts.get(i).getNativePtr());
            }
            final long ptr = android.graphics.fonts.FontFamily.Builder.nBuild(builderPtr, langTags, variant, isCustomFallback);
            final android.graphics.fonts.FontFamily family = new android.graphics.fonts.FontFamily(mFonts, ptr);
            android.graphics.fonts.FontFamily.Builder.sFamilyRegistory.registerNativeAllocation(family, ptr);
            return family;
        }

        private static int makeStyleIdentifier(@android.annotation.NonNull
        android.graphics.fonts.Font font) {
            return font.getStyle().getWeight() | (font.getStyle().getSlant() << 16);
        }

        private static native long nInitBuilder();

        @dalvik.annotation.optimization.CriticalNative
        private static native void nAddFont(long builderPtr, long fontPtr);

        private static native long nBuild(long builderPtr, java.lang.String langTags, int variant, boolean isCustomFallback);

        @dalvik.annotation.optimization.CriticalNative
        private static native long nGetReleaseNativeFamily();
    }

    private final java.util.ArrayList<android.graphics.fonts.Font> mFonts;

    private final long mNativePtr;

    // Use Builder instead.
    private FontFamily(@android.annotation.NonNull
    java.util.ArrayList<android.graphics.fonts.Font> fonts, long ptr) {
        mFonts = fonts;
        mNativePtr = ptr;
    }

    /**
     * Returns a font
     *
     * @param index
     * 		an index of the font
     * @return a registered font
     */
    @android.annotation.NonNull
    public android.graphics.fonts.Font getFont(@android.annotation.IntRange(from = 0)
    int index) {
        return mFonts.get(index);
    }

    /**
     * Returns the number of fonts in this FontFamily.
     *
     * @return the number of fonts registered in this family.
     */
    @android.annotation.IntRange(from = 1)
    public int getSize() {
        return mFonts.size();
    }

    /**
     *
     *
     * @unknown 
     */
    public long getNativePtr() {
        return mNativePtr;
    }
}

