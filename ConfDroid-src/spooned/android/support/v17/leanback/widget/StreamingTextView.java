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
package android.support.v17.leanback.widget;


/**
 * Shows the recognized text as a continuous stream of words.
 */
class StreamingTextView extends android.widget.EditText {
    private static final boolean DEBUG = false;

    private static final java.lang.String TAG = "StreamingTextView";

    private static final float TEXT_DOT_SCALE = 1.3F;

    private static final boolean DOTS_FOR_STABLE = false;

    private static final boolean DOTS_FOR_PENDING = true;

    static final boolean ANIMATE_DOTS_FOR_PENDING = true;

    private static final long STREAM_UPDATE_DELAY_MILLIS = 50;

    private static final java.util.regex.Pattern SPLIT_PATTERN = java.util.regex.Pattern.compile("\\S+");

    private static final android.util.Property<android.support.v17.leanback.widget.StreamingTextView, java.lang.Integer> STREAM_POSITION_PROPERTY = new android.util.Property<android.support.v17.leanback.widget.StreamingTextView, java.lang.Integer>(java.lang.Integer.class, "streamPosition") {
        @java.lang.Override
        public java.lang.Integer get(android.support.v17.leanback.widget.StreamingTextView view) {
            return view.getStreamPosition();
        }

        @java.lang.Override
        public void set(android.support.v17.leanback.widget.StreamingTextView view, java.lang.Integer value) {
            view.setStreamPosition(value);
        }
    };

    final java.util.Random mRandom = new java.util.Random();

    android.graphics.Bitmap mOneDot;

    android.graphics.Bitmap mTwoDot;

    int mStreamPosition;

    private android.animation.ObjectAnimator mStreamingAnimation;

    public StreamingTextView(android.content.Context context, android.util.AttributeSet attrs) {
        super(context, attrs);
    }

    public StreamingTextView(android.content.Context context, android.util.AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @java.lang.Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mOneDot = getScaledBitmap(R.drawable.lb_text_dot_one, android.support.v17.leanback.widget.StreamingTextView.TEXT_DOT_SCALE);
        mTwoDot = getScaledBitmap(R.drawable.lb_text_dot_two, android.support.v17.leanback.widget.StreamingTextView.TEXT_DOT_SCALE);
        reset();
    }

    private android.graphics.Bitmap getScaledBitmap(int resourceId, float scaled) {
        android.graphics.Bitmap bitmap = android.graphics.BitmapFactory.decodeResource(getResources(), resourceId);
        return android.graphics.Bitmap.createScaledBitmap(bitmap, ((int) (bitmap.getWidth() * scaled)), ((int) (bitmap.getHeight() * scaled)), false);
    }

    /**
     * Resets the text view.
     */
    public void reset() {
        if (android.support.v17.leanback.widget.StreamingTextView.DEBUG)
            android.util.Log.d(android.support.v17.leanback.widget.StreamingTextView.TAG, "#reset");

        mStreamPosition = -1;
        cancelStreamAnimation();
        setText("");
    }

    /**
     * Updates the recognized text.
     */
    public void updateRecognizedText(java.lang.String stableText, java.lang.String pendingText) {
        if (android.support.v17.leanback.widget.StreamingTextView.DEBUG)
            android.util.Log.d(android.support.v17.leanback.widget.StreamingTextView.TAG, ((("updateText(" + stableText) + ",") + pendingText) + ")");

        if (stableText == null) {
            stableText = "";
        }
        android.text.SpannableStringBuilder displayText = new android.text.SpannableStringBuilder(stableText);
        if (android.support.v17.leanback.widget.StreamingTextView.DOTS_FOR_STABLE) {
            addDottySpans(displayText, stableText, 0);
        }
        if (pendingText != null) {
            int pendingTextStart = displayText.length();
            displayText.append(pendingText);
            if (android.support.v17.leanback.widget.StreamingTextView.DOTS_FOR_PENDING) {
                addDottySpans(displayText, pendingText, pendingTextStart);
            } else {
                int pendingColor = getResources().getColor(R.color.lb_search_plate_hint_text_color);
                addColorSpan(displayText, pendingColor, pendingText, pendingTextStart);
            }
        }
        // Start streaming in dots from beginning of partials, or current position,
        // whichever is larger
        mStreamPosition = java.lang.Math.max(stableText.length(), mStreamPosition);
        // Copy the text and spans to a SpannedString, since editable text
        // doesn't redraw in invalidate() when hardware accelerated
        // if the text or spans haven't changed. (probably a framework bug)
        updateText(new android.text.SpannedString(displayText));
        if (android.support.v17.leanback.widget.StreamingTextView.ANIMATE_DOTS_FOR_PENDING) {
            startStreamAnimation();
        }
    }

    int getStreamPosition() {
        return mStreamPosition;
    }

    void setStreamPosition(int streamPosition) {
        mStreamPosition = streamPosition;
        invalidate();
    }

    private void startStreamAnimation() {
        cancelStreamAnimation();
        int pos = getStreamPosition();
        int totalLen = length();
        int animLen = totalLen - pos;
        if (animLen > 0) {
            if (mStreamingAnimation == null) {
                mStreamingAnimation = new android.animation.ObjectAnimator();
                mStreamingAnimation.setTarget(this);
                mStreamingAnimation.setProperty(android.support.v17.leanback.widget.StreamingTextView.STREAM_POSITION_PROPERTY);
            }
            mStreamingAnimation.setIntValues(pos, totalLen);
            mStreamingAnimation.setDuration(android.support.v17.leanback.widget.StreamingTextView.STREAM_UPDATE_DELAY_MILLIS * animLen);
            mStreamingAnimation.start();
        }
    }

    private void cancelStreamAnimation() {
        if (mStreamingAnimation != null) {
            mStreamingAnimation.cancel();
        }
    }

    private void addDottySpans(android.text.SpannableStringBuilder displayText, java.lang.String text, int textStart) {
        java.util.regex.Matcher m = android.support.v17.leanback.widget.StreamingTextView.SPLIT_PATTERN.matcher(text);
        while (m.find()) {
            int wordStart = textStart + m.start();
            int wordEnd = textStart + m.end();
            android.support.v17.leanback.widget.StreamingTextView.DottySpan span = new android.support.v17.leanback.widget.StreamingTextView.DottySpan(text.charAt(m.start()), wordStart);
            displayText.setSpan(span, wordStart, wordEnd, android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        } 
    }

    private void addColorSpan(android.text.SpannableStringBuilder displayText, int color, java.lang.String text, int textStart) {
        android.text.style.ForegroundColorSpan span = new android.text.style.ForegroundColorSpan(color);
        int start = textStart;
        int end = textStart + text.length();
        displayText.setSpan(span, start, end, android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    /**
     * Sets the final, non changing, full text result. This should only happen at the very end of
     * a recognition.
     *
     * @param finalText
     * 		to the view to.
     */
    public void setFinalRecognizedText(java.lang.CharSequence finalText) {
        if (android.support.v17.leanback.widget.StreamingTextView.DEBUG)
            android.util.Log.d(android.support.v17.leanback.widget.StreamingTextView.TAG, ("setFinalRecognizedText(" + finalText) + ")");

        updateText(finalText);
    }

    private void updateText(java.lang.CharSequence displayText) {
        setText(displayText);
        bringPointIntoView(length());
    }

    /**
     * This is required to make the View findable by uiautomator.
     */
    @java.lang.Override
    public void onInitializeAccessibilityNodeInfo(android.view.accessibility.AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfo(info);
        info.setClassName(android.support.v17.leanback.widget.StreamingTextView.class.getCanonicalName());
    }

    private class DottySpan extends android.text.style.ReplacementSpan {
        private final int mSeed;

        private final int mPosition;

        public DottySpan(int seed, int pos) {
            mSeed = seed;
            mPosition = pos;
        }

        @java.lang.Override
        public void draw(android.graphics.Canvas canvas, java.lang.CharSequence text, int start, int end, float x, int top, int y, int bottom, android.graphics.Paint paint) {
            int width = ((int) (paint.measureText(text, start, end)));
            int dotWidth = mOneDot.getWidth();
            int sliceWidth = 2 * dotWidth;
            int sliceCount = width / sliceWidth;
            int excess = width % sliceWidth;
            int prop = excess / 2;
            boolean rtl = android.support.v17.leanback.widget.StreamingTextView.isLayoutRtl(android.support.v17.leanback.widget.StreamingTextView.this);
            mRandom.setSeed(mSeed);
            int oldAlpha = paint.getAlpha();
            for (int i = 0; i < sliceCount; i++) {
                if (android.support.v17.leanback.widget.StreamingTextView.ANIMATE_DOTS_FOR_PENDING) {
                    if ((mPosition + i) >= mStreamPosition)
                        break;

                }
                float left = ((i * sliceWidth) + prop) + (dotWidth / 2);
                float dotLeft = (rtl) ? ((x + width) - left) - dotWidth : x + left;
                // give the dots some visual variety
                paint.setAlpha((mRandom.nextInt(4) + 1) * 63);
                if (mRandom.nextBoolean()) {
                    canvas.drawBitmap(mTwoDot, dotLeft, y - mTwoDot.getHeight(), paint);
                } else {
                    canvas.drawBitmap(mOneDot, dotLeft, y - mOneDot.getHeight(), paint);
                }
            }
            paint.setAlpha(oldAlpha);
        }

        @java.lang.Override
        public int getSize(android.graphics.Paint paint, java.lang.CharSequence text, int start, int end, android.graphics.Paint.FontMetricsInt fontMetricsInt) {
            return ((int) (paint.measureText(text, start, end)));
        }
    }

    public static boolean isLayoutRtl(android.view.View view) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return android.view.View.LAYOUT_DIRECTION_RTL == view.getLayoutDirection();
        } else {
            return false;
        }
    }

    public void updateRecognizedText(java.lang.String stableText, java.util.List<java.lang.Float> rmsValues) {
    }
}

