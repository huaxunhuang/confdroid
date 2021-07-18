/**
 * Copyright (C) 2015 The Android Open Source Project
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
package android.hardware.radio;


/**
 * The RadioManager class allows to control a broadcast radio tuner present on the device.
 * It provides data structures and methods to query for available radio modules, list their
 * properties and open an interface to control tuning operations and receive callbacks when
 * asynchronous operations complete or events occur.
 *
 * @unknown 
 */
@android.annotation.SystemApi
public class RadioManager {
    /**
     * Method return status: successful operation
     */
    public static final int STATUS_OK = 0;

    /**
     * Method return status: unspecified error
     */
    public static final int STATUS_ERROR = java.lang.Integer.MIN_VALUE;

    /**
     * Method return status: permission denied
     */
    public static final int STATUS_PERMISSION_DENIED = -1;

    /**
     * Method return status: initialization failure
     */
    public static final int STATUS_NO_INIT = -19;

    /**
     * Method return status: invalid argument provided
     */
    public static final int STATUS_BAD_VALUE = -22;

    /**
     * Method return status: cannot reach service
     */
    public static final int STATUS_DEAD_OBJECT = -32;

    /**
     * Method return status: invalid or out of sequence operation
     */
    public static final int STATUS_INVALID_OPERATION = -38;

    /**
     * Method return status: time out before operation completion
     */
    public static final int STATUS_TIMED_OUT = -110;

    // keep in sync with radio_class_t in /system/core/incluse/system/radio.h
    /**
     * Radio module class supporting FM (including HD radio) and AM
     */
    public static final int CLASS_AM_FM = 0;

    /**
     * Radio module class supporting satellite radio
     */
    public static final int CLASS_SAT = 1;

    /**
     * Radio module class supporting Digital terrestrial radio
     */
    public static final int CLASS_DT = 2;

    // keep in sync with radio_band_t in /system/core/incluse/system/radio.h
    /**
     * AM radio band (LW/MW/SW).
     *
     * @see BandDescriptor
     */
    public static final int BAND_AM = 0;

    /**
     * FM radio band.
     *
     * @see BandDescriptor
     */
    public static final int BAND_FM = 1;

    /**
     * FM HD radio or DRM  band.
     *
     * @see BandDescriptor
     */
    public static final int BAND_FM_HD = 2;

    /**
     * AM HD radio or DRM band.
     *
     * @see BandDescriptor
     */
    public static final int BAND_AM_HD = 3;

    // keep in sync with radio_region_t in /system/core/incluse/system/radio.h
    /**
     * Africa, Europe.
     *
     * @see BandDescriptor
     */
    public static final int REGION_ITU_1 = 0;

    /**
     * Americas.
     *
     * @see BandDescriptor
     */
    public static final int REGION_ITU_2 = 1;

    /**
     * Russia.
     *
     * @see BandDescriptor
     */
    public static final int REGION_OIRT = 2;

    /**
     * Japan.
     *
     * @see BandDescriptor
     */
    public static final int REGION_JAPAN = 3;

    /**
     * Korea.
     *
     * @see BandDescriptor
     */
    public static final int REGION_KOREA = 4;

    /**
     * ***************************************************************************
     * Lists properties, options and radio bands supported by a given broadcast radio module.
     * Each module has a unique ID used to address it when calling RadioManager APIs.
     * Module properties are returned by {@link #listModules(List <ModuleProperties>)} method.
     * **************************************************************************
     */
    public static class ModuleProperties implements android.os.Parcelable {
        private final int mId;

        private final int mClassId;

        private final java.lang.String mImplementor;

        private final java.lang.String mProduct;

        private final java.lang.String mVersion;

        private final java.lang.String mSerial;

        private final int mNumTuners;

        private final int mNumAudioSources;

        private final boolean mIsCaptureSupported;

        private final android.hardware.radio.RadioManager.BandDescriptor[] mBands;

        ModuleProperties(int id, int classId, java.lang.String implementor, java.lang.String product, java.lang.String version, java.lang.String serial, int numTuners, int numAudioSources, boolean isCaptureSupported, android.hardware.radio.RadioManager.BandDescriptor[] bands) {
            mId = id;
            mClassId = classId;
            mImplementor = implementor;
            mProduct = product;
            mVersion = version;
            mSerial = serial;
            mNumTuners = numTuners;
            mNumAudioSources = numAudioSources;
            mIsCaptureSupported = isCaptureSupported;
            mBands = bands;
        }

        /**
         * Unique module identifier provided by the native service.
         * For use with {@link #openTuner(int, BandConfig, boolean, Callback, Handler)}.
         *
         * @return the radio module unique identifier.
         */
        public int getId() {
            return mId;
        }

        /**
         * Module class identifier: {@link #CLASS_AM_FM}, {@link #CLASS_SAT}, {@link #CLASS_DT}
         *
         * @return the radio module class identifier.
         */
        public int getClassId() {
            return mClassId;
        }

        /**
         * Human readable broadcast radio module implementor
         *
         * @return the name of the radio module implementator.
         */
        public java.lang.String getImplementor() {
            return mImplementor;
        }

        /**
         * Human readable broadcast radio module product name
         *
         * @return the radio module product name.
         */
        public java.lang.String getProduct() {
            return mProduct;
        }

        /**
         * Human readable broadcast radio module version number
         *
         * @return the radio module version.
         */
        public java.lang.String getVersion() {
            return mVersion;
        }

        /**
         * Radio module serial number.
         * Can be used for subscription services.
         *
         * @return the radio module serial number.
         */
        public java.lang.String getSerial() {
            return mSerial;
        }

        /**
         * Number of tuners available.
         * This is the number of tuners that can be open simultaneously.
         *
         * @return the number of tuners supported.
         */
        public int getNumTuners() {
            return mNumTuners;
        }

        /**
         * Number tuner audio sources available. Must be less or equal to getNumTuners().
         * When more than one tuner is supported, one is usually for playback and has one
         * associated audio source and the other is for pre scanning and building a
         * program list.
         *
         * @return the number of audio sources available.
         */
        public int getNumAudioSources() {
            return mNumAudioSources;
        }

        /**
         * {@code true} if audio capture is possible from radio tuner output.
         * This indicates if routing to audio devices not connected to the same HAL as the FM radio
         * is possible (e.g. to USB) or DAR (Digital Audio Recorder) feature can be implemented.
         *
         * @return {@code true} if audio capture is possible, {@code false} otherwise.
         */
        public boolean isCaptureSupported() {
            return mIsCaptureSupported;
        }

        /**
         * List of descriptors for all bands supported by this module.
         *
         * @return an array of {@link BandDescriptor}.
         */
        public android.hardware.radio.RadioManager.BandDescriptor[] getBands() {
            return mBands;
        }

        private ModuleProperties(android.os.Parcel in) {
            mId = in.readInt();
            mClassId = in.readInt();
            mImplementor = in.readString();
            mProduct = in.readString();
            mVersion = in.readString();
            mSerial = in.readString();
            mNumTuners = in.readInt();
            mNumAudioSources = in.readInt();
            mIsCaptureSupported = in.readInt() == 1;
            android.os.Parcelable[] tmp = in.readParcelableArray(android.hardware.radio.RadioManager.BandDescriptor.class.getClassLoader());
            mBands = new android.hardware.radio.RadioManager.BandDescriptor[tmp.length];
            for (int i = 0; i < tmp.length; i++) {
                mBands[i] = ((android.hardware.radio.RadioManager.BandDescriptor) (tmp[i]));
            }
        }

        public static final android.os.Parcelable.Creator<android.hardware.radio.RadioManager.ModuleProperties> CREATOR = new android.os.Parcelable.Creator<android.hardware.radio.RadioManager.ModuleProperties>() {
            public android.hardware.radio.RadioManager.ModuleProperties createFromParcel(android.os.Parcel in) {
                return new android.hardware.radio.RadioManager.ModuleProperties(in);
            }

            public android.hardware.radio.RadioManager.ModuleProperties[] newArray(int size) {
                return new android.hardware.radio.RadioManager.ModuleProperties[size];
            }
        };

        @java.lang.Override
        public void writeToParcel(android.os.Parcel dest, int flags) {
            dest.writeInt(mId);
            dest.writeInt(mClassId);
            dest.writeString(mImplementor);
            dest.writeString(mProduct);
            dest.writeString(mVersion);
            dest.writeString(mSerial);
            dest.writeInt(mNumTuners);
            dest.writeInt(mNumAudioSources);
            dest.writeInt(mIsCaptureSupported ? 1 : 0);
            dest.writeParcelableArray(mBands, flags);
        }

        @java.lang.Override
        public int describeContents() {
            return 0;
        }

        @java.lang.Override
        public java.lang.String toString() {
            return ((((((((((((((((((("ModuleProperties [mId=" + mId) + ", mClassId=") + mClassId) + ", mImplementor=") + mImplementor) + ", mProduct=") + mProduct) + ", mVersion=") + mVersion) + ", mSerial=") + mSerial) + ", mNumTuners=") + mNumTuners) + ", mNumAudioSources=") + mNumAudioSources) + ", mIsCaptureSupported=") + mIsCaptureSupported) + ", mBands=") + java.util.Arrays.toString(mBands)) + "]";
        }

        @java.lang.Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = (prime * result) + mId;
            result = (prime * result) + mClassId;
            result = (prime * result) + (mImplementor == null ? 0 : mImplementor.hashCode());
            result = (prime * result) + (mProduct == null ? 0 : mProduct.hashCode());
            result = (prime * result) + (mVersion == null ? 0 : mVersion.hashCode());
            result = (prime * result) + (mSerial == null ? 0 : mSerial.hashCode());
            result = (prime * result) + mNumTuners;
            result = (prime * result) + mNumAudioSources;
            result = (prime * result) + (mIsCaptureSupported ? 1 : 0);
            result = (prime * result) + java.util.Arrays.hashCode(mBands);
            return result;
        }

        @java.lang.Override
        public boolean equals(java.lang.Object obj) {
            if (this == obj)
                return true;

            if (!(obj instanceof android.hardware.radio.RadioManager.ModuleProperties))
                return false;

            android.hardware.radio.RadioManager.ModuleProperties other = ((android.hardware.radio.RadioManager.ModuleProperties) (obj));
            if (mId != other.getId())
                return false;

            if (mClassId != other.getClassId())
                return false;

            if (mImplementor == null) {
                if (other.getImplementor() != null)
                    return false;

            } else
                if (!mImplementor.equals(other.getImplementor()))
                    return false;


            if (mProduct == null) {
                if (other.getProduct() != null)
                    return false;

            } else
                if (!mProduct.equals(other.getProduct()))
                    return false;


            if (mVersion == null) {
                if (other.getVersion() != null)
                    return false;

            } else
                if (!mVersion.equals(other.getVersion()))
                    return false;


            if (mSerial == null) {
                if (other.getSerial() != null)
                    return false;

            } else
                if (!mSerial.equals(other.getSerial()))
                    return false;


            if (mNumTuners != other.getNumTuners())
                return false;

            if (mNumAudioSources != other.getNumAudioSources())
                return false;

            if (mIsCaptureSupported != other.isCaptureSupported())
                return false;

            if (!java.util.Arrays.equals(mBands, other.getBands()))
                return false;

            return true;
        }
    }

    /**
     * Radio band descriptor: an element in ModuleProperties bands array.
     * It is either an instance of {@link FmBandDescriptor} or {@link AmBandDescriptor}
     */
    public static class BandDescriptor implements android.os.Parcelable {
        private final int mRegion;

        private final int mType;

        private final int mLowerLimit;

        private final int mUpperLimit;

        private final int mSpacing;

        BandDescriptor(int region, int type, int lowerLimit, int upperLimit, int spacing) {
            mRegion = region;
            mType = type;
            mLowerLimit = lowerLimit;
            mUpperLimit = upperLimit;
            mSpacing = spacing;
        }

        /**
         * Region this band applies to. E.g. {@link #REGION_ITU_1}
         *
         * @return the region this band is associated to.
         */
        public int getRegion() {
            return mRegion;
        }

        /**
         * Band type, e.g {@link #BAND_FM}. Defines the subclass this descriptor can be cast to:
         * <ul>
         *  <li>{@link #BAND_FM} or {@link #BAND_FM_HD} cast to {@link FmBandDescriptor}, </li>
         *  <li>{@link #BAND_AM} cast to {@link AmBandDescriptor}, </li>
         * </ul>
         *
         * @return the band type.
         */
        public int getType() {
            return mType;
        }

        /**
         * Lower band limit expressed in units according to band type.
         * Currently all defined band types express channels as frequency in kHz
         *
         * @return the lower band limit.
         */
        public int getLowerLimit() {
            return mLowerLimit;
        }

        /**
         * Upper band limit expressed in units according to band type.
         * Currently all defined band types express channels as frequency in kHz
         *
         * @return the upper band limit.
         */
        public int getUpperLimit() {
            return mUpperLimit;
        }

        /**
         * Channel spacing in units according to band type.
         * Currently all defined band types express channels as frequency in kHz
         *
         * @return the channel spacing.
         */
        public int getSpacing() {
            return mSpacing;
        }

        private BandDescriptor(android.os.Parcel in) {
            mRegion = in.readInt();
            mType = in.readInt();
            mLowerLimit = in.readInt();
            mUpperLimit = in.readInt();
            mSpacing = in.readInt();
        }

        public static final android.os.Parcelable.Creator<android.hardware.radio.RadioManager.BandDescriptor> CREATOR = new android.os.Parcelable.Creator<android.hardware.radio.RadioManager.BandDescriptor>() {
            public android.hardware.radio.RadioManager.BandDescriptor createFromParcel(android.os.Parcel in) {
                return new android.hardware.radio.RadioManager.BandDescriptor(in);
            }

            public android.hardware.radio.RadioManager.BandDescriptor[] newArray(int size) {
                return new android.hardware.radio.RadioManager.BandDescriptor[size];
            }
        };

        @java.lang.Override
        public void writeToParcel(android.os.Parcel dest, int flags) {
            dest.writeInt(mRegion);
            dest.writeInt(mType);
            dest.writeInt(mLowerLimit);
            dest.writeInt(mUpperLimit);
            dest.writeInt(mSpacing);
        }

        @java.lang.Override
        public int describeContents() {
            return 0;
        }

        @java.lang.Override
        public java.lang.String toString() {
            return ((((((((("BandDescriptor [mRegion=" + mRegion) + ", mType=") + mType) + ", mLowerLimit=") + mLowerLimit) + ", mUpperLimit=") + mUpperLimit) + ", mSpacing=") + mSpacing) + "]";
        }

        @java.lang.Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = (prime * result) + mRegion;
            result = (prime * result) + mType;
            result = (prime * result) + mLowerLimit;
            result = (prime * result) + mUpperLimit;
            result = (prime * result) + mSpacing;
            return result;
        }

        @java.lang.Override
        public boolean equals(java.lang.Object obj) {
            if (this == obj)
                return true;

            if (!(obj instanceof android.hardware.radio.RadioManager.BandDescriptor))
                return false;

            android.hardware.radio.RadioManager.BandDescriptor other = ((android.hardware.radio.RadioManager.BandDescriptor) (obj));
            if (mRegion != other.getRegion())
                return false;

            if (mType != other.getType())
                return false;

            if (mLowerLimit != other.getLowerLimit())
                return false;

            if (mUpperLimit != other.getUpperLimit())
                return false;

            if (mSpacing != other.getSpacing())
                return false;

            return true;
        }
    }

    /**
     * FM band descriptor
     *
     * @see #BAND_FM
     * @see #BAND_FM_HD
     */
    public static class FmBandDescriptor extends android.hardware.radio.RadioManager.BandDescriptor {
        private final boolean mStereo;

        private final boolean mRds;

        private final boolean mTa;

        private final boolean mAf;

        private final boolean mEa;

        FmBandDescriptor(int region, int type, int lowerLimit, int upperLimit, int spacing, boolean stereo, boolean rds, boolean ta, boolean af, boolean ea) {
            super(region, type, lowerLimit, upperLimit, spacing);
            mStereo = stereo;
            mRds = rds;
            mTa = ta;
            mAf = af;
            mEa = ea;
        }

        /**
         * Stereo is supported
         *
         * @return {@code true} if stereo is supported, {@code false} otherwise.
         */
        public boolean isStereoSupported() {
            return mStereo;
        }

        /**
         * RDS or RBDS(if region is ITU2) is supported
         *
         * @return {@code true} if RDS or RBDS is supported, {@code false} otherwise.
         */
        public boolean isRdsSupported() {
            return mRds;
        }

        /**
         * Traffic announcement is supported
         *
         * @return {@code true} if TA is supported, {@code false} otherwise.
         */
        public boolean isTaSupported() {
            return mTa;
        }

        /**
         * Alternate Frequency Switching is supported
         *
         * @return {@code true} if AF switching is supported, {@code false} otherwise.
         */
        public boolean isAfSupported() {
            return mAf;
        }

        /**
         * Emergency Announcement is supported
         *
         * @return {@code true} if Emergency annoucement is supported, {@code false} otherwise.
         */
        public boolean isEaSupported() {
            return mEa;
        }

        /* Parcelable implementation */
        private FmBandDescriptor(android.os.Parcel in) {
            super(in);
            mStereo = in.readByte() == 1;
            mRds = in.readByte() == 1;
            mTa = in.readByte() == 1;
            mAf = in.readByte() == 1;
            mEa = in.readByte() == 1;
        }

        public static final android.os.Parcelable.Creator<android.hardware.radio.RadioManager.FmBandDescriptor> CREATOR = new android.os.Parcelable.Creator<android.hardware.radio.RadioManager.FmBandDescriptor>() {
            public android.hardware.radio.RadioManager.FmBandDescriptor createFromParcel(android.os.Parcel in) {
                return new android.hardware.radio.RadioManager.FmBandDescriptor(in);
            }

            public android.hardware.radio.RadioManager.FmBandDescriptor[] newArray(int size) {
                return new android.hardware.radio.RadioManager.FmBandDescriptor[size];
            }
        };

        @java.lang.Override
        public void writeToParcel(android.os.Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeByte(((byte) (mStereo ? 1 : 0)));
            dest.writeByte(((byte) (mRds ? 1 : 0)));
            dest.writeByte(((byte) (mTa ? 1 : 0)));
            dest.writeByte(((byte) (mAf ? 1 : 0)));
            dest.writeByte(((byte) (mEa ? 1 : 0)));
        }

        @java.lang.Override
        public int describeContents() {
            return 0;
        }

        @java.lang.Override
        public java.lang.String toString() {
            return ((((((((((("FmBandDescriptor [ " + super.toString()) + " mStereo=") + mStereo) + ", mRds=") + mRds) + ", mTa=") + mTa) + ", mAf=") + mAf) + ", mEa =") + mEa) + "]";
        }

        @java.lang.Override
        public int hashCode() {
            final int prime = 31;
            int result = super.hashCode();
            result = (prime * result) + (mStereo ? 1 : 0);
            result = (prime * result) + (mRds ? 1 : 0);
            result = (prime * result) + (mTa ? 1 : 0);
            result = (prime * result) + (mAf ? 1 : 0);
            result = (prime * result) + (mEa ? 1 : 0);
            return result;
        }

        @java.lang.Override
        public boolean equals(java.lang.Object obj) {
            if (this == obj)
                return true;

            if (!super.equals(obj))
                return false;

            if (!(obj instanceof android.hardware.radio.RadioManager.FmBandDescriptor))
                return false;

            android.hardware.radio.RadioManager.FmBandDescriptor other = ((android.hardware.radio.RadioManager.FmBandDescriptor) (obj));
            if (mStereo != other.isStereoSupported())
                return false;

            if (mRds != other.isRdsSupported())
                return false;

            if (mTa != other.isTaSupported())
                return false;

            if (mAf != other.isAfSupported())
                return false;

            if (mEa != other.isEaSupported())
                return false;

            return true;
        }
    }

    /**
     * AM band descriptor.
     *
     * @see #BAND_AM
     */
    public static class AmBandDescriptor extends android.hardware.radio.RadioManager.BandDescriptor {
        private final boolean mStereo;

        AmBandDescriptor(int region, int type, int lowerLimit, int upperLimit, int spacing, boolean stereo) {
            super(region, type, lowerLimit, upperLimit, spacing);
            mStereo = stereo;
        }

        /**
         * Stereo is supported
         *
         * @return {@code true} if stereo is supported, {@code false} otherwise.
         */
        public boolean isStereoSupported() {
            return mStereo;
        }

        private AmBandDescriptor(android.os.Parcel in) {
            super(in);
            mStereo = in.readByte() == 1;
        }

        public static final android.os.Parcelable.Creator<android.hardware.radio.RadioManager.AmBandDescriptor> CREATOR = new android.os.Parcelable.Creator<android.hardware.radio.RadioManager.AmBandDescriptor>() {
            public android.hardware.radio.RadioManager.AmBandDescriptor createFromParcel(android.os.Parcel in) {
                return new android.hardware.radio.RadioManager.AmBandDescriptor(in);
            }

            public android.hardware.radio.RadioManager.AmBandDescriptor[] newArray(int size) {
                return new android.hardware.radio.RadioManager.AmBandDescriptor[size];
            }
        };

        @java.lang.Override
        public void writeToParcel(android.os.Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeByte(((byte) (mStereo ? 1 : 0)));
        }

        @java.lang.Override
        public int describeContents() {
            return 0;
        }

        @java.lang.Override
        public java.lang.String toString() {
            return ((("AmBandDescriptor [ " + super.toString()) + " mStereo=") + mStereo) + "]";
        }

        @java.lang.Override
        public int hashCode() {
            final int prime = 31;
            int result = super.hashCode();
            result = (prime * result) + (mStereo ? 1 : 0);
            return result;
        }

        @java.lang.Override
        public boolean equals(java.lang.Object obj) {
            if (this == obj)
                return true;

            if (!super.equals(obj))
                return false;

            if (!(obj instanceof android.hardware.radio.RadioManager.AmBandDescriptor))
                return false;

            android.hardware.radio.RadioManager.AmBandDescriptor other = ((android.hardware.radio.RadioManager.AmBandDescriptor) (obj));
            if (mStereo != other.isStereoSupported())
                return false;

            return true;
        }
    }

    /**
     * Radio band configuration.
     */
    public static class BandConfig implements android.os.Parcelable {
        final android.hardware.radio.RadioManager.BandDescriptor mDescriptor;

        BandConfig(android.hardware.radio.RadioManager.BandDescriptor descriptor) {
            mDescriptor = descriptor;
        }

        BandConfig(int region, int type, int lowerLimit, int upperLimit, int spacing) {
            mDescriptor = new android.hardware.radio.RadioManager.BandDescriptor(region, type, lowerLimit, upperLimit, spacing);
        }

        private BandConfig(android.os.Parcel in) {
            mDescriptor = new android.hardware.radio.RadioManager.BandDescriptor(in);
        }

        android.hardware.radio.RadioManager.BandDescriptor getDescriptor() {
            return mDescriptor;
        }

        /**
         * Region this band applies to. E.g. {@link #REGION_ITU_1}
         *
         * @return the region associated with this band.
         */
        public int getRegion() {
            return mDescriptor.getRegion();
        }

        /**
         * Band type, e.g {@link #BAND_FM}. Defines the subclass this descriptor can be cast to:
         * <ul>
         *  <li>{@link #BAND_FM} or {@link #BAND_FM_HD} cast to {@link FmBandDescriptor}, </li>
         *  <li>{@link #BAND_AM} cast to {@link AmBandDescriptor}, </li>
         * </ul>
         *
         * @return the band type.
         */
        public int getType() {
            return mDescriptor.getType();
        }

        /**
         * Lower band limit expressed in units according to band type.
         * Currently all defined band types express channels as frequency in kHz
         *
         * @return the lower band limit.
         */
        public int getLowerLimit() {
            return mDescriptor.getLowerLimit();
        }

        /**
         * Upper band limit expressed in units according to band type.
         * Currently all defined band types express channels as frequency in kHz
         *
         * @return the upper band limit.
         */
        public int getUpperLimit() {
            return mDescriptor.getUpperLimit();
        }

        /**
         * Channel spacing in units according to band type.
         * Currently all defined band types express channels as frequency in kHz
         *
         * @return the channel spacing.
         */
        public int getSpacing() {
            return mDescriptor.getSpacing();
        }

        public static final android.os.Parcelable.Creator<android.hardware.radio.RadioManager.BandConfig> CREATOR = new android.os.Parcelable.Creator<android.hardware.radio.RadioManager.BandConfig>() {
            public android.hardware.radio.RadioManager.BandConfig createFromParcel(android.os.Parcel in) {
                return new android.hardware.radio.RadioManager.BandConfig(in);
            }

            public android.hardware.radio.RadioManager.BandConfig[] newArray(int size) {
                return new android.hardware.radio.RadioManager.BandConfig[size];
            }
        };

        @java.lang.Override
        public void writeToParcel(android.os.Parcel dest, int flags) {
            mDescriptor.writeToParcel(dest, flags);
        }

        @java.lang.Override
        public int describeContents() {
            return 0;
        }

        @java.lang.Override
        public java.lang.String toString() {
            return ("BandConfig [ " + mDescriptor.toString()) + "]";
        }

        @java.lang.Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = (prime * result) + mDescriptor.hashCode();
            return result;
        }

        @java.lang.Override
        public boolean equals(java.lang.Object obj) {
            if (this == obj)
                return true;

            if (!(obj instanceof android.hardware.radio.RadioManager.BandConfig))
                return false;

            android.hardware.radio.RadioManager.BandConfig other = ((android.hardware.radio.RadioManager.BandConfig) (obj));
            if (mDescriptor != other.getDescriptor())
                return false;

            return true;
        }
    }

    /**
     * FM band configuration.
     *
     * @see #BAND_FM
     * @see #BAND_FM_HD
     */
    public static class FmBandConfig extends android.hardware.radio.RadioManager.BandConfig {
        private final boolean mStereo;

        private final boolean mRds;

        private final boolean mTa;

        private final boolean mAf;

        private final boolean mEa;

        FmBandConfig(android.hardware.radio.RadioManager.FmBandDescriptor descriptor) {
            super(((android.hardware.radio.RadioManager.BandDescriptor) (descriptor)));
            mStereo = descriptor.isStereoSupported();
            mRds = descriptor.isRdsSupported();
            mTa = descriptor.isTaSupported();
            mAf = descriptor.isAfSupported();
            mEa = descriptor.isEaSupported();
        }

        FmBandConfig(int region, int type, int lowerLimit, int upperLimit, int spacing, boolean stereo, boolean rds, boolean ta, boolean af, boolean ea) {
            super(region, type, lowerLimit, upperLimit, spacing);
            mStereo = stereo;
            mRds = rds;
            mTa = ta;
            mAf = af;
            mEa = ea;
        }

        /**
         * Get stereo enable state
         *
         * @return the enable state.
         */
        public boolean getStereo() {
            return mStereo;
        }

        /**
         * Get RDS or RBDS(if region is ITU2) enable state
         *
         * @return the enable state.
         */
        public boolean getRds() {
            return mRds;
        }

        /**
         * Get Traffic announcement enable state
         *
         * @return the enable state.
         */
        public boolean getTa() {
            return mTa;
        }

        /**
         * Get Alternate Frequency Switching enable state
         *
         * @return the enable state.
         */
        public boolean getAf() {
            return mAf;
        }

        /**
         * Get Emergency announcement enable state
         *
         * @return the enable state.
         */
        public boolean getEa() {
            return mEa;
        }

        private FmBandConfig(android.os.Parcel in) {
            super(in);
            mStereo = in.readByte() == 1;
            mRds = in.readByte() == 1;
            mTa = in.readByte() == 1;
            mAf = in.readByte() == 1;
            mEa = in.readByte() == 1;
        }

        public static final android.os.Parcelable.Creator<android.hardware.radio.RadioManager.FmBandConfig> CREATOR = new android.os.Parcelable.Creator<android.hardware.radio.RadioManager.FmBandConfig>() {
            public android.hardware.radio.RadioManager.FmBandConfig createFromParcel(android.os.Parcel in) {
                return new android.hardware.radio.RadioManager.FmBandConfig(in);
            }

            public android.hardware.radio.RadioManager.FmBandConfig[] newArray(int size) {
                return new android.hardware.radio.RadioManager.FmBandConfig[size];
            }
        };

        @java.lang.Override
        public void writeToParcel(android.os.Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeByte(((byte) (mStereo ? 1 : 0)));
            dest.writeByte(((byte) (mRds ? 1 : 0)));
            dest.writeByte(((byte) (mTa ? 1 : 0)));
            dest.writeByte(((byte) (mAf ? 1 : 0)));
            dest.writeByte(((byte) (mEa ? 1 : 0)));
        }

        @java.lang.Override
        public int describeContents() {
            return 0;
        }

        @java.lang.Override
        public java.lang.String toString() {
            return ((((((((((("FmBandConfig [" + super.toString()) + ", mStereo=") + mStereo) + ", mRds=") + mRds) + ", mTa=") + mTa) + ", mAf=") + mAf) + ", mEa =") + mEa) + "]";
        }

        @java.lang.Override
        public int hashCode() {
            final int prime = 31;
            int result = super.hashCode();
            result = (prime * result) + (mStereo ? 1 : 0);
            result = (prime * result) + (mRds ? 1 : 0);
            result = (prime * result) + (mTa ? 1 : 0);
            result = (prime * result) + (mAf ? 1 : 0);
            result = (prime * result) + (mEa ? 1 : 0);
            return result;
        }

        @java.lang.Override
        public boolean equals(java.lang.Object obj) {
            if (this == obj)
                return true;

            if (!super.equals(obj))
                return false;

            if (!(obj instanceof android.hardware.radio.RadioManager.FmBandConfig))
                return false;

            android.hardware.radio.RadioManager.FmBandConfig other = ((android.hardware.radio.RadioManager.FmBandConfig) (obj));
            if (mStereo != other.mStereo)
                return false;

            if (mRds != other.mRds)
                return false;

            if (mTa != other.mTa)
                return false;

            if (mAf != other.mAf)
                return false;

            if (mEa != other.mEa)
                return false;

            return true;
        }

        /**
         * Builder class for {@link FmBandConfig} objects.
         */
        public static class Builder {
            private final android.hardware.radio.RadioManager.BandDescriptor mDescriptor;

            private boolean mStereo;

            private boolean mRds;

            private boolean mTa;

            private boolean mAf;

            private boolean mEa;

            /**
             * Constructs a new Builder with the defaults from an {@link FmBandDescriptor} .
             *
             * @param descriptor
             * 		the FmBandDescriptor defaults are read from .
             */
            public Builder(android.hardware.radio.RadioManager.FmBandDescriptor descriptor) {
                mDescriptor = new android.hardware.radio.RadioManager.BandDescriptor(descriptor.getRegion(), descriptor.getType(), descriptor.getLowerLimit(), descriptor.getUpperLimit(), descriptor.getSpacing());
                mStereo = descriptor.isStereoSupported();
                mRds = descriptor.isRdsSupported();
                mTa = descriptor.isTaSupported();
                mAf = descriptor.isAfSupported();
                mEa = descriptor.isEaSupported();
            }

            /**
             * Constructs a new Builder from a given {@link FmBandConfig}
             *
             * @param config
             * 		the FmBandConfig object whose data will be reused in the new Builder.
             */
            public Builder(android.hardware.radio.RadioManager.FmBandConfig config) {
                mDescriptor = new android.hardware.radio.RadioManager.BandDescriptor(config.getRegion(), config.getType(), config.getLowerLimit(), config.getUpperLimit(), config.getSpacing());
                mStereo = config.getStereo();
                mRds = config.getRds();
                mTa = config.getTa();
                mAf = config.getAf();
                mEa = config.getEa();
            }

            /**
             * Combines all of the parameters that have been set and return a new
             * {@link FmBandConfig} object.
             *
             * @return a new {@link FmBandConfig} object
             */
            public android.hardware.radio.RadioManager.FmBandConfig build() {
                android.hardware.radio.RadioManager.FmBandConfig config = new android.hardware.radio.RadioManager.FmBandConfig(mDescriptor.getRegion(), mDescriptor.getType(), mDescriptor.getLowerLimit(), mDescriptor.getUpperLimit(), mDescriptor.getSpacing(), mStereo, mRds, mTa, mAf, mEa);
                return config;
            }

            /**
             * Set stereo enable state
             *
             * @param state
             * 		The new enable state.
             * @return the same Builder instance.
             */
            public android.hardware.radio.RadioManager.FmBandConfig.Builder setStereo(boolean state) {
                mStereo = state;
                return this;
            }

            /**
             * Set RDS or RBDS(if region is ITU2) enable state
             *
             * @param state
             * 		The new enable state.
             * @return the same Builder instance.
             */
            public android.hardware.radio.RadioManager.FmBandConfig.Builder setRds(boolean state) {
                mRds = state;
                return this;
            }

            /**
             * Set Traffic announcement enable state
             *
             * @param state
             * 		The new enable state.
             * @return the same Builder instance.
             */
            public android.hardware.radio.RadioManager.FmBandConfig.Builder setTa(boolean state) {
                mTa = state;
                return this;
            }

            /**
             * Set Alternate Frequency Switching enable state
             *
             * @param state
             * 		The new enable state.
             * @return the same Builder instance.
             */
            public android.hardware.radio.RadioManager.FmBandConfig.Builder setAf(boolean state) {
                mAf = state;
                return this;
            }

            /**
             * Set Emergency Announcement enable state
             *
             * @param state
             * 		The new enable state.
             * @return the same Builder instance.
             */
            public android.hardware.radio.RadioManager.FmBandConfig.Builder setEa(boolean state) {
                mEa = state;
                return this;
            }
        }
    }

    /**
     * AM band configuration.
     *
     * @see #BAND_AM
     */
    public static class AmBandConfig extends android.hardware.radio.RadioManager.BandConfig {
        private final boolean mStereo;

        AmBandConfig(android.hardware.radio.RadioManager.AmBandDescriptor descriptor) {
            super(((android.hardware.radio.RadioManager.BandDescriptor) (descriptor)));
            mStereo = descriptor.isStereoSupported();
        }

        AmBandConfig(int region, int type, int lowerLimit, int upperLimit, int spacing, boolean stereo) {
            super(region, type, lowerLimit, upperLimit, spacing);
            mStereo = stereo;
        }

        /**
         * Get stereo enable state
         *
         * @return the enable state.
         */
        public boolean getStereo() {
            return mStereo;
        }

        private AmBandConfig(android.os.Parcel in) {
            super(in);
            mStereo = in.readByte() == 1;
        }

        public static final android.os.Parcelable.Creator<android.hardware.radio.RadioManager.AmBandConfig> CREATOR = new android.os.Parcelable.Creator<android.hardware.radio.RadioManager.AmBandConfig>() {
            public android.hardware.radio.RadioManager.AmBandConfig createFromParcel(android.os.Parcel in) {
                return new android.hardware.radio.RadioManager.AmBandConfig(in);
            }

            public android.hardware.radio.RadioManager.AmBandConfig[] newArray(int size) {
                return new android.hardware.radio.RadioManager.AmBandConfig[size];
            }
        };

        @java.lang.Override
        public void writeToParcel(android.os.Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeByte(((byte) (mStereo ? 1 : 0)));
        }

        @java.lang.Override
        public int describeContents() {
            return 0;
        }

        @java.lang.Override
        public java.lang.String toString() {
            return ((("AmBandConfig [" + super.toString()) + ", mStereo=") + mStereo) + "]";
        }

        @java.lang.Override
        public int hashCode() {
            final int prime = 31;
            int result = super.hashCode();
            result = (prime * result) + (mStereo ? 1 : 0);
            return result;
        }

        @java.lang.Override
        public boolean equals(java.lang.Object obj) {
            if (this == obj)
                return true;

            if (!super.equals(obj))
                return false;

            if (!(obj instanceof android.hardware.radio.RadioManager.AmBandConfig))
                return false;

            android.hardware.radio.RadioManager.AmBandConfig other = ((android.hardware.radio.RadioManager.AmBandConfig) (obj));
            if (mStereo != other.getStereo())
                return false;

            return true;
        }

        /**
         * Builder class for {@link AmBandConfig} objects.
         */
        public static class Builder {
            private final android.hardware.radio.RadioManager.BandDescriptor mDescriptor;

            private boolean mStereo;

            /**
             * Constructs a new Builder with the defaults from an {@link AmBandDescriptor} .
             *
             * @param descriptor
             * 		the FmBandDescriptor defaults are read from .
             */
            public Builder(android.hardware.radio.RadioManager.AmBandDescriptor descriptor) {
                mDescriptor = new android.hardware.radio.RadioManager.BandDescriptor(descriptor.getRegion(), descriptor.getType(), descriptor.getLowerLimit(), descriptor.getUpperLimit(), descriptor.getSpacing());
                mStereo = descriptor.isStereoSupported();
            }

            /**
             * Constructs a new Builder from a given {@link AmBandConfig}
             *
             * @param config
             * 		the FmBandConfig object whose data will be reused in the new Builder.
             */
            public Builder(android.hardware.radio.RadioManager.AmBandConfig config) {
                mDescriptor = new android.hardware.radio.RadioManager.BandDescriptor(config.getRegion(), config.getType(), config.getLowerLimit(), config.getUpperLimit(), config.getSpacing());
                mStereo = config.getStereo();
            }

            /**
             * Combines all of the parameters that have been set and return a new
             * {@link AmBandConfig} object.
             *
             * @return a new {@link AmBandConfig} object
             */
            public android.hardware.radio.RadioManager.AmBandConfig build() {
                android.hardware.radio.RadioManager.AmBandConfig config = new android.hardware.radio.RadioManager.AmBandConfig(mDescriptor.getRegion(), mDescriptor.getType(), mDescriptor.getLowerLimit(), mDescriptor.getUpperLimit(), mDescriptor.getSpacing(), mStereo);
                return config;
            }

            /**
             * Set stereo enable state
             *
             * @param state
             * 		The new enable state.
             * @return the same Builder instance.
             */
            public android.hardware.radio.RadioManager.AmBandConfig.Builder setStereo(boolean state) {
                mStereo = state;
                return this;
            }
        }
    }

    /**
     * Radio program information returned by
     * {@link RadioTuner#getProgramInformation(RadioManager.ProgramInfo[])}
     */
    public static class ProgramInfo implements android.os.Parcelable {
        private final int mChannel;

        private final int mSubChannel;

        private final boolean mTuned;

        private final boolean mStereo;

        private final boolean mDigital;

        private final int mSignalStrength;

        private final android.hardware.radio.RadioMetadata mMetadata;

        ProgramInfo(int channel, int subChannel, boolean tuned, boolean stereo, boolean digital, int signalStrength, android.hardware.radio.RadioMetadata metadata) {
            mChannel = channel;
            mSubChannel = subChannel;
            mTuned = tuned;
            mStereo = stereo;
            mDigital = digital;
            mSignalStrength = signalStrength;
            mMetadata = metadata;
        }

        /**
         * Main channel expressed in units according to band type.
         * Currently all defined band types express channels as frequency in kHz
         *
         * @return the program channel
         */
        public int getChannel() {
            return mChannel;
        }

        /**
         * Sub channel ID. E.g 1 for HD radio HD1
         *
         * @return the program sub channel
         */
        public int getSubChannel() {
            return mSubChannel;
        }

        /**
         * {@code true} if the tuner is currently tuned on a valid station
         *
         * @return {@code true} if currently tuned, {@code false} otherwise.
         */
        public boolean isTuned() {
            return mTuned;
        }

        /**
         * {@code true} if the received program is stereo
         *
         * @return {@code true} if stereo, {@code false} otherwise.
         */
        public boolean isStereo() {
            return mStereo;
        }

        /**
         * {@code true} if the received program is digital (e.g HD radio)
         *
         * @return {@code true} if digital, {@code false} otherwise.
         */
        public boolean isDigital() {
            return mDigital;
        }

        /**
         * Signal strength indicator from 0 (no signal) to 100 (excellent)
         *
         * @return the signal strength indication.
         */
        public int getSignalStrength() {
            return mSignalStrength;
        }

        /**
         * Metadata currently received from this station.
         * null if no metadata have been received
         *
         * @return current meta data received from this program.
         */
        public android.hardware.radio.RadioMetadata getMetadata() {
            return mMetadata;
        }

        private ProgramInfo(android.os.Parcel in) {
            mChannel = in.readInt();
            mSubChannel = in.readInt();
            mTuned = in.readByte() == 1;
            mStereo = in.readByte() == 1;
            mDigital = in.readByte() == 1;
            mSignalStrength = in.readInt();
            if (in.readByte() == 1) {
                mMetadata = android.hardware.radio.RadioMetadata.CREATOR.createFromParcel(in);
            } else {
                mMetadata = null;
            }
        }

        public static final android.os.Parcelable.Creator<android.hardware.radio.RadioManager.ProgramInfo> CREATOR = new android.os.Parcelable.Creator<android.hardware.radio.RadioManager.ProgramInfo>() {
            public android.hardware.radio.RadioManager.ProgramInfo createFromParcel(android.os.Parcel in) {
                return new android.hardware.radio.RadioManager.ProgramInfo(in);
            }

            public android.hardware.radio.RadioManager.ProgramInfo[] newArray(int size) {
                return new android.hardware.radio.RadioManager.ProgramInfo[size];
            }
        };

        @java.lang.Override
        public void writeToParcel(android.os.Parcel dest, int flags) {
            dest.writeInt(mChannel);
            dest.writeInt(mSubChannel);
            dest.writeByte(((byte) (mTuned ? 1 : 0)));
            dest.writeByte(((byte) (mStereo ? 1 : 0)));
            dest.writeByte(((byte) (mDigital ? 1 : 0)));
            dest.writeInt(mSignalStrength);
            if (mMetadata == null) {
                dest.writeByte(((byte) (0)));
            } else {
                dest.writeByte(((byte) (1)));
                mMetadata.writeToParcel(dest, flags);
            }
        }

        @java.lang.Override
        public int describeContents() {
            return 0;
        }

        @java.lang.Override
        public java.lang.String toString() {
            return (((((((((((("ProgramInfo [mChannel=" + mChannel) + ", mSubChannel=") + mSubChannel) + ", mTuned=") + mTuned) + ", mStereo=") + mStereo) + ", mDigital=") + mDigital) + ", mSignalStrength=") + mSignalStrength) + (mMetadata == null ? "" : ", mMetadata=" + mMetadata.toString())) + "]";
        }

        @java.lang.Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = (prime * result) + mChannel;
            result = (prime * result) + mSubChannel;
            result = (prime * result) + (mTuned ? 1 : 0);
            result = (prime * result) + (mStereo ? 1 : 0);
            result = (prime * result) + (mDigital ? 1 : 0);
            result = (prime * result) + mSignalStrength;
            result = (prime * result) + (mMetadata == null ? 0 : mMetadata.hashCode());
            return result;
        }

        @java.lang.Override
        public boolean equals(java.lang.Object obj) {
            if (this == obj)
                return true;

            if (!(obj instanceof android.hardware.radio.RadioManager.ProgramInfo))
                return false;

            android.hardware.radio.RadioManager.ProgramInfo other = ((android.hardware.radio.RadioManager.ProgramInfo) (obj));
            if (mChannel != other.getChannel())
                return false;

            if (mSubChannel != other.getSubChannel())
                return false;

            if (mTuned != other.isTuned())
                return false;

            if (mStereo != other.isStereo())
                return false;

            if (mDigital != other.isDigital())
                return false;

            if (mSignalStrength != other.getSignalStrength())
                return false;

            if (mMetadata == null) {
                if (other.getMetadata() != null)
                    return false;

            } else
                if (!mMetadata.equals(other.getMetadata()))
                    return false;


            return true;
        }
    }

    /**
     * Returns a list of descriptors for all broadcast radio modules present on the device.
     *
     * @param modules
     * 		An List of {@link ModuleProperties} where the list will be returned.
     * @return <ul>
    <li>{@link #STATUS_OK} in case of success, </li>
    <li>{@link #STATUS_ERROR} in case of unspecified error, </li>
    <li>{@link #STATUS_NO_INIT} if the native service cannot be reached, </li>
    <li>{@link #STATUS_BAD_VALUE} if modules is null, </li>
    <li>{@link #STATUS_DEAD_OBJECT} if the binder transaction to the native service fails, </li>
    </ul>
     */
    public native int listModules(java.util.List<android.hardware.radio.RadioManager.ModuleProperties> modules);

    /**
     * Open an interface to control a tuner on a given broadcast radio module.
     * Optionally selects and applies the configuration passed as "config" argument.
     *
     * @param moduleId
     * 		radio module identifier {@link ModuleProperties#getId()}. Mandatory.
     * @param config
     * 		desired band and configuration to apply when enabling the hardware module.
     * 		optional, can be null.
     * @param withAudio
     * 		{@code true} to request a tuner with an audio source.
     * 		This tuner is intended for live listening or recording or a radio program.
     * 		If {@code false}, the tuner can only be used to retrieve program informations.
     * @param callback
     * 		{@link RadioTuner.Callback} interface. Mandatory.
     * @param handler
     * 		the Handler on which the callbacks will be received.
     * 		Can be null if default handler is OK.
     * @return a valid {@link RadioTuner} interface in case of success or null in case of error.
     */
    public android.hardware.radio.RadioTuner openTuner(int moduleId, android.hardware.radio.RadioManager.BandConfig config, boolean withAudio, android.hardware.radio.RadioTuner.Callback callback, android.os.Handler handler) {
        if (callback == null) {
            return null;
        }
        android.hardware.radio.RadioModule module = new android.hardware.radio.RadioModule(moduleId, config, withAudio, callback, handler);
        if (module != null) {
            if (!module.initCheck()) {
                module = null;
            }
        }
        return ((android.hardware.radio.RadioTuner) (module));
    }

    private final android.content.Context mContext;

    /**
     *
     *
     * @unknown 
     */
    public RadioManager(android.content.Context context) {
        mContext = context;
    }
}

