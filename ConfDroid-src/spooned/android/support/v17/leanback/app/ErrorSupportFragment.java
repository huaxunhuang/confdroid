/**
 * This file is auto-generated from ErrorFragment.java.  DO NOT MODIFY.
 */
/**
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package android.support.v17.leanback.app;


/**
 * A fragment for displaying an error indication.
 */
public class ErrorSupportFragment extends android.support.v17.leanback.app.BrandedSupportFragment {
    private android.view.ViewGroup mErrorFrame;

    private android.widget.ImageView mImageView;

    private android.widget.TextView mTextView;

    private android.widget.Button mButton;

    private android.graphics.drawable.Drawable mDrawable;

    private java.lang.CharSequence mMessage;

    private java.lang.String mButtonText;

    private android.view.View.OnClickListener mButtonClickListener;

    private android.graphics.drawable.Drawable mBackgroundDrawable;

    private boolean mIsBackgroundTranslucent = true;

    /**
     * Sets the default background.
     *
     * @param translucent
     * 		True to set a translucent background.
     */
    public void setDefaultBackground(boolean translucent) {
        mBackgroundDrawable = null;
        mIsBackgroundTranslucent = translucent;
        updateBackground();
        updateMessage();
    }

    /**
     * Returns true if the background is translucent.
     */
    public boolean isBackgroundTranslucent() {
        return mIsBackgroundTranslucent;
    }

    /**
     * Sets a drawable for the fragment background.
     *
     * @param drawable
     * 		The drawable used for the background.
     */
    public void setBackgroundDrawable(android.graphics.drawable.Drawable drawable) {
        mBackgroundDrawable = drawable;
        if (drawable != null) {
            final int opacity = drawable.getOpacity();
            mIsBackgroundTranslucent = (opacity == android.graphics.PixelFormat.TRANSLUCENT) || (opacity == android.graphics.PixelFormat.TRANSPARENT);
        }
        updateBackground();
        updateMessage();
    }

    /**
     * Returns the background drawable.  May be null if a default is used.
     */
    public android.graphics.drawable.Drawable getBackgroundDrawable() {
        return mBackgroundDrawable;
    }

    /**
     * Sets the drawable to be used for the error image.
     *
     * @param drawable
     * 		The drawable used for the error image.
     */
    public void setImageDrawable(android.graphics.drawable.Drawable drawable) {
        mDrawable = drawable;
        updateImageDrawable();
    }

    /**
     * Returns the drawable used for the error image.
     */
    public android.graphics.drawable.Drawable getImageDrawable() {
        return mDrawable;
    }

    /**
     * Sets the error message.
     *
     * @param message
     * 		The error message.
     */
    public void setMessage(java.lang.CharSequence message) {
        mMessage = message;
        updateMessage();
    }

    /**
     * Returns the error message.
     */
    public java.lang.CharSequence getMessage() {
        return mMessage;
    }

    /**
     * Sets the button text.
     *
     * @param text
     * 		The button text.
     */
    public void setButtonText(java.lang.String text) {
        mButtonText = text;
        updateButton();
    }

    /**
     * Returns the button text.
     */
    public java.lang.String getButtonText() {
        return mButtonText;
    }

    /**
     * Set the button click listener.
     *
     * @param clickListener
     * 		The click listener for the button.
     */
    public void setButtonClickListener(android.view.View.OnClickListener clickListener) {
        mButtonClickListener = clickListener;
        updateButton();
    }

    /**
     * Returns the button click listener.
     */
    public android.view.View.OnClickListener getButtonClickListener() {
        return mButtonClickListener;
    }

    @java.lang.Override
    public android.view.View onCreateView(android.view.LayoutInflater inflater, android.view.ViewGroup container, android.os.Bundle savedInstanceState) {
        android.view.View root = inflater.inflate(R.layout.lb_error_fragment, container, false);
        mErrorFrame = ((android.view.ViewGroup) (root.findViewById(R.id.error_frame)));
        updateBackground();
        installTitleView(inflater, mErrorFrame, savedInstanceState);
        mImageView = ((android.widget.ImageView) (root.findViewById(R.id.image)));
        updateImageDrawable();
        mTextView = ((android.widget.TextView) (root.findViewById(R.id.message)));
        updateMessage();
        mButton = ((android.widget.Button) (root.findViewById(R.id.button)));
        updateButton();
        android.graphics.Paint.FontMetricsInt metrics = android.support.v17.leanback.app.ErrorSupportFragment.getFontMetricsInt(mTextView);
        int underImageBaselineMargin = container.getResources().getDimensionPixelSize(R.dimen.lb_error_under_image_baseline_margin);
        android.support.v17.leanback.app.ErrorSupportFragment.setTopMargin(mTextView, underImageBaselineMargin + metrics.ascent);
        int underMessageBaselineMargin = container.getResources().getDimensionPixelSize(R.dimen.lb_error_under_message_baseline_margin);
        android.support.v17.leanback.app.ErrorSupportFragment.setTopMargin(mButton, underMessageBaselineMargin - metrics.descent);
        return root;
    }

    private void updateBackground() {
        if (mErrorFrame != null) {
            if (mBackgroundDrawable != null) {
                mErrorFrame.setBackground(mBackgroundDrawable);
            } else {
                mErrorFrame.setBackgroundColor(mErrorFrame.getResources().getColor(mIsBackgroundTranslucent ? R.color.lb_error_background_color_translucent : R.color.lb_error_background_color_opaque));
            }
        }
    }

    private void updateMessage() {
        if (mTextView != null) {
            mTextView.setText(mMessage);
            mTextView.setVisibility(android.text.TextUtils.isEmpty(mMessage) ? android.view.View.GONE : android.view.View.VISIBLE);
        }
    }

    private void updateImageDrawable() {
        if (mImageView != null) {
            mImageView.setImageDrawable(mDrawable);
            mImageView.setVisibility(mDrawable == null ? android.view.View.GONE : android.view.View.VISIBLE);
        }
    }

    private void updateButton() {
        if (mButton != null) {
            mButton.setText(mButtonText);
            mButton.setOnClickListener(mButtonClickListener);
            mButton.setVisibility(android.text.TextUtils.isEmpty(mButtonText) ? android.view.View.GONE : android.view.View.VISIBLE);
            mButton.requestFocus();
        }
    }

    @java.lang.Override
    public void onStart() {
        super.onStart();
        mErrorFrame.requestFocus();
    }

    private static android.graphics.Paint.FontMetricsInt getFontMetricsInt(android.widget.TextView textView) {
        android.graphics.Paint paint = new android.graphics.Paint(android.graphics.Paint.ANTI_ALIAS_FLAG);
        paint.setTextSize(textView.getTextSize());
        paint.setTypeface(textView.getTypeface());
        return paint.getFontMetricsInt();
    }

    private static void setTopMargin(android.widget.TextView textView, int topMargin) {
        android.view.ViewGroup.MarginLayoutParams lp = ((android.view.ViewGroup.MarginLayoutParams) (textView.getLayoutParams()));
        lp.topMargin = topMargin;
        textView.setLayoutParams(lp);
    }
}

