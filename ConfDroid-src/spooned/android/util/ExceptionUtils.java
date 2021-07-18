/**
 * Copyright (C) 2014 The Android Open Source Project
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
package android.util;


/**
 * Utility methods for proxying richer exceptions across Binder calls.
 *
 * @unknown 
 */
public class ExceptionUtils {
    // TODO: longer term these should be replaced with first-class
    // Parcel.read/writeException() and AIDL support, but for now do this using
    // a nasty hack.
    private static final java.lang.String PREFIX_IO = "\u2603";

    public static java.lang.RuntimeException wrap(java.io.IOException e) {
        throw new java.lang.IllegalStateException(android.util.ExceptionUtils.PREFIX_IO + e.getMessage());
    }

    public static void maybeUnwrapIOException(java.lang.RuntimeException e) throws java.io.IOException {
        if ((e instanceof java.lang.IllegalStateException) && e.getMessage().startsWith(android.util.ExceptionUtils.PREFIX_IO)) {
            throw new java.io.IOException(e.getMessage().substring(android.util.ExceptionUtils.PREFIX_IO.length()));
        }
    }

    public static java.lang.String getCompleteMessage(java.lang.String msg, java.lang.Throwable t) {
        final java.lang.StringBuilder builder = new java.lang.StringBuilder();
        if (msg != null) {
            builder.append(msg).append(": ");
        }
        builder.append(t.getMessage());
        while ((t = t.getCause()) != null) {
            builder.append(": ").append(t.getMessage());
        } 
        return builder.toString();
    }

    public static java.lang.String getCompleteMessage(java.lang.Throwable t) {
        return android.util.ExceptionUtils.getCompleteMessage(null, t);
    }
}

