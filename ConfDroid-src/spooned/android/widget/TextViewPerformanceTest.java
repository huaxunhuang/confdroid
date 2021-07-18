/**
 * Copyright (C) 2006 The Android Open Source Project
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
package android.widget;


public class TextViewPerformanceTest extends android.test.AndroidTestCase {
    private java.lang.String mString = "The quick brown fox";

    private android.graphics.Canvas mCanvas;

    private android.widget.TextViewPerformanceTest.PerformanceTextView mTextView;

    private android.graphics.Paint mPaint;

    private android.widget.TextViewPerformanceTest.PerformanceLabelView mLabelView;

    @java.lang.Override
    protected void setUp() throws java.lang.Exception {
        setUp();
        android.graphics.Bitmap mBitmap = android.graphics.Bitmap.createBitmap(320, 240, android.graphics.Bitmap.Config.RGB_565);
        mCanvas = new android.graphics.Canvas(mBitmap);
        android.view.ViewGroup.LayoutParams p = new android.view.ViewGroup.LayoutParams(320, 240);
        mLabelView = new android.widget.TextViewPerformanceTest.PerformanceLabelView(mContext);
        mLabelView.setText(mString);
        mLabelView.measure(android.view.View.MeasureSpec.AT_MOST | 320, android.view.View.MeasureSpec.AT_MOST | 240);
        mLabelView.mySetFrame(320, 240);
        mLabelView.setLayoutParams(p);
        mLabelView.myDraw(mCanvas);
        mPaint = new android.graphics.Paint();
        mCanvas.save();
        mTextView = new android.widget.TextViewPerformanceTest.PerformanceTextView(mContext);
        mTextView.setLayoutParams(p);
        mTextView.setText(mString);
        mTextView.mySetFrame(320, 240);
        mTextView.measure(android.view.View.MeasureSpec.AT_MOST | 320, android.view.View.MeasureSpec.AT_MOST | 240);
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testDrawTextViewLine() throws java.lang.Exception {
        mTextView.myDraw(mCanvas);
        mTextView.myDraw(mCanvas);
        mTextView.myDraw(mCanvas);
        mTextView.myDraw(mCanvas);
        mTextView.myDraw(mCanvas);
        mTextView.myDraw(mCanvas);
        mTextView.myDraw(mCanvas);
        mTextView.myDraw(mCanvas);
        mTextView.myDraw(mCanvas);
        mTextView.myDraw(mCanvas);
    }

    @android.test.suitebuilder.annotation.SmallTest
    public void testSpan() throws java.lang.Exception {
        java.lang.CharSequence charSeq = new android.text.SpannedString(mString);
        mTextView.setText(charSeq);
        mTextView.myDraw(mCanvas);
        mTextView.myDraw(mCanvas);
        mTextView.myDraw(mCanvas);
        mTextView.myDraw(mCanvas);
        mTextView.myDraw(mCanvas);
        mTextView.myDraw(mCanvas);
        mTextView.myDraw(mCanvas);
        mTextView.myDraw(mCanvas);
        mTextView.myDraw(mCanvas);
        mTextView.myDraw(mCanvas);
    }

    @android.test.suitebuilder.annotation.SmallTest
    public void testCanvasDrawText() throws java.lang.Exception {
        mCanvas.drawText(mString, 30, 30, mPaint);
    }

    @android.test.suitebuilder.annotation.SmallTest
    public void testLabelViewDraw() throws java.lang.Exception {
        mLabelView.myDraw(mCanvas);
    }

    private class PerformanceTextView extends android.widget.TextView {
        public PerformanceTextView(android.content.Context context) {
            super(context);
        }

        final void myDraw(android.graphics.Canvas c) {
            super.onDraw(c);
        }

        final void mySetFrame(int w, int h) {
            super.setFrame(0, 0, w, h);
        }
    }

    private class PerformanceLabelView extends android.widget.LabelView {
        public PerformanceLabelView(android.content.Context context) {
            super(context);
        }

        final void myDraw(android.graphics.Canvas c) {
            super.onDraw(c);
        }

        final void mySetFrame(int w, int h) {
            super.setFrame(0, 0, w, h);
        }
    }
}

