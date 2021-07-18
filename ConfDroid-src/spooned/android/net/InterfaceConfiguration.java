/**
 * Copyright (C) 2008 The Android Open Source Project
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
 * Configuration details for a network interface.
 *
 * @unknown 
 */
public class InterfaceConfiguration implements android.os.Parcelable {
    private java.lang.String mHwAddr;

    private android.net.LinkAddress mAddr;

    private java.util.HashSet<java.lang.String> mFlags = com.google.android.collect.Sets.newHashSet();

    private static final java.lang.String FLAG_UP = "up";

    private static final java.lang.String FLAG_DOWN = "down";

    @java.lang.Override
    public java.lang.String toString() {
        final java.lang.StringBuilder builder = new java.lang.StringBuilder();
        builder.append("mHwAddr=").append(mHwAddr);
        builder.append(" mAddr=").append(java.lang.String.valueOf(mAddr));
        builder.append(" mFlags=").append(getFlags());
        return builder.toString();
    }

    public java.lang.Iterable<java.lang.String> getFlags() {
        return mFlags;
    }

    public boolean hasFlag(java.lang.String flag) {
        android.net.InterfaceConfiguration.validateFlag(flag);
        return mFlags.contains(flag);
    }

    public void clearFlag(java.lang.String flag) {
        android.net.InterfaceConfiguration.validateFlag(flag);
        mFlags.remove(flag);
    }

    public void setFlag(java.lang.String flag) {
        android.net.InterfaceConfiguration.validateFlag(flag);
        mFlags.add(flag);
    }

    /**
     * Set flags to mark interface as up.
     */
    public void setInterfaceUp() {
        mFlags.remove(android.net.InterfaceConfiguration.FLAG_DOWN);
        mFlags.add(android.net.InterfaceConfiguration.FLAG_UP);
    }

    /**
     * Set flags to mark interface as down.
     */
    public void setInterfaceDown() {
        mFlags.remove(android.net.InterfaceConfiguration.FLAG_UP);
        mFlags.add(android.net.InterfaceConfiguration.FLAG_DOWN);
    }

    public android.net.LinkAddress getLinkAddress() {
        return mAddr;
    }

    public void setLinkAddress(android.net.LinkAddress addr) {
        mAddr = addr;
    }

    public java.lang.String getHardwareAddress() {
        return mHwAddr;
    }

    public void setHardwareAddress(java.lang.String hwAddr) {
        mHwAddr = hwAddr;
    }

    /**
     * This function determines if the interface is up and has a valid IP
     * configuration (IP address has a non zero octet).
     *
     * Note: It is supposed to be quick and hence should not initiate
     * any network activity
     */
    public boolean isActive() {
        try {
            if (hasFlag(android.net.InterfaceConfiguration.FLAG_UP)) {
                for (byte b : mAddr.getAddress().getAddress()) {
                    if (b != 0)
                        return true;

                }
            }
        } catch (java.lang.NullPointerException e) {
            return false;
        }
        return false;
    }

    /**
     * {@inheritDoc }
     */
    public int describeContents() {
        return 0;
    }

    /**
     * {@inheritDoc }
     */
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeString(mHwAddr);
        if (mAddr != null) {
            dest.writeByte(((byte) (1)));
            dest.writeParcelable(mAddr, flags);
        } else {
            dest.writeByte(((byte) (0)));
        }
        dest.writeInt(mFlags.size());
        for (java.lang.String flag : mFlags) {
            dest.writeString(flag);
        }
    }

    public static final android.os.Parcelable.Creator<android.net.InterfaceConfiguration> CREATOR = new android.os.Parcelable.Creator<android.net.InterfaceConfiguration>() {
        public android.net.InterfaceConfiguration createFromParcel(android.os.Parcel in) {
            android.net.InterfaceConfiguration info = new android.net.InterfaceConfiguration();
            info.mHwAddr = in.readString();
            if (in.readByte() == 1) {
                info.mAddr = in.readParcelable(null);
            }
            final int size = in.readInt();
            for (int i = 0; i < size; i++) {
                info.mFlags.add(in.readString());
            }
            return info;
        }

        public android.net.InterfaceConfiguration[] newArray(int size) {
            return new android.net.InterfaceConfiguration[size];
        }
    };

    private static void validateFlag(java.lang.String flag) {
        if (flag.indexOf(' ') >= 0) {
            throw new java.lang.IllegalArgumentException("flag contains space: " + flag);
        }
    }
}

