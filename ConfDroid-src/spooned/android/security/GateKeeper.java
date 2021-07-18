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
package android.security;


/**
 * Convenience class for accessing the gatekeeper service.
 *
 * @unknown 
 */
public abstract class GateKeeper {
    private GateKeeper() {
    }

    public static android.service.gatekeeper.IGateKeeperService getService() {
        android.service.gatekeeper.IGateKeeperService service = IGateKeeperService.Stub.asInterface(android.os.ServiceManager.getService(android.content.Context.GATEKEEPER_SERVICE));
        if (service == null) {
            throw new java.lang.IllegalStateException("Gatekeeper service not available");
        }
        return service;
    }

    public static long getSecureUserId() throws java.lang.IllegalStateException {
        try {
            return android.security.GateKeeper.getService().getSecureUserId(android.os.UserHandle.myUserId());
        } catch (android.os.RemoteException e) {
            throw new java.lang.IllegalStateException("Failed to obtain secure user ID from gatekeeper", e);
        }
    }
}

