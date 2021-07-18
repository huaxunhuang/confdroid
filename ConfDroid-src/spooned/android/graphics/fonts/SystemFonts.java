/**
 * Copyright 2018 The Android Open Source Project
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
 * Provides the system font configurations.
 */
public final class SystemFonts {
    private static final java.lang.String TAG = "SystemFonts";

    private static final java.lang.String DEFAULT_FAMILY = "sans-serif";

    // Do not instansiate.
    private SystemFonts() {
    }

    private static final java.util.Map<java.lang.String, android.graphics.fonts.FontFamily[]> sSystemFallbackMap;

    private static final FontConfig.Alias[] sAliases;

    private static final java.util.List<android.graphics.fonts.Font> sAvailableFonts;

    /**
     * Returns all available font files in the system.
     *
     * @return a set of system fonts
     */
    @android.annotation.NonNull
    public static java.util.Set<android.graphics.fonts.Font> getAvailableFonts() {
        java.util.HashSet<android.graphics.fonts.Font> set = new java.util.HashSet<>();
        set.addAll(android.graphics.fonts.SystemFonts.sAvailableFonts);
        return set;
    }

    /**
     * Returns fallback list for the given family name.
     *
     * If no fallback found for the given family name, returns fallback for the default family.
     *
     * @param familyName
     * 		family name, e.g. "serif"
     * @unknown 
     */
    @android.annotation.NonNull
    public static android.graphics.fonts.FontFamily[] getSystemFallback(@android.annotation.Nullable
    java.lang.String familyName) {
        final android.graphics.fonts.FontFamily[] families = android.graphics.fonts.SystemFonts.sSystemFallbackMap.get(familyName);
        return families == null ? android.graphics.fonts.SystemFonts.sSystemFallbackMap.get(android.graphics.fonts.SystemFonts.DEFAULT_FAMILY) : families;
    }

    /**
     * Returns raw system fallback map.
     *
     * This method is intended to be used only by Typeface static initializer.
     *
     * @unknown 
     */
    @android.annotation.NonNull
    public static java.util.Map<java.lang.String, android.graphics.fonts.FontFamily[]> getRawSystemFallbackMap() {
        return android.graphics.fonts.SystemFonts.sSystemFallbackMap;
    }

    /**
     * Returns a list of aliases.
     *
     * This method is intended to be used only by Typeface static initializer.
     *
     * @unknown 
     */
    @android.annotation.NonNull
    public static FontConfig.Alias[] getAliases() {
        return android.graphics.fonts.SystemFonts.sAliases;
    }

    @android.annotation.Nullable
    private static java.nio.ByteBuffer mmap(@android.annotation.NonNull
    java.lang.String fullPath) {
        try (java.io.FileInputStream file = new java.io.FileInputStream(fullPath)) {
            final java.nio.channels.FileChannel fileChannel = file.getChannel();
            final long fontSize = fileChannel.size();
            return fileChannel.map(java.nio.channels.FileChannel.MapMode.READ_ONLY, 0, fontSize);
        } catch (java.io.IOException e) {
            android.util.Log.e(android.graphics.fonts.SystemFonts.TAG, "Error mapping font file " + fullPath);
            return null;
        }
    }

    private static void pushFamilyToFallback(@android.annotation.NonNull
    android.text.FontConfig.Family xmlFamily, @android.annotation.NonNull
    android.util.ArrayMap<java.lang.String, java.util.ArrayList<android.graphics.fonts.FontFamily>> fallbackMap, @android.annotation.NonNull
    java.util.Map<java.lang.String, java.nio.ByteBuffer> cache, @android.annotation.NonNull
    java.util.ArrayList<android.graphics.fonts.Font> availableFonts) {
        final java.lang.String languageTags = xmlFamily.getLanguages();
        final int variant = xmlFamily.getVariant();
        final java.util.ArrayList<android.text.FontConfig.Font> defaultFonts = new java.util.ArrayList<>();
        final android.util.ArrayMap<java.lang.String, java.util.ArrayList<android.text.FontConfig.Font>> specificFallbackFonts = new android.util.ArrayMap();
        // Collect default fallback and specific fallback fonts.
        for (final android.text.FontConfig.Font font : xmlFamily.getFonts()) {
            final java.lang.String fallbackName = font.getFallbackFor();
            if (fallbackName == null) {
                defaultFonts.add(font);
            } else {
                java.util.ArrayList<android.text.FontConfig.Font> fallback = specificFallbackFonts.get(fallbackName);
                if (fallback == null) {
                    fallback = new java.util.ArrayList();
                    specificFallbackFonts.put(fallbackName, fallback);
                }
                fallback.add(font);
            }
        }
        final android.graphics.fonts.FontFamily defaultFamily = (defaultFonts.isEmpty()) ? null : android.graphics.fonts.SystemFonts.createFontFamily(xmlFamily.getName(), defaultFonts, languageTags, variant, cache, availableFonts);
        // Insert family into fallback map.
        for (int i = 0; i < fallbackMap.size(); i++) {
            final java.util.ArrayList<android.text.FontConfig.Font> fallback = specificFallbackFonts.get(fallbackMap.keyAt(i));
            if (fallback == null) {
                if (defaultFamily != null) {
                    fallbackMap.valueAt(i).add(defaultFamily);
                }
            } else {
                final android.graphics.fonts.FontFamily family = android.graphics.fonts.SystemFonts.createFontFamily(xmlFamily.getName(), fallback, languageTags, variant, cache, availableFonts);
                if (family != null) {
                    fallbackMap.valueAt(i).add(family);
                } else
                    if (defaultFamily != null) {
                        fallbackMap.valueAt(i).add(defaultFamily);
                    } else {
                        // There is no valid for for default fallback. Ignore.
                    }

            }
        }
    }

    @android.annotation.Nullable
    private static android.graphics.fonts.FontFamily createFontFamily(@android.annotation.NonNull
    java.lang.String familyName, @android.annotation.NonNull
    java.util.List<android.text.FontConfig.Font> fonts, @android.annotation.NonNull
    java.lang.String languageTags, @android.text.FontConfig.Family.Variant
    int variant, @android.annotation.NonNull
    java.util.Map<java.lang.String, java.nio.ByteBuffer> cache, @android.annotation.NonNull
    java.util.ArrayList<android.graphics.fonts.Font> availableFonts) {
        if (fonts.size() == 0) {
            return null;
        }
        android.graphics.fonts.FontFamily.Builder b = null;
        for (int i = 0; i < fonts.size(); i++) {
            final android.text.FontConfig.Font fontConfig = fonts.get(i);
            final java.lang.String fullPath = fontConfig.getFontName();
            java.nio.ByteBuffer buffer = cache.get(fullPath);
            if (buffer == null) {
                if (cache.containsKey(fullPath)) {
                    continue;// Already failed to mmap. Skip it.

                }
                buffer = android.graphics.fonts.SystemFonts.mmap(fullPath);
                cache.put(fullPath, buffer);
                if (buffer == null) {
                    continue;
                }
            }
            final android.graphics.fonts.Font font;
            try {
                font = new android.graphics.fonts.Font.Builder(buffer, new java.io.File(fullPath), languageTags).setWeight(fontConfig.getWeight()).setSlant(fontConfig.isItalic() ? android.graphics.fonts.FontStyle.FONT_SLANT_ITALIC : android.graphics.fonts.FontStyle.FONT_SLANT_UPRIGHT).setTtcIndex(fontConfig.getTtcIndex()).setFontVariationSettings(fontConfig.getAxes()).build();
            } catch (java.io.IOException e) {
                throw new java.lang.RuntimeException(e);// Never reaches here

            }
            availableFonts.add(font);
            if (b == null) {
                b = new android.graphics.fonts.FontFamily.Builder(font);
            } else {
                b.addFont(font);
            }
        }
        return b == null ? null : /* isCustomFallback */
        b.build(languageTags, variant, false);
    }

    private static void appendNamedFamily(@android.annotation.NonNull
    android.text.FontConfig.Family xmlFamily, @android.annotation.NonNull
    java.util.HashMap<java.lang.String, java.nio.ByteBuffer> bufferCache, @android.annotation.NonNull
    android.util.ArrayMap<java.lang.String, java.util.ArrayList<android.graphics.fonts.FontFamily>> fallbackListMap, @android.annotation.NonNull
    java.util.ArrayList<android.graphics.fonts.Font> availableFonts) {
        final java.lang.String familyName = xmlFamily.getName();
        final android.graphics.fonts.FontFamily family = android.graphics.fonts.SystemFonts.createFontFamily(familyName, java.util.Arrays.asList(xmlFamily.getFonts()), xmlFamily.getLanguages(), xmlFamily.getVariant(), bufferCache, availableFonts);
        if (family == null) {
            return;
        }
        final java.util.ArrayList<android.graphics.fonts.FontFamily> fallback = new java.util.ArrayList<>();
        fallback.add(family);
        fallbackListMap.put(familyName, fallback);
    }

    /**
     * Build the system fallback from xml file.
     *
     * @param xmlPath
     * 		A full path string to the fonts.xml file.
     * @param fontDir
     * 		A full path string to the system font directory. This must end with
     * 		slash('/').
     * @param fallbackMap
     * 		An output system fallback map. Caller must pass empty map.
     * @return a list of aliases
     * @unknown 
     */
    @com.android.internal.annotations.VisibleForTesting
    public static FontConfig.Alias[] buildSystemFallback(@android.annotation.NonNull
    java.lang.String xmlPath, @android.annotation.NonNull
    java.lang.String fontDir, @android.annotation.NonNull
    android.graphics.fonts.FontCustomizationParser.Result oemCustomization, @android.annotation.NonNull
    android.util.ArrayMap<java.lang.String, android.graphics.fonts.FontFamily[]> fallbackMap, @android.annotation.NonNull
    java.util.ArrayList<android.graphics.fonts.Font> availableFonts) {
        try {
            final java.io.FileInputStream fontsIn = new java.io.FileInputStream(xmlPath);
            final android.text.FontConfig fontConfig = android.graphics.FontListParser.parse(fontsIn, fontDir);
            final java.util.HashMap<java.lang.String, java.nio.ByteBuffer> bufferCache = new java.util.HashMap<java.lang.String, java.nio.ByteBuffer>();
            final android.text.FontConfig[] xmlFamilies = fontConfig.getFamilies();
            final android.util.ArrayMap<java.lang.String, java.util.ArrayList<android.graphics.fonts.FontFamily>> fallbackListMap = new android.util.ArrayMap();
            // First traverse families which have a 'name' attribute to create fallback map.
            for (final android.text.FontConfig.Family xmlFamily : xmlFamilies) {
                final java.lang.String familyName = xmlFamily.getName();
                if (familyName == null) {
                    continue;
                }
                android.graphics.fonts.SystemFonts.appendNamedFamily(xmlFamily, bufferCache, fallbackListMap, availableFonts);
            }
            for (int i = 0; i < oemCustomization.mAdditionalNamedFamilies.size(); ++i) {
                android.graphics.fonts.SystemFonts.appendNamedFamily(oemCustomization.mAdditionalNamedFamilies.get(i), bufferCache, fallbackListMap, availableFonts);
            }
            // Then, add fallback fonts to the each fallback map.
            for (int i = 0; i < xmlFamilies.length; i++) {
                final android.text.FontConfig.Family xmlFamily = xmlFamilies[i];
                // The first family (usually the sans-serif family) is always placed immediately
                // after the primary family in the fallback.
                if ((i == 0) || (xmlFamily.getName() == null)) {
                    android.graphics.fonts.SystemFonts.pushFamilyToFallback(xmlFamily, fallbackListMap, bufferCache, availableFonts);
                }
            }
            // Build the font map and fallback map.
            for (int i = 0; i < fallbackListMap.size(); i++) {
                final java.lang.String fallbackName = fallbackListMap.keyAt(i);
                final java.util.List<android.graphics.fonts.FontFamily> familyList = fallbackListMap.valueAt(i);
                final android.graphics.fonts.FontFamily[] families = familyList.toArray(new android.graphics.fonts.FontFamily[familyList.size()]);
                fallbackMap.put(fallbackName, families);
            }
            final java.util.ArrayList<android.text.FontConfig.Alias> list = new java.util.ArrayList<>();
            list.addAll(java.util.Arrays.asList(fontConfig.getAliases()));
            list.addAll(oemCustomization.mAdditionalAliases);
            return list.toArray(new android.text.FontConfig.Alias[list.size()]);
        } catch (java.io.IOException | org.xmlpull.v1.XmlPullParserException e) {
            android.util.Log.e(android.graphics.fonts.SystemFonts.TAG, "Failed initialize system fallbacks.", e);
            return com.android.internal.util.ArrayUtils.emptyArray(FontConfig.Alias.class);
        }
    }

    private static android.graphics.fonts.FontCustomizationParser.Result readFontCustomization(@android.annotation.NonNull
    java.lang.String customizeXml, @android.annotation.NonNull
    java.lang.String customFontsDir) {
        try (java.io.FileInputStream f = new java.io.FileInputStream(customizeXml)) {
            return android.graphics.fonts.FontCustomizationParser.parse(f, customFontsDir);
        } catch (java.io.IOException e) {
            return new android.graphics.fonts.FontCustomizationParser.Result();
        } catch (org.xmlpull.v1.XmlPullParserException e) {
            android.util.Log.e(android.graphics.fonts.SystemFonts.TAG, "Failed to parse font customization XML", e);
            return new android.graphics.fonts.FontCustomizationParser.Result();
        }
    }

    static {
        final android.util.ArrayMap<java.lang.String, android.graphics.fonts.FontFamily[]> systemFallbackMap = new android.util.ArrayMap();
        final java.util.ArrayList<android.graphics.fonts.Font> availableFonts = new java.util.ArrayList<>();
        final android.graphics.fonts.FontCustomizationParser.Result oemCustomization = android.graphics.fonts.SystemFonts.readFontCustomization("/product/etc/fonts_customization.xml", "/product/fonts/");
        sAliases = android.graphics.fonts.SystemFonts.buildSystemFallback("/system/etc/fonts.xml", "/system/fonts/", oemCustomization, systemFallbackMap, availableFonts);
        sSystemFallbackMap = java.util.Collections.unmodifiableMap(systemFallbackMap);
        sAvailableFonts = java.util.Collections.unmodifiableList(availableFonts);
    }
}

