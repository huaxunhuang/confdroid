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
 * A simplistic implementation of IWindowSession. Rather than managing Surfaces
 * as children of the display, it manages Surfaces as children of a given root.
 *
 * By parcelling the root surface, the app can offer another app content for embedding.
 *
 * @unknown 
 */
public class WindowlessWindowManager implements android.view.IWindowSession {
    private static final java.lang.String TAG = "WindowlessWindowManager";

    private class State {
        android.view.SurfaceControl mSurfaceControl;

        android.view.WindowManager.LayoutParams mParams = new android.view.WindowManager.LayoutParams();

        int mDisplayId;

        android.os.IBinder mInputChannelToken;

        android.graphics.Region mInputRegion;

        State(android.view.SurfaceControl sc, android.view.WindowManager.LayoutParams p, int displayId, android.os.IBinder inputChannelToken) {
            mSurfaceControl = sc;
            mParams.copyFrom(p);
            mDisplayId = displayId;
            mInputChannelToken = inputChannelToken;
        }
    }

    /**
     * Used to store SurfaceControl we've built for clients to
     * reconfigure them if relayout is called.
     */
    final java.util.HashMap<android.os.IBinder, android.view.WindowlessWindowManager.State> mStateForWindow = new java.util.HashMap<android.os.IBinder, android.view.WindowlessWindowManager.State>();

    public interface ResizeCompleteCallback {
        public void finished(android.view.SurfaceControl.Transaction completion);
    }

    final java.util.HashMap<android.os.IBinder, android.view.WindowlessWindowManager.ResizeCompleteCallback> mResizeCompletionForWindow = new java.util.HashMap<android.os.IBinder, android.view.WindowlessWindowManager.ResizeCompleteCallback>();

    private final android.view.SurfaceSession mSurfaceSession = new android.view.SurfaceSession();

    private final android.view.SurfaceControl mRootSurface;

    private final android.content.res.Configuration mConfiguration;

    private final android.view.IWindowSession mRealWm;

    private final android.os.IBinder mHostInputToken;

    private int mForceHeight = -1;

    private int mForceWidth = -1;

    public WindowlessWindowManager(android.content.res.Configuration c, android.view.SurfaceControl rootSurface, android.os.IBinder hostInputToken) {
        mRootSurface = rootSurface;
        mConfiguration = new android.content.res.Configuration(c);
        mRealWm = android.view.WindowManagerGlobal.getWindowSession();
        mHostInputToken = hostInputToken;
    }

    protected void setConfiguration(android.content.res.Configuration configuration) {
        mConfiguration.setTo(configuration);
    }

    /**
     * Utility API.
     */
    void setCompletionCallback(android.os.IBinder window, android.view.WindowlessWindowManager.ResizeCompleteCallback callback) {
        if (mResizeCompletionForWindow.get(window) != null) {
            android.util.Log.w(android.view.WindowlessWindowManager.TAG, "Unsupported overlapping resizes");
        }
        mResizeCompletionForWindow.put(window, callback);
    }

    protected void setTouchRegion(android.os.IBinder window, @android.annotation.Nullable
    android.graphics.Region region) {
        android.view.WindowlessWindowManager.State state;
        synchronized(this) {
            // Do everything while locked so that we synchronize with relayout. This should be a
            // very infrequent operation.
            state = mStateForWindow.get(window);
            if (state == null) {
                return;
            }
            if (java.util.Objects.equals(region, state.mInputRegion)) {
                return;
            }
            state.mInputRegion = (region != null) ? new android.graphics.Region(region) : null;
            if (state.mInputChannelToken != null) {
                try {
                    mRealWm.updateInputChannel(state.mInputChannelToken, state.mDisplayId, state.mSurfaceControl, state.mParams.flags, state.mInputRegion);
                } catch (android.os.RemoteException e) {
                    android.util.Log.e(android.view.WindowlessWindowManager.TAG, "Failed to update surface input channel: ", e);
                }
            }
        }
    }

    /**
     * IWindowSession implementation.
     */
    @java.lang.Override
    public int addToDisplay(android.view.IWindow window, int seq, android.view.WindowManager.LayoutParams attrs, int viewVisibility, int displayId, android.graphics.Rect outFrame, android.graphics.Rect outContentInsets, android.graphics.Rect outStableInsets, android.view.DisplayCutout.ParcelableWrapper outDisplayCutout, android.view.InputChannel outInputChannel, android.view.InsetsState outInsetsState, android.view.InsetsSourceControl[] outActiveControls) {
        final android.view.SurfaceControl.Builder b = new android.view.SurfaceControl.Builder(mSurfaceSession).setParent(mRootSurface).setFormat(attrs.format).setBufferSize(getSurfaceWidth(attrs), getSurfaceHeight(attrs)).setName(attrs.getTitle().toString()).setCallsite("WindowlessWindowManager.addToDisplay");
        final android.view.SurfaceControl sc = b.build();
        if ((attrs.inputFeatures & android.view.WindowManager.LayoutParams.INPUT_FEATURE_NO_INPUT_CHANNEL) == 0) {
            try {
                mRealWm.grantInputChannel(displayId, sc, window, mHostInputToken, attrs.flags, attrs.type, outInputChannel);
            } catch (android.os.RemoteException e) {
                android.util.Log.e(android.view.WindowlessWindowManager.TAG, "Failed to grant input to surface: ", e);
            }
        }
        final android.view.WindowlessWindowManager.State state = new android.view.WindowlessWindowManager.State(sc, attrs, displayId, outInputChannel != null ? outInputChannel.getToken() : null);
        synchronized(this) {
            mStateForWindow.put(window.asBinder(), state);
        }
        return android.view.WindowManagerGlobal.ADD_OKAY | android.view.WindowManagerGlobal.ADD_FLAG_APP_VISIBLE;
    }

    /**
     * IWindowSession implementation. Currently this class doesn't need to support for multi-user.
     */
    @java.lang.Override
    public int addToDisplayAsUser(android.view.IWindow window, int seq, android.view.WindowManager.LayoutParams attrs, int viewVisibility, int displayId, int userId, android.graphics.Rect outFrame, android.graphics.Rect outContentInsets, android.graphics.Rect outStableInsets, android.view.DisplayCutout.ParcelableWrapper outDisplayCutout, android.view.InputChannel outInputChannel, android.view.InsetsState outInsetsState, android.view.InsetsSourceControl[] outActiveControls) {
        return addToDisplay(window, seq, attrs, viewVisibility, displayId, outFrame, outContentInsets, outStableInsets, outDisplayCutout, outInputChannel, outInsetsState, outActiveControls);
    }

    @java.lang.Override
    public int addToDisplayWithoutInputChannel(android.view.android.view.IWindow window, int seq, android.view.WindowManager.LayoutParams attrs, int viewVisibility, int layerStackId, android.graphics.Rect outContentInsets, android.graphics.Rect outStableInsets, android.view.InsetsState insetsState) {
        return 0;
    }

    @java.lang.Override
    public void remove(android.view.android.view.IWindow window) throws android.os.RemoteException {
        mRealWm.remove(window);
        android.view.WindowlessWindowManager.State state;
        synchronized(this) {
            state = mStateForWindow.remove(window.asBinder());
        }
        if (state == null) {
            throw new java.lang.IllegalArgumentException("Invalid window token (never added or removed already)");
        }
        try (android.view.SurfaceControl.Transaction t = new android.view.SurfaceControl.Transaction()) {
            t.remove(state.mSurfaceControl).apply();
        }
    }

    private boolean isOpaque(android.view.WindowManager.LayoutParams attrs) {
        if (((((attrs.surfaceInsets != null) && (attrs.surfaceInsets.left != 0)) || (attrs.surfaceInsets.top != 0)) || (attrs.surfaceInsets.right != 0)) || (attrs.surfaceInsets.bottom != 0)) {
            return false;
        }
        return !android.graphics.PixelFormat.formatHasAlpha(attrs.format);
    }

    /**
     *
     *
     * @unknown 
     */
    protected android.view.SurfaceControl getSurfaceControl(android.view.View rootView) {
        final android.view.ViewRootImpl root = rootView.getViewRootImpl();
        if (root == null) {
            return null;
        }
        final android.view.WindowlessWindowManager.State s = mStateForWindow.get(root.mWindow.asBinder());
        if (s == null) {
            return null;
        }
        return s.mSurfaceControl;
    }

    @java.lang.Override
    public int relayout(android.view.IWindow window, int seq, android.view.WindowManager.LayoutParams inAttrs, int requestedWidth, int requestedHeight, int viewFlags, int flags, long frameNumber, android.graphics.Rect outFrame, android.graphics.Rect outContentInsets, android.graphics.Rect outVisibleInsets, android.graphics.Rect outStableInsets, android.graphics.Rect outBackdropFrame, android.view.DisplayCutout.ParcelableWrapper cutout, android.util.MergedConfiguration mergedConfiguration, android.view.SurfaceControl outSurfaceControl, android.view.InsetsState outInsetsState, android.view.InsetsSourceControl[] outActiveControls, android.graphics.Point outSurfaceSize, android.view.SurfaceControl outBLASTSurfaceControl) {
        final android.view.WindowlessWindowManager.State state;
        synchronized(this) {
            state = mStateForWindow.get(window.asBinder());
        }
        if (state == null) {
            throw new java.lang.IllegalArgumentException("Invalid window token (never added or removed already)");
        }
        android.view.SurfaceControl sc = state.mSurfaceControl;
        android.view.SurfaceControl.Transaction t = new android.view.SurfaceControl.Transaction();
        int attrChanges = 0;
        if (inAttrs != null) {
            attrChanges = state.mParams.copyFrom(inAttrs);
        }
        android.view.WindowManager.LayoutParams attrs = state.mParams;
        if (viewFlags == android.view.View.VISIBLE) {
            t.setBufferSize(sc, getSurfaceWidth(attrs), getSurfaceHeight(attrs)).setOpaque(sc, isOpaque(attrs)).show(sc).apply();
            outSurfaceControl.copyFrom(sc, "WindowlessWindowManager.relayout");
        } else {
            t.hide(sc).apply();
            outSurfaceControl.release();
        }
        outFrame.set(0, 0, attrs.width, attrs.height);
        mergedConfiguration.setConfiguration(mConfiguration, mConfiguration);
        if (((attrChanges & android.view.WindowManager.LayoutParams.FLAGS_CHANGED) != 0) && (state.mInputChannelToken != null)) {
            try {
                mRealWm.updateInputChannel(state.mInputChannelToken, state.mDisplayId, sc, attrs.flags, state.mInputRegion);
            } catch (android.os.RemoteException e) {
                android.util.Log.e(android.view.WindowlessWindowManager.TAG, "Failed to update surface input channel: ", e);
            }
        }
        return 0;
    }

    @java.lang.Override
    public void prepareToReplaceWindows(android.os.IBinder appToken, boolean childrenOnly) {
    }

    @java.lang.Override
    public boolean outOfMemory(android.view.android.view.IWindow window) {
        return false;
    }

    @java.lang.Override
    public void setTransparentRegion(android.view.android.view.IWindow window, android.graphics.Region region) {
    }

    @java.lang.Override
    public void setInsets(android.view.android.view.IWindow window, int touchableInsets, android.graphics.Rect contentInsets, android.graphics.Rect visibleInsets, android.graphics.Region touchableRegion) {
    }

    @java.lang.Override
    public void getDisplayFrame(android.view.android.view.IWindow window, android.graphics.Rect outDisplayFrame) {
    }

    @java.lang.Override
    public void finishDrawing(android.view.android.view.IWindow window, android.view.SurfaceControl.Transaction postDrawTransaction) {
        synchronized(this) {
            final android.view.WindowlessWindowManager.ResizeCompleteCallback c = mResizeCompletionForWindow.get(window.asBinder());
            if (c == null) {
                // No one wanted the callback, but it wasn't necessarily unexpected.
                postDrawTransaction.apply();
                return;
            }
            c.finished(postDrawTransaction);
            mResizeCompletionForWindow.remove(window.asBinder());
        }
    }

    @java.lang.Override
    public void setInTouchMode(boolean showFocus) {
    }

    @java.lang.Override
    public boolean getInTouchMode() {
        return false;
    }

    @java.lang.Override
    public boolean performHapticFeedback(int effectId, boolean always) {
        return false;
    }

    @java.lang.Override
    public android.os.IBinder performDrag(android.view.android.view.IWindow window, int flags, android.view.SurfaceControl surface, int touchSource, float touchX, float touchY, float thumbCenterX, float thumbCenterY, android.content.ClipData data) {
        return null;
    }

    @java.lang.Override
    public void reportDropResult(android.view.android.view.IWindow window, boolean consumed) {
    }

    @java.lang.Override
    public void cancelDragAndDrop(android.os.IBinder dragToken, boolean skipAnimation) {
    }

    @java.lang.Override
    public void dragRecipientEntered(android.view.android.view.IWindow window) {
    }

    @java.lang.Override
    public void dragRecipientExited(android.view.android.view.IWindow window) {
    }

    @java.lang.Override
    public void setWallpaperPosition(android.os.IBinder windowToken, float x, float y, float xstep, float ystep) {
    }

    @java.lang.Override
    public void setWallpaperZoomOut(android.os.IBinder windowToken, float zoom) {
    }

    @java.lang.Override
    public void setShouldZoomOutWallpaper(android.os.IBinder windowToken, boolean shouldZoom) {
    }

    @java.lang.Override
    public void wallpaperOffsetsComplete(android.os.IBinder window) {
    }

    @java.lang.Override
    public void setWallpaperDisplayOffset(android.os.IBinder windowToken, int x, int y) {
    }

    @java.lang.Override
    public android.os.Bundle sendWallpaperCommand(android.os.IBinder window, java.lang.String action, int x, int y, int z, android.os.Bundle extras, boolean sync) {
        return null;
    }

    @java.lang.Override
    public void wallpaperCommandComplete(android.os.IBinder window, android.os.Bundle result) {
    }

    @java.lang.Override
    public void onRectangleOnScreenRequested(android.os.IBinder token, android.graphics.Rect rectangle) {
    }

    @java.lang.Override
    public android.view.IWindowId getWindowId(android.os.IBinder window) {
        return null;
    }

    @java.lang.Override
    public void pokeDrawLock(android.os.IBinder window) {
    }

    @java.lang.Override
    public boolean startMovingTask(android.view.android.view.IWindow window, float startX, float startY) {
        return false;
    }

    @java.lang.Override
    public void finishMovingTask(android.view.android.view.IWindow window) {
    }

    @java.lang.Override
    public void updatePointerIcon(android.view.android.view.IWindow window) {
    }

    @java.lang.Override
    public void reparentDisplayContent(android.view.android.view.IWindow window, android.view.SurfaceControl sc, int displayId) {
    }

    @java.lang.Override
    public void updateDisplayContentLocation(android.view.android.view.IWindow window, int x, int y, int displayId) {
    }

    @java.lang.Override
    public void updateTapExcludeRegion(android.view.android.view.IWindow window, android.graphics.Region region) {
    }

    @java.lang.Override
    public void insetsModified(android.view.android.view.IWindow window, android.view.InsetsState state) {
    }

    @java.lang.Override
    public void reportSystemGestureExclusionChanged(android.view.android.view.IWindow window, java.util.List<android.graphics.Rect> exclusionRects) {
    }

    @java.lang.Override
    public void grantInputChannel(int displayId, android.view.SurfaceControl surface, android.view.IWindow window, android.os.IBinder hostInputToken, int flags, int type, android.view.InputChannel outInputChannel) {
    }

    @java.lang.Override
    public void updateInputChannel(android.os.IBinder channelToken, int displayId, android.view.SurfaceControl surface, int flags, android.graphics.Region region) {
    }

    @java.lang.Override
    public android.os.IBinder asBinder() {
        return null;
    }

    private int getSurfaceWidth(android.view.WindowManager.LayoutParams attrs) {
        final android.graphics.Rect surfaceInsets = attrs.surfaceInsets;
        return surfaceInsets != null ? (attrs.width + surfaceInsets.left) + surfaceInsets.right : attrs.width;
    }

    private int getSurfaceHeight(android.view.WindowManager.LayoutParams attrs) {
        final android.graphics.Rect surfaceInsets = attrs.surfaceInsets;
        return surfaceInsets != null ? (attrs.height + surfaceInsets.top) + surfaceInsets.bottom : attrs.height;
    }
}

