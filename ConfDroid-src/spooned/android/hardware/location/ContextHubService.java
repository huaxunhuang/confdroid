/**
 * Copyright (C) 2016 The Android Open Source Project
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
package android.hardware.location;


/**
 *
 *
 * @unknown 
 */
public class ContextHubService extends android.hardware.location.IContextHubService.Stub {
    public static final java.lang.String CONTEXTHUB_SERVICE = "contexthub_service";

    private static final java.lang.String TAG = "ContextHubService";

    private static final java.lang.String HARDWARE_PERMISSION = Manifest.permission.LOCATION_HARDWARE;

    private static final java.lang.String ENFORCE_HW_PERMISSION_MESSAGE = ("Permission '" + android.hardware.location.ContextHubService.HARDWARE_PERMISSION) + "' not granted to access ContextHub Hardware";

    public static final int ANY_HUB = -1;

    public static final int MSG_LOAD_NANO_APP = 3;

    public static final int MSG_UNLOAD_NANO_APP = 4;

    private static final java.lang.String PRE_LOADED_GENERIC_UNKNOWN = "Preloaded app, unknown";

    private static final java.lang.String PRE_LOADED_APP_NAME = android.hardware.location.ContextHubService.PRE_LOADED_GENERIC_UNKNOWN;

    private static final java.lang.String PRE_LOADED_APP_PUBLISHER = android.hardware.location.ContextHubService.PRE_LOADED_GENERIC_UNKNOWN;

    private static final int PRE_LOADED_APP_MEM_REQ = 0;

    private static final int MSG_HEADER_SIZE = 4;

    private static final int HEADER_FIELD_MSG_TYPE = 0;

    private static final int HEADER_FIELD_MSG_VERSION = 1;

    private static final int HEADER_FIELD_HUB_HANDLE = 2;

    private static final int HEADER_FIELD_APP_INSTANCE = 3;

    private static final int HEADER_FIELD_LOAD_APP_ID_LO = android.hardware.location.ContextHubService.MSG_HEADER_SIZE;

    private static final int HEADER_FIELD_LOAD_APP_ID_HI = android.hardware.location.ContextHubService.MSG_HEADER_SIZE + 1;

    private static final int MSG_LOAD_APP_HEADER_SIZE = android.hardware.location.ContextHubService.MSG_HEADER_SIZE + 2;

    private static final int OS_APP_INSTANCE = -1;

    private static final long APP_ID_ACTIVITY_RECOGNITION = 0x476f6f676c001000L;

    private final android.content.Context mContext;

    private final java.util.concurrent.ConcurrentHashMap<java.lang.Integer, android.hardware.location.NanoAppInstanceInfo> mNanoAppHash = new java.util.concurrent.ConcurrentHashMap<>();

    private final android.hardware.location.ContextHubInfo[] mContextHubInfo;

    private final android.os.RemoteCallbackList<android.hardware.location.IContextHubCallback> mCallbacksList = new android.os.RemoteCallbackList();

    private native int nativeSendMessage(int[] header, byte[] data);

    private native android.hardware.location.ContextHubInfo[] nativeInitialize();

    private final android.service.vr.IVrStateCallbacks mVrStateCallbacks = new android.service.vr.IVrStateCallbacks.Stub() {
        @java.lang.Override
        public void onVrStateChanged(boolean enabled) {
            for (android.hardware.location.NanoAppInstanceInfo app : mNanoAppHash.values()) {
                if (app.getAppId() == android.hardware.location.ContextHubService.APP_ID_ACTIVITY_RECOGNITION) {
                    sendVrStateChangeMessageToApp(app, enabled);
                    break;
                }
            }
        }
    };

    public ContextHubService(android.content.Context context) {
        mContext = context;
        mContextHubInfo = nativeInitialize();
        for (int i = 0; i < mContextHubInfo.length; i++) {
            android.util.Log.d(android.hardware.location.ContextHubService.TAG, (((("ContextHub[" + i) + "] id: ") + mContextHubInfo[i].getId()) + ", name:  ") + mContextHubInfo[i].getName());
        }
        if (context.getPackageManager().hasSystemFeature(android.content.pm.PackageManager.FEATURE_VR_MODE)) {
            android.service.vr.IVrManager vrManager = IVrManager.Stub.asInterface(android.os.ServiceManager.getService("vrmanager"));
            if (vrManager != null) {
                try {
                    vrManager.registerListener(mVrStateCallbacks);
                } catch (android.os.RemoteException e) {
                    android.util.Log.e(android.hardware.location.ContextHubService.TAG, "VR state listener registration failed", e);
                }
            }
        }
    }

    @java.lang.Override
    public int registerCallback(android.hardware.location.IContextHubCallback callback) throws android.os.RemoteException {
        checkPermissions();
        mCallbacksList.register(callback);
        return 0;
    }

    @java.lang.Override
    public int[] getContextHubHandles() throws android.os.RemoteException {
        checkPermissions();
        int[] returnArray = new int[mContextHubInfo.length];
        for (int i = 0; i < returnArray.length; ++i) {
            returnArray[i] = i;
            android.util.Log.d(android.hardware.location.ContextHubService.TAG, java.lang.String.format("Hub %s is mapped to %d", mContextHubInfo[i].getName(), returnArray[i]));
        }
        return returnArray;
    }

    @java.lang.Override
    public android.hardware.location.ContextHubInfo getContextHubInfo(int contextHubHandle) throws android.os.RemoteException {
        checkPermissions();
        if (!((contextHubHandle >= 0) && (contextHubHandle < mContextHubInfo.length))) {
            return null;// null means fail

        }
        return mContextHubInfo[contextHubHandle];
    }

    // TODO(b/30808791): Remove this when NanoApp's API is correctly treating
    // app IDs as 64-bits.
    private static long parseAppId(android.hardware.location.NanoApp app) {
        // NOTE: If this shifting seems odd (since it's actually "ONAN"), note
        // that it matches how this is defined in context_hub.h.
        final int HEADER_MAGIC = (((((int) ('N')) << 0) | (((int) ('A')) << 8)) | (((int) ('N')) << 16)) | (((int) ('O')) << 24);
        final int HEADER_MAGIC_OFFSET = 4;
        final int HEADER_APP_ID_OFFSET = 8;
        java.nio.ByteBuffer header = java.nio.ByteBuffer.wrap(app.getAppBinary()).order(java.nio.ByteOrder.LITTLE_ENDIAN);
        try {
            if (header.getInt(HEADER_MAGIC_OFFSET) == HEADER_MAGIC) {
                // This is a legitimate nanoapp header.  Let's grab the app ID.
                return header.getLong(HEADER_APP_ID_OFFSET);
            }
        } catch (java.lang.IndexOutOfBoundsException e) {
            // The header is undersized.  We'll fall through to our code
            // path below, which handles being unable to parse the header.
        }
        // We failed to parse the header.  Even through it's probably wrong,
        // let's give NanoApp's idea of our ID.  This is at least consistent.
        return app.getAppId();
    }

    @java.lang.Override
    public int loadNanoApp(int contextHubHandle, android.hardware.location.NanoApp app) throws android.os.RemoteException {
        checkPermissions();
        if (!((contextHubHandle >= 0) && (contextHubHandle < mContextHubInfo.length))) {
            android.util.Log.e(android.hardware.location.ContextHubService.TAG, "Invalid contextHubhandle " + contextHubHandle);
            return -1;
        }
        int[] msgHeader = new int[android.hardware.location.ContextHubService.MSG_LOAD_APP_HEADER_SIZE];
        msgHeader[android.hardware.location.ContextHubService.HEADER_FIELD_HUB_HANDLE] = contextHubHandle;
        msgHeader[android.hardware.location.ContextHubService.HEADER_FIELD_APP_INSTANCE] = android.hardware.location.ContextHubService.OS_APP_INSTANCE;
        msgHeader[android.hardware.location.ContextHubService.HEADER_FIELD_MSG_VERSION] = 0;
        msgHeader[android.hardware.location.ContextHubService.HEADER_FIELD_MSG_TYPE] = android.hardware.location.ContextHubService.MSG_LOAD_NANO_APP;
        long appId = app.getAppId();
        // TODO(b/30808791): Remove this hack when the NanoApp API is fixed,
        // and getAppId() returns a 'long' instead of an 'int'.
        if ((appId >> 32) != 0) {
            // We're unlikely to notice this warning, but at least
            // we can avoid running our hack logic.
            android.util.Log.w(android.hardware.location.ContextHubService.TAG, "Code has not been updated since API fix.");
        } else {
            appId = android.hardware.location.ContextHubService.parseAppId(app);
        }
        msgHeader[android.hardware.location.ContextHubService.HEADER_FIELD_LOAD_APP_ID_LO] = ((int) (appId & 0xffffffff));
        msgHeader[android.hardware.location.ContextHubService.HEADER_FIELD_LOAD_APP_ID_HI] = ((int) ((appId >> 32) & 0xffffffff));
        int errVal = nativeSendMessage(msgHeader, app.getAppBinary());
        if (errVal != 0) {
            android.util.Log.e(android.hardware.location.ContextHubService.TAG, "Send Message returns error" + contextHubHandle);
            return -1;
        }
        // Do not add an entry to mNanoAppInstance Hash yet. The HAL may reject the app
        return 0;
    }

    @java.lang.Override
    public int unloadNanoApp(int nanoAppInstanceHandle) throws android.os.RemoteException {
        checkPermissions();
        android.hardware.location.NanoAppInstanceInfo info = mNanoAppHash.get(nanoAppInstanceHandle);
        if (info == null) {
            return -1;// means failed

        }
        // Call Native interface here
        int[] msgHeader = new int[android.hardware.location.ContextHubService.MSG_HEADER_SIZE];
        msgHeader[android.hardware.location.ContextHubService.HEADER_FIELD_HUB_HANDLE] = android.hardware.location.ContextHubService.ANY_HUB;
        msgHeader[android.hardware.location.ContextHubService.HEADER_FIELD_APP_INSTANCE] = nanoAppInstanceHandle;
        msgHeader[android.hardware.location.ContextHubService.HEADER_FIELD_MSG_VERSION] = 0;
        msgHeader[android.hardware.location.ContextHubService.HEADER_FIELD_MSG_TYPE] = android.hardware.location.ContextHubService.MSG_UNLOAD_NANO_APP;
        byte[] msg = new byte[0];
        if (nativeSendMessage(msgHeader, msg) != 0) {
            return -1;
        }
        // Do not add an entry to mNanoAppInstance Hash yet. The HAL may reject the app
        return 0;
    }

    @java.lang.Override
    public android.hardware.location.NanoAppInstanceInfo getNanoAppInstanceInfo(int nanoAppInstanceHandle) throws android.os.RemoteException {
        checkPermissions();
        // This assumes that all the nanoAppInfo is current. This is reasonable
        // for the use cases for tightly controlled nanoApps.
        if (mNanoAppHash.containsKey(nanoAppInstanceHandle)) {
            return mNanoAppHash.get(nanoAppInstanceHandle);
        } else {
            return null;
        }
    }

    @java.lang.Override
    public int[] findNanoAppOnHub(int hubHandle, android.hardware.location.NanoAppFilter filter) throws android.os.RemoteException {
        checkPermissions();
        java.util.ArrayList<java.lang.Integer> foundInstances = new java.util.ArrayList<java.lang.Integer>();
        for (java.lang.Integer nanoAppInstance : mNanoAppHash.keySet()) {
            android.hardware.location.NanoAppInstanceInfo info = mNanoAppHash.get(nanoAppInstance);
            if (filter.testMatch(info)) {
                foundInstances.add(nanoAppInstance);
            }
        }
        int[] retArray = new int[foundInstances.size()];
        for (int i = 0; i < foundInstances.size(); i++) {
            retArray[i] = foundInstances.get(i).intValue();
        }
        return retArray;
    }

    @java.lang.Override
    public int sendMessage(int hubHandle, int nanoAppHandle, android.hardware.location.ContextHubMessage msg) throws android.os.RemoteException {
        checkPermissions();
        int[] msgHeader = new int[android.hardware.location.ContextHubService.MSG_HEADER_SIZE];
        msgHeader[android.hardware.location.ContextHubService.HEADER_FIELD_HUB_HANDLE] = hubHandle;
        msgHeader[android.hardware.location.ContextHubService.HEADER_FIELD_APP_INSTANCE] = nanoAppHandle;
        msgHeader[android.hardware.location.ContextHubService.HEADER_FIELD_MSG_VERSION] = msg.getVersion();
        msgHeader[android.hardware.location.ContextHubService.HEADER_FIELD_MSG_TYPE] = msg.getMsgType();
        return nativeSendMessage(msgHeader, msg.getData());
    }

    @java.lang.Override
    protected void dump(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args) {
        if (mContext.checkCallingOrSelfPermission("android.permission.DUMP") != android.content.pm.PackageManager.PERMISSION_GRANTED) {
            pw.println("Permission Denial: can't dump contexthub_service");
            return;
        }
        pw.println("Dumping ContextHub Service");
        pw.println("");
        // dump ContextHubInfo
        pw.println("=================== CONTEXT HUBS ====================");
        for (int i = 0; i < mContextHubInfo.length; i++) {
            pw.println((("Handle " + i) + " : ") + mContextHubInfo[i].toString());
        }
        pw.println("");
        pw.println("=================== NANOAPPS ====================");
        // Dump nanoAppHash
        for (java.lang.Integer nanoAppInstance : mNanoAppHash.keySet()) {
            pw.println((nanoAppInstance + " : ") + mNanoAppHash.get(nanoAppInstance).toString());
        }
        // dump eventLog
    }

    private void checkPermissions() {
        mContext.enforceCallingPermission(android.hardware.location.ContextHubService.HARDWARE_PERMISSION, android.hardware.location.ContextHubService.ENFORCE_HW_PERMISSION_MESSAGE);
    }

    private int onMessageReceipt(int[] header, byte[] data) {
        if (((header == null) || (data == null)) || (header.length < android.hardware.location.ContextHubService.MSG_HEADER_SIZE)) {
            return -1;
        }
        int callbacksCount = mCallbacksList.beginBroadcast();
        if (callbacksCount < 1) {
            android.util.Log.v(android.hardware.location.ContextHubService.TAG, "No message callbacks registered.");
            return 0;
        }
        android.hardware.location.ContextHubMessage msg = new android.hardware.location.ContextHubMessage(header[android.hardware.location.ContextHubService.HEADER_FIELD_MSG_TYPE], header[android.hardware.location.ContextHubService.HEADER_FIELD_MSG_VERSION], data);
        for (int i = 0; i < callbacksCount; ++i) {
            android.hardware.location.IContextHubCallback callback = mCallbacksList.getBroadcastItem(i);
            try {
                callback.onMessageReceipt(header[android.hardware.location.ContextHubService.HEADER_FIELD_HUB_HANDLE], header[android.hardware.location.ContextHubService.HEADER_FIELD_APP_INSTANCE], msg);
            } catch (android.os.RemoteException e) {
                android.util.Log.i(android.hardware.location.ContextHubService.TAG, ((("Exception (" + e) + ") calling remote callback (") + callback) + ").");
                continue;
            }
        }
        mCallbacksList.finishBroadcast();
        return 0;
    }

    private int addAppInstance(int hubHandle, int appInstanceHandle, long appId, int appVersion) {
        // App Id encodes vendor & version
        android.hardware.location.NanoAppInstanceInfo appInfo = new android.hardware.location.NanoAppInstanceInfo();
        appInfo.setAppId(appId);
        appInfo.setAppVersion(appVersion);
        appInfo.setName(android.hardware.location.ContextHubService.PRE_LOADED_APP_NAME);
        appInfo.setContexthubId(hubHandle);
        appInfo.setHandle(appInstanceHandle);
        appInfo.setPublisher(android.hardware.location.ContextHubService.PRE_LOADED_APP_PUBLISHER);
        appInfo.setNeededExecMemBytes(android.hardware.location.ContextHubService.PRE_LOADED_APP_MEM_REQ);
        appInfo.setNeededReadMemBytes(android.hardware.location.ContextHubService.PRE_LOADED_APP_MEM_REQ);
        appInfo.setNeededWriteMemBytes(android.hardware.location.ContextHubService.PRE_LOADED_APP_MEM_REQ);
        java.lang.String action;
        if (mNanoAppHash.containsKey(appInstanceHandle)) {
            action = "Updated";
        } else {
            action = "Added";
        }
        mNanoAppHash.put(appInstanceHandle, appInfo);
        android.util.Log.d(android.hardware.location.ContextHubService.TAG, (((((action + " app instance ") + appInstanceHandle) + " with id ") + appId) + " version ") + appVersion);
        return 0;
    }

    private int deleteAppInstance(int appInstanceHandle) {
        if (mNanoAppHash.remove(appInstanceHandle) == null) {
            return -1;
        }
        return 0;
    }

    private void sendVrStateChangeMessageToApp(android.hardware.location.NanoAppInstanceInfo app, boolean vrModeEnabled) {
        int[] msgHeader = new int[android.hardware.location.ContextHubService.MSG_HEADER_SIZE];
        msgHeader[android.hardware.location.ContextHubService.HEADER_FIELD_MSG_TYPE] = 0;
        msgHeader[android.hardware.location.ContextHubService.HEADER_FIELD_MSG_VERSION] = 0;
        msgHeader[android.hardware.location.ContextHubService.HEADER_FIELD_HUB_HANDLE] = android.hardware.location.ContextHubService.ANY_HUB;
        msgHeader[android.hardware.location.ContextHubService.HEADER_FIELD_APP_INSTANCE] = app.getHandle();
        byte[] data = new byte[1];
        data[0] = ((byte) ((vrModeEnabled) ? 1 : 0));
        int ret = nativeSendMessage(msgHeader, data);
        if (ret != 0) {
            android.util.Log.e(android.hardware.location.ContextHubService.TAG, ("Couldn't send VR state change notification (" + ret) + ")!");
        }
    }
}

