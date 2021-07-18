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
package android.view;


/**
 * An input channel specifies the file descriptors used to send input events to
 * a window in another process.  It is Parcelable so that it can be sent
 * to the process that is to receive events.  Only one thread should be reading
 * from an InputChannel at a time.
 *
 * @unknown 
 */
public final class InputChannel implements android.os.Parcelable {
    private static final java.lang.String TAG = "InputChannel";

    private static final boolean DEBUG = false;

    @android.annotation.UnsupportedAppUsage
    @android.annotation.NonNull
    public static final android.os.Parcelable.Creator<android.view.InputChannel> CREATOR = new android.os.Parcelable.Creator<android.view.InputChannel>() {
        public android.view.InputChannel createFromParcel(android.os.Parcel source) {
            android.view.InputChannel result = new android.view.InputChannel();
            result.readFromParcel(source);
            return result;
        }

        public android.view.InputChannel[] newArray(int size) {
            return new android.view.InputChannel[size];
        }
    };

    @java.lang.SuppressWarnings("unused")
    @android.annotation.UnsupportedAppUsage
    private long mPtr;// used by native code


    private static native android.view.InputChannel[] nativeOpenInputChannelPair(java.lang.String name);

    private native void nativeDispose(boolean finalized);

    private native void nativeTransferTo(android.view.InputChannel other);

    private native void nativeReadFromParcel(android.os.Parcel parcel);

    private native void nativeWriteToParcel(android.os.Parcel parcel);

    private native void nativeDup(android.view.InputChannel target);

    private native android.os.IBinder nativeGetToken();

    private native void nativeSetToken(android.os.IBinder token);

    private native java.lang.String nativeGetName();

    /**
     * Creates an uninitialized input channel.
     * It can be initialized by reading from a Parcel or by transferring the state of
     * another input channel into this one.
     */
    @android.annotation.UnsupportedAppUsage
    public InputChannel() {
    }

    @java.lang.Override
    protected void finalize() throws java.lang.Throwable {
        try {
            nativeDispose(true);
        } finally {
            super.finalize();
        }
    }

    /**
     * Creates a new input channel pair.  One channel should be provided to the input
     * dispatcher and the other to the application's input queue.
     *
     * @param name
     * 		The descriptive (non-unique) name of the channel pair.
     * @return A pair of input channels.  The first channel is designated as the
    server channel and should be used to publish input events.  The second channel
    is designated as the client channel and should be used to consume input events.
     */
    public static android.view.InputChannel[] openInputChannelPair(java.lang.String name) {
        if (name == null) {
            throw new java.lang.IllegalArgumentException("name must not be null");
        }
        if (android.view.InputChannel.DEBUG) {
            android.util.Slog.d(android.view.InputChannel.TAG, ("Opening input channel pair '" + name) + "'");
        }
        return android.view.InputChannel.nativeOpenInputChannelPair(name);
    }

    /**
     * Gets the name of the input channel.
     *
     * @return The input channel name.
     */
    public java.lang.String getName() {
        java.lang.String name = nativeGetName();
        return name != null ? name : "uninitialized";
    }

    /**
     * Disposes the input channel.
     * Explicitly releases the reference this object is holding on the input channel.
     * When all references are released, the input channel will be closed.
     */
    public void dispose() {
        nativeDispose(false);
    }

    /**
     * Transfers ownership of the internal state of the input channel to another
     * instance and invalidates this instance.  This is used to pass an input channel
     * as an out parameter in a binder call.
     *
     * @param other
     * 		The other input channel instance.
     */
    public void transferTo(android.view.InputChannel outParameter) {
        if (outParameter == null) {
            throw new java.lang.IllegalArgumentException("outParameter must not be null");
        }
        nativeTransferTo(outParameter);
    }

    /**
     * Duplicates the input channel.
     */
    public android.view.InputChannel dup() {
        android.view.InputChannel target = new android.view.InputChannel();
        nativeDup(target);
        return target;
    }

    @java.lang.Override
    public int describeContents() {
        return android.os.Parcelable.CONTENTS_FILE_DESCRIPTOR;
    }

    public void readFromParcel(android.os.Parcel in) {
        if (in == null) {
            throw new java.lang.IllegalArgumentException("in must not be null");
        }
        nativeReadFromParcel(in);
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel out, int flags) {
        if (out == null) {
            throw new java.lang.IllegalArgumentException("out must not be null");
        }
        nativeWriteToParcel(out);
        if ((flags & PARCELABLE_WRITE_RETURN_VALUE) != 0) {
            dispose();
        }
    }

    @java.lang.Override
    public java.lang.String toString() {
        return getName();
    }

    public android.os.IBinder getToken() {
        return nativeGetToken();
    }

    public void setToken(android.os.IBinder token) {
        nativeSetToken(token);
    }
}

