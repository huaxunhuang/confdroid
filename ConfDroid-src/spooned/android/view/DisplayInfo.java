/**
 * Copyright (C) 2012 The Android Open Source Project
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
package android.view;


/**
 * Describes the characteristics of a particular logical display.
 *
 * @unknown 
 */
public final class DisplayInfo implements android.os.Parcelable {
    /**
     * The surface flinger layer stack associated with this logical display.
     */
    public int layerStack;

    /**
     * Display flags.
     */
    public int flags;

    /**
     * Display type.
     */
    public int type;

    /**
     * Logical display identifier.
     */
    public int displayId;

    /**
     * Display address, or null if none.
     * Interpretation varies by display type.
     */
    public android.view.DisplayAddress address;

    /**
     * The human-readable name of the display.
     */
    public java.lang.String name;

    /**
     * Unique identifier for the display. Shouldn't be displayed to the user.
     */
    public java.lang.String uniqueId;

    /**
     * The width of the portion of the display that is available to applications, in pixels.
     * Represents the size of the display minus any system decorations.
     */
    public int appWidth;

    /**
     * The height of the portion of the display that is available to applications, in pixels.
     * Represents the size of the display minus any system decorations.
     */
    public int appHeight;

    /**
     * The smallest value of {@link #appWidth} that an application is likely to encounter,
     * in pixels, excepting cases where the width may be even smaller due to the presence
     * of a soft keyboard, for example.
     */
    public int smallestNominalAppWidth;

    /**
     * The smallest value of {@link #appHeight} that an application is likely to encounter,
     * in pixels, excepting cases where the height may be even smaller due to the presence
     * of a soft keyboard, for example.
     */
    public int smallestNominalAppHeight;

    /**
     * The largest value of {@link #appWidth} that an application is likely to encounter,
     * in pixels, excepting cases where the width may be even larger due to system decorations
     * such as the status bar being hidden, for example.
     */
    public int largestNominalAppWidth;

    /**
     * The largest value of {@link #appHeight} that an application is likely to encounter,
     * in pixels, excepting cases where the height may be even larger due to system decorations
     * such as the status bar being hidden, for example.
     */
    public int largestNominalAppHeight;

    /**
     * The logical width of the display, in pixels.
     * Represents the usable size of the display which may be smaller than the
     * physical size when the system is emulating a smaller display.
     */
    @android.annotation.UnsupportedAppUsage
    public int logicalWidth;

    /**
     * The logical height of the display, in pixels.
     * Represents the usable size of the display which may be smaller than the
     * physical size when the system is emulating a smaller display.
     */
    @android.annotation.UnsupportedAppUsage
    public int logicalHeight;

    /**
     *
     *
     * @unknown Number of overscan pixels on the left side of the display.
     */
    public int overscanLeft;

    /**
     *
     *
     * @unknown Number of overscan pixels on the top side of the display.
     */
    public int overscanTop;

    /**
     *
     *
     * @unknown Number of overscan pixels on the right side of the display.
     */
    public int overscanRight;

    /**
     *
     *
     * @unknown Number of overscan pixels on the bottom side of the display.
     */
    public int overscanBottom;

    /**
     * The {@link DisplayCutout} if present, otherwise {@code null}.
     *
     * @unknown 
     */
    // Remark on @UnsupportedAppUsage: Display.getCutout should be used instead
    @android.annotation.Nullable
    @android.annotation.UnsupportedAppUsage(maxTargetSdk = Build.VERSION_CODES.P)
    public android.view.DisplayCutout displayCutout;

    /**
     * The rotation of the display relative to its natural orientation.
     * May be one of {@link android.view.Surface#ROTATION_0},
     * {@link android.view.Surface#ROTATION_90}, {@link android.view.Surface#ROTATION_180},
     * {@link android.view.Surface#ROTATION_270}.
     * <p>
     * The value of this field is indeterminate if the logical display is presented on
     * more than one physical display.
     * </p>
     */
    @android.view.Surface.Rotation
    @android.annotation.UnsupportedAppUsage
    public int rotation;

    /**
     * The active display mode.
     */
    public int modeId;

    /**
     * The default display mode.
     */
    public int defaultModeId;

    /**
     * The supported modes of this display.
     */
    public android.view.Display.Mode[] supportedModes = android.view.Display.Mode.EMPTY_ARRAY;

    /**
     * The active color mode.
     */
    public int colorMode;

    /**
     * The list of supported color modes
     */
    public int[] supportedColorModes = new int[]{ android.view.Display.COLOR_MODE_DEFAULT };

    /**
     * The display's HDR capabilities
     */
    public android.view.Display.HdrCapabilities hdrCapabilities;

    /**
     * The logical display density which is the basis for density-independent
     * pixels.
     */
    public int logicalDensityDpi;

    /**
     * The exact physical pixels per inch of the screen in the X dimension.
     * <p>
     * The value of this field is indeterminate if the logical display is presented on
     * more than one physical display.
     * </p>
     */
    public float physicalXDpi;

    /**
     * The exact physical pixels per inch of the screen in the Y dimension.
     * <p>
     * The value of this field is indeterminate if the logical display is presented on
     * more than one physical display.
     * </p>
     */
    public float physicalYDpi;

    /**
     * This is a positive value indicating the phase offset of the VSYNC events provided by
     * Choreographer relative to the display refresh.  For example, if Choreographer reports
     * that the refresh occurred at time N, it actually occurred at (N - appVsyncOffsetNanos).
     */
    public long appVsyncOffsetNanos;

    /**
     * This is how far in advance a buffer must be queued for presentation at
     * a given time.  If you want a buffer to appear on the screen at
     * time N, you must submit the buffer before (N - bufferDeadlineNanos).
     */
    public long presentationDeadlineNanos;

    /**
     * The state of the display, such as {@link android.view.Display#STATE_ON}.
     */
    public int state;

    /**
     * The UID of the application that owns this display, or zero if it is owned by the system.
     * <p>
     * If the display is private, then only the owner can use it.
     * </p>
     */
    public int ownerUid;

    /**
     * The package name of the application that owns this display, or null if it is
     * owned by the system.
     * <p>
     * If the display is private, then only the owner can use it.
     * </p>
     */
    public java.lang.String ownerPackageName;

    /**
     *
     *
     * @unknown Get current remove mode of the display - what actions should be performed with the display's
    content when it is removed.
     * @see Display#getRemoveMode()
     */
    // TODO (b/114338689): Remove the flag and use IWindowManager#getRemoveContentMode
    public int removeMode = android.view.Display.REMOVE_MODE_MOVE_CONTENT_TO_PRIMARY;

    @android.annotation.NonNull
    public static final android.view.Creator<android.view.DisplayInfo> CREATOR = new android.view.Creator<android.view.DisplayInfo>() {
        @java.lang.Override
        public android.view.DisplayInfo createFromParcel(android.os.Parcel source) {
            return new android.view.DisplayInfo(source);
        }

        @java.lang.Override
        public android.view.DisplayInfo[] newArray(int size) {
            return new android.view.DisplayInfo[size];
        }
    };

    @android.annotation.UnsupportedAppUsage(maxTargetSdk = Build.VERSION_CODES.P, trackingBug = 123769467)
    public DisplayInfo() {
    }

    public DisplayInfo(android.view.DisplayInfo other) {
        copyFrom(other);
    }

    private DisplayInfo(android.os.Parcel source) {
        readFromParcel(source);
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        return (o instanceof android.view.DisplayInfo) && equals(((android.view.DisplayInfo) (o)));
    }

    public boolean equals(android.view.DisplayInfo other) {
        return ((((((((((((((((((((((((((((((((((other != null) && (layerStack == other.layerStack)) && (flags == other.flags)) && (type == other.type)) && (displayId == other.displayId)) && java.util.Objects.equals(address, other.address)) && java.util.Objects.equals(uniqueId, other.uniqueId)) && (appWidth == other.appWidth)) && (appHeight == other.appHeight)) && (smallestNominalAppWidth == other.smallestNominalAppWidth)) && (smallestNominalAppHeight == other.smallestNominalAppHeight)) && (largestNominalAppWidth == other.largestNominalAppWidth)) && (largestNominalAppHeight == other.largestNominalAppHeight)) && (logicalWidth == other.logicalWidth)) && (logicalHeight == other.logicalHeight)) && (overscanLeft == other.overscanLeft)) && (overscanTop == other.overscanTop)) && (overscanRight == other.overscanRight)) && (overscanBottom == other.overscanBottom)) && java.util.Objects.equals(displayCutout, other.displayCutout)) && (rotation == other.rotation)) && (modeId == other.modeId)) && (defaultModeId == other.defaultModeId)) && (colorMode == other.colorMode)) && java.util.Arrays.equals(supportedColorModes, other.supportedColorModes)) && java.util.Objects.equals(hdrCapabilities, other.hdrCapabilities)) && (logicalDensityDpi == other.logicalDensityDpi)) && (physicalXDpi == other.physicalXDpi)) && (physicalYDpi == other.physicalYDpi)) && (appVsyncOffsetNanos == other.appVsyncOffsetNanos)) && (presentationDeadlineNanos == other.presentationDeadlineNanos)) && (state == other.state)) && (ownerUid == other.ownerUid)) && java.util.Objects.equals(ownerPackageName, other.ownerPackageName)) && (removeMode == other.removeMode);
    }

    @java.lang.Override
    public int hashCode() {
        return 0;// don't care

    }

    public void copyFrom(android.view.DisplayInfo other) {
        layerStack = other.layerStack;
        flags = other.flags;
        type = other.type;
        displayId = other.displayId;
        address = other.address;
        name = other.name;
        uniqueId = other.uniqueId;
        appWidth = other.appWidth;
        appHeight = other.appHeight;
        smallestNominalAppWidth = other.smallestNominalAppWidth;
        smallestNominalAppHeight = other.smallestNominalAppHeight;
        largestNominalAppWidth = other.largestNominalAppWidth;
        largestNominalAppHeight = other.largestNominalAppHeight;
        logicalWidth = other.logicalWidth;
        logicalHeight = other.logicalHeight;
        overscanLeft = other.overscanLeft;
        overscanTop = other.overscanTop;
        overscanRight = other.overscanRight;
        overscanBottom = other.overscanBottom;
        displayCutout = other.displayCutout;
        rotation = other.rotation;
        modeId = other.modeId;
        defaultModeId = other.defaultModeId;
        supportedModes = java.util.Arrays.copyOf(other.supportedModes, other.supportedModes.length);
        colorMode = other.colorMode;
        supportedColorModes = java.util.Arrays.copyOf(other.supportedColorModes, other.supportedColorModes.length);
        hdrCapabilities = other.hdrCapabilities;
        logicalDensityDpi = other.logicalDensityDpi;
        physicalXDpi = other.physicalXDpi;
        physicalYDpi = other.physicalYDpi;
        appVsyncOffsetNanos = other.appVsyncOffsetNanos;
        presentationDeadlineNanos = other.presentationDeadlineNanos;
        state = other.state;
        ownerUid = other.ownerUid;
        ownerPackageName = other.ownerPackageName;
        removeMode = other.removeMode;
    }

    public void readFromParcel(android.os.Parcel source) {
        layerStack = source.readInt();
        flags = source.readInt();
        type = source.readInt();
        displayId = source.readInt();
        address = source.readParcelable(null);
        name = source.readString();
        appWidth = source.readInt();
        appHeight = source.readInt();
        smallestNominalAppWidth = source.readInt();
        smallestNominalAppHeight = source.readInt();
        largestNominalAppWidth = source.readInt();
        largestNominalAppHeight = source.readInt();
        logicalWidth = source.readInt();
        logicalHeight = source.readInt();
        overscanLeft = source.readInt();
        overscanTop = source.readInt();
        overscanRight = source.readInt();
        overscanBottom = source.readInt();
        displayCutout = android.view.DisplayCutout.ParcelableWrapper.readCutoutFromParcel(source);
        rotation = source.readInt();
        modeId = source.readInt();
        defaultModeId = source.readInt();
        int nModes = source.readInt();
        supportedModes = new android.view.Display.Mode[nModes];
        for (int i = 0; i < nModes; i++) {
            supportedModes[i] = this.CREATOR.createFromParcel(source);
        }
        colorMode = source.readInt();
        int nColorModes = source.readInt();
        supportedColorModes = new int[nColorModes];
        for (int i = 0; i < nColorModes; i++) {
            supportedColorModes[i] = source.readInt();
        }
        hdrCapabilities = source.readParcelable(null);
        logicalDensityDpi = source.readInt();
        physicalXDpi = source.readFloat();
        physicalYDpi = source.readFloat();
        appVsyncOffsetNanos = source.readLong();
        presentationDeadlineNanos = source.readLong();
        state = source.readInt();
        ownerUid = source.readInt();
        ownerPackageName = source.readString();
        uniqueId = source.readString();
        removeMode = source.readInt();
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeInt(layerStack);
        dest.writeInt(this.flags);
        dest.writeInt(type);
        dest.writeInt(displayId);
        dest.writeParcelable(address, flags);
        dest.writeString(name);
        dest.writeInt(appWidth);
        dest.writeInt(appHeight);
        dest.writeInt(smallestNominalAppWidth);
        dest.writeInt(smallestNominalAppHeight);
        dest.writeInt(largestNominalAppWidth);
        dest.writeInt(largestNominalAppHeight);
        dest.writeInt(logicalWidth);
        dest.writeInt(logicalHeight);
        dest.writeInt(overscanLeft);
        dest.writeInt(overscanTop);
        dest.writeInt(overscanRight);
        dest.writeInt(overscanBottom);
        android.view.DisplayCutout.ParcelableWrapper.writeCutoutToParcel(displayCutout, dest, flags);
        dest.writeInt(rotation);
        dest.writeInt(modeId);
        dest.writeInt(defaultModeId);
        dest.writeInt(supportedModes.length);
        for (int i = 0; i < supportedModes.length; i++) {
            supportedModes[i].writeToParcel(dest, flags);
        }
        dest.writeInt(colorMode);
        dest.writeInt(supportedColorModes.length);
        for (int i = 0; i < supportedColorModes.length; i++) {
            dest.writeInt(supportedColorModes[i]);
        }
        dest.writeParcelable(hdrCapabilities, flags);
        dest.writeInt(logicalDensityDpi);
        dest.writeFloat(physicalXDpi);
        dest.writeFloat(physicalYDpi);
        dest.writeLong(appVsyncOffsetNanos);
        dest.writeLong(presentationDeadlineNanos);
        dest.writeInt(state);
        dest.writeInt(ownerUid);
        dest.writeString(ownerPackageName);
        dest.writeString(uniqueId);
        dest.writeInt(removeMode);
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    public android.view.Display.Mode getMode() {
        return findMode(modeId);
    }

    public android.view.Display.Mode getDefaultMode() {
        return findMode(defaultModeId);
    }

    private android.view.Display.Mode findMode(int id) {
        for (int i = 0; i < supportedModes.length; i++) {
            if (supportedModes[i].getModeId() == id) {
                return supportedModes[i];
            }
        }
        throw new java.lang.IllegalStateException("Unable to locate mode " + id);
    }

    /**
     * Returns the id of the "default" mode with the given refresh rate, or {@code 0} if no suitable
     * mode could be found.
     */
    public int findDefaultModeByRefreshRate(float refreshRate) {
        android.view.Display.Mode[] modes = supportedModes;
        android.view.Display.Mode defaultMode = getDefaultMode();
        for (int i = 0; i < modes.length; i++) {
            if (modes[i].matches(defaultMode.getPhysicalWidth(), defaultMode.getPhysicalHeight(), refreshRate)) {
                return modes[i].getModeId();
            }
        }
        return 0;
    }

    /**
     * Returns the list of supported refresh rates in the default mode.
     */
    public float[] getDefaultRefreshRates() {
        android.view.Display.Mode[] modes = supportedModes;
        android.util.ArraySet<java.lang.Float> rates = new android.util.ArraySet();
        android.view.Display.Mode defaultMode = getDefaultMode();
        for (int i = 0; i < modes.length; i++) {
            android.view.Display.Mode mode = modes[i];
            if ((mode.getPhysicalWidth() == defaultMode.getPhysicalWidth()) && (mode.getPhysicalHeight() == defaultMode.getPhysicalHeight())) {
                rates.add(mode.getRefreshRate());
            }
        }
        float[] result = new float[rates.size()];
        int i = 0;
        for (java.lang.Float rate : rates) {
            result[i++] = rate;
        }
        return result;
    }

    public void getAppMetrics(android.util.DisplayMetrics outMetrics) {
        getAppMetrics(outMetrics, android.content.res.CompatibilityInfo.DEFAULT_COMPATIBILITY_INFO, null);
    }

    public void getAppMetrics(android.util.DisplayMetrics outMetrics, android.view.DisplayAdjustments displayAdjustments) {
        getMetricsWithSize(outMetrics, displayAdjustments.getCompatibilityInfo(), displayAdjustments.getConfiguration(), appWidth, appHeight);
    }

    public void getAppMetrics(android.util.DisplayMetrics outMetrics, android.content.res.CompatibilityInfo ci, android.content.res.Configuration configuration) {
        getMetricsWithSize(outMetrics, ci, configuration, appWidth, appHeight);
    }

    public void getLogicalMetrics(android.util.DisplayMetrics outMetrics, android.content.res.CompatibilityInfo compatInfo, android.content.res.Configuration configuration) {
        getMetricsWithSize(outMetrics, compatInfo, configuration, logicalWidth, logicalHeight);
    }

    public int getNaturalWidth() {
        return (rotation == android.view.Surface.ROTATION_0) || (rotation == android.view.Surface.ROTATION_180) ? logicalWidth : logicalHeight;
    }

    public int getNaturalHeight() {
        return (rotation == android.view.Surface.ROTATION_0) || (rotation == android.view.Surface.ROTATION_180) ? logicalHeight : logicalWidth;
    }

    public boolean isHdr() {
        int[] types = (hdrCapabilities != null) ? hdrCapabilities.getSupportedHdrTypes() : null;
        return (types != null) && (types.length > 0);
    }

    public boolean isWideColorGamut() {
        for (int colorMode : supportedColorModes) {
            if ((colorMode == android.view.Display.COLOR_MODE_DCI_P3) || (colorMode > android.view.Display.COLOR_MODE_SRGB)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns true if the specified UID has access to this display.
     */
    public boolean hasAccess(int uid) {
        return android.view.Display.hasAccess(uid, flags, ownerUid, displayId);
    }

    private void getMetricsWithSize(android.util.DisplayMetrics outMetrics, android.content.res.CompatibilityInfo compatInfo, android.content.res.Configuration configuration, int width, int height) {
        outMetrics.densityDpi = outMetrics.noncompatDensityDpi = logicalDensityDpi;
        outMetrics.density = outMetrics.noncompatDensity = logicalDensityDpi * android.util.DisplayMetrics.DENSITY_DEFAULT_SCALE;
        outMetrics.scaledDensity = outMetrics.noncompatScaledDensity = outMetrics.density;
        outMetrics.xdpi = outMetrics.noncompatXdpi = physicalXDpi;
        outMetrics.ydpi = outMetrics.noncompatYdpi = physicalYDpi;
        final android.graphics.Rect appBounds = (configuration != null) ? configuration.windowConfiguration.getAppBounds() : null;
        width = (appBounds != null) ? appBounds.width() : width;
        height = (appBounds != null) ? appBounds.height() : height;
        outMetrics.noncompatWidthPixels = outMetrics.widthPixels = width;
        outMetrics.noncompatHeightPixels = outMetrics.heightPixels = height;
        if (!compatInfo.equals(android.content.res.CompatibilityInfo.DEFAULT_COMPATIBILITY_INFO)) {
            compatInfo.applyToDisplayMetrics(outMetrics);
        }
    }

    // For debugging purposes
    @java.lang.Override
    public java.lang.String toString() {
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        sb.append("DisplayInfo{\"");
        sb.append(name);
        sb.append(", displayId ");
        sb.append(displayId);
        sb.append("\", uniqueId \"");
        sb.append(uniqueId);
        sb.append("\", app ");
        sb.append(appWidth);
        sb.append(" x ");
        sb.append(appHeight);
        sb.append(", real ");
        sb.append(logicalWidth);
        sb.append(" x ");
        sb.append(logicalHeight);
        if ((((overscanLeft != 0) || (overscanTop != 0)) || (overscanRight != 0)) || (overscanBottom != 0)) {
            sb.append(", overscan (");
            sb.append(overscanLeft);
            sb.append(",");
            sb.append(overscanTop);
            sb.append(",");
            sb.append(overscanRight);
            sb.append(",");
            sb.append(overscanBottom);
            sb.append(")");
        }
        sb.append(", largest app ");
        sb.append(largestNominalAppWidth);
        sb.append(" x ");
        sb.append(largestNominalAppHeight);
        sb.append(", smallest app ");
        sb.append(smallestNominalAppWidth);
        sb.append(" x ");
        sb.append(smallestNominalAppHeight);
        sb.append(", mode ");
        sb.append(modeId);
        sb.append(", defaultMode ");
        sb.append(defaultModeId);
        sb.append(", modes ");
        sb.append(java.util.Arrays.toString(supportedModes));
        sb.append(", colorMode ");
        sb.append(colorMode);
        sb.append(", supportedColorModes ");
        sb.append(java.util.Arrays.toString(supportedColorModes));
        sb.append(", hdrCapabilities ");
        sb.append(hdrCapabilities);
        sb.append(", rotation ");
        sb.append(rotation);
        sb.append(", density ");
        sb.append(logicalDensityDpi);
        sb.append(" (");
        sb.append(physicalXDpi);
        sb.append(" x ");
        sb.append(physicalYDpi);
        sb.append(") dpi, layerStack ");
        sb.append(layerStack);
        sb.append(", appVsyncOff ");
        sb.append(appVsyncOffsetNanos);
        sb.append(", presDeadline ");
        sb.append(presentationDeadlineNanos);
        sb.append(", type ");
        sb.append(android.view.Display.typeToString(type));
        if (address != null) {
            sb.append(", address ").append(address);
        }
        sb.append(", state ");
        sb.append(android.view.Display.stateToString(state));
        if ((ownerUid != 0) || (ownerPackageName != null)) {
            sb.append(", owner ").append(ownerPackageName);
            sb.append(" (uid ").append(ownerUid).append(")");
        }
        sb.append(android.view.DisplayInfo.flagsToString(flags));
        sb.append(", removeMode ");
        sb.append(removeMode);
        sb.append("}");
        return sb.toString();
    }

    /**
     * Write to a protocol buffer output stream.
     * Protocol buffer message definition at {@link android.view.DisplayInfoProto}
     *
     * @param protoOutputStream
     * 		Stream to write the Rect object to.
     * @param fieldId
     * 		Field Id of the DisplayInfoProto as defined in the parent message
     */
    public void writeToProto(android.util.proto.ProtoOutputStream protoOutputStream, long fieldId) {
        final long token = protoOutputStream.start(fieldId);
        protoOutputStream.write(android.view.DisplayInfoProto.LOGICAL_WIDTH, logicalWidth);
        protoOutputStream.write(android.view.DisplayInfoProto.LOGICAL_HEIGHT, logicalHeight);
        protoOutputStream.write(android.view.DisplayInfoProto.APP_WIDTH, appWidth);
        protoOutputStream.write(android.view.DisplayInfoProto.APP_HEIGHT, appHeight);
        protoOutputStream.write(android.view.DisplayInfoProto.NAME, name);
        protoOutputStream.end(token);
    }

    private static java.lang.String flagsToString(int flags) {
        java.lang.StringBuilder result = new java.lang.StringBuilder();
        if ((flags & android.view.Display.FLAG_SECURE) != 0) {
            result.append(", FLAG_SECURE");
        }
        if ((flags & android.view.Display.FLAG_SUPPORTS_PROTECTED_BUFFERS) != 0) {
            result.append(", FLAG_SUPPORTS_PROTECTED_BUFFERS");
        }
        if ((flags & android.view.Display.FLAG_PRIVATE) != 0) {
            result.append(", FLAG_PRIVATE");
        }
        if ((flags & android.view.Display.FLAG_PRESENTATION) != 0) {
            result.append(", FLAG_PRESENTATION");
        }
        if ((flags & android.view.Display.FLAG_SCALING_DISABLED) != 0) {
            result.append(", FLAG_SCALING_DISABLED");
        }
        if ((flags & android.view.Display.FLAG_ROUND) != 0) {
            result.append(", FLAG_ROUND");
        }
        return result.toString();
    }
}

