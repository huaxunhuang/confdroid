/**
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package android.location;


/**
 * This class provides access to the system country detector service. This
 * service allows applications to obtain the country that the user is in.
 * <p>
 * The country will be detected in order of reliability, like
 * <ul>
 * <li>Mobile network</li>
 * <li>Location</li>
 * <li>SIM's country</li>
 * <li>Phone's locale</li>
 * </ul>
 * <p>
 * Call the {@link #detectCountry()} to get the available country immediately.
 * <p>
 * To be notified of the future country change, use the
 * {@link #addCountryListener}
 * <p>
 * <p>
 * You do not instantiate this class directly; instead, retrieve it through
 * {@link android.content.Context#getSystemService
 * Context.getSystemService(Context.COUNTRY_DETECTOR)}.
 *
 * @unknown 
 */
public class CountryDetector {
    /**
     * The class to wrap the ICountryListener.Stub and CountryListener objects
     * together. The CountryListener will be notified through the specific
     * looper once the country changed and detected.
     */
    private static final class ListenerTransport extends android.location.ICountryListener.Stub {
        private final android.location.CountryListener mListener;

        private final android.os.Handler mHandler;

        public ListenerTransport(android.location.CountryListener listener, android.os.Looper looper) {
            mListener = listener;
            if (looper != null) {
                mHandler = new android.os.Handler(looper);
            } else {
                mHandler = new android.os.Handler();
            }
        }

        public void onCountryDetected(final android.location.Country country) {
            mHandler.post(new java.lang.Runnable() {
                public void run() {
                    mListener.onCountryDetected(country);
                }
            });
        }
    }

    private static final java.lang.String TAG = "CountryDetector";

    private final android.location.ICountryDetector mService;

    private final java.util.HashMap<android.location.CountryListener, android.location.CountryDetector.ListenerTransport> mListeners;

    /**
     *
     *
     * @unknown - hide this constructor because it has a parameter of type
    ICountryDetector, which is a system private class. The right way to
    create an instance of this class is using the factory
    Context.getSystemService.
     */
    public CountryDetector(android.location.ICountryDetector service) {
        mService = service;
        mListeners = new java.util.HashMap<android.location.CountryListener, android.location.CountryDetector.ListenerTransport>();
    }

    /**
     * Start detecting the country that the user is in.
     *
     * @return the country if it is available immediately, otherwise null will
    be returned.
     */
    public android.location.Country detectCountry() {
        try {
            return mService.detectCountry();
        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.location.CountryDetector.TAG, "detectCountry: RemoteException", e);
            return null;
        }
    }

    /**
     * Add a listener to receive the notification when the country is detected
     * or changed.
     *
     * @param listener
     * 		will be called when the country is detected or changed.
     * @param looper
     * 		a Looper object whose message queue will be used to
     * 		implement the callback mechanism. If looper is null then the
     * 		callbacks will be called on the main thread.
     */
    public void addCountryListener(android.location.CountryListener listener, android.os.Looper looper) {
        synchronized(mListeners) {
            if (!mListeners.containsKey(listener)) {
                android.location.CountryDetector.ListenerTransport transport = new android.location.CountryDetector.ListenerTransport(listener, looper);
                try {
                    mService.addCountryListener(transport);
                    mListeners.put(listener, transport);
                } catch (android.os.RemoteException e) {
                    android.util.Log.e(android.location.CountryDetector.TAG, "addCountryListener: RemoteException", e);
                }
            }
        }
    }

    /**
     * Remove the listener
     */
    public void removeCountryListener(android.location.CountryListener listener) {
        synchronized(mListeners) {
            android.location.CountryDetector.ListenerTransport transport = mListeners.get(listener);
            if (transport != null) {
                try {
                    mListeners.remove(listener);
                    mService.removeCountryListener(transport);
                } catch (android.os.RemoteException e) {
                    android.util.Log.e(android.location.CountryDetector.TAG, "removeCountryListener: RemoteException", e);
                }
            }
        }
    }
}

