/**
 * Copyright (C) 2016 The Android Open Source Project
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
 * Class to represent various settings for the visual voicemail SMS filter. When the filter is
 * enabled, incoming SMS matching the generalized OMTP format:
 *
 * <p>[clientPrefix]:[prefix]:([key]=[value];)*
 *
 * <p>will be regarded as a visual voicemail SMS, and removed before reaching the SMS provider. The
 * intent {@link android.provider.VoicemailContract#ACTION_VOICEMAIL_SMS_RECEIVED} will then be sent
 * to the default dialer with the information extracted from the SMS.
 *
 * <p>Use {@link android.telephony.VisualVoicemailSmsFilterSettings.Builder} to construct this
 * class.
 *
 * @see android.telephony.TelephonyManager#enableVisualVoicemailSmsFilter
 * @unknown 
 */
public class VisualVoicemailSmsFilterSettings implements android.os.Parcelable {
    /**
     * The visual voicemail SMS message does not have to be a data SMS, and can be directed to any
     * port.
     *
     * @unknown 
     */
    public static final int DESTINATION_PORT_ANY = -1;

    /**
     * The visual voicemail SMS message can be directed to any port, but must be a data SMS.
     *
     * @unknown 
     */
    public static final int DESTINATION_PORT_DATA_SMS = -2;

    public static final java.lang.String DEFAULT_CLIENT_PREFIX = "//VVM";

    public static final java.util.List<java.lang.String> DEFAULT_ORIGINATING_NUMBERS = java.util.Collections.emptyList();

    public static final int DEFAULT_DESTINATION_PORT = android.telephony.VisualVoicemailSmsFilterSettings.DESTINATION_PORT_ANY;

    /**
     * Builder class for {@link VisualVoicemailSmsFilterSettings} objects.
     *
     * @unknown 
     */
    public static class Builder {
        private java.lang.String mClientPrefix = android.telephony.VisualVoicemailSmsFilterSettings.DEFAULT_CLIENT_PREFIX;

        private java.util.List<java.lang.String> mOriginatingNumbers = android.telephony.VisualVoicemailSmsFilterSettings.DEFAULT_ORIGINATING_NUMBERS;

        private int mDestinationPort = android.telephony.VisualVoicemailSmsFilterSettings.DEFAULT_DESTINATION_PORT;

        public android.telephony.VisualVoicemailSmsFilterSettings build() {
            return new android.telephony.VisualVoicemailSmsFilterSettings(this);
        }

        /**
         * Sets the client prefix for the visual voicemail SMS filter. The client prefix will appear
         * at the start of a visual voicemail SMS message, followed by a colon(:).
         */
        public android.telephony.VisualVoicemailSmsFilterSettings.Builder setClientPrefix(java.lang.String clientPrefix) {
            if (clientPrefix == null) {
                throw new java.lang.IllegalArgumentException("Client prefix cannot be null");
            }
            mClientPrefix = clientPrefix;
            return this;
        }

        /**
         * Sets the originating number whitelist for the visual voicemail SMS filter. If the list is
         * not null only the SMS messages from a number in the list can be considered as a visual
         * voicemail SMS. Otherwise, messages from any address will be considered.
         */
        public android.telephony.VisualVoicemailSmsFilterSettings.Builder setOriginatingNumbers(java.util.List<java.lang.String> originatingNumbers) {
            if (originatingNumbers == null) {
                throw new java.lang.IllegalArgumentException("Originating numbers cannot be null");
            }
            mOriginatingNumbers = originatingNumbers;
            return this;
        }

        /**
         * Sets the destination port for the visual voicemail SMS filter.
         *
         * @param destinationPort
         * 		The destination port, or {@link #DESTINATION_PORT_ANY}, or {@link #DESTINATION_PORT_DATA_SMS}
         */
        public android.telephony.VisualVoicemailSmsFilterSettings.Builder setDestinationPort(int destinationPort) {
            mDestinationPort = destinationPort;
            return this;
        }
    }

    /**
     * The client prefix for the visual voicemail SMS filter. The client prefix will appear at the
     * start of a visual voicemail SMS message, followed by a colon(:).
     */
    public final java.lang.String clientPrefix;

    /**
     * The originating number whitelist for the visual voicemail SMS filter of a phone account. If
     * the list is not null only the SMS messages from a number in the list can be considered as a
     * visual voicemail SMS. Otherwise, messages from any address will be considered.
     */
    public final java.util.List<java.lang.String> originatingNumbers;

    /**
     * The destination port for the visual voicemail SMS filter, or {@link #DESTINATION_PORT_ANY},
     * or {@link #DESTINATION_PORT_DATA_SMS}
     */
    public final int destinationPort;

    /**
     * Use {@link Builder} to construct
     */
    private VisualVoicemailSmsFilterSettings(android.telephony.VisualVoicemailSmsFilterSettings.Builder builder) {
        clientPrefix = builder.mClientPrefix;
        originatingNumbers = builder.mOriginatingNumbers;
        destinationPort = builder.mDestinationPort;
    }

    public static final android.os.Parcelable.Creator<android.telephony.VisualVoicemailSmsFilterSettings> CREATOR = new android.os.Parcelable.Creator<android.telephony.VisualVoicemailSmsFilterSettings>() {
        @java.lang.Override
        public android.telephony.VisualVoicemailSmsFilterSettings createFromParcel(android.os.Parcel in) {
            android.telephony.VisualVoicemailSmsFilterSettings.Builder builder = new android.telephony.VisualVoicemailSmsFilterSettings.Builder();
            builder.setClientPrefix(in.readString());
            builder.setOriginatingNumbers(in.createStringArrayList());
            builder.setDestinationPort(in.readInt());
            return builder.build();
        }

        @java.lang.Override
        public android.telephony.VisualVoicemailSmsFilterSettings[] newArray(int size) {
            return new android.telephony.VisualVoicemailSmsFilterSettings[size];
        }
    };

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeString(clientPrefix);
        dest.writeStringList(originatingNumbers);
        dest.writeInt(destinationPort);
    }
}

