/**
 * Copyright (C) 2006 The Android Open Source Project
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
 * CompatibilityInfo class keeps the information about compatibility mode that the application is
 * running under.
 *
 *  {@hide }
 */
public class CompatibilityInfo implements android.os.Parcelable {
    /**
     * default compatibility info object for compatible applications
     */
    @android.annotation.UnsupportedAppUsage
    public static final android.content.res.CompatibilityInfo DEFAULT_COMPATIBILITY_INFO = new android.content.res.CompatibilityInfo() {};

    /**
     * This is the number of pixels we would like to have along the
     * short axis of an app that needs to run on a normal size screen.
     */
    public static final int DEFAULT_NORMAL_SHORT_DIMENSION = 320;

    /**
     * This is the maximum aspect ratio we will allow while keeping
     * applications in a compatible screen size.
     */
    public static final float MAXIMUM_ASPECT_RATIO = 854.0F / 480.0F;

    /**
     * A compatibility flags
     */
    private final int mCompatibilityFlags;

    /**
     * A flag mask to tell if the application needs scaling (when mApplicationScale != 1.0f)
     * {@see compatibilityFlag}
     */
    private static final int SCALING_REQUIRED = 1;

    /**
     * Application must always run in compatibility mode?
     */
    private static final int ALWAYS_NEEDS_COMPAT = 2;

    /**
     * Application never should run in compatibility mode?
     */
    private static final int NEVER_NEEDS_COMPAT = 4;

    /**
     * Set if the application needs to run in screen size compatibility mode.
     */
    private static final int NEEDS_SCREEN_COMPAT = 8;

    /**
     * Set if the application needs to run in with compat resources.
     */
    private static final int NEEDS_COMPAT_RES = 16;

    /**
     * The effective screen density we have selected for this application.
     */
    public final int applicationDensity;

    /**
     * Application's scale.
     */
    @android.annotation.UnsupportedAppUsage
    public final float applicationScale;

    /**
     * Application's inverted scale.
     */
    public final float applicationInvertedScale;

    @android.annotation.UnsupportedAppUsage
    public CompatibilityInfo(android.content.pm.ApplicationInfo appInfo, int screenLayout, int sw, boolean forceCompat) {
        int compatFlags = 0;
        if (appInfo.targetSdkVersion < android.os.Build.VERSION_CODES.O) {
            compatFlags |= android.content.res.CompatibilityInfo.NEEDS_COMPAT_RES;
        }
        if (((appInfo.requiresSmallestWidthDp != 0) || (appInfo.compatibleWidthLimitDp != 0)) || (appInfo.largestWidthLimitDp != 0)) {
            // New style screen requirements spec.
            int required = (appInfo.requiresSmallestWidthDp != 0) ? appInfo.requiresSmallestWidthDp : appInfo.compatibleWidthLimitDp;
            if (required == 0) {
                required = appInfo.largestWidthLimitDp;
            }
            int compat = (appInfo.compatibleWidthLimitDp != 0) ? appInfo.compatibleWidthLimitDp : required;
            if (compat < required) {
                compat = required;
            }
            int largest = appInfo.largestWidthLimitDp;
            if (required > android.content.res.CompatibilityInfo.DEFAULT_NORMAL_SHORT_DIMENSION) {
                // For now -- if they require a size larger than the only
                // size we can do in compatibility mode, then don't ever
                // allow the app to go in to compat mode.  Trying to run
                // it at a smaller size it can handle will make it far more
                // broken than running at a larger size than it wants or
                // thinks it can handle.
                compatFlags |= android.content.res.CompatibilityInfo.NEVER_NEEDS_COMPAT;
            } else
                if ((largest != 0) && (sw > largest)) {
                    // If the screen size is larger than the largest size the
                    // app thinks it can work with, then always force it in to
                    // compatibility mode.
                    compatFlags |= android.content.res.CompatibilityInfo.NEEDS_SCREEN_COMPAT | android.content.res.CompatibilityInfo.ALWAYS_NEEDS_COMPAT;
                } else
                    if (compat >= sw) {
                        // The screen size is something the app says it was designed
                        // for, so never do compatibility mode.
                        compatFlags |= android.content.res.CompatibilityInfo.NEVER_NEEDS_COMPAT;
                    } else
                        if (forceCompat) {
                            // The app may work better with or without compatibility mode.
                            // Let the user decide.
                            compatFlags |= android.content.res.CompatibilityInfo.NEEDS_SCREEN_COMPAT;
                        }



            // Modern apps always support densities.
            applicationDensity = android.util.DisplayMetrics.DENSITY_DEVICE;
            applicationScale = 1.0F;
            applicationInvertedScale = 1.0F;
        } else {
            /**
             * Has the application said that its UI is expandable?  Based on the
             * <supports-screen> android:expandible in the manifest.
             */
            final int EXPANDABLE = 2;
            /**
             * Has the application said that its UI supports large screens?  Based on the
             * <supports-screen> android:largeScreens in the manifest.
             */
            final int LARGE_SCREENS = 8;
            /**
             * Has the application said that its UI supports xlarge screens?  Based on the
             * <supports-screen> android:xlargeScreens in the manifest.
             */
            final int XLARGE_SCREENS = 32;
            int sizeInfo = 0;
            // We can't rely on the application always setting
            // FLAG_RESIZEABLE_FOR_SCREENS so will compute it based on various input.
            boolean anyResizeable = false;
            if ((appInfo.flags & android.content.pm.ApplicationInfo.FLAG_SUPPORTS_LARGE_SCREENS) != 0) {
                sizeInfo |= LARGE_SCREENS;
                anyResizeable = true;
                if (!forceCompat) {
                    // If we aren't forcing the app into compatibility mode, then
                    // assume if it supports large screens that we should allow it
                    // to use the full space of an xlarge screen as well.
                    sizeInfo |= XLARGE_SCREENS | EXPANDABLE;
                }
            }
            if ((appInfo.flags & android.content.pm.ApplicationInfo.FLAG_SUPPORTS_XLARGE_SCREENS) != 0) {
                anyResizeable = true;
                if (!forceCompat) {
                    sizeInfo |= XLARGE_SCREENS | EXPANDABLE;
                }
            }
            if ((appInfo.flags & android.content.pm.ApplicationInfo.FLAG_RESIZEABLE_FOR_SCREENS) != 0) {
                anyResizeable = true;
                sizeInfo |= EXPANDABLE;
            }
            if (forceCompat) {
                // If we are forcing compatibility mode, then ignore an app that
                // just says it is resizable for screens.  We'll only have it fill
                // the screen if it explicitly says it supports the screen size we
                // are running in.
                sizeInfo &= ~EXPANDABLE;
            }
            compatFlags |= android.content.res.CompatibilityInfo.NEEDS_SCREEN_COMPAT;
            switch (screenLayout & android.content.res.Configuration.SCREENLAYOUT_SIZE_MASK) {
                case android.content.res.Configuration.SCREENLAYOUT_SIZE_XLARGE :
                    if ((sizeInfo & XLARGE_SCREENS) != 0) {
                        compatFlags &= ~android.content.res.CompatibilityInfo.NEEDS_SCREEN_COMPAT;
                    }
                    if ((appInfo.flags & android.content.pm.ApplicationInfo.FLAG_SUPPORTS_XLARGE_SCREENS) != 0) {
                        compatFlags |= android.content.res.CompatibilityInfo.NEVER_NEEDS_COMPAT;
                    }
                    break;
                case android.content.res.Configuration.SCREENLAYOUT_SIZE_LARGE :
                    if ((sizeInfo & LARGE_SCREENS) != 0) {
                        compatFlags &= ~android.content.res.CompatibilityInfo.NEEDS_SCREEN_COMPAT;
                    }
                    if ((appInfo.flags & android.content.pm.ApplicationInfo.FLAG_SUPPORTS_LARGE_SCREENS) != 0) {
                        compatFlags |= android.content.res.CompatibilityInfo.NEVER_NEEDS_COMPAT;
                    }
                    break;
            }
            if ((screenLayout & android.content.res.Configuration.SCREENLAYOUT_COMPAT_NEEDED) != 0) {
                if ((sizeInfo & EXPANDABLE) != 0) {
                    compatFlags &= ~android.content.res.CompatibilityInfo.NEEDS_SCREEN_COMPAT;
                } else
                    if (!anyResizeable) {
                        compatFlags |= android.content.res.CompatibilityInfo.ALWAYS_NEEDS_COMPAT;
                    }

            } else {
                compatFlags &= ~android.content.res.CompatibilityInfo.NEEDS_SCREEN_COMPAT;
                compatFlags |= android.content.res.CompatibilityInfo.NEVER_NEEDS_COMPAT;
            }
            if ((appInfo.flags & android.content.pm.ApplicationInfo.FLAG_SUPPORTS_SCREEN_DENSITIES) != 0) {
                applicationDensity = android.util.DisplayMetrics.DENSITY_DEVICE;
                applicationScale = 1.0F;
                applicationInvertedScale = 1.0F;
            } else {
                applicationDensity = android.util.DisplayMetrics.DENSITY_DEFAULT;
                applicationScale = android.util.DisplayMetrics.DENSITY_DEVICE / ((float) (android.util.DisplayMetrics.DENSITY_DEFAULT));
                applicationInvertedScale = 1.0F / applicationScale;
                compatFlags |= android.content.res.CompatibilityInfo.SCALING_REQUIRED;
            }
        }
        mCompatibilityFlags = compatFlags;
    }

    private CompatibilityInfo(int compFlags, int dens, float scale, float invertedScale) {
        mCompatibilityFlags = compFlags;
        applicationDensity = dens;
        applicationScale = scale;
        applicationInvertedScale = invertedScale;
    }

    @android.annotation.UnsupportedAppUsage
    private CompatibilityInfo() {
        this(android.content.res.CompatibilityInfo.NEVER_NEEDS_COMPAT, DisplayMetrics.DENSITY_DEVICE, 1.0F, 1.0F);
    }

    /**
     *
     *
     * @return true if the scaling is required
     */
    @android.annotation.UnsupportedAppUsage
    public boolean isScalingRequired() {
        return (mCompatibilityFlags & android.content.res.CompatibilityInfo.SCALING_REQUIRED) != 0;
    }

    @android.annotation.UnsupportedAppUsage
    public boolean supportsScreen() {
        return (mCompatibilityFlags & android.content.res.CompatibilityInfo.NEEDS_SCREEN_COMPAT) == 0;
    }

    public boolean neverSupportsScreen() {
        return (mCompatibilityFlags & android.content.res.CompatibilityInfo.ALWAYS_NEEDS_COMPAT) != 0;
    }

    public boolean alwaysSupportsScreen() {
        return (mCompatibilityFlags & android.content.res.CompatibilityInfo.NEVER_NEEDS_COMPAT) != 0;
    }

    public boolean needsCompatResources() {
        return (mCompatibilityFlags & android.content.res.CompatibilityInfo.NEEDS_COMPAT_RES) != 0;
    }

    /**
     * Returns the translator which translates the coordinates in compatibility mode.
     *
     * @param params
     * 		the window's parameter
     */
    @android.annotation.UnsupportedAppUsage
    public android.content.res.CompatibilityInfo.Translator getTranslator() {
        return isScalingRequired() ? new android.content.res.CompatibilityInfo.Translator() : null;
    }

    /**
     * A helper object to translate the screen and window coordinates back and forth.
     *
     * @unknown 
     */
    public class Translator {
        @android.annotation.UnsupportedAppUsage
        public final float applicationScale;

        @android.annotation.UnsupportedAppUsage
        public final float applicationInvertedScale;

        private android.graphics.Rect mContentInsetsBuffer = null;

        private android.graphics.Rect mVisibleInsetsBuffer = null;

        private android.graphics.Region mTouchableAreaBuffer = null;

        Translator(float applicationScale, float applicationInvertedScale) {
            this.applicationScale = applicationScale;
            this.applicationInvertedScale = applicationInvertedScale;
        }

        Translator() {
            this(android.content.res.CompatibilityInfo.this.applicationScale, android.content.res.CompatibilityInfo.this.applicationInvertedScale);
        }

        /**
         * Translate the screen rect to the application frame.
         */
        @android.annotation.UnsupportedAppUsage
        public void translateRectInScreenToAppWinFrame(android.graphics.Rect rect) {
            rect.scale(applicationInvertedScale);
        }

        /**
         * Translate the region in window to screen.
         */
        @android.annotation.UnsupportedAppUsage
        public void translateRegionInWindowToScreen(android.graphics.Region transparentRegion) {
            transparentRegion.scale(applicationScale);
        }

        /**
         * Apply translation to the canvas that is necessary to draw the content.
         */
        @android.annotation.UnsupportedAppUsage
        public void translateCanvas(android.graphics.Canvas canvas) {
            if (applicationScale == 1.5F) {
                /* When we scale for compatibility, we can put our stretched
                bitmaps and ninepatches on exacty 1/2 pixel boundaries,
                which can give us inconsistent drawing due to imperfect
                float precision in the graphics engine's inverse matrix.

                As a work-around, we translate by a tiny amount to avoid
                landing on exact pixel centers and boundaries, giving us
                the slop we need to draw consistently.

                This constant is meant to resolve to 1/255 after it is
                scaled by 1.5 (applicationScale). Note, this is just a guess
                as to what is small enough not to create its own artifacts,
                and big enough to avoid the precision problems. Feel free
                to experiment with smaller values as you choose.
                 */
                final float tinyOffset = 2.0F / (3 * 255);
                canvas.translate(tinyOffset, tinyOffset);
            }
            canvas.scale(applicationScale, applicationScale);
        }

        /**
         * Translate the motion event captured on screen to the application's window.
         */
        @android.annotation.UnsupportedAppUsage
        public void translateEventInScreenToAppWindow(android.view.MotionEvent event) {
            event.scale(applicationInvertedScale);
        }

        /**
         * Translate the window's layout parameter, from application's view to
         * Screen's view.
         */
        @android.annotation.UnsupportedAppUsage
        public void translateWindowLayout(android.view.WindowManager.LayoutParams params) {
            params.scale(applicationScale);
        }

        /**
         * Translate a Rect in application's window to screen.
         */
        @android.annotation.UnsupportedAppUsage
        public void translateRectInAppWindowToScreen(android.graphics.Rect rect) {
            rect.scale(applicationScale);
        }

        /**
         * Translate a Rect in screen coordinates into the app window's coordinates.
         */
        @android.annotation.UnsupportedAppUsage
        public void translateRectInScreenToAppWindow(android.graphics.Rect rect) {
            rect.scale(applicationInvertedScale);
        }

        /**
         * Translate a Point in screen coordinates into the app window's coordinates.
         */
        public void translatePointInScreenToAppWindow(android.graphics.PointF point) {
            final float scale = applicationInvertedScale;
            if (scale != 1.0F) {
                point.x *= scale;
                point.y *= scale;
            }
        }

        /**
         * Translate the location of the sub window.
         *
         * @param params
         * 		
         */
        public void translateLayoutParamsInAppWindowToScreen(android.view.WindowManager.LayoutParams params) {
            params.scale(applicationScale);
        }

        /**
         * Translate the content insets in application window to Screen. This uses
         * the internal buffer for content insets to avoid extra object allocation.
         */
        @android.annotation.UnsupportedAppUsage
        public android.graphics.Rect getTranslatedContentInsets(android.graphics.Rect contentInsets) {
            if (mContentInsetsBuffer == null)
                mContentInsetsBuffer = new android.graphics.Rect();

            mContentInsetsBuffer.set(contentInsets);
            translateRectInAppWindowToScreen(mContentInsetsBuffer);
            return mContentInsetsBuffer;
        }

        /**
         * Translate the visible insets in application window to Screen. This uses
         * the internal buffer for visible insets to avoid extra object allocation.
         */
        public android.graphics.Rect getTranslatedVisibleInsets(android.graphics.Rect visibleInsets) {
            if (mVisibleInsetsBuffer == null)
                mVisibleInsetsBuffer = new android.graphics.Rect();

            mVisibleInsetsBuffer.set(visibleInsets);
            translateRectInAppWindowToScreen(mVisibleInsetsBuffer);
            return mVisibleInsetsBuffer;
        }

        /**
         * Translate the touchable area in application window to Screen. This uses
         * the internal buffer for touchable area to avoid extra object allocation.
         */
        public android.graphics.Region getTranslatedTouchableArea(android.graphics.Region touchableArea) {
            if (mTouchableAreaBuffer == null)
                mTouchableAreaBuffer = new android.graphics.Region();

            mTouchableAreaBuffer.set(touchableArea);
            mTouchableAreaBuffer.scale(applicationScale);
            return mTouchableAreaBuffer;
        }
    }

    public void applyToDisplayMetrics(android.util.DisplayMetrics inoutDm) {
        if (!supportsScreen()) {
            // This is a larger screen device and the app is not
            // compatible with large screens, so diddle it.
            android.content.res.CompatibilityInfo.computeCompatibleScaling(inoutDm, inoutDm);
        } else {
            inoutDm.widthPixels = inoutDm.noncompatWidthPixels;
            inoutDm.heightPixels = inoutDm.noncompatHeightPixels;
        }
        if (isScalingRequired()) {
            float invertedRatio = applicationInvertedScale;
            inoutDm.density = inoutDm.noncompatDensity * invertedRatio;
            inoutDm.densityDpi = ((int) ((inoutDm.noncompatDensityDpi * invertedRatio) + 0.5F));
            inoutDm.scaledDensity = inoutDm.noncompatScaledDensity * invertedRatio;
            inoutDm.xdpi = inoutDm.noncompatXdpi * invertedRatio;
            inoutDm.ydpi = inoutDm.noncompatYdpi * invertedRatio;
            inoutDm.widthPixels = ((int) ((inoutDm.widthPixels * invertedRatio) + 0.5F));
            inoutDm.heightPixels = ((int) ((inoutDm.heightPixels * invertedRatio) + 0.5F));
        }
    }

    public void applyToConfiguration(int displayDensity, android.content.res.Configuration inoutConfig) {
        if (!supportsScreen()) {
            // This is a larger screen device and the app is not
            // compatible with large screens, so we are forcing it to
            // run as if the screen is normal size.
            inoutConfig.screenLayout = (inoutConfig.screenLayout & (~android.content.res.Configuration.SCREENLAYOUT_SIZE_MASK)) | android.content.res.Configuration.SCREENLAYOUT_SIZE_NORMAL;
            inoutConfig.screenWidthDp = inoutConfig.compatScreenWidthDp;
            inoutConfig.screenHeightDp = inoutConfig.compatScreenHeightDp;
            inoutConfig.smallestScreenWidthDp = inoutConfig.compatSmallestScreenWidthDp;
        }
        inoutConfig.densityDpi = displayDensity;
        if (isScalingRequired()) {
            float invertedRatio = applicationInvertedScale;
            inoutConfig.densityDpi = ((int) ((inoutConfig.densityDpi * invertedRatio) + 0.5F));
        }
    }

    /**
     * Compute the frame Rect for applications runs under compatibility mode.
     *
     * @param dm
     * 		the display metrics used to compute the frame size.
     * @param outDm
     * 		If non-null the width and height will be set to their scaled values.
     * @return Returns the scaling factor for the window.
     */
    @android.annotation.UnsupportedAppUsage
    public static float computeCompatibleScaling(android.util.DisplayMetrics dm, android.util.DisplayMetrics outDm) {
        final int width = dm.noncompatWidthPixels;
        final int height = dm.noncompatHeightPixels;
        int shortSize;
        int longSize;
        if (width < height) {
            shortSize = width;
            longSize = height;
        } else {
            shortSize = height;
            longSize = width;
        }
        int newShortSize = ((int) ((android.content.res.CompatibilityInfo.DEFAULT_NORMAL_SHORT_DIMENSION * dm.density) + 0.5F));
        float aspect = ((float) (longSize)) / shortSize;
        if (aspect > android.content.res.CompatibilityInfo.MAXIMUM_ASPECT_RATIO) {
            aspect = android.content.res.CompatibilityInfo.MAXIMUM_ASPECT_RATIO;
        }
        int newLongSize = ((int) ((newShortSize * aspect) + 0.5F));
        int newWidth;
        int newHeight;
        if (width < height) {
            newWidth = newShortSize;
            newHeight = newLongSize;
        } else {
            newWidth = newLongSize;
            newHeight = newShortSize;
        }
        float sw = width / ((float) (newWidth));
        float sh = height / ((float) (newHeight));
        float scale = (sw < sh) ? sw : sh;
        if (scale < 1) {
            scale = 1;
        }
        if (outDm != null) {
            outDm.widthPixels = newWidth;
            outDm.heightPixels = newHeight;
        }
        return scale;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        try {
            android.content.res.CompatibilityInfo oc = ((android.content.res.CompatibilityInfo) (o));
            if (mCompatibilityFlags != oc.mCompatibilityFlags)
                return false;

            if (applicationDensity != oc.applicationDensity)
                return false;

            if (applicationScale != oc.applicationScale)
                return false;

            if (applicationInvertedScale != oc.applicationInvertedScale)
                return false;

            return true;
        } catch (java.lang.ClassCastException e) {
            return false;
        }
    }

    @java.lang.Override
    public java.lang.String toString() {
        java.lang.StringBuilder sb = new java.lang.StringBuilder(128);
        sb.append("{");
        sb.append(applicationDensity);
        sb.append("dpi");
        if (isScalingRequired()) {
            sb.append(" ");
            sb.append(applicationScale);
            sb.append("x");
        }
        if (!supportsScreen()) {
            sb.append(" resizing");
        }
        if (neverSupportsScreen()) {
            sb.append(" never-compat");
        }
        if (alwaysSupportsScreen()) {
            sb.append(" always-compat");
        }
        sb.append("}");
        return sb.toString();
    }

    @java.lang.Override
    public int hashCode() {
        int result = 17;
        result = (31 * result) + mCompatibilityFlags;
        result = (31 * result) + applicationDensity;
        result = (31 * result) + java.lang.Float.floatToIntBits(applicationScale);
        result = (31 * result) + java.lang.Float.floatToIntBits(applicationInvertedScale);
        return result;
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeInt(mCompatibilityFlags);
        dest.writeInt(applicationDensity);
        dest.writeFloat(applicationScale);
        dest.writeFloat(applicationInvertedScale);
    }

    @android.annotation.UnsupportedAppUsage(maxTargetSdk = android.os.Build.VERSION_CODES.P, trackingBug = 115609023)
    @android.annotation.NonNull
    public static final android.os.Parcelable.Creator<android.content.res.CompatibilityInfo> CREATOR = new android.os.Parcelable.Creator<android.content.res.CompatibilityInfo>() {
        @java.lang.Override
        public android.content.res.CompatibilityInfo createFromParcel(android.os.Parcel source) {
            return new android.content.res.CompatibilityInfo(source);
        }

        @java.lang.Override
        public android.content.res.CompatibilityInfo[] newArray(int size) {
            return new android.content.res.CompatibilityInfo[size];
        }
    };

    private CompatibilityInfo(android.os.Parcel source) {
        mCompatibilityFlags = source.readInt();
        applicationDensity = source.readInt();
        applicationScale = source.readFloat();
        applicationInvertedScale = source.readFloat();
    }
}

