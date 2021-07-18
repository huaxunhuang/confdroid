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
 * Parser for font customization
 *
 * @unknown 
 */
public class FontCustomizationParser {
    /**
     * Represents a customization XML
     */
    public static class Result {
        java.util.ArrayList<android.text.FontConfig.Family> mAdditionalNamedFamilies = new java.util.ArrayList<>();

        java.util.ArrayList<android.text.FontConfig.Alias> mAdditionalAliases = new java.util.ArrayList<>();
    }

    /**
     * Parses the customization XML
     *
     * Caller must close the input stream
     */
    public static android.graphics.fonts.FontCustomizationParser.Result parse(@android.annotation.NonNull
    java.io.InputStream in, @android.annotation.NonNull
    java.lang.String fontDir) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        org.xmlpull.v1.XmlPullParser parser = android.util.Xml.newPullParser();
        parser.setInput(in, null);
        parser.nextTag();
        return android.graphics.fonts.FontCustomizationParser.readFamilies(parser, fontDir);
    }

    private static void validate(android.graphics.fonts.FontCustomizationParser.Result result) {
        java.util.HashSet<java.lang.String> familyNames = new java.util.HashSet<>();
        for (int i = 0; i < result.mAdditionalNamedFamilies.size(); ++i) {
            final android.text.FontConfig.Family family = result.mAdditionalNamedFamilies.get(i);
            final java.lang.String name = family.getName();
            if (name == null) {
                throw new java.lang.IllegalArgumentException("new-named-family requires name attribute");
            }
            if (!familyNames.add(name)) {
                throw new java.lang.IllegalArgumentException("new-named-family requires unique name attribute");
            }
        }
    }

    private static android.graphics.fonts.FontCustomizationParser.Result readFamilies(org.xmlpull.v1.XmlPullParser parser, java.lang.String fontDir) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        android.graphics.fonts.FontCustomizationParser.Result out = new android.graphics.fonts.FontCustomizationParser.Result();
        parser.require(org.xmlpull.v1.XmlPullParser.START_TAG, null, "fonts-modification");
        while (parser.next() != org.xmlpull.v1.XmlPullParser.END_TAG) {
            if (parser.getEventType() != org.xmlpull.v1.XmlPullParser.START_TAG)
                continue;

            java.lang.String tag = parser.getName();
            if (tag.equals("family")) {
                android.graphics.fonts.FontCustomizationParser.readFamily(parser, fontDir, out);
            } else
                if (tag.equals("alias")) {
                    out.mAdditionalAliases.add(android.graphics.FontListParser.readAlias(parser));
                } else {
                    android.graphics.FontListParser.skip(parser);
                }

        } 
        android.graphics.fonts.FontCustomizationParser.validate(out);
        return out;
    }

    private static void readFamily(org.xmlpull.v1.XmlPullParser parser, java.lang.String fontDir, android.graphics.fonts.FontCustomizationParser.Result out) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        final java.lang.String customizationType = parser.getAttributeValue(null, "customizationType");
        if (customizationType == null) {
            throw new java.lang.IllegalArgumentException("customizationType must be specified");
        }
        if (customizationType.equals("new-named-family")) {
            out.mAdditionalNamedFamilies.add(android.graphics.FontListParser.readFamily(parser, fontDir));
        } else {
            throw new java.lang.IllegalArgumentException("Unknown customizationType=" + customizationType);
        }
    }
}

