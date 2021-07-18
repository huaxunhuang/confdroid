/**
 * Copyright (C) 2014 The Android Open Source Project
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
package android.graphics;


/**
 * Defines a simple shape, used for bounding graphical regions.
 * <p>
 * Can be computed for a View, or computed by a Drawable, to drive the shape of
 * shadows cast by a View, or to clip the contents of the View.
 *
 * @see android.view.ViewOutlineProvider
 * @see android.view.View#setOutlineProvider(android.view.ViewOutlineProvider)
 * @see Drawable#getOutline(Outline)
 */
public final class Outline {
    private static final float RADIUS_UNDEFINED = java.lang.Float.NEGATIVE_INFINITY;

    /**
     *
     *
     * @unknown 
     */
    public static final int MODE_EMPTY = 0;

    /**
     *
     *
     * @unknown 
     */
    public static final int MODE_ROUND_RECT = 1;

    /**
     *
     *
     * @unknown 
     */
    public static final int MODE_CONVEX_PATH = 2;

    /**
     *
     *
     * @unknown 
     */
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    @android.annotation.IntDef(flag = false, value = { android.graphics.Outline.MODE_EMPTY, android.graphics.Outline.MODE_ROUND_RECT, android.graphics.Outline.MODE_CONVEX_PATH })
    public @interface Mode {}

    /**
     *
     *
     * @unknown 
     */
    @android.graphics.Outline.Mode
    public int mMode = android.graphics.Outline.MODE_EMPTY;

    /**
     * Only guaranteed to be non-null when mode == MODE_CONVEX_PATH
     *
     * @unknown 
     */
    public android.graphics.Path mPath;

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.UnsupportedAppUsage
    public final android.graphics.Rect mRect = new android.graphics.Rect();

    /**
     *
     *
     * @unknown 
     */
    public float mRadius = android.graphics.Outline.RADIUS_UNDEFINED;

    /**
     *
     *
     * @unknown 
     */
    public float mAlpha;

    /**
     * Constructs an empty Outline. Call one of the setter methods to make
     * the outline valid for use with a View.
     */
    public Outline() {
    }

    /**
     * Constructs an Outline with a copy of the data in src.
     */
    public Outline(@android.annotation.NonNull
    android.graphics.Outline src) {
        set(src);
    }

    /**
     * Sets the outline to be empty.
     *
     * @see #isEmpty()
     */
    public void setEmpty() {
        if (mPath != null) {
            // rewind here to avoid thrashing the allocations, but could alternately clear ref
            mPath.rewind();
        }
        mMode = android.graphics.Outline.MODE_EMPTY;
        mRect.setEmpty();
        mRadius = android.graphics.Outline.RADIUS_UNDEFINED;
    }

    /**
     * Returns whether the Outline is empty.
     * <p>
     * Outlines are empty when constructed, or if {@link #setEmpty()} is called,
     * until a setter method is called
     *
     * @see #setEmpty()
     */
    public boolean isEmpty() {
        return mMode == android.graphics.Outline.MODE_EMPTY;
    }

    /**
     * Returns whether the outline can be used to clip a View.
     * <p>
     * Currently, only Outlines that can be represented as a rectangle, circle,
     * or round rect support clipping.
     *
     * @see android.view.View#setClipToOutline(boolean)
     */
    public boolean canClip() {
        return mMode != android.graphics.Outline.MODE_CONVEX_PATH;
    }

    /**
     * Sets the alpha represented by the Outline - the degree to which the
     * producer is guaranteed to be opaque over the Outline's shape.
     * <p>
     * An alpha value of <code>0.0f</code> either represents completely
     * transparent content, or content that isn't guaranteed to fill the shape
     * it publishes.
     * <p>
     * Content producing a fully opaque (alpha = <code>1.0f</code>) outline is
     * assumed by the drawing system to fully cover content beneath it,
     * meaning content beneath may be optimized away.
     */
    public void setAlpha(@android.annotation.FloatRange(from = 0.0, to = 1.0)
    float alpha) {
        mAlpha = alpha;
    }

    /**
     * Returns the alpha represented by the Outline.
     */
    public float getAlpha() {
        return mAlpha;
    }

    /**
     * Replace the contents of this Outline with the contents of src.
     *
     * @param src
     * 		Source outline to copy from.
     */
    public void set(@android.annotation.NonNull
    android.graphics.Outline src) {
        mMode = src.mMode;
        if (src.mMode == android.graphics.Outline.MODE_CONVEX_PATH) {
            if (mPath == null) {
                mPath = new android.graphics.Path();
            }
            mPath.set(src.mPath);
        }
        mRect.set(src.mRect);
        mRadius = src.mRadius;
        mAlpha = src.mAlpha;
    }

    /**
     * Sets the Outline to the rounded rect defined by the input rect, and
     * corner radius.
     */
    public void setRect(int left, int top, int right, int bottom) {
        setRoundRect(left, top, right, bottom, 0.0F);
    }

    /**
     * Convenience for {@link #setRect(int, int, int, int)}
     */
    public void setRect(@android.annotation.NonNull
    android.graphics.Rect rect) {
        setRect(rect.left, rect.top, rect.right, rect.bottom);
    }

    /**
     * Sets the Outline to the rounded rect defined by the input rect, and corner radius.
     * <p>
     * Passing a zero radius is equivalent to calling {@link #setRect(int, int, int, int)}
     */
    public void setRoundRect(int left, int top, int right, int bottom, float radius) {
        if ((left >= right) || (top >= bottom)) {
            setEmpty();
            return;
        }
        if (mMode == android.graphics.Outline.MODE_CONVEX_PATH) {
            // rewind here to avoid thrashing the allocations, but could alternately clear ref
            mPath.rewind();
        }
        mMode = android.graphics.Outline.MODE_ROUND_RECT;
        mRect.set(left, top, right, bottom);
        mRadius = radius;
    }

    /**
     * Convenience for {@link #setRoundRect(int, int, int, int, float)}
     */
    public void setRoundRect(@android.annotation.NonNull
    android.graphics.Rect rect, float radius) {
        setRoundRect(rect.left, rect.top, rect.right, rect.bottom, radius);
    }

    /**
     * Populates {@code outBounds} with the outline bounds, if set, and returns
     * {@code true}. If no outline bounds are set, or if a path has been set
     * via {@link #setConvexPath(Path)}, returns {@code false}.
     *
     * @param outRect
     * 		the rect to populate with the outline bounds, if set
     * @return {@code true} if {@code outBounds} was populated with outline
    bounds, or {@code false} if no outline bounds are set
     */
    public boolean getRect(@android.annotation.NonNull
    android.graphics.Rect outRect) {
        if (mMode != android.graphics.Outline.MODE_ROUND_RECT) {
            return false;
        }
        outRect.set(mRect);
        return true;
    }

    /**
     * Returns the rounded rect radius, if set, or a value less than 0 if a path has
     * been set via {@link #setConvexPath(Path)}. A return value of {@code 0}
     * indicates a non-rounded rect.
     *
     * @return the rounded rect radius, or value < 0
     */
    public float getRadius() {
        return mRadius;
    }

    /**
     * Sets the outline to the oval defined by input rect.
     */
    public void setOval(int left, int top, int right, int bottom) {
        if ((left >= right) || (top >= bottom)) {
            setEmpty();
            return;
        }
        if ((bottom - top) == (right - left)) {
            // represent circle as round rect, for efficiency, and to enable clipping
            setRoundRect(left, top, right, bottom, (bottom - top) / 2.0F);
            return;
        }
        if (mPath == null) {
            mPath = new android.graphics.Path();
        } else {
            mPath.rewind();
        }
        mMode = android.graphics.Outline.MODE_CONVEX_PATH;
        mPath.addOval(left, top, right, bottom, android.graphics.Path.Direction.CW);
        mRect.setEmpty();
        mRadius = android.graphics.Outline.RADIUS_UNDEFINED;
    }

    /**
     * Convenience for {@link #setOval(int, int, int, int)}
     */
    public void setOval(@android.annotation.NonNull
    android.graphics.Rect rect) {
        setOval(rect.left, rect.top, rect.right, rect.bottom);
    }

    /**
     * Sets the Outline to a
     * {@link android.graphics.Path#isConvex() convex path}.
     *
     * @param convexPath
     * 		used to construct the Outline. As of
     * 		{@link android.os.Build.VERSION_CODES#Q}, it is no longer required to be
     * 		convex.
     */
    public void setConvexPath(@android.annotation.NonNull
    android.graphics.Path convexPath) {
        if (convexPath.isEmpty()) {
            setEmpty();
            return;
        }
        if (mPath == null) {
            mPath = new android.graphics.Path();
        }
        mMode = android.graphics.Outline.MODE_CONVEX_PATH;
        mPath.set(convexPath);
        mRect.setEmpty();
        mRadius = android.graphics.Outline.RADIUS_UNDEFINED;
    }

    /**
     * Offsets the Outline by (dx,dy)
     */
    public void offset(int dx, int dy) {
        if (mMode == android.graphics.Outline.MODE_ROUND_RECT) {
            mRect.offset(dx, dy);
        } else
            if (mMode == android.graphics.Outline.MODE_CONVEX_PATH) {
                mPath.offset(dx, dy);
            }

    }
}

