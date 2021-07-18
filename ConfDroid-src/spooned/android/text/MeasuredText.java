/**
 * Copyright (C) 2010 The Android Open Source Project
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
package android.text;


/**
 *
 *
 * @unknown 
 */
class MeasuredText {
    private static final boolean localLOGV = false;

    java.lang.CharSequence mText;

    int mTextStart;

    float[] mWidths;

    char[] mChars;

    byte[] mLevels;

    int mDir;

    boolean mEasy;

    int mLen;

    private int mPos;

    private android.text.TextPaint mWorkPaint;

    private android.text.StaticLayout.Builder mBuilder;

    private MeasuredText() {
        mWorkPaint = new android.text.TextPaint();
    }

    private static final java.lang.Object[] sLock = new java.lang.Object[0];

    private static final android.text.MeasuredText[] sCached = new android.text.MeasuredText[3];

    static android.text.MeasuredText obtain() {
        android.text.MeasuredText mt;
        synchronized(android.text.MeasuredText.sLock) {
            for (int i = android.text.MeasuredText.sCached.length; (--i) >= 0;) {
                if (android.text.MeasuredText.sCached[i] != null) {
                    mt = android.text.MeasuredText.sCached[i];
                    android.text.MeasuredText.sCached[i] = null;
                    return mt;
                }
            }
        }
        mt = new android.text.MeasuredText();
        if (android.text.MeasuredText.localLOGV) {
            android.util.Log.v("MEAS", "new: " + mt);
        }
        return mt;
    }

    static android.text.MeasuredText recycle(android.text.MeasuredText mt) {
        mt.finish();
        synchronized(android.text.MeasuredText.sLock) {
            for (int i = 0; i < android.text.MeasuredText.sCached.length; ++i) {
                if (android.text.MeasuredText.sCached[i] == null) {
                    android.text.MeasuredText.sCached[i] = mt;
                    mt.mText = null;
                    break;
                }
            }
        }
        return null;
    }

    void finish() {
        mText = null;
        mBuilder = null;
        if (mLen > 1000) {
            mWidths = null;
            mChars = null;
            mLevels = null;
        }
    }

    void setPos(int pos) {
        mPos = pos - mTextStart;
    }

    /**
     * Analyzes text for bidirectional runs.  Allocates working buffers.
     */
    void setPara(java.lang.CharSequence text, int start, int end, android.text.TextDirectionHeuristic textDir, android.text.StaticLayout.Builder builder) {
        mBuilder = builder;
        mText = text;
        mTextStart = start;
        int len = end - start;
        mLen = len;
        mPos = 0;
        if ((mWidths == null) || (mWidths.length < len)) {
            mWidths = com.android.internal.util.ArrayUtils.newUnpaddedFloatArray(len);
        }
        if ((mChars == null) || (mChars.length < len)) {
            mChars = com.android.internal.util.ArrayUtils.newUnpaddedCharArray(len);
        }
        android.text.TextUtils.getChars(text, start, end, mChars, 0);
        if (text instanceof android.text.Spanned) {
            android.text.Spanned spanned = ((android.text.Spanned) (text));
            android.text.style.ReplacementSpan[] spans = spanned.getSpans(start, end, android.text.style.ReplacementSpan.class);
            for (int i = 0; i < spans.length; i++) {
                int startInPara = spanned.getSpanStart(spans[i]) - start;
                int endInPara = spanned.getSpanEnd(spans[i]) - start;
                // The span interval may be larger and must be restricted to [start, end[
                if (startInPara < 0)
                    startInPara = 0;

                if (endInPara > len)
                    endInPara = len;

                for (int j = startInPara; j < endInPara; j++) {
                    mChars[j] = '\ufffc';// object replacement character

                }
            }
        }
        if ((((textDir == android.text.TextDirectionHeuristics.LTR) || (textDir == android.text.TextDirectionHeuristics.FIRSTSTRONG_LTR)) || (textDir == android.text.TextDirectionHeuristics.ANYRTL_LTR)) && android.text.TextUtils.doesNotNeedBidi(mChars, 0, len)) {
            mDir = android.text.Layout.DIR_LEFT_TO_RIGHT;
            mEasy = true;
        } else {
            if ((mLevels == null) || (mLevels.length < len)) {
                mLevels = com.android.internal.util.ArrayUtils.newUnpaddedByteArray(len);
            }
            int bidiRequest;
            if (textDir == android.text.TextDirectionHeuristics.LTR) {
                bidiRequest = android.text.Layout.DIR_REQUEST_LTR;
            } else
                if (textDir == android.text.TextDirectionHeuristics.RTL) {
                    bidiRequest = android.text.Layout.DIR_REQUEST_RTL;
                } else
                    if (textDir == android.text.TextDirectionHeuristics.FIRSTSTRONG_LTR) {
                        bidiRequest = android.text.Layout.DIR_REQUEST_DEFAULT_LTR;
                    } else
                        if (textDir == android.text.TextDirectionHeuristics.FIRSTSTRONG_RTL) {
                            bidiRequest = android.text.Layout.DIR_REQUEST_DEFAULT_RTL;
                        } else {
                            boolean isRtl = textDir.isRtl(mChars, 0, len);
                            bidiRequest = (isRtl) ? android.text.Layout.DIR_REQUEST_RTL : android.text.Layout.DIR_REQUEST_LTR;
                        }



            mDir = android.text.AndroidBidi.bidi(bidiRequest, mChars, mLevels, len, false);
            mEasy = false;
        }
    }

    float addStyleRun(android.text.TextPaint paint, int len, android.graphics.Paint.FontMetricsInt fm) {
        if (fm != null) {
            paint.getFontMetricsInt(fm);
        }
        int p = mPos;
        mPos = p + len;
        // try to do widths measurement in native code, but use Java if paint has been subclassed
        // FIXME: may want to eliminate special case for subclass
        float[] widths = null;
        if ((mBuilder == null) || (paint.getClass() != android.text.TextPaint.class)) {
            widths = mWidths;
        }
        if (mEasy) {
            boolean isRtl = mDir != android.text.Layout.DIR_LEFT_TO_RIGHT;
            float width = 0;
            if (widths != null) {
                width = paint.getTextRunAdvances(mChars, p, len, p, len, isRtl, widths, p);
                if (mBuilder != null) {
                    mBuilder.addMeasuredRun(p, p + len, widths);
                }
            } else {
                width = mBuilder.addStyleRun(paint, p, p + len, isRtl);
            }
            return width;
        }
        float totalAdvance = 0;
        int level = mLevels[p];
        for (int q = p, i = p + 1, e = p + len; ; ++i) {
            if ((i == e) || (mLevels[i] != level)) {
                boolean isRtl = (level & 0x1) != 0;
                if (widths != null) {
                    totalAdvance += paint.getTextRunAdvances(mChars, q, i - q, q, i - q, isRtl, widths, q);
                    if (mBuilder != null) {
                        mBuilder.addMeasuredRun(q, i, widths);
                    }
                } else {
                    totalAdvance += mBuilder.addStyleRun(paint, q, i, isRtl);
                }
                if (i == e) {
                    break;
                }
                q = i;
                level = mLevels[i];
            }
        }
        return totalAdvance;
    }

    float addStyleRun(android.text.TextPaint paint, android.text.style.MetricAffectingSpan[] spans, int len, android.graphics.Paint.FontMetricsInt fm) {
        android.text.TextPaint workPaint = mWorkPaint;
        workPaint.set(paint);
        // XXX paint should not have a baseline shift, but...
        workPaint.baselineShift = 0;
        android.text.style.ReplacementSpan replacement = null;
        for (int i = 0; i < spans.length; i++) {
            android.text.style.MetricAffectingSpan span = spans[i];
            if (span instanceof android.text.style.ReplacementSpan) {
                replacement = ((android.text.style.ReplacementSpan) (span));
            } else {
                span.updateMeasureState(workPaint);
            }
        }
        float wid;
        if (replacement == null) {
            wid = addStyleRun(workPaint, len, fm);
        } else {
            // Use original text.  Shouldn't matter.
            wid = replacement.getSize(workPaint, mText, mTextStart + mPos, (mTextStart + mPos) + len, fm);
            if (mBuilder == null) {
                float[] w = mWidths;
                w[mPos] = wid;
                for (int i = mPos + 1, e = mPos + len; i < e; i++)
                    w[i] = 0;

            } else {
                mBuilder.addReplacementRun(mPos, mPos + len, wid);
            }
            mPos += len;
        }
        if (fm != null) {
            if (workPaint.baselineShift < 0) {
                fm.ascent += workPaint.baselineShift;
                fm.top += workPaint.baselineShift;
            } else {
                fm.descent += workPaint.baselineShift;
                fm.bottom += workPaint.baselineShift;
            }
        }
        return wid;
    }

    int breakText(int limit, boolean forwards, float width) {
        float[] w = mWidths;
        if (forwards) {
            int i = 0;
            while (i < limit) {
                width -= w[i];
                if (width < 0.0F)
                    break;

                i++;
            } 
            while ((i > 0) && (mChars[i - 1] == ' '))
                i--;

            return i;
        } else {
            int i = limit - 1;
            while (i >= 0) {
                width -= w[i];
                if (width < 0.0F)
                    break;

                i--;
            } 
            while ((i < (limit - 1)) && (mChars[i + 1] == ' '))
                i++;

            return (limit - i) - 1;
        }
    }

    float measure(int start, int limit) {
        float width = 0;
        float[] w = mWidths;
        for (int i = start; i < limit; ++i) {
            width += w[i];
        }
        return width;
    }
}

