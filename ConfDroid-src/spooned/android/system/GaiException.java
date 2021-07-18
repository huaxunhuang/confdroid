/**
 * Copyright (C) 2011 The Android Open Source Project
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
package android.system;


/**
 * An unchecked exception thrown when {@code getaddrinfo} or {@code getnameinfo} fails.
 * This exception contains the native {@link #error} value, should sophisticated
 * callers need to adjust their behavior based on the exact failure.
 *
 * @unknown 
 */
public final class GaiException extends java.lang.RuntimeException {
    private final java.lang.String functionName;

    /**
     * The native error value, for comparison with the {@code GAI_} constants in {@link OsConstants}.
     */
    public final int error;

    /**
     * Constructs an instance with the given function name and error value.
     */
    public GaiException(java.lang.String functionName, int error) {
        this.functionName = functionName;
        this.error = error;
    }

    /**
     * Constructs an instance with the given function name, error value, and cause.
     */
    public GaiException(java.lang.String functionName, int error, java.lang.Throwable cause) {
        super(cause);
        this.functionName = functionName;
        this.error = error;
    }

    /**
     * Converts the stashed function name and error value to a human-readable string.
     * We do this here rather than in the constructor so that callers only pay for
     * this if they need it.
     */
    @java.lang.Override
    public java.lang.String getMessage() {
        java.lang.String gaiName = android.system.OsConstants.gaiName(error);
        if (gaiName == null) {
            gaiName = "GAI_ error " + error;
        }
        java.lang.String description = Libcore.os.gai_strerror(error);
        return ((((functionName + " failed: ") + gaiName) + " (") + description) + ")";
    }

    /**
     *
     *
     * @unknown - internal use only.
     */
    public java.net.UnknownHostException rethrowAsUnknownHostException(java.lang.String detailMessage) throws java.net.UnknownHostException {
        java.net.UnknownHostException newException = new java.net.UnknownHostException(detailMessage);
        newException.initCause(this);
        throw newException;
    }

    /**
     *
     *
     * @unknown - internal use only.
     */
    public java.net.UnknownHostException rethrowAsUnknownHostException() throws java.net.UnknownHostException {
        throw rethrowAsUnknownHostException(getMessage());
    }
}

