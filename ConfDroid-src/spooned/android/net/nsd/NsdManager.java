/**
 * Copyright (C) 2012 The Android Open Source Project
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
package android.net.nsd;


/**
 * The Network Service Discovery Manager class provides the API to discover services
 * on a network. As an example, if device A and device B are connected over a Wi-Fi
 * network, a game registered on device A can be discovered by a game on device
 * B. Another example use case is an application discovering printers on the network.
 *
 * <p> The API currently supports DNS based service discovery and discovery is currently
 * limited to a local network over Multicast DNS. DNS service discovery is described at
 * http://files.dns-sd.org/draft-cheshire-dnsext-dns-sd.txt
 *
 * <p> The API is asynchronous and responses to requests from an application are on listener
 * callbacks on a seperate thread.
 *
 * <p> There are three main operations the API supports - registration, discovery and resolution.
 * <pre>
 *                          Application start
 *                                 |
 *                                 |
 *                                 |                  onServiceRegistered()
 *                     Register any local services  /
 *                      to be advertised with       \
 *                       registerService()            onRegistrationFailed()
 *                                 |
 *                                 |
 *                          discoverServices()
 *                                 |
 *                      Maintain a list to track
 *                        discovered services
 *                                 |
 *                                 |--------->
 *                                 |          |
 *                                 |      onServiceFound()
 *                                 |          |
 *                                 |     add service to list
 *                                 |          |
 *                                 |<----------
 *                                 |
 *                                 |--------->
 *                                 |          |
 *                                 |      onServiceLost()
 *                                 |          |
 *                                 |   remove service from list
 *                                 |          |
 *                                 |<----------
 *                                 |
 *                                 |
 *                                 | Connect to a service
 *                                 | from list ?
 *                                 |
 *                          resolveService()
 *                                 |
 *                         onServiceResolved()
 *                                 |
 *                     Establish connection to service
 *                     with the host and port information
 *
 * </pre>
 * An application that needs to advertise itself over a network for other applications to
 * discover it can do so with a call to {@link #registerService}. If Example is a http based
 * application that can provide HTML data to peer services, it can register a name "Example"
 * with service type "_http._tcp". A successful registration is notified with a callback to
 * {@link RegistrationListener#onServiceRegistered} and a failure to register is notified
 * over {@link RegistrationListener#onRegistrationFailed}
 *
 * <p> A peer application looking for http services can initiate a discovery for "_http._tcp"
 * with a call to {@link #discoverServices}. A service found is notified with a callback
 * to {@link DiscoveryListener#onServiceFound} and a service lost is notified on
 * {@link DiscoveryListener#onServiceLost}.
 *
 * <p> Once the peer application discovers the "Example" http srevice, and needs to receive data
 * from the "Example" application, it can initiate a resolve with {@link #resolveService} to
 * resolve the host and port details for the purpose of establishing a connection. A successful
 * resolve is notified on {@link ResolveListener#onServiceResolved} and a failure is notified
 * on {@link ResolveListener#onResolveFailed}.
 *
 * Applications can reserve for a service type at
 * http://www.iana.org/form/ports-service. Existing services can be found at
 * http://www.iana.org/assignments/service-names-port-numbers/service-names-port-numbers.xml
 *
 * Get an instance of this class by calling {@link android.content.Context#getSystemService(String)
 * Context.getSystemService(Context.NSD_SERVICE)}.
 *
 * {@see NsdServiceInfo}
 */
public final class NsdManager {
    private static final java.lang.String TAG = "NsdManager";

    android.net.nsd.INsdManager mService;

    /**
     * Broadcast intent action to indicate whether network service discovery is
     * enabled or disabled. An extra {@link #EXTRA_NSD_STATE} provides the state
     * information as int.
     *
     * @see #EXTRA_NSD_STATE
     */
    @android.annotation.SdkConstant(android.annotation.SdkConstant.SdkConstantType.BROADCAST_INTENT_ACTION)
    public static final java.lang.String ACTION_NSD_STATE_CHANGED = "android.net.nsd.STATE_CHANGED";

    /**
     * The lookup key for an int that indicates whether network service discovery is enabled
     * or disabled. Retrieve it with {@link android.content.Intent#getIntExtra(String,int)}.
     *
     * @see #NSD_STATE_DISABLED
     * @see #NSD_STATE_ENABLED
     */
    public static final java.lang.String EXTRA_NSD_STATE = "nsd_state";

    /**
     * Network service discovery is disabled
     *
     * @see #ACTION_NSD_STATE_CHANGED
     */
    public static final int NSD_STATE_DISABLED = 1;

    /**
     * Network service discovery is enabled
     *
     * @see #ACTION_NSD_STATE_CHANGED
     */
    public static final int NSD_STATE_ENABLED = 2;

    private static final int BASE = com.android.internal.util.Protocol.BASE_NSD_MANAGER;

    /**
     *
     *
     * @unknown 
     */
    public static final int DISCOVER_SERVICES = android.net.nsd.NsdManager.BASE + 1;

    /**
     *
     *
     * @unknown 
     */
    public static final int DISCOVER_SERVICES_STARTED = android.net.nsd.NsdManager.BASE + 2;

    /**
     *
     *
     * @unknown 
     */
    public static final int DISCOVER_SERVICES_FAILED = android.net.nsd.NsdManager.BASE + 3;

    /**
     *
     *
     * @unknown 
     */
    public static final int SERVICE_FOUND = android.net.nsd.NsdManager.BASE + 4;

    /**
     *
     *
     * @unknown 
     */
    public static final int SERVICE_LOST = android.net.nsd.NsdManager.BASE + 5;

    /**
     *
     *
     * @unknown 
     */
    public static final int STOP_DISCOVERY = android.net.nsd.NsdManager.BASE + 6;

    /**
     *
     *
     * @unknown 
     */
    public static final int STOP_DISCOVERY_FAILED = android.net.nsd.NsdManager.BASE + 7;

    /**
     *
     *
     * @unknown 
     */
    public static final int STOP_DISCOVERY_SUCCEEDED = android.net.nsd.NsdManager.BASE + 8;

    /**
     *
     *
     * @unknown 
     */
    public static final int REGISTER_SERVICE = android.net.nsd.NsdManager.BASE + 9;

    /**
     *
     *
     * @unknown 
     */
    public static final int REGISTER_SERVICE_FAILED = android.net.nsd.NsdManager.BASE + 10;

    /**
     *
     *
     * @unknown 
     */
    public static final int REGISTER_SERVICE_SUCCEEDED = android.net.nsd.NsdManager.BASE + 11;

    /**
     *
     *
     * @unknown 
     */
    public static final int UNREGISTER_SERVICE = android.net.nsd.NsdManager.BASE + 12;

    /**
     *
     *
     * @unknown 
     */
    public static final int UNREGISTER_SERVICE_FAILED = android.net.nsd.NsdManager.BASE + 13;

    /**
     *
     *
     * @unknown 
     */
    public static final int UNREGISTER_SERVICE_SUCCEEDED = android.net.nsd.NsdManager.BASE + 14;

    /**
     *
     *
     * @unknown 
     */
    public static final int RESOLVE_SERVICE = android.net.nsd.NsdManager.BASE + 18;

    /**
     *
     *
     * @unknown 
     */
    public static final int RESOLVE_SERVICE_FAILED = android.net.nsd.NsdManager.BASE + 19;

    /**
     *
     *
     * @unknown 
     */
    public static final int RESOLVE_SERVICE_SUCCEEDED = android.net.nsd.NsdManager.BASE + 20;

    /**
     *
     *
     * @unknown 
     */
    public static final int ENABLE = android.net.nsd.NsdManager.BASE + 24;

    /**
     *
     *
     * @unknown 
     */
    public static final int DISABLE = android.net.nsd.NsdManager.BASE + 25;

    /**
     *
     *
     * @unknown 
     */
    public static final int NATIVE_DAEMON_EVENT = android.net.nsd.NsdManager.BASE + 26;

    /**
     * Dns based service discovery protocol
     */
    public static final int PROTOCOL_DNS_SD = 0x1;

    private android.content.Context mContext;

    private static final int INVALID_LISTENER_KEY = 0;

    private static final int BUSY_LISTENER_KEY = -1;

    private int mListenerKey = 1;

    private final android.util.SparseArray mListenerMap = new android.util.SparseArray();

    private final android.util.SparseArray<android.net.nsd.NsdServiceInfo> mServiceMap = new android.util.SparseArray<android.net.nsd.NsdServiceInfo>();

    private final java.lang.Object mMapLock = new java.lang.Object();

    private final com.android.internal.util.AsyncChannel mAsyncChannel = new com.android.internal.util.AsyncChannel();

    private android.net.nsd.NsdManager.ServiceHandler mHandler;

    private final java.util.concurrent.CountDownLatch mConnected = new java.util.concurrent.CountDownLatch(1);

    /**
     * Create a new Nsd instance. Applications use
     * {@link android.content.Context#getSystemService Context.getSystemService()} to retrieve
     * {@link android.content.Context#NSD_SERVICE Context.NSD_SERVICE}.
     *
     * @param service
     * 		the Binder interface
     * @unknown - hide this because it takes in a parameter of type INsdManager, which
    is a system private class.
     */
    public NsdManager(android.content.Context context, android.net.nsd.INsdManager service) {
        mService = service;
        mContext = context;
        init();
    }

    /**
     * Failures are passed with {@link RegistrationListener#onRegistrationFailed},
     * {@link RegistrationListener#onUnregistrationFailed},
     * {@link DiscoveryListener#onStartDiscoveryFailed},
     * {@link DiscoveryListener#onStopDiscoveryFailed} or {@link ResolveListener#onResolveFailed}.
     *
     * Indicates that the operation failed due to an internal error.
     */
    public static final int FAILURE_INTERNAL_ERROR = 0;

    /**
     * Indicates that the operation failed because it is already active.
     */
    public static final int FAILURE_ALREADY_ACTIVE = 3;

    /**
     * Indicates that the operation failed because the maximum outstanding
     * requests from the applications have reached.
     */
    public static final int FAILURE_MAX_LIMIT = 4;

    /**
     * Interface for callback invocation for service discovery
     */
    public interface DiscoveryListener {
        public void onStartDiscoveryFailed(java.lang.String serviceType, int errorCode);

        public void onStopDiscoveryFailed(java.lang.String serviceType, int errorCode);

        public void onDiscoveryStarted(java.lang.String serviceType);

        public void onDiscoveryStopped(java.lang.String serviceType);

        public void onServiceFound(android.net.nsd.NsdServiceInfo serviceInfo);

        public void onServiceLost(android.net.nsd.NsdServiceInfo serviceInfo);
    }

    /**
     * Interface for callback invocation for service registration
     */
    public interface RegistrationListener {
        public void onRegistrationFailed(android.net.nsd.NsdServiceInfo serviceInfo, int errorCode);

        public void onUnregistrationFailed(android.net.nsd.NsdServiceInfo serviceInfo, int errorCode);

        public void onServiceRegistered(android.net.nsd.NsdServiceInfo serviceInfo);

        public void onServiceUnregistered(android.net.nsd.NsdServiceInfo serviceInfo);
    }

    /**
     * Interface for callback invocation for service resolution
     */
    public interface ResolveListener {
        public void onResolveFailed(android.net.nsd.NsdServiceInfo serviceInfo, int errorCode);

        public void onServiceResolved(android.net.nsd.NsdServiceInfo serviceInfo);
    }

    private class ServiceHandler extends android.os.Handler {
        ServiceHandler(android.os.Looper looper) {
            super(looper);
        }

        @java.lang.Override
        public void handleMessage(android.os.Message message) {
            switch (message.what) {
                case com.android.internal.util.AsyncChannel.CMD_CHANNEL_HALF_CONNECTED :
                    mAsyncChannel.sendMessage(AsyncChannel.CMD_CHANNEL_FULL_CONNECTION);
                    return;
                case com.android.internal.util.AsyncChannel.CMD_CHANNEL_FULLY_CONNECTED :
                    mConnected.countDown();
                    return;
                case com.android.internal.util.AsyncChannel.CMD_CHANNEL_DISCONNECTED :
                    android.util.Log.e(android.net.nsd.NsdManager.TAG, "Channel lost");
                    return;
                default :
                    break;
            }
            java.lang.Object listener = getListener(message.arg2);
            if (listener == null) {
                android.util.Log.d(android.net.nsd.NsdManager.TAG, "Stale key " + message.arg2);
                return;
            }
            android.net.nsd.NsdServiceInfo ns = getNsdService(message.arg2);
            switch (message.what) {
                case android.net.nsd.NsdManager.DISCOVER_SERVICES_STARTED :
                    java.lang.String s = getNsdServiceInfoType(((android.net.nsd.NsdServiceInfo) (message.obj)));
                    ((android.net.nsd.NsdManager.DiscoveryListener) (listener)).onDiscoveryStarted(s);
                    break;
                case android.net.nsd.NsdManager.DISCOVER_SERVICES_FAILED :
                    removeListener(message.arg2);
                    ((android.net.nsd.NsdManager.DiscoveryListener) (listener)).onStartDiscoveryFailed(getNsdServiceInfoType(ns), message.arg1);
                    break;
                case android.net.nsd.NsdManager.SERVICE_FOUND :
                    ((android.net.nsd.NsdManager.DiscoveryListener) (listener)).onServiceFound(((android.net.nsd.NsdServiceInfo) (message.obj)));
                    break;
                case android.net.nsd.NsdManager.SERVICE_LOST :
                    ((android.net.nsd.NsdManager.DiscoveryListener) (listener)).onServiceLost(((android.net.nsd.NsdServiceInfo) (message.obj)));
                    break;
                case android.net.nsd.NsdManager.STOP_DISCOVERY_FAILED :
                    removeListener(message.arg2);
                    ((android.net.nsd.NsdManager.DiscoveryListener) (listener)).onStopDiscoveryFailed(getNsdServiceInfoType(ns), message.arg1);
                    break;
                case android.net.nsd.NsdManager.STOP_DISCOVERY_SUCCEEDED :
                    removeListener(message.arg2);
                    ((android.net.nsd.NsdManager.DiscoveryListener) (listener)).onDiscoveryStopped(getNsdServiceInfoType(ns));
                    break;
                case android.net.nsd.NsdManager.REGISTER_SERVICE_FAILED :
                    removeListener(message.arg2);
                    ((android.net.nsd.NsdManager.RegistrationListener) (listener)).onRegistrationFailed(ns, message.arg1);
                    break;
                case android.net.nsd.NsdManager.REGISTER_SERVICE_SUCCEEDED :
                    ((android.net.nsd.NsdManager.RegistrationListener) (listener)).onServiceRegistered(((android.net.nsd.NsdServiceInfo) (message.obj)));
                    break;
                case android.net.nsd.NsdManager.UNREGISTER_SERVICE_FAILED :
                    removeListener(message.arg2);
                    ((android.net.nsd.NsdManager.RegistrationListener) (listener)).onUnregistrationFailed(ns, message.arg1);
                    break;
                case android.net.nsd.NsdManager.UNREGISTER_SERVICE_SUCCEEDED :
                    removeListener(message.arg2);
                    ((android.net.nsd.NsdManager.RegistrationListener) (listener)).onServiceUnregistered(ns);
                    break;
                case android.net.nsd.NsdManager.RESOLVE_SERVICE_FAILED :
                    removeListener(message.arg2);
                    ((android.net.nsd.NsdManager.ResolveListener) (listener)).onResolveFailed(ns, message.arg1);
                    break;
                case android.net.nsd.NsdManager.RESOLVE_SERVICE_SUCCEEDED :
                    removeListener(message.arg2);
                    ((android.net.nsd.NsdManager.ResolveListener) (listener)).onServiceResolved(((android.net.nsd.NsdServiceInfo) (message.obj)));
                    break;
                default :
                    android.util.Log.d(android.net.nsd.NsdManager.TAG, "Ignored " + message);
                    break;
            }
        }
    }

    // if the listener is already in the map, reject it.  Otherwise, add it and
    // return its key.
    private int putListener(java.lang.Object listener, android.net.nsd.NsdServiceInfo s) {
        if (listener == null)
            return android.net.nsd.NsdManager.INVALID_LISTENER_KEY;

        int key;
        synchronized(mMapLock) {
            int valueIndex = mListenerMap.indexOfValue(listener);
            if (valueIndex != (-1)) {
                return android.net.nsd.NsdManager.BUSY_LISTENER_KEY;
            }
            do {
                key = mListenerKey++;
            } while (key == android.net.nsd.NsdManager.INVALID_LISTENER_KEY );
            mListenerMap.put(key, listener);
            mServiceMap.put(key, s);
        }
        return key;
    }

    private java.lang.Object getListener(int key) {
        if (key == android.net.nsd.NsdManager.INVALID_LISTENER_KEY)
            return null;

        synchronized(mMapLock) {
            return mListenerMap.get(key);
        }
    }

    private android.net.nsd.NsdServiceInfo getNsdService(int key) {
        synchronized(mMapLock) {
            return mServiceMap.get(key);
        }
    }

    private void removeListener(int key) {
        if (key == android.net.nsd.NsdManager.INVALID_LISTENER_KEY)
            return;

        synchronized(mMapLock) {
            mListenerMap.remove(key);
            mServiceMap.remove(key);
        }
    }

    private int getListenerKey(java.lang.Object listener) {
        synchronized(mMapLock) {
            int valueIndex = mListenerMap.indexOfValue(listener);
            if (valueIndex != (-1)) {
                return mListenerMap.keyAt(valueIndex);
            }
        }
        return android.net.nsd.NsdManager.INVALID_LISTENER_KEY;
    }

    private java.lang.String getNsdServiceInfoType(android.net.nsd.NsdServiceInfo s) {
        if (s == null)
            return "?";

        return s.getServiceType();
    }

    /**
     * Initialize AsyncChannel
     */
    private void init() {
        final android.os.Messenger messenger = getMessenger();
        if (messenger == null)
            throw new java.lang.RuntimeException("Failed to initialize");

        android.os.HandlerThread t = new android.os.HandlerThread("NsdManager");
        t.start();
        mHandler = new android.net.nsd.NsdManager.ServiceHandler(t.getLooper());
        mAsyncChannel.connect(mContext, mHandler, messenger);
        try {
            mConnected.await();
        } catch (java.lang.InterruptedException e) {
            android.util.Log.e(android.net.nsd.NsdManager.TAG, "interrupted wait at init");
        }
    }

    /**
     * Register a service to be discovered by other services.
     *
     * <p> The function call immediately returns after sending a request to register service
     * to the framework. The application is notified of a successful registration
     * through the callback {@link RegistrationListener#onServiceRegistered} or a failure
     * through {@link RegistrationListener#onRegistrationFailed}.
     *
     * <p> The application should call {@link #unregisterService} when the service
     * registration is no longer required, and/or whenever the application is stopped.
     *
     * @param serviceInfo
     * 		The service being registered
     * @param protocolType
     * 		The service discovery protocol
     * @param listener
     * 		The listener notifies of a successful registration and is used to
     * 		unregister this service through a call on {@link #unregisterService}. Cannot be null.
     * 		Cannot be in use for an active service registration.
     */
    public void registerService(android.net.nsd.NsdServiceInfo serviceInfo, int protocolType, android.net.nsd.NsdManager.RegistrationListener listener) {
        if (android.text.TextUtils.isEmpty(serviceInfo.getServiceName()) || android.text.TextUtils.isEmpty(serviceInfo.getServiceType())) {
            throw new java.lang.IllegalArgumentException("Service name or type cannot be empty");
        }
        if (serviceInfo.getPort() <= 0) {
            throw new java.lang.IllegalArgumentException("Invalid port number");
        }
        if (listener == null) {
            throw new java.lang.IllegalArgumentException("listener cannot be null");
        }
        if (protocolType != android.net.nsd.NsdManager.PROTOCOL_DNS_SD) {
            throw new java.lang.IllegalArgumentException("Unsupported protocol");
        }
        int key = putListener(listener, serviceInfo);
        if (key == android.net.nsd.NsdManager.BUSY_LISTENER_KEY) {
            throw new java.lang.IllegalArgumentException("listener already in use");
        }
        mAsyncChannel.sendMessage(android.net.nsd.NsdManager.REGISTER_SERVICE, 0, key, serviceInfo);
    }

    /**
     * Unregister a service registered through {@link #registerService}. A successful
     * unregister is notified to the application with a call to
     * {@link RegistrationListener#onServiceUnregistered}.
     *
     * @param listener
     * 		This should be the listener object that was passed to
     * 		{@link #registerService}. It identifies the service that should be unregistered
     * 		and notifies of a successful or unsuccessful unregistration via the listener
     * 		callbacks.  In API versions 20 and above, the listener object may be used for
     * 		another service registration once the callback has been called.  In API versions <= 19,
     * 		there is no entirely reliable way to know when a listener may be re-used, and a new
     * 		listener should be created for each service registration request.
     */
    public void unregisterService(android.net.nsd.NsdManager.RegistrationListener listener) {
        int id = getListenerKey(listener);
        if (id == android.net.nsd.NsdManager.INVALID_LISTENER_KEY) {
            throw new java.lang.IllegalArgumentException("listener not registered");
        }
        if (listener == null) {
            throw new java.lang.IllegalArgumentException("listener cannot be null");
        }
        mAsyncChannel.sendMessage(android.net.nsd.NsdManager.UNREGISTER_SERVICE, 0, id);
    }

    /**
     * Initiate service discovery to browse for instances of a service type. Service discovery
     * consumes network bandwidth and will continue until the application calls
     * {@link #stopServiceDiscovery}.
     *
     * <p> The function call immediately returns after sending a request to start service
     * discovery to the framework. The application is notified of a success to initiate
     * discovery through the callback {@link DiscoveryListener#onDiscoveryStarted} or a failure
     * through {@link DiscoveryListener#onStartDiscoveryFailed}.
     *
     * <p> Upon successful start, application is notified when a service is found with
     * {@link DiscoveryListener#onServiceFound} or when a service is lost with
     * {@link DiscoveryListener#onServiceLost}.
     *
     * <p> Upon failure to start, service discovery is not active and application does
     * not need to invoke {@link #stopServiceDiscovery}
     *
     * <p> The application should call {@link #stopServiceDiscovery} when discovery of this
     * service type is no longer required, and/or whenever the application is paused or
     * stopped.
     *
     * @param serviceType
     * 		The service type being discovered. Examples include "_http._tcp" for
     * 		http services or "_ipp._tcp" for printers
     * @param protocolType
     * 		The service discovery protocol
     * @param listener
     * 		The listener notifies of a successful discovery and is used
     * 		to stop discovery on this serviceType through a call on {@link #stopServiceDiscovery}.
     * 		Cannot be null. Cannot be in use for an active service discovery.
     */
    public void discoverServices(java.lang.String serviceType, int protocolType, android.net.nsd.NsdManager.DiscoveryListener listener) {
        if (listener == null) {
            throw new java.lang.IllegalArgumentException("listener cannot be null");
        }
        if (android.text.TextUtils.isEmpty(serviceType)) {
            throw new java.lang.IllegalArgumentException("Service type cannot be empty");
        }
        if (protocolType != android.net.nsd.NsdManager.PROTOCOL_DNS_SD) {
            throw new java.lang.IllegalArgumentException("Unsupported protocol");
        }
        android.net.nsd.NsdServiceInfo s = new android.net.nsd.NsdServiceInfo();
        s.setServiceType(serviceType);
        int key = putListener(listener, s);
        if (key == android.net.nsd.NsdManager.BUSY_LISTENER_KEY) {
            throw new java.lang.IllegalArgumentException("listener already in use");
        }
        mAsyncChannel.sendMessage(android.net.nsd.NsdManager.DISCOVER_SERVICES, 0, key, s);
    }

    /**
     * Stop service discovery initiated with {@link #discoverServices}.  An active service
     * discovery is notified to the application with {@link DiscoveryListener#onDiscoveryStarted}
     * and it stays active until the application invokes a stop service discovery. A successful
     * stop is notified to with a call to {@link DiscoveryListener#onDiscoveryStopped}.
     *
     * <p> Upon failure to stop service discovery, application is notified through
     * {@link DiscoveryListener#onStopDiscoveryFailed}.
     *
     * @param listener
     * 		This should be the listener object that was passed to {@link #discoverServices}.
     * 		It identifies the discovery that should be stopped and notifies of a successful or
     * 		unsuccessful stop.  In API versions 20 and above, the listener object may be used for
     * 		another service discovery once the callback has been called.  In API versions <= 19,
     * 		there is no entirely reliable way to know when a listener may be re-used, and a new
     * 		listener should be created for each service discovery request.
     */
    public void stopServiceDiscovery(android.net.nsd.NsdManager.DiscoveryListener listener) {
        int id = getListenerKey(listener);
        if (id == android.net.nsd.NsdManager.INVALID_LISTENER_KEY) {
            throw new java.lang.IllegalArgumentException("service discovery not active on listener");
        }
        if (listener == null) {
            throw new java.lang.IllegalArgumentException("listener cannot be null");
        }
        mAsyncChannel.sendMessage(android.net.nsd.NsdManager.STOP_DISCOVERY, 0, id);
    }

    /**
     * Resolve a discovered service. An application can resolve a service right before
     * establishing a connection to fetch the IP and port details on which to setup
     * the connection.
     *
     * @param serviceInfo
     * 		service to be resolved
     * @param listener
     * 		to receive callback upon success or failure. Cannot be null.
     * 		Cannot be in use for an active service resolution.
     */
    public void resolveService(android.net.nsd.NsdServiceInfo serviceInfo, android.net.nsd.NsdManager.ResolveListener listener) {
        if (android.text.TextUtils.isEmpty(serviceInfo.getServiceName()) || android.text.TextUtils.isEmpty(serviceInfo.getServiceType())) {
            throw new java.lang.IllegalArgumentException("Service name or type cannot be empty");
        }
        if (listener == null) {
            throw new java.lang.IllegalArgumentException("listener cannot be null");
        }
        int key = putListener(listener, serviceInfo);
        if (key == android.net.nsd.NsdManager.BUSY_LISTENER_KEY) {
            throw new java.lang.IllegalArgumentException("listener already in use");
        }
        mAsyncChannel.sendMessage(android.net.nsd.NsdManager.RESOLVE_SERVICE, 0, key, serviceInfo);
    }

    /**
     * Internal use only @hide
     */
    public void setEnabled(boolean enabled) {
        try {
            mService.setEnabled(enabled);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * Get a reference to NetworkService handler. This is used to establish
     * an AsyncChannel communication with the service
     *
     * @return Messenger pointing to the NetworkService handler
     */
    private android.os.Messenger getMessenger() {
        try {
            return mService.getMessenger();
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }
}

