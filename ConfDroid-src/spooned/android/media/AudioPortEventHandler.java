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
package android.media;


/**
 * The AudioPortEventHandler handles AudioManager.OnAudioPortUpdateListener callbacks
 * posted from JNI
 *
 * @unknown 
 */
class AudioPortEventHandler {
    private android.os.Handler mHandler;

    private final java.util.ArrayList<android.media.AudioManager.OnAudioPortUpdateListener> mListeners = new java.util.ArrayList<android.media.AudioManager.OnAudioPortUpdateListener>();

    private static final java.lang.String TAG = "AudioPortEventHandler";

    private static final int AUDIOPORT_EVENT_PORT_LIST_UPDATED = 1;

    private static final int AUDIOPORT_EVENT_PATCH_LIST_UPDATED = 2;

    private static final int AUDIOPORT_EVENT_SERVICE_DIED = 3;

    private static final int AUDIOPORT_EVENT_NEW_LISTENER = 4;

    /**
     * Accessed by native methods: JNI Callback context.
     */
    @java.lang.SuppressWarnings("unused")
    private long mJniCallback;

    void init() {
        synchronized(this) {
            if (mHandler != null) {
                return;
            }
            // find the looper for our new event handler
            android.os.Looper looper = android.os.Looper.getMainLooper();
            if (looper != null) {
                mHandler = new android.os.Handler(looper) {
                    @java.lang.Override
                    public void handleMessage(android.os.Message msg) {
                        java.util.ArrayList<android.media.AudioManager.OnAudioPortUpdateListener> listeners;
                        synchronized(this) {
                            if (msg.what == android.media.AudioPortEventHandler.AUDIOPORT_EVENT_NEW_LISTENER) {
                                listeners = new java.util.ArrayList<android.media.AudioManager.OnAudioPortUpdateListener>();
                                if (mListeners.contains(msg.obj)) {
                                    listeners.add(((android.media.AudioManager.OnAudioPortUpdateListener) (msg.obj)));
                                }
                            } else {
                                listeners = mListeners;
                            }
                        }
                        // reset audio port cache if the event corresponds to a change coming
                        // from audio policy service or if mediaserver process died.
                        if (((msg.what == android.media.AudioPortEventHandler.AUDIOPORT_EVENT_PORT_LIST_UPDATED) || (msg.what == android.media.AudioPortEventHandler.AUDIOPORT_EVENT_PATCH_LIST_UPDATED)) || (msg.what == android.media.AudioPortEventHandler.AUDIOPORT_EVENT_SERVICE_DIED)) {
                            android.media.AudioManager.resetAudioPortGeneration();
                        }
                        if (listeners.isEmpty()) {
                            return;
                        }
                        java.util.ArrayList<android.media.AudioPort> ports = new java.util.ArrayList<android.media.AudioPort>();
                        java.util.ArrayList<android.media.AudioPatch> patches = new java.util.ArrayList<android.media.AudioPatch>();
                        if (msg.what != android.media.AudioPortEventHandler.AUDIOPORT_EVENT_SERVICE_DIED) {
                            int status = android.media.AudioManager.updateAudioPortCache(ports, patches, null);
                            if (status != android.media.AudioManager.SUCCESS) {
                                return;
                            }
                        }
                        switch (msg.what) {
                            case android.media.AudioPortEventHandler.AUDIOPORT_EVENT_NEW_LISTENER :
                            case android.media.AudioPortEventHandler.AUDIOPORT_EVENT_PORT_LIST_UPDATED :
                                android.media.AudioPort[] portList = ports.toArray(new android.media.AudioPort[0]);
                                for (int i = 0; i < listeners.size(); i++) {
                                    listeners.get(i).onAudioPortListUpdate(portList);
                                }
                                if (msg.what == android.media.AudioPortEventHandler.AUDIOPORT_EVENT_PORT_LIST_UPDATED) {
                                    break;
                                }
                                // FALL THROUGH
                            case android.media.AudioPortEventHandler.AUDIOPORT_EVENT_PATCH_LIST_UPDATED :
                                android.media.AudioPatch[] patchList = patches.toArray(new android.media.AudioPatch[0]);
                                for (int i = 0; i < listeners.size(); i++) {
                                    listeners.get(i).onAudioPatchListUpdate(patchList);
                                }
                                break;
                            case android.media.AudioPortEventHandler.AUDIOPORT_EVENT_SERVICE_DIED :
                                for (int i = 0; i < listeners.size(); i++) {
                                    listeners.get(i).onServiceDied();
                                }
                                break;
                            default :
                                break;
                        }
                    }
                };
                native_setup(new java.lang.ref.WeakReference<android.media.AudioPortEventHandler>(this));
            } else {
                mHandler = null;
            }
        }
    }

    private native void native_setup(java.lang.Object module_this);

    @java.lang.Override
    protected void finalize() {
        native_finalize();
    }

    private native void native_finalize();

    void registerListener(android.media.AudioManager.OnAudioPortUpdateListener l) {
        synchronized(this) {
            mListeners.add(l);
        }
        if (mHandler != null) {
            android.os.Message m = mHandler.obtainMessage(android.media.AudioPortEventHandler.AUDIOPORT_EVENT_NEW_LISTENER, 0, 0, l);
            mHandler.sendMessage(m);
        }
    }

    void unregisterListener(android.media.AudioManager.OnAudioPortUpdateListener l) {
        synchronized(this) {
            mListeners.remove(l);
        }
    }

    android.os.Handler handler() {
        return mHandler;
    }

    @java.lang.SuppressWarnings("unused")
    private static void postEventFromNative(java.lang.Object module_ref, int what, int arg1, int arg2, java.lang.Object obj) {
        android.media.AudioPortEventHandler eventHandler = ((android.media.AudioPortEventHandler) (((java.lang.ref.WeakReference) (module_ref)).get()));
        if (eventHandler == null) {
            return;
        }
        if (eventHandler != null) {
            android.os.Handler handler = eventHandler.handler();
            if (handler != null) {
                android.os.Message m = handler.obtainMessage(what, arg1, arg2, obj);
                handler.sendMessage(m);
            }
        }
    }
}

