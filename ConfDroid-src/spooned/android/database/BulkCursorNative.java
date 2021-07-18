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
package android.database;


/**
 * Native implementation of the bulk cursor. This is only for use in implementing
 * IPC, application code should use the Cursor interface.
 *
 * {@hide }
 */
public abstract class BulkCursorNative extends android.os.Binder implements android.database.IBulkCursor {
    public BulkCursorNative() {
        attachInterface(this, android.database.IBulkCursor.descriptor);
    }

    /**
     * Cast a Binder object into a content resolver interface, generating
     * a proxy if needed.
     */
    public static android.database.IBulkCursor asInterface(android.os.IBinder obj) {
        if (obj == null) {
            return null;
        }
        android.database.IBulkCursor in = ((android.database.IBulkCursor) (obj.queryLocalInterface(android.database.IBulkCursor.descriptor)));
        if (in != null) {
            return in;
        }
        return new android.database.BulkCursorProxy(obj);
    }

    @java.lang.Override
    public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException {
        try {
            switch (code) {
                case android.database.IBulkCursor.GET_CURSOR_WINDOW_TRANSACTION :
                    {
                        data.enforceInterface(android.database.IBulkCursor.descriptor);
                        int startPos = data.readInt();
                        android.database.CursorWindow window = getWindow(startPos);
                        reply.writeNoException();
                        if (window == null) {
                            reply.writeInt(0);
                        } else {
                            reply.writeInt(1);
                            window.writeToParcel(reply, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
                        }
                        return true;
                    }
                case android.database.IBulkCursor.DEACTIVATE_TRANSACTION :
                    {
                        data.enforceInterface(android.database.IBulkCursor.descriptor);
                        deactivate();
                        reply.writeNoException();
                        return true;
                    }
                case android.database.IBulkCursor.CLOSE_TRANSACTION :
                    {
                        data.enforceInterface(android.database.IBulkCursor.descriptor);
                        close();
                        reply.writeNoException();
                        return true;
                    }
                case android.database.IBulkCursor.REQUERY_TRANSACTION :
                    {
                        data.enforceInterface(android.database.IBulkCursor.descriptor);
                        android.database.IContentObserver observer = IContentObserver.Stub.asInterface(data.readStrongBinder());
                        int count = requery(observer);
                        reply.writeNoException();
                        reply.writeInt(count);
                        reply.writeBundle(getExtras());
                        return true;
                    }
                case android.database.IBulkCursor.ON_MOVE_TRANSACTION :
                    {
                        data.enforceInterface(android.database.IBulkCursor.descriptor);
                        int position = data.readInt();
                        onMove(position);
                        reply.writeNoException();
                        return true;
                    }
                case android.database.IBulkCursor.GET_EXTRAS_TRANSACTION :
                    {
                        data.enforceInterface(android.database.IBulkCursor.descriptor);
                        android.os.Bundle extras = getExtras();
                        reply.writeNoException();
                        reply.writeBundle(extras);
                        return true;
                    }
                case android.database.IBulkCursor.RESPOND_TRANSACTION :
                    {
                        data.enforceInterface(android.database.IBulkCursor.descriptor);
                        android.os.Bundle extras = data.readBundle();
                        android.os.Bundle returnExtras = respond(extras);
                        reply.writeNoException();
                        reply.writeBundle(returnExtras);
                        return true;
                    }
            }
        } catch (java.lang.Exception e) {
            android.database.DatabaseUtils.writeExceptionToParcel(reply, e);
            return true;
        }
        return super.onTransact(code, data, reply, flags);
    }

    public android.os.IBinder asBinder() {
        return this;
    }
}

