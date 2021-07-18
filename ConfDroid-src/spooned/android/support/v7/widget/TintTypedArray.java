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
 * A class that wraps a {@link android.content.res.TypedArray} and provides the same public API
 * surface. The purpose of this class is so that we can intercept the {@link #getDrawable(int)}
 * call and tint the result.
 *
 * @unknown 
 */
@android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
public class TintTypedArray {
    private final android.content.Context mContext;

    private final android.content.res.TypedArray mWrapped;

    private android.util.TypedValue mTypedValue;

    public static android.support.v7.widget.TintTypedArray obtainStyledAttributes(android.content.Context context, android.util.AttributeSet set, int[] attrs) {
        return new android.support.v7.widget.TintTypedArray(context, context.obtainStyledAttributes(set, attrs));
    }

    public static android.support.v7.widget.TintTypedArray obtainStyledAttributes(android.content.Context context, android.util.AttributeSet set, int[] attrs, int defStyleAttr, int defStyleRes) {
        return new android.support.v7.widget.TintTypedArray(context, context.obtainStyledAttributes(set, attrs, defStyleAttr, defStyleRes));
    }

    public static android.support.v7.widget.TintTypedArray obtainStyledAttributes(android.content.Context context, int resid, int[] attrs) {
        return new android.support.v7.widget.TintTypedArray(context, context.obtainStyledAttributes(resid, attrs));
    }

    private TintTypedArray(android.content.Context context, android.content.res.TypedArray array) {
        mContext = context;
        mWrapped = array;
    }

    public android.graphics.drawable.Drawable getDrawable(int index) {
        if (mWrapped.hasValue(index)) {
            final int resourceId = mWrapped.getResourceId(index, 0);
            if (resourceId != 0) {
                return android.support.v7.content.res.AppCompatResources.getDrawable(mContext, resourceId);
            }
        }
        return mWrapped.getDrawable(index);
    }

    public android.graphics.drawable.Drawable getDrawableIfKnown(int index) {
        if (mWrapped.hasValue(index)) {
            final int resourceId = mWrapped.getResourceId(index, 0);
            if (resourceId != 0) {
                return android.support.v7.widget.AppCompatDrawableManager.get().getDrawable(mContext, resourceId, true);
            }
        }
        return null;
    }

    public int length() {
        return mWrapped.length();
    }

    public int getIndexCount() {
        return mWrapped.getIndexCount();
    }

    public int getIndex(int at) {
        return mWrapped.getIndex(at);
    }

    public android.content.res.Resources getResources() {
        return mWrapped.getResources();
    }

    public java.lang.CharSequence getText(int index) {
        return mWrapped.getText(index);
    }

    public java.lang.String getString(int index) {
        return mWrapped.getString(index);
    }

    public java.lang.String getNonResourceString(int index) {
        return mWrapped.getNonResourceString(index);
    }

    public boolean getBoolean(int index, boolean defValue) {
        return mWrapped.getBoolean(index, defValue);
    }

    public int getInt(int index, int defValue) {
        return mWrapped.getInt(index, defValue);
    }

    public float getFloat(int index, float defValue) {
        return mWrapped.getFloat(index, defValue);
    }

    public int getColor(int index, int defValue) {
        return mWrapped.getColor(index, defValue);
    }

    public android.content.res.ColorStateList getColorStateList(int index) {
        if (mWrapped.hasValue(index)) {
            final int resourceId = mWrapped.getResourceId(index, 0);
            if (resourceId != 0) {
                final android.content.res.ColorStateList value = android.support.v7.content.res.AppCompatResources.getColorStateList(mContext, resourceId);
                if (value != null) {
                    return value;
                }
            }
        }
        return mWrapped.getColorStateList(index);
    }

    public int getInteger(int index, int defValue) {
        return mWrapped.getInteger(index, defValue);
    }

    public float getDimension(int index, float defValue) {
        return mWrapped.getDimension(index, defValue);
    }

    public int getDimensionPixelOffset(int index, int defValue) {
        return mWrapped.getDimensionPixelOffset(index, defValue);
    }

    public int getDimensionPixelSize(int index, int defValue) {
        return mWrapped.getDimensionPixelSize(index, defValue);
    }

    public int getLayoutDimension(int index, java.lang.String name) {
        return mWrapped.getLayoutDimension(index, name);
    }

    public int getLayoutDimension(int index, int defValue) {
        return mWrapped.getLayoutDimension(index, defValue);
    }

    public float getFraction(int index, int base, int pbase, float defValue) {
        return mWrapped.getFraction(index, base, pbase, defValue);
    }

    public int getResourceId(int index, int defValue) {
        return mWrapped.getResourceId(index, defValue);
    }

    public java.lang.CharSequence[] getTextArray(int index) {
        return mWrapped.getTextArray(index);
    }

    public boolean getValue(int index, android.util.TypedValue outValue) {
        return mWrapped.getValue(index, outValue);
    }

    public int getType(int index) {
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            return mWrapped.getType(index);
        } else {
            if (mTypedValue == null) {
                mTypedValue = new android.util.TypedValue();
            }
            mWrapped.getValue(index, mTypedValue);
            return mTypedValue.type;
        }
    }

    public boolean hasValue(int index) {
        return mWrapped.hasValue(index);
    }

    public android.util.TypedValue peekValue(int index) {
        return mWrapped.peekValue(index);
    }

    public java.lang.String getPositionDescription() {
        return mWrapped.getPositionDescription();
    }

    public void recycle() {
        mWrapped.recycle();
    }

    public int getChangingConfigurations() {
        return mWrapped.getChangingConfigurations();
    }
}

