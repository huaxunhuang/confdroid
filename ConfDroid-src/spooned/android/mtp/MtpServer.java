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
package android.mtp;


/**
 * Java wrapper for MTP/PTP support as USB responder.
 * {@hide }
 */
public class MtpServer implements java.lang.Runnable {
    private long mNativeContext;// accessed by native methods


    private final android.mtp.MtpDatabase mDatabase;

    static {
        java.lang.System.loadLibrary("media_jni");
    }

    public MtpServer(android.mtp.MtpDatabase database, boolean usePtp) {
        mDatabase = database;
        native_setup(database, usePtp);
        database.setServer(this);
    }

    public void start() {
        java.lang.Thread thread = new java.lang.Thread(this, "MtpServer");
        thread.start();
    }

    @java.lang.Override
    public void run() {
        native_run();
        native_cleanup();
        mDatabase.close();
    }

    public void sendObjectAdded(int handle) {
        native_send_object_added(handle);
    }

    public void sendObjectRemoved(int handle) {
        native_send_object_removed(handle);
    }

    public void sendDevicePropertyChanged(int property) {
        native_send_device_property_changed(property);
    }

    public void addStorage(android.mtp.MtpStorage storage) {
        native_add_storage(storage);
    }

    public void removeStorage(android.mtp.MtpStorage storage) {
        native_remove_storage(storage.getStorageId());
    }

    private final native void native_setup(android.mtp.MtpDatabase database, boolean usePtp);

    private final native void native_run();

    private final native void native_cleanup();

    private final native void native_send_object_added(int handle);

    private final native void native_send_object_removed(int handle);

    private final native void native_send_device_property_changed(int property);

    private final native void native_add_storage(android.mtp.MtpStorage storage);

    private final native void native_remove_storage(int storageId);
}

