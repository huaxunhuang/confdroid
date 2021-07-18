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
package android.bluetooth;


/**
 * The Bluetooth Health Application Configuration that is used in conjunction with
 * the {@link BluetoothHealth} class. This class represents an application configuration
 * that the Bluetooth Health third party application will register to communicate with the
 * remote Bluetooth health device.
 */
public final class BluetoothHealthAppConfiguration implements android.os.Parcelable {
    private final java.lang.String mName;

    private final int mDataType;

    private final int mRole;

    private final int mChannelType;

    /**
     * Constructor to register the SINK role
     *
     * @param name
     * 		Friendly name associated with the application configuration
     * @param dataType
     * 		Data Type of the remote Bluetooth Health device
     * @unknown 
     */
    BluetoothHealthAppConfiguration(java.lang.String name, int dataType) {
        mName = name;
        mDataType = dataType;
        mRole = android.bluetooth.BluetoothHealth.SINK_ROLE;
        mChannelType = android.bluetooth.BluetoothHealth.CHANNEL_TYPE_ANY;
    }

    /**
     * Constructor to register the application configuration.
     *
     * @param name
     * 		Friendly name associated with the application configuration
     * @param dataType
     * 		Data Type of the remote Bluetooth Health device
     * @param role
     * 		{@link BluetoothHealth#SOURCE_ROLE} or
     * 		{@link BluetoothHealth#SINK_ROLE}
     * @unknown 
     */
    BluetoothHealthAppConfiguration(java.lang.String name, int dataType, int role, int channelType) {
        mName = name;
        mDataType = dataType;
        mRole = role;
        mChannelType = channelType;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (o instanceof android.bluetooth.BluetoothHealthAppConfiguration) {
            android.bluetooth.BluetoothHealthAppConfiguration config = ((android.bluetooth.BluetoothHealthAppConfiguration) (o));
            if (mName == null)
                return false;

            return ((mName.equals(config.getName()) && (mDataType == config.getDataType())) && (mRole == config.getRole())) && (mChannelType == config.getChannelType());
        }
        return false;
    }

    @java.lang.Override
    public int hashCode() {
        int result = 17;
        result = (31 * result) + (mName != null ? mName.hashCode() : 0);
        result = (31 * result) + mDataType;
        result = (31 * result) + mRole;
        result = (31 * result) + mChannelType;
        return result;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return ((((((("BluetoothHealthAppConfiguration [mName = " + mName) + ",mDataType = ") + mDataType) + ", mRole = ") + mRole) + ",mChannelType = ") + mChannelType) + "]";
    }

    public int describeContents() {
        return 0;
    }

    /**
     * Return the data type associated with this application configuration.
     *
     * @return dataType
     */
    public int getDataType() {
        return mDataType;
    }

    /**
     * Return the name of the application configuration.
     *
     * @return String name
     */
    public java.lang.String getName() {
        return mName;
    }

    /**
     * Return the role associated with this application configuration.
     *
     * @return One of {@link BluetoothHealth#SOURCE_ROLE} or
    {@link BluetoothHealth#SINK_ROLE}
     */
    public int getRole() {
        return mRole;
    }

    /**
     * Return the channel type associated with this application configuration.
     *
     * @return One of {@link BluetoothHealth#CHANNEL_TYPE_RELIABLE} or
    {@link BluetoothHealth#CHANNEL_TYPE_STREAMING} or
    {@link BluetoothHealth#CHANNEL_TYPE_ANY}.
     * @unknown 
     */
    public int getChannelType() {
        return mChannelType;
    }

    public static final android.os.Parcelable.Creator<android.bluetooth.BluetoothHealthAppConfiguration> CREATOR = new android.os.Parcelable.Creator<android.bluetooth.BluetoothHealthAppConfiguration>() {
        @java.lang.Override
        public android.bluetooth.BluetoothHealthAppConfiguration createFromParcel(android.os.Parcel in) {
            java.lang.String name = in.readString();
            int type = in.readInt();
            int role = in.readInt();
            int channelType = in.readInt();
            return new android.bluetooth.BluetoothHealthAppConfiguration(name, type, role, channelType);
        }

        @java.lang.Override
        public android.bluetooth.BluetoothHealthAppConfiguration[] newArray(int size) {
            return new android.bluetooth.BluetoothHealthAppConfiguration[size];
        }
    };

    public void writeToParcel(android.os.Parcel out, int flags) {
        out.writeString(mName);
        out.writeInt(mDataType);
        out.writeInt(mRole);
        out.writeInt(mChannelType);
    }
}

