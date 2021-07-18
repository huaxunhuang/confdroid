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
package android.content.res;


/**
 * Parser for xml type font resources.
 *
 * @unknown 
 */
public class FontResourcesParser {
    private static final java.lang.String TAG = "FontResourcesParser";

    // A class represents single entry of font-family in xml file.
    public interface FamilyResourceEntry {}

    // A class represents font provider based font-family element in xml file.
    public static final class ProviderResourceEntry implements android.content.res.FontResourcesParser.FamilyResourceEntry {
        @android.annotation.NonNull
        private final java.lang.String mProviderAuthority;

        @android.annotation.NonNull
        private final java.lang.String mProviderPackage;

        @android.annotation.NonNull
        private final java.lang.String mQuery;

        @android.annotation.Nullable
        private final java.util.List<java.util.List<java.lang.String>> mCerts;

        public ProviderResourceEntry(@android.annotation.NonNull
        java.lang.String authority, @android.annotation.NonNull
        java.lang.String pkg, @android.annotation.NonNull
        java.lang.String query, @android.annotation.Nullable
        java.util.List<java.util.List<java.lang.String>> certs) {
            mProviderAuthority = authority;
            mProviderPackage = pkg;
            mQuery = query;
            mCerts = certs;
        }

        @android.annotation.NonNull
        public java.lang.String getAuthority() {
            return mProviderAuthority;
        }

        @android.annotation.NonNull
        public java.lang.String getPackage() {
            return mProviderPackage;
        }

        @android.annotation.NonNull
        public java.lang.String getQuery() {
            return mQuery;
        }

        @android.annotation.Nullable
        public java.util.List<java.util.List<java.lang.String>> getCerts() {
            return mCerts;
        }
    }

    // A class represents font element in xml file which points a file in resource.
    public static final class FontFileResourceEntry {
        public static final int RESOLVE_BY_FONT_TABLE = android.graphics.Typeface.RESOLVE_BY_FONT_TABLE;

        public static final int UPRIGHT = 0;

        public static final int ITALIC = 1;

        @android.annotation.NonNull
        private final java.lang.String mFileName;

        private int mWeight;

        private int mItalic;

        private int mTtcIndex;

        private java.lang.String mVariationSettings;

        private int mResourceId;

        public FontFileResourceEntry(@android.annotation.NonNull
        java.lang.String fileName, int weight, int italic, @android.annotation.Nullable
        java.lang.String variationSettings, int ttcIndex) {
            mFileName = fileName;
            mWeight = weight;
            mItalic = italic;
            mVariationSettings = variationSettings;
            mTtcIndex = ttcIndex;
        }

        @android.annotation.NonNull
        public java.lang.String getFileName() {
            return mFileName;
        }

        public int getWeight() {
            return mWeight;
        }

        public int getItalic() {
            return mItalic;
        }

        @android.annotation.Nullable
        public java.lang.String getVariationSettings() {
            return mVariationSettings;
        }

        public int getTtcIndex() {
            return mTtcIndex;
        }
    }

    // A class represents file based font-family element in xml file.
    public static final class FontFamilyFilesResourceEntry implements android.content.res.FontResourcesParser.FamilyResourceEntry {
        @android.annotation.NonNull
        private final android.content.res.FontResourcesParser.FontFileResourceEntry[] mEntries;

        public FontFamilyFilesResourceEntry(@android.annotation.NonNull
        android.content.res.FontResourcesParser.FontFileResourceEntry[] entries) {
            mEntries = entries;
        }

        @android.annotation.NonNull
        public android.content.res.FontResourcesParser.FontFileResourceEntry[] getEntries() {
            return mEntries;
        }
    }

    @android.annotation.Nullable
    public static android.content.res.FontResourcesParser.FamilyResourceEntry parse(org.xmlpull.v1.XmlPullParser parser, android.content.res.Resources resources) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        int type;
        while (((type = parser.next()) != org.xmlpull.v1.XmlPullParser.START_TAG) && (type != org.xmlpull.v1.XmlPullParser.END_DOCUMENT)) {
            // Empty loop.
        } 
        if (type != org.xmlpull.v1.XmlPullParser.START_TAG) {
            throw new org.xmlpull.v1.XmlPullParserException("No start tag found");
        }
        return android.content.res.FontResourcesParser.readFamilies(parser, resources);
    }

    @android.annotation.Nullable
    private static android.content.res.FontResourcesParser.FamilyResourceEntry readFamilies(org.xmlpull.v1.XmlPullParser parser, android.content.res.Resources resources) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        parser.require(org.xmlpull.v1.XmlPullParser.START_TAG, null, "font-family");
        java.lang.String tag = parser.getName();
        android.content.res.FontResourcesParser.FamilyResourceEntry result = null;
        if (tag.equals("font-family")) {
            return android.content.res.FontResourcesParser.readFamily(parser, resources);
        } else {
            android.content.res.FontResourcesParser.skip(parser);
            android.util.Log.e(android.content.res.FontResourcesParser.TAG, "Failed to find font-family tag");
            return null;
        }
    }

    @android.annotation.Nullable
    private static android.content.res.FontResourcesParser.FamilyResourceEntry readFamily(org.xmlpull.v1.XmlPullParser parser, android.content.res.Resources resources) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        android.util.AttributeSet attrs = android.util.Xml.asAttributeSet(parser);
        android.content.res.TypedArray array = resources.obtainAttributes(attrs, R.styleable.FontFamily);
        java.lang.String authority = array.getString(R.styleable.FontFamily_fontProviderAuthority);
        java.lang.String providerPackage = array.getString(R.styleable.FontFamily_fontProviderPackage);
        java.lang.String query = array.getString(R.styleable.FontFamily_fontProviderQuery);
        int certsId = array.getResourceId(R.styleable.FontFamily_fontProviderCerts, 0);
        array.recycle();
        if (((authority != null) && (providerPackage != null)) && (query != null)) {
            while (parser.next() != org.xmlpull.v1.XmlPullParser.END_TAG) {
                android.content.res.FontResourcesParser.skip(parser);
            } 
            java.util.List<java.util.List<java.lang.String>> certs = null;
            if (certsId != 0) {
                android.content.res.TypedArray typedArray = resources.obtainTypedArray(certsId);
                if (typedArray.length() > 0) {
                    certs = new java.util.ArrayList<>();
                    boolean isArrayOfArrays = typedArray.getResourceId(0, 0) != 0;
                    if (isArrayOfArrays) {
                        for (int i = 0; i < typedArray.length(); i++) {
                            int certId = typedArray.getResourceId(i, 0);
                            java.lang.String[] certsArray = resources.getStringArray(certId);
                            java.util.List<java.lang.String> certsList = java.util.Arrays.asList(certsArray);
                            certs.add(certsList);
                        }
                    } else {
                        java.lang.String[] certsArray = resources.getStringArray(certsId);
                        java.util.List<java.lang.String> certsList = java.util.Arrays.asList(certsArray);
                        certs.add(certsList);
                    }
                }
            }
            return new android.content.res.FontResourcesParser.ProviderResourceEntry(authority, providerPackage, query, certs);
        }
        java.util.List<android.content.res.FontResourcesParser.FontFileResourceEntry> fonts = new java.util.ArrayList<>();
        while (parser.next() != org.xmlpull.v1.XmlPullParser.END_TAG) {
            if (parser.getEventType() != org.xmlpull.v1.XmlPullParser.START_TAG)
                continue;

            java.lang.String tag = parser.getName();
            if (tag.equals("font")) {
                final android.content.res.FontResourcesParser.FontFileResourceEntry entry = android.content.res.FontResourcesParser.readFont(parser, resources);
                if (entry != null) {
                    fonts.add(entry);
                }
            } else {
                android.content.res.FontResourcesParser.skip(parser);
            }
        } 
        if (fonts.isEmpty()) {
            return null;
        }
        return new android.content.res.FontResourcesParser.FontFamilyFilesResourceEntry(fonts.toArray(new android.content.res.FontResourcesParser.FontFileResourceEntry[fonts.size()]));
    }

    private static android.content.res.FontResourcesParser.FontFileResourceEntry readFont(org.xmlpull.v1.XmlPullParser parser, android.content.res.Resources resources) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        android.util.AttributeSet attrs = android.util.Xml.asAttributeSet(parser);
        android.content.res.TypedArray array = resources.obtainAttributes(attrs, R.styleable.FontFamilyFont);
        int weight = array.getInt(R.styleable.FontFamilyFont_fontWeight, android.graphics.Typeface.RESOLVE_BY_FONT_TABLE);
        int italic = array.getInt(R.styleable.FontFamilyFont_fontStyle, android.content.res.FontResourcesParser.FontFileResourceEntry.RESOLVE_BY_FONT_TABLE);
        java.lang.String variationSettings = array.getString(R.styleable.FontFamilyFont_fontVariationSettings);
        int ttcIndex = array.getInt(R.styleable.FontFamilyFont_ttcIndex, 0);
        java.lang.String filename = array.getString(R.styleable.FontFamilyFont_font);
        array.recycle();
        while (parser.next() != org.xmlpull.v1.XmlPullParser.END_TAG) {
            android.content.res.FontResourcesParser.skip(parser);
        } 
        if (filename == null) {
            return null;
        }
        return new android.content.res.FontResourcesParser.FontFileResourceEntry(filename, weight, italic, variationSettings, ttcIndex);
    }

    private static void skip(org.xmlpull.v1.XmlPullParser parser) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
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

