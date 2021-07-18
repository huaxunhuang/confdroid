/**
 * Copyright (C) 2010 The Android Open Source Project
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
 * WARNING! Update IMountService.h and IMountService.cpp if you change this
 * file. In particular, the ordering of the methods below must match the
 * _TRANSACTION enum in IMountService.cpp
 *
 * @unknown - Applications should use android.os.storage.StorageManager to access
storage functions.
 */
public interface IMountService extends android.os.IInterface {
    /**
     * Local-side IPC implementation stub class.
     */
    public static abstract class Stub extends android.os.Binder implements android.os.storage.IMountService {
        private static class Proxy implements android.os.storage.IMountService {
            private final android.os.IBinder mRemote;

            Proxy(android.os.IBinder remote) {
                mRemote = remote;
            }

            public android.os.IBinder asBinder() {
                return mRemote;
            }

            public java.lang.String getInterfaceDescriptor() {
                return android.os.storage.IMountService.Stub.DESCRIPTOR;
            }

            /**
             * Registers an IMountServiceListener for receiving async
             * notifications.
             */
            public void registerListener(android.os.storage.IMountServiceListener listener) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.storage.IMountService.Stub.DESCRIPTOR);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    mRemote.transact(android.os.storage.IMountService.Stub.TRANSACTION_registerListener, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            /**
             * Unregisters an IMountServiceListener
             */
            public void unregisterListener(android.os.storage.IMountServiceListener listener) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.storage.IMountService.Stub.DESCRIPTOR);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    mRemote.transact(android.os.storage.IMountService.Stub.TRANSACTION_unregisterListener, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            /**
             * Returns true if a USB mass storage host is connected
             */
            public boolean isUsbMassStorageConnected() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                boolean _result;
                try {
                    _data.writeInterfaceToken(android.os.storage.IMountService.Stub.DESCRIPTOR);
                    mRemote.transact(android.os.storage.IMountService.Stub.TRANSACTION_isUsbMassStorageConnected, _data, _reply, 0);
                    _reply.readException();
                    _result = 0 != _reply.readInt();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
                return _result;
            }

            /**
             * Enables / disables USB mass storage. The caller should check
             * actual status of enabling/disabling USB mass storage via
             * StorageEventListener.
             */
            public void setUsbMassStorageEnabled(boolean enable) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.storage.IMountService.Stub.DESCRIPTOR);
                    _data.writeInt(enable ? 1 : 0);
                    mRemote.transact(android.os.storage.IMountService.Stub.TRANSACTION_setUsbMassStorageEnabled, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            /**
             * Returns true if a USB mass storage host is enabled (media is
             * shared)
             */
            public boolean isUsbMassStorageEnabled() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                boolean _result;
                try {
                    _data.writeInterfaceToken(android.os.storage.IMountService.Stub.DESCRIPTOR);
                    mRemote.transact(android.os.storage.IMountService.Stub.TRANSACTION_isUsbMassStorageEnabled, _data, _reply, 0);
                    _reply.readException();
                    _result = 0 != _reply.readInt();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
                return _result;
            }

            /**
             * Mount external storage at given mount point. Returns an int
             * consistent with MountServiceResultCode
             */
            public int mountVolume(java.lang.String mountPoint) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                int _result;
                try {
                    _data.writeInterfaceToken(android.os.storage.IMountService.Stub.DESCRIPTOR);
                    _data.writeString(mountPoint);
                    mRemote.transact(android.os.storage.IMountService.Stub.TRANSACTION_mountVolume, _data, _reply, 0);
                    _reply.readException();
                    _result = _reply.readInt();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
                return _result;
            }

            /**
             * Safely unmount external storage at given mount point. The unmount
             * is an asynchronous operation. Applications should register
             * StorageEventListener for storage related status changes.
             */
            public void unmountVolume(java.lang.String mountPoint, boolean force, boolean removeEncryption) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.storage.IMountService.Stub.DESCRIPTOR);
                    _data.writeString(mountPoint);
                    _data.writeInt(force ? 1 : 0);
                    _data.writeInt(removeEncryption ? 1 : 0);
                    mRemote.transact(android.os.storage.IMountService.Stub.TRANSACTION_unmountVolume, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            /**
             * Format external storage given a mount point. Returns an int
             * consistent with MountServiceResultCode
             */
            public int formatVolume(java.lang.String mountPoint) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                int _result;
                try {
                    _data.writeInterfaceToken(android.os.storage.IMountService.Stub.DESCRIPTOR);
                    _data.writeString(mountPoint);
                    mRemote.transact(android.os.storage.IMountService.Stub.TRANSACTION_formatVolume, _data, _reply, 0);
                    _reply.readException();
                    _result = _reply.readInt();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
                return _result;
            }

            /**
             * Returns an array of pids with open files on the specified path.
             */
            public int[] getStorageUsers(java.lang.String path) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                int[] _result;
                try {
                    _data.writeInterfaceToken(android.os.storage.IMountService.Stub.DESCRIPTOR);
                    _data.writeString(path);
                    mRemote.transact(android.os.storage.IMountService.Stub.TRANSACTION_getStorageUsers, _data, _reply, 0);
                    _reply.readException();
                    _result = _reply.createIntArray();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
                return _result;
            }

            /**
             * Gets the state of a volume via its mountpoint.
             */
            public java.lang.String getVolumeState(java.lang.String mountPoint) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                java.lang.String _result;
                try {
                    _data.writeInterfaceToken(android.os.storage.IMountService.Stub.DESCRIPTOR);
                    _data.writeString(mountPoint);
                    mRemote.transact(android.os.storage.IMountService.Stub.TRANSACTION_getVolumeState, _data, _reply, 0);
                    _reply.readException();
                    _result = _reply.readString();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
                return _result;
            }

            /* Creates a secure container with the specified parameters. Returns
            an int consistent with MountServiceResultCode
             */
            public int createSecureContainer(java.lang.String id, int sizeMb, java.lang.String fstype, java.lang.String key, int ownerUid, boolean external) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                int _result;
                try {
                    _data.writeInterfaceToken(android.os.storage.IMountService.Stub.DESCRIPTOR);
                    _data.writeString(id);
                    _data.writeInt(sizeMb);
                    _data.writeString(fstype);
                    _data.writeString(key);
                    _data.writeInt(ownerUid);
                    _data.writeInt(external ? 1 : 0);
                    mRemote.transact(android.os.storage.IMountService.Stub.TRANSACTION_createSecureContainer, _data, _reply, 0);
                    _reply.readException();
                    _result = _reply.readInt();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
                return _result;
            }

            /* Destroy a secure container, and free up all resources associated
            with it. NOTE: Ensure all references are released prior to
            deleting. Returns an int consistent with MountServiceResultCode
             */
            public int destroySecureContainer(java.lang.String id, boolean force) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                int _result;
                try {
                    _data.writeInterfaceToken(android.os.storage.IMountService.Stub.DESCRIPTOR);
                    _data.writeString(id);
                    _data.writeInt(force ? 1 : 0);
                    mRemote.transact(android.os.storage.IMountService.Stub.TRANSACTION_destroySecureContainer, _data, _reply, 0);
                    _reply.readException();
                    _result = _reply.readInt();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
                return _result;
            }

            /* Finalize a container which has just been created and populated.
            After finalization, the container is immutable. Returns an int
            consistent with MountServiceResultCode
             */
            public int finalizeSecureContainer(java.lang.String id) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                int _result;
                try {
                    _data.writeInterfaceToken(android.os.storage.IMountService.Stub.DESCRIPTOR);
                    _data.writeString(id);
                    mRemote.transact(android.os.storage.IMountService.Stub.TRANSACTION_finalizeSecureContainer, _data, _reply, 0);
                    _reply.readException();
                    _result = _reply.readInt();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
                return _result;
            }

            /* Mount a secure container with the specified key and owner UID.
            Returns an int consistent with MountServiceResultCode
             */
            public int mountSecureContainer(java.lang.String id, java.lang.String key, int ownerUid, boolean readOnly) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                int _result;
                try {
                    _data.writeInterfaceToken(android.os.storage.IMountService.Stub.DESCRIPTOR);
                    _data.writeString(id);
                    _data.writeString(key);
                    _data.writeInt(ownerUid);
                    _data.writeInt(readOnly ? 1 : 0);
                    mRemote.transact(android.os.storage.IMountService.Stub.TRANSACTION_mountSecureContainer, _data, _reply, 0);
                    _reply.readException();
                    _result = _reply.readInt();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
                return _result;
            }

            /* Unount a secure container. Returns an int consistent with
            MountServiceResultCode
             */
            public int unmountSecureContainer(java.lang.String id, boolean force) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                int _result;
                try {
                    _data.writeInterfaceToken(android.os.storage.IMountService.Stub.DESCRIPTOR);
                    _data.writeString(id);
                    _data.writeInt(force ? 1 : 0);
                    mRemote.transact(android.os.storage.IMountService.Stub.TRANSACTION_unmountSecureContainer, _data, _reply, 0);
                    _reply.readException();
                    _result = _reply.readInt();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
                return _result;
            }

            /* Returns true if the specified container is mounted */
            public boolean isSecureContainerMounted(java.lang.String id) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                boolean _result;
                try {
                    _data.writeInterfaceToken(android.os.storage.IMountService.Stub.DESCRIPTOR);
                    _data.writeString(id);
                    mRemote.transact(android.os.storage.IMountService.Stub.TRANSACTION_isSecureContainerMounted, _data, _reply, 0);
                    _reply.readException();
                    _result = 0 != _reply.readInt();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
                return _result;
            }

            /* Rename an unmounted secure container. Returns an int consistent
            with MountServiceResultCode
             */
            public int renameSecureContainer(java.lang.String oldId, java.lang.String newId) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                int _result;
                try {
                    _data.writeInterfaceToken(android.os.storage.IMountService.Stub.DESCRIPTOR);
                    _data.writeString(oldId);
                    _data.writeString(newId);
                    mRemote.transact(android.os.storage.IMountService.Stub.TRANSACTION_renameSecureContainer, _data, _reply, 0);
                    _reply.readException();
                    _result = _reply.readInt();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
                return _result;
            }

            /* Returns the filesystem path of a mounted secure container. */
            public java.lang.String getSecureContainerPath(java.lang.String id) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                java.lang.String _result;
                try {
                    _data.writeInterfaceToken(android.os.storage.IMountService.Stub.DESCRIPTOR);
                    _data.writeString(id);
                    mRemote.transact(android.os.storage.IMountService.Stub.TRANSACTION_getSecureContainerPath, _data, _reply, 0);
                    _reply.readException();
                    _result = _reply.readString();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
                return _result;
            }

            /**
             * Gets an Array of currently known secure container IDs
             */
            public java.lang.String[] getSecureContainerList() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                java.lang.String[] _result;
                try {
                    _data.writeInterfaceToken(android.os.storage.IMountService.Stub.DESCRIPTOR);
                    mRemote.transact(android.os.storage.IMountService.Stub.TRANSACTION_getSecureContainerList, _data, _reply, 0);
                    _reply.readException();
                    _result = _reply.createStringArray();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
                return _result;
            }

            /**
             * Shuts down the MountService and gracefully unmounts all external
             * media. Invokes call back once the shutdown is complete.
             */
            public void shutdown(android.os.storage.IMountShutdownObserver observer) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.storage.IMountService.Stub.DESCRIPTOR);
                    _data.writeStrongBinder(observer != null ? observer.asBinder() : null);
                    mRemote.transact(android.os.storage.IMountService.Stub.TRANSACTION_shutdown, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            /**
             * Call into MountService by PackageManager to notify that its done
             * processing the media status update request.
             */
            public void finishMediaUpdate() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.storage.IMountService.Stub.DESCRIPTOR);
                    mRemote.transact(android.os.storage.IMountService.Stub.TRANSACTION_finishMediaUpdate, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            /**
             * Mounts an Opaque Binary Blob (OBB) with the specified decryption
             * key and only allows the calling process's UID access to the
             * contents. MountService will call back to the supplied
             * IObbActionListener to inform it of the terminal state of the
             * call.
             */
            public void mountObb(java.lang.String rawPath, java.lang.String canonicalPath, java.lang.String key, android.os.storage.IObbActionListener token, int nonce) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.storage.IMountService.Stub.DESCRIPTOR);
                    _data.writeString(rawPath);
                    _data.writeString(canonicalPath);
                    _data.writeString(key);
                    _data.writeStrongBinder(token != null ? token.asBinder() : null);
                    _data.writeInt(nonce);
                    mRemote.transact(android.os.storage.IMountService.Stub.TRANSACTION_mountObb, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            /**
             * Unmounts an Opaque Binary Blob (OBB). When the force flag is
             * specified, any program using it will be forcibly killed to
             * unmount the image. MountService will call back to the supplied
             * IObbActionListener to inform it of the terminal state of the
             * call.
             */
            public void unmountObb(java.lang.String rawPath, boolean force, android.os.storage.IObbActionListener token, int nonce) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.storage.IMountService.Stub.DESCRIPTOR);
                    _data.writeString(rawPath);
                    _data.writeInt(force ? 1 : 0);
                    _data.writeStrongBinder(token != null ? token.asBinder() : null);
                    _data.writeInt(nonce);
                    mRemote.transact(android.os.storage.IMountService.Stub.TRANSACTION_unmountObb, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            /**
             * Checks whether the specified Opaque Binary Blob (OBB) is mounted
             * somewhere.
             */
            public boolean isObbMounted(java.lang.String rawPath) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                boolean _result;
                try {
                    _data.writeInterfaceToken(android.os.storage.IMountService.Stub.DESCRIPTOR);
                    _data.writeString(rawPath);
                    mRemote.transact(android.os.storage.IMountService.Stub.TRANSACTION_isObbMounted, _data, _reply, 0);
                    _reply.readException();
                    _result = 0 != _reply.readInt();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
                return _result;
            }

            /**
             * Gets the path to the mounted Opaque Binary Blob (OBB).
             */
            public java.lang.String getMountedObbPath(java.lang.String rawPath) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                java.lang.String _result;
                try {
                    _data.writeInterfaceToken(android.os.storage.IMountService.Stub.DESCRIPTOR);
                    _data.writeString(rawPath);
                    mRemote.transact(android.os.storage.IMountService.Stub.TRANSACTION_getMountedObbPath, _data, _reply, 0);
                    _reply.readException();
                    _result = _reply.readString();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
                return _result;
            }

            /**
             * Returns whether the external storage is emulated.
             */
            public boolean isExternalStorageEmulated() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                boolean _result;
                try {
                    _data.writeInterfaceToken(android.os.storage.IMountService.Stub.DESCRIPTOR);
                    mRemote.transact(android.os.storage.IMountService.Stub.TRANSACTION_isExternalStorageEmulated, _data, _reply, 0);
                    _reply.readException();
                    _result = 0 != _reply.readInt();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
                return _result;
            }

            public int getEncryptionState() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                int _result;
                try {
                    _data.writeInterfaceToken(android.os.storage.IMountService.Stub.DESCRIPTOR);
                    mRemote.transact(android.os.storage.IMountService.Stub.TRANSACTION_getEncryptionState, _data, _reply, 0);
                    _reply.readException();
                    _result = _reply.readInt();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
                return _result;
            }

            public int decryptStorage(java.lang.String password) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                int _result;
                try {
                    _data.writeInterfaceToken(android.os.storage.IMountService.Stub.DESCRIPTOR);
                    _data.writeString(password);
                    mRemote.transact(android.os.storage.IMountService.Stub.TRANSACTION_decryptStorage, _data, _reply, 0);
                    _reply.readException();
                    _result = _reply.readInt();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
                return _result;
            }

            public int encryptStorage(int type, java.lang.String password) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                int _result;
                try {
                    _data.writeInterfaceToken(android.os.storage.IMountService.Stub.DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeString(password);
                    mRemote.transact(android.os.storage.IMountService.Stub.TRANSACTION_encryptStorage, _data, _reply, 0);
                    _reply.readException();
                    _result = _reply.readInt();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
                return _result;
            }

            public int changeEncryptionPassword(int type, java.lang.String password) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                int _result;
                try {
                    _data.writeInterfaceToken(android.os.storage.IMountService.Stub.DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeString(password);
                    mRemote.transact(android.os.storage.IMountService.Stub.TRANSACTION_changeEncryptionPassword, _data, _reply, 0);
                    _reply.readException();
                    _result = _reply.readInt();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
                return _result;
            }

            @java.lang.Override
            public int verifyEncryptionPassword(java.lang.String password) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                int _result;
                try {
                    _data.writeInterfaceToken(android.os.storage.IMountService.Stub.DESCRIPTOR);
                    _data.writeString(password);
                    mRemote.transact(android.os.storage.IMountService.Stub.TRANSACTION_verifyEncryptionPassword, _data, _reply, 0);
                    _reply.readException();
                    _result = _reply.readInt();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
                return _result;
            }

            public int getPasswordType() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                int _result;
                try {
                    _data.writeInterfaceToken(android.os.storage.IMountService.Stub.DESCRIPTOR);
                    mRemote.transact(android.os.storage.IMountService.Stub.TRANSACTION_getPasswordType, _data, _reply, 0);
                    _reply.readException();
                    _result = _reply.readInt();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
                return _result;
            }

            public java.lang.String getPassword() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                java.lang.String _result;
                try {
                    _data.writeInterfaceToken(android.os.storage.IMountService.Stub.DESCRIPTOR);
                    mRemote.transact(android.os.storage.IMountService.Stub.TRANSACTION_getPassword, _data, _reply, 0);
                    _reply.readException();
                    _result = _reply.readString();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
                return _result;
            }

            public void clearPassword() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.storage.IMountService.Stub.DESCRIPTOR);
                    mRemote.transact(android.os.storage.IMountService.Stub.TRANSACTION_clearPassword, _data, _reply, android.os.IBinder.FLAG_ONEWAY);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setField(java.lang.String field, java.lang.String data) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.storage.IMountService.Stub.DESCRIPTOR);
                    _data.writeString(field);
                    _data.writeString(data);
                    mRemote.transact(android.os.storage.IMountService.Stub.TRANSACTION_setField, _data, _reply, android.os.IBinder.FLAG_ONEWAY);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public java.lang.String getField(java.lang.String field) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                java.lang.String _result;
                try {
                    _data.writeInterfaceToken(android.os.storage.IMountService.Stub.DESCRIPTOR);
                    _data.writeString(field);
                    mRemote.transact(android.os.storage.IMountService.Stub.TRANSACTION_getField, _data, _reply, 0);
                    _reply.readException();
                    _result = _reply.readString();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
                return _result;
            }

            public boolean isConvertibleToFBE() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                boolean _result;
                try {
                    _data.writeInterfaceToken(android.os.storage.IMountService.Stub.DESCRIPTOR);
                    mRemote.transact(android.os.storage.IMountService.Stub.TRANSACTION_isConvertibleToFBE, _data, _reply, 0);
                    _reply.readException();
                    _result = _reply.readInt() != 0;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
                return _result;
            }

            public android.os.storage.StorageVolume[] getVolumeList(int uid, java.lang.String packageName, int flags) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                android.os.storage.StorageVolume[] _result;
                try {
                    _data.writeInterfaceToken(android.os.storage.IMountService.Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    _data.writeString(packageName);
                    _data.writeInt(flags);
                    mRemote.transact(android.os.storage.IMountService.Stub.TRANSACTION_getVolumeList, _data, _reply, 0);
                    _reply.readException();
                    _result = _reply.createTypedArray(android.os.storage.StorageVolume.CREATOR);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
                return _result;
            }

            /* Returns the filesystem path of a mounted secure container. */
            public java.lang.String getSecureContainerFilesystemPath(java.lang.String id) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                java.lang.String _result;
                try {
                    _data.writeInterfaceToken(android.os.storage.IMountService.Stub.DESCRIPTOR);
                    _data.writeString(id);
                    mRemote.transact(android.os.storage.IMountService.Stub.TRANSACTION_getSecureContainerFilesystemPath, _data, _reply, 0);
                    _reply.readException();
                    _result = _reply.readString();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
                return _result;
            }

            /**
             * Fix permissions in a container which has just been created and
             * populated. Returns an int consistent with MountServiceResultCode
             */
            public int fixPermissionsSecureContainer(java.lang.String id, int gid, java.lang.String filename) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                int _result;
                try {
                    _data.writeInterfaceToken(android.os.storage.IMountService.Stub.DESCRIPTOR);
                    _data.writeString(id);
                    _data.writeInt(gid);
                    _data.writeString(filename);
                    mRemote.transact(android.os.storage.IMountService.Stub.TRANSACTION_fixPermissionsSecureContainer, _data, _reply, 0);
                    _reply.readException();
                    _result = _reply.readInt();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
                return _result;
            }

            @java.lang.Override
            public int mkdirs(java.lang.String callingPkg, java.lang.String path) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                int _result;
                try {
                    _data.writeInterfaceToken(android.os.storage.IMountService.Stub.DESCRIPTOR);
                    _data.writeString(callingPkg);
                    _data.writeString(path);
                    mRemote.transact(android.os.storage.IMountService.Stub.TRANSACTION_mkdirs, _data, _reply, 0);
                    _reply.readException();
                    _result = _reply.readInt();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
                return _result;
            }

            @java.lang.Override
            public int resizeSecureContainer(java.lang.String id, int sizeMb, java.lang.String key) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                int _result;
                try {
                    _data.writeInterfaceToken(android.os.storage.IMountService.Stub.DESCRIPTOR);
                    _data.writeString(id);
                    _data.writeInt(sizeMb);
                    _data.writeString(key);
                    mRemote.transact(android.os.storage.IMountService.Stub.TRANSACTION_resizeSecureContainer, _data, _reply, 0);
                    _reply.readException();
                    _result = _reply.readInt();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
                return _result;
            }

            @java.lang.Override
            public long lastMaintenance() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                long _result;
                try {
                    _data.writeInterfaceToken(android.os.storage.IMountService.Stub.DESCRIPTOR);
                    mRemote.transact(android.os.storage.IMountService.Stub.TRANSACTION_lastMaintenance, _data, _reply, 0);
                    _reply.readException();
                    _result = _reply.readLong();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
                return _result;
            }

            @java.lang.Override
            public void runMaintenance() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.storage.IMountService.Stub.DESCRIPTOR);
                    mRemote.transact(android.os.storage.IMountService.Stub.TRANSACTION_runMaintenance, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
                return;
            }

            @java.lang.Override
            public void waitForAsecScan() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.storage.IMountService.Stub.DESCRIPTOR);
                    mRemote.transact(android.os.storage.IMountService.Stub.TRANSACTION_waitForAsecScan, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
                return;
            }

            @java.lang.Override
            public android.os.storage.DiskInfo[] getDisks() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                android.os.storage.DiskInfo[] _result;
                try {
                    _data.writeInterfaceToken(android.os.storage.IMountService.Stub.DESCRIPTOR);
                    mRemote.transact(android.os.storage.IMountService.Stub.TRANSACTION_getDisks, _data, _reply, 0);
                    _reply.readException();
                    _result = _reply.createTypedArray(android.os.storage.DiskInfo.CREATOR);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
                return _result;
            }

            @java.lang.Override
            public android.os.storage.VolumeInfo[] getVolumes(int _flags) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                android.os.storage.VolumeInfo[] _result;
                try {
                    _data.writeInterfaceToken(android.os.storage.IMountService.Stub.DESCRIPTOR);
                    _data.writeInt(_flags);
                    mRemote.transact(android.os.storage.IMountService.Stub.TRANSACTION_getVolumes, _data, _reply, 0);
                    _reply.readException();
                    _result = _reply.createTypedArray(android.os.storage.VolumeInfo.CREATOR);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
                return _result;
            }

            @java.lang.Override
            public android.os.storage.VolumeRecord[] getVolumeRecords(int _flags) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                android.os.storage.VolumeRecord[] _result;
                try {
                    _data.writeInterfaceToken(android.os.storage.IMountService.Stub.DESCRIPTOR);
                    _data.writeInt(_flags);
                    mRemote.transact(android.os.storage.IMountService.Stub.TRANSACTION_getVolumeRecords, _data, _reply, 0);
                    _reply.readException();
                    _result = _reply.createTypedArray(android.os.storage.VolumeRecord.CREATOR);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
                return _result;
            }

            @java.lang.Override
            public void mount(java.lang.String volId) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.storage.IMountService.Stub.DESCRIPTOR);
                    _data.writeString(volId);
                    mRemote.transact(android.os.storage.IMountService.Stub.TRANSACTION_mount, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @java.lang.Override
            public void unmount(java.lang.String volId) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.storage.IMountService.Stub.DESCRIPTOR);
                    _data.writeString(volId);
                    mRemote.transact(android.os.storage.IMountService.Stub.TRANSACTION_unmount, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @java.lang.Override
            public void format(java.lang.String volId) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.storage.IMountService.Stub.DESCRIPTOR);
                    _data.writeString(volId);
                    mRemote.transact(android.os.storage.IMountService.Stub.TRANSACTION_format, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @java.lang.Override
            public long benchmark(java.lang.String volId) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.storage.IMountService.Stub.DESCRIPTOR);
                    _data.writeString(volId);
                    mRemote.transact(android.os.storage.IMountService.Stub.TRANSACTION_benchmark, _data, _reply, 0);
                    _reply.readException();
                    return _reply.readLong();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @java.lang.Override
            public void partitionPublic(java.lang.String diskId) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.storage.IMountService.Stub.DESCRIPTOR);
                    _data.writeString(diskId);
                    mRemote.transact(android.os.storage.IMountService.Stub.TRANSACTION_partitionPublic, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @java.lang.Override
            public void partitionPrivate(java.lang.String diskId) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.storage.IMountService.Stub.DESCRIPTOR);
                    _data.writeString(diskId);
                    mRemote.transact(android.os.storage.IMountService.Stub.TRANSACTION_partitionPrivate, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @java.lang.Override
            public void partitionMixed(java.lang.String diskId, int ratio) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.storage.IMountService.Stub.DESCRIPTOR);
                    _data.writeString(diskId);
                    _data.writeInt(ratio);
                    mRemote.transact(android.os.storage.IMountService.Stub.TRANSACTION_partitionMixed, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @java.lang.Override
            public void setVolumeNickname(java.lang.String fsUuid, java.lang.String nickname) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.storage.IMountService.Stub.DESCRIPTOR);
                    _data.writeString(fsUuid);
                    _data.writeString(nickname);
                    mRemote.transact(android.os.storage.IMountService.Stub.TRANSACTION_setVolumeNickname, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @java.lang.Override
            public void setVolumeUserFlags(java.lang.String fsUuid, int flags, int mask) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.storage.IMountService.Stub.DESCRIPTOR);
                    _data.writeString(fsUuid);
                    _data.writeInt(flags);
                    _data.writeInt(mask);
                    mRemote.transact(android.os.storage.IMountService.Stub.TRANSACTION_setVolumeUserFlags, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @java.lang.Override
            public void forgetVolume(java.lang.String fsUuid) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.storage.IMountService.Stub.DESCRIPTOR);
                    _data.writeString(fsUuid);
                    mRemote.transact(android.os.storage.IMountService.Stub.TRANSACTION_forgetVolume, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @java.lang.Override
            public void forgetAllVolumes() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.storage.IMountService.Stub.DESCRIPTOR);
                    mRemote.transact(android.os.storage.IMountService.Stub.TRANSACTION_forgetAllVolumes, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @java.lang.Override
            public void setDebugFlags(int _flags, int _mask) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.storage.IMountService.Stub.DESCRIPTOR);
                    _data.writeInt(_flags);
                    _data.writeInt(_mask);
                    mRemote.transact(android.os.storage.IMountService.Stub.TRANSACTION_setDebugFlags, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @java.lang.Override
            public java.lang.String getPrimaryStorageUuid() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                java.lang.String _result;
                try {
                    _data.writeInterfaceToken(android.os.storage.IMountService.Stub.DESCRIPTOR);
                    mRemote.transact(android.os.storage.IMountService.Stub.TRANSACTION_getPrimaryStorageUuid, _data, _reply, 0);
                    _reply.readException();
                    _result = _reply.readString();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
                return _result;
            }

            @java.lang.Override
            public void setPrimaryStorageUuid(java.lang.String volumeUuid, android.content.pm.IPackageMoveObserver callback) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.storage.IMountService.Stub.DESCRIPTOR);
                    _data.writeString(volumeUuid);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    mRemote.transact(android.os.storage.IMountService.Stub.TRANSACTION_setPrimaryStorageUuid, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @java.lang.Override
            public void createUserKey(int userId, int serialNumber, boolean ephemeral) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.storage.IMountService.Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    _data.writeInt(serialNumber);
                    _data.writeInt(ephemeral ? 1 : 0);
                    mRemote.transact(android.os.storage.IMountService.Stub.TRANSACTION_createUserKey, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @java.lang.Override
            public void destroyUserKey(int userId) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.storage.IMountService.Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    mRemote.transact(android.os.storage.IMountService.Stub.TRANSACTION_destroyUserKey, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @java.lang.Override
            public void addUserKeyAuth(int userId, int serialNumber, byte[] token, byte[] secret) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.storage.IMountService.Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    _data.writeInt(serialNumber);
                    _data.writeByteArray(token);
                    _data.writeByteArray(secret);
                    mRemote.transact(android.os.storage.IMountService.Stub.TRANSACTION_addUserKeyAuth, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @java.lang.Override
            public void fixateNewestUserKeyAuth(int userId) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.storage.IMountService.Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    mRemote.transact(android.os.storage.IMountService.Stub.TRANSACTION_fixateNewestUserKeyAuth, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @java.lang.Override
            public void unlockUserKey(int userId, int serialNumber, byte[] token, byte[] secret) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.storage.IMountService.Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    _data.writeInt(serialNumber);
                    _data.writeByteArray(token);
                    _data.writeByteArray(secret);
                    mRemote.transact(android.os.storage.IMountService.Stub.TRANSACTION_unlockUserKey, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @java.lang.Override
            public void lockUserKey(int userId) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.storage.IMountService.Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    mRemote.transact(android.os.storage.IMountService.Stub.TRANSACTION_lockUserKey, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @java.lang.Override
            public boolean isUserKeyUnlocked(int userId) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                boolean _result;
                try {
                    _data.writeInterfaceToken(android.os.storage.IMountService.Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    mRemote.transact(android.os.storage.IMountService.Stub.TRANSACTION_isUserKeyUnlocked, _data, _reply, 0);
                    _reply.readException();
                    _result = 0 != _reply.readInt();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
                return _result;
            }

            @java.lang.Override
            public void prepareUserStorage(java.lang.String volumeUuid, int userId, int serialNumber, int flags) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.storage.IMountService.Stub.DESCRIPTOR);
                    _data.writeString(volumeUuid);
                    _data.writeInt(userId);
                    _data.writeInt(serialNumber);
                    _data.writeInt(flags);
                    mRemote.transact(android.os.storage.IMountService.Stub.TRANSACTION_prepareUserStorage, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @java.lang.Override
            public void destroyUserStorage(java.lang.String volumeUuid, int userId, int flags) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.storage.IMountService.Stub.DESCRIPTOR);
                    _data.writeString(volumeUuid);
                    _data.writeInt(userId);
                    _data.writeInt(flags);
                    mRemote.transact(android.os.storage.IMountService.Stub.TRANSACTION_destroyUserStorage, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @java.lang.Override
            public android.os.ParcelFileDescriptor mountAppFuse(java.lang.String name) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                android.os.ParcelFileDescriptor _result = null;
                try {
                    _data.writeInterfaceToken(android.os.storage.IMountService.Stub.DESCRIPTOR);
                    _data.writeString(name);
                    mRemote.transact(android.os.storage.IMountService.Stub.TRANSACTION_mountAppFuse, _data, _reply, 0);
                    _reply.readException();
                    _result = _reply.<android.os.ParcelFileDescriptor>readParcelable(java.lang.ClassLoader.getSystemClassLoader());
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
                return _result;
            }
        }

        private static final java.lang.String DESCRIPTOR = "IMountService";

        static final int TRANSACTION_registerListener = android.os.IBinder.FIRST_CALL_TRANSACTION + 0;

        static final int TRANSACTION_unregisterListener = android.os.IBinder.FIRST_CALL_TRANSACTION + 1;

        static final int TRANSACTION_isUsbMassStorageConnected = android.os.IBinder.FIRST_CALL_TRANSACTION + 2;

        static final int TRANSACTION_setUsbMassStorageEnabled = android.os.IBinder.FIRST_CALL_TRANSACTION + 3;

        static final int TRANSACTION_isUsbMassStorageEnabled = android.os.IBinder.FIRST_CALL_TRANSACTION + 4;

        static final int TRANSACTION_mountVolume = android.os.IBinder.FIRST_CALL_TRANSACTION + 5;

        static final int TRANSACTION_unmountVolume = android.os.IBinder.FIRST_CALL_TRANSACTION + 6;

        static final int TRANSACTION_formatVolume = android.os.IBinder.FIRST_CALL_TRANSACTION + 7;

        static final int TRANSACTION_getStorageUsers = android.os.IBinder.FIRST_CALL_TRANSACTION + 8;

        static final int TRANSACTION_getVolumeState = android.os.IBinder.FIRST_CALL_TRANSACTION + 9;

        static final int TRANSACTION_createSecureContainer = android.os.IBinder.FIRST_CALL_TRANSACTION + 10;

        static final int TRANSACTION_finalizeSecureContainer = android.os.IBinder.FIRST_CALL_TRANSACTION + 11;

        static final int TRANSACTION_destroySecureContainer = android.os.IBinder.FIRST_CALL_TRANSACTION + 12;

        static final int TRANSACTION_mountSecureContainer = android.os.IBinder.FIRST_CALL_TRANSACTION + 13;

        static final int TRANSACTION_unmountSecureContainer = android.os.IBinder.FIRST_CALL_TRANSACTION + 14;

        static final int TRANSACTION_isSecureContainerMounted = android.os.IBinder.FIRST_CALL_TRANSACTION + 15;

        static final int TRANSACTION_renameSecureContainer = android.os.IBinder.FIRST_CALL_TRANSACTION + 16;

        static final int TRANSACTION_getSecureContainerPath = android.os.IBinder.FIRST_CALL_TRANSACTION + 17;

        static final int TRANSACTION_getSecureContainerList = android.os.IBinder.FIRST_CALL_TRANSACTION + 18;

        static final int TRANSACTION_shutdown = android.os.IBinder.FIRST_CALL_TRANSACTION + 19;

        static final int TRANSACTION_finishMediaUpdate = android.os.IBinder.FIRST_CALL_TRANSACTION + 20;

        static final int TRANSACTION_mountObb = android.os.IBinder.FIRST_CALL_TRANSACTION + 21;

        static final int TRANSACTION_unmountObb = android.os.IBinder.FIRST_CALL_TRANSACTION + 22;

        static final int TRANSACTION_isObbMounted = android.os.IBinder.FIRST_CALL_TRANSACTION + 23;

        static final int TRANSACTION_getMountedObbPath = android.os.IBinder.FIRST_CALL_TRANSACTION + 24;

        static final int TRANSACTION_isExternalStorageEmulated = android.os.IBinder.FIRST_CALL_TRANSACTION + 25;

        static final int TRANSACTION_decryptStorage = android.os.IBinder.FIRST_CALL_TRANSACTION + 26;

        static final int TRANSACTION_encryptStorage = android.os.IBinder.FIRST_CALL_TRANSACTION + 27;

        static final int TRANSACTION_changeEncryptionPassword = android.os.IBinder.FIRST_CALL_TRANSACTION + 28;

        static final int TRANSACTION_getVolumeList = android.os.IBinder.FIRST_CALL_TRANSACTION + 29;

        static final int TRANSACTION_getSecureContainerFilesystemPath = android.os.IBinder.FIRST_CALL_TRANSACTION + 30;

        static final int TRANSACTION_getEncryptionState = android.os.IBinder.FIRST_CALL_TRANSACTION + 31;

        static final int TRANSACTION_verifyEncryptionPassword = android.os.IBinder.FIRST_CALL_TRANSACTION + 32;

        static final int TRANSACTION_fixPermissionsSecureContainer = android.os.IBinder.FIRST_CALL_TRANSACTION + 33;

        static final int TRANSACTION_mkdirs = android.os.IBinder.FIRST_CALL_TRANSACTION + 34;

        static final int TRANSACTION_getPasswordType = android.os.IBinder.FIRST_CALL_TRANSACTION + 35;

        static final int TRANSACTION_getPassword = android.os.IBinder.FIRST_CALL_TRANSACTION + 36;

        static final int TRANSACTION_clearPassword = android.os.IBinder.FIRST_CALL_TRANSACTION + 37;

        static final int TRANSACTION_setField = android.os.IBinder.FIRST_CALL_TRANSACTION + 38;

        static final int TRANSACTION_getField = android.os.IBinder.FIRST_CALL_TRANSACTION + 39;

        static final int TRANSACTION_resizeSecureContainer = android.os.IBinder.FIRST_CALL_TRANSACTION + 40;

        static final int TRANSACTION_lastMaintenance = android.os.IBinder.FIRST_CALL_TRANSACTION + 41;

        static final int TRANSACTION_runMaintenance = android.os.IBinder.FIRST_CALL_TRANSACTION + 42;

        static final int TRANSACTION_waitForAsecScan = android.os.IBinder.FIRST_CALL_TRANSACTION + 43;

        static final int TRANSACTION_getDisks = android.os.IBinder.FIRST_CALL_TRANSACTION + 44;

        static final int TRANSACTION_getVolumes = android.os.IBinder.FIRST_CALL_TRANSACTION + 45;

        static final int TRANSACTION_getVolumeRecords = android.os.IBinder.FIRST_CALL_TRANSACTION + 46;

        static final int TRANSACTION_mount = android.os.IBinder.FIRST_CALL_TRANSACTION + 47;

        static final int TRANSACTION_unmount = android.os.IBinder.FIRST_CALL_TRANSACTION + 48;

        static final int TRANSACTION_format = android.os.IBinder.FIRST_CALL_TRANSACTION + 49;

        static final int TRANSACTION_partitionPublic = android.os.IBinder.FIRST_CALL_TRANSACTION + 50;

        static final int TRANSACTION_partitionPrivate = android.os.IBinder.FIRST_CALL_TRANSACTION + 51;

        static final int TRANSACTION_partitionMixed = android.os.IBinder.FIRST_CALL_TRANSACTION + 52;

        static final int TRANSACTION_setVolumeNickname = android.os.IBinder.FIRST_CALL_TRANSACTION + 53;

        static final int TRANSACTION_setVolumeUserFlags = android.os.IBinder.FIRST_CALL_TRANSACTION + 54;

        static final int TRANSACTION_forgetVolume = android.os.IBinder.FIRST_CALL_TRANSACTION + 55;

        static final int TRANSACTION_forgetAllVolumes = android.os.IBinder.FIRST_CALL_TRANSACTION + 56;

        static final int TRANSACTION_getPrimaryStorageUuid = android.os.IBinder.FIRST_CALL_TRANSACTION + 57;

        static final int TRANSACTION_setPrimaryStorageUuid = android.os.IBinder.FIRST_CALL_TRANSACTION + 58;

        static final int TRANSACTION_benchmark = android.os.IBinder.FIRST_CALL_TRANSACTION + 59;

        static final int TRANSACTION_setDebugFlags = android.os.IBinder.FIRST_CALL_TRANSACTION + 60;

        static final int TRANSACTION_createUserKey = android.os.IBinder.FIRST_CALL_TRANSACTION + 61;

        static final int TRANSACTION_destroyUserKey = android.os.IBinder.FIRST_CALL_TRANSACTION + 62;

        static final int TRANSACTION_unlockUserKey = android.os.IBinder.FIRST_CALL_TRANSACTION + 63;

        static final int TRANSACTION_lockUserKey = android.os.IBinder.FIRST_CALL_TRANSACTION + 64;

        static final int TRANSACTION_isUserKeyUnlocked = android.os.IBinder.FIRST_CALL_TRANSACTION + 65;

        static final int TRANSACTION_prepareUserStorage = android.os.IBinder.FIRST_CALL_TRANSACTION + 66;

        static final int TRANSACTION_destroyUserStorage = android.os.IBinder.FIRST_CALL_TRANSACTION + 67;

        static final int TRANSACTION_isConvertibleToFBE = android.os.IBinder.FIRST_CALL_TRANSACTION + 68;

        static final int TRANSACTION_mountAppFuse = android.os.IBinder.FIRST_CALL_TRANSACTION + 69;

        static final int TRANSACTION_addUserKeyAuth = android.os.IBinder.FIRST_CALL_TRANSACTION + 70;

        static final int TRANSACTION_fixateNewestUserKeyAuth = android.os.IBinder.FIRST_CALL_TRANSACTION + 71;

        /**
         * Cast an IBinder object into an IMountService interface, generating a
         * proxy if needed.
         */
        public static android.os.storage.IMountService asInterface(android.os.IBinder obj) {
            if (obj == null) {
                return null;
            }
            android.os.IInterface iin = obj.queryLocalInterface(android.os.storage.IMountService.Stub.DESCRIPTOR);
            if ((iin != null) && (iin instanceof android.os.storage.IMountService)) {
                return ((android.os.storage.IMountService) (iin));
            }
            return new android.os.storage.IMountService.Stub.Proxy(obj);
        }

        /**
         * Construct the stub at attach it to the interface.
         */
        public Stub() {
            attachInterface(this, android.os.storage.IMountService.Stub.DESCRIPTOR);
        }

        public android.os.IBinder asBinder() {
            return this;
        }

        @java.lang.Override
        public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException {
            switch (code) {
                case android.os.IBinder.INTERFACE_TRANSACTION :
                    {
                        reply.writeString(android.os.storage.IMountService.Stub.DESCRIPTOR);
                        return true;
                    }
                case android.os.storage.IMountService.Stub.TRANSACTION_registerListener :
                    {
                        data.enforceInterface(android.os.storage.IMountService.Stub.DESCRIPTOR);
                        android.os.storage.IMountServiceListener listener;
                        listener = android.os.storage.IMountServiceListener.Stub.asInterface(data.readStrongBinder());
                        registerListener(listener);
                        reply.writeNoException();
                        return true;
                    }
                case android.os.storage.IMountService.Stub.TRANSACTION_unregisterListener :
                    {
                        data.enforceInterface(android.os.storage.IMountService.Stub.DESCRIPTOR);
                        android.os.storage.IMountServiceListener listener;
                        listener = android.os.storage.IMountServiceListener.Stub.asInterface(data.readStrongBinder());
                        unregisterListener(listener);
                        reply.writeNoException();
                        return true;
                    }
                case android.os.storage.IMountService.Stub.TRANSACTION_isUsbMassStorageConnected :
                    {
                        data.enforceInterface(android.os.storage.IMountService.Stub.DESCRIPTOR);
                        boolean result = isUsbMassStorageConnected();
                        reply.writeNoException();
                        reply.writeInt(result ? 1 : 0);
                        return true;
                    }
                case android.os.storage.IMountService.Stub.TRANSACTION_setUsbMassStorageEnabled :
                    {
                        data.enforceInterface(android.os.storage.IMountService.Stub.DESCRIPTOR);
                        boolean enable;
                        enable = 0 != data.readInt();
                        setUsbMassStorageEnabled(enable);
                        reply.writeNoException();
                        return true;
                    }
                case android.os.storage.IMountService.Stub.TRANSACTION_isUsbMassStorageEnabled :
                    {
                        data.enforceInterface(android.os.storage.IMountService.Stub.DESCRIPTOR);
                        boolean result = isUsbMassStorageEnabled();
                        reply.writeNoException();
                        reply.writeInt(result ? 1 : 0);
                        return true;
                    }
                case android.os.storage.IMountService.Stub.TRANSACTION_mountVolume :
                    {
                        data.enforceInterface(android.os.storage.IMountService.Stub.DESCRIPTOR);
                        java.lang.String mountPoint;
                        mountPoint = data.readString();
                        int resultCode = mountVolume(mountPoint);
                        reply.writeNoException();
                        reply.writeInt(resultCode);
                        return true;
                    }
                case android.os.storage.IMountService.Stub.TRANSACTION_unmountVolume :
                    {
                        data.enforceInterface(android.os.storage.IMountService.Stub.DESCRIPTOR);
                        java.lang.String mountPoint;
                        mountPoint = data.readString();
                        boolean force = 0 != data.readInt();
                        boolean removeEncrypt = 0 != data.readInt();
                        unmountVolume(mountPoint, force, removeEncrypt);
                        reply.writeNoException();
                        return true;
                    }
                case android.os.storage.IMountService.Stub.TRANSACTION_formatVolume :
                    {
                        data.enforceInterface(android.os.storage.IMountService.Stub.DESCRIPTOR);
                        java.lang.String mountPoint;
                        mountPoint = data.readString();
                        int result = formatVolume(mountPoint);
                        reply.writeNoException();
                        reply.writeInt(result);
                        return true;
                    }
                case android.os.storage.IMountService.Stub.TRANSACTION_getStorageUsers :
                    {
                        data.enforceInterface(android.os.storage.IMountService.Stub.DESCRIPTOR);
                        java.lang.String path;
                        path = data.readString();
                        int[] pids = getStorageUsers(path);
                        reply.writeNoException();
                        reply.writeIntArray(pids);
                        return true;
                    }
                case android.os.storage.IMountService.Stub.TRANSACTION_getVolumeState :
                    {
                        data.enforceInterface(android.os.storage.IMountService.Stub.DESCRIPTOR);
                        java.lang.String mountPoint;
                        mountPoint = data.readString();
                        java.lang.String state = getVolumeState(mountPoint);
                        reply.writeNoException();
                        reply.writeString(state);
                        return true;
                    }
                case android.os.storage.IMountService.Stub.TRANSACTION_createSecureContainer :
                    {
                        data.enforceInterface(android.os.storage.IMountService.Stub.DESCRIPTOR);
                        java.lang.String id;
                        id = data.readString();
                        int sizeMb;
                        sizeMb = data.readInt();
                        java.lang.String fstype;
                        fstype = data.readString();
                        java.lang.String key;
                        key = data.readString();
                        int ownerUid;
                        ownerUid = data.readInt();
                        boolean external;
                        external = 0 != data.readInt();
                        int resultCode = createSecureContainer(id, sizeMb, fstype, key, ownerUid, external);
                        reply.writeNoException();
                        reply.writeInt(resultCode);
                        return true;
                    }
                case android.os.storage.IMountService.Stub.TRANSACTION_finalizeSecureContainer :
                    {
                        data.enforceInterface(android.os.storage.IMountService.Stub.DESCRIPTOR);
                        java.lang.String id;
                        id = data.readString();
                        int resultCode = finalizeSecureContainer(id);
                        reply.writeNoException();
                        reply.writeInt(resultCode);
                        return true;
                    }
                case android.os.storage.IMountService.Stub.TRANSACTION_destroySecureContainer :
                    {
                        data.enforceInterface(android.os.storage.IMountService.Stub.DESCRIPTOR);
                        java.lang.String id;
                        id = data.readString();
                        boolean force;
                        force = 0 != data.readInt();
                        int resultCode = destroySecureContainer(id, force);
                        reply.writeNoException();
                        reply.writeInt(resultCode);
                        return true;
                    }
                case android.os.storage.IMountService.Stub.TRANSACTION_mountSecureContainer :
                    {
                        data.enforceInterface(android.os.storage.IMountService.Stub.DESCRIPTOR);
                        java.lang.String id;
                        id = data.readString();
                        java.lang.String key;
                        key = data.readString();
                        int ownerUid;
                        ownerUid = data.readInt();
                        boolean readOnly;
                        readOnly = data.readInt() != 0;
                        int resultCode = mountSecureContainer(id, key, ownerUid, readOnly);
                        reply.writeNoException();
                        reply.writeInt(resultCode);
                        return true;
                    }
                case android.os.storage.IMountService.Stub.TRANSACTION_unmountSecureContainer :
                    {
                        data.enforceInterface(android.os.storage.IMountService.Stub.DESCRIPTOR);
                        java.lang.String id;
                        id = data.readString();
                        boolean force;
                        force = 0 != data.readInt();
                        int resultCode = unmountSecureContainer(id, force);
                        reply.writeNoException();
                        reply.writeInt(resultCode);
                        return true;
                    }
                case android.os.storage.IMountService.Stub.TRANSACTION_isSecureContainerMounted :
                    {
                        data.enforceInterface(android.os.storage.IMountService.Stub.DESCRIPTOR);
                        java.lang.String id;
                        id = data.readString();
                        boolean status = isSecureContainerMounted(id);
                        reply.writeNoException();
                        reply.writeInt(status ? 1 : 0);
                        return true;
                    }
                case android.os.storage.IMountService.Stub.TRANSACTION_renameSecureContainer :
                    {
                        data.enforceInterface(android.os.storage.IMountService.Stub.DESCRIPTOR);
                        java.lang.String oldId;
                        oldId = data.readString();
                        java.lang.String newId;
                        newId = data.readString();
                        int resultCode = renameSecureContainer(oldId, newId);
                        reply.writeNoException();
                        reply.writeInt(resultCode);
                        return true;
                    }
                case android.os.storage.IMountService.Stub.TRANSACTION_getSecureContainerPath :
                    {
                        data.enforceInterface(android.os.storage.IMountService.Stub.DESCRIPTOR);
                        java.lang.String id;
                        id = data.readString();
                        java.lang.String path = getSecureContainerPath(id);
                        reply.writeNoException();
                        reply.writeString(path);
                        return true;
                    }
                case android.os.storage.IMountService.Stub.TRANSACTION_getSecureContainerList :
                    {
                        data.enforceInterface(android.os.storage.IMountService.Stub.DESCRIPTOR);
                        java.lang.String[] ids = getSecureContainerList();
                        reply.writeNoException();
                        reply.writeStringArray(ids);
                        return true;
                    }
                case android.os.storage.IMountService.Stub.TRANSACTION_shutdown :
                    {
                        data.enforceInterface(android.os.storage.IMountService.Stub.DESCRIPTOR);
                        android.os.storage.IMountShutdownObserver observer;
                        observer = android.os.storage.IMountShutdownObserver.Stub.asInterface(data.readStrongBinder());
                        shutdown(observer);
                        reply.writeNoException();
                        return true;
                    }
                case android.os.storage.IMountService.Stub.TRANSACTION_finishMediaUpdate :
                    {
                        data.enforceInterface(android.os.storage.IMountService.Stub.DESCRIPTOR);
                        finishMediaUpdate();
                        reply.writeNoException();
                        return true;
                    }
                case android.os.storage.IMountService.Stub.TRANSACTION_mountObb :
                    {
                        data.enforceInterface(android.os.storage.IMountService.Stub.DESCRIPTOR);
                        final java.lang.String rawPath = data.readString();
                        final java.lang.String canonicalPath = data.readString();
                        final java.lang.String key = data.readString();
                        android.os.storage.IObbActionListener observer;
                        observer = android.os.storage.IObbActionListener.Stub.asInterface(data.readStrongBinder());
                        int nonce;
                        nonce = data.readInt();
                        mountObb(rawPath, canonicalPath, key, observer, nonce);
                        reply.writeNoException();
                        return true;
                    }
                case android.os.storage.IMountService.Stub.TRANSACTION_unmountObb :
                    {
                        data.enforceInterface(android.os.storage.IMountService.Stub.DESCRIPTOR);
                        java.lang.String filename;
                        filename = data.readString();
                        boolean force;
                        force = 0 != data.readInt();
                        android.os.storage.IObbActionListener observer;
                        observer = android.os.storage.IObbActionListener.Stub.asInterface(data.readStrongBinder());
                        int nonce;
                        nonce = data.readInt();
                        unmountObb(filename, force, observer, nonce);
                        reply.writeNoException();
                        return true;
                    }
                case android.os.storage.IMountService.Stub.TRANSACTION_isObbMounted :
                    {
                        data.enforceInterface(android.os.storage.IMountService.Stub.DESCRIPTOR);
                        java.lang.String filename;
                        filename = data.readString();
                        boolean status = isObbMounted(filename);
                        reply.writeNoException();
                        reply.writeInt(status ? 1 : 0);
                        return true;
                    }
                case android.os.storage.IMountService.Stub.TRANSACTION_getMountedObbPath :
                    {
                        data.enforceInterface(android.os.storage.IMountService.Stub.DESCRIPTOR);
                        java.lang.String filename;
                        filename = data.readString();
                        java.lang.String mountedPath = getMountedObbPath(filename);
                        reply.writeNoException();
                        reply.writeString(mountedPath);
                        return true;
                    }
                case android.os.storage.IMountService.Stub.TRANSACTION_isExternalStorageEmulated :
                    {
                        data.enforceInterface(android.os.storage.IMountService.Stub.DESCRIPTOR);
                        boolean emulated = isExternalStorageEmulated();
                        reply.writeNoException();
                        reply.writeInt(emulated ? 1 : 0);
                        return true;
                    }
                case android.os.storage.IMountService.Stub.TRANSACTION_decryptStorage :
                    {
                        data.enforceInterface(android.os.storage.IMountService.Stub.DESCRIPTOR);
                        java.lang.String password = data.readString();
                        int result = decryptStorage(password);
                        reply.writeNoException();
                        reply.writeInt(result);
                        return true;
                    }
                case android.os.storage.IMountService.Stub.TRANSACTION_encryptStorage :
                    {
                        data.enforceInterface(android.os.storage.IMountService.Stub.DESCRIPTOR);
                        int type = data.readInt();
                        java.lang.String password = data.readString();
                        int result = encryptStorage(type, password);
                        reply.writeNoException();
                        reply.writeInt(result);
                        return true;
                    }
                case android.os.storage.IMountService.Stub.TRANSACTION_changeEncryptionPassword :
                    {
                        data.enforceInterface(android.os.storage.IMountService.Stub.DESCRIPTOR);
                        int type = data.readInt();
                        java.lang.String password = data.readString();
                        int result = changeEncryptionPassword(type, password);
                        reply.writeNoException();
                        reply.writeInt(result);
                        return true;
                    }
                case android.os.storage.IMountService.Stub.TRANSACTION_getVolumeList :
                    {
                        data.enforceInterface(android.os.storage.IMountService.Stub.DESCRIPTOR);
                        int uid = data.readInt();
                        java.lang.String packageName = data.readString();
                        int _flags = data.readInt();
                        android.os.storage.StorageVolume[] result = getVolumeList(uid, packageName, _flags);
                        reply.writeNoException();
                        reply.writeTypedArray(result, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
                        return true;
                    }
                case android.os.storage.IMountService.Stub.TRANSACTION_getSecureContainerFilesystemPath :
                    {
                        data.enforceInterface(android.os.storage.IMountService.Stub.DESCRIPTOR);
                        java.lang.String id;
                        id = data.readString();
                        java.lang.String path = getSecureContainerFilesystemPath(id);
                        reply.writeNoException();
                        reply.writeString(path);
                        return true;
                    }
                case android.os.storage.IMountService.Stub.TRANSACTION_getEncryptionState :
                    {
                        data.enforceInterface(android.os.storage.IMountService.Stub.DESCRIPTOR);
                        int result = getEncryptionState();
                        reply.writeNoException();
                        reply.writeInt(result);
                        return true;
                    }
                case android.os.storage.IMountService.Stub.TRANSACTION_fixPermissionsSecureContainer :
                    {
                        data.enforceInterface(android.os.storage.IMountService.Stub.DESCRIPTOR);
                        java.lang.String id;
                        id = data.readString();
                        int gid;
                        gid = data.readInt();
                        java.lang.String filename;
                        filename = data.readString();
                        int resultCode = fixPermissionsSecureContainer(id, gid, filename);
                        reply.writeNoException();
                        reply.writeInt(resultCode);
                        return true;
                    }
                case android.os.storage.IMountService.Stub.TRANSACTION_mkdirs :
                    {
                        data.enforceInterface(android.os.storage.IMountService.Stub.DESCRIPTOR);
                        java.lang.String callingPkg = data.readString();
                        java.lang.String path = data.readString();
                        int result = mkdirs(callingPkg, path);
                        reply.writeNoException();
                        reply.writeInt(result);
                        return true;
                    }
                case android.os.storage.IMountService.Stub.TRANSACTION_getPasswordType :
                    {
                        data.enforceInterface(android.os.storage.IMountService.Stub.DESCRIPTOR);
                        int result = getPasswordType();
                        reply.writeNoException();
                        reply.writeInt(result);
                        return true;
                    }
                case android.os.storage.IMountService.Stub.TRANSACTION_getPassword :
                    {
                        data.enforceInterface(android.os.storage.IMountService.Stub.DESCRIPTOR);
                        java.lang.String result = getPassword();
                        reply.writeNoException();
                        reply.writeString(result);
                        return true;
                    }
                case android.os.storage.IMountService.Stub.TRANSACTION_clearPassword :
                    {
                        data.enforceInterface(android.os.storage.IMountService.Stub.DESCRIPTOR);
                        clearPassword();
                        reply.writeNoException();
                        return true;
                    }
                case android.os.storage.IMountService.Stub.TRANSACTION_setField :
                    {
                        data.enforceInterface(android.os.storage.IMountService.Stub.DESCRIPTOR);
                        java.lang.String field = data.readString();
                        java.lang.String contents = data.readString();
                        setField(field, contents);
                        reply.writeNoException();
                        return true;
                    }
                case android.os.storage.IMountService.Stub.TRANSACTION_getField :
                    {
                        data.enforceInterface(android.os.storage.IMountService.Stub.DESCRIPTOR);
                        java.lang.String field = data.readString();
                        java.lang.String contents = getField(field);
                        reply.writeNoException();
                        reply.writeString(contents);
                        return true;
                    }
                case android.os.storage.IMountService.Stub.TRANSACTION_isConvertibleToFBE :
                    {
                        data.enforceInterface(android.os.storage.IMountService.Stub.DESCRIPTOR);
                        int resultCode = (isConvertibleToFBE()) ? 1 : 0;
                        reply.writeNoException();
                        reply.writeInt(resultCode);
                        return true;
                    }
                case android.os.storage.IMountService.Stub.TRANSACTION_resizeSecureContainer :
                    {
                        data.enforceInterface(android.os.storage.IMountService.Stub.DESCRIPTOR);
                        java.lang.String id;
                        id = data.readString();
                        int sizeMb;
                        sizeMb = data.readInt();
                        java.lang.String key;
                        key = data.readString();
                        int resultCode = resizeSecureContainer(id, sizeMb, key);
                        reply.writeNoException();
                        reply.writeInt(resultCode);
                        return true;
                    }
                case android.os.storage.IMountService.Stub.TRANSACTION_lastMaintenance :
                    {
                        data.enforceInterface(android.os.storage.IMountService.Stub.DESCRIPTOR);
                        long lastMaintenance = lastMaintenance();
                        reply.writeNoException();
                        reply.writeLong(lastMaintenance);
                        return true;
                    }
                case android.os.storage.IMountService.Stub.TRANSACTION_runMaintenance :
                    {
                        data.enforceInterface(android.os.storage.IMountService.Stub.DESCRIPTOR);
                        runMaintenance();
                        reply.writeNoException();
                        return true;
                    }
                case android.os.storage.IMountService.Stub.TRANSACTION_waitForAsecScan :
                    {
                        data.enforceInterface(android.os.storage.IMountService.Stub.DESCRIPTOR);
                        waitForAsecScan();
                        reply.writeNoException();
                        return true;
                    }
                case android.os.storage.IMountService.Stub.TRANSACTION_getDisks :
                    {
                        data.enforceInterface(android.os.storage.IMountService.Stub.DESCRIPTOR);
                        android.os.storage.DiskInfo[] disks = getDisks();
                        reply.writeNoException();
                        reply.writeTypedArray(disks, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
                        return true;
                    }
                case android.os.storage.IMountService.Stub.TRANSACTION_getVolumes :
                    {
                        data.enforceInterface(android.os.storage.IMountService.Stub.DESCRIPTOR);
                        int _flags = data.readInt();
                        android.os.storage.VolumeInfo[] volumes = getVolumes(_flags);
                        reply.writeNoException();
                        reply.writeTypedArray(volumes, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
                        return true;
                    }
                case android.os.storage.IMountService.Stub.TRANSACTION_getVolumeRecords :
                    {
                        data.enforceInterface(android.os.storage.IMountService.Stub.DESCRIPTOR);
                        int _flags = data.readInt();
                        android.os.storage.VolumeRecord[] volumes = getVolumeRecords(_flags);
                        reply.writeNoException();
                        reply.writeTypedArray(volumes, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
                        return true;
                    }
                case android.os.storage.IMountService.Stub.TRANSACTION_mount :
                    {
                        data.enforceInterface(android.os.storage.IMountService.Stub.DESCRIPTOR);
                        java.lang.String volId = data.readString();
                        mount(volId);
                        reply.writeNoException();
                        return true;
                    }
                case android.os.storage.IMountService.Stub.TRANSACTION_unmount :
                    {
                        data.enforceInterface(android.os.storage.IMountService.Stub.DESCRIPTOR);
                        java.lang.String volId = data.readString();
                        unmount(volId);
                        reply.writeNoException();
                        return true;
                    }
                case android.os.storage.IMountService.Stub.TRANSACTION_format :
                    {
                        data.enforceInterface(android.os.storage.IMountService.Stub.DESCRIPTOR);
                        java.lang.String volId = data.readString();
                        format(volId);
                        reply.writeNoException();
                        return true;
                    }
                case android.os.storage.IMountService.Stub.TRANSACTION_benchmark :
                    {
                        data.enforceInterface(android.os.storage.IMountService.Stub.DESCRIPTOR);
                        java.lang.String volId = data.readString();
                        long res = benchmark(volId);
                        reply.writeNoException();
                        reply.writeLong(res);
                        return true;
                    }
                case android.os.storage.IMountService.Stub.TRANSACTION_partitionPublic :
                    {
                        data.enforceInterface(android.os.storage.IMountService.Stub.DESCRIPTOR);
                        java.lang.String diskId = data.readString();
                        partitionPublic(diskId);
                        reply.writeNoException();
                        return true;
                    }
                case android.os.storage.IMountService.Stub.TRANSACTION_partitionPrivate :
                    {
                        data.enforceInterface(android.os.storage.IMountService.Stub.DESCRIPTOR);
                        java.lang.String diskId = data.readString();
                        partitionPrivate(diskId);
                        reply.writeNoException();
                        return true;
                    }
                case android.os.storage.IMountService.Stub.TRANSACTION_partitionMixed :
                    {
                        data.enforceInterface(android.os.storage.IMountService.Stub.DESCRIPTOR);
                        java.lang.String diskId = data.readString();
                        int ratio = data.readInt();
                        partitionMixed(diskId, ratio);
                        reply.writeNoException();
                        return true;
                    }
                case android.os.storage.IMountService.Stub.TRANSACTION_setVolumeNickname :
                    {
                        data.enforceInterface(android.os.storage.IMountService.Stub.DESCRIPTOR);
                        java.lang.String volId = data.readString();
                        java.lang.String nickname = data.readString();
                        setVolumeNickname(volId, nickname);
                        reply.writeNoException();
                        return true;
                    }
                case android.os.storage.IMountService.Stub.TRANSACTION_setVolumeUserFlags :
                    {
                        data.enforceInterface(android.os.storage.IMountService.Stub.DESCRIPTOR);
                        java.lang.String volId = data.readString();
                        int _flags = data.readInt();
                        int _mask = data.readInt();
                        setVolumeUserFlags(volId, _flags, _mask);
                        reply.writeNoException();
                        return true;
                    }
                case android.os.storage.IMountService.Stub.TRANSACTION_forgetVolume :
                    {
                        data.enforceInterface(android.os.storage.IMountService.Stub.DESCRIPTOR);
                        java.lang.String fsUuid = data.readString();
                        forgetVolume(fsUuid);
                        reply.writeNoException();
                        return true;
                    }
                case android.os.storage.IMountService.Stub.TRANSACTION_forgetAllVolumes :
                    {
                        data.enforceInterface(android.os.storage.IMountService.Stub.DESCRIPTOR);
                        forgetAllVolumes();
                        reply.writeNoException();
                        return true;
                    }
                case android.os.storage.IMountService.Stub.TRANSACTION_setDebugFlags :
                    {
                        data.enforceInterface(android.os.storage.IMountService.Stub.DESCRIPTOR);
                        int _flags = data.readInt();
                        int _mask = data.readInt();
                        setDebugFlags(_flags, _mask);
                        reply.writeNoException();
                        return true;
                    }
                case android.os.storage.IMountService.Stub.TRANSACTION_getPrimaryStorageUuid :
                    {
                        data.enforceInterface(android.os.storage.IMountService.Stub.DESCRIPTOR);
                        java.lang.String volumeUuid = getPrimaryStorageUuid();
                        reply.writeNoException();
                        reply.writeString(volumeUuid);
                        return true;
                    }
                case android.os.storage.IMountService.Stub.TRANSACTION_setPrimaryStorageUuid :
                    {
                        data.enforceInterface(android.os.storage.IMountService.Stub.DESCRIPTOR);
                        java.lang.String volumeUuid = data.readString();
                        android.content.pm.IPackageMoveObserver listener = IPackageMoveObserver.Stub.asInterface(data.readStrongBinder());
                        setPrimaryStorageUuid(volumeUuid, listener);
                        reply.writeNoException();
                        return true;
                    }
                case android.os.storage.IMountService.Stub.TRANSACTION_createUserKey :
                    {
                        data.enforceInterface(android.os.storage.IMountService.Stub.DESCRIPTOR);
                        int userId = data.readInt();
                        int serialNumber = data.readInt();
                        boolean ephemeral = data.readInt() != 0;
                        createUserKey(userId, serialNumber, ephemeral);
                        reply.writeNoException();
                        return true;
                    }
                case android.os.storage.IMountService.Stub.TRANSACTION_destroyUserKey :
                    {
                        data.enforceInterface(android.os.storage.IMountService.Stub.DESCRIPTOR);
                        int userId = data.readInt();
                        destroyUserKey(userId);
                        reply.writeNoException();
                        return true;
                    }
                case android.os.storage.IMountService.Stub.TRANSACTION_addUserKeyAuth :
                    {
                        data.enforceInterface(android.os.storage.IMountService.Stub.DESCRIPTOR);
                        int userId = data.readInt();
                        int serialNumber = data.readInt();
                        byte[] token = data.createByteArray();
                        byte[] secret = data.createByteArray();
                        addUserKeyAuth(userId, serialNumber, token, secret);
                        reply.writeNoException();
                        return true;
                    }
                case android.os.storage.IMountService.Stub.TRANSACTION_fixateNewestUserKeyAuth :
                    {
                        data.enforceInterface(android.os.storage.IMountService.Stub.DESCRIPTOR);
                        int userId = data.readInt();
                        fixateNewestUserKeyAuth(userId);
                        reply.writeNoException();
                        return true;
                    }
                case android.os.storage.IMountService.Stub.TRANSACTION_unlockUserKey :
                    {
                        data.enforceInterface(android.os.storage.IMountService.Stub.DESCRIPTOR);
                        int userId = data.readInt();
                        int serialNumber = data.readInt();
                        byte[] token = data.createByteArray();
                        byte[] secret = data.createByteArray();
                        unlockUserKey(userId, serialNumber, token, secret);
                        reply.writeNoException();
                        return true;
                    }
                case android.os.storage.IMountService.Stub.TRANSACTION_lockUserKey :
                    {
                        data.enforceInterface(android.os.storage.IMountService.Stub.DESCRIPTOR);
                        int userId = data.readInt();
                        lockUserKey(userId);
                        reply.writeNoException();
                        return true;
                    }
                case android.os.storage.IMountService.Stub.TRANSACTION_isUserKeyUnlocked :
                    {
                        data.enforceInterface(android.os.storage.IMountService.Stub.DESCRIPTOR);
                        int userId = data.readInt();
                        boolean result = isUserKeyUnlocked(userId);
                        reply.writeNoException();
                        reply.writeInt(result ? 1 : 0);
                        return true;
                    }
                case android.os.storage.IMountService.Stub.TRANSACTION_prepareUserStorage :
                    {
                        data.enforceInterface(android.os.storage.IMountService.Stub.DESCRIPTOR);
                        java.lang.String volumeUuid = data.readString();
                        int userId = data.readInt();
                        int serialNumber = data.readInt();
                        int _flags = data.readInt();
                        prepareUserStorage(volumeUuid, userId, serialNumber, _flags);
                        reply.writeNoException();
                        return true;
                    }
                case android.os.storage.IMountService.Stub.TRANSACTION_destroyUserStorage :
                    {
                        data.enforceInterface(android.os.storage.IMountService.Stub.DESCRIPTOR);
                        java.lang.String volumeUuid = data.readString();
                        int userId = data.readInt();
                        int _flags = data.readInt();
                        destroyUserStorage(volumeUuid, userId, _flags);
                        reply.writeNoException();
                        return true;
                    }
                case android.os.storage.IMountService.Stub.TRANSACTION_mountAppFuse :
                    {
                        data.enforceInterface(android.os.storage.IMountService.Stub.DESCRIPTOR);
                        java.lang.String name = data.readString();
                        android.os.ParcelFileDescriptor fd = mountAppFuse(name);
                        reply.writeNoException();
                        reply.writeParcelable(fd, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
                        return true;
                    }
            }
            return super.onTransact(code, data, reply, flags);
        }
    }

    /* Creates a secure container with the specified parameters. Returns an int
    consistent with MountServiceResultCode
     */
    public int createSecureContainer(java.lang.String id, int sizeMb, java.lang.String fstype, java.lang.String key, int ownerUid, boolean external) throws android.os.RemoteException;

    /* Destroy a secure container, and free up all resources associated with it.
    NOTE: Ensure all references are released prior to deleting. Returns an
    int consistent with MountServiceResultCode
     */
    public int destroySecureContainer(java.lang.String id, boolean force) throws android.os.RemoteException;

    /* Finalize a container which has just been created and populated. After
    finalization, the container is immutable. Returns an int consistent with
    MountServiceResultCode
     */
    public int finalizeSecureContainer(java.lang.String id) throws android.os.RemoteException;

    /**
     * Call into MountService by PackageManager to notify that its done
     * processing the media status update request.
     */
    public void finishMediaUpdate() throws android.os.RemoteException;

    /**
     * Format external storage given a mount point. Returns an int consistent
     * with MountServiceResultCode
     */
    public int formatVolume(java.lang.String mountPoint) throws android.os.RemoteException;

    /**
     * Gets the path to the mounted Opaque Binary Blob (OBB).
     */
    public java.lang.String getMountedObbPath(java.lang.String rawPath) throws android.os.RemoteException;

    /**
     * Gets an Array of currently known secure container IDs
     */
    public java.lang.String[] getSecureContainerList() throws android.os.RemoteException;

    /* Returns the filesystem path of a mounted secure container. */
    public java.lang.String getSecureContainerPath(java.lang.String id) throws android.os.RemoteException;

    /**
     * Returns an array of pids with open files on the specified path.
     */
    public int[] getStorageUsers(java.lang.String path) throws android.os.RemoteException;

    /**
     * Gets the state of a volume via its mountpoint.
     */
    public java.lang.String getVolumeState(java.lang.String mountPoint) throws android.os.RemoteException;

    /**
     * Checks whether the specified Opaque Binary Blob (OBB) is mounted
     * somewhere.
     */
    public boolean isObbMounted(java.lang.String rawPath) throws android.os.RemoteException;

    /* Returns true if the specified container is mounted */
    public boolean isSecureContainerMounted(java.lang.String id) throws android.os.RemoteException;

    /**
     * Returns true if a USB mass storage host is connected
     */
    public boolean isUsbMassStorageConnected() throws android.os.RemoteException;

    /**
     * Returns true if a USB mass storage host is enabled (media is shared)
     */
    public boolean isUsbMassStorageEnabled() throws android.os.RemoteException;

    /**
     * Mounts an Opaque Binary Blob (OBB) with the specified decryption key and
     * only allows the calling process's UID access to the contents.
     * MountService will call back to the supplied IObbActionListener to inform
     * it of the terminal state of the call.
     */
    public void mountObb(java.lang.String rawPath, java.lang.String canonicalPath, java.lang.String key, android.os.storage.IObbActionListener token, int nonce) throws android.os.RemoteException;

    /* Mount a secure container with the specified key and owner UID. Returns an
    int consistent with MountServiceResultCode
     */
    public int mountSecureContainer(java.lang.String id, java.lang.String key, int ownerUid, boolean readOnly) throws android.os.RemoteException;

    /**
     * Mount external storage at given mount point. Returns an int consistent
     * with MountServiceResultCode
     */
    public int mountVolume(java.lang.String mountPoint) throws android.os.RemoteException;

    /**
     * Registers an IMountServiceListener for receiving async notifications.
     */
    public void registerListener(android.os.storage.IMountServiceListener listener) throws android.os.RemoteException;

    /* Rename an unmounted secure container. Returns an int consistent with
    MountServiceResultCode
     */
    public int renameSecureContainer(java.lang.String oldId, java.lang.String newId) throws android.os.RemoteException;

    /**
     * Enables / disables USB mass storage. The caller should check actual
     * status of enabling/disabling USB mass storage via StorageEventListener.
     */
    public void setUsbMassStorageEnabled(boolean enable) throws android.os.RemoteException;

    /**
     * Shuts down the MountService and gracefully unmounts all external media.
     * Invokes call back once the shutdown is complete.
     */
    public void shutdown(android.os.storage.IMountShutdownObserver observer) throws android.os.RemoteException;

    /**
     * Unmounts an Opaque Binary Blob (OBB). When the force flag is specified,
     * any program using it will be forcibly killed to unmount the image.
     * MountService will call back to the supplied IObbActionListener to inform
     * it of the terminal state of the call.
     */
    public void unmountObb(java.lang.String rawPath, boolean force, android.os.storage.IObbActionListener token, int nonce) throws android.os.RemoteException;

    /* Unount a secure container. Returns an int consistent with
    MountServiceResultCode
     */
    public int unmountSecureContainer(java.lang.String id, boolean force) throws android.os.RemoteException;

    /**
     * Safely unmount external storage at given mount point. The unmount is an
     * asynchronous operation. Applications should register StorageEventListener
     * for storage related status changes.
     *
     * @param mountPoint
     * 		the mount point
     * @param force
     * 		whether or not to forcefully unmount it (e.g. even if programs are using this
     * 		data currently)
     * @param removeEncryption
     * 		whether or not encryption mapping should be removed from the volume.
     * 		This value implies {@code force}.
     */
    public void unmountVolume(java.lang.String mountPoint, boolean force, boolean removeEncryption) throws android.os.RemoteException;

    /**
     * Unregisters an IMountServiceListener
     */
    public void unregisterListener(android.os.storage.IMountServiceListener listener) throws android.os.RemoteException;

    /**
     * Returns whether or not the external storage is emulated.
     */
    public boolean isExternalStorageEmulated() throws android.os.RemoteException;

    /**
     * The volume is not encrypted.
     */
    static final int ENCRYPTION_STATE_NONE = 1;

    /**
     * The volume has been encrypted succesfully.
     */
    static final int ENCRYPTION_STATE_OK = 0;

    /**
     * The volume is in a bad state.
     */
    static final int ENCRYPTION_STATE_ERROR_UNKNOWN = -1;

    /**
     * Encryption is incomplete
     */
    static final int ENCRYPTION_STATE_ERROR_INCOMPLETE = -2;

    /**
     * Encryption is incomplete and irrecoverable
     */
    static final int ENCRYPTION_STATE_ERROR_INCONSISTENT = -3;

    /**
     * Underlying data is corrupt
     */
    static final int ENCRYPTION_STATE_ERROR_CORRUPT = -4;

    /**
     * Determines the encryption state of the volume.
     *
     * @return a numerical value. See {@code ENCRYPTION_STATE_*} for possible
    values.
    Note that this has been replaced in most cases by the APIs in
    StorageManager (see isEncryptable and below)
    This is still useful to get the error state when encryption has failed
    and CryptKeeper needs to throw up a screen advising the user what to do
     */
    public int getEncryptionState() throws android.os.RemoteException;

    /**
     * Decrypts any encrypted volumes.
     */
    public int decryptStorage(java.lang.String password) throws android.os.RemoteException;

    /**
     * Encrypts storage.
     */
    public int encryptStorage(int type, java.lang.String password) throws android.os.RemoteException;

    /**
     * Changes the encryption password.
     */
    public int changeEncryptionPassword(int type, java.lang.String password) throws android.os.RemoteException;

    /**
     * Verify the encryption password against the stored volume.  This method
     * may only be called by the system process.
     */
    public int verifyEncryptionPassword(java.lang.String password) throws android.os.RemoteException;

    /**
     * Returns list of all mountable volumes.
     */
    public android.os.storage.StorageVolume[] getVolumeList(int uid, java.lang.String packageName, int flags) throws android.os.RemoteException;

    /**
     * Gets the path on the filesystem for the ASEC container itself.
     *
     * @param cid
     * 		ASEC container ID
     * @return path to filesystem or {@code null} if it's not found
     * @throws RemoteException
     * 		
     */
    public java.lang.String getSecureContainerFilesystemPath(java.lang.String cid) throws android.os.RemoteException;

    /* Fix permissions in a container which has just been created and populated.
    Returns an int consistent with MountServiceResultCode
     */
    public int fixPermissionsSecureContainer(java.lang.String id, int gid, java.lang.String filename) throws android.os.RemoteException;

    /**
     * Ensure that all directories along given path exist, creating parent
     * directories as needed. Validates that given path is absolute and that it
     * contains no relative "." or ".." paths or symlinks. Also ensures that
     * path belongs to a volume managed by vold, and that path is either
     * external storage data or OBB directory belonging to calling app.
     */
    public int mkdirs(java.lang.String callingPkg, java.lang.String path) throws android.os.RemoteException;

    /**
     * Determines the type of the encryption password
     *
     * @return PasswordType
     */
    public int getPasswordType() throws android.os.RemoteException;

    /**
     * Get password from vold
     *
     * @return password or empty string
     */
    public java.lang.String getPassword() throws android.os.RemoteException;

    /**
     * Securely clear password from vold
     */
    public void clearPassword() throws android.os.RemoteException;

    /**
     * Set a field in the crypto header.
     *
     * @param field
     * 		field to set
     * @param contents
     * 		contents to set in field
     */
    public void setField(java.lang.String field, java.lang.String contents) throws android.os.RemoteException;

    /**
     * Gets a field from the crypto header.
     *
     * @param field
     * 		field to get
     * @return contents of field
     */
    public java.lang.String getField(java.lang.String field) throws android.os.RemoteException;

    public boolean isConvertibleToFBE() throws android.os.RemoteException;

    public int resizeSecureContainer(java.lang.String id, int sizeMb, java.lang.String key) throws android.os.RemoteException;

    /**
     * Report the time of the last maintenance operation such as fstrim.
     *
     * @return Timestamp of the last maintenance operation, in the
    System.currentTimeMillis() time base
     * @throws RemoteException
     * 		
     */
    public long lastMaintenance() throws android.os.RemoteException;

    /**
     * Kick off an immediate maintenance operation
     *
     * @throws RemoteException
     * 		
     */
    public void runMaintenance() throws android.os.RemoteException;

    public void waitForAsecScan() throws android.os.RemoteException;

    public android.os.storage.DiskInfo[] getDisks() throws android.os.RemoteException;

    public android.os.storage.VolumeInfo[] getVolumes(int flags) throws android.os.RemoteException;

    public android.os.storage.VolumeRecord[] getVolumeRecords(int flags) throws android.os.RemoteException;

    public void mount(java.lang.String volId) throws android.os.RemoteException;

    public void unmount(java.lang.String volId) throws android.os.RemoteException;

    public void format(java.lang.String volId) throws android.os.RemoteException;

    public long benchmark(java.lang.String volId) throws android.os.RemoteException;

    public void partitionPublic(java.lang.String diskId) throws android.os.RemoteException;

    public void partitionPrivate(java.lang.String diskId) throws android.os.RemoteException;

    public void partitionMixed(java.lang.String diskId, int ratio) throws android.os.RemoteException;

    public void setVolumeNickname(java.lang.String fsUuid, java.lang.String nickname) throws android.os.RemoteException;

    public void setVolumeUserFlags(java.lang.String fsUuid, int flags, int mask) throws android.os.RemoteException;

    public void forgetVolume(java.lang.String fsUuid) throws android.os.RemoteException;

    public void forgetAllVolumes() throws android.os.RemoteException;

    public void setDebugFlags(int flags, int mask) throws android.os.RemoteException;

    public java.lang.String getPrimaryStorageUuid() throws android.os.RemoteException;

    public void setPrimaryStorageUuid(java.lang.String volumeUuid, android.content.pm.IPackageMoveObserver callback) throws android.os.RemoteException;

    public void createUserKey(int userId, int serialNumber, boolean ephemeral) throws android.os.RemoteException;

    public void destroyUserKey(int userId) throws android.os.RemoteException;

    public void addUserKeyAuth(int userId, int serialNumber, byte[] token, byte[] secret) throws android.os.RemoteException;

    public void fixateNewestUserKeyAuth(int userId) throws android.os.RemoteException;

    public void unlockUserKey(int userId, int serialNumber, byte[] token, byte[] secret) throws android.os.RemoteException;

    public void lockUserKey(int userId) throws android.os.RemoteException;

    public boolean isUserKeyUnlocked(int userId) throws android.os.RemoteException;

    public void prepareUserStorage(java.lang.String volumeUuid, int userId, int serialNumber, int flags) throws android.os.RemoteException;

    public void destroyUserStorage(java.lang.String volumeUuid, int userId, int flags) throws android.os.RemoteException;

    public android.os.ParcelFileDescriptor mountAppFuse(java.lang.String name) throws android.os.RemoteException;
}

