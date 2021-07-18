/**
 * Copyright (C) 2012 The Android Open Source Project
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
 * This class provides access to the centralized jni bindings for
 * SELinux interaction.
 * {@hide }
 */
public class SELinux {
    private static final java.lang.String TAG = "SELinux";

    /**
     * Keep in sync with ./external/libselinux/include/selinux/android.h
     */
    private static final int SELINUX_ANDROID_RESTORECON_NOCHANGE = 1;

    private static final int SELINUX_ANDROID_RESTORECON_VERBOSE = 2;

    private static final int SELINUX_ANDROID_RESTORECON_RECURSE = 4;

    private static final int SELINUX_ANDROID_RESTORECON_FORCE = 8;

    private static final int SELINUX_ANDROID_RESTORECON_DATADATA = 16;

    /**
     * Determine whether SELinux is disabled or enabled.
     *
     * @return a boolean indicating whether SELinux is enabled.
     */
    public static final native boolean isSELinuxEnabled();

    /**
     * Determine whether SELinux is permissive or enforcing.
     *
     * @return a boolean indicating whether SELinux is enforcing.
     */
    public static final native boolean isSELinuxEnforced();

    /**
     * Sets the security context for newly created file objects.
     *
     * @param context
     * 		a security context given as a String.
     * @return a boolean indicating whether the operation succeeded.
     */
    public static final native boolean setFSCreateContext(java.lang.String context);

    /**
     * Change the security context of an existing file object.
     *
     * @param path
     * 		representing the path of file object to relabel.
     * @param context
     * 		new security context given as a String.
     * @return a boolean indicating whether the operation succeeded.
     */
    public static final native boolean setFileContext(java.lang.String path, java.lang.String context);

    /**
     * Get the security context of a file object.
     *
     * @param path
     * 		the pathname of the file object.
     * @return a security context given as a String.
     */
    public static final native java.lang.String getFileContext(java.lang.String path);

    /**
     * Get the security context of a peer socket.
     *
     * @param fd
     * 		FileDescriptor class of the peer socket.
     * @return a String representing the peer socket security context.
     */
    public static final native java.lang.String getPeerContext(java.io.FileDescriptor fd);

    /**
     * Gets the security context of the current process.
     *
     * @return a String representing the security context of the current process.
     */
    public static final native java.lang.String getContext();

    /**
     * Gets the security context of a given process id.
     *
     * @param pid
     * 		an int representing the process id to check.
     * @return a String representing the security context of the given pid.
     */
    public static final native java.lang.String getPidContext(int pid);

    /**
     * Check permissions between two security contexts.
     *
     * @param scon
     * 		The source or subject security context.
     * @param tcon
     * 		The target or object security context.
     * @param tclass
     * 		The object security class name.
     * @param perm
     * 		The permission name.
     * @return a boolean indicating whether permission was granted.
     */
    public static final native boolean checkSELinuxAccess(java.lang.String scon, java.lang.String tcon, java.lang.String tclass, java.lang.String perm);

    /**
     * Restores a file to its default SELinux security context.
     * If the system is not compiled with SELinux, then {@code true}
     * is automatically returned.
     * If SELinux is compiled in, but disabled, then {@code true} is
     * returned.
     *
     * @param pathname
     * 		The pathname of the file to be relabeled.
     * @return a boolean indicating whether the relabeling succeeded.
     * @exception NullPointerException
     * 		if the pathname is a null object.
     */
    public static boolean restorecon(java.lang.String pathname) throws java.lang.NullPointerException {
        if (pathname == null) {
            throw new java.lang.NullPointerException();
        }
        return android.os.SELinux.native_restorecon(pathname, 0);
    }

    /**
     * Restores a file to its default SELinux security context.
     * If the system is not compiled with SELinux, then {@code true}
     * is automatically returned.
     * If SELinux is compiled in, but disabled, then {@code true} is
     * returned.
     *
     * @param pathname
     * 		The pathname of the file to be relabeled.
     * @return a boolean indicating whether the relabeling succeeded.
     */
    private static native boolean native_restorecon(java.lang.String pathname, int flags);

    /**
     * Restores a file to its default SELinux security context.
     * If the system is not compiled with SELinux, then {@code true}
     * is automatically returned.
     * If SELinux is compiled in, but disabled, then {@code true} is
     * returned.
     *
     * @param file
     * 		The File object representing the path to be relabeled.
     * @return a boolean indicating whether the relabeling succeeded.
     * @exception NullPointerException
     * 		if the file is a null object.
     */
    public static boolean restorecon(java.io.File file) throws java.lang.NullPointerException {
        try {
            return android.os.SELinux.native_restorecon(file.getCanonicalPath(), 0);
        } catch (java.io.IOException e) {
            android.util.Slog.e(android.os.SELinux.TAG, "Error getting canonical path. Restorecon failed for " + file.getPath(), e);
            return false;
        }
    }

    /**
     * Recursively restores all files under the given path to their default
     * SELinux security context. If the system is not compiled with SELinux,
     * then {@code true} is automatically returned. If SELinux is compiled in,
     * but disabled, then {@code true} is returned.
     *
     * @return a boolean indicating whether the relabeling succeeded.
     */
    public static boolean restoreconRecursive(java.io.File file) {
        try {
            return android.os.SELinux.native_restorecon(file.getCanonicalPath(), android.os.SELinux.SELINUX_ANDROID_RESTORECON_RECURSE);
        } catch (java.io.IOException e) {
            android.util.Slog.e(android.os.SELinux.TAG, "Error getting canonical path. Restorecon failed for " + file.getPath(), e);
            return false;
        }
    }
}

