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
package android.view;


/**
 * This class exists temporarily to workaround broken apps
 *
 * b/119066174
 *
 * @unknown 
 */
public abstract class DisplayListCanvas extends android.graphics.BaseRecordingCanvas {
    /**
     *
     *
     * @unknown 
     */
    protected DisplayListCanvas(long nativeCanvas) {
        super(nativeCanvas);
    }

    /**
     * TODO: Public API alternative
     *
     * @unknown 
     */
    @android.annotation.UnsupportedAppUsage
    public abstract void drawRoundRect(android.graphics.CanvasProperty<java.lang.Float> left, android.graphics.CanvasProperty<java.lang.Float> top, android.graphics.CanvasProperty<java.lang.Float> right, android.graphics.CanvasProperty<java.lang.Float> bottom, android.graphics.CanvasProperty<java.lang.Float> rx, android.graphics.CanvasProperty<java.lang.Float> ry, android.graphics.CanvasProperty<android.graphics.Paint> paint);

    /**
     * TODO: Public API alternative
     *
     * @unknown 
     */
    @android.annotation.UnsupportedAppUsage
    public abstract void drawCircle(android.graphics.CanvasProperty<java.lang.Float> cx, android.graphics.CanvasProperty<java.lang.Float> cy, android.graphics.CanvasProperty<java.lang.Float> radius, android.graphics.CanvasProperty<android.graphics.Paint> paint);
}

