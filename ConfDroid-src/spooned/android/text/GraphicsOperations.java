/**
 * Copyright (C) 2008 The Android Open Source Project
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
 * Please implement this interface if your CharSequence can do quick
 * draw/measure/widths calculations from an internal array.
 * {@hide }
 */
public interface GraphicsOperations extends java.lang.CharSequence {
    /**
     * Just like {@link Canvas#drawText}.
     */
    void drawText(android.graphics.Canvas c, int start, int end, float x, float y, android.graphics.Paint p);

    /**
     * Just like {@link Canvas#drawTextRun}.
     * {@hide }
     */
    void drawTextRun(android.graphics.Canvas c, int start, int end, int contextStart, int contextEnd, float x, float y, boolean isRtl, android.graphics.Paint p);

    /**
     * Just like {@link Paint#measureText}.
     */
    float measureText(int start, int end, android.graphics.Paint p);

    /**
     * Just like {@link Paint#getTextWidths}.
     */
    public int getTextWidths(int start, int end, float[] widths, android.graphics.Paint p);

    /**
     * Just like {@link Paint#getTextRunAdvances}.
     *
     * @unknown 
     */
    float getTextRunAdvances(int start, int end, int contextStart, int contextEnd, boolean isRtl, float[] advances, int advancesIndex, android.graphics.Paint paint);

    /**
     * Just like {@link Paint#getTextRunCursor}.
     *
     * @unknown 
     */
    int getTextRunCursor(int contextStart, int contextEnd, int dir, int offset, int cursorOpt, android.graphics.Paint p);
}

