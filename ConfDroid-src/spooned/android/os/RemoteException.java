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
 * Parent exception for all Binder remote-invocation errors
 */
public class RemoteException extends android.util.AndroidException {
    public RemoteException() {
        super();
    }

    public RemoteException(java.lang.String message) {
        super(message);
    }

    /**
     * {@hide }
     */
    public java.lang.RuntimeException rethrowAsRuntimeException() {
        throw new java.lang.RuntimeException(this);
    }

    /**
     * Rethrow this exception when we know it came from the system server. This
     * gives us an opportunity to throw a nice clean
     * {@link DeadSystemException} signal to avoid spamming logs with
     * misleading stack traces.
     * <p>
     * Apps making calls into the system server may end up persisting internal
     * state or making security decisions based on the perceived success or
     * failure of a call, or any default values returned. For this reason, we
     * want to strongly throw when there was trouble with the transaction.
     *
     * @unknown 
     */
    public java.lang.RuntimeException rethrowFromSystemServer() {
        if (this instanceof android.os.DeadObjectException) {
            throw new java.lang.RuntimeException(new android.os.DeadSystemException());
        } else {
            throw new java.lang.RuntimeException(this);
        }
    }
}

