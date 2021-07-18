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
package android.service.notification;


/**
 * A service that provides conditions about boolean state.
 * <p>To extend this class, you must declare the service in your manifest file with
 * the {@link android.Manifest.permission#BIND_CONDITION_PROVIDER_SERVICE} permission
 * and include an intent filter with the {@link #SERVICE_INTERFACE} action. If you want users to be
 * able to create and update conditions for this service to monitor, include the
 * {@link #META_DATA_RULE_TYPE} and {@link #META_DATA_CONFIGURATION_ACTIVITY} tags and request the
 * {@link android.Manifest.permission#ACCESS_NOTIFICATION_POLICY} permission. For example:</p>
 * <pre>
 * &lt;service android:name=".MyConditionProvider"
 *          android:label="&#64;string/service_name"
 *          android:permission="android.permission.BIND_CONDITION_PROVIDER_SERVICE">
 *     &lt;intent-filter>
 *         &lt;action android:name="android.service.notification.ConditionProviderService" />
 *     &lt;/intent-filter>
 *     &lt;meta-data
 *               android:name="android.service.zen.automatic.ruleType"
 *               android:value="@string/my_condition_rule">
 *           &lt;/meta-data>
 *           &lt;meta-data
 *               android:name="android.service.zen.automatic.configurationActivity"
 *               android:value="com.my.package/.MyConditionConfigurationActivity">
 *           &lt;/meta-data>
 * &lt;/service></pre>
 */
public abstract class ConditionProviderService extends android.app.Service {
    private final java.lang.String TAG = ((android.service.notification.ConditionProviderService.class.getSimpleName() + "[") + getClass().getSimpleName()) + "]";

    private final android.service.notification.ConditionProviderService.H mHandler = new android.service.notification.ConditionProviderService.H();

    private android.service.notification.ConditionProviderService.Provider mProvider;

    private android.app.INotificationManager mNoMan;

    /**
     * The {@link Intent} that must be declared as handled by the service.
     */
    @android.annotation.SdkConstant(android.annotation.SdkConstant.SdkConstantType.SERVICE_ACTION)
    public static final java.lang.String SERVICE_INTERFACE = "android.service.notification.ConditionProviderService";

    /**
     * The name of the {@code meta-data} tag containing a localized name of the type of zen rules
     * provided by this service.
     */
    public static final java.lang.String META_DATA_RULE_TYPE = "android.service.zen.automatic.ruleType";

    /**
     * The name of the {@code meta-data} tag containing the {@link ComponentName} of an activity
     * that allows users to configure the conditions provided by this service.
     */
    public static final java.lang.String META_DATA_CONFIGURATION_ACTIVITY = "android.service.zen.automatic.configurationActivity";

    /**
     * The name of the {@code meta-data} tag containing the maximum number of rule instances that
     * can be created for this rule type. Omit or enter a value <= 0 to allow unlimited instances.
     */
    public static final java.lang.String META_DATA_RULE_INSTANCE_LIMIT = "android.service.zen.automatic.ruleInstanceLimit";

    /**
     * A String rule id extra passed to {@link #META_DATA_CONFIGURATION_ACTIVITY}.
     */
    public static final java.lang.String EXTRA_RULE_ID = "android.service.notification.extra.RULE_ID";

    /**
     * Called when this service is connected.
     */
    public abstract void onConnected();

    @android.annotation.SystemApi
    public void onRequestConditions(int relevance) {
    }

    /**
     * Called by the system when there is a new {@link Condition} to be managed by this provider.
     *
     * @param conditionId
     * 		the Uri describing the criteria of the condition.
     */
    public abstract void onSubscribe(android.net.Uri conditionId);

    /**
     * Called by the system when a {@link Condition} has been deleted.
     *
     * @param conditionId
     * 		the Uri describing the criteria of the deleted condition.
     */
    public abstract void onUnsubscribe(android.net.Uri conditionId);

    private final android.app.INotificationManager getNotificationInterface() {
        if (mNoMan == null) {
            mNoMan = INotificationManager.Stub.asInterface(android.os.ServiceManager.getService(android.content.Context.NOTIFICATION_SERVICE));
        }
        return mNoMan;
    }

    /**
     * Informs the notification manager that the state of a Condition has changed. Use this method
     * to put the system into Do Not Disturb mode or request that it exits Do Not Disturb mode. This
     * call will be ignored unless there is an enabled {@link android.app.AutomaticZenRule} owned by
     * service that has an {@link android.app.AutomaticZenRule#getConditionId()} equal to this
     * {@link Condition#id}.
     *
     * @param condition
     * 		the condition that has changed.
     */
    public final void notifyCondition(android.service.notification.Condition condition) {
        if (condition == null)
            return;

        notifyConditions(new android.service.notification.Condition[]{ condition });
    }

    /**
     * Informs the notification manager that the state of one or more Conditions has changed. See
     * {@link #notifyCondition(Condition)} for restrictions.
     *
     * @param conditions
     * 		the changed conditions.
     */
    public final void notifyConditions(android.service.notification.Condition... conditions) {
        if ((!isBound()) || (conditions == null))
            return;

        try {
            getNotificationInterface().notifyConditions(getPackageName(), mProvider, conditions);
        } catch (android.os.RemoteException ex) {
            android.util.Log.v(TAG, "Unable to contact notification manager", ex);
        }
    }

    @java.lang.Override
    public android.os.IBinder onBind(android.content.Intent intent) {
        if (mProvider == null) {
            mProvider = new android.service.notification.ConditionProviderService.Provider();
        }
        return mProvider;
    }

    private boolean isBound() {
        if (mProvider == null) {
            android.util.Log.w(TAG, "Condition provider service not yet bound.");
            return false;
        }
        return true;
    }

    private final class Provider extends android.service.notification.IConditionProvider.Stub {
        @java.lang.Override
        public void onConnected() {
            mHandler.obtainMessage(android.service.notification.ConditionProviderService.H.ON_CONNECTED).sendToTarget();
        }

        @java.lang.Override
        public void onSubscribe(android.net.Uri conditionId) {
            mHandler.obtainMessage(android.service.notification.ConditionProviderService.H.ON_SUBSCRIBE, conditionId).sendToTarget();
        }

        @java.lang.Override
        public void onUnsubscribe(android.net.Uri conditionId) {
            mHandler.obtainMessage(android.service.notification.ConditionProviderService.H.ON_UNSUBSCRIBE, conditionId).sendToTarget();
        }
    }

    private final class H extends android.os.Handler {
        private static final int ON_CONNECTED = 1;

        private static final int ON_SUBSCRIBE = 3;

        private static final int ON_UNSUBSCRIBE = 4;

        @java.lang.Override
        public void handleMessage(android.os.Message msg) {
            java.lang.String name = null;
            try {
                switch (msg.what) {
                    case android.service.notification.ConditionProviderService.H.ON_CONNECTED :
                        name = "onConnected";
                        onConnected();
                        break;
                    case android.service.notification.ConditionProviderService.H.ON_SUBSCRIBE :
                        name = "onSubscribe";
                        onSubscribe(((android.net.Uri) (msg.obj)));
                        break;
                    case android.service.notification.ConditionProviderService.H.ON_UNSUBSCRIBE :
                        name = "onUnsubscribe";
                        onUnsubscribe(((android.net.Uri) (msg.obj)));
                        break;
                }
            } catch (java.lang.Throwable t) {
                android.util.Log.w(android.service.notification.ConditionProviderService.this.TAG, "Error running " + name, t);
            }
        }
    }
}

