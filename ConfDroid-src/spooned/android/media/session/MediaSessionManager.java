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
package android.media.session;


/**
 * Provides support for interacting with {@link MediaSession media sessions}
 * that applications have published to express their ongoing media playback
 * state.
 * <p>
 * Use <code>Context.getSystemService(Context.MEDIA_SESSION_SERVICE)</code> to
 * get an instance of this class.
 *
 * @see MediaSession
 * @see MediaController
 */
public final class MediaSessionManager {
    private static final java.lang.String TAG = "SessionManager";

    private final android.util.ArrayMap<android.media.session.MediaSessionManager.OnActiveSessionsChangedListener, android.media.session.MediaSessionManager.SessionsChangedWrapper> mListeners = new android.util.ArrayMap<android.media.session.MediaSessionManager.OnActiveSessionsChangedListener, android.media.session.MediaSessionManager.SessionsChangedWrapper>();

    private final java.lang.Object mLock = new java.lang.Object();

    private final android.media.session.ISessionManager mService;

    private android.content.Context mContext;

    /**
     *
     *
     * @unknown 
     */
    public MediaSessionManager(android.content.Context context) {
        // Consider rewriting like DisplayManagerGlobal
        // Decide if we need context
        mContext = context;
        android.os.IBinder b = android.os.ServiceManager.getService(android.content.Context.MEDIA_SESSION_SERVICE);
        mService = ISessionManager.Stub.asInterface(b);
    }

    /**
     * Create a new session in the system and get the binder for it.
     *
     * @param tag
     * 		A short name for debugging purposes.
     * @return The binder object from the system
     * @unknown 
     */
    @android.annotation.NonNull
    public android.media.session.ISession createSession(@android.annotation.NonNull
    android.media.session.MediaSession.CallbackStub cbStub, @android.annotation.NonNull
    java.lang.String tag, int userId) throws android.os.RemoteException {
        return mService.createSession(mContext.getPackageName(), cbStub, tag, userId);
    }

    /**
     * Get a list of controllers for all ongoing sessions. The controllers will
     * be provided in priority order with the most important controller at index
     * 0.
     * <p>
     * This requires the android.Manifest.permission.MEDIA_CONTENT_CONTROL
     * permission be held by the calling app. You may also retrieve this list if
     * your app is an enabled notification listener using the
     * {@link NotificationListenerService} APIs, in which case you must pass the
     * {@link ComponentName} of your enabled listener.
     *
     * @param notificationListener
     * 		The enabled notification listener component.
     * 		May be null.
     * @return A list of controllers for ongoing sessions.
     */
    @android.annotation.NonNull
    public java.util.List<android.media.session.MediaController> getActiveSessions(@android.annotation.Nullable
    android.content.ComponentName notificationListener) {
        return getActiveSessionsForUser(notificationListener, android.os.UserHandle.myUserId());
    }

    /**
     * Get active sessions for a specific user. To retrieve actions for a user
     * other than your own you must hold the
     * {@link android.Manifest.permission#INTERACT_ACROSS_USERS_FULL} permission
     * in addition to any other requirements. If you are an enabled notification
     * listener you may only get sessions for the users you are enabled for.
     *
     * @param notificationListener
     * 		The enabled notification listener component.
     * 		May be null.
     * @param userId
     * 		The user id to fetch sessions for.
     * @return A list of controllers for ongoing sessions.
     * @unknown 
     */
    @android.annotation.NonNull
    public java.util.List<android.media.session.MediaController> getActiveSessionsForUser(@android.annotation.Nullable
    android.content.ComponentName notificationListener, int userId) {
        java.util.ArrayList<android.media.session.MediaController> controllers = new java.util.ArrayList<android.media.session.MediaController>();
        try {
            java.util.List<android.os.IBinder> binders = mService.getSessions(notificationListener, userId);
            int size = binders.size();
            for (int i = 0; i < size; i++) {
                android.media.session.MediaController controller = new android.media.session.MediaController(mContext, ISessionController.Stub.asInterface(binders.get(i)));
                controllers.add(controller);
            }
        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.media.session.MediaSessionManager.TAG, "Failed to get active sessions: ", e);
        }
        return controllers;
    }

    /**
     * Add a listener to be notified when the list of active sessions
     * changes.This requires the
     * android.Manifest.permission.MEDIA_CONTENT_CONTROL permission be held by
     * the calling app. You may also retrieve this list if your app is an
     * enabled notification listener using the
     * {@link NotificationListenerService} APIs, in which case you must pass the
     * {@link ComponentName} of your enabled listener. Updates will be posted to
     * the thread that registered the listener.
     *
     * @param sessionListener
     * 		The listener to add.
     * @param notificationListener
     * 		The enabled notification listener component.
     * 		May be null.
     */
    public void addOnActiveSessionsChangedListener(@android.annotation.NonNull
    android.media.session.MediaSessionManager.OnActiveSessionsChangedListener sessionListener, @android.annotation.Nullable
    android.content.ComponentName notificationListener) {
        addOnActiveSessionsChangedListener(sessionListener, notificationListener, null);
    }

    /**
     * Add a listener to be notified when the list of active sessions
     * changes.This requires the
     * android.Manifest.permission.MEDIA_CONTENT_CONTROL permission be held by
     * the calling app. You may also retrieve this list if your app is an
     * enabled notification listener using the
     * {@link NotificationListenerService} APIs, in which case you must pass the
     * {@link ComponentName} of your enabled listener. Updates will be posted to
     * the handler specified or to the caller's thread if the handler is null.
     *
     * @param sessionListener
     * 		The listener to add.
     * @param notificationListener
     * 		The enabled notification listener component.
     * 		May be null.
     * @param handler
     * 		The handler to post events to.
     */
    public void addOnActiveSessionsChangedListener(@android.annotation.NonNull
    android.media.session.MediaSessionManager.OnActiveSessionsChangedListener sessionListener, @android.annotation.Nullable
    android.content.ComponentName notificationListener, @android.annotation.Nullable
    android.os.Handler handler) {
        addOnActiveSessionsChangedListener(sessionListener, notificationListener, android.os.UserHandle.myUserId(), handler);
    }

    /**
     * Add a listener to be notified when the list of active sessions
     * changes.This requires the
     * android.Manifest.permission.MEDIA_CONTENT_CONTROL permission be held by
     * the calling app. You may also retrieve this list if your app is an
     * enabled notification listener using the
     * {@link NotificationListenerService} APIs, in which case you must pass the
     * {@link ComponentName} of your enabled listener.
     *
     * @param sessionListener
     * 		The listener to add.
     * @param notificationListener
     * 		The enabled notification listener component.
     * 		May be null.
     * @param userId
     * 		The userId to listen for changes on.
     * @param handler
     * 		The handler to post updates on.
     * @unknown 
     */
    public void addOnActiveSessionsChangedListener(@android.annotation.NonNull
    android.media.session.MediaSessionManager.OnActiveSessionsChangedListener sessionListener, @android.annotation.Nullable
    android.content.ComponentName notificationListener, int userId, @android.annotation.Nullable
    android.os.Handler handler) {
        if (sessionListener == null) {
            throw new java.lang.IllegalArgumentException("listener may not be null");
        }
        if (handler == null) {
            handler = new android.os.Handler();
        }
        synchronized(mLock) {
            if (mListeners.get(sessionListener) != null) {
                android.util.Log.w(android.media.session.MediaSessionManager.TAG, "Attempted to add session listener twice, ignoring.");
                return;
            }
            android.media.session.MediaSessionManager.SessionsChangedWrapper wrapper = new android.media.session.MediaSessionManager.SessionsChangedWrapper(mContext, sessionListener, handler);
            try {
                mService.addSessionsListener(wrapper.mStub, notificationListener, userId);
                mListeners.put(sessionListener, wrapper);
            } catch (android.os.RemoteException e) {
                android.util.Log.e(android.media.session.MediaSessionManager.TAG, "Error in addOnActiveSessionsChangedListener.", e);
            }
        }
    }

    /**
     * Stop receiving active sessions updates on the specified listener.
     *
     * @param listener
     * 		The listener to remove.
     */
    public void removeOnActiveSessionsChangedListener(@android.annotation.NonNull
    android.media.session.MediaSessionManager.OnActiveSessionsChangedListener listener) {
        if (listener == null) {
            throw new java.lang.IllegalArgumentException("listener may not be null");
        }
        synchronized(mLock) {
            android.media.session.MediaSessionManager.SessionsChangedWrapper wrapper = mListeners.remove(listener);
            if (wrapper != null) {
                try {
                    mService.removeSessionsListener(wrapper.mStub);
                } catch (android.os.RemoteException e) {
                    android.util.Log.e(android.media.session.MediaSessionManager.TAG, "Error in removeOnActiveSessionsChangedListener.", e);
                } finally {
                    wrapper.release();
                }
            }
        }
    }

    /**
     * Set the remote volume controller to receive volume updates on. Only for
     * use by system UI.
     *
     * @param rvc
     * 		The volume controller to receive updates on.
     * @unknown 
     */
    public void setRemoteVolumeController(android.media.IRemoteVolumeController rvc) {
        try {
            mService.setRemoteVolumeController(rvc);
        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.media.session.MediaSessionManager.TAG, "Error in setRemoteVolumeController.", e);
        }
    }

    /**
     * Send a media key event. The receiver will be selected automatically.
     *
     * @param keyEvent
     * 		The KeyEvent to send.
     * @unknown 
     */
    public void dispatchMediaKeyEvent(@android.annotation.NonNull
    android.view.KeyEvent keyEvent) {
        dispatchMediaKeyEvent(keyEvent, false);
    }

    /**
     * Send a media key event. The receiver will be selected automatically.
     *
     * @param keyEvent
     * 		The KeyEvent to send.
     * @param needWakeLock
     * 		True if a wake lock should be held while sending the key.
     * @unknown 
     */
    public void dispatchMediaKeyEvent(@android.annotation.NonNull
    android.view.KeyEvent keyEvent, boolean needWakeLock) {
        try {
            mService.dispatchMediaKeyEvent(keyEvent, needWakeLock);
        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.media.session.MediaSessionManager.TAG, "Failed to send key event.", e);
        }
    }

    /**
     * Dispatch an adjust volume request to the system. It will be sent to the
     * most relevant audio stream or media session. The direction must be one of
     * {@link AudioManager#ADJUST_LOWER}, {@link AudioManager#ADJUST_RAISE},
     * {@link AudioManager#ADJUST_SAME}.
     *
     * @param suggestedStream
     * 		The stream to fall back to if there isn't a
     * 		relevant stream
     * @param direction
     * 		The direction to adjust volume in.
     * @param flags
     * 		Any flags to include with the volume change.
     * @unknown 
     */
    public void dispatchAdjustVolume(int suggestedStream, int direction, int flags) {
        try {
            mService.dispatchAdjustVolume(suggestedStream, direction, flags);
        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.media.session.MediaSessionManager.TAG, "Failed to send adjust volume.", e);
        }
    }

    /**
     * Check if the global priority session is currently active. This can be
     * used to decide if media keys should be sent to the session or to the app.
     *
     * @unknown 
     */
    public boolean isGlobalPriorityActive() {
        try {
            return mService.isGlobalPriorityActive();
        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.media.session.MediaSessionManager.TAG, "Failed to check if the global priority is active.", e);
        }
        return false;
    }

    /**
     * Listens for changes to the list of active sessions. This can be added
     * using {@link #addOnActiveSessionsChangedListener}.
     */
    public interface OnActiveSessionsChangedListener {
        public void onActiveSessionsChanged(@android.annotation.Nullable
        java.util.List<android.media.session.MediaController> controllers);
    }

    private static final class SessionsChangedWrapper {
        private android.content.Context mContext;

        private android.media.session.MediaSessionManager.OnActiveSessionsChangedListener mListener;

        private android.os.Handler mHandler;

        public SessionsChangedWrapper(android.content.Context context, android.media.session.MediaSessionManager.OnActiveSessionsChangedListener listener, android.os.Handler handler) {
            mContext = context;
            mListener = listener;
            mHandler = handler;
        }

        private final IActiveSessionsListener.Stub mStub = new android.media.session.IActiveSessionsListener.Stub() {
            @java.lang.Override
            public void onActiveSessionsChanged(final java.util.List<android.media.session.MediaSession.Token> tokens) {
                if (mHandler != null) {
                    mHandler.post(new java.lang.Runnable() {
                        @java.lang.Override
                        public void run() {
                            if (mListener != null) {
                                java.util.ArrayList<android.media.session.MediaController> controllers = new java.util.ArrayList<android.media.session.MediaController>();
                                int size = tokens.size();
                                for (int i = 0; i < size; i++) {
                                    controllers.add(new android.media.session.MediaController(mContext, tokens.get(i)));
                                }
                                mListener.onActiveSessionsChanged(controllers);
                            }
                        }
                    });
                }
            }
        };

        private void release() {
            mContext = null;
            mListener = null;
            mHandler = null;
        }
    }
}

