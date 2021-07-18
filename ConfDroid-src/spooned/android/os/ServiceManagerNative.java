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
 * Native implementation of the service manager.  Most clients will only
 * care about getDefault() and possibly asInterface().
 *
 * @unknown 
 */
public abstract class ServiceManagerNative extends android.os.Binder implements android.os.IServiceManager {
    /**
     * Cast a Binder object into a service manager interface, generating
     * a proxy if needed.
     */
    public static android.os.IServiceManager asInterface(android.os.IBinder obj) {
        if (obj == null) {
            return null;
        }
        android.os.IServiceManager in = ((android.os.IServiceManager) (obj.queryLocalInterface(android.os.IServiceManager.descriptor)));
        if (in != null) {
            return in;
        }
        return new android.os.ServiceManagerProxy(obj);
    }

    public ServiceManagerNative() {
        attachInterface(this, android.os.IServiceManager.descriptor);
    }

    public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) {
        try {
            switch (code) {
                case android.os.IServiceManager.GET_SERVICE_TRANSACTION :
                    {
                        data.enforceInterface(android.os.IServiceManager.descriptor);
                        java.lang.String name = data.readString();
                        android.os.IBinder service = getService(name);
                        reply.writeStrongBinder(service);
                        return true;
                    }
                case android.os.IServiceManager.CHECK_SERVICE_TRANSACTION :
                    {
                        data.enforceInterface(android.os.IServiceManager.descriptor);
                        java.lang.String name = data.readString();
                        android.os.IBinder service = checkService(name);
                        reply.writeStrongBinder(service);
                        return true;
                    }
                case android.os.IServiceManager.ADD_SERVICE_TRANSACTION :
                    {
                        data.enforceInterface(android.os.IServiceManager.descriptor);
                        java.lang.String name = data.readString();
                        android.os.IBinder service = data.readStrongBinder();
                        boolean allowIsolated = data.readInt() != 0;
                        addService(name, service, allowIsolated);
                        return true;
                    }
                case android.os.IServiceManager.LIST_SERVICES_TRANSACTION :
                    {
                        data.enforceInterface(android.os.IServiceManager.descriptor);
                        java.lang.String[] list = listServices();
                        reply.writeStringArray(list);
                        return true;
                    }
                case android.os.IServiceManager.SET_PERMISSION_CONTROLLER_TRANSACTION :
                    {
                        data.enforceInterface(android.os.IServiceManager.descriptor);
                        android.os.IPermissionController controller = IPermissionController.Stub.asInterface(data.readStrongBinder());
                        setPermissionController(controller);
                        return true;
                    }
            }
        } catch (android.os.RemoteException e) {
        }
        return false;
    }

    public android.os.IBinder asBinder() {
        return this;
    }
}

