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
package android.graphics;


/**
 * TODO: Make public?
 *
 * @unknown 
 */
public final class CanvasProperty<T> {
    private com.android.internal.util.VirtualRefBasePtr mProperty;

    @android.annotation.UnsupportedAppUsage
    public static android.graphics.CanvasProperty<java.lang.Float> createFloat(float initialValue) {
        return new android.graphics.CanvasProperty<java.lang.Float>(android.graphics.CanvasProperty.nCreateFloat(initialValue));
    }

    @android.annotation.UnsupportedAppUsage
    public static android.graphics.CanvasProperty<android.graphics.Paint> createPaint(android.graphics.Paint initialValue) {
        return new android.graphics.CanvasProperty<android.graphics.Paint>(android.graphics.CanvasProperty.nCreatePaint(initialValue.getNativeInstance()));
    }

    private CanvasProperty(long nativeContainer) {
        mProperty = new com.android.internal.util.VirtualRefBasePtr(nativeContainer);
    }

    /**
     *
     *
     * @unknown 
     */
    public long getNativeContainer() {
        return mProperty.get();
    }

    private static native long nCreateFloat(float initialValue);

    private static native long nCreatePaint(long initialValuePaintPtr);
}

