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
 * Delegate implementing the native methods of android.graphics.ComposePathEffect
 *
 * Through the layoutlib_create tool, the original native methods of ComposePathEffect have been
 * replaced by calls to methods of the same name in this delegate class.
 *
 * This class behaves like the original native implementation, but in Java, keeping previously
 * native data into its own objects and mapping them to int that are sent back and forth between
 * it and the original ComposePathEffect class.
 *
 * Because this extends {@link PathEffect_Delegate}, there's no need to use a {@link DelegateManager},
 * as all the Shader classes will be added to the manager owned by {@link PathEffect_Delegate}.
 *
 * @see PathEffect_Delegate
 */
// ---- Private delegate/helper methods ----
public class ComposePathEffect_Delegate extends android.graphics.PathEffect_Delegate {
    // ---- delegate data ----
    // ---- Public Helper methods ----
    @java.lang.Override
    public java.awt.Stroke getStroke(android.graphics.Paint_Delegate paint) {
        // FIXME
        return null;
    }

    @java.lang.Override
    public boolean isSupported() {
        return false;
    }

    @java.lang.Override
    public java.lang.String getSupportMessage() {
        return "Compose Path Effects are not supported in Layout Preview mode.";
    }

    // ---- native methods ----
    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static long nativeCreate(long outerpe, long innerpe) {
        android.graphics.ComposePathEffect_Delegate newDelegate = new android.graphics.ComposePathEffect_Delegate();
        return android.graphics.PathEffect_Delegate.sManager.addNewDelegate(newDelegate);
    }
}

