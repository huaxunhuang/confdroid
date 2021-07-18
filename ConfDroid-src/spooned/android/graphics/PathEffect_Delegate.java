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
 * Delegate implementing the native methods of android.graphics.PathEffect
 *
 * Through the layoutlib_create tool, the original native methods of PathEffect have been replaced
 * by calls to methods of the same name in this delegate class.
 *
 * This class behaves like the original native implementation, but in Java, keeping previously
 * native data into its own objects and mapping them to int that are sent back and forth between
 * it and the original PathEffect class.
 *
 * This also serve as a base class for all PathEffect delegate classes.
 *
 * @see DelegateManager
 */
// ---- Private delegate/helper methods ----
public abstract class PathEffect_Delegate {
    // ---- delegate manager ----
    protected static final com.android.layoutlib.bridge.impl.DelegateManager<android.graphics.PathEffect_Delegate> sManager = new com.android.layoutlib.bridge.impl.DelegateManager<android.graphics.PathEffect_Delegate>(android.graphics.PathEffect_Delegate.class);

    // ---- delegate helper data ----
    // ---- delegate data ----
    // ---- Public Helper methods ----
    public static android.graphics.PathEffect_Delegate getDelegate(long nativeShader) {
        return android.graphics.PathEffect_Delegate.sManager.getDelegate(nativeShader);
    }

    public abstract java.awt.Stroke getStroke(android.graphics.Paint_Delegate paint);

    public abstract boolean isSupported();

    public abstract java.lang.String getSupportMessage();

    // ---- native methods ----
    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nativeDestructor(long native_patheffect) {
        android.graphics.PathEffect_Delegate.sManager.removeJavaReferenceFor(native_patheffect);
    }
}

