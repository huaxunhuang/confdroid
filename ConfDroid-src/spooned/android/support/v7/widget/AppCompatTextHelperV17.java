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


class AppCompatTextHelperV17 extends android.support.v7.widget.AppCompatTextHelper {
    private android.support.v7.widget.TintInfo mDrawableStartTint;

    private android.support.v7.widget.TintInfo mDrawableEndTint;

    AppCompatTextHelperV17(android.widget.TextView view) {
        super(view);
    }

    @java.lang.Override
    void loadFromAttributes(android.util.AttributeSet attrs, int defStyleAttr) {
        super.loadFromAttributes(attrs, defStyleAttr);
        final android.content.Context context = mView.getContext();
        final android.support.v7.widget.AppCompatDrawableManager drawableManager = android.support.v7.widget.AppCompatDrawableManager.get();
        android.content.res.TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AppCompatTextHelper, defStyleAttr, 0);
        if (a.hasValue(R.styleable.AppCompatTextHelper_android_drawableStart)) {
            mDrawableStartTint = android.support.v7.widget.AppCompatTextHelper.createTintInfo(context, drawableManager, a.getResourceId(R.styleable.AppCompatTextHelper_android_drawableStart, 0));
        }
        if (a.hasValue(R.styleable.AppCompatTextHelper_android_drawableEnd)) {
            mDrawableEndTint = android.support.v7.widget.AppCompatTextHelper.createTintInfo(context, drawableManager, a.getResourceId(R.styleable.AppCompatTextHelper_android_drawableEnd, 0));
        }
        a.recycle();
    }

    @java.lang.Override
    void applyCompoundDrawablesTints() {
        super.applyCompoundDrawablesTints();
        if ((mDrawableStartTint != null) || (mDrawableEndTint != null)) {
            final android.graphics.drawable.Drawable[] compoundDrawables = mView.getCompoundDrawablesRelative();
            applyCompoundDrawableTint(compoundDrawables[0], mDrawableStartTint);
            applyCompoundDrawableTint(compoundDrawables[2], mDrawableEndTint);
        }
    }
}

