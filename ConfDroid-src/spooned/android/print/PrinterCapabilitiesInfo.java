/**
 * Copyright (C) 2013 The Android Open Source Project
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
package android.print;


/**
 * This class represents the capabilities of a printer. Instances
 * of this class are created by a print service to report the
 * capabilities of a printer it manages. The capabilities of a
 * printer specify how it can print content. For example, what
 * are the media sizes supported by the printer, what are the
 * minimal margins of the printer based on its technical design,
 * etc.
 */
public final class PrinterCapabilitiesInfo implements android.os.Parcelable {
    /**
     * Undefined default value.
     *
     * @unknown 
     */
    public static final int DEFAULT_UNDEFINED = -1;

    private static final int PROPERTY_MEDIA_SIZE = 0;

    private static final int PROPERTY_RESOLUTION = 1;

    private static final int PROPERTY_COLOR_MODE = 2;

    private static final int PROPERTY_DUPLEX_MODE = 3;

    private static final int PROPERTY_COUNT = 4;

    private static final android.print.PrintAttributes.Margins DEFAULT_MARGINS = new android.print.PrintAttributes.Margins(0, 0, 0, 0);

    @android.annotation.NonNull
    private android.print.PrintAttributes.Margins mMinMargins = android.print.PrinterCapabilitiesInfo.DEFAULT_MARGINS;

    @android.annotation.NonNull
    private java.util.List<android.print.PrintAttributes.MediaSize> mMediaSizes;

    @android.annotation.NonNull
    private java.util.List<android.print.PrintAttributes.Resolution> mResolutions;

    private int mColorModes;

    private int mDuplexModes;

    private final int[] mDefaults = new int[android.print.PrinterCapabilitiesInfo.PROPERTY_COUNT];

    /**
     *
     *
     * @unknown 
     */
    public PrinterCapabilitiesInfo() {
        java.util.Arrays.fill(mDefaults, android.print.PrinterCapabilitiesInfo.DEFAULT_UNDEFINED);
    }

    /**
     *
     *
     * @unknown 
     */
    public PrinterCapabilitiesInfo(android.print.PrinterCapabilitiesInfo prototype) {
        copyFrom(prototype);
    }

    /**
     *
     *
     * @unknown 
     */
    public void copyFrom(android.print.PrinterCapabilitiesInfo other) {
        if (this == other) {
            return;
        }
        mMinMargins = other.mMinMargins;
        if (other.mMediaSizes != null) {
            if (mMediaSizes != null) {
                mMediaSizes.clear();
                mMediaSizes.addAll(other.mMediaSizes);
            } else {
                mMediaSizes = new java.util.ArrayList<android.print.PrintAttributes.MediaSize>(other.mMediaSizes);
            }
        } else {
            mMediaSizes = null;
        }
        if (other.mResolutions != null) {
            if (mResolutions != null) {
                mResolutions.clear();
                mResolutions.addAll(other.mResolutions);
            } else {
                mResolutions = new java.util.ArrayList<android.print.PrintAttributes.Resolution>(other.mResolutions);
            }
        } else {
            mResolutions = null;
        }
        mColorModes = other.mColorModes;
        mDuplexModes = other.mDuplexModes;
        final int defaultCount = other.mDefaults.length;
        for (int i = 0; i < defaultCount; i++) {
            mDefaults[i] = other.mDefaults[i];
        }
    }

    /**
     * Gets the supported media sizes.
     *
     * @return The media sizes.
     */
    @android.annotation.NonNull
    public java.util.List<android.print.PrintAttributes.MediaSize> getMediaSizes() {
        return java.util.Collections.unmodifiableList(mMediaSizes);
    }

    /**
     * Gets the supported resolutions.
     *
     * @return The resolutions.
     */
    @android.annotation.NonNull
    public java.util.List<android.print.PrintAttributes.Resolution> getResolutions() {
        return java.util.Collections.unmodifiableList(mResolutions);
    }

    /**
     * Gets the minimal margins. These are the minimal margins
     * the printer physically supports.
     *
     * @return The minimal margins.
     */
    @android.annotation.NonNull
    public android.print.PrintAttributes.Margins getMinMargins() {
        return mMinMargins;
    }

    /**
     * Gets the bit mask of supported color modes.
     *
     * @return The bit mask of supported color modes.
     * @see PrintAttributes#COLOR_MODE_COLOR
     * @see PrintAttributes#COLOR_MODE_MONOCHROME
     */
    @android.print.PrintAttributes.ColorMode
    public int getColorModes() {
        return mColorModes;
    }

    /**
     * Gets the bit mask of supported duplex modes.
     *
     * @return The bit mask of supported duplex modes.
     * @see PrintAttributes#DUPLEX_MODE_NONE
     * @see PrintAttributes#DUPLEX_MODE_LONG_EDGE
     * @see PrintAttributes#DUPLEX_MODE_SHORT_EDGE
     */
    @android.print.PrintAttributes.DuplexMode
    public int getDuplexModes() {
        return mDuplexModes;
    }

    /**
     * Gets the default print attributes.
     *
     * @return The default attributes.
     */
    @android.annotation.NonNull
    public android.print.PrintAttributes getDefaults() {
        android.print.PrintAttributes.Builder builder = new android.print.PrintAttributes.Builder();
        builder.setMinMargins(mMinMargins);
        final int mediaSizeIndex = mDefaults[android.print.PrinterCapabilitiesInfo.PROPERTY_MEDIA_SIZE];
        if (mediaSizeIndex >= 0) {
            builder.setMediaSize(mMediaSizes.get(mediaSizeIndex));
        }
        final int resolutionIndex = mDefaults[android.print.PrinterCapabilitiesInfo.PROPERTY_RESOLUTION];
        if (resolutionIndex >= 0) {
            builder.setResolution(mResolutions.get(resolutionIndex));
        }
        final int colorMode = mDefaults[android.print.PrinterCapabilitiesInfo.PROPERTY_COLOR_MODE];
        if (colorMode > 0) {
            builder.setColorMode(colorMode);
        }
        final int duplexMode = mDefaults[android.print.PrinterCapabilitiesInfo.PROPERTY_DUPLEX_MODE];
        if (duplexMode > 0) {
            builder.setDuplexMode(duplexMode);
        }
        return builder.build();
    }

    /**
     * Call enforceSingle for each bit in the mask.
     *
     * @param mask
     * 		The mask
     * @param enforceSingle
     * 		The function to call
     */
    private static void enforceValidMask(int mask, java.util.function.IntConsumer enforceSingle) {
        int current = mask;
        while (current > 0) {
            final int currentMode = 1 << java.lang.Integer.numberOfTrailingZeros(current);
            current &= ~currentMode;
            enforceSingle.accept(currentMode);
        } 
    }

    private PrinterCapabilitiesInfo(android.os.Parcel parcel) {
        mMinMargins = com.android.internal.util.Preconditions.checkNotNull(readMargins(parcel));
        readMediaSizes(parcel);
        readResolutions(parcel);
        mColorModes = parcel.readInt();
        android.print.PrinterCapabilitiesInfo.enforceValidMask(mColorModes, ( currentMode) -> android.print.PrintAttributes.enforceValidColorMode(currentMode));
        mDuplexModes = parcel.readInt();
        android.print.PrinterCapabilitiesInfo.enforceValidMask(mDuplexModes, ( currentMode) -> android.print.PrintAttributes.enforceValidDuplexMode(currentMode));
        readDefaults(parcel);
        com.android.internal.util.Preconditions.checkArgument(mMediaSizes.size() > mDefaults[android.print.PrinterCapabilitiesInfo.PROPERTY_MEDIA_SIZE]);
        com.android.internal.util.Preconditions.checkArgument(mResolutions.size() > mDefaults[android.print.PrinterCapabilitiesInfo.PROPERTY_RESOLUTION]);
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel parcel, int flags) {
        writeMargins(mMinMargins, parcel);
        writeMediaSizes(parcel);
        writeResolutions(parcel);
        parcel.writeInt(mColorModes);
        parcel.writeInt(mDuplexModes);
        writeDefaults(parcel);
    }

    @java.lang.Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + (mMinMargins == null ? 0 : mMinMargins.hashCode());
        result = (prime * result) + (mMediaSizes == null ? 0 : mMediaSizes.hashCode());
        result = (prime * result) + (mResolutions == null ? 0 : mResolutions.hashCode());
        result = (prime * result) + mColorModes;
        result = (prime * result) + mDuplexModes;
        result = (prime * result) + java.util.Arrays.hashCode(mDefaults);
        return result;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        android.print.PrinterCapabilitiesInfo other = ((android.print.PrinterCapabilitiesInfo) (obj));
        if (mMinMargins == null) {
            if (other.mMinMargins != null) {
                return false;
            }
        } else
            if (!mMinMargins.equals(other.mMinMargins)) {
                return false;
            }

        if (mMediaSizes == null) {
            if (other.mMediaSizes != null) {
                return false;
            }
        } else
            if (!mMediaSizes.equals(other.mMediaSizes)) {
                return false;
            }

        if (mResolutions == null) {
            if (other.mResolutions != null) {
                return false;
            }
        } else
            if (!mResolutions.equals(other.mResolutions)) {
                return false;
            }

        if (mColorModes != other.mColorModes) {
            return false;
        }
        if (mDuplexModes != other.mDuplexModes) {
            return false;
        }
        if (!java.util.Arrays.equals(mDefaults, other.mDefaults)) {
            return false;
        }
        return true;
    }

    @java.lang.Override
    public java.lang.String toString() {
        java.lang.StringBuilder builder = new java.lang.StringBuilder();
        builder.append("PrinterInfo{");
        builder.append("minMargins=").append(mMinMargins);
        builder.append(", mediaSizes=").append(mMediaSizes);
        builder.append(", resolutions=").append(mResolutions);
        builder.append(", colorModes=").append(colorModesToString());
        builder.append(", duplexModes=").append(duplexModesToString());
        builder.append("\"}");
        return builder.toString();
    }

    private java.lang.String colorModesToString() {
        java.lang.StringBuilder builder = new java.lang.StringBuilder();
        builder.append('[');
        int colorModes = mColorModes;
        while (colorModes != 0) {
            final int colorMode = 1 << java.lang.Integer.numberOfTrailingZeros(colorModes);
            colorModes &= ~colorMode;
            if (builder.length() > 1) {
                builder.append(", ");
            }
            builder.append(android.print.PrintAttributes.colorModeToString(colorMode));
        } 
        builder.append(']');
        return builder.toString();
    }

    private java.lang.String duplexModesToString() {
        java.lang.StringBuilder builder = new java.lang.StringBuilder();
        builder.append('[');
        int duplexModes = mDuplexModes;
        while (duplexModes != 0) {
            final int duplexMode = 1 << java.lang.Integer.numberOfTrailingZeros(duplexModes);
            duplexModes &= ~duplexMode;
            if (builder.length() > 1) {
                builder.append(", ");
            }
            builder.append(android.print.PrintAttributes.duplexModeToString(duplexMode));
        } 
        builder.append(']');
        return builder.toString();
    }

    private void writeMediaSizes(android.os.Parcel parcel) {
        if (mMediaSizes == null) {
            parcel.writeInt(0);
            return;
        }
        final int mediaSizeCount = mMediaSizes.size();
        parcel.writeInt(mediaSizeCount);
        for (int i = 0; i < mediaSizeCount; i++) {
            mMediaSizes.get(i).writeToParcel(parcel);
        }
    }

    private void readMediaSizes(android.os.Parcel parcel) {
        final int mediaSizeCount = parcel.readInt();
        if ((mediaSizeCount > 0) && (mMediaSizes == null)) {
            mMediaSizes = new java.util.ArrayList<android.print.PrintAttributes.MediaSize>();
        }
        for (int i = 0; i < mediaSizeCount; i++) {
            mMediaSizes.add(android.print.PrintAttributes.MediaSize.createFromParcel(parcel));
        }
    }

    private void writeResolutions(android.os.Parcel parcel) {
        if (mResolutions == null) {
            parcel.writeInt(0);
            return;
        }
        final int resolutionCount = mResolutions.size();
        parcel.writeInt(resolutionCount);
        for (int i = 0; i < resolutionCount; i++) {
            mResolutions.get(i).writeToParcel(parcel);
        }
    }

    private void readResolutions(android.os.Parcel parcel) {
        final int resolutionCount = parcel.readInt();
        if ((resolutionCount > 0) && (mResolutions == null)) {
            mResolutions = new java.util.ArrayList<android.print.PrintAttributes.Resolution>();
        }
        for (int i = 0; i < resolutionCount; i++) {
            mResolutions.add(android.print.PrintAttributes.Resolution.createFromParcel(parcel));
        }
    }

    private void writeMargins(android.print.PrintAttributes.Margins margins, android.os.Parcel parcel) {
        if (margins == null) {
            parcel.writeInt(0);
        } else {
            parcel.writeInt(1);
            margins.writeToParcel(parcel);
        }
    }

    private android.print.PrintAttributes.Margins readMargins(android.os.Parcel parcel) {
        return parcel.readInt() == 1 ? android.print.PrintAttributes.Margins.createFromParcel(parcel) : null;
    }

    private void readDefaults(android.os.Parcel parcel) {
        final int defaultCount = parcel.readInt();
        for (int i = 0; i < defaultCount; i++) {
            mDefaults[i] = parcel.readInt();
        }
    }

    private void writeDefaults(android.os.Parcel parcel) {
        final int defaultCount = mDefaults.length;
        parcel.writeInt(defaultCount);
        for (int i = 0; i < defaultCount; i++) {
            parcel.writeInt(mDefaults[i]);
        }
    }

    /**
     * Builder for creating of a {@link PrinterCapabilitiesInfo}. This class is
     * responsible to enforce that all required attributes have at least one
     * default value. In other words, this class creates only well-formed {@link PrinterCapabilitiesInfo}s.
     * <p>
     * Look at the individual methods for a reference whether a property is
     * required or if it is optional.
     * </p>
     */
    public static final class Builder {
        private final android.print.PrinterCapabilitiesInfo mPrototype;

        /**
         * Creates a new instance.
         *
         * @param printerId
         * 		The printer id. Cannot be <code>null</code>.
         * @throws IllegalArgumentException
         * 		If the printer id is <code>null</code>.
         */
        public Builder(@android.annotation.NonNull
        android.print.PrinterId printerId) {
            if (printerId == null) {
                throw new java.lang.IllegalArgumentException("printerId cannot be null.");
            }
            mPrototype = new android.print.PrinterCapabilitiesInfo();
        }

        /**
         * Adds a supported media size.
         * <p>
         * <strong>Required:</strong> Yes
         * </p>
         *
         * @param mediaSize
         * 		A media size.
         * @param isDefault
         * 		Whether this is the default.
         * @return This builder.
         * @throws IllegalArgumentException
         * 		If set as default and there
         * 		is already a default.
         * @see PrintAttributes.MediaSize
         */
        @android.annotation.NonNull
        public android.print.PrinterCapabilitiesInfo.Builder addMediaSize(@android.annotation.NonNull
        android.print.PrintAttributes.MediaSize mediaSize, boolean isDefault) {
            if (mPrototype.mMediaSizes == null) {
                mPrototype.mMediaSizes = new java.util.ArrayList<android.print.PrintAttributes.MediaSize>();
            }
            final int insertionIndex = mPrototype.mMediaSizes.size();
            mPrototype.mMediaSizes.add(mediaSize);
            if (isDefault) {
                throwIfDefaultAlreadySpecified(android.print.PrinterCapabilitiesInfo.PROPERTY_MEDIA_SIZE);
                mPrototype.mDefaults[android.print.PrinterCapabilitiesInfo.PROPERTY_MEDIA_SIZE] = insertionIndex;
            }
            return this;
        }

        /**
         * Adds a supported resolution.
         * <p>
         * <strong>Required:</strong> Yes
         * </p>
         *
         * @param resolution
         * 		A resolution.
         * @param isDefault
         * 		Whether this is the default.
         * @return This builder.
         * @throws IllegalArgumentException
         * 		If set as default and there
         * 		is already a default.
         * @see PrintAttributes.Resolution
         */
        @android.annotation.NonNull
        public android.print.PrinterCapabilitiesInfo.Builder addResolution(@android.annotation.NonNull
        android.print.PrintAttributes.Resolution resolution, boolean isDefault) {
            if (mPrototype.mResolutions == null) {
                mPrototype.mResolutions = new java.util.ArrayList<android.print.PrintAttributes.Resolution>();
            }
            final int insertionIndex = mPrototype.mResolutions.size();
            mPrototype.mResolutions.add(resolution);
            if (isDefault) {
                throwIfDefaultAlreadySpecified(android.print.PrinterCapabilitiesInfo.PROPERTY_RESOLUTION);
                mPrototype.mDefaults[android.print.PrinterCapabilitiesInfo.PROPERTY_RESOLUTION] = insertionIndex;
            }
            return this;
        }

        /**
         * Sets the minimal margins. These are the minimal margins
         * the printer physically supports.
         *
         * <p>
         * <strong>Required:</strong> Yes
         * </p>
         *
         * @param margins
         * 		The margins.
         * @return This builder.
         * @throws IllegalArgumentException
         * 		If margins are <code>null</code>.
         * @see PrintAttributes.Margins
         */
        @android.annotation.NonNull
        public android.print.PrinterCapabilitiesInfo.Builder setMinMargins(@android.annotation.NonNull
        android.print.PrintAttributes.Margins margins) {
            if (margins == null) {
                throw new java.lang.IllegalArgumentException("margins cannot be null");
            }
            mPrototype.mMinMargins = margins;
            return this;
        }

        /**
         * Sets the color modes.
         * <p>
         * <strong>Required:</strong> Yes
         * </p>
         *
         * @param colorModes
         * 		The color mode bit mask.
         * @param defaultColorMode
         * 		The default color mode.
         * @return This builder.
        <p>
        <strong>Note:</strong> On platform version 19 (Kitkat) specifying
        only PrintAttributes#COLOR_MODE_MONOCHROME leads to a print spooler
        crash. Hence, you should declare either both color modes or
        PrintAttributes#COLOR_MODE_COLOR.
        </p>
         * @throws IllegalArgumentException
         * 		If color modes contains an invalid
         * 		mode bit or if the default color mode is invalid.
         * @see PrintAttributes#COLOR_MODE_COLOR
         * @see PrintAttributes#COLOR_MODE_MONOCHROME
         */
        @android.annotation.NonNull
        public android.print.PrinterCapabilitiesInfo.Builder setColorModes(@android.print.PrintAttributes.ColorMode
        int colorModes, @android.print.PrintAttributes.ColorMode
        int defaultColorMode) {
            android.print.PrinterCapabilitiesInfo.enforceValidMask(colorModes, ( currentMode) -> android.print.PrintAttributes.enforceValidColorMode(currentMode));
            android.print.PrintAttributes.enforceValidColorMode(defaultColorMode);
            mPrototype.mColorModes = colorModes;
            mPrototype.mDefaults[android.print.PrinterCapabilitiesInfo.PROPERTY_COLOR_MODE] = defaultColorMode;
            return this;
        }

        /**
         * Sets the duplex modes.
         * <p>
         * <strong>Required:</strong> No
         * </p>
         *
         * @param duplexModes
         * 		The duplex mode bit mask.
         * @param defaultDuplexMode
         * 		The default duplex mode.
         * @return This builder.
         * @throws IllegalArgumentException
         * 		If duplex modes contains an invalid
         * 		mode bit or if the default duplex mode is invalid.
         * @see PrintAttributes#DUPLEX_MODE_NONE
         * @see PrintAttributes#DUPLEX_MODE_LONG_EDGE
         * @see PrintAttributes#DUPLEX_MODE_SHORT_EDGE
         */
        @android.annotation.NonNull
        public android.print.PrinterCapabilitiesInfo.Builder setDuplexModes(@android.print.PrintAttributes.DuplexMode
        int duplexModes, @android.print.PrintAttributes.DuplexMode
        int defaultDuplexMode) {
            android.print.PrinterCapabilitiesInfo.enforceValidMask(duplexModes, ( currentMode) -> android.print.PrintAttributes.enforceValidDuplexMode(currentMode));
            android.print.PrintAttributes.enforceValidDuplexMode(defaultDuplexMode);
            mPrototype.mDuplexModes = duplexModes;
            mPrototype.mDefaults[android.print.PrinterCapabilitiesInfo.PROPERTY_DUPLEX_MODE] = defaultDuplexMode;
            return this;
        }

        /**
         * Crates a new {@link PrinterCapabilitiesInfo} enforcing that all
         * required properties have been specified. See individual methods
         * in this class for reference about required attributes.
         * <p>
         * <strong>Note:</strong> If you do not add supported duplex modes,
         * {@link android.print.PrintAttributes#DUPLEX_MODE_NONE} will set
         * as the only supported mode and also as the default duplex mode.
         * </p>
         *
         * @return A new {@link PrinterCapabilitiesInfo}.
         * @throws IllegalStateException
         * 		If a required attribute was not specified.
         */
        @android.annotation.NonNull
        public android.print.PrinterCapabilitiesInfo build() {
            if ((mPrototype.mMediaSizes == null) || mPrototype.mMediaSizes.isEmpty()) {
                throw new java.lang.IllegalStateException("No media size specified.");
            }
            if (mPrototype.mDefaults[android.print.PrinterCapabilitiesInfo.PROPERTY_MEDIA_SIZE] == android.print.PrinterCapabilitiesInfo.DEFAULT_UNDEFINED) {
                throw new java.lang.IllegalStateException("No default media size specified.");
            }
            if ((mPrototype.mResolutions == null) || mPrototype.mResolutions.isEmpty()) {
                throw new java.lang.IllegalStateException("No resolution specified.");
            }
            if (mPrototype.mDefaults[android.print.PrinterCapabilitiesInfo.PROPERTY_RESOLUTION] == android.print.PrinterCapabilitiesInfo.DEFAULT_UNDEFINED) {
                throw new java.lang.IllegalStateException("No default resolution specified.");
            }
            if (mPrototype.mColorModes == 0) {
                throw new java.lang.IllegalStateException("No color mode specified.");
            }
            if (mPrototype.mDefaults[android.print.PrinterCapabilitiesInfo.PROPERTY_COLOR_MODE] == android.print.PrinterCapabilitiesInfo.DEFAULT_UNDEFINED) {
                throw new java.lang.IllegalStateException("No default color mode specified.");
            }
            if (mPrototype.mDuplexModes == 0) {
                setDuplexModes(android.print.PrintAttributes.DUPLEX_MODE_NONE, android.print.PrintAttributes.DUPLEX_MODE_NONE);
            }
            if (mPrototype.mMinMargins == null) {
                throw new java.lang.IllegalArgumentException("margins cannot be null");
            }
            return mPrototype;
        }

        private void throwIfDefaultAlreadySpecified(int propertyIndex) {
            if (mPrototype.mDefaults[propertyIndex] != android.print.PrinterCapabilitiesInfo.DEFAULT_UNDEFINED) {
                throw new java.lang.IllegalArgumentException("Default already specified.");
            }
        }
    }

    public static final android.os.Parcelable.Creator<android.print.PrinterCapabilitiesInfo> CREATOR = new android.os.Parcelable.Creator<android.print.PrinterCapabilitiesInfo>() {
        @java.lang.Override
        public android.print.PrinterCapabilitiesInfo createFromParcel(android.os.Parcel parcel) {
            return new android.print.PrinterCapabilitiesInfo(parcel);
        }

        @java.lang.Override
        public android.print.PrinterCapabilitiesInfo[] newArray(int size) {
            return new android.print.PrinterCapabilitiesInfo[size];
        }
    };
}

