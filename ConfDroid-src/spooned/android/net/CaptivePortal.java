/**
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed urnder the Apache License, Version 2.0 (the "License");
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
 * A class allowing apps handling the {@link ConnectivityManager#ACTION_CAPTIVE_PORTAL_SIGN_IN}
 * activity to indicate to the system different outcomes of captive portal sign in.  This class is
 * passed as an extra named {@link ConnectivityManager#EXTRA_CAPTIVE_PORTAL} with the
 * {@code ACTION_CAPTIVE_PORTAL_SIGN_IN} activity.
 */
public class CaptivePortal implements android.os.Parcelable {
    /**
     *
     *
     * @unknown 
     */
    public static final int APP_RETURN_DISMISSED = 0;

    /**
     *
     *
     * @unknown 
     */
    public static final int APP_RETURN_UNWANTED = 1;

    /**
     *
     *
     * @unknown 
     */
    public static final int APP_RETURN_WANTED_AS_IS = 2;

    private final android.os.IBinder mBinder;

    /**
     *
     *
     * @unknown 
     */
    public CaptivePortal(android.os.IBinder binder) {
        mBinder = binder;
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel out, int flags) {
        out.writeStrongBinder(mBinder);
    }

    public static final android.os.Parcelable.Creator<android.net.CaptivePortal> CREATOR = new android.os.Parcelable.Creator<android.net.CaptivePortal>() {
        @java.lang.Override
        public android.net.CaptivePortal createFromParcel(android.os.Parcel in) {
            return new android.net.CaptivePortal(in.readStrongBinder());
        }

        @java.lang.Override
        public android.net.CaptivePortal[] newArray(int size) {
            return new android.net.CaptivePortal[size];
        }
    };

    /**
     * Indicate to the system that the captive portal has been
     * dismissed.  In response the framework will re-evaluate the network's
     * connectivity and might take further action thereafter.
     */
    public void reportCaptivePortalDismissed() {
        try {
            ICaptivePortal.Stub.asInterface(mBinder).appResponse(android.net.CaptivePortal.APP_RETURN_DISMISSED);
        } catch (android.os.RemoteException e) {
        }
    }

    /**
     * Indicate to the system that the user does not want to pursue signing in to the
     * captive portal and the system should continue to prefer other networks
     * without captive portals for use as the default active data network.  The
     * system will not retest the network for a captive portal so as to avoid
     * disturbing the user with further sign in to network notifications.
     */
    public void ignoreNetwork() {
        try {
            ICaptivePortal.Stub.asInterface(mBinder).appResponse(android.net.CaptivePortal.APP_RETURN_UNWANTED);
        } catch (android.os.RemoteException e) {
        }
    }

    /**
     * Indicate to the system the user wants to use this network as is, even though
     * the captive portal is still in place.  The system will treat the network
     * as if it did not have a captive portal when selecting the network to use
     * as the default active data network. This may result in this network
     * becoming the default active data network, which could disrupt network
     * connectivity for apps because the captive portal is still in place.
     *
     * @unknown 
     */
    public void useNetwork() {
        try {
            ICaptivePortal.Stub.asInterface(mBinder).appResponse(android.net.CaptivePortal.APP_RETURN_WANTED_AS_IS);
        } catch (android.os.RemoteException e) {
        }
    }
}

