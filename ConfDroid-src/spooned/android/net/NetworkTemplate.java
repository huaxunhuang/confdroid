/**
 * Copyright (C) 2011 The Android Open Source Project
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
package android.net;


/**
 * Template definition used to generically match {@link NetworkIdentity},
 * usually when collecting statistics.
 *
 * @unknown 
 */
public class NetworkTemplate implements android.os.Parcelable {
    /**
     * Current Version of the Backup Serializer.
     */
    private static final int BACKUP_VERSION = 1;

    public static final int MATCH_MOBILE_ALL = 1;

    @java.lang.Deprecated
    public static final int MATCH_MOBILE_3G_LOWER = 2;

    @java.lang.Deprecated
    public static final int MATCH_MOBILE_4G = 3;

    public static final int MATCH_WIFI = 4;

    public static final int MATCH_ETHERNET = 5;

    public static final int MATCH_MOBILE_WILDCARD = 6;

    public static final int MATCH_WIFI_WILDCARD = 7;

    public static final int MATCH_BLUETOOTH = 8;

    public static final int MATCH_PROXY = 9;

    private static boolean sForceAllNetworkTypes = false;

    @com.android.internal.annotations.VisibleForTesting
    public static void forceAllNetworkTypes() {
        android.net.NetworkTemplate.sForceAllNetworkTypes = true;
    }

    /**
     * Template to match {@link ConnectivityManager#TYPE_MOBILE} networks with
     * the given IMSI.
     */
    public static android.net.NetworkTemplate buildTemplateMobileAll(java.lang.String subscriberId) {
        return new android.net.NetworkTemplate(android.net.NetworkTemplate.MATCH_MOBILE_ALL, subscriberId, null);
    }

    /**
     * Template to match {@link ConnectivityManager#TYPE_MOBILE} networks with
     * the given IMSI that roughly meet a "3G" definition, or lower.
     */
    @java.lang.Deprecated
    public static android.net.NetworkTemplate buildTemplateMobile3gLower(java.lang.String subscriberId) {
        return new android.net.NetworkTemplate(android.net.NetworkTemplate.MATCH_MOBILE_3G_LOWER, subscriberId, null);
    }

    /**
     * Template to match {@link ConnectivityManager#TYPE_MOBILE} networks with
     * the given IMSI that roughly meet a "4G" definition.
     */
    @java.lang.Deprecated
    public static android.net.NetworkTemplate buildTemplateMobile4g(java.lang.String subscriberId) {
        return new android.net.NetworkTemplate(android.net.NetworkTemplate.MATCH_MOBILE_4G, subscriberId, null);
    }

    /**
     * Template to match {@link ConnectivityManager#TYPE_MOBILE} networks,
     * regardless of IMSI.
     */
    public static android.net.NetworkTemplate buildTemplateMobileWildcard() {
        return new android.net.NetworkTemplate(android.net.NetworkTemplate.MATCH_MOBILE_WILDCARD, null, null);
    }

    /**
     * Template to match all {@link ConnectivityManager#TYPE_WIFI} networks,
     * regardless of SSID.
     */
    public static android.net.NetworkTemplate buildTemplateWifiWildcard() {
        return new android.net.NetworkTemplate(android.net.NetworkTemplate.MATCH_WIFI_WILDCARD, null, null);
    }

    @java.lang.Deprecated
    public static android.net.NetworkTemplate buildTemplateWifi() {
        return android.net.NetworkTemplate.buildTemplateWifiWildcard();
    }

    /**
     * Template to match {@link ConnectivityManager#TYPE_WIFI} networks with the
     * given SSID.
     */
    public static android.net.NetworkTemplate buildTemplateWifi(java.lang.String networkId) {
        return new android.net.NetworkTemplate(android.net.NetworkTemplate.MATCH_WIFI, null, networkId);
    }

    /**
     * Template to combine all {@link ConnectivityManager#TYPE_ETHERNET} style
     * networks together.
     */
    public static android.net.NetworkTemplate buildTemplateEthernet() {
        return new android.net.NetworkTemplate(android.net.NetworkTemplate.MATCH_ETHERNET, null, null);
    }

    /**
     * Template to combine all {@link ConnectivityManager#TYPE_BLUETOOTH} style
     * networks together.
     */
    public static android.net.NetworkTemplate buildTemplateBluetooth() {
        return new android.net.NetworkTemplate(android.net.NetworkTemplate.MATCH_BLUETOOTH, null, null);
    }

    /**
     * Template to combine all {@link ConnectivityManager#TYPE_PROXY} style
     * networks together.
     */
    public static android.net.NetworkTemplate buildTemplateProxy() {
        return new android.net.NetworkTemplate(android.net.NetworkTemplate.MATCH_PROXY, null, null);
    }

    private final int mMatchRule;

    private final java.lang.String mSubscriberId;

    /**
     * Ugh, templates are designed to target a single subscriber, but we might
     * need to match several "merged" subscribers. These are the subscribers
     * that should be considered to match this template.
     * <p>
     * Since the merge set is dynamic, it should <em>not</em> be persisted or
     * used for determining equality.
     */
    private final java.lang.String[] mMatchSubscriberIds;

    private final java.lang.String mNetworkId;

    public NetworkTemplate(int matchRule, java.lang.String subscriberId, java.lang.String networkId) {
        this(matchRule, subscriberId, new java.lang.String[]{ subscriberId }, networkId);
    }

    public NetworkTemplate(int matchRule, java.lang.String subscriberId, java.lang.String[] matchSubscriberIds, java.lang.String networkId) {
        mMatchRule = matchRule;
        mSubscriberId = subscriberId;
        mMatchSubscriberIds = matchSubscriberIds;
        mNetworkId = networkId;
    }

    private NetworkTemplate(android.os.Parcel in) {
        mMatchRule = in.readInt();
        mSubscriberId = in.readString();
        mMatchSubscriberIds = in.createStringArray();
        mNetworkId = in.readString();
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeInt(mMatchRule);
        dest.writeString(mSubscriberId);
        dest.writeStringArray(mMatchSubscriberIds);
        dest.writeString(mNetworkId);
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public java.lang.String toString() {
        final java.lang.StringBuilder builder = new java.lang.StringBuilder("NetworkTemplate: ");
        builder.append("matchRule=").append(android.net.NetworkTemplate.getMatchRuleName(mMatchRule));
        if (mSubscriberId != null) {
            builder.append(", subscriberId=").append(android.net.NetworkIdentity.scrubSubscriberId(mSubscriberId));
        }
        if (mMatchSubscriberIds != null) {
            builder.append(", matchSubscriberIds=").append(java.util.Arrays.toString(android.net.NetworkIdentity.scrubSubscriberId(mMatchSubscriberIds)));
        }
        if (mNetworkId != null) {
            builder.append(", networkId=").append(mNetworkId);
        }
        return builder.toString();
    }

    @java.lang.Override
    public int hashCode() {
        return java.util.Objects.hash(mMatchRule, mSubscriberId, mNetworkId);
    }

    @java.lang.Override
    public boolean equals(java.lang.Object obj) {
        if (obj instanceof android.net.NetworkTemplate) {
            final android.net.NetworkTemplate other = ((android.net.NetworkTemplate) (obj));
            return ((mMatchRule == other.mMatchRule) && java.util.Objects.equals(mSubscriberId, other.mSubscriberId)) && java.util.Objects.equals(mNetworkId, other.mNetworkId);
        }
        return false;
    }

    public boolean isMatchRuleMobile() {
        switch (mMatchRule) {
            case android.net.NetworkTemplate.MATCH_MOBILE_3G_LOWER :
            case android.net.NetworkTemplate.MATCH_MOBILE_4G :
            case android.net.NetworkTemplate.MATCH_MOBILE_ALL :
            case android.net.NetworkTemplate.MATCH_MOBILE_WILDCARD :
                return true;
            default :
                return false;
        }
    }

    public boolean isPersistable() {
        switch (mMatchRule) {
            case android.net.NetworkTemplate.MATCH_MOBILE_WILDCARD :
            case android.net.NetworkTemplate.MATCH_WIFI_WILDCARD :
                return false;
            default :
                return true;
        }
    }

    public int getMatchRule() {
        return mMatchRule;
    }

    public java.lang.String getSubscriberId() {
        return mSubscriberId;
    }

    public java.lang.String getNetworkId() {
        return mNetworkId;
    }

    /**
     * Test if given {@link NetworkIdentity} matches this template.
     */
    public boolean matches(android.net.NetworkIdentity ident) {
        switch (mMatchRule) {
            case android.net.NetworkTemplate.MATCH_MOBILE_ALL :
                return matchesMobile(ident);
            case android.net.NetworkTemplate.MATCH_MOBILE_3G_LOWER :
                return matchesMobile3gLower(ident);
            case android.net.NetworkTemplate.MATCH_MOBILE_4G :
                return matchesMobile4g(ident);
            case android.net.NetworkTemplate.MATCH_WIFI :
                return matchesWifi(ident);
            case android.net.NetworkTemplate.MATCH_ETHERNET :
                return matchesEthernet(ident);
            case android.net.NetworkTemplate.MATCH_MOBILE_WILDCARD :
                return matchesMobileWildcard(ident);
            case android.net.NetworkTemplate.MATCH_WIFI_WILDCARD :
                return matchesWifiWildcard(ident);
            case android.net.NetworkTemplate.MATCH_BLUETOOTH :
                return matchesBluetooth(ident);
            case android.net.NetworkTemplate.MATCH_PROXY :
                return matchesProxy(ident);
            default :
                throw new java.lang.IllegalArgumentException("unknown network template");
        }
    }

    /**
     * Check if mobile network with matching IMSI.
     */
    private boolean matchesMobile(android.net.NetworkIdentity ident) {
        if (ident.mType == android.net.ConnectivityManager.TYPE_WIMAX) {
            // TODO: consider matching against WiMAX subscriber identity
            return true;
        } else {
            return ((android.net.NetworkTemplate.sForceAllNetworkTypes || ((ident.mType == android.net.ConnectivityManager.TYPE_MOBILE) && ident.mMetered)) && (!com.android.internal.util.ArrayUtils.isEmpty(mMatchSubscriberIds))) && com.android.internal.util.ArrayUtils.contains(mMatchSubscriberIds, ident.mSubscriberId);
        }
    }

    /**
     * Check if mobile network classified 3G or lower with matching IMSI.
     */
    @java.lang.Deprecated
    private boolean matchesMobile3gLower(android.net.NetworkIdentity ident) {
        android.net.NetworkTemplate.ensureSubtypeAvailable();
        if (ident.mType == android.net.ConnectivityManager.TYPE_WIMAX) {
            return false;
        } else
            if (matchesMobile(ident)) {
                switch (android.telephony.TelephonyManager.getNetworkClass(ident.mSubType)) {
                    case android.telephony.TelephonyManager.NETWORK_CLASS_UNKNOWN :
                    case android.telephony.TelephonyManager.NETWORK_CLASS_2_G :
                    case android.telephony.TelephonyManager.NETWORK_CLASS_3_G :
                        return true;
                }
            }

        return false;
    }

    /**
     * Check if mobile network classified 4G with matching IMSI.
     */
    @java.lang.Deprecated
    private boolean matchesMobile4g(android.net.NetworkIdentity ident) {
        android.net.NetworkTemplate.ensureSubtypeAvailable();
        if (ident.mType == android.net.ConnectivityManager.TYPE_WIMAX) {
            // TODO: consider matching against WiMAX subscriber identity
            return true;
        } else
            if (matchesMobile(ident)) {
                switch (android.telephony.TelephonyManager.getNetworkClass(ident.mSubType)) {
                    case android.telephony.TelephonyManager.NETWORK_CLASS_4_G :
                        return true;
                }
            }

        return false;
    }

    /**
     * Check if matches Wi-Fi network template.
     */
    private boolean matchesWifi(android.net.NetworkIdentity ident) {
        switch (ident.mType) {
            case android.net.ConnectivityManager.TYPE_WIFI :
                return java.util.Objects.equals(android.net.wifi.WifiInfo.removeDoubleQuotes(mNetworkId), android.net.wifi.WifiInfo.removeDoubleQuotes(ident.mNetworkId));
            default :
                return false;
        }
    }

    /**
     * Check if matches Ethernet network template.
     */
    private boolean matchesEthernet(android.net.NetworkIdentity ident) {
        if (ident.mType == android.net.ConnectivityManager.TYPE_ETHERNET) {
            return true;
        }
        return false;
    }

    private boolean matchesMobileWildcard(android.net.NetworkIdentity ident) {
        if (ident.mType == android.net.ConnectivityManager.TYPE_WIMAX) {
            return true;
        } else {
            return android.net.NetworkTemplate.sForceAllNetworkTypes || ((ident.mType == android.net.ConnectivityManager.TYPE_MOBILE) && ident.mMetered);
        }
    }

    private boolean matchesWifiWildcard(android.net.NetworkIdentity ident) {
        switch (ident.mType) {
            case android.net.ConnectivityManager.TYPE_WIFI :
            case android.net.ConnectivityManager.TYPE_WIFI_P2P :
                return true;
            default :
                return false;
        }
    }

    /**
     * Check if matches Bluetooth network template.
     */
    private boolean matchesBluetooth(android.net.NetworkIdentity ident) {
        if (ident.mType == android.net.ConnectivityManager.TYPE_BLUETOOTH) {
            return true;
        }
        return false;
    }

    /**
     * Check if matches Proxy network template.
     */
    private boolean matchesProxy(android.net.NetworkIdentity ident) {
        return ident.mType == android.net.ConnectivityManager.TYPE_PROXY;
    }

    private static java.lang.String getMatchRuleName(int matchRule) {
        switch (matchRule) {
            case android.net.NetworkTemplate.MATCH_MOBILE_3G_LOWER :
                return "MOBILE_3G_LOWER";
            case android.net.NetworkTemplate.MATCH_MOBILE_4G :
                return "MOBILE_4G";
            case android.net.NetworkTemplate.MATCH_MOBILE_ALL :
                return "MOBILE_ALL";
            case android.net.NetworkTemplate.MATCH_WIFI :
                return "WIFI";
            case android.net.NetworkTemplate.MATCH_ETHERNET :
                return "ETHERNET";
            case android.net.NetworkTemplate.MATCH_MOBILE_WILDCARD :
                return "MOBILE_WILDCARD";
            case android.net.NetworkTemplate.MATCH_WIFI_WILDCARD :
                return "WIFI_WILDCARD";
            case android.net.NetworkTemplate.MATCH_BLUETOOTH :
                return "BLUETOOTH";
            case android.net.NetworkTemplate.MATCH_PROXY :
                return "PROXY";
            default :
                return "UNKNOWN";
        }
    }

    private static void ensureSubtypeAvailable() {
        if (android.net.NetworkIdentity.COMBINE_SUBTYPE_ENABLED) {
            throw new java.lang.IllegalArgumentException("Unable to enforce 3G_LOWER template on combined data.");
        }
    }

    /**
     * Examine the given template and normalize if it refers to a "merged"
     * mobile subscriber. We pick the "lowest" merged subscriber as the primary
     * for key purposes, and expand the template to match all other merged
     * subscribers.
     * <p>
     * For example, given an incoming template matching B, and the currently
     * active merge set [A,B], we'd return a new template that primarily matches
     * A, but also matches B.
     */
    public static android.net.NetworkTemplate normalize(android.net.NetworkTemplate template, java.lang.String[] merged) {
        if (template.isMatchRuleMobile() && com.android.internal.util.ArrayUtils.contains(merged, template.mSubscriberId)) {
            // Requested template subscriber is part of the merge group; return
            // a template that matches all merged subscribers.
            return new android.net.NetworkTemplate(template.mMatchRule, merged[0], merged, template.mNetworkId);
        } else {
            return template;
        }
    }

    public static final android.os.Parcelable.Creator<android.net.NetworkTemplate> CREATOR = new android.os.Parcelable.Creator<android.net.NetworkTemplate>() {
        @java.lang.Override
        public android.net.NetworkTemplate createFromParcel(android.os.Parcel in) {
            return new android.net.NetworkTemplate(in);
        }

        @java.lang.Override
        public android.net.NetworkTemplate[] newArray(int size) {
            return new android.net.NetworkTemplate[size];
        }
    };

    public byte[] getBytesForBackup() throws java.io.IOException {
        java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
        java.io.DataOutputStream out = new java.io.DataOutputStream(baos);
        out.writeInt(android.net.NetworkTemplate.BACKUP_VERSION);
        out.writeInt(mMatchRule);
        android.util.BackupUtils.writeString(out, mSubscriberId);
        android.util.BackupUtils.writeString(out, mNetworkId);
        return baos.toByteArray();
    }

    public static android.net.NetworkTemplate getNetworkTemplateFromBackup(java.io.DataInputStream in) throws android.util.BackupUtils.BadVersionException, java.io.IOException {
        int version = in.readInt();
        if ((version < 1) || (version > android.net.NetworkTemplate.BACKUP_VERSION)) {
            throw new android.util.BackupUtils.BadVersionException("Unknown Backup Serialization Version");
        }
        int matchRule = in.readInt();
        java.lang.String subscriberId = android.util.BackupUtils.readString(in);
        java.lang.String networkId = android.util.BackupUtils.readString(in);
        return new android.net.NetworkTemplate(matchRule, subscriberId, networkId);
    }
}

