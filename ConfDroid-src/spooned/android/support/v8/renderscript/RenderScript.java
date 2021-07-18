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
package android.support.v8.renderscript;


/**
 * This class provides access to a RenderScript context, which controls RenderScript
 * initialization, resource management, and teardown. An instance of the RenderScript
 * class must be created before any other RS objects can be created.
 *
 * <div class="special reference">
 * <h3>Developer Guides</h3>
 * <p>For more information about creating an application that uses RenderScript, read the
 * <a href="{@docRoot }guide/topics/renderscript/index.html">RenderScript</a> developer guide.</p>
 * </div>
 */
public class RenderScript {
    static final java.lang.String LOG_TAG = "RenderScript_jni";

    static final boolean DEBUG = false;

    @java.lang.SuppressWarnings({ "UnusedDeclaration", "deprecation" })
    static final boolean LOG_ENABLED = false;

    static final int SUPPORT_LIB_API = 23;

    static final int SUPPORT_LIB_VERSION = 2301;

    private static java.util.ArrayList<android.support.v8.renderscript.RenderScript> mProcessContextList = new java.util.ArrayList<android.support.v8.renderscript.RenderScript>();

    private boolean mIsProcessContext = false;

    private boolean mEnableMultiInput = false;

    private int mDispatchAPILevel = 0;

    private int mContextFlags = 0;

    private int mContextSdkVersion = 0;

    private android.content.Context mApplicationContext;

    private java.lang.String mNativeLibDir;

    private static java.lang.String mBlackList = "";

    /**
     * Sets the blackList of Models to only use support lib runtime.
     * Should be used before context create.
     *
     * @param blackList
     * 		User provided black list string.
     * 		
     * 		Format: "(MANUFACTURER1:PRODUCT1:MODEL1), (MANUFACTURER2:PRODUCT2:MODEL2)..."
     * 		e.g. : To Blacklist Nexus 7(2013) and Nexus 5.
     * 		mBlackList = "(asus:razor:Nexus 7), (LGE:hammerhead:Nexus 5)";
     */
    public static void setBlackList(java.lang.String blackList) {
        if (blackList != null) {
            android.support.v8.renderscript.RenderScript.mBlackList = blackList;
        }
    }

    /**
     * Force using support lib runtime.
     * Should be used before context create.
     */
    public static void forceCompat() {
        android.support.v8.renderscript.RenderScript.sNative = 0;
    }

    /* We use a class initializer to allow the native code to cache some
    field offsets.
     */
    @java.lang.SuppressWarnings({ "FieldCanBeLocal", "UnusedDeclaration" })
    static boolean sInitialized;

    static boolean sUseGCHooks;

    static java.lang.Object sRuntime;

    static java.lang.reflect.Method registerNativeAllocation;

    static java.lang.reflect.Method registerNativeFree;

    static java.lang.Object lock = new java.lang.Object();

    // Non-threadsafe functions.
    native boolean nLoadSO(boolean useNative, int deviceApi, java.lang.String libPath);

    native boolean nLoadIOSO();

    native long nDeviceCreate();

    native void nDeviceDestroy(long dev);

    native void nDeviceSetConfig(long dev, int param, int value);

    native int nContextGetUserMessage(long con, int[] data);

    native java.lang.String nContextGetErrorMessage(long con);

    native int nContextPeekMessage(long con, int[] subID);

    native void nContextInitToClient(long con);

    native void nContextDeinitToClient(long con);

    private static int sNative = -1;

    private static int sSdkVersion = -1;

    private static boolean useIOlib = false;

    private static boolean useNative;

    /* Context creation flag that specifies a normal context.
    RenderScript Support lib only support normal context.
     */
    public static final int CREATE_FLAG_NONE = 0x0;

    int getDispatchAPILevel() {
        return mDispatchAPILevel;
    }

    boolean isUseNative() {
        return android.support.v8.renderscript.RenderScript.useNative;
    }

    /* Detect the bitness of the VM to allow FieldPacker to do the right thing. */
    static native int rsnSystemGetPointerSize();

    static int sPointerSize;

    /**
     * Determines whether or not we should be thunking into the native
     * RenderScript layer or actually using the compatibility library.
     */
    private static boolean setupNative(int sdkVersion, android.content.Context ctx) {
        // if targetSdkVersion is higher than the device api version, always use compat mode.
        // Workaround for KK
        if ((android.os.Build.VERSION.SDK_INT < sdkVersion) && (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP)) {
            android.support.v8.renderscript.RenderScript.sNative = 0;
        }
        if (android.support.v8.renderscript.RenderScript.sNative == (-1)) {
            // get the value of the debug.rs.forcecompat property
            int forcecompat = 0;
            try {
                java.lang.Class<?> sysprop = java.lang.Class.forName("android.os.SystemProperties");
                java.lang.Class[] signature = new java.lang.Class[]{ java.lang.String.class, java.lang.Integer.TYPE };
                java.lang.reflect.Method getint = sysprop.getDeclaredMethod("getInt", signature);
                java.lang.Object[] args = new java.lang.Object[]{ "debug.rs.forcecompat", new java.lang.Integer(0) };
                forcecompat = ((java.lang.Integer) (getint.invoke(null, args))).intValue();
            } catch (java.lang.Exception e) {
            }
            if ((android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) && (forcecompat == 0)) {
                android.support.v8.renderscript.RenderScript.sNative = 1;
            } else {
                android.support.v8.renderscript.RenderScript.sNative = 0;
            }
            if (android.support.v8.renderscript.RenderScript.sNative == 1) {
                // Workarounds that may disable thunking go here
                android.content.pm.ApplicationInfo info;
                try {
                    info = ctx.getPackageManager().getApplicationInfo(ctx.getPackageName(), android.content.pm.PackageManager.GET_META_DATA);
                } catch (android.content.pm.PackageManager.NameNotFoundException e) {
                    // assume no workarounds needed
                    return true;
                }
                long minorVersion = 0;
                // load minorID from reflection
                try {
                    java.lang.Class<?> javaRS = java.lang.Class.forName("android.renderscript.RenderScript");
                    java.lang.reflect.Method getMinorID = javaRS.getDeclaredMethod("getMinorID");
                    minorVersion = ((java.lang.Long) (getMinorID.invoke(null))).longValue();
                } catch (java.lang.Exception e) {
                    // minor version remains 0 on devices with no possible WARs
                }
                if (info.metaData != null) {
                    // asynchronous teardown: minor version 1+
                    if (info.metaData.getBoolean("com.android.support.v8.renderscript.EnableAsyncTeardown") == true) {
                        if (minorVersion == 0) {
                            android.support.v8.renderscript.RenderScript.sNative = 0;
                        }
                    }
                    // blur issues on some drivers with 4.4
                    if (info.metaData.getBoolean("com.android.support.v8.renderscript.EnableBlurWorkaround") == true) {
                        if (android.os.Build.VERSION.SDK_INT <= 19) {
                            // android.util.Log.e("rs", "war on");
                            android.support.v8.renderscript.RenderScript.sNative = 0;
                        }
                    }
                }
                // end of workarounds
            }
        }
        if (android.support.v8.renderscript.RenderScript.sNative == 1) {
            // check against the blacklist
            if (android.support.v8.renderscript.RenderScript.mBlackList.length() > 0) {
                java.lang.String deviceInfo = ((((('(' + android.os.Build.MANUFACTURER) + ':') + android.os.Build.PRODUCT) + ':') + android.os.Build.MODEL) + ')';
                if (android.support.v8.renderscript.RenderScript.mBlackList.contains(deviceInfo)) {
                    android.support.v8.renderscript.RenderScript.sNative = 0;
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    /**
     * Name of the file that holds the object cache.
     */
    private static final java.lang.String CACHE_PATH = "com.android.renderscript.cache";

    static java.lang.String mCachePath;

    /**
     * Sets the directory to use as a persistent storage for the
     * renderscript object file cache.
     *
     * @unknown 
     * @param cacheDir
     * 		A directory the current process can write to
     */
    public static void setupDiskCache(java.io.File cacheDir) {
        java.io.File f = new java.io.File(cacheDir, android.support.v8.renderscript.RenderScript.CACHE_PATH);
        android.support.v8.renderscript.RenderScript.mCachePath = f.getAbsolutePath();
        f.mkdirs();
    }

    /**
     * ContextType specifies the specific type of context to be created.
     */
    public enum ContextType {

        /**
         * NORMAL context, this is the default and what shipping apps should
         * use.
         */
        NORMAL(0),
        /**
         * DEBUG context, perform extra runtime checks to validate the
         * kernels and APIs are being used as intended.  Get and SetElementAt
         * will be bounds checked in this mode.
         */
        DEBUG(1),
        /**
         * PROFILE context, Intended to be used once the first time an
         * application is run on a new device.  This mode allows the runtime to
         * do additional testing and performance tuning.
         */
        PROFILE(2);
        int mID;

        ContextType(int id) {
            mID = id;
        }
    }

    android.support.v8.renderscript.RenderScript.ContextType mContextType;

    // Methods below are wrapped to protect the non-threadsafe
    // lockless fifo.
    native long rsnContextCreate(long dev, int ver, int sdkVer, int contextType, java.lang.String nativeLibDir);

    synchronized long nContextCreate(long dev, int ver, int sdkVer, int contextType, java.lang.String nativeLibDir) {
        return rsnContextCreate(dev, ver, sdkVer, contextType, nativeLibDir);
    }

    native void rsnContextDestroy(long con);

    synchronized void nContextDestroy() {
        validate();
        // take teardown lock
        // teardown lock can only be taken when no objects are being destroyed
        java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock wlock = mRWLock.writeLock();
        wlock.lock();
        long curCon = mContext;
        // context is considered dead as of this point
        mContext = 0;
        wlock.unlock();
        rsnContextDestroy(curCon);
    }

    native void rsnContextSetPriority(long con, int p);

    synchronized void nContextSetPriority(int p) {
        validate();
        rsnContextSetPriority(mContext, p);
    }

    native void rsnContextDump(long con, int bits);

    synchronized void nContextDump(int bits) {
        validate();
        rsnContextDump(mContext, bits);
    }

    native void rsnContextFinish(long con);

    synchronized void nContextFinish() {
        validate();
        rsnContextFinish(mContext);
    }

    native void rsnContextSendMessage(long con, int id, int[] data);

    synchronized void nContextSendMessage(int id, int[] data) {
        validate();
        rsnContextSendMessage(mContext, id, data);
    }

    // nObjDestroy is explicitly _not_ synchronous to prevent crashes in finalizers
    native void rsnObjDestroy(long con, long id);

    void nObjDestroy(long id) {
        // There is a race condition here.  The calling code may be run
        // by the gc while teardown is occuring.  This protects againts
        // deleting dead objects.
        if (mContext != 0) {
            rsnObjDestroy(mContext, id);
        }
    }

    native long rsnElementCreate(long con, long type, int kind, boolean norm, int vecSize);

    synchronized long nElementCreate(long type, int kind, boolean norm, int vecSize) {
        validate();
        return rsnElementCreate(mContext, type, kind, norm, vecSize);
    }

    native long rsnElementCreate2(long con, long[] elements, java.lang.String[] names, int[] arraySizes);

    synchronized long nElementCreate2(long[] elements, java.lang.String[] names, int[] arraySizes) {
        validate();
        return rsnElementCreate2(mContext, elements, names, arraySizes);
    }

    native void rsnElementGetNativeData(long con, long id, int[] elementData);

    synchronized void nElementGetNativeData(long id, int[] elementData) {
        validate();
        rsnElementGetNativeData(mContext, id, elementData);
    }

    native void rsnElementGetSubElements(long con, long id, long[] IDs, java.lang.String[] names, int[] arraySizes);

    synchronized void nElementGetSubElements(long id, long[] IDs, java.lang.String[] names, int[] arraySizes) {
        validate();
        rsnElementGetSubElements(mContext, id, IDs, names, arraySizes);
    }

    native long rsnTypeCreate(long con, long eid, int x, int y, int z, boolean mips, boolean faces, int yuv);

    synchronized long nTypeCreate(long eid, int x, int y, int z, boolean mips, boolean faces, int yuv) {
        validate();
        return rsnTypeCreate(mContext, eid, x, y, z, mips, faces, yuv);
    }

    native void rsnTypeGetNativeData(long con, long id, long[] typeData);

    synchronized void nTypeGetNativeData(long id, long[] typeData) {
        validate();
        rsnTypeGetNativeData(mContext, id, typeData);
    }

    native long rsnAllocationCreateTyped(long con, long type, int mip, int usage, long pointer);

    synchronized long nAllocationCreateTyped(long type, int mip, int usage, long pointer) {
        validate();
        return rsnAllocationCreateTyped(mContext, type, mip, usage, pointer);
    }

    native long rsnAllocationCreateFromBitmap(long con, long type, int mip, android.graphics.Bitmap bmp, int usage);

    synchronized long nAllocationCreateFromBitmap(long type, int mip, android.graphics.Bitmap bmp, int usage) {
        validate();
        return rsnAllocationCreateFromBitmap(mContext, type, mip, bmp, usage);
    }

    native long rsnAllocationCreateBitmapBackedAllocation(long con, long type, int mip, android.graphics.Bitmap bmp, int usage);

    synchronized long nAllocationCreateBitmapBackedAllocation(long type, int mip, android.graphics.Bitmap bmp, int usage) {
        validate();
        return rsnAllocationCreateBitmapBackedAllocation(mContext, type, mip, bmp, usage);
    }

    native long rsnAllocationCubeCreateFromBitmap(long con, long type, int mip, android.graphics.Bitmap bmp, int usage);

    synchronized long nAllocationCubeCreateFromBitmap(long type, int mip, android.graphics.Bitmap bmp, int usage) {
        validate();
        return rsnAllocationCubeCreateFromBitmap(mContext, type, mip, bmp, usage);
    }

    native long rsnAllocationCreateBitmapRef(long con, long type, android.graphics.Bitmap bmp);

    synchronized long nAllocationCreateBitmapRef(long type, android.graphics.Bitmap bmp) {
        validate();
        return rsnAllocationCreateBitmapRef(mContext, type, bmp);
    }

    native long rsnAllocationCreateFromAssetStream(long con, int mips, int assetStream, int usage);

    synchronized long nAllocationCreateFromAssetStream(int mips, int assetStream, int usage) {
        validate();
        return rsnAllocationCreateFromAssetStream(mContext, mips, assetStream, usage);
    }

    native void rsnAllocationCopyToBitmap(long con, long alloc, android.graphics.Bitmap bmp);

    synchronized void nAllocationCopyToBitmap(long alloc, android.graphics.Bitmap bmp) {
        validate();
        rsnAllocationCopyToBitmap(mContext, alloc, bmp);
    }

    native void rsnAllocationSyncAll(long con, long alloc, int src);

    synchronized void nAllocationSyncAll(long alloc, int src) {
        validate();
        rsnAllocationSyncAll(mContext, alloc, src);
    }

    native void rsnAllocationSetSurface(long con, long alloc, android.view.Surface sur);

    synchronized void nAllocationSetSurface(long alloc, android.view.Surface sur) {
        validate();
        rsnAllocationSetSurface(mContext, alloc, sur);
    }

    native void rsnAllocationIoSend(long con, long alloc);

    synchronized void nAllocationIoSend(long alloc) {
        validate();
        rsnAllocationIoSend(mContext, alloc);
    }

    native void rsnAllocationIoReceive(long con, long alloc);

    synchronized void nAllocationIoReceive(long alloc) {
        validate();
        rsnAllocationIoReceive(mContext, alloc);
    }

    native java.nio.ByteBuffer rsnAllocationGetByteBuffer(long con, long alloc, int xBytesSize, int dimY, int dimZ);

    synchronized java.nio.ByteBuffer nAllocationGetByteBuffer(long alloc, int xBytesSize, int dimY, int dimZ) {
        validate();
        return rsnAllocationGetByteBuffer(mContext, alloc, xBytesSize, dimY, dimZ);
    }

    native long rsnAllocationGetStride(long con, long alloc);

    synchronized long nAllocationGetStride(long alloc) {
        validate();
        return rsnAllocationGetStride(mContext, alloc);
    }

    native void rsnAllocationGenerateMipmaps(long con, long alloc);

    synchronized void nAllocationGenerateMipmaps(long alloc) {
        validate();
        rsnAllocationGenerateMipmaps(mContext, alloc);
    }

    native void rsnAllocationCopyFromBitmap(long con, long alloc, android.graphics.Bitmap bmp);

    synchronized void nAllocationCopyFromBitmap(long alloc, android.graphics.Bitmap bmp) {
        validate();
        rsnAllocationCopyFromBitmap(mContext, alloc, bmp);
    }

    native void rsnAllocationData1D(long con, long id, int off, int mip, int count, java.lang.Object d, int sizeBytes, int dt, int mSize, boolean usePadding);

    synchronized void nAllocationData1D(long id, int off, int mip, int count, java.lang.Object d, int sizeBytes, android.support.v8.renderscript.Element.DataType dt, int mSize, boolean usePadding) {
        validate();
        rsnAllocationData1D(mContext, id, off, mip, count, d, sizeBytes, dt.mID, mSize, usePadding);
    }

    native void rsnAllocationElementData1D(long con, long id, int xoff, int mip, int compIdx, byte[] d, int sizeBytes);

    synchronized void nAllocationElementData1D(long id, int xoff, int mip, int compIdx, byte[] d, int sizeBytes) {
        validate();
        rsnAllocationElementData1D(mContext, id, xoff, mip, compIdx, d, sizeBytes);
    }

    /* native void rsnAllocationElementData(long con,long id, int xoff, int yoff, int zoff, int mip, int compIdx, byte[] d, int sizeBytes);
    synchronized void nAllocationElementData(long id, int xoff, int yoff, int zoff, int mip, int compIdx, byte[] d, int sizeBytes) {
    validate();
    rsnAllocationElementData(mContext, id, xoff, yoff, zoff, mip, compIdx, d, sizeBytes);
    }
     */
    native void rsnAllocationData2D(long con, long dstAlloc, int dstXoff, int dstYoff, int dstMip, int dstFace, int width, int height, long srcAlloc, int srcXoff, int srcYoff, int srcMip, int srcFace);

    synchronized void nAllocationData2D(long dstAlloc, int dstXoff, int dstYoff, int dstMip, int dstFace, int width, int height, long srcAlloc, int srcXoff, int srcYoff, int srcMip, int srcFace) {
        validate();
        rsnAllocationData2D(mContext, dstAlloc, dstXoff, dstYoff, dstMip, dstFace, width, height, srcAlloc, srcXoff, srcYoff, srcMip, srcFace);
    }

    native void rsnAllocationData2D(long con, long id, int xoff, int yoff, int mip, int face, int w, int h, java.lang.Object d, int sizeBytes, int dt, int mSize, boolean usePadding);

    synchronized void nAllocationData2D(long id, int xoff, int yoff, int mip, int face, int w, int h, java.lang.Object d, int sizeBytes, android.support.v8.renderscript.Element.DataType dt, int mSize, boolean usePadding) {
        validate();
        rsnAllocationData2D(mContext, id, xoff, yoff, mip, face, w, h, d, sizeBytes, dt.mID, mSize, usePadding);
    }

    native void rsnAllocationData2D(long con, long id, int xoff, int yoff, int mip, int face, android.graphics.Bitmap b);

    synchronized void nAllocationData2D(long id, int xoff, int yoff, int mip, int face, android.graphics.Bitmap b) {
        validate();
        rsnAllocationData2D(mContext, id, xoff, yoff, mip, face, b);
    }

    native void rsnAllocationData3D(long con, long dstAlloc, int dstXoff, int dstYoff, int dstZoff, int dstMip, int width, int height, int depth, long srcAlloc, int srcXoff, int srcYoff, int srcZoff, int srcMip);

    synchronized void nAllocationData3D(long dstAlloc, int dstXoff, int dstYoff, int dstZoff, int dstMip, int width, int height, int depth, long srcAlloc, int srcXoff, int srcYoff, int srcZoff, int srcMip) {
        validate();
        rsnAllocationData3D(mContext, dstAlloc, dstXoff, dstYoff, dstZoff, dstMip, width, height, depth, srcAlloc, srcXoff, srcYoff, srcZoff, srcMip);
    }

    native void rsnAllocationData3D(long con, long id, int xoff, int yoff, int zoff, int mip, int w, int h, int depth, java.lang.Object d, int sizeBytes, int dt, int mSize, boolean usePadding);

    synchronized void nAllocationData3D(long id, int xoff, int yoff, int zoff, int mip, int w, int h, int depth, java.lang.Object d, int sizeBytes, android.support.v8.renderscript.Element.DataType dt, int mSize, boolean usePadding) {
        validate();
        rsnAllocationData3D(mContext, id, xoff, yoff, zoff, mip, w, h, depth, d, sizeBytes, dt.mID, mSize, usePadding);
    }

    native void rsnAllocationRead(long con, long id, java.lang.Object d, int dt, int mSize, boolean usePadding);

    synchronized void nAllocationRead(long id, java.lang.Object d, android.support.v8.renderscript.Element.DataType dt, int mSize, boolean usePadding) {
        validate();
        rsnAllocationRead(mContext, id, d, dt.mID, mSize, usePadding);
    }

    native void rsnAllocationRead1D(long con, long id, int off, int mip, int count, java.lang.Object d, int sizeBytes, int dt, int mSize, boolean usePadding);

    synchronized void nAllocationRead1D(long id, int off, int mip, int count, java.lang.Object d, int sizeBytes, android.support.v8.renderscript.Element.DataType dt, int mSize, boolean usePadding) {
        validate();
        rsnAllocationRead1D(mContext, id, off, mip, count, d, sizeBytes, dt.mID, mSize, usePadding);
    }

    /* native void rsnAllocationElementRead(long con,long id, int xoff, int yoff, int zoff,
    int mip, int compIdx, byte[] d, int sizeBytes);
    synchronized void nAllocationElementRead(long id, int xoff, int yoff, int zoff,
    int mip, int compIdx, byte[] d, int sizeBytes) {
    validate();
    rsnAllocationElementRead(mContext, id, xoff, yoff, zoff, mip, compIdx, d, sizeBytes);
    }
     */
    native void rsnAllocationRead2D(long con, long id, int xoff, int yoff, int mip, int face, int w, int h, java.lang.Object d, int sizeBytes, int dt, int mSize, boolean usePadding);

    synchronized void nAllocationRead2D(long id, int xoff, int yoff, int mip, int face, int w, int h, java.lang.Object d, int sizeBytes, android.support.v8.renderscript.Element.DataType dt, int mSize, boolean usePadding) {
        validate();
        rsnAllocationRead2D(mContext, id, xoff, yoff, mip, face, w, h, d, sizeBytes, dt.mID, mSize, usePadding);
    }

    /* native void rsnAllocationRead3D(long con, long id, int xoff, int yoff, int zoff, int mip,
    int w, int h, int depth, Object d, int sizeBytes, int dt,
    int mSize, boolean usePadding);
    synchronized void nAllocationRead3D(long id, int xoff, int yoff, int zoff, int mip,
    int w, int h, int depth, Object d, int sizeBytes, Element.DataType dt,
    int mSize, boolean usePadding) {
    validate();
    rsnAllocationRead3D(mContext, id, xoff, yoff, zoff, mip, w, h, depth, d, sizeBytes, dt.mID, mSize, usePadding);
    }
     */
    native long rsnAllocationGetType(long con, long id);

    synchronized long nAllocationGetType(long id) {
        validate();
        return rsnAllocationGetType(mContext, id);
    }

    native void rsnAllocationResize1D(long con, long id, int dimX);

    synchronized void nAllocationResize1D(long id, int dimX) {
        validate();
        rsnAllocationResize1D(mContext, id, dimX);
    }

    native void rsnAllocationResize2D(long con, long id, int dimX, int dimY);

    synchronized void nAllocationResize2D(long id, int dimX, int dimY) {
        validate();
        rsnAllocationResize2D(mContext, id, dimX, dimY);
    }

    native void rsnScriptBindAllocation(long con, long script, long alloc, int slot, boolean mUseInc);

    synchronized void nScriptBindAllocation(long script, long alloc, int slot, boolean mUseInc) {
        validate();
        long curCon = mContext;
        if (mUseInc) {
            curCon = mIncCon;
        }
        rsnScriptBindAllocation(curCon, script, alloc, slot, mUseInc);
    }

    native void rsnScriptSetTimeZone(long con, long script, byte[] timeZone, boolean mUseInc);

    synchronized void nScriptSetTimeZone(long script, byte[] timeZone, boolean mUseInc) {
        validate();
        long curCon = mContext;
        if (mUseInc) {
            curCon = mIncCon;
        }
        rsnScriptSetTimeZone(curCon, script, timeZone, mUseInc);
    }

    native void rsnScriptInvoke(long con, long id, int slot, boolean mUseInc);

    synchronized void nScriptInvoke(long id, int slot, boolean mUseInc) {
        validate();
        long curCon = mContext;
        if (mUseInc) {
            curCon = mIncCon;
        }
        rsnScriptInvoke(curCon, id, slot, mUseInc);
    }

    native void rsnScriptForEach(long con, long incCon, long id, int slot, long ain, long aout, byte[] params, boolean mUseInc);

    native void rsnScriptForEach(long con, long incCon, long id, int slot, long ain, long aout, boolean mUseInc);

    native void rsnScriptForEachClipped(long con, long incCon, long id, int slot, long ain, long aout, byte[] params, int xstart, int xend, int ystart, int yend, int zstart, int zend, boolean mUseInc);

    native void rsnScriptForEachClipped(long con, long incCon, long id, int slot, long ain, long aout, int xstart, int xend, int ystart, int yend, int zstart, int zend, boolean mUseInc);

    synchronized void nScriptForEach(long id, int slot, long ain, long aout, byte[] params, boolean mUseInc) {
        validate();
        if (params == null) {
            rsnScriptForEach(mContext, mIncCon, id, slot, ain, aout, mUseInc);
        } else {
            rsnScriptForEach(mContext, mIncCon, id, slot, ain, aout, params, mUseInc);
        }
    }

    synchronized void nScriptForEachClipped(long id, int slot, long ain, long aout, byte[] params, int xstart, int xend, int ystart, int yend, int zstart, int zend, boolean mUseInc) {
        validate();
        if (params == null) {
            rsnScriptForEachClipped(mContext, mIncCon, id, slot, ain, aout, xstart, xend, ystart, yend, zstart, zend, mUseInc);
        } else {
            rsnScriptForEachClipped(mContext, mIncCon, id, slot, ain, aout, params, xstart, xend, ystart, yend, zstart, zend, mUseInc);
        }
    }

    native void rsnScriptForEach(long con, long id, int slot, long[] ains, long aout, byte[] params, int[] limits);

    synchronized void nScriptForEach(long id, int slot, long[] ains, long aout, byte[] params, int[] limits) {
        if (!mEnableMultiInput) {
            android.util.Log.e(android.support.v8.renderscript.RenderScript.LOG_TAG, "Multi-input kernels are not supported, please change targetSdkVersion to >= 23");
            throw new android.support.v8.renderscript.RSRuntimeException("Multi-input kernels are not supported before API 23)");
        }
        validate();
        rsnScriptForEach(mContext, id, slot, ains, aout, params, limits);
    }

    native void rsnScriptReduce(long con, long id, int slot, long[] ains, long aout, int[] limits);

    synchronized void nScriptReduce(long id, int slot, long[] ains, long aout, int[] limits) {
        validate();
        rsnScriptReduce(mContext, id, slot, ains, aout, limits);
    }

    native void rsnScriptInvokeV(long con, long id, int slot, byte[] params, boolean mUseInc);

    synchronized void nScriptInvokeV(long id, int slot, byte[] params, boolean mUseInc) {
        validate();
        long curCon = mContext;
        if (mUseInc) {
            curCon = mIncCon;
        }
        rsnScriptInvokeV(curCon, id, slot, params, mUseInc);
    }

    native void rsnScriptSetVarI(long con, long id, int slot, int val, boolean mUseInc);

    synchronized void nScriptSetVarI(long id, int slot, int val, boolean mUseInc) {
        validate();
        long curCon = mContext;
        if (mUseInc) {
            curCon = mIncCon;
        }
        rsnScriptSetVarI(curCon, id, slot, val, mUseInc);
    }

    native void rsnScriptSetVarJ(long con, long id, int slot, long val, boolean mUseInc);

    synchronized void nScriptSetVarJ(long id, int slot, long val, boolean mUseInc) {
        validate();
        long curCon = mContext;
        if (mUseInc) {
            curCon = mIncCon;
        }
        rsnScriptSetVarJ(curCon, id, slot, val, mUseInc);
    }

    native void rsnScriptSetVarF(long con, long id, int slot, float val, boolean mUseInc);

    synchronized void nScriptSetVarF(long id, int slot, float val, boolean mUseInc) {
        validate();
        long curCon = mContext;
        if (mUseInc) {
            curCon = mIncCon;
        }
        rsnScriptSetVarF(curCon, id, slot, val, mUseInc);
    }

    native void rsnScriptSetVarD(long con, long id, int slot, double val, boolean mUseInc);

    synchronized void nScriptSetVarD(long id, int slot, double val, boolean mUseInc) {
        validate();
        long curCon = mContext;
        if (mUseInc) {
            curCon = mIncCon;
        }
        rsnScriptSetVarD(curCon, id, slot, val, mUseInc);
    }

    native void rsnScriptSetVarV(long con, long id, int slot, byte[] val, boolean mUseInc);

    synchronized void nScriptSetVarV(long id, int slot, byte[] val, boolean mUseInc) {
        validate();
        long curCon = mContext;
        if (mUseInc) {
            curCon = mIncCon;
        }
        rsnScriptSetVarV(curCon, id, slot, val, mUseInc);
    }

    native void rsnScriptSetVarVE(long con, long id, int slot, byte[] val, long e, int[] dims, boolean mUseInc);

    synchronized void nScriptSetVarVE(long id, int slot, byte[] val, long e, int[] dims, boolean mUseInc) {
        validate();
        long curCon = mContext;
        if (mUseInc) {
            curCon = mIncCon;
        }
        rsnScriptSetVarVE(curCon, id, slot, val, e, dims, mUseInc);
    }

    native void rsnScriptSetVarObj(long con, long id, int slot, long val, boolean mUseInc);

    synchronized void nScriptSetVarObj(long id, int slot, long val, boolean mUseInc) {
        validate();
        long curCon = mContext;
        if (mUseInc) {
            curCon = mIncCon;
        }
        rsnScriptSetVarObj(curCon, id, slot, val, mUseInc);
    }

    native long rsnScriptCCreate(long con, java.lang.String resName, java.lang.String cacheDir, byte[] script, int length);

    synchronized long nScriptCCreate(java.lang.String resName, java.lang.String cacheDir, byte[] script, int length) {
        validate();
        return rsnScriptCCreate(mContext, resName, cacheDir, script, length);
    }

    native long rsnScriptIntrinsicCreate(long con, int id, long eid, boolean mUseInc);

    synchronized long nScriptIntrinsicCreate(int id, long eid, boolean mUseInc) {
        validate();
        if (mUseInc) {
            if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP) {
                android.util.Log.e(android.support.v8.renderscript.RenderScript.LOG_TAG, "Incremental Intrinsics are not supported, please change targetSdkVersion to >= 21");
                throw new android.support.v8.renderscript.RSRuntimeException("Incremental Intrinsics are not supported before Lollipop (API 21)");
            }
            if (!mIncLoaded) {
                try {
                    java.lang.System.loadLibrary("RSSupport");
                } catch (java.lang.UnsatisfiedLinkError e) {
                    android.util.Log.e(android.support.v8.renderscript.RenderScript.LOG_TAG, "Error loading RS Compat library for Incremental Intrinsic Support: " + e);
                    throw new android.support.v8.renderscript.RSRuntimeException("Error loading RS Compat library for Incremental Intrinsic Support: " + e);
                }
                if (!nIncLoadSO(android.support.v8.renderscript.RenderScript.SUPPORT_LIB_API, mNativeLibDir + "/libRSSupport.so")) {
                    throw new android.support.v8.renderscript.RSRuntimeException("Error loading libRSSupport library for Incremental Intrinsic Support");
                }
                mIncLoaded = true;
            }
            if (mIncCon == 0) {
                // Create a dummy compat context (synchronous).
                long device = nIncDeviceCreate();
                mIncCon = nIncContextCreate(device, 0, 0, 0);
            }
            return rsnScriptIntrinsicCreate(mIncCon, id, eid, mUseInc);
        } else {
            return rsnScriptIntrinsicCreate(mContext, id, eid, mUseInc);
        }
    }

    native long rsnScriptKernelIDCreate(long con, long sid, int slot, int sig, boolean mUseInc);

    synchronized long nScriptKernelIDCreate(long sid, int slot, int sig, boolean mUseInc) {
        validate();
        long curCon = mContext;
        if (mUseInc) {
            curCon = mIncCon;
        }
        return rsnScriptKernelIDCreate(curCon, sid, slot, sig, mUseInc);
    }

    native long rsnScriptInvokeIDCreate(long con, long sid, int slot);

    synchronized long nScriptInvokeIDCreate(long sid, int slot) {
        validate();
        return rsnScriptInvokeIDCreate(mContext, sid, slot);
    }

    native long rsnScriptFieldIDCreate(long con, long sid, int slot, boolean mUseInc);

    synchronized long nScriptFieldIDCreate(long sid, int slot, boolean mUseInc) {
        validate();
        long curCon = mContext;
        if (mUseInc) {
            curCon = mIncCon;
        }
        return rsnScriptFieldIDCreate(curCon, sid, slot, mUseInc);
    }

    native long rsnScriptGroupCreate(long con, long[] kernels, long[] src, long[] dstk, long[] dstf, long[] types);

    synchronized long nScriptGroupCreate(long[] kernels, long[] src, long[] dstk, long[] dstf, long[] types) {
        validate();
        return rsnScriptGroupCreate(mContext, kernels, src, dstk, dstf, types);
    }

    native void rsnScriptGroupSetInput(long con, long group, long kernel, long alloc);

    synchronized void nScriptGroupSetInput(long group, long kernel, long alloc) {
        validate();
        rsnScriptGroupSetInput(mContext, group, kernel, alloc);
    }

    native void rsnScriptGroupSetOutput(long con, long group, long kernel, long alloc);

    synchronized void nScriptGroupSetOutput(long group, long kernel, long alloc) {
        validate();
        rsnScriptGroupSetOutput(mContext, group, kernel, alloc);
    }

    native void rsnScriptGroupExecute(long con, long group);

    synchronized void nScriptGroupExecute(long group) {
        validate();
        rsnScriptGroupExecute(mContext, group);
    }

    native long rsnSamplerCreate(long con, int magFilter, int minFilter, int wrapS, int wrapT, int wrapR, float aniso);

    synchronized long nSamplerCreate(int magFilter, int minFilter, int wrapS, int wrapT, int wrapR, float aniso) {
        validate();
        return rsnSamplerCreate(mContext, magFilter, minFilter, wrapS, wrapT, wrapR, aniso);
    }

    // entry points for ScriptGroup2
    native long rsnClosureCreate(long con, long kernelID, long returnValue, long[] fieldIDs, long[] values, int[] sizes, long[] depClosures, long[] depFieldIDs);

    synchronized long nClosureCreate(long kernelID, long returnValue, long[] fieldIDs, long[] values, int[] sizes, long[] depClosures, long[] depFieldIDs) {
        validate();
        long c = rsnClosureCreate(mContext, kernelID, returnValue, fieldIDs, values, sizes, depClosures, depFieldIDs);
        if (c == 0) {
            throw new android.support.v8.renderscript.RSRuntimeException("Failed creating closure.");
        }
        return c;
    }

    native long rsnInvokeClosureCreate(long con, long invokeID, byte[] params, long[] fieldIDs, long[] values, int[] sizes);

    synchronized long nInvokeClosureCreate(long invokeID, byte[] params, long[] fieldIDs, long[] values, int[] sizes) {
        validate();
        long c = rsnInvokeClosureCreate(mContext, invokeID, params, fieldIDs, values, sizes);
        if (c == 0) {
            throw new android.support.v8.renderscript.RSRuntimeException("Failed creating closure.");
        }
        return c;
    }

    native void rsnClosureSetArg(long con, long closureID, int index, long value, int size);

    synchronized void nClosureSetArg(long closureID, int index, long value, int size) {
        validate();
        rsnClosureSetArg(mContext, closureID, index, value, size);
    }

    native void rsnClosureSetGlobal(long con, long closureID, long fieldID, long value, int size);

    // Does this have to be synchronized?
    synchronized void nClosureSetGlobal(long closureID, long fieldID, long value, int size) {
        validate();// TODO: is this necessary?

        rsnClosureSetGlobal(mContext, closureID, fieldID, value, size);
    }

    native long rsnScriptGroup2Create(long con, java.lang.String name, java.lang.String cachePath, long[] closures);

    synchronized long nScriptGroup2Create(java.lang.String name, java.lang.String cachePath, long[] closures) {
        validate();
        return rsnScriptGroup2Create(mContext, name, cachePath, closures);
    }

    native void rsnScriptGroup2Execute(long con, long groupID);

    synchronized void nScriptGroup2Execute(long groupID) {
        validate();
        rsnScriptGroup2Execute(mContext, groupID);
    }

    native void rsnScriptIntrinsicBLAS_Single(long con, long incCon, long id, int func, int TransA, int TransB, int Side, int Uplo, int Diag, int M, int N, int K, float alpha, long A, long B, float beta, long C, int incX, int incY, int KL, int KU, boolean mUseInc);

    synchronized void nScriptIntrinsicBLAS_Single(long id, int func, int TransA, int TransB, int Side, int Uplo, int Diag, int M, int N, int K, float alpha, long A, long B, float beta, long C, int incX, int incY, int KL, int KU, boolean mUseInc) {
        validate();
        rsnScriptIntrinsicBLAS_Single(mContext, mIncCon, id, func, TransA, TransB, Side, Uplo, Diag, M, N, K, alpha, A, B, beta, C, incX, incY, KL, KU, mUseInc);
    }

    native void rsnScriptIntrinsicBLAS_Double(long con, long incCon, long id, int func, int TransA, int TransB, int Side, int Uplo, int Diag, int M, int N, int K, double alpha, long A, long B, double beta, long C, int incX, int incY, int KL, int KU, boolean mUseInc);

    synchronized void nScriptIntrinsicBLAS_Double(long id, int func, int TransA, int TransB, int Side, int Uplo, int Diag, int M, int N, int K, double alpha, long A, long B, double beta, long C, int incX, int incY, int KL, int KU, boolean mUseInc) {
        validate();
        rsnScriptIntrinsicBLAS_Double(mContext, mIncCon, id, func, TransA, TransB, Side, Uplo, Diag, M, N, K, alpha, A, B, beta, C, incX, incY, KL, KU, mUseInc);
    }

    native void rsnScriptIntrinsicBLAS_Complex(long con, long incCon, long id, int func, int TransA, int TransB, int Side, int Uplo, int Diag, int M, int N, int K, float alphaX, float alphaY, long A, long B, float betaX, float betaY, long C, int incX, int incY, int KL, int KU, boolean mUseInc);

    synchronized void nScriptIntrinsicBLAS_Complex(long id, int func, int TransA, int TransB, int Side, int Uplo, int Diag, int M, int N, int K, float alphaX, float alphaY, long A, long B, float betaX, float betaY, long C, int incX, int incY, int KL, int KU, boolean mUseInc) {
        validate();
        rsnScriptIntrinsicBLAS_Complex(mContext, mIncCon, id, func, TransA, TransB, Side, Uplo, Diag, M, N, K, alphaX, alphaY, A, B, betaX, betaY, C, incX, incY, KL, KU, mUseInc);
    }

    native void rsnScriptIntrinsicBLAS_Z(long con, long incCon, long id, int func, int TransA, int TransB, int Side, int Uplo, int Diag, int M, int N, int K, double alphaX, double alphaY, long A, long B, double betaX, double betaY, long C, int incX, int incY, int KL, int KU, boolean mUseInc);

    synchronized void nScriptIntrinsicBLAS_Z(long id, int func, int TransA, int TransB, int Side, int Uplo, int Diag, int M, int N, int K, double alphaX, double alphaY, long A, long B, double betaX, double betaY, long C, int incX, int incY, int KL, int KU, boolean mUseInc) {
        validate();
        rsnScriptIntrinsicBLAS_Z(mContext, mIncCon, id, func, TransA, TransB, Side, Uplo, Diag, M, N, K, alphaX, alphaY, A, B, betaX, betaY, C, incX, incY, KL, KU, mUseInc);
    }

    native void rsnScriptIntrinsicBLAS_BNNM(long con, long incCon, long id, int M, int N, int K, long A, int a_offset, long B, int b_offset, long C, int c_offset, int c_mult_int, boolean mUseInc);

    synchronized void nScriptIntrinsicBLAS_BNNM(long id, int M, int N, int K, long A, int a_offset, long B, int b_offset, long C, int c_offset, int c_mult_int, boolean mUseInc) {
        validate();
        rsnScriptIntrinsicBLAS_BNNM(mContext, mIncCon, id, M, N, K, A, a_offset, B, b_offset, C, c_offset, c_mult_int, mUseInc);
    }

    // Additional Entry points For inc libRSSupport
    native boolean nIncLoadSO(int deviceApi, java.lang.String libPath);

    native long nIncDeviceCreate();

    native void nIncDeviceDestroy(long dev);

    // Methods below are wrapped to protect the non-threadsafe
    // lockless fifo.
    native long rsnIncContextCreate(long dev, int ver, int sdkVer, int contextType);

    synchronized long nIncContextCreate(long dev, int ver, int sdkVer, int contextType) {
        return rsnIncContextCreate(dev, ver, sdkVer, contextType);
    }

    native void rsnIncContextDestroy(long con);

    synchronized void nIncContextDestroy() {
        validate();
        // take teardown lock
        // teardown lock can only be taken when no objects are being destroyed
        java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock wlock = mRWLock.writeLock();
        wlock.lock();
        long curCon = mIncCon;
        // context is considered dead as of this point
        mIncCon = 0;
        wlock.unlock();
        rsnIncContextDestroy(curCon);
    }

    native void rsnIncContextFinish(long con);

    synchronized void nIncContextFinish() {
        validate();
        rsnIncContextFinish(mIncCon);
    }

    native void rsnIncObjDestroy(long con, long id);

    void nIncObjDestroy(long id) {
        // There is a race condition here.  The calling code may be run
        // by the gc while teardown is occuring.  This protects againts
        // deleting dead objects.
        if (mIncCon != 0) {
            rsnIncObjDestroy(mIncCon, id);
        }
    }

    native long rsnIncElementCreate(long con, long type, int kind, boolean norm, int vecSize);

    synchronized long nIncElementCreate(long type, int kind, boolean norm, int vecSize) {
        validate();
        return rsnIncElementCreate(mIncCon, type, kind, norm, vecSize);
    }

    native long rsnIncTypeCreate(long con, long eid, int x, int y, int z, boolean mips, boolean faces, int yuv);

    synchronized long nIncTypeCreate(long eid, int x, int y, int z, boolean mips, boolean faces, int yuv) {
        validate();
        return rsnIncTypeCreate(mIncCon, eid, x, y, z, mips, faces, yuv);
    }

    native long rsnIncAllocationCreateTyped(long con, long incCon, long alloc, long type, int xBytesSize);

    synchronized long nIncAllocationCreateTyped(long alloc, long type, int xBytesSize) {
        validate();
        return rsnIncAllocationCreateTyped(mContext, mIncCon, alloc, type, xBytesSize);
    }

    long mContext;

    private boolean mDestroyed = false;

    // Dummy device & context for Inc Support Lib
    long mIncCon;

    // indicator of whether inc support lib has been loaded or not.
    boolean mIncLoaded;

    java.util.concurrent.locks.ReentrantReadWriteLock mRWLock;

    @java.lang.SuppressWarnings({ "FieldCanBeLocal" })
    android.support.v8.renderscript.RenderScript.MessageThread mMessageThread;

    android.support.v8.renderscript.Element mElement_U8;

    android.support.v8.renderscript.Element mElement_I8;

    android.support.v8.renderscript.Element mElement_U16;

    android.support.v8.renderscript.Element mElement_I16;

    android.support.v8.renderscript.Element mElement_U32;

    android.support.v8.renderscript.Element mElement_I32;

    android.support.v8.renderscript.Element mElement_U64;

    android.support.v8.renderscript.Element mElement_I64;

    android.support.v8.renderscript.Element mElement_F32;

    android.support.v8.renderscript.Element mElement_F64;

    android.support.v8.renderscript.Element mElement_BOOLEAN;

    android.support.v8.renderscript.Element mElement_ELEMENT;

    android.support.v8.renderscript.Element mElement_TYPE;

    android.support.v8.renderscript.Element mElement_ALLOCATION;

    android.support.v8.renderscript.Element mElement_SAMPLER;

    android.support.v8.renderscript.Element mElement_SCRIPT;

    android.support.v8.renderscript.Element mElement_A_8;

    android.support.v8.renderscript.Element mElement_RGB_565;

    android.support.v8.renderscript.Element mElement_RGB_888;

    android.support.v8.renderscript.Element mElement_RGBA_5551;

    android.support.v8.renderscript.Element mElement_RGBA_4444;

    android.support.v8.renderscript.Element mElement_RGBA_8888;

    android.support.v8.renderscript.Element mElement_FLOAT_2;

    android.support.v8.renderscript.Element mElement_FLOAT_3;

    android.support.v8.renderscript.Element mElement_FLOAT_4;

    android.support.v8.renderscript.Element mElement_DOUBLE_2;

    android.support.v8.renderscript.Element mElement_DOUBLE_3;

    android.support.v8.renderscript.Element mElement_DOUBLE_4;

    android.support.v8.renderscript.Element mElement_UCHAR_2;

    android.support.v8.renderscript.Element mElement_UCHAR_3;

    android.support.v8.renderscript.Element mElement_UCHAR_4;

    android.support.v8.renderscript.Element mElement_CHAR_2;

    android.support.v8.renderscript.Element mElement_CHAR_3;

    android.support.v8.renderscript.Element mElement_CHAR_4;

    android.support.v8.renderscript.Element mElement_USHORT_2;

    android.support.v8.renderscript.Element mElement_USHORT_3;

    android.support.v8.renderscript.Element mElement_USHORT_4;

    android.support.v8.renderscript.Element mElement_SHORT_2;

    android.support.v8.renderscript.Element mElement_SHORT_3;

    android.support.v8.renderscript.Element mElement_SHORT_4;

    android.support.v8.renderscript.Element mElement_UINT_2;

    android.support.v8.renderscript.Element mElement_UINT_3;

    android.support.v8.renderscript.Element mElement_UINT_4;

    android.support.v8.renderscript.Element mElement_INT_2;

    android.support.v8.renderscript.Element mElement_INT_3;

    android.support.v8.renderscript.Element mElement_INT_4;

    android.support.v8.renderscript.Element mElement_ULONG_2;

    android.support.v8.renderscript.Element mElement_ULONG_3;

    android.support.v8.renderscript.Element mElement_ULONG_4;

    android.support.v8.renderscript.Element mElement_LONG_2;

    android.support.v8.renderscript.Element mElement_LONG_3;

    android.support.v8.renderscript.Element mElement_LONG_4;

    android.support.v8.renderscript.Element mElement_MATRIX_4X4;

    android.support.v8.renderscript.Element mElement_MATRIX_3X3;

    android.support.v8.renderscript.Element mElement_MATRIX_2X2;

    android.support.v8.renderscript.Sampler mSampler_CLAMP_NEAREST;

    android.support.v8.renderscript.Sampler mSampler_CLAMP_LINEAR;

    android.support.v8.renderscript.Sampler mSampler_CLAMP_LINEAR_MIP_LINEAR;

    android.support.v8.renderscript.Sampler mSampler_WRAP_NEAREST;

    android.support.v8.renderscript.Sampler mSampler_WRAP_LINEAR;

    android.support.v8.renderscript.Sampler mSampler_WRAP_LINEAR_MIP_LINEAR;

    android.support.v8.renderscript.Sampler mSampler_MIRRORED_REPEAT_NEAREST;

    android.support.v8.renderscript.Sampler mSampler_MIRRORED_REPEAT_LINEAR;

    android.support.v8.renderscript.Sampler mSampler_MIRRORED_REPEAT_LINEAR_MIP_LINEAR;

    // /////////////////////////////////////////////////////////////////////////////////
    // 
    /**
     * The base class from which an application should derive in order
     * to receive RS messages from scripts. When a script calls {@code rsSendToClient}, the data fields will be filled, and the run
     * method will be called on a separate thread.  This will occur
     * some time after {@code rsSendToClient} completes in the script,
     * as {@code rsSendToClient} is asynchronous. Message handlers are
     * not guaranteed to have completed when {@link android.support.v8.renderscript.RenderScript#finish} returns.
     */
    public static class RSMessageHandler implements java.lang.Runnable {
        protected int[] mData;

        protected int mID;

        protected int mLength;

        public void run() {
        }
    }

    /**
     * If an application is expecting messages, it should set this
     * field to an instance of {@link RSMessageHandler}.  This
     * instance will receive all the user messages sent from {@code sendToClient} by scripts from this context.
     */
    android.support.v8.renderscript.RenderScript.RSMessageHandler mMessageCallback = null;

    public void setMessageHandler(android.support.v8.renderscript.RenderScript.RSMessageHandler msg) {
        mMessageCallback = msg;
    }

    public android.support.v8.renderscript.RenderScript.RSMessageHandler getMessageHandler() {
        return mMessageCallback;
    }

    /**
     * Place a message into the message queue to be sent back to the message
     * handler once all previous commands have been executed.
     *
     * @param id
     * 		
     * @param data
     * 		
     */
    public void sendMessage(int id, int[] data) {
        nContextSendMessage(id, data);
    }

    /**
     * The runtime error handler base class.  An application should derive from this class
     * if it wishes to install an error handler.  When errors occur at runtime,
     * the fields in this class will be filled, and the run method will be called.
     */
    public static class RSErrorHandler implements java.lang.Runnable {
        protected java.lang.String mErrorMessage;

        protected int mErrorNum;

        public void run() {
        }
    }

    /**
     * Application Error handler.  All runtime errors will be dispatched to the
     * instance of RSAsyncError set here.  If this field is null a
     * {@link RSRuntimeException} will instead be thrown with details about the error.
     * This will cause program termaination.
     */
    android.support.v8.renderscript.RenderScript.RSErrorHandler mErrorCallback = null;

    public void setErrorHandler(android.support.v8.renderscript.RenderScript.RSErrorHandler msg) {
        mErrorCallback = msg;
    }

    public android.support.v8.renderscript.RenderScript.RSErrorHandler getErrorHandler() {
        return mErrorCallback;
    }

    /**
     * RenderScript worker thread priority enumeration.  The default value is
     * NORMAL.  Applications wishing to do background processing should set
     * their priority to LOW to avoid starving forground processes.
     */
    public enum Priority {

        LOW(android.os.Process.THREAD_PRIORITY_BACKGROUND + (5 * android.os.Process.THREAD_PRIORITY_LESS_FAVORABLE)),
        NORMAL(android.os.Process.THREAD_PRIORITY_DISPLAY);
        int mID;

        Priority(int id) {
            mID = id;
        }
    }

    void validateObject(android.support.v8.renderscript.BaseObj o) {
        if (o != null) {
            if (o.mRS != this) {
                throw new android.support.v8.renderscript.RSIllegalArgumentException("Attempting to use an object across contexts.");
            }
        }
    }

    void validate() {
        if (mContext == 0) {
            throw new android.support.v8.renderscript.RSInvalidStateException("Calling RS with no Context active.");
        }
    }

    /**
     * check if IO support lib is available.
     */
    boolean usingIO() {
        return android.support.v8.renderscript.RenderScript.useIOlib;
    }

    /**
     * Change the priority of the worker threads for this context.
     *
     * @param p
     * 		New priority to be set.
     */
    public void setPriority(android.support.v8.renderscript.RenderScript.Priority p) {
        validate();
        nContextSetPriority(p.mID);
    }

    static class MessageThread extends java.lang.Thread {
        android.support.v8.renderscript.RenderScript mRS;

        boolean mRun = true;

        int[] mAuxData = new int[2];

        static final int RS_MESSAGE_TO_CLIENT_NONE = 0;

        static final int RS_MESSAGE_TO_CLIENT_EXCEPTION = 1;

        static final int RS_MESSAGE_TO_CLIENT_RESIZE = 2;

        static final int RS_MESSAGE_TO_CLIENT_ERROR = 3;

        static final int RS_MESSAGE_TO_CLIENT_USER = 4;

        static final int RS_ERROR_FATAL_UNKNOWN = 0x1000;

        MessageThread(android.support.v8.renderscript.RenderScript rs) {
            super("RSMessageThread");
            mRS = rs;
        }

        public void run() {
            // This function is a temporary solution.  The final solution will
            // used typed allocations where the message id is the type indicator.
            int[] rbuf = new int[16];
            mRS.nContextInitToClient(mRS.mContext);
            while (mRun) {
                rbuf[0] = 0;
                int msg = mRS.nContextPeekMessage(mRS.mContext, mAuxData);
                int size = mAuxData[1];
                int subID = mAuxData[0];
                if (msg == android.support.v8.renderscript.RenderScript.MessageThread.RS_MESSAGE_TO_CLIENT_USER) {
                    if ((size >> 2) >= rbuf.length) {
                        rbuf = new int[(size + 3) >> 2];
                    }
                    if (mRS.nContextGetUserMessage(mRS.mContext, rbuf) != android.support.v8.renderscript.RenderScript.MessageThread.RS_MESSAGE_TO_CLIENT_USER) {
                        throw new android.support.v8.renderscript.RSDriverException("Error processing message from RenderScript.");
                    }
                    if (mRS.mMessageCallback != null) {
                        mRS.mMessageCallback.mData = rbuf;
                        mRS.mMessageCallback.mID = subID;
                        mRS.mMessageCallback.mLength = size;
                        mRS.mMessageCallback.run();
                    } else {
                        throw new android.support.v8.renderscript.RSInvalidStateException("Received a message from the script with no message handler installed.");
                    }
                    continue;
                }
                if (msg == android.support.v8.renderscript.RenderScript.MessageThread.RS_MESSAGE_TO_CLIENT_ERROR) {
                    java.lang.String e = mRS.nContextGetErrorMessage(mRS.mContext);
                    if (subID >= android.support.v8.renderscript.RenderScript.MessageThread.RS_ERROR_FATAL_UNKNOWN) {
                        throw new android.support.v8.renderscript.RSRuntimeException((("Fatal error " + subID) + ", details: ") + e);
                    }
                    if (mRS.mErrorCallback != null) {
                        mRS.mErrorCallback.mErrorMessage = e;
                        mRS.mErrorCallback.mErrorNum = subID;
                        mRS.mErrorCallback.run();
                    } else {
                        android.util.Log.e(android.support.v8.renderscript.RenderScript.LOG_TAG, "non fatal RS error, " + e);
                        // Do not throw here. In these cases, we do not have
                        // a fatal error.
                    }
                    continue;
                }
                // 2: teardown.
                // But we want to avoid starving other threads during
                // teardown by yielding until the next line in the destructor
                // can execute to set mRun = false
                try {
                    java.lang.Thread.sleep(1, 0);
                } catch (java.lang.InterruptedException e) {
                }
            } 
            // Log.d(LOG_TAG, "MessageThread exiting.");
        }
    }

    RenderScript(android.content.Context ctx) {
        mContextType = android.support.v8.renderscript.RenderScript.ContextType.NORMAL;
        if (ctx != null) {
            mApplicationContext = ctx.getApplicationContext();
            // Only set mNativeLibDir for API 9+.
            mNativeLibDir = mApplicationContext.getApplicationInfo().nativeLibraryDir;
        }
        mIncCon = 0;
        mIncLoaded = false;
        mRWLock = new java.util.concurrent.locks.ReentrantReadWriteLock();
    }

    /**
     * Gets the application context associated with the RenderScript context.
     *
     * @return The application context.
     */
    public final android.content.Context getApplicationContext() {
        return mApplicationContext;
    }

    /**
     * Create a RenderScript context.
     *
     * @param ctx
     * 		The context.
     * @return RenderScript
     */
    private static android.support.v8.renderscript.RenderScript internalCreate(android.content.Context ctx, int sdkVersion, android.support.v8.renderscript.RenderScript.ContextType ct, int flags) {
        android.support.v8.renderscript.RenderScript rs = new android.support.v8.renderscript.RenderScript(ctx);
        if (android.support.v8.renderscript.RenderScript.sSdkVersion == (-1)) {
            android.support.v8.renderscript.RenderScript.sSdkVersion = sdkVersion;
        } else
            if (android.support.v8.renderscript.RenderScript.sSdkVersion != sdkVersion) {
                throw new android.support.v8.renderscript.RSRuntimeException("Can't have two contexts with different SDK versions in support lib");
            }

        android.support.v8.renderscript.RenderScript.useNative = android.support.v8.renderscript.RenderScript.setupNative(android.support.v8.renderscript.RenderScript.sSdkVersion, ctx);
        synchronized(android.support.v8.renderscript.RenderScript.lock) {
            if (android.support.v8.renderscript.RenderScript.sInitialized == false) {
                try {
                    java.lang.Class<?> vm_runtime = java.lang.Class.forName("dalvik.system.VMRuntime");
                    java.lang.reflect.Method get_runtime = vm_runtime.getDeclaredMethod("getRuntime");
                    android.support.v8.renderscript.RenderScript.sRuntime = get_runtime.invoke(null);
                    android.support.v8.renderscript.RenderScript.registerNativeAllocation = vm_runtime.getDeclaredMethod("registerNativeAllocation", java.lang.Integer.TYPE);
                    android.support.v8.renderscript.RenderScript.registerNativeFree = vm_runtime.getDeclaredMethod("registerNativeFree", java.lang.Integer.TYPE);
                    android.support.v8.renderscript.RenderScript.sUseGCHooks = true;
                } catch (java.lang.Exception e) {
                    android.util.Log.e(android.support.v8.renderscript.RenderScript.LOG_TAG, "No GC methods");
                    android.support.v8.renderscript.RenderScript.sUseGCHooks = false;
                }
                try {
                    // For API 9 - 22, always use the absolute path of librsjni.so
                    // http://b/25226912
                    if ((android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.M) && (rs.mNativeLibDir != null)) {
                        java.lang.System.load(rs.mNativeLibDir + "/librsjni.so");
                    } else {
                        java.lang.System.loadLibrary("rsjni");
                    }
                    android.support.v8.renderscript.RenderScript.sInitialized = true;
                    android.support.v8.renderscript.RenderScript.sPointerSize = android.support.v8.renderscript.RenderScript.rsnSystemGetPointerSize();
                } catch (java.lang.UnsatisfiedLinkError e) {
                    android.util.Log.e(android.support.v8.renderscript.RenderScript.LOG_TAG, "Error loading RS jni library: " + e);
                    throw new android.support.v8.renderscript.RSRuntimeException((("Error loading RS jni library: " + e) + " Support lib API: ") + android.support.v8.renderscript.RenderScript.SUPPORT_LIB_VERSION);
                }
            }
        }
        if (android.support.v8.renderscript.RenderScript.useNative) {
            android.util.Log.v(android.support.v8.renderscript.RenderScript.LOG_TAG, "RS native mode");
        } else {
            android.util.Log.v(android.support.v8.renderscript.RenderScript.LOG_TAG, "RS compat mode");
        }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            android.support.v8.renderscript.RenderScript.useIOlib = true;
        }
        // The target API level used to init dispatchTable.
        int dispatchAPI = sdkVersion;
        if (sdkVersion < android.os.Build.VERSION.SDK_INT) {
            // If the device API is higher than target API level, init dispatch table based on device API.
            dispatchAPI = android.os.Build.VERSION.SDK_INT;
        }
        java.lang.String rssupportPath = null;
        // For API 9 - 22, always use the absolute path of libRSSupport.so
        // http://b/25226912
        if ((android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.M) && (rs.mNativeLibDir != null)) {
            rssupportPath = rs.mNativeLibDir + "/libRSSupport.so";
        }
        if (!rs.nLoadSO(android.support.v8.renderscript.RenderScript.useNative, dispatchAPI, rssupportPath)) {
            if (android.support.v8.renderscript.RenderScript.useNative) {
                android.util.Log.v(android.support.v8.renderscript.RenderScript.LOG_TAG, "Unable to load libRS.so, falling back to compat mode");
                android.support.v8.renderscript.RenderScript.useNative = false;
            }
            try {
                if ((android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.M) && (rs.mNativeLibDir != null)) {
                    java.lang.System.load(rssupportPath);
                } else {
                    java.lang.System.loadLibrary("RSSupport");
                }
            } catch (java.lang.UnsatisfiedLinkError e) {
                android.util.Log.e(android.support.v8.renderscript.RenderScript.LOG_TAG, (("Error loading RS Compat library: " + e) + " Support lib version: ") + android.support.v8.renderscript.RenderScript.SUPPORT_LIB_VERSION);
                throw new android.support.v8.renderscript.RSRuntimeException((("Error loading RS Compat library: " + e) + " Support lib version: ") + android.support.v8.renderscript.RenderScript.SUPPORT_LIB_VERSION);
            }
            if (!rs.nLoadSO(false, dispatchAPI, rssupportPath)) {
                android.util.Log.e(android.support.v8.renderscript.RenderScript.LOG_TAG, "Error loading RS Compat library: nLoadSO() failed; Support lib version: " + android.support.v8.renderscript.RenderScript.SUPPORT_LIB_VERSION);
                throw new android.support.v8.renderscript.RSRuntimeException("Error loading libRSSupport library, Support lib version: " + android.support.v8.renderscript.RenderScript.SUPPORT_LIB_VERSION);
            }
        }
        if (android.support.v8.renderscript.RenderScript.useIOlib) {
            try {
                java.lang.System.loadLibrary("RSSupportIO");
            } catch (java.lang.UnsatisfiedLinkError e) {
                android.support.v8.renderscript.RenderScript.useIOlib = false;
            }
            if ((!android.support.v8.renderscript.RenderScript.useIOlib) || (!rs.nLoadIOSO())) {
                android.util.Log.v(android.support.v8.renderscript.RenderScript.LOG_TAG, "Unable to load libRSSupportIO.so, USAGE_IO not supported");
                android.support.v8.renderscript.RenderScript.useIOlib = false;
            }
        }
        // For old APIs with dlopen bug, need to load blas lib in Java first.
        // Only try load to blasV8 when the desired API level includes IntrinsicBLAS.
        if (dispatchAPI >= 23) {
            // Enable multi-input kernels only when diapatchAPI is M+.
            rs.mEnableMultiInput = true;
            try {
                java.lang.System.loadLibrary("blasV8");
            } catch (java.lang.UnsatisfiedLinkError e) {
                android.util.Log.v(android.support.v8.renderscript.RenderScript.LOG_TAG, "Unable to load BLAS lib, ONLY BNNM will be supported: " + e);
            }
        }
        long device = rs.nDeviceCreate();
        rs.mContext = rs.nContextCreate(device, 0, sdkVersion, ct.mID, rs.mNativeLibDir);
        rs.mContextType = ct;
        rs.mContextFlags = flags;
        rs.mContextSdkVersion = sdkVersion;
        rs.mDispatchAPILevel = dispatchAPI;
        if (rs.mContext == 0) {
            throw new android.support.v8.renderscript.RSDriverException("Failed to create RS context.");
        }
        rs.mMessageThread = new android.support.v8.renderscript.RenderScript.MessageThread(rs);
        rs.mMessageThread.start();
        return rs;
    }

    /**
     * Create a RenderScript context.
     *
     * See documentation for @create for details
     *
     * @param ctx
     * 		The context.
     * @return RenderScript
     */
    public static android.support.v8.renderscript.RenderScript create(android.content.Context ctx) {
        return android.support.v8.renderscript.RenderScript.create(ctx, android.support.v8.renderscript.RenderScript.ContextType.NORMAL);
    }

    /**
     * calls create(ctx, ct, CREATE_FLAG_NONE)
     *
     * See documentation for @create for details
     *
     * @param ctx
     * 		The context.
     * @param ct
     * 		The type of context to be created.
     * @return RenderScript
     */
    public static android.support.v8.renderscript.RenderScript create(android.content.Context ctx, android.support.v8.renderscript.RenderScript.ContextType ct) {
        return android.support.v8.renderscript.RenderScript.create(ctx, ct, android.support.v8.renderscript.RenderScript.CREATE_FLAG_NONE);
    }

    /**
     * Gets or creates a RenderScript context of the specified type.
     *
     * The returned context will be cached for future reuse within
     * the process. When an application is finished using
     * RenderScript it should call releaseAllContexts()
     *
     * A process context is a context designed for easy creation and
     * lifecycle management.  Multiple calls to this function will
     * return the same object provided they are called with the same
     * options.  This allows it to be used any time a RenderScript
     * context is needed.
     *
     * @param ctx
     * 		The context.
     * @param ct
     * 		The type of context to be created.
     * @param flags
     * 		The OR of the CREATE_FLAG_* options desired
     * @return RenderScript
     */
    public static android.support.v8.renderscript.RenderScript create(android.content.Context ctx, android.support.v8.renderscript.RenderScript.ContextType ct, int flags) {
        int v = ctx.getApplicationInfo().targetSdkVersion;
        return android.support.v8.renderscript.RenderScript.create(ctx, v, ct, flags);
    }

    /**
     * calls create(ctx, sdkVersion, ContextType.NORMAL, CREATE_FLAG_NONE)
     *
     * Used by the RenderScriptThunker to maintain backward compatibility.
     *
     * @unknown 
     * @param ctx
     * 		The context.
     * @param sdkVersion
     * 		The target SDK Version.
     * @return RenderScript
     */
    public static android.support.v8.renderscript.RenderScript create(android.content.Context ctx, int sdkVersion) {
        return android.support.v8.renderscript.RenderScript.create(ctx, sdkVersion, android.support.v8.renderscript.RenderScript.ContextType.NORMAL, android.support.v8.renderscript.RenderScript.CREATE_FLAG_NONE);
    }

    /**
     * calls create(ctx, sdkVersion, ct, CREATE_FLAG_NONE)
     * Create a RenderScript context.
     *
     * @unknown 
     * @param ctx
     * 		The context.
     * @return RenderScript
     */
    public static android.support.v8.renderscript.RenderScript create(android.content.Context ctx, int sdkVersion, android.support.v8.renderscript.RenderScript.ContextType ct) {
        return android.support.v8.renderscript.RenderScript.create(ctx, sdkVersion, ct, android.support.v8.renderscript.RenderScript.CREATE_FLAG_NONE);
    }

    /**
     * Gets or creates a RenderScript context of the specified type.
     *
     * @param ctx
     * 		The context.
     * @param ct
     * 		The type of context to be created.
     * @param sdkVersion
     * 		The target SDK Version.
     * @param flags
     * 		The OR of the CREATE_FLAG_* options desired
     * @return RenderScript
     */
    public static android.support.v8.renderscript.RenderScript create(android.content.Context ctx, int sdkVersion, android.support.v8.renderscript.RenderScript.ContextType ct, int flags) {
        synchronized(android.support.v8.renderscript.RenderScript.mProcessContextList) {
            for (android.support.v8.renderscript.RenderScript prs : android.support.v8.renderscript.RenderScript.mProcessContextList) {
                if (((prs.mContextType == ct) && (prs.mContextFlags == flags)) && (prs.mContextSdkVersion == sdkVersion)) {
                    return prs;
                }
            }
            android.support.v8.renderscript.RenderScript prs = android.support.v8.renderscript.RenderScript.internalCreate(ctx, sdkVersion, ct, flags);
            prs.mIsProcessContext = true;
            android.support.v8.renderscript.RenderScript.mProcessContextList.add(prs);
            return prs;
        }
    }

    /**
     * Releases all the process contexts.  This is the same as
     * calling .destroy() on each unique context retreived with
     * create(...). If no contexts have been created this
     * function does nothing.
     *
     * Typically you call this when your application is losing focus
     * and will not be using a context for some time.
     *
     * This has no effect on a context created with
     * createMultiContext()
     */
    public static void releaseAllContexts() {
        java.util.ArrayList<android.support.v8.renderscript.RenderScript> oldList;
        synchronized(android.support.v8.renderscript.RenderScript.mProcessContextList) {
            oldList = android.support.v8.renderscript.RenderScript.mProcessContextList;
            android.support.v8.renderscript.RenderScript.mProcessContextList = new java.util.ArrayList<android.support.v8.renderscript.RenderScript>();
        }
        for (android.support.v8.renderscript.RenderScript prs : oldList) {
            prs.mIsProcessContext = false;
            prs.destroy();
        }
        oldList.clear();
    }

    /**
     * Create a RenderScript context.
     *
     * This is an advanced function intended for applications which
     * need to create more than one RenderScript context to be used
     * at the same time.
     *
     * If you need a single context please use create()
     *
     * @param ctx
     * 		The context.
     * @return RenderScript
     */
    public static android.support.v8.renderscript.RenderScript createMultiContext(android.content.Context ctx, android.support.v8.renderscript.RenderScript.ContextType ct, int flags, int API_number) {
        return android.support.v8.renderscript.RenderScript.internalCreate(ctx, API_number, ct, flags);
    }

    /**
     * Print the currently available debugging information about the state of
     * the RS context to the log.
     */
    public void contextDump() {
        validate();
        nContextDump(0);
    }

    /**
     * Wait for any pending asynchronous opeations (such as copies to a RS
     * allocation or RS script executions) to complete.
     */
    public void finish() {
        nContextFinish();
    }

    private void helpDestroy() {
        boolean shouldDestroy = false;
        synchronized(this) {
            if (!mDestroyed) {
                shouldDestroy = true;
                mDestroyed = true;
            }
        }
        if (shouldDestroy) {
            nContextFinish();
            if (mIncCon != 0) {
                nIncContextFinish();
                nIncContextDestroy();
                mIncCon = 0;
            }
            nContextDeinitToClient(mContext);
            mMessageThread.mRun = false;
            // Interrupt mMessageThread so it gets to see immediately that mRun is false
            // and exit rightaway.
            mMessageThread.interrupt();
            // Wait for mMessageThread to join.  Try in a loop, in case this thread gets interrupted
            // during the wait.  If interrupted, set the "interrupted" status of the current thread.
            boolean hasJoined = false;
            boolean interrupted = false;
            while (!hasJoined) {
                try {
                    mMessageThread.join();
                    hasJoined = true;
                } catch (java.lang.InterruptedException e) {
                    interrupted = true;
                }
            } 
            if (interrupted) {
                android.util.Log.v(android.support.v8.renderscript.RenderScript.LOG_TAG, "Interrupted during wait for MessageThread to join");
                java.lang.Thread.currentThread().interrupt();
            }
            nContextDestroy();
        }
    }

    @java.lang.Override
    protected void finalize() throws java.lang.Throwable {
        helpDestroy();
        super.finalize();
    }

    /**
     * Destroys this RenderScript context.  Once this function is called,
     * using this context or any objects belonging to this context is
     * illegal.
     *
     * This function is a NOP if the context was created
     * with create().  Please use releaseAllContexts() to clean up
     * contexts created with the create function.
     */
    public void destroy() {
        if (mIsProcessContext) {
            // users cannot destroy a process context
            return;
        }
        validate();
        helpDestroy();
    }

    boolean isAlive() {
        return mContext != 0;
    }

    long safeID(android.support.v8.renderscript.BaseObj o) {
        if (o != null) {
            return o.getID(this);
        }
        return 0;
    }
}

