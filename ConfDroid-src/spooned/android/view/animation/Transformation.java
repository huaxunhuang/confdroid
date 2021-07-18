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
package android.view.animation;


/**
 * Defines the transformation to be applied at
 * one point in time of an Animation.
 */
public class Transformation {
    /**
     * Indicates a transformation that has no effect (alpha = 1 and identity matrix.)
     */
    public static final int TYPE_IDENTITY = 0x0;

    /**
     * Indicates a transformation that applies an alpha only (uses an identity matrix.)
     */
    public static final int TYPE_ALPHA = 0x1;

    /**
     * Indicates a transformation that applies a matrix only (alpha = 1.)
     */
    public static final int TYPE_MATRIX = 0x2;

    /**
     * Indicates a transformation that applies an alpha and a matrix.
     */
    public static final int TYPE_BOTH = android.view.animation.Transformation.TYPE_ALPHA | android.view.animation.Transformation.TYPE_MATRIX;

    protected android.graphics.Matrix mMatrix;

    protected float mAlpha;

    protected int mTransformationType;

    private boolean mHasClipRect;

    private android.graphics.Rect mClipRect = new android.graphics.Rect();

    /**
     * Creates a new transformation with alpha = 1 and the identity matrix.
     */
    public Transformation() {
        clear();
    }

    /**
     * Reset the transformation to a state that leaves the object
     * being animated in an unmodified state. The transformation type is
     * {@link #TYPE_BOTH} by default.
     */
    public void clear() {
        if (mMatrix == null) {
            mMatrix = new android.graphics.Matrix();
        } else {
            mMatrix.reset();
        }
        mClipRect.setEmpty();
        mHasClipRect = false;
        mAlpha = 1.0F;
        mTransformationType = android.view.animation.Transformation.TYPE_BOTH;
    }

    /**
     * Indicates the nature of this transformation.
     *
     * @return {@link #TYPE_ALPHA}, {@link #TYPE_MATRIX},
    {@link #TYPE_BOTH} or {@link #TYPE_IDENTITY}.
     */
    public int getTransformationType() {
        return mTransformationType;
    }

    /**
     * Sets the transformation type.
     *
     * @param transformationType
     * 		One of {@link #TYPE_ALPHA},
     * 		{@link #TYPE_MATRIX}, {@link #TYPE_BOTH} or
     * 		{@link #TYPE_IDENTITY}.
     */
    public void setTransformationType(int transformationType) {
        mTransformationType = transformationType;
    }

    /**
     * Clones the specified transformation.
     *
     * @param t
     * 		The transformation to clone.
     */
    public void set(android.view.animation.Transformation t) {
        mAlpha = t.getAlpha();
        mMatrix.set(t.getMatrix());
        if (t.mHasClipRect) {
            setClipRect(t.getClipRect());
        } else {
            mHasClipRect = false;
            mClipRect.setEmpty();
        }
        mTransformationType = t.getTransformationType();
    }

    /**
     * Apply this Transformation to an existing Transformation, e.g. apply
     * a scale effect to something that has already been rotated.
     *
     * @param t
     * 		
     */
    public void compose(android.view.animation.Transformation t) {
        mAlpha *= t.getAlpha();
        mMatrix.preConcat(t.getMatrix());
        if (t.mHasClipRect) {
            android.graphics.Rect bounds = t.getClipRect();
            if (mHasClipRect) {
                setClipRect(mClipRect.left + bounds.left, mClipRect.top + bounds.top, mClipRect.right + bounds.right, mClipRect.bottom + bounds.bottom);
            } else {
                setClipRect(bounds);
            }
        }
    }

    /**
     * Like {@link #compose(Transformation)} but does this.postConcat(t) of
     * the transformation matrix.
     *
     * @unknown 
     */
    public void postCompose(android.view.animation.Transformation t) {
        mAlpha *= t.getAlpha();
        mMatrix.postConcat(t.getMatrix());
        if (t.mHasClipRect) {
            android.graphics.Rect bounds = t.getClipRect();
            if (mHasClipRect) {
                setClipRect(mClipRect.left + bounds.left, mClipRect.top + bounds.top, mClipRect.right + bounds.right, mClipRect.bottom + bounds.bottom);
            } else {
                setClipRect(bounds);
            }
        }
    }

    /**
     *
     *
     * @return The 3x3 Matrix representing the trnasformation to apply to the
    coordinates of the object being animated
     */
    public android.graphics.Matrix getMatrix() {
        return mMatrix;
    }

    /**
     * Sets the degree of transparency
     *
     * @param alpha
     * 		1.0 means fully opaqe and 0.0 means fully transparent
     */
    public void setAlpha(@android.annotation.FloatRange(from = 0.0, to = 1.0)
    float alpha) {
        mAlpha = alpha;
    }

    /**
     * Sets the current Transform's clip rect
     *
     * @unknown 
     */
    public void setClipRect(android.graphics.Rect r) {
        setClipRect(r.left, r.top, r.right, r.bottom);
    }

    /**
     * Sets the current Transform's clip rect
     *
     * @unknown 
     */
    public void setClipRect(int l, int t, int r, int b) {
        mClipRect.set(l, t, r, b);
        mHasClipRect = true;
    }

    /**
     * Returns the current Transform's clip rect
     *
     * @unknown 
     */
    public android.graphics.Rect getClipRect() {
        return mClipRect;
    }

    /**
     * Returns whether the current Transform's clip rect is set
     *
     * @unknown 
     */
    public boolean hasClipRect() {
        return mHasClipRect;
    }

    /**
     *
     *
     * @return The degree of transparency
     */
    public float getAlpha() {
        return mAlpha;
    }

    @java.lang.Override
    public java.lang.String toString() {
        java.lang.StringBuilder sb = new java.lang.StringBuilder(64);
        sb.append("Transformation");
        toShortString(sb);
        return sb.toString();
    }

    /**
     * Return a string representation of the transformation in a compact form.
     */
    public java.lang.String toShortString() {
        java.lang.StringBuilder sb = new java.lang.StringBuilder(64);
        toShortString(sb);
        return sb.toString();
    }

    /**
     *
     *
     * @unknown 
     */
    public void toShortString(java.lang.StringBuilder sb) {
        sb.append("{alpha=");
        sb.append(mAlpha);
        sb.append(" matrix=");
        mMatrix.toShortString(sb);
        sb.append('}');
    }

    /**
     * Print short string, to optimize dumping.
     *
     * @unknown 
     */
    @android.annotation.UnsupportedAppUsage
    public void printShortString(java.io.PrintWriter pw) {
        pw.print("{alpha=");
        pw.print(mAlpha);
        pw.print(" matrix=");
        mMatrix.printShortString(pw);
        pw.print('}');
    }
}

