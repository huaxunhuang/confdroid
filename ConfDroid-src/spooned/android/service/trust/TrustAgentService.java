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
package android.service.trust;


/**
 * A service that notifies the system about whether it believes the environment of the device
 * to be trusted.
 *
 * <p>Trust agents may only be provided by the platform. It is expected that there is only
 * one trust agent installed on the platform. In the event there is more than one,
 * either trust agent can enable trust.
 * </p>
 *
 * <p>To extend this class, you must declare the service in your manifest file with
 * the {@link android.Manifest.permission#BIND_TRUST_AGENT} permission
 * and include an intent filter with the {@link #SERVICE_INTERFACE} action. For example:</p>
 * <pre>
 * &lt;service android:name=".TrustAgent"
 *          android:label="&#64;string/service_name"
 *          android:permission="android.permission.BIND_TRUST_AGENT">
 *     &lt;intent-filter>
 *         &lt;action android:name="android.service.trust.TrustAgentService" />
 *     &lt;/intent-filter>
 *     &lt;meta-data android:name="android.service.trust.trustagent"
 *          android:value="&#64;xml/trust_agent" />
 * &lt;/service></pre>
 *
 * <p>The associated meta-data file can specify an activity that is accessible through Settings
 * and should allow configuring the trust agent, as defined in
 * {@link android.R.styleable#TrustAgent}. For example:</p>
 *
 * <pre>
 * &lt;trust-agent xmlns:android="http://schemas.android.com/apk/res/android"
 *          android:settingsActivity=".TrustAgentSettings" /></pre>
 *
 * @unknown 
 */
@android.annotation.SystemApi
public class TrustAgentService extends android.app.Service {
    private final java.lang.String TAG = ((android.service.trust.TrustAgentService.class.getSimpleName() + "[") + getClass().getSimpleName()) + "]";

    private static final boolean DEBUG = false;

    /**
     * The {@link Intent} that must be declared as handled by the service.
     */
    @android.annotation.SdkConstant(android.annotation.SdkConstant.SdkConstantType.SERVICE_ACTION)
    public static final java.lang.String SERVICE_INTERFACE = "android.service.trust.TrustAgentService";

    /**
     * The name of the {@code meta-data} tag pointing to additional configuration of the trust
     * agent.
     */
    public static final java.lang.String TRUST_AGENT_META_DATA = "android.service.trust.trustagent";

    /**
     * Flag for {@link #grantTrust(CharSequence, long, int)} indicating that trust is being granted
     * as the direct result of user action - such as solving a security challenge. The hint is used
     * by the system to optimize the experience. Behavior may vary by device and release, so
     * one should only set this parameter if it meets the above criteria rather than relying on
     * the behavior of any particular device or release.
     */
    public static final int FLAG_GRANT_TRUST_INITIATED_BY_USER = 1 << 0;

    /**
     * Flag for {@link #grantTrust(CharSequence, long, int)} indicating that the agent would like
     * to dismiss the keyguard. When using this flag, the {@code TrustAgentService} must ensure
     * it is only set in response to a direct user action with the expectation of dismissing the
     * keyguard.
     */
    public static final int FLAG_GRANT_TRUST_DISMISS_KEYGUARD = 1 << 1;

    /**
     *
     *
     * @unknown 
     */
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    @android.annotation.IntDef(flag = true, value = { android.service.trust.TrustAgentService.FLAG_GRANT_TRUST_INITIATED_BY_USER, android.service.trust.TrustAgentService.FLAG_GRANT_TRUST_DISMISS_KEYGUARD })
    public @interface GrantTrustFlags {}

    private static final int MSG_UNLOCK_ATTEMPT = 1;

    private static final int MSG_CONFIGURE = 2;

    private static final int MSG_TRUST_TIMEOUT = 3;

    private static final int MSG_DEVICE_LOCKED = 4;

    private static final int MSG_DEVICE_UNLOCKED = 5;

    /**
     * Class containing raw data for a given configuration request.
     */
    private static final class ConfigurationData {
        final android.os.IBinder token;

        final java.util.List<android.os.PersistableBundle> options;

        ConfigurationData(java.util.List<android.os.PersistableBundle> opts, android.os.IBinder t) {
            options = opts;
            token = t;
        }
    }

    private android.service.trust.ITrustAgentServiceCallback mCallback;

    private java.lang.Runnable mPendingGrantTrustTask;

    private boolean mManagingTrust;

    // Lock used to access mPendingGrantTrustTask and mCallback.
    private final java.lang.Object mLock = new java.lang.Object();

    private android.os.Handler mHandler = new android.os.Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case android.service.trust.TrustAgentService.MSG_UNLOCK_ATTEMPT :
                    onUnlockAttempt(msg.arg1 != 0);
                    break;
                case android.service.trust.TrustAgentService.MSG_CONFIGURE :
                    android.service.trust.TrustAgentService.ConfigurationData data = ((android.service.trust.TrustAgentService.ConfigurationData) (msg.obj));
                    boolean result = onConfigure(data.options);
                    if (data.token != null) {
                        try {
                            synchronized(mLock) {
                                android.service.trust.TrustAgentService.this.mCallback.onConfigureCompleted(result, data.token);
                            }
                        } catch (android.os.RemoteException e) {
                            onError("calling onSetTrustAgentFeaturesEnabledCompleted()");
                        }
                    }
                    break;
                case android.service.trust.TrustAgentService.MSG_TRUST_TIMEOUT :
                    onTrustTimeout();
                    break;
                case android.service.trust.TrustAgentService.MSG_DEVICE_LOCKED :
                    onDeviceLocked();
                    break;
                case android.service.trust.TrustAgentService.MSG_DEVICE_UNLOCKED :
                    onDeviceUnlocked();
                    break;
            }
        }
    };

    @java.lang.Override
    public void onCreate() {
        super.onCreate();
        android.content.ComponentName component = new android.content.ComponentName(this, getClass());
        try {
            android.content.pm.ServiceInfo serviceInfo = /* flags */
            getPackageManager().getServiceInfo(component, 0);
            if (!Manifest.permission.BIND_TRUST_AGENT.equals(serviceInfo.permission)) {
                throw new java.lang.IllegalStateException((((component.flattenToShortString() + " is not declared with the permission ") + "\"") + Manifest.permission.BIND_TRUST_AGENT) + "\"");
            }
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            android.util.Log.e(TAG, "Can't get ServiceInfo for " + component.toShortString());
        }
    }

    /**
     * Called after the user attempts to authenticate in keyguard with their device credentials,
     * such as pin, pattern or password.
     *
     * @param successful
     * 		true if the user successfully completed the challenge.
     */
    public void onUnlockAttempt(boolean successful) {
    }

    /**
     * Called when the timeout provided by the agent expires.  Note that this may be called earlier
     * than requested by the agent if the trust timeout is adjusted by the system or
     * {@link DevicePolicyManager}.  The agent is expected to re-evaluate the trust state and only
     * call {@link #grantTrust(CharSequence, long, boolean)} if the trust state should be
     * continued.
     */
    public void onTrustTimeout() {
    }

    /**
     * Called when the device enters a state where a PIN, pattern or
     * password must be entered to unlock it.
     */
    public void onDeviceLocked() {
    }

    /**
     * Called when the device leaves a state where a PIN, pattern or
     * password must be entered to unlock it.
     */
    public void onDeviceUnlocked() {
    }

    private void onError(java.lang.String msg) {
        android.util.Slog.v(TAG, "Remote exception while " + msg);
    }

    /**
     * Called when device policy admin wants to enable specific options for agent in response to
     * {@link DevicePolicyManager#setKeyguardDisabledFeatures(ComponentName, int)} and
     * {@link DevicePolicyManager#setTrustAgentConfiguration(ComponentName, ComponentName,
     * PersistableBundle)}.
     * <p>Agents that support configuration options should overload this method and return 'true'.
     *
     * @param options
     * 		The aggregated list of options or an empty list if no restrictions apply.
     * @return true if the {@link TrustAgentService} supports configuration options.
     */
    public boolean onConfigure(java.util.List<android.os.PersistableBundle> options) {
        return false;
    }

    /**
     * Call to grant trust on the device.
     *
     * @param message
     * 		describes why the device is trusted, e.g. "Trusted by location".
     * @param durationMs
     * 		amount of time in milliseconds to keep the device in a trusted state.
     * 		Trust for this agent will automatically be revoked when the timeout expires unless
     * 		extended by a subsequent call to this function. The timeout is measured from the
     * 		invocation of this function as dictated by {@link SystemClock#elapsedRealtime())}.
     * 		For security reasons, the value should be no larger than necessary.
     * 		The value may be adjusted by the system as necessary to comply with a policy controlled
     * 		by the system or {@link DevicePolicyManager} restrictions. See {@link #onTrustTimeout()}
     * 		for determining when trust expires.
     * @param initiatedByUser
     * 		this is a hint to the system that trust is being granted as the
     * 		direct result of user action - such as solving a security challenge. The hint is used
     * 		by the system to optimize the experience. Behavior may vary by device and release, so
     * 		one should only set this parameter if it meets the above criteria rather than relying on
     * 		the behavior of any particular device or release. Corresponds to
     * 		{@link #FLAG_GRANT_TRUST_INITIATED_BY_USER}.
     * @throws IllegalStateException
     * 		if the agent is not currently managing trust.
     * @deprecated use {@link #grantTrust(CharSequence, long, int)} instead.
     */
    @java.lang.Deprecated
    public final void grantTrust(final java.lang.CharSequence message, final long durationMs, final boolean initiatedByUser) {
        grantTrust(message, durationMs, initiatedByUser ? android.service.trust.TrustAgentService.FLAG_GRANT_TRUST_INITIATED_BY_USER : 0);
    }

    /**
     * Call to grant trust on the device.
     *
     * @param message
     * 		describes why the device is trusted, e.g. "Trusted by location".
     * @param durationMs
     * 		amount of time in milliseconds to keep the device in a trusted state.
     * 		Trust for this agent will automatically be revoked when the timeout expires unless
     * 		extended by a subsequent call to this function. The timeout is measured from the
     * 		invocation of this function as dictated by {@link SystemClock#elapsedRealtime())}.
     * 		For security reasons, the value should be no larger than necessary.
     * 		The value may be adjusted by the system as necessary to comply with a policy controlled
     * 		by the system or {@link DevicePolicyManager} restrictions. See {@link #onTrustTimeout()}
     * 		for determining when trust expires.
     * @param flags
     * 		TBDocumented
     * @throws IllegalStateException
     * 		if the agent is not currently managing trust.
     */
    public final void grantTrust(final java.lang.CharSequence message, final long durationMs, @android.service.trust.TrustAgentService.GrantTrustFlags
    final int flags) {
        synchronized(mLock) {
            if (!mManagingTrust) {
                throw new java.lang.IllegalStateException("Cannot grant trust if agent is not managing trust." + " Call setManagingTrust(true) first.");
            }
            if (mCallback != null) {
                try {
                    mCallback.grantTrust(message.toString(), durationMs, flags);
                } catch (android.os.RemoteException e) {
                    onError("calling enableTrust()");
                }
            } else {
                // Remember trust has been granted so we can effectively grant it once the service
                // is bound.
                mPendingGrantTrustTask = new java.lang.Runnable() {
                    @java.lang.Override
                    public void run() {
                        grantTrust(message, durationMs, flags);
                    }
                };
            }
        }
    }

    /**
     * Call to revoke trust on the device.
     */
    public final void revokeTrust() {
        synchronized(mLock) {
            if (mPendingGrantTrustTask != null) {
                mPendingGrantTrustTask = null;
            }
            if (mCallback != null) {
                try {
                    mCallback.revokeTrust();
                } catch (android.os.RemoteException e) {
                    onError("calling revokeTrust()");
                }
            }
        }
    }

    /**
     * Call to notify the system if the agent is ready to manage trust.
     *
     * This property is not persistent across recreating the service and defaults to false.
     * Therefore this method is typically called when initializing the agent in {@link #onCreate}.
     *
     * @param managingTrust
     * 		indicates if the agent would like to manage trust.
     */
    public final void setManagingTrust(boolean managingTrust) {
        synchronized(mLock) {
            if (mManagingTrust != managingTrust) {
                mManagingTrust = managingTrust;
                if (mCallback != null) {
                    try {
                        mCallback.setManagingTrust(managingTrust);
                    } catch (android.os.RemoteException e) {
                        onError("calling setManagingTrust()");
                    }
                }
            }
        }
    }

    @java.lang.Override
    public final android.os.IBinder onBind(android.content.Intent intent) {
        if (android.service.trust.TrustAgentService.DEBUG)
            android.util.Slog.v(TAG, "onBind() intent = " + intent);

        return new android.service.trust.TrustAgentService.TrustAgentServiceWrapper();
    }

    private final class TrustAgentServiceWrapper extends android.service.trust.ITrustAgentService.Stub {
        /* Binder API */
        @java.lang.Override
        public void onUnlockAttempt(boolean successful) {
            mHandler.obtainMessage(android.service.trust.TrustAgentService.MSG_UNLOCK_ATTEMPT, successful ? 1 : 0, 0).sendToTarget();
        }

        /* Binder API */
        @java.lang.Override
        public void onTrustTimeout() {
            mHandler.sendEmptyMessage(android.service.trust.TrustAgentService.MSG_TRUST_TIMEOUT);
        }

        /* Binder API */
        @java.lang.Override
        public void onConfigure(java.util.List<android.os.PersistableBundle> args, android.os.IBinder token) {
            mHandler.obtainMessage(android.service.trust.TrustAgentService.MSG_CONFIGURE, new android.service.trust.TrustAgentService.ConfigurationData(args, token)).sendToTarget();
        }

        @java.lang.Override
        public void onDeviceLocked() throws android.os.RemoteException {
            mHandler.obtainMessage(android.service.trust.TrustAgentService.MSG_DEVICE_LOCKED).sendToTarget();
        }

        @java.lang.Override
        public void onDeviceUnlocked() throws android.os.RemoteException {
            mHandler.obtainMessage(android.service.trust.TrustAgentService.MSG_DEVICE_UNLOCKED).sendToTarget();
        }

        /* Binder API */
        @java.lang.Override
        public void setCallback(android.service.trust.ITrustAgentServiceCallback callback) {
            synchronized(mLock) {
                mCallback = callback;
                // The managingTrust property is false implicitly on the server-side, so we only
                // need to set it here if the agent has decided to manage trust.
                if (mManagingTrust) {
                    try {
                        mCallback.setManagingTrust(mManagingTrust);
                    } catch (android.os.RemoteException e) {
                        onError("calling setManagingTrust()");
                    }
                }
                if (mPendingGrantTrustTask != null) {
                    mPendingGrantTrustTask.run();
                    mPendingGrantTrustTask = null;
                }
            }
        }
    }
}

