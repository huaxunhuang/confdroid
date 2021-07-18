/**
 * Copyright (C) 2009 The Android Open Source Project
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
 * Static helper methods and constants to decode the ParcelUuid of remote devices.
 *
 * @unknown 
 */
public final class BluetoothUuid {
    /* See Bluetooth Assigned Numbers document - SDP section, to get the values of UUIDs
    for the various services.

    The following 128 bit values are calculated as:
     uuid * 2^96 + BASE_UUID
     */
    public static final android.os.ParcelUuid AudioSink = android.os.ParcelUuid.fromString("0000110B-0000-1000-8000-00805F9B34FB");

    public static final android.os.ParcelUuid AudioSource = android.os.ParcelUuid.fromString("0000110A-0000-1000-8000-00805F9B34FB");

    public static final android.os.ParcelUuid AdvAudioDist = android.os.ParcelUuid.fromString("0000110D-0000-1000-8000-00805F9B34FB");

    public static final android.os.ParcelUuid HSP = android.os.ParcelUuid.fromString("00001108-0000-1000-8000-00805F9B34FB");

    public static final android.os.ParcelUuid HSP_AG = android.os.ParcelUuid.fromString("00001112-0000-1000-8000-00805F9B34FB");

    public static final android.os.ParcelUuid Handsfree = android.os.ParcelUuid.fromString("0000111E-0000-1000-8000-00805F9B34FB");

    public static final android.os.ParcelUuid Handsfree_AG = android.os.ParcelUuid.fromString("0000111F-0000-1000-8000-00805F9B34FB");

    public static final android.os.ParcelUuid AvrcpController = android.os.ParcelUuid.fromString("0000110E-0000-1000-8000-00805F9B34FB");

    public static final android.os.ParcelUuid AvrcpTarget = android.os.ParcelUuid.fromString("0000110C-0000-1000-8000-00805F9B34FB");

    public static final android.os.ParcelUuid ObexObjectPush = android.os.ParcelUuid.fromString("00001105-0000-1000-8000-00805f9b34fb");

    public static final android.os.ParcelUuid Hid = android.os.ParcelUuid.fromString("00001124-0000-1000-8000-00805f9b34fb");

    public static final android.os.ParcelUuid Hogp = android.os.ParcelUuid.fromString("00001812-0000-1000-8000-00805f9b34fb");

    public static final android.os.ParcelUuid PANU = android.os.ParcelUuid.fromString("00001115-0000-1000-8000-00805F9B34FB");

    public static final android.os.ParcelUuid NAP = android.os.ParcelUuid.fromString("00001116-0000-1000-8000-00805F9B34FB");

    public static final android.os.ParcelUuid BNEP = android.os.ParcelUuid.fromString("0000000f-0000-1000-8000-00805F9B34FB");

    public static final android.os.ParcelUuid PBAP_PCE = android.os.ParcelUuid.fromString("0000112e-0000-1000-8000-00805F9B34FB");

    public static final android.os.ParcelUuid PBAP_PSE = android.os.ParcelUuid.fromString("0000112f-0000-1000-8000-00805F9B34FB");

    public static final android.os.ParcelUuid MAP = android.os.ParcelUuid.fromString("00001134-0000-1000-8000-00805F9B34FB");

    public static final android.os.ParcelUuid MNS = android.os.ParcelUuid.fromString("00001133-0000-1000-8000-00805F9B34FB");

    public static final android.os.ParcelUuid MAS = android.os.ParcelUuid.fromString("00001132-0000-1000-8000-00805F9B34FB");

    public static final android.os.ParcelUuid SAP = android.os.ParcelUuid.fromString("0000112D-0000-1000-8000-00805F9B34FB");

    public static final android.os.ParcelUuid BASE_UUID = android.os.ParcelUuid.fromString("00000000-0000-1000-8000-00805F9B34FB");

    /**
     * Length of bytes for 16 bit UUID
     */
    public static final int UUID_BYTES_16_BIT = 2;

    /**
     * Length of bytes for 32 bit UUID
     */
    public static final int UUID_BYTES_32_BIT = 4;

    /**
     * Length of bytes for 128 bit UUID
     */
    public static final int UUID_BYTES_128_BIT = 16;

    public static final android.os.ParcelUuid[] RESERVED_UUIDS = new android.os.ParcelUuid[]{ android.bluetooth.BluetoothUuid.AudioSink, android.bluetooth.BluetoothUuid.AudioSource, android.bluetooth.BluetoothUuid.AdvAudioDist, android.bluetooth.BluetoothUuid.HSP, android.bluetooth.BluetoothUuid.Handsfree, android.bluetooth.BluetoothUuid.AvrcpController, android.bluetooth.BluetoothUuid.AvrcpTarget, android.bluetooth.BluetoothUuid.ObexObjectPush, android.bluetooth.BluetoothUuid.PANU, android.bluetooth.BluetoothUuid.NAP, android.bluetooth.BluetoothUuid.MAP, android.bluetooth.BluetoothUuid.MNS, android.bluetooth.BluetoothUuid.MAS, android.bluetooth.BluetoothUuid.SAP };

    public static boolean isAudioSource(android.os.ParcelUuid uuid) {
        return uuid.equals(android.bluetooth.BluetoothUuid.AudioSource);
    }

    public static boolean isAudioSink(android.os.ParcelUuid uuid) {
        return uuid.equals(android.bluetooth.BluetoothUuid.AudioSink);
    }

    public static boolean isAdvAudioDist(android.os.ParcelUuid uuid) {
        return uuid.equals(android.bluetooth.BluetoothUuid.AdvAudioDist);
    }

    public static boolean isHandsfree(android.os.ParcelUuid uuid) {
        return uuid.equals(android.bluetooth.BluetoothUuid.Handsfree);
    }

    public static boolean isHeadset(android.os.ParcelUuid uuid) {
        return uuid.equals(android.bluetooth.BluetoothUuid.HSP);
    }

    public static boolean isAvrcpController(android.os.ParcelUuid uuid) {
        return uuid.equals(android.bluetooth.BluetoothUuid.AvrcpController);
    }

    public static boolean isAvrcpTarget(android.os.ParcelUuid uuid) {
        return uuid.equals(android.bluetooth.BluetoothUuid.AvrcpTarget);
    }

    public static boolean isInputDevice(android.os.ParcelUuid uuid) {
        return uuid.equals(android.bluetooth.BluetoothUuid.Hid);
    }

    public static boolean isPanu(android.os.ParcelUuid uuid) {
        return uuid.equals(android.bluetooth.BluetoothUuid.PANU);
    }

    public static boolean isNap(android.os.ParcelUuid uuid) {
        return uuid.equals(android.bluetooth.BluetoothUuid.NAP);
    }

    public static boolean isBnep(android.os.ParcelUuid uuid) {
        return uuid.equals(android.bluetooth.BluetoothUuid.BNEP);
    }

    public static boolean isMap(android.os.ParcelUuid uuid) {
        return uuid.equals(android.bluetooth.BluetoothUuid.MAP);
    }

    public static boolean isMns(android.os.ParcelUuid uuid) {
        return uuid.equals(android.bluetooth.BluetoothUuid.MNS);
    }

    public static boolean isMas(android.os.ParcelUuid uuid) {
        return uuid.equals(android.bluetooth.BluetoothUuid.MAS);
    }

    public static boolean isSap(android.os.ParcelUuid uuid) {
        return uuid.equals(android.bluetooth.BluetoothUuid.SAP);
    }

    /**
     * Returns true if ParcelUuid is present in uuidArray
     *
     * @param uuidArray
     * 		- Array of ParcelUuids
     * @param uuid
     * 		
     */
    public static boolean isUuidPresent(android.os.ParcelUuid[] uuidArray, android.os.ParcelUuid uuid) {
        if (((uuidArray == null) || (uuidArray.length == 0)) && (uuid == null))
            return true;

        if (uuidArray == null)
            return false;

        for (android.os.ParcelUuid element : uuidArray) {
            if (element.equals(uuid))
                return true;

        }
        return false;
    }

    /**
     * Returns true if there any common ParcelUuids in uuidA and uuidB.
     *
     * @param uuidA
     * 		- List of ParcelUuids
     * @param uuidB
     * 		- List of ParcelUuids
     */
    public static boolean containsAnyUuid(android.os.ParcelUuid[] uuidA, android.os.ParcelUuid[] uuidB) {
        if ((uuidA == null) && (uuidB == null))
            return true;

        if (uuidA == null) {
            return uuidB.length == 0 ? true : false;
        }
        if (uuidB == null) {
            return uuidA.length == 0 ? true : false;
        }
        java.util.HashSet<android.os.ParcelUuid> uuidSet = new java.util.HashSet<android.os.ParcelUuid>(java.util.Arrays.asList(uuidA));
        for (android.os.ParcelUuid uuid : uuidB) {
            if (uuidSet.contains(uuid))
                return true;

        }
        return false;
    }

    /**
     * Returns true if all the ParcelUuids in ParcelUuidB are present in
     * ParcelUuidA
     *
     * @param uuidA
     * 		- Array of ParcelUuidsA
     * @param uuidB
     * 		- Array of ParcelUuidsB
     */
    public static boolean containsAllUuids(android.os.ParcelUuid[] uuidA, android.os.ParcelUuid[] uuidB) {
        if ((uuidA == null) && (uuidB == null))
            return true;

        if (uuidA == null) {
            return uuidB.length == 0 ? true : false;
        }
        if (uuidB == null)
            return true;

        java.util.HashSet<android.os.ParcelUuid> uuidSet = new java.util.HashSet<android.os.ParcelUuid>(java.util.Arrays.asList(uuidA));
        for (android.os.ParcelUuid uuid : uuidB) {
            if (!uuidSet.contains(uuid))
                return false;

        }
        return true;
    }

    /**
     * Extract the Service Identifier or the actual uuid from the Parcel Uuid.
     * For example, if 0000110B-0000-1000-8000-00805F9B34FB is the parcel Uuid,
     * this function will return 110B
     *
     * @param parcelUuid
     * 		
     * @return the service identifier.
     */
    public static int getServiceIdentifierFromParcelUuid(android.os.ParcelUuid parcelUuid) {
        java.util.UUID uuid = parcelUuid.getUuid();
        long value = (uuid.getMostSignificantBits() & 0xffff00000000L) >>> 32;
        return ((int) (value));
    }

    /**
     * Parse UUID from bytes. The {@code uuidBytes} can represent a 16-bit, 32-bit or 128-bit UUID,
     * but the returned UUID is always in 128-bit format.
     * Note UUID is little endian in Bluetooth.
     *
     * @param uuidBytes
     * 		Byte representation of uuid.
     * @return {@link ParcelUuid} parsed from bytes.
     * @throws IllegalArgumentException
     * 		If the {@code uuidBytes} cannot be parsed.
     */
    public static android.os.ParcelUuid parseUuidFrom(byte[] uuidBytes) {
        if (uuidBytes == null) {
            throw new java.lang.IllegalArgumentException("uuidBytes cannot be null");
        }
        int length = uuidBytes.length;
        if (((length != android.bluetooth.BluetoothUuid.UUID_BYTES_16_BIT) && (length != android.bluetooth.BluetoothUuid.UUID_BYTES_32_BIT)) && (length != android.bluetooth.BluetoothUuid.UUID_BYTES_128_BIT)) {
            throw new java.lang.IllegalArgumentException("uuidBytes length invalid - " + length);
        }
        // Construct a 128 bit UUID.
        if (length == android.bluetooth.BluetoothUuid.UUID_BYTES_128_BIT) {
            java.nio.ByteBuffer buf = java.nio.ByteBuffer.wrap(uuidBytes).order(java.nio.ByteOrder.LITTLE_ENDIAN);
            long msb = buf.getLong(8);
            long lsb = buf.getLong(0);
            return new android.os.ParcelUuid(new java.util.UUID(msb, lsb));
        }
        // For 16 bit and 32 bit UUID we need to convert them to 128 bit value.
        // 128_bit_value = uuid * 2^96 + BASE_UUID
        long shortUuid;
        if (length == android.bluetooth.BluetoothUuid.UUID_BYTES_16_BIT) {
            shortUuid = uuidBytes[0] & 0xff;
            shortUuid += (uuidBytes[1] & 0xff) << 8;
        } else {
            shortUuid = uuidBytes[0] & 0xff;
            shortUuid += (uuidBytes[1] & 0xff) << 8;
            shortUuid += (uuidBytes[2] & 0xff) << 16;
            shortUuid += (uuidBytes[3] & 0xff) << 24;
        }
        long msb = android.bluetooth.BluetoothUuid.BASE_UUID.getUuid().getMostSignificantBits() + (shortUuid << 32);
        long lsb = android.bluetooth.BluetoothUuid.BASE_UUID.getUuid().getLeastSignificantBits();
        return new android.os.ParcelUuid(new java.util.UUID(msb, lsb));
    }

    /**
     * Check whether the given parcelUuid can be converted to 16 bit bluetooth uuid.
     *
     * @param parcelUuid
     * 		
     * @return true if the parcelUuid can be converted to 16 bit uuid, false otherwise.
     */
    public static boolean is16BitUuid(android.os.ParcelUuid parcelUuid) {
        java.util.UUID uuid = parcelUuid.getUuid();
        if (uuid.getLeastSignificantBits() != android.bluetooth.BluetoothUuid.BASE_UUID.getUuid().getLeastSignificantBits()) {
            return false;
        }
        return (uuid.getMostSignificantBits() & 0xffff0000ffffffffL) == 0x1000L;
    }

    /**
     * Check whether the given parcelUuid can be converted to 32 bit bluetooth uuid.
     *
     * @param parcelUuid
     * 		
     * @return true if the parcelUuid can be converted to 32 bit uuid, false otherwise.
     */
    public static boolean is32BitUuid(android.os.ParcelUuid parcelUuid) {
        java.util.UUID uuid = parcelUuid.getUuid();
        if (uuid.getLeastSignificantBits() != android.bluetooth.BluetoothUuid.BASE_UUID.getUuid().getLeastSignificantBits()) {
            return false;
        }
        if (android.bluetooth.BluetoothUuid.is16BitUuid(parcelUuid)) {
            return false;
        }
        return (uuid.getMostSignificantBits() & 0xffffffffL) == 0x1000L;
    }
}

