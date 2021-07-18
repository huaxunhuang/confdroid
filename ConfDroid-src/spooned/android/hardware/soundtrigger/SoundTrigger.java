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
package android.hardware.soundtrigger;


/**
 * The SoundTrigger class provides access via JNI to the native service managing
 * the sound trigger HAL.
 *
 * @unknown 
 */
public class SoundTrigger {
    public static final int STATUS_OK = 0;

    public static final int STATUS_ERROR = java.lang.Integer.MIN_VALUE;

    public static final int STATUS_PERMISSION_DENIED = -android.system.OsConstants.EPERM;

    public static final int STATUS_NO_INIT = -android.system.OsConstants.ENODEV;

    public static final int STATUS_BAD_VALUE = -android.system.OsConstants.EINVAL;

    public static final int STATUS_DEAD_OBJECT = -android.system.OsConstants.EPIPE;

    public static final int STATUS_INVALID_OPERATION = -android.system.OsConstants.ENOSYS;

    /**
     * ***************************************************************************
     * A ModuleProperties describes a given sound trigger hardware module
     * managed by the native sound trigger service. Each module has a unique
     * ID used to target any API call to this paricular module. Module
     * properties are returned by listModules() method.
     * **************************************************************************
     */
    public static class ModuleProperties implements android.os.Parcelable {
        /**
         * Unique module ID provided by the native service
         */
        public final int id;

        /**
         * human readable voice detection engine implementor
         */
        public final java.lang.String implementor;

        /**
         * human readable voice detection engine description
         */
        public final java.lang.String description;

        /**
         * Unique voice engine Id (changes with each version)
         */
        public final java.util.UUID uuid;

        /**
         * Voice detection engine version
         */
        public final int version;

        /**
         * Maximum number of active sound models
         */
        public final int maxSoundModels;

        /**
         * Maximum number of key phrases
         */
        public final int maxKeyphrases;

        /**
         * Maximum number of users per key phrase
         */
        public final int maxUsers;

        /**
         * Supported recognition modes (bit field, RECOGNITION_MODE_VOICE_TRIGGER ...)
         */
        public final int recognitionModes;

        /**
         * Supports seamless transition to capture mode after recognition
         */
        public final boolean supportsCaptureTransition;

        /**
         * Maximum buffering capacity in ms if supportsCaptureTransition() is true
         */
        public final int maxBufferMs;

        /**
         * Supports capture by other use cases while detection is active
         */
        public final boolean supportsConcurrentCapture;

        /**
         * Rated power consumption when detection is active with TDB silence/sound/speech ratio
         */
        public final int powerConsumptionMw;

        /**
         * Returns the trigger (key phrase) capture in the binary data of the
         * recognition callback event
         */
        public final boolean returnsTriggerInEvent;

        ModuleProperties(int id, java.lang.String implementor, java.lang.String description, java.lang.String uuid, int version, int maxSoundModels, int maxKeyphrases, int maxUsers, int recognitionModes, boolean supportsCaptureTransition, int maxBufferMs, boolean supportsConcurrentCapture, int powerConsumptionMw, boolean returnsTriggerInEvent) {
            this.id = id;
            this.implementor = implementor;
            this.description = description;
            this.uuid = java.util.UUID.fromString(uuid);
            this.version = version;
            this.maxSoundModels = maxSoundModels;
            this.maxKeyphrases = maxKeyphrases;
            this.maxUsers = maxUsers;
            this.recognitionModes = recognitionModes;
            this.supportsCaptureTransition = supportsCaptureTransition;
            this.maxBufferMs = maxBufferMs;
            this.supportsConcurrentCapture = supportsConcurrentCapture;
            this.powerConsumptionMw = powerConsumptionMw;
            this.returnsTriggerInEvent = returnsTriggerInEvent;
        }

        public static final android.os.Parcelable.Creator<android.hardware.soundtrigger.SoundTrigger.ModuleProperties> CREATOR = new android.os.Parcelable.Creator<android.hardware.soundtrigger.SoundTrigger.ModuleProperties>() {
            public android.hardware.soundtrigger.SoundTrigger.ModuleProperties createFromParcel(android.os.Parcel in) {
                return android.hardware.soundtrigger.SoundTrigger.ModuleProperties.fromParcel(in);
            }

            public android.hardware.soundtrigger.SoundTrigger.ModuleProperties[] newArray(int size) {
                return new android.hardware.soundtrigger.SoundTrigger.ModuleProperties[size];
            }
        };

        private static android.hardware.soundtrigger.SoundTrigger.ModuleProperties fromParcel(android.os.Parcel in) {
            int id = in.readInt();
            java.lang.String implementor = in.readString();
            java.lang.String description = in.readString();
            java.lang.String uuid = in.readString();
            int version = in.readInt();
            int maxSoundModels = in.readInt();
            int maxKeyphrases = in.readInt();
            int maxUsers = in.readInt();
            int recognitionModes = in.readInt();
            boolean supportsCaptureTransition = in.readByte() == 1;
            int maxBufferMs = in.readInt();
            boolean supportsConcurrentCapture = in.readByte() == 1;
            int powerConsumptionMw = in.readInt();
            boolean returnsTriggerInEvent = in.readByte() == 1;
            return new android.hardware.soundtrigger.SoundTrigger.ModuleProperties(id, implementor, description, uuid, version, maxSoundModels, maxKeyphrases, maxUsers, recognitionModes, supportsCaptureTransition, maxBufferMs, supportsConcurrentCapture, powerConsumptionMw, returnsTriggerInEvent);
        }

        @java.lang.Override
        public void writeToParcel(android.os.Parcel dest, int flags) {
            dest.writeInt(id);
            dest.writeString(implementor);
            dest.writeString(description);
            dest.writeString(uuid.toString());
            dest.writeInt(version);
            dest.writeInt(maxSoundModels);
            dest.writeInt(maxKeyphrases);
            dest.writeInt(maxUsers);
            dest.writeInt(recognitionModes);
            dest.writeByte(((byte) (supportsCaptureTransition ? 1 : 0)));
            dest.writeInt(maxBufferMs);
            dest.writeByte(((byte) (supportsConcurrentCapture ? 1 : 0)));
            dest.writeInt(powerConsumptionMw);
            dest.writeByte(((byte) (returnsTriggerInEvent ? 1 : 0)));
        }

        @java.lang.Override
        public int describeContents() {
            return 0;
        }

        @java.lang.Override
        public java.lang.String toString() {
            return ((((((((((((((((((((((((((("ModuleProperties [id=" + id) + ", implementor=") + implementor) + ", description=") + description) + ", uuid=") + uuid) + ", version=") + version) + ", maxSoundModels=") + maxSoundModels) + ", maxKeyphrases=") + maxKeyphrases) + ", maxUsers=") + maxUsers) + ", recognitionModes=") + recognitionModes) + ", supportsCaptureTransition=") + supportsCaptureTransition) + ", maxBufferMs=") + maxBufferMs) + ", supportsConcurrentCapture=") + supportsConcurrentCapture) + ", powerConsumptionMw=") + powerConsumptionMw) + ", returnsTriggerInEvent=") + returnsTriggerInEvent) + "]";
        }
    }

    /**
     * ***************************************************************************
     * A SoundModel describes the attributes and contains the binary data used by the hardware
     * implementation to detect a particular sound pattern.
     * A specialized version {@link KeyphraseSoundModel} is defined for key phrase
     * sound models.
     * **************************************************************************
     */
    public static class SoundModel {
        /**
         * Undefined sound model type
         */
        public static final int TYPE_UNKNOWN = -1;

        /**
         * Keyphrase sound model
         */
        public static final int TYPE_KEYPHRASE = 0;

        /**
         * A generic sound model. Use this type only for non-keyphrase sound models such as
         * ones that match a particular sound pattern.
         */
        public static final int TYPE_GENERIC_SOUND = 1;

        /**
         * Unique sound model identifier
         */
        public final java.util.UUID uuid;

        /**
         * Sound model type (e.g. TYPE_KEYPHRASE);
         */
        public final int type;

        /**
         * Unique sound model vendor identifier
         */
        public final java.util.UUID vendorUuid;

        /**
         * Opaque data. For use by vendor implementation and enrollment application
         */
        public final byte[] data;

        public SoundModel(java.util.UUID uuid, java.util.UUID vendorUuid, int type, byte[] data) {
            this.uuid = uuid;
            this.vendorUuid = vendorUuid;
            this.type = type;
            this.data = data;
        }

        @java.lang.Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = (prime * result) + java.util.Arrays.hashCode(data);
            result = (prime * result) + type;
            result = (prime * result) + (uuid == null ? 0 : uuid.hashCode());
            result = (prime * result) + (vendorUuid == null ? 0 : vendorUuid.hashCode());
            return result;
        }

        @java.lang.Override
        public boolean equals(java.lang.Object obj) {
            if (this == obj)
                return true;

            if (obj == null)
                return false;

            if (!(obj instanceof android.hardware.soundtrigger.SoundTrigger.SoundModel))
                return false;

            android.hardware.soundtrigger.SoundTrigger.SoundModel other = ((android.hardware.soundtrigger.SoundTrigger.SoundModel) (obj));
            if (!java.util.Arrays.equals(data, other.data))
                return false;

            if (type != other.type)
                return false;

            if (uuid == null) {
                if (other.uuid != null)
                    return false;

            } else
                if (!uuid.equals(other.uuid))
                    return false;


            if (vendorUuid == null) {
                if (other.vendorUuid != null)
                    return false;

            } else
                if (!vendorUuid.equals(other.vendorUuid))
                    return false;


            return true;
        }
    }

    /**
     * ***************************************************************************
     * A Keyphrase describes a key phrase that can be detected by a
     * {@link KeyphraseSoundModel}
     * **************************************************************************
     */
    public static class Keyphrase implements android.os.Parcelable {
        /**
         * Unique identifier for this keyphrase
         */
        public final int id;

        /**
         * Recognition modes supported for this key phrase in the model
         */
        public final int recognitionModes;

        /**
         * Locale of the keyphrase. JAVA Locale string e.g en_US
         */
        public final java.lang.String locale;

        /**
         * Key phrase text
         */
        public final java.lang.String text;

        /**
         * Users this key phrase has been trained for. countains sound trigger specific user IDs
         * derived from system user IDs {@link android.os.UserHandle#getIdentifier()}.
         */
        public final int[] users;

        public Keyphrase(int id, int recognitionModes, java.lang.String locale, java.lang.String text, int[] users) {
            this.id = id;
            this.recognitionModes = recognitionModes;
            this.locale = locale;
            this.text = text;
            this.users = users;
        }

        public static final android.os.Parcelable.Creator<android.hardware.soundtrigger.SoundTrigger.Keyphrase> CREATOR = new android.os.Parcelable.Creator<android.hardware.soundtrigger.SoundTrigger.Keyphrase>() {
            public android.hardware.soundtrigger.SoundTrigger.Keyphrase createFromParcel(android.os.Parcel in) {
                return android.hardware.soundtrigger.SoundTrigger.Keyphrase.fromParcel(in);
            }

            public android.hardware.soundtrigger.SoundTrigger.Keyphrase[] newArray(int size) {
                return new android.hardware.soundtrigger.SoundTrigger.Keyphrase[size];
            }
        };

        private static android.hardware.soundtrigger.SoundTrigger.Keyphrase fromParcel(android.os.Parcel in) {
            int id = in.readInt();
            int recognitionModes = in.readInt();
            java.lang.String locale = in.readString();
            java.lang.String text = in.readString();
            int[] users = null;
            int numUsers = in.readInt();
            if (numUsers >= 0) {
                users = new int[numUsers];
                in.readIntArray(users);
            }
            return new android.hardware.soundtrigger.SoundTrigger.Keyphrase(id, recognitionModes, locale, text, users);
        }

        @java.lang.Override
        public void writeToParcel(android.os.Parcel dest, int flags) {
            dest.writeInt(id);
            dest.writeInt(recognitionModes);
            dest.writeString(locale);
            dest.writeString(text);
            if (users != null) {
                dest.writeInt(users.length);
                dest.writeIntArray(users);
            } else {
                dest.writeInt(-1);
            }
        }

        @java.lang.Override
        public int describeContents() {
            return 0;
        }

        @java.lang.Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = (prime * result) + (text == null ? 0 : text.hashCode());
            result = (prime * result) + id;
            result = (prime * result) + (locale == null ? 0 : locale.hashCode());
            result = (prime * result) + recognitionModes;
            result = (prime * result) + java.util.Arrays.hashCode(users);
            return result;
        }

        @java.lang.Override
        public boolean equals(java.lang.Object obj) {
            if (this == obj)
                return true;

            if (obj == null)
                return false;

            if (getClass() != obj.getClass())
                return false;

            android.hardware.soundtrigger.SoundTrigger.Keyphrase other = ((android.hardware.soundtrigger.SoundTrigger.Keyphrase) (obj));
            if (text == null) {
                if (other.text != null)
                    return false;

            } else
                if (!text.equals(other.text))
                    return false;


            if (id != other.id)
                return false;

            if (locale == null) {
                if (other.locale != null)
                    return false;

            } else
                if (!locale.equals(other.locale))
                    return false;


            if (recognitionModes != other.recognitionModes)
                return false;

            if (!java.util.Arrays.equals(users, other.users))
                return false;

            return true;
        }

        @java.lang.Override
        public java.lang.String toString() {
            return ((((((((("Keyphrase [id=" + id) + ", recognitionModes=") + recognitionModes) + ", locale=") + locale) + ", text=") + text) + ", users=") + java.util.Arrays.toString(users)) + "]";
        }
    }

    /**
     * ***************************************************************************
     * A KeyphraseSoundModel is a specialized {@link SoundModel} for key phrases.
     * It contains data needed by the hardware to detect a certain number of key phrases
     * and the list of corresponding {@link Keyphrase} descriptors.
     * **************************************************************************
     */
    public static class KeyphraseSoundModel extends android.hardware.soundtrigger.SoundTrigger.SoundModel implements android.os.Parcelable {
        /**
         * Key phrases in this sound model
         */
        public final android.hardware.soundtrigger.SoundTrigger.Keyphrase[] keyphrases;// keyword phrases in model


        public KeyphraseSoundModel(java.util.UUID uuid, java.util.UUID vendorUuid, byte[] data, android.hardware.soundtrigger.SoundTrigger.Keyphrase[] keyphrases) {
            super(uuid, vendorUuid, android.hardware.soundtrigger.SoundTrigger.SoundModel.TYPE_KEYPHRASE, data);
            this.keyphrases = keyphrases;
        }

        public static final android.os.Parcelable.Creator<android.hardware.soundtrigger.SoundTrigger.KeyphraseSoundModel> CREATOR = new android.os.Parcelable.Creator<android.hardware.soundtrigger.SoundTrigger.KeyphraseSoundModel>() {
            public android.hardware.soundtrigger.SoundTrigger.KeyphraseSoundModel createFromParcel(android.os.Parcel in) {
                return android.hardware.soundtrigger.SoundTrigger.KeyphraseSoundModel.fromParcel(in);
            }

            public android.hardware.soundtrigger.SoundTrigger.KeyphraseSoundModel[] newArray(int size) {
                return new android.hardware.soundtrigger.SoundTrigger.KeyphraseSoundModel[size];
            }
        };

        private static android.hardware.soundtrigger.SoundTrigger.KeyphraseSoundModel fromParcel(android.os.Parcel in) {
            java.util.UUID uuid = java.util.UUID.fromString(in.readString());
            java.util.UUID vendorUuid = null;
            int length = in.readInt();
            if (length >= 0) {
                vendorUuid = java.util.UUID.fromString(in.readString());
            }
            byte[] data = in.readBlob();
            android.hardware.soundtrigger.SoundTrigger.Keyphrase[] keyphrases = in.createTypedArray(android.hardware.soundtrigger.SoundTrigger.Keyphrase.CREATOR);
            return new android.hardware.soundtrigger.SoundTrigger.KeyphraseSoundModel(uuid, vendorUuid, data, keyphrases);
        }

        @java.lang.Override
        public int describeContents() {
            return 0;
        }

        @java.lang.Override
        public void writeToParcel(android.os.Parcel dest, int flags) {
            dest.writeString(uuid.toString());
            if (vendorUuid == null) {
                dest.writeInt(-1);
            } else {
                dest.writeInt(vendorUuid.toString().length());
                dest.writeString(vendorUuid.toString());
            }
            dest.writeBlob(data);
            dest.writeTypedArray(keyphrases, flags);
        }

        @java.lang.Override
        public java.lang.String toString() {
            return ((((((((("KeyphraseSoundModel [keyphrases=" + java.util.Arrays.toString(keyphrases)) + ", uuid=") + uuid) + ", vendorUuid=") + vendorUuid) + ", type=") + type) + ", data=") + (data == null ? 0 : data.length)) + "]";
        }

        @java.lang.Override
        public int hashCode() {
            final int prime = 31;
            int result = super.hashCode();
            result = (prime * result) + java.util.Arrays.hashCode(keyphrases);
            return result;
        }

        @java.lang.Override
        public boolean equals(java.lang.Object obj) {
            if (this == obj)
                return true;

            if (!super.equals(obj))
                return false;

            if (!(obj instanceof android.hardware.soundtrigger.SoundTrigger.KeyphraseSoundModel))
                return false;

            android.hardware.soundtrigger.SoundTrigger.KeyphraseSoundModel other = ((android.hardware.soundtrigger.SoundTrigger.KeyphraseSoundModel) (obj));
            if (!java.util.Arrays.equals(keyphrases, other.keyphrases))
                return false;

            return true;
        }
    }

    /**
     * ***************************************************************************
     * A GenericSoundModel is a specialized {@link SoundModel} for non-voice sound
     * patterns.
     * **************************************************************************
     */
    public static class GenericSoundModel extends android.hardware.soundtrigger.SoundTrigger.SoundModel implements android.os.Parcelable {
        public static final android.os.Parcelable.Creator<android.hardware.soundtrigger.SoundTrigger.GenericSoundModel> CREATOR = new android.os.Parcelable.Creator<android.hardware.soundtrigger.SoundTrigger.GenericSoundModel>() {
            public android.hardware.soundtrigger.SoundTrigger.GenericSoundModel createFromParcel(android.os.Parcel in) {
                return android.hardware.soundtrigger.SoundTrigger.GenericSoundModel.fromParcel(in);
            }

            public android.hardware.soundtrigger.SoundTrigger.GenericSoundModel[] newArray(int size) {
                return new android.hardware.soundtrigger.SoundTrigger.GenericSoundModel[size];
            }
        };

        public GenericSoundModel(java.util.UUID uuid, java.util.UUID vendorUuid, byte[] data) {
            super(uuid, vendorUuid, android.hardware.soundtrigger.SoundTrigger.SoundModel.TYPE_GENERIC_SOUND, data);
        }

        @java.lang.Override
        public int describeContents() {
            return 0;
        }

        private static android.hardware.soundtrigger.SoundTrigger.GenericSoundModel fromParcel(android.os.Parcel in) {
            java.util.UUID uuid = java.util.UUID.fromString(in.readString());
            java.util.UUID vendorUuid = null;
            int length = in.readInt();
            if (length >= 0) {
                vendorUuid = java.util.UUID.fromString(in.readString());
            }
            byte[] data = in.readBlob();
            return new android.hardware.soundtrigger.SoundTrigger.GenericSoundModel(uuid, vendorUuid, data);
        }

        @java.lang.Override
        public void writeToParcel(android.os.Parcel dest, int flags) {
            dest.writeString(uuid.toString());
            if (vendorUuid == null) {
                dest.writeInt(-1);
            } else {
                dest.writeInt(vendorUuid.toString().length());
                dest.writeString(vendorUuid.toString());
            }
            dest.writeBlob(data);
        }

        @java.lang.Override
        public java.lang.String toString() {
            return ((((((("GenericSoundModel [uuid=" + uuid) + ", vendorUuid=") + vendorUuid) + ", type=") + type) + ", data=") + (data == null ? 0 : data.length)) + "]";
        }
    }

    /**
     * Modes for key phrase recognition
     */
    /**
     * Simple recognition of the key phrase
     */
    public static final int RECOGNITION_MODE_VOICE_TRIGGER = 0x1;

    /**
     * Trigger only if one user is identified
     */
    public static final int RECOGNITION_MODE_USER_IDENTIFICATION = 0x2;

    /**
     * Trigger only if one user is authenticated
     */
    public static final int RECOGNITION_MODE_USER_AUTHENTICATION = 0x4;

    /**
     * Status codes for {@link RecognitionEvent}
     */
    /**
     * Recognition success
     */
    public static final int RECOGNITION_STATUS_SUCCESS = 0;

    /**
     * Recognition aborted (e.g. capture preempted by anotehr use case
     */
    public static final int RECOGNITION_STATUS_ABORT = 1;

    /**
     * Recognition failure
     */
    public static final int RECOGNITION_STATUS_FAILURE = 2;

    /**
     * A RecognitionEvent is provided by the
     *  {@link StatusListener#onRecognition(RecognitionEvent)}
     *  callback upon recognition success or failure.
     */
    public static class RecognitionEvent implements android.os.Parcelable {
        /**
         * Recognition status e.g {@link #RECOGNITION_STATUS_SUCCESS}
         */
        public final int status;

        /**
         * Sound Model corresponding to this event callback
         */
        public final int soundModelHandle;

        /**
         * True if it is possible to capture audio from this utterance buffered by the hardware
         */
        public final boolean captureAvailable;

        /**
         * Audio session ID to be used when capturing the utterance with an AudioRecord
         * if captureAvailable() is true.
         */
        public final int captureSession;

        /**
         * Delay in ms between end of model detection and start of audio available for capture.
         * A negative value is possible (e.g. if keyphrase is also available for capture)
         */
        public final int captureDelayMs;

        /**
         * Duration in ms of audio captured before the start of the trigger. 0 if none.
         */
        public final int capturePreambleMs;

        /**
         * True if  the trigger (key phrase capture is present in binary data
         */
        public final boolean triggerInData;

        /**
         * Audio format of either the trigger in event data or to use for capture of the
         * rest of the utterance
         */
        public android.media.AudioFormat captureFormat;

        /**
         * Opaque data for use by system applications who know about voice engine internals,
         * typically during enrollment.
         */
        public final byte[] data;

        public RecognitionEvent(int status, int soundModelHandle, boolean captureAvailable, int captureSession, int captureDelayMs, int capturePreambleMs, boolean triggerInData, android.media.AudioFormat captureFormat, byte[] data) {
            this.status = status;
            this.soundModelHandle = soundModelHandle;
            this.captureAvailable = captureAvailable;
            this.captureSession = captureSession;
            this.captureDelayMs = captureDelayMs;
            this.capturePreambleMs = capturePreambleMs;
            this.triggerInData = triggerInData;
            this.captureFormat = captureFormat;
            this.data = data;
        }

        public static final android.os.Parcelable.Creator<android.hardware.soundtrigger.SoundTrigger.RecognitionEvent> CREATOR = new android.os.Parcelable.Creator<android.hardware.soundtrigger.SoundTrigger.RecognitionEvent>() {
            public android.hardware.soundtrigger.SoundTrigger.RecognitionEvent createFromParcel(android.os.Parcel in) {
                return android.hardware.soundtrigger.SoundTrigger.RecognitionEvent.fromParcel(in);
            }

            public android.hardware.soundtrigger.SoundTrigger.RecognitionEvent[] newArray(int size) {
                return new android.hardware.soundtrigger.SoundTrigger.RecognitionEvent[size];
            }
        };

        protected static android.hardware.soundtrigger.SoundTrigger.RecognitionEvent fromParcel(android.os.Parcel in) {
            int status = in.readInt();
            int soundModelHandle = in.readInt();
            boolean captureAvailable = in.readByte() == 1;
            int captureSession = in.readInt();
            int captureDelayMs = in.readInt();
            int capturePreambleMs = in.readInt();
            boolean triggerInData = in.readByte() == 1;
            android.media.AudioFormat captureFormat = null;
            if (in.readByte() == 1) {
                int sampleRate = in.readInt();
                int encoding = in.readInt();
                int channelMask = in.readInt();
                captureFormat = new android.media.AudioFormat.Builder().setChannelMask(channelMask).setEncoding(encoding).setSampleRate(sampleRate).build();
            }
            byte[] data = in.readBlob();
            return new android.hardware.soundtrigger.SoundTrigger.RecognitionEvent(status, soundModelHandle, captureAvailable, captureSession, captureDelayMs, capturePreambleMs, triggerInData, captureFormat, data);
        }

        @java.lang.Override
        public int describeContents() {
            return 0;
        }

        @java.lang.Override
        public void writeToParcel(android.os.Parcel dest, int flags) {
            dest.writeInt(status);
            dest.writeInt(soundModelHandle);
            dest.writeByte(((byte) (captureAvailable ? 1 : 0)));
            dest.writeInt(captureSession);
            dest.writeInt(captureDelayMs);
            dest.writeInt(capturePreambleMs);
            dest.writeByte(((byte) (triggerInData ? 1 : 0)));
            if (captureFormat != null) {
                dest.writeByte(((byte) (1)));
                dest.writeInt(captureFormat.getSampleRate());
                dest.writeInt(captureFormat.getEncoding());
                dest.writeInt(captureFormat.getChannelMask());
            } else {
                dest.writeByte(((byte) (0)));
            }
            dest.writeBlob(data);
        }

        @java.lang.Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = (prime * result) + (captureAvailable ? 1231 : 1237);
            result = (prime * result) + captureDelayMs;
            result = (prime * result) + capturePreambleMs;
            result = (prime * result) + captureSession;
            result = (prime * result) + (triggerInData ? 1231 : 1237);
            if (captureFormat != null) {
                result = (prime * result) + captureFormat.getSampleRate();
                result = (prime * result) + captureFormat.getEncoding();
                result = (prime * result) + captureFormat.getChannelMask();
            }
            result = (prime * result) + java.util.Arrays.hashCode(data);
            result = (prime * result) + soundModelHandle;
            result = (prime * result) + status;
            return result;
        }

        @java.lang.Override
        public boolean equals(java.lang.Object obj) {
            if (this == obj)
                return true;

            if (obj == null)
                return false;

            if (getClass() != obj.getClass())
                return false;

            android.hardware.soundtrigger.SoundTrigger.RecognitionEvent other = ((android.hardware.soundtrigger.SoundTrigger.RecognitionEvent) (obj));
            if (captureAvailable != other.captureAvailable)
                return false;

            if (captureDelayMs != other.captureDelayMs)
                return false;

            if (capturePreambleMs != other.capturePreambleMs)
                return false;

            if (captureSession != other.captureSession)
                return false;

            if (!java.util.Arrays.equals(data, other.data))
                return false;

            if (soundModelHandle != other.soundModelHandle)
                return false;

            if (status != other.status)
                return false;

            if (triggerInData != other.triggerInData)
                return false;

            if (captureFormat == null) {
                if (other.captureFormat != null)
                    return false;

            } else {
                if (other.captureFormat == null)
                    return false;

                if (captureFormat.getSampleRate() != other.captureFormat.getSampleRate())
                    return false;

                if (captureFormat.getEncoding() != other.captureFormat.getEncoding())
                    return false;

                if (captureFormat.getChannelMask() != other.captureFormat.getChannelMask())
                    return false;

            }
            return true;
        }

        @java.lang.Override
        public java.lang.String toString() {
            return (((((((((((((((((("RecognitionEvent [status=" + status) + ", soundModelHandle=") + soundModelHandle) + ", captureAvailable=") + captureAvailable) + ", captureSession=") + captureSession) + ", captureDelayMs=") + captureDelayMs) + ", capturePreambleMs=") + capturePreambleMs) + ", triggerInData=") + triggerInData) + (captureFormat == null ? "" : ", sampleRate=" + captureFormat.getSampleRate())) + (captureFormat == null ? "" : ", encoding=" + captureFormat.getEncoding())) + (captureFormat == null ? "" : ", channelMask=" + captureFormat.getChannelMask())) + ", data=") + (data == null ? 0 : data.length)) + "]";
        }
    }

    /**
     * A RecognitionConfig is provided to
     *  {@link SoundTriggerModule#startRecognition(int, RecognitionConfig)} to configure the
     *  recognition request.
     */
    public static class RecognitionConfig implements android.os.Parcelable {
        /**
         * True if the DSP should capture the trigger sound and make it available for further
         * capture.
         */
        public final boolean captureRequested;

        /**
         * True if the service should restart listening after the DSP triggers.
         * Note: This config flag is currently used at the service layer rather than by the DSP.
         */
        public final boolean allowMultipleTriggers;

        /**
         * List of all keyphrases in the sound model for which recognition should be performed with
         * options for each keyphrase.
         */
        public final android.hardware.soundtrigger.SoundTrigger.KeyphraseRecognitionExtra[] keyphrases;

        /**
         * Opaque data for use by system applications who know about voice engine internals,
         * typically during enrollment.
         */
        public final byte[] data;

        public RecognitionConfig(boolean captureRequested, boolean allowMultipleTriggers, android.hardware.soundtrigger.SoundTrigger.KeyphraseRecognitionExtra[] keyphrases, byte[] data) {
            this.captureRequested = captureRequested;
            this.allowMultipleTriggers = allowMultipleTriggers;
            this.keyphrases = keyphrases;
            this.data = data;
        }

        public static final android.os.Parcelable.Creator<android.hardware.soundtrigger.SoundTrigger.RecognitionConfig> CREATOR = new android.os.Parcelable.Creator<android.hardware.soundtrigger.SoundTrigger.RecognitionConfig>() {
            public android.hardware.soundtrigger.SoundTrigger.RecognitionConfig createFromParcel(android.os.Parcel in) {
                return android.hardware.soundtrigger.SoundTrigger.RecognitionConfig.fromParcel(in);
            }

            public android.hardware.soundtrigger.SoundTrigger.RecognitionConfig[] newArray(int size) {
                return new android.hardware.soundtrigger.SoundTrigger.RecognitionConfig[size];
            }
        };

        private static android.hardware.soundtrigger.SoundTrigger.RecognitionConfig fromParcel(android.os.Parcel in) {
            boolean captureRequested = in.readByte() == 1;
            boolean allowMultipleTriggers = in.readByte() == 1;
            android.hardware.soundtrigger.SoundTrigger.KeyphraseRecognitionExtra[] keyphrases = in.createTypedArray(android.hardware.soundtrigger.SoundTrigger.KeyphraseRecognitionExtra.CREATOR);
            byte[] data = in.readBlob();
            return new android.hardware.soundtrigger.SoundTrigger.RecognitionConfig(captureRequested, allowMultipleTriggers, keyphrases, data);
        }

        @java.lang.Override
        public void writeToParcel(android.os.Parcel dest, int flags) {
            dest.writeByte(((byte) (captureRequested ? 1 : 0)));
            dest.writeByte(((byte) (allowMultipleTriggers ? 1 : 0)));
            dest.writeTypedArray(keyphrases, flags);
            dest.writeBlob(data);
        }

        @java.lang.Override
        public int describeContents() {
            return 0;
        }

        @java.lang.Override
        public java.lang.String toString() {
            return ((((((("RecognitionConfig [captureRequested=" + captureRequested) + ", allowMultipleTriggers=") + allowMultipleTriggers) + ", keyphrases=") + java.util.Arrays.toString(keyphrases)) + ", data=") + java.util.Arrays.toString(data)) + "]";
        }
    }

    /**
     * Confidence level for users defined in a keyphrase.
     * - The confidence level is expressed in percent (0% -100%).
     * When used in a {@link KeyphraseRecognitionEvent} it indicates the detected confidence level
     * When used in a {@link RecognitionConfig} it indicates the minimum confidence level that
     * should trigger a recognition.
     * - The user ID is derived from the system ID {@link android.os.UserHandle#getIdentifier()}.
     */
    public static class ConfidenceLevel implements android.os.Parcelable {
        public final int userId;

        public final int confidenceLevel;

        public ConfidenceLevel(int userId, int confidenceLevel) {
            this.userId = userId;
            this.confidenceLevel = confidenceLevel;
        }

        public static final android.os.Parcelable.Creator<android.hardware.soundtrigger.SoundTrigger.ConfidenceLevel> CREATOR = new android.os.Parcelable.Creator<android.hardware.soundtrigger.SoundTrigger.ConfidenceLevel>() {
            public android.hardware.soundtrigger.SoundTrigger.ConfidenceLevel createFromParcel(android.os.Parcel in) {
                return android.hardware.soundtrigger.SoundTrigger.ConfidenceLevel.fromParcel(in);
            }

            public android.hardware.soundtrigger.SoundTrigger.ConfidenceLevel[] newArray(int size) {
                return new android.hardware.soundtrigger.SoundTrigger.ConfidenceLevel[size];
            }
        };

        private static android.hardware.soundtrigger.SoundTrigger.ConfidenceLevel fromParcel(android.os.Parcel in) {
            int userId = in.readInt();
            int confidenceLevel = in.readInt();
            return new android.hardware.soundtrigger.SoundTrigger.ConfidenceLevel(userId, confidenceLevel);
        }

        @java.lang.Override
        public void writeToParcel(android.os.Parcel dest, int flags) {
            dest.writeInt(userId);
            dest.writeInt(confidenceLevel);
        }

        @java.lang.Override
        public int describeContents() {
            return 0;
        }

        @java.lang.Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = (prime * result) + confidenceLevel;
            result = (prime * result) + userId;
            return result;
        }

        @java.lang.Override
        public boolean equals(java.lang.Object obj) {
            if (this == obj)
                return true;

            if (obj == null)
                return false;

            if (getClass() != obj.getClass())
                return false;

            android.hardware.soundtrigger.SoundTrigger.ConfidenceLevel other = ((android.hardware.soundtrigger.SoundTrigger.ConfidenceLevel) (obj));
            if (confidenceLevel != other.confidenceLevel)
                return false;

            if (userId != other.userId)
                return false;

            return true;
        }

        @java.lang.Override
        public java.lang.String toString() {
            return ((("ConfidenceLevel [userId=" + userId) + ", confidenceLevel=") + confidenceLevel) + "]";
        }
    }

    /**
     * Additional data conveyed by a {@link KeyphraseRecognitionEvent}
     *  for a key phrase detection.
     */
    public static class KeyphraseRecognitionExtra implements android.os.Parcelable {
        /**
         * The keyphrase ID
         */
        public final int id;

        /**
         * Recognition modes matched for this event
         */
        public final int recognitionModes;

        /**
         * Confidence level for mode RECOGNITION_MODE_VOICE_TRIGGER when user identification
         * is not performed
         */
        public final int coarseConfidenceLevel;

        /**
         * Confidence levels for all users recognized (KeyphraseRecognitionEvent) or to
         * be recognized (RecognitionConfig)
         */
        public final android.hardware.soundtrigger.SoundTrigger.ConfidenceLevel[] confidenceLevels;

        public KeyphraseRecognitionExtra(int id, int recognitionModes, int coarseConfidenceLevel, android.hardware.soundtrigger.SoundTrigger.ConfidenceLevel[] confidenceLevels) {
            this.id = id;
            this.recognitionModes = recognitionModes;
            this.coarseConfidenceLevel = coarseConfidenceLevel;
            this.confidenceLevels = confidenceLevels;
        }

        public static final android.os.Parcelable.Creator<android.hardware.soundtrigger.SoundTrigger.KeyphraseRecognitionExtra> CREATOR = new android.os.Parcelable.Creator<android.hardware.soundtrigger.SoundTrigger.KeyphraseRecognitionExtra>() {
            public android.hardware.soundtrigger.SoundTrigger.KeyphraseRecognitionExtra createFromParcel(android.os.Parcel in) {
                return android.hardware.soundtrigger.SoundTrigger.KeyphraseRecognitionExtra.fromParcel(in);
            }

            public android.hardware.soundtrigger.SoundTrigger.KeyphraseRecognitionExtra[] newArray(int size) {
                return new android.hardware.soundtrigger.SoundTrigger.KeyphraseRecognitionExtra[size];
            }
        };

        private static android.hardware.soundtrigger.SoundTrigger.KeyphraseRecognitionExtra fromParcel(android.os.Parcel in) {
            int id = in.readInt();
            int recognitionModes = in.readInt();
            int coarseConfidenceLevel = in.readInt();
            android.hardware.soundtrigger.SoundTrigger.ConfidenceLevel[] confidenceLevels = in.createTypedArray(android.hardware.soundtrigger.SoundTrigger.ConfidenceLevel.CREATOR);
            return new android.hardware.soundtrigger.SoundTrigger.KeyphraseRecognitionExtra(id, recognitionModes, coarseConfidenceLevel, confidenceLevels);
        }

        @java.lang.Override
        public void writeToParcel(android.os.Parcel dest, int flags) {
            dest.writeInt(id);
            dest.writeInt(recognitionModes);
            dest.writeInt(coarseConfidenceLevel);
            dest.writeTypedArray(confidenceLevels, flags);
        }

        @java.lang.Override
        public int describeContents() {
            return 0;
        }

        @java.lang.Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = (prime * result) + java.util.Arrays.hashCode(confidenceLevels);
            result = (prime * result) + id;
            result = (prime * result) + recognitionModes;
            result = (prime * result) + coarseConfidenceLevel;
            return result;
        }

        @java.lang.Override
        public boolean equals(java.lang.Object obj) {
            if (this == obj)
                return true;

            if (obj == null)
                return false;

            if (getClass() != obj.getClass())
                return false;

            android.hardware.soundtrigger.SoundTrigger.KeyphraseRecognitionExtra other = ((android.hardware.soundtrigger.SoundTrigger.KeyphraseRecognitionExtra) (obj));
            if (!java.util.Arrays.equals(confidenceLevels, other.confidenceLevels))
                return false;

            if (id != other.id)
                return false;

            if (recognitionModes != other.recognitionModes)
                return false;

            if (coarseConfidenceLevel != other.coarseConfidenceLevel)
                return false;

            return true;
        }

        @java.lang.Override
        public java.lang.String toString() {
            return ((((((("KeyphraseRecognitionExtra [id=" + id) + ", recognitionModes=") + recognitionModes) + ", coarseConfidenceLevel=") + coarseConfidenceLevel) + ", confidenceLevels=") + java.util.Arrays.toString(confidenceLevels)) + "]";
        }
    }

    /**
     * Specialized {@link RecognitionEvent} for a key phrase detection.
     */
    public static class KeyphraseRecognitionEvent extends android.hardware.soundtrigger.SoundTrigger.RecognitionEvent {
        /**
         * Indicates if the key phrase is present in the buffered audio available for capture
         */
        public final android.hardware.soundtrigger.SoundTrigger.KeyphraseRecognitionExtra[] keyphraseExtras;

        public KeyphraseRecognitionEvent(int status, int soundModelHandle, boolean captureAvailable, int captureSession, int captureDelayMs, int capturePreambleMs, boolean triggerInData, android.media.AudioFormat captureFormat, byte[] data, android.hardware.soundtrigger.SoundTrigger.KeyphraseRecognitionExtra[] keyphraseExtras) {
            super(status, soundModelHandle, captureAvailable, captureSession, captureDelayMs, capturePreambleMs, triggerInData, captureFormat, data);
            this.keyphraseExtras = keyphraseExtras;
        }

        public static final android.os.Parcelable.Creator<android.hardware.soundtrigger.SoundTrigger.KeyphraseRecognitionEvent> CREATOR = new android.os.Parcelable.Creator<android.hardware.soundtrigger.SoundTrigger.KeyphraseRecognitionEvent>() {
            public android.hardware.soundtrigger.SoundTrigger.KeyphraseRecognitionEvent createFromParcel(android.os.Parcel in) {
                return android.hardware.soundtrigger.SoundTrigger.KeyphraseRecognitionEvent.fromParcelForKeyphrase(in);
            }

            public android.hardware.soundtrigger.SoundTrigger.KeyphraseRecognitionEvent[] newArray(int size) {
                return new android.hardware.soundtrigger.SoundTrigger.KeyphraseRecognitionEvent[size];
            }
        };

        private static android.hardware.soundtrigger.SoundTrigger.KeyphraseRecognitionEvent fromParcelForKeyphrase(android.os.Parcel in) {
            int status = in.readInt();
            int soundModelHandle = in.readInt();
            boolean captureAvailable = in.readByte() == 1;
            int captureSession = in.readInt();
            int captureDelayMs = in.readInt();
            int capturePreambleMs = in.readInt();
            boolean triggerInData = in.readByte() == 1;
            android.media.AudioFormat captureFormat = null;
            if (in.readByte() == 1) {
                int sampleRate = in.readInt();
                int encoding = in.readInt();
                int channelMask = in.readInt();
                captureFormat = new android.media.AudioFormat.Builder().setChannelMask(channelMask).setEncoding(encoding).setSampleRate(sampleRate).build();
            }
            byte[] data = in.readBlob();
            android.hardware.soundtrigger.SoundTrigger.KeyphraseRecognitionExtra[] keyphraseExtras = in.createTypedArray(android.hardware.soundtrigger.SoundTrigger.KeyphraseRecognitionExtra.CREATOR);
            return new android.hardware.soundtrigger.SoundTrigger.KeyphraseRecognitionEvent(status, soundModelHandle, captureAvailable, captureSession, captureDelayMs, capturePreambleMs, triggerInData, captureFormat, data, keyphraseExtras);
        }

        @java.lang.Override
        public void writeToParcel(android.os.Parcel dest, int flags) {
            dest.writeInt(status);
            dest.writeInt(soundModelHandle);
            dest.writeByte(((byte) (captureAvailable ? 1 : 0)));
            dest.writeInt(captureSession);
            dest.writeInt(captureDelayMs);
            dest.writeInt(capturePreambleMs);
            dest.writeByte(((byte) (triggerInData ? 1 : 0)));
            if (captureFormat != null) {
                dest.writeByte(((byte) (1)));
                dest.writeInt(captureFormat.getSampleRate());
                dest.writeInt(captureFormat.getEncoding());
                dest.writeInt(captureFormat.getChannelMask());
            } else {
                dest.writeByte(((byte) (0)));
            }
            dest.writeBlob(data);
            dest.writeTypedArray(keyphraseExtras, flags);
        }

        @java.lang.Override
        public int describeContents() {
            return 0;
        }

        @java.lang.Override
        public int hashCode() {
            final int prime = 31;
            int result = super.hashCode();
            result = (prime * result) + java.util.Arrays.hashCode(keyphraseExtras);
            return result;
        }

        @java.lang.Override
        public boolean equals(java.lang.Object obj) {
            if (this == obj)
                return true;

            if (!super.equals(obj))
                return false;

            if (getClass() != obj.getClass())
                return false;

            android.hardware.soundtrigger.SoundTrigger.KeyphraseRecognitionEvent other = ((android.hardware.soundtrigger.SoundTrigger.KeyphraseRecognitionEvent) (obj));
            if (!java.util.Arrays.equals(keyphraseExtras, other.keyphraseExtras))
                return false;

            return true;
        }

        @java.lang.Override
        public java.lang.String toString() {
            return (((((((((((((((((((("KeyphraseRecognitionEvent [keyphraseExtras=" + java.util.Arrays.toString(keyphraseExtras)) + ", status=") + status) + ", soundModelHandle=") + soundModelHandle) + ", captureAvailable=") + captureAvailable) + ", captureSession=") + captureSession) + ", captureDelayMs=") + captureDelayMs) + ", capturePreambleMs=") + capturePreambleMs) + ", triggerInData=") + triggerInData) + (captureFormat == null ? "" : ", sampleRate=" + captureFormat.getSampleRate())) + (captureFormat == null ? "" : ", encoding=" + captureFormat.getEncoding())) + (captureFormat == null ? "" : ", channelMask=" + captureFormat.getChannelMask())) + ", data=") + (data == null ? 0 : data.length)) + "]";
        }
    }

    /**
     * Sub-class of RecognitionEvent specifically for sound-trigger based sound
     * models(non-keyphrase). Currently does not contain any additional fields.
     */
    public static class GenericRecognitionEvent extends android.hardware.soundtrigger.SoundTrigger.RecognitionEvent {
        public GenericRecognitionEvent(int status, int soundModelHandle, boolean captureAvailable, int captureSession, int captureDelayMs, int capturePreambleMs, boolean triggerInData, android.media.AudioFormat captureFormat, byte[] data) {
            super(status, soundModelHandle, captureAvailable, captureSession, captureDelayMs, capturePreambleMs, triggerInData, captureFormat, data);
        }

        public static final android.os.Parcelable.Creator<android.hardware.soundtrigger.SoundTrigger.GenericRecognitionEvent> CREATOR = new android.os.Parcelable.Creator<android.hardware.soundtrigger.SoundTrigger.GenericRecognitionEvent>() {
            public android.hardware.soundtrigger.SoundTrigger.GenericRecognitionEvent createFromParcel(android.os.Parcel in) {
                return android.hardware.soundtrigger.SoundTrigger.GenericRecognitionEvent.fromParcelForGeneric(in);
            }

            public android.hardware.soundtrigger.SoundTrigger.GenericRecognitionEvent[] newArray(int size) {
                return new android.hardware.soundtrigger.SoundTrigger.GenericRecognitionEvent[size];
            }
        };

        private static android.hardware.soundtrigger.SoundTrigger.GenericRecognitionEvent fromParcelForGeneric(android.os.Parcel in) {
            android.hardware.soundtrigger.SoundTrigger.RecognitionEvent event = android.hardware.soundtrigger.SoundTrigger.RecognitionEvent.fromParcel(in);
            return new android.hardware.soundtrigger.SoundTrigger.GenericRecognitionEvent(event.status, event.soundModelHandle, event.captureAvailable, event.captureSession, event.captureDelayMs, event.capturePreambleMs, event.triggerInData, event.captureFormat, event.data);
        }

        @java.lang.Override
        public boolean equals(java.lang.Object obj) {
            if (this == obj)
                return true;

            if (obj == null)
                return false;

            if (getClass() != obj.getClass())
                return false;

            android.hardware.soundtrigger.SoundTrigger.RecognitionEvent other = ((android.hardware.soundtrigger.SoundTrigger.RecognitionEvent) (obj));
            return super.equals(obj);
        }

        @java.lang.Override
        public java.lang.String toString() {
            return "GenericRecognitionEvent ::" + super.toString();
        }
    }

    /**
     * Status codes for {@link SoundModelEvent}
     */
    /**
     * Sound Model was updated
     */
    public static final int SOUNDMODEL_STATUS_UPDATED = 0;

    /**
     * A SoundModelEvent is provided by the
     *  {@link StatusListener#onSoundModelUpdate(SoundModelEvent)}
     *  callback when a sound model has been updated by the implementation
     */
    public static class SoundModelEvent implements android.os.Parcelable {
        /**
         * Status e.g {@link #SOUNDMODEL_STATUS_UPDATED}
         */
        public final int status;

        /**
         * The updated sound model handle
         */
        public final int soundModelHandle;

        /**
         * New sound model data
         */
        public final byte[] data;

        SoundModelEvent(int status, int soundModelHandle, byte[] data) {
            this.status = status;
            this.soundModelHandle = soundModelHandle;
            this.data = data;
        }

        public static final android.os.Parcelable.Creator<android.hardware.soundtrigger.SoundTrigger.SoundModelEvent> CREATOR = new android.os.Parcelable.Creator<android.hardware.soundtrigger.SoundTrigger.SoundModelEvent>() {
            public android.hardware.soundtrigger.SoundTrigger.SoundModelEvent createFromParcel(android.os.Parcel in) {
                return android.hardware.soundtrigger.SoundTrigger.SoundModelEvent.fromParcel(in);
            }

            public android.hardware.soundtrigger.SoundTrigger.SoundModelEvent[] newArray(int size) {
                return new android.hardware.soundtrigger.SoundTrigger.SoundModelEvent[size];
            }
        };

        private static android.hardware.soundtrigger.SoundTrigger.SoundModelEvent fromParcel(android.os.Parcel in) {
            int status = in.readInt();
            int soundModelHandle = in.readInt();
            byte[] data = in.readBlob();
            return new android.hardware.soundtrigger.SoundTrigger.SoundModelEvent(status, soundModelHandle, data);
        }

        @java.lang.Override
        public int describeContents() {
            return 0;
        }

        @java.lang.Override
        public void writeToParcel(android.os.Parcel dest, int flags) {
            dest.writeInt(status);
            dest.writeInt(soundModelHandle);
            dest.writeBlob(data);
        }

        @java.lang.Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = (prime * result) + java.util.Arrays.hashCode(data);
            result = (prime * result) + soundModelHandle;
            result = (prime * result) + status;
            return result;
        }

        @java.lang.Override
        public boolean equals(java.lang.Object obj) {
            if (this == obj)
                return true;

            if (obj == null)
                return false;

            if (getClass() != obj.getClass())
                return false;

            android.hardware.soundtrigger.SoundTrigger.SoundModelEvent other = ((android.hardware.soundtrigger.SoundTrigger.SoundModelEvent) (obj));
            if (!java.util.Arrays.equals(data, other.data))
                return false;

            if (soundModelHandle != other.soundModelHandle)
                return false;

            if (status != other.status)
                return false;

            return true;
        }

        @java.lang.Override
        public java.lang.String toString() {
            return ((((("SoundModelEvent [status=" + status) + ", soundModelHandle=") + soundModelHandle) + ", data=") + (data == null ? 0 : data.length)) + "]";
        }
    }

    /**
     * Native service state. {@link StatusListener#onServiceStateChange(int)}
     */
    // Keep in sync with system/core/include/system/sound_trigger.h
    /**
     * Sound trigger service is enabled
     */
    public static final int SERVICE_STATE_ENABLED = 0;

    /**
     * Sound trigger service is disabled
     */
    public static final int SERVICE_STATE_DISABLED = 1;

    /**
     * Returns a list of descriptors for all hardware modules loaded.
     *
     * @param modules
     * 		A ModuleProperties array where the list will be returned.
     * @return - {@link #STATUS_OK} in case of success
    - {@link #STATUS_ERROR} in case of unspecified error
    - {@link #STATUS_PERMISSION_DENIED} if the caller does not have system permission
    - {@link #STATUS_NO_INIT} if the native service cannot be reached
    - {@link #STATUS_BAD_VALUE} if modules is null
    - {@link #STATUS_DEAD_OBJECT} if the binder transaction to the native service fails
     */
    public static native int listModules(java.util.ArrayList<android.hardware.soundtrigger.SoundTrigger.ModuleProperties> modules);

    /**
     * Get an interface on a hardware module to control sound models and recognition on
     * this module.
     *
     * @param moduleId
     * 		Sound module system identifier {@link ModuleProperties#id}. mandatory.
     * @param listener
     * 		{@link StatusListener} interface. Mandatory.
     * @param handler
     * 		the Handler that will receive the callabcks. Can be null if default handler
     * 		is OK.
     * @return a valid sound module in case of success or null in case of error.
     */
    public static android.hardware.soundtrigger.SoundTriggerModule attachModule(int moduleId, android.hardware.soundtrigger.SoundTrigger.StatusListener listener, android.os.Handler handler) {
        if (listener == null) {
            return null;
        }
        android.hardware.soundtrigger.SoundTriggerModule module = new android.hardware.soundtrigger.SoundTriggerModule(moduleId, listener, handler);
        return module;
    }

    /**
     * Interface provided by the client application when attaching to a {@link SoundTriggerModule}
     * to received recognition and error notifications.
     */
    public static interface StatusListener {
        /**
         * Called when recognition succeeds of fails
         */
        public abstract void onRecognition(android.hardware.soundtrigger.SoundTrigger.RecognitionEvent event);

        /**
         * Called when a sound model has been updated
         */
        public abstract void onSoundModelUpdate(android.hardware.soundtrigger.SoundTrigger.SoundModelEvent event);

        /**
         * Called when the sound trigger native service state changes.
         *
         * @param state
         * 		Native service state. One of {@link SoundTrigger#SERVICE_STATE_ENABLED},
         * 		{@link SoundTrigger#SERVICE_STATE_DISABLED}
         */
        public abstract void onServiceStateChange(int state);

        /**
         * Called when the sound trigger native service dies
         */
        public abstract void onServiceDied();
    }
}

