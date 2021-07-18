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


/**
 *
 *
 * @unknown 
 */
@android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
public class AppCompatImageHelper {
    private final android.widget.ImageView mView;

    public AppCompatImageHelper(android.widget.ImageView view) {
        mView = view;
    }

    public void loadFromAttributes(android.util.AttributeSet attrs, int defStyleAttr) {
        android.support.v7.widget.TintTypedArray a = null;
        try {
            android.graphics.drawable.Drawable drawable = mView.getDrawable();
            if (drawable == null) {
                a = android.support.v7.widget.TintTypedArray.obtainStyledAttributes(mView.getContext(), attrs, R.styleable.AppCompatImageView, defStyleAttr, 0);
                // If the view doesn't already have a drawable (from android:src), try loading
                // it from srcCompat
                final int id = a.getResourceId(R.styleable.AppCompatImageView_srcCompat, -1);
                if (id != (-1)) {
                    drawable = android.support.v7.content.res.AppCompatResources.getDrawable(mView.getContext(), id);
                    if (drawable != null) {
                        mView.setImageDrawable(drawable);
                    }
                }
            }
            if (drawable != null) {
                android.support.v7.widget.DrawableUtils.fixDrawable(drawable);
            }
        } finally {
            if (a != null) {
                a.recycle();
            }
        }
    }

    public void setImageResource(int resId) {
        if (resId != 0) {
            final android.graphics.drawable.Drawable d = android.support.v7.content.res.AppCompatResources.getDrawable(mView.getContext(), resId);
            if (d != null) {
                android.support.v7.widget.DrawableUtils.fixDrawable(d);
            }
            mView.setImageDrawable(d);
        } else {
            mView.setImageDrawable(null);
        }
    }

    boolean hasOverlappingRendering() {
        final android.graphics.drawable.Drawable background = mView.getBackground();
        if ((android.os.Build.VERSION.SDK_INT >= 21) && (background instanceof android.graphics.drawable.RippleDrawable)) {
            // RippleDrawable has an issue on L+ when used with an alpha animation.
            // This workaround should be disabled when the platform bug is fixed. See b/27715789
            return false;
        }
        return true;
    }
}

