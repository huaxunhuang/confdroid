/**
 * Copyright (C) 2015 The Android Open Source Project
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
package android.support.v7.widget;


class AppCompatProgressBarHelper {
    private static final int[] TINT_ATTRS = new int[]{ android.support.v7.widget.android.R.attr, android.R.attr.progressDrawable };

    private final android.widget.ProgressBar mView;

    private android.graphics.Bitmap mSampleTile;

    AppCompatProgressBarHelper(android.widget.ProgressBar view) {
        mView = view;
    }

    void loadFromAttributes(android.util.AttributeSet attrs, int defStyleAttr) {
        android.support.v7.widget.TintTypedArray a = android.support.v7.widget.TintTypedArray.obtainStyledAttributes(mView.getContext(), attrs, android.support.v7.widget.AppCompatProgressBarHelper.TINT_ATTRS, defStyleAttr, 0);
        android.graphics.drawable.Drawable drawable = a.getDrawableIfKnown(0);
        if (drawable != null) {
            mView.setIndeterminateDrawable(tileifyIndeterminate(drawable));
        }
        drawable = a.getDrawableIfKnown(1);
        if (drawable != null) {
            mView.setProgressDrawable(tileify(drawable, false));
        }
        a.recycle();
    }

    /**
     * Converts a drawable to a tiled version of itself. It will recursively
     * traverse layer and state list drawables.
     */
    private android.graphics.drawable.Drawable tileify(android.graphics.drawable.Drawable drawable, boolean clip) {
        if (drawable instanceof android.support.v4.graphics.drawable.DrawableWrapper) {
            android.graphics.drawable.Drawable inner = ((android.support.v4.graphics.drawable.DrawableWrapper) (drawable)).getWrappedDrawable();
            if (inner != null) {
                inner = tileify(inner, clip);
                ((android.support.v4.graphics.drawable.DrawableWrapper) (drawable)).setWrappedDrawable(inner);
            }
        } else
            if (drawable instanceof android.graphics.drawable.LayerDrawable) {
                android.graphics.drawable.LayerDrawable background = ((android.graphics.drawable.LayerDrawable) (drawable));
                final int N = background.getNumberOfLayers();
                android.graphics.drawable.Drawable[] outDrawables = new android.graphics.drawable.Drawable[N];
                for (int i = 0; i < N; i++) {
                    int id = background.getId(i);
                    outDrawables[i] = tileify(background.getDrawable(i), (id == android.R.id.progress) || (id == android.R.id.secondaryProgress));
                }
                android.graphics.drawable.LayerDrawable newBg = new android.graphics.drawable.LayerDrawable(outDrawables);
                for (int i = 0; i < N; i++) {
                    newBg.setId(i, background.getId(i));
                }
                return newBg;
            } else
                if (drawable instanceof android.graphics.drawable.BitmapDrawable) {
                    final android.graphics.drawable.BitmapDrawable bitmapDrawable = ((android.graphics.drawable.BitmapDrawable) (drawable));
                    final android.graphics.Bitmap tileBitmap = bitmapDrawable.getBitmap();
                    if (mSampleTile == null) {
                        mSampleTile = tileBitmap;
                    }
                    final android.graphics.drawable.ShapeDrawable shapeDrawable = new android.graphics.drawable.ShapeDrawable(getDrawableShape());
                    final android.graphics.BitmapShader bitmapShader = new android.graphics.BitmapShader(tileBitmap, android.graphics.Shader.TileMode.REPEAT, android.graphics.Shader.TileMode.CLAMP);
                    shapeDrawable.getPaint().setShader(bitmapShader);
                    shapeDrawable.getPaint().setColorFilter(bitmapDrawable.getPaint().getColorFilter());
                    return clip ? new android.graphics.drawable.ClipDrawable(shapeDrawable, android.view.Gravity.LEFT, android.graphics.drawable.ClipDrawable.HORIZONTAL) : shapeDrawable;
                }


        return drawable;
    }

    /**
     * Convert a AnimationDrawable for use as a barberpole animation.
     * Each frame of the animation is wrapped in a ClipDrawable and
     * given a tiling BitmapShader.
     */
    private android.graphics.drawable.Drawable tileifyIndeterminate(android.graphics.drawable.Drawable drawable) {
        if (drawable instanceof android.graphics.drawable.AnimationDrawable) {
            android.graphics.drawable.AnimationDrawable background = ((android.graphics.drawable.AnimationDrawable) (drawable));
            final int N = background.getNumberOfFrames();
            android.graphics.drawable.AnimationDrawable newBg = new android.graphics.drawable.AnimationDrawable();
            newBg.setOneShot(background.isOneShot());
            for (int i = 0; i < N; i++) {
                android.graphics.drawable.Drawable frame = tileify(background.getFrame(i), true);
                frame.setLevel(10000);
                newBg.addFrame(frame, background.getDuration(i));
            }
            newBg.setLevel(10000);
            drawable = newBg;
        }
        return drawable;
    }

    private android.graphics.drawable.shapes.Shape getDrawableShape() {
        final float[] roundedCorners = new float[]{ 5, 5, 5, 5, 5, 5, 5, 5 };
        return new android.graphics.drawable.shapes.RoundRectShape(roundedCorners, null, null);
    }

    android.graphics.Bitmap getSampleTime() {
        return mSampleTile;
    }
}

