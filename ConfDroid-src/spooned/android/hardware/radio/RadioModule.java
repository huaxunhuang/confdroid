/**
 * Copyright (C) 2015 The Android Open Source Project
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
package android.hardware.radio;


/**
 * A RadioModule implements the RadioTuner interface for a broadcast radio tuner physically
 * present on the device and exposed by the radio HAL.
 *
 * @unknown 
 */
public class RadioModule extends android.hardware.radio.RadioTuner {
    private long mNativeContext = 0;

    private int mId;

    private android.hardware.radio.RadioModule.NativeEventHandlerDelegate mEventHandlerDelegate;

    RadioModule(int moduleId, android.hardware.radio.RadioManager.BandConfig config, boolean withAudio, android.hardware.radio.RadioTuner.Callback callback, android.os.Handler handler) {
        mId = moduleId;
        mEventHandlerDelegate = new android.hardware.radio.RadioModule.NativeEventHandlerDelegate(callback, handler);
        native_setup(new java.lang.ref.WeakReference<android.hardware.radio.RadioModule>(this), config, withAudio);
    }

    private native void native_setup(java.lang.Object module_this, android.hardware.radio.RadioManager.BandConfig config, boolean withAudio);

    @java.lang.Override
    protected void finalize() {
        native_finalize();
    }

    private native void native_finalize();

    boolean initCheck() {
        return mNativeContext != 0;
    }

    // RadioTuner implementation
    public native void close();

    public native int setConfiguration(android.hardware.radio.RadioManager.BandConfig config);

    public native int getConfiguration(android.hardware.radio.RadioManager.BandConfig[] config);

    public native int setMute(boolean mute);

    public native boolean getMute();

    public native int step(int direction, boolean skipSubChannel);

    public native int scan(int direction, boolean skipSubChannel);

    public native int tune(int channel, int subChannel);

    public native int cancel();

    public native int getProgramInformation(android.hardware.radio.RadioManager.ProgramInfo[] info);

    public native boolean isAntennaConnected();

    public native boolean hasControl();

    /* keep in sync with radio_event_type_t in system/core/include/system/radio.h */
    static final int EVENT_HW_FAILURE = 0;

    static final int EVENT_CONFIG = 1;

    static final int EVENT_ANTENNA = 2;

    static final int EVENT_TUNED = 3;

    static final int EVENT_METADATA = 4;

    static final int EVENT_TA = 5;

    static final int EVENT_AF_SWITCH = 6;

    static final int EVENT_EA = 7;

    static final int EVENT_CONTROL = 100;

    static final int EVENT_SERVER_DIED = 101;

    private class NativeEventHandlerDelegate {
        private final android.os.Handler mHandler;

        NativeEventHandlerDelegate(final android.hardware.radio.RadioTuner.Callback callback, android.os.Handler handler) {
            // find the looper for our new event handler
            android.os.Looper looper;
            if (handler != null) {
                looper = handler.getLooper();
            } else {
                looper = android.os.Looper.getMainLooper();
            }
            // construct the event handler with this looper
            if (looper != null) {
                // implement the event handler delegate
                mHandler = new android.os.Handler(looper) {
                    @java.lang.Override
                    public void handleMessage(android.os.Message msg) {
                        switch (msg.what) {
                            case android.hardware.radio.RadioModule.EVENT_HW_FAILURE :
                                if (callback != null) {
                                    callback.onError(android.hardware.radio.RadioTuner.ERROR_HARDWARE_FAILURE);
                                }
                                break;
                            case android.hardware.radio.RadioModule.EVENT_CONFIG :
                                {
                                    android.hardware.radio.RadioManager.BandConfig config = ((android.hardware.radio.RadioManager.BandConfig) (msg.obj));
                                    switch (msg.arg1) {
                                        case android.hardware.radio.RadioManager.STATUS_OK :
                                            if (callback != null) {
                                                callback.onConfigurationChanged(config);
                                            }
                                            break;
                                        default :
                                            if (callback != null) {
                                                callback.onError(android.hardware.radio.RadioTuner.ERROR_CONFIG);
                                            }
                                            break;
                                    }
                                }
                                break;
                            case android.hardware.radio.RadioModule.EVENT_ANTENNA :
                                if (callback != null) {
                                    callback.onAntennaState(msg.arg2 == 1);
                                }
                                break;
                            case android.hardware.radio.RadioModule.EVENT_AF_SWITCH :
                            case android.hardware.radio.RadioModule.EVENT_TUNED :
                                {
                                    android.hardware.radio.RadioManager.ProgramInfo info = ((android.hardware.radio.RadioManager.ProgramInfo) (msg.obj));
                                    switch (msg.arg1) {
                                        case android.hardware.radio.RadioManager.STATUS_OK :
                                            if (callback != null) {
                                                callback.onProgramInfoChanged(info);
                                            }
                                            break;
                                        case android.hardware.radio.RadioManager.STATUS_TIMED_OUT :
                                            if (callback != null) {
                                                callback.onError(android.hardware.radio.RadioTuner.ERROR_SCAN_TIMEOUT);
                                            }
                                            break;
                                        case android.hardware.radio.RadioManager.STATUS_INVALID_OPERATION :
                                        default :
                                            if (callback != null) {
                                                callback.onError(android.hardware.radio.RadioTuner.ERROR_CANCELLED);
                                            }
                                            break;
                                    }
                                }
                                break;
                            case android.hardware.radio.RadioModule.EVENT_METADATA :
                                {
                                    android.hardware.radio.RadioMetadata metadata = ((android.hardware.radio.RadioMetadata) (msg.obj));
                                    if (callback != null) {
                                        callback.onMetadataChanged(metadata);
                                    }
                                }
                                break;
                            case android.hardware.radio.RadioModule.EVENT_TA :
                                if (callback != null) {
                                    callback.onTrafficAnnouncement(msg.arg2 == 1);
                                }
                                break;
                            case android.hardware.radio.RadioModule.EVENT_EA :
                                if (callback != null) {
                                    callback.onEmergencyAnnouncement(msg.arg2 == 1);
                                }
                            case android.hardware.radio.RadioModule.EVENT_CONTROL :
                                if (callback != null) {
                                    callback.onControlChanged(msg.arg2 == 1);
                                }
                                break;
                            case android.hardware.radio.RadioModule.EVENT_SERVER_DIED :
                                if (callback != null) {
                                    callback.onError(android.hardware.radio.RadioTuner.ERROR_SERVER_DIED);
                                }
                                break;
                            default :
                                // Should not happen
                                break;
                        }
                    }
                };
            } else {
                mHandler = null;
            }
        }

        android.os.Handler handler() {
            return mHandler;
        }
    }

    @java.lang.SuppressWarnings("unused")
    private static void postEventFromNative(java.lang.Object module_ref, int what, int arg1, int arg2, java.lang.Object obj) {
        android.hardware.radio.RadioModule module = ((android.hardware.radio.RadioModule) (((java.lang.ref.WeakReference) (module_ref)).get()));
        if (module == null) {
            return;
        }
        android.hardware.radio.RadioModule.NativeEventHandlerDelegate delegate = module.mEventHandlerDelegate;
        if (delegate != null) {
            android.os.Handler handler = delegate.handler();
            if (handler != null) {
                android.os.Message m = handler.obtainMessage(what, arg1, arg2, obj);
                handler.sendMessage(m);
            }
        }
    }
}

