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
 * A {@link CheckedTextView} which supports compatible features on older version of the platform.
 *
 * <p>This will automatically be used when you use {@link CheckedTextView} in your layouts.
 * You should only need to manually use this class when writing custom views.</p>
 */
public class AppCompatCheckedTextView extends android.widget.CheckedTextView {
    private static final int[] TINT_ATTRS = new int[]{ android.R.attr.checkMark };

    private android.support.v7.widget.AppCompatTextHelper mTextHelper;

    public AppCompatCheckedTextView(android.content.Context context) {
        this(context, null);
    }

    public AppCompatCheckedTextView(android.content.Context context, android.util.AttributeSet attrs) {
        this(context, attrs, android.R.attr.checkedTextViewStyle);
    }

    public AppCompatCheckedTextView(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr) {
        super(android.support.v7.widget.TintContextWrapper.wrap(context), attrs, defStyleAttr);
        mTextHelper = android.support.v7.widget.AppCompatTextHelper.create(this);
        mTextHelper.loadFromAttributes(attrs, defStyleAttr);
        mTextHelper.applyCompoundDrawablesTints();
        android.support.v7.widget.TintTypedArray a = android.support.v7.widget.TintTypedArray.obtainStyledAttributes(getContext(), attrs, android.support.v7.widget.AppCompatCheckedTextView.TINT_ATTRS, defStyleAttr, 0);
        setCheckMarkDrawable(a.getDrawable(0));
        a.recycle();
    }

    @java.lang.Override
    public void setCheckMarkDrawable(@android.support.annotation.DrawableRes
    int resId) {
        setCheckMarkDrawable(android.support.v7.content.res.AppCompatResources.getDrawable(getContext(), resId));
    }

    @java.lang.Override
    public void setTextAppearance(android.content.Context context, int resId) {
        super.setTextAppearance(context, resId);
        if (mTextHelper != null) {
            mTextHelper.onSetTextAppearance(context, resId);
        }
    }

    @java.lang.Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        if (mTextHelper != null) {
            mTextHelper.applyCompoundDrawablesTints();
        }
    }
}

