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
package android.support.v7.media;


/**
 * Maintains a connection to a particular media route provider service.
 */
final class RegisteredMediaRouteProvider extends android.support.v7.media.MediaRouteProvider implements android.content.ServiceConnection {
    static final java.lang.String TAG = "MediaRouteProviderProxy";// max. 23 chars


    static final boolean DEBUG = android.util.Log.isLoggable(android.support.v7.media.RegisteredMediaRouteProvider.TAG, android.util.Log.DEBUG);

    private final android.content.ComponentName mComponentName;

    final android.support.v7.media.RegisteredMediaRouteProvider.PrivateHandler mPrivateHandler;

    private final java.util.ArrayList<android.support.v7.media.RegisteredMediaRouteProvider.Controller> mControllers = new java.util.ArrayList<android.support.v7.media.RegisteredMediaRouteProvider.Controller>();

    private boolean mStarted;

    private boolean mBound;

    private android.support.v7.media.RegisteredMediaRouteProvider.Connection mActiveConnection;

    private boolean mConnectionReady;

    public RegisteredMediaRouteProvider(android.content.Context context, android.content.ComponentName componentName) {
        super(context, new android.support.v7.media.MediaRouteProvider.ProviderMetadata(componentName));
        mComponentName = componentName;
        mPrivateHandler = new android.support.v7.media.RegisteredMediaRouteProvider.PrivateHandler();
    }

    @java.lang.Override
    public android.support.v7.media.MediaRouteProvider.RouteController onCreateRouteController(@android.support.annotation.NonNull
    java.lang.String routeId) {
        if (routeId == null) {
            throw new java.lang.IllegalArgumentException("routeId cannot be null");
        }
        return createRouteController(routeId, null);
    }

    @java.lang.Override
    public android.support.v7.media.MediaRouteProvider.RouteController onCreateRouteController(@android.support.annotation.NonNull
    java.lang.String routeId, @android.support.annotation.NonNull
    java.lang.String routeGroupId) {
        if (routeId == null) {
            throw new java.lang.IllegalArgumentException("routeId cannot be null");
        }
        if (routeGroupId == null) {
            throw new java.lang.IllegalArgumentException("routeGroupId cannot be null");
        }
        return createRouteController(routeId, routeGroupId);
    }

    @java.lang.Override
    public void onDiscoveryRequestChanged(android.support.v7.media.MediaRouteDiscoveryRequest request) {
        if (mConnectionReady) {
            mActiveConnection.setDiscoveryRequest(request);
        }
        updateBinding();
    }

    @java.lang.Override
    public void onServiceConnected(android.content.ComponentName name, android.os.IBinder service) {
        if (android.support.v7.media.RegisteredMediaRouteProvider.DEBUG) {
            android.util.Log.d(android.support.v7.media.RegisteredMediaRouteProvider.TAG, this + ": Connected");
        }
        if (mBound) {
            disconnect();
            android.os.Messenger messenger = (service != null) ? new android.os.Messenger(service) : null;
            if (android.support.v7.media.MediaRouteProviderProtocol.isValidRemoteMessenger(messenger)) {
                android.support.v7.media.RegisteredMediaRouteProvider.Connection connection = new android.support.v7.media.RegisteredMediaRouteProvider.Connection(messenger);
                if (connection.register()) {
                    mActiveConnection = connection;
                } else {
                    if (android.support.v7.media.RegisteredMediaRouteProvider.DEBUG) {
                        android.util.Log.d(android.support.v7.media.RegisteredMediaRouteProvider.TAG, this + ": Registration failed");
                    }
                }
            } else {
                android.util.Log.e(android.support.v7.media.RegisteredMediaRouteProvider.TAG, this + ": Service returned invalid messenger binder");
            }
        }
    }

    @java.lang.Override
    public void onServiceDisconnected(android.content.ComponentName name) {
        if (android.support.v7.media.RegisteredMediaRouteProvider.DEBUG) {
            android.util.Log.d(android.support.v7.media.RegisteredMediaRouteProvider.TAG, this + ": Service disconnected");
        }
        disconnect();
    }

    @java.lang.Override
    public java.lang.String toString() {
        return "Service connection " + mComponentName.flattenToShortString();
    }

    public boolean hasComponentName(java.lang.String packageName, java.lang.String className) {
        return mComponentName.getPackageName().equals(packageName) && mComponentName.getClassName().equals(className);
    }

    public void start() {
        if (!mStarted) {
            if (android.support.v7.media.RegisteredMediaRouteProvider.DEBUG) {
                android.util.Log.d(android.support.v7.media.RegisteredMediaRouteProvider.TAG, this + ": Starting");
            }
            mStarted = true;
            updateBinding();
        }
    }

    public void stop() {
        if (mStarted) {
            if (android.support.v7.media.RegisteredMediaRouteProvider.DEBUG) {
                android.util.Log.d(android.support.v7.media.RegisteredMediaRouteProvider.TAG, this + ": Stopping");
            }
            mStarted = false;
            updateBinding();
        }
    }

    public void rebindIfDisconnected() {
        if ((mActiveConnection == null) && shouldBind()) {
            unbind();
            bind();
        }
    }

    private void updateBinding() {
        if (shouldBind()) {
            bind();
        } else {
            unbind();
        }
    }

    private boolean shouldBind() {
        if (mStarted) {
            // Bind whenever there is a discovery request.
            if (getDiscoveryRequest() != null) {
                return true;
            }
            // Bind whenever the application has an active route controller.
            // This means that one of this provider's routes is selected.
            if (!mControllers.isEmpty()) {
                return true;
            }
        }
        return false;
    }

    private void bind() {
        if (!mBound) {
            if (android.support.v7.media.RegisteredMediaRouteProvider.DEBUG) {
                android.util.Log.d(android.support.v7.media.RegisteredMediaRouteProvider.TAG, this + ": Binding");
            }
            android.content.Intent service = new android.content.Intent(android.support.v7.media.MediaRouteProviderProtocol.SERVICE_INTERFACE);
            service.setComponent(mComponentName);
            try {
                mBound = getContext().bindService(service, this, android.content.Context.BIND_AUTO_CREATE);
                if ((!mBound) && android.support.v7.media.RegisteredMediaRouteProvider.DEBUG) {
                    android.util.Log.d(android.support.v7.media.RegisteredMediaRouteProvider.TAG, this + ": Bind failed");
                }
            } catch (java.lang.SecurityException ex) {
                if (android.support.v7.media.RegisteredMediaRouteProvider.DEBUG) {
                    android.util.Log.d(android.support.v7.media.RegisteredMediaRouteProvider.TAG, this + ": Bind failed", ex);
                }
            }
        }
    }

    private void unbind() {
        if (mBound) {
            if (android.support.v7.media.RegisteredMediaRouteProvider.DEBUG) {
                android.util.Log.d(android.support.v7.media.RegisteredMediaRouteProvider.TAG, this + ": Unbinding");
            }
            mBound = false;
            disconnect();
            getContext().unbindService(this);
        }
    }

    private android.support.v7.media.MediaRouteProvider.RouteController createRouteController(java.lang.String routeId, java.lang.String routeGroupId) {
        android.support.v7.media.MediaRouteProviderDescriptor descriptor = getDescriptor();
        if (descriptor != null) {
            java.util.List<android.support.v7.media.MediaRouteDescriptor> routes = descriptor.getRoutes();
            final int count = routes.size();
            for (int i = 0; i < count; i++) {
                final android.support.v7.media.MediaRouteDescriptor route = routes.get(i);
                if (route.getId().equals(routeId)) {
                    android.support.v7.media.RegisteredMediaRouteProvider.Controller controller = new android.support.v7.media.RegisteredMediaRouteProvider.Controller(routeId, routeGroupId);
                    mControllers.add(controller);
                    if (mConnectionReady) {
                        controller.attachConnection(mActiveConnection);
                    }
                    updateBinding();
                    return controller;
                }
            }
        }
        return null;
    }

    void onConnectionReady(android.support.v7.media.RegisteredMediaRouteProvider.Connection connection) {
        if (mActiveConnection == connection) {
            mConnectionReady = true;
            attachControllersToConnection();
            android.support.v7.media.MediaRouteDiscoveryRequest request = getDiscoveryRequest();
            if (request != null) {
                mActiveConnection.setDiscoveryRequest(request);
            }
        }
    }

    void onConnectionDied(android.support.v7.media.RegisteredMediaRouteProvider.Connection connection) {
        if (mActiveConnection == connection) {
            if (android.support.v7.media.RegisteredMediaRouteProvider.DEBUG) {
                android.util.Log.d(android.support.v7.media.RegisteredMediaRouteProvider.TAG, this + ": Service connection died");
            }
            disconnect();
        }
    }

    void onConnectionError(android.support.v7.media.RegisteredMediaRouteProvider.Connection connection, java.lang.String error) {
        if (mActiveConnection == connection) {
            if (android.support.v7.media.RegisteredMediaRouteProvider.DEBUG) {
                android.util.Log.d(android.support.v7.media.RegisteredMediaRouteProvider.TAG, (this + ": Service connection error - ") + error);
            }
            unbind();
        }
    }

    void onConnectionDescriptorChanged(android.support.v7.media.RegisteredMediaRouteProvider.Connection connection, android.support.v7.media.MediaRouteProviderDescriptor descriptor) {
        if (mActiveConnection == connection) {
            if (android.support.v7.media.RegisteredMediaRouteProvider.DEBUG) {
                android.util.Log.d(android.support.v7.media.RegisteredMediaRouteProvider.TAG, (this + ": Descriptor changed, descriptor=") + descriptor);
            }
            setDescriptor(descriptor);
        }
    }

    private void disconnect() {
        if (mActiveConnection != null) {
            setDescriptor(null);
            mConnectionReady = false;
            detachControllersFromConnection();
            mActiveConnection.dispose();
            mActiveConnection = null;
        }
    }

    void onControllerReleased(android.support.v7.media.RegisteredMediaRouteProvider.Controller controller) {
        mControllers.remove(controller);
        controller.detachConnection();
        updateBinding();
    }

    private void attachControllersToConnection() {
        int count = mControllers.size();
        for (int i = 0; i < count; i++) {
            mControllers.get(i).attachConnection(mActiveConnection);
        }
    }

    private void detachControllersFromConnection() {
        int count = mControllers.size();
        for (int i = 0; i < count; i++) {
            mControllers.get(i).detachConnection();
        }
    }

    private final class Controller extends android.support.v7.media.MediaRouteProvider.RouteController {
        private final java.lang.String mRouteId;

        private final java.lang.String mRouteGroupId;

        private boolean mSelected;

        private int mPendingSetVolume = -1;

        private int mPendingUpdateVolumeDelta;

        private android.support.v7.media.RegisteredMediaRouteProvider.Connection mConnection;

        private int mControllerId;

        public Controller(java.lang.String routeId, java.lang.String routeGroupId) {
            mRouteId = routeId;
            mRouteGroupId = routeGroupId;
        }

        public void attachConnection(android.support.v7.media.RegisteredMediaRouteProvider.Connection connection) {
            mConnection = connection;
            mControllerId = connection.createRouteController(mRouteId, mRouteGroupId);
            if (mSelected) {
                connection.selectRoute(mControllerId);
                if (mPendingSetVolume >= 0) {
                    connection.setVolume(mControllerId, mPendingSetVolume);
                    mPendingSetVolume = -1;
                }
                if (mPendingUpdateVolumeDelta != 0) {
                    connection.updateVolume(mControllerId, mPendingUpdateVolumeDelta);
                    mPendingUpdateVolumeDelta = 0;
                }
            }
        }

        public void detachConnection() {
            if (mConnection != null) {
                mConnection.releaseRouteController(mControllerId);
                mConnection = null;
                mControllerId = 0;
            }
        }

        @java.lang.Override
        public void onRelease() {
            onControllerReleased(this);
        }

        @java.lang.Override
        public void onSelect() {
            mSelected = true;
            if (mConnection != null) {
                mConnection.selectRoute(mControllerId);
            }
        }

        @java.lang.Override
        public void onUnselect() {
            onUnselect(android.support.v7.media.MediaRouter.UNSELECT_REASON_UNKNOWN);
        }

        @java.lang.Override
        public void onUnselect(int reason) {
            mSelected = false;
            if (mConnection != null) {
                mConnection.unselectRoute(mControllerId, reason);
            }
        }

        @java.lang.Override
        public void onSetVolume(int volume) {
            if (mConnection != null) {
                mConnection.setVolume(mControllerId, volume);
            } else {
                mPendingSetVolume = volume;
                mPendingUpdateVolumeDelta = 0;
            }
        }

        @java.lang.Override
        public void onUpdateVolume(int delta) {
            if (mConnection != null) {
                mConnection.updateVolume(mControllerId, delta);
            } else {
                mPendingUpdateVolumeDelta += delta;
            }
        }

        @java.lang.Override
        public boolean onControlRequest(android.content.Intent intent, android.support.v7.media.MediaRouter.ControlRequestCallback callback) {
            if (mConnection != null) {
                return mConnection.sendControlRequest(mControllerId, intent, callback);
            }
            return false;
        }
    }

    private final class Connection implements android.os.IBinder.DeathRecipient {
        private final android.os.Messenger mServiceMessenger;

        private final android.support.v7.media.RegisteredMediaRouteProvider.ReceiveHandler mReceiveHandler;

        private final android.os.Messenger mReceiveMessenger;

        private int mNextRequestId = 1;

        private int mNextControllerId = 1;

        private int mServiceVersion;// non-zero when registration complete


        private int mPendingRegisterRequestId;

        private final android.util.SparseArray<android.support.v7.media.MediaRouter.ControlRequestCallback> mPendingCallbacks = new android.util.SparseArray<android.support.v7.media.MediaRouter.ControlRequestCallback>();

        public Connection(android.os.Messenger serviceMessenger) {
            mServiceMessenger = serviceMessenger;
            mReceiveHandler = new android.support.v7.media.RegisteredMediaRouteProvider.ReceiveHandler(this);
            mReceiveMessenger = new android.os.Messenger(mReceiveHandler);
        }

        public boolean register() {
            mPendingRegisterRequestId = mNextRequestId++;
            if (!sendRequest(android.support.v7.media.MediaRouteProviderProtocol.CLIENT_MSG_REGISTER, mPendingRegisterRequestId, android.support.v7.media.MediaRouteProviderProtocol.CLIENT_VERSION_CURRENT, null, null)) {
                return false;
            }
            try {
                mServiceMessenger.getBinder().linkToDeath(this, 0);
                return true;
            } catch (android.os.RemoteException ex) {
                binderDied();
            }
            return false;
        }

        public void dispose() {
            sendRequest(android.support.v7.media.MediaRouteProviderProtocol.CLIENT_MSG_UNREGISTER, 0, 0, null, null);
            mReceiveHandler.dispose();
            mServiceMessenger.getBinder().unlinkToDeath(this, 0);
            mPrivateHandler.post(new java.lang.Runnable() {
                @java.lang.Override
                public void run() {
                    failPendingCallbacks();
                }
            });
        }

        void failPendingCallbacks() {
            int count = 0;
            for (int i = 0; i < mPendingCallbacks.size(); i++) {
                mPendingCallbacks.valueAt(i).onError(null, null);
            }
            mPendingCallbacks.clear();
        }

        public boolean onGenericFailure(int requestId) {
            if (requestId == mPendingRegisterRequestId) {
                mPendingRegisterRequestId = 0;
                onConnectionError(this, "Registration failed");
            }
            android.support.v7.media.MediaRouter.ControlRequestCallback callback = mPendingCallbacks.get(requestId);
            if (callback != null) {
                mPendingCallbacks.remove(requestId);
                callback.onError(null, null);
            }
            return true;
        }

        public boolean onGenericSuccess(int requestId) {
            return true;
        }

        public boolean onRegistered(int requestId, int serviceVersion, android.os.Bundle descriptorBundle) {
            if (((mServiceVersion == 0) && (requestId == mPendingRegisterRequestId)) && (serviceVersion >= android.support.v7.media.MediaRouteProviderProtocol.SERVICE_VERSION_1)) {
                mPendingRegisterRequestId = 0;
                mServiceVersion = serviceVersion;
                onConnectionDescriptorChanged(this, android.support.v7.media.MediaRouteProviderDescriptor.fromBundle(descriptorBundle));
                onConnectionReady(this);
                return true;
            }
            return false;
        }

        public boolean onDescriptorChanged(android.os.Bundle descriptorBundle) {
            if (mServiceVersion != 0) {
                onConnectionDescriptorChanged(this, android.support.v7.media.MediaRouteProviderDescriptor.fromBundle(descriptorBundle));
                return true;
            }
            return false;
        }

        public boolean onControlRequestSucceeded(int requestId, android.os.Bundle data) {
            android.support.v7.media.MediaRouter.ControlRequestCallback callback = mPendingCallbacks.get(requestId);
            if (callback != null) {
                mPendingCallbacks.remove(requestId);
                callback.onResult(data);
                return true;
            }
            return false;
        }

        public boolean onControlRequestFailed(int requestId, java.lang.String error, android.os.Bundle data) {
            android.support.v7.media.MediaRouter.ControlRequestCallback callback = mPendingCallbacks.get(requestId);
            if (callback != null) {
                mPendingCallbacks.remove(requestId);
                callback.onError(error, data);
                return true;
            }
            return false;
        }

        @java.lang.Override
        public void binderDied() {
            mPrivateHandler.post(new java.lang.Runnable() {
                @java.lang.Override
                public void run() {
                    onConnectionDied(android.support.v7.media.RegisteredMediaRouteProvider.Connection.this);
                }
            });
        }

        public int createRouteController(java.lang.String routeId, java.lang.String routeGroupId) {
            int controllerId = mNextControllerId++;
            android.os.Bundle data = new android.os.Bundle();
            data.putString(android.support.v7.media.MediaRouteProviderProtocol.CLIENT_DATA_ROUTE_ID, routeId);
            data.putString(android.support.v7.media.MediaRouteProviderProtocol.CLIENT_DATA_ROUTE_GROUP_ID, routeGroupId);
            sendRequest(android.support.v7.media.MediaRouteProviderProtocol.CLIENT_MSG_CREATE_ROUTE_CONTROLLER, mNextRequestId++, controllerId, null, data);
            return controllerId;
        }

        public void releaseRouteController(int controllerId) {
            sendRequest(android.support.v7.media.MediaRouteProviderProtocol.CLIENT_MSG_RELEASE_ROUTE_CONTROLLER, mNextRequestId++, controllerId, null, null);
        }

        public void selectRoute(int controllerId) {
            sendRequest(android.support.v7.media.MediaRouteProviderProtocol.CLIENT_MSG_SELECT_ROUTE, mNextRequestId++, controllerId, null, null);
        }

        public void unselectRoute(int controllerId, int reason) {
            android.os.Bundle extras = new android.os.Bundle();
            extras.putInt(android.support.v7.media.MediaRouteProviderProtocol.CLIENT_DATA_UNSELECT_REASON, reason);
            sendRequest(android.support.v7.media.MediaRouteProviderProtocol.CLIENT_MSG_UNSELECT_ROUTE, mNextRequestId++, controllerId, null, extras);
        }

        public void setVolume(int controllerId, int volume) {
            android.os.Bundle data = new android.os.Bundle();
            data.putInt(android.support.v7.media.MediaRouteProviderProtocol.CLIENT_DATA_VOLUME, volume);
            sendRequest(android.support.v7.media.MediaRouteProviderProtocol.CLIENT_MSG_SET_ROUTE_VOLUME, mNextRequestId++, controllerId, null, data);
        }

        public void updateVolume(int controllerId, int delta) {
            android.os.Bundle data = new android.os.Bundle();
            data.putInt(android.support.v7.media.MediaRouteProviderProtocol.CLIENT_DATA_VOLUME, delta);
            sendRequest(android.support.v7.media.MediaRouteProviderProtocol.CLIENT_MSG_UPDATE_ROUTE_VOLUME, mNextRequestId++, controllerId, null, data);
        }

        public boolean sendControlRequest(int controllerId, android.content.Intent intent, android.support.v7.media.MediaRouter.ControlRequestCallback callback) {
            int requestId = mNextRequestId++;
            if (sendRequest(android.support.v7.media.MediaRouteProviderProtocol.CLIENT_MSG_ROUTE_CONTROL_REQUEST, requestId, controllerId, intent, null)) {
                if (callback != null) {
                    mPendingCallbacks.put(requestId, callback);
                }
                return true;
            }
            return false;
        }

        public void setDiscoveryRequest(android.support.v7.media.MediaRouteDiscoveryRequest request) {
            sendRequest(android.support.v7.media.MediaRouteProviderProtocol.CLIENT_MSG_SET_DISCOVERY_REQUEST, mNextRequestId++, 0, request != null ? request.asBundle() : null, null);
        }

        private boolean sendRequest(int what, int requestId, int arg, java.lang.Object obj, android.os.Bundle data) {
            android.os.Message msg = android.os.Message.obtain();
            msg.what = what;
            msg.arg1 = requestId;
            msg.arg2 = arg;
            msg.obj = obj;
            msg.setData(data);
            msg.replyTo = mReceiveMessenger;
            try {
                mServiceMessenger.send(msg);
                return true;
            } catch (android.os.DeadObjectException ex) {
                // The service died.
            } catch (android.os.RemoteException ex) {
                if (what != android.support.v7.media.MediaRouteProviderProtocol.CLIENT_MSG_UNREGISTER) {
                    android.util.Log.e(android.support.v7.media.RegisteredMediaRouteProvider.TAG, "Could not send message to service.", ex);
                }
            }
            return false;
        }
    }

    private final class PrivateHandler extends android.os.Handler {
        PrivateHandler() {
        }
    }

    /**
     * Handler that receives messages from the server.
     * <p>
     * This inner class is static and only retains a weak reference to the connection
     * to prevent the client from being leaked in case the service is holding an
     * active reference to the client's messenger.
     * </p><p>
     * This handler should not be used to handle any messages other than those
     * that come from the service.
     * </p>
     */
    private static final class ReceiveHandler extends android.os.Handler {
        private final java.lang.ref.WeakReference<android.support.v7.media.RegisteredMediaRouteProvider.Connection> mConnectionRef;

        public ReceiveHandler(android.support.v7.media.RegisteredMediaRouteProvider.Connection connection) {
            mConnectionRef = new java.lang.ref.WeakReference<android.support.v7.media.RegisteredMediaRouteProvider.Connection>(connection);
        }

        public void dispose() {
            mConnectionRef.clear();
        }

        @java.lang.Override
        public void handleMessage(android.os.Message msg) {
            android.support.v7.media.RegisteredMediaRouteProvider.Connection connection = mConnectionRef.get();
            if (connection != null) {
                final int what = msg.what;
                final int requestId = msg.arg1;
                final int arg = msg.arg2;
                final java.lang.Object obj = msg.obj;
                final android.os.Bundle data = msg.peekData();
                if (!processMessage(connection, what, requestId, arg, obj, data)) {
                    if (android.support.v7.media.RegisteredMediaRouteProvider.DEBUG) {
                        android.util.Log.d(android.support.v7.media.RegisteredMediaRouteProvider.TAG, "Unhandled message from server: " + msg);
                    }
                }
            }
        }

        private boolean processMessage(android.support.v7.media.RegisteredMediaRouteProvider.Connection connection, int what, int requestId, int arg, java.lang.Object obj, android.os.Bundle data) {
            switch (what) {
                case android.support.v7.media.MediaRouteProviderProtocol.SERVICE_MSG_GENERIC_FAILURE :
                    connection.onGenericFailure(requestId);
                    return true;
                case android.support.v7.media.MediaRouteProviderProtocol.SERVICE_MSG_GENERIC_SUCCESS :
                    connection.onGenericSuccess(requestId);
                    return true;
                case android.support.v7.media.MediaRouteProviderProtocol.SERVICE_MSG_REGISTERED :
                    if ((obj == null) || (obj instanceof android.os.Bundle)) {
                        return connection.onRegistered(requestId, arg, ((android.os.Bundle) (obj)));
                    }
                    break;
                case android.support.v7.media.MediaRouteProviderProtocol.SERVICE_MSG_DESCRIPTOR_CHANGED :
                    if ((obj == null) || (obj instanceof android.os.Bundle)) {
                        return connection.onDescriptorChanged(((android.os.Bundle) (obj)));
                    }
                    break;
                case android.support.v7.media.MediaRouteProviderProtocol.SERVICE_MSG_CONTROL_REQUEST_SUCCEEDED :
                    if ((obj == null) || (obj instanceof android.os.Bundle)) {
                        return connection.onControlRequestSucceeded(requestId, ((android.os.Bundle) (obj)));
                    }
                    break;
                case android.support.v7.media.MediaRouteProviderProtocol.SERVICE_MSG_CONTROL_REQUEST_FAILED :
                    if ((obj == null) || (obj instanceof android.os.Bundle)) {
                        java.lang.String error = (data == null) ? null : data.getString(android.support.v7.media.MediaRouteProviderProtocol.SERVICE_DATA_ERROR);
                        return connection.onControlRequestFailed(requestId, error, ((android.os.Bundle) (obj)));
                    }
                    break;
            }
            return false;
        }
    }
}

