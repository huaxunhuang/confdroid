/**
 * Copyright (C) 2013 The Android Open Source Project
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
 * This is a remote object that is passed from the shell to an instrumentation
 * for enabling access to privileged operations which the shell can do and the
 * instrumentation cannot. These privileged operations are needed for implementing
 * a {@link UiAutomation} that enables across application testing by simulating
 * user actions and performing screen introspection.
 *
 * @unknown 
 */
public final class UiAutomationConnection extends android.app.IUiAutomationConnection.Stub {
    private static final int INITIAL_FROZEN_ROTATION_UNSPECIFIED = -1;

    private final android.view.IWindowManager mWindowManager = IWindowManager.Stub.asInterface(android.os.ServiceManager.getService(android.app.Service.WINDOW_SERVICE));

    private final android.view.accessibility.IAccessibilityManager mAccessibilityManager = IAccessibilityManager.Stub.asInterface(android.os.ServiceManager.getService(android.app.Service.ACCESSIBILITY_SERVICE));

    private final android.content.pm.IPackageManager mPackageManager = IPackageManager.Stub.asInterface(android.os.ServiceManager.getService("package"));

    private final java.lang.Object mLock = new java.lang.Object();

    private final android.os.Binder mToken = new android.os.Binder();

    private int mInitialFrozenRotation = android.app.UiAutomationConnection.INITIAL_FROZEN_ROTATION_UNSPECIFIED;

    private android.accessibilityservice.IAccessibilityServiceClient mClient;

    private boolean mIsShutdown;

    private int mOwningUid;

    @java.lang.Override
    public void connect(android.accessibilityservice.IAccessibilityServiceClient client, int flags) {
        if (client == null) {
            throw new java.lang.IllegalArgumentException("Client cannot be null!");
        }
        synchronized(mLock) {
            throwIfShutdownLocked();
            if (isConnectedLocked()) {
                throw new java.lang.IllegalStateException("Already connected.");
            }
            mOwningUid = android.os.Binder.getCallingUid();
            registerUiTestAutomationServiceLocked(client, flags);
            storeRotationStateLocked();
        }
    }

    @java.lang.Override
    public void disconnect() {
        synchronized(mLock) {
            throwIfCalledByNotTrustedUidLocked();
            throwIfShutdownLocked();
            if (!isConnectedLocked()) {
                throw new java.lang.IllegalStateException("Already disconnected.");
            }
            mOwningUid = -1;
            unregisterUiTestAutomationServiceLocked();
            restoreRotationStateLocked();
        }
    }

    @java.lang.Override
    public boolean injectInputEvent(android.view.InputEvent event, boolean sync) {
        synchronized(mLock) {
            throwIfCalledByNotTrustedUidLocked();
            throwIfShutdownLocked();
            throwIfNotConnectedLocked();
        }
        final int mode = (sync) ? android.hardware.input.InputManager.INJECT_INPUT_EVENT_MODE_WAIT_FOR_FINISH : android.hardware.input.InputManager.INJECT_INPUT_EVENT_MODE_ASYNC;
        final long identity = android.os.Binder.clearCallingIdentity();
        try {
            return android.hardware.input.InputManager.getInstance().injectInputEvent(event, mode);
        } finally {
            android.os.Binder.restoreCallingIdentity(identity);
        }
    }

    @java.lang.Override
    public boolean setRotation(int rotation) {
        synchronized(mLock) {
            throwIfCalledByNotTrustedUidLocked();
            throwIfShutdownLocked();
            throwIfNotConnectedLocked();
        }
        final long identity = android.os.Binder.clearCallingIdentity();
        try {
            if (rotation == android.app.UiAutomation.ROTATION_UNFREEZE) {
                mWindowManager.thawRotation();
            } else {
                mWindowManager.freezeRotation(rotation);
            }
            return true;
        } catch (android.os.RemoteException re) {
            /* ignore */
        } finally {
            android.os.Binder.restoreCallingIdentity(identity);
        }
        return false;
    }

    @java.lang.Override
    public android.graphics.Bitmap takeScreenshot(int width, int height) {
        synchronized(mLock) {
            throwIfCalledByNotTrustedUidLocked();
            throwIfShutdownLocked();
            throwIfNotConnectedLocked();
        }
        final long identity = android.os.Binder.clearCallingIdentity();
        try {
            return android.view.SurfaceControl.screenshot(width, height);
        } finally {
            android.os.Binder.restoreCallingIdentity(identity);
        }
    }

    @java.lang.Override
    public boolean clearWindowContentFrameStats(int windowId) throws android.os.RemoteException {
        synchronized(mLock) {
            throwIfCalledByNotTrustedUidLocked();
            throwIfShutdownLocked();
            throwIfNotConnectedLocked();
        }
        int callingUserId = android.os.UserHandle.getCallingUserId();
        final long identity = android.os.Binder.clearCallingIdentity();
        try {
            android.os.IBinder token = mAccessibilityManager.getWindowToken(windowId, callingUserId);
            if (token == null) {
                return false;
            }
            return mWindowManager.clearWindowContentFrameStats(token);
        } finally {
            android.os.Binder.restoreCallingIdentity(identity);
        }
    }

    @java.lang.Override
    public android.view.WindowContentFrameStats getWindowContentFrameStats(int windowId) throws android.os.RemoteException {
        synchronized(mLock) {
            throwIfCalledByNotTrustedUidLocked();
            throwIfShutdownLocked();
            throwIfNotConnectedLocked();
        }
        int callingUserId = android.os.UserHandle.getCallingUserId();
        final long identity = android.os.Binder.clearCallingIdentity();
        try {
            android.os.IBinder token = mAccessibilityManager.getWindowToken(windowId, callingUserId);
            if (token == null) {
                return null;
            }
            return mWindowManager.getWindowContentFrameStats(token);
        } finally {
            android.os.Binder.restoreCallingIdentity(identity);
        }
    }

    @java.lang.Override
    public void clearWindowAnimationFrameStats() {
        synchronized(mLock) {
            throwIfCalledByNotTrustedUidLocked();
            throwIfShutdownLocked();
            throwIfNotConnectedLocked();
        }
        final long identity = android.os.Binder.clearCallingIdentity();
        try {
            android.view.SurfaceControl.clearAnimationFrameStats();
        } finally {
            android.os.Binder.restoreCallingIdentity(identity);
        }
    }

    @java.lang.Override
    public android.view.WindowAnimationFrameStats getWindowAnimationFrameStats() {
        synchronized(mLock) {
            throwIfCalledByNotTrustedUidLocked();
            throwIfShutdownLocked();
            throwIfNotConnectedLocked();
        }
        final long identity = android.os.Binder.clearCallingIdentity();
        try {
            android.view.WindowAnimationFrameStats stats = new android.view.WindowAnimationFrameStats();
            android.view.SurfaceControl.getAnimationFrameStats(stats);
            return stats;
        } finally {
            android.os.Binder.restoreCallingIdentity(identity);
        }
    }

    @java.lang.Override
    public void grantRuntimePermission(java.lang.String packageName, java.lang.String permission, int userId) throws android.os.RemoteException {
        synchronized(mLock) {
            throwIfCalledByNotTrustedUidLocked();
            throwIfShutdownLocked();
            throwIfNotConnectedLocked();
        }
        final long identity = android.os.Binder.clearCallingIdentity();
        try {
            mPackageManager.grantRuntimePermission(packageName, permission, userId);
        } finally {
            android.os.Binder.restoreCallingIdentity(identity);
        }
    }

    @java.lang.Override
    public void revokeRuntimePermission(java.lang.String packageName, java.lang.String permission, int userId) throws android.os.RemoteException {
        synchronized(mLock) {
            throwIfCalledByNotTrustedUidLocked();
            throwIfShutdownLocked();
            throwIfNotConnectedLocked();
        }
        final long identity = android.os.Binder.clearCallingIdentity();
        try {
            mPackageManager.revokeRuntimePermission(packageName, permission, userId);
        } finally {
            android.os.Binder.restoreCallingIdentity(identity);
        }
    }

    @java.lang.Override
    public void executeShellCommand(final java.lang.String command, final android.os.ParcelFileDescriptor sink) throws android.os.RemoteException {
        synchronized(mLock) {
            throwIfCalledByNotTrustedUidLocked();
            throwIfShutdownLocked();
            throwIfNotConnectedLocked();
        }
        java.lang.Thread streamReader = new java.lang.Thread() {
            public void run() {
                java.io.InputStream in = null;
                java.io.OutputStream out = null;
                java.lang.Process process = null;
                try {
                    process = java.lang.Runtime.getRuntime().exec(command);
                    in = process.getInputStream();
                    out = new java.io.FileOutputStream(sink.getFileDescriptor());
                    final byte[] buffer = new byte[8192];
                    while (true) {
                        final int readByteCount = in.read(buffer);
                        if (readByteCount < 0) {
                            break;
                        }
                        out.write(buffer, 0, readByteCount);
                    } 
                } catch (java.io.IOException ioe) {
                    throw new java.lang.RuntimeException("Error running shell command", ioe);
                } finally {
                    if (process != null) {
                        process.destroy();
                    }
                    libcore.io.IoUtils.closeQuietly(out);
                    libcore.io.IoUtils.closeQuietly(sink);
                }
            }
        };
        streamReader.start();
    }

    @java.lang.Override
    public void shutdown() {
        synchronized(mLock) {
            if (isConnectedLocked()) {
                throwIfCalledByNotTrustedUidLocked();
            }
            throwIfShutdownLocked();
            mIsShutdown = true;
            if (isConnectedLocked()) {
                disconnect();
            }
        }
    }

    private void registerUiTestAutomationServiceLocked(android.accessibilityservice.IAccessibilityServiceClient client, int flags) {
        android.view.accessibility.IAccessibilityManager manager = IAccessibilityManager.Stub.asInterface(android.os.ServiceManager.getService(android.content.Context.ACCESSIBILITY_SERVICE));
        final android.accessibilityservice.AccessibilityServiceInfo info = new android.accessibilityservice.AccessibilityServiceInfo();
        info.eventTypes = android.view.accessibility.AccessibilityEvent.TYPES_ALL_MASK;
        info.feedbackType = android.accessibilityservice.AccessibilityServiceInfo.FEEDBACK_GENERIC;
        info.flags |= (android.accessibilityservice.AccessibilityServiceInfo.FLAG_INCLUDE_NOT_IMPORTANT_VIEWS | android.accessibilityservice.AccessibilityServiceInfo.FLAG_REPORT_VIEW_IDS) | android.accessibilityservice.AccessibilityServiceInfo.FLAG_FORCE_DIRECT_BOOT_AWARE;
        info.setCapabilities(((android.accessibilityservice.AccessibilityServiceInfo.CAPABILITY_CAN_RETRIEVE_WINDOW_CONTENT | android.accessibilityservice.AccessibilityServiceInfo.CAPABILITY_CAN_REQUEST_TOUCH_EXPLORATION) | android.accessibilityservice.AccessibilityServiceInfo.CAPABILITY_CAN_REQUEST_ENHANCED_WEB_ACCESSIBILITY) | android.accessibilityservice.AccessibilityServiceInfo.CAPABILITY_CAN_REQUEST_FILTER_KEY_EVENTS);
        try {
            // Calling out with a lock held is fine since if the system
            // process is gone the client calling in will be killed.
            manager.registerUiTestAutomationService(mToken, client, info, flags);
            mClient = client;
        } catch (android.os.RemoteException re) {
            throw new java.lang.IllegalStateException("Error while registering UiTestAutomationService.", re);
        }
    }

    private void unregisterUiTestAutomationServiceLocked() {
        android.view.accessibility.IAccessibilityManager manager = IAccessibilityManager.Stub.asInterface(android.os.ServiceManager.getService(android.content.Context.ACCESSIBILITY_SERVICE));
        try {
            // Calling out with a lock held is fine since if the system
            // process is gone the client calling in will be killed.
            manager.unregisterUiTestAutomationService(mClient);
            mClient = null;
        } catch (android.os.RemoteException re) {
            throw new java.lang.IllegalStateException("Error while unregistering UiTestAutomationService", re);
        }
    }

    private void storeRotationStateLocked() {
        try {
            if (mWindowManager.isRotationFrozen()) {
                // Calling out with a lock held is fine since if the system
                // process is gone the client calling in will be killed.
                mInitialFrozenRotation = mWindowManager.getRotation();
            }
        } catch (android.os.RemoteException re) {
            /* ignore */
        }
    }

    private void restoreRotationStateLocked() {
        try {
            if (mInitialFrozenRotation != android.app.UiAutomationConnection.INITIAL_FROZEN_ROTATION_UNSPECIFIED) {
                // Calling out with a lock held is fine since if the system
                // process is gone the client calling in will be killed.
                mWindowManager.freezeRotation(mInitialFrozenRotation);
            } else {
                // Calling out with a lock held is fine since if the system
                // process is gone the client calling in will be killed.
                mWindowManager.thawRotation();
            }
        } catch (android.os.RemoteException re) {
            /* ignore */
        }
    }

    private boolean isConnectedLocked() {
        return mClient != null;
    }

    private void throwIfShutdownLocked() {
        if (mIsShutdown) {
            throw new java.lang.IllegalStateException("Connection shutdown!");
        }
    }

    private void throwIfNotConnectedLocked() {
        if (!isConnectedLocked()) {
            throw new java.lang.IllegalStateException("Not connected!");
        }
    }

    private void throwIfCalledByNotTrustedUidLocked() {
        final int callingUid = android.os.Binder.getCallingUid();
        /* root */
        if (((callingUid != mOwningUid) && (mOwningUid != android.os.Process.SYSTEM_UID)) && (callingUid != 0)) {
            throw new java.lang.SecurityException("Calling from not trusted UID!");
        }
    }
}

