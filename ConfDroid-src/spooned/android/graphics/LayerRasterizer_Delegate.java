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
package android.graphics;


/**
 * Delegate implementing the native methods of android.graphics.LayerRasterizer
 *
 * Through the layoutlib_create tool, the original native methods of LayerRasterizer have
 * been replaced by calls to methods of the same name in this delegate class.
 *
 * This class behaves like the original native implementation, but in Java, keeping previously
 * native data into its own objects and mapping them to int that are sent back and forth between
 * it and the original LayerRasterizer class.
 *
 * Because this extends {@link Rasterizer_Delegate}, there's no need to use a
 * {@link DelegateManager}, as all the Shader classes will be added to the manager
 * owned by {@link Rasterizer_Delegate}.
 *
 * @see Rasterizer_Delegate
 */
// ---- Private delegate/helper methods ----
public class LayerRasterizer_Delegate extends android.graphics.Rasterizer_Delegate {
    // ---- delegate data ----
    // ---- Public Helper methods ----
    @java.lang.Override
    public boolean isSupported() {
        return false;
    }

    @java.lang.Override
    public java.lang.String getSupportMessage() {
        return "Layer Rasterizers are not supported.";
    }

    // ---- native methods ----
    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static long nativeConstructor() {
        android.graphics.LayerRasterizer_Delegate newDelegate = new android.graphics.LayerRasterizer_Delegate();
        return android.graphics.Rasterizer_Delegate.sManager.addNewDelegate(newDelegate);
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nativeAddLayer(long native_layer, long native_paint, float dx, float dy) {
    }
}

