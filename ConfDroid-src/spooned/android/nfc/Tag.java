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
package android.nfc;


/**
 * Represents an NFC tag that has been discovered.
 * <p>
 * {@link Tag} is an immutable object that represents the state of a NFC tag at
 * the time of discovery. It can be used as a handle to {@link TagTechnology} classes
 * to perform advanced operations, or directly queried for its ID via {@link #getId} and the
 * set of technologies it contains via {@link #getTechList}. Arrays passed to and
 * returned by this class are <em>not</em> cloned, so be careful not to modify them.
 * <p>
 * A new tag object is created every time a tag is discovered (comes into range), even
 * if it is the same physical tag. If a tag is removed and then returned into range, then
 * only the most recent tag object can be successfully used to create a {@link TagTechnology}.
 *
 * <h3>Tag Dispatch</h3>
 * When a tag is discovered, a {@link Tag} object is created and passed to a
 * single activity via the {@link NfcAdapter#EXTRA_TAG} extra in an
 * {@link android.content.Intent} via {@link Context#startActivity}. A four stage dispatch is used
 * to select the
 * most appropriate activity to handle the tag. The Android OS executes each stage in order,
 * and completes dispatch as soon as a single matching activity is found. If there are multiple
 * matching activities found at any one stage then the Android activity chooser dialog is shown
 * to allow the user to select the activity to receive the tag.
 *
 * <p>The Tag dispatch mechanism was designed to give a high probability of dispatching
 * a tag to the correct activity without showing the user an activity chooser dialog.
 * This is important for NFC interactions because they are very transient -- if a user has to
 * move the Android device to choose an application then the connection will likely be broken.
 *
 * <h4>1. Foreground activity dispatch</h4>
 * A foreground activity that has called
 * {@link NfcAdapter#enableForegroundDispatch NfcAdapter.enableForegroundDispatch()} is
 * given priority. See the documentation on
 * {@link NfcAdapter#enableForegroundDispatch NfcAdapter.enableForegroundDispatch()} for
 * its usage.
 * <h4>2. NDEF data dispatch</h4>
 * If the tag contains NDEF data the system inspects the first {@link NdefRecord} in the first
 * {@link NdefMessage}. If the record is a URI, SmartPoster, or MIME data
 * {@link Context#startActivity} is called with {@link NfcAdapter#ACTION_NDEF_DISCOVERED}. For URI
 * and SmartPoster records the URI is put into the intent's data field. For MIME records the MIME
 * type is put in the intent's type field. This allows activities to register to be launched only
 * when data they know how to handle is present on a tag. This is the preferred method of handling
 * data on a tag since NDEF data can be stored on many types of tags and doesn't depend on a
 * specific tag technology.
 * See {@link NfcAdapter#ACTION_NDEF_DISCOVERED} for more detail. If the tag does not contain
 * NDEF data, or if no activity is registered
 * for {@link NfcAdapter#ACTION_NDEF_DISCOVERED} with a matching data URI or MIME type then dispatch
 * moves to stage 3.
 * <h4>3. Tag Technology dispatch</h4>
 * {@link Context#startActivity} is called with {@link NfcAdapter#ACTION_TECH_DISCOVERED} to
 * dispatch the tag to an activity that can handle the technologies present on the tag.
 * Technologies are defined as sub-classes of {@link TagTechnology}, see the package
 * {@link android.nfc.tech}. The Android OS looks for an activity that can handle one or
 * more technologies in the tag. See {@link NfcAdapter#ACTION_TECH_DISCOVERED} for more detail.
 * <h4>4. Fall-back dispatch</h4>
 * If no activity has been matched then {@link Context#startActivity} is called with
 * {@link NfcAdapter#ACTION_TAG_DISCOVERED}. This is intended as a fall-back mechanism.
 * See {@link NfcAdapter#ACTION_TAG_DISCOVERED}.
 *
 * <h3>NFC Tag Background</h3>
 * An NFC tag is a passive NFC device, powered by the NFC field of this Android device while
 * it is in range. Tag's can come in many forms, such as stickers, cards, key fobs, or
 * even embedded in a more sophisticated device.
 * <p>
 * Tags can have a wide range of capabilities. Simple tags just offer read/write semantics,
 * and contain some one time
 * programmable areas to make read-only. More complex tags offer math operations
 * and per-sector access control and authentication. The most sophisticated tags
 * contain operating environments allowing complex interactions with the
 * code executing on the tag. Use {@link TagTechnology} classes to access a broad
 * range of capabilities available in NFC tags.
 * <p>
 */
public final class Tag implements android.os.Parcelable {
    final byte[] mId;

    final int[] mTechList;

    final java.lang.String[] mTechStringList;

    final android.os.Bundle[] mTechExtras;

    final int mServiceHandle;// for use by NFC service, 0 indicates a mock


    final android.nfc.INfcTag mTagService;// interface to NFC service, will be null if mock tag


    int mConnectedTechnology;

    /**
     * Hidden constructor to be used by NFC service and internal classes.
     *
     * @unknown 
     */
    public Tag(byte[] id, int[] techList, android.os.Bundle[] techListExtras, int serviceHandle, android.nfc.INfcTag tagService) {
        if (techList == null) {
            throw new java.lang.IllegalArgumentException("rawTargets cannot be null");
        }
        mId = id;
        mTechList = java.util.Arrays.copyOf(techList, techList.length);
        mTechStringList = generateTechStringList(techList);
        // Ensure mTechExtras is as long as mTechList
        mTechExtras = java.util.Arrays.copyOf(techListExtras, techList.length);
        mServiceHandle = serviceHandle;
        mTagService = tagService;
        mConnectedTechnology = -1;
    }

    /**
     * Construct a mock Tag.
     * <p>This is an application constructed tag, so NfcAdapter methods on this Tag may fail
     * with {@link IllegalArgumentException} since it does not represent a physical Tag.
     * <p>This constructor might be useful for mock testing.
     *
     * @param id
     * 		The tag identifier, can be null
     * @param techList
     * 		must not be null
     * @return freshly constructed tag
     * @unknown 
     */
    public static android.nfc.Tag createMockTag(byte[] id, int[] techList, android.os.Bundle[] techListExtras) {
        // set serviceHandle to 0 and tagService to null to indicate mock tag
        return new android.nfc.Tag(id, techList, techListExtras, 0, null);
    }

    private java.lang.String[] generateTechStringList(int[] techList) {
        final int size = techList.length;
        java.lang.String[] strings = new java.lang.String[size];
        for (int i = 0; i < size; i++) {
            switch (techList[i]) {
                case android.nfc.tech.TagTechnology.ISO_DEP :
                    strings[i] = android.nfc.tech.IsoDep.class.getName();
                    break;
                case android.nfc.tech.TagTechnology.MIFARE_CLASSIC :
                    strings[i] = android.nfc.tech.MifareClassic.class.getName();
                    break;
                case android.nfc.tech.TagTechnology.MIFARE_ULTRALIGHT :
                    strings[i] = android.nfc.tech.MifareUltralight.class.getName();
                    break;
                case android.nfc.tech.TagTechnology.NDEF :
                    strings[i] = android.nfc.tech.Ndef.class.getName();
                    break;
                case android.nfc.tech.TagTechnology.NDEF_FORMATABLE :
                    strings[i] = android.nfc.tech.NdefFormatable.class.getName();
                    break;
                case android.nfc.tech.TagTechnology.NFC_A :
                    strings[i] = android.nfc.tech.NfcA.class.getName();
                    break;
                case android.nfc.tech.TagTechnology.NFC_B :
                    strings[i] = android.nfc.tech.NfcB.class.getName();
                    break;
                case android.nfc.tech.TagTechnology.NFC_F :
                    strings[i] = android.nfc.tech.NfcF.class.getName();
                    break;
                case android.nfc.tech.TagTechnology.NFC_V :
                    strings[i] = android.nfc.tech.NfcV.class.getName();
                    break;
                case android.nfc.tech.TagTechnology.NFC_BARCODE :
                    strings[i] = android.nfc.tech.NfcBarcode.class.getName();
                    break;
                default :
                    throw new java.lang.IllegalArgumentException("Unknown tech type " + techList[i]);
            }
        }
        return strings;
    }

    static int[] getTechCodesFromStrings(java.lang.String[] techStringList) throws java.lang.IllegalArgumentException {
        if (techStringList == null) {
            throw new java.lang.IllegalArgumentException("List cannot be null");
        }
        int[] techIntList = new int[techStringList.length];
        java.util.HashMap<java.lang.String, java.lang.Integer> stringToCodeMap = android.nfc.Tag.getTechStringToCodeMap();
        for (int i = 0; i < techStringList.length; i++) {
            java.lang.Integer code = stringToCodeMap.get(techStringList[i]);
            if (code == null) {
                throw new java.lang.IllegalArgumentException("Unknown tech type " + techStringList[i]);
            }
            techIntList[i] = code.intValue();
        }
        return techIntList;
    }

    private static java.util.HashMap<java.lang.String, java.lang.Integer> getTechStringToCodeMap() {
        java.util.HashMap<java.lang.String, java.lang.Integer> techStringToCodeMap = new java.util.HashMap<java.lang.String, java.lang.Integer>();
        techStringToCodeMap.put(android.nfc.tech.IsoDep.class.getName(), android.nfc.tech.TagTechnology.ISO_DEP);
        techStringToCodeMap.put(android.nfc.tech.MifareClassic.class.getName(), android.nfc.tech.TagTechnology.MIFARE_CLASSIC);
        techStringToCodeMap.put(android.nfc.tech.MifareUltralight.class.getName(), android.nfc.tech.TagTechnology.MIFARE_ULTRALIGHT);
        techStringToCodeMap.put(android.nfc.tech.Ndef.class.getName(), android.nfc.tech.TagTechnology.NDEF);
        techStringToCodeMap.put(android.nfc.tech.NdefFormatable.class.getName(), android.nfc.tech.TagTechnology.NDEF_FORMATABLE);
        techStringToCodeMap.put(android.nfc.tech.NfcA.class.getName(), android.nfc.tech.TagTechnology.NFC_A);
        techStringToCodeMap.put(android.nfc.tech.NfcB.class.getName(), android.nfc.tech.TagTechnology.NFC_B);
        techStringToCodeMap.put(android.nfc.tech.NfcF.class.getName(), android.nfc.tech.TagTechnology.NFC_F);
        techStringToCodeMap.put(android.nfc.tech.NfcV.class.getName(), android.nfc.tech.TagTechnology.NFC_V);
        techStringToCodeMap.put(android.nfc.tech.NfcBarcode.class.getName(), android.nfc.tech.TagTechnology.NFC_BARCODE);
        return techStringToCodeMap;
    }

    /**
     * For use by NfcService only.
     *
     * @unknown 
     */
    public int getServiceHandle() {
        return mServiceHandle;
    }

    /**
     * For use by NfcService only.
     *
     * @unknown 
     */
    public int[] getTechCodeList() {
        return mTechList;
    }

    /**
     * Get the Tag Identifier (if it has one).
     * <p>The tag identifier is a low level serial number, used for anti-collision
     * and identification.
     * <p> Most tags have a stable unique identifier
     * (UID), but some tags will generate a random ID every time they are discovered
     * (RID), and there are some tags with no ID at all (the byte array will be zero-sized).
     * <p> The size and format of an ID is specific to the RF technology used by the tag.
     * <p> This function retrieves the ID as determined at discovery time, and does not
     * perform any further RF communication or block.
     *
     * @return ID as byte array, never null
     */
    public byte[] getId() {
        return mId;
    }

    /**
     * Get the technologies available in this tag, as fully qualified class names.
     * <p>
     * A technology is an implementation of the {@link TagTechnology} interface,
     * and can be instantiated by calling the static <code>get(Tag)</code>
     * method on the implementation with this Tag. The {@link TagTechnology}
     * object can then be used to perform advanced, technology-specific operations on a tag.
     * <p>
     * Android defines a mandatory set of technologies that must be correctly
     * enumerated by all Android NFC devices, and an optional
     * set of proprietary technologies.
     * See {@link TagTechnology} for more details.
     * <p>
     * The ordering of the returned array is undefined and should not be relied upon.
     *
     * @return an array of fully-qualified {@link TagTechnology} class-names.
     */
    public java.lang.String[] getTechList() {
        return mTechStringList;
    }

    /**
     * Rediscover the technologies available on this tag.
     * <p>
     * The technologies that are available on a tag may change due to
     * operations being performed on a tag. For example, formatting a
     * tag as NDEF adds the {@link Ndef} technology. The {@link rediscover}
     * method reenumerates the available technologies on the tag
     * and returns a new {@link Tag} object containing these technologies.
     * <p>
     * You may not be connected to any of this {@link Tag}'s technologies
     * when calling this method.
     * This method guarantees that you will be returned the same Tag
     * if it is still in the field.
     * <p>May cause RF activity and may block. Must not be called
     * from the main application thread. A blocked call will be canceled with
     * {@link IOException} by calling {@link #close} from another thread.
     * <p>Does not remove power from the RF field, so a tag having a random
     * ID should not change its ID.
     *
     * @return the rediscovered tag object.
     * @throws IOException
     * 		if the tag cannot be rediscovered
     * @unknown 
     */
    // TODO See if we need TagLostException
    // TODO Unhide for ICS
    // TODO Update documentation to make sure it matches with the final
    // implementation.
    public android.nfc.Tag rediscover() throws java.io.IOException {
        if (getConnectedTechnology() != (-1)) {
            throw new java.lang.IllegalStateException("Close connection to the technology first!");
        }
        if (mTagService == null) {
            throw new java.io.IOException("Mock tags don't support this operation.");
        }
        try {
            android.nfc.Tag newTag = mTagService.rediscover(getServiceHandle());
            if (newTag != null) {
                return newTag;
            } else {
                throw new java.io.IOException("Failed to rediscover tag");
            }
        } catch (android.os.RemoteException e) {
            throw new java.io.IOException("NFC service dead");
        }
    }

    /**
     *
     *
     * @unknown 
     */
    public boolean hasTech(int techType) {
        for (int tech : mTechList) {
            if (tech == techType)
                return true;

        }
        return false;
    }

    /**
     *
     *
     * @unknown 
     */
    public android.os.Bundle getTechExtras(int tech) {
        int pos = -1;
        for (int idx = 0; idx < mTechList.length; idx++) {
            if (mTechList[idx] == tech) {
                pos = idx;
                break;
            }
        }
        if (pos < 0) {
            return null;
        }
        return mTechExtras[pos];
    }

    /**
     *
     *
     * @unknown 
     */
    public android.nfc.INfcTag getTagService() {
        return mTagService;
    }

    /**
     * Human-readable description of the tag, for debugging.
     */
    @java.lang.Override
    public java.lang.String toString() {
        java.lang.StringBuilder sb = new java.lang.StringBuilder("TAG: Tech [");
        java.lang.String[] techList = getTechList();
        int length = techList.length;
        for (int i = 0; i < length; i++) {
            sb.append(techList[i]);
            if (i < (length - 1)) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    /* package */
    static byte[] readBytesWithNull(android.os.Parcel in) {
        int len = in.readInt();
        byte[] result = null;
        if (len >= 0) {
            result = new byte[len];
            in.readByteArray(result);
        }
        return result;
    }

    /* package */
    static void writeBytesWithNull(android.os.Parcel out, byte[] b) {
        if (b == null) {
            out.writeInt(-1);
            return;
        }
        out.writeInt(b.length);
        out.writeByteArray(b);
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        // Null mTagService means this is a mock tag
        int isMock = (mTagService == null) ? 1 : 0;
        android.nfc.Tag.writeBytesWithNull(dest, mId);
        dest.writeInt(mTechList.length);
        dest.writeIntArray(mTechList);
        dest.writeTypedArray(mTechExtras, 0);
        dest.writeInt(mServiceHandle);
        dest.writeInt(isMock);
        if (isMock == 0) {
            dest.writeStrongBinder(mTagService.asBinder());
        }
    }

    public static final android.os.Parcelable.Creator<android.nfc.Tag> CREATOR = new android.os.Parcelable.Creator<android.nfc.Tag>() {
        @java.lang.Override
        public android.nfc.Tag createFromParcel(android.os.Parcel in) {
            android.nfc.INfcTag tagService;
            // Tag fields
            byte[] id = android.nfc.Tag.readBytesWithNull(in);
            int[] techList = new int[in.readInt()];
            in.readIntArray(techList);
            android.os.Bundle[] techExtras = in.createTypedArray(android.os.Bundle.CREATOR);
            int serviceHandle = in.readInt();
            int isMock = in.readInt();
            if (isMock == 0) {
                tagService = INfcTag.Stub.asInterface(in.readStrongBinder());
            } else {
                tagService = null;
            }
            return new android.nfc.Tag(id, techList, techExtras, serviceHandle, tagService);
        }

        @java.lang.Override
        public android.nfc.Tag[] newArray(int size) {
            return new android.nfc.Tag[size];
        }
    };

    /**
     * For internal use only.
     *
     * @unknown 
     */
    public synchronized void setConnectedTechnology(int technology) {
        if (mConnectedTechnology == (-1)) {
            mConnectedTechnology = technology;
        } else {
            throw new java.lang.IllegalStateException("Close other technology first!");
        }
    }

    /**
     * For internal use only.
     *
     * @unknown 
     */
    public int getConnectedTechnology() {
        return mConnectedTechnology;
    }

    /**
     * For internal use only.
     *
     * @unknown 
     */
    public void setTechnologyDisconnected() {
        mConnectedTechnology = -1;
    }
}

