/**
 * Copyright (C) 2014 The Android Open Source Project
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
package android.support.v7.widget;


class CardViewJellybeanMr1 extends android.support.v7.widget.CardViewGingerbread {
    @java.lang.Override
    public void initStatic() {
        android.support.v7.widget.RoundRectDrawableWithShadow.sRoundRectHelper = new android.support.v7.widget.RoundRectDrawableWithShadow.RoundRectHelper() {
            @java.lang.Override
            public void drawRoundRect(android.graphics.Canvas canvas, android.graphics.RectF bounds, float cornerRadius, android.graphics.Paint paint) {
                canvas.drawRoundRect(bounds, cornerRadius, cornerRadius, paint);
            }
        };
    }
}

