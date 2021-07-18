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
 * Base class for media route provider services.
 * <p>
 * A media router will bind to media route provider services when a callback is added via
 * {@link MediaRouter#addCallback(MediaRouteSelector, MediaRouter.Callback, int)} with a discovery
 * flag: {@link MediaRouter#CALLBACK_FLAG_REQUEST_DISCOVERY},
 * {@link MediaRouter#CALLBACK_FLAG_FORCE_DISCOVERY}, or
 * {@link MediaRouter#CALLBACK_FLAG_PERFORM_ACTIVE_SCAN}, and will unbind when the callback
 * is removed via {@link MediaRouter#removeCallback(MediaRouter.Callback)}.
 * </p><p>
 * To implement your own media route provider service, extend this class and
 * override the {@link #onCreateMediaRouteProvider} method to return an
 * instance of your {@link MediaRouteProvider}.
 * </p><p>
 * Declare your media route provider service in your application manifest
 * like this:
 * </p>
 * <pre>
 *   &lt;service android:name=".MyMediaRouteProviderService"
 *           android:label="@string/my_media_route_provider_service">
 *       &lt;intent-filter>
 *           &lt;action android:name="android.media.MediaRouteProviderService" />
 *       &lt;/intent-filter>
 *   &lt;/service>
 * </pre>
 */
public abstract class MediaRouteProviderService extends android.app.Service {
    static final java.lang.String TAG = "MediaRouteProviderSrv";// max. 23 chars


    static final boolean DEBUG = android.util.Log.isLoggable(android.support.v7.media.MediaRouteProviderService.TAG, android.util.Log.DEBUG);

    private final java.util.ArrayList<android.support.v7.media.MediaRouteProviderService.ClientRecord> mClients = new java.util.ArrayList<android.support.v7.media.MediaRouteProviderService.ClientRecord>();

    private final android.support.v7.media.MediaRouteProviderService.ReceiveHandler mReceiveHandler;

    private final android.os.Messenger mReceiveMessenger;

    final android.support.v7.media.MediaRouteProviderService.PrivateHandler mPrivateHandler;

    private final android.support.v7.media.MediaRouteProviderService.ProviderCallback mProviderCallback;

    android.support.v7.media.MediaRouteProvider mProvider;

    private android.support.v7.media.MediaRouteDiscoveryRequest mCompositeDiscoveryRequest;

    /**
     * The {@link Intent} that must be declared as handled by the service.
     * Put this in your manifest.
     */
    public static final java.lang.String SERVICE_INTERFACE = android.support.v7.media.MediaRouteProviderProtocol.SERVICE_INTERFACE;

    /* Private messages used internally.  (Yes, you can renumber these.) */
    static final int PRIVATE_MSG_CLIENT_DIED = 1;

    /**
     * Creates a media route provider service.
     */
    public MediaRouteProviderService() {
        mReceiveHandler = new android.support.v7.media.MediaRouteProviderService.ReceiveHandler(this);
        mReceiveMessenger = new android.os.Messenger(mReceiveHandler);
        mPrivateHandler = new android.support.v7.media.MediaRouteProviderService.PrivateHandler();
        mProviderCallback = new android.support.v7.media.MediaRouteProviderService.ProviderCallback();
    }

    /**
     * Called by the system when it is time to create the media route provider.
     *
     * @return The media route provider offered by this service, or null if
    this service has decided not to offer a media route provider.
     */
    public abstract android.support.v7.media.MediaRouteProvider onCreateMediaRouteProvider();

    /**
     * Gets the media route provider offered by this service.
     *
     * @return The media route provider offered by this service, or null if
    it has not yet been created.
     * @see #onCreateMediaRouteProvider()
     */
    public android.support.v7.media.MediaRouteProvider getMediaRouteProvider() {
        return mProvider;
    }

    @java.lang.Override
    public android.os.IBinder onBind(android.content.Intent intent) {
        if (intent.getAction().equals(android.support.v7.media.MediaRouteProviderService.SERVICE_INTERFACE)) {
            if (mProvider == null) {
                android.support.v7.media.MediaRouteProvider provider = onCreateMediaRouteProvider();
                if (provider != null) {
                    java.lang.String providerPackage = provider.getMetadata().getPackageName();
                    if (!providerPackage.equals(getPackageName())) {
                        throw new java.lang.IllegalStateException((((("onCreateMediaRouteProvider() returned " + ((("a provider whose package name does not match the package " + "name of the service.  A media route provider service can ") + "only export its own media route providers.  ") + "Provider package name: ")) + providerPackage) + ".  Service package name: ") + getPackageName()) + ".");
                    }
                    mProvider = provider;
                    mProvider.setCallback(mProviderCallback);
                }
            }
            if (mProvider != null) {
                return mReceiveMessenger.getBinder();
            }
        }
        return null;
    }

    @java.lang.Override
    public boolean onUnbind(android.content.Intent intent) {
        if (mProvider != null) {
            mProvider.setCallback(null);
        }
        return super.onUnbind(intent);
    }

    boolean onRegisterClient(android.os.Messenger messenger, int requestId, int version) {
        if (version >= android.support.v7.media.MediaRouteProviderProtocol.CLIENT_VERSION_1) {
            int index = findClient(messenger);
            if (index < 0) {
                android.support.v7.media.MediaRouteProviderService.ClientRecord client = new android.support.v7.media.MediaRouteProviderService.ClientRecord(messenger, version);
                if (client.register()) {
                    mClients.add(client);
                    if (android.support.v7.media.MediaRouteProviderService.DEBUG) {
                        android.util.Log.d(android.support.v7.media.MediaRouteProviderService.TAG, (client + ": Registered, version=") + version);
                    }
                    if (requestId != 0) {
                        android.support.v7.media.MediaRouteProviderDescriptor descriptor = mProvider.getDescriptor();
                        android.support.v7.media.MediaRouteProviderService.sendReply(messenger, android.support.v7.media.MediaRouteProviderProtocol.SERVICE_MSG_REGISTERED, requestId, android.support.v7.media.MediaRouteProviderProtocol.SERVICE_VERSION_CURRENT, createDescriptorBundleForClient(descriptor, client), null);
                    }
                    return true;
                }
            }
        }
        return false;
    }

    boolean onUnregisterClient(android.os.Messenger messenger, int requestId) {
        int index = findClient(messenger);
        if (index >= 0) {
            android.support.v7.media.MediaRouteProviderService.ClientRecord client = mClients.remove(index);
            if (android.support.v7.media.MediaRouteProviderService.DEBUG) {
                android.util.Log.d(android.support.v7.media.MediaRouteProviderService.TAG, client + ": Unregistered");
            }
            client.dispose();
            android.support.v7.media.MediaRouteProviderService.sendGenericSuccess(messenger, requestId);
            return true;
        }
        return false;
    }

    void onBinderDied(android.os.Messenger messenger) {
        int index = findClient(messenger);
        if (index >= 0) {
            android.support.v7.media.MediaRouteProviderService.ClientRecord client = mClients.remove(index);
            if (android.support.v7.media.MediaRouteProviderService.DEBUG) {
                android.util.Log.d(android.support.v7.media.MediaRouteProviderService.TAG, client + ": Binder died");
            }
            client.dispose();
        }
    }

    boolean onCreateRouteController(android.os.Messenger messenger, int requestId, int controllerId, java.lang.String routeId, java.lang.String routeGroupId) {
        android.support.v7.media.MediaRouteProviderService.ClientRecord client = getClient(messenger);
        if (client != null) {
            if (client.createRouteController(routeId, routeGroupId, controllerId)) {
                if (android.support.v7.media.MediaRouteProviderService.DEBUG) {
                    android.util.Log.d(android.support.v7.media.MediaRouteProviderService.TAG, (((((client + ": Route controller created, controllerId=") + controllerId) + ", routeId=") + routeId) + ", routeGroupId=") + routeGroupId);
                }
                android.support.v7.media.MediaRouteProviderService.sendGenericSuccess(messenger, requestId);
                return true;
            }
        }
        return false;
    }

    boolean onReleaseRouteController(android.os.Messenger messenger, int requestId, int controllerId) {
        android.support.v7.media.MediaRouteProviderService.ClientRecord client = getClient(messenger);
        if (client != null) {
            if (client.releaseRouteController(controllerId)) {
                if (android.support.v7.media.MediaRouteProviderService.DEBUG) {
                    android.util.Log.d(android.support.v7.media.MediaRouteProviderService.TAG, ((client + ": Route controller released") + ", controllerId=") + controllerId);
                }
                android.support.v7.media.MediaRouteProviderService.sendGenericSuccess(messenger, requestId);
                return true;
            }
        }
        return false;
    }

    boolean onSelectRoute(android.os.Messenger messenger, int requestId, int controllerId) {
        android.support.v7.media.MediaRouteProviderService.ClientRecord client = getClient(messenger);
        if (client != null) {
            android.support.v7.media.MediaRouteProvider.RouteController controller = client.getRouteController(controllerId);
            if (controller != null) {
                controller.onSelect();
                if (android.support.v7.media.MediaRouteProviderService.DEBUG) {
                    android.util.Log.d(android.support.v7.media.MediaRouteProviderService.TAG, ((client + ": Route selected") + ", controllerId=") + controllerId);
                }
                android.support.v7.media.MediaRouteProviderService.sendGenericSuccess(messenger, requestId);
                return true;
            }
        }
        return false;
    }

    boolean onUnselectRoute(android.os.Messenger messenger, int requestId, int controllerId, int reason) {
        android.support.v7.media.MediaRouteProviderService.ClientRecord client = getClient(messenger);
        if (client != null) {
            android.support.v7.media.MediaRouteProvider.RouteController controller = client.getRouteController(controllerId);
            if (controller != null) {
                controller.onUnselect(reason);
                if (android.support.v7.media.MediaRouteProviderService.DEBUG) {
                    android.util.Log.d(android.support.v7.media.MediaRouteProviderService.TAG, ((client + ": Route unselected") + ", controllerId=") + controllerId);
                }
                android.support.v7.media.MediaRouteProviderService.sendGenericSuccess(messenger, requestId);
                return true;
            }
        }
        return false;
    }

    boolean onSetRouteVolume(android.os.Messenger messenger, int requestId, int controllerId, int volume) {
        android.support.v7.media.MediaRouteProviderService.ClientRecord client = getClient(messenger);
        if (client != null) {
            android.support.v7.media.MediaRouteProvider.RouteController controller = client.getRouteController(controllerId);
            if (controller != null) {
                controller.onSetVolume(volume);
                if (android.support.v7.media.MediaRouteProviderService.DEBUG) {
                    android.util.Log.d(android.support.v7.media.MediaRouteProviderService.TAG, ((((client + ": Route volume changed") + ", controllerId=") + controllerId) + ", volume=") + volume);
                }
                android.support.v7.media.MediaRouteProviderService.sendGenericSuccess(messenger, requestId);
                return true;
            }
        }
        return false;
    }

    boolean onUpdateRouteVolume(android.os.Messenger messenger, int requestId, int controllerId, int delta) {
        android.support.v7.media.MediaRouteProviderService.ClientRecord client = getClient(messenger);
        if (client != null) {
            android.support.v7.media.MediaRouteProvider.RouteController controller = client.getRouteController(controllerId);
            if (controller != null) {
                controller.onUpdateVolume(delta);
                if (android.support.v7.media.MediaRouteProviderService.DEBUG) {
                    android.util.Log.d(android.support.v7.media.MediaRouteProviderService.TAG, ((((client + ": Route volume updated") + ", controllerId=") + controllerId) + ", delta=") + delta);
                }
                android.support.v7.media.MediaRouteProviderService.sendGenericSuccess(messenger, requestId);
                return true;
            }
        }
        return false;
    }

    boolean onRouteControlRequest(final android.os.Messenger messenger, final int requestId, final int controllerId, final android.content.Intent intent) {
        final android.support.v7.media.MediaRouteProviderService.ClientRecord client = getClient(messenger);
        if (client != null) {
            android.support.v7.media.MediaRouteProvider.RouteController controller = client.getRouteController(controllerId);
            if (controller != null) {
                android.support.v7.media.MediaRouter.ControlRequestCallback callback = null;
                if (requestId != 0) {
                    callback = new android.support.v7.media.MediaRouter.ControlRequestCallback() {
                        @java.lang.Override
                        public void onResult(android.os.Bundle data) {
                            if (android.support.v7.media.MediaRouteProviderService.DEBUG) {
                                android.util.Log.d(android.support.v7.media.MediaRouteProviderService.TAG, ((((((client + ": Route control request succeeded") + ", controllerId=") + controllerId) + ", intent=") + intent) + ", data=") + data);
                            }
                            if (findClient(messenger) >= 0) {
                                android.support.v7.media.MediaRouteProviderService.sendReply(messenger, android.support.v7.media.MediaRouteProviderProtocol.SERVICE_MSG_CONTROL_REQUEST_SUCCEEDED, requestId, 0, data, null);
                            }
                        }

                        @java.lang.Override
                        public void onError(java.lang.String error, android.os.Bundle data) {
                            if (android.support.v7.media.MediaRouteProviderService.DEBUG) {
                                android.util.Log.d(android.support.v7.media.MediaRouteProviderService.TAG, ((((((((client + ": Route control request failed") + ", controllerId=") + controllerId) + ", intent=") + intent) + ", error=") + error) + ", data=") + data);
                            }
                            if (findClient(messenger) >= 0) {
                                if (error != null) {
                                    android.os.Bundle bundle = new android.os.Bundle();
                                    bundle.putString(android.support.v7.media.MediaRouteProviderProtocol.SERVICE_DATA_ERROR, error);
                                    android.support.v7.media.MediaRouteProviderService.sendReply(messenger, android.support.v7.media.MediaRouteProviderProtocol.SERVICE_MSG_CONTROL_REQUEST_FAILED, requestId, 0, data, bundle);
                                } else {
                                    android.support.v7.media.MediaRouteProviderService.sendReply(messenger, android.support.v7.media.MediaRouteProviderProtocol.SERVICE_MSG_CONTROL_REQUEST_FAILED, requestId, 0, data, null);
                                }
                            }
                        }
                    };
                }
                if (controller.onControlRequest(intent, callback)) {
                    if (android.support.v7.media.MediaRouteProviderService.DEBUG) {
                        android.util.Log.d(android.support.v7.media.MediaRouteProviderService.TAG, ((((client + ": Route control request delivered") + ", controllerId=") + controllerId) + ", intent=") + intent);
                    }
                    return true;
                }
            }
        }
        return false;
    }

    boolean onSetDiscoveryRequest(android.os.Messenger messenger, int requestId, android.support.v7.media.MediaRouteDiscoveryRequest request) {
        android.support.v7.media.MediaRouteProviderService.ClientRecord client = getClient(messenger);
        if (client != null) {
            boolean actuallyChanged = client.setDiscoveryRequest(request);
            if (android.support.v7.media.MediaRouteProviderService.DEBUG) {
                android.util.Log.d(android.support.v7.media.MediaRouteProviderService.TAG, (((((client + ": Set discovery request, request=") + request) + ", actuallyChanged=") + actuallyChanged) + ", compositeDiscoveryRequest=") + mCompositeDiscoveryRequest);
            }
            android.support.v7.media.MediaRouteProviderService.sendGenericSuccess(messenger, requestId);
            return true;
        }
        return false;
    }

    void sendDescriptorChanged(android.support.v7.media.MediaRouteProviderDescriptor descriptor) {
        final int count = mClients.size();
        for (int i = 0; i < count; i++) {
            android.support.v7.media.MediaRouteProviderService.ClientRecord client = mClients.get(i);
            android.support.v7.media.MediaRouteProviderService.sendReply(client.mMessenger, android.support.v7.media.MediaRouteProviderProtocol.SERVICE_MSG_DESCRIPTOR_CHANGED, 0, 0, createDescriptorBundleForClient(descriptor, client), null);
            if (android.support.v7.media.MediaRouteProviderService.DEBUG) {
                android.util.Log.d(android.support.v7.media.MediaRouteProviderService.TAG, (client + ": Sent descriptor change event, descriptor=") + descriptor);
            }
        }
    }

    private android.os.Bundle createDescriptorBundleForClient(android.support.v7.media.MediaRouteProviderDescriptor descriptor, android.support.v7.media.MediaRouteProviderService.ClientRecord client) {
        if (descriptor == null) {
            return null;
        }
        java.util.List<android.support.v7.media.MediaRouteDescriptor> routes = descriptor.getRoutes();
        for (int i = routes.size() - 1; i >= 0; i--) {
            if ((client.mVersion < routes.get(i).getMinClientVersion()) || (client.mVersion > routes.get(i).getMaxClientVersion())) {
                routes.remove(i);
            }
        }
        // Keep the values of the bundle from descriptor excepts routes values.
        android.os.Bundle bundle = descriptor.asBundle();
        bundle.remove(android.support.v7.media.MediaRouteProviderDescriptor.KEY_ROUTES);
        return new android.support.v7.media.MediaRouteProviderDescriptor.Builder(android.support.v7.media.MediaRouteProviderDescriptor.fromBundle(bundle)).addRoutes(routes).build().asBundle();
    }

    boolean updateCompositeDiscoveryRequest() {
        android.support.v7.media.MediaRouteDiscoveryRequest composite = null;
        android.support.v7.media.MediaRouteSelector.Builder selectorBuilder = null;
        boolean activeScan = false;
        final int count = mClients.size();
        for (int i = 0; i < count; i++) {
            android.support.v7.media.MediaRouteDiscoveryRequest request = mClients.get(i).mDiscoveryRequest;
            if ((request != null) && ((!request.getSelector().isEmpty()) || request.isActiveScan())) {
                activeScan |= request.isActiveScan();
                if (composite == null) {
                    composite = request;
                } else {
                    if (selectorBuilder == null) {
                        selectorBuilder = new android.support.v7.media.MediaRouteSelector.Builder(composite.getSelector());
                    }
                    selectorBuilder.addSelector(request.getSelector());
                }
            }
        }
        if (selectorBuilder != null) {
            composite = new android.support.v7.media.MediaRouteDiscoveryRequest(selectorBuilder.build(), activeScan);
        }
        if ((mCompositeDiscoveryRequest != composite) && ((mCompositeDiscoveryRequest == null) || (!mCompositeDiscoveryRequest.equals(composite)))) {
            mCompositeDiscoveryRequest = composite;
            mProvider.setDiscoveryRequest(composite);
            return true;
        }
        return false;
    }

    private android.support.v7.media.MediaRouteProviderService.ClientRecord getClient(android.os.Messenger messenger) {
        int index = findClient(messenger);
        return index >= 0 ? mClients.get(index) : null;
    }

    int findClient(android.os.Messenger messenger) {
        final int count = mClients.size();
        for (int i = 0; i < count; i++) {
            android.support.v7.media.MediaRouteProviderService.ClientRecord client = mClients.get(i);
            if (client.hasMessenger(messenger)) {
                return i;
            }
        }
        return -1;
    }

    static void sendGenericFailure(android.os.Messenger messenger, int requestId) {
        if (requestId != 0) {
            android.support.v7.media.MediaRouteProviderService.sendReply(messenger, android.support.v7.media.MediaRouteProviderProtocol.SERVICE_MSG_GENERIC_FAILURE, requestId, 0, null, null);
        }
    }

    private static void sendGenericSuccess(android.os.Messenger messenger, int requestId) {
        if (requestId != 0) {
            android.support.v7.media.MediaRouteProviderService.sendReply(messenger, android.support.v7.media.MediaRouteProviderProtocol.SERVICE_MSG_GENERIC_SUCCESS, requestId, 0, null, null);
        }
    }

    static void sendReply(android.os.Messenger messenger, int what, int requestId, int arg, java.lang.Object obj, android.os.Bundle data) {
        android.os.Message msg = android.os.Message.obtain();
        msg.what = what;
        msg.arg1 = requestId;
        msg.arg2 = arg;
        msg.obj = obj;
        msg.setData(data);
        try {
            messenger.send(msg);
        } catch (android.os.DeadObjectException ex) {
            // The client died.
        } catch (android.os.RemoteException ex) {
            android.util.Log.e(android.support.v7.media.MediaRouteProviderService.TAG, "Could not send message to " + android.support.v7.media.MediaRouteProviderService.getClientId(messenger), ex);
        }
    }

    static java.lang.String getClientId(android.os.Messenger messenger) {
        return "Client connection " + messenger.getBinder().toString();
    }

    private final class PrivateHandler extends android.os.Handler {
        PrivateHandler() {
        }

        @java.lang.Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case android.support.v7.media.MediaRouteProviderService.PRIVATE_MSG_CLIENT_DIED :
                    onBinderDied(((android.os.Messenger) (msg.obj)));
                    break;
            }
        }
    }

    private final class ProviderCallback extends android.support.v7.media.MediaRouteProvider.Callback {
        ProviderCallback() {
        }

        @java.lang.Override
        public void onDescriptorChanged(android.support.v7.media.MediaRouteProvider provider, android.support.v7.media.MediaRouteProviderDescriptor descriptor) {
            sendDescriptorChanged(descriptor);
        }
    }

    private final class ClientRecord implements android.os.IBinder.DeathRecipient {
        public final android.os.Messenger mMessenger;

        public final int mVersion;

        public android.support.v7.media.MediaRouteDiscoveryRequest mDiscoveryRequest;

        private final android.util.SparseArray<android.support.v7.media.MediaRouteProvider.RouteController> mControllers = new android.util.SparseArray<android.support.v7.media.MediaRouteProvider.RouteController>();

        public ClientRecord(android.os.Messenger messenger, int version) {
            mMessenger = messenger;
            mVersion = version;
        }

        public boolean register() {
            try {
                mMessenger.getBinder().linkToDeath(this, 0);
                return true;
            } catch (android.os.RemoteException ex) {
                binderDied();
            }
            return false;
        }

        public void dispose() {
            int count = mControllers.size();
            for (int i = 0; i < count; i++) {
                mControllers.valueAt(i).onRelease();
            }
            mControllers.clear();
            mMessenger.getBinder().unlinkToDeath(this, 0);
            setDiscoveryRequest(null);
        }

        public boolean hasMessenger(android.os.Messenger other) {
            return mMessenger.getBinder() == other.getBinder();
        }

        public boolean createRouteController(java.lang.String routeId, java.lang.String routeGroupId, int controllerId) {
            if (mControllers.indexOfKey(controllerId) < 0) {
                android.support.v7.media.MediaRouteProvider.RouteController controller = (routeGroupId == null) ? mProvider.onCreateRouteController(routeId) : mProvider.onCreateRouteController(routeId, routeGroupId);
                if (controller != null) {
                    mControllers.put(controllerId, controller);
                    return true;
                }
            }
            return false;
        }

        public boolean releaseRouteController(int controllerId) {
            android.support.v7.media.MediaRouteProvider.RouteController controller = mControllers.get(controllerId);
            if (controller != null) {
                mControllers.remove(controllerId);
                controller.onRelease();
                return true;
            }
            return false;
        }

        public android.support.v7.media.MediaRouteProvider.RouteController getRouteController(int controllerId) {
            return mControllers.get(controllerId);
        }

        public boolean setDiscoveryRequest(android.support.v7.media.MediaRouteDiscoveryRequest request) {
            if ((mDiscoveryRequest != request) && ((mDiscoveryRequest == null) || (!mDiscoveryRequest.equals(request)))) {
                mDiscoveryRequest = request;
                return updateCompositeDiscoveryRequest();
            }
            return false;
        }

        // Runs on a binder thread.
        @java.lang.Override
        public void binderDied() {
            mPrivateHandler.obtainMessage(android.support.v7.media.MediaRouteProviderService.PRIVATE_MSG_CLIENT_DIED, mMessenger).sendToTarget();
        }

        @java.lang.Override
        public java.lang.String toString() {
            return android.support.v7.media.MediaRouteProviderService.getClientId(mMessenger);
        }
    }

    /**
     * Handler that receives messages from clients.
     * <p>
     * This inner class is static and only retains a weak reference to the service
     * to prevent the service from being leaked in case one of the clients is holding an
     * active reference to the server's messenger.
     * </p><p>
     * This handler should not be used to handle any messages other than those
     * that come from the client.
     * </p>
     */
    private static final class ReceiveHandler extends android.os.Handler {
        private final java.lang.ref.WeakReference<android.support.v7.media.MediaRouteProviderService> mServiceRef;

        public ReceiveHandler(android.support.v7.media.MediaRouteProviderService service) {
            mServiceRef = new java.lang.ref.WeakReference<android.support.v7.media.MediaRouteProviderService>(service);
        }

        @java.lang.Override
        public void handleMessage(android.os.Message msg) {
            final android.os.Messenger messenger = msg.replyTo;
            if (android.support.v7.media.MediaRouteProviderProtocol.isValidRemoteMessenger(messenger)) {
                final int what = msg.what;
                final int requestId = msg.arg1;
                final int arg = msg.arg2;
                final java.lang.Object obj = msg.obj;
                final android.os.Bundle data = msg.peekData();
                if (!processMessage(what, messenger, requestId, arg, obj, data)) {
                    if (android.support.v7.media.MediaRouteProviderService.DEBUG) {
                        android.util.Log.d(android.support.v7.media.MediaRouteProviderService.TAG, (((((((((android.support.v7.media.MediaRouteProviderService.getClientId(messenger) + ": Message failed, what=") + what) + ", requestId=") + requestId) + ", arg=") + arg) + ", obj=") + obj) + ", data=") + data);
                    }
                    android.support.v7.media.MediaRouteProviderService.sendGenericFailure(messenger, requestId);
                }
            } else {
                if (android.support.v7.media.MediaRouteProviderService.DEBUG) {
                    android.util.Log.d(android.support.v7.media.MediaRouteProviderService.TAG, "Ignoring message without valid reply messenger.");
                }
            }
        }

        private boolean processMessage(int what, android.os.Messenger messenger, int requestId, int arg, java.lang.Object obj, android.os.Bundle data) {
            android.support.v7.media.MediaRouteProviderService service = mServiceRef.get();
            if (service != null) {
                switch (what) {
                    case android.support.v7.media.MediaRouteProviderProtocol.CLIENT_MSG_REGISTER :
                        return service.onRegisterClient(messenger, requestId, arg);
                    case android.support.v7.media.MediaRouteProviderProtocol.CLIENT_MSG_UNREGISTER :
                        return service.onUnregisterClient(messenger, requestId);
                    case android.support.v7.media.MediaRouteProviderProtocol.CLIENT_MSG_CREATE_ROUTE_CONTROLLER :
                        {
                            java.lang.String routeId = data.getString(android.support.v7.media.MediaRouteProviderProtocol.CLIENT_DATA_ROUTE_ID);
                            java.lang.String routeGroupId = data.getString(android.support.v7.media.MediaRouteProviderProtocol.CLIENT_DATA_ROUTE_GROUP_ID);
                            if (routeId != null) {
                                return service.onCreateRouteController(messenger, requestId, arg, routeId, routeGroupId);
                            }
                            break;
                        }
                    case android.support.v7.media.MediaRouteProviderProtocol.CLIENT_MSG_RELEASE_ROUTE_CONTROLLER :
                        return service.onReleaseRouteController(messenger, requestId, arg);
                    case android.support.v7.media.MediaRouteProviderProtocol.CLIENT_MSG_SELECT_ROUTE :
                        return service.onSelectRoute(messenger, requestId, arg);
                    case android.support.v7.media.MediaRouteProviderProtocol.CLIENT_MSG_UNSELECT_ROUTE :
                        int reason = (data == null) ? android.support.v7.media.MediaRouter.UNSELECT_REASON_UNKNOWN : data.getInt(android.support.v7.media.MediaRouteProviderProtocol.CLIENT_DATA_UNSELECT_REASON, android.support.v7.media.MediaRouter.UNSELECT_REASON_UNKNOWN);
                        return service.onUnselectRoute(messenger, requestId, arg, reason);
                    case android.support.v7.media.MediaRouteProviderProtocol.CLIENT_MSG_SET_ROUTE_VOLUME :
                        {
                            int volume = data.getInt(android.support.v7.media.MediaRouteProviderProtocol.CLIENT_DATA_VOLUME, -1);
                            if (volume >= 0) {
                                return service.onSetRouteVolume(messenger, requestId, arg, volume);
                            }
                            break;
                        }
                    case android.support.v7.media.MediaRouteProviderProtocol.CLIENT_MSG_UPDATE_ROUTE_VOLUME :
                        {
                            int delta = data.getInt(android.support.v7.media.MediaRouteProviderProtocol.CLIENT_DATA_VOLUME, 0);
                            if (delta != 0) {
                                return service.onUpdateRouteVolume(messenger, requestId, arg, delta);
                            }
                            break;
                        }
                    case android.support.v7.media.MediaRouteProviderProtocol.CLIENT_MSG_ROUTE_CONTROL_REQUEST :
                        if (obj instanceof android.content.Intent) {
                            return service.onRouteControlRequest(messenger, requestId, arg, ((android.content.Intent) (obj)));
                        }
                        break;
                    case android.support.v7.media.MediaRouteProviderProtocol.CLIENT_MSG_SET_DISCOVERY_REQUEST :
                        {
                            if ((obj == null) || (obj instanceof android.os.Bundle)) {
                                android.support.v7.media.MediaRouteDiscoveryRequest request = android.support.v7.media.MediaRouteDiscoveryRequest.fromBundle(((android.os.Bundle) (obj)));
                                return service.onSetDiscoveryRequest(messenger, requestId, (request != null) && request.isValid() ? request : null);
                            }
                        }
                }
            }
            return false;
        }
    }
}

