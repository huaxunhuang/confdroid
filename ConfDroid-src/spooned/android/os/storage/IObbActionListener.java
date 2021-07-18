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
 * Callback class for receiving events from MountService about Opaque Binary
 * Blobs (OBBs).
 *
 * @unknown - Applications should use StorageManager to interact with OBBs.
 */
public interface IObbActionListener extends android.os.IInterface {
    /**
     * Local-side IPC implementation stub class.
     */
    public static abstract class Stub extends android.os.Binder implements android.os.storage.IObbActionListener {
        private static final java.lang.String DESCRIPTOR = "IObbActionListener";

        /**
         * Construct the stub at attach it to the interface.
         */
        public Stub() {
            this.attachInterface(this, android.os.storage.IObbActionListener.Stub.DESCRIPTOR);
        }

        /**
         * Cast an IBinder object into an IObbActionListener interface,
         * generating a proxy if needed.
         */
        public static android.os.storage.IObbActionListener asInterface(android.os.IBinder obj) {
            if (obj == null) {
                return null;
            }
            android.os.IInterface iin = ((android.os.IInterface) (obj.queryLocalInterface(android.os.storage.IObbActionListener.Stub.DESCRIPTOR)));
            if ((iin != null) && (iin instanceof android.os.storage.IObbActionListener)) {
                return ((android.os.storage.IObbActionListener) (iin));
            }
            return new android.os.storage.IObbActionListener.Stub.Proxy(obj);
        }

        public android.os.IBinder asBinder() {
            return this;
        }

        @java.lang.Override
        public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException {
            switch (code) {
                case android.os.IBinder.INTERFACE_TRANSACTION :
                    {
                        reply.writeString(android.os.storage.IObbActionListener.Stub.DESCRIPTOR);
                        return true;
                    }
                case android.os.storage.IObbActionListener.Stub.TRANSACTION_onObbResult :
                    {
                        data.enforceInterface(android.os.storage.IObbActionListener.Stub.DESCRIPTOR);
                        java.lang.String filename;
                        filename = data.readString();
                        int nonce;
                        nonce = data.readInt();
                        int status;
                        status = data.readInt();
                        this.onObbResult(filename, nonce, status);
                        reply.writeNoException();
                        return true;
                    }
            }
            return super.onTransact(code, data, reply, flags);
        }

        private static class Proxy implements android.os.storage.IObbActionListener {
            private android.os.IBinder mRemote;

            Proxy(android.os.IBinder remote) {
                mRemote = remote;
            }

            public android.os.IBinder asBinder() {
                return mRemote;
            }

            public java.lang.String getInterfaceDescriptor() {
                return android.os.storage.IObbActionListener.Stub.DESCRIPTOR;
            }

            /**
             * Return from an OBB action result.
             *
             * @param filename
             * 		the path to the OBB the operation was performed
             * 		on
             * @param returnCode
             * 		status of the operation
             */
            public void onObbResult(java.lang.String filename, int nonce, int status) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.storage.IObbActionListener.Stub.DESCRIPTOR);
                    _data.writeString(filename);
                    _data.writeInt(nonce);
                    _data.writeInt(status);
                    mRemote.transact(android.os.storage.IObbActionListener.Stub.TRANSACTION_onObbResult, _data, _reply, android.os.IBinder.FLAG_ONEWAY);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        static final int TRANSACTION_onObbResult = android.os.IBinder.FIRST_CALL_TRANSACTION + 0;
    }

    /**
     * Return from an OBB action result.
     *
     * @param filename
     * 		the path to the OBB the operation was performed on
     * @param nonce
     * 		identifier that is meaningful to the receiver
     * @param status
     * 		status code as defined in {@link OnObbStateChangeListener}
     */
    public void onObbResult(java.lang.String filename, int nonce, int status) throws android.os.RemoteException;
}

