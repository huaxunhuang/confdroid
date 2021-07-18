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
 * Network definition that includes strong identity. Analogous to combining
 * {@link NetworkInfo} and an IMSI.
 *
 * @unknown 
 */
public class NetworkIdentity implements java.lang.Comparable<android.net.NetworkIdentity> {
    private static final java.lang.String TAG = "NetworkIdentity";

    /**
     * When enabled, combine all {@link #mSubType} together under
     * {@link #SUBTYPE_COMBINED}.
     *
     * @deprecated we no longer offer to collect statistics on a per-subtype
    basis; this is always disabled.
     */
    @java.lang.Deprecated
    public static final boolean COMBINE_SUBTYPE_ENABLED = true;

    public static final int SUBTYPE_COMBINED = -1;

    final int mType;

    final int mSubType;

    final java.lang.String mSubscriberId;

    final java.lang.String mNetworkId;

    final boolean mRoaming;

    final boolean mMetered;

    public NetworkIdentity(int type, int subType, java.lang.String subscriberId, java.lang.String networkId, boolean roaming, boolean metered) {
        mType = type;
        mSubType = (android.net.NetworkIdentity.COMBINE_SUBTYPE_ENABLED) ? android.net.NetworkIdentity.SUBTYPE_COMBINED : subType;
        mSubscriberId = subscriberId;
        mNetworkId = networkId;
        mRoaming = roaming;
        mMetered = metered;
    }

    @java.lang.Override
    public int hashCode() {
        return java.util.Objects.hash(mType, mSubType, mSubscriberId, mNetworkId, mRoaming, mMetered);
    }

    @java.lang.Override
    public boolean equals(java.lang.Object obj) {
        if (obj instanceof android.net.NetworkIdentity) {
            final android.net.NetworkIdentity ident = ((android.net.NetworkIdentity) (obj));
            return (((((mType == ident.mType) && (mSubType == ident.mSubType)) && (mRoaming == ident.mRoaming)) && java.util.Objects.equals(mSubscriberId, ident.mSubscriberId)) && java.util.Objects.equals(mNetworkId, ident.mNetworkId)) && (mMetered == ident.mMetered);
        }
        return false;
    }

    @java.lang.Override
    public java.lang.String toString() {
        final java.lang.StringBuilder builder = new java.lang.StringBuilder("{");
        builder.append("type=").append(android.net.ConnectivityManager.getNetworkTypeName(mType));
        builder.append(", subType=");
        if (android.net.NetworkIdentity.COMBINE_SUBTYPE_ENABLED) {
            builder.append("COMBINED");
        } else
            if (android.net.ConnectivityManager.isNetworkTypeMobile(mType)) {
                builder.append(android.telephony.TelephonyManager.getNetworkTypeName(mSubType));
            } else {
                builder.append(mSubType);
            }

        if (mSubscriberId != null) {
            builder.append(", subscriberId=").append(android.net.NetworkIdentity.scrubSubscriberId(mSubscriberId));
        }
        if (mNetworkId != null) {
            builder.append(", networkId=").append(mNetworkId);
        }
        if (mRoaming) {
            builder.append(", ROAMING");
        }
        builder.append(", metered=").append(mMetered);
        return builder.append("}").toString();
    }

    public int getType() {
        return mType;
    }

    public int getSubType() {
        return mSubType;
    }

    public java.lang.String getSubscriberId() {
        return mSubscriberId;
    }

    public java.lang.String getNetworkId() {
        return mNetworkId;
    }

    public boolean getRoaming() {
        return mRoaming;
    }

    public boolean getMetered() {
        return mMetered;
    }

    /**
     * Scrub given IMSI on production builds.
     */
    public static java.lang.String scrubSubscriberId(java.lang.String subscriberId) {
        if ("eng".equals(android.os.Build.TYPE)) {
            return subscriberId;
        } else
            if (subscriberId != null) {
                // TODO: parse this as MCC+MNC instead of hard-coding
                return subscriberId.substring(0, java.lang.Math.min(6, subscriberId.length())) + "...";
            } else {
                return "null";
            }

    }

    /**
     * Scrub given IMSI on production builds.
     */
    public static java.lang.String[] scrubSubscriberId(java.lang.String[] subscriberId) {
        if (subscriberId == null)
            return null;

        final java.lang.String[] res = new java.lang.String[subscriberId.length];
        for (int i = 0; i < res.length; i++) {
            res[i] = android.net.NetworkIdentity.scrubSubscriberId(subscriberId[i]);
        }
        return res;
    }

    /**
     * Build a {@link NetworkIdentity} from the given {@link NetworkState},
     * assuming that any mobile networks are using the current IMSI.
     */
    public static android.net.NetworkIdentity buildNetworkIdentity(android.content.Context context, android.net.NetworkState state) {
        final int type = state.networkInfo.getType();
        final int subType = state.networkInfo.getSubtype();
        java.lang.String subscriberId = null;
        java.lang.String networkId = null;
        boolean roaming = false;
        boolean metered = false;
        if (android.net.ConnectivityManager.isNetworkTypeMobile(type)) {
            if (state.subscriberId == null) {
                if ((state.networkInfo.getState() != android.net.NetworkInfo.State.DISCONNECTED) && (state.networkInfo.getState() != android.net.NetworkInfo.State.UNKNOWN)) {
                    android.util.Slog.w(android.net.NetworkIdentity.TAG, "Active mobile network without subscriber! ni = " + state.networkInfo);
                }
            }
            subscriberId = state.subscriberId;
            roaming = state.networkInfo.isRoaming();
            metered = !state.networkCapabilities.hasCapability(android.net.NetworkCapabilities.NET_CAPABILITY_NOT_METERED);
        } else
            if (type == android.net.ConnectivityManager.TYPE_WIFI) {
                if (state.networkId != null) {
                    networkId = state.networkId;
                } else {
                    final android.net.wifi.WifiManager wifi = ((android.net.wifi.WifiManager) (context.getSystemService(android.content.Context.WIFI_SERVICE)));
                    final android.net.wifi.WifiInfo info = wifi.getConnectionInfo();
                    networkId = (info != null) ? info.getSSID() : null;
                }
            }

        return new android.net.NetworkIdentity(type, subType, subscriberId, networkId, roaming, metered);
    }

    @java.lang.Override
    public int compareTo(android.net.NetworkIdentity another) {
        int res = java.lang.Integer.compare(mType, another.mType);
        if (res == 0) {
            res = java.lang.Integer.compare(mSubType, another.mSubType);
        }
        if (((res == 0) && (mSubscriberId != null)) && (another.mSubscriberId != null)) {
            res = mSubscriberId.compareTo(another.mSubscriberId);
        }
        if (((res == 0) && (mNetworkId != null)) && (another.mNetworkId != null)) {
            res = mNetworkId.compareTo(another.mNetworkId);
        }
        if (res == 0) {
            res = java.lang.Boolean.compare(mRoaming, another.mRoaming);
        }
        if (res == 0) {
            res = java.lang.Boolean.compare(mMetered, another.mMetered);
        }
        return res;
    }
}

