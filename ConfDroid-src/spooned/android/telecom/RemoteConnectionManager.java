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
 * R* limitations under the License.
 */
package android.telecom;


/**
 *
 *
 * @unknown 
 */
public class RemoteConnectionManager {
    private final java.util.Map<android.content.ComponentName, android.telecom.RemoteConnectionService> mRemoteConnectionServices = new java.util.HashMap<>();

    private final android.telecom.ConnectionService mOurConnectionServiceImpl;

    public RemoteConnectionManager(android.telecom.ConnectionService ourConnectionServiceImpl) {
        mOurConnectionServiceImpl = ourConnectionServiceImpl;
    }

    void addConnectionService(android.content.ComponentName componentName, com.android.internal.telecom.IConnectionService outgoingConnectionServiceRpc) {
        if (!mRemoteConnectionServices.containsKey(componentName)) {
            try {
                android.telecom.RemoteConnectionService remoteConnectionService = new android.telecom.RemoteConnectionService(outgoingConnectionServiceRpc, mOurConnectionServiceImpl);
                mRemoteConnectionServices.put(componentName, remoteConnectionService);
            } catch (android.os.RemoteException ignored) {
            }
        }
    }

    public android.telecom.RemoteConnection createRemoteConnection(android.telecom.PhoneAccountHandle connectionManagerPhoneAccount, android.telecom.ConnectionRequest request, boolean isIncoming) {
        android.telecom.PhoneAccountHandle accountHandle = request.getAccountHandle();
        if (accountHandle == null) {
            throw new java.lang.IllegalArgumentException("accountHandle must be specified.");
        }
        android.content.ComponentName componentName = request.getAccountHandle().getComponentName();
        if (!mRemoteConnectionServices.containsKey(componentName)) {
            throw new java.lang.UnsupportedOperationException("accountHandle not supported: " + componentName);
        }
        android.telecom.RemoteConnectionService remoteService = mRemoteConnectionServices.get(componentName);
        if (remoteService != null) {
            return remoteService.createRemoteConnection(connectionManagerPhoneAccount, request, isIncoming);
        }
        return null;
    }

    public void conferenceRemoteConnections(android.telecom.RemoteConnection a, android.telecom.RemoteConnection b) {
        if (a.getConnectionService() == b.getConnectionService()) {
            try {
                a.getConnectionService().conference(a.getId(), b.getId());
            } catch (android.os.RemoteException e) {
            }
        } else {
            android.telecom.Log.w(this, "Request to conference incompatible remote connections (%s,%s) (%s,%s)", a.getConnectionService(), a.getId(), b.getConnectionService(), b.getId());
        }
    }
}

