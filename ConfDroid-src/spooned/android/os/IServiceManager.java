/**
 * Copyright (C) 2006 The Android Open Source Project
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
package android.os;


/**
 * Basic interface for finding and publishing system services.
 *
 * An implementation of this interface is usually published as the
 * global context object, which can be retrieved via
 * BinderNative.getContextObject().  An easy way to retrieve this
 * is with the static method BnServiceManager.getDefault().
 *
 * @unknown 
 */
public interface IServiceManager extends android.os.IInterface {
    /**
     * Retrieve an existing service called @a name from the
     * service manager.  Blocks for a few seconds waiting for it to be
     * published if it does not already exist.
     */
    public android.os.IBinder getService(java.lang.String name) throws android.os.RemoteException;

    /**
     * Retrieve an existing service called @a name from the
     * service manager.  Non-blocking.
     */
    public android.os.IBinder checkService(java.lang.String name) throws android.os.RemoteException;

    /**
     * Place a new @a service called @a name into the service
     * manager.
     */
    public void addService(java.lang.String name, android.os.IBinder service, boolean allowIsolated) throws android.os.RemoteException;

    /**
     * Return a list of all currently running services.
     */
    public java.lang.String[] listServices() throws android.os.RemoteException;

    /**
     * Assign a permission controller to the service manager.  After set, this
     * interface is checked before any services are added.
     */
    public void setPermissionController(android.os.IPermissionController controller) throws android.os.RemoteException;

    static final java.lang.String descriptor = "android.os.IServiceManager";

    int GET_SERVICE_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION;

    int CHECK_SERVICE_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 1;

    int ADD_SERVICE_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 2;

    int LIST_SERVICES_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 3;

    int CHECK_SERVICES_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 4;

    int SET_PERMISSION_CONTROLLER_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 5;
}

