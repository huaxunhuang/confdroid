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
package android.graphics.drawable.shapes;


/**
 * Defines an oval shape.
 * <p>
 * The oval can be drawn to a Canvas with its own draw() method,
 * but more graphical control is available if you instead pass
 * the OvalShape to a {@link android.graphics.drawable.ShapeDrawable}.
 */
public class OvalShape extends android.graphics.drawable.shapes.RectShape {
    public OvalShape() {
    }

    @java.lang.Override
    public void draw(android.graphics.Canvas canvas, android.graphics.Paint paint) {
        canvas.drawOval(rect(), paint);
    }

    @java.lang.Override
    public void getOutline(android.graphics.Outline outline) {
        final android.graphics.RectF rect = rect();
        outline.setOval(((int) (java.lang.Math.ceil(rect.left))), ((int) (java.lang.Math.ceil(rect.top))), ((int) (java.lang.Math.floor(rect.right))), ((int) (java.lang.Math.floor(rect.bottom))));
    }

    @java.lang.Override
    public android.graphics.drawable.shapes.OvalShape clone() throws java.lang.CloneNotSupportedException {
        return ((android.graphics.drawable.shapes.OvalShape) (super.clone()));
    }
}

