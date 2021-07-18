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
 * A handler class to manage transport callbacks for {@link GnssMeasurementsEvent.Callback}.
 *
 * @unknown 
 */
class GnssMeasurementCallbackTransport extends android.location.LocalListenerHelper<android.location.GnssMeasurementsEvent.Callback> {
    private final android.location.ILocationManager mLocationManager;

    private final android.location.IGnssMeasurementsListener mListenerTransport = new android.location.GnssMeasurementCallbackTransport.ListenerTransport();

    public GnssMeasurementCallbackTransport(android.content.Context context, android.location.ILocationManager locationManager) {
        super(context, "GnssMeasurementListenerTransport");
        mLocationManager = locationManager;
    }

    @java.lang.Override
    protected boolean registerWithServer() throws android.os.RemoteException {
        return mLocationManager.addGnssMeasurementsListener(mListenerTransport, getContext().getPackageName());
    }

    @java.lang.Override
    protected void unregisterFromServer() throws android.os.RemoteException {
        mLocationManager.removeGnssMeasurementsListener(mListenerTransport);
    }

    private class ListenerTransport extends android.location.IGnssMeasurementsListener.Stub {
        @java.lang.Override
        public void onGnssMeasurementsReceived(final android.location.GnssMeasurementsEvent event) {
            android.location.LocalListenerHelper.ListenerOperation<android.location.GnssMeasurementsEvent.Callback> operation = new android.location.LocalListenerHelper.ListenerOperation<android.location.GnssMeasurementsEvent.Callback>() {
                @java.lang.Override
                public void execute(android.location.GnssMeasurementsEvent.Callback callback) throws android.os.RemoteException {
                    callback.onGnssMeasurementsReceived(event);
                }
            };
            foreach(operation);
        }

        @java.lang.Override
        public void onStatusChanged(final int status) {
            android.location.LocalListenerHelper.ListenerOperation<android.location.GnssMeasurementsEvent.Callback> operation = new android.location.LocalListenerHelper.ListenerOperation<android.location.GnssMeasurementsEvent.Callback>() {
                @java.lang.Override
                public void execute(android.location.GnssMeasurementsEvent.Callback callback) throws android.os.RemoteException {
                    callback.onStatusChanged(status);
                }
            };
            foreach(operation);
        }
    }
}

