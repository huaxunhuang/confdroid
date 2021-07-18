/**
 * Copyright 2017 The Android Open Source Project
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
 * Represents the area of the display that is not functional for displaying content.
 *
 * <p>{@code DisplayCutout} is immutable.
 */
public final class DisplayCutout {
    private static final java.lang.String TAG = "DisplayCutout";

    private static final java.lang.String BOTTOM_MARKER = "@bottom";

    private static final java.lang.String DP_MARKER = "@dp";

    private static final java.lang.String RIGHT_MARKER = "@right";

    /**
     * Category for overlays that allow emulating a display cutout on devices that don't have
     * one.
     *
     * @see android.content.om.IOverlayManager
     * @unknown 
     */
    public static final java.lang.String EMULATION_OVERLAY_CATEGORY = "com.android.internal.display_cutout_emulation";

    private static final android.graphics.Rect ZERO_RECT = new android.graphics.Rect();

    /**
     * An instance where {@link #isEmpty()} returns {@code true}.
     *
     * @unknown 
     */
    public static final android.view.DisplayCutout NO_CUTOUT = /* copyArguments */
    new android.view.DisplayCutout(android.view.DisplayCutout.ZERO_RECT, android.view.DisplayCutout.ZERO_RECT, android.view.DisplayCutout.ZERO_RECT, android.view.DisplayCutout.ZERO_RECT, android.view.DisplayCutout.ZERO_RECT, false);

    private static final android.util.Pair<android.graphics.Path, android.view.DisplayCutout> NULL_PAIR = new android.util.Pair(null, null);

    private static final java.lang.Object CACHE_LOCK = new java.lang.Object();

    @com.android.internal.annotations.GuardedBy("CACHE_LOCK")
    private static java.lang.String sCachedSpec;

    @com.android.internal.annotations.GuardedBy("CACHE_LOCK")
    private static int sCachedDisplayWidth;

    @com.android.internal.annotations.GuardedBy("CACHE_LOCK")
    private static int sCachedDisplayHeight;

    @com.android.internal.annotations.GuardedBy("CACHE_LOCK")
    private static float sCachedDensity;

    @com.android.internal.annotations.GuardedBy("CACHE_LOCK")
    private static android.util.Pair<android.graphics.Path, android.view.DisplayCutout> sCachedCutout = android.view.DisplayCutout.NULL_PAIR;

    private final android.graphics.Rect mSafeInsets;

    /**
     * The bound is at the left of the screen.
     *
     * @unknown 
     */
    public static final int BOUNDS_POSITION_LEFT = 0;

    /**
     * The bound is at the top of the screen.
     *
     * @unknown 
     */
    public static final int BOUNDS_POSITION_TOP = 1;

    /**
     * The bound is at the right of the screen.
     *
     * @unknown 
     */
    public static final int BOUNDS_POSITION_RIGHT = 2;

    /**
     * The bound is at the bottom of the screen.
     *
     * @unknown 
     */
    public static final int BOUNDS_POSITION_BOTTOM = 3;

    /**
     * The number of possible positions at which bounds can be located.
     *
     * @unknown 
     */
    public static final int BOUNDS_POSITION_LENGTH = 4;

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.IntDef(prefix = { "BOUNDS_POSITION_" }, value = { android.view.DisplayCutout.BOUNDS_POSITION_LEFT, android.view.DisplayCutout.BOUNDS_POSITION_TOP, android.view.DisplayCutout.BOUNDS_POSITION_RIGHT, android.view.DisplayCutout.BOUNDS_POSITION_BOTTOM })
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface BoundsPosition {}

    private static class Bounds {
        private final android.graphics.Rect[] mRects;

        private Bounds(android.graphics.Rect left, android.graphics.Rect top, android.graphics.Rect right, android.graphics.Rect bottom, boolean copyArguments) {
            mRects = new android.graphics.Rect[android.view.DisplayCutout.BOUNDS_POSITION_LENGTH];
            mRects[android.view.DisplayCutout.BOUNDS_POSITION_LEFT] = android.view.DisplayCutout.getCopyOrRef(left, copyArguments);
            mRects[android.view.DisplayCutout.BOUNDS_POSITION_TOP] = android.view.DisplayCutout.getCopyOrRef(top, copyArguments);
            mRects[android.view.DisplayCutout.BOUNDS_POSITION_RIGHT] = android.view.DisplayCutout.getCopyOrRef(right, copyArguments);
            mRects[android.view.DisplayCutout.BOUNDS_POSITION_BOTTOM] = android.view.DisplayCutout.getCopyOrRef(bottom, copyArguments);
        }

        private Bounds(android.graphics.Rect[] rects, boolean copyArguments) {
            if (rects.length != android.view.DisplayCutout.BOUNDS_POSITION_LENGTH) {
                throw new java.lang.IllegalArgumentException("rects must have exactly 4 elements: rects=" + java.util.Arrays.toString(rects));
            }
            if (copyArguments) {
                mRects = new android.graphics.Rect[android.view.DisplayCutout.BOUNDS_POSITION_LENGTH];
                for (int i = 0; i < android.view.DisplayCutout.BOUNDS_POSITION_LENGTH; ++i) {
                    mRects[i] = new android.graphics.Rect(rects[i]);
                }
            } else {
                for (android.graphics.Rect rect : rects) {
                    if (rect == null) {
                        throw new java.lang.IllegalArgumentException("rects must have non-null elements: rects=" + java.util.Arrays.toString(rects));
                    }
                }
                mRects = rects;
            }
        }

        private boolean isEmpty() {
            for (android.graphics.Rect rect : mRects) {
                if (!rect.isEmpty()) {
                    return false;
                }
            }
            return true;
        }

        private android.graphics.Rect getRect(@android.view.DisplayCutout.BoundsPosition
        int pos) {
            return new android.graphics.Rect(mRects[pos]);
        }

        private android.graphics.Rect[] getRects() {
            android.graphics.Rect[] rects = new android.graphics.Rect[android.view.DisplayCutout.BOUNDS_POSITION_LENGTH];
            for (int i = 0; i < android.view.DisplayCutout.BOUNDS_POSITION_LENGTH; ++i) {
                rects[i] = new android.graphics.Rect(mRects[i]);
            }
            return rects;
        }

        @java.lang.Override
        public int hashCode() {
            int result = 0;
            for (android.graphics.Rect rect : mRects) {
                result = (result * 48271) + rect.hashCode();
            }
            return result;
        }

        @java.lang.Override
        public boolean equals(java.lang.Object o) {
            if (o == this) {
                return true;
            }
            if (o instanceof android.view.DisplayCutout.Bounds) {
                android.view.DisplayCutout.Bounds b = ((android.view.DisplayCutout.Bounds) (o));
                return java.util.Arrays.deepEquals(mRects, b.mRects);
            }
            return false;
        }

        @java.lang.Override
        public java.lang.String toString() {
            return "Bounds=" + java.util.Arrays.toString(mRects);
        }
    }

    private final android.view.DisplayCutout.Bounds mBounds;

    /**
     * Creates a DisplayCutout instance.
     *
     * <p>Note that this is only useful for tests. For production code, developers should always
     * use a {@link DisplayCutout} obtained from the system.</p>
     *
     * @param safeInsets
     * 		the insets from each edge which avoid the display cutout as returned by
     * 		{@link #getSafeInsetTop()} etc.
     * @param boundLeft
     * 		the left bounding rect of the display cutout in pixels. If null is passed,
     * 		it's treated as an empty rectangle (0,0)-(0,0).
     * @param boundTop
     * 		the top bounding rect of the display cutout in pixels.  If null is passed,
     * 		it's treated as an empty rectangle (0,0)-(0,0).
     * @param boundRight
     * 		the right bounding rect of the display cutout in pixels.  If null is
     * 		passed, it's treated as an empty rectangle (0,0)-(0,0).
     * @param boundBottom
     * 		the bottom bounding rect of the display cutout in pixels.  If null is
     * 		passed, it's treated as an empty rectangle (0,0)-(0,0).
     */
    // TODO(b/73953958): @VisibleForTesting(visibility = PRIVATE)
    public DisplayCutout(@android.annotation.NonNull
    android.graphics.Insets safeInsets, @android.annotation.Nullable
    android.graphics.Rect boundLeft, @android.annotation.Nullable
    android.graphics.Rect boundTop, @android.annotation.Nullable
    android.graphics.Rect boundRight, @android.annotation.Nullable
    android.graphics.Rect boundBottom) {
        this(safeInsets.toRect(), boundLeft, boundTop, boundRight, boundBottom, true);
    }

    /**
     * Creates a DisplayCutout instance.
     *
     * <p>Note that this is only useful for tests. For production code, developers should always
     * use a {@link DisplayCutout} obtained from the system.</p>
     *
     * @param safeInsets
     * 		the insets from each edge which avoid the display cutout as returned by
     * 		{@link #getSafeInsetTop()} etc.
     * @param boundingRects
     * 		the bounding rects of the display cutouts as returned by
     * 		{@link #getBoundingRects()} ()}.
     * @deprecated Use {@link DisplayCutout#DisplayCutout(Insets, Rect, Rect, Rect, Rect)} instead.
     */
    // TODO(b/73953958): @VisibleForTesting(visibility = PRIVATE)
    @java.lang.Deprecated
    public DisplayCutout(@android.annotation.Nullable
    android.graphics.Rect safeInsets, @android.annotation.Nullable
    java.util.List<android.graphics.Rect> boundingRects) {
        /* copyArguments */
        this(safeInsets, android.view.DisplayCutout.extractBoundsFromList(safeInsets, boundingRects), true);
    }

    /**
     * Creates a DisplayCutout instance.
     *
     * @param safeInsets
     * 		the insets from each edge which avoid the display cutout as returned by
     * 		{@link #getSafeInsetTop()} etc.
     * @param copyArguments
     * 		if true, create a copy of the arguments. If false, the passed arguments
     * 		are not copied and MUST remain unchanged forever.
     */
    private DisplayCutout(android.graphics.Rect safeInsets, android.graphics.Rect boundLeft, android.graphics.Rect boundTop, android.graphics.Rect boundRight, android.graphics.Rect boundBottom, boolean copyArguments) {
        mSafeInsets = android.view.DisplayCutout.getCopyOrRef(safeInsets, copyArguments);
        mBounds = new android.view.DisplayCutout.Bounds(boundLeft, boundTop, boundRight, boundBottom, copyArguments);
    }

    private DisplayCutout(android.graphics.Rect safeInsets, android.graphics.Rect[] bounds, boolean copyArguments) {
        mSafeInsets = android.view.DisplayCutout.getCopyOrRef(safeInsets, copyArguments);
        mBounds = new android.view.DisplayCutout.Bounds(bounds, copyArguments);
    }

    private DisplayCutout(android.graphics.Rect safeInsets, android.view.DisplayCutout.Bounds bounds) {
        mSafeInsets = safeInsets;
        mBounds = bounds;
    }

    private static android.graphics.Rect getCopyOrRef(android.graphics.Rect r, boolean copyArguments) {
        if (r == null) {
            return android.view.DisplayCutout.ZERO_RECT;
        } else
            if (copyArguments) {
                return new android.graphics.Rect(r);
            } else {
                return r;
            }

    }

    /**
     * Find the position of the bounding rect, and create an array of Rect whose index represents
     * the position (= BoundsPosition).
     *
     * @unknown 
     */
    public static android.graphics.Rect[] extractBoundsFromList(android.graphics.Rect safeInsets, java.util.List<android.graphics.Rect> boundingRects) {
        android.graphics.Rect[] sortedBounds = new android.graphics.Rect[android.view.DisplayCutout.BOUNDS_POSITION_LENGTH];
        for (int i = 0; i < sortedBounds.length; ++i) {
            sortedBounds[i] = android.view.DisplayCutout.ZERO_RECT;
        }
        if ((safeInsets != null) && (boundingRects != null)) {
            for (android.graphics.Rect bound : boundingRects) {
                // There is at most one non-functional area per short edge of the device, but none
                // on the long edges, so either safeInsets.right or safeInsets.bottom must be 0.
                // TODO(b/117199965): Refine the logic to handle edge cases.
                if (bound.left == 0) {
                    sortedBounds[android.view.DisplayCutout.BOUNDS_POSITION_LEFT] = bound;
                } else
                    if (bound.top == 0) {
                        sortedBounds[android.view.DisplayCutout.BOUNDS_POSITION_TOP] = bound;
                    } else
                        if (safeInsets.right > 0) {
                            sortedBounds[android.view.DisplayCutout.BOUNDS_POSITION_RIGHT] = bound;
                        } else
                            if (safeInsets.bottom > 0) {
                                sortedBounds[android.view.DisplayCutout.BOUNDS_POSITION_BOTTOM] = bound;
                            }



            }
        }
        return sortedBounds;
    }

    /**
     * Returns true if there is no cutout, i.e. the bounds are empty.
     *
     * @unknown 
     */
    public boolean isBoundsEmpty() {
        return mBounds.isEmpty();
    }

    /**
     * Returns true if the safe insets are empty (and therefore the current view does not
     * overlap with the cutout or cutout area).
     *
     * @unknown 
     */
    public boolean isEmpty() {
        return mSafeInsets.equals(android.view.DisplayCutout.ZERO_RECT);
    }

    /**
     * Returns the inset from the top which avoids the display cutout in pixels.
     */
    public int getSafeInsetTop() {
        return mSafeInsets.top;
    }

    /**
     * Returns the inset from the bottom which avoids the display cutout in pixels.
     */
    public int getSafeInsetBottom() {
        return mSafeInsets.bottom;
    }

    /**
     * Returns the inset from the left which avoids the display cutout in pixels.
     */
    public int getSafeInsetLeft() {
        return mSafeInsets.left;
    }

    /**
     * Returns the inset from the right which avoids the display cutout in pixels.
     */
    public int getSafeInsetRight() {
        return mSafeInsets.right;
    }

    /**
     * Returns the safe insets in a rect in pixel units.
     *
     * @return a rect which is set to the safe insets.
     * @unknown 
     */
    public android.graphics.Rect getSafeInsets() {
        return new android.graphics.Rect(mSafeInsets);
    }

    /**
     * Returns a list of {@code Rect}s, each of which is the bounding rectangle for a non-functional
     * area on the display.
     *
     * There will be at most one non-functional area per short edge of the device, and none on
     * the long edges.
     *
     * @return a list of bounding {@code Rect}s, one for each display cutout area. No empty Rect is
    returned.
     */
    @android.annotation.NonNull
    public java.util.List<android.graphics.Rect> getBoundingRects() {
        java.util.List<android.graphics.Rect> result = new java.util.ArrayList<>();
        for (android.graphics.Rect bound : getBoundingRectsAll()) {
            if (!bound.isEmpty()) {
                result.add(new android.graphics.Rect(bound));
            }
        }
        return result;
    }

    /**
     * Returns an array of {@code Rect}s, each of which is the bounding rectangle for a non-
     * functional area on the display. Ordinal value of BoundPosition is used as an index of
     * the array.
     *
     * There will be at most one non-functional area per short edge of the device, and none on
     * the long edges.
     *
     * @return an array of bounding {@code Rect}s, one for each display cutout area. This might
    contain ZERO_RECT, which means there is no cutout area at the position.
     * @unknown 
     */
    public android.graphics.Rect[] getBoundingRectsAll() {
        return mBounds.getRects();
    }

    /**
     * Returns a bounding rectangle for a non-functional area on the display which is located on
     * the left of the screen.
     *
     * @return bounding rectangle in pixels. In case of no bounding rectangle, an empty rectangle
    is returned.
     */
    @android.annotation.NonNull
    public android.graphics.Rect getBoundingRectLeft() {
        return mBounds.getRect(android.view.DisplayCutout.BOUNDS_POSITION_LEFT);
    }

    /**
     * Returns a bounding rectangle for a non-functional area on the display which is located on
     * the top of the screen.
     *
     * @return bounding rectangle in pixels. In case of no bounding rectangle, an empty rectangle
    is returned.
     */
    @android.annotation.NonNull
    public android.graphics.Rect getBoundingRectTop() {
        return mBounds.getRect(android.view.DisplayCutout.BOUNDS_POSITION_TOP);
    }

    /**
     * Returns a bounding rectangle for a non-functional area on the display which is located on
     * the right of the screen.
     *
     * @return bounding rectangle in pixels. In case of no bounding rectangle, an empty rectangle
    is returned.
     */
    @android.annotation.NonNull
    public android.graphics.Rect getBoundingRectRight() {
        return mBounds.getRect(android.view.DisplayCutout.BOUNDS_POSITION_RIGHT);
    }

    /**
     * Returns a bounding rectangle for a non-functional area on the display which is located on
     * the bottom of the screen.
     *
     * @return bounding rectangle in pixels. In case of no bounding rectangle, an empty rectangle
    is returned.
     */
    @android.annotation.NonNull
    public android.graphics.Rect getBoundingRectBottom() {
        return mBounds.getRect(android.view.DisplayCutout.BOUNDS_POSITION_BOTTOM);
    }

    @java.lang.Override
    public int hashCode() {
        return (mSafeInsets.hashCode() * 48271) + mBounds.hashCode();
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof android.view.DisplayCutout) {
            android.view.DisplayCutout c = ((android.view.DisplayCutout) (o));
            return mSafeInsets.equals(c.mSafeInsets) && mBounds.equals(c.mBounds);
        }
        return false;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return (((("DisplayCutout{insets=" + mSafeInsets) + " boundingRect={") + mBounds) + "}") + "}";
    }

    /**
     *
     *
     * @unknown 
     */
    public void writeToProto(android.util.proto.ProtoOutputStream proto, long fieldId) {
        final long token = proto.start(fieldId);
        mSafeInsets.writeToProto(proto, android.view.DisplayCutoutProto.INSETS);
        mBounds.getRect(android.view.DisplayCutout.BOUNDS_POSITION_LEFT).writeToProto(proto, android.view.DisplayCutoutProto.BOUND_LEFT);
        mBounds.getRect(android.view.DisplayCutout.BOUNDS_POSITION_TOP).writeToProto(proto, android.view.DisplayCutoutProto.BOUND_TOP);
        mBounds.getRect(android.view.DisplayCutout.BOUNDS_POSITION_RIGHT).writeToProto(proto, android.view.DisplayCutoutProto.BOUND_RIGHT);
        mBounds.getRect(android.view.DisplayCutout.BOUNDS_POSITION_BOTTOM).writeToProto(proto, android.view.DisplayCutoutProto.BOUND_BOTTOM);
        proto.end(token);
    }

    /**
     * Insets the reference frame of the cutout in the given directions.
     *
     * @return a copy of this instance which has been inset
     * @unknown 
     */
    public android.view.DisplayCutout inset(int insetLeft, int insetTop, int insetRight, int insetBottom) {
        if (((((insetLeft == 0) && (insetTop == 0)) && (insetRight == 0)) && (insetBottom == 0)) || isBoundsEmpty()) {
            return this;
        }
        android.graphics.Rect safeInsets = new android.graphics.Rect(mSafeInsets);
        // Note: it's not really well defined what happens when the inset is negative, because we
        // don't know if the safe inset needs to expand in general.
        if ((insetTop > 0) || (safeInsets.top > 0)) {
            safeInsets.top = android.view.DisplayCutout.atLeastZero(safeInsets.top - insetTop);
        }
        if ((insetBottom > 0) || (safeInsets.bottom > 0)) {
            safeInsets.bottom = android.view.DisplayCutout.atLeastZero(safeInsets.bottom - insetBottom);
        }
        if ((insetLeft > 0) || (safeInsets.left > 0)) {
            safeInsets.left = android.view.DisplayCutout.atLeastZero(safeInsets.left - insetLeft);
        }
        if ((insetRight > 0) || (safeInsets.right > 0)) {
            safeInsets.right = android.view.DisplayCutout.atLeastZero(safeInsets.right - insetRight);
        }
        // If we are not cutting off part of the cutout by insetting it on bottom/right, and we also
        // don't move it around, we can avoid the allocation and copy of the instance.
        if (((insetLeft == 0) && (insetTop == 0)) && mSafeInsets.equals(safeInsets)) {
            return this;
        }
        android.graphics.Rect[] bounds = mBounds.getRects();
        for (int i = 0; i < bounds.length; ++i) {
            if (!bounds[i].equals(android.view.DisplayCutout.ZERO_RECT)) {
                bounds[i].offset(-insetLeft, -insetTop);
            }
        }
        return /* copyArguments */
        new android.view.DisplayCutout(safeInsets, bounds, false);
    }

    /**
     * Returns a copy of this instance with the safe insets replaced with the parameter.
     *
     * @param safeInsets
     * 		the new safe insets in pixels
     * @return a copy of this instance with the safe insets replaced with the argument.
     * @unknown 
     */
    public android.view.DisplayCutout replaceSafeInsets(android.graphics.Rect safeInsets) {
        return new android.view.DisplayCutout(new android.graphics.Rect(safeInsets), mBounds);
    }

    private static int atLeastZero(int value) {
        return value < 0 ? 0 : value;
    }

    /**
     * Creates an instance from a bounding rect.
     *
     * @unknown 
     */
    @com.android.internal.annotations.VisibleForTesting
    public static android.view.DisplayCutout fromBoundingRect(int left, int top, int right, int bottom, @android.view.DisplayCutout.BoundsPosition
    int pos) {
        android.graphics.Rect[] bounds = new android.graphics.Rect[android.view.DisplayCutout.BOUNDS_POSITION_LENGTH];
        for (int i = 0; i < android.view.DisplayCutout.BOUNDS_POSITION_LENGTH; ++i) {
            bounds[i] = (pos == i) ? new android.graphics.Rect(left, top, right, bottom) : new android.graphics.Rect();
        }
        return /* copyArguments */
        new android.view.DisplayCutout(android.view.DisplayCutout.ZERO_RECT, bounds, false);
    }

    /**
     * Creates an instance from a bounding {@link Path}.
     *
     * @unknown 
     */
    public static android.view.DisplayCutout fromBounds(android.graphics.Rect[] bounds) {
        return /* copyArguments */
        new android.view.DisplayCutout(android.view.DisplayCutout.ZERO_RECT, bounds, false);
    }

    /**
     * Creates the display cutout according to
     *
     * @unknown which is the closest
    rectangle-base approximation of the cutout.
     * @unknown 
     */
    public static android.view.DisplayCutout fromResourcesRectApproximation(android.content.res.Resources res, int displayWidth, int displayHeight) {
        return android.view.DisplayCutout.fromSpec(res.getString(R.string.config_mainBuiltInDisplayCutoutRectApproximation), displayWidth, displayHeight, android.util.DisplayMetrics.DENSITY_DEVICE_STABLE / ((float) (android.util.DisplayMetrics.DENSITY_DEFAULT)));
    }

    /**
     * Creates an instance according to @android:string/config_mainBuiltInDisplayCutout.
     *
     * @unknown 
     */
    public static android.graphics.Path pathFromResources(android.content.res.Resources res, int displayWidth, int displayHeight) {
        return android.view.DisplayCutout.pathAndDisplayCutoutFromSpec(res.getString(R.string.config_mainBuiltInDisplayCutout), displayWidth, displayHeight, android.util.DisplayMetrics.DENSITY_DEVICE_STABLE / ((float) (android.util.DisplayMetrics.DENSITY_DEFAULT))).first;
    }

    /**
     * Creates an instance according to the supplied {@link android.util.PathParser.PathData} spec.
     *
     * @unknown 
     */
    @com.android.internal.annotations.VisibleForTesting(visibility = PRIVATE)
    public static android.view.DisplayCutout fromSpec(java.lang.String spec, int displayWidth, int displayHeight, float density) {
        return android.view.DisplayCutout.pathAndDisplayCutoutFromSpec(spec, displayWidth, displayHeight, density).second;
    }

    private static android.util.Pair<android.graphics.Path, android.view.DisplayCutout> pathAndDisplayCutoutFromSpec(java.lang.String spec, int displayWidth, int displayHeight, float density) {
        if (android.text.TextUtils.isEmpty(spec)) {
            return android.view.DisplayCutout.NULL_PAIR;
        }
        synchronized(android.view.DisplayCutout.CACHE_LOCK) {
            if (((spec.equals(android.view.DisplayCutout.sCachedSpec) && (android.view.DisplayCutout.sCachedDisplayWidth == displayWidth)) && (android.view.DisplayCutout.sCachedDisplayHeight == displayHeight)) && (android.view.DisplayCutout.sCachedDensity == density)) {
                return android.view.DisplayCutout.sCachedCutout;
            }
        }
        spec = spec.trim();
        final float offsetX;
        if (spec.endsWith(android.view.DisplayCutout.RIGHT_MARKER)) {
            offsetX = displayWidth;
            spec = spec.substring(0, spec.length() - android.view.DisplayCutout.RIGHT_MARKER.length()).trim();
        } else {
            offsetX = displayWidth / 2.0F;
        }
        final boolean inDp = spec.endsWith(android.view.DisplayCutout.DP_MARKER);
        if (inDp) {
            spec = spec.substring(0, spec.length() - android.view.DisplayCutout.DP_MARKER.length());
        }
        java.lang.String bottomSpec = null;
        if (spec.contains(android.view.DisplayCutout.BOTTOM_MARKER)) {
            java.lang.String[] splits = spec.split(android.view.DisplayCutout.BOTTOM_MARKER, 2);
            spec = splits[0].trim();
            bottomSpec = splits[1].trim();
        }
        final android.graphics.Path p;
        final android.graphics.Region r = android.graphics.Region.obtain();
        try {
            p = android.util.PathParser.createPathFromPathData(spec);
        } catch (java.lang.Throwable e) {
            android.util.Log.wtf(android.view.DisplayCutout.TAG, "Could not inflate cutout: ", e);
            return android.view.DisplayCutout.NULL_PAIR;
        }
        final android.graphics.Matrix m = new android.graphics.Matrix();
        if (inDp) {
            m.postScale(density, density);
        }
        m.postTranslate(offsetX, 0);
        p.transform(m);
        android.graphics.Rect boundTop = new android.graphics.Rect();
        android.view.DisplayCutout.toRectAndAddToRegion(p, r, boundTop);
        final int topInset = boundTop.bottom;
        android.graphics.Rect boundBottom = null;
        final int bottomInset;
        if (bottomSpec != null) {
            final android.graphics.Path bottomPath;
            try {
                bottomPath = android.util.PathParser.createPathFromPathData(bottomSpec);
            } catch (java.lang.Throwable e) {
                android.util.Log.wtf(android.view.DisplayCutout.TAG, "Could not inflate bottom cutout: ", e);
                return android.view.DisplayCutout.NULL_PAIR;
            }
            // Keep top transform
            m.postTranslate(0, displayHeight);
            bottomPath.transform(m);
            p.addPath(bottomPath);
            boundBottom = new android.graphics.Rect();
            android.view.DisplayCutout.toRectAndAddToRegion(bottomPath, r, boundBottom);
            bottomInset = displayHeight - boundBottom.top;
        } else {
            bottomInset = 0;
        }
        android.graphics.Rect safeInset = new android.graphics.Rect(0, topInset, 0, bottomInset);
        final android.view.DisplayCutout cutout = /* boundLeft */
        /* boundRight */
        /* copyArguments */
        new android.view.DisplayCutout(safeInset, null, boundTop, null, boundBottom, false);
        final android.util.Pair<android.graphics.Path, android.view.DisplayCutout> result = new android.util.Pair(p, cutout);
        synchronized(android.view.DisplayCutout.CACHE_LOCK) {
            android.view.DisplayCutout.sCachedSpec = spec;
            android.view.DisplayCutout.sCachedDisplayWidth = displayWidth;
            android.view.DisplayCutout.sCachedDisplayHeight = displayHeight;
            android.view.DisplayCutout.sCachedDensity = density;
            android.view.DisplayCutout.sCachedCutout = result;
        }
        return result;
    }

    private static void toRectAndAddToRegion(android.graphics.Path p, android.graphics.Region inoutRegion, android.graphics.Rect inoutRect) {
        final android.graphics.RectF rectF = new android.graphics.RectF();
        /* unused */
        p.computeBounds(rectF, false);
        rectF.round(inoutRect);
        inoutRegion.op(inoutRect, android.graphics.Region.Op.UNION);
    }

    /**
     * Helper class for passing {@link DisplayCutout} through binder.
     *
     * Needed, because {@code readFromParcel} cannot be used with immutable classes.
     *
     * @unknown 
     */
    public static final class ParcelableWrapper implements android.os.Parcelable {
        private android.view.DisplayCutout mInner;

        public ParcelableWrapper() {
            this(android.view.DisplayCutout.NO_CUTOUT);
        }

        public ParcelableWrapper(android.view.DisplayCutout cutout) {
            mInner = cutout;
        }

        @java.lang.Override
        public int describeContents() {
            return 0;
        }

        @java.lang.Override
        public void writeToParcel(android.os.Parcel out, int flags) {
            android.view.DisplayCutout.ParcelableWrapper.writeCutoutToParcel(mInner, out, flags);
        }

        /**
         * Writes a DisplayCutout to a {@link Parcel}.
         *
         * @see #readCutoutFromParcel(Parcel)
         */
        public static void writeCutoutToParcel(android.view.DisplayCutout cutout, android.os.Parcel out, int flags) {
            if (cutout == null) {
                out.writeInt(-1);
            } else
                if (cutout == android.view.DisplayCutout.NO_CUTOUT) {
                    out.writeInt(0);
                } else {
                    out.writeInt(1);
                    out.writeTypedObject(cutout.mSafeInsets, flags);
                    out.writeTypedArray(cutout.mBounds.getRects(), flags);
                }

        }

        /**
         * Similar to {@link Creator#createFromParcel(Parcel)}, but reads into an existing
         * instance.
         *
         * Needed for AIDL out parameters.
         */
        public void readFromParcel(android.os.Parcel in) {
            mInner = android.view.DisplayCutout.ParcelableWrapper.readCutoutFromParcel(in);
        }

        @android.annotation.NonNull
        public static final android.view.Creator<android.view.DisplayCutout.ParcelableWrapper> CREATOR = new android.view.Creator<android.view.DisplayCutout.ParcelableWrapper>() {
            @java.lang.Override
            public android.view.DisplayCutout.ParcelableWrapper createFromParcel(android.os.Parcel in) {
                return new android.view.DisplayCutout.ParcelableWrapper(android.view.DisplayCutout.ParcelableWrapper.readCutoutFromParcel(in));
            }

            @java.lang.Override
            public android.view.DisplayCutout.ParcelableWrapper[] newArray(int size) {
                return new android.view.DisplayCutout.ParcelableWrapper[size];
            }
        };

        /**
         * Reads a DisplayCutout from a {@link Parcel}.
         *
         * @see #writeCutoutToParcel(DisplayCutout, Parcel, int)
         */
        public static android.view.DisplayCutout readCutoutFromParcel(android.os.Parcel in) {
            int variant = in.readInt();
            if (variant == (-1)) {
                return null;
            }
            if (variant == 0) {
                return android.view.DisplayCutout.NO_CUTOUT;
            }
            android.graphics.Rect safeInsets = in.readTypedObject(android.graphics.Rect.this.CREATOR);
            android.graphics.Rect[] bounds = new android.graphics.Rect[android.view.DisplayCutout.BOUNDS_POSITION_LENGTH];
            in.readTypedArray(bounds, android.graphics.Rect.this.CREATOR);
            return /* copyArguments */
            new android.view.DisplayCutout(safeInsets, bounds, false);
        }

        public android.view.DisplayCutout get() {
            return mInner;
        }

        public void set(android.view.DisplayCutout.ParcelableWrapper cutout) {
            mInner = cutout.get();
        }

        public void set(android.view.DisplayCutout cutout) {
            mInner = cutout;
        }

        @java.lang.Override
        public int hashCode() {
            return mInner.hashCode();
        }

        @java.lang.Override
        public boolean equals(java.lang.Object o) {
            return (o instanceof android.view.DisplayCutout.ParcelableWrapper) && mInner.equals(((android.view.DisplayCutout.ParcelableWrapper) (o)).mInner);
        }

        @java.lang.Override
        public java.lang.String toString() {
            return java.lang.String.valueOf(mInner);
        }
    }
}

