/**
 * Copyright (C) 2017 The Android Open Source Project
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
 * Class that holds information about single font variation axis.
 */
public final class FontVariationAxis {
    @android.annotation.UnsupportedAppUsage(maxTargetSdk = Build.VERSION_CODES.P, trackingBug = 115609023)
    private final int mTag;

    private final java.lang.String mTagString;

    @android.annotation.UnsupportedAppUsage
    private final float mStyleValue;

    /**
     * Construct FontVariationAxis.
     *
     * The axis tag must contain four ASCII characters. Tag string that are longer or shorter than
     * four characters, or contains characters outside of U+0020..U+007E are invalid.
     *
     * @throws IllegalArgumentException
     * 		If given tag string is invalid.
     */
    public FontVariationAxis(@android.annotation.NonNull
    java.lang.String tagString, float styleValue) {
        if (!android.graphics.fonts.FontVariationAxis.isValidTag(tagString)) {
            throw new java.lang.IllegalArgumentException("Illegal tag pattern: " + tagString);
        }
        mTag = android.graphics.fonts.FontVariationAxis.makeTag(tagString);
        mTagString = tagString;
        mStyleValue = styleValue;
    }

    /**
     * Returns the OpenType style tag value.
     *
     * @unknown 
     */
    public int getOpenTypeTagValue() {
        return mTag;
    }

    /**
     * Returns the variable font axis tag associated to this axis.
     */
    public java.lang.String getTag() {
        return mTagString;
    }

    /**
     * Returns the style value associated to the given axis for this font.
     */
    public float getStyleValue() {
        return mStyleValue;
    }

    /**
     * Returns a valid font variation setting string for this object.
     */
    @java.lang.Override
    @android.annotation.NonNull
    public java.lang.String toString() {
        return (("'" + mTagString) + "' ") + java.lang.Float.toString(mStyleValue);
    }

    /**
     * The 'tag' attribute value is read as four character values between U+0020 and U+007E
     * inclusive.
     */
    private static final java.util.regex.Pattern TAG_PATTERN = java.util.regex.Pattern.compile("[ -~]{4}");

    /**
     * Returns true if 'tagString' is valid for font variation axis tag.
     */
    private static boolean isValidTag(java.lang.String tagString) {
        return (tagString != null) && android.graphics.fonts.FontVariationAxis.TAG_PATTERN.matcher(tagString).matches();
    }

    /**
     * The 'styleValue' attribute has an optional leading '-', followed by '<digits>',
     * '<digits>.<digits>', or '.<digits>' where '<digits>' is one or more of [0-9].
     */
    private static final java.util.regex.Pattern STYLE_VALUE_PATTERN = java.util.regex.Pattern.compile("-?(([0-9]+(\\.[0-9]+)?)|(\\.[0-9]+))");

    private static boolean isValidValueFormat(java.lang.String valueString) {
        return (valueString != null) && android.graphics.fonts.FontVariationAxis.STYLE_VALUE_PATTERN.matcher(valueString).matches();
    }

    /**
     *
     *
     * @unknown 
     */
    public static int makeTag(java.lang.String tagString) {
        final char c1 = tagString.charAt(0);
        final char c2 = tagString.charAt(1);
        final char c3 = tagString.charAt(2);
        final char c4 = tagString.charAt(3);
        return (((c1 << 24) | (c2 << 16)) | (c3 << 8)) | c4;
    }

    /**
     * Construct FontVariationAxis array from font variation settings.
     *
     * The settings string is constructed from multiple pairs of axis tag and style values. The axis
     * tag must contain four ASCII characters and must be wrapped with single quotes (U+0027) or
     * double quotes (U+0022). Axis strings that are longer or shorter than four characters, or
     * contain characters outside of U+0020..U+007E are invalid. If a specified axis name is not
     * defined in the font, the settings will be ignored.
     *
     * <pre>
     *   FontVariationAxis.fromFontVariationSettings("'wdth' 1.0");
     *   FontVariationAxis.fromFontVariationSettings("'AX  ' 1.0, 'FB  ' 2.0");
     * </pre>
     *
     * @param settings
     * 		font variation settings.
     * @return FontVariationAxis[] the array of parsed font variation axis. {@code null} if settings
    has no font variation settings.
     * @throws IllegalArgumentException
     * 		If given string is not a valid font variation settings
     * 		format.
     */
    @android.annotation.Nullable
    public static android.graphics.fonts.FontVariationAxis[] fromFontVariationSettings(@android.annotation.Nullable
    java.lang.String settings) {
        if ((settings == null) || settings.isEmpty()) {
            return null;
        }
        final java.util.ArrayList<android.graphics.fonts.FontVariationAxis> axisList = new java.util.ArrayList<>();
        final int length = settings.length();
        for (int i = 0; i < length; i++) {
            final char c = settings.charAt(i);
            if (java.lang.Character.isWhitespace(c)) {
                continue;
            }
            if (((!((c == '\'') || (c == '"'))) || (length < (i + 6))) || (settings.charAt(i + 5) != c)) {
                throw new java.lang.IllegalArgumentException("Tag should be wrapped with double or single quote: " + settings);
            }
            final java.lang.String tagString = settings.substring(i + 1, i + 5);
            i += 6;// Move to end of tag.

            int endOfValueString = settings.indexOf(',', i);
            if (endOfValueString == (-1)) {
                endOfValueString = length;
            }
            final float value;
            try {
                // Float.parseFloat ignores leading/trailing whitespaces.
                value = java.lang.Float.parseFloat(settings.substring(i, endOfValueString));
            } catch (java.lang.NumberFormatException e) {
                throw new java.lang.IllegalArgumentException("Failed to parse float string: " + e.getMessage());
            }
            axisList.add(new android.graphics.fonts.FontVariationAxis(tagString, value));
            i = endOfValueString;
        }
        if (axisList.isEmpty()) {
            return null;
        }
        return axisList.toArray(new android.graphics.fonts.FontVariationAxis[0]);
    }

    /**
     * Stringify the array of FontVariationAxis.
     *
     * @param axes
     * 		an array of FontVariationAxis.
     * @return String a valid font variation settings string.
     */
    @android.annotation.NonNull
    public static java.lang.String toFontVariationSettings(@android.annotation.Nullable
    android.graphics.fonts.FontVariationAxis[] axes) {
        if (axes == null) {
            return "";
        }
        return android.text.TextUtils.join(",", axes);
    }

    @java.lang.Override
    public boolean equals(@android.annotation.Nullable
    java.lang.Object o) {
        if (o == this) {
            return true;
        }
        if ((o == null) || (!(o instanceof android.graphics.fonts.FontVariationAxis))) {
            return false;
        }
        android.graphics.fonts.FontVariationAxis axis = ((android.graphics.fonts.FontVariationAxis) (o));
        return (axis.mTag == mTag) && (axis.mStyleValue == mStyleValue);
    }

    @java.lang.Override
    public int hashCode() {
        return java.util.Objects.hash(mTag, mStyleValue);
    }
}

