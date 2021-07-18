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
 * A handler class to manage transport callback for {@link GnssNavigationMessage.Callback}.
 *
 * @unknown 
 */
class GnssNavigationMessageCallbackTransport extends android.location.LocalListenerHelper<android.location.GnssNavigationMessage.Callback> {
    private final android.location.ILocationManager mLocationManager;

    private final android.location.IGnssNavigationMessageListener mListenerTransport = new android.location.GnssNavigationMessageCallbackTransport.ListenerTransport();

    public GnssNavigationMessageCallbackTransport(android.content.Context context, android.location.ILocationManager locationManager) {
        super(context, "GnssNavigationMessageCallbackTransport");
        mLocationManager = locationManager;
    }

    @java.lang.Override
    protected boolean registerWithServer() throws android.os.RemoteException {
        return mLocationManager.addGnssNavigationMessageListener(mListenerTransport, getContext().getPackageName());
    }

    @java.lang.Override
    protected void unregisterFromServer() throws android.os.RemoteException {
        mLocationManager.removeGnssNavigationMessageListener(mListenerTransport);
    }

    private class ListenerTransport extends android.location.IGnssNavigationMessageListener.Stub {
        @java.lang.Override
        public void onGnssNavigationMessageReceived(final android.location.GnssNavigationMessage event) {
            android.location.LocalListenerHelper.ListenerOperation<android.location.GnssNavigationMessage.Callback> operation = new android.location.LocalListenerHelper.ListenerOperation<android.location.GnssNavigationMessage.Callback>() {
                @java.lang.Override
                public void execute(android.location.GnssNavigationMessage.Callback callback) throws android.os.RemoteException {
                    callback.onGnssNavigationMessageReceived(event);
                }
            };
            foreach(operation);
        }

        @java.lang.Override
        public void onStatusChanged(final int status) {
            android.location.LocalListenerHelper.ListenerOperation<android.location.GnssNavigationMessage.Callback> operation = new android.location.LocalListenerHelper.ListenerOperation<android.location.GnssNavigationMessage.Callback>() {
                @java.lang.Override
                public void execute(android.location.GnssNavigationMessage.Callback callback) throws android.os.RemoteException {
                    callback.onStatusChanged(status);
                }
            };
            foreach(operation);
        }
    }
}

