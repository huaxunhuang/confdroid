/**
 * Copyright (C) 2015 The Android Open Source Project
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
package android.support.customtabs;


/**
 * Wrapper class that can be used as a unique identifier for a session. Also contains an accessor
 * for the {@link CustomTabsCallback} for the session if there was any.
 */
public class CustomTabsSessionToken {
    private static final java.lang.String TAG = "CustomTabsSessionToken";

    private final android.support.customtabs.ICustomTabsCallback mCallbackBinder;

    private final android.support.customtabs.CustomTabsCallback mCallback;

    /**
     * Obtain a {@link CustomTabsSessionToken} from an intent. See {@link CustomTabsIntent.Builder}
     * for ways to generate an intent for custom tabs.
     *
     * @param intent
     * 		The intent to generate the token from. This has to include an extra for
     * 		{@link CustomTabsIntent#EXTRA_SESSION}.
     * @return The token that was generated.
     */
    public static android.support.customtabs.CustomTabsSessionToken getSessionTokenFromIntent(android.content.Intent intent) {
        android.os.Bundle b = intent.getExtras();
        android.os.IBinder binder = android.support.v4.app.BundleCompat.getBinder(b, android.support.customtabs.CustomTabsIntent.EXTRA_SESSION);
        if (binder == null)
            return null;

        return new android.support.customtabs.CustomTabsSessionToken(ICustomTabsCallback.Stub.asInterface(binder));
    }

    CustomTabsSessionToken(android.support.customtabs.ICustomTabsCallback callbackBinder) {
        mCallbackBinder = callbackBinder;
        mCallback = new android.support.customtabs.CustomTabsCallback() {
            @java.lang.Override
            public void onNavigationEvent(int navigationEvent, android.os.Bundle extras) {
                try {
                    mCallbackBinder.onNavigationEvent(navigationEvent, extras);
                } catch (android.os.RemoteException e) {
                    android.util.Log.e(android.support.customtabs.CustomTabsSessionToken.TAG, "RemoteException during ICustomTabsCallback transaction");
                }
            }
        };
    }

    android.os.IBinder getCallbackBinder() {
        return mCallbackBinder.asBinder();
    }

    @java.lang.Override
    public int hashCode() {
        return getCallbackBinder().hashCode();
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (!(o instanceof android.support.customtabs.CustomTabsSessionToken))
            return false;

        android.support.customtabs.CustomTabsSessionToken token = ((android.support.customtabs.CustomTabsSessionToken) (o));
        return token.getCallbackBinder().equals(mCallbackBinder.asBinder());
    }

    /**
     *
     *
     * @return {@link CustomTabsCallback} corresponding to this session if there was any non-null
    callbacks passed by the client.
     */
    public android.support.customtabs.CustomTabsCallback getCallback() {
        return mCallback;
    }
}

