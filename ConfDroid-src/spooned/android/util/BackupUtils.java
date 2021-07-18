/**
 * Copyright (C) 2008 The Android Open Source Project
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
 * Utility methods for Backup/Restore
 *
 * @unknown 
 */
public class BackupUtils {
    public static final int NULL = 0;

    public static final int NOT_NULL = 1;

    /**
     * Thrown when there is a backup version mismatch
     * between the data received and what the system can handle
     */
    public static class BadVersionException extends java.lang.Exception {
        public BadVersionException(java.lang.String message) {
            super(message);
        }
    }

    public static java.lang.String readString(java.io.DataInputStream in) throws java.io.IOException {
        return in.readByte() == android.util.BackupUtils.NOT_NULL ? in.readUTF() : null;
    }

    public static void writeString(java.io.DataOutputStream out, java.lang.String val) throws java.io.IOException {
        if (val != null) {
            out.writeByte(android.util.BackupUtils.NOT_NULL);
            out.writeUTF(val);
        } else {
            out.writeByte(android.util.BackupUtils.NULL);
        }
    }
}

