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
public class DdmHandleThread extends org.apache.harmony.dalvik.ddmc.ChunkHandler {
    public static final int CHUNK_THEN = type("THEN");

    public static final int CHUNK_THCR = type("THCR");

    public static final int CHUNK_THDE = type("THDE");

    public static final int CHUNK_THST = type("THST");

    public static final int CHUNK_STKL = type("STKL");

    private static android.ddm.DdmHandleThread mInstance = new android.ddm.DdmHandleThread();

    /* singleton, do not instantiate */
    private DdmHandleThread() {
    }

    /**
     * Register for the messages we're interested in.
     */
    public static void register() {
        org.apache.harmony.dalvik.ddmc.DdmServer.registerHandler(android.ddm.DdmHandleThread.CHUNK_THEN, android.ddm.DdmHandleThread.mInstance);
        org.apache.harmony.dalvik.ddmc.DdmServer.registerHandler(android.ddm.DdmHandleThread.CHUNK_THST, android.ddm.DdmHandleThread.mInstance);
        org.apache.harmony.dalvik.ddmc.DdmServer.registerHandler(android.ddm.DdmHandleThread.CHUNK_STKL, android.ddm.DdmHandleThread.mInstance);
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
        if (false)
            android.util.Log.v("ddm-thread", ("Handling " + name(request.type)) + " chunk");

        int type = request.type;
        if (type == android.ddm.DdmHandleThread.CHUNK_THEN) {
            return handleTHEN(request);
        } else
            if (type == android.ddm.DdmHandleThread.CHUNK_THST) {
                return handleTHST(request);
            } else
                if (type == android.ddm.DdmHandleThread.CHUNK_STKL) {
                    return handleSTKL(request);
                } else {
                    throw new java.lang.RuntimeException("Unknown packet " + org.apache.harmony.dalvik.ddmc.ChunkHandler.name(type));
                }


    }

    /* Handle a "THread notification ENable" request. */
    private org.apache.harmony.dalvik.ddmc.Chunk handleTHEN(org.apache.harmony.dalvik.ddmc.Chunk request) {
        java.nio.ByteBuffer in = wrapChunk(request);
        boolean enable = in.get() != 0;
        // Log.i("ddm-thread", "Thread notify enable: " + enable);
        org.apache.harmony.dalvik.ddmc.DdmVmInternal.threadNotify(enable);
        return null;// empty response

    }

    /* Handle a "THread STatus" request.  This is constructed by the VM. */
    private org.apache.harmony.dalvik.ddmc.Chunk handleTHST(org.apache.harmony.dalvik.ddmc.Chunk request) {
        java.nio.ByteBuffer in = wrapChunk(request);
        // currently nothing to read from "in"
        // Log.d("ddm-thread", "Thread status request");
        byte[] status = org.apache.harmony.dalvik.ddmc.DdmVmInternal.getThreadStats();
        if (status != null)
            return new org.apache.harmony.dalvik.ddmc.Chunk(android.ddm.DdmHandleThread.CHUNK_THST, status, 0, status.length);
        else
            return createFailChunk(1, "Can't build THST chunk");

    }

    /* Handle a STacK List request.

    This is done by threadId, which isn't great since those are
    recycled.  We need a thread serial ID.  The Linux tid is an okay
    answer as it's unlikely to recycle at the exact wrong moment.
    However, we're using the short threadId in THST messages, so we
    use them here for consistency.  (One thought is to keep the current
    thread ID in the low 16 bits and somehow serialize the top 16 bits.)
     */
    private org.apache.harmony.dalvik.ddmc.Chunk handleSTKL(org.apache.harmony.dalvik.ddmc.Chunk request) {
        java.nio.ByteBuffer in = wrapChunk(request);
        int threadId;
        threadId = in.getInt();
        // Log.d("ddm-thread", "Stack list request " + threadId);
        java.lang.StackTraceElement[] trace = org.apache.harmony.dalvik.ddmc.DdmVmInternal.getStackTraceById(threadId);
        if (trace == null) {
            return createFailChunk(1, "Stack trace unavailable");
        } else {
            return createStackChunk(trace, threadId);
        }
    }

    /* Serialize a StackTraceElement[] into an STKL chunk.

    We include the threadId in the response so the other side doesn't have
    to match up requests and responses as carefully.
     */
    private org.apache.harmony.dalvik.ddmc.Chunk createStackChunk(java.lang.StackTraceElement[] trace, int threadId) {
        int bufferSize = 0;
        bufferSize += 4;
        // version, flags, whatever
        bufferSize += 4;
        // thread ID
        bufferSize += 4;
        // frame count
        for (java.lang.StackTraceElement elem : trace) {
            bufferSize += 4 + (elem.getClassName().length() * 2);
            bufferSize += 4 + (elem.getMethodName().length() * 2);
            bufferSize += 4;
            if (elem.getFileName() != null)
                bufferSize += elem.getFileName().length() * 2;

            bufferSize += 4;// line number

        }
        java.nio.ByteBuffer out = java.nio.ByteBuffer.allocate(bufferSize);
        out.putInt(0);
        out.putInt(threadId);
        out.putInt(trace.length);
        for (java.lang.StackTraceElement elem : trace) {
            out.putInt(elem.getClassName().length());
            putString(out, elem.getClassName());
            out.putInt(elem.getMethodName().length());
            putString(out, elem.getMethodName());
            if (elem.getFileName() != null) {
                out.putInt(elem.getFileName().length());
                putString(out, elem.getFileName());
            } else {
                out.putInt(0);
            }
            out.putInt(elem.getLineNumber());
        }
        return new org.apache.harmony.dalvik.ddmc.Chunk(android.ddm.DdmHandleThread.CHUNK_STKL, out);
    }
}

