/**
 * Copyright (C) 2007 The Android Open Source Project
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
 *
 *
 * @unknown 
 */
public final class ServiceManager {
    private static final java.lang.String TAG = "ServiceManager";

    private static android.os.IServiceManager sServiceManager;

    private static java.util.HashMap<java.lang.String, android.os.IBinder> sCache = new java.util.HashMap<java.lang.String, android.os.IBinder>();

    private static android.os.IServiceManager getIServiceManager() {
        if (android.os.ServiceManager.sServiceManager != null) {
            return android.os.ServiceManager.sServiceManager;
        }
        // Find the service manager
        android.os.ServiceManager.sServiceManager = android.os.ServiceManagerNative.asInterface(com.android.internal.os.BinderInternal.getContextObject());
        return android.os.ServiceManager.sServiceManager;
    }

    /**
     * Returns a reference to a service with the given name.
     *
     * @param name
     * 		the name of the service to get
     * @return a reference to the service, or <code>null</code> if the service doesn't exist
     */
    public static android.os.IBinder getService(java.lang.String name) {
        try {
            android.os.IBinder service = android.os.ServiceManager.sCache.get(name);
            if (service != null) {
                return service;
            } else {
                return android.os.ServiceManager.getIServiceManager().getService(name);
            }
        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.os.ServiceManager.TAG, "error in getService", e);
        }
        return null;
    }

    /**
     * Place a new @a service called @a name into the service
     * manager.
     *
     * @param name
     * 		the name of the new service
     * @param service
     * 		the service object
     */
    public static void addService(java.lang.String name, android.os.IBinder service) {
        try {
            android.os.ServiceManager.getIServiceManager().addService(name, service, false);
        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.os.ServiceManager.TAG, "error in addService", e);
        }
    }

    /**
     * Place a new @a service called @a name into the service
     * manager.
     *
     * @param name
     * 		the name of the new service
     * @param service
     * 		the service object
     * @param allowIsolated
     * 		set to true to allow isolated sandboxed processes
     * 		to access this service
     */
    public static void addService(java.lang.String name, android.os.IBinder service, boolean allowIsolated) {
        try {
            android.os.ServiceManager.getIServiceManager().addService(name, service, allowIsolated);
        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.os.ServiceManager.TAG, "error in addService", e);
        }
    }

    /**
     * Retrieve an existing service called @a name from the
     * service manager.  Non-blocking.
     */
    public static android.os.IBinder checkService(java.lang.String name) {
        try {
            android.os.IBinder service = android.os.ServiceManager.sCache.get(name);
            if (service != null) {
                return service;
            } else {
                return android.os.ServiceManager.getIServiceManager().checkService(name);
            }
        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.os.ServiceManager.TAG, "error in checkService", e);
            return null;
        }
    }

    /**
     * Return a list of all currently running services.
     *
     * @return an array of all currently running services, or <code>null</code> in
    case of an exception
     */
    public static java.lang.String[] listServices() {
        try {
            return android.os.ServiceManager.getIServiceManager().listServices();
        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.os.ServiceManager.TAG, "error in listServices", e);
            return null;
        }
    }

    /**
     * This is only intended to be called when the process is first being brought
     * up and bound by the activity manager. There is only one thread in the process
     * at that time, so no locking is done.
     *
     * @param cache
     * 		the cache of service references
     * @unknown 
     */
    public static void initServiceCache(java.util.Map<java.lang.String, android.os.IBinder> cache) {
        if (android.os.ServiceManager.sCache.size() != 0) {
            throw new java.lang.IllegalStateException("setServiceCache may only be called once");
        }
        android.os.ServiceManager.sCache.putAll(cache);
    }
}

