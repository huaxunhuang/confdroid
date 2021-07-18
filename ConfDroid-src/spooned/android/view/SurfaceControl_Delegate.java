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


public class SurfaceControl_Delegate {
    // ---- delegate manager ----
    private static final com.android.layoutlib.bridge.impl.DelegateManager<android.view.SurfaceControl_Delegate> sManager = new com.android.layoutlib.bridge.impl.DelegateManager(android.view.SurfaceControl_Delegate.class);

    private static long sFinalizer = -1;

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static long nativeCreateTransaction() {
        return android.view.SurfaceControl_Delegate.sManager.addNewDelegate(new android.view.SurfaceControl_Delegate());
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static long nativeGetNativeTransactionFinalizer() {
        synchronized(android.view.SurfaceControl_Delegate.class) {
            if (android.view.SurfaceControl_Delegate.sFinalizer == (-1)) {
                android.view.SurfaceControl_Delegate.sFinalizer = libcore.util.NativeAllocationRegistry_Delegate.createFinalizer(android.view.SurfaceControl_Delegate.sManager::removeJavaReferenceFor);
            }
        }
        return android.view.SurfaceControl_Delegate.sFinalizer;
    }
}

