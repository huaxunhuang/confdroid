/**
 * Copyright (C) 2020 The Android Open Source Project
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
package android.view;


/**
 * Base class for verified events.
 * Verified events contain the subset of an InputEvent that the system can verify.
 * Data contained inside VerifiedInputEvent's should be considered trusted and contain only
 * the original event data that first came from the system.
 *
 * @see android.hardware.input.InputManager#verifyInputEvent(InputEvent)
 */
@android.annotation.SuppressLint("ParcelNotFinal")
public abstract class VerifiedInputEvent implements android.os.Parcelable {
    private static final java.lang.String TAG = "VerifiedInputEvent";

    /**
     *
     *
     * @unknown 
     */
    protected static final int VERIFIED_KEY = 1;

    /**
     *
     *
     * @unknown 
     */
    protected static final int VERIFIED_MOTION = 2;

    /**
     *
     *
     * @unknown 
     */
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    @android.annotation.IntDef(prefix = "VERIFIED", value = { android.view.VerifiedInputEvent.VERIFIED_KEY, android.view.VerifiedInputEvent.VERIFIED_MOTION })
    public @interface VerifiedInputEventType {}

    @android.view.VerifiedInputEvent.VerifiedInputEventType
    private int mType;

    private int mDeviceId;

    private long mEventTimeNanos;

    private int mSource;

    private int mDisplayId;

    /**
     *
     *
     * @unknown 
     */
    protected VerifiedInputEvent(int type, int deviceId, long eventTimeNanos, int source, int displayId) {
        mType = type;
        mDeviceId = deviceId;
        mEventTimeNanos = eventTimeNanos;
        mSource = source;
        mDisplayId = displayId;
    }

    /**
     *
     *
     * @unknown 
     */
    protected VerifiedInputEvent(@android.annotation.NonNull
    android.os.Parcel in, int expectedType) {
        mType = in.readInt();
        if (mType != expectedType) {
            throw new java.lang.IllegalArgumentException("Unexpected input event type token in parcel.");
        }
        mDeviceId = in.readInt();
        mEventTimeNanos = in.readLong();
        mSource = in.readInt();
        mDisplayId = in.readInt();
    }

    /**
     * Get the id of the device that generated this event.
     *
     * @see InputEvent#getDeviceId()
     */
    public int getDeviceId() {
        return mDeviceId;
    }

    /**
     * Get the time this event occurred, in the {@link android.os.SystemClock#uptimeMillis()}
     * time base.
     *
     * @see InputEvent#getEventTime()
     */
    @android.annotation.SuppressLint("MethodNameUnits")
    public long getEventTimeNanos() {
        return mEventTimeNanos;
    }

    /**
     * Get the source of the event.
     *
     * @see InputEvent#getSource()
     */
    public int getSource() {
        return mSource;
    }

    /**
     * Get the display id that is associated with this event.
     *
     * @see Display#getDisplayId()
     */
    public int getDisplayId() {
        return mDisplayId;
    }

    /**
     * {@inheritDoc }
     */
    @java.lang.Override
    public void writeToParcel(@android.annotation.NonNull
    android.os.Parcel dest, int flags) {
        dest.writeInt(mType);
        dest.writeInt(mDeviceId);
        dest.writeLong(mEventTimeNanos);
        dest.writeInt(mSource);
        dest.writeInt(mDisplayId);
    }

    /**
     * {@inheritDoc }
     */
    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    private static int peekInt(@android.annotation.NonNull
    android.os.Parcel parcel) {
        final int initialDataPosition = parcel.dataPosition();
        int data = parcel.readInt();
        parcel.setDataPosition(initialDataPosition);
        return data;
    }

    @android.annotation.NonNull
    public static final android.os.Parcelable.Creator<android.view.VerifiedInputEvent> CREATOR = new android.os.Parcelable.Creator<android.view.VerifiedInputEvent>() {
        @java.lang.Override
        public android.view.VerifiedInputEvent[] newArray(int size) {
            return new android.view.VerifiedInputEvent[size];
        }

        @java.lang.Override
        public android.view.VerifiedInputEvent createFromParcel(@android.annotation.NonNull
        android.os.Parcel in) {
            final int type = peekInt(in);
            if (type == android.view.VerifiedInputEvent.VERIFIED_KEY) {
                return VerifiedKeyEvent.CREATOR.createFromParcel(in);
            } else
                if (type == android.view.VerifiedInputEvent.VERIFIED_MOTION) {
                    return VerifiedMotionEvent.CREATOR.createFromParcel(in);
                }

            throw new java.lang.IllegalArgumentException("Unexpected input event type in parcel.");
        }
    };
}

