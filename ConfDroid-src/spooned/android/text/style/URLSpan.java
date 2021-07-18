/**
 * Copyright (C) 2006 The Android Open Source Project
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
package android.text.style;


public class URLSpan extends android.text.style.ClickableSpan implements android.text.ParcelableSpan {
    private final java.lang.String mURL;

    public URLSpan(java.lang.String url) {
        mURL = url;
    }

    public URLSpan(android.os.Parcel src) {
        mURL = src.readString();
    }

    public int getSpanTypeId() {
        return getSpanTypeIdInternal();
    }

    /**
     *
     *
     * @unknown 
     */
    public int getSpanTypeIdInternal() {
        return android.text.TextUtils.URL_SPAN;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(android.os.Parcel dest, int flags) {
        writeToParcelInternal(dest, flags);
    }

    /**
     *
     *
     * @unknown 
     */
    public void writeToParcelInternal(android.os.Parcel dest, int flags) {
        dest.writeString(mURL);
    }

    public java.lang.String getURL() {
        return mURL;
    }

    @java.lang.Override
    public void onClick(android.view.View widget) {
        android.net.Uri uri = android.net.Uri.parse(getURL());
        android.content.Context context = widget.getContext();
        android.content.Intent intent = new android.content.Intent(android.content.Intent.ACTION_VIEW, uri);
        intent.putExtra(android.provider.Browser.EXTRA_APPLICATION_ID, context.getPackageName());
        try {
            context.startActivity(intent);
        } catch (android.content.ActivityNotFoundException e) {
            android.util.Log.w("URLSpan", "Actvity was not found for intent, " + intent.toString());
        }
    }
}

