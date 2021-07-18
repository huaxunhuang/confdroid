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
package android.telephony;


/**
 * Contains LTE network state related information.
 *
 * @unknown 
 */
public final class VoLteServiceState implements android.os.Parcelable {
    private static final java.lang.String LOG_TAG = "VoLteServiceState";

    private static final boolean DBG = false;

    // Use int max, as -1 is a valid value in signal strength
    public static final int INVALID = 0x7fffffff;

    public static final int NOT_SUPPORTED = 0;

    public static final int SUPPORTED = 1;

    // Single Radio Voice Call Continuity(SRVCC) progress state
    public static final int HANDOVER_STARTED = 0;

    public static final int HANDOVER_COMPLETED = 1;

    public static final int HANDOVER_FAILED = 2;

    public static final int HANDOVER_CANCELED = 3;

    private int mSrvccState;

    /**
     * Create a new VoLteServiceState from a intent notifier Bundle
     *
     * This method is used by PhoneStateIntentReceiver and maybe by
     * external applications.
     *
     * @param m
     * 		Bundle from intent notifier
     * @return newly created VoLteServiceState
     * @unknown 
     */
    public static android.telephony.VoLteServiceState newFromBundle(android.os.Bundle m) {
        android.telephony.VoLteServiceState ret;
        ret = new android.telephony.VoLteServiceState();
        ret.setFromNotifierBundle(m);
        return ret;
    }

    /**
     * Empty constructor
     *
     * @unknown 
     */
    public VoLteServiceState() {
        initialize();
    }

    /**
     * Constructor
     *
     * @unknown 
     */
    public VoLteServiceState(int srvccState) {
        initialize();
        mSrvccState = srvccState;
    }

    /**
     * Copy constructors
     *
     * @param s
     * 		Source VoLteServiceState
     * @unknown 
     */
    public VoLteServiceState(android.telephony.VoLteServiceState s) {
        copyFrom(s);
    }

    /**
     * Initialize values to defaults.
     *
     * @unknown 
     */
    private void initialize() {
        mSrvccState = android.telephony.VoLteServiceState.INVALID;
    }

    /**
     *
     *
     * @unknown 
     */
    protected void copyFrom(android.telephony.VoLteServiceState s) {
        mSrvccState = s.mSrvccState;
    }

    /**
     * Construct a VoLteServiceState object from the given parcel.
     *
     * @unknown 
     */
    public VoLteServiceState(android.os.Parcel in) {
        if (android.telephony.VoLteServiceState.DBG)
            android.telephony.VoLteServiceState.log("Size of VoLteServiceState parcel:" + in.dataSize());

        mSrvccState = in.readInt();
    }

    /**
     * {@link Parcelable#writeToParcel}
     */
    public void writeToParcel(android.os.Parcel out, int flags) {
        out.writeInt(mSrvccState);
    }

    /**
     * {@link Parcelable#describeContents}
     */
    public int describeContents() {
        return 0;
    }

    /**
     * {@link Parcelable.Creator}
     *
     * @unknown 
     */
    public static final android.os.Parcelable.Creator<android.telephony.VoLteServiceState> CREATOR = new android.os.Parcelable.Creator() {
        public android.telephony.VoLteServiceState createFromParcel(android.os.Parcel in) {
            return new android.telephony.VoLteServiceState(in);
        }

        public android.telephony.VoLteServiceState[] newArray(int size) {
            return new android.telephony.VoLteServiceState[size];
        }
    };

    /**
     * Validate the individual fields as per the range
     * specified in ril.h
     * Set to invalid any field that is not in the valid range
     *
     * @return Valid values for all fields
     * @unknown 
     */
    public void validateInput() {
    }

    public int hashCode() {
        int primeNum = 31;
        return mSrvccState * primeNum;
    }

    /**
     *
     *
     * @return true if the LTE network states are the same
     */
    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        android.telephony.VoLteServiceState s;
        try {
            s = ((android.telephony.VoLteServiceState) (o));
        } catch (java.lang.ClassCastException ex) {
            return false;
        }
        if (o == null) {
            return false;
        }
        return mSrvccState == s.mSrvccState;
    }

    /**
     *
     *
     * @return string representation.
     */
    @java.lang.Override
    public java.lang.String toString() {
        return ("VoLteServiceState:" + " ") + mSrvccState;
    }

    /**
     * Set VoLteServiceState based on intent notifier map
     *
     * @param m
     * 		intent notifier map
     * @unknown 
     */
    private void setFromNotifierBundle(android.os.Bundle m) {
        mSrvccState = m.getInt("mSrvccState");
    }

    /**
     * Set intent notifier Bundle based on VoLteServiceState
     *
     * @param m
     * 		intent notifier Bundle
     * @unknown 
     */
    public void fillInNotifierBundle(android.os.Bundle m) {
        m.putInt("mSrvccState", mSrvccState);
    }

    public int getSrvccState() {
        return mSrvccState;
    }

    /**
     * log
     */
    private static void log(java.lang.String s) {
        android.telephony.Rlog.w(android.telephony.VoLteServiceState.LOG_TAG, s);
    }
}

