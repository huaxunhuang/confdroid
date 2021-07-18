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
 * Callback class for receiving events related to shutdown.
 *
 * @unknown - For internal consumption only.
 */
public interface IMountShutdownObserver extends android.os.IInterface {
    /**
     * Local-side IPC implementation stub class.
     */
    public static abstract class Stub extends android.os.Binder implements android.os.storage.IMountShutdownObserver {
        private static final java.lang.String DESCRIPTOR = "IMountShutdownObserver";

        /**
         * Construct the stub at attach it to the interface.
         */
        public Stub() {
            this.attachInterface(this, android.os.storage.IMountShutdownObserver.Stub.DESCRIPTOR);
        }

        /**
         * Cast an IBinder object into an IMountShutdownObserver interface,
         * generating a proxy if needed.
         */
        public static android.os.storage.IMountShutdownObserver asInterface(android.os.IBinder obj) {
            if (obj == null) {
                return null;
            }
            android.os.IInterface iin = ((android.os.IInterface) (obj.queryLocalInterface(android.os.storage.IMountShutdownObserver.Stub.DESCRIPTOR)));
            if ((iin != null) && (iin instanceof android.os.storage.IMountShutdownObserver)) {
                return ((android.os.storage.IMountShutdownObserver) (iin));
            }
            return new android.os.storage.IMountShutdownObserver.Stub.Proxy(obj);
        }

        public android.os.IBinder asBinder() {
            return this;
        }

        @java.lang.Override
        public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException {
            switch (code) {
                case android.os.IBinder.INTERFACE_TRANSACTION :
                    {
                        reply.writeString(android.os.storage.IMountShutdownObserver.Stub.DESCRIPTOR);
                        return true;
                    }
                case android.os.storage.IMountShutdownObserver.Stub.TRANSACTION_onShutDownComplete :
                    {
                        data.enforceInterface(android.os.storage.IMountShutdownObserver.Stub.DESCRIPTOR);
                        int statusCode;
                        statusCode = data.readInt();
                        this.onShutDownComplete(statusCode);
                        reply.writeNoException();
                        return true;
                    }
            }
            return super.onTransact(code, data, reply, flags);
        }

        private static class Proxy implements android.os.storage.IMountShutdownObserver {
            private android.os.IBinder mRemote;

            Proxy(android.os.IBinder remote) {
                mRemote = remote;
            }

            public android.os.IBinder asBinder() {
                return mRemote;
            }

            public java.lang.String getInterfaceDescriptor() {
                return android.os.storage.IMountShutdownObserver.Stub.DESCRIPTOR;
            }

            /**
             * This method is called when the shutdown of MountService
             * completed.
             *
             * @param statusCode
             * 		indicates success or failure of the shutdown.
             */
            public void onShutDownComplete(int statusCode) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.storage.IMountShutdownObserver.Stub.DESCRIPTOR);
                    _data.writeInt(statusCode);
                    mRemote.transact(android.os.storage.IMountShutdownObserver.Stub.TRANSACTION_onShutDownComplete, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        static final int TRANSACTION_onShutDownComplete = android.os.IBinder.FIRST_CALL_TRANSACTION + 0;
    }

    /**
     * This method is called when the shutdown of MountService completed.
     *
     * @param statusCode
     * 		indicates success or failure of the shutdown.
     */
    public void onShutDownComplete(int statusCode) throws android.os.RemoteException;
}

