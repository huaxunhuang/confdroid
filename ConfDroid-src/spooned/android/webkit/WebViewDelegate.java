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
package android.webkit;


/**
 * Delegate used by the WebView provider implementation to access
 * the required framework functionality needed to implement a {@link WebView}.
 *
 * @unknown 
 */
@android.annotation.SystemApi
public final class WebViewDelegate {
    /* package */
    WebViewDelegate() {
    }

    /**
     * Listener that gets notified whenever tracing has been enabled/disabled.
     */
    public interface OnTraceEnabledChangeListener {
        void onTraceEnabledChange(boolean enabled);
    }

    /**
     * Register a callback to be invoked when tracing for the WebView component has been
     * enabled/disabled.
     */
    public void setOnTraceEnabledChangeListener(final android.webkit.WebViewDelegate.OnTraceEnabledChangeListener listener) {
        android.os.SystemProperties.addChangeCallback(new java.lang.Runnable() {
            @java.lang.Override
            public void run() {
                listener.onTraceEnabledChange(isTraceTagEnabled());
            }
        });
    }

    /**
     * Returns true if the WebView trace tag is enabled and false otherwise.
     */
    public boolean isTraceTagEnabled() {
        return android.os.Trace.isTagEnabled(android.os.Trace.TRACE_TAG_WEBVIEW);
    }

    /**
     * Returns true if the draw GL functor can be invoked (see {@link #invokeDrawGlFunctor})
     * and false otherwise.
     */
    public boolean canInvokeDrawGlFunctor(android.view.View containerView) {
        return true;
    }

    /**
     * Invokes the draw GL functor. If waitForCompletion is false the functor
     * may be invoked asynchronously.
     *
     * @param nativeDrawGLFunctor
     * 		the pointer to the native functor that implements
     * 		system/core/include/utils/Functor.h
     */
    public void invokeDrawGlFunctor(android.view.View containerView, long nativeDrawGLFunctor, boolean waitForCompletion) {
        android.view.ViewRootImpl.invokeFunctor(nativeDrawGLFunctor, waitForCompletion);
    }

    /**
     * Calls the function specified with the nativeDrawGLFunctor functor pointer. This
     * functionality is used by the WebView for calling into their renderer from the
     * framework display lists.
     *
     * @param canvas
     * 		a hardware accelerated canvas (see {@link Canvas#isHardwareAccelerated()})
     * @param nativeDrawGLFunctor
     * 		the pointer to the native functor that implements
     * 		system/core/include/utils/Functor.h
     * @throws IllegalArgumentException
     * 		if the canvas is not hardware accelerated
     */
    public void callDrawGlFunction(android.graphics.Canvas canvas, long nativeDrawGLFunctor) {
        if (!(canvas instanceof android.view.DisplayListCanvas)) {
            // Canvas#isHardwareAccelerated() is only true for subclasses of HardwareCanvas.
            throw new java.lang.IllegalArgumentException(canvas.getClass().getName() + " is not a DisplayList canvas");
        }
        ((android.view.DisplayListCanvas) (canvas)).drawGLFunctor2(nativeDrawGLFunctor, null);
    }

    /**
     * Calls the function specified with the nativeDrawGLFunctor functor pointer. This
     * functionality is used by the WebView for calling into their renderer from the
     * framework display lists.
     *
     * @param canvas
     * 		a hardware accelerated canvas (see {@link Canvas#isHardwareAccelerated()})
     * @param nativeDrawGLFunctor
     * 		the pointer to the native functor that implements
     * 		system/core/include/utils/Functor.h
     * @param releasedRunnable
     * 		Called when this nativeDrawGLFunctor is no longer referenced by this
     * 		canvas, so is safe to be destroyed.
     * @throws IllegalArgumentException
     * 		if the canvas is not hardware accelerated
     */
    public void callDrawGlFunction(@android.annotation.NonNull
    android.graphics.Canvas canvas, long nativeDrawGLFunctor, @android.annotation.Nullable
    java.lang.Runnable releasedRunnable) {
        if (!(canvas instanceof android.view.DisplayListCanvas)) {
            // Canvas#isHardwareAccelerated() is only true for subclasses of HardwareCanvas.
            throw new java.lang.IllegalArgumentException(canvas.getClass().getName() + " is not a DisplayList canvas");
        }
        ((android.view.DisplayListCanvas) (canvas)).drawGLFunctor2(nativeDrawGLFunctor, releasedRunnable);
    }

    /**
     * Detaches the draw GL functor.
     *
     * @param nativeDrawGLFunctor
     * 		the pointer to the native functor that implements
     * 		system/core/include/utils/Functor.h
     */
    public void detachDrawGlFunctor(android.view.View containerView, long nativeDrawGLFunctor) {
        android.view.ViewRootImpl viewRootImpl = containerView.getViewRootImpl();
        if ((nativeDrawGLFunctor != 0) && (viewRootImpl != null)) {
            viewRootImpl.detachFunctor(nativeDrawGLFunctor);
        }
    }

    /**
     * Returns the package id of the given {@code packageName}.
     */
    public int getPackageId(android.content.res.Resources resources, java.lang.String packageName) {
        android.util.SparseArray<java.lang.String> packageIdentifiers = resources.getAssets().getAssignedPackageIdentifiers();
        for (int i = 0; i < packageIdentifiers.size(); i++) {
            final java.lang.String name = packageIdentifiers.valueAt(i);
            if (packageName.equals(name)) {
                return packageIdentifiers.keyAt(i);
            }
        }
        throw new java.lang.RuntimeException("Package not found: " + packageName);
    }

    /**
     * Returns the application which is embedding the WebView.
     */
    public android.app.Application getApplication() {
        return android.app.ActivityThread.currentApplication();
    }

    /**
     * Returns the error string for the given {@code errorCode}.
     */
    public java.lang.String getErrorString(android.content.Context context, int errorCode) {
        return android.webkit.LegacyErrorStrings.getString(errorCode, context);
    }

    /**
     * Adds the WebView asset path to {@link android.content.res.AssetManager}.
     */
    public void addWebViewAssetPath(android.content.Context context) {
        final java.lang.String newAssetPath = android.webkit.WebViewFactory.getLoadedPackageInfo().applicationInfo.sourceDir;
        final android.content.pm.ApplicationInfo appInfo = context.getApplicationInfo();
        final java.lang.String[] libs = appInfo.sharedLibraryFiles;
        if (!com.android.internal.util.ArrayUtils.contains(libs, newAssetPath)) {
            // Build the new library asset path list.
            final int newLibAssetsCount = 1 + (libs != null ? libs.length : 0);
            final java.lang.String[] newLibAssets = new java.lang.String[newLibAssetsCount];
            if (libs != null) {
                java.lang.System.arraycopy(libs, 0, newLibAssets, 0, libs.length);
            }
            newLibAssets[newLibAssetsCount - 1] = newAssetPath;
            // Update the ApplicationInfo object with the new list.
            // We know this will persist and future Resources created via ResourcesManager
            // will include the shared library because this ApplicationInfo comes from the
            // underlying LoadedApk in ContextImpl, which does not change during the life of the
            // application.
            appInfo.sharedLibraryFiles = newLibAssets;
            // Update existing Resources with the WebView library.
            android.app.ResourcesManager.getInstance().appendLibAssetForMainAssetPath(appInfo.getBaseResourcePath(), newAssetPath);
        }
    }
}

