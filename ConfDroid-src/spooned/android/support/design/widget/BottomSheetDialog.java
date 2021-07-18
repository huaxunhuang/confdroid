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
package android.support.design.widget;


/**
 * Base class for {@link android.app.Dialog}s styled as a bottom sheet.
 */
public class BottomSheetDialog extends android.support.v7.app.AppCompatDialog {
    private android.support.design.widget.BottomSheetBehavior<android.widget.FrameLayout> mBehavior;

    boolean mCancelable = true;

    private boolean mCanceledOnTouchOutside = true;

    private boolean mCanceledOnTouchOutsideSet;

    public BottomSheetDialog(@android.support.annotation.NonNull
    android.content.Context context) {
        this(context, 0);
    }

    public BottomSheetDialog(@android.support.annotation.NonNull
    android.content.Context context, @android.support.annotation.StyleRes
    int theme) {
        super(context, android.support.design.widget.BottomSheetDialog.getThemeResId(context, theme));
        // We hide the title bar for any style configuration. Otherwise, there will be a gap
        // above the bottom sheet when it is expanded.
        supportRequestWindowFeature(android.view.Window.FEATURE_NO_TITLE);
    }

    protected BottomSheetDialog(@android.support.annotation.NonNull
    android.content.Context context, boolean cancelable, android.content.DialogInterface.OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        supportRequestWindowFeature(android.view.Window.FEATURE_NO_TITLE);
        mCancelable = cancelable;
    }

    @java.lang.Override
    public void setContentView(@android.support.annotation.LayoutRes
    int layoutResId) {
        super.setContentView(wrapInBottomSheet(layoutResId, null, null));
    }

    @java.lang.Override
    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setLayout(android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.MATCH_PARENT);
    }

    @java.lang.Override
    public void setContentView(android.view.View view) {
        super.setContentView(wrapInBottomSheet(0, view, null));
    }

    @java.lang.Override
    public void setContentView(android.view.View view, android.view.ViewGroup.LayoutParams params) {
        super.setContentView(wrapInBottomSheet(0, view, params));
    }

    @java.lang.Override
    public void setCancelable(boolean cancelable) {
        super.setCancelable(cancelable);
        if (mCancelable != cancelable) {
            mCancelable = cancelable;
            if (mBehavior != null) {
                mBehavior.setHideable(cancelable);
            }
        }
    }

    @java.lang.Override
    public void setCanceledOnTouchOutside(boolean cancel) {
        super.setCanceledOnTouchOutside(cancel);
        if (cancel && (!mCancelable)) {
            mCancelable = true;
        }
        mCanceledOnTouchOutside = cancel;
        mCanceledOnTouchOutsideSet = true;
    }

    private android.view.View wrapInBottomSheet(int layoutResId, android.view.View view, android.view.ViewGroup.LayoutParams params) {
        final android.support.design.widget.CoordinatorLayout coordinator = ((android.support.design.widget.CoordinatorLayout) (android.view.View.inflate(getContext(), R.layout.design_bottom_sheet_dialog, null)));
        if ((layoutResId != 0) && (view == null)) {
            view = getLayoutInflater().inflate(layoutResId, coordinator, false);
        }
        android.widget.FrameLayout bottomSheet = ((android.widget.FrameLayout) (coordinator.findViewById(R.id.design_bottom_sheet)));
        mBehavior = android.support.design.widget.BottomSheetBehavior.from(bottomSheet);
        mBehavior.setBottomSheetCallback(mBottomSheetCallback);
        mBehavior.setHideable(mCancelable);
        if (params == null) {
            bottomSheet.addView(view);
        } else {
            bottomSheet.addView(view, params);
        }
        // We treat the CoordinatorLayout as outside the dialog though it is technically inside
        coordinator.findViewById(R.id.touch_outside).setOnClickListener(new android.view.View.OnClickListener() {
            @java.lang.Override
            public void onClick(android.view.View view) {
                if ((mCancelable && isShowing()) && shouldWindowCloseOnTouchOutside()) {
                    cancel();
                }
            }
        });
        return coordinator;
    }

    boolean shouldWindowCloseOnTouchOutside() {
        if (!mCanceledOnTouchOutsideSet) {
            if (android.os.Build.VERSION.SDK_INT < 11) {
                mCanceledOnTouchOutside = true;
            } else {
                android.content.res.TypedArray a = getContext().obtainStyledAttributes(new int[]{ android.R.attr.windowCloseOnTouchOutside });
                mCanceledOnTouchOutside = a.getBoolean(0, true);
                a.recycle();
            }
            mCanceledOnTouchOutsideSet = true;
        }
        return mCanceledOnTouchOutside;
    }

    private static int getThemeResId(android.content.Context context, int themeId) {
        if (themeId == 0) {
            // If the provided theme is 0, then retrieve the dialogTheme from our theme
            android.util.TypedValue outValue = new android.util.TypedValue();
            if (context.getTheme().resolveAttribute(R.attr.bottomSheetDialogTheme, outValue, true)) {
                themeId = outValue.resourceId;
            } else {
                // bottomSheetDialogTheme is not provided; we default to our light theme
                themeId = R.style.Theme_Design_Light_BottomSheetDialog;
            }
        }
        return themeId;
    }

    private android.support.design.widget.BottomSheetBehavior.BottomSheetCallback mBottomSheetCallback = new android.support.design.widget.BottomSheetBehavior.BottomSheetCallback() {
        @java.lang.Override
        public void onStateChanged(@android.support.annotation.NonNull
        android.view.View bottomSheet, @android.support.design.widget.BottomSheetBehavior.State
        int newState) {
            if (newState == android.support.design.widget.BottomSheetBehavior.STATE_HIDDEN) {
                cancel();
            }
        }

        @java.lang.Override
        public void onSlide(@android.support.annotation.NonNull
        android.view.View bottomSheet, float slideOffset) {
        }
    };
}

