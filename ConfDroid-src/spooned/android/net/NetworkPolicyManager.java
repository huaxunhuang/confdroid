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
 * Manager for creating and modifying network policy rules.
 *
 * {@hide }
 */
public class NetworkPolicyManager {
    /* POLICY_* are masks and can be ORed */
    /**
     * No specific network policy, use system default.
     */
    public static final int POLICY_NONE = 0x0;

    /**
     * Reject network usage on metered networks when application in background.
     */
    public static final int POLICY_REJECT_METERED_BACKGROUND = 0x1;

    /**
     * Allow network use (metered or not) in the background in battery save mode.
     */
    public static final int POLICY_ALLOW_BACKGROUND_BATTERY_SAVE = 0x2;

    /* Rules defining whether an uid has access to a network given its type (metered / non-metered).

    These rules are bits and can be used in bitmask operations; in particular:
    - rule & RULE_MASK_METERED: returns the metered-networks status.
    - rule & RULE_MASK_ALL: returns the all-networks status.

    The RULE_xxx_ALL rules applies to all networks (metered or non-metered), but on
    metered networks, the RULE_xxx_METERED rules should be checked first. For example,
    if the device is on Battery Saver Mode and Data Saver Mode simulatenously, and a uid
    is whitelisted for the former but not the latter, its status would be
    RULE_REJECT_METERED | RULE_ALLOW_ALL, meaning it could have access to non-metered
    networks but not to metered networks.

    See network-policy-restrictions.md for more info.
     */
    /**
     * No specific rule was set
     */
    public static final int RULE_NONE = 0;

    /**
     * Allow traffic on metered networks.
     */
    public static final int RULE_ALLOW_METERED = 1 << 0;

    /**
     * Temporarily allow traffic on metered networks because app is on foreground.
     */
    public static final int RULE_TEMPORARY_ALLOW_METERED = 1 << 1;

    /**
     * Reject traffic on metered networks.
     */
    public static final int RULE_REJECT_METERED = 1 << 2;

    /**
     * Network traffic should be allowed on all networks (metered or non-metered), although
     * metered-network restrictions could still apply.
     */
    public static final int RULE_ALLOW_ALL = 1 << 5;

    /**
     * Reject traffic on all networks.
     */
    public static final int RULE_REJECT_ALL = 1 << 6;

    /**
     * Mask used to get the {@code RULE_xxx_METERED} rules
     */
    public static final int MASK_METERED_NETWORKS = 0b1111;

    /**
     * Mask used to get the {@code RULE_xxx_ALL} rules
     */
    public static final int MASK_ALL_NETWORKS = 0b11110000;

    public static final int FIREWALL_RULE_DEFAULT = 0;

    public static final int FIREWALL_RULE_ALLOW = 1;

    public static final int FIREWALL_RULE_DENY = 2;

    public static final int FIREWALL_TYPE_WHITELIST = 0;

    public static final int FIREWALL_TYPE_BLACKLIST = 1;

    public static final int FIREWALL_CHAIN_NONE = 0;

    public static final int FIREWALL_CHAIN_DOZABLE = 1;

    public static final int FIREWALL_CHAIN_STANDBY = 2;

    public static final int FIREWALL_CHAIN_POWERSAVE = 3;

    public static final java.lang.String FIREWALL_CHAIN_NAME_NONE = "none";

    public static final java.lang.String FIREWALL_CHAIN_NAME_DOZABLE = "dozable";

    public static final java.lang.String FIREWALL_CHAIN_NAME_STANDBY = "standby";

    public static final java.lang.String FIREWALL_CHAIN_NAME_POWERSAVE = "powersave";

    private static final boolean ALLOW_PLATFORM_APP_POLICY = true;

    /**
     * {@link Intent} extra that indicates which {@link NetworkTemplate} rule it
     * applies to.
     */
    public static final java.lang.String EXTRA_NETWORK_TEMPLATE = "android.net.NETWORK_TEMPLATE";

    private final android.content.Context mContext;

    private android.net.INetworkPolicyManager mService;

    public NetworkPolicyManager(android.content.Context context, android.net.INetworkPolicyManager service) {
        if (service == null) {
            throw new java.lang.IllegalArgumentException("missing INetworkPolicyManager");
        }
        mContext = context;
        mService = service;
    }

    public static android.net.NetworkPolicyManager from(android.content.Context context) {
        return ((android.net.NetworkPolicyManager) (context.getSystemService(android.content.Context.NETWORK_POLICY_SERVICE)));
    }

    /**
     * Set policy flags for specific UID.
     *
     * @param policy
     * 		{@link #POLICY_NONE} or combination of flags like
     * 		{@link #POLICY_REJECT_METERED_BACKGROUND} or {@link #POLICY_ALLOW_BACKGROUND_BATTERY_SAVE}.
     */
    public void setUidPolicy(int uid, int policy) {
        try {
            mService.setUidPolicy(uid, policy);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * Add policy flags for specific UID.  The given policy bits will be set for
     * the uid.  Policy flags may be either
     * {@link #POLICY_REJECT_METERED_BACKGROUND} or {@link #POLICY_ALLOW_BACKGROUND_BATTERY_SAVE}.
     */
    public void addUidPolicy(int uid, int policy) {
        try {
            mService.addUidPolicy(uid, policy);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * Clear/remove policy flags for specific UID.  The given policy bits will be set for
     * the uid.  Policy flags may be either
     * {@link #POLICY_REJECT_METERED_BACKGROUND} or {@link #POLICY_ALLOW_BACKGROUND_BATTERY_SAVE}.
     */
    public void removeUidPolicy(int uid, int policy) {
        try {
            mService.removeUidPolicy(uid, policy);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public int getUidPolicy(int uid) {
        try {
            return mService.getUidPolicy(uid);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public int[] getUidsWithPolicy(int policy) {
        try {
            return mService.getUidsWithPolicy(policy);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void registerListener(android.net.INetworkPolicyListener listener) {
        try {
            mService.registerListener(listener);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void unregisterListener(android.net.INetworkPolicyListener listener) {
        try {
            mService.unregisterListener(listener);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void setNetworkPolicies(android.net.NetworkPolicy[] policies) {
        try {
            mService.setNetworkPolicies(policies);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public android.net.NetworkPolicy[] getNetworkPolicies() {
        try {
            return mService.getNetworkPolicies(mContext.getOpPackageName());
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void setRestrictBackground(boolean restrictBackground) {
        try {
            mService.setRestrictBackground(restrictBackground);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public boolean getRestrictBackground() {
        try {
            return mService.getRestrictBackground();
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * Resets network policy settings back to factory defaults.
     *
     * @unknown 
     */
    public void factoryReset(java.lang.String subscriber) {
        try {
            mService.factoryReset(subscriber);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * Compute the last cycle boundary for the given {@link NetworkPolicy}. For
     * example, if cycle day is 20th, and today is June 15th, it will return May
     * 20th. When cycle day doesn't exist in current month, it snaps to the 1st
     * of following month.
     *
     * @unknown 
     */
    public static long computeLastCycleBoundary(long currentTime, android.net.NetworkPolicy policy) {
        if (policy.cycleDay == android.net.NetworkPolicy.CYCLE_NONE) {
            throw new java.lang.IllegalArgumentException("Unable to compute boundary without cycleDay");
        }
        final java.util.Calendar cal = java.util.Calendar.getInstance(java.util.TimeZone.getTimeZone(policy.cycleTimezone));
        cal.setTimeInMillis(currentTime);
        android.net.NetworkPolicyManager.snapToCycleDay(cal, policy.cycleDay);
        if (cal.getTimeInMillis() >= currentTime) {
            // Cycle boundary is beyond now, use last cycle boundary
            cal.set(java.util.Calendar.DAY_OF_MONTH, 1);
            cal.add(java.util.Calendar.MONTH, -1);
            android.net.NetworkPolicyManager.snapToCycleDay(cal, policy.cycleDay);
        }
        return cal.getTimeInMillis();
    }

    /**
     * {@hide }
     */
    public static long computeNextCycleBoundary(long currentTime, android.net.NetworkPolicy policy) {
        if (policy.cycleDay == android.net.NetworkPolicy.CYCLE_NONE) {
            throw new java.lang.IllegalArgumentException("Unable to compute boundary without cycleDay");
        }
        final java.util.Calendar cal = java.util.Calendar.getInstance(java.util.TimeZone.getTimeZone(policy.cycleTimezone));
        cal.setTimeInMillis(currentTime);
        android.net.NetworkPolicyManager.snapToCycleDay(cal, policy.cycleDay);
        if (cal.getTimeInMillis() <= currentTime) {
            // Cycle boundary is before now, use next cycle boundary
            cal.set(java.util.Calendar.DAY_OF_MONTH, 1);
            cal.add(java.util.Calendar.MONTH, 1);
            android.net.NetworkPolicyManager.snapToCycleDay(cal, policy.cycleDay);
        }
        return cal.getTimeInMillis();
    }

    /**
     * Snap to the cycle day for the current month given; when cycle day doesn't
     * exist, it snaps to last second of current month.
     *
     * @unknown 
     */
    public static void snapToCycleDay(java.util.Calendar cal, int cycleDay) {
        cal.set(java.util.Calendar.HOUR_OF_DAY, 0);
        cal.set(java.util.Calendar.MINUTE, 0);
        cal.set(java.util.Calendar.SECOND, 0);
        if (cycleDay > cal.getActualMaximum(java.util.Calendar.DAY_OF_MONTH)) {
            cal.set(java.util.Calendar.DAY_OF_MONTH, 1);
            cal.add(java.util.Calendar.MONTH, 1);
            cal.add(java.util.Calendar.SECOND, -1);
        } else {
            cal.set(java.util.Calendar.DAY_OF_MONTH, cycleDay);
        }
    }

    /**
     * Check if given UID can have a {@link #setUidPolicy(int, int)} defined,
     * usually to protect critical system services.
     */
    @java.lang.Deprecated
    public static boolean isUidValidForPolicy(android.content.Context context, int uid) {
        // first, quick-reject non-applications
        if (!android.os.UserHandle.isApp(uid)) {
            return false;
        }
        if (!android.net.NetworkPolicyManager.ALLOW_PLATFORM_APP_POLICY) {
            final android.content.pm.PackageManager pm = context.getPackageManager();
            final java.util.HashSet<android.content.pm.Signature> systemSignature;
            try {
                systemSignature = com.google.android.collect.Sets.newHashSet(pm.getPackageInfo("android", android.content.pm.PackageManager.GET_SIGNATURES).signatures);
            } catch (android.content.pm.PackageManager.NameNotFoundException e) {
                throw new java.lang.RuntimeException("problem finding system signature", e);
            }
            try {
                // reject apps signed with platform cert
                for (java.lang.String packageName : pm.getPackagesForUid(uid)) {
                    final java.util.HashSet<android.content.pm.Signature> packageSignature = com.google.android.collect.Sets.newHashSet(pm.getPackageInfo(packageName, android.content.pm.PackageManager.GET_SIGNATURES).signatures);
                    if (packageSignature.containsAll(systemSignature)) {
                        return false;
                    }
                }
            } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            }
        }
        // nothing found above; we can apply policy to UID
        return true;
    }

    /* @hide */
    public static java.lang.String uidRulesToString(int uidRules) {
        final java.lang.StringBuilder string = new java.lang.StringBuilder().append(uidRules).append(" (");
        if (uidRules == android.net.NetworkPolicyManager.RULE_NONE) {
            string.append("NONE");
        } else {
            string.append(android.util.DebugUtils.flagsToString(android.net.NetworkPolicyManager.class, "RULE_", uidRules));
        }
        string.append(")");
        return string.toString();
    }
}

