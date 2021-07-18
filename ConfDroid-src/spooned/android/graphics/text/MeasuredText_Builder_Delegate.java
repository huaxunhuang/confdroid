/**
 * Copyright (C) 2018 The Android Open Source Project
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
package android.graphics.text;


/**
 * Delegate that provides implementation for native methods in
 * {@link android.graphics.text.MeasuredText}
 * <p/>
 * Through the layoutlib_create tool, selected methods of StaticLayout have been replaced
 * by calls to methods of the same name in this delegate class.
 */
public class MeasuredText_Builder_Delegate {
    // ---- Builder delegate manager ----
    protected static final com.android.layoutlib.bridge.impl.DelegateManager<android.graphics.text.MeasuredText_Builder_Delegate> sBuilderManager = new com.android.layoutlib.bridge.impl.DelegateManager(android.graphics.text.MeasuredText_Builder_Delegate.class);

    protected final java.util.ArrayList<android.graphics.text.LineBreaker_Delegate.Run> mRuns = new java.util.ArrayList<>();

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static long nInitBuilder() {
        return android.graphics.text.MeasuredText_Builder_Delegate.sBuilderManager.addNewDelegate(new android.graphics.text.MeasuredText_Builder_Delegate());
    }

    /**
     * Apply style to make native measured text.
     *
     * @param nativeBuilderPtr
     * 		The native NativeMeasuredParagraph builder pointer.
     * @param paintPtr
     * 		The native paint pointer to be applied.
     * @param start
     * 		The start offset in the copied buffer.
     * @param end
     * 		The end offset in the copied buffer.
     * @param isRtl
     * 		True if the text is RTL.
     */
    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nAddStyleRun(long nativeBuilderPtr, long paintPtr, int start, int end, boolean isRtl) {
        android.graphics.text.MeasuredText_Builder_Delegate builder = android.graphics.text.MeasuredText_Builder_Delegate.sBuilderManager.getDelegate(nativeBuilderPtr);
        if (builder == null) {
            return;
        }
        builder.mRuns.add(new android.graphics.text.MeasuredText_Builder_Delegate.StyleRun(paintPtr, start, end, isRtl));
    }

    /**
     * Apply ReplacementRun to make native measured text.
     *
     * @param nativeBuilderPtr
     * 		The native NativeMeasuredParagraph builder pointer.
     * @param paintPtr
     * 		The native paint pointer to be applied.
     * @param start
     * 		The start offset in the copied buffer.
     * @param end
     * 		The end offset in the copied buffer.
     * @param width
     * 		The width of the replacement.
     */
    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nAddReplacementRun(long nativeBuilderPtr, long paintPtr, int start, int end, float width) {
        android.graphics.text.MeasuredText_Builder_Delegate builder = android.graphics.text.MeasuredText_Builder_Delegate.sBuilderManager.getDelegate(nativeBuilderPtr);
        if (builder == null) {
            return;
        }
        builder.mRuns.add(new android.graphics.text.MeasuredText_Builder_Delegate.ReplacementRun(start, end, width));
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static long nBuildMeasuredText(long nativeBuilderPtr, long hintMtPtr, @android.annotation.NonNull
    char[] text, boolean computeHyphenation, boolean computeLayout) {
        android.graphics.text.MeasuredText_Delegate delegate = new android.graphics.text.MeasuredText_Delegate();
        delegate.mNativeBuilderPtr = nativeBuilderPtr;
        return android.graphics.text.MeasuredText_Delegate.sManager.addNewDelegate(delegate);
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nFreeBuilder(long nativeBuilderPtr) {
        android.graphics.text.MeasuredText_Builder_Delegate.sBuilderManager.removeJavaReferenceFor(nativeBuilderPtr);
    }

    private static float measureText(long nativePaint, char[] text, int index, int count, float[] widths, int bidiFlags) {
        android.graphics.Paint_Delegate paint = android.graphics.Paint_Delegate.getDelegate(nativePaint);
        android.graphics.RectF bounds = new android.graphics.BidiRenderer(null, paint, text).renderText(index, index + count, bidiFlags, widths, 0, false);
        return bounds.right - bounds.left;
    }

    private static class StyleRun extends android.graphics.text.LineBreaker_Delegate.Run {
        private final long mNativePaint;

        private final boolean mIsRtl;

        private StyleRun(long nativePaint, int start, int end, boolean isRtl) {
            super(start, end);
            mNativePaint = nativePaint;
            mIsRtl = isRtl;
        }

        @java.lang.Override
        void addTo(android.graphics.text.LineBreaker_Delegate.Builder builder) {
            int bidiFlags = (mIsRtl) ? android.graphics.Paint.BIDI_FORCE_RTL : android.graphics.Paint.BIDI_FORCE_LTR;
            android.graphics.text.MeasuredText_Builder_Delegate.measureText(mNativePaint, builder.mText, mStart, mEnd - mStart, builder.mWidths, bidiFlags);
        }
    }

    private static class ReplacementRun extends android.graphics.text.LineBreaker_Delegate.Run {
        private final float mWidth;

        private ReplacementRun(int start, int end, float width) {
            super(start, end);
            mWidth = width;
        }

        @java.lang.Override
        void addTo(android.graphics.text.LineBreaker_Delegate.Builder builder) {
            builder.mWidths[mStart] = mWidth;
            java.util.Arrays.fill(builder.mWidths, mStart + 1, mEnd, 0.0F);
        }
    }
}

