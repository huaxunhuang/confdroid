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
 * Handle thread-related traffic.
 */
public class DdmHandleNativeHeap extends org.apache.harmony.dalvik.ddmc.ChunkHandler {
    public static final int CHUNK_NHGT = type("NHGT");

    private static android.ddm.DdmHandleNativeHeap mInstance = new android.ddm.DdmHandleNativeHeap();

    /* singleton, do not instantiate */
    private DdmHandleNativeHeap() {
    }

    /**
     * Register for the messages we're interested in.
     */
    public static void register() {
        org.apache.harmony.dalvik.ddmc.DdmServer.registerHandler(android.ddm.DdmHandleNativeHeap.CHUNK_NHGT, android.ddm.DdmHandleNativeHeap.mInstance);
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
        android.util.Log.i("ddm-nativeheap", ("Handling " + name(request.type)) + " chunk");
        int type = request.type;
        if (type == android.ddm.DdmHandleNativeHeap.CHUNK_NHGT) {
            return handleNHGT(request);
        } else {
            throw new java.lang.RuntimeException("Unknown packet " + org.apache.harmony.dalvik.ddmc.ChunkHandler.name(type));
        }
    }

    /* Handle a "Native Heap GeT" request. */
    private org.apache.harmony.dalvik.ddmc.Chunk handleNHGT(org.apache.harmony.dalvik.ddmc.Chunk request) {
        // ByteBuffer in = wrapChunk(request);
        byte[] data = getLeakInfo();
        if (data != null) {
            // wrap & return
            android.util.Log.i("ddm-nativeheap", ("Sending " + data.length) + " bytes");
            return new org.apache.harmony.dalvik.ddmc.Chunk(org.apache.harmony.dalvik.ddmc.ChunkHandler.type("NHGT"), data, 0, data.length);
        } else {
            // failed, return a failure error code and message
            return createFailChunk(1, "Something went wrong");
        }
    }

    private native byte[] getLeakInfo();
}

