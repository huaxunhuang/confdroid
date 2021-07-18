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
 * Delegate implementing the native methods of android.graphics.DrawFilter
 *
 * Through the layoutlib_create tool, the original native methods of DrawFilter have been replaced
 * by calls to methods of the same name in this delegate class.
 *
 * This class behaves like the original native implementation, but in Java, keeping previously
 * native data into its own objects and mapping them to int that are sent back and forth between
 * it and the original DrawFilter class.
 *
 * This also serve as a base class for all DrawFilter delegate classes.
 *
 * @see DelegateManager
 */
// ---- Private delegate/helper methods ----
public abstract class DrawFilter_Delegate {
    // ---- delegate manager ----
    protected static final com.android.layoutlib.bridge.impl.DelegateManager<android.graphics.DrawFilter_Delegate> sManager = new com.android.layoutlib.bridge.impl.DelegateManager<android.graphics.DrawFilter_Delegate>(android.graphics.DrawFilter_Delegate.class);

    // ---- delegate helper data ----
    // ---- delegate data ----
    // ---- Public Helper methods ----
    public static android.graphics.DrawFilter_Delegate getDelegate(long nativeDrawFilter) {
        return android.graphics.DrawFilter_Delegate.sManager.getDelegate(nativeDrawFilter);
    }

    public abstract boolean isSupported();

    public abstract java.lang.String getSupportMessage();

    // ---- native methods ----
    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nativeDestructor(long nativeDrawFilter) {
        android.graphics.DrawFilter_Delegate.sManager.removeJavaReferenceFor(nativeDrawFilter);
    }
}

