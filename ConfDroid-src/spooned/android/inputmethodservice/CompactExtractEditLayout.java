/**
 * Copyright (C) 2016 The Android Open Source Project
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
package android.inputmethodservice;


/**
 * A special purpose layout for the editor extract view for tiny (sub 250dp) screens.
 * The layout is based on sizes proportional to screen pixel size to provide for the
 * best layout fidelity on varying pixel sizes and densities.
 *
 * @unknown 
 */
public class CompactExtractEditLayout extends android.widget.LinearLayout {
    private android.view.View mInputExtractEditText;

    private android.view.View mInputExtractAccessories;

    private android.view.View mInputExtractAction;

    private boolean mPerformLayoutChanges;

    public CompactExtractEditLayout(android.content.Context context) {
        super(context);
    }

    public CompactExtractEditLayout(android.content.Context context, android.util.AttributeSet attrs) {
        super(context, attrs);
    }

    public CompactExtractEditLayout(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @java.lang.Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mInputExtractEditText = findViewById(com.android.internal.R.id.inputExtractEditText);
        mInputExtractAccessories = findViewById(com.android.internal.R.id.inputExtractAccessories);
        mInputExtractAction = findViewById(com.android.internal.R.id.inputExtractAction);
        if (((mInputExtractEditText != null) && (mInputExtractAccessories != null)) && (mInputExtractAction != null)) {
            mPerformLayoutChanges = true;
        }
    }

    private int applyFractionInt(@android.annotation.FractionRes
    int fraction, int whole) {
        return java.lang.Math.round(getResources().getFraction(fraction, whole, whole));
    }

    private static void setLayoutHeight(android.view.View v, int px) {
        android.view.ViewGroup.LayoutParams lp = v.getLayoutParams();
        lp.height = px;
        v.setLayoutParams(lp);
    }

    private static void setLayoutMarginBottom(android.view.View v, int px) {
        android.view.ViewGroup.MarginLayoutParams lp = ((android.view.ViewGroup.MarginLayoutParams) (v.getLayoutParams()));
        lp.bottomMargin = px;
        v.setLayoutParams(lp);
    }

    private void applyProportionalLayout(int screenWidthPx, int screenHeightPx) {
        if (getResources().getConfiguration().isScreenRound()) {
            setGravity(android.view.Gravity.BOTTOM);
        }
        android.inputmethodservice.CompactExtractEditLayout.setLayoutHeight(this, applyFractionInt(com.android.internal.R.fraction.input_extract_layout_height, screenHeightPx));
        setPadding(applyFractionInt(com.android.internal.R.fraction.input_extract_layout_padding_left, screenWidthPx), 0, applyFractionInt(com.android.internal.R.fraction.input_extract_layout_padding_right, screenWidthPx), 0);
        android.inputmethodservice.CompactExtractEditLayout.setLayoutMarginBottom(mInputExtractEditText, applyFractionInt(com.android.internal.R.fraction.input_extract_text_margin_bottom, screenHeightPx));
        android.inputmethodservice.CompactExtractEditLayout.setLayoutMarginBottom(mInputExtractAccessories, applyFractionInt(com.android.internal.R.fraction.input_extract_action_margin_bottom, screenHeightPx));
    }

    @java.lang.Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (mPerformLayoutChanges) {
            android.content.res.Resources res = getResources();
            android.util.DisplayMetrics dm = res.getDisplayMetrics();
            int heightPixels = dm.heightPixels;
            int widthPixels = dm.widthPixels;
            applyProportionalLayout(widthPixels, heightPixels);
        }
    }
}

