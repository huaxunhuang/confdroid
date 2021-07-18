/**
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package android.telephony;


/**
 * Application wrapper for {@link SmsCbMessage}. This is Parcelable so that
 * decoded broadcast message objects can be passed between running Services.
 * New broadcasts are received by the CellBroadcastReceiver app, which exports
 * the database of previously received broadcasts at "content://cellbroadcasts/".
 * The "android.permission.READ_CELL_BROADCASTS" permission is required to read
 * from the ContentProvider, and writes to the database are not allowed.<p>
 *
 * Use {@link #createFromCursor} to create CellBroadcastMessage objects from rows
 * in the database cursor returned by the ContentProvider.
 *
 * {@hide }
 */
public class CellBroadcastMessage implements android.os.Parcelable {
    /**
     * Identifier for getExtra() when adding this object to an Intent.
     */
    public static final java.lang.String SMS_CB_MESSAGE_EXTRA = "com.android.cellbroadcastreceiver.SMS_CB_MESSAGE";

    /**
     * SmsCbMessage.
     */
    private final android.telephony.SmsCbMessage mSmsCbMessage;

    private final long mDeliveryTime;

    private boolean mIsRead;

    /**
     * Indicates the subId
     *
     * @unknown 
     */
    private int mSubId = 0;

    /**
     * set Subscription information
     *
     * @unknown 
     */
    public void setSubId(int subId) {
        mSubId = subId;
    }

    /**
     * get Subscription information
     *
     * @unknown 
     */
    public int getSubId() {
        return mSubId;
    }

    public CellBroadcastMessage(android.telephony.SmsCbMessage message) {
        mSmsCbMessage = message;
        mDeliveryTime = java.lang.System.currentTimeMillis();
        mIsRead = false;
    }

    private CellBroadcastMessage(android.telephony.SmsCbMessage message, long deliveryTime, boolean isRead) {
        mSmsCbMessage = message;
        mDeliveryTime = deliveryTime;
        mIsRead = isRead;
    }

    private CellBroadcastMessage(android.os.Parcel in) {
        mSmsCbMessage = new android.telephony.SmsCbMessage(in);
        mDeliveryTime = in.readLong();
        mIsRead = in.readInt() != 0;
        mSubId = in.readInt();
    }

    /**
     * Parcelable: no special flags.
     */
    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel out, int flags) {
        mSmsCbMessage.writeToParcel(out, flags);
        out.writeLong(mDeliveryTime);
        out.writeInt(mIsRead ? 1 : 0);
        out.writeInt(mSubId);
    }

    public static final android.os.Parcelable.Creator<android.telephony.CellBroadcastMessage> CREATOR = new android.os.Parcelable.Creator<android.telephony.CellBroadcastMessage>() {
        @java.lang.Override
        public android.telephony.CellBroadcastMessage createFromParcel(android.os.Parcel in) {
            return new android.telephony.CellBroadcastMessage(in);
        }

        @java.lang.Override
        public android.telephony.CellBroadcastMessage[] newArray(int size) {
            return new android.telephony.CellBroadcastMessage[size];
        }
    };

    /**
     * Create a CellBroadcastMessage from a row in the database.
     *
     * @param cursor
     * 		an open SQLite cursor pointing to the row to read
     * @return the new CellBroadcastMessage
     * @throws IllegalArgumentException
     * 		if one of the required columns is missing
     */
    public static android.telephony.CellBroadcastMessage createFromCursor(android.database.Cursor cursor) {
        int geoScope = cursor.getInt(cursor.getColumnIndexOrThrow(android.provider.Telephony.CellBroadcasts.GEOGRAPHICAL_SCOPE));
        int serialNum = cursor.getInt(cursor.getColumnIndexOrThrow(android.provider.Telephony.CellBroadcasts.SERIAL_NUMBER));
        int category = cursor.getInt(cursor.getColumnIndexOrThrow(android.provider.Telephony.CellBroadcasts.SERVICE_CATEGORY));
        java.lang.String language = cursor.getString(cursor.getColumnIndexOrThrow(android.provider.Telephony.CellBroadcasts.LANGUAGE_CODE));
        java.lang.String body = cursor.getString(cursor.getColumnIndexOrThrow(android.provider.Telephony.CellBroadcasts.MESSAGE_BODY));
        int format = cursor.getInt(cursor.getColumnIndexOrThrow(android.provider.Telephony.CellBroadcasts.MESSAGE_FORMAT));
        int priority = cursor.getInt(cursor.getColumnIndexOrThrow(android.provider.Telephony.CellBroadcasts.MESSAGE_PRIORITY));
        java.lang.String plmn;
        int plmnColumn = cursor.getColumnIndex(android.provider.Telephony.CellBroadcasts.PLMN);
        if ((plmnColumn != (-1)) && (!cursor.isNull(plmnColumn))) {
            plmn = cursor.getString(plmnColumn);
        } else {
            plmn = null;
        }
        int lac;
        int lacColumn = cursor.getColumnIndex(android.provider.Telephony.CellBroadcasts.LAC);
        if ((lacColumn != (-1)) && (!cursor.isNull(lacColumn))) {
            lac = cursor.getInt(lacColumn);
        } else {
            lac = -1;
        }
        int cid;
        int cidColumn = cursor.getColumnIndex(android.provider.Telephony.CellBroadcasts.CID);
        if ((cidColumn != (-1)) && (!cursor.isNull(cidColumn))) {
            cid = cursor.getInt(cidColumn);
        } else {
            cid = -1;
        }
        android.telephony.SmsCbLocation location = new android.telephony.SmsCbLocation(plmn, lac, cid);
        android.telephony.SmsCbEtwsInfo etwsInfo;
        int etwsWarningTypeColumn = cursor.getColumnIndex(android.provider.Telephony.CellBroadcasts.ETWS_WARNING_TYPE);
        if ((etwsWarningTypeColumn != (-1)) && (!cursor.isNull(etwsWarningTypeColumn))) {
            int warningType = cursor.getInt(etwsWarningTypeColumn);
            etwsInfo = new android.telephony.SmsCbEtwsInfo(warningType, false, false, false, null);
        } else {
            etwsInfo = null;
        }
        android.telephony.SmsCbCmasInfo cmasInfo;
        int cmasMessageClassColumn = cursor.getColumnIndex(android.provider.Telephony.CellBroadcasts.CMAS_MESSAGE_CLASS);
        if ((cmasMessageClassColumn != (-1)) && (!cursor.isNull(cmasMessageClassColumn))) {
            int messageClass = cursor.getInt(cmasMessageClassColumn);
            int cmasCategory;
            int cmasCategoryColumn = cursor.getColumnIndex(android.provider.Telephony.CellBroadcasts.CMAS_CATEGORY);
            if ((cmasCategoryColumn != (-1)) && (!cursor.isNull(cmasCategoryColumn))) {
                cmasCategory = cursor.getInt(cmasCategoryColumn);
            } else {
                cmasCategory = android.telephony.SmsCbCmasInfo.CMAS_CATEGORY_UNKNOWN;
            }
            int responseType;
            int cmasResponseTypeColumn = cursor.getColumnIndex(android.provider.Telephony.CellBroadcasts.CMAS_RESPONSE_TYPE);
            if ((cmasResponseTypeColumn != (-1)) && (!cursor.isNull(cmasResponseTypeColumn))) {
                responseType = cursor.getInt(cmasResponseTypeColumn);
            } else {
                responseType = android.telephony.SmsCbCmasInfo.CMAS_RESPONSE_TYPE_UNKNOWN;
            }
            int severity;
            int cmasSeverityColumn = cursor.getColumnIndex(android.provider.Telephony.CellBroadcasts.CMAS_SEVERITY);
            if ((cmasSeverityColumn != (-1)) && (!cursor.isNull(cmasSeverityColumn))) {
                severity = cursor.getInt(cmasSeverityColumn);
            } else {
                severity = android.telephony.SmsCbCmasInfo.CMAS_SEVERITY_UNKNOWN;
            }
            int urgency;
            int cmasUrgencyColumn = cursor.getColumnIndex(android.provider.Telephony.CellBroadcasts.CMAS_URGENCY);
            if ((cmasUrgencyColumn != (-1)) && (!cursor.isNull(cmasUrgencyColumn))) {
                urgency = cursor.getInt(cmasUrgencyColumn);
            } else {
                urgency = android.telephony.SmsCbCmasInfo.CMAS_URGENCY_UNKNOWN;
            }
            int certainty;
            int cmasCertaintyColumn = cursor.getColumnIndex(android.provider.Telephony.CellBroadcasts.CMAS_CERTAINTY);
            if ((cmasCertaintyColumn != (-1)) && (!cursor.isNull(cmasCertaintyColumn))) {
                certainty = cursor.getInt(cmasCertaintyColumn);
            } else {
                certainty = android.telephony.SmsCbCmasInfo.CMAS_CERTAINTY_UNKNOWN;
            }
            cmasInfo = new android.telephony.SmsCbCmasInfo(messageClass, cmasCategory, responseType, severity, urgency, certainty);
        } else {
            cmasInfo = null;
        }
        android.telephony.SmsCbMessage msg = new android.telephony.SmsCbMessage(format, geoScope, serialNum, location, category, language, body, priority, etwsInfo, cmasInfo);
        long deliveryTime = cursor.getLong(cursor.getColumnIndexOrThrow(android.provider.Telephony.CellBroadcasts.DELIVERY_TIME));
        boolean isRead = cursor.getInt(cursor.getColumnIndexOrThrow(android.provider.Telephony.CellBroadcasts.MESSAGE_READ)) != 0;
        return new android.telephony.CellBroadcastMessage(msg, deliveryTime, isRead);
    }

    /**
     * Return a ContentValues object for insertion into the database.
     *
     * @return a new ContentValues object containing this object's data
     */
    public android.content.ContentValues getContentValues() {
        android.content.ContentValues cv = new android.content.ContentValues(16);
        android.telephony.SmsCbMessage msg = mSmsCbMessage;
        cv.put(android.provider.Telephony.CellBroadcasts.GEOGRAPHICAL_SCOPE, msg.getGeographicalScope());
        android.telephony.SmsCbLocation location = msg.getLocation();
        if (location.getPlmn() != null) {
            cv.put(android.provider.Telephony.CellBroadcasts.PLMN, location.getPlmn());
        }
        if (location.getLac() != (-1)) {
            cv.put(android.provider.Telephony.CellBroadcasts.LAC, location.getLac());
        }
        if (location.getCid() != (-1)) {
            cv.put(android.provider.Telephony.CellBroadcasts.CID, location.getCid());
        }
        cv.put(android.provider.Telephony.CellBroadcasts.SERIAL_NUMBER, msg.getSerialNumber());
        cv.put(android.provider.Telephony.CellBroadcasts.SERVICE_CATEGORY, msg.getServiceCategory());
        cv.put(android.provider.Telephony.CellBroadcasts.LANGUAGE_CODE, msg.getLanguageCode());
        cv.put(android.provider.Telephony.CellBroadcasts.MESSAGE_BODY, msg.getMessageBody());
        cv.put(android.provider.Telephony.CellBroadcasts.DELIVERY_TIME, mDeliveryTime);
        cv.put(android.provider.Telephony.CellBroadcasts.MESSAGE_READ, mIsRead);
        cv.put(android.provider.Telephony.CellBroadcasts.MESSAGE_FORMAT, msg.getMessageFormat());
        cv.put(android.provider.Telephony.CellBroadcasts.MESSAGE_PRIORITY, msg.getMessagePriority());
        android.telephony.SmsCbEtwsInfo etwsInfo = mSmsCbMessage.getEtwsWarningInfo();
        if (etwsInfo != null) {
            cv.put(android.provider.Telephony.CellBroadcasts.ETWS_WARNING_TYPE, etwsInfo.getWarningType());
        }
        android.telephony.SmsCbCmasInfo cmasInfo = mSmsCbMessage.getCmasWarningInfo();
        if (cmasInfo != null) {
            cv.put(android.provider.Telephony.CellBroadcasts.CMAS_MESSAGE_CLASS, cmasInfo.getMessageClass());
            cv.put(android.provider.Telephony.CellBroadcasts.CMAS_CATEGORY, cmasInfo.getCategory());
            cv.put(android.provider.Telephony.CellBroadcasts.CMAS_RESPONSE_TYPE, cmasInfo.getResponseType());
            cv.put(android.provider.Telephony.CellBroadcasts.CMAS_SEVERITY, cmasInfo.getSeverity());
            cv.put(android.provider.Telephony.CellBroadcasts.CMAS_URGENCY, cmasInfo.getUrgency());
            cv.put(android.provider.Telephony.CellBroadcasts.CMAS_CERTAINTY, cmasInfo.getCertainty());
        }
        return cv;
    }

    /**
     * Set or clear the "read message" flag.
     *
     * @param isRead
     * 		true if the message has been read; false if not
     */
    public void setIsRead(boolean isRead) {
        mIsRead = isRead;
    }

    public java.lang.String getLanguageCode() {
        return mSmsCbMessage.getLanguageCode();
    }

    public int getServiceCategory() {
        return mSmsCbMessage.getServiceCategory();
    }

    public long getDeliveryTime() {
        return mDeliveryTime;
    }

    public java.lang.String getMessageBody() {
        return mSmsCbMessage.getMessageBody();
    }

    public boolean isRead() {
        return mIsRead;
    }

    public int getSerialNumber() {
        return mSmsCbMessage.getSerialNumber();
    }

    public android.telephony.SmsCbCmasInfo getCmasWarningInfo() {
        return mSmsCbMessage.getCmasWarningInfo();
    }

    public android.telephony.SmsCbEtwsInfo getEtwsWarningInfo() {
        return mSmsCbMessage.getEtwsWarningInfo();
    }

    /**
     * Return whether the broadcast is an emergency (PWS) message type.
     * This includes lower priority test messages and Amber alerts.
     *
     * All public alerts show the flashing warning icon in the dialog,
     * but only emergency alerts play the alert sound and speak the message.
     *
     * @return true if the message is PWS type; false otherwise
     */
    public boolean isPublicAlertMessage() {
        return mSmsCbMessage.isEmergencyMessage();
    }

    /**
     * Returns whether the broadcast is an emergency (PWS) message type,
     * including test messages and AMBER alerts.
     *
     * @return true if the message is PWS type (ETWS or CMAS)
     */
    public boolean isEmergencyAlertMessage() {
        return mSmsCbMessage.isEmergencyMessage();
    }

    /**
     * Return whether the broadcast is an ETWS emergency message type.
     *
     * @return true if the message is ETWS emergency type; false otherwise
     */
    public boolean isEtwsMessage() {
        return mSmsCbMessage.isEtwsMessage();
    }

    /**
     * Return whether the broadcast is a CMAS emergency message type.
     *
     * @return true if the message is CMAS emergency type; false otherwise
     */
    public boolean isCmasMessage() {
        return mSmsCbMessage.isCmasMessage();
    }

    /**
     * Return the CMAS message class.
     *
     * @return the CMAS message class, e.g. {@link SmsCbCmasInfo#CMAS_CLASS_SEVERE_THREAT}, or
    {@link SmsCbCmasInfo#CMAS_CLASS_UNKNOWN} if this is not a CMAS alert
     */
    public int getCmasMessageClass() {
        if (mSmsCbMessage.isCmasMessage()) {
            return mSmsCbMessage.getCmasWarningInfo().getMessageClass();
        } else {
            return android.telephony.SmsCbCmasInfo.CMAS_CLASS_UNKNOWN;
        }
    }

    /**
     * Return whether the broadcast is an ETWS popup alert.
     * This method checks the message ID and the message code.
     *
     * @return true if the message indicates an ETWS popup alert
     */
    public boolean isEtwsPopupAlert() {
        android.telephony.SmsCbEtwsInfo etwsInfo = mSmsCbMessage.getEtwsWarningInfo();
        return (etwsInfo != null) && etwsInfo.isPopupAlert();
    }

    /**
     * Return whether the broadcast is an ETWS emergency user alert.
     * This method checks the message ID and the message code.
     *
     * @return true if the message indicates an ETWS emergency user alert
     */
    public boolean isEtwsEmergencyUserAlert() {
        android.telephony.SmsCbEtwsInfo etwsInfo = mSmsCbMessage.getEtwsWarningInfo();
        return (etwsInfo != null) && etwsInfo.isEmergencyUserAlert();
    }

    /**
     * Return whether the broadcast is an ETWS test message.
     *
     * @return true if the message is an ETWS test message; false otherwise
     */
    public boolean isEtwsTestMessage() {
        android.telephony.SmsCbEtwsInfo etwsInfo = mSmsCbMessage.getEtwsWarningInfo();
        return (etwsInfo != null) && (etwsInfo.getWarningType() == android.telephony.SmsCbEtwsInfo.ETWS_WARNING_TYPE_TEST_MESSAGE);
    }

    /**
     * Return the abbreviated date string for the message delivery time.
     *
     * @param context
     * 		the context object
     * @return a String to use in the broadcast list UI
     */
    public java.lang.String getDateString(android.content.Context context) {
        int flags = (((android.text.format.DateUtils.FORMAT_NO_NOON_MIDNIGHT | android.text.format.DateUtils.FORMAT_SHOW_TIME) | android.text.format.DateUtils.FORMAT_ABBREV_ALL) | android.text.format.DateUtils.FORMAT_SHOW_DATE) | android.text.format.DateUtils.FORMAT_CAP_AMPM;
        return android.text.format.DateUtils.formatDateTime(context, mDeliveryTime, flags);
    }

    /**
     * Return the date string for the message delivery time, suitable for text-to-speech.
     *
     * @param context
     * 		the context object
     * @return a String for populating the list item AccessibilityEvent for TTS
     */
    public java.lang.String getSpokenDateString(android.content.Context context) {
        int flags = android.text.format.DateUtils.FORMAT_SHOW_TIME | android.text.format.DateUtils.FORMAT_SHOW_DATE;
        return android.text.format.DateUtils.formatDateTime(context, mDeliveryTime, flags);
    }
}

