/**
 * Copyright (C) 2008-2012 The Android Open Source Project
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
package android.renderscript;


/**
 *
 *
 * @unknown 
 * @deprecated in API 16
<p>This class gives users a simple way to draw hardware accelerated text.
Internally, the glyphs are rendered using the Freetype library and an internal cache of
rendered glyph bitmaps is maintained. Each font object represents a combination of a typeface,
and point size. You can create multiple font objects to represent styles such as bold or italic text,
faces, and different font sizes. During creation, the Android system quieries device's screen DPI to
ensure proper sizing across multiple device configurations.</p>
<p>Fonts are rendered using screen-space positions and no state setup beyond binding a
font to the RenderScript is required. A note of caution on performance, though the state changes
are transparent to the user, they do happen internally, and it is more efficient to
render large batches of text in sequence. It is also more efficient to render multiple
characters at once instead of one by one to improve draw call batching.</p>
<p>Font color and transparency are not part of the font object and you can freely modify
them in the script to suit the user's rendering needs. Font colors work as a state machine.
Every new call to draw text uses the last color set in the script.</p>
 */
public class Font extends android.renderscript.BaseObj {
    // These help us create a font by family name
    private static final java.lang.String[] sSansNames = new java.lang.String[]{ "sans-serif", "arial", "helvetica", "tahoma", "verdana" };

    private static final java.lang.String[] sSerifNames = new java.lang.String[]{ "serif", "times", "times new roman", "palatino", "georgia", "baskerville", "goudy", "fantasy", "cursive", "ITC Stone Serif" };

    private static final java.lang.String[] sMonoNames = new java.lang.String[]{ "monospace", "courier", "courier new", "monaco" };

    private static class FontFamily {
        java.lang.String[] mNames;

        java.lang.String mNormalFileName;

        java.lang.String mBoldFileName;

        java.lang.String mItalicFileName;

        java.lang.String mBoldItalicFileName;
    }

    private static java.util.Map<java.lang.String, android.renderscript.Font.FontFamily> sFontFamilyMap;

    /**
     *
     *
     * @deprecated in API 16
     */
    public enum Style {

        /**
         *
         *
         * @deprecated in API 16
         */
        NORMAL,
        /**
         *
         *
         * @deprecated in API 16
         */
        BOLD,
        /**
         *
         *
         * @deprecated in API 16
         */
        ITALIC,
        /**
         *
         *
         * @deprecated in API 16
         */
        BOLD_ITALIC;}

    private static void addFamilyToMap(android.renderscript.Font.FontFamily family) {
        for (int i = 0; i < family.mNames.length; i++) {
            android.renderscript.Font.sFontFamilyMap.put(family.mNames[i], family);
        }
    }

    private static void initFontFamilyMap() {
        android.renderscript.Font.sFontFamilyMap = new java.util.HashMap<java.lang.String, android.renderscript.Font.FontFamily>();
        android.renderscript.Font.FontFamily sansFamily = new android.renderscript.Font.FontFamily();
        sansFamily.mNames = android.renderscript.Font.sSansNames;
        sansFamily.mNormalFileName = "Roboto-Regular.ttf";
        sansFamily.mBoldFileName = "Roboto-Bold.ttf";
        sansFamily.mItalicFileName = "Roboto-Italic.ttf";
        sansFamily.mBoldItalicFileName = "Roboto-BoldItalic.ttf";
        android.renderscript.Font.addFamilyToMap(sansFamily);
        android.renderscript.Font.FontFamily serifFamily = new android.renderscript.Font.FontFamily();
        serifFamily.mNames = android.renderscript.Font.sSerifNames;
        serifFamily.mNormalFileName = "NotoSerif-Regular.ttf";
        serifFamily.mBoldFileName = "NotoSerif-Bold.ttf";
        serifFamily.mItalicFileName = "NotoSerif-Italic.ttf";
        serifFamily.mBoldItalicFileName = "NotoSerif-BoldItalic.ttf";
        android.renderscript.Font.addFamilyToMap(serifFamily);
        android.renderscript.Font.FontFamily monoFamily = new android.renderscript.Font.FontFamily();
        monoFamily.mNames = android.renderscript.Font.sMonoNames;
        monoFamily.mNormalFileName = "DroidSansMono.ttf";
        monoFamily.mBoldFileName = "DroidSansMono.ttf";
        monoFamily.mItalicFileName = "DroidSansMono.ttf";
        monoFamily.mBoldItalicFileName = "DroidSansMono.ttf";
        android.renderscript.Font.addFamilyToMap(monoFamily);
    }

    static {
        android.renderscript.Font.initFontFamilyMap();
    }

    static java.lang.String getFontFileName(java.lang.String familyName, android.renderscript.Font.Style style) {
        android.renderscript.Font.FontFamily family = android.renderscript.Font.sFontFamilyMap.get(familyName);
        if (family != null) {
            switch (style) {
                case NORMAL :
                    return family.mNormalFileName;
                case BOLD :
                    return family.mBoldFileName;
                case ITALIC :
                    return family.mItalicFileName;
                case BOLD_ITALIC :
                    return family.mBoldItalicFileName;
            }
        }
        // Fallback if we could not find the desired family
        return "DroidSans.ttf";
    }

    Font(long id, android.renderscript.RenderScript rs) {
        super(id, rs);
        guard.open("destroy");
    }

    /**
     *
     *
     * @deprecated in API 16
    Takes a specific file name as an argument
     */
    public static android.renderscript.Font createFromFile(android.renderscript.RenderScript rs, android.content.res.Resources res, java.lang.String path, float pointSize) {
        rs.validate();
        int dpi = res.getDisplayMetrics().densityDpi;
        long fontId = rs.nFontCreateFromFile(path, pointSize, dpi);
        if (fontId == 0) {
            throw new android.renderscript.RSRuntimeException("Unable to create font from file " + path);
        }
        android.renderscript.Font rsFont = new android.renderscript.Font(fontId, rs);
        return rsFont;
    }

    /**
     *
     *
     * @deprecated in API 16
     */
    public static android.renderscript.Font createFromFile(android.renderscript.RenderScript rs, android.content.res.Resources res, java.io.File path, float pointSize) {
        return android.renderscript.Font.createFromFile(rs, res, path.getAbsolutePath(), pointSize);
    }

    /**
     *
     *
     * @deprecated in API 16
     */
    public static android.renderscript.Font createFromAsset(android.renderscript.RenderScript rs, android.content.res.Resources res, java.lang.String path, float pointSize) {
        rs.validate();
        android.content.res.AssetManager mgr = res.getAssets();
        int dpi = res.getDisplayMetrics().densityDpi;
        long fontId = rs.nFontCreateFromAsset(mgr, path, pointSize, dpi);
        if (fontId == 0) {
            throw new android.renderscript.RSRuntimeException("Unable to create font from asset " + path);
        }
        android.renderscript.Font rsFont = new android.renderscript.Font(fontId, rs);
        return rsFont;
    }

    /**
     *
     *
     * @deprecated in API 16
     */
    public static android.renderscript.Font createFromResource(android.renderscript.RenderScript rs, android.content.res.Resources res, int id, float pointSize) {
        java.lang.String name = "R." + java.lang.Integer.toString(id);
        rs.validate();
        java.io.InputStream is = null;
        try {
            is = res.openRawResource(id);
        } catch (java.lang.Exception e) {
            throw new android.renderscript.RSRuntimeException("Unable to open resource " + id);
        }
        int dpi = res.getDisplayMetrics().densityDpi;
        long fontId = 0;
        if (is instanceof android.content.res.AssetManager.AssetInputStream) {
            long asset = ((android.content.res.AssetManager.AssetInputStream) (is)).getNativeAsset();
            fontId = rs.nFontCreateFromAssetStream(name, pointSize, dpi, asset);
        } else {
            throw new android.renderscript.RSRuntimeException("Unsupported asset stream created");
        }
        if (fontId == 0) {
            throw new android.renderscript.RSRuntimeException("Unable to create font from resource " + id);
        }
        android.renderscript.Font rsFont = new android.renderscript.Font(fontId, rs);
        return rsFont;
    }

    /**
     *
     *
     * @deprecated in API 16
    Accepts one of the following family names as an argument
    and will attempt to produce the best match with a system font:

    "sans-serif" "arial" "helvetica" "tahoma" "verdana"
    "serif" "times" "times new roman" "palatino" "georgia" "baskerville"
    "goudy" "fantasy" "cursive" "ITC Stone Serif"
    "monospace" "courier" "courier new" "monaco"

    Returns default font if no match could be found.
     */
    public static android.renderscript.Font create(android.renderscript.RenderScript rs, android.content.res.Resources res, java.lang.String familyName, android.renderscript.Font.Style fontStyle, float pointSize) {
        java.lang.String fileName = android.renderscript.Font.getFontFileName(familyName, fontStyle);
        java.lang.String fontPath = android.os.Environment.getRootDirectory().getAbsolutePath();
        fontPath += "/fonts/" + fileName;
        return android.renderscript.Font.createFromFile(rs, res, fontPath, pointSize);
    }
}

