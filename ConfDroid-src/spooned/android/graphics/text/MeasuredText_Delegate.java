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
package android.graphics.text;


/**
 * Delegate that provides implementation for native methods in
 * {@link android.graphics.text.MeasuredText}
 * <p/>
 * Through the layoutlib_create tool, selected methods of StaticLayout have been replaced
 * by calls to methods of the same name in this delegate class.
 */
public class MeasuredText_Delegate {
    // ---- Builder delegate manager ----
    protected static final com.android.layoutlib.bridge.impl.DelegateManager<android.graphics.text.MeasuredText_Delegate> sManager = new com.android.layoutlib.bridge.impl.DelegateManager(android.graphics.text.MeasuredText_Delegate.class);

    private static long sFinalizer = -1;

    protected long mNativeBuilderPtr;

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static float nGetWidth(long nativePtr, int start, int end) {
        // Ignore as it is not used for the layoutlib implementation
        return 0.0F;
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static long nGetReleaseFunc() {
        synchronized(android.graphics.text.MeasuredText_Delegate.class) {
            if (android.graphics.text.MeasuredText_Delegate.sFinalizer == (-1)) {
                android.graphics.text.MeasuredText_Delegate.sFinalizer = libcore.util.NativeAllocationRegistry_Delegate.createFinalizer(android.graphics.text.MeasuredText_Delegate.sManager::removeJavaReferenceFor);
            }
        }
        return android.graphics.text.MeasuredText_Delegate.sFinalizer;
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static int nGetMemoryUsage(long nativePtr) {
        // Ignore as it is not used for the layoutlib implementation
        return 0;
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nGetBounds(long nativePtr, char[] buf, int start, int end, android.graphics.Rect rect) {
        // Ignore as it is not used for the layoutlib implementation
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static float nGetCharWidthAt(long nativePtr, int offset) {
        // Ignore as it is not used for the layoutlib implementation
        return 0.0F;
    }

    public static void computeRuns(long measuredTextPtr, android.graphics.text.LineBreaker_Delegate.Builder staticLayoutBuilder) {
        android.graphics.text.MeasuredText_Delegate delegate = android.graphics.text.MeasuredText_Delegate.sManager.getDelegate(measuredTextPtr);
        if (delegate == null) {
            return;
        }
        android.graphics.text.MeasuredText_Builder_Delegate builder = android.graphics.text.MeasuredText_Builder_Delegate.sBuilderManager.getDelegate(delegate.mNativeBuilderPtr);
        if (builder == null) {
            return;
        }
        for (android.graphics.text.LineBreaker_Delegate.Run run : builder.mRuns) {
            run.addTo(staticLayoutBuilder);
        }
    }
}

