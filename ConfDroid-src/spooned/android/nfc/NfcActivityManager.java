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
package android.nfc;


/**
 * Manages NFC API's that are coupled to the life-cycle of an Activity.
 *
 * <p>Uses {@link Application#registerActivityLifecycleCallbacks} to hook
 * into activity life-cycle events such as onPause() and onResume().
 *
 * @unknown 
 */
public final class NfcActivityManager extends android.nfc.IAppCallback.Stub implements android.app.Application.ActivityLifecycleCallbacks {
    static final java.lang.String TAG = android.nfc.NfcAdapter.TAG;

    static final java.lang.Boolean DBG = false;

    final android.nfc.NfcAdapter mAdapter;

    // All objects in the lists are protected by this
    final java.util.List<android.nfc.NfcActivityManager.NfcApplicationState> mApps;// Application(s) that have NFC state. Usually one


    final java.util.List<android.nfc.NfcActivityManager.NfcActivityState> mActivities;// Activities that have NFC state


    /**
     * NFC State associated with an {@link Application}.
     */
    class NfcApplicationState {
        int refCount = 0;

        final android.app.Application app;

        public NfcApplicationState(android.app.Application app) {
            this.app = app;
        }

        public void register() {
            refCount++;
            if (refCount == 1) {
                this.app.registerActivityLifecycleCallbacks(android.nfc.NfcActivityManager.this);
            }
        }

        public void unregister() {
            refCount--;
            if (refCount == 0) {
                this.app.unregisterActivityLifecycleCallbacks(android.nfc.NfcActivityManager.this);
            } else
                if (refCount < 0) {
                    android.util.Log.e(android.nfc.NfcActivityManager.TAG, "-ve refcount for " + app);
                }

        }
    }

    android.nfc.NfcActivityManager.NfcApplicationState findAppState(android.app.Application app) {
        for (android.nfc.NfcActivityManager.NfcApplicationState appState : mApps) {
            if (appState.app == app) {
                return appState;
            }
        }
        return null;
    }

    void registerApplication(android.app.Application app) {
        android.nfc.NfcActivityManager.NfcApplicationState appState = findAppState(app);
        if (appState == null) {
            appState = new android.nfc.NfcActivityManager.NfcApplicationState(app);
            mApps.add(appState);
        }
        appState.register();
    }

    void unregisterApplication(android.app.Application app) {
        android.nfc.NfcActivityManager.NfcApplicationState appState = findAppState(app);
        if (appState == null) {
            android.util.Log.e(android.nfc.NfcActivityManager.TAG, "app was not registered " + app);
            return;
        }
        appState.unregister();
    }

    /**
     * NFC state associated with an {@link Activity}
     */
    class NfcActivityState {
        boolean resumed = false;

        android.app.Activity activity;

        android.nfc.NdefMessage ndefMessage = null;// static NDEF message


        android.nfc.NfcAdapter.CreateNdefMessageCallback ndefMessageCallback = null;

        android.nfc.NfcAdapter.OnNdefPushCompleteCallback onNdefPushCompleteCallback = null;

        android.nfc.NfcAdapter.CreateBeamUrisCallback uriCallback = null;

        android.net.Uri[] uris = null;

        int flags = 0;

        int readerModeFlags = 0;

        android.nfc.NfcAdapter.ReaderCallback readerCallback = null;

        android.os.Bundle readerModeExtras = null;

        android.os.Binder token;

        public NfcActivityState(android.app.Activity activity) {
            if (activity.getWindow().isDestroyed()) {
                throw new java.lang.IllegalStateException("activity is already destroyed");
            }
            // Check if activity is resumed right now, as we will not
            // immediately get a callback for that.
            resumed = activity.isResumed();
            this.activity = activity;
            this.token = new android.os.Binder();
            registerApplication(activity.getApplication());
        }

        public void destroy() {
            unregisterApplication(activity.getApplication());
            resumed = false;
            activity = null;
            ndefMessage = null;
            ndefMessageCallback = null;
            onNdefPushCompleteCallback = null;
            uriCallback = null;
            uris = null;
            readerModeFlags = 0;
            token = null;
        }

        @java.lang.Override
        public java.lang.String toString() {
            java.lang.StringBuilder s = new java.lang.StringBuilder("[").append(" ");
            s.append(ndefMessage).append(" ").append(ndefMessageCallback).append(" ");
            s.append(uriCallback).append(" ");
            if (uris != null) {
                for (android.net.Uri uri : uris) {
                    s.append(onNdefPushCompleteCallback).append(" ").append(uri).append("]");
                }
            }
            return s.toString();
        }
    }

    /**
     * find activity state from mActivities
     */
    synchronized android.nfc.NfcActivityManager.NfcActivityState findActivityState(android.app.Activity activity) {
        for (android.nfc.NfcActivityManager.NfcActivityState state : mActivities) {
            if (state.activity == activity) {
                return state;
            }
        }
        return null;
    }

    /**
     * find or create activity state from mActivities
     */
    synchronized android.nfc.NfcActivityManager.NfcActivityState getActivityState(android.app.Activity activity) {
        android.nfc.NfcActivityManager.NfcActivityState state = findActivityState(activity);
        if (state == null) {
            state = new android.nfc.NfcActivityManager.NfcActivityState(activity);
            mActivities.add(state);
        }
        return state;
    }

    synchronized android.nfc.NfcActivityManager.NfcActivityState findResumedActivityState() {
        for (android.nfc.NfcActivityManager.NfcActivityState state : mActivities) {
            if (state.resumed) {
                return state;
            }
        }
        return null;
    }

    synchronized void destroyActivityState(android.app.Activity activity) {
        android.nfc.NfcActivityManager.NfcActivityState activityState = findActivityState(activity);
        if (activityState != null) {
            activityState.destroy();
            mActivities.remove(activityState);
        }
    }

    public NfcActivityManager(android.nfc.NfcAdapter adapter) {
        mAdapter = adapter;
        mActivities = new java.util.LinkedList<android.nfc.NfcActivityManager.NfcActivityState>();
        mApps = new java.util.ArrayList<android.nfc.NfcActivityManager.NfcApplicationState>(1);// Android VM usually has 1 app

    }

    public void enableReaderMode(android.app.Activity activity, android.nfc.NfcAdapter.ReaderCallback callback, int flags, android.os.Bundle extras) {
        boolean isResumed;
        android.os.Binder token;
        synchronized(this) {
            android.nfc.NfcActivityManager.NfcActivityState state = getActivityState(activity);
            state.readerCallback = callback;
            state.readerModeFlags = flags;
            state.readerModeExtras = extras;
            token = state.token;
            isResumed = state.resumed;
        }
        if (isResumed) {
            setReaderMode(token, flags, extras);
        }
    }

    public void disableReaderMode(android.app.Activity activity) {
        boolean isResumed;
        android.os.Binder token;
        synchronized(this) {
            android.nfc.NfcActivityManager.NfcActivityState state = getActivityState(activity);
            state.readerCallback = null;
            state.readerModeFlags = 0;
            state.readerModeExtras = null;
            token = state.token;
            isResumed = state.resumed;
        }
        if (isResumed) {
            setReaderMode(token, 0, null);
        }
    }

    public void setReaderMode(android.os.Binder token, int flags, android.os.Bundle extras) {
        if (android.nfc.NfcActivityManager.DBG)
            android.util.Log.d(android.nfc.NfcActivityManager.TAG, "Setting reader mode");

        try {
            android.nfc.NfcAdapter.sService.setReaderMode(token, this, flags, extras);
        } catch (android.os.RemoteException e) {
            mAdapter.attemptDeadServiceRecovery(e);
        }
    }

    public void setNdefPushContentUri(android.app.Activity activity, android.net.Uri[] uris) {
        boolean isResumed;
        synchronized(this) {
            android.nfc.NfcActivityManager.NfcActivityState state = getActivityState(activity);
            state.uris = uris;
            isResumed = state.resumed;
        }
        if (isResumed) {
            // requestNfcServiceCallback() verifies permission also
            requestNfcServiceCallback();
        } else {
            // Crash API calls early in case NFC permission is missing
            verifyNfcPermission();
        }
    }

    public void setNdefPushContentUriCallback(android.app.Activity activity, android.nfc.NfcAdapter.CreateBeamUrisCallback callback) {
        boolean isResumed;
        synchronized(this) {
            android.nfc.NfcActivityManager.NfcActivityState state = getActivityState(activity);
            state.uriCallback = callback;
            isResumed = state.resumed;
        }
        if (isResumed) {
            // requestNfcServiceCallback() verifies permission also
            requestNfcServiceCallback();
        } else {
            // Crash API calls early in case NFC permission is missing
            verifyNfcPermission();
        }
    }

    public void setNdefPushMessage(android.app.Activity activity, android.nfc.NdefMessage message, int flags) {
        boolean isResumed;
        synchronized(this) {
            android.nfc.NfcActivityManager.NfcActivityState state = getActivityState(activity);
            state.ndefMessage = message;
            state.flags = flags;
            isResumed = state.resumed;
        }
        if (isResumed) {
            // requestNfcServiceCallback() verifies permission also
            requestNfcServiceCallback();
        } else {
            // Crash API calls early in case NFC permission is missing
            verifyNfcPermission();
        }
    }

    public void setNdefPushMessageCallback(android.app.Activity activity, android.nfc.NfcAdapter.CreateNdefMessageCallback callback, int flags) {
        boolean isResumed;
        synchronized(this) {
            android.nfc.NfcActivityManager.NfcActivityState state = getActivityState(activity);
            state.ndefMessageCallback = callback;
            state.flags = flags;
            isResumed = state.resumed;
        }
        if (isResumed) {
            // requestNfcServiceCallback() verifies permission also
            requestNfcServiceCallback();
        } else {
            // Crash API calls early in case NFC permission is missing
            verifyNfcPermission();
        }
    }

    public void setOnNdefPushCompleteCallback(android.app.Activity activity, android.nfc.NfcAdapter.OnNdefPushCompleteCallback callback) {
        boolean isResumed;
        synchronized(this) {
            android.nfc.NfcActivityManager.NfcActivityState state = getActivityState(activity);
            state.onNdefPushCompleteCallback = callback;
            isResumed = state.resumed;
        }
        if (isResumed) {
            // requestNfcServiceCallback() verifies permission also
            requestNfcServiceCallback();
        } else {
            // Crash API calls early in case NFC permission is missing
            verifyNfcPermission();
        }
    }

    /**
     * Request or unrequest NFC service callbacks.
     * Makes IPC call - do not hold lock.
     */
    void requestNfcServiceCallback() {
        try {
            android.nfc.NfcAdapter.sService.setAppCallback(this);
        } catch (android.os.RemoteException e) {
            mAdapter.attemptDeadServiceRecovery(e);
        }
    }

    void verifyNfcPermission() {
        try {
            android.nfc.NfcAdapter.sService.verifyNfcPermission();
        } catch (android.os.RemoteException e) {
            mAdapter.attemptDeadServiceRecovery(e);
        }
    }

    /**
     * Callback from NFC service, usually on binder thread
     */
    @java.lang.Override
    public android.nfc.BeamShareData createBeamShareData(byte peerLlcpVersion) {
        android.nfc.NfcAdapter.CreateNdefMessageCallback ndefCallback;
        android.nfc.NfcAdapter.CreateBeamUrisCallback urisCallback;
        android.nfc.NdefMessage message;
        android.app.Activity activity;
        android.net.Uri[] uris;
        int flags;
        android.nfc.NfcEvent event = new android.nfc.NfcEvent(mAdapter, peerLlcpVersion);
        synchronized(this) {
            android.nfc.NfcActivityManager.NfcActivityState state = findResumedActivityState();
            if (state == null)
                return null;

            ndefCallback = state.ndefMessageCallback;
            urisCallback = state.uriCallback;
            message = state.ndefMessage;
            uris = state.uris;
            flags = state.flags;
            activity = state.activity;
        }
        final long ident = android.os.Binder.clearCallingIdentity();
        try {
            // Make callbacks without lock
            if (ndefCallback != null) {
                message = ndefCallback.createNdefMessage(event);
            }
            if (urisCallback != null) {
                uris = urisCallback.createBeamUris(event);
                if (uris != null) {
                    java.util.ArrayList<android.net.Uri> validUris = new java.util.ArrayList<android.net.Uri>();
                    for (android.net.Uri uri : uris) {
                        if (uri == null) {
                            android.util.Log.e(android.nfc.NfcActivityManager.TAG, "Uri not allowed to be null.");
                            continue;
                        }
                        java.lang.String scheme = uri.getScheme();
                        if ((scheme == null) || ((!scheme.equalsIgnoreCase("file")) && (!scheme.equalsIgnoreCase("content")))) {
                            android.util.Log.e(android.nfc.NfcActivityManager.TAG, "Uri needs to have " + "either scheme file or scheme content");
                            continue;
                        }
                        uri = android.content.ContentProvider.maybeAddUserId(uri, android.os.UserHandle.myUserId());
                        validUris.add(uri);
                    }
                    uris = validUris.toArray(new android.net.Uri[validUris.size()]);
                }
            }
            if ((uris != null) && (uris.length > 0)) {
                for (android.net.Uri uri : uris) {
                    // Grant the NFC process permission to read these URIs
                    activity.grantUriPermission("com.android.nfc", uri, android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION);
                }
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(ident);
        }
        return new android.nfc.BeamShareData(message, uris, new android.os.UserHandle(android.os.UserHandle.myUserId()), flags);
    }

    /**
     * Callback from NFC service, usually on binder thread
     */
    @java.lang.Override
    public void onNdefPushComplete(byte peerLlcpVersion) {
        android.nfc.NfcAdapter.OnNdefPushCompleteCallback callback;
        synchronized(this) {
            android.nfc.NfcActivityManager.NfcActivityState state = findResumedActivityState();
            if (state == null)
                return;

            callback = state.onNdefPushCompleteCallback;
        }
        android.nfc.NfcEvent event = new android.nfc.NfcEvent(mAdapter, peerLlcpVersion);
        // Make callback without lock
        if (callback != null) {
            callback.onNdefPushComplete(event);
        }
    }

    @java.lang.Override
    public void onTagDiscovered(android.nfc.Tag tag) throws android.os.RemoteException {
        android.nfc.NfcAdapter.ReaderCallback callback;
        synchronized(this) {
            android.nfc.NfcActivityManager.NfcActivityState state = findResumedActivityState();
            if (state == null)
                return;

            callback = state.readerCallback;
        }
        // Make callback without lock
        if (callback != null) {
            callback.onTagDiscovered(tag);
        }
    }

    /**
     * Callback from Activity life-cycle, on main thread
     */
    @java.lang.Override
    public void onActivityCreated(android.app.Activity activity, android.os.Bundle savedInstanceState) {
        /* NO-OP */
    }

    /**
     * Callback from Activity life-cycle, on main thread
     */
    @java.lang.Override
    public void onActivityStarted(android.app.Activity activity) {
        /* NO-OP */
    }

    /**
     * Callback from Activity life-cycle, on main thread
     */
    @java.lang.Override
    public void onActivityResumed(android.app.Activity activity) {
        int readerModeFlags = 0;
        android.os.Bundle readerModeExtras = null;
        android.os.Binder token;
        synchronized(this) {
            android.nfc.NfcActivityManager.NfcActivityState state = findActivityState(activity);
            if (android.nfc.NfcActivityManager.DBG)
                android.util.Log.d(android.nfc.NfcActivityManager.TAG, (("onResume() for " + activity) + " ") + state);

            if (state == null)
                return;

            state.resumed = true;
            token = state.token;
            readerModeFlags = state.readerModeFlags;
            readerModeExtras = state.readerModeExtras;
        }
        if (readerModeFlags != 0) {
            setReaderMode(token, readerModeFlags, readerModeExtras);
        }
        requestNfcServiceCallback();
    }

    /**
     * Callback from Activity life-cycle, on main thread
     */
    @java.lang.Override
    public void onActivityPaused(android.app.Activity activity) {
        boolean readerModeFlagsSet;
        android.os.Binder token;
        synchronized(this) {
            android.nfc.NfcActivityManager.NfcActivityState state = findActivityState(activity);
            if (android.nfc.NfcActivityManager.DBG)
                android.util.Log.d(android.nfc.NfcActivityManager.TAG, (("onPause() for " + activity) + " ") + state);

            if (state == null)
                return;

            state.resumed = false;
            token = state.token;
            readerModeFlagsSet = state.readerModeFlags != 0;
        }
        if (readerModeFlagsSet) {
            // Restore default p2p modes
            setReaderMode(token, 0, null);
        }
    }

    /**
     * Callback from Activity life-cycle, on main thread
     */
    @java.lang.Override
    public void onActivityStopped(android.app.Activity activity) {
        /* NO-OP */
    }

    /**
     * Callback from Activity life-cycle, on main thread
     */
    @java.lang.Override
    public void onActivitySaveInstanceState(android.app.Activity activity, android.os.Bundle outState) {
        /* NO-OP */
    }

    /**
     * Callback from Activity life-cycle, on main thread
     */
    @java.lang.Override
    public void onActivityDestroyed(android.app.Activity activity) {
        synchronized(this) {
            android.nfc.NfcActivityManager.NfcActivityState state = findActivityState(activity);
            if (android.nfc.NfcActivityManager.DBG)
                android.util.Log.d(android.nfc.NfcActivityManager.TAG, (("onDestroy() for " + activity) + " ") + state);

            if (state != null) {
                // release all associated references
                destroyActivityState(activity);
            }
        }
    }
}

