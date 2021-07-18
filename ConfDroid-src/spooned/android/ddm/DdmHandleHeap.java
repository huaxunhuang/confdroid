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
 * Handle native and virtual heap requests.
 */
public class DdmHandleHeap extends org.apache.harmony.dalvik.ddmc.ChunkHandler {
    public static final int CHUNK_HPIF = type("HPIF");

    public static final int CHUNK_HPSG = type("HPSG");

    public static final int CHUNK_HPDU = type("HPDU");

    public static final int CHUNK_HPDS = type("HPDS");

    public static final int CHUNK_NHSG = type("NHSG");

    public static final int CHUNK_HPGC = type("HPGC");

    public static final int CHUNK_REAE = type("REAE");

    public static final int CHUNK_REAQ = type("REAQ");

    public static final int CHUNK_REAL = type("REAL");

    private static android.ddm.DdmHandleHeap mInstance = new android.ddm.DdmHandleHeap();

    /* singleton, do not instantiate */
    private DdmHandleHeap() {
    }

    /**
     * Register for the messages we're interested in.
     */
    public static void register() {
        org.apache.harmony.dalvik.ddmc.DdmServer.registerHandler(android.ddm.DdmHandleHeap.CHUNK_HPIF, android.ddm.DdmHandleHeap.mInstance);
        org.apache.harmony.dalvik.ddmc.DdmServer.registerHandler(android.ddm.DdmHandleHeap.CHUNK_HPSG, android.ddm.DdmHandleHeap.mInstance);
        org.apache.harmony.dalvik.ddmc.DdmServer.registerHandler(android.ddm.DdmHandleHeap.CHUNK_HPDU, android.ddm.DdmHandleHeap.mInstance);
        org.apache.harmony.dalvik.ddmc.DdmServer.registerHandler(android.ddm.DdmHandleHeap.CHUNK_HPDS, android.ddm.DdmHandleHeap.mInstance);
        org.apache.harmony.dalvik.ddmc.DdmServer.registerHandler(android.ddm.DdmHandleHeap.CHUNK_NHSG, android.ddm.DdmHandleHeap.mInstance);
        org.apache.harmony.dalvik.ddmc.DdmServer.registerHandler(android.ddm.DdmHandleHeap.CHUNK_HPGC, android.ddm.DdmHandleHeap.mInstance);
        org.apache.harmony.dalvik.ddmc.DdmServer.registerHandler(android.ddm.DdmHandleHeap.CHUNK_REAE, android.ddm.DdmHandleHeap.mInstance);
        org.apache.harmony.dalvik.ddmc.DdmServer.registerHandler(android.ddm.DdmHandleHeap.CHUNK_REAQ, android.ddm.DdmHandleHeap.mInstance);
        org.apache.harmony.dalvik.ddmc.DdmServer.registerHandler(android.ddm.DdmHandleHeap.CHUNK_REAL, android.ddm.DdmHandleHeap.mInstance);
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
            android.util.Log.v("ddm-heap", ("Handling " + name(request.type)) + " chunk");

        int type = request.type;
        if (type == android.ddm.DdmHandleHeap.CHUNK_HPIF) {
            return handleHPIF(request);
        } else
            if (type == android.ddm.DdmHandleHeap.CHUNK_HPSG) {
                return handleHPSGNHSG(request, false);
            } else
                if (type == android.ddm.DdmHandleHeap.CHUNK_HPDU) {
                    return handleHPDU(request);
                } else
                    if (type == android.ddm.DdmHandleHeap.CHUNK_HPDS) {
                        return handleHPDS(request);
                    } else
                        if (type == android.ddm.DdmHandleHeap.CHUNK_NHSG) {
                            return handleHPSGNHSG(request, true);
                        } else
                            if (type == android.ddm.DdmHandleHeap.CHUNK_HPGC) {
                                return handleHPGC(request);
                            } else
                                if (type == android.ddm.DdmHandleHeap.CHUNK_REAE) {
                                    return handleREAE(request);
                                } else
                                    if (type == android.ddm.DdmHandleHeap.CHUNK_REAQ) {
                                        return handleREAQ(request);
                                    } else
                                        if (type == android.ddm.DdmHandleHeap.CHUNK_REAL) {
                                            return handleREAL(request);
                                        } else {
                                            throw new java.lang.RuntimeException("Unknown packet " + org.apache.harmony.dalvik.ddmc.ChunkHandler.name(type));
                                        }








    }

    /* Handle a "HeaP InFo" request. */
    private org.apache.harmony.dalvik.ddmc.Chunk handleHPIF(org.apache.harmony.dalvik.ddmc.Chunk request) {
        java.nio.ByteBuffer in = wrapChunk(request);
        int when = in.get();
        if (false)
            android.util.Log.v("ddm-heap", "Heap segment enable: when=" + when);

        boolean ok = org.apache.harmony.dalvik.ddmc.DdmVmInternal.heapInfoNotify(when);
        if (!ok) {
            return createFailChunk(1, "Unsupported HPIF what");
        } else {
            return null;// empty response

        }
    }

    /* Handle a "HeaP SeGment" or "Native Heap SeGment" request. */
    private org.apache.harmony.dalvik.ddmc.Chunk handleHPSGNHSG(org.apache.harmony.dalvik.ddmc.Chunk request, boolean isNative) {
        java.nio.ByteBuffer in = wrapChunk(request);
        int when = in.get();
        int what = in.get();
        if (false)
            android.util.Log.v("ddm-heap", (((("Heap segment enable: when=" + when) + ", what=") + what) + ", isNative=") + isNative);

        boolean ok = org.apache.harmony.dalvik.ddmc.DdmVmInternal.heapSegmentNotify(when, what, isNative);
        if (!ok) {
            return createFailChunk(1, "Unsupported HPSG what/when");
        } else {
            // TODO: if "when" is non-zero and we want to see a dump
            // right away, initiate a GC.
            return null;// empty response

        }
    }

    /* Handle a "HeaP DUmp" request.

    This currently just returns a result code.  We could pull up
    the entire contents of the file and return them, but hprof dump
    files can be a few megabytes.
     */
    private org.apache.harmony.dalvik.ddmc.Chunk handleHPDU(org.apache.harmony.dalvik.ddmc.Chunk request) {
        java.nio.ByteBuffer in = wrapChunk(request);
        byte result;
        /* get the filename for the output file */
        int len = in.getInt();
        java.lang.String fileName = getString(in, len);
        if (false)
            android.util.Log.d("ddm-heap", ("Heap dump: file='" + fileName) + "'");

        try {
            android.os.Debug.dumpHprofData(fileName);
            result = 0;
        } catch (java.lang.UnsupportedOperationException uoe) {
            android.util.Log.w("ddm-heap", "hprof dumps not supported in this VM");
            result = -1;
        } catch (java.io.IOException ioe) {
            result = -1;
        } catch (java.lang.RuntimeException re) {
            result = -1;
        }
        /* create a non-empty reply so the handler fires on completion */
        byte[] reply = new byte[]{ result };
        return new org.apache.harmony.dalvik.ddmc.Chunk(android.ddm.DdmHandleHeap.CHUNK_HPDU, reply, 0, reply.length);
    }

    /* Handle a "HeaP Dump Streaming" request.

    This tells the VM to create a heap dump and send it directly to
    DDMS.  The dumps are large enough that we don't want to copy the
    data into a byte[] and send it from here.
     */
    private org.apache.harmony.dalvik.ddmc.Chunk handleHPDS(org.apache.harmony.dalvik.ddmc.Chunk request) {
        java.nio.ByteBuffer in = wrapChunk(request);
        byte result;
        /* get the filename for the output file */
        if (false)
            android.util.Log.d("ddm-heap", "Heap dump: [DDMS]");

        java.lang.String failMsg = null;
        try {
            android.os.Debug.dumpHprofDataDdms();
        } catch (java.lang.UnsupportedOperationException uoe) {
            failMsg = "hprof dumps not supported in this VM";
        } catch (java.lang.RuntimeException re) {
            failMsg = "Exception: " + re.getMessage();
        }
        if (failMsg != null) {
            android.util.Log.w("ddm-heap", failMsg);
            return createFailChunk(1, failMsg);
        } else {
            return null;
        }
    }

    /* Handle a "HeaP Garbage Collection" request. */
    private org.apache.harmony.dalvik.ddmc.Chunk handleHPGC(org.apache.harmony.dalvik.ddmc.Chunk request) {
        // ByteBuffer in = wrapChunk(request);
        if (false)
            android.util.Log.d("ddm-heap", "Heap GC request");

        java.lang.Runtime.getRuntime().gc();
        return null;// empty response

    }

    /* Handle a "REcent Allocation Enable" request. */
    private org.apache.harmony.dalvik.ddmc.Chunk handleREAE(org.apache.harmony.dalvik.ddmc.Chunk request) {
        java.nio.ByteBuffer in = wrapChunk(request);
        boolean enable;
        enable = in.get() != 0;
        if (false)
            android.util.Log.d("ddm-heap", "Recent allocation enable request: " + enable);

        org.apache.harmony.dalvik.ddmc.DdmVmInternal.enableRecentAllocations(enable);
        return null;// empty response

    }

    /* Handle a "REcent Allocation Query" request. */
    private org.apache.harmony.dalvik.ddmc.Chunk handleREAQ(org.apache.harmony.dalvik.ddmc.Chunk request) {
        // ByteBuffer in = wrapChunk(request);
        byte[] reply = new byte[1];
        reply[0] = (org.apache.harmony.dalvik.ddmc.DdmVmInternal.getRecentAllocationStatus()) ? ((byte) (1)) : ((byte) (0));
        return new org.apache.harmony.dalvik.ddmc.Chunk(android.ddm.DdmHandleHeap.CHUNK_REAQ, reply, 0, reply.length);
    }

    /* Handle a "REcent ALlocations" request. */
    private org.apache.harmony.dalvik.ddmc.Chunk handleREAL(org.apache.harmony.dalvik.ddmc.Chunk request) {
        // ByteBuffer in = wrapChunk(request);
        if (false)
            android.util.Log.d("ddm-heap", "Recent allocations request");

        /* generate the reply in a ready-to-go format */
        byte[] reply = org.apache.harmony.dalvik.ddmc.DdmVmInternal.getRecentAllocations();
        return new org.apache.harmony.dalvik.ddmc.Chunk(android.ddm.DdmHandleHeap.CHUNK_REAL, reply, 0, reply.length);
    }
}

