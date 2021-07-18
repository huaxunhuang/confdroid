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
 * Handle "hello" messages and feature discovery.
 */
public class DdmHandleHello extends org.apache.harmony.dalvik.ddmc.ChunkHandler {
    public static final int CHUNK_HELO = type("HELO");

    public static final int CHUNK_WAIT = type("WAIT");

    public static final int CHUNK_FEAT = type("FEAT");

    private static android.ddm.DdmHandleHello mInstance = new android.ddm.DdmHandleHello();

    private static final java.lang.String[] FRAMEWORK_FEATURES = new java.lang.String[]{ "opengl-tracing", "view-hierarchy" };

    /* singleton, do not instantiate */
    private DdmHandleHello() {
    }

    /**
     * Register for the messages we're interested in.
     */
    public static void register() {
        org.apache.harmony.dalvik.ddmc.DdmServer.registerHandler(android.ddm.DdmHandleHello.CHUNK_HELO, android.ddm.DdmHandleHello.mInstance);
        org.apache.harmony.dalvik.ddmc.DdmServer.registerHandler(android.ddm.DdmHandleHello.CHUNK_FEAT, android.ddm.DdmHandleHello.mInstance);
    }

    /**
     * Called when the DDM server connects.  The handler is allowed to
     * send messages to the server.
     */
    public void connected() {
        if (false)
            android.util.Log.v("ddm-hello", "Connected!");

        if (false) {
            /* test spontaneous transmission */
            byte[] data = new byte[]{ 0, 1, 2, 3, 4, -4, -3, -2, -1, 127 };
            org.apache.harmony.dalvik.ddmc.Chunk testChunk = new org.apache.harmony.dalvik.ddmc.Chunk(org.apache.harmony.dalvik.ddmc.ChunkHandler.type("TEST"), data, 1, data.length - 2);
            org.apache.harmony.dalvik.ddmc.DdmServer.sendChunk(testChunk);
        }
    }

    /**
     * Called when the DDM server disconnects.  Can be used to disable
     * periodic transmissions or clean up saved state.
     */
    public void disconnected() {
        if (false)
            android.util.Log.v("ddm-hello", "Disconnected!");

    }

    /**
     * Handle a chunk of data.
     */
    public org.apache.harmony.dalvik.ddmc.Chunk handleChunk(org.apache.harmony.dalvik.ddmc.Chunk request) {
        if (false)
            android.util.Log.v("ddm-heap", ("Handling " + name(request.type)) + " chunk");

        int type = request.type;
        if (type == android.ddm.DdmHandleHello.CHUNK_HELO) {
            return handleHELO(request);
        } else
            if (type == android.ddm.DdmHandleHello.CHUNK_FEAT) {
                return handleFEAT(request);
            } else {
                throw new java.lang.RuntimeException("Unknown packet " + org.apache.harmony.dalvik.ddmc.ChunkHandler.name(type));
            }

    }

    /* Handle introductory packet. This is called during JNI_CreateJavaVM
    before frameworks native methods are registered, so be careful not
    to call any APIs that depend on frameworks native code.
     */
    private org.apache.harmony.dalvik.ddmc.Chunk handleHELO(org.apache.harmony.dalvik.ddmc.Chunk request) {
        if (false)
            return createFailChunk(123, "This is a test");

        /* Process the request. */
        java.nio.ByteBuffer in = wrapChunk(request);
        int serverProtoVers = in.getInt();
        if (false)
            android.util.Log.v("ddm-hello", "Server version is " + serverProtoVers);

        /* Create a response. */
        java.lang.String vmName = java.lang.System.getProperty("java.vm.name", "?");
        java.lang.String vmVersion = java.lang.System.getProperty("java.vm.version", "?");
        java.lang.String vmIdent = (vmName + " v") + vmVersion;
        // String appName = android.app.ActivityThread.currentPackageName();
        // if (appName == null)
        // appName = "unknown";
        java.lang.String appName = android.ddm.DdmHandleAppName.getAppName();
        dalvik.system.VMRuntime vmRuntime = dalvik.system.VMRuntime.getRuntime();
        java.lang.String instructionSetDescription = (vmRuntime.is64Bit()) ? "64-bit" : "32-bit";
        java.lang.String vmInstructionSet = vmRuntime.vmInstructionSet();
        if ((vmInstructionSet != null) && (vmInstructionSet.length() > 0)) {
            instructionSetDescription += (" (" + vmInstructionSet) + ")";
        }
        java.lang.String vmFlags = "CheckJNI=" + (vmRuntime.isCheckJniEnabled() ? "true" : "false");
        boolean isNativeDebuggable = vmRuntime.isNativeDebuggable();
        java.nio.ByteBuffer out = java.nio.ByteBuffer.allocate(((((28 + (vmIdent.length() * 2)) + (appName.length() * 2)) + (instructionSetDescription.length() * 2)) + (vmFlags.length() * 2)) + 1);
        out.order(ChunkHandler.CHUNK_ORDER);
        out.putInt(DdmServer.CLIENT_PROTOCOL_VERSION);
        out.putInt(android.os.Process.myPid());
        out.putInt(vmIdent.length());
        out.putInt(appName.length());
        putString(out, vmIdent);
        putString(out, appName);
        out.putInt(android.os.UserHandle.myUserId());
        out.putInt(instructionSetDescription.length());
        putString(out, instructionSetDescription);
        out.putInt(vmFlags.length());
        putString(out, vmFlags);
        out.put(((byte) (isNativeDebuggable ? 1 : 0)));
        org.apache.harmony.dalvik.ddmc.Chunk reply = new org.apache.harmony.dalvik.ddmc.Chunk(android.ddm.DdmHandleHello.CHUNK_HELO, out);
        /* Take the opportunity to inform DDMS if we are waiting for a
        debugger to attach.
         */
        if (android.os.Debug.waitingForDebugger())
            android.ddm.DdmHandleHello.sendWAIT(0);

        return reply;
    }

    /* Handle request for list of supported features. */
    private org.apache.harmony.dalvik.ddmc.Chunk handleFEAT(org.apache.harmony.dalvik.ddmc.Chunk request) {
        // TODO: query the VM to ensure that support for these features
        // is actually compiled in
        final java.lang.String[] vmFeatures = android.os.Debug.getVmFeatureList();
        if (false)
            android.util.Log.v("ddm-heap", "Got feature list request");

        int size = 4 + (4 * (vmFeatures.length + android.ddm.DdmHandleHello.FRAMEWORK_FEATURES.length));
        for (int i = vmFeatures.length - 1; i >= 0; i--)
            size += vmFeatures[i].length() * 2;

        for (int i = android.ddm.DdmHandleHello.FRAMEWORK_FEATURES.length - 1; i >= 0; i--)
            size += android.ddm.DdmHandleHello.FRAMEWORK_FEATURES[i].length() * 2;

        java.nio.ByteBuffer out = java.nio.ByteBuffer.allocate(size);
        out.order(ChunkHandler.CHUNK_ORDER);
        out.putInt(vmFeatures.length + android.ddm.DdmHandleHello.FRAMEWORK_FEATURES.length);
        for (int i = vmFeatures.length - 1; i >= 0; i--) {
            out.putInt(vmFeatures[i].length());
            putString(out, vmFeatures[i]);
        }
        for (int i = android.ddm.DdmHandleHello.FRAMEWORK_FEATURES.length - 1; i >= 0; i--) {
            out.putInt(android.ddm.DdmHandleHello.FRAMEWORK_FEATURES[i].length());
            putString(out, android.ddm.DdmHandleHello.FRAMEWORK_FEATURES[i]);
        }
        return new org.apache.harmony.dalvik.ddmc.Chunk(android.ddm.DdmHandleHello.CHUNK_FEAT, out);
    }

    /**
     * Send up a WAIT chunk.  The only currently defined value for "reason"
     * is zero, which means "waiting for a debugger".
     */
    public static void sendWAIT(int reason) {
        byte[] data = new byte[]{ ((byte) (reason)) };
        org.apache.harmony.dalvik.ddmc.Chunk waitChunk = new org.apache.harmony.dalvik.ddmc.Chunk(android.ddm.DdmHandleHello.CHUNK_WAIT, data, 0, 1);
        org.apache.harmony.dalvik.ddmc.DdmServer.sendChunk(waitChunk);
    }
}

