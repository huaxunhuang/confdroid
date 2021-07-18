/**
 * Copyright (C) 2009 The Android Open Source Project
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
package android.os.storage;


/**
 * Callback class for receiving events from MountService.
 *
 * @unknown - Applications should use IStorageEventListener for storage event
callbacks.
 */
public interface IMountServiceListener extends android.os.IInterface {
    /**
     * Local-side IPC implementation stub class.
     */
    public static abstract class Stub extends android.os.Binder implements android.os.storage.IMountServiceListener {
        private static final java.lang.String DESCRIPTOR = "IMountServiceListener";

        /**
         * Construct the stub at attach it to the interface.
         */
        public Stub() {
            this.attachInterface(this, android.os.storage.IMountServiceListener.Stub.DESCRIPTOR);
        }

        /**
         * Cast an IBinder object into an IMountServiceListener interface,
         * generating a proxy if needed.
         */
        public static android.os.storage.IMountServiceListener asInterface(android.os.IBinder obj) {
            if (obj == null) {
                return null;
            }
            android.os.IInterface iin = ((android.os.IInterface) (obj.queryLocalInterface(android.os.storage.IMountServiceListener.Stub.DESCRIPTOR)));
            if ((iin != null) && (iin instanceof android.os.storage.IMountServiceListener)) {
                return ((android.os.storage.IMountServiceListener) (iin));
            }
            return new android.os.storage.IMountServiceListener.Stub.Proxy(obj);
        }

        public android.os.IBinder asBinder() {
            return this;
        }

        @java.lang.Override
        public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException {
            switch (code) {
                case android.os.IBinder.INTERFACE_TRANSACTION :
                    {
                        reply.writeString(android.os.storage.IMountServiceListener.Stub.DESCRIPTOR);
                        return true;
                    }
                case android.os.storage.IMountServiceListener.Stub.TRANSACTION_onUsbMassStorageConnectionChanged :
                    {
                        data.enforceInterface(android.os.storage.IMountServiceListener.Stub.DESCRIPTOR);
                        boolean connected;
                        connected = 0 != data.readInt();
                        this.onUsbMassStorageConnectionChanged(connected);
                        reply.writeNoException();
                        return true;
                    }
                case android.os.storage.IMountServiceListener.Stub.TRANSACTION_onStorageStateChanged :
                    {
                        data.enforceInterface(android.os.storage.IMountServiceListener.Stub.DESCRIPTOR);
                        final java.lang.String path = data.readString();
                        final java.lang.String oldState = data.readString();
                        final java.lang.String newState = data.readString();
                        this.onStorageStateChanged(path, oldState, newState);
                        reply.writeNoException();
                        return true;
                    }
                case android.os.storage.IMountServiceListener.Stub.TRANSACTION_onVolumeStateChanged :
                    {
                        data.enforceInterface(android.os.storage.IMountServiceListener.Stub.DESCRIPTOR);
                        final android.os.storage.VolumeInfo vol = ((android.os.storage.VolumeInfo) (data.readParcelable(null)));
                        final int oldState = data.readInt();
                        final int newState = data.readInt();
                        onVolumeStateChanged(vol, oldState, newState);
                        reply.writeNoException();
                        return true;
                    }
                case android.os.storage.IMountServiceListener.Stub.TRANSACTION_onVolumeRecordChanged :
                    {
                        data.enforceInterface(android.os.storage.IMountServiceListener.Stub.DESCRIPTOR);
                        final android.os.storage.VolumeRecord rec = ((android.os.storage.VolumeRecord) (data.readParcelable(null)));
                        onVolumeRecordChanged(rec);
                        reply.writeNoException();
                        return true;
                    }
                case android.os.storage.IMountServiceListener.Stub.TRANSACTION_onVolumeForgotten :
                    {
                        data.enforceInterface(android.os.storage.IMountServiceListener.Stub.DESCRIPTOR);
                        final java.lang.String fsUuid = data.readString();
                        onVolumeForgotten(fsUuid);
                        reply.writeNoException();
                        return true;
                    }
                case android.os.storage.IMountServiceListener.Stub.TRANSACTION_onDiskScanned :
                    {
                        data.enforceInterface(android.os.storage.IMountServiceListener.Stub.DESCRIPTOR);
                        final android.os.storage.DiskInfo disk = ((android.os.storage.DiskInfo) (data.readParcelable(null)));
                        final int volumeCount = data.readInt();
                        onDiskScanned(disk, volumeCount);
                        reply.writeNoException();
                        return true;
                    }
                case android.os.storage.IMountServiceListener.Stub.TRANSACTION_onDiskDestroyed :
                    {
                        data.enforceInterface(android.os.storage.IMountServiceListener.Stub.DESCRIPTOR);
                        final android.os.storage.DiskInfo disk = ((android.os.storage.DiskInfo) (data.readParcelable(null)));
                        onDiskDestroyed(disk);
                        reply.writeNoException();
                        return true;
                    }
            }
            return super.onTransact(code, data, reply, flags);
        }

        private static class Proxy implements android.os.storage.IMountServiceListener {
            private android.os.IBinder mRemote;

            Proxy(android.os.IBinder remote) {
                mRemote = remote;
            }

            public android.os.IBinder asBinder() {
                return mRemote;
            }

            public java.lang.String getInterfaceDescriptor() {
                return android.os.storage.IMountServiceListener.Stub.DESCRIPTOR;
            }

            /**
             * Detection state of USB Mass Storage has changed
             *
             * @param available
             * 		true if a UMS host is connected.
             */
            public void onUsbMassStorageConnectionChanged(boolean connected) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.storage.IMountServiceListener.Stub.DESCRIPTOR);
                    _data.writeInt(connected ? 1 : 0);
                    mRemote.transact(android.os.storage.IMountServiceListener.Stub.TRANSACTION_onUsbMassStorageConnectionChanged, _data, _reply, android.os.IBinder.FLAG_ONEWAY);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            /**
             * Storage state has changed.
             *
             * @param path
             * 		The volume mount path.
             * @param oldState
             * 		The old state of the volume.
             * @param newState
             * 		The new state of the volume. Note: State is one
             * 		of the values returned by
             * 		Environment.getExternalStorageState()
             */
            public void onStorageStateChanged(java.lang.String path, java.lang.String oldState, java.lang.String newState) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.storage.IMountServiceListener.Stub.DESCRIPTOR);
                    _data.writeString(path);
                    _data.writeString(oldState);
                    _data.writeString(newState);
                    mRemote.transact(android.os.storage.IMountServiceListener.Stub.TRANSACTION_onStorageStateChanged, _data, _reply, android.os.IBinder.FLAG_ONEWAY);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @java.lang.Override
            public void onVolumeStateChanged(android.os.storage.VolumeInfo vol, int oldState, int newState) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.storage.IMountServiceListener.Stub.DESCRIPTOR);
                    _data.writeParcelable(vol, 0);
                    _data.writeInt(oldState);
                    _data.writeInt(newState);
                    mRemote.transact(android.os.storage.IMountServiceListener.Stub.TRANSACTION_onVolumeStateChanged, _data, _reply, android.os.IBinder.FLAG_ONEWAY);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @java.lang.Override
            public void onVolumeRecordChanged(android.os.storage.VolumeRecord rec) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.storage.IMountServiceListener.Stub.DESCRIPTOR);
                    _data.writeParcelable(rec, 0);
                    mRemote.transact(android.os.storage.IMountServiceListener.Stub.TRANSACTION_onVolumeRecordChanged, _data, _reply, android.os.IBinder.FLAG_ONEWAY);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @java.lang.Override
            public void onVolumeForgotten(java.lang.String fsUuid) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.storage.IMountServiceListener.Stub.DESCRIPTOR);
                    _data.writeString(fsUuid);
                    mRemote.transact(android.os.storage.IMountServiceListener.Stub.TRANSACTION_onVolumeForgotten, _data, _reply, android.os.IBinder.FLAG_ONEWAY);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @java.lang.Override
            public void onDiskScanned(android.os.storage.DiskInfo disk, int volumeCount) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.storage.IMountServiceListener.Stub.DESCRIPTOR);
                    _data.writeParcelable(disk, 0);
                    _data.writeInt(volumeCount);
                    mRemote.transact(android.os.storage.IMountServiceListener.Stub.TRANSACTION_onDiskScanned, _data, _reply, android.os.IBinder.FLAG_ONEWAY);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @java.lang.Override
            public void onDiskDestroyed(android.os.storage.DiskInfo disk) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.storage.IMountServiceListener.Stub.DESCRIPTOR);
                    _data.writeParcelable(disk, 0);
                    mRemote.transact(android.os.storage.IMountServiceListener.Stub.TRANSACTION_onDiskDestroyed, _data, _reply, android.os.IBinder.FLAG_ONEWAY);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        static final int TRANSACTION_onUsbMassStorageConnectionChanged = android.os.IBinder.FIRST_CALL_TRANSACTION + 0;

        static final int TRANSACTION_onStorageStateChanged = android.os.IBinder.FIRST_CALL_TRANSACTION + 1;

        static final int TRANSACTION_onVolumeStateChanged = android.os.IBinder.FIRST_CALL_TRANSACTION + 2;

        static final int TRANSACTION_onVolumeRecordChanged = android.os.IBinder.FIRST_CALL_TRANSACTION + 3;

        static final int TRANSACTION_onVolumeForgotten = android.os.IBinder.FIRST_CALL_TRANSACTION + 4;

        static final int TRANSACTION_onDiskScanned = android.os.IBinder.FIRST_CALL_TRANSACTION + 5;

        static final int TRANSACTION_onDiskDestroyed = android.os.IBinder.FIRST_CALL_TRANSACTION + 6;
    }

    /**
     * Detection state of USB Mass Storage has changed
     *
     * @param available
     * 		true if a UMS host is connected.
     */
    public void onUsbMassStorageConnectionChanged(boolean connected) throws android.os.RemoteException;

    /**
     * Storage state has changed.
     *
     * @param path
     * 		The volume mount path.
     * @param oldState
     * 		The old state of the volume.
     * @param newState
     * 		The new state of the volume. Note: State is one of the
     * 		values returned by Environment.getExternalStorageState()
     */
    public void onStorageStateChanged(java.lang.String path, java.lang.String oldState, java.lang.String newState) throws android.os.RemoteException;

    public void onVolumeStateChanged(android.os.storage.VolumeInfo vol, int oldState, int newState) throws android.os.RemoteException;

    public void onVolumeRecordChanged(android.os.storage.VolumeRecord rec) throws android.os.RemoteException;

    public void onVolumeForgotten(java.lang.String fsUuid) throws android.os.RemoteException;

    public void onDiskScanned(android.os.storage.DiskInfo disk, int volumeCount) throws android.os.RemoteException;

    public void onDiskDestroyed(android.os.storage.DiskInfo disk) throws android.os.RemoteException;
}

