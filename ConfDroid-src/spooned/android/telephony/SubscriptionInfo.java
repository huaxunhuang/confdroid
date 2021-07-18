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
 * A Parcelable class for Subscription Information.
 */
public class SubscriptionInfo implements android.os.Parcelable {
    /**
     * Size of text to render on the icon.
     */
    private static final int TEXT_SIZE = 16;

    /**
     * Subscription Identifier, this is a device unique number
     * and not an index into an array
     */
    private int mId;

    /**
     * The GID for a SIM that maybe associated with this subscription, empty if unknown
     */
    private java.lang.String mIccId;

    /**
     * The index of the slot that currently contains the subscription
     * and not necessarily unique and maybe INVALID_SLOT_ID if unknown
     */
    private int mSimSlotIndex;

    /**
     * The name displayed to the user that identifies this subscription
     */
    private java.lang.CharSequence mDisplayName;

    /**
     * String that identifies SPN/PLMN
     * TODO : Add a new field that identifies only SPN for a sim
     */
    private java.lang.CharSequence mCarrierName;

    /**
     * The source of the name, NAME_SOURCE_UNDEFINED, NAME_SOURCE_DEFAULT_SOURCE,
     * NAME_SOURCE_SIM_SOURCE or NAME_SOURCE_USER_INPUT.
     */
    private int mNameSource;

    /**
     * The color to be used for tinting the icon when displaying to the user
     */
    private int mIconTint;

    /**
     * A number presented to the user identify this subscription
     */
    private java.lang.String mNumber;

    /**
     * Data roaming state, DATA_RAOMING_ENABLE, DATA_RAOMING_DISABLE
     */
    private int mDataRoaming;

    /**
     * SIM Icon bitmap
     */
    private android.graphics.Bitmap mIconBitmap;

    /**
     * Mobile Country Code
     */
    private int mMcc;

    /**
     * Mobile Network Code
     */
    private int mMnc;

    /**
     * ISO Country code for the subscription's provider
     */
    private java.lang.String mCountryIso;

    /**
     *
     *
     * @unknown 
     */
    public SubscriptionInfo(int id, java.lang.String iccId, int simSlotIndex, java.lang.CharSequence displayName, java.lang.CharSequence carrierName, int nameSource, int iconTint, java.lang.String number, int roaming, android.graphics.Bitmap icon, int mcc, int mnc, java.lang.String countryIso) {
        this.mId = id;
        this.mIccId = iccId;
        this.mSimSlotIndex = simSlotIndex;
        this.mDisplayName = displayName;
        this.mCarrierName = carrierName;
        this.mNameSource = nameSource;
        this.mIconTint = iconTint;
        this.mNumber = number;
        this.mDataRoaming = roaming;
        this.mIconBitmap = icon;
        this.mMcc = mcc;
        this.mMnc = mnc;
        this.mCountryIso = countryIso;
    }

    /**
     *
     *
     * @return the subscription ID.
     */
    public int getSubscriptionId() {
        return this.mId;
    }

    /**
     *
     *
     * @return the ICC ID.
     */
    public java.lang.String getIccId() {
        return this.mIccId;
    }

    /**
     *
     *
     * @return the slot index of this Subscription's SIM card.
     */
    public int getSimSlotIndex() {
        return this.mSimSlotIndex;
    }

    /**
     *
     *
     * @return the name displayed to the user that identifies this subscription
     */
    public java.lang.CharSequence getDisplayName() {
        return this.mDisplayName;
    }

    /**
     * Sets the name displayed to the user that identifies this subscription
     *
     * @unknown 
     */
    public void setDisplayName(java.lang.CharSequence name) {
        this.mDisplayName = name;
    }

    /**
     *
     *
     * @return the name displayed to the user that identifies Subscription provider name
     */
    public java.lang.CharSequence getCarrierName() {
        return this.mCarrierName;
    }

    /**
     * Sets the name displayed to the user that identifies Subscription provider name
     *
     * @unknown 
     */
    public void setCarrierName(java.lang.CharSequence name) {
        this.mCarrierName = name;
    }

    /**
     *
     *
     * @return the source of the name, eg NAME_SOURCE_UNDEFINED, NAME_SOURCE_DEFAULT_SOURCE,
    NAME_SOURCE_SIM_SOURCE or NAME_SOURCE_USER_INPUT.
     * @unknown 
     */
    public int getNameSource() {
        return this.mNameSource;
    }

    /**
     * Creates and returns an icon {@code Bitmap} to represent this {@code SubscriptionInfo} in a user
     * interface.
     *
     * @param context
     * 		A {@code Context} to get the {@code DisplayMetrics}s from.
     * @return A bitmap icon for this {@code SubscriptionInfo}.
     */
    public android.graphics.Bitmap createIconBitmap(android.content.Context context) {
        int width = mIconBitmap.getWidth();
        int height = mIconBitmap.getHeight();
        android.util.DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        // Create a new bitmap of the same size because it will be modified.
        android.graphics.Bitmap workingBitmap = android.graphics.Bitmap.createBitmap(metrics, width, height, mIconBitmap.getConfig());
        android.graphics.Canvas canvas = new android.graphics.Canvas(workingBitmap);
        android.graphics.Paint paint = new android.graphics.Paint();
        // Tint the icon with the color.
        paint.setColorFilter(new android.graphics.PorterDuffColorFilter(mIconTint, android.graphics.PorterDuff.Mode.SRC_ATOP));
        canvas.drawBitmap(mIconBitmap, 0, 0, paint);
        paint.setColorFilter(null);
        // Write the sim slot index.
        paint.setAntiAlias(true);
        paint.setTypeface(android.graphics.Typeface.create("sans-serif", android.graphics.Typeface.NORMAL));
        paint.setColor(android.graphics.Color.WHITE);
        // Set text size scaled by density
        paint.setTextSize(android.telephony.SubscriptionInfo.TEXT_SIZE * metrics.density);
        // Convert sim slot index to localized string
        final java.lang.String index = java.lang.String.format("%d", mSimSlotIndex + 1);
        final android.graphics.Rect textBound = new android.graphics.Rect();
        paint.getTextBounds(index, 0, 1, textBound);
        final float xOffset = (width / 2.0F) - textBound.centerX();
        final float yOffset = (height / 2.0F) - textBound.centerY();
        canvas.drawText(index, xOffset, yOffset, paint);
        return workingBitmap;
    }

    /**
     * A highlight color to use in displaying information about this {@code PhoneAccount}.
     *
     * @return A hexadecimal color value.
     */
    public int getIconTint() {
        return mIconTint;
    }

    /**
     * Sets the color displayed to the user that identifies this subscription
     *
     * @unknown 
     */
    public void setIconTint(int iconTint) {
        this.mIconTint = iconTint;
    }

    /**
     *
     *
     * @return the number of this subscription.
     */
    public java.lang.String getNumber() {
        return mNumber;
    }

    /**
     *
     *
     * @return the data roaming state for this subscription, either
    {@link SubscriptionManager#DATA_ROAMING_ENABLE} or {@link SubscriptionManager#DATA_ROAMING_DISABLE}.
     */
    public int getDataRoaming() {
        return this.mDataRoaming;
    }

    /**
     *
     *
     * @return the MCC.
     */
    public int getMcc() {
        return this.mMcc;
    }

    /**
     *
     *
     * @return the MNC.
     */
    public int getMnc() {
        return this.mMnc;
    }

    /**
     *
     *
     * @return the ISO country code
     */
    public java.lang.String getCountryIso() {
        return this.mCountryIso;
    }

    public static final android.os.Parcelable.Creator<android.telephony.SubscriptionInfo> CREATOR = new android.os.Parcelable.Creator<android.telephony.SubscriptionInfo>() {
        @java.lang.Override
        public android.telephony.SubscriptionInfo createFromParcel(android.os.Parcel source) {
            int id = source.readInt();
            java.lang.String iccId = source.readString();
            int simSlotIndex = source.readInt();
            java.lang.CharSequence displayName = source.readCharSequence();
            java.lang.CharSequence carrierName = source.readCharSequence();
            int nameSource = source.readInt();
            int iconTint = source.readInt();
            java.lang.String number = source.readString();
            int dataRoaming = source.readInt();
            int mcc = source.readInt();
            int mnc = source.readInt();
            java.lang.String countryIso = source.readString();
            android.graphics.Bitmap iconBitmap = android.graphics.Bitmap.CREATOR.createFromParcel(source);
            return new android.telephony.SubscriptionInfo(id, iccId, simSlotIndex, displayName, carrierName, nameSource, iconTint, number, dataRoaming, iconBitmap, mcc, mnc, countryIso);
        }

        @java.lang.Override
        public android.telephony.SubscriptionInfo[] newArray(int size) {
            return new android.telephony.SubscriptionInfo[size];
        }
    };

    @java.lang.Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeString(mIccId);
        dest.writeInt(mSimSlotIndex);
        dest.writeCharSequence(mDisplayName);
        dest.writeCharSequence(mCarrierName);
        dest.writeInt(mNameSource);
        dest.writeInt(mIconTint);
        dest.writeString(mNumber);
        dest.writeInt(mDataRoaming);
        dest.writeInt(mMcc);
        dest.writeInt(mMnc);
        dest.writeString(mCountryIso);
        mIconBitmap.writeToParcel(dest, flags);
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    /**
     *
     *
     * @unknown 
     */
    public static java.lang.String givePrintableIccid(java.lang.String iccId) {
        java.lang.String iccIdToPrint = null;
        if (iccId != null) {
            if ((iccId.length() > 9) && (!android.os.Build.IS_DEBUGGABLE)) {
                iccIdToPrint = iccId.substring(0, 9) + android.telephony.Rlog.pii(false, iccId.substring(9));
            } else {
                iccIdToPrint = iccId;
            }
        }
        return iccIdToPrint;
    }

    @java.lang.Override
    public java.lang.String toString() {
        java.lang.String iccIdToPrint = android.telephony.SubscriptionInfo.givePrintableIccid(mIccId);
        return ((((((((((((((((((((("{id=" + mId) + ", iccId=") + iccIdToPrint) + " simSlotIndex=") + mSimSlotIndex) + " displayName=") + mDisplayName) + " carrierName=") + mCarrierName) + " nameSource=") + mNameSource) + " iconTint=") + mIconTint) + " dataRoaming=") + mDataRoaming) + " iconBitmap=") + mIconBitmap) + " mcc ") + mMcc) + " mnc ") + mMnc) + "}";
    }
}

