/**
 * Copyright (C) 2011 The Android Open Source Project
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
 * Class allowing access to package-protected methods/fields.
 */
public class AttachInfo_Accessor {
    public static void setAttachInfo(android.view.View view) {
        android.content.Context context = view.getContext();
        android.view.WindowManager wm = ((android.view.WindowManager) (context.getSystemService(android.content.Context.WINDOW_SERVICE)));
        android.view.Display display = wm.getDefaultDisplay();
        android.view.ViewRootImpl root = new android.view.ViewRootImpl(context, display);
        android.view.View.AttachInfo info = new android.view.View.AttachInfo(com.android.layoutlib.bridge.util.ReflectionUtils.createProxy(android.view.IWindowSession.class), com.android.layoutlib.bridge.util.ReflectionUtils.createProxy(android.view.IWindow.class), display, root, new android.os.Handler(), null, context);
        info.mHasWindowFocus = true;
        info.mWindowVisibility = android.view.View.VISIBLE;
        info.mInTouchMode = false;// this is so that we can display selections.

        info.mHardwareAccelerated = false;
        view.dispatchAttachedToWindow(info, 0);
    }

    public static void dispatchOnPreDraw(android.view.View view) {
        view.mAttachInfo.mTreeObserver.dispatchOnPreDraw();
    }

    public static void detachFromWindow(android.view.View view) {
        if (view != null) {
            view.dispatchDetachedFromWindow();
        }
    }

    public static android.view.ViewRootImpl getRootView(android.view.View view) {
        return view.mAttachInfo != null ? view.mAttachInfo.mViewRootImpl : null;
    }
}

