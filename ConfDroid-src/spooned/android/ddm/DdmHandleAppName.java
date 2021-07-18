/**
 * Copyright (C) 2007 The Android Open Source Project
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
package android.ddm;


/**
 * Track our app name.  We don't (currently) handle any inbound packets.
 */
public class DdmHandleAppName extends org.apache.harmony.dalvik.ddmc.ChunkHandler {
    public static final int CHUNK_APNM = type("APNM");

    private static volatile java.lang.String mAppName = "";

    private static android.ddm.DdmHandleAppName mInstance = new android.ddm.DdmHandleAppName();

    /* singleton, do not instantiate */
    private DdmHandleAppName() {
    }

    /**
     * Register for the messages we're interested in.
     */
    public static void register() {
    }

    /**
     * Called when the DDM server connects.  The handler is allowed to
     * send messages to the server.
     */
    public void connected() {
    }

    /**
     * Called when the DDM server disconnects.  Can be used to disable
     * periodic transmissions or clean up saved state.
     */
    public void disconnected() {
    }

    /**
     * Handle a chunk of data.
     */
    public org.apache.harmony.dalvik.ddmc.Chunk handleChunk(org.apache.harmony.dalvik.ddmc.Chunk request) {
        return null;
    }

    /**
     * Set the application name.  Called when we get named, which may be
     * before or after DDMS connects.  For the latter we need to send up
     * an APNM message.
     */
    public static void setAppName(java.lang.String name, int userId) {
        if ((name == null) || (name.length() == 0))
            return;

        android.ddm.DdmHandleAppName.mAppName = name;
        // if DDMS is already connected, send the app name up
        android.ddm.DdmHandleAppName.sendAPNM(name, userId);
    }

    public static java.lang.String getAppName() {
        return android.ddm.DdmHandleAppName.mAppName;
    }

    /* Send an APNM (APplication NaMe) chunk. */
    private static void sendAPNM(java.lang.String appName, int userId) {
        if (false)
            android.util.Log.v("ddm", "Sending app name");

        java.nio.ByteBuffer out = /* userId */
        java.nio.ByteBuffer.allocate((4/* appName's length */
         + (appName.length() * 2))/* appName */
         + 4);
        out.order(ChunkHandler.CHUNK_ORDER);
        out.putInt(appName.length());
        putString(out, appName);
        out.putInt(userId);
        org.apache.harmony.dalvik.ddmc.Chunk chunk = new org.apache.harmony.dalvik.ddmc.Chunk(android.ddm.DdmHandleAppName.CHUNK_APNM, out);
        org.apache.harmony.dalvik.ddmc.DdmServer.sendChunk(chunk);
    }
}

