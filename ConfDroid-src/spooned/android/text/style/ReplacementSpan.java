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
package android.text.style;


public abstract class ReplacementSpan extends android.text.style.MetricAffectingSpan {
    /**
     * Returns the width of the span. Extending classes can set the height of the span by updating
     * attributes of {@link android.graphics.Paint.FontMetricsInt}. If the span covers the whole
     * text, and the height is not set,
     * {@link #draw(Canvas, CharSequence, int, int, float, int, int, int, Paint)} will not be
     * called for the span.
     *
     * @param paint
     * 		Paint instance.
     * @param text
     * 		Current text.
     * @param start
     * 		Start character index for span.
     * @param end
     * 		End character index for span.
     * @param fm
     * 		Font metrics, can be null.
     * @return Width of the span.
     */
    public abstract int getSize(@android.annotation.NonNull
    android.graphics.Paint paint, java.lang.CharSequence text, @android.annotation.IntRange(from = 0)
    int start, @android.annotation.IntRange(from = 0)
    int end, @android.annotation.Nullable
    android.graphics.Paint.FontMetricsInt fm);

    /**
     * Draws the span into the canvas.
     *
     * @param canvas
     * 		Canvas into which the span should be rendered.
     * @param text
     * 		Current text.
     * @param start
     * 		Start character index for span.
     * @param end
     * 		End character index for span.
     * @param x
     * 		Edge of the replacement closest to the leading margin.
     * @param top
     * 		Top of the line.
     * @param y
     * 		Baseline.
     * @param bottom
     * 		Bottom of the line.
     * @param paint
     * 		Paint instance.
     */
    public abstract void draw(@android.annotation.NonNull
    android.graphics.Canvas canvas, java.lang.CharSequence text, @android.annotation.IntRange(from = 0)
    int start, @android.annotation.IntRange(from = 0)
    int end, float x, int top, int y, int bottom, @android.annotation.NonNull
    android.graphics.Paint paint);

    /**
     * This method does nothing, since ReplacementSpans are measured
     * explicitly instead of affecting Paint properties.
     */
    public void updateMeasureState(android.text.TextPaint p) {
    }

    /**
     * This method does nothing, since ReplacementSpans are drawn
     * explicitly instead of affecting Paint properties.
     */
    public void updateDrawState(android.text.TextPaint ds) {
    }
}

