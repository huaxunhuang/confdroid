/**
 * Copyright (C) 2016 The Android Open Source Project
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
package android.telecom;


/**
 * This service can be implemented by the default dialer (see
 * {@link TelecomManager#getDefaultDialerPackage()}) to allow or disallow incoming calls before
 * they are shown to a user.
 * <p>
 * Below is an example manifest registration for a {@code CallScreeningService}.
 * <pre>
 * {@code <service android:name="your.package.YourCallScreeningServiceImplementation"
 *          android:permission="android.permission.BIND_SCREENING_SERVICE">
 *      <intent-filter>
 *          <action android:name="android.telecom.CallScreeningService"/>
 *      </intent-filter>
 * </service>}
 * </pre>
 */
public abstract class CallScreeningService extends android.app.Service {
    /**
     * The {@link Intent} that must be declared as handled by the service.
     */
    @android.annotation.SdkConstant(android.annotation.SdkConstant.SdkConstantType.SERVICE_ACTION)
    public static final java.lang.String SERVICE_INTERFACE = "android.telecom.CallScreeningService";

    private static final int MSG_SCREEN_CALL = 1;

    private final android.os.Handler mHandler = new android.os.Handler(android.os.Looper.getMainLooper()) {
        @java.lang.Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case android.telecom.CallScreeningService.MSG_SCREEN_CALL :
                    com.android.internal.os.SomeArgs args = ((com.android.internal.os.SomeArgs) (msg.obj));
                    try {
                        mCallScreeningAdapter = ((com.android.internal.telecom.ICallScreeningAdapter) (args.arg1));
                        onScreenCall(android.telecom.Call.Details.createFromParcelableCall(((android.telecom.ParcelableCall) (args.arg2))));
                    } finally {
                        args.recycle();
                    }
                    break;
            }
        }
    };

    private final class CallScreeningBinder extends com.android.internal.telecom.ICallScreeningService.Stub {
        @java.lang.Override
        public void screenCall(com.android.internal.telecom.ICallScreeningAdapter adapter, android.telecom.ParcelableCall call) {
            android.telecom.Log.v(this, "screenCall");
            com.android.internal.os.SomeArgs args = com.android.internal.os.SomeArgs.obtain();
            args.arg1 = adapter;
            args.arg2 = call;
            mHandler.obtainMessage(android.telecom.CallScreeningService.MSG_SCREEN_CALL, args).sendToTarget();
        }
    }

    private com.android.internal.telecom.ICallScreeningAdapter mCallScreeningAdapter;

    /* Information about how to respond to an incoming call. */
    public static class CallResponse {
        private final boolean mShouldDisallowCall;

        private final boolean mShouldRejectCall;

        private final boolean mShouldSkipCallLog;

        private final boolean mShouldSkipNotification;

        private CallResponse(boolean shouldDisallowCall, boolean shouldRejectCall, boolean shouldSkipCallLog, boolean shouldSkipNotification) {
            if ((!shouldDisallowCall) && ((shouldRejectCall || shouldSkipCallLog) || shouldSkipNotification)) {
                throw new java.lang.IllegalStateException("Invalid response state for allowed call.");
            }
            mShouldDisallowCall = shouldDisallowCall;
            mShouldRejectCall = shouldRejectCall;
            mShouldSkipCallLog = shouldSkipCallLog;
            mShouldSkipNotification = shouldSkipNotification;
        }

        /* @return Whether the incoming call should be blocked. */
        public boolean getDisallowCall() {
            return mShouldDisallowCall;
        }

        /* @return Whether the incoming call should be disconnected as if the user had manually
        rejected it.
         */
        public boolean getRejectCall() {
            return mShouldRejectCall;
        }

        /* @return Whether the incoming call should not be displayed in the call log. */
        public boolean getSkipCallLog() {
            return mShouldSkipCallLog;
        }

        /* @return Whether a missed call notification should not be shown for the incoming call. */
        public boolean getSkipNotification() {
            return mShouldSkipNotification;
        }

        public static class Builder {
            private boolean mShouldDisallowCall;

            private boolean mShouldRejectCall;

            private boolean mShouldSkipCallLog;

            private boolean mShouldSkipNotification;

            /* Sets whether the incoming call should be blocked. */
            public android.telecom.CallScreeningService.CallResponse.Builder setDisallowCall(boolean shouldDisallowCall) {
                mShouldDisallowCall = shouldDisallowCall;
                return this;
            }

            /* Sets whether the incoming call should be disconnected as if the user had manually
            rejected it. This property should only be set to true if the call is disallowed.
             */
            public android.telecom.CallScreeningService.CallResponse.Builder setRejectCall(boolean shouldRejectCall) {
                mShouldRejectCall = shouldRejectCall;
                return this;
            }

            /* Sets whether the incoming call should not be displayed in the call log. This property
            should only be set to true if the call is disallowed.
             */
            public android.telecom.CallScreeningService.CallResponse.Builder setSkipCallLog(boolean shouldSkipCallLog) {
                mShouldSkipCallLog = shouldSkipCallLog;
                return this;
            }

            /* Sets whether a missed call notification should not be shown for the incoming call.
            This property should only be set to true if the call is disallowed.
             */
            public android.telecom.CallScreeningService.CallResponse.Builder setSkipNotification(boolean shouldSkipNotification) {
                mShouldSkipNotification = shouldSkipNotification;
                return this;
            }

            public android.telecom.CallScreeningService.CallResponse build() {
                return new android.telecom.CallScreeningService.CallResponse(mShouldDisallowCall, mShouldRejectCall, mShouldSkipCallLog, mShouldSkipNotification);
            }
        }
    }

    public CallScreeningService() {
    }

    @java.lang.Override
    public android.os.IBinder onBind(android.content.Intent intent) {
        android.telecom.Log.v(this, "onBind");
        return new android.telecom.CallScreeningService.CallScreeningBinder();
    }

    @java.lang.Override
    public boolean onUnbind(android.content.Intent intent) {
        android.telecom.Log.v(this, "onUnbind");
        return false;
    }

    /**
     * Called when a new incoming call is added.
     * {@link CallScreeningService#respondToCall(Call.Details, CallScreeningService.CallResponse)}
     * should be called to allow or disallow the call.
     *
     * @param callDetails
     * 		Information about a new incoming call, see {@link Call.Details}.
     */
    public abstract void onScreenCall(android.telecom.Call.Details callDetails);

    /**
     * Responds to the given call, either allowing it or disallowing it.
     *
     * @param callDetails
     * 		The call to allow.
     * @param response
     * 		The {@link CallScreeningService.CallResponse} which contains information
     * 		about how to respond to a call.
     */
    public final void respondToCall(android.telecom.Call.Details callDetails, android.telecom.CallScreeningService.CallResponse response) {
        try {
            if (response.getDisallowCall()) {
                mCallScreeningAdapter.disallowCall(callDetails.getTelecomCallId(), response.getRejectCall(), !response.getSkipCallLog(), !response.getSkipNotification());
            } else {
                mCallScreeningAdapter.allowCall(callDetails.getTelecomCallId());
            }
        } catch (android.os.RemoteException e) {
        }
    }
}

