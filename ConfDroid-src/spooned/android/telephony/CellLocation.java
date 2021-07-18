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
package android.telephony;


/**
 * Abstract class that represents the location of the device.  {@more }
 */
public abstract class CellLocation {
    /**
     * Request an update of the current location.  If the location has changed,
     * a broadcast will be sent to everyone registered with {@link PhoneStateListener#LISTEN_CELL_LOCATION}.
     */
    public static void requestLocationUpdate() {
        try {
            com.android.internal.telephony.ITelephony phone = ITelephony.Stub.asInterface(android.os.ServiceManager.getService("phone"));
            if (phone != null) {
                phone.updateServiceLocation();
            }
        } catch (android.os.RemoteException ex) {
            // ignore it
        }
    }

    /**
     * Create a new CellLocation from a intent notifier Bundle
     *
     * This method is used by PhoneStateIntentReceiver and maybe by
     * external applications.
     *
     * @param bundle
     * 		Bundle from intent notifier
     * @return newly created CellLocation
     * @unknown 
     */
    public static android.telephony.CellLocation newFromBundle(android.os.Bundle bundle) {
        // TelephonyManager.getDefault().getCurrentPhoneType() handles the case when
        // ITelephony interface is not up yet.
        switch (android.telephony.TelephonyManager.getDefault().getCurrentPhoneType()) {
            case com.android.internal.telephony.PhoneConstants.PHONE_TYPE_CDMA :
                return new android.telephony.cdma.CdmaCellLocation(bundle);
            case com.android.internal.telephony.PhoneConstants.PHONE_TYPE_GSM :
                return new android.telephony.gsm.GsmCellLocation(bundle);
            default :
                return null;
        }
    }

    /**
     *
     *
     * @unknown 
     */
    public abstract void fillInNotifierBundle(android.os.Bundle bundle);

    /**
     *
     *
     * @unknown 
     */
    public abstract boolean isEmpty();

    /**
     * Invalidate this object.  The location area code and the cell id are set to -1.
     *
     * @unknown 
     */
    public abstract void setStateInvalid();

    /**
     * Return a new CellLocation object representing an unknown
     * location, or null for unknown/none phone radio types.
     */
    public static android.telephony.CellLocation getEmpty() {
        // TelephonyManager.getDefault().getCurrentPhoneType() handles the case when
        // ITelephony interface is not up yet.
        switch (android.telephony.TelephonyManager.getDefault().getCurrentPhoneType()) {
            case com.android.internal.telephony.PhoneConstants.PHONE_TYPE_CDMA :
                return new android.telephony.cdma.CdmaCellLocation();
            case com.android.internal.telephony.PhoneConstants.PHONE_TYPE_GSM :
                return new android.telephony.gsm.GsmCellLocation();
            default :
                return null;
        }
    }
}

