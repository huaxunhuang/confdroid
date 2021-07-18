/**
 * Copyright (C) 2009 The Android Open Source Project
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
 * Handle profiling requests.
 */
public class DdmHandleProfiling extends org.apache.harmony.dalvik.ddmc.ChunkHandler {
    public static final int CHUNK_MPRS = type("MPRS");

    public static final int CHUNK_MPRE = type("MPRE");

    public static final int CHUNK_MPSS = type("MPSS");

    public static final int CHUNK_MPSE = type("MPSE");

    public static final int CHUNK_MPRQ = type("MPRQ");

    public static final int CHUNK_SPSS = type("SPSS");

    public static final int CHUNK_SPSE = type("SPSE");

    private static final boolean DEBUG = false;

    private static android.ddm.DdmHandleProfiling mInstance = new android.ddm.DdmHandleProfiling();

    /* singleton, do not instantiate */
    private DdmHandleProfiling() {
    }

    /**
     * Register for the messages we're interested in.
     */
    public static void register() {
        org.apache.harmony.dalvik.ddmc.DdmServer.registerHandler(android.ddm.DdmHandleProfiling.CHUNK_MPRS, android.ddm.DdmHandleProfiling.mInstance);
        org.apache.harmony.dalvik.ddmc.DdmServer.registerHandler(android.ddm.DdmHandleProfiling.CHUNK_MPRE, android.ddm.DdmHandleProfiling.mInstance);
        org.apache.harmony.dalvik.ddmc.DdmServer.registerHandler(android.ddm.DdmHandleProfiling.CHUNK_MPSS, android.ddm.DdmHandleProfiling.mInstance);
        org.apache.harmony.dalvik.ddmc.DdmServer.registerHandler(android.ddm.DdmHandleProfiling.CHUNK_MPSE, android.ddm.DdmHandleProfiling.mInstance);
        org.apache.harmony.dalvik.ddmc.DdmServer.registerHandler(android.ddm.DdmHandleProfiling.CHUNK_MPRQ, android.ddm.DdmHandleProfiling.mInstance);
        org.apache.harmony.dalvik.ddmc.DdmServer.registerHandler(android.ddm.DdmHandleProfiling.CHUNK_SPSS, android.ddm.DdmHandleProfiling.mInstance);
        org.apache.harmony.dalvik.ddmc.DdmServer.registerHandler(android.ddm.DdmHandleProfiling.CHUNK_SPSE, android.ddm.DdmHandleProfiling.mInstance);
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
        if (android.ddm.DdmHandleProfiling.DEBUG)
            android.util.Log.v("ddm-heap", ("Handling " + name(request.type)) + " chunk");

        int type = request.type;
        if (type == android.ddm.DdmHandleProfiling.CHUNK_MPRS) {
            return handleMPRS(request);
        } else
            if (type == android.ddm.DdmHandleProfiling.CHUNK_MPRE) {
                return handleMPRE(request);
            } else
                if (type == android.ddm.DdmHandleProfiling.CHUNK_MPSS) {
                    return handleMPSS(request);
                } else
                    if (type == android.ddm.DdmHandleProfiling.CHUNK_MPSE) {
                        return handleMPSEOrSPSE(request, "Method");
                    } else
                        if (type == android.ddm.DdmHandleProfiling.CHUNK_MPRQ) {
                            return handleMPRQ(request);
                        } else
                            if (type == android.ddm.DdmHandleProfiling.CHUNK_SPSS) {
                                return handleSPSS(request);
                            } else
                                if (type == android.ddm.DdmHandleProfiling.CHUNK_SPSE) {
                                    return handleMPSEOrSPSE(request, "Sample");
                                } else {
                                    throw new java.lang.RuntimeException("Unknown packet " + org.apache.harmony.dalvik.ddmc.ChunkHandler.name(type));
                                }






    }

    /* Handle a "Method PRofiling Start" request. */
    private org.apache.harmony.dalvik.ddmc.Chunk handleMPRS(org.apache.harmony.dalvik.ddmc.Chunk request) {
        java.nio.ByteBuffer in = wrapChunk(request);
        int bufferSize = in.getInt();
        int flags = in.getInt();
        int len = in.getInt();
        java.lang.String fileName = getString(in, len);
        if (android.ddm.DdmHandleProfiling.DEBUG)
            android.util.Log.v("ddm-heap", (((("Method profiling start: filename='" + fileName) + "', size=") + bufferSize) + ", flags=") + flags);

        try {
            android.os.Debug.startMethodTracing(fileName, bufferSize, flags);
            return null;// empty response

        } catch (java.lang.RuntimeException re) {
            return createFailChunk(1, re.getMessage());
        }
    }

    /* Handle a "Method PRofiling End" request. */
    private org.apache.harmony.dalvik.ddmc.Chunk handleMPRE(org.apache.harmony.dalvik.ddmc.Chunk request) {
        byte result;
        try {
            android.os.Debug.stopMethodTracing();
            result = 0;
        } catch (java.lang.RuntimeException re) {
            android.util.Log.w("ddm-heap", "Method profiling end failed: " + re.getMessage());
            result = 1;
        }
        /* create a non-empty reply so the handler fires on completion */
        byte[] reply = new byte[]{ result };
        return new org.apache.harmony.dalvik.ddmc.Chunk(android.ddm.DdmHandleProfiling.CHUNK_MPRE, reply, 0, reply.length);
    }

    /* Handle a "Method Profiling w/Streaming Start" request. */
    private org.apache.harmony.dalvik.ddmc.Chunk handleMPSS(org.apache.harmony.dalvik.ddmc.Chunk request) {
        java.nio.ByteBuffer in = wrapChunk(request);
        int bufferSize = in.getInt();
        int flags = in.getInt();
        if (android.ddm.DdmHandleProfiling.DEBUG) {
            android.util.Log.v("ddm-heap", (("Method prof stream start: size=" + bufferSize) + ", flags=") + flags);
        }
        try {
            android.os.Debug.startMethodTracingDdms(bufferSize, flags, false, 0);
            return null;// empty response

        } catch (java.lang.RuntimeException re) {
            return createFailChunk(1, re.getMessage());
        }
    }

    /* Handle a "Method Profiling w/Streaming End" request or a
    "Sample Profiling w/Streaming End" request.
     */
    private org.apache.harmony.dalvik.ddmc.Chunk handleMPSEOrSPSE(org.apache.harmony.dalvik.ddmc.Chunk request, java.lang.String type) {
        if (android.ddm.DdmHandleProfiling.DEBUG) {
            android.util.Log.v("ddm-heap", type + " prof stream end");
        }
        try {
            android.os.Debug.stopMethodTracing();
        } catch (java.lang.RuntimeException re) {
            android.util.Log.w("ddm-heap", (type + " prof stream end failed: ") + re.getMessage());
            return createFailChunk(1, re.getMessage());
        }
        /* VM sent the (perhaps very large) response directly */
        return null;
    }

    /* Handle a "Method PRofiling Query" request. */
    private org.apache.harmony.dalvik.ddmc.Chunk handleMPRQ(org.apache.harmony.dalvik.ddmc.Chunk request) {
        int result = android.os.Debug.getMethodTracingMode();
        /* create a non-empty reply so the handler fires on completion */
        byte[] reply = new byte[]{ ((byte) (result)) };
        return new org.apache.harmony.dalvik.ddmc.Chunk(android.ddm.DdmHandleProfiling.CHUNK_MPRQ, reply, 0, reply.length);
    }

    /* Handle a "Sample Profiling w/Streaming Start" request. */
    private org.apache.harmony.dalvik.ddmc.Chunk handleSPSS(org.apache.harmony.dalvik.ddmc.Chunk request) {
        java.nio.ByteBuffer in = wrapChunk(request);
        int bufferSize = in.getInt();
        int flags = in.getInt();
        int interval = in.getInt();
        if (android.ddm.DdmHandleProfiling.DEBUG) {
            android.util.Log.v("ddm-heap", (((("Sample prof stream start: size=" + bufferSize) + ", flags=") + flags) + ", interval=") + interval);
        }
        try {
            android.os.Debug.startMethodTracingDdms(bufferSize, flags, true, interval);
            return null;// empty response

        } catch (java.lang.RuntimeException re) {
            return createFailChunk(1, re.getMessage());
        }
    }
}

