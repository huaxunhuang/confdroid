/**
 * Copyright (C) 2010 The Android Open Source Project
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
package android.nfc.tech;


/**
 * Provides access to MIFARE Classic properties and I/O operations on a {@link Tag}.
 *
 * <p>Acquire a {@link MifareClassic} object using {@link #get}.
 *
 * <p>MIFARE Classic is also known as MIFARE Standard.
 * <p>MIFARE Classic tags are divided into sectors, and each sector is sub-divided into
 * blocks. Block size is always 16 bytes ({@link #BLOCK_SIZE}. Sector size varies.
 * <ul>
 * <li>MIFARE Classic Mini are 320 bytes ({@link #SIZE_MINI}), with 5 sectors each of 4 blocks.
 * <li>MIFARE Classic 1k are 1024 bytes ({@link #SIZE_1K}), with 16 sectors each of 4 blocks.
 * <li>MIFARE Classic 2k are 2048 bytes ({@link #SIZE_2K}), with 32 sectors each of 4 blocks.
 * <li>MIFARE Classic 4k} are 4096 bytes ({@link #SIZE_4K}). The first 32 sectors contain 4 blocks
 * and the last 8 sectors contain 16 blocks.
 * </ul>
 *
 * <p>MIFARE Classic tags require authentication on a per-sector basis before any
 * other I/O operations on that sector can be performed. There are two keys per sector,
 * and ACL bits determine what I/O operations are allowed on that sector after
 * authenticating with a key. {@see #authenticateSectorWithKeyA} and
 * {@see #authenticateSectorWithKeyB}.
 *
 * <p>Three well-known authentication keys are defined in this class:
 * {@link #KEY_DEFAULT}, {@link #KEY_MIFARE_APPLICATION_DIRECTORY},
 * {@link #KEY_NFC_FORUM}.
 * <ul>
 * <li>{@link #KEY_DEFAULT} is the default factory key for MIFARE Classic.
 * <li>{@link #KEY_MIFARE_APPLICATION_DIRECTORY} is the well-known key for
 * MIFARE Classic cards that have been formatted according to the
 * MIFARE Application Directory (MAD) specification.
 * <li>{@link #KEY_NFC_FORUM} is the well-known key for MIFARE Classic cards that
 * have been formatted according to the NXP specification for NDEF on MIFARE Classic.
 *
 * <p>Implementation of this class on a Android NFC device is optional.
 * If it is not implemented, then
 * {@link MifareClassic} will never be enumerated in {@link Tag#getTechList}.
 * If it is enumerated, then all {@link MifareClassic} I/O operations will be supported,
 * and {@link Ndef#MIFARE_CLASSIC} NDEF tags will also be supported. In either case,
 * {@link NfcA} will also be enumerated on the tag, because all MIFARE Classic tags are also
 * {@link NfcA}.
 *
 * <p class="note"><strong>Note:</strong> Methods that perform I/O operations
 * require the {@link android.Manifest.permission#NFC} permission.
 */
public final class MifareClassic extends android.nfc.tech.BasicTagTechnology {
    private static final java.lang.String TAG = "NFC";

    /**
     * The default factory key.
     */
    public static final byte[] KEY_DEFAULT = new byte[]{ ((byte) (0xff)), ((byte) (0xff)), ((byte) (0xff)), ((byte) (0xff)), ((byte) (0xff)), ((byte) (0xff)) };

    /**
     * The well-known key for tags formatted according to the
     * MIFARE Application Directory (MAD) specification.
     */
    public static final byte[] KEY_MIFARE_APPLICATION_DIRECTORY = new byte[]{ ((byte) (0xa0)), ((byte) (0xa1)), ((byte) (0xa2)), ((byte) (0xa3)), ((byte) (0xa4)), ((byte) (0xa5)) };

    /**
     * The well-known key for tags formatted according to the
     * NDEF on MIFARE Classic specification.
     */
    public static final byte[] KEY_NFC_FORUM = new byte[]{ ((byte) (0xd3)), ((byte) (0xf7)), ((byte) (0xd3)), ((byte) (0xf7)), ((byte) (0xd3)), ((byte) (0xf7)) };

    /**
     * A MIFARE Classic compatible card of unknown type
     */
    public static final int TYPE_UNKNOWN = -1;

    /**
     * A MIFARE Classic tag
     */
    public static final int TYPE_CLASSIC = 0;

    /**
     * A MIFARE Plus tag
     */
    public static final int TYPE_PLUS = 1;

    /**
     * A MIFARE Pro tag
     */
    public static final int TYPE_PRO = 2;

    /**
     * Tag contains 16 sectors, each with 4 blocks.
     */
    public static final int SIZE_1K = 1024;

    /**
     * Tag contains 32 sectors, each with 4 blocks.
     */
    public static final int SIZE_2K = 2048;

    /**
     * Tag contains 40 sectors. The first 32 sectors contain 4 blocks and the last 8 sectors
     * contain 16 blocks.
     */
    public static final int SIZE_4K = 4096;

    /**
     * Tag contains 5 sectors, each with 4 blocks.
     */
    public static final int SIZE_MINI = 320;

    /**
     * Size of a MIFARE Classic block (in bytes)
     */
    public static final int BLOCK_SIZE = 16;

    private static final int MAX_BLOCK_COUNT = 256;

    private static final int MAX_SECTOR_COUNT = 40;

    private boolean mIsEmulated;

    private int mType;

    private int mSize;

    /**
     * Get an instance of {@link MifareClassic} for the given tag.
     * <p>Does not cause any RF activity and does not block.
     * <p>Returns null if {@link MifareClassic} was not enumerated in {@link Tag#getTechList}.
     * This indicates the tag is not MIFARE Classic compatible, or this Android
     * device does not support MIFARE Classic.
     *
     * @param tag
     * 		an MIFARE Classic compatible tag
     * @return MIFARE Classic object
     */
    public static android.nfc.tech.MifareClassic get(android.nfc.Tag tag) {
        if (!tag.hasTech(android.nfc.tech.TagTechnology.MIFARE_CLASSIC))
            return null;

        try {
            return new android.nfc.tech.MifareClassic(tag);
        } catch (android.os.RemoteException e) {
            return null;
        }
    }

    /**
     *
     *
     * @unknown 
     */
    public MifareClassic(android.nfc.Tag tag) throws android.os.RemoteException {
        super(tag, android.nfc.tech.TagTechnology.MIFARE_CLASSIC);
        android.nfc.tech.NfcA a = android.nfc.tech.NfcA.get(tag);// MIFARE Classic is always based on NFC a

        mIsEmulated = false;
        switch (a.getSak()) {
            case 0x1 :
            case 0x8 :
                mType = android.nfc.tech.MifareClassic.TYPE_CLASSIC;
                mSize = android.nfc.tech.MifareClassic.SIZE_1K;
                break;
            case 0x9 :
                mType = android.nfc.tech.MifareClassic.TYPE_CLASSIC;
                mSize = android.nfc.tech.MifareClassic.SIZE_MINI;
                break;
            case 0x10 :
                mType = android.nfc.tech.MifareClassic.TYPE_PLUS;
                mSize = android.nfc.tech.MifareClassic.SIZE_2K;
                // SecLevel = SL2
                break;
            case 0x11 :
                mType = android.nfc.tech.MifareClassic.TYPE_PLUS;
                mSize = android.nfc.tech.MifareClassic.SIZE_4K;
                // Seclevel = SL2
                break;
            case 0x18 :
                mType = android.nfc.tech.MifareClassic.TYPE_CLASSIC;
                mSize = android.nfc.tech.MifareClassic.SIZE_4K;
                break;
            case 0x28 :
                mType = android.nfc.tech.MifareClassic.TYPE_CLASSIC;
                mSize = android.nfc.tech.MifareClassic.SIZE_1K;
                mIsEmulated = true;
                break;
            case 0x38 :
                mType = android.nfc.tech.MifareClassic.TYPE_CLASSIC;
                mSize = android.nfc.tech.MifareClassic.SIZE_4K;
                mIsEmulated = true;
                break;
            case 0x88 :
                mType = android.nfc.tech.MifareClassic.TYPE_CLASSIC;
                mSize = android.nfc.tech.MifareClassic.SIZE_1K;
                // NXP-tag: false
                break;
            case 0x98 :
            case 0xb8 :
                mType = android.nfc.tech.MifareClassic.TYPE_PRO;
                mSize = android.nfc.tech.MifareClassic.SIZE_4K;
                break;
            default :
                // Stack incorrectly reported a MifareClassic. We cannot handle this
                // gracefully - we have no idea of the memory layout. Bail.
                throw new java.lang.RuntimeException("Tag incorrectly enumerated as MIFARE Classic, SAK = " + a.getSak());
        }
    }

    /**
     * Return the type of this MIFARE Classic compatible tag.
     * <p>One of {@link #TYPE_UNKNOWN}, {@link #TYPE_CLASSIC}, {@link #TYPE_PLUS} or
     * {@link #TYPE_PRO}.
     * <p>Does not cause any RF activity and does not block.
     *
     * @return type
     */
    public int getType() {
        return mType;
    }

    /**
     * Return the size of the tag in bytes
     * <p>One of {@link #SIZE_MINI}, {@link #SIZE_1K}, {@link #SIZE_2K}, {@link #SIZE_4K}.
     * These constants are equal to their respective size in bytes.
     * <p>Does not cause any RF activity and does not block.
     *
     * @return size in bytes
     */
    public int getSize() {
        return mSize;
    }

    /**
     * Return true if the tag is emulated, determined at discovery time.
     * These are actually smart-cards that emulate a MIFARE Classic interface.
     * They can be treated identically to a MIFARE Classic tag.
     *
     * @unknown 
     */
    public boolean isEmulated() {
        return mIsEmulated;
    }

    /**
     * Return the number of MIFARE Classic sectors.
     * <p>Does not cause any RF activity and does not block.
     *
     * @return number of sectors
     */
    public int getSectorCount() {
        switch (mSize) {
            case android.nfc.tech.MifareClassic.SIZE_1K :
                return 16;
            case android.nfc.tech.MifareClassic.SIZE_2K :
                return 32;
            case android.nfc.tech.MifareClassic.SIZE_4K :
                return 40;
            case android.nfc.tech.MifareClassic.SIZE_MINI :
                return 5;
            default :
                return 0;
        }
    }

    /**
     * Return the total number of MIFARE Classic blocks.
     * <p>Does not cause any RF activity and does not block.
     *
     * @return total number of blocks
     */
    public int getBlockCount() {
        return mSize / android.nfc.tech.MifareClassic.BLOCK_SIZE;
    }

    /**
     * Return the number of blocks in the given sector.
     * <p>Does not cause any RF activity and does not block.
     *
     * @param sectorIndex
     * 		index of sector, starting from 0
     * @return number of blocks in the sector
     */
    public int getBlockCountInSector(int sectorIndex) {
        android.nfc.tech.MifareClassic.validateSector(sectorIndex);
        if (sectorIndex < 32) {
            return 4;
        } else {
            return 16;
        }
    }

    /**
     * Return the sector that contains a given block.
     * <p>Does not cause any RF activity and does not block.
     *
     * @param blockIndex
     * 		index of block to lookup, starting from 0
     * @return sector index that contains the block
     */
    public int blockToSector(int blockIndex) {
        android.nfc.tech.MifareClassic.validateBlock(blockIndex);
        if (blockIndex < (32 * 4)) {
            return blockIndex / 4;
        } else {
            return 32 + ((blockIndex - (32 * 4)) / 16);
        }
    }

    /**
     * Return the first block of a given sector.
     * <p>Does not cause any RF activity and does not block.
     *
     * @param sectorIndex
     * 		index of sector to lookup, starting from 0
     * @return block index of first block in sector
     */
    public int sectorToBlock(int sectorIndex) {
        if (sectorIndex < 32) {
            return sectorIndex * 4;
        } else {
            return (32 * 4) + ((sectorIndex - 32) * 16);
        }
    }

    /**
     * Authenticate a sector with key A.
     *
     * <p>Successful authentication of a sector with key A enables other
     * I/O operations on that sector. The set of operations granted by key A
     * key depends on the ACL bits set in that sector. For more information
     * see the MIFARE Classic specification on {@see http://www.nxp.com}.
     *
     * <p>A failed authentication attempt causes an implicit reconnection to the
     * tag, so authentication to other sectors will be lost.
     *
     * <p>This is an I/O operation and will block until complete. It must
     * not be called from the main application thread. A blocked call will be canceled with
     * {@link IOException} if {@link #close} is called from another thread.
     *
     * <p class="note">Requires the {@link android.Manifest.permission#NFC} permission.
     *
     * @param sectorIndex
     * 		index of sector to authenticate, starting from 0
     * @param key
     * 		6-byte authentication key
     * @return true on success, false on authentication failure
     * @throws TagLostException
     * 		if the tag leaves the field
     * @throws IOException
     * 		if there is an I/O failure, or the operation is canceled
     */
    public boolean authenticateSectorWithKeyA(int sectorIndex, byte[] key) throws java.io.IOException {
        return authenticate(sectorIndex, key, true);
    }

    /**
     * Authenticate a sector with key B.
     *
     * <p>Successful authentication of a sector with key B enables other
     * I/O operations on that sector. The set of operations granted by key B
     * depends on the ACL bits set in that sector. For more information
     * see the MIFARE Classic specification on {@see http://www.nxp.com}.
     *
     * <p>A failed authentication attempt causes an implicit reconnection to the
     * tag, so authentication to other sectors will be lost.
     *
     * <p>This is an I/O operation and will block until complete. It must
     * not be called from the main application thread. A blocked call will be canceled with
     * {@link IOException} if {@link #close} is called from another thread.
     *
     * <p class="note">Requires the {@link android.Manifest.permission#NFC} permission.
     *
     * @param sectorIndex
     * 		index of sector to authenticate, starting from 0
     * @param key
     * 		6-byte authentication key
     * @return true on success, false on authentication failure
     * @throws TagLostException
     * 		if the tag leaves the field
     * @throws IOException
     * 		if there is an I/O failure, or the operation is canceled
     */
    public boolean authenticateSectorWithKeyB(int sectorIndex, byte[] key) throws java.io.IOException {
        return authenticate(sectorIndex, key, false);
    }

    private boolean authenticate(int sector, byte[] key, boolean keyA) throws java.io.IOException {
        android.nfc.tech.MifareClassic.validateSector(sector);
        checkConnected();
        byte[] cmd = new byte[12];
        // First byte is the command
        if (keyA) {
            cmd[0] = 0x60;// phHal_eMifareAuthentA

        } else {
            cmd[0] = 0x61;// phHal_eMifareAuthentB

        }
        // Second byte is block address
        // Authenticate command takes a block address. Authenticating a block
        // of a sector will authenticate the entire sector.
        cmd[1] = ((byte) (sectorToBlock(sector)));
        // Next 4 bytes are last 4 bytes of UID
        byte[] uid = getTag().getId();
        java.lang.System.arraycopy(uid, uid.length - 4, cmd, 2, 4);
        // Next 6 bytes are key
        java.lang.System.arraycopy(key, 0, cmd, 6, 6);
        try {
            if (transceive(cmd, false) != null) {
                return true;
            }
        } catch (android.nfc.TagLostException e) {
            throw e;
        } catch (java.io.IOException e) {
            // No need to deal with, will return false anyway
        }
        return false;
    }

    /**
     * Read 16-byte block.
     *
     * <p>This is an I/O operation and will block until complete. It must
     * not be called from the main application thread. A blocked call will be canceled with
     * {@link IOException} if {@link #close} is called from another thread.
     *
     * <p class="note">Requires the {@link android.Manifest.permission#NFC} permission.
     *
     * @param blockIndex
     * 		index of block to read, starting from 0
     * @return 16 byte block
     * @throws TagLostException
     * 		if the tag leaves the field
     * @throws IOException
     * 		if there is an I/O failure, or the operation is canceled
     */
    public byte[] readBlock(int blockIndex) throws java.io.IOException {
        android.nfc.tech.MifareClassic.validateBlock(blockIndex);
        checkConnected();
        byte[] cmd = new byte[]{ 0x30, ((byte) (blockIndex)) };
        return transceive(cmd, false);
    }

    /**
     * Write 16-byte block.
     *
     * <p>This is an I/O operation and will block until complete. It must
     * not be called from the main application thread. A blocked call will be canceled with
     * {@link IOException} if {@link #close} is called from another thread.
     *
     * <p class="note">Requires the {@link android.Manifest.permission#NFC} permission.
     *
     * @param blockIndex
     * 		index of block to write, starting from 0
     * @param data
     * 		16 bytes of data to write
     * @throws TagLostException
     * 		if the tag leaves the field
     * @throws IOException
     * 		if there is an I/O failure, or the operation is canceled
     */
    public void writeBlock(int blockIndex, byte[] data) throws java.io.IOException {
        android.nfc.tech.MifareClassic.validateBlock(blockIndex);
        checkConnected();
        if (data.length != 16) {
            throw new java.lang.IllegalArgumentException("must write 16-bytes");
        }
        byte[] cmd = new byte[data.length + 2];
        cmd[0] = ((byte) (0xa0));// MF write command

        cmd[1] = ((byte) (blockIndex));
        java.lang.System.arraycopy(data, 0, cmd, 2, data.length);
        transceive(cmd, false);
    }

    /**
     * Increment a value block, storing the result in the temporary block on the tag.
     *
     * <p>This is an I/O operation and will block until complete. It must
     * not be called from the main application thread. A blocked call will be canceled with
     * {@link IOException} if {@link #close} is called from another thread.
     *
     * <p class="note">Requires the {@link android.Manifest.permission#NFC} permission.
     *
     * @param blockIndex
     * 		index of block to increment, starting from 0
     * @param value
     * 		non-negative to increment by
     * @throws TagLostException
     * 		if the tag leaves the field
     * @throws IOException
     * 		if there is an I/O failure, or the operation is canceled
     */
    public void increment(int blockIndex, int value) throws java.io.IOException {
        android.nfc.tech.MifareClassic.validateBlock(blockIndex);
        android.nfc.tech.MifareClassic.validateValueOperand(value);
        checkConnected();
        java.nio.ByteBuffer cmd = java.nio.ByteBuffer.allocate(6);
        cmd.order(java.nio.ByteOrder.LITTLE_ENDIAN);
        cmd.put(((byte) (0xc1)));
        cmd.put(((byte) (blockIndex)));
        cmd.putInt(value);
        transceive(cmd.array(), false);
    }

    /**
     * Decrement a value block, storing the result in the temporary block on the tag.
     *
     * <p>This is an I/O operation and will block until complete. It must
     * not be called from the main application thread. A blocked call will be canceled with
     * {@link IOException} if {@link #close} is called from another thread.
     *
     * <p class="note">Requires the {@link android.Manifest.permission#NFC} permission.
     *
     * @param blockIndex
     * 		index of block to decrement, starting from 0
     * @param value
     * 		non-negative to decrement by
     * @throws TagLostException
     * 		if the tag leaves the field
     * @throws IOException
     * 		if there is an I/O failure, or the operation is canceled
     */
    public void decrement(int blockIndex, int value) throws java.io.IOException {
        android.nfc.tech.MifareClassic.validateBlock(blockIndex);
        android.nfc.tech.MifareClassic.validateValueOperand(value);
        checkConnected();
        java.nio.ByteBuffer cmd = java.nio.ByteBuffer.allocate(6);
        cmd.order(java.nio.ByteOrder.LITTLE_ENDIAN);
        cmd.put(((byte) (0xc0)));
        cmd.put(((byte) (blockIndex)));
        cmd.putInt(value);
        transceive(cmd.array(), false);
    }

    /**
     * Copy from the temporary block to a value block.
     *
     * <p>This is an I/O operation and will block until complete. It must
     * not be called from the main application thread. A blocked call will be canceled with
     * {@link IOException} if {@link #close} is called from another thread.
     *
     * <p class="note">Requires the {@link android.Manifest.permission#NFC} permission.
     *
     * @param blockIndex
     * 		index of block to copy to
     * @throws TagLostException
     * 		if the tag leaves the field
     * @throws IOException
     * 		if there is an I/O failure, or the operation is canceled
     */
    public void transfer(int blockIndex) throws java.io.IOException {
        android.nfc.tech.MifareClassic.validateBlock(blockIndex);
        checkConnected();
        byte[] cmd = new byte[]{ ((byte) (0xb0)), ((byte) (blockIndex)) };
        transceive(cmd, false);
    }

    /**
     * Copy from a value block to the temporary block.
     *
     * <p>This is an I/O operation and will block until complete. It must
     * not be called from the main application thread. A blocked call will be canceled with
     * {@link IOException} if {@link #close} is called from another thread.
     *
     * <p class="note">Requires the {@link android.Manifest.permission#NFC} permission.
     *
     * @param blockIndex
     * 		index of block to copy from
     * @throws TagLostException
     * 		if the tag leaves the field
     * @throws IOException
     * 		if there is an I/O failure, or the operation is canceled
     */
    public void restore(int blockIndex) throws java.io.IOException {
        android.nfc.tech.MifareClassic.validateBlock(blockIndex);
        checkConnected();
        byte[] cmd = new byte[]{ ((byte) (0xc2)), ((byte) (blockIndex)) };
        transceive(cmd, false);
    }

    /**
     * Send raw NfcA data to a tag and receive the response.
     *
     * <p>This is equivalent to connecting to this tag via {@link NfcA}
     * and calling {@link NfcA#transceive}. Note that all MIFARE Classic
     * tags are based on {@link NfcA} technology.
     *
     * <p>Use {@link #getMaxTransceiveLength} to retrieve the maximum number of bytes
     * that can be sent with {@link #transceive}.
     *
     * <p>This is an I/O operation and will block until complete. It must
     * not be called from the main application thread. A blocked call will be canceled with
     * {@link IOException} if {@link #close} is called from another thread.
     *
     * <p class="note">Requires the {@link android.Manifest.permission#NFC} permission.
     *
     * @see NfcA#transceive
     */
    public byte[] transceive(byte[] data) throws java.io.IOException {
        return transceive(data, true);
    }

    /**
     * Return the maximum number of bytes that can be sent with {@link #transceive}.
     *
     * @return the maximum number of bytes that can be sent with {@link #transceive}.
     */
    public int getMaxTransceiveLength() {
        return getMaxTransceiveLengthInternal();
    }

    /**
     * Set the {@link #transceive} timeout in milliseconds.
     *
     * <p>The timeout only applies to {@link #transceive} on this object,
     * and is reset to a default value when {@link #close} is called.
     *
     * <p>Setting a longer timeout may be useful when performing
     * transactions that require a long processing time on the tag
     * such as key generation.
     *
     * <p class="note">Requires the {@link android.Manifest.permission#NFC} permission.
     *
     * @param timeout
     * 		timeout value in milliseconds
     */
    public void setTimeout(int timeout) {
        try {
            int err = mTag.getTagService().setTimeout(android.nfc.tech.TagTechnology.MIFARE_CLASSIC, timeout);
            if (err != android.nfc.ErrorCodes.SUCCESS) {
                throw new java.lang.IllegalArgumentException("The supplied timeout is not valid");
            }
        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.nfc.tech.MifareClassic.TAG, "NFC service dead", e);
        }
    }

    /**
     * Get the current {@link #transceive} timeout in milliseconds.
     *
     * <p class="note">Requires the {@link android.Manifest.permission#NFC} permission.
     *
     * @return timeout value in milliseconds
     */
    public int getTimeout() {
        try {
            return mTag.getTagService().getTimeout(android.nfc.tech.TagTechnology.MIFARE_CLASSIC);
        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.nfc.tech.MifareClassic.TAG, "NFC service dead", e);
            return 0;
        }
    }

    private static void validateSector(int sector) {
        // Do not be too strict on upper bounds checking, since some cards
        // have more addressable memory than they report. For example,
        // MIFARE Plus 2k cards will appear as MIFARE Classic 1k cards when in
        // MIFARE Classic compatibility mode.
        // Note that issuing a command to an out-of-bounds block is safe - the
        // tag should report error causing IOException. This validation is a
        // helper to guard against obvious programming mistakes.
        if ((sector < 0) || (sector >= android.nfc.tech.MifareClassic.MAX_SECTOR_COUNT)) {
            throw new java.lang.IndexOutOfBoundsException("sector out of bounds: " + sector);
        }
    }

    private static void validateBlock(int block) {
        // Just looking for obvious out of bounds...
        if ((block < 0) || (block >= android.nfc.tech.MifareClassic.MAX_BLOCK_COUNT)) {
            throw new java.lang.IndexOutOfBoundsException("block out of bounds: " + block);
        }
    }

    private static void validateValueOperand(int value) {
        if (value < 0) {
            throw new java.lang.IllegalArgumentException("value operand negative");
        }
    }
}

