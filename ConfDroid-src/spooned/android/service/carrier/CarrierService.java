/**
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package android.service.carrier;


/**
 * A service that exposes carrier-specific functionality to the system.
 * <p>
 * To extend this class, you must declare the service in your manifest file to require the
 * {@link android.Manifest.permission#BIND_CARRIER_SERVICES} permission and include an intent
 * filter with the {@link #CARRIER_SERVICE_INTERFACE}. If the service should have a long-lived
 * binding, set android.service.carrier.LONG_LIVED_BINDING to true in the service's metadata.
 * For example:
 * </p>
 *
 * <pre>{@code <service android:name=".MyCarrierService"
 *       android:label="@string/service_name"
 *       android:permission="android.permission.BIND_CARRIER_SERVICES">
 *  <intent-filter>
 *      <action android:name="android.service.carrier.CarrierService" />
 *  </intent-filter>
 *  <meta-data android:name="android.service.carrier.LONG_LIVED_BINDING"
 *             android:value="true" />
 * </service>}</pre>
 */
public abstract class CarrierService extends android.app.Service {
    public static final java.lang.String CARRIER_SERVICE_INTERFACE = "android.service.carrier.CarrierService";

    private static com.android.internal.telephony.ITelephonyRegistry sRegistry;

    private final ICarrierService.Stub mStubWrapper;

    public CarrierService() {
        mStubWrapper = new android.service.carrier.CarrierService.ICarrierServiceWrapper();
        if (android.service.carrier.CarrierService.sRegistry == null) {
            android.service.carrier.CarrierService.sRegistry = ITelephonyRegistry.Stub.asInterface(android.os.ServiceManager.getService("telephony.registry"));
        }
    }

    /**
     * Override this method to set carrier configuration.
     * <p>
     * This method will be called by telephony services to get carrier-specific configuration
     * values. The returned config will be saved by the system until,
     * <ol>
     * <li>The carrier app package is updated, or</li>
     * <li>The carrier app requests a reload with
     * {@link android.telephony.CarrierConfigManager#notifyConfigChangedForSubId
     * notifyConfigChangedForSubId}.</li>
     * </ol>
     * This method can be called after a SIM card loads, which may be before or after boot.
     * </p>
     * <p>
     * This method should not block for a long time. If expensive operations (e.g. network access)
     * are required, this method can schedule the work and return null. Then, use
     * {@link android.telephony.CarrierConfigManager#notifyConfigChangedForSubId
     * notifyConfigChangedForSubId} to trigger a reload when the config is ready.
     * </p>
     * <p>
     * Implementations should use the keys defined in {@link android.telephony.CarrierConfigManager
     * CarrierConfigManager}. Any configuration values not set in the returned {@link PersistableBundle} may be overridden by the system's default configuration service.
     * </p>
     *
     * @param id
     * 		contains details about the current carrier that can be used do decide what
     * 		configuration values to return.
     * @return a {@link PersistableBundle} object containing the configuration or null if default
    values should be used.
     */
    public abstract android.os.PersistableBundle onLoadConfig(android.service.carrier.CarrierIdentifier id);

    /**
     * Informs the system of an intentional upcoming carrier network change by
     * a carrier app. This call is optional and is only used to allow the
     * system to provide alternative UI while telephony is performing an action
     * that may result in intentional, temporary network lack of connectivity.
     * <p>
     * Based on the active parameter passed in, this method will either show or
     * hide the alternative UI. There is no timeout associated with showing
     * this UX, so a carrier app must be sure to call with active set to false
     * sometime after calling with it set to true.
     * <p>
     * Requires Permission:
     *   {@link android.Manifest.permission#MODIFY_PHONE_STATE MODIFY_PHONE_STATE}
     * Or the calling app has carrier privileges.
     *
     * @see {@link android.telephony.TelephonyManager#hasCarrierPrivileges}
     * @param active
     * 		Whether the carrier network change is or shortly will be
     * 		active. Set this value to true to begin showing
     * 		alternative UI and false to stop.
     */
    public final void notifyCarrierNetworkChange(boolean active) {
        try {
            if (android.service.carrier.CarrierService.sRegistry != null)
                android.service.carrier.CarrierService.sRegistry.notifyCarrierNetworkChange(active);

        } catch (android.os.RemoteException | java.lang.NullPointerException ex) {
        }
    }

    /**
     * If overriding this method, call through to the super method for any unknown actions.
     * {@inheritDoc }
     */
    @java.lang.Override
    @android.annotation.CallSuper
    public android.os.IBinder onBind(android.content.Intent intent) {
        return mStubWrapper;
    }

    /**
     * A wrapper around ICarrierService that forwards calls to implementations of
     * {@link CarrierService}.
     */
    private class ICarrierServiceWrapper extends android.service.carrier.ICarrierService.Stub {
        @java.lang.Override
        public android.os.PersistableBundle getCarrierConfig(android.service.carrier.CarrierIdentifier id) {
            return android.service.carrier.CarrierService.this.onLoadConfig(id);
        }
    }
}

