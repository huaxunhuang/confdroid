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
 * Handle an EXIT chunk.
 */
public class DdmHandleExit extends org.apache.harmony.dalvik.ddmc.ChunkHandler {
    public static final int CHUNK_EXIT = type("EXIT");

    private static android.ddm.DdmHandleExit mInstance = new android.ddm.DdmHandleExit();

    /* singleton, do not instantiate */
    private DdmHandleExit() {
    }

    /**
     * Register for the messages we're interested in.
     */
    public static void register() {
        org.apache.harmony.dalvik.ddmc.DdmServer.registerHandler(android.ddm.DdmHandleExit.CHUNK_EXIT, android.ddm.DdmHandleExit.mInstance);
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
     * Handle a chunk of data.  We're only registered for "EXIT".
     */
    public org.apache.harmony.dalvik.ddmc.Chunk handleChunk(org.apache.harmony.dalvik.ddmc.Chunk request) {
        if (false)
            android.util.Log.v("ddm-exit", ("Handling " + name(request.type)) + " chunk");

        /* Process the request. */
        java.nio.ByteBuffer in = wrapChunk(request);
        int statusCode = in.getInt();
        java.lang.Runtime.getRuntime().halt(statusCode);
        // if that doesn't work, return an empty message
        return null;
    }
}

