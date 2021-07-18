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
package android.view;


/**
 * Delegate used to provide new implementation of a select few methods of {@link View}
 *
 * Through the layoutlib_create tool, the original  methods of View have been replaced
 * by calls to methods of the same name in this delegate class.
 */
public class View_Delegate {
    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static boolean isInEditMode(android.view.View thisView) {
        return true;
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static android.os.IBinder getWindowToken(android.view.View thisView) {
        android.content.Context baseContext = com.android.layoutlib.bridge.android.BridgeContext.getBaseContext(thisView.getContext());
        if (baseContext instanceof com.android.layoutlib.bridge.android.BridgeContext) {
            return ((com.android.layoutlib.bridge.android.BridgeContext) (baseContext)).getBinder();
        }
        return null;
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void draw(android.view.View thisView, android.graphics.Canvas canvas) {
        try {
            // This code is run within a catch to prevent misbehaving components from breaking
            // all the layout.
            thisView.draw_Original(canvas);
        } catch (java.lang.Throwable t) {
            com.android.layoutlib.bridge.Bridge.getLog().error(LayoutLog.TAG_BROKEN, "View draw failed", t, null);
        }
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static boolean draw(android.view.View thisView, android.graphics.Canvas canvas, android.view.ViewGroup parent, long drawingTime) {
        try {
            // This code is run within a catch to prevent misbehaving components from breaking
            // all the layout.
            return thisView.draw_Original(canvas, parent, drawingTime);
        } catch (java.lang.Throwable t) {
            com.android.layoutlib.bridge.Bridge.getLog().error(LayoutLog.TAG_BROKEN, "View draw failed", t, null);
        }
        return false;
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void measure(android.view.View thisView, int widthMeasureSpec, int heightMeasureSpec) {
        try {
            // This code is run within a catch to prevent misbehaving components from breaking
            // all the layout.
            thisView.measure_Original(widthMeasureSpec, heightMeasureSpec);
        } catch (java.lang.Throwable t) {
            com.android.layoutlib.bridge.Bridge.getLog().error(LayoutLog.TAG_BROKEN, "View measure failed", t, null);
        }
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void layout(android.view.View thisView, int l, int t, int r, int b) {
        try {
            // This code is run within a catch to prevent misbehaving components from breaking
            // all the layout.
            thisView.layout_Original(l, t, r, b);
        } catch (java.lang.Throwable th) {
            com.android.layoutlib.bridge.Bridge.getLog().error(LayoutLog.TAG_BROKEN, "View layout failed", th, null);
        }
    }
}

