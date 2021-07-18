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
 * A {@link CheckBox} which supports compatible features on older version of the platform,
 * including:
 * <ul>
 *     <li>Allows dynamic tint of it background via the background tint methods in
 *     {@link android.support.v4.widget.CompoundButtonCompat}.</li>
 *     <li>Allows setting of the background tint using {@link R.attr#buttonTint} and
 *     {@link R.attr#buttonTintMode}.</li>
 * </ul>
 *
 * <p>This will automatically be used when you use {@link CheckBox} in your layouts.
 * You should only need to manually use this class when writing custom views.</p>
 */
public class AppCompatCheckBox extends android.widget.CheckBox implements android.support.v4.widget.TintableCompoundButton {
    private android.support.v7.widget.AppCompatCompoundButtonHelper mCompoundButtonHelper;

    public AppCompatCheckBox(android.content.Context context) {
        this(context, null);
    }

    public AppCompatCheckBox(android.content.Context context, android.util.AttributeSet attrs) {
        this(context, attrs, R.attr.checkboxStyle);
    }

    public AppCompatCheckBox(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr) {
        super(android.support.v7.widget.TintContextWrapper.wrap(context), attrs, defStyleAttr);
        mCompoundButtonHelper = new android.support.v7.widget.AppCompatCompoundButtonHelper(this);
        mCompoundButtonHelper.loadFromAttributes(attrs, defStyleAttr);
    }

    @java.lang.Override
    public void setButtonDrawable(android.graphics.drawable.Drawable buttonDrawable) {
        super.setButtonDrawable(buttonDrawable);
        if (mCompoundButtonHelper != null) {
            mCompoundButtonHelper.onSetButtonDrawable();
        }
    }

    @java.lang.Override
    public void setButtonDrawable(@android.support.annotation.DrawableRes
    int resId) {
        setButtonDrawable(android.support.v7.content.res.AppCompatResources.getDrawable(getContext(), resId));
    }

    @java.lang.Override
    public int getCompoundPaddingLeft() {
        final int value = super.getCompoundPaddingLeft();
        return mCompoundButtonHelper != null ? mCompoundButtonHelper.getCompoundPaddingLeft(value) : value;
    }

    /**
     * This should be accessed from {@link android.support.v4.widget.CompoundButtonCompat}
     *
     * @unknown 
     */
    @android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
    @java.lang.Override
    public void setSupportButtonTintList(@android.support.annotation.Nullable
    android.content.res.ColorStateList tint) {
        if (mCompoundButtonHelper != null) {
            mCompoundButtonHelper.setSupportButtonTintList(tint);
        }
    }

    /**
     * This should be accessed from {@link android.support.v4.widget.CompoundButtonCompat}
     *
     * @unknown 
     */
    @android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
    @android.support.annotation.Nullable
    @java.lang.Override
    public android.content.res.ColorStateList getSupportButtonTintList() {
        return mCompoundButtonHelper != null ? mCompoundButtonHelper.getSupportButtonTintList() : null;
    }

    /**
     * This should be accessed from {@link android.support.v4.widget.CompoundButtonCompat}
     *
     * @unknown 
     */
    @android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
    @java.lang.Override
    public void setSupportButtonTintMode(@android.support.annotation.Nullable
    android.graphics.PorterDuff.Mode tintMode) {
        if (mCompoundButtonHelper != null) {
            mCompoundButtonHelper.setSupportButtonTintMode(tintMode);
        }
    }

    /**
     * This should be accessed from {@link android.support.v4.widget.CompoundButtonCompat}
     *
     * @unknown 
     */
    @android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
    @android.support.annotation.Nullable
    @java.lang.Override
    public android.graphics.PorterDuff.Mode getSupportButtonTintMode() {
        return mCompoundButtonHelper != null ? mCompoundButtonHelper.getSupportButtonTintMode() : null;
    }
}

