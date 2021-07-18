/**
 * Copyright (C) 2006 The Android Open Source Project
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
 * Provides low-level communication with the system window manager for
 * operations that are bound to a particular context, display or parent window.
 * Instances of this object are sensitive to the compatibility info associated
 * with the running application.
 *
 * This object implements the {@link ViewManager} interface,
 * allowing you to add any View subclass as a top-level window on the screen.
 * Additional window manager specific layout parameters are defined for
 * control over how windows are displayed.  It also implements the {@link WindowManager}
 * interface, allowing you to control the displays attached to the device.
 *
 * <p>Applications will not normally use WindowManager directly, instead relying
 * on the higher-level facilities in {@link android.app.Activity} and
 * {@link android.app.Dialog}.
 *
 * <p>Even for low-level window manager access, it is almost never correct to use
 * this class.  For example, {@link android.app.Activity#getWindowManager}
 * provides a window manager for adding windows that are associated with that
 * activity -- the window manager will not normally allow you to add arbitrary
 * windows that are not associated with an activity.
 *
 * @see WindowManager
 * @see WindowManagerGlobal
 * @unknown 
 */
public final class WindowManagerImpl implements android.view.WindowManager {
    @android.annotation.UnsupportedAppUsage
    private final android.view.WindowManagerGlobal mGlobal = android.view.WindowManagerGlobal.getInstance();

    private final android.content.Context mContext;

    private final android.view.Window mParentWindow;

    private android.os.IBinder mDefaultToken;

    public WindowManagerImpl(android.content.Context context) {
        this(context, null);
    }

    private WindowManagerImpl(android.content.Context context, android.view.Window parentWindow) {
        mContext = context;
        mParentWindow = parentWindow;
    }

    public android.view.WindowManagerImpl createLocalWindowManager(android.view.Window parentWindow) {
        return new android.view.WindowManagerImpl(mContext, parentWindow);
    }

    public android.view.WindowManagerImpl createPresentationWindowManager(android.content.Context displayContext) {
        return new android.view.WindowManagerImpl(displayContext, mParentWindow);
    }

    /**
     * Sets the window token to assign when none is specified by the client or
     * available from the parent window.
     *
     * @param token
     * 		The default token to assign.
     */
    public void setDefaultToken(android.os.IBinder token) {
        mDefaultToken = token;
    }

    @java.lang.Override
    public void addView(@android.annotation.NonNull
    android.view.View view, @android.annotation.NonNull
    android.view.ViewGroup.LayoutParams params) {
        applyDefaultToken(params);
        mGlobal.addView(view, params, mContext.getDisplay(), mParentWindow);
    }

    @java.lang.Override
    public void updateViewLayout(@android.annotation.NonNull
    android.view.View view, @android.annotation.NonNull
    android.view.ViewGroup.LayoutParams params) {
        applyDefaultToken(params);
        mGlobal.updateViewLayout(view, params);
    }

    private void applyDefaultToken(@android.annotation.NonNull
    android.view.ViewGroup.LayoutParams params) {
        // Only use the default token if we don't have a parent window.
        if ((mDefaultToken != null) && (mParentWindow == null)) {
            if (!(params instanceof android.view.WindowManager.LayoutParams)) {
                throw new java.lang.IllegalArgumentException("Params must be WindowManager.LayoutParams");
            }
            // Only use the default token if we don't already have a token.
            final android.view.WindowManager.LayoutParams wparams = ((android.view.WindowManager.LayoutParams) (params));
            if (wparams.token == null) {
                wparams.token = mDefaultToken;
            }
        }
    }

    @java.lang.Override
    public void removeView(android.view.View view) {
        mGlobal.removeView(view, false);
    }

    @java.lang.Override
    public void removeViewImmediate(android.view.View view) {
        mGlobal.removeView(view, true);
    }

    @java.lang.Override
    public void requestAppKeyboardShortcuts(final android.view.WindowManager.KeyboardShortcutsReceiver receiver, int deviceId) {
        com.android.internal.os.IResultReceiver resultReceiver = new com.android.internal.os.IResultReceiver.Stub() {
            @java.lang.Override
            public void send(int resultCode, android.os.Bundle resultData) throws android.os.RemoteException {
                java.util.List<android.view.KeyboardShortcutGroup> result = resultData.getParcelableArrayList(android.view.WindowManager.PARCEL_KEY_SHORTCUTS_ARRAY);
                receiver.onKeyboardShortcutsReceived(result);
            }
        };
        try {
            android.view.WindowManagerGlobal.getWindowManagerService().requestAppKeyboardShortcuts(resultReceiver, deviceId);
        } catch (android.os.RemoteException e) {
        }
    }

    @java.lang.Override
    public android.view.Display getDefaultDisplay() {
        return mContext.getDisplay();
    }

    @java.lang.Override
    public android.graphics.Region getCurrentImeTouchRegion() {
        try {
            return android.view.WindowManagerGlobal.getWindowManagerService().getCurrentImeTouchRegion();
        } catch (android.os.RemoteException e) {
        }
        return null;
    }

    @java.lang.Override
    public void setShouldShowWithInsecureKeyguard(int displayId, boolean shouldShow) {
        try {
            android.view.WindowManagerGlobal.getWindowManagerService().setShouldShowWithInsecureKeyguard(displayId, shouldShow);
        } catch (android.os.RemoteException e) {
        }
    }

    @java.lang.Override
    public void setShouldShowSystemDecors(int displayId, boolean shouldShow) {
        try {
            android.view.WindowManagerGlobal.getWindowManagerService().setShouldShowSystemDecors(displayId, shouldShow);
        } catch (android.os.RemoteException e) {
        }
    }

    @java.lang.Override
    public boolean shouldShowSystemDecors(int displayId) {
        try {
            return android.view.WindowManagerGlobal.getWindowManagerService().shouldShowSystemDecors(displayId);
        } catch (android.os.RemoteException e) {
        }
        return false;
    }

    @java.lang.Override
    public void setShouldShowIme(int displayId, boolean shouldShow) {
        try {
            android.view.WindowManagerGlobal.getWindowManagerService().setShouldShowIme(displayId, shouldShow);
        } catch (android.os.RemoteException e) {
        }
    }

    @java.lang.Override
    public boolean shouldShowIme(int displayId) {
        try {
            return android.view.WindowManagerGlobal.getWindowManagerService().shouldShowIme(displayId);
        } catch (android.os.RemoteException e) {
        }
        return false;
    }
}

