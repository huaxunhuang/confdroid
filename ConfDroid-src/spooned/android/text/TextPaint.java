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
package android.text;


/**
 * TextPaint is an extension of Paint that leaves room for some extra
 * data used during text measuring and drawing.
 */
public class TextPaint extends android.graphics.Paint {
    // Special value 0 means no background paint
    @android.annotation.ColorInt
    public int bgColor;

    public int baselineShift;

    @android.annotation.ColorInt
    public int linkColor;

    public int[] drawableState;

    public float density = 1.0F;

    /**
     * Special value 0 means no custom underline
     *
     * @unknown 
     */
    @android.annotation.ColorInt
    public int underlineColor = 0;

    /**
     * Defined as a multiplier of the default underline thickness. Use 1.0f for default thickness.
     *
     * @unknown 
     */
    public float underlineThickness;

    public TextPaint() {
        super();
    }

    public TextPaint(int flags) {
        super(flags);
    }

    public TextPaint(android.graphics.Paint p) {
        super(p);
    }

    /**
     * Copy the fields from tp into this TextPaint, including the
     * fields inherited from Paint.
     */
    public void set(android.text.TextPaint tp) {
        super.set(tp);
        bgColor = tp.bgColor;
        baselineShift = tp.baselineShift;
        linkColor = tp.linkColor;
        drawableState = tp.drawableState;
        density = tp.density;
        underlineColor = tp.underlineColor;
        underlineThickness = tp.underlineThickness;
    }

    /**
     * Defines a custom underline for this Paint.
     *
     * @param color
     * 		underline solid color
     * @param thickness
     * 		underline thickness
     * @unknown 
     */
    public void setUnderlineText(int color, float thickness) {
        underlineColor = color;
        underlineThickness = thickness;
    }
}

