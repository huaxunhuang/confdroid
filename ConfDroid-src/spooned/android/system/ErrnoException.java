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
 * A checked exception thrown when {@link Os} methods fail. This exception contains the native
 * errno value, for comparison against the constants in {@link OsConstants}, should sophisticated
 * callers need to adjust their behavior based on the exact failure.
 */
public final class ErrnoException extends java.lang.Exception {
    private final java.lang.String functionName;

    /**
     * The errno value, for comparison with the {@code E} constants in {@link OsConstants}.
     */
    public final int errno;

    /**
     * Constructs an instance with the given function name and errno value.
     */
    public ErrnoException(java.lang.String functionName, int errno) {
        this.functionName = functionName;
        this.errno = errno;
    }

    /**
     * Constructs an instance with the given function name, errno value, and cause.
     */
    public ErrnoException(java.lang.String functionName, int errno, java.lang.Throwable cause) {
        super(cause);
        this.functionName = functionName;
        this.errno = errno;
    }

    /**
     * Converts the stashed function name and errno value to a human-readable string.
     * We do this here rather than in the constructor so that callers only pay for
     * this if they need it.
     */
    @java.lang.Override
    public java.lang.String getMessage() {
        java.lang.String errnoName = android.system.OsConstants.errnoName(errno);
        if (errnoName == null) {
            errnoName = "errno " + errno;
        }
        java.lang.String description = Libcore.os.strerror(errno);
        return ((((functionName + " failed: ") + errnoName) + " (") + description) + ")";
    }

    /**
     *
     *
     * @unknown - internal use only.
     */
    public java.io.IOException rethrowAsIOException() throws java.io.IOException {
        java.io.IOException newException = new java.io.IOException(getMessage());
        newException.initCause(this);
        throw newException;
    }

    /**
     *
     *
     * @unknown - internal use only.
     */
    public java.net.SocketException rethrowAsSocketException() throws java.net.SocketException {
        throw new java.net.SocketException(getMessage(), this);
    }
}

