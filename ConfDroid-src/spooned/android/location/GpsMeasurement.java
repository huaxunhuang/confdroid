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
 * limitations under the License
 */
package android.location;


/**
 * A class representing a GPS satellite measurement, containing raw and computed information.
 *
 * @unknown 
 */
@android.annotation.SystemApi
public class GpsMeasurement implements android.os.Parcelable {
    private int mFlags;

    private byte mPrn;

    private double mTimeOffsetInNs;

    private short mState;

    private long mReceivedGpsTowInNs;

    private long mReceivedGpsTowUncertaintyInNs;

    private double mCn0InDbHz;

    private double mPseudorangeRateInMetersPerSec;

    private double mPseudorangeRateUncertaintyInMetersPerSec;

    private short mAccumulatedDeltaRangeState;

    private double mAccumulatedDeltaRangeInMeters;

    private double mAccumulatedDeltaRangeUncertaintyInMeters;

    private double mPseudorangeInMeters;

    private double mPseudorangeUncertaintyInMeters;

    private double mCodePhaseInChips;

    private double mCodePhaseUncertaintyInChips;

    private float mCarrierFrequencyInHz;

    private long mCarrierCycles;

    private double mCarrierPhase;

    private double mCarrierPhaseUncertainty;

    private byte mLossOfLock;

    private int mBitNumber;

    private short mTimeFromLastBitInMs;

    private double mDopplerShiftInHz;

    private double mDopplerShiftUncertaintyInHz;

    private byte mMultipathIndicator;

    private double mSnrInDb;

    private double mElevationInDeg;

    private double mElevationUncertaintyInDeg;

    private double mAzimuthInDeg;

    private double mAzimuthUncertaintyInDeg;

    private boolean mUsedInFix;

    // The following enumerations must be in sync with the values declared in gps.h
    private static final int HAS_NO_FLAGS = 0;

    private static final int HAS_SNR = 1 << 0;

    private static final int HAS_ELEVATION = 1 << 1;

    private static final int HAS_ELEVATION_UNCERTAINTY = 1 << 2;

    private static final int HAS_AZIMUTH = 1 << 3;

    private static final int HAS_AZIMUTH_UNCERTAINTY = 1 << 4;

    private static final int HAS_PSEUDORANGE = 1 << 5;

    private static final int HAS_PSEUDORANGE_UNCERTAINTY = 1 << 6;

    private static final int HAS_CODE_PHASE = 1 << 7;

    private static final int HAS_CODE_PHASE_UNCERTAINTY = 1 << 8;

    private static final int HAS_CARRIER_FREQUENCY = 1 << 9;

    private static final int HAS_CARRIER_CYCLES = 1 << 10;

    private static final int HAS_CARRIER_PHASE = 1 << 11;

    private static final int HAS_CARRIER_PHASE_UNCERTAINTY = 1 << 12;

    private static final int HAS_BIT_NUMBER = 1 << 13;

    private static final int HAS_TIME_FROM_LAST_BIT = 1 << 14;

    private static final int HAS_DOPPLER_SHIFT = 1 << 15;

    private static final int HAS_DOPPLER_SHIFT_UNCERTAINTY = 1 << 16;

    private static final int HAS_USED_IN_FIX = 1 << 17;

    private static final int GPS_MEASUREMENT_HAS_UNCORRECTED_PSEUDORANGE_RATE = 1 << 18;

    /**
     * The indicator is not available or it is unknown.
     */
    public static final byte LOSS_OF_LOCK_UNKNOWN = 0;

    /**
     * The measurement does not present any indication of 'loss of lock'.
     */
    public static final byte LOSS_OF_LOCK_OK = 1;

    /**
     * 'Loss of lock' detected between the previous and current observation: cycle slip possible.
     */
    public static final byte LOSS_OF_LOCK_CYCLE_SLIP = 2;

    /**
     * The indicator is not available or it is unknown.
     */
    public static final byte MULTIPATH_INDICATOR_UNKNOWN = 0;

    /**
     * The measurement has been indicated to use multi-path.
     */
    public static final byte MULTIPATH_INDICATOR_DETECTED = 1;

    /**
     * The measurement has been indicated not tu use multi-path.
     */
    public static final byte MULTIPATH_INDICATOR_NOT_USED = 2;

    /**
     * The state of GPS receiver the measurement is invalid or unknown.
     */
    public static final short STATE_UNKNOWN = 0;

    /**
     * The state of the GPS receiver is ranging code lock.
     */
    public static final short STATE_CODE_LOCK = 1 << 0;

    /**
     * The state of the GPS receiver is in bit sync.
     */
    public static final short STATE_BIT_SYNC = 1 << 1;

    /**
     * The state of the GPS receiver is in sub-frame sync.
     */
    public static final short STATE_SUBFRAME_SYNC = 1 << 2;

    /**
     * The state of the GPS receiver has TOW decoded.
     */
    public static final short STATE_TOW_DECODED = 1 << 3;

    /**
     * The state of the GPS receiver contains millisecond ambiguity.
     */
    public static final short STATE_MSEC_AMBIGUOUS = 1 << 4;

    /**
     * All the GPS receiver state flags.
     */
    private static final short STATE_ALL = (((android.location.GpsMeasurement.STATE_CODE_LOCK | android.location.GpsMeasurement.STATE_BIT_SYNC) | android.location.GpsMeasurement.STATE_SUBFRAME_SYNC) | android.location.GpsMeasurement.STATE_TOW_DECODED) | android.location.GpsMeasurement.STATE_MSEC_AMBIGUOUS;

    /**
     * The state of the 'Accumulated Delta Range' is invalid or unknown.
     */
    public static final short ADR_STATE_UNKNOWN = 0;

    /**
     * The state of the 'Accumulated Delta Range' is valid.
     */
    public static final short ADR_STATE_VALID = 1 << 0;

    /**
     * The state of the 'Accumulated Delta Range' has detected a reset.
     */
    public static final short ADR_STATE_RESET = 1 << 1;

    /**
     * The state of the 'Accumulated Delta Range' has a cycle slip detected.
     */
    public static final short ADR_STATE_CYCLE_SLIP = 1 << 2;

    /**
     * All the 'Accumulated Delta Range' flags.
     */
    private static final short ADR_ALL = (android.location.GpsMeasurement.ADR_STATE_VALID | android.location.GpsMeasurement.ADR_STATE_RESET) | android.location.GpsMeasurement.ADR_STATE_CYCLE_SLIP;

    // End enumerations in sync with gps.h
    GpsMeasurement() {
        initialize();
    }

    /**
     * Sets all contents to the values stored in the provided object.
     */
    public void set(android.location.GpsMeasurement measurement) {
        mFlags = measurement.mFlags;
        mPrn = measurement.mPrn;
        mTimeOffsetInNs = measurement.mTimeOffsetInNs;
        mState = measurement.mState;
        mReceivedGpsTowInNs = measurement.mReceivedGpsTowInNs;
        mReceivedGpsTowUncertaintyInNs = measurement.mReceivedGpsTowUncertaintyInNs;
        mCn0InDbHz = measurement.mCn0InDbHz;
        mPseudorangeRateInMetersPerSec = measurement.mPseudorangeRateInMetersPerSec;
        mPseudorangeRateUncertaintyInMetersPerSec = measurement.mPseudorangeRateUncertaintyInMetersPerSec;
        mAccumulatedDeltaRangeState = measurement.mAccumulatedDeltaRangeState;
        mAccumulatedDeltaRangeInMeters = measurement.mAccumulatedDeltaRangeInMeters;
        mAccumulatedDeltaRangeUncertaintyInMeters = measurement.mAccumulatedDeltaRangeUncertaintyInMeters;
        mPseudorangeInMeters = measurement.mPseudorangeInMeters;
        mPseudorangeUncertaintyInMeters = measurement.mPseudorangeUncertaintyInMeters;
        mCodePhaseInChips = measurement.mCodePhaseInChips;
        mCodePhaseUncertaintyInChips = measurement.mCodePhaseUncertaintyInChips;
        mCarrierFrequencyInHz = measurement.mCarrierFrequencyInHz;
        mCarrierCycles = measurement.mCarrierCycles;
        mCarrierPhase = measurement.mCarrierPhase;
        mCarrierPhaseUncertainty = measurement.mCarrierPhaseUncertainty;
        mLossOfLock = measurement.mLossOfLock;
        mBitNumber = measurement.mBitNumber;
        mTimeFromLastBitInMs = measurement.mTimeFromLastBitInMs;
        mDopplerShiftInHz = measurement.mDopplerShiftInHz;
        mDopplerShiftUncertaintyInHz = measurement.mDopplerShiftUncertaintyInHz;
        mMultipathIndicator = measurement.mMultipathIndicator;
        mSnrInDb = measurement.mSnrInDb;
        mElevationInDeg = measurement.mElevationInDeg;
        mElevationUncertaintyInDeg = measurement.mElevationUncertaintyInDeg;
        mAzimuthInDeg = measurement.mAzimuthInDeg;
        mAzimuthUncertaintyInDeg = measurement.mAzimuthUncertaintyInDeg;
        mUsedInFix = measurement.mUsedInFix;
    }

    /**
     * Resets all the contents to its original state.
     */
    public void reset() {
        initialize();
    }

    /**
     * Gets the Pseudo-random number (PRN).
     * Range: [1, 32]
     */
    public byte getPrn() {
        return mPrn;
    }

    /**
     * Sets the Pseud-random number (PRN).
     */
    public void setPrn(byte value) {
        mPrn = value;
    }

    /**
     * Gets the time offset at which the measurement was taken in nanoseconds.
     * The reference receiver's time is specified by {@link GpsClock#getTimeInNs()} and should be
     * interpreted in the same way as indicated by {@link GpsClock#getType()}.
     *
     * The sign of this value is given by the following equation:
     *      measurement time = time_ns + time_offset_ns
     *
     * The value provides an individual time-stamp for the measurement, and allows sub-nanosecond
     * accuracy.
     */
    public double getTimeOffsetInNs() {
        return mTimeOffsetInNs;
    }

    /**
     * Sets the time offset at which the measurement was taken in nanoseconds.
     */
    public void setTimeOffsetInNs(double value) {
        mTimeOffsetInNs = value;
    }

    /**
     * Gets per-satellite sync state.
     * It represents the current sync state for the associated satellite.
     *
     * This value helps interpret {@link #getReceivedGpsTowInNs()}.
     */
    public short getState() {
        return mState;
    }

    /**
     * Sets the sync state.
     */
    public void setState(short value) {
        mState = value;
    }

    /**
     * Gets a string representation of the 'sync state'.
     * For internal and logging use only.
     */
    private java.lang.String getStateString() {
        if (mState == android.location.GpsMeasurement.STATE_UNKNOWN) {
            return "Unknown";
        }
        java.lang.StringBuilder builder = new java.lang.StringBuilder();
        if ((mState & android.location.GpsMeasurement.STATE_CODE_LOCK) == android.location.GpsMeasurement.STATE_CODE_LOCK) {
            builder.append("CodeLock|");
        }
        if ((mState & android.location.GpsMeasurement.STATE_BIT_SYNC) == android.location.GpsMeasurement.STATE_BIT_SYNC) {
            builder.append("BitSync|");
        }
        if ((mState & android.location.GpsMeasurement.STATE_SUBFRAME_SYNC) == android.location.GpsMeasurement.STATE_SUBFRAME_SYNC) {
            builder.append("SubframeSync|");
        }
        if ((mState & android.location.GpsMeasurement.STATE_TOW_DECODED) == android.location.GpsMeasurement.STATE_TOW_DECODED) {
            builder.append("TowDecoded|");
        }
        if ((mState & android.location.GpsMeasurement.STATE_MSEC_AMBIGUOUS) == android.location.GpsMeasurement.STATE_MSEC_AMBIGUOUS) {
            builder.append("MsecAmbiguous");
        }
        int remainingStates = mState & (~android.location.GpsMeasurement.STATE_ALL);
        if (remainingStates > 0) {
            builder.append("Other(");
            builder.append(java.lang.Integer.toBinaryString(remainingStates));
            builder.append(")|");
        }
        builder.deleteCharAt(builder.length() - 1);
        return builder.toString();
    }

    /**
     * Gets the received GPS Time-of-Week at the measurement time, in nanoseconds.
     * The value is relative to the beginning of the current GPS week.
     *
     * Given {@link #getState()} of the GPS receiver, the range of this field can be:
     *      Searching           : [ 0           ]   : {@link #STATE_UNKNOWN} is set
     *      Ranging code lock   : [ 0    1 ms   ]   : {@link #STATE_CODE_LOCK} is set
     *      Bit sync            : [ 0   20 ms   ]   : {@link #STATE_BIT_SYNC} is set
     *      Subframe sync       : [ 0    6 ms   ]   : {@link #STATE_SUBFRAME_SYNC} is set
     *      TOW decoded         : [ 0    1 week ]   : {@link #STATE_TOW_DECODED} is set
     */
    public long getReceivedGpsTowInNs() {
        return mReceivedGpsTowInNs;
    }

    /**
     * Sets the received GPS time-of-week in nanoseconds.
     */
    public void setReceivedGpsTowInNs(long value) {
        mReceivedGpsTowInNs = value;
    }

    /**
     * Gets the received GPS time-of-week's uncertainty (1-Sigma) in nanoseconds.
     */
    public long getReceivedGpsTowUncertaintyInNs() {
        return mReceivedGpsTowUncertaintyInNs;
    }

    /**
     * Sets the received GPS time-of-week's uncertainty (1-Sigma) in nanoseconds.
     */
    public void setReceivedGpsTowUncertaintyInNs(long value) {
        mReceivedGpsTowUncertaintyInNs = value;
    }

    /**
     * Gets the Carrier-to-noise density in dB-Hz.
     * Range: [0, 63].
     *
     * The value contains the measured C/N0 for the signal at the antenna input.
     */
    public double getCn0InDbHz() {
        return mCn0InDbHz;
    }

    /**
     * Sets the carrier-to-noise density in dB-Hz.
     */
    public void setCn0InDbHz(double value) {
        mCn0InDbHz = value;
    }

    /**
     * Gets the Pseudorange rate at the timestamp in m/s.
     * The reported value includes {@link #getPseudorangeRateUncertaintyInMetersPerSec()}.
     *
     * The correction of a given Pseudorange Rate value includes corrections from receiver and
     * satellite clock frequency errors.
     * {@link #isPseudorangeRateCorrected()} identifies the type of value reported.
     *
     * A positive 'uncorrected' value indicates that the SV is moving away from the receiver.
     * The sign of the 'uncorrected' Pseudorange Rate and its relation to the sign of
     * {@link #getDopplerShiftInHz()} is given by the equation:
     *      pseudorange rate = -k * doppler shift   (where k is a constant)
     */
    public double getPseudorangeRateInMetersPerSec() {
        return mPseudorangeRateInMetersPerSec;
    }

    /**
     * Sets the pseudorange rate at the timestamp in m/s.
     */
    public void setPseudorangeRateInMetersPerSec(double value) {
        mPseudorangeRateInMetersPerSec = value;
    }

    /**
     * See {@link #getPseudorangeRateInMetersPerSec()} for more details.
     *
     * @return {@code true} if {@link #getPseudorangeRateInMetersPerSec()} contains a corrected
    value, {@code false} if it contains an uncorrected value.
     */
    public boolean isPseudorangeRateCorrected() {
        return !isFlagSet(android.location.GpsMeasurement.GPS_MEASUREMENT_HAS_UNCORRECTED_PSEUDORANGE_RATE);
    }

    /**
     * Gets the pseudorange's rate uncertainty (1-Sigma) in m/s.
     * The uncertainty is represented as an absolute (single sided) value.
     */
    public double getPseudorangeRateUncertaintyInMetersPerSec() {
        return mPseudorangeRateUncertaintyInMetersPerSec;
    }

    /**
     * Sets the pseudorange's rate uncertainty (1-Sigma) in m/s.
     */
    public void setPseudorangeRateUncertaintyInMetersPerSec(double value) {
        mPseudorangeRateUncertaintyInMetersPerSec = value;
    }

    /**
     * Gets 'Accumulated Delta Range' state.
     * It indicates whether {@link #getAccumulatedDeltaRangeInMeters()} is reset or there is a
     * cycle slip (indicating 'loss of lock').
     */
    public short getAccumulatedDeltaRangeState() {
        return mAccumulatedDeltaRangeState;
    }

    /**
     * Sets the 'Accumulated Delta Range' state.
     */
    public void setAccumulatedDeltaRangeState(short value) {
        mAccumulatedDeltaRangeState = value;
    }

    /**
     * Gets a string representation of the 'Accumulated Delta Range state'.
     * For internal and logging use only.
     */
    private java.lang.String getAccumulatedDeltaRangeStateString() {
        if (mAccumulatedDeltaRangeState == android.location.GpsMeasurement.ADR_STATE_UNKNOWN) {
            return "Unknown";
        }
        java.lang.StringBuilder builder = new java.lang.StringBuilder();
        if ((mAccumulatedDeltaRangeState & android.location.GpsMeasurement.ADR_STATE_VALID) == android.location.GpsMeasurement.ADR_STATE_VALID) {
            builder.append("Valid|");
        }
        if ((mAccumulatedDeltaRangeState & android.location.GpsMeasurement.ADR_STATE_RESET) == android.location.GpsMeasurement.ADR_STATE_RESET) {
            builder.append("Reset|");
        }
        if ((mAccumulatedDeltaRangeState & android.location.GpsMeasurement.ADR_STATE_CYCLE_SLIP) == android.location.GpsMeasurement.ADR_STATE_CYCLE_SLIP) {
            builder.append("CycleSlip|");
        }
        int remainingStates = mAccumulatedDeltaRangeState & (~android.location.GpsMeasurement.ADR_ALL);
        if (remainingStates > 0) {
            builder.append("Other(");
            builder.append(java.lang.Integer.toBinaryString(remainingStates));
            builder.append(")|");
        }
        builder.deleteCharAt(builder.length() - 1);
        return builder.toString();
    }

    /**
     * Gets the accumulated delta range since the last channel reset, in meters.
     * The reported value includes {@link #getAccumulatedDeltaRangeUncertaintyInMeters()}.
     *
     * The availability of the value is represented by {@link #getAccumulatedDeltaRangeState()}.
     *
     * A positive value indicates that the SV is moving away from the receiver.
     * The sign of {@link #getAccumulatedDeltaRangeInMeters()} and its relation to the sign of
     * {@link #getCarrierPhase()} is given by the equation:
     *          accumulated delta range = -k * carrier phase    (where k is a constant)
     */
    public double getAccumulatedDeltaRangeInMeters() {
        return mAccumulatedDeltaRangeInMeters;
    }

    /**
     * Sets the accumulated delta range in meters.
     */
    public void setAccumulatedDeltaRangeInMeters(double value) {
        mAccumulatedDeltaRangeInMeters = value;
    }

    /**
     * Gets the accumulated delta range's uncertainty (1-Sigma) in meters.
     * The uncertainty is represented as an absolute (single sided) value.
     *
     * The status of the value is represented by {@link #getAccumulatedDeltaRangeState()}.
     */
    public double getAccumulatedDeltaRangeUncertaintyInMeters() {
        return mAccumulatedDeltaRangeUncertaintyInMeters;
    }

    /**
     * Sets the accumulated delta range's uncertainty (1-sigma) in meters.
     *
     * The status of the value is represented by {@link #getAccumulatedDeltaRangeState()}.
     */
    public void setAccumulatedDeltaRangeUncertaintyInMeters(double value) {
        mAccumulatedDeltaRangeUncertaintyInMeters = value;
    }

    /**
     * Returns true if {@link #getPseudorangeInMeters()} is available, false otherwise.
     */
    public boolean hasPseudorangeInMeters() {
        return isFlagSet(android.location.GpsMeasurement.HAS_PSEUDORANGE);
    }

    /**
     * Gets the best derived pseudorange by the chipset, in meters.
     * The reported pseudorange includes {@link #getPseudorangeUncertaintyInMeters()}.
     *
     * The value is only available if {@link #hasPseudorangeInMeters()} is true.
     */
    public double getPseudorangeInMeters() {
        return mPseudorangeInMeters;
    }

    /**
     * Sets the Pseudo-range in meters.
     */
    public void setPseudorangeInMeters(double value) {
        setFlag(android.location.GpsMeasurement.HAS_PSEUDORANGE);
        mPseudorangeInMeters = value;
    }

    /**
     * Resets the Pseudo-range in meters.
     */
    public void resetPseudorangeInMeters() {
        resetFlag(android.location.GpsMeasurement.HAS_PSEUDORANGE);
        mPseudorangeInMeters = java.lang.Double.NaN;
    }

    /**
     * Returns true if {@link #getPseudorangeUncertaintyInMeters()} is available, false otherwise.
     */
    public boolean hasPseudorangeUncertaintyInMeters() {
        return isFlagSet(android.location.GpsMeasurement.HAS_PSEUDORANGE_UNCERTAINTY);
    }

    /**
     * Gets the pseudorange's uncertainty (1-Sigma) in meters.
     * The value contains the 'pseudorange' and 'clock' uncertainty in it.
     * The uncertainty is represented as an absolute (single sided) value.
     *
     * The value is only available if {@link #hasPseudorangeUncertaintyInMeters()} is true.
     */
    public double getPseudorangeUncertaintyInMeters() {
        return mPseudorangeUncertaintyInMeters;
    }

    /**
     * Sets the pseudo-range's uncertainty (1-Sigma) in meters.
     */
    public void setPseudorangeUncertaintyInMeters(double value) {
        setFlag(android.location.GpsMeasurement.HAS_PSEUDORANGE_UNCERTAINTY);
        mPseudorangeUncertaintyInMeters = value;
    }

    /**
     * Resets the pseudo-range's uncertainty (1-Sigma) in meters.
     */
    public void resetPseudorangeUncertaintyInMeters() {
        resetFlag(android.location.GpsMeasurement.HAS_PSEUDORANGE_UNCERTAINTY);
        mPseudorangeUncertaintyInMeters = java.lang.Double.NaN;
    }

    /**
     * Returns true if {@link #getCodePhaseInChips()} is available, false otherwise.
     */
    public boolean hasCodePhaseInChips() {
        return isFlagSet(android.location.GpsMeasurement.HAS_CODE_PHASE);
    }

    /**
     * Gets the fraction of the current C/A code cycle.
     * Range: [0, 1023]
     * The reference frequency is given by the value of {@link #getCarrierFrequencyInHz()}.
     * The reported code-phase includes {@link #getCodePhaseUncertaintyInChips()}.
     *
     * The value is only available if {@link #hasCodePhaseInChips()} is true.
     */
    public double getCodePhaseInChips() {
        return mCodePhaseInChips;
    }

    /**
     * Sets the Code-phase in chips.
     */
    public void setCodePhaseInChips(double value) {
        setFlag(android.location.GpsMeasurement.HAS_CODE_PHASE);
        mCodePhaseInChips = value;
    }

    /**
     * Resets the Code-phase in chips.
     */
    public void resetCodePhaseInChips() {
        resetFlag(android.location.GpsMeasurement.HAS_CODE_PHASE);
        mCodePhaseInChips = java.lang.Double.NaN;
    }

    /**
     * Returns true if {@link #getCodePhaseUncertaintyInChips()} is available, false otherwise.
     */
    public boolean hasCodePhaseUncertaintyInChips() {
        return isFlagSet(android.location.GpsMeasurement.HAS_CODE_PHASE_UNCERTAINTY);
    }

    /**
     * Gets the code-phase's uncertainty (1-Sigma) as a fraction of chips.
     * The uncertainty is represented as an absolute (single sided) value.
     *
     * The value is only available if {@link #hasCodePhaseUncertaintyInChips()} is true.
     */
    public double getCodePhaseUncertaintyInChips() {
        return mCodePhaseUncertaintyInChips;
    }

    /**
     * Sets the Code-phase's uncertainty (1-Sigma) in fractions of chips.
     */
    public void setCodePhaseUncertaintyInChips(double value) {
        setFlag(android.location.GpsMeasurement.HAS_CODE_PHASE_UNCERTAINTY);
        mCodePhaseUncertaintyInChips = value;
    }

    /**
     * Resets the Code-phase's uncertainty (1-Sigma) in fractions of chips.
     */
    public void resetCodePhaseUncertaintyInChips() {
        resetFlag(android.location.GpsMeasurement.HAS_CODE_PHASE_UNCERTAINTY);
        mCodePhaseUncertaintyInChips = java.lang.Double.NaN;
    }

    /**
     * Returns true if {@link #getCarrierFrequencyInHz()} is available, false otherwise.
     */
    public boolean hasCarrierFrequencyInHz() {
        return isFlagSet(android.location.GpsMeasurement.HAS_CARRIER_FREQUENCY);
    }

    /**
     * Gets the carrier frequency at which codes and messages are modulated, it can be L1 or L2.
     * If the field is not set, the carrier frequency corresponds to L1.
     *
     * The value is only available if {@link #hasCarrierFrequencyInHz()} is true.
     */
    public float getCarrierFrequencyInHz() {
        return mCarrierFrequencyInHz;
    }

    /**
     * Sets the Carrier frequency (L1 or L2) in Hz.
     */
    public void setCarrierFrequencyInHz(float carrierFrequencyInHz) {
        setFlag(android.location.GpsMeasurement.HAS_CARRIER_FREQUENCY);
        mCarrierFrequencyInHz = carrierFrequencyInHz;
    }

    /**
     * Resets the Carrier frequency (L1 or L2) in Hz.
     */
    public void resetCarrierFrequencyInHz() {
        resetFlag(android.location.GpsMeasurement.HAS_CARRIER_FREQUENCY);
        mCarrierFrequencyInHz = java.lang.Float.NaN;
    }

    /**
     * Returns true if {@link #getCarrierCycles()} is available, false otherwise.
     */
    public boolean hasCarrierCycles() {
        return isFlagSet(android.location.GpsMeasurement.HAS_CARRIER_CYCLES);
    }

    /**
     * The number of full carrier cycles between the satellite and the receiver.
     * The reference frequency is given by the value of {@link #getCarrierFrequencyInHz()}.
     *
     * The value is only available if {@link #hasCarrierCycles()} is true.
     */
    public long getCarrierCycles() {
        return mCarrierCycles;
    }

    /**
     * Sets the number of full carrier cycles between the satellite and the receiver.
     */
    public void setCarrierCycles(long value) {
        setFlag(android.location.GpsMeasurement.HAS_CARRIER_CYCLES);
        mCarrierCycles = value;
    }

    /**
     * Resets the number of full carrier cycles between the satellite and the receiver.
     */
    public void resetCarrierCycles() {
        resetFlag(android.location.GpsMeasurement.HAS_CARRIER_CYCLES);
        mCarrierCycles = java.lang.Long.MIN_VALUE;
    }

    /**
     * Returns true if {@link #getCarrierPhase()} is available, false otherwise.
     */
    public boolean hasCarrierPhase() {
        return isFlagSet(android.location.GpsMeasurement.HAS_CARRIER_PHASE);
    }

    /**
     * Gets the RF phase detected by the receiver.
     * Range: [0.0, 1.0].
     * This is usually the fractional part of the complete carrier phase measurement.
     *
     * The reference frequency is given by the value of {@link #getCarrierFrequencyInHz()}.
     * The reported carrier-phase includes {@link #getCarrierPhaseUncertainty()}.
     *
     * The value is only available if {@link #hasCarrierPhase()} is true.
     */
    public double getCarrierPhase() {
        return mCarrierPhase;
    }

    /**
     * Sets the RF phase detected by the receiver.
     */
    public void setCarrierPhase(double value) {
        setFlag(android.location.GpsMeasurement.HAS_CARRIER_PHASE);
        mCarrierPhase = value;
    }

    /**
     * Resets the RF phase detected by the receiver.
     */
    public void resetCarrierPhase() {
        resetFlag(android.location.GpsMeasurement.HAS_CARRIER_PHASE);
        mCarrierPhase = java.lang.Double.NaN;
    }

    /**
     * Returns true if {@link #getCarrierPhaseUncertainty()} is available, false otherwise.
     */
    public boolean hasCarrierPhaseUncertainty() {
        return isFlagSet(android.location.GpsMeasurement.HAS_CARRIER_PHASE_UNCERTAINTY);
    }

    /**
     * Gets the carrier-phase's uncertainty (1-Sigma).
     * The uncertainty is represented as an absolute (single sided) value.
     *
     * The value is only available if {@link #hasCarrierPhaseUncertainty()} is true.
     */
    public double getCarrierPhaseUncertainty() {
        return mCarrierPhaseUncertainty;
    }

    /**
     * Sets the Carrier-phase's uncertainty (1-Sigma) in cycles.
     */
    public void setCarrierPhaseUncertainty(double value) {
        setFlag(android.location.GpsMeasurement.HAS_CARRIER_PHASE_UNCERTAINTY);
        mCarrierPhaseUncertainty = value;
    }

    /**
     * Resets the Carrier-phase's uncertainty (1-Sigma) in cycles.
     */
    public void resetCarrierPhaseUncertainty() {
        resetFlag(android.location.GpsMeasurement.HAS_CARRIER_PHASE_UNCERTAINTY);
        mCarrierPhaseUncertainty = java.lang.Double.NaN;
    }

    /**
     * Gets a value indicating the 'loss of lock' state of the event.
     */
    public byte getLossOfLock() {
        return mLossOfLock;
    }

    /**
     * Sets the 'loss of lock' status.
     */
    public void setLossOfLock(byte value) {
        mLossOfLock = value;
    }

    /**
     * Gets a string representation of the 'loss of lock'.
     * For internal and logging use only.
     */
    private java.lang.String getLossOfLockString() {
        switch (mLossOfLock) {
            case android.location.GpsMeasurement.LOSS_OF_LOCK_UNKNOWN :
                return "Unknown";
            case android.location.GpsMeasurement.LOSS_OF_LOCK_OK :
                return "Ok";
            case android.location.GpsMeasurement.LOSS_OF_LOCK_CYCLE_SLIP :
                return "CycleSlip";
            default :
                return ("<Invalid:" + mLossOfLock) + ">";
        }
    }

    /**
     * Returns true if {@link #getBitNumber()} is available, false otherwise.
     */
    public boolean hasBitNumber() {
        return isFlagSet(android.location.GpsMeasurement.HAS_BIT_NUMBER);
    }

    /**
     * Gets the number of GPS bits transmitted since Sat-Sun midnight (GPS week).
     *
     * The value is only available if {@link #hasBitNumber()} is true.
     */
    public int getBitNumber() {
        return mBitNumber;
    }

    /**
     * Sets the bit number within the broadcast frame.
     */
    public void setBitNumber(int bitNumber) {
        setFlag(android.location.GpsMeasurement.HAS_BIT_NUMBER);
        mBitNumber = bitNumber;
    }

    /**
     * Resets the bit number within the broadcast frame.
     */
    public void resetBitNumber() {
        resetFlag(android.location.GpsMeasurement.HAS_BIT_NUMBER);
        mBitNumber = java.lang.Integer.MIN_VALUE;
    }

    /**
     * Returns true if {@link #getTimeFromLastBitInMs()} is available, false otherwise.
     */
    public boolean hasTimeFromLastBitInMs() {
        return isFlagSet(android.location.GpsMeasurement.HAS_TIME_FROM_LAST_BIT);
    }

    /**
     * Gets the elapsed time since the last received bit in milliseconds.
     * Range: [0, 20].
     *
     * The value is only available if {@link #hasTimeFromLastBitInMs()} is true.
     */
    public short getTimeFromLastBitInMs() {
        return mTimeFromLastBitInMs;
    }

    /**
     * Sets the elapsed time since the last received bit in milliseconds.
     */
    public void setTimeFromLastBitInMs(short value) {
        setFlag(android.location.GpsMeasurement.HAS_TIME_FROM_LAST_BIT);
        mTimeFromLastBitInMs = value;
    }

    /**
     * Resets the elapsed time since the last received bit in milliseconds.
     */
    public void resetTimeFromLastBitInMs() {
        resetFlag(android.location.GpsMeasurement.HAS_TIME_FROM_LAST_BIT);
        mTimeFromLastBitInMs = java.lang.Short.MIN_VALUE;
    }

    /**
     * Returns true if {@link #getDopplerShiftInHz()} is available, false otherwise.
     */
    public boolean hasDopplerShiftInHz() {
        return isFlagSet(android.location.GpsMeasurement.HAS_DOPPLER_SHIFT);
    }

    /**
     * Gets the Doppler Shift in Hz.
     * A positive value indicates that the SV is moving toward the receiver.
     *
     * The reference frequency is given by the value of {@link #getCarrierFrequencyInHz()}.
     * The reported doppler shift includes {@link #getDopplerShiftUncertaintyInHz()}.
     *
     * The value is only available if {@link #hasDopplerShiftInHz()} is true.
     */
    public double getDopplerShiftInHz() {
        return mDopplerShiftInHz;
    }

    /**
     * Sets the Doppler shift in Hz.
     */
    public void setDopplerShiftInHz(double value) {
        setFlag(android.location.GpsMeasurement.HAS_DOPPLER_SHIFT);
        mDopplerShiftInHz = value;
    }

    /**
     * Resets the Doppler shift in Hz.
     */
    public void resetDopplerShiftInHz() {
        resetFlag(android.location.GpsMeasurement.HAS_DOPPLER_SHIFT);
        mDopplerShiftInHz = java.lang.Double.NaN;
    }

    /**
     * Returns true if {@link #getDopplerShiftUncertaintyInHz()} is available, false otherwise.
     */
    public boolean hasDopplerShiftUncertaintyInHz() {
        return isFlagSet(android.location.GpsMeasurement.HAS_DOPPLER_SHIFT_UNCERTAINTY);
    }

    /**
     * Gets the Doppler's Shift uncertainty (1-Sigma) in Hz.
     * The uncertainty is represented as an absolute (single sided) value.
     *
     * The value is only available if {@link #hasDopplerShiftUncertaintyInHz()} is true.
     */
    public double getDopplerShiftUncertaintyInHz() {
        return mDopplerShiftUncertaintyInHz;
    }

    /**
     * Sets the Doppler's shift uncertainty (1-Sigma) in Hz.
     */
    public void setDopplerShiftUncertaintyInHz(double value) {
        setFlag(android.location.GpsMeasurement.HAS_DOPPLER_SHIFT_UNCERTAINTY);
        mDopplerShiftUncertaintyInHz = value;
    }

    /**
     * Resets the Doppler's shift uncertainty (1-Sigma) in Hz.
     */
    public void resetDopplerShiftUncertaintyInHz() {
        resetFlag(android.location.GpsMeasurement.HAS_DOPPLER_SHIFT_UNCERTAINTY);
        mDopplerShiftUncertaintyInHz = java.lang.Double.NaN;
    }

    /**
     * Gets a value indicating the 'multipath' state of the event.
     */
    public byte getMultipathIndicator() {
        return mMultipathIndicator;
    }

    /**
     * Sets the 'multi-path' indicator.
     */
    public void setMultipathIndicator(byte value) {
        mMultipathIndicator = value;
    }

    /**
     * Gets a string representation of the 'multi-path indicator'.
     * For internal and logging use only.
     */
    private java.lang.String getMultipathIndicatorString() {
        switch (mMultipathIndicator) {
            case android.location.GpsMeasurement.MULTIPATH_INDICATOR_UNKNOWN :
                return "Unknown";
            case android.location.GpsMeasurement.MULTIPATH_INDICATOR_DETECTED :
                return "Detected";
            case android.location.GpsMeasurement.MULTIPATH_INDICATOR_NOT_USED :
                return "NotUsed";
            default :
                return ("<Invalid:" + mMultipathIndicator) + ">";
        }
    }

    /**
     * Returns true if {@link #getSnrInDb()} is available, false otherwise.
     */
    public boolean hasSnrInDb() {
        return isFlagSet(android.location.GpsMeasurement.HAS_SNR);
    }

    /**
     * Gets the Signal-to-Noise ratio (SNR) in dB.
     *
     * The value is only available if {@link #hasSnrInDb()} is true.
     */
    public double getSnrInDb() {
        return mSnrInDb;
    }

    /**
     * Sets the Signal-to-noise ratio (SNR) in dB.
     */
    public void setSnrInDb(double snrInDb) {
        setFlag(android.location.GpsMeasurement.HAS_SNR);
        mSnrInDb = snrInDb;
    }

    /**
     * Resets the Signal-to-noise ratio (SNR) in dB.
     */
    public void resetSnrInDb() {
        resetFlag(android.location.GpsMeasurement.HAS_SNR);
        mSnrInDb = java.lang.Double.NaN;
    }

    /**
     * Returns true if {@link #getElevationInDeg()} is available, false otherwise.
     */
    public boolean hasElevationInDeg() {
        return isFlagSet(android.location.GpsMeasurement.HAS_ELEVATION);
    }

    /**
     * Gets the Elevation in degrees.
     * Range: [-90, 90]
     * The reported elevation includes {@link #getElevationUncertaintyInDeg()}.
     *
     * The value is only available if {@link #hasElevationInDeg()} is true.
     */
    public double getElevationInDeg() {
        return mElevationInDeg;
    }

    /**
     * Sets the Elevation in degrees.
     */
    public void setElevationInDeg(double elevationInDeg) {
        setFlag(android.location.GpsMeasurement.HAS_ELEVATION);
        mElevationInDeg = elevationInDeg;
    }

    /**
     * Resets the Elevation in degrees.
     */
    public void resetElevationInDeg() {
        resetFlag(android.location.GpsMeasurement.HAS_ELEVATION);
        mElevationInDeg = java.lang.Double.NaN;
    }

    /**
     * Returns true if {@link #getElevationUncertaintyInDeg()} is available, false otherwise.
     */
    public boolean hasElevationUncertaintyInDeg() {
        return isFlagSet(android.location.GpsMeasurement.HAS_ELEVATION_UNCERTAINTY);
    }

    /**
     * Gets the elevation's uncertainty (1-Sigma) in degrees.
     * Range: [0, 90]
     *
     * The uncertainty is represented as an absolute (single sided) value.
     *
     * The value is only available if {@link #hasElevationUncertaintyInDeg()} is true.
     */
    public double getElevationUncertaintyInDeg() {
        return mElevationUncertaintyInDeg;
    }

    /**
     * Sets the elevation's uncertainty (1-Sigma) in degrees.
     */
    public void setElevationUncertaintyInDeg(double value) {
        setFlag(android.location.GpsMeasurement.HAS_ELEVATION_UNCERTAINTY);
        mElevationUncertaintyInDeg = value;
    }

    /**
     * Resets the elevation's uncertainty (1-Sigma) in degrees.
     */
    public void resetElevationUncertaintyInDeg() {
        resetFlag(android.location.GpsMeasurement.HAS_ELEVATION_UNCERTAINTY);
        mElevationUncertaintyInDeg = java.lang.Double.NaN;
    }

    /**
     * Returns true if {@link #getAzimuthInDeg()} is available, false otherwise.
     */
    public boolean hasAzimuthInDeg() {
        return isFlagSet(android.location.GpsMeasurement.HAS_AZIMUTH);
    }

    /**
     * Gets the azimuth in degrees.
     * Range: [0, 360).
     *
     * The reported azimuth includes {@link #getAzimuthUncertaintyInDeg()}.
     *
     * The value is only available if {@link #hasAzimuthInDeg()} is true.
     */
    public double getAzimuthInDeg() {
        return mAzimuthInDeg;
    }

    /**
     * Sets the Azimuth in degrees.
     */
    public void setAzimuthInDeg(double value) {
        setFlag(android.location.GpsMeasurement.HAS_AZIMUTH);
        mAzimuthInDeg = value;
    }

    /**
     * Resets the Azimuth in degrees.
     */
    public void resetAzimuthInDeg() {
        resetFlag(android.location.GpsMeasurement.HAS_AZIMUTH);
        mAzimuthInDeg = java.lang.Double.NaN;
    }

    /**
     * Returns true if {@link #getAzimuthUncertaintyInDeg()} is available, false otherwise.
     */
    public boolean hasAzimuthUncertaintyInDeg() {
        return isFlagSet(android.location.GpsMeasurement.HAS_AZIMUTH_UNCERTAINTY);
    }

    /**
     * Gets the azimuth's uncertainty (1-Sigma) in degrees.
     * Range: [0, 180].
     *
     * The uncertainty is represented as an absolute (single sided) value.
     *
     * The value is only available if {@link #hasAzimuthUncertaintyInDeg()} is true.
     */
    public double getAzimuthUncertaintyInDeg() {
        return mAzimuthUncertaintyInDeg;
    }

    /**
     * Sets the Azimuth's uncertainty (1-Sigma) in degrees.
     */
    public void setAzimuthUncertaintyInDeg(double value) {
        setFlag(android.location.GpsMeasurement.HAS_AZIMUTH_UNCERTAINTY);
        mAzimuthUncertaintyInDeg = value;
    }

    /**
     * Resets the Azimuth's uncertainty (1-Sigma) in degrees.
     */
    public void resetAzimuthUncertaintyInDeg() {
        resetFlag(android.location.GpsMeasurement.HAS_AZIMUTH_UNCERTAINTY);
        mAzimuthUncertaintyInDeg = java.lang.Double.NaN;
    }

    /**
     * Gets a flag indicating whether the GPS represented by the measurement was used for computing
     * the most recent fix.
     *
     * @return A non-null value if the data is available, null otherwise.
     */
    public boolean isUsedInFix() {
        return mUsedInFix;
    }

    /**
     * Sets the Used-in-Fix flag.
     */
    public void setUsedInFix(boolean value) {
        mUsedInFix = value;
    }

    public static final android.os.Parcelable.Creator<android.location.GpsMeasurement> CREATOR = new android.os.Parcelable.Creator<android.location.GpsMeasurement>() {
        @java.lang.Override
        public android.location.GpsMeasurement createFromParcel(android.os.Parcel parcel) {
            android.location.GpsMeasurement gpsMeasurement = new android.location.GpsMeasurement();
            gpsMeasurement.mFlags = parcel.readInt();
            gpsMeasurement.mPrn = parcel.readByte();
            gpsMeasurement.mTimeOffsetInNs = parcel.readDouble();
            gpsMeasurement.mState = ((short) (parcel.readInt()));
            gpsMeasurement.mReceivedGpsTowInNs = parcel.readLong();
            gpsMeasurement.mReceivedGpsTowUncertaintyInNs = parcel.readLong();
            gpsMeasurement.mCn0InDbHz = parcel.readDouble();
            gpsMeasurement.mPseudorangeRateInMetersPerSec = parcel.readDouble();
            gpsMeasurement.mPseudorangeRateUncertaintyInMetersPerSec = parcel.readDouble();
            gpsMeasurement.mAccumulatedDeltaRangeState = ((short) (parcel.readInt()));
            gpsMeasurement.mAccumulatedDeltaRangeInMeters = parcel.readDouble();
            gpsMeasurement.mAccumulatedDeltaRangeUncertaintyInMeters = parcel.readDouble();
            gpsMeasurement.mPseudorangeInMeters = parcel.readDouble();
            gpsMeasurement.mPseudorangeUncertaintyInMeters = parcel.readDouble();
            gpsMeasurement.mCodePhaseInChips = parcel.readDouble();
            gpsMeasurement.mCodePhaseUncertaintyInChips = parcel.readDouble();
            gpsMeasurement.mCarrierFrequencyInHz = parcel.readFloat();
            gpsMeasurement.mCarrierCycles = parcel.readLong();
            gpsMeasurement.mCarrierPhase = parcel.readDouble();
            gpsMeasurement.mCarrierPhaseUncertainty = parcel.readDouble();
            gpsMeasurement.mLossOfLock = parcel.readByte();
            gpsMeasurement.mBitNumber = parcel.readInt();
            gpsMeasurement.mTimeFromLastBitInMs = ((short) (parcel.readInt()));
            gpsMeasurement.mDopplerShiftInHz = parcel.readDouble();
            gpsMeasurement.mDopplerShiftUncertaintyInHz = parcel.readDouble();
            gpsMeasurement.mMultipathIndicator = parcel.readByte();
            gpsMeasurement.mSnrInDb = parcel.readDouble();
            gpsMeasurement.mElevationInDeg = parcel.readDouble();
            gpsMeasurement.mElevationUncertaintyInDeg = parcel.readDouble();
            gpsMeasurement.mAzimuthInDeg = parcel.readDouble();
            gpsMeasurement.mAzimuthUncertaintyInDeg = parcel.readDouble();
            gpsMeasurement.mUsedInFix = parcel.readInt() != 0;
            return gpsMeasurement;
        }

        @java.lang.Override
        public android.location.GpsMeasurement[] newArray(int i) {
            return new android.location.GpsMeasurement[i];
        }
    };

    public void writeToParcel(android.os.Parcel parcel, int flags) {
        parcel.writeInt(mFlags);
        parcel.writeByte(mPrn);
        parcel.writeDouble(mTimeOffsetInNs);
        parcel.writeInt(mState);
        parcel.writeLong(mReceivedGpsTowInNs);
        parcel.writeLong(mReceivedGpsTowUncertaintyInNs);
        parcel.writeDouble(mCn0InDbHz);
        parcel.writeDouble(mPseudorangeRateInMetersPerSec);
        parcel.writeDouble(mPseudorangeRateUncertaintyInMetersPerSec);
        parcel.writeInt(mAccumulatedDeltaRangeState);
        parcel.writeDouble(mAccumulatedDeltaRangeInMeters);
        parcel.writeDouble(mAccumulatedDeltaRangeUncertaintyInMeters);
        parcel.writeDouble(mPseudorangeInMeters);
        parcel.writeDouble(mPseudorangeUncertaintyInMeters);
        parcel.writeDouble(mCodePhaseInChips);
        parcel.writeDouble(mCodePhaseUncertaintyInChips);
        parcel.writeFloat(mCarrierFrequencyInHz);
        parcel.writeLong(mCarrierCycles);
        parcel.writeDouble(mCarrierPhase);
        parcel.writeDouble(mCarrierPhaseUncertainty);
        parcel.writeByte(mLossOfLock);
        parcel.writeInt(mBitNumber);
        parcel.writeInt(mTimeFromLastBitInMs);
        parcel.writeDouble(mDopplerShiftInHz);
        parcel.writeDouble(mDopplerShiftUncertaintyInHz);
        parcel.writeByte(mMultipathIndicator);
        parcel.writeDouble(mSnrInDb);
        parcel.writeDouble(mElevationInDeg);
        parcel.writeDouble(mElevationUncertaintyInDeg);
        parcel.writeDouble(mAzimuthInDeg);
        parcel.writeDouble(mAzimuthUncertaintyInDeg);
        parcel.writeInt(mUsedInFix ? 1 : 0);
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public java.lang.String toString() {
        final java.lang.String format = "   %-29s = %s\n";
        final java.lang.String formatWithUncertainty = "   %-29s = %-25s   %-40s = %s\n";
        java.lang.StringBuilder builder = new java.lang.StringBuilder("GpsMeasurement:\n");
        builder.append(java.lang.String.format(format, "Prn", mPrn));
        builder.append(java.lang.String.format(format, "TimeOffsetInNs", mTimeOffsetInNs));
        builder.append(java.lang.String.format(format, "State", getStateString()));
        builder.append(java.lang.String.format(formatWithUncertainty, "ReceivedGpsTowInNs", mReceivedGpsTowInNs, "ReceivedGpsTowUncertaintyInNs", mReceivedGpsTowUncertaintyInNs));
        builder.append(java.lang.String.format(format, "Cn0InDbHz", mCn0InDbHz));
        builder.append(java.lang.String.format(formatWithUncertainty, "PseudorangeRateInMetersPerSec", mPseudorangeRateInMetersPerSec, "PseudorangeRateUncertaintyInMetersPerSec", mPseudorangeRateUncertaintyInMetersPerSec));
        builder.append(java.lang.String.format(format, "PseudorangeRateIsCorrected", isPseudorangeRateCorrected()));
        builder.append(java.lang.String.format(format, "AccumulatedDeltaRangeState", getAccumulatedDeltaRangeStateString()));
        builder.append(java.lang.String.format(formatWithUncertainty, "AccumulatedDeltaRangeInMeters", mAccumulatedDeltaRangeInMeters, "AccumulatedDeltaRangeUncertaintyInMeters", mAccumulatedDeltaRangeUncertaintyInMeters));
        builder.append(java.lang.String.format(formatWithUncertainty, "PseudorangeInMeters", hasPseudorangeInMeters() ? mPseudorangeInMeters : null, "PseudorangeUncertaintyInMeters", hasPseudorangeUncertaintyInMeters() ? mPseudorangeUncertaintyInMeters : null));
        builder.append(java.lang.String.format(formatWithUncertainty, "CodePhaseInChips", hasCodePhaseInChips() ? mCodePhaseInChips : null, "CodePhaseUncertaintyInChips", hasCodePhaseUncertaintyInChips() ? mCodePhaseUncertaintyInChips : null));
        builder.append(java.lang.String.format(format, "CarrierFrequencyInHz", hasCarrierFrequencyInHz() ? mCarrierFrequencyInHz : null));
        builder.append(java.lang.String.format(format, "CarrierCycles", hasCarrierCycles() ? mCarrierCycles : null));
        builder.append(java.lang.String.format(formatWithUncertainty, "CarrierPhase", hasCarrierPhase() ? mCarrierPhase : null, "CarrierPhaseUncertainty", hasCarrierPhaseUncertainty() ? mCarrierPhaseUncertainty : null));
        builder.append(java.lang.String.format(format, "LossOfLock", getLossOfLockString()));
        builder.append(java.lang.String.format(format, "BitNumber", hasBitNumber() ? mBitNumber : null));
        builder.append(java.lang.String.format(format, "TimeFromLastBitInMs", hasTimeFromLastBitInMs() ? mTimeFromLastBitInMs : null));
        builder.append(java.lang.String.format(formatWithUncertainty, "DopplerShiftInHz", hasDopplerShiftInHz() ? mDopplerShiftInHz : null, "DopplerShiftUncertaintyInHz", hasDopplerShiftUncertaintyInHz() ? mDopplerShiftUncertaintyInHz : null));
        builder.append(java.lang.String.format(format, "MultipathIndicator", getMultipathIndicatorString()));
        builder.append(java.lang.String.format(format, "SnrInDb", hasSnrInDb() ? mSnrInDb : null));
        builder.append(java.lang.String.format(formatWithUncertainty, "ElevationInDeg", hasElevationInDeg() ? mElevationInDeg : null, "ElevationUncertaintyInDeg", hasElevationUncertaintyInDeg() ? mElevationUncertaintyInDeg : null));
        builder.append(java.lang.String.format(formatWithUncertainty, "AzimuthInDeg", hasAzimuthInDeg() ? mAzimuthInDeg : null, "AzimuthUncertaintyInDeg", hasAzimuthUncertaintyInDeg() ? mAzimuthUncertaintyInDeg : null));
        builder.append(java.lang.String.format(format, "UsedInFix", mUsedInFix));
        return builder.toString();
    }

    private void initialize() {
        mFlags = android.location.GpsMeasurement.HAS_NO_FLAGS;
        setPrn(java.lang.Byte.MIN_VALUE);
        setTimeOffsetInNs(java.lang.Long.MIN_VALUE);
        setState(android.location.GpsMeasurement.STATE_UNKNOWN);
        setReceivedGpsTowInNs(java.lang.Long.MIN_VALUE);
        setReceivedGpsTowUncertaintyInNs(java.lang.Long.MAX_VALUE);
        setCn0InDbHz(java.lang.Double.MIN_VALUE);
        setPseudorangeRateInMetersPerSec(java.lang.Double.MIN_VALUE);
        setPseudorangeRateUncertaintyInMetersPerSec(java.lang.Double.MIN_VALUE);
        setAccumulatedDeltaRangeState(android.location.GpsMeasurement.ADR_STATE_UNKNOWN);
        setAccumulatedDeltaRangeInMeters(java.lang.Double.MIN_VALUE);
        setAccumulatedDeltaRangeUncertaintyInMeters(java.lang.Double.MIN_VALUE);
        resetPseudorangeInMeters();
        resetPseudorangeUncertaintyInMeters();
        resetCodePhaseInChips();
        resetCodePhaseUncertaintyInChips();
        resetCarrierFrequencyInHz();
        resetCarrierCycles();
        resetCarrierPhase();
        resetCarrierPhaseUncertainty();
        setLossOfLock(android.location.GpsMeasurement.LOSS_OF_LOCK_UNKNOWN);
        resetBitNumber();
        resetTimeFromLastBitInMs();
        resetDopplerShiftInHz();
        resetDopplerShiftUncertaintyInHz();
        setMultipathIndicator(android.location.GpsMeasurement.MULTIPATH_INDICATOR_UNKNOWN);
        resetSnrInDb();
        resetElevationInDeg();
        resetElevationUncertaintyInDeg();
        resetAzimuthInDeg();
        resetAzimuthUncertaintyInDeg();
        setUsedInFix(false);
    }

    private void setFlag(int flag) {
        mFlags |= flag;
    }

    private void resetFlag(int flag) {
        mFlags &= ~flag;
    }

    private boolean isFlagSet(int flag) {
        return (mFlags & flag) == flag;
    }
}

