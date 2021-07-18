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


class AppCompatTextHelper {
    static android.support.v7.widget.AppCompatTextHelper create(android.widget.TextView textView) {
        if (android.os.Build.VERSION.SDK_INT >= 17) {
            return new android.support.v7.widget.AppCompatTextHelperV17(textView);
        }
        return new android.support.v7.widget.AppCompatTextHelper(textView);
    }

    final android.widget.TextView mView;

    private android.support.v7.widget.TintInfo mDrawableLeftTint;

    private android.support.v7.widget.TintInfo mDrawableTopTint;

    private android.support.v7.widget.TintInfo mDrawableRightTint;

    private android.support.v7.widget.TintInfo mDrawableBottomTint;

    AppCompatTextHelper(android.widget.TextView view) {
        mView = view;
    }

    void loadFromAttributes(android.util.AttributeSet attrs, int defStyleAttr) {
        final android.content.Context context = mView.getContext();
        final android.support.v7.widget.AppCompatDrawableManager drawableManager = android.support.v7.widget.AppCompatDrawableManager.get();
        // First read the TextAppearance style id
        android.support.v7.widget.TintTypedArray a = android.support.v7.widget.TintTypedArray.obtainStyledAttributes(context, attrs, R.styleable.AppCompatTextHelper, defStyleAttr, 0);
        final int ap = a.getResourceId(R.styleable.AppCompatTextHelper_android_textAppearance, -1);
        // Now read the compound drawable and grab any tints
        if (a.hasValue(R.styleable.AppCompatTextHelper_android_drawableLeft)) {
            mDrawableLeftTint = android.support.v7.widget.AppCompatTextHelper.createTintInfo(context, drawableManager, a.getResourceId(R.styleable.AppCompatTextHelper_android_drawableLeft, 0));
        }
        if (a.hasValue(R.styleable.AppCompatTextHelper_android_drawableTop)) {
            mDrawableTopTint = android.support.v7.widget.AppCompatTextHelper.createTintInfo(context, drawableManager, a.getResourceId(R.styleable.AppCompatTextHelper_android_drawableTop, 0));
        }
        if (a.hasValue(R.styleable.AppCompatTextHelper_android_drawableRight)) {
            mDrawableRightTint = android.support.v7.widget.AppCompatTextHelper.createTintInfo(context, drawableManager, a.getResourceId(R.styleable.AppCompatTextHelper_android_drawableRight, 0));
        }
        if (a.hasValue(R.styleable.AppCompatTextHelper_android_drawableBottom)) {
            mDrawableBottomTint = android.support.v7.widget.AppCompatTextHelper.createTintInfo(context, drawableManager, a.getResourceId(R.styleable.AppCompatTextHelper_android_drawableBottom, 0));
        }
        a.recycle();
        // PasswordTransformationMethod wipes out all other TransformationMethod instances
        // in TextView's constructor, so we should only set a new transformation method
        // if we don't have a PasswordTransformationMethod currently...
        final boolean hasPwdTm = mView.getTransformationMethod() instanceof android.text.method.PasswordTransformationMethod;
        boolean allCaps = false;
        boolean allCapsSet = false;
        android.content.res.ColorStateList textColor = null;
        android.content.res.ColorStateList textColorHint = null;
        // First check TextAppearance's textAllCaps value
        if (ap != (-1)) {
            a = android.support.v7.widget.TintTypedArray.obtainStyledAttributes(context, ap, R.styleable.TextAppearance);
            if ((!hasPwdTm) && a.hasValue(R.styleable.TextAppearance_textAllCaps)) {
                allCapsSet = true;
                allCaps = a.getBoolean(R.styleable.TextAppearance_textAllCaps, false);
            }
            if (android.os.Build.VERSION.SDK_INT < 23) {
                // If we're running on < API 23, the text color may contain theme references
                // so let's re-set using our own inflater
                if (a.hasValue(R.styleable.TextAppearance_android_textColor)) {
                    textColor = a.getColorStateList(R.styleable.TextAppearance_android_textColor);
                }
                if (a.hasValue(R.styleable.TextAppearance_android_textColorHint)) {
                    textColorHint = a.getColorStateList(R.styleable.TextAppearance_android_textColorHint);
                }
            }
            a.recycle();
        }
        // Now read the style's values
        a = android.support.v7.widget.TintTypedArray.obtainStyledAttributes(context, attrs, R.styleable.TextAppearance, defStyleAttr, 0);
        if ((!hasPwdTm) && a.hasValue(R.styleable.TextAppearance_textAllCaps)) {
            allCapsSet = true;
            allCaps = a.getBoolean(R.styleable.TextAppearance_textAllCaps, false);
        }
        if (android.os.Build.VERSION.SDK_INT < 23) {
            // If we're running on < API 23, the text color may contain theme references
            // so let's re-set using our own inflater
            if (a.hasValue(R.styleable.TextAppearance_android_textColor)) {
                textColor = a.getColorStateList(R.styleable.TextAppearance_android_textColor);
            }
            if (a.hasValue(R.styleable.TextAppearance_android_textColorHint)) {
                textColorHint = a.getColorStateList(R.styleable.TextAppearance_android_textColorHint);
            }
        }
        a.recycle();
        if (textColor != null) {
            mView.setTextColor(textColor);
        }
        if (textColorHint != null) {
            mView.setHintTextColor(textColorHint);
        }
        if ((!hasPwdTm) && allCapsSet) {
            setAllCaps(allCaps);
        }
    }

    void onSetTextAppearance(android.content.Context context, int resId) {
        final android.support.v7.widget.TintTypedArray a = android.support.v7.widget.TintTypedArray.obtainStyledAttributes(context, resId, R.styleable.TextAppearance);
        if (a.hasValue(R.styleable.TextAppearance_textAllCaps)) {
            // This breaks away slightly from the logic in TextView.setTextAppearance that serves
            // as an "overlay" on the current state of the TextView. Since android:textAllCaps
            // may have been set to true in this text appearance, we need to make sure that
            // app:textAllCaps has the chance to override it
            setAllCaps(a.getBoolean(R.styleable.TextAppearance_textAllCaps, false));
        }
        if ((android.os.Build.VERSION.SDK_INT < 23) && a.hasValue(R.styleable.TextAppearance_android_textColor)) {
            // If we're running on < API 23, the text color may contain theme references
            // so let's re-set using our own inflater
            final android.content.res.ColorStateList textColor = a.getColorStateList(R.styleable.TextAppearance_android_textColor);
            if (textColor != null) {
                mView.setTextColor(textColor);
            }
        }
        a.recycle();
    }

    void setAllCaps(boolean allCaps) {
        mView.setTransformationMethod(allCaps ? new android.support.v7.text.AllCapsTransformationMethod(mView.getContext()) : null);
    }

    void applyCompoundDrawablesTints() {
        if ((((mDrawableLeftTint != null) || (mDrawableTopTint != null)) || (mDrawableRightTint != null)) || (mDrawableBottomTint != null)) {
            final android.graphics.drawable.Drawable[] compoundDrawables = mView.getCompoundDrawables();
            applyCompoundDrawableTint(compoundDrawables[0], mDrawableLeftTint);
            applyCompoundDrawableTint(compoundDrawables[1], mDrawableTopTint);
            applyCompoundDrawableTint(compoundDrawables[2], mDrawableRightTint);
            applyCompoundDrawableTint(compoundDrawables[3], mDrawableBottomTint);
        }
    }

    final void applyCompoundDrawableTint(android.graphics.drawable.Drawable drawable, android.support.v7.widget.TintInfo info) {
        if ((drawable != null) && (info != null)) {
            android.support.v7.widget.AppCompatDrawableManager.tintDrawable(drawable, info, mView.getDrawableState());
        }
    }

    protected static android.support.v7.widget.TintInfo createTintInfo(android.content.Context context, android.support.v7.widget.AppCompatDrawableManager drawableManager, int drawableId) {
        final android.content.res.ColorStateList tintList = drawableManager.getTintList(context, drawableId);
        if (tintList != null) {
            final android.support.v7.widget.TintInfo tintInfo = new android.support.v7.widget.TintInfo();
            tintInfo.mHasTintList = true;
            tintInfo.mTintList = tintList;
            return tintInfo;
        }
        return null;
    }
}

