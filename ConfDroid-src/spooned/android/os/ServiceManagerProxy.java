package android.os;


class ServiceManagerProxy implements android.os.IServiceManager {
    public ServiceManagerProxy(android.os.IBinder remote) {
        mRemote = remote;
    }

    public android.os.IBinder asBinder() {
        return mRemote;
    }

    public android.os.IBinder getService(java.lang.String name) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.os.IServiceManager.descriptor);
        data.writeString(name);
        mRemote.transact(android.os.IServiceManager.GET_SERVICE_TRANSACTION, data, reply, 0);
        android.os.IBinder binder = reply.readStrongBinder();
        reply.recycle();
        data.recycle();
        return binder;
    }

    public android.os.IBinder checkService(java.lang.String name) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.os.IServiceManager.descriptor);
        data.writeString(name);
        mRemote.transact(android.os.IServiceManager.CHECK_SERVICE_TRANSACTION, data, reply, 0);
        android.os.IBinder binder = reply.readStrongBinder();
        reply.recycle();
        data.recycle();
        return binder;
    }

    public void addService(java.lang.String name, android.os.IBinder service, boolean allowIsolated) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.os.IServiceManager.descriptor);
        data.writeString(name);
        data.writeStrongBinder(service);
        data.writeInt(allowIsolated ? 1 : 0);
        mRemote.transact(android.os.IServiceManager.ADD_SERVICE_TRANSACTION, data, reply, 0);
        reply.recycle();
        data.recycle();
    }

    public java.lang.String[] listServices() throws android.os.RemoteException {
        java.util.ArrayList<java.lang.String> services = new java.util.ArrayList<java.lang.String>();
        int n = 0;
        while (true) {
            android.os.Parcel data = android.os.Parcel.obtain();
            android.os.Parcel reply = android.os.Parcel.obtain();
            data.writeInterfaceToken(android.os.IServiceManager.descriptor);
            data.writeInt(n);
            n++;
            try {
                boolean res = mRemote.transact(android.os.IServiceManager.LIST_SERVICES_TRANSACTION, data, reply, 0);
                if (!res) {
                    break;
                }
            } catch (java.lang.RuntimeException e) {
                // The result code that is returned by the C++ code can
                // cause the call to throw an exception back instead of
                // returning a nice result...  so eat it here and go on.
                break;
            }
            services.add(reply.readString());
            reply.recycle();
            data.recycle();
        } 
        java.lang.String[] array = new java.lang.String[services.size()];
        services.toArray(array);
        return array;
    }

    public void setPermissionController(android.os.IPermissionController controller) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.os.IServiceManager.descriptor);
        data.writeStrongBinder(controller.asBinder());
        mRemote.transact(android.os.IServiceManager.SET_PERMISSION_CONTROLLER_TRANSACTION, data, reply, 0);
        reply.recycle();
        data.recycle();
    }

    private android.os.IBinder mRemote;
}

