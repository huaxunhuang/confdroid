/**
 * Copyright (C) 2007 The Android Open Source Project
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
package android.app;


/**
 * <p>A dialog showing a progress indicator and an optional text message or view.
 * Only a text message or a view can be used at the same time.</p>
 * <p>The dialog can be made cancelable on back key press.</p>
 * <p>The progress range is 0..10000.</p>
 */
public class ProgressDialog extends android.app.AlertDialog {
    /**
     * Creates a ProgressDialog with a circular, spinning progress
     * bar. This is the default.
     */
    public static final int STYLE_SPINNER = 0;

    /**
     * Creates a ProgressDialog with a horizontal progress bar.
     */
    public static final int STYLE_HORIZONTAL = 1;

    private android.widget.ProgressBar mProgress;

    private android.widget.TextView mMessageView;

    private int mProgressStyle = android.app.ProgressDialog.STYLE_SPINNER;

    private android.widget.TextView mProgressNumber;

    private java.lang.String mProgressNumberFormat;

    private android.widget.TextView mProgressPercent;

    private java.text.NumberFormat mProgressPercentFormat;

    private int mMax;

    private int mProgressVal;

    private int mSecondaryProgressVal;

    private int mIncrementBy;

    private int mIncrementSecondaryBy;

    private android.graphics.drawable.Drawable mProgressDrawable;

    private android.graphics.drawable.Drawable mIndeterminateDrawable;

    private java.lang.CharSequence mMessage;

    private boolean mIndeterminate;

    private boolean mHasStarted;

    private android.os.Handler mViewUpdateHandler;

    public ProgressDialog(android.content.Context context) {
        super(context);
        initFormats();
    }

    public ProgressDialog(android.content.Context context, int theme) {
        super(context, theme);
        initFormats();
    }

    private void initFormats() {
        mProgressNumberFormat = "%1d/%2d";
        mProgressPercentFormat = java.text.NumberFormat.getPercentInstance();
        mProgressPercentFormat.setMaximumFractionDigits(0);
    }

    public static android.app.ProgressDialog show(android.content.Context context, java.lang.CharSequence title, java.lang.CharSequence message) {
        return android.app.ProgressDialog.show(context, title, message, false);
    }

    public static android.app.ProgressDialog show(android.content.Context context, java.lang.CharSequence title, java.lang.CharSequence message, boolean indeterminate) {
        return android.app.ProgressDialog.show(context, title, message, indeterminate, false, null);
    }

    public static android.app.ProgressDialog show(android.content.Context context, java.lang.CharSequence title, java.lang.CharSequence message, boolean indeterminate, boolean cancelable) {
        return android.app.ProgressDialog.show(context, title, message, indeterminate, cancelable, null);
    }

    public static android.app.ProgressDialog show(android.content.Context context, java.lang.CharSequence title, java.lang.CharSequence message, boolean indeterminate, boolean cancelable, android.content.DialogInterface.OnCancelListener cancelListener) {
        android.app.ProgressDialog dialog = new android.app.ProgressDialog(context);
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.setIndeterminate(indeterminate);
        dialog.setCancelable(cancelable);
        dialog.setOnCancelListener(cancelListener);
        dialog.show();
        return dialog;
    }

    @java.lang.Override
    protected void onCreate(android.os.Bundle savedInstanceState) {
        android.view.LayoutInflater inflater = android.view.LayoutInflater.from(mContext);
        android.content.res.TypedArray a = mContext.obtainStyledAttributes(null, com.android.internal.R.styleable.AlertDialog, com.android.internal.R.attr.alertDialogStyle, 0);
        if (mProgressStyle == android.app.ProgressDialog.STYLE_HORIZONTAL) {
            /* Use a separate handler to update the text views as they
            must be updated on the same thread that created them.
             */
            mViewUpdateHandler = new android.os.Handler() {
                @java.lang.Override
                public void handleMessage(android.os.Message msg) {
                    super.handleMessage(msg);
                    /* Update the number and percent */
                    int progress = mProgress.getProgress();
                    int max = mProgress.getMax();
                    if (mProgressNumberFormat != null) {
                        java.lang.String format = mProgressNumberFormat;
                        mProgressNumber.setText(java.lang.String.format(format, progress, max));
                    } else {
                        mProgressNumber.setText("");
                    }
                    if (mProgressPercentFormat != null) {
                        double percent = ((double) (progress)) / ((double) (max));
                        android.text.SpannableString tmp = new android.text.SpannableString(mProgressPercentFormat.format(percent));
                        tmp.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, tmp.length(), android.text.Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        mProgressPercent.setText(tmp);
                    } else {
                        mProgressPercent.setText("");
                    }
                }
            };
            android.view.View view = inflater.inflate(a.getResourceId(com.android.internal.R.styleable.AlertDialog_horizontalProgressLayout, R.layout.alert_dialog_progress), null);
            mProgress = ((android.widget.ProgressBar) (view.findViewById(R.id.progress)));
            mProgressNumber = ((android.widget.TextView) (view.findViewById(R.id.progress_number)));
            mProgressPercent = ((android.widget.TextView) (view.findViewById(R.id.progress_percent)));
            setView(view);
        } else {
            android.view.View view = inflater.inflate(a.getResourceId(com.android.internal.R.styleable.AlertDialog_progressLayout, R.layout.progress_dialog), null);
            mProgress = ((android.widget.ProgressBar) (view.findViewById(R.id.progress)));
            mMessageView = ((android.widget.TextView) (view.findViewById(R.id.message)));
            setView(view);
        }
        a.recycle();
        if (mMax > 0) {
            setMax(mMax);
        }
        if (mProgressVal > 0) {
            setProgress(mProgressVal);
        }
        if (mSecondaryProgressVal > 0) {
            setSecondaryProgress(mSecondaryProgressVal);
        }
        if (mIncrementBy > 0) {
            incrementProgressBy(mIncrementBy);
        }
        if (mIncrementSecondaryBy > 0) {
            incrementSecondaryProgressBy(mIncrementSecondaryBy);
        }
        if (mProgressDrawable != null) {
            setProgressDrawable(mProgressDrawable);
        }
        if (mIndeterminateDrawable != null) {
            setIndeterminateDrawable(mIndeterminateDrawable);
        }
        if (mMessage != null) {
            setMessage(mMessage);
        }
        setIndeterminate(mIndeterminate);
        onProgressChanged();
        super.onCreate(savedInstanceState);
    }

    @java.lang.Override
    public void onStart() {
        super.onStart();
        mHasStarted = true;
    }

    @java.lang.Override
    protected void onStop() {
        super.onStop();
        mHasStarted = false;
    }

    public void setProgress(int value) {
        if (mHasStarted) {
            mProgress.setProgress(value);
            onProgressChanged();
        } else {
            mProgressVal = value;
        }
    }

    public void setSecondaryProgress(int secondaryProgress) {
        if (mProgress != null) {
            mProgress.setSecondaryProgress(secondaryProgress);
            onProgressChanged();
        } else {
            mSecondaryProgressVal = secondaryProgress;
        }
    }

    public int getProgress() {
        if (mProgress != null) {
            return mProgress.getProgress();
        }
        return mProgressVal;
    }

    public int getSecondaryProgress() {
        if (mProgress != null) {
            return mProgress.getSecondaryProgress();
        }
        return mSecondaryProgressVal;
    }

    public int getMax() {
        if (mProgress != null) {
            return mProgress.getMax();
        }
        return mMax;
    }

    public void setMax(int max) {
        if (mProgress != null) {
            mProgress.setMax(max);
            onProgressChanged();
        } else {
            mMax = max;
        }
    }

    public void incrementProgressBy(int diff) {
        if (mProgress != null) {
            mProgress.incrementProgressBy(diff);
            onProgressChanged();
        } else {
            mIncrementBy += diff;
        }
    }

    public void incrementSecondaryProgressBy(int diff) {
        if (mProgress != null) {
            mProgress.incrementSecondaryProgressBy(diff);
            onProgressChanged();
        } else {
            mIncrementSecondaryBy += diff;
        }
    }

    public void setProgressDrawable(android.graphics.drawable.Drawable d) {
        if (mProgress != null) {
            mProgress.setProgressDrawable(d);
        } else {
            mProgressDrawable = d;
        }
    }

    public void setIndeterminateDrawable(android.graphics.drawable.Drawable d) {
        if (mProgress != null) {
            mProgress.setIndeterminateDrawable(d);
        } else {
            mIndeterminateDrawable = d;
        }
    }

    public void setIndeterminate(boolean indeterminate) {
        if (mProgress != null) {
            mProgress.setIndeterminate(indeterminate);
        } else {
            mIndeterminate = indeterminate;
        }
    }

    public boolean isIndeterminate() {
        if (mProgress != null) {
            return mProgress.isIndeterminate();
        }
        return mIndeterminate;
    }

    @java.lang.Override
    public void setMessage(java.lang.CharSequence message) {
        if (mProgress != null) {
            if (mProgressStyle == android.app.ProgressDialog.STYLE_HORIZONTAL) {
                super.setMessage(message);
            } else {
                mMessageView.setText(message);
            }
        } else {
            mMessage = message;
        }
    }

    public void setProgressStyle(int style) {
        mProgressStyle = style;
    }

    /**
     * Change the format of the small text showing current and maximum units
     * of progress.  The default is "%1d/%2d".
     * Should not be called during the number is progressing.
     *
     * @param format
     * 		A string passed to {@link String#format String.format()};
     * 		use "%1d" for the current number and "%2d" for the maximum.  If null,
     * 		nothing will be shown.
     */
    public void setProgressNumberFormat(java.lang.String format) {
        mProgressNumberFormat = format;
        onProgressChanged();
    }

    /**
     * Change the format of the small text showing the percentage of progress.
     * The default is
     * {@link NumberFormat#getPercentInstance() NumberFormat.getPercentageInstnace().}
     * Should not be called during the number is progressing.
     *
     * @param format
     * 		An instance of a {@link NumberFormat} to generate the
     * 		percentage text.  If null, nothing will be shown.
     */
    public void setProgressPercentFormat(java.text.NumberFormat format) {
        mProgressPercentFormat = format;
        onProgressChanged();
    }

    private void onProgressChanged() {
        if (mProgressStyle == android.app.ProgressDialog.STYLE_HORIZONTAL) {
            if ((mViewUpdateHandler != null) && (!mViewUpdateHandler.hasMessages(0))) {
                mViewUpdateHandler.sendEmptyMessage(0);
            }
        }
    }
}

