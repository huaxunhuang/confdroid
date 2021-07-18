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
package android.service.restrictions;


/**
 * Abstract implementation of a Restrictions Provider BroadcastReceiver. To implement a
 * Restrictions Provider, extend from this class and implement the abstract methods.
 * Export this receiver in the manifest. A profile owner device admin can then register this
 * component as a Restrictions Provider using
 * {@link DevicePolicyManager#setRestrictionsProvider(ComponentName, ComponentName)}.
 * <p>
 * The function of a Restrictions Provider is to transport permission requests from apps on this
 * device to an administrator (most likely on a remote device or computer) and deliver back
 * responses. The response should be sent back to the app via
 * {@link RestrictionsManager#notifyPermissionResponse(String, PersistableBundle)}.
 *
 * @see RestrictionsManager
 */
public abstract class RestrictionsReceiver extends android.content.BroadcastReceiver {
    private static final java.lang.String TAG = "RestrictionsReceiver";

    /**
     * An asynchronous permission request made by an application for an operation that requires
     * authorization by a local or remote administrator other than the user. The Restrictions
     * Provider should transfer the request to the administrator and deliver back a response, when
     * available. The calling application is aware that the response could take an indefinite
     * amount of time.
     * <p>
     * If the request bundle contains the key {@link RestrictionsManager#REQUEST_KEY_NEW_REQUEST},
     * then a new request must be sent. Otherwise the provider can look up any previous response
     * to the same requestId and return the cached response.
     *
     * @param packageName
     * 		the application requesting permission.
     * @param requestType
     * 		the type of request, which determines the content and presentation of
     * 		the request data.
     * @param request
     * 		the request data bundle containing at a minimum a request id.
     * @see RestrictionsManager#REQUEST_TYPE_APPROVAL
     * @see RestrictionsManager#REQUEST_TYPE_LOCAL_APPROVAL
     * @see RestrictionsManager#REQUEST_KEY_ID
     */
    public abstract void onRequestPermission(android.content.Context context, java.lang.String packageName, java.lang.String requestType, java.lang.String requestId, android.os.PersistableBundle request);

    /**
     * Intercept standard Restrictions Provider broadcasts.  Implementations
     * should not override this method; it is better to implement the
     * convenience callbacks for each action.
     */
    @java.lang.Override
    public void onReceive(android.content.Context context, android.content.Intent intent) {
        java.lang.String action = intent.getAction();
        if (android.content.RestrictionsManager.ACTION_REQUEST_PERMISSION.equals(action)) {
            java.lang.String packageName = intent.getStringExtra(android.content.RestrictionsManager.EXTRA_PACKAGE_NAME);
            java.lang.String requestType = intent.getStringExtra(android.content.RestrictionsManager.EXTRA_REQUEST_TYPE);
            java.lang.String requestId = intent.getStringExtra(android.content.RestrictionsManager.EXTRA_REQUEST_ID);
            android.os.PersistableBundle request = ((android.os.PersistableBundle) (intent.getParcelableExtra(android.content.RestrictionsManager.EXTRA_REQUEST_BUNDLE)));
            onRequestPermission(context, packageName, requestType, requestId, request);
        }
    }
}

