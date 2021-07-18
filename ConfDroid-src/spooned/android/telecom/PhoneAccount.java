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
package android.telecom;


/**
 * Represents a distinct method to place or receive a phone call. Apps which can place calls and
 * want those calls to be integrated into the dialer and in-call UI should build an instance of
 * this class and register it with the system using {@link TelecomManager}.
 * <p>
 * {@link TelecomManager} uses registered {@link PhoneAccount}s to present the user with
 * alternative options when placing a phone call. When building a {@link PhoneAccount}, the app
 * should supply a valid {@link PhoneAccountHandle} that references the connection service
 * implementation Telecom will use to interact with the app.
 */
public final class PhoneAccount implements android.os.Parcelable {
    /**
     * {@link PhoneAccount} extras key (see {@link PhoneAccount#getExtras()}) which determines the
     * maximum permitted length of a call subject specified via the
     * {@link TelecomManager#EXTRA_CALL_SUBJECT} extra on an
     * {@link android.content.Intent#ACTION_CALL} intent.  Ultimately a {@link ConnectionService} is
     * responsible for enforcing the maximum call subject length when sending the message, however
     * this extra is provided so that the user interface can proactively limit the length of the
     * call subject as the user types it.
     */
    public static final java.lang.String EXTRA_CALL_SUBJECT_MAX_LENGTH = "android.telecom.extra.CALL_SUBJECT_MAX_LENGTH";

    /**
     * {@link PhoneAccount} extras key (see {@link PhoneAccount#getExtras()}) which determines the
     * character encoding to be used when determining the length of messages.
     * The user interface can use this when determining the number of characters the user may type
     * in a call subject.  If empty-string, the call subject message size limit will be enforced on
     * a 1:1 basis.  That is, each character will count towards the messages size limit as a single
     * character.  If a character encoding is specified, the message size limit will be based on the
     * number of bytes in the message per the specified encoding.  See
     * {@link #EXTRA_CALL_SUBJECT_MAX_LENGTH} for more information on the call subject maximum
     * length.
     */
    public static final java.lang.String EXTRA_CALL_SUBJECT_CHARACTER_ENCODING = "android.telecom.extra.CALL_SUBJECT_CHARACTER_ENCODING";

    /**
     * Flag indicating that this {@code PhoneAccount} can act as a connection manager for
     * other connections. The {@link ConnectionService} associated with this {@code PhoneAccount}
     * will be allowed to manage phone calls including using its own proprietary phone-call
     * implementation (like VoIP calling) to make calls instead of the telephony stack.
     * <p>
     * When a user opts to place a call using the SIM-based telephony stack, the
     * {@link ConnectionService} associated with this {@code PhoneAccount} will be attempted first
     * if the user has explicitly selected it to be used as the default connection manager.
     * <p>
     * See {@link #getCapabilities}
     */
    public static final int CAPABILITY_CONNECTION_MANAGER = 0x1;

    /**
     * Flag indicating that this {@code PhoneAccount} can make phone calls in place of
     * traditional SIM-based telephony calls. This account will be treated as a distinct method
     * for placing calls alongside the traditional SIM-based telephony stack. This flag is
     * distinct from {@link #CAPABILITY_CONNECTION_MANAGER} in that it is not allowed to manage
     * or place calls from the built-in telephony stack.
     * <p>
     * See {@link #getCapabilities}
     * <p>
     */
    public static final int CAPABILITY_CALL_PROVIDER = 0x2;

    /**
     * Flag indicating that this {@code PhoneAccount} represents a built-in PSTN SIM
     * subscription.
     * <p>
     * Only the Android framework can register a {@code PhoneAccount} having this capability.
     * <p>
     * See {@link #getCapabilities}
     */
    public static final int CAPABILITY_SIM_SUBSCRIPTION = 0x4;

    /**
     * Flag indicating that this {@code PhoneAccount} is capable of placing video calls.
     * <p>
     * See {@link #getCapabilities}
     */
    public static final int CAPABILITY_VIDEO_CALLING = 0x8;

    /**
     * Flag indicating that this {@code PhoneAccount} is capable of placing emergency calls.
     * By default all PSTN {@code PhoneAccount}s are capable of placing emergency calls.
     * <p>
     * See {@link #getCapabilities}
     */
    public static final int CAPABILITY_PLACE_EMERGENCY_CALLS = 0x10;

    /**
     * Flag indicating that this {@code PhoneAccount} is capable of being used by all users. This
     * should only be used by system apps (and will be ignored for all other apps trying to use it).
     * <p>
     * See {@link #getCapabilities}
     *
     * @unknown 
     */
    @android.annotation.SystemApi
    public static final int CAPABILITY_MULTI_USER = 0x20;

    /**
     * Flag indicating that this {@code PhoneAccount} supports a subject for Calls.  This means a
     * caller is able to specify a short subject line for an outgoing call.  A capable receiving
     * device displays the call subject on the incoming call screen.
     * <p>
     * See {@link #getCapabilities}
     */
    public static final int CAPABILITY_CALL_SUBJECT = 0x40;

    /**
     * Flag indicating that this {@code PhoneAccount} should only be used for emergency calls.
     * <p>
     * See {@link #getCapabilities}
     *
     * @unknown 
     */
    public static final int CAPABILITY_EMERGENCY_CALLS_ONLY = 0x80;

    /**
     * Flag indicating that for this {@code PhoneAccount}, the ability to make a video call to a
     * number relies on presence.  Should only be set if the {@code PhoneAccount} also has
     * {@link #CAPABILITY_VIDEO_CALLING}.
     * <p>
     * When set, the {@link ConnectionService} is responsible for toggling the
     * {@link android.provider.ContactsContract.Data#CARRIER_PRESENCE_VT_CAPABLE} bit on the
     * {@link android.provider.ContactsContract.Data#CARRIER_PRESENCE} column to indicate whether
     * a contact's phone number supports video calling.
     * <p>
     * See {@link #getCapabilities}
     */
    public static final int CAPABILITY_VIDEO_CALLING_RELIES_ON_PRESENCE = 0x100;

    /**
     * Flag indicating that for this {@link PhoneAccount}, emergency video calling is allowed.
     * <p>
     * When set, Telecom will allow emergency video calls to be placed.  When not set, Telecom will
     * convert all outgoing video calls to emergency numbers to audio-only.
     *
     * @unknown 
     */
    public static final int CAPABILITY_EMERGENCY_VIDEO_CALLING = 0x200;

    /**
     * URI scheme for telephone number URIs.
     */
    public static final java.lang.String SCHEME_TEL = "tel";

    /**
     * URI scheme for voicemail URIs.
     */
    public static final java.lang.String SCHEME_VOICEMAIL = "voicemail";

    /**
     * URI scheme for SIP URIs.
     */
    public static final java.lang.String SCHEME_SIP = "sip";

    /**
     * Indicating no icon tint is set.
     *
     * @unknown 
     */
    public static final int NO_ICON_TINT = 0;

    /**
     * Indicating no hightlight color is set.
     */
    public static final int NO_HIGHLIGHT_COLOR = 0;

    /**
     * Indicating no resource ID is set.
     */
    public static final int NO_RESOURCE_ID = -1;

    private final android.telecom.PhoneAccountHandle mAccountHandle;

    private final android.net.Uri mAddress;

    private final android.net.Uri mSubscriptionAddress;

    private final int mCapabilities;

    private final int mHighlightColor;

    private final java.lang.CharSequence mLabel;

    private final java.lang.CharSequence mShortDescription;

    private final java.util.List<java.lang.String> mSupportedUriSchemes;

    private final android.graphics.drawable.Icon mIcon;

    private final android.os.Bundle mExtras;

    private boolean mIsEnabled;

    private java.lang.String mGroupId;

    /**
     * Helper class for creating a {@link PhoneAccount}.
     */
    public static class Builder {
        private android.telecom.PhoneAccountHandle mAccountHandle;

        private android.net.Uri mAddress;

        private android.net.Uri mSubscriptionAddress;

        private int mCapabilities;

        private int mHighlightColor = android.telecom.PhoneAccount.NO_HIGHLIGHT_COLOR;

        private java.lang.CharSequence mLabel;

        private java.lang.CharSequence mShortDescription;

        private java.util.List<java.lang.String> mSupportedUriSchemes = new java.util.ArrayList<java.lang.String>();

        private android.graphics.drawable.Icon mIcon;

        private android.os.Bundle mExtras;

        private boolean mIsEnabled = false;

        private java.lang.String mGroupId = "";

        /**
         * Creates a builder with the specified {@link PhoneAccountHandle} and label.
         */
        public Builder(android.telecom.PhoneAccountHandle accountHandle, java.lang.CharSequence label) {
            this.mAccountHandle = accountHandle;
            this.mLabel = label;
        }

        /**
         * Creates an instance of the {@link PhoneAccount.Builder} from an existing
         * {@link PhoneAccount}.
         *
         * @param phoneAccount
         * 		The {@link PhoneAccount} used to initialize the builder.
         */
        public Builder(android.telecom.PhoneAccount phoneAccount) {
            mAccountHandle = phoneAccount.getAccountHandle();
            mAddress = phoneAccount.getAddress();
            mSubscriptionAddress = phoneAccount.getSubscriptionAddress();
            mCapabilities = phoneAccount.getCapabilities();
            mHighlightColor = phoneAccount.getHighlightColor();
            mLabel = phoneAccount.getLabel();
            mShortDescription = phoneAccount.getShortDescription();
            mSupportedUriSchemes.addAll(phoneAccount.getSupportedUriSchemes());
            mIcon = phoneAccount.getIcon();
            mIsEnabled = phoneAccount.isEnabled();
            mExtras = phoneAccount.getExtras();
            mGroupId = phoneAccount.getGroupId();
        }

        /**
         * Sets the address. See {@link PhoneAccount#getAddress}.
         *
         * @param value
         * 		The address of the phone account.
         * @return The builder.
         */
        public android.telecom.PhoneAccount.Builder setAddress(android.net.Uri value) {
            this.mAddress = value;
            return this;
        }

        /**
         * Sets the subscription address. See {@link PhoneAccount#getSubscriptionAddress}.
         *
         * @param value
         * 		The subscription address.
         * @return The builder.
         */
        public android.telecom.PhoneAccount.Builder setSubscriptionAddress(android.net.Uri value) {
            this.mSubscriptionAddress = value;
            return this;
        }

        /**
         * Sets the capabilities. See {@link PhoneAccount#getCapabilities}.
         *
         * @param value
         * 		The capabilities to set.
         * @return The builder.
         */
        public android.telecom.PhoneAccount.Builder setCapabilities(int value) {
            this.mCapabilities = value;
            return this;
        }

        /**
         * Sets the icon. See {@link PhoneAccount#getIcon}.
         *
         * @param icon
         * 		The icon to set.
         */
        public android.telecom.PhoneAccount.Builder setIcon(android.graphics.drawable.Icon icon) {
            mIcon = icon;
            return this;
        }

        /**
         * Sets the highlight color. See {@link PhoneAccount#getHighlightColor}.
         *
         * @param value
         * 		The highlight color.
         * @return The builder.
         */
        public android.telecom.PhoneAccount.Builder setHighlightColor(int value) {
            this.mHighlightColor = value;
            return this;
        }

        /**
         * Sets the short description. See {@link PhoneAccount#getShortDescription}.
         *
         * @param value
         * 		The short description.
         * @return The builder.
         */
        public android.telecom.PhoneAccount.Builder setShortDescription(java.lang.CharSequence value) {
            this.mShortDescription = value;
            return this;
        }

        /**
         * Specifies an additional URI scheme supported by the {@link PhoneAccount}.
         *
         * @param uriScheme
         * 		The URI scheme.
         * @return The builder.
         */
        public android.telecom.PhoneAccount.Builder addSupportedUriScheme(java.lang.String uriScheme) {
            if ((!android.text.TextUtils.isEmpty(uriScheme)) && (!mSupportedUriSchemes.contains(uriScheme))) {
                this.mSupportedUriSchemes.add(uriScheme);
            }
            return this;
        }

        /**
         * Specifies the URI schemes supported by the {@link PhoneAccount}.
         *
         * @param uriSchemes
         * 		The URI schemes.
         * @return The builder.
         */
        public android.telecom.PhoneAccount.Builder setSupportedUriSchemes(java.util.List<java.lang.String> uriSchemes) {
            mSupportedUriSchemes.clear();
            if ((uriSchemes != null) && (!uriSchemes.isEmpty())) {
                for (java.lang.String uriScheme : uriSchemes) {
                    addSupportedUriScheme(uriScheme);
                }
            }
            return this;
        }

        /**
         * Specifies the extras associated with the {@link PhoneAccount}.
         * <p>
         * {@code PhoneAccount}s only support extra values of type: {@link String}, {@link Integer},
         * and {@link Boolean}.  Extras which are not of these types are ignored.
         *
         * @param extras
         * 		
         * @return 
         */
        public android.telecom.PhoneAccount.Builder setExtras(android.os.Bundle extras) {
            mExtras = extras;
            return this;
        }

        /**
         * Sets the enabled state of the phone account.
         *
         * @param isEnabled
         * 		The enabled state.
         * @return The builder.
         * @unknown 
         */
        public android.telecom.PhoneAccount.Builder setIsEnabled(boolean isEnabled) {
            mIsEnabled = isEnabled;
            return this;
        }

        /**
         * Sets the group Id of the {@link PhoneAccount}. When a new {@link PhoneAccount} is
         * registered to Telecom, it will replace another {@link PhoneAccount} that is already
         * registered in Telecom and take on the current user defaults and enabled status. There can
         * only be one {@link PhoneAccount} with a non-empty group number registered to Telecom at a
         * time. By default, there is no group Id for a {@link PhoneAccount} (an empty String). Only
         * grouped {@link PhoneAccount}s with the same {@link ConnectionService} can be replaced.
         *
         * @param groupId
         * 		The group Id of the {@link PhoneAccount} that will replace any other
         * 		registered {@link PhoneAccount} in Telecom with the same Group Id.
         * @return The builder
         * @unknown 
         */
        public android.telecom.PhoneAccount.Builder setGroupId(java.lang.String groupId) {
            if (groupId != null) {
                mGroupId = groupId;
            } else {
                mGroupId = "";
            }
            return this;
        }

        /**
         * Creates an instance of a {@link PhoneAccount} based on the current builder settings.
         *
         * @return The {@link PhoneAccount}.
         */
        public android.telecom.PhoneAccount build() {
            // If no supported URI schemes were defined, assume "tel" is supported.
            if (mSupportedUriSchemes.isEmpty()) {
                addSupportedUriScheme(android.telecom.PhoneAccount.SCHEME_TEL);
            }
            return new android.telecom.PhoneAccount(mAccountHandle, mAddress, mSubscriptionAddress, mCapabilities, mIcon, mHighlightColor, mLabel, mShortDescription, mSupportedUriSchemes, mExtras, mIsEnabled, mGroupId);
        }
    }

    private PhoneAccount(android.telecom.PhoneAccountHandle account, android.net.Uri address, android.net.Uri subscriptionAddress, int capabilities, android.graphics.drawable.Icon icon, int highlightColor, java.lang.CharSequence label, java.lang.CharSequence shortDescription, java.util.List<java.lang.String> supportedUriSchemes, android.os.Bundle extras, boolean isEnabled, java.lang.String groupId) {
        mAccountHandle = account;
        mAddress = address;
        mSubscriptionAddress = subscriptionAddress;
        mCapabilities = capabilities;
        mIcon = icon;
        mHighlightColor = highlightColor;
        mLabel = label;
        mShortDescription = shortDescription;
        mSupportedUriSchemes = java.util.Collections.unmodifiableList(supportedUriSchemes);
        mExtras = extras;
        mIsEnabled = isEnabled;
        mGroupId = groupId;
    }

    public static android.telecom.PhoneAccount.Builder builder(android.telecom.PhoneAccountHandle accountHandle, java.lang.CharSequence label) {
        return new android.telecom.PhoneAccount.Builder(accountHandle, label);
    }

    /**
     * Returns a builder initialized with the current {@link PhoneAccount} instance.
     *
     * @return The builder.
     */
    public android.telecom.PhoneAccount.Builder toBuilder() {
        return new android.telecom.PhoneAccount.Builder(this);
    }

    /**
     * The unique identifier of this {@code PhoneAccount}.
     *
     * @return A {@code PhoneAccountHandle}.
     */
    public android.telecom.PhoneAccountHandle getAccountHandle() {
        return mAccountHandle;
    }

    /**
     * The address (e.g., a phone number) associated with this {@code PhoneAccount}. This
     * represents the destination from which outgoing calls using this {@code PhoneAccount}
     * will appear to come, if applicable, and the destination to which incoming calls using this
     * {@code PhoneAccount} may be addressed.
     *
     * @return A address expressed as a {@code Uri}, for example, a phone number.
     */
    public android.net.Uri getAddress() {
        return mAddress;
    }

    /**
     * The raw callback number used for this {@code PhoneAccount}, as distinct from
     * {@link #getAddress()}. For the majority of {@code PhoneAccount}s this should be registered
     * as {@code null}.  It is used by the system for SIM-based {@code PhoneAccount} registration
     * where {@link android.telephony.TelephonyManager#setLine1NumberForDisplay(String, String)}
     * has been used to alter the callback number.
     * <p>
     *
     * @return The subscription number, suitable for display to the user.
     */
    public android.net.Uri getSubscriptionAddress() {
        return mSubscriptionAddress;
    }

    /**
     * The capabilities of this {@code PhoneAccount}.
     *
     * @return A bit field of flags describing this {@code PhoneAccount}'s capabilities.
     */
    public int getCapabilities() {
        return mCapabilities;
    }

    /**
     * Determines if this {@code PhoneAccount} has a capabilities specified by the passed in
     * bit mask.
     *
     * @param capability
     * 		The capabilities to check.
     * @return {@code true} if the phone account has the capability.
     */
    public boolean hasCapabilities(int capability) {
        return (mCapabilities & capability) == capability;
    }

    /**
     * A short label describing a {@code PhoneAccount}.
     *
     * @return A label for this {@code PhoneAccount}.
     */
    public java.lang.CharSequence getLabel() {
        return mLabel;
    }

    /**
     * A short paragraph describing this {@code PhoneAccount}.
     *
     * @return A description for this {@code PhoneAccount}.
     */
    public java.lang.CharSequence getShortDescription() {
        return mShortDescription;
    }

    /**
     * The URI schemes supported by this {@code PhoneAccount}.
     *
     * @return The URI schemes.
     */
    public java.util.List<java.lang.String> getSupportedUriSchemes() {
        return mSupportedUriSchemes;
    }

    /**
     * The extras associated with this {@code PhoneAccount}.
     * <p>
     * A {@link ConnectionService} may provide implementation specific information about the
     * {@link PhoneAccount} via the extras.
     *
     * @return The extras.
     */
    public android.os.Bundle getExtras() {
        return mExtras;
    }

    /**
     * The icon to represent this {@code PhoneAccount}.
     *
     * @return The icon.
     */
    public android.graphics.drawable.Icon getIcon() {
        return mIcon;
    }

    /**
     * Indicates whether the user has enabled this {@code PhoneAccount} or not. This value is only
     * populated for {@code PhoneAccount}s returned by {@link TelecomManager#getPhoneAccount}.
     *
     * @return {@code true} if the account is enabled by the user, {@code false} otherwise.
     */
    public boolean isEnabled() {
        return mIsEnabled;
    }

    /**
     * A non-empty {@link String} representing the group that A {@link PhoneAccount} is in or an
     * empty {@link String} if the {@link PhoneAccount} is not in a group. If this
     * {@link PhoneAccount} is in a group, this new {@link PhoneAccount} will replace a registered
     * {@link PhoneAccount} that is in the same group. When the {@link PhoneAccount} is replaced,
     * its user defined defaults and enabled status will also pass to this new {@link PhoneAccount}.
     * Only {@link PhoneAccount}s that share the same {@link ConnectionService} can be replaced.
     *
     * @return A non-empty String Id if this {@link PhoneAccount} belongs to a group.
     * @unknown 
     */
    public java.lang.String getGroupId() {
        return mGroupId;
    }

    /**
     * Determines if the {@link PhoneAccount} supports calls to/from addresses with a specified URI
     * scheme.
     *
     * @param uriScheme
     * 		The URI scheme to check.
     * @return {@code true} if the {@code PhoneAccount} supports calls to/from addresses with the
    specified URI scheme.
     */
    public boolean supportsUriScheme(java.lang.String uriScheme) {
        if ((mSupportedUriSchemes == null) || (uriScheme == null)) {
            return false;
        }
        for (java.lang.String scheme : mSupportedUriSchemes) {
            if ((scheme != null) && scheme.equals(uriScheme)) {
                return true;
            }
        }
        return false;
    }

    /**
     * A highlight color to use in displaying information about this {@code PhoneAccount}.
     *
     * @return A hexadecimal color value.
     */
    public int getHighlightColor() {
        return mHighlightColor;
    }

    /**
     * Sets the enabled state of the phone account.
     *
     * @unknown 
     */
    public void setIsEnabled(boolean isEnabled) {
        mIsEnabled = isEnabled;
    }

    // 
    // Parcelable implementation
    // 
    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel out, int flags) {
        if (mAccountHandle == null) {
            out.writeInt(0);
        } else {
            out.writeInt(1);
            mAccountHandle.writeToParcel(out, flags);
        }
        if (mAddress == null) {
            out.writeInt(0);
        } else {
            out.writeInt(1);
            mAddress.writeToParcel(out, flags);
        }
        if (mSubscriptionAddress == null) {
            out.writeInt(0);
        } else {
            out.writeInt(1);
            mSubscriptionAddress.writeToParcel(out, flags);
        }
        out.writeInt(mCapabilities);
        out.writeInt(mHighlightColor);
        out.writeCharSequence(mLabel);
        out.writeCharSequence(mShortDescription);
        out.writeStringList(mSupportedUriSchemes);
        if (mIcon == null) {
            out.writeInt(0);
        } else {
            out.writeInt(1);
            mIcon.writeToParcel(out, flags);
        }
        out.writeByte(((byte) (mIsEnabled ? 1 : 0)));
        out.writeBundle(mExtras);
        out.writeString(mGroupId);
    }

    public static final android.os.Parcelable.Creator<android.telecom.PhoneAccount> CREATOR = new android.os.Parcelable.Creator<android.telecom.PhoneAccount>() {
        @java.lang.Override
        public android.telecom.PhoneAccount createFromParcel(android.os.Parcel in) {
            return new android.telecom.PhoneAccount(in);
        }

        @java.lang.Override
        public android.telecom.PhoneAccount[] newArray(int size) {
            return new android.telecom.PhoneAccount[size];
        }
    };

    private PhoneAccount(android.os.Parcel in) {
        if (in.readInt() > 0) {
            mAccountHandle = android.telecom.PhoneAccountHandle.CREATOR.createFromParcel(in);
        } else {
            mAccountHandle = null;
        }
        if (in.readInt() > 0) {
            mAddress = android.net.Uri.CREATOR.createFromParcel(in);
        } else {
            mAddress = null;
        }
        if (in.readInt() > 0) {
            mSubscriptionAddress = android.net.Uri.CREATOR.createFromParcel(in);
        } else {
            mSubscriptionAddress = null;
        }
        mCapabilities = in.readInt();
        mHighlightColor = in.readInt();
        mLabel = in.readCharSequence();
        mShortDescription = in.readCharSequence();
        mSupportedUriSchemes = java.util.Collections.unmodifiableList(in.createStringArrayList());
        if (in.readInt() > 0) {
            mIcon = android.graphics.drawable.Icon.CREATOR.createFromParcel(in);
        } else {
            mIcon = null;
        }
        mIsEnabled = in.readByte() == 1;
        mExtras = in.readBundle();
        mGroupId = in.readString();
    }

    @java.lang.Override
    public java.lang.String toString() {
        java.lang.StringBuilder sb = new java.lang.StringBuilder().append("[[").append(mIsEnabled ? 'X' : ' ').append("] PhoneAccount: ").append(mAccountHandle).append(" Capabilities: ").append(capabilitiesToString(mCapabilities)).append(" Schemes: ");
        for (java.lang.String scheme : mSupportedUriSchemes) {
            sb.append(scheme).append(" ");
        }
        sb.append(" Extras: ");
        sb.append(mExtras);
        sb.append(" GroupId: ");
        sb.append(android.telecom.Log.pii(mGroupId));
        sb.append("]");
        return sb.toString();
    }

    /**
     * Generates a string representation of a capabilities bitmask.
     *
     * @param capabilities
     * 		The capabilities bitmask.
     * @return String representation of the capabilities bitmask.
     */
    private java.lang.String capabilitiesToString(int capabilities) {
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        if (hasCapabilities(android.telecom.PhoneAccount.CAPABILITY_VIDEO_CALLING)) {
            sb.append("Video ");
        }
        if (hasCapabilities(android.telecom.PhoneAccount.CAPABILITY_VIDEO_CALLING_RELIES_ON_PRESENCE)) {
            sb.append("Presence ");
        }
        if (hasCapabilities(android.telecom.PhoneAccount.CAPABILITY_CALL_PROVIDER)) {
            sb.append("CallProvider ");
        }
        if (hasCapabilities(android.telecom.PhoneAccount.CAPABILITY_CALL_SUBJECT)) {
            sb.append("CallSubject ");
        }
        if (hasCapabilities(android.telecom.PhoneAccount.CAPABILITY_CONNECTION_MANAGER)) {
            sb.append("ConnectionMgr ");
        }
        if (hasCapabilities(android.telecom.PhoneAccount.CAPABILITY_EMERGENCY_CALLS_ONLY)) {
            sb.append("EmergOnly ");
        }
        if (hasCapabilities(android.telecom.PhoneAccount.CAPABILITY_MULTI_USER)) {
            sb.append("MultiUser ");
        }
        if (hasCapabilities(android.telecom.PhoneAccount.CAPABILITY_PLACE_EMERGENCY_CALLS)) {
            sb.append("PlaceEmerg ");
        }
        if (hasCapabilities(android.telecom.PhoneAccount.CAPABILITY_EMERGENCY_VIDEO_CALLING)) {
            sb.append("EmergVideo ");
        }
        if (hasCapabilities(android.telecom.PhoneAccount.CAPABILITY_SIM_SUBSCRIPTION)) {
            sb.append("SimSub ");
        }
        return sb.toString();
    }
}

