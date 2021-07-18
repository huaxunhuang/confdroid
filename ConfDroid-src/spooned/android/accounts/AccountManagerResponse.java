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
 * Used to return a response to the AccountManager.
 *
 * @unknown 
 */
public class AccountManagerResponse implements android.os.Parcelable {
    private android.accounts.IAccountManagerResponse mResponse;

    /**
     *
     *
     * @unknown 
     */
    public AccountManagerResponse(android.accounts.IAccountManagerResponse response) {
        mResponse = response;
    }

    /**
     *
     *
     * @unknown 
     */
    public AccountManagerResponse(android.os.Parcel parcel) {
        mResponse = IAccountManagerResponse.Stub.asInterface(parcel.readStrongBinder());
    }

    public void onResult(android.os.Bundle result) {
        try {
            mResponse.onResult(result);
        } catch (android.os.RemoteException e) {
            // this should never happen
        }
    }

    public void onError(int errorCode, java.lang.String errorMessage) {
        try {
            mResponse.onError(errorCode, errorMessage);
        } catch (android.os.RemoteException e) {
            // this should never happen
        }
    }

    /**
     *
     *
     * @unknown 
     */
    public int describeContents() {
        return 0;
    }

    /**
     *
     *
     * @unknown 
     */
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeStrongBinder(mResponse.asBinder());
    }

    /**
     *
     *
     * @unknown 
     */
    public static final android.os.Parcelable.Creator<android.accounts.AccountManagerResponse> CREATOR = new android.os.Parcelable.Creator<android.accounts.AccountManagerResponse>() {
        public android.accounts.AccountManagerResponse createFromParcel(android.os.Parcel source) {
            return new android.accounts.AccountManagerResponse(source);
        }

        public android.accounts.AccountManagerResponse[] newArray(int size) {
            return new android.accounts.AccountManagerResponse[size];
        }
    };
}

