/**
 * Copyright (C) 2009 The Android Open Source Project
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
package android.accounts;


/**
 * Object used to communicate responses back to the AccountManager
 */
public class AccountAuthenticatorResponse implements android.os.Parcelable {
    private static final java.lang.String TAG = "AccountAuthenticator";

    private android.accounts.IAccountAuthenticatorResponse mAccountAuthenticatorResponse;

    /**
     *
     *
     * @unknown 
     */
    public AccountAuthenticatorResponse(android.accounts.IAccountAuthenticatorResponse response) {
        mAccountAuthenticatorResponse = response;
    }

    public AccountAuthenticatorResponse(android.os.Parcel parcel) {
        mAccountAuthenticatorResponse = IAccountAuthenticatorResponse.Stub.asInterface(parcel.readStrongBinder());
    }

    public void onResult(android.os.Bundle result) {
        if (android.util.Log.isLoggable(android.accounts.AccountAuthenticatorResponse.TAG, android.util.Log.VERBOSE)) {
            result.keySet();// force it to be unparcelled

            android.util.Log.v(android.accounts.AccountAuthenticatorResponse.TAG, "AccountAuthenticatorResponse.onResult: " + android.accounts.AccountManager.sanitizeResult(result));
        }
        try {
            mAccountAuthenticatorResponse.onResult(result);
        } catch (android.os.RemoteException e) {
            // this should never happen
        }
    }

    public void onRequestContinued() {
        if (android.util.Log.isLoggable(android.accounts.AccountAuthenticatorResponse.TAG, android.util.Log.VERBOSE)) {
            android.util.Log.v(android.accounts.AccountAuthenticatorResponse.TAG, "AccountAuthenticatorResponse.onRequestContinued");
        }
        try {
            mAccountAuthenticatorResponse.onRequestContinued();
        } catch (android.os.RemoteException e) {
            // this should never happen
        }
    }

    public void onError(int errorCode, java.lang.String errorMessage) {
        if (android.util.Log.isLoggable(android.accounts.AccountAuthenticatorResponse.TAG, android.util.Log.VERBOSE)) {
            android.util.Log.v(android.accounts.AccountAuthenticatorResponse.TAG, (("AccountAuthenticatorResponse.onError: " + errorCode) + ", ") + errorMessage);
        }
        try {
            mAccountAuthenticatorResponse.onError(errorCode, errorMessage);
        } catch (android.os.RemoteException e) {
            // this should never happen
        }
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeStrongBinder(mAccountAuthenticatorResponse.asBinder());
    }

    public static final android.os.Parcelable.Creator<android.accounts.AccountAuthenticatorResponse> CREATOR = new android.os.Parcelable.Creator<android.accounts.AccountAuthenticatorResponse>() {
        public android.accounts.AccountAuthenticatorResponse createFromParcel(android.os.Parcel source) {
            return new android.accounts.AccountAuthenticatorResponse(source);
        }

        public android.accounts.AccountAuthenticatorResponse[] newArray(int size) {
            return new android.accounts.AccountAuthenticatorResponse[size];
        }
    };
}

