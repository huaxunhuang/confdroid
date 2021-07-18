/**
 * Copyright (C) 2014 The Android Open Source Project
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
package android.graphics;


/**
 * Parser for font config files.
 *
 * @unknown 
 */
public class FontListParser {
    /* Parse fallback list (no names) */
    @android.annotation.UnsupportedAppUsage
    public static android.text.FontConfig parse(java.io.InputStream in) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        return android.graphics.FontListParser.parse(in, "/system/fonts");
    }

    /**
     * Parse the fonts.xml
     */
    public static android.text.FontConfig parse(java.io.InputStream in, java.lang.String fontDir) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        try {
            org.xmlpull.v1.XmlPullParser parser = android.util.Xml.newPullParser();
            parser.setInput(in, null);
            parser.nextTag();
            return android.graphics.FontListParser.readFamilies(parser, fontDir);
        } finally {
            in.close();
        }
    }

    private static android.text.FontConfig readFamilies(org.xmlpull.v1.XmlPullParser parser, java.lang.String fontDir) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        java.util.List<android.text.FontConfig.Family> families = new java.util.ArrayList<>();
        java.util.List<android.text.FontConfig.Alias> aliases = new java.util.ArrayList<>();
        parser.require(org.xmlpull.v1.XmlPullParser.START_TAG, null, "familyset");
        while (parser.next() != org.xmlpull.v1.XmlPullParser.END_TAG) {
            if (parser.getEventType() != org.xmlpull.v1.XmlPullParser.START_TAG)
                continue;

            java.lang.String tag = parser.getName();
            if (tag.equals("family")) {
                families.add(android.graphics.FontListParser.readFamily(parser, fontDir));
            } else
                if (tag.equals("alias")) {
                    aliases.add(android.graphics.FontListParser.readAlias(parser));
                } else {
                    android.graphics.FontListParser.skip(parser);
                }

        } 
        return new android.text.FontConfig(families.toArray(new android.text.FontConfig.Family[families.size()]), aliases.toArray(new android.text.FontConfig.Alias[aliases.size()]));
    }

    /**
     * Reads a family element
     */
    public static FontConfig.Family readFamily(org.xmlpull.v1.XmlPullParser parser, java.lang.String fontDir) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        final java.lang.String name = parser.getAttributeValue(null, "name");
        final java.lang.String lang = parser.getAttributeValue("", "lang");
        final java.lang.String variant = parser.getAttributeValue(null, "variant");
        final java.util.List<android.text.FontConfig.Font> fonts = new java.util.ArrayList<android.text.FontConfig.Font>();
        while (parser.next() != org.xmlpull.v1.XmlPullParser.END_TAG) {
            if (parser.getEventType() != org.xmlpull.v1.XmlPullParser.START_TAG)
                continue;

            final java.lang.String tag = parser.getName();
            if (tag.equals("font")) {
                fonts.add(android.graphics.FontListParser.readFont(parser, fontDir));
            } else {
                android.graphics.FontListParser.skip(parser);
            }
        } 
        int intVariant = FontConfig.Family.VARIANT_DEFAULT;
        if (variant != null) {
            if (variant.equals("compact")) {
                intVariant = FontConfig.Family.VARIANT_COMPACT;
            } else
                if (variant.equals("elegant")) {
                    intVariant = FontConfig.Family.VARIANT_ELEGANT;
                }

        }
        return new android.text.FontConfig.Family(name, fonts.toArray(new android.text.FontConfig.Font[fonts.size()]), lang, intVariant);
    }

    /**
     * Matches leading and trailing XML whitespace.
     */
    private static final java.util.regex.Pattern FILENAME_WHITESPACE_PATTERN = java.util.regex.Pattern.compile("^[ \\n\\r\\t]+|[ \\n\\r\\t]+$");

    private static FontConfig.Font readFont(org.xmlpull.v1.XmlPullParser parser, java.lang.String fontDir) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        java.lang.String indexStr = parser.getAttributeValue(null, "index");
        int index = (indexStr == null) ? 0 : java.lang.Integer.parseInt(indexStr);
        java.util.List<android.graphics.fonts.FontVariationAxis> axes = new java.util.ArrayList<android.graphics.fonts.FontVariationAxis>();
        java.lang.String weightStr = parser.getAttributeValue(null, "weight");
        int weight = (weightStr == null) ? 400 : java.lang.Integer.parseInt(weightStr);
        boolean isItalic = "italic".equals(parser.getAttributeValue(null, "style"));
        java.lang.String fallbackFor = parser.getAttributeValue(null, "fallbackFor");
        java.lang.StringBuilder filename = new java.lang.StringBuilder();
        while (parser.next() != org.xmlpull.v1.XmlPullParser.END_TAG) {
            if (parser.getEventType() == org.xmlpull.v1.XmlPullParser.TEXT) {
                filename.append(parser.getText());
            }
            if (parser.getEventType() != org.xmlpull.v1.XmlPullParser.START_TAG)
                continue;

            java.lang.String tag = parser.getName();
            if (tag.equals("axis")) {
                axes.add(android.graphics.FontListParser.readAxis(parser));
            } else {
                android.graphics.FontListParser.skip(parser);
            }
        } 
        java.lang.String sanitizedName = android.graphics.FontListParser.FILENAME_WHITESPACE_PATTERN.matcher(filename).replaceAll("");
        return new android.text.FontConfig.Font(fontDir + sanitizedName, index, axes.toArray(new android.graphics.fonts.FontVariationAxis[axes.size()]), weight, isItalic, fallbackFor);
    }

    private static android.graphics.fonts.FontVariationAxis readAxis(org.xmlpull.v1.XmlPullParser parser) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        java.lang.String tagStr = parser.getAttributeValue(null, "tag");
        java.lang.String styleValueStr = parser.getAttributeValue(null, "stylevalue");
        android.graphics.FontListParser.skip(parser);// axis tag is empty, ignore any contents and consume end tag

        return new android.graphics.fonts.FontVariationAxis(tagStr, java.lang.Float.parseFloat(styleValueStr));
    }

    /**
     * Reads alias elements
     */
    public static FontConfig.Alias readAlias(org.xmlpull.v1.XmlPullParser parser) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        java.lang.String name = parser.getAttributeValue(null, "name");
        java.lang.String toName = parser.getAttributeValue(null, "to");
        java.lang.String weightStr = parser.getAttributeValue(null, "weight");
        int weight;
        if (weightStr == null) {
            weight = 400;
        } else {
            weight = java.lang.Integer.parseInt(weightStr);
        }
        android.graphics.FontListParser.skip(parser);// alias tag is empty, ignore any contents and consume end tag

        return new android.text.FontConfig.Alias(name, toName, weight);
    }

    /**
     * Skip until next element
     */
    public static void skip(org.xmlpull.v1.XmlPullParser parser) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        int depth = 1;
        while (depth > 0) {
            switch (parser.next()) {
                case org.xmlpull.v1.XmlPullParser.START_TAG :
                    depth++;
                    break;
                case org.xmlpull.v1.XmlPullParser.END_TAG :
                    depth--;
                    break;
            }
        } 
    }
}

