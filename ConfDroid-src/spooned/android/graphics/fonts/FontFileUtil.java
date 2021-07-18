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
 * Provides a utility for font file operations.
 *
 * @unknown 
 */
public class FontFileUtil {
    // Do not instanciate
    private FontFileUtil() {
    }

    /**
     * Unpack the weight value from packed integer.
     */
    public static int unpackWeight(int packed) {
        return packed & 0xffff;
    }

    /**
     * Unpack the italic value from packed integer.
     */
    public static boolean unpackItalic(int packed) {
        return (packed & 0x10000) != 0;
    }

    /**
     * Returns true if the analyzeStyle succeeded
     */
    public static boolean isSuccess(int packed) {
        return packed != android.graphics.fonts.FontFileUtil.ANALYZE_ERROR;
    }

    private static int pack(@android.annotation.IntRange(from = 0, to = 1000)
    int weight, boolean italic) {
        return weight | (italic ? 0x10000 : 0);
    }

    private static final int SFNT_VERSION_1 = 0x10000;

    private static final int SFNT_VERSION_OTTO = 0x4f54544f;

    private static final int TTC_TAG = 0x74746366;

    private static final int OS2_TABLE_TAG = 0x4f532f32;

    private static final int ANALYZE_ERROR = 0xffffffff;

    /**
     * Analyze the font file returns packed style info
     */
    public static final int analyzeStyle(@android.annotation.NonNull
    java.nio.ByteBuffer buffer, @android.annotation.IntRange(from = 0)
    int ttcIndex, @android.annotation.Nullable
    android.graphics.fonts.FontVariationAxis[] varSettings) {
        int weight = -1;
        int italic = -1;
        if (varSettings != null) {
            for (android.graphics.fonts.FontVariationAxis axis : varSettings) {
                if ("wght".equals(axis.getTag())) {
                    weight = ((int) (axis.getStyleValue()));
                } else
                    if ("ital".equals(axis.getTag())) {
                        italic = (axis.getStyleValue() == 1.0F) ? 1 : 0;
                    }

            }
        }
        if ((weight != (-1)) && (italic != (-1))) {
            // Both weight/italic style are specifeid by variation settings.
            // No need to look into OS/2 table.
            // TODO: Good to look HVAR table to check if this font supports wght/ital axes.
            return android.graphics.fonts.FontFileUtil.pack(weight, italic == 1);
        }
        java.nio.ByteOrder originalOrder = buffer.order();
        buffer.order(java.nio.ByteOrder.BIG_ENDIAN);
        try {
            int fontFileOffset = 0;
            int magicNumber = buffer.getInt(0);
            if (magicNumber == android.graphics.fonts.FontFileUtil.TTC_TAG) {
                // TTC file.
                if (ttcIndex >= /* offset to number of fonts in TTC */
                buffer.getInt(8)) {
                    return android.graphics.fonts.FontFileUtil.ANALYZE_ERROR;
                }
                fontFileOffset = buffer.getInt(12/* offset to array of offsets of font files */
                 + (4 * ttcIndex));
            }
            int sfntVersion = buffer.getInt(fontFileOffset);
            if ((sfntVersion != android.graphics.fonts.FontFileUtil.SFNT_VERSION_1) && (sfntVersion != android.graphics.fonts.FontFileUtil.SFNT_VERSION_OTTO)) {
                return android.graphics.fonts.FontFileUtil.ANALYZE_ERROR;
            }
            int numTables = /* offset to number of tables */
            buffer.getShort(fontFileOffset + 4);
            int os2TableOffset = -1;
            for (int i = 0; i < numTables; ++i) {
                /* size of table record */
                int tableOffset = (fontFileOffset + 12)/* size of offset table */
                 + (i * 16);
                if (buffer.getInt(tableOffset) == android.graphics.fonts.FontFileUtil.OS2_TABLE_TAG) {
                    os2TableOffset = /* offset to the table */
                    buffer.getInt(tableOffset + 8);
                    break;
                }
            }
            if (os2TableOffset == (-1)) {
                // Couldn't find OS/2 table. use regular style
                return android.graphics.fonts.FontFileUtil.pack(400, false);
            }
            int weightFromOS2 = /* offset to weight class */
            buffer.getShort(os2TableOffset + 4);
            boolean italicFromOS2 = (/* offset to fsSelection */
            buffer.getShort(os2TableOffset + 62) & 1) != 0;
            return android.graphics.fonts.FontFileUtil.pack(weight == (-1) ? weightFromOS2 : weight, italic == (-1) ? italicFromOS2 : italic == 1);
        } finally {
            buffer.order(originalOrder);
        }
    }
}

