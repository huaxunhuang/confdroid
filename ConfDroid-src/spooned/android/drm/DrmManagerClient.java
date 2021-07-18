/**
 * Copyright (C) 2010 The Android Open Source Project
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
package android.drm;


/**
 * The main programming interface for the DRM framework. An application must instantiate this class
 * to access DRM agents through the DRM framework.
 */
public class DrmManagerClient implements java.lang.AutoCloseable {
    /**
     * Indicates that a request was successful or that no error occurred.
     */
    public static final int ERROR_NONE = 0;

    /**
     * Indicates that an error occurred and the reason is not known.
     */
    public static final int ERROR_UNKNOWN = -2000;

    /**
     * {@hide }
     */
    public static final int INVALID_SESSION = -1;

    android.os.HandlerThread mInfoThread;

    android.os.HandlerThread mEventThread;

    private static final java.lang.String TAG = "DrmManagerClient";

    private final java.util.concurrent.atomic.AtomicBoolean mClosed = new java.util.concurrent.atomic.AtomicBoolean();

    private final dalvik.system.CloseGuard mCloseGuard = dalvik.system.CloseGuard.get();

    static {
        // Load the respective library
        java.lang.System.loadLibrary("drmframework_jni");
    }

    /**
     * Interface definition for a callback that receives status messages and warnings
     * during registration and rights acquisition.
     */
    public interface OnInfoListener {
        /**
         * Called when the DRM framework sends status or warning information during registration
         * and rights acquisition.
         *
         * @param client
         * 		The <code>DrmManagerClient</code> instance.
         * @param event
         * 		The {@link DrmInfoEvent} instance that wraps the status information or
         * 		warnings.
         */
        public void onInfo(android.drm.DrmManagerClient client, android.drm.DrmInfoEvent event);
    }

    /**
     * Interface definition for a callback that receives information
     * about DRM processing events.
     */
    public interface OnEventListener {
        /**
         * Called when the DRM framework sends information about a DRM processing request.
         *
         * @param client
         * 		The <code>DrmManagerClient</code> instance.
         * @param event
         * 		The {@link DrmEvent} instance that wraps the information being
         * 		conveyed, such as the information type and message.
         */
        public void onEvent(android.drm.DrmManagerClient client, android.drm.DrmEvent event);
    }

    /**
     * Interface definition for a callback that receives information about DRM framework errors.
     */
    public interface OnErrorListener {
        /**
         * Called when the DRM framework sends error information.
         *
         * @param client
         * 		The <code>DrmManagerClient</code> instance.
         * @param event
         * 		The {@link DrmErrorEvent} instance that wraps the error type and message.
         */
        public void onError(android.drm.DrmManagerClient client, android.drm.DrmErrorEvent event);
    }

    private static final int ACTION_REMOVE_ALL_RIGHTS = 1001;

    private static final int ACTION_PROCESS_DRM_INFO = 1002;

    private int mUniqueId;

    private long mNativeContext;

    private android.content.Context mContext;

    private android.drm.DrmManagerClient.InfoHandler mInfoHandler;

    private android.drm.DrmManagerClient.EventHandler mEventHandler;

    private android.drm.DrmManagerClient.OnInfoListener mOnInfoListener;

    private android.drm.DrmManagerClient.OnEventListener mOnEventListener;

    private android.drm.DrmManagerClient.OnErrorListener mOnErrorListener;

    private class EventHandler extends android.os.Handler {
        public EventHandler(android.os.Looper looper) {
            super(looper);
        }

        public void handleMessage(android.os.Message msg) {
            android.drm.DrmEvent event = null;
            android.drm.DrmErrorEvent error = null;
            java.util.HashMap<java.lang.String, java.lang.Object> attributes = new java.util.HashMap<java.lang.String, java.lang.Object>();
            switch (msg.what) {
                case android.drm.DrmManagerClient.ACTION_PROCESS_DRM_INFO :
                    {
                        final android.drm.DrmInfo drmInfo = ((android.drm.DrmInfo) (msg.obj));
                        android.drm.DrmInfoStatus status = _processDrmInfo(mUniqueId, drmInfo);
                        attributes.put(android.drm.DrmEvent.DRM_INFO_STATUS_OBJECT, status);
                        attributes.put(android.drm.DrmEvent.DRM_INFO_OBJECT, drmInfo);
                        if ((null != status) && (android.drm.DrmInfoStatus.STATUS_OK == status.statusCode)) {
                            event = new android.drm.DrmEvent(mUniqueId, getEventType(status.infoType), null, attributes);
                        } else {
                            int infoType = (null != status) ? status.infoType : drmInfo.getInfoType();
                            error = new android.drm.DrmErrorEvent(mUniqueId, getErrorType(infoType), null, attributes);
                        }
                        break;
                    }
                case android.drm.DrmManagerClient.ACTION_REMOVE_ALL_RIGHTS :
                    {
                        if (android.drm.DrmManagerClient.ERROR_NONE == _removeAllRights(mUniqueId)) {
                            event = new android.drm.DrmEvent(mUniqueId, android.drm.DrmEvent.TYPE_ALL_RIGHTS_REMOVED, null);
                        } else {
                            error = new android.drm.DrmErrorEvent(mUniqueId, android.drm.DrmErrorEvent.TYPE_REMOVE_ALL_RIGHTS_FAILED, null);
                        }
                        break;
                    }
                default :
                    android.util.Log.e(android.drm.DrmManagerClient.TAG, "Unknown message type " + msg.what);
                    return;
            }
            if ((null != mOnEventListener) && (null != event)) {
                mOnEventListener.onEvent(android.drm.DrmManagerClient.this, event);
            }
            if ((null != mOnErrorListener) && (null != error)) {
                mOnErrorListener.onError(android.drm.DrmManagerClient.this, error);
            }
        }
    }

    /**
     * {@hide }
     */
    public static void notify(java.lang.Object thisReference, int uniqueId, int infoType, java.lang.String message) {
        android.drm.DrmManagerClient instance = ((android.drm.DrmManagerClient) (((java.lang.ref.WeakReference) (thisReference)).get()));
        if ((null != instance) && (null != instance.mInfoHandler)) {
            android.os.Message m = instance.mInfoHandler.obtainMessage(android.drm.DrmManagerClient.InfoHandler.INFO_EVENT_TYPE, uniqueId, infoType, message);
            instance.mInfoHandler.sendMessage(m);
        }
    }

    private class InfoHandler extends android.os.Handler {
        public static final int INFO_EVENT_TYPE = 1;

        public InfoHandler(android.os.Looper looper) {
            super(looper);
        }

        public void handleMessage(android.os.Message msg) {
            android.drm.DrmInfoEvent info = null;
            android.drm.DrmErrorEvent error = null;
            switch (msg.what) {
                case android.drm.DrmManagerClient.InfoHandler.INFO_EVENT_TYPE :
                    int uniqueId = msg.arg1;
                    int infoType = msg.arg2;
                    java.lang.String message = msg.obj.toString();
                    switch (infoType) {
                        case android.drm.DrmInfoEvent.TYPE_REMOVE_RIGHTS :
                            {
                                try {
                                    android.drm.DrmUtils.removeFile(message);
                                } catch (java.io.IOException e) {
                                    e.printStackTrace();
                                }
                                info = new android.drm.DrmInfoEvent(uniqueId, infoType, message);
                                break;
                            }
                        case android.drm.DrmInfoEvent.TYPE_ALREADY_REGISTERED_BY_ANOTHER_ACCOUNT :
                        case android.drm.DrmInfoEvent.TYPE_RIGHTS_INSTALLED :
                        case android.drm.DrmInfoEvent.TYPE_WAIT_FOR_RIGHTS :
                        case android.drm.DrmInfoEvent.TYPE_ACCOUNT_ALREADY_REGISTERED :
                        case android.drm.DrmInfoEvent.TYPE_RIGHTS_REMOVED :
                            {
                                info = new android.drm.DrmInfoEvent(uniqueId, infoType, message);
                                break;
                            }
                        default :
                            error = new android.drm.DrmErrorEvent(uniqueId, infoType, message);
                            break;
                    }
                    if ((null != mOnInfoListener) && (null != info)) {
                        mOnInfoListener.onInfo(android.drm.DrmManagerClient.this, info);
                    }
                    if ((null != mOnErrorListener) && (null != error)) {
                        mOnErrorListener.onError(android.drm.DrmManagerClient.this, error);
                    }
                    return;
                default :
                    android.util.Log.e(android.drm.DrmManagerClient.TAG, "Unknown message type " + msg.what);
                    return;
            }
        }
    }

    /**
     * Creates a <code>DrmManagerClient</code>.
     *
     * @param context
     * 		Context of the caller.
     */
    public DrmManagerClient(android.content.Context context) {
        mContext = context;
        createEventThreads();
        // save the unique id
        mUniqueId = _initialize();
        mCloseGuard.open("release");
    }

    @java.lang.Override
    protected void finalize() throws java.lang.Throwable {
        try {
            mCloseGuard.warnIfOpen();
            close();
        } finally {
            super.finalize();
        }
    }

    /**
     * Releases resources associated with the current session of
     * DrmManagerClient. It is considered good practice to call this method when
     * the {@link DrmManagerClient} object is no longer needed in your
     * application. After this method is called, {@link DrmManagerClient} is no
     * longer usable since it has lost all of its required resource.
     */
    @java.lang.Override
    public void close() {
        mCloseGuard.close();
        if (mClosed.compareAndSet(false, true)) {
            if (mEventHandler != null) {
                mEventThread.quit();
                mEventThread = null;
            }
            if (mInfoHandler != null) {
                mInfoThread.quit();
                mInfoThread = null;
            }
            mEventHandler = null;
            mInfoHandler = null;
            mOnEventListener = null;
            mOnInfoListener = null;
            mOnErrorListener = null;
            _release(mUniqueId);
        }
    }

    /**
     *
     *
     * @deprecated replaced by {@link #close()}.
     */
    @java.lang.Deprecated
    public void release() {
        close();
    }

    /**
     * Registers an {@link DrmManagerClient.OnInfoListener} callback, which is invoked when the
     * DRM framework sends status or warning information during registration or rights acquisition.
     *
     * @param infoListener
     * 		Interface definition for the callback.
     */
    public synchronized void setOnInfoListener(android.drm.DrmManagerClient.OnInfoListener infoListener) {
        mOnInfoListener = infoListener;
        if (null != infoListener) {
            createListeners();
        }
    }

    /**
     * Registers an {@link DrmManagerClient.OnEventListener} callback, which is invoked when the
     * DRM framework sends information about DRM processing.
     *
     * @param eventListener
     * 		Interface definition for the callback.
     */
    public synchronized void setOnEventListener(android.drm.DrmManagerClient.OnEventListener eventListener) {
        mOnEventListener = eventListener;
        if (null != eventListener) {
            createListeners();
        }
    }

    /**
     * Registers an {@link DrmManagerClient.OnErrorListener} callback, which is invoked when
     * the DRM framework sends error information.
     *
     * @param errorListener
     * 		Interface definition for the callback.
     */
    public synchronized void setOnErrorListener(android.drm.DrmManagerClient.OnErrorListener errorListener) {
        mOnErrorListener = errorListener;
        if (null != errorListener) {
            createListeners();
        }
    }

    /**
     * Retrieves information about all the DRM plug-ins (agents) that are registered with
     * the DRM framework.
     *
     * @return A <code>String</code> array of DRM plug-in descriptions.
     */
    public java.lang.String[] getAvailableDrmEngines() {
        android.drm.DrmSupportInfo[] supportInfos = _getAllSupportInfo(mUniqueId);
        java.util.ArrayList<java.lang.String> descriptions = new java.util.ArrayList<java.lang.String>();
        for (int i = 0; i < supportInfos.length; i++) {
            descriptions.add(supportInfos[i].getDescriprition());
        }
        java.lang.String[] drmEngines = new java.lang.String[descriptions.size()];
        return descriptions.toArray(drmEngines);
    }

    /**
     * Retrieves constraint information for rights-protected content.
     *
     * @param path
     * 		Path to the content from which you are retrieving DRM constraints.
     * @param action
     * 		Action defined in {@link DrmStore.Action}.
     * @return A {@link android.content.ContentValues} instance that contains
    key-value pairs representing the constraints. Null in case of failure.
    The keys are defined in {@link DrmStore.ConstraintsColumns}.
     */
    public android.content.ContentValues getConstraints(java.lang.String path, int action) {
        if (((null == path) || path.equals("")) || (!android.drm.DrmStore.Action.isValid(action))) {
            throw new java.lang.IllegalArgumentException("Given usage or path is invalid/null");
        }
        return _getConstraints(mUniqueId, path, action);
    }

    /**
     * Retrieves metadata information for rights-protected content.
     *
     * @param path
     * 		Path to the content from which you are retrieving metadata information.
     * @return A {@link android.content.ContentValues} instance that contains
    key-value pairs representing the metadata. Null in case of failure.
     */
    public android.content.ContentValues getMetadata(java.lang.String path) {
        if ((null == path) || path.equals("")) {
            throw new java.lang.IllegalArgumentException("Given path is invalid/null");
        }
        return _getMetadata(mUniqueId, path);
    }

    /**
     * Retrieves constraint information for rights-protected content.
     *
     * @param uri
     * 		URI for the content from which you are retrieving DRM constraints.
     * @param action
     * 		Action defined in {@link DrmStore.Action}.
     * @return A {@link android.content.ContentValues} instance that contains
    key-value pairs representing the constraints. Null in case of failure.
     */
    public android.content.ContentValues getConstraints(android.net.Uri uri, int action) {
        if ((null == uri) || (android.net.Uri.EMPTY == uri)) {
            throw new java.lang.IllegalArgumentException("Uri should be non null");
        }
        return getConstraints(convertUriToPath(uri), action);
    }

    /**
     * Retrieves metadata information for rights-protected content.
     *
     * @param uri
     * 		URI for the content from which you are retrieving metadata information.
     * @return A {@link android.content.ContentValues} instance that contains
    key-value pairs representing the constraints. Null in case of failure.
     */
    public android.content.ContentValues getMetadata(android.net.Uri uri) {
        if ((null == uri) || (android.net.Uri.EMPTY == uri)) {
            throw new java.lang.IllegalArgumentException("Uri should be non null");
        }
        return getMetadata(convertUriToPath(uri));
    }

    /**
     * Saves rights to a specified path and associates that path with the content path.
     *
     * <p class="note"><strong>Note:</strong> For OMA or WM-DRM, <code>rightsPath</code> and
     * <code>contentPath</code> can be null.</p>
     *
     * @param drmRights
     * 		The {@link DrmRights} to be saved.
     * @param rightsPath
     * 		File path where rights will be saved.
     * @param contentPath
     * 		File path where content is saved.
     * @return ERROR_NONE for success; ERROR_UNKNOWN for failure.
     * @throws IOException
     * 		If the call failed to save rights information at the given
     * 		<code>rightsPath</code>.
     */
    public int saveRights(android.drm.DrmRights drmRights, java.lang.String rightsPath, java.lang.String contentPath) throws java.io.IOException {
        if ((null == drmRights) || (!drmRights.isValid())) {
            throw new java.lang.IllegalArgumentException("Given drmRights or contentPath is not valid");
        }
        if ((null != rightsPath) && (!rightsPath.equals(""))) {
            android.drm.DrmUtils.writeToFile(rightsPath, drmRights.getData());
        }
        return _saveRights(mUniqueId, drmRights, rightsPath, contentPath);
    }

    /**
     * Installs a new DRM plug-in (agent) at runtime.
     *
     * @param engineFilePath
     * 		File path to the plug-in file to be installed.
     * 		
     * 		{@hide }
     */
    public void installDrmEngine(java.lang.String engineFilePath) {
        if ((null == engineFilePath) || engineFilePath.equals("")) {
            throw new java.lang.IllegalArgumentException(("Given engineFilePath: " + engineFilePath) + "is not valid");
        }
        _installDrmEngine(mUniqueId, engineFilePath);
    }

    /**
     * Checks whether the given MIME type or path can be handled.
     *
     * @param path
     * 		Path of the content to be handled.
     * @param mimeType
     * 		MIME type of the object to be handled.
     * @return True if the given MIME type or path can be handled; false if they cannot be handled.
     */
    public boolean canHandle(java.lang.String path, java.lang.String mimeType) {
        if (((null == path) || path.equals("")) && ((null == mimeType) || mimeType.equals(""))) {
            throw new java.lang.IllegalArgumentException("Path or the mimetype should be non null");
        }
        return _canHandle(mUniqueId, path, mimeType);
    }

    /**
     * Checks whether the given MIME type or URI can be handled.
     *
     * @param uri
     * 		URI for the content to be handled.
     * @param mimeType
     * 		MIME type of the object to be handled
     * @return True if the given MIME type or URI can be handled; false if they cannot be handled.
     */
    public boolean canHandle(android.net.Uri uri, java.lang.String mimeType) {
        if (((null == uri) || (android.net.Uri.EMPTY == uri)) && ((null == mimeType) || mimeType.equals(""))) {
            throw new java.lang.IllegalArgumentException("Uri or the mimetype should be non null");
        }
        return canHandle(convertUriToPath(uri), mimeType);
    }

    /**
     * Processes the given DRM information based on the information type.
     *
     * @param drmInfo
     * 		The {@link DrmInfo} to be processed.
     * @return ERROR_NONE for success; ERROR_UNKNOWN for failure.
     */
    public int processDrmInfo(android.drm.DrmInfo drmInfo) {
        if ((null == drmInfo) || (!drmInfo.isValid())) {
            throw new java.lang.IllegalArgumentException("Given drmInfo is invalid/null");
        }
        int result = android.drm.DrmManagerClient.ERROR_UNKNOWN;
        if (null != mEventHandler) {
            android.os.Message msg = mEventHandler.obtainMessage(android.drm.DrmManagerClient.ACTION_PROCESS_DRM_INFO, drmInfo);
            result = (mEventHandler.sendMessage(msg)) ? android.drm.DrmManagerClient.ERROR_NONE : result;
        }
        return result;
    }

    /**
     * Retrieves information for registering, unregistering, or acquiring rights.
     *
     * @param drmInfoRequest
     * 		The {@link DrmInfoRequest} that specifies the type of DRM
     * 		information being retrieved.
     * @return A {@link DrmInfo} instance.
     */
    public android.drm.DrmInfo acquireDrmInfo(android.drm.DrmInfoRequest drmInfoRequest) {
        if ((null == drmInfoRequest) || (!drmInfoRequest.isValid())) {
            throw new java.lang.IllegalArgumentException("Given drmInfoRequest is invalid/null");
        }
        return _acquireDrmInfo(mUniqueId, drmInfoRequest);
    }

    /**
     * Processes a given {@link DrmInfoRequest} and returns the rights information asynchronously.
     * <p>
     * This is a utility method that consists of an
     * {@link #acquireDrmInfo(DrmInfoRequest) acquireDrmInfo()} and a
     * {@link #processDrmInfo(DrmInfo) processDrmInfo()} method call. This utility method can be
     * used only if the selected DRM plug-in (agent) supports this sequence of calls. Some DRM
     * agents, such as OMA, do not support this utility method, in which case an application must
     * invoke {@link #acquireDrmInfo(DrmInfoRequest) acquireDrmInfo()} and
     * {@link #processDrmInfo(DrmInfo) processDrmInfo()} separately.
     *
     * @param drmInfoRequest
     * 		The {@link DrmInfoRequest} used to acquire the rights.
     * @return ERROR_NONE for success; ERROR_UNKNOWN for failure.
     */
    public int acquireRights(android.drm.DrmInfoRequest drmInfoRequest) {
        android.drm.DrmInfo drmInfo = acquireDrmInfo(drmInfoRequest);
        if (null == drmInfo) {
            return android.drm.DrmManagerClient.ERROR_UNKNOWN;
        }
        return processDrmInfo(drmInfo);
    }

    /**
     * Retrieves the type of rights-protected object (for example, content object, rights
     * object, and so on) using the specified path or MIME type. At least one parameter must
     * be specified to retrieve the DRM object type.
     *
     * @param path
     * 		Path to the content or null.
     * @param mimeType
     * 		MIME type of the content or null.
     * @return An <code>int</code> that corresponds to a {@link DrmStore.DrmObjectType}.
     */
    public int getDrmObjectType(java.lang.String path, java.lang.String mimeType) {
        if (((null == path) || path.equals("")) && ((null == mimeType) || mimeType.equals(""))) {
            throw new java.lang.IllegalArgumentException("Path or the mimetype should be non null");
        }
        return _getDrmObjectType(mUniqueId, path, mimeType);
    }

    /**
     * Retrieves the type of rights-protected object (for example, content object, rights
     * object, and so on) using the specified URI or MIME type. At least one parameter must
     * be specified to retrieve the DRM object type.
     *
     * @param uri
     * 		URI for the content or null.
     * @param mimeType
     * 		MIME type of the content or null.
     * @return An <code>int</code> that corresponds to a {@link DrmStore.DrmObjectType}.
     */
    public int getDrmObjectType(android.net.Uri uri, java.lang.String mimeType) {
        if (((null == uri) || (android.net.Uri.EMPTY == uri)) && ((null == mimeType) || mimeType.equals(""))) {
            throw new java.lang.IllegalArgumentException("Uri or the mimetype should be non null");
        }
        java.lang.String path = "";
        try {
            path = convertUriToPath(uri);
        } catch (java.lang.Exception e) {
            // Even uri is invalid the mimetype shall be valid, so allow to proceed further.
            android.util.Log.w(android.drm.DrmManagerClient.TAG, "Given Uri could not be found in media store");
        }
        return getDrmObjectType(path, mimeType);
    }

    /**
     * Retrieves the MIME type embedded in the original content.
     *
     * @param path
     * 		Path to the rights-protected content.
     * @return The MIME type of the original content, such as <code>video/mpeg</code>.
     */
    public java.lang.String getOriginalMimeType(java.lang.String path) {
        if ((null == path) || path.equals("")) {
            throw new java.lang.IllegalArgumentException("Given path should be non null");
        }
        java.lang.String mime = null;
        java.io.FileInputStream is = null;
        try {
            java.io.FileDescriptor fd = null;
            java.io.File file = new java.io.File(path);
            if (file.exists()) {
                is = new java.io.FileInputStream(file);
                fd = is.getFD();
            }
            mime = _getOriginalMimeType(mUniqueId, path, fd);
        } catch (java.io.IOException ioe) {
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (java.io.IOException e) {
                }
            }
        }
        return mime;
    }

    /**
     * Retrieves the MIME type embedded in the original content.
     *
     * @param uri
     * 		URI of the rights-protected content.
     * @return MIME type of the original content, such as <code>video/mpeg</code>.
     */
    public java.lang.String getOriginalMimeType(android.net.Uri uri) {
        if ((null == uri) || (android.net.Uri.EMPTY == uri)) {
            throw new java.lang.IllegalArgumentException("Given uri is not valid");
        }
        return getOriginalMimeType(convertUriToPath(uri));
    }

    /**
     * Checks whether the given content has valid rights.
     *
     * @param path
     * 		Path to the rights-protected content.
     * @return An <code>int</code> representing the {@link DrmStore.RightsStatus} of the content.
     */
    public int checkRightsStatus(java.lang.String path) {
        return checkRightsStatus(path, android.drm.DrmStore.Action.DEFAULT);
    }

    /**
     * Check whether the given content has valid rights.
     *
     * @param uri
     * 		URI of the rights-protected content.
     * @return An <code>int</code> representing the {@link DrmStore.RightsStatus} of the content.
     */
    public int checkRightsStatus(android.net.Uri uri) {
        if ((null == uri) || (android.net.Uri.EMPTY == uri)) {
            throw new java.lang.IllegalArgumentException("Given uri is not valid");
        }
        return checkRightsStatus(convertUriToPath(uri));
    }

    /**
     * Checks whether the given rights-protected content has valid rights for the specified
     * {@link DrmStore.Action}.
     *
     * @param path
     * 		Path to the rights-protected content.
     * @param action
     * 		The {@link DrmStore.Action} to perform.
     * @return An <code>int</code> representing the {@link DrmStore.RightsStatus} of the content.
     */
    public int checkRightsStatus(java.lang.String path, int action) {
        if (((null == path) || path.equals("")) || (!android.drm.DrmStore.Action.isValid(action))) {
            throw new java.lang.IllegalArgumentException("Given path or action is not valid");
        }
        return _checkRightsStatus(mUniqueId, path, action);
    }

    /**
     * Checks whether the given rights-protected content has valid rights for the specified
     * {@link DrmStore.Action}.
     *
     * @param uri
     * 		URI for the rights-protected content.
     * @param action
     * 		The {@link DrmStore.Action} to perform.
     * @return An <code>int</code> representing the {@link DrmStore.RightsStatus} of the content.
     */
    public int checkRightsStatus(android.net.Uri uri, int action) {
        if ((null == uri) || (android.net.Uri.EMPTY == uri)) {
            throw new java.lang.IllegalArgumentException("Given uri is not valid");
        }
        return checkRightsStatus(convertUriToPath(uri), action);
    }

    /**
     * Removes the rights associated with the given rights-protected content.
     *
     * @param path
     * 		Path to the rights-protected content.
     * @return ERROR_NONE for success; ERROR_UNKNOWN for failure.
     */
    public int removeRights(java.lang.String path) {
        if ((null == path) || path.equals("")) {
            throw new java.lang.IllegalArgumentException("Given path should be non null");
        }
        return _removeRights(mUniqueId, path);
    }

    /**
     * Removes the rights associated with the given rights-protected content.
     *
     * @param uri
     * 		URI for the rights-protected content.
     * @return ERROR_NONE for success; ERROR_UNKNOWN for failure.
     */
    public int removeRights(android.net.Uri uri) {
        if ((null == uri) || (android.net.Uri.EMPTY == uri)) {
            throw new java.lang.IllegalArgumentException("Given uri is not valid");
        }
        return removeRights(convertUriToPath(uri));
    }

    /**
     * Removes all the rights information of every DRM plug-in (agent) associated with
     * the DRM framework. Will be used during a master reset.
     *
     * @return ERROR_NONE for success; ERROR_UNKNOWN for failure.
     */
    public int removeAllRights() {
        int result = android.drm.DrmManagerClient.ERROR_UNKNOWN;
        if (null != mEventHandler) {
            android.os.Message msg = mEventHandler.obtainMessage(android.drm.DrmManagerClient.ACTION_REMOVE_ALL_RIGHTS);
            result = (mEventHandler.sendMessage(msg)) ? android.drm.DrmManagerClient.ERROR_NONE : result;
        }
        return result;
    }

    /**
     * Initiates a new conversion session. An application must initiate a conversion session
     * with this method each time it downloads a rights-protected file that needs to be converted.
     * <p>
     * This method applies only to forward-locking (copy protection) DRM schemes.
     *
     * @param mimeType
     * 		MIME type of the input data packet.
     * @return A convert ID that is used used to maintain the conversion session.
     */
    public int openConvertSession(java.lang.String mimeType) {
        if ((null == mimeType) || mimeType.equals("")) {
            throw new java.lang.IllegalArgumentException("Path or the mimeType should be non null");
        }
        return _openConvertSession(mUniqueId, mimeType);
    }

    /**
     * Converts the input data (content) that is part of a rights-protected file. The converted
     * data and status is returned in a {@link DrmConvertedStatus} object. This method should be
     * called each time there is a new block of data received by the application.
     *
     * @param convertId
     * 		Handle for the conversion session.
     * @param inputData
     * 		Input data that needs to be converted.
     * @return A {@link DrmConvertedStatus} object that contains the status of the data conversion,
    the converted data, and offset for the header and body signature. An application can
    ignore the offset because it is only relevant to the
    {@link #closeConvertSession closeConvertSession()} method.
     */
    public android.drm.DrmConvertedStatus convertData(int convertId, byte[] inputData) {
        if ((null == inputData) || (0 >= inputData.length)) {
            throw new java.lang.IllegalArgumentException("Given inputData should be non null");
        }
        return _convertData(mUniqueId, convertId, inputData);
    }

    /**
     * Informs the DRM plug-in (agent) that there is no more data to convert or that an error
     * has occurred. Upon successful conversion of the data, the DRM agent will provide an offset
     * value indicating where the header and body signature should be added. Appending the
     * signature is necessary to protect the integrity of the converted file.
     *
     * @param convertId
     * 		Handle for the conversion session.
     * @return A {@link DrmConvertedStatus} object that contains the status of the data conversion,
    the converted data, and the offset for the header and body signature.
     */
    public android.drm.DrmConvertedStatus closeConvertSession(int convertId) {
        return _closeConvertSession(mUniqueId, convertId);
    }

    private int getEventType(int infoType) {
        int eventType = -1;
        switch (infoType) {
            case android.drm.DrmInfoRequest.TYPE_REGISTRATION_INFO :
            case android.drm.DrmInfoRequest.TYPE_UNREGISTRATION_INFO :
            case android.drm.DrmInfoRequest.TYPE_RIGHTS_ACQUISITION_INFO :
                eventType = android.drm.DrmEvent.TYPE_DRM_INFO_PROCESSED;
                break;
        }
        return eventType;
    }

    private int getErrorType(int infoType) {
        int error = -1;
        switch (infoType) {
            case android.drm.DrmInfoRequest.TYPE_REGISTRATION_INFO :
            case android.drm.DrmInfoRequest.TYPE_UNREGISTRATION_INFO :
            case android.drm.DrmInfoRequest.TYPE_RIGHTS_ACQUISITION_INFO :
                error = android.drm.DrmErrorEvent.TYPE_PROCESS_DRM_INFO_FAILED;
                break;
        }
        return error;
    }

    /**
     * This method expects uri in the following format
     *     content://media/<table_name>/<row_index> (or)
     *     file://sdcard/test.mp4
     *     http://test.com/test.mp4
     *
     * Here <table_name> shall be "video" or "audio" or "images"
     * <row_index> the index of the content in given table
     */
    private java.lang.String convertUriToPath(android.net.Uri uri) {
        java.lang.String path = null;
        if (null != uri) {
            java.lang.String scheme = uri.getScheme();
            if (((null == scheme) || scheme.equals("")) || scheme.equals(android.content.ContentResolver.SCHEME_FILE)) {
                path = uri.getPath();
            } else
                if (scheme.equals("http")) {
                    path = uri.toString();
                } else
                    if (scheme.equals(android.content.ContentResolver.SCHEME_CONTENT)) {
                        java.lang.String[] projection = new java.lang.String[]{ android.provider.MediaStore.MediaColumns.DATA };
                        android.database.Cursor cursor = null;
                        try {
                            cursor = mContext.getContentResolver().query(uri, projection, null, null, null);
                            if (((null == cursor) || (0 == cursor.getCount())) || (!cursor.moveToFirst())) {
                                throw new java.lang.IllegalArgumentException("Given Uri could not be found" + " in media store");
                            }
                            int pathIndex = cursor.getColumnIndexOrThrow(android.provider.MediaStore.MediaColumns.DATA);
                            path = cursor.getString(pathIndex);
                        } catch (android.database.sqlite.SQLiteException e) {
                            throw new java.lang.IllegalArgumentException("Given Uri is not formatted in a way " + "so that it can be found in media store.");
                        } finally {
                            if (null != cursor) {
                                cursor.close();
                            }
                        }
                    } else {
                        throw new java.lang.IllegalArgumentException("Given Uri scheme is not supported");
                    }


        }
        return path;
    }

    // private native interfaces
    private native int _initialize();

    private native void _setListeners(int uniqueId, java.lang.Object weak_this);

    private native void _release(int uniqueId);

    private native void _installDrmEngine(int uniqueId, java.lang.String engineFilepath);

    private native android.content.ContentValues _getConstraints(int uniqueId, java.lang.String path, int usage);

    private native android.content.ContentValues _getMetadata(int uniqueId, java.lang.String path);

    private native boolean _canHandle(int uniqueId, java.lang.String path, java.lang.String mimeType);

    private native android.drm.DrmInfoStatus _processDrmInfo(int uniqueId, android.drm.DrmInfo drmInfo);

    private native android.drm.DrmInfo _acquireDrmInfo(int uniqueId, android.drm.DrmInfoRequest drmInfoRequest);

    private native int _saveRights(int uniqueId, android.drm.DrmRights drmRights, java.lang.String rightsPath, java.lang.String contentPath);

    private native int _getDrmObjectType(int uniqueId, java.lang.String path, java.lang.String mimeType);

    private native java.lang.String _getOriginalMimeType(int uniqueId, java.lang.String path, java.io.FileDescriptor fd);

    private native int _checkRightsStatus(int uniqueId, java.lang.String path, int action);

    private native int _removeRights(int uniqueId, java.lang.String path);

    private native int _removeAllRights(int uniqueId);

    private native int _openConvertSession(int uniqueId, java.lang.String mimeType);

    private native android.drm.DrmConvertedStatus _convertData(int uniqueId, int convertId, byte[] inputData);

    private native android.drm.DrmConvertedStatus _closeConvertSession(int uniqueId, int convertId);

    private native android.drm.DrmSupportInfo[] _getAllSupportInfo(int uniqueId);

    private void createEventThreads() {
        if ((mEventHandler == null) && (mInfoHandler == null)) {
            mInfoThread = new android.os.HandlerThread("DrmManagerClient.InfoHandler");
            mInfoThread.start();
            mInfoHandler = new android.drm.DrmManagerClient.InfoHandler(mInfoThread.getLooper());
            mEventThread = new android.os.HandlerThread("DrmManagerClient.EventHandler");
            mEventThread.start();
            mEventHandler = new android.drm.DrmManagerClient.EventHandler(mEventThread.getLooper());
        }
    }

    private void createListeners() {
        _setListeners(mUniqueId, new java.lang.ref.WeakReference<android.drm.DrmManagerClient>(this));
    }
}

