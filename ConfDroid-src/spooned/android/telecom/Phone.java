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
package android.telecom;


/**
 * A unified virtual device providing a means of voice (and other) communication on a device.
 *
 * @unknown 
 * @deprecated Use {@link InCallService} directly instead of using this class.
 */
@android.annotation.SystemApi
@java.lang.Deprecated
public final class Phone {
    public static abstract class Listener {
        /**
         * Called when the audio state changes.
         *
         * @param phone
         * 		The {@code Phone} calling this method.
         * @param audioState
         * 		The new {@link AudioState}.
         * @deprecated Use {@link #onCallAudioStateChanged(Phone, CallAudioState)} instead.
         */
        @java.lang.Deprecated
        public void onAudioStateChanged(android.telecom.Phone phone, android.telecom.AudioState audioState) {
        }

        /**
         * Called when the audio state changes.
         *
         * @param phone
         * 		The {@code Phone} calling this method.
         * @param callAudioState
         * 		The new {@link CallAudioState}.
         */
        public void onCallAudioStateChanged(android.telecom.Phone phone, android.telecom.CallAudioState callAudioState) {
        }

        /**
         * Called to bring the in-call screen to the foreground. The in-call experience should
         * respond immediately by coming to the foreground to inform the user of the state of
         * ongoing {@code Call}s.
         *
         * @param phone
         * 		The {@code Phone} calling this method.
         * @param showDialpad
         * 		If true, put up the dialpad when the screen is shown.
         */
        public void onBringToForeground(android.telecom.Phone phone, boolean showDialpad) {
        }

        /**
         * Called when a {@code Call} has been added to this in-call session. The in-call user
         * experience should add necessary state listeners to the specified {@code Call} and
         * immediately start to show the user information about the existence
         * and nature of this {@code Call}. Subsequent invocations of {@link #getCalls()} will
         * include this {@code Call}.
         *
         * @param phone
         * 		The {@code Phone} calling this method.
         * @param call
         * 		A newly added {@code Call}.
         */
        public void onCallAdded(android.telecom.Phone phone, android.telecom.Call call) {
        }

        /**
         * Called when a {@code Call} has been removed from this in-call session. The in-call user
         * experience should remove any state listeners from the specified {@code Call} and
         * immediately stop displaying any information about this {@code Call}.
         * Subsequent invocations of {@link #getCalls()} will no longer include this {@code Call}.
         *
         * @param phone
         * 		The {@code Phone} calling this method.
         * @param call
         * 		A newly removed {@code Call}.
         */
        public void onCallRemoved(android.telecom.Phone phone, android.telecom.Call call) {
        }

        /**
         * Called when the {@code Phone} ability to add more calls changes.  If the phone cannot
         * support more calls then {@code canAddCall} is set to {@code false}.  If it can, then it
         * is set to {@code true}.
         *
         * @param phone
         * 		The {@code Phone} calling this method.
         * @param canAddCall
         * 		Indicates whether an additional call can be added.
         */
        public void onCanAddCallChanged(android.telecom.Phone phone, boolean canAddCall) {
        }

        /**
         * Called to silence the ringer if a ringing call exists.
         *
         * @param phone
         * 		The {@code Phone} calling this method.
         */
        public void onSilenceRinger(android.telecom.Phone phone) {
        }
    }

    // A Map allows us to track each Call by its Telecom-specified call ID
    private final java.util.Map<java.lang.String, android.telecom.Call> mCallByTelecomCallId = new android.util.ArrayMap<>();

    // A List allows us to keep the Calls in a stable iteration order so that casually developed
    // user interface components do not incur any spurious jank
    private final java.util.List<android.telecom.Call> mCalls = new java.util.concurrent.CopyOnWriteArrayList<>();

    // An unmodifiable view of the above List can be safely shared with subclass implementations
    private final java.util.List<android.telecom.Call> mUnmodifiableCalls = java.util.Collections.unmodifiableList(mCalls);

    private final android.telecom.InCallAdapter mInCallAdapter;

    private android.telecom.CallAudioState mCallAudioState;

    private final java.util.List<android.telecom.Phone.Listener> mListeners = new java.util.concurrent.CopyOnWriteArrayList<>();

    private boolean mCanAddCall = true;

    Phone(android.telecom.InCallAdapter adapter) {
        mInCallAdapter = adapter;
    }

    final void internalAddCall(android.telecom.ParcelableCall parcelableCall) {
        android.telecom.Call call = new android.telecom.Call(this, parcelableCall.getId(), mInCallAdapter, parcelableCall.getState());
        mCallByTelecomCallId.put(parcelableCall.getId(), call);
        mCalls.add(call);
        checkCallTree(parcelableCall);
        call.internalUpdate(parcelableCall, mCallByTelecomCallId);
        fireCallAdded(call);
    }

    final void internalRemoveCall(android.telecom.Call call) {
        mCallByTelecomCallId.remove(call.internalGetCallId());
        mCalls.remove(call);
        android.telecom.InCallService.VideoCall videoCall = call.getVideoCall();
        if (videoCall != null) {
            videoCall.destroy();
        }
        fireCallRemoved(call);
    }

    final void internalUpdateCall(android.telecom.ParcelableCall parcelableCall) {
        android.telecom.Call call = mCallByTelecomCallId.get(parcelableCall.getId());
        if (call != null) {
            checkCallTree(parcelableCall);
            call.internalUpdate(parcelableCall, mCallByTelecomCallId);
        }
    }

    final void internalSetPostDialWait(java.lang.String telecomId, java.lang.String remaining) {
        android.telecom.Call call = mCallByTelecomCallId.get(telecomId);
        if (call != null) {
            call.internalSetPostDialWait(remaining);
        }
    }

    final void internalCallAudioStateChanged(android.telecom.CallAudioState callAudioState) {
        if (!java.util.Objects.equals(mCallAudioState, callAudioState)) {
            mCallAudioState = callAudioState;
            fireCallAudioStateChanged(callAudioState);
        }
    }

    final android.telecom.Call internalGetCallByTelecomId(java.lang.String telecomId) {
        return mCallByTelecomCallId.get(telecomId);
    }

    final void internalBringToForeground(boolean showDialpad) {
        fireBringToForeground(showDialpad);
    }

    final void internalSetCanAddCall(boolean canAddCall) {
        if (mCanAddCall != canAddCall) {
            mCanAddCall = canAddCall;
            fireCanAddCallChanged(canAddCall);
        }
    }

    final void internalSilenceRinger() {
        fireSilenceRinger();
    }

    final void internalOnConnectionEvent(java.lang.String telecomId, java.lang.String event, android.os.Bundle extras) {
        android.telecom.Call call = mCallByTelecomCallId.get(telecomId);
        if (call != null) {
            call.internalOnConnectionEvent(event, extras);
        }
    }

    /**
     * Called to destroy the phone and cleanup any lingering calls.
     */
    final void destroy() {
        for (android.telecom.Call call : mCalls) {
            android.telecom.InCallService.VideoCall videoCall = call.getVideoCall();
            if (videoCall != null) {
                videoCall.destroy();
            }
            if (call.getState() != android.telecom.Call.STATE_DISCONNECTED) {
                call.internalSetDisconnected();
            }
        }
    }

    /**
     * Adds a listener to this {@code Phone}.
     *
     * @param listener
     * 		A {@code Listener} object.
     */
    public final void addListener(android.telecom.Phone.Listener listener) {
        mListeners.add(listener);
    }

    /**
     * Removes a listener from this {@code Phone}.
     *
     * @param listener
     * 		A {@code Listener} object.
     */
    public final void removeListener(android.telecom.Phone.Listener listener) {
        if (listener != null) {
            mListeners.remove(listener);
        }
    }

    /**
     * Obtains the current list of {@code Call}s to be displayed by this in-call experience.
     *
     * @return A list of the relevant {@code Call}s.
     */
    public final java.util.List<android.telecom.Call> getCalls() {
        return mUnmodifiableCalls;
    }

    /**
     * Returns if the {@code Phone} can support additional calls.
     *
     * @return Whether the phone supports adding more calls.
     */
    public final boolean canAddCall() {
        return mCanAddCall;
    }

    /**
     * Sets the microphone mute state. When this request is honored, there will be change to
     * the {@link #getAudioState()}.
     *
     * @param state
     * 		{@code true} if the microphone should be muted; {@code false} otherwise.
     */
    public final void setMuted(boolean state) {
        mInCallAdapter.mute(state);
    }

    /**
     * Sets the audio route (speaker, bluetooth, etc...).  When this request is honored, there will
     * be change to the {@link #getAudioState()}.
     *
     * @param route
     * 		The audio route to use.
     */
    public final void setAudioRoute(int route) {
        mInCallAdapter.setAudioRoute(route);
    }

    /**
     * Turns the proximity sensor on. When this request is made, the proximity sensor will
     * become active, and the touch screen and display will be turned off when the user's face
     * is detected to be in close proximity to the screen. This operation is a no-op on devices
     * that do not have a proximity sensor.
     *
     * @unknown 
     */
    public final void setProximitySensorOn() {
        mInCallAdapter.turnProximitySensorOn();
    }

    /**
     * Turns the proximity sensor off. When this request is made, the proximity sensor will
     * become inactive, and no longer affect the touch screen and display. This operation is a
     * no-op on devices that do not have a proximity sensor.
     *
     * @param screenOnImmediately
     * 		If true, the screen will be turned on immediately if it was
     * 		previously off. Otherwise, the screen will only be turned on after the proximity sensor
     * 		is no longer triggered.
     * @unknown 
     */
    public final void setProximitySensorOff(boolean screenOnImmediately) {
        mInCallAdapter.turnProximitySensorOff(screenOnImmediately);
    }

    /**
     * Obtains the current phone call audio state of the {@code Phone}.
     *
     * @return An object encapsulating the audio state.
     * @deprecated Use {@link #getCallAudioState()} instead.
     */
    @java.lang.Deprecated
    public final android.telecom.AudioState getAudioState() {
        return new android.telecom.AudioState(mCallAudioState);
    }

    /**
     * Obtains the current phone call audio state of the {@code Phone}.
     *
     * @return An object encapsulating the audio state.
     */
    public final android.telecom.CallAudioState getCallAudioState() {
        return mCallAudioState;
    }

    private void fireCallAdded(android.telecom.Call call) {
        for (android.telecom.Phone.Listener listener : mListeners) {
            listener.onCallAdded(this, call);
        }
    }

    private void fireCallRemoved(android.telecom.Call call) {
        for (android.telecom.Phone.Listener listener : mListeners) {
            listener.onCallRemoved(this, call);
        }
    }

    private void fireCallAudioStateChanged(android.telecom.CallAudioState audioState) {
        for (android.telecom.Phone.Listener listener : mListeners) {
            listener.onCallAudioStateChanged(this, audioState);
            listener.onAudioStateChanged(this, new android.telecom.AudioState(audioState));
        }
    }

    private void fireBringToForeground(boolean showDialpad) {
        for (android.telecom.Phone.Listener listener : mListeners) {
            listener.onBringToForeground(this, showDialpad);
        }
    }

    private void fireCanAddCallChanged(boolean canAddCall) {
        for (android.telecom.Phone.Listener listener : mListeners) {
            listener.onCanAddCallChanged(this, canAddCall);
        }
    }

    private void fireSilenceRinger() {
        for (android.telecom.Phone.Listener listener : mListeners) {
            listener.onSilenceRinger(this);
        }
    }

    private void checkCallTree(android.telecom.ParcelableCall parcelableCall) {
        if (parcelableCall.getChildCallIds() != null) {
            for (int i = 0; i < parcelableCall.getChildCallIds().size(); i++) {
                if (!mCallByTelecomCallId.containsKey(parcelableCall.getChildCallIds().get(i))) {
                    android.telecom.Log.wtf(this, "ParcelableCall %s has nonexistent child %s", parcelableCall.getId(), parcelableCall.getChildCallIds().get(i));
                }
            }
        }
    }
}

