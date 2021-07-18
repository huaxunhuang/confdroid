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


class AppCompatPopupWindow extends android.widget.PopupWindow {
    private static final java.lang.String TAG = "AppCompatPopupWindow";

    private static final boolean COMPAT_OVERLAP_ANCHOR = android.os.Build.VERSION.SDK_INT < 21;

    private boolean mOverlapAnchor;

    public AppCompatPopupWindow(@android.support.annotation.NonNull
    android.content.Context context, @android.support.annotation.Nullable
    android.util.AttributeSet attrs, @android.support.annotation.AttrRes
    int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr, 0);
    }

    @android.annotation.TargetApi(11)
    public AppCompatPopupWindow(@android.support.annotation.NonNull
    android.content.Context context, @android.support.annotation.Nullable
    android.util.AttributeSet attrs, @android.support.annotation.AttrRes
    int defStyleAttr, @android.support.annotation.StyleRes
    int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        android.support.v7.widget.TintTypedArray a = android.support.v7.widget.TintTypedArray.obtainStyledAttributes(context, attrs, R.styleable.PopupWindow, defStyleAttr, defStyleRes);
        if (a.hasValue(R.styleable.PopupWindow_overlapAnchor)) {
            setSupportOverlapAnchor(a.getBoolean(R.styleable.PopupWindow_overlapAnchor, false));
        }
        // We re-set this for tinting purposes
        setBackgroundDrawable(a.getDrawable(R.styleable.PopupWindow_android_popupBackground));
        final int sdk = android.os.Build.VERSION.SDK_INT;
        if ((defStyleRes != 0) && (sdk < 11)) {
            // If we have a defStyleRes, but we're on < API 11, we need to manually set attributes
            // from the style
            // android:popupAnimationStyle was added in API 9
            if (a.hasValue(R.styleable.PopupWindow_android_popupAnimationStyle)) {
                setAnimationStyle(a.getResourceId(R.styleable.PopupWindow_android_popupAnimationStyle, -1));
            }
        }
        a.recycle();
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            // For devices pre-ICS, we need to wrap the internal OnScrollChangedListener
            // due to NPEs.
            android.support.v7.widget.AppCompatPopupWindow.wrapOnScrollChangedListener(this);
        }
    }

    @java.lang.Override
    public void showAsDropDown(android.view.View anchor, int xoff, int yoff) {
        if (android.support.v7.widget.AppCompatPopupWindow.COMPAT_OVERLAP_ANCHOR && mOverlapAnchor) {
            // If we're pre-L, emulate overlapAnchor by modifying the yOff
            yoff -= anchor.getHeight();
        }
        super.showAsDropDown(anchor, xoff, yoff);
    }

    @android.annotation.TargetApi(android.os.Build.VERSION_CODES.KITKAT)
    @java.lang.Override
    public void showAsDropDown(android.view.View anchor, int xoff, int yoff, int gravity) {
        if (android.support.v7.widget.AppCompatPopupWindow.COMPAT_OVERLAP_ANCHOR && mOverlapAnchor) {
            // If we're pre-L, emulate overlapAnchor by modifying the yOff
            yoff -= anchor.getHeight();
        }
        super.showAsDropDown(anchor, xoff, yoff, gravity);
    }

    @java.lang.Override
    public void update(android.view.View anchor, int xoff, int yoff, int width, int height) {
        if (android.support.v7.widget.AppCompatPopupWindow.COMPAT_OVERLAP_ANCHOR && mOverlapAnchor) {
            // If we're pre-L, emulate overlapAnchor by modifying the yOff
            yoff -= anchor.getHeight();
        }
        super.update(anchor, xoff, yoff, width, height);
    }

    private static void wrapOnScrollChangedListener(final android.widget.PopupWindow popup) {
        try {
            final java.lang.reflect.Field fieldAnchor = android.widget.PopupWindow.class.getDeclaredField("mAnchor");
            fieldAnchor.setAccessible(true);
            final java.lang.reflect.Field fieldListener = android.widget.PopupWindow.class.getDeclaredField("mOnScrollChangedListener");
            fieldListener.setAccessible(true);
            final android.view.ViewTreeObserver.OnScrollChangedListener originalListener = ((android.view.ViewTreeObserver.OnScrollChangedListener) (fieldListener.get(popup)));
            // Now set a new listener, wrapping the original and only proxying the call when
            // we have an anchor view.
            fieldListener.set(popup, new android.view.ViewTreeObserver.OnScrollChangedListener() {
                @java.lang.Override
                public void onScrollChanged() {
                    try {
                        java.lang.ref.WeakReference<android.view.View> mAnchor = ((java.lang.ref.WeakReference<android.view.View>) (fieldAnchor.get(popup)));
                        if ((mAnchor == null) || (mAnchor.get() == null)) {
                            return;
                        } else {
                            originalListener.onScrollChanged();
                        }
                    } catch (java.lang.IllegalAccessException e) {
                        // Oh well...
                    }
                }
            });
        } catch (java.lang.Exception e) {
            android.util.Log.d(android.support.v7.widget.AppCompatPopupWindow.TAG, "Exception while installing workaround OnScrollChangedListener", e);
        }
    }

    /**
     *
     *
     * @unknown 
     */
    @android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
    public void setSupportOverlapAnchor(boolean overlapAnchor) {
        if (android.support.v7.widget.AppCompatPopupWindow.COMPAT_OVERLAP_ANCHOR) {
            mOverlapAnchor = overlapAnchor;
        } else {
            android.support.v4.widget.PopupWindowCompat.setOverlapAnchor(this, overlapAnchor);
        }
    }

    /**
     *
     *
     * @unknown 
     */
    @android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
    public boolean getSupportOverlapAnchor() {
        if (android.support.v7.widget.AppCompatPopupWindow.COMPAT_OVERLAP_ANCHOR) {
            return mOverlapAnchor;
        } else {
            return android.support.v4.widget.PopupWindowCompat.getOverlapAnchor(this);
        }
    }
}

