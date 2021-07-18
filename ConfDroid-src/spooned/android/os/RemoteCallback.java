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
package android.os;


/**
 *
 *
 * @unknown 
 */
@android.annotation.SystemApi
public final class RemoteCallback implements android.os.Parcelable {
    public interface OnResultListener {
        public void onResult(android.os.Bundle result);
    }

    private final android.os.RemoteCallback.OnResultListener mListener;

    private final android.os.Handler mHandler;

    private final android.os.IRemoteCallback mCallback;

    public RemoteCallback(android.os.RemoteCallback.OnResultListener listener) {
        this(listener, null);
    }

    public RemoteCallback(@android.annotation.NonNull
    android.os.RemoteCallback.OnResultListener listener, @android.annotation.Nullable
    android.os.Handler handler) {
        if (listener == null) {
            throw new java.lang.NullPointerException("listener cannot be null");
        }
        mListener = listener;
        mHandler = handler;
        mCallback = new android.os.IRemoteCallback.Stub() {
            @java.lang.Override
            public void sendResult(android.os.Bundle data) {
                android.os.RemoteCallback.this.sendResult(data);
            }
        };
    }

    RemoteCallback(android.os.Parcel parcel) {
        mListener = null;
        mHandler = null;
        mCallback = IRemoteCallback.Stub.asInterface(parcel.readStrongBinder());
    }

    public void sendResult(@android.annotation.Nullable
    final android.os.Bundle result) {
        // Do local dispatch
        if (mListener != null) {
            if (mHandler != null) {
                mHandler.post(new java.lang.Runnable() {
                    @java.lang.Override
                    public void run() {
                        mListener.onResult(result);
                    }
                });
            } else {
                mListener.onResult(result);
            }
            // Do remote dispatch
        } else {
            try {
                mCallback.sendResult(result);
            } catch (android.os.RemoteException e) {
                /* ignore */
            }
        }
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel parcel, int flags) {
        parcel.writeStrongBinder(mCallback.asBinder());
    }

    public static final android.os.Parcelable.Creator<android.os.RemoteCallback> CREATOR = new android.os.Parcelable.Creator<android.os.RemoteCallback>() {
        public android.os.RemoteCallback createFromParcel(android.os.Parcel parcel) {
            return new android.os.RemoteCallback(parcel);
        }

        public android.os.RemoteCallback[] newArray(int size) {
            return new android.os.RemoteCallback[size];
        }
    };
}

