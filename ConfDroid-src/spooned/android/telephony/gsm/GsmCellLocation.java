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
package android.telephony.gsm;


/**
 * Represents the cell location on a GSM phone.
 */
public class GsmCellLocation extends android.telephony.CellLocation {
    private int mLac;

    private int mCid;

    private int mPsc;

    /**
     * Empty constructor.  Initializes the LAC and CID to -1.
     */
    public GsmCellLocation() {
        mLac = -1;
        mCid = -1;
        mPsc = -1;
    }

    /**
     * Initialize the object from a bundle.
     */
    public GsmCellLocation(android.os.Bundle bundle) {
        mLac = bundle.getInt("lac", -1);
        mCid = bundle.getInt("cid", -1);
        mPsc = bundle.getInt("psc", -1);
    }

    /**
     *
     *
     * @return gsm location area code, -1 if unknown, 0xffff max legal value
     */
    public int getLac() {
        return mLac;
    }

    /**
     *
     *
     * @return gsm cell id, -1 if unknown, 0xffff max legal value
     */
    public int getCid() {
        return mCid;
    }

    /**
     * On a UMTS network, returns the primary scrambling code of the serving
     * cell.
     *
     * @return primary scrambling code for UMTS, -1 if unknown or GSM
     */
    public int getPsc() {
        return mPsc;
    }

    /**
     * Invalidate this object.  The location area code and the cell id are set to -1.
     */
    @java.lang.Override
    public void setStateInvalid() {
        mLac = -1;
        mCid = -1;
        mPsc = -1;
    }

    /**
     * Set the location area code and the cell id.
     */
    public void setLacAndCid(int lac, int cid) {
        mLac = lac;
        mCid = cid;
    }

    /**
     * Set the primary scrambling code.
     *
     * @unknown 
     */
    public void setPsc(int psc) {
        mPsc = psc;
    }

    @java.lang.Override
    public int hashCode() {
        return mLac ^ mCid;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        android.telephony.gsm.GsmCellLocation s;
        try {
            s = ((android.telephony.gsm.GsmCellLocation) (o));
        } catch (java.lang.ClassCastException ex) {
            return false;
        }
        if (o == null) {
            return false;
        }
        return (android.telephony.gsm.GsmCellLocation.equalsHandlesNulls(mLac, s.mLac) && android.telephony.gsm.GsmCellLocation.equalsHandlesNulls(mCid, s.mCid)) && android.telephony.gsm.GsmCellLocation.equalsHandlesNulls(mPsc, s.mPsc);
    }

    @java.lang.Override
    public java.lang.String toString() {
        return ((((("[" + mLac) + ",") + mCid) + ",") + mPsc) + "]";
    }

    /**
     * Test whether two objects hold the same data values or both are null
     *
     * @param a
     * 		first obj
     * @param b
     * 		second obj
     * @return true if two objects equal or both are null
     */
    private static boolean equalsHandlesNulls(java.lang.Object a, java.lang.Object b) {
        return a == null ? b == null : a.equals(b);
    }

    /**
     * Set intent notifier Bundle based on service state
     *
     * @param m
     * 		intent notifier Bundle
     */
    public void fillInNotifierBundle(android.os.Bundle m) {
        m.putInt("lac", mLac);
        m.putInt("cid", mCid);
        m.putInt("psc", mPsc);
    }

    /**
     *
     *
     * @unknown 
     */
    public boolean isEmpty() {
        return ((mLac == (-1)) && (mCid == (-1))) && (mPsc == (-1));
    }
}

