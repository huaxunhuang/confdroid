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
package android.app;


/**
 * Convenience for implementing an activity that will be implemented
 * purely in native code.  That is, a game (or game-like thing).  There
 * is no need to derive from this class; you can simply declare it in your
 * manifest, and use the NDK APIs from there.
 *
 * <p>A typical manifest would look like:
 *
 * {@sample development/ndk/platforms/android-9/samples/native-activity/AndroidManifest.xml
 *      manifest}
 *
 * <p>A very simple example of native code that is run by NativeActivity
 * follows.  This reads input events from the user and uses OpenGLES to
 * draw into the native activity's window.
 *
 * {@sample development/ndk/platforms/android-9/samples/native-activity/jni/main.c all}
 */
public class NativeActivity extends android.app.Activity implements android.view.InputQueue.Callback , android.view.SurfaceHolder.Callback2 , android.view.ViewTreeObserver.OnGlobalLayoutListener {
    /**
     * Optional meta-that can be in the manifest for this component, specifying
     * the name of the native shared library to load.  If not specified,
     * "main" is used.
     */
    public static final java.lang.String META_DATA_LIB_NAME = "android.app.lib_name";

    /**
     * Optional meta-that can be in the manifest for this component, specifying
     * the name of the main entry point for this native activity in the
     * {@link #META_DATA_LIB_NAME} native code.  If not specified,
     * "ANativeActivity_onCreate" is used.
     */
    public static final java.lang.String META_DATA_FUNC_NAME = "android.app.func_name";

    private static final java.lang.String KEY_NATIVE_SAVED_STATE = "android:native_state";

    private android.app.NativeActivity.NativeContentView mNativeContentView;

    private android.view.inputmethod.InputMethodManager mIMM;

    private long mNativeHandle;

    private android.view.InputQueue mCurInputQueue;

    private android.view.SurfaceHolder mCurSurfaceHolder;

    final int[] mLocation = new int[2];

    int mLastContentX;

    int mLastContentY;

    int mLastContentWidth;

    int mLastContentHeight;

    private boolean mDispatchingUnhandledKey;

    private boolean mDestroyed;

    private native long loadNativeCode(java.lang.String path, java.lang.String funcname, android.os.MessageQueue queue, java.lang.String internalDataPath, java.lang.String obbPath, java.lang.String externalDataPath, int sdkVersion, android.content.res.AssetManager assetMgr, byte[] savedState, java.lang.ClassLoader classLoader, java.lang.String libraryPath);

    private native java.lang.String getDlError();

    private native void unloadNativeCode(long handle);

    private native void onStartNative(long handle);

    private native void onResumeNative(long handle);

    private native byte[] onSaveInstanceStateNative(long handle);

    private native void onPauseNative(long handle);

    private native void onStopNative(long handle);

    private native void onConfigurationChangedNative(long handle);

    private native void onLowMemoryNative(long handle);

    private native void onWindowFocusChangedNative(long handle, boolean focused);

    private native void onSurfaceCreatedNative(long handle, android.view.Surface surface);

    private native void onSurfaceChangedNative(long handle, android.view.Surface surface, int format, int width, int height);

    private native void onSurfaceRedrawNeededNative(long handle, android.view.Surface surface);

    private native void onSurfaceDestroyedNative(long handle);

    private native void onInputQueueCreatedNative(long handle, long queuePtr);

    private native void onInputQueueDestroyedNative(long handle, long queuePtr);

    private native void onContentRectChangedNative(long handle, int x, int y, int w, int h);

    static class NativeContentView extends android.view.View {
        android.app.NativeActivity mActivity;

        public NativeContentView(android.content.Context context) {
            super(context);
        }

        public NativeContentView(android.content.Context context, android.util.AttributeSet attrs) {
            super(context, attrs);
        }
    }

    @java.lang.Override
    protected void onCreate(android.os.Bundle savedInstanceState) {
        java.lang.String libname = "main";
        java.lang.String funcname = "ANativeActivity_onCreate";
        android.content.pm.ActivityInfo ai;
        mIMM = getSystemService(android.view.inputmethod.InputMethodManager.class);
        getWindow().takeSurface(this);
        getWindow().takeInputQueue(this);
        getWindow().setFormat(android.graphics.PixelFormat.RGB_565);
        getWindow().setSoftInputMode(android.view.WindowManager.LayoutParams.SOFT_INPUT_STATE_UNSPECIFIED | android.view.WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        mNativeContentView = new android.app.NativeActivity.NativeContentView(this);
        mNativeContentView.mActivity = this;
        setContentView(mNativeContentView);
        mNativeContentView.requestFocus();
        mNativeContentView.getViewTreeObserver().addOnGlobalLayoutListener(this);
        try {
            ai = getPackageManager().getActivityInfo(getIntent().getComponent(), android.content.pm.PackageManager.GET_META_DATA);
            if (ai.metaData != null) {
                java.lang.String ln = ai.metaData.getString(android.app.NativeActivity.META_DATA_LIB_NAME);
                if (ln != null)
                    libname = ln;

                ln = ai.metaData.getString(android.app.NativeActivity.META_DATA_FUNC_NAME);
                if (ln != null)
                    funcname = ln;

            }
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            throw new java.lang.RuntimeException("Error getting activity info", e);
        }
        dalvik.system.BaseDexClassLoader classLoader = ((dalvik.system.BaseDexClassLoader) (getClassLoader()));
        java.lang.String path = classLoader.findLibrary(libname);
        if (path == null) {
            throw new java.lang.IllegalArgumentException((("Unable to find native library " + libname) + " using classloader: ") + classLoader.toString());
        }
        byte[] nativeSavedState = (savedInstanceState != null) ? savedInstanceState.getByteArray(android.app.NativeActivity.KEY_NATIVE_SAVED_STATE) : null;
        mNativeHandle = loadNativeCode(path, funcname, android.os.Looper.myQueue(), android.app.NativeActivity.getAbsolutePath(getFilesDir()), android.app.NativeActivity.getAbsolutePath(getObbDir()), android.app.NativeActivity.getAbsolutePath(getExternalFilesDir(null)), android.os.Build.VERSION.SDK_INT, getAssets(), nativeSavedState, classLoader, classLoader.getLdLibraryPath());
        if (mNativeHandle == 0) {
            throw new java.lang.UnsatisfiedLinkError((("Unable to load native library \"" + path) + "\": ") + getDlError());
        }
        super.onCreate(savedInstanceState);
    }

    private static java.lang.String getAbsolutePath(java.io.File file) {
        return file != null ? file.getAbsolutePath() : null;
    }

    @java.lang.Override
    protected void onDestroy() {
        mDestroyed = true;
        if (mCurSurfaceHolder != null) {
            onSurfaceDestroyedNative(mNativeHandle);
            mCurSurfaceHolder = null;
        }
        if (mCurInputQueue != null) {
            onInputQueueDestroyedNative(mNativeHandle, mCurInputQueue.getNativePtr());
            mCurInputQueue = null;
        }
        unloadNativeCode(mNativeHandle);
        super.onDestroy();
    }

    @java.lang.Override
    protected void onPause() {
        super.onPause();
        onPauseNative(mNativeHandle);
    }

    @java.lang.Override
    protected void onResume() {
        super.onResume();
        onResumeNative(mNativeHandle);
    }

    @java.lang.Override
    protected void onSaveInstanceState(android.os.Bundle outState) {
        super.onSaveInstanceState(outState);
        byte[] state = onSaveInstanceStateNative(mNativeHandle);
        if (state != null) {
            outState.putByteArray(android.app.NativeActivity.KEY_NATIVE_SAVED_STATE, state);
        }
    }

    @java.lang.Override
    protected void onStart() {
        super.onStart();
        onStartNative(mNativeHandle);
    }

    @java.lang.Override
    protected void onStop() {
        super.onStop();
        onStopNative(mNativeHandle);
    }

    @java.lang.Override
    public void onConfigurationChanged(android.content.res.Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (!mDestroyed) {
            onConfigurationChangedNative(mNativeHandle);
        }
    }

    @java.lang.Override
    public void onLowMemory() {
        super.onLowMemory();
        if (!mDestroyed) {
            onLowMemoryNative(mNativeHandle);
        }
    }

    @java.lang.Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (!mDestroyed) {
            onWindowFocusChangedNative(mNativeHandle, hasFocus);
        }
    }

    public void surfaceCreated(android.view.SurfaceHolder holder) {
        if (!mDestroyed) {
            mCurSurfaceHolder = holder;
            onSurfaceCreatedNative(mNativeHandle, holder.getSurface());
        }
    }

    public void surfaceChanged(android.view.SurfaceHolder holder, int format, int width, int height) {
        if (!mDestroyed) {
            mCurSurfaceHolder = holder;
            onSurfaceChangedNative(mNativeHandle, holder.getSurface(), format, width, height);
        }
    }

    public void surfaceRedrawNeeded(android.view.SurfaceHolder holder) {
        if (!mDestroyed) {
            mCurSurfaceHolder = holder;
            onSurfaceRedrawNeededNative(mNativeHandle, holder.getSurface());
        }
    }

    public void surfaceDestroyed(android.view.SurfaceHolder holder) {
        mCurSurfaceHolder = null;
        if (!mDestroyed) {
            onSurfaceDestroyedNative(mNativeHandle);
        }
    }

    public void onInputQueueCreated(android.view.InputQueue queue) {
        if (!mDestroyed) {
            mCurInputQueue = queue;
            onInputQueueCreatedNative(mNativeHandle, queue.getNativePtr());
        }
    }

    public void onInputQueueDestroyed(android.view.InputQueue queue) {
        if (!mDestroyed) {
            onInputQueueDestroyedNative(mNativeHandle, queue.getNativePtr());
            mCurInputQueue = null;
        }
    }

    public void onGlobalLayout() {
        mNativeContentView.getLocationInWindow(mLocation);
        int w = mNativeContentView.getWidth();
        int h = mNativeContentView.getHeight();
        if ((((mLocation[0] != mLastContentX) || (mLocation[1] != mLastContentY)) || (w != mLastContentWidth)) || (h != mLastContentHeight)) {
            mLastContentX = mLocation[0];
            mLastContentY = mLocation[1];
            mLastContentWidth = w;
            mLastContentHeight = h;
            if (!mDestroyed) {
                onContentRectChangedNative(mNativeHandle, mLastContentX, mLastContentY, mLastContentWidth, mLastContentHeight);
            }
        }
    }

    void setWindowFlags(int flags, int mask) {
        getWindow().setFlags(flags, mask);
    }

    void setWindowFormat(int format) {
        getWindow().setFormat(format);
    }

    void showIme(int mode) {
        mIMM.showSoftInput(mNativeContentView, mode);
    }

    void hideIme(int mode) {
        mIMM.hideSoftInputFromWindow(mNativeContentView.getWindowToken(), mode);
    }
}

