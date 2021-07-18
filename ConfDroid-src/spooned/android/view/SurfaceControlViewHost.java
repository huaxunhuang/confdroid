/**
 * Copyright (C) 2019 The Android Open Source Project
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
 * Utility class for adding a View hierarchy to a {@link SurfaceControl}. The View hierarchy
 * will render in to a root SurfaceControl, and receive input based on the SurfaceControl's
 * placement on-screen. The primary usage of this class is to embed a View hierarchy from
 * one process in to another. After the SurfaceControlViewHost has been set up in the embedded
 * content provider, we can send the {@link SurfaceControlViewHost.SurfacePackage}
 * to the host process. The host process can then attach the hierarchy to a SurfaceView within
 * its own by calling
 * {@link SurfaceView#setChildSurfacePackage}.
 */
public class SurfaceControlViewHost {
    private final android.view.ViewRootImpl mViewRoot;

    private android.view.WindowlessWindowManager mWm;

    private android.view.SurfaceControl mSurfaceControl;

    private android.view.accessibility.IAccessibilityEmbeddedConnection mAccessibilityEmbeddedConnection;

    /**
     * Package encapsulating a Surface hierarchy which contains interactive view
     * elements. It's expected to get this object from
     * {@link SurfaceControlViewHost#getSurfacePackage} afterwards it can be embedded within
     * a SurfaceView by calling {@link SurfaceView#setChildSurfacePackage}.
     *
     * Note that each {@link SurfacePackage} must be released by calling
     * {@link SurfacePackage#release}. However, if you use the recommended flow,
     *  the framework will automatically handle the lifetime for you.
     *
     * 1. When sending the package to the remote process, return it from an AIDL method
     * or manually use FLAG_WRITE_RETURN_VALUE in writeToParcel. This will automatically
     * release the package in the local process.
     * 2. In the remote process, consume the package using SurfaceView. This way the
     * SurfaceView will take over the lifetime and call {@link SurfacePackage#release}
     * for the user.
     *
     * One final note: The {@link SurfacePackage} lifetime is totally de-coupled
     * from the lifetime of the underlying {@link SurfaceControlViewHost}. Regardless
     * of the lifetime of the package the user should still call
     * {@link SurfaceControlViewHost#release} when finished.
     */
    public static final class SurfacePackage implements android.os.Parcelable {
        private android.view.SurfaceControl mSurfaceControl;

        private final android.view.accessibility.IAccessibilityEmbeddedConnection mAccessibilityEmbeddedConnection;

        SurfacePackage(android.view.SurfaceControl sc, android.view.accessibility.IAccessibilityEmbeddedConnection connection) {
            mSurfaceControl = sc;
            mAccessibilityEmbeddedConnection = connection;
        }

        private SurfacePackage(android.os.Parcel in) {
            mSurfaceControl = new android.view.SurfaceControl();
            mSurfaceControl.readFromParcel(in);
            mAccessibilityEmbeddedConnection = IAccessibilityEmbeddedConnection.Stub.asInterface(in.readStrongBinder());
        }

        /**
         * Use {@link SurfaceView#setChildSurfacePackage} or manually fix
         * accessibility (see SurfaceView implementation).
         *
         * @unknown 
         */
        @android.annotation.NonNull
        public android.view.SurfaceControl getSurfaceControl() {
            return mSurfaceControl;
        }

        /**
         * Gets an accessibility embedded connection interface for this SurfaceControlViewHost.
         *
         * @return {@link IAccessibilityEmbeddedConnection} interface.
         * @unknown 
         */
        public android.view.accessibility.IAccessibilityEmbeddedConnection getAccessibilityEmbeddedConnection() {
            return mAccessibilityEmbeddedConnection;
        }

        @java.lang.Override
        public int describeContents() {
            return 0;
        }

        @java.lang.Override
        public void writeToParcel(@android.annotation.NonNull
        android.os.Parcel out, int flags) {
            mSurfaceControl.writeToParcel(out, flags);
            out.writeStrongBinder(mAccessibilityEmbeddedConnection.asBinder());
        }

        /**
         * Release the {@link SurfaceControl} associated with this package.
         * It's not necessary to call this if you pass the package to
         * {@link SurfaceView#setChildSurfacePackage} as {@link SurfaceView} will
         * take ownership in that case.
         */
        public void release() {
            if (mSurfaceControl != null) {
                mSurfaceControl.release();
            }
            mSurfaceControl = null;
        }

        @android.annotation.NonNull
        public static final android.view.Creator<android.view.SurfaceControlViewHost.SurfacePackage> CREATOR = new android.view.Creator<android.view.SurfaceControlViewHost.SurfacePackage>() {
            public android.view.SurfaceControlViewHost.SurfacePackage createFromParcel(android.os.Parcel in) {
                return new android.view.SurfaceControlViewHost.SurfacePackage(in);
            }

            public android.view.SurfaceControlViewHost.SurfacePackage[] newArray(int size) {
                return new android.view.SurfaceControlViewHost.SurfacePackage[size];
            }
        };
    }

    /**
     *
     *
     * @unknown 
     */
    public SurfaceControlViewHost(@android.annotation.NonNull
    android.content.Context c, @android.annotation.NonNull
    android.view.Display d, @android.annotation.NonNull
    android.view.WindowlessWindowManager wwm) {
        /* useSfChoreographer */
        this(c, d, wwm, false);
    }

    /**
     *
     *
     * @unknown 
     */
    public SurfaceControlViewHost(@android.annotation.NonNull
    android.content.Context c, @android.annotation.NonNull
    android.view.Display d, @android.annotation.NonNull
    android.view.WindowlessWindowManager wwm, boolean useSfChoreographer) {
        mWm = wwm;
        mViewRoot = new android.view.ViewRootImpl(c, d, mWm, useSfChoreographer);
        mViewRoot.forceDisableBLAST();
        mAccessibilityEmbeddedConnection = mViewRoot.getAccessibilityEmbeddedConnection();
    }

    /**
     * Construct a new SurfaceControlViewHost. The root Surface will be
     * allocated internally and is accessible via getSurfacePackage().
     *
     * The {@param hostToken} parameter, primarily used for ANR reporting,
     * must be obtained from whomever will be hosting the embedded hierarchy.
     * It's accessible from {@link SurfaceView#getHostToken}.
     *
     * @param context
     * 		The Context object for your activity or application.
     * @param display
     * 		The Display the hierarchy will be placed on.
     * @param hostToken
     * 		The host token, as discussed above.
     */
    public SurfaceControlViewHost(@android.annotation.NonNull
    android.content.Context context, @android.annotation.NonNull
    android.view.Display display, @android.annotation.Nullable
    android.os.IBinder hostToken) {
        mSurfaceControl = new android.view.SurfaceControl.Builder().setContainerLayer().setName("SurfaceControlViewHost").setCallsite("SurfaceControlViewHost").build();
        mWm = new android.view.WindowlessWindowManager(context.getResources().getConfiguration(), mSurfaceControl, hostToken);
        mViewRoot = new android.view.ViewRootImpl(context, display, mWm);
        mViewRoot.forceDisableBLAST();
        mAccessibilityEmbeddedConnection = mViewRoot.getAccessibilityEmbeddedConnection();
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    protected void finalize() throws java.lang.Throwable {
        // We aren't on the UI thread here so we need to pass false to
        // doDie
        /* immediate */
        mViewRoot.die(false);
    }

    /**
     * Return a SurfacePackage for the root SurfaceControl of the embedded hierarchy.
     * Rather than be directly reparented using {@link SurfaceControl.Transaction} this
     * SurfacePackage should be passed to {@link SurfaceView#setChildSurfacePackage}
     * which will not only reparent the Surface, but ensure the accessibility hierarchies
     * are linked.
     */
    @android.annotation.Nullable
    public android.view.SurfaceControlViewHost.SurfacePackage getSurfacePackage() {
        if ((mSurfaceControl != null) && (mAccessibilityEmbeddedConnection != null)) {
            return new android.view.SurfaceControlViewHost.SurfacePackage(mSurfaceControl, mAccessibilityEmbeddedConnection);
        } else {
            return null;
        }
    }

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.TestApi
    public void setView(@android.annotation.NonNull
    android.view.View view, @android.annotation.NonNull
    android.view.WindowManager.LayoutParams attrs) {
        java.util.Objects.requireNonNull(view);
        mViewRoot.setView(view, attrs, null);
    }

    /**
     * Set the root view of the SurfaceControlViewHost. This view will render in to
     * the SurfaceControl, and receive input based on the SurfaceControls positioning on
     * screen. It will be laid as if it were in a window of the passed in width and height.
     *
     * @param view
     * 		The View to add
     * @param width
     * 		The width to layout the View within, in pixels.
     * @param height
     * 		The height to layout the View within, in pixels.
     */
    public void setView(@android.annotation.NonNull
    android.view.View view, int width, int height) {
        final android.view.WindowManager.LayoutParams lp = new android.view.WindowManager.LayoutParams(width, height, android.view.WindowManager.LayoutParams.TYPE_APPLICATION, 0, android.graphics.PixelFormat.TRANSPARENT);
        lp.flags |= android.view.WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED;
        setView(view, lp);
    }

    /**
     *
     *
     * @return The view passed to setView, or null if none has been passed.
     */
    @android.annotation.Nullable
    public android.view.View getView() {
        return mViewRoot.getView();
    }

    /**
     *
     *
     * @return the ViewRootImpl wrapped by this host.
     * @unknown 
     */
    public android.view.IWindow getWindowToken() {
        return mViewRoot.mWindow;
    }

    /**
     *
     *
     * @return the WindowlessWindowManager instance that this host is attached to.
     * @unknown 
     */
    @android.annotation.NonNull
    public android.view.WindowlessWindowManager getWindowlessWM() {
        return mWm;
    }

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.TestApi
    public void relayout(android.view.WindowManager.LayoutParams attrs) {
        mViewRoot.setLayoutParams(attrs, false);
        mViewRoot.setReportNextDraw();
        mWm.setCompletionCallback(mViewRoot.mWindow.asBinder(), (android.view.SurfaceControl.Transaction t) -> {
            t.apply();
        });
    }

    /**
     * Modify the size of the root view.
     *
     * @param width
     * 		Width in pixels
     * @param height
     * 		Height in pixels
     */
    public void relayout(int width, int height) {
        final android.view.WindowManager.LayoutParams lp = new android.view.WindowManager.LayoutParams(width, height, android.view.WindowManager.LayoutParams.TYPE_APPLICATION, 0, android.graphics.PixelFormat.TRANSPARENT);
        relayout(lp);
    }

    /**
     * Trigger the tear down of the embedded view hierarchy and release the SurfaceControl.
     * This will result in onDispatchedFromWindow being dispatched to the embedded view hierarchy
     * and render the object unusable.
     */
    public void release() {
        // ViewRoot will release mSurfaceControl for us.
        /* immediate */
        mViewRoot.die(true);
    }
}

