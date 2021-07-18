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
package android.webkit;


/**
 *
 *
 * @unknown 
 */
public final class WebViewProviderResponse implements android.os.Parcelable {
    public WebViewProviderResponse(android.content.pm.PackageInfo packageInfo, int status) {
        this.packageInfo = packageInfo;
        this.status = status;
    }

    // aidl stuff
    public static final android.os.Parcelable.Creator<android.webkit.WebViewProviderResponse> CREATOR = new android.os.Parcelable.Creator<android.webkit.WebViewProviderResponse>() {
        public android.webkit.WebViewProviderResponse createFromParcel(android.os.Parcel in) {
            return new android.webkit.WebViewProviderResponse(in);
        }

        public android.webkit.WebViewProviderResponse[] newArray(int size) {
            return new android.webkit.WebViewProviderResponse[size];
        }
    };

    private WebViewProviderResponse(android.os.Parcel in) {
        packageInfo = in.readTypedObject(android.content.pm.PackageInfo.CREATOR);
        status = in.readInt();
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel out, int flags) {
        out.writeTypedObject(packageInfo, flags);
        out.writeInt(status);
    }

    public final android.content.pm.PackageInfo packageInfo;

    public final int status;
}

