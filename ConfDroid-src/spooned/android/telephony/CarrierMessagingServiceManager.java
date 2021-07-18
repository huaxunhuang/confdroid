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
package android.telephony;


/**
 * Provides basic structure for platform to connect to the carrier messaging service.
 * <p>
 * <code>
 * CarrierMessagingServiceManager carrierMessagingServiceManager =
 *     new CarrierMessagingServiceManagerImpl();
 * if (carrierMessagingServiceManager.bindToCarrierMessagingService(context, carrierPackageName)) {
 *   // wait for onServiceReady callback
 * } else {
 *   // Unable to bind: handle error.
 * }
 * </code>
 * <p> Upon completion {@link #disposeConnection} should be called to unbind the
 * CarrierMessagingService.
 *
 * @unknown 
 */
public abstract class CarrierMessagingServiceManager {
    // Populated by bindToCarrierMessagingService. bindToCarrierMessagingService must complete
    // prior to calling disposeConnection so that mCarrierMessagingServiceConnection is initialized.
    private volatile android.telephony.CarrierMessagingServiceManager.CarrierMessagingServiceConnection mCarrierMessagingServiceConnection;

    /**
     * Binds to the carrier messaging service under package {@code carrierPackageName}. This method
     * should be called exactly once.
     *
     * @param context
     * 		the context
     * @param carrierPackageName
     * 		the carrier package name
     * @return true upon successfully binding to a carrier messaging service, false otherwise
     */
    public boolean bindToCarrierMessagingService(android.content.Context context, java.lang.String carrierPackageName) {
        com.android.internal.util.Preconditions.checkState(mCarrierMessagingServiceConnection == null);
        android.content.Intent intent = new android.content.Intent(android.service.carrier.CarrierMessagingService.SERVICE_INTERFACE);
        intent.setPackage(carrierPackageName);
        mCarrierMessagingServiceConnection = new android.telephony.CarrierMessagingServiceManager.CarrierMessagingServiceConnection();
        return context.bindService(intent, mCarrierMessagingServiceConnection, android.content.Context.BIND_AUTO_CREATE);
    }

    /**
     * Unbinds the carrier messaging service. This method should be called exactly once.
     *
     * @param context
     * 		the context
     */
    public void disposeConnection(android.content.Context context) {
        com.android.internal.util.Preconditions.checkNotNull(mCarrierMessagingServiceConnection);
        context.unbindService(mCarrierMessagingServiceConnection);
        mCarrierMessagingServiceConnection = null;
    }

    /**
     * Implemented by subclasses to use the carrier messaging service once it is ready.
     *
     * @param carrierMessagingService
     * 		the carrier messaing service interface
     */
    protected abstract void onServiceReady(android.service.carrier.ICarrierMessagingService carrierMessagingService);

    /**
     * A basic {@link ServiceConnection}.
     */
    private final class CarrierMessagingServiceConnection implements android.content.ServiceConnection {
        @java.lang.Override
        public void onServiceConnected(android.content.ComponentName name, android.os.IBinder service) {
            onServiceReady(ICarrierMessagingService.Stub.asInterface(service));
        }

        @java.lang.Override
        public void onServiceDisconnected(android.content.ComponentName name) {
        }
    }
}

