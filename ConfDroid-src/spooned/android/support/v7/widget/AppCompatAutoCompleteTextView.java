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
package android.support.v7.widget;


/**
 * A {@link AutoCompleteTextView} which supports compatible features on older version of the
 * platform, including:
 * <ul>
 *     <li>Supports {@link R.attr#textAllCaps} style attribute which works back to
 *     {@link android.os.Build.VERSION_CODES#GINGERBREAD Gingerbread}.</li>
 *     <li>Allows dynamic tint of it background via the background tint methods in
 *     {@link android.support.v4.view.ViewCompat}.</li>
 *     <li>Allows setting of the background tint using {@link R.attr#backgroundTint} and
 *     {@link R.attr#backgroundTintMode}.</li>
 * </ul>
 *
 * <p>This will automatically be used when you use {@link AutoCompleteTextView} in your layouts.
 * You should only need to manually use this class when writing custom views.</p>
 */
public class AppCompatAutoCompleteTextView extends android.widget.AutoCompleteTextView implements android.support.v4.view.TintableBackgroundView {
    private static final int[] TINT_ATTRS = new int[]{ android.R.attr.popupBackground };

    private android.support.v7.widget.AppCompatBackgroundHelper mBackgroundTintHelper;

    private android.support.v7.widget.AppCompatTextHelper mTextHelper;

    public AppCompatAutoCompleteTextView(android.content.Context context) {
        this(context, null);
    }

    public AppCompatAutoCompleteTextView(android.content.Context context, android.util.AttributeSet attrs) {
        this(context, attrs, R.attr.autoCompleteTextViewStyle);
    }

    public AppCompatAutoCompleteTextView(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr) {
        super(android.support.v7.widget.TintContextWrapper.wrap(context), attrs, defStyleAttr);
        android.support.v7.widget.TintTypedArray a = android.support.v7.widget.TintTypedArray.obtainStyledAttributes(getContext(), attrs, android.support.v7.widget.AppCompatAutoCompleteTextView.TINT_ATTRS, defStyleAttr, 0);
        if (a.hasValue(0)) {
            setDropDownBackgroundDrawable(a.getDrawable(0));
        }
        a.recycle();
        mBackgroundTintHelper = new android.support.v7.widget.AppCompatBackgroundHelper(this);
        mBackgroundTintHelper.loadFromAttributes(attrs, defStyleAttr);
        mTextHelper = android.support.v7.widget.AppCompatTextHelper.create(this);
        mTextHelper.loadFromAttributes(attrs, defStyleAttr);
        mTextHelper.applyCompoundDrawablesTints();
    }

    @java.lang.Override
    public void setDropDownBackgroundResource(@android.support.annotation.DrawableRes
    int resId) {
        setDropDownBackgroundDrawable(android.support.v7.content.res.AppCompatResources.getDrawable(getContext(), resId));
    }

    @java.lang.Override
    public void setBackgroundResource(@android.support.annotation.DrawableRes
    int resId) {
        super.setBackgroundResource(resId);
        if (mBackgroundTintHelper != null) {
            mBackgroundTintHelper.onSetBackgroundResource(resId);
        }
    }

    @java.lang.Override
    public void setBackgroundDrawable(android.graphics.drawable.Drawable background) {
        super.setBackgroundDrawable(background);
        if (mBackgroundTintHelper != null) {
            mBackgroundTintHelper.onSetBackgroundDrawable(background);
        }
    }

    /**
     * This should be accessed via
     * {@link android.support.v4.view.ViewCompat#setBackgroundTintList(android.view.View, ColorStateList)}
     *
     * @unknown 
     */
    @android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
    @java.lang.Override
    public void setSupportBackgroundTintList(@android.support.annotation.Nullable
    android.content.res.ColorStateList tint) {
        if (mBackgroundTintHelper != null) {
            mBackgroundTintHelper.setSupportBackgroundTintList(tint);
        }
    }

    /**
     * This should be accessed via
     * {@link android.support.v4.view.ViewCompat#getBackgroundTintList(android.view.View)}
     *
     * @unknown 
     */
    @android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
    @java.lang.Override
    @android.support.annotation.Nullable
    public android.content.res.ColorStateList getSupportBackgroundTintList() {
        return mBackgroundTintHelper != null ? mBackgroundTintHelper.getSupportBackgroundTintList() : null;
    }

    /**
     * This should be accessed via
     * {@link android.support.v4.view.ViewCompat#setBackgroundTintMode(android.view.View, PorterDuff.Mode)}
     *
     * @unknown 
     */
    @android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
    @java.lang.Override
    public void setSupportBackgroundTintMode(@android.support.annotation.Nullable
    android.graphics.PorterDuff.Mode tintMode) {
        if (mBackgroundTintHelper != null) {
            mBackgroundTintHelper.setSupportBackgroundTintMode(tintMode);
        }
    }

    /**
     * This should be accessed via
     * {@link android.support.v4.view.ViewCompat#getBackgroundTintMode(android.view.View)}
     *
     * @unknown 
     */
    @android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
    @java.lang.Override
    @android.support.annotation.Nullable
    public android.graphics.PorterDuff.Mode getSupportBackgroundTintMode() {
        return mBackgroundTintHelper != null ? mBackgroundTintHelper.getSupportBackgroundTintMode() : null;
    }

    @java.lang.Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        if (mBackgroundTintHelper != null) {
            mBackgroundTintHelper.applySupportBackgroundTint();
        }
        if (mTextHelper != null) {
            mTextHelper.applyCompoundDrawablesTints();
        }
    }

    @java.lang.Override
    public void setTextAppearance(android.content.Context context, int resId) {
        super.setTextAppearance(context, resId);
        if (mTextHelper != null) {
            mTextHelper.onSetTextAppearance(context, resId);
        }
    }
}

