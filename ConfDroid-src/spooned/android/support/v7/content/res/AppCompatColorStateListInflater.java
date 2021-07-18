/**
 * Copyright (C) 2015 The Android Open Source Project
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
package android.support.v7.content.res;


final class AppCompatColorStateListInflater {
    private static final int DEFAULT_COLOR = android.graphics.Color.RED;

    private AppCompatColorStateListInflater() {
    }

    /**
     * Creates a ColorStateList from an XML document using given a set of
     * {@link Resources} and a {@link Theme}.
     *
     * @param r
     * 		Resources against which the ColorStateList should be inflated.
     * @param parser
     * 		Parser for the XML document defining the ColorStateList.
     * @param theme
     * 		Optional theme to apply to the color state list, may be
     * 		{@code null}.
     * @return A new color state list.
     */
    @android.support.annotation.NonNull
    public static android.content.res.ColorStateList createFromXml(@android.support.annotation.NonNull
    android.content.res.Resources r, @android.support.annotation.NonNull
    org.xmlpull.v1.XmlPullParser parser, @android.support.annotation.Nullable
    android.content.res.Resources.Theme theme) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        final android.util.AttributeSet attrs = android.util.Xml.asAttributeSet(parser);
        int type;
        while (((type = parser.next()) != org.xmlpull.v1.XmlPullParser.START_TAG) && (type != org.xmlpull.v1.XmlPullParser.END_DOCUMENT)) {
            // Seek parser to start tag.
        } 
        if (type != org.xmlpull.v1.XmlPullParser.START_TAG) {
            throw new org.xmlpull.v1.XmlPullParserException("No start tag found");
        }
        return android.support.v7.content.res.AppCompatColorStateListInflater.createFromXmlInner(r, parser, attrs, theme);
    }

    /**
     * Create from inside an XML document. Called on a parser positioned at a
     * tag in an XML document, tries to create a ColorStateList from that tag.
     *
     * @throws XmlPullParserException
     * 		if the current tag is not &lt;selector>
     * @return A new color state list for the current tag.
     */
    @android.support.annotation.NonNull
    private static android.content.res.ColorStateList createFromXmlInner(@android.support.annotation.NonNull
    android.content.res.Resources r, @android.support.annotation.NonNull
    org.xmlpull.v1.XmlPullParser parser, @android.support.annotation.NonNull
    android.util.AttributeSet attrs, @android.support.annotation.Nullable
    android.content.res.Resources.Theme theme) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        final java.lang.String name = parser.getName();
        if (!name.equals("selector")) {
            throw new org.xmlpull.v1.XmlPullParserException((parser.getPositionDescription() + ": invalid color state list tag ") + name);
        }
        return android.support.v7.content.res.AppCompatColorStateListInflater.inflate(r, parser, attrs, theme);
    }

    /**
     * Fill in this object based on the contents of an XML "selector" element.
     */
    private static android.content.res.ColorStateList inflate(@android.support.annotation.NonNull
    android.content.res.Resources r, @android.support.annotation.NonNull
    org.xmlpull.v1.XmlPullParser parser, @android.support.annotation.NonNull
    android.util.AttributeSet attrs, @android.support.annotation.Nullable
    android.content.res.Resources.Theme theme) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        final int innerDepth = parser.getDepth() + 1;
        int depth;
        int type;
        int defaultColor = android.support.v7.content.res.AppCompatColorStateListInflater.DEFAULT_COLOR;
        int[][] stateSpecList = new int[20][];
        int[] colorList = new int[stateSpecList.length];
        int listSize = 0;
        while (((type = parser.next()) != org.xmlpull.v1.XmlPullParser.END_DOCUMENT) && (((depth = parser.getDepth()) >= innerDepth) || (type != org.xmlpull.v1.XmlPullParser.END_TAG))) {
            if (((type != org.xmlpull.v1.XmlPullParser.START_TAG) || (depth > innerDepth)) || (!parser.getName().equals("item"))) {
                continue;
            }
            final android.content.res.TypedArray a = android.support.v7.content.res.AppCompatColorStateListInflater.obtainAttributes(r, theme, attrs, R.styleable.ColorStateListItem);
            final int baseColor = a.getColor(R.styleable.ColorStateListItem_android_color, android.graphics.Color.MAGENTA);
            float alphaMod = 1.0F;
            if (a.hasValue(R.styleable.ColorStateListItem_android_alpha)) {
                alphaMod = a.getFloat(R.styleable.ColorStateListItem_android_alpha, alphaMod);
            } else
                if (a.hasValue(R.styleable.ColorStateListItem_alpha)) {
                    alphaMod = a.getFloat(R.styleable.ColorStateListItem_alpha, alphaMod);
                }

            a.recycle();
            // Parse all unrecognized attributes as state specifiers.
            int j = 0;
            final int numAttrs = attrs.getAttributeCount();
            int[] stateSpec = new int[numAttrs];
            for (int i = 0; i < numAttrs; i++) {
                final int stateResId = attrs.getAttributeNameResource(i);
                if (((stateResId != android.R.attr.color) && (stateResId != android.R.attr.alpha)) && (stateResId != R.attr.alpha)) {
                    // Unrecognized attribute, add to state set
                    stateSpec[j++] = (attrs.getAttributeBooleanValue(i, false)) ? stateResId : -stateResId;
                }
            }
            stateSpec = android.util.StateSet.trimStateSet(stateSpec, j);
            // Apply alpha modulation. If we couldn't resolve the color or
            // alpha yet, the default values leave us enough information to
            // modulate again during applyTheme().
            final int color = android.support.v7.content.res.AppCompatColorStateListInflater.modulateColorAlpha(baseColor, alphaMod);
            if ((listSize == 0) || (stateSpec.length == 0)) {
                defaultColor = color;
            }
            colorList = android.support.v7.content.res.GrowingArrayUtils.append(colorList, listSize, color);
            stateSpecList = android.support.v7.content.res.GrowingArrayUtils.append(stateSpecList, listSize, stateSpec);
            listSize++;
        } 
        int[] colors = new int[listSize];
        int[][] stateSpecs = new int[listSize][];
        java.lang.System.arraycopy(colorList, 0, colors, 0, listSize);
        java.lang.System.arraycopy(stateSpecList, 0, stateSpecs, 0, listSize);
        return new android.content.res.ColorStateList(stateSpecs, colors);
    }

    private static android.content.res.TypedArray obtainAttributes(android.content.res.Resources res, android.content.res.Resources.Theme theme, android.util.AttributeSet set, int[] attrs) {
        return theme == null ? res.obtainAttributes(set, attrs) : theme.obtainStyledAttributes(set, attrs, 0, 0);
    }

    private static int modulateColorAlpha(int color, float alphaMod) {
        return android.support.v4.graphics.ColorUtils.setAlphaComponent(color, java.lang.Math.round(android.graphics.Color.alpha(color) * alphaMod));
    }
}

