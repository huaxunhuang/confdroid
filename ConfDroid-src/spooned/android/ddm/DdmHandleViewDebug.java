/**
 * Copyright (C) 2013 The Android Open Source Project
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
 * Handle various requests related to profiling / debugging of the view system.
 * Support for these features are advertised via {@link DdmHandleHello}.
 */
public class DdmHandleViewDebug extends org.apache.harmony.dalvik.ddmc.ChunkHandler {
    /**
     * List {@link ViewRootImpl}'s of this process.
     */
    private static final int CHUNK_VULW = type("VULW");

    /**
     * Operation on view root, first parameter in packet should be one of VURT_* constants
     */
    private static final int CHUNK_VURT = type("VURT");

    /**
     * Dump view hierarchy.
     */
    private static final int VURT_DUMP_HIERARCHY = 1;

    /**
     * Capture View Layers.
     */
    private static final int VURT_CAPTURE_LAYERS = 2;

    /**
     * Dump View Theme.
     */
    private static final int VURT_DUMP_THEME = 3;

    /**
     * Generic View Operation, first parameter in the packet should be one of the
     * VUOP_* constants below.
     */
    private static final int CHUNK_VUOP = type("VUOP");

    /**
     * Capture View.
     */
    private static final int VUOP_CAPTURE_VIEW = 1;

    /**
     * Obtain the Display List corresponding to the view.
     */
    private static final int VUOP_DUMP_DISPLAYLIST = 2;

    /**
     * Profile a view.
     */
    private static final int VUOP_PROFILE_VIEW = 3;

    /**
     * Invoke a method on the view.
     */
    private static final int VUOP_INVOKE_VIEW_METHOD = 4;

    /**
     * Set layout parameter.
     */
    private static final int VUOP_SET_LAYOUT_PARAMETER = 5;

    /**
     * Error code indicating operation specified in chunk is invalid.
     */
    private static final int ERR_INVALID_OP = -1;

    /**
     * Error code indicating that the parameters are invalid.
     */
    private static final int ERR_INVALID_PARAM = -2;

    /**
     * Error code indicating an exception while performing operation.
     */
    private static final int ERR_EXCEPTION = -3;

    private static final java.lang.String TAG = "DdmViewDebug";

    private static final android.ddm.DdmHandleViewDebug sInstance = new android.ddm.DdmHandleViewDebug();

    /**
     * singleton, do not instantiate.
     */
    private DdmHandleViewDebug() {
    }

    public static void register() {
        org.apache.harmony.dalvik.ddmc.DdmServer.registerHandler(android.ddm.DdmHandleViewDebug.CHUNK_VULW, android.ddm.DdmHandleViewDebug.sInstance);
        org.apache.harmony.dalvik.ddmc.DdmServer.registerHandler(android.ddm.DdmHandleViewDebug.CHUNK_VURT, android.ddm.DdmHandleViewDebug.sInstance);
        org.apache.harmony.dalvik.ddmc.DdmServer.registerHandler(android.ddm.DdmHandleViewDebug.CHUNK_VUOP, android.ddm.DdmHandleViewDebug.sInstance);
    }

    @java.lang.Override
    public void connected() {
    }

    @java.lang.Override
    public void disconnected() {
    }

    @java.lang.Override
    public org.apache.harmony.dalvik.ddmc.Chunk handleChunk(org.apache.harmony.dalvik.ddmc.Chunk request) {
        int type = request.type;
        if (type == android.ddm.DdmHandleViewDebug.CHUNK_VULW) {
            return listWindows();
        }
        java.nio.ByteBuffer in = wrapChunk(request);
        int op = in.getInt();
        android.view.View rootView = getRootView(in);
        if (rootView == null) {
            return createFailChunk(android.ddm.DdmHandleViewDebug.ERR_INVALID_PARAM, "Invalid View Root");
        }
        if (type == android.ddm.DdmHandleViewDebug.CHUNK_VURT) {
            if (op == android.ddm.DdmHandleViewDebug.VURT_DUMP_HIERARCHY)
                return dumpHierarchy(rootView, in);
            else
                if (op == android.ddm.DdmHandleViewDebug.VURT_CAPTURE_LAYERS)
                    return captureLayers(rootView);
                else
                    if (op == android.ddm.DdmHandleViewDebug.VURT_DUMP_THEME)
                        return dumpTheme(rootView);
                    else
                        return createFailChunk(android.ddm.DdmHandleViewDebug.ERR_INVALID_OP, "Unknown view root operation: " + op);



        }
        final android.view.View targetView = getTargetView(rootView, in);
        if (targetView == null) {
            return createFailChunk(android.ddm.DdmHandleViewDebug.ERR_INVALID_PARAM, "Invalid target view");
        }
        if (type == android.ddm.DdmHandleViewDebug.CHUNK_VUOP) {
            switch (op) {
                case android.ddm.DdmHandleViewDebug.VUOP_CAPTURE_VIEW :
                    return captureView(rootView, targetView);
                case android.ddm.DdmHandleViewDebug.VUOP_DUMP_DISPLAYLIST :
                    return dumpDisplayLists(rootView, targetView);
                case android.ddm.DdmHandleViewDebug.VUOP_PROFILE_VIEW :
                    return profileView(rootView, targetView);
                case android.ddm.DdmHandleViewDebug.VUOP_INVOKE_VIEW_METHOD :
                    return invokeViewMethod(rootView, targetView, in);
                case android.ddm.DdmHandleViewDebug.VUOP_SET_LAYOUT_PARAMETER :
                    return setLayoutParameter(rootView, targetView, in);
                default :
                    return createFailChunk(android.ddm.DdmHandleViewDebug.ERR_INVALID_OP, "Unknown view operation: " + op);
            }
        } else {
            throw new java.lang.RuntimeException("Unknown packet " + org.apache.harmony.dalvik.ddmc.ChunkHandler.name(type));
        }
    }

    /**
     * Returns the list of windows owned by this client.
     */
    private org.apache.harmony.dalvik.ddmc.Chunk listWindows() {
        java.lang.String[] windowNames = android.view.WindowManagerGlobal.getInstance().getViewRootNames();
        int responseLength = 4;
        // # of windows
        for (java.lang.String name : windowNames) {
            responseLength += 4;
            // length of next window name
            responseLength += name.length() * 2;// window name

        }
        java.nio.ByteBuffer out = java.nio.ByteBuffer.allocate(responseLength);
        out.order(ChunkHandler.CHUNK_ORDER);
        out.putInt(windowNames.length);
        for (java.lang.String name : windowNames) {
            out.putInt(name.length());
            putString(out, name);
        }
        return new org.apache.harmony.dalvik.ddmc.Chunk(android.ddm.DdmHandleViewDebug.CHUNK_VULW, out);
    }

    private android.view.View getRootView(java.nio.ByteBuffer in) {
        try {
            int viewRootNameLength = in.getInt();
            java.lang.String viewRootName = getString(in, viewRootNameLength);
            return android.view.WindowManagerGlobal.getInstance().getRootView(viewRootName);
        } catch (java.nio.BufferUnderflowException e) {
            return null;
        }
    }

    private android.view.View getTargetView(android.view.View root, java.nio.ByteBuffer in) {
        int viewLength;
        java.lang.String viewName;
        try {
            viewLength = in.getInt();
            viewName = getString(in, viewLength);
        } catch (java.nio.BufferUnderflowException e) {
            return null;
        }
        return android.view.ViewDebug.findView(root, viewName);
    }

    /**
     * Returns the view hierarchy and/or view properties starting at the provided view.
     * Based on the input options, the return data may include:
     *  - just the view hierarchy
     *  - view hierarchy & the properties for each of the views
     *  - just the view properties for a specific view.
     *  TODO: Currently this only returns views starting at the root, need to fix so that
     *  it can return properties of any view.
     */
    private org.apache.harmony.dalvik.ddmc.Chunk dumpHierarchy(android.view.View rootView, java.nio.ByteBuffer in) {
        boolean skipChildren = in.getInt() > 0;
        boolean includeProperties = in.getInt() > 0;
        boolean v2 = in.hasRemaining() && (in.getInt() > 0);
        long start = java.lang.System.currentTimeMillis();
        java.io.ByteArrayOutputStream b = new java.io.ByteArrayOutputStream((2 * 1024) * 1024);
        try {
            if (v2) {
                android.view.ViewDebug.dumpv2(rootView, b);
            } else {
                android.view.ViewDebug.dump(rootView, skipChildren, includeProperties, b);
            }
        } catch (java.io.IOException | java.lang.InterruptedException e) {
            return createFailChunk(1, "Unexpected error while obtaining view hierarchy: " + e.getMessage());
        }
        long end = java.lang.System.currentTimeMillis();
        android.util.Log.d(android.ddm.DdmHandleViewDebug.TAG, "Time to obtain view hierarchy (ms): " + (end - start));
        byte[] data = b.toByteArray();
        return new org.apache.harmony.dalvik.ddmc.Chunk(android.ddm.DdmHandleViewDebug.CHUNK_VURT, data, 0, data.length);
    }

    /**
     * Returns a buffer with region details & bitmap of every single view.
     */
    private org.apache.harmony.dalvik.ddmc.Chunk captureLayers(android.view.View rootView) {
        java.io.ByteArrayOutputStream b = new java.io.ByteArrayOutputStream(1024);
        java.io.DataOutputStream dos = new java.io.DataOutputStream(b);
        try {
            android.view.ViewDebug.captureLayers(rootView, dos);
        } catch (java.io.IOException e) {
            return createFailChunk(1, "Unexpected error while obtaining view hierarchy: " + e.getMessage());
        } finally {
            try {
                dos.close();
            } catch (java.io.IOException e) {
                // ignore
            }
        }
        byte[] data = b.toByteArray();
        return new org.apache.harmony.dalvik.ddmc.Chunk(android.ddm.DdmHandleViewDebug.CHUNK_VURT, data, 0, data.length);
    }

    /**
     * Returns the Theme dump of the provided view.
     */
    private org.apache.harmony.dalvik.ddmc.Chunk dumpTheme(android.view.View rootView) {
        java.io.ByteArrayOutputStream b = new java.io.ByteArrayOutputStream(1024);
        try {
            android.view.ViewDebug.dumpTheme(rootView, b);
        } catch (java.io.IOException e) {
            return createFailChunk(1, "Unexpected error while dumping the theme: " + e.getMessage());
        }
        byte[] data = b.toByteArray();
        return new org.apache.harmony.dalvik.ddmc.Chunk(android.ddm.DdmHandleViewDebug.CHUNK_VURT, data, 0, data.length);
    }

    private org.apache.harmony.dalvik.ddmc.Chunk captureView(android.view.View rootView, android.view.View targetView) {
        java.io.ByteArrayOutputStream b = new java.io.ByteArrayOutputStream(1024);
        try {
            android.view.ViewDebug.capture(rootView, b, targetView);
        } catch (java.io.IOException e) {
            return createFailChunk(1, "Unexpected error while capturing view: " + e.getMessage());
        }
        byte[] data = b.toByteArray();
        return new org.apache.harmony.dalvik.ddmc.Chunk(android.ddm.DdmHandleViewDebug.CHUNK_VUOP, data, 0, data.length);
    }

    /**
     * Returns the display lists corresponding to the provided view.
     */
    private org.apache.harmony.dalvik.ddmc.Chunk dumpDisplayLists(final android.view.View rootView, final android.view.View targetView) {
        rootView.post(new java.lang.Runnable() {
            @java.lang.Override
            public void run() {
                android.view.ViewDebug.outputDisplayList(rootView, targetView);
            }
        });
        return null;
    }

    /**
     * Invokes provided method on the view.
     * The method name and its arguments are passed in as inputs via the byte buffer.
     * The buffer contains:<ol>
     *  <li> len(method name) </li>
     *  <li> method name </li>
     *  <li> # of args </li>
     *  <li> arguments: Each argument comprises of a type specifier followed by the actual argument.
     *          The type specifier is a single character as used in JNI:
     *          (Z - boolean, B - byte, C - char, S - short, I - int, J - long,
     *          F - float, D - double). <p>
     *          The type specifier is followed by the actual value of argument.
     *          Booleans are encoded via bytes with 0 indicating false.</li>
     * </ol>
     * Methods that take no arguments need only specify the method name.
     */
    private org.apache.harmony.dalvik.ddmc.Chunk invokeViewMethod(final android.view.View rootView, final android.view.View targetView, java.nio.ByteBuffer in) {
        int l = in.getInt();
        java.lang.String methodName = getString(in, l);
        java.lang.Class<?>[] argTypes;
        java.lang.Object[] args;
        if (!in.hasRemaining()) {
            argTypes = new java.lang.Class<?>[0];
            args = new java.lang.Object[0];
        } else {
            int nArgs = in.getInt();
            argTypes = new java.lang.Class<?>[nArgs];
            args = new java.lang.Object[nArgs];
            for (int i = 0; i < nArgs; i++) {
                char c = in.getChar();
                switch (c) {
                    case 'Z' :
                        argTypes[i] = boolean.class;
                        args[i] = (in.get() == 0) ? false : true;
                        break;
                    case 'B' :
                        argTypes[i] = byte.class;
                        args[i] = in.get();
                        break;
                    case 'C' :
                        argTypes[i] = char.class;
                        args[i] = in.getChar();
                        break;
                    case 'S' :
                        argTypes[i] = short.class;
                        args[i] = in.getShort();
                        break;
                    case 'I' :
                        argTypes[i] = int.class;
                        args[i] = in.getInt();
                        break;
                    case 'J' :
                        argTypes[i] = long.class;
                        args[i] = in.getLong();
                        break;
                    case 'F' :
                        argTypes[i] = float.class;
                        args[i] = in.getFloat();
                        break;
                    case 'D' :
                        argTypes[i] = double.class;
                        args[i] = in.getDouble();
                        break;
                    default :
                        android.util.Log.e(android.ddm.DdmHandleViewDebug.TAG, (("arg " + i) + ", unrecognized type: ") + c);
                        return createFailChunk(android.ddm.DdmHandleViewDebug.ERR_INVALID_PARAM, ("Unsupported parameter type (" + c) + ") to invoke view method.");
                }
            }
        }
        java.lang.reflect.Method method = null;
        try {
            method = targetView.getClass().getMethod(methodName, argTypes);
        } catch (java.lang.NoSuchMethodException e) {
            android.util.Log.e(android.ddm.DdmHandleViewDebug.TAG, "No such method: " + e.getMessage());
            return createFailChunk(android.ddm.DdmHandleViewDebug.ERR_INVALID_PARAM, "No such method: " + e.getMessage());
        }
        try {
            android.view.ViewDebug.invokeViewMethod(targetView, method, args);
        } catch (java.lang.Exception e) {
            android.util.Log.e(android.ddm.DdmHandleViewDebug.TAG, "Exception while invoking method: " + e.getCause().getMessage());
            java.lang.String msg = e.getCause().getMessage();
            if (msg == null) {
                msg = e.getCause().toString();
            }
            return createFailChunk(android.ddm.DdmHandleViewDebug.ERR_EXCEPTION, msg);
        }
        return null;
    }

    private org.apache.harmony.dalvik.ddmc.Chunk setLayoutParameter(final android.view.View rootView, final android.view.View targetView, java.nio.ByteBuffer in) {
        int l = in.getInt();
        java.lang.String param = getString(in, l);
        int value = in.getInt();
        try {
            android.view.ViewDebug.setLayoutParameter(targetView, param, value);
        } catch (java.lang.Exception e) {
            android.util.Log.e(android.ddm.DdmHandleViewDebug.TAG, "Exception setting layout parameter: " + e);
            return createFailChunk(android.ddm.DdmHandleViewDebug.ERR_EXCEPTION, (("Error accessing field " + param) + ":") + e.getMessage());
        }
        return null;
    }

    /**
     * Profiles provided view.
     */
    private org.apache.harmony.dalvik.ddmc.Chunk profileView(android.view.View rootView, final android.view.View targetView) {
        java.io.ByteArrayOutputStream b = new java.io.ByteArrayOutputStream(32 * 1024);
        java.io.BufferedWriter bw = new java.io.BufferedWriter(new java.io.OutputStreamWriter(b), 32 * 1024);
        try {
            android.view.ViewDebug.profileViewAndChildren(targetView, bw);
        } catch (java.io.IOException e) {
            return createFailChunk(1, "Unexpected error while profiling view: " + e.getMessage());
        } finally {
            try {
                bw.close();
            } catch (java.io.IOException e) {
                // ignore
            }
        }
        byte[] data = b.toByteArray();
        return new org.apache.harmony.dalvik.ddmc.Chunk(android.ddm.DdmHandleViewDebug.CHUNK_VUOP, data, 0, data.length);
    }
}

