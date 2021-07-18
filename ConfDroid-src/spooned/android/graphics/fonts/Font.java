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
 * A font class can be used for creating FontFamily.
 */
public final class Font {
    private static final java.lang.String TAG = "Font";

    private static final int NOT_SPECIFIED = -1;

    private static final int STYLE_ITALIC = 1;

    private static final int STYLE_NORMAL = 0;

    /**
     * A builder class for creating new Font.
     */
    public static final class Builder {
        private static final libcore.util.NativeAllocationRegistry sAssetByteBufferRegistry = libcore.util.NativeAllocationRegistry.createMalloced(java.nio.ByteBuffer.class.getClassLoader(), android.graphics.fonts.Font.Builder.nGetReleaseNativeAssetFunc());

        private static final libcore.util.NativeAllocationRegistry sFontRegistry = libcore.util.NativeAllocationRegistry.createMalloced(android.graphics.fonts.Font.class.getClassLoader(), android.graphics.fonts.Font.Builder.nGetReleaseNativeFont());

        @android.annotation.Nullable
        private java.nio.ByteBuffer mBuffer;

        @android.annotation.Nullable
        private java.io.File mFile;

        @android.annotation.NonNull
        private java.lang.String mLocaleList = "";

        @android.annotation.IntRange(from = -1, to = 1000)
        private int mWeight = android.graphics.fonts.Font.NOT_SPECIFIED;

        @android.annotation.IntRange(from = -1, to = 1)
        private int mItalic = android.graphics.fonts.Font.NOT_SPECIFIED;

        @android.annotation.IntRange(from = 0)
        private int mTtcIndex = 0;

        @android.annotation.Nullable
        private android.graphics.fonts.FontVariationAxis[] mAxes = null;

        @android.annotation.Nullable
        private java.io.IOException mException;

        /**
         * Constructs a builder with a byte buffer.
         *
         * Note that only direct buffer can be used as the source of font data.
         *
         * @see ByteBuffer#allocateDirect(int)
         * @param buffer
         * 		a byte buffer of a font data
         */
        public Builder(@android.annotation.NonNull
        java.nio.ByteBuffer buffer) {
            com.android.internal.util.Preconditions.checkNotNull(buffer, "buffer can not be null");
            if (!buffer.isDirect()) {
                throw new java.lang.IllegalArgumentException("Only direct buffer can be used as the source of font data.");
            }
            mBuffer = buffer;
        }

        /**
         * Construct a builder with a byte buffer and file path.
         *
         * This method is intended to be called only from SystemFonts.
         *
         * @unknown 
         */
        public Builder(@android.annotation.NonNull
        java.nio.ByteBuffer buffer, @android.annotation.NonNull
        java.io.File path, @android.annotation.NonNull
        java.lang.String localeList) {
            this(buffer);
            mFile = path;
            mLocaleList = localeList;
        }

        /**
         * Constructs a builder with a file path.
         *
         * @param path
         * 		a file path to the font file
         */
        public Builder(@android.annotation.NonNull
        java.io.File path) {
            com.android.internal.util.Preconditions.checkNotNull(path, "path can not be null");
            try (java.io.FileInputStream fis = new java.io.FileInputStream(path)) {
                final java.nio.channels.FileChannel fc = fis.getChannel();
                mBuffer = fc.map(java.nio.channels.FileChannel.MapMode.READ_ONLY, 0, fc.size());
            } catch (java.io.IOException e) {
                mException = e;
            }
            mFile = path;
        }

        /**
         * Constructs a builder with a file descriptor.
         *
         * @param fd
         * 		a file descriptor
         */
        public Builder(@android.annotation.NonNull
        android.os.ParcelFileDescriptor fd) {
            this(fd, 0, -1);
        }

        /**
         * Constructs a builder with a file descriptor.
         *
         * @param fd
         * 		a file descriptor
         * @param offset
         * 		an offset to of the font data in the file
         * @param size
         * 		a size of the font data. If -1 is passed, use until end of the file.
         */
        public Builder(@android.annotation.NonNull
        android.os.ParcelFileDescriptor fd, @android.annotation.IntRange(from = 0)
        long offset, @android.annotation.IntRange(from = -1)
        long size) {
            try (java.io.FileInputStream fis = new java.io.FileInputStream(fd.getFileDescriptor())) {
                final java.nio.channels.FileChannel fc = fis.getChannel();
                size = (size == (-1)) ? fc.size() - offset : size;
                mBuffer = fc.map(java.nio.channels.FileChannel.MapMode.READ_ONLY, offset, size);
            } catch (java.io.IOException e) {
                mException = e;
            }
        }

        /**
         * Constructs a builder from an asset manager and a file path in an asset directory.
         *
         * @param am
         * 		the application's asset manager
         * @param path
         * 		the file name of the font data in the asset directory
         */
        public Builder(@android.annotation.NonNull
        android.content.res.AssetManager am, @android.annotation.NonNull
        java.lang.String path) {
            /* is asset */
            /* cookie */
            this(am, path, true, 0);
        }

        /**
         * Constructs a builder from an asset manager and a file path in an asset directory.
         *
         * @param am
         * 		the application's asset manager
         * @param path
         * 		the file name of the font data in the asset directory
         * @param isAsset
         * 		true if the undelying data is in asset
         * @param cookie
         * 		set asset cookie
         * @unknown 
         */
        public Builder(@android.annotation.NonNull
        android.content.res.AssetManager am, @android.annotation.NonNull
        java.lang.String path, boolean isAsset, int cookie) {
            final long nativeAsset = android.graphics.fonts.Font.Builder.nGetNativeAsset(am, path, isAsset, cookie);
            if (nativeAsset == 0) {
                mException = new java.io.FileNotFoundException("Unable to open " + path);
                return;
            }
            final java.nio.ByteBuffer b = android.graphics.fonts.Font.Builder.nGetAssetBuffer(nativeAsset);
            android.graphics.fonts.Font.Builder.sAssetByteBufferRegistry.registerNativeAllocation(b, nativeAsset);
            if (b == null) {
                mException = new java.io.FileNotFoundException(path + " not found");
                return;
            }
            mBuffer = b;
        }

        /**
         * Constructs a builder from resources.
         *
         * Resource ID must points the font file. XML font can not be used here.
         *
         * @param res
         * 		the resource of this application.
         * @param resId
         * 		the resource ID of font file.
         */
        public Builder(@android.annotation.NonNull
        android.content.res.Resources res, int resId) {
            final android.util.TypedValue value = new android.util.TypedValue();
            res.getValue(resId, value, true);
            if (value.string == null) {
                mException = new java.io.FileNotFoundException(resId + " not found");
                return;
            }
            final java.lang.String str = value.string.toString();
            if (str.toLowerCase().endsWith(".xml")) {
                mException = new java.io.FileNotFoundException(resId + " must be font file.");
                return;
            }
            final long nativeAsset = /* is asset */
            android.graphics.fonts.Font.Builder.nGetNativeAsset(res.getAssets(), str, false, value.assetCookie);
            if (nativeAsset == 0) {
                mException = new java.io.FileNotFoundException("Unable to open " + str);
                return;
            }
            final java.nio.ByteBuffer b = android.graphics.fonts.Font.Builder.nGetAssetBuffer(nativeAsset);
            android.graphics.fonts.Font.Builder.sAssetByteBufferRegistry.registerNativeAllocation(b, nativeAsset);
            if (b == null) {
                mException = new java.io.FileNotFoundException(str + " not found");
                return;
            }
            mBuffer = b;
        }

        /**
         * Sets weight of the font.
         *
         * Tells the system the weight of the given font. If this function is not called, the system
         * will resolve the weight value by reading font tables.
         *
         * Here are pairs of the common names and their values.
         * <p>
         *  <table>
         *  <thead>
         *  <tr>
         *  <th align="center">Value</th>
         *  <th align="center">Name</th>
         *  <th align="center">Android Definition</th>
         *  </tr>
         *  </thead>
         *  <tbody>
         *  <tr>
         *  <td align="center">100</td>
         *  <td align="center">Thin</td>
         *  <td align="center">{@link FontStyle#FONT_WEIGHT_THIN}</td>
         *  </tr>
         *  <tr>
         *  <td align="center">200</td>
         *  <td align="center">Extra Light (Ultra Light)</td>
         *  <td align="center">{@link FontStyle#FONT_WEIGHT_EXTRA_LIGHT}</td>
         *  </tr>
         *  <tr>
         *  <td align="center">300</td>
         *  <td align="center">Light</td>
         *  <td align="center">{@link FontStyle#FONT_WEIGHT_LIGHT}</td>
         *  </tr>
         *  <tr>
         *  <td align="center">400</td>
         *  <td align="center">Normal (Regular)</td>
         *  <td align="center">{@link FontStyle#FONT_WEIGHT_NORMAL}</td>
         *  </tr>
         *  <tr>
         *  <td align="center">500</td>
         *  <td align="center">Medium</td>
         *  <td align="center">{@link FontStyle#FONT_WEIGHT_MEDIUM}</td>
         *  </tr>
         *  <tr>
         *  <td align="center">600</td>
         *  <td align="center">Semi Bold (Demi Bold)</td>
         *  <td align="center">{@link FontStyle#FONT_WEIGHT_SEMI_BOLD}</td>
         *  </tr>
         *  <tr>
         *  <td align="center">700</td>
         *  <td align="center">Bold</td>
         *  <td align="center">{@link FontStyle#FONT_WEIGHT_BOLD}</td>
         *  </tr>
         *  <tr>
         *  <td align="center">800</td>
         *  <td align="center">Extra Bold (Ultra Bold)</td>
         *  <td align="center">{@link FontStyle#FONT_WEIGHT_EXTRA_BOLD}</td>
         *  </tr>
         *  <tr>
         *  <td align="center">900</td>
         *  <td align="center">Black (Heavy)</td>
         *  <td align="center">{@link FontStyle#FONT_WEIGHT_BLACK}</td>
         *  </tr>
         *  </tbody>
         * </p>
         *
         * @see FontStyle#FONT_WEIGHT_THIN
         * @see FontStyle#FONT_WEIGHT_EXTRA_LIGHT
         * @see FontStyle#FONT_WEIGHT_LIGHT
         * @see FontStyle#FONT_WEIGHT_NORMAL
         * @see FontStyle#FONT_WEIGHT_MEDIUM
         * @see FontStyle#FONT_WEIGHT_SEMI_BOLD
         * @see FontStyle#FONT_WEIGHT_BOLD
         * @see FontStyle#FONT_WEIGHT_EXTRA_BOLD
         * @see FontStyle#FONT_WEIGHT_BLACK
         * @param weight
         * 		a weight value
         * @return this builder
         */
        @android.annotation.NonNull
        public android.graphics.fonts.Font.Builder setWeight(@android.annotation.IntRange(from = android.graphics.fonts.FontStyle.FONT_WEIGHT_MIN, to = android.graphics.fonts.FontStyle.FONT_WEIGHT_MAX)
        int weight) {
            com.android.internal.util.Preconditions.checkArgument((android.graphics.fonts.FontStyle.FONT_WEIGHT_MIN <= weight) && (weight <= android.graphics.fonts.FontStyle.FONT_WEIGHT_MAX));
            mWeight = weight;
            return this;
        }

        /**
         * Sets italic information of the font.
         *
         * Tells the system the style of the given font. If this function is not called, the system
         * will resolve the style by reading font tables.
         *
         * For example, if you want to use italic font as upright font, call {@code setSlant(FontStyle.FONT_SLANT_UPRIGHT)} explicitly.
         *
         * @return this builder
         */
        @android.annotation.NonNull
        public android.graphics.fonts.Font.Builder setSlant(@android.graphics.fonts.FontStyle.FontSlant
        int slant) {
            mItalic = (slant == android.graphics.fonts.FontStyle.FONT_SLANT_UPRIGHT) ? android.graphics.fonts.Font.STYLE_NORMAL : android.graphics.fonts.Font.STYLE_ITALIC;
            return this;
        }

        /**
         * Sets an index of the font collection. See {@link android.R.attr#ttcIndex}.
         *
         * @param ttcIndex
         * 		An index of the font collection. If the font source is not font
         * 		collection, do not call this method or specify 0.
         * @return this builder
         */
        @android.annotation.NonNull
        public android.graphics.fonts.Font.Builder setTtcIndex(@android.annotation.IntRange(from = 0)
        int ttcIndex) {
            mTtcIndex = ttcIndex;
            return this;
        }

        /**
         * Sets the font variation settings.
         *
         * @param variationSettings
         * 		see {@link FontVariationAxis#fromFontVariationSettings(String)}
         * @return this builder
         * @throws IllegalArgumentException
         * 		If given string is not a valid font variation settings
         * 		format.
         */
        @android.annotation.NonNull
        public android.graphics.fonts.Font.Builder setFontVariationSettings(@android.annotation.Nullable
        java.lang.String variationSettings) {
            mAxes = android.graphics.fonts.FontVariationAxis.fromFontVariationSettings(variationSettings);
            return this;
        }

        /**
         * Sets the font variation settings.
         *
         * @param axes
         * 		an array of font variation axis tag-value pairs
         * @return this builder
         */
        @android.annotation.NonNull
        public android.graphics.fonts.Font.Builder setFontVariationSettings(@android.annotation.Nullable
        android.graphics.fonts.FontVariationAxis[] axes) {
            mAxes = (axes == null) ? null : axes.clone();
            return this;
        }

        /**
         * Creates the font based on the configured values.
         *
         * @return the Font object
         */
        @android.annotation.NonNull
        public android.graphics.fonts.Font build() throws java.io.IOException {
            if (mException != null) {
                throw new java.io.IOException("Failed to read font contents", mException);
            }
            if ((mWeight == android.graphics.fonts.Font.NOT_SPECIFIED) || (mItalic == android.graphics.fonts.Font.NOT_SPECIFIED)) {
                final int packed = android.graphics.fonts.FontFileUtil.analyzeStyle(mBuffer, mTtcIndex, mAxes);
                if (android.graphics.fonts.FontFileUtil.isSuccess(packed)) {
                    if (mWeight == android.graphics.fonts.Font.NOT_SPECIFIED) {
                        mWeight = android.graphics.fonts.FontFileUtil.unpackWeight(packed);
                    }
                    if (mItalic == android.graphics.fonts.Font.NOT_SPECIFIED) {
                        mItalic = (android.graphics.fonts.FontFileUtil.unpackItalic(packed)) ? android.graphics.fonts.Font.STYLE_ITALIC : android.graphics.fonts.Font.STYLE_NORMAL;
                    }
                } else {
                    mWeight = 400;
                    mItalic = android.graphics.fonts.Font.STYLE_NORMAL;
                }
            }
            mWeight = java.lang.Math.max(android.graphics.fonts.FontStyle.FONT_WEIGHT_MIN, java.lang.Math.min(android.graphics.fonts.FontStyle.FONT_WEIGHT_MAX, mWeight));
            final boolean italic = mItalic == android.graphics.fonts.Font.STYLE_ITALIC;
            final int slant = (mItalic == android.graphics.fonts.Font.STYLE_ITALIC) ? android.graphics.fonts.FontStyle.FONT_SLANT_ITALIC : android.graphics.fonts.FontStyle.FONT_SLANT_UPRIGHT;
            final long builderPtr = android.graphics.fonts.Font.Builder.nInitBuilder();
            if (mAxes != null) {
                for (android.graphics.fonts.FontVariationAxis axis : mAxes) {
                    android.graphics.fonts.Font.Builder.nAddAxis(builderPtr, axis.getOpenTypeTagValue(), axis.getStyleValue());
                }
            }
            final java.nio.ByteBuffer readonlyBuffer = mBuffer.asReadOnlyBuffer();
            final java.lang.String filePath = (mFile == null) ? "" : mFile.getAbsolutePath();
            final long ptr = android.graphics.fonts.Font.Builder.nBuild(builderPtr, readonlyBuffer, filePath, mWeight, italic, mTtcIndex);
            final android.graphics.fonts.Font font = new android.graphics.fonts.Font(ptr, readonlyBuffer, mFile, new android.graphics.fonts.FontStyle(mWeight, slant), mTtcIndex, mAxes, mLocaleList);
            android.graphics.fonts.Font.Builder.sFontRegistry.registerNativeAllocation(font, ptr);
            return font;
        }

        /**
         * Native methods for accessing underlying buffer in Asset
         */
        private static native long nGetNativeAsset(@android.annotation.NonNull
        android.content.res.AssetManager am, @android.annotation.NonNull
        java.lang.String path, boolean isAsset, int cookie);

        private static native java.nio.ByteBuffer nGetAssetBuffer(long nativeAsset);

        @dalvik.annotation.optimization.CriticalNative
        private static native long nGetReleaseNativeAssetFunc();

        /**
         * Native methods for creating Font
         */
        private static native long nInitBuilder();

        @dalvik.annotation.optimization.CriticalNative
        private static native void nAddAxis(long builderPtr, int tag, float value);

        private static native long nBuild(long builderPtr, @android.annotation.NonNull
        java.nio.ByteBuffer buffer, @android.annotation.NonNull
        java.lang.String filePath, int weight, boolean italic, int ttcIndex);

        @dalvik.annotation.optimization.CriticalNative
        private static native long nGetReleaseNativeFont();
    }

    private final long mNativePtr;// address of the shared ptr of minikin::Font


    @android.annotation.NonNull
    private final java.nio.ByteBuffer mBuffer;

    @android.annotation.Nullable
    private final java.io.File mFile;

    private final android.graphics.fonts.FontStyle mFontStyle;

    @android.annotation.IntRange(from = 0)
    private final int mTtcIndex;

    @android.annotation.Nullable
    private final android.graphics.fonts.FontVariationAxis[] mAxes;

    @android.annotation.NonNull
    private final java.lang.String mLocaleList;

    /**
     * Use Builder instead
     */
    private Font(long nativePtr, @android.annotation.NonNull
    java.nio.ByteBuffer buffer, @android.annotation.Nullable
    java.io.File file, @android.annotation.NonNull
    android.graphics.fonts.FontStyle fontStyle, @android.annotation.IntRange(from = 0)
    int ttcIndex, @android.annotation.Nullable
    android.graphics.fonts.FontVariationAxis[] axes, @android.annotation.NonNull
    java.lang.String localeList) {
        mBuffer = buffer;
        mFile = file;
        mFontStyle = fontStyle;
        mNativePtr = nativePtr;
        mTtcIndex = ttcIndex;
        mAxes = axes;
        mLocaleList = localeList;
    }

    /**
     * Returns a font file buffer.
     *
     * @return a font buffer
     */
    @android.annotation.NonNull
    public java.nio.ByteBuffer getBuffer() {
        return mBuffer;
    }

    /**
     * Returns a file path of this font.
     *
     * This returns null if this font is not created from regular file.
     *
     * @return a file path of the font
     */
    @android.annotation.Nullable
    public java.io.File getFile() {
        return mFile;
    }

    /**
     * Get a style associated with this font.
     *
     * @see Builder#setWeight(int)
     * @see Builder#setSlant(int)
     * @return a font style
     */
    @android.annotation.NonNull
    public android.graphics.fonts.FontStyle getStyle() {
        return mFontStyle;
    }

    /**
     * Get a TTC index value associated with this font.
     *
     * If TTF/OTF file is provided, this value is always 0.
     *
     * @see Builder#setTtcIndex(int)
     * @return a TTC index value
     */
    @android.annotation.IntRange(from = 0)
    public int getTtcIndex() {
        return mTtcIndex;
    }

    /**
     * Get a font variation settings associated with this font
     *
     * @see Builder#setFontVariationSettings(String)
     * @see Builder#setFontVariationSettings(FontVariationAxis[])
     * @return font variation settings
     */
    @android.annotation.Nullable
    public android.graphics.fonts.FontVariationAxis[] getAxes() {
        return mAxes == null ? null : mAxes.clone();
    }

    /**
     * Get a locale list of this font.
     *
     * This is always empty if this font is not a system font.
     *
     * @return a locale list
     */
    @android.annotation.NonNull
    public android.os.LocaleList getLocaleList() {
        return android.os.LocaleList.forLanguageTags(mLocaleList);
    }

    /**
     *
     *
     * @unknown 
     */
    public long getNativePtr() {
        return mNativePtr;
    }

    @java.lang.Override
    public boolean equals(@android.annotation.Nullable
    java.lang.Object o) {
        if (o == this) {
            return true;
        }
        if ((o == null) || (!(o instanceof android.graphics.fonts.Font))) {
            return false;
        }
        android.graphics.fonts.Font f = ((android.graphics.fonts.Font) (o));
        return ((mFontStyle.equals(f.mFontStyle) && (f.mTtcIndex == mTtcIndex)) && java.util.Arrays.equals(f.mAxes, mAxes)) && f.mBuffer.equals(mBuffer);
    }

    @java.lang.Override
    public int hashCode() {
        return java.util.Objects.hash(mFontStyle, mTtcIndex, java.util.Arrays.hashCode(mAxes), mBuffer);
    }

    @java.lang.Override
    public java.lang.String toString() {
        return (((((((((((("Font {" + "path=") + mFile) + ", style=") + mFontStyle) + ", ttcIndex=") + mTtcIndex) + ", axes=") + android.graphics.fonts.FontVariationAxis.toFontVariationSettings(mAxes)) + ", localeList=") + mLocaleList) + ", buffer=") + mBuffer) + "}";
    }
}

