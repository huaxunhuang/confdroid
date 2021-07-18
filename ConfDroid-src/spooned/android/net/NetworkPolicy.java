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
 * Policy for networks matching a {@link NetworkTemplate}, including usage cycle
 * and limits to be enforced.
 *
 * @unknown 
 */
public class NetworkPolicy implements android.os.Parcelable , java.lang.Comparable<android.net.NetworkPolicy> {
    /**
     * Current Version of the Backup Serializer.
     */
    private static final int BACKUP_VERSION = 1;

    public static final int CYCLE_NONE = -1;

    public static final long WARNING_DISABLED = -1;

    public static final long LIMIT_DISABLED = -1;

    public static final long SNOOZE_NEVER = -1;

    public android.net.NetworkTemplate template;

    public int cycleDay;

    public java.lang.String cycleTimezone;

    public long warningBytes;

    public long limitBytes;

    public long lastWarningSnooze;

    public long lastLimitSnooze;

    public boolean metered;

    public boolean inferred;

    private static final long DEFAULT_MTU = 1500;

    @java.lang.Deprecated
    public NetworkPolicy(android.net.NetworkTemplate template, int cycleDay, java.lang.String cycleTimezone, long warningBytes, long limitBytes, boolean metered) {
        this(template, cycleDay, cycleTimezone, warningBytes, limitBytes, android.net.NetworkPolicy.SNOOZE_NEVER, android.net.NetworkPolicy.SNOOZE_NEVER, metered, false);
    }

    public NetworkPolicy(android.net.NetworkTemplate template, int cycleDay, java.lang.String cycleTimezone, long warningBytes, long limitBytes, long lastWarningSnooze, long lastLimitSnooze, boolean metered, boolean inferred) {
        this.template = com.android.internal.util.Preconditions.checkNotNull(template, "missing NetworkTemplate");
        this.cycleDay = cycleDay;
        this.cycleTimezone = com.android.internal.util.Preconditions.checkNotNull(cycleTimezone, "missing cycleTimezone");
        this.warningBytes = warningBytes;
        this.limitBytes = limitBytes;
        this.lastWarningSnooze = lastWarningSnooze;
        this.lastLimitSnooze = lastLimitSnooze;
        this.metered = metered;
        this.inferred = inferred;
    }

    public NetworkPolicy(android.os.Parcel in) {
        template = in.readParcelable(null);
        cycleDay = in.readInt();
        cycleTimezone = in.readString();
        warningBytes = in.readLong();
        limitBytes = in.readLong();
        lastWarningSnooze = in.readLong();
        lastLimitSnooze = in.readLong();
        metered = in.readInt() != 0;
        inferred = in.readInt() != 0;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeParcelable(template, flags);
        dest.writeInt(cycleDay);
        dest.writeString(cycleTimezone);
        dest.writeLong(warningBytes);
        dest.writeLong(limitBytes);
        dest.writeLong(lastWarningSnooze);
        dest.writeLong(lastLimitSnooze);
        dest.writeInt(metered ? 1 : 0);
        dest.writeInt(inferred ? 1 : 0);
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    /**
     * Test if given measurement is over {@link #warningBytes}.
     */
    public boolean isOverWarning(long totalBytes) {
        return (warningBytes != android.net.NetworkPolicy.WARNING_DISABLED) && (totalBytes >= warningBytes);
    }

    /**
     * Test if given measurement is near enough to {@link #limitBytes} to be
     * considered over-limit.
     */
    public boolean isOverLimit(long totalBytes) {
        // over-estimate, since kernel will trigger limit once first packet
        // trips over limit.
        totalBytes += 2 * android.net.NetworkPolicy.DEFAULT_MTU;
        return (limitBytes != android.net.NetworkPolicy.LIMIT_DISABLED) && (totalBytes >= limitBytes);
    }

    /**
     * Clear any existing snooze values, setting to {@link #SNOOZE_NEVER}.
     */
    public void clearSnooze() {
        lastWarningSnooze = android.net.NetworkPolicy.SNOOZE_NEVER;
        lastLimitSnooze = android.net.NetworkPolicy.SNOOZE_NEVER;
    }

    /**
     * Test if this policy has a cycle defined, after which usage should reset.
     */
    public boolean hasCycle() {
        return cycleDay != android.net.NetworkPolicy.CYCLE_NONE;
    }

    @java.lang.Override
    public int compareTo(android.net.NetworkPolicy another) {
        if ((another == null) || (another.limitBytes == android.net.NetworkPolicy.LIMIT_DISABLED)) {
            // other value is missing or disabled; we win
            return -1;
        }
        if ((limitBytes == android.net.NetworkPolicy.LIMIT_DISABLED) || (another.limitBytes < limitBytes)) {
            // we're disabled or other limit is smaller; they win
            return 1;
        }
        return 0;
    }

    @java.lang.Override
    public int hashCode() {
        return java.util.Objects.hash(template, cycleDay, cycleTimezone, warningBytes, limitBytes, lastWarningSnooze, lastLimitSnooze, metered, inferred);
    }

    @java.lang.Override
    public boolean equals(java.lang.Object obj) {
        if (obj instanceof android.net.NetworkPolicy) {
            final android.net.NetworkPolicy other = ((android.net.NetworkPolicy) (obj));
            return ((((((((cycleDay == other.cycleDay) && (warningBytes == other.warningBytes)) && (limitBytes == other.limitBytes)) && (lastWarningSnooze == other.lastWarningSnooze)) && (lastLimitSnooze == other.lastLimitSnooze)) && (metered == other.metered)) && (inferred == other.inferred)) && java.util.Objects.equals(cycleTimezone, other.cycleTimezone)) && java.util.Objects.equals(template, other.template);
        }
        return false;
    }

    @java.lang.Override
    public java.lang.String toString() {
        final java.lang.StringBuilder builder = new java.lang.StringBuilder("NetworkPolicy");
        builder.append("[").append(template).append("]:");
        builder.append(" cycleDay=").append(cycleDay);
        builder.append(", cycleTimezone=").append(cycleTimezone);
        builder.append(", warningBytes=").append(warningBytes);
        builder.append(", limitBytes=").append(limitBytes);
        builder.append(", lastWarningSnooze=").append(lastWarningSnooze);
        builder.append(", lastLimitSnooze=").append(lastLimitSnooze);
        builder.append(", metered=").append(metered);
        builder.append(", inferred=").append(inferred);
        return builder.toString();
    }

    public static final android.os.Parcelable.Creator<android.net.NetworkPolicy> CREATOR = new android.os.Parcelable.Creator<android.net.NetworkPolicy>() {
        @java.lang.Override
        public android.net.NetworkPolicy createFromParcel(android.os.Parcel in) {
            return new android.net.NetworkPolicy(in);
        }

        @java.lang.Override
        public android.net.NetworkPolicy[] newArray(int size) {
            return new android.net.NetworkPolicy[size];
        }
    };

    public byte[] getBytesForBackup() throws java.io.IOException {
        java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
        java.io.DataOutputStream out = new java.io.DataOutputStream(baos);
        out.writeInt(android.net.NetworkPolicy.BACKUP_VERSION);
        out.write(template.getBytesForBackup());
        out.writeInt(cycleDay);
        android.util.BackupUtils.writeString(out, cycleTimezone);
        out.writeLong(warningBytes);
        out.writeLong(limitBytes);
        out.writeLong(lastWarningSnooze);
        out.writeLong(lastLimitSnooze);
        out.writeInt(metered ? 1 : 0);
        out.writeInt(inferred ? 1 : 0);
        return baos.toByteArray();
    }

    public static android.net.NetworkPolicy getNetworkPolicyFromBackup(java.io.DataInputStream in) throws android.util.BackupUtils.BadVersionException, java.io.IOException {
        int version = in.readInt();
        if ((version < 1) || (version > android.net.NetworkPolicy.BACKUP_VERSION)) {
            throw new android.util.BackupUtils.BadVersionException("Unknown Backup Serialization Version");
        }
        android.net.NetworkTemplate template = android.net.NetworkTemplate.getNetworkTemplateFromBackup(in);
        int cycleDay = in.readInt();
        java.lang.String cycleTimeZone = android.util.BackupUtils.readString(in);
        long warningBytes = in.readLong();
        long limitBytes = in.readLong();
        long lastWarningSnooze = in.readLong();
        long lastLimitSnooze = in.readLong();
        boolean metered = in.readInt() == 1;
        boolean inferred = in.readInt() == 1;
        return new android.net.NetworkPolicy(template, cycleDay, cycleTimeZone, warningBytes, limitBytes, lastWarningSnooze, lastLimitSnooze, metered, inferred);
    }
}

