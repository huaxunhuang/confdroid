/**
 * Copyright (C) 2014 The Android Open Source Project
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
package android.net;


/**
 * A class representing the IP configuration of the Ethernet network.
 *
 * @unknown 
 */
public class EthernetManager {
    private static final java.lang.String TAG = "EthernetManager";

    private static final int MSG_AVAILABILITY_CHANGED = 1000;

    private final android.content.Context mContext;

    private final android.net.IEthernetManager mService;

    private final android.os.Handler mHandler = new android.os.Handler() {
        @java.lang.Override
        public void handleMessage(android.os.Message msg) {
            if (msg.what == android.net.EthernetManager.MSG_AVAILABILITY_CHANGED) {
                boolean isAvailable = msg.arg1 == 1;
                for (android.net.EthernetManager.Listener listener : mListeners) {
                    listener.onAvailabilityChanged(isAvailable);
                }
            }
        }
    };

    private final java.util.ArrayList<android.net.EthernetManager.Listener> mListeners = new java.util.ArrayList<android.net.EthernetManager.Listener>();

    private final IEthernetServiceListener.Stub mServiceListener = new android.net.IEthernetServiceListener.Stub() {
        @java.lang.Override
        public void onAvailabilityChanged(boolean isAvailable) {
            mHandler.obtainMessage(android.net.EthernetManager.MSG_AVAILABILITY_CHANGED, isAvailable ? 1 : 0, 0, null).sendToTarget();
        }
    };

    /**
     * A listener interface to receive notification on changes in Ethernet.
     */
    public interface Listener {
        /**
         * Called when Ethernet port's availability is changed.
         *
         * @param isAvailable
         * 		{@code true} if one or more Ethernet port exists.
         */
        public void onAvailabilityChanged(boolean isAvailable);
    }

    /**
     * Create a new EthernetManager instance.
     * Applications will almost always want to use
     * {@link android.content.Context#getSystemService Context.getSystemService()} to retrieve
     * the standard {@link android.content.Context#ETHERNET_SERVICE Context.ETHERNET_SERVICE}.
     */
    public EthernetManager(android.content.Context context, android.net.IEthernetManager service) {
        mContext = context;
        mService = service;
    }

    /**
     * Get Ethernet configuration.
     *
     * @return the Ethernet Configuration, contained in {@link IpConfiguration}.
     */
    public android.net.IpConfiguration getConfiguration() {
        try {
            return mService.getConfiguration();
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * Set Ethernet configuration.
     */
    public void setConfiguration(android.net.IpConfiguration config) {
        try {
            mService.setConfiguration(config);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * Indicates whether the system currently has one or more
     * Ethernet interfaces.
     */
    public boolean isAvailable() {
        try {
            return mService.isAvailable();
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * Adds a listener.
     *
     * @param listener
     * 		A {@link Listener} to add.
     * @throws IllegalArgumentException
     * 		If the listener is null.
     */
    public void addListener(android.net.EthernetManager.Listener listener) {
        if (listener == null) {
            throw new java.lang.IllegalArgumentException("listener must not be null");
        }
        mListeners.add(listener);
        if (mListeners.size() == 1) {
            try {
                mService.addListener(mServiceListener);
            } catch (android.os.RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }
    }

    /**
     * Removes a listener.
     *
     * @param listener
     * 		A {@link Listener} to remove.
     * @throws IllegalArgumentException
     * 		If the listener is null.
     */
    public void removeListener(android.net.EthernetManager.Listener listener) {
        if (listener == null) {
            throw new java.lang.IllegalArgumentException("listener must not be null");
        }
        mListeners.remove(listener);
        if (mListeners.isEmpty()) {
            try {
                mService.removeListener(mServiceListener);
            } catch (android.os.RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }
    }
}

