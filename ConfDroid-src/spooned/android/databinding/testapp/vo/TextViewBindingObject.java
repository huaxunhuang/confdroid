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
package android.databinding.testapp.vo;


public class TextViewBindingObject extends android.databinding.testapp.vo.BindingAdapterBindingObject {
    @android.databinding.Bindable
    private int mAutoLink = android.text.util.Linkify.WEB_URLS;

    @android.databinding.Bindable
    private int mDrawablePadding;

    @android.databinding.Bindable
    private int mInputType = android.text.InputType.TYPE_CLASS_PHONE;

    @android.databinding.Bindable
    private boolean mScrollHorizontally;

    @android.databinding.Bindable
    private boolean mTextAllCaps;

    @android.databinding.Bindable
    private int mTextColorHighlight;

    @android.databinding.Bindable
    private int mTextColorHint;

    @android.databinding.Bindable
    private int mTextColorLink;

    @android.databinding.Bindable
    private boolean mAutoText;

    @android.databinding.Bindable
    private android.text.method.TextKeyListener.Capitalize mCapitalize = android.text.method.TextKeyListener.Capitalize.NONE;

    @android.databinding.Bindable
    private android.widget.TextView.BufferType mBufferType = android.widget.TextView.BufferType.NORMAL;

    @android.databinding.Bindable
    private java.lang.String mDigits = "abcdefg";

    @android.databinding.Bindable
    private int mNumeric = android.databinding.adapters.TextViewBindingAdapter.DECIMAL;

    @android.databinding.Bindable
    private boolean mPhoneNumber;

    @android.databinding.Bindable
    private int mDrawableBottom;

    @android.databinding.Bindable
    private int mDrawableTop;

    @android.databinding.Bindable
    private int mDrawableLeft;

    @android.databinding.Bindable
    private int mDrawableRight;

    @android.databinding.Bindable
    private int mDrawableStart;

    @android.databinding.Bindable
    private int mDrawableEnd;

    @android.databinding.Bindable
    private java.lang.String mImeActionLabel;

    @android.databinding.Bindable
    private int mImeActionId;

    @android.databinding.Bindable
    private java.lang.String mInputMethod = "android.databinding.testapp.vo.TextViewBindingObject$KeyListener1";

    @android.databinding.Bindable
    private float mLineSpacingExtra;

    @android.databinding.Bindable
    private float mLineSpacingMultiplier;

    @android.databinding.Bindable
    private int mMaxLength;

    @android.databinding.Bindable
    private int mShadowColor;

    @android.databinding.Bindable
    private float mShadowDx;

    @android.databinding.Bindable
    private float mShadowDy;

    @android.databinding.Bindable
    private float mShadowRadius;

    @android.databinding.Bindable
    private float mTextSize = 10.0F;

    public android.widget.TextView.BufferType getBufferType() {
        return mBufferType;
    }

    public float getLineSpacingExtra() {
        return mLineSpacingExtra;
    }

    public float getLineSpacingMultiplier() {
        return mLineSpacingMultiplier;
    }

    public float getShadowDx() {
        return mShadowDx;
    }

    public float getShadowDy() {
        return mShadowDy;
    }

    public float getShadowRadius() {
        return mShadowRadius;
    }

    public float getTextSize() {
        return mTextSize;
    }

    public int getAutoLink() {
        return mAutoLink;
    }

    public int getDrawableBottom() {
        return mDrawableBottom;
    }

    public int getDrawableEnd() {
        return mDrawableEnd;
    }

    public int getDrawableLeft() {
        return mDrawableLeft;
    }

    public int getDrawablePadding() {
        return mDrawablePadding;
    }

    public int getDrawableRight() {
        return mDrawableRight;
    }

    public int getDrawableStart() {
        return mDrawableStart;
    }

    public int getDrawableTop() {
        return mDrawableTop;
    }

    public int getImeActionId() {
        return mImeActionId;
    }

    public int getInputType() {
        return mInputType;
    }

    public int getMaxLength() {
        return mMaxLength;
    }

    public int getNumeric() {
        return mNumeric;
    }

    public int getShadowColor() {
        return mShadowColor;
    }

    public int getTextColorHighlight() {
        return mTextColorHighlight;
    }

    public int getTextColorHint() {
        return mTextColorHint;
    }

    public int getTextColorLink() {
        return mTextColorLink;
    }

    public java.lang.String getDigits() {
        return mDigits;
    }

    public java.lang.String getImeActionLabel() {
        return mImeActionLabel;
    }

    public java.lang.String getInputMethod() {
        return mInputMethod;
    }

    public boolean isAutoText() {
        return mAutoText;
    }

    public android.text.method.TextKeyListener.Capitalize getCapitalize() {
        return mCapitalize;
    }

    public void setCapitalize(android.text.method.TextKeyListener.Capitalize capitalize) {
        mCapitalize = capitalize;
        notifyPropertyChanged(BR.capitalize);
    }

    public boolean isPhoneNumber() {
        return mPhoneNumber;
    }

    public boolean isScrollHorizontally() {
        return mScrollHorizontally;
    }

    public boolean isTextAllCaps() {
        return mTextAllCaps;
    }

    public void changeValues() {
        mAutoLink = android.text.util.Linkify.EMAIL_ADDRESSES;
        mDrawablePadding = 10;
        mInputType = android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_FLAG_CAP_WORDS;
        mScrollHorizontally = true;
        mTextAllCaps = true;
        mTextColorHighlight = 0xff00ff00;
        mTextColorHint = 0xffff0000;
        mTextColorLink = 0xff0000ff;
        mAutoText = true;
        mCapitalize = android.text.method.TextKeyListener.Capitalize.SENTENCES;
        mBufferType = android.widget.TextView.BufferType.SPANNABLE;
        mDigits = "hijklmno";
        mNumeric = android.databinding.adapters.TextViewBindingAdapter.SIGNED;
        mPhoneNumber = true;
        mDrawableBottom = 0xff880088;
        mDrawableTop = 0xff111111;
        mDrawableLeft = 0xff222222;
        mDrawableRight = 0xff333333;
        mDrawableStart = 0xff444444;
        mDrawableEnd = 0xff555555;
        mImeActionLabel = "Hello World";
        mImeActionId = 3;
        mInputMethod = "android.databinding.testapp.vo.TextViewBindingObject$KeyListener2";
        mLineSpacingExtra = 2;
        mLineSpacingMultiplier = 3;
        mMaxLength = 100;
        mShadowColor = 0xff666666;
        mShadowDx = 2;
        mShadowDy = 3;
        mShadowRadius = 4;
        mTextSize = 20.0F;
        notifyChange();
    }

    public static class KeyListener1 implements android.text.method.KeyListener {
        @java.lang.Override
        public int getInputType() {
            return android.text.InputType.TYPE_CLASS_TEXT;
        }

        @java.lang.Override
        public boolean onKeyDown(android.view.View view, android.text.Editable text, int keyCode, android.view.KeyEvent event) {
            return false;
        }

        @java.lang.Override
        public boolean onKeyUp(android.view.View view, android.text.Editable text, int keyCode, android.view.KeyEvent event) {
            return false;
        }

        @java.lang.Override
        public boolean onKeyOther(android.view.View view, android.text.Editable text, android.view.KeyEvent event) {
            return false;
        }

        @java.lang.Override
        public void clearMetaKeyState(android.view.View view, android.text.Editable content, int states) {
        }
    }

    public static class KeyListener2 extends android.databinding.testapp.vo.TextViewBindingObject.KeyListener1 {}
}

