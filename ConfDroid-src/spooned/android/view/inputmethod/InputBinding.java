/**
 * Copyright (C) 2007-2008 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package android.view.inputmethod;


/**
 * Information given to an {@link InputMethod} about a client connecting
 * to it.
 */
public final class InputBinding implements android.os.Parcelable {
    static final java.lang.String TAG = "InputBinding";

    /**
     * The connection back to the client.
     */
    final android.view.inputmethod.InputConnection mConnection;

    /**
     * A remotable token for the connection back to the client.
     */
    final android.os.IBinder mConnectionToken;

    /**
     * The UID where this binding came from.
     */
    final int mUid;

    /**
     * The PID where this binding came from.
     */
    final int mPid;

    /**
     * Constructor.
     *
     * @param conn
     * 		The interface for communicating back with the application.
     * @param connToken
     * 		A remoteable token for communicating across processes.
     * @param uid
     * 		The user id of the client of this binding.
     * @param pid
     * 		The process id of where the binding came from.
     */
    public InputBinding(android.view.inputmethod.InputConnection conn, android.os.IBinder connToken, int uid, int pid) {
        mConnection = conn;
        mConnectionToken = connToken;
        mUid = uid;
        mPid = pid;
    }

    /**
     * Constructor from an existing InputBinding taking a new local input
     * connection interface.
     *
     * @param conn
     * 		The new connection interface.
     * @param binding
     * 		Existing binding to copy.
     */
    public InputBinding(android.view.inputmethod.InputConnection conn, android.view.inputmethod.InputBinding binding) {
        mConnection = conn;
        mConnectionToken = binding.getConnectionToken();
        mUid = binding.getUid();
        mPid = binding.getPid();
    }

    InputBinding(android.os.Parcel source) {
        mConnection = null;
        mConnectionToken = source.readStrongBinder();
        mUid = source.readInt();
        mPid = source.readInt();
    }

    /**
     * Return the connection for interacting back with the application.
     */
    public android.view.inputmethod.InputConnection getConnection() {
        return mConnection;
    }

    /**
     * Return the token for the connection back to the application.  You can
     * not use this directly, it must be converted to a {@link InputConnection}
     * for you.
     */
    public android.os.IBinder getConnectionToken() {
        return mConnectionToken;
    }

    /**
     * Return the user id of the client associated with this binding.
     */
    public int getUid() {
        return mUid;
    }

    /**
     * Return the process id where this binding came from.
     */
    public int getPid() {
        return mPid;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return ((((("InputBinding{" + mConnectionToken) + " / uid ") + mUid) + " / pid ") + mPid) + "}";
    }

    /**
     * Used to package this object into a {@link Parcel}.
     *
     * @param dest
     * 		The {@link Parcel} to be written.
     * @param flags
     * 		The flags used for parceling.
     */
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeStrongBinder(mConnectionToken);
        dest.writeInt(mUid);
        dest.writeInt(mPid);
    }

    /**
     * Used to make this class parcelable.
     */
    @android.annotation.NonNull
    public static final android.os.Parcelable.Creator<android.view.inputmethod.InputBinding> CREATOR = new android.os.Parcelable.Creator<android.view.inputmethod.InputBinding>() {
        public android.view.inputmethod.InputBinding createFromParcel(android.os.Parcel source) {
            return new android.view.inputmethod.InputBinding(source);
        }

        public android.view.inputmethod.InputBinding[] newArray(int size) {
            return new android.view.inputmethod.InputBinding[size];
        }
    };

    public int describeContents() {
        return 0;
    }
}

