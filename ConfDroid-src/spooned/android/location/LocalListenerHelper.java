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
 * limitations under the License
 */
package android.location;


/**
 * A base handler class to manage transport and local listeners.
 *
 * @unknown 
 */
abstract class LocalListenerHelper<TListener> {
    private final java.util.HashMap<TListener, android.os.Handler> mListeners = new java.util.HashMap<>();

    private final java.lang.String mTag;

    private final android.content.Context mContext;

    protected LocalListenerHelper(android.content.Context context, java.lang.String name) {
        com.android.internal.util.Preconditions.checkNotNull(name);
        mContext = context;
        mTag = name;
    }

    public boolean add(@android.annotation.NonNull
    TListener listener, android.os.Handler handler) {
        com.android.internal.util.Preconditions.checkNotNull(listener);
        synchronized(mListeners) {
            // we need to register with the service first, because we need to find out if the
            // service will actually support the request before we attempt anything
            if (mListeners.isEmpty()) {
                boolean registeredWithService;
                try {
                    registeredWithService = registerWithServer();
                } catch (android.os.RemoteException e) {
                    android.util.Log.e(mTag, "Error handling first listener.", e);
                    return false;
                }
                if (!registeredWithService) {
                    android.util.Log.e(mTag, "Unable to register listener transport.");
                    return false;
                }
            }
            if (mListeners.containsKey(listener)) {
                return true;
            }
            mListeners.put(listener, handler);
            return true;
        }
    }

    public void remove(@android.annotation.NonNull
    TListener listener) {
        com.android.internal.util.Preconditions.checkNotNull(listener);
        synchronized(mListeners) {
            boolean removed = mListeners.containsKey(listener);
            mListeners.remove(listener);
            boolean isLastRemoved = removed && mListeners.isEmpty();
            if (isLastRemoved) {
                try {
                    unregisterFromServer();
                } catch (android.os.RemoteException e) {
                    android.util.Log.v(mTag, "Error handling last listener removal", e);
                }
            }
        }
    }

    protected abstract boolean registerWithServer() throws android.os.RemoteException;

    protected abstract void unregisterFromServer() throws android.os.RemoteException;

    protected interface ListenerOperation<TListener> {
        void execute(TListener listener) throws android.os.RemoteException;
    }

    protected android.content.Context getContext() {
        return mContext;
    }

    private void executeOperation(android.location.LocalListenerHelper.ListenerOperation<TListener> operation, TListener listener) {
        try {
            operation.execute(listener);
        } catch (android.os.RemoteException e) {
            android.util.Log.e(mTag, "Error in monitored listener.", e);
            // don't return, give a fair chance to all listeners to receive the event
        }
    }

    protected void foreach(final android.location.LocalListenerHelper.ListenerOperation<TListener> operation) {
        java.util.Collection<java.util.Map.Entry<TListener, android.os.Handler>> listeners;
        synchronized(mListeners) {
            listeners = new java.util.ArrayList<>(mListeners.entrySet());
        }
        for (final java.util.Map.Entry<TListener, android.os.Handler> listener : listeners) {
            if (listener.getValue() == null) {
                executeOperation(operation, listener.getKey());
            } else {
                listener.getValue().post(new java.lang.Runnable() {
                    @java.lang.Override
                    public void run() {
                        executeOperation(operation, listener.getKey());
                    }
                });
            }
        }
    }
}

