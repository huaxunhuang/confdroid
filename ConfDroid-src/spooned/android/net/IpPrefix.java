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
package android.net;


/**
 * This class represents an IP prefix, i.e., a contiguous block of IP addresses aligned on a
 * power of two boundary (also known as an "IP subnet"). A prefix is specified by two pieces of
 * information:
 *
 * <ul>
 * <li>A starting IP address (IPv4 or IPv6). This is the first IP address of the prefix.
 * <li>A prefix length. This specifies the length of the prefix by specifing the number of bits
 *     in the IP address, starting from the most significant bit in network byte order, that
 *     are constant for all addresses in the prefix.
 * </ul>
 *
 * For example, the prefix <code>192.0.2.0/24</code> covers the 256 IPv4 addresses from
 * <code>192.0.2.0</code> to <code>192.0.2.255</code>, inclusive, and the prefix
 * <code>2001:db8:1:2</code>  covers the 2^64 IPv6 addresses from <code>2001:db8:1:2::</code> to
 * <code>2001:db8:1:2:ffff:ffff:ffff:ffff</code>, inclusive.
 *
 * Objects of this class are immutable.
 */
public final class IpPrefix implements android.os.Parcelable {
    private final byte[] address;// network byte order


    private final int prefixLength;

    private void checkAndMaskAddressAndPrefixLength() {
        if ((address.length != 4) && (address.length != 16)) {
            throw new java.lang.IllegalArgumentException(("IpPrefix has " + address.length) + " bytes which is neither 4 nor 16");
        }
        android.net.NetworkUtils.maskRawAddress(address, prefixLength);
    }

    /**
     * Constructs a new {@code IpPrefix} from a byte array containing an IPv4 or IPv6 address in
     * network byte order and a prefix length. Silently truncates the address to the prefix length,
     * so for example {@code 192.0.2.1/24} is silently converted to {@code 192.0.2.0/24}.
     *
     * @param address
     * 		the IP address. Must be non-null and exactly 4 or 16 bytes long.
     * @param prefixLength
     * 		the prefix length. Must be &gt;= 0 and &lt;= (32 or 128) (IPv4 or IPv6).
     * @unknown 
     */
    public IpPrefix(byte[] address, int prefixLength) {
        this.address = address.clone();
        this.prefixLength = prefixLength;
        checkAndMaskAddressAndPrefixLength();
    }

    /**
     * Constructs a new {@code IpPrefix} from an IPv4 or IPv6 address and a prefix length. Silently
     * truncates the address to the prefix length, so for example {@code 192.0.2.1/24} is silently
     * converted to {@code 192.0.2.0/24}.
     *
     * @param address
     * 		the IP address. Must be non-null.
     * @param prefixLength
     * 		the prefix length. Must be &gt;= 0 and &lt;= (32 or 128) (IPv4 or IPv6).
     * @unknown 
     */
    public IpPrefix(java.net.InetAddress address, int prefixLength) {
        // We don't reuse the (byte[], int) constructor because it calls clone() on the byte array,
        // which is unnecessary because getAddress() already returns a clone.
        this.address = address.getAddress();
        this.prefixLength = prefixLength;
        checkAndMaskAddressAndPrefixLength();
    }

    /**
     * Constructs a new IpPrefix from a string such as "192.0.2.1/24" or "2001:db8::1/64".
     * Silently truncates the address to the prefix length, so for example {@code 192.0.2.1/24}
     * is silently converted to {@code 192.0.2.0/24}.
     *
     * @param prefix
     * 		the prefix to parse
     * @unknown 
     */
    public IpPrefix(java.lang.String prefix) {
        // We don't reuse the (InetAddress, int) constructor because "error: call to this must be
        // first statement in constructor". We could factor out setting the member variables to an
        // init() method, but if we did, then we'd have to make the members non-final, or "error:
        // cannot assign a value to final variable address". So we just duplicate the code here.
        android.util.Pair<java.net.InetAddress, java.lang.Integer> ipAndMask = android.net.NetworkUtils.parseIpAndMask(prefix);
        this.address = ipAndMask.first.getAddress();
        this.prefixLength = ipAndMask.second;
        checkAndMaskAddressAndPrefixLength();
    }

    /**
     * Compares this {@code IpPrefix} object against the specified object in {@code obj}. Two
     * objects are equal if they have the same startAddress and prefixLength.
     *
     * @param obj
     * 		the object to be tested for equality.
     * @return {@code true} if both objects are equal, {@code false} otherwise.
     */
    @java.lang.Override
    public boolean equals(java.lang.Object obj) {
        if (!(obj instanceof android.net.IpPrefix)) {
            return false;
        }
        android.net.IpPrefix that = ((android.net.IpPrefix) (obj));
        return java.util.Arrays.equals(this.address, that.address) && (this.prefixLength == that.prefixLength);
    }

    /**
     * Gets the hashcode of the represented IP prefix.
     *
     * @return the appropriate hashcode value.
     */
    @java.lang.Override
    public int hashCode() {
        return java.util.Arrays.hashCode(address) + (11 * prefixLength);
    }

    /**
     * Returns a copy of the first IP address in the prefix. Modifying the returned object does not
     * change this object's contents.
     *
     * @return the address in the form of a byte array.
     */
    public java.net.InetAddress getAddress() {
        try {
            return java.net.InetAddress.getByAddress(address);
        } catch (java.net.UnknownHostException e) {
            // Cannot happen. InetAddress.getByAddress can only throw an exception if the byte
            // array is the wrong length, but we check that in the constructor.
            return null;
        }
    }

    /**
     * Returns a copy of the IP address bytes in network order (the highest order byte is the zeroth
     * element). Modifying the returned array does not change this object's contents.
     *
     * @return the address in the form of a byte array.
     */
    public byte[] getRawAddress() {
        return address.clone();
    }

    /**
     * Returns the prefix length of this {@code IpPrefix}.
     *
     * @return the prefix length.
     */
    public int getPrefixLength() {
        return prefixLength;
    }

    /**
     * Determines whether the prefix contains the specified address.
     *
     * @param address
     * 		An {@link InetAddress} to test.
     * @return {@code true} if the prefix covers the given address.
     */
    public boolean contains(java.net.InetAddress address) {
        byte[] addrBytes = (address == null) ? null : address.getAddress();
        if ((addrBytes == null) || (addrBytes.length != this.address.length)) {
            return false;
        }
        android.net.NetworkUtils.maskRawAddress(addrBytes, prefixLength);
        return java.util.Arrays.equals(this.address, addrBytes);
    }

    /**
     * Returns a string representation of this {@code IpPrefix}.
     *
     * @return a string such as {@code "192.0.2.0/24"} or {@code "2001:db8:1:2::/64"}.
     */
    public java.lang.String toString() {
        try {
            return (java.net.InetAddress.getByAddress(address).getHostAddress() + "/") + prefixLength;
        } catch (java.net.UnknownHostException e) {
            // Cosmic rays?
            throw new java.lang.IllegalStateException("IpPrefix with invalid address! Shouldn't happen.", e);
        }
    }

    /**
     * Implement the Parcelable interface.
     */
    public int describeContents() {
        return 0;
    }

    /**
     * Implement the Parcelable interface.
     */
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeByteArray(address);
        dest.writeInt(prefixLength);
    }

    /**
     * Implement the Parcelable interface.
     */
    public static final android.os.Parcelable.Creator<android.net.IpPrefix> CREATOR = new android.os.Parcelable.Creator<android.net.IpPrefix>() {
        public android.net.IpPrefix createFromParcel(android.os.Parcel in) {
            byte[] address = in.createByteArray();
            int prefixLength = in.readInt();
            return new android.net.IpPrefix(address, prefixLength);
        }

        public android.net.IpPrefix[] newArray(int size) {
            return new android.net.IpPrefix[size];
        }
    };
}

