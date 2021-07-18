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
package android.support.v4.view;


/**
 * Helper for accessing features in {@link MotionEvent} introduced
 * after API level 4 in a backwards compatible fashion.
 */
public final class MotionEventCompat {
    /**
     * Interface for the full API.
     */
    interface MotionEventVersionImpl {
        float getAxisValue(android.view.MotionEvent event, int axis);

        float getAxisValue(android.view.MotionEvent event, int axis, int pointerIndex);

        int getButtonState(android.view.MotionEvent event);
    }

    /**
     * Interface implementation that doesn't use anything about v4 APIs.
     */
    static class BaseMotionEventVersionImpl implements android.support.v4.view.MotionEventCompat.MotionEventVersionImpl {
        @java.lang.Override
        public float getAxisValue(android.view.MotionEvent event, int axis) {
            return 0;
        }

        @java.lang.Override
        public float getAxisValue(android.view.MotionEvent event, int axis, int pointerIndex) {
            return 0;
        }

        @java.lang.Override
        public int getButtonState(android.view.MotionEvent event) {
            return 0;
        }
    }

    /**
     * Interface implementation for devices with at least v12 APIs.
     */
    static class HoneycombMr1MotionEventVersionImpl extends android.support.v4.view.MotionEventCompat.BaseMotionEventVersionImpl {
        @java.lang.Override
        public float getAxisValue(android.view.MotionEvent event, int axis) {
            return android.support.v4.view.MotionEventCompatHoneycombMr1.getAxisValue(event, axis);
        }

        @java.lang.Override
        public float getAxisValue(android.view.MotionEvent event, int axis, int pointerIndex) {
            return android.support.v4.view.MotionEventCompatHoneycombMr1.getAxisValue(event, axis, pointerIndex);
        }
    }

    /**
     * Interface implementation for devices with at least v14 APIs.
     */
    private static class ICSMotionEventVersionImpl extends android.support.v4.view.MotionEventCompat.HoneycombMr1MotionEventVersionImpl {
        ICSMotionEventVersionImpl() {
        }

        @java.lang.Override
        public int getButtonState(android.view.MotionEvent event) {
            return android.support.v4.view.MotionEventCompatICS.getButtonState(event);
        }
    }

    /**
     * Select the correct implementation to use for the current platform.
     */
    static final android.support.v4.view.MotionEventCompat.MotionEventVersionImpl IMPL;

    static {
        if (android.os.Build.VERSION.SDK_INT >= 14) {
            IMPL = new android.support.v4.view.MotionEventCompat.ICSMotionEventVersionImpl();
        } else
            if (android.os.Build.VERSION.SDK_INT >= 12) {
                IMPL = new android.support.v4.view.MotionEventCompat.HoneycombMr1MotionEventVersionImpl();
            } else {
                IMPL = new android.support.v4.view.MotionEventCompat.BaseMotionEventVersionImpl();
            }

    }

    // -------------------------------------------------------------------
    /**
     * Synonym for {@link MotionEvent#ACTION_MASK}.
     */
    public static final int ACTION_MASK = 0xff;

    /**
     * Synonym for {@link MotionEvent#ACTION_POINTER_DOWN}.
     */
    public static final int ACTION_POINTER_DOWN = 5;

    /**
     * Synonym for {@link MotionEvent#ACTION_POINTER_UP}.
     */
    public static final int ACTION_POINTER_UP = 6;

    /**
     * Synonym for {@link MotionEvent#ACTION_HOVER_MOVE}.
     */
    public static final int ACTION_HOVER_MOVE = 7;

    /**
     * Synonym for {@link MotionEvent#ACTION_SCROLL}.
     */
    public static final int ACTION_SCROLL = 8;

    /**
     * Synonym for {@link MotionEvent#ACTION_POINTER_INDEX_MASK}.
     */
    public static final int ACTION_POINTER_INDEX_MASK = 0xff00;

    /**
     * Synonym for {@link MotionEvent#ACTION_POINTER_INDEX_SHIFT}.
     */
    public static final int ACTION_POINTER_INDEX_SHIFT = 8;

    /**
     * Synonym for {@link MotionEvent#ACTION_HOVER_ENTER}.
     */
    public static final int ACTION_HOVER_ENTER = 9;

    /**
     * Synonym for {@link MotionEvent#ACTION_HOVER_EXIT}.
     */
    public static final int ACTION_HOVER_EXIT = 10;

    /**
     * Synonym for {@link MotionEvent#AXIS_X}.
     */
    public static final int AXIS_X = 0;

    /**
     * Synonym for {@link MotionEvent#AXIS_Y}.
     */
    public static final int AXIS_Y = 1;

    /**
     * Synonym for {@link MotionEvent#AXIS_PRESSURE}.
     */
    public static final int AXIS_PRESSURE = 2;

    /**
     * Synonym for {@link MotionEvent#AXIS_SIZE}.
     */
    public static final int AXIS_SIZE = 3;

    /**
     * Synonym for {@link MotionEvent#AXIS_TOUCH_MAJOR}.
     */
    public static final int AXIS_TOUCH_MAJOR = 4;

    /**
     * Synonym for {@link MotionEvent#AXIS_TOUCH_MINOR}.
     */
    public static final int AXIS_TOUCH_MINOR = 5;

    /**
     * Synonym for {@link MotionEvent#AXIS_TOOL_MAJOR}.
     */
    public static final int AXIS_TOOL_MAJOR = 6;

    /**
     * Synonym for {@link MotionEvent#AXIS_TOOL_MINOR}.
     */
    public static final int AXIS_TOOL_MINOR = 7;

    /**
     * Synonym for {@link MotionEvent#AXIS_ORIENTATION}.
     */
    public static final int AXIS_ORIENTATION = 8;

    /**
     * Synonym for {@link MotionEvent#AXIS_VSCROLL}.
     */
    public static final int AXIS_VSCROLL = 9;

    /**
     * Synonym for {@link MotionEvent#AXIS_HSCROLL}.
     */
    public static final int AXIS_HSCROLL = 10;

    /**
     * Synonym for {@link MotionEvent#AXIS_Z}.
     */
    public static final int AXIS_Z = 11;

    /**
     * Synonym for {@link MotionEvent#AXIS_RX}.
     */
    public static final int AXIS_RX = 12;

    /**
     * Synonym for {@link MotionEvent#AXIS_RY}.
     */
    public static final int AXIS_RY = 13;

    /**
     * Synonym for {@link MotionEvent#AXIS_RZ}.
     */
    public static final int AXIS_RZ = 14;

    /**
     * Synonym for {@link MotionEvent#AXIS_HAT_X}.
     */
    public static final int AXIS_HAT_X = 15;

    /**
     * Synonym for {@link MotionEvent#AXIS_HAT_Y}.
     */
    public static final int AXIS_HAT_Y = 16;

    /**
     * Synonym for {@link MotionEvent#AXIS_LTRIGGER}.
     */
    public static final int AXIS_LTRIGGER = 17;

    /**
     * Synonym for {@link MotionEvent#AXIS_RTRIGGER}.
     */
    public static final int AXIS_RTRIGGER = 18;

    /**
     * Synonym for {@link MotionEvent#AXIS_THROTTLE}.
     */
    public static final int AXIS_THROTTLE = 19;

    /**
     * Synonym for {@link MotionEvent#AXIS_RUDDER}.
     */
    public static final int AXIS_RUDDER = 20;

    /**
     * Synonym for {@link MotionEvent#AXIS_WHEEL}.
     */
    public static final int AXIS_WHEEL = 21;

    /**
     * Synonym for {@link MotionEvent#AXIS_GAS}.
     */
    public static final int AXIS_GAS = 22;

    /**
     * Synonym for {@link MotionEvent#AXIS_BRAKE}.
     */
    public static final int AXIS_BRAKE = 23;

    /**
     * Synonym for {@link MotionEvent#AXIS_DISTANCE}.
     */
    public static final int AXIS_DISTANCE = 24;

    /**
     * Synonym for {@link MotionEvent#AXIS_TILT}.
     */
    public static final int AXIS_TILT = 25;

    /**
     * Synonym for {@link MotionEvent#AXIS_RELATIVE_X}.
     */
    public static final int AXIS_RELATIVE_X = 27;

    /**
     * Synonym for {@link MotionEvent#AXIS_RELATIVE_Y}.
     */
    public static final int AXIS_RELATIVE_Y = 28;

    /**
     * Synonym for {@link MotionEvent#AXIS_GENERIC_1}.
     */
    public static final int AXIS_GENERIC_1 = 32;

    /**
     * Synonym for {@link MotionEvent#AXIS_GENERIC_2}.
     */
    public static final int AXIS_GENERIC_2 = 33;

    /**
     * Synonym for {@link MotionEvent#AXIS_GENERIC_3}.
     */
    public static final int AXIS_GENERIC_3 = 34;

    /**
     * Synonym for {@link MotionEvent#AXIS_GENERIC_4}.
     */
    public static final int AXIS_GENERIC_4 = 35;

    /**
     * Synonym for {@link MotionEvent#AXIS_GENERIC_5}.
     */
    public static final int AXIS_GENERIC_5 = 36;

    /**
     * Synonym for {@link MotionEvent#AXIS_GENERIC_6}.
     */
    public static final int AXIS_GENERIC_6 = 37;

    /**
     * Synonym for {@link MotionEvent#AXIS_GENERIC_7}.
     */
    public static final int AXIS_GENERIC_7 = 38;

    /**
     * Synonym for {@link MotionEvent#AXIS_GENERIC_8}.
     */
    public static final int AXIS_GENERIC_8 = 39;

    /**
     * Synonym for {@link MotionEvent#AXIS_GENERIC_9}.
     */
    public static final int AXIS_GENERIC_9 = 40;

    /**
     * Synonym for {@link MotionEvent#AXIS_GENERIC_10}.
     */
    public static final int AXIS_GENERIC_10 = 41;

    /**
     * Synonym for {@link MotionEvent#AXIS_GENERIC_11}.
     */
    public static final int AXIS_GENERIC_11 = 42;

    /**
     * Synonym for {@link MotionEvent#AXIS_GENERIC_12}.
     */
    public static final int AXIS_GENERIC_12 = 43;

    /**
     * Synonym for {@link MotionEvent#AXIS_GENERIC_13}.
     */
    public static final int AXIS_GENERIC_13 = 44;

    /**
     * Synonym for {@link MotionEvent#AXIS_GENERIC_14}.
     */
    public static final int AXIS_GENERIC_14 = 45;

    /**
     * Synonym for {@link MotionEvent#AXIS_GENERIC_15}.
     */
    public static final int AXIS_GENERIC_15 = 46;

    /**
     * Synonym for {@link MotionEvent#AXIS_GENERIC_16}.
     */
    public static final int AXIS_GENERIC_16 = 47;

    /**
     * Synonym for {@link MotionEvent#BUTTON_PRIMARY}.
     */
    public static final int BUTTON_PRIMARY = 1;

    /**
     * Call {@link MotionEvent#getAction}, returning only the {@link #ACTION_MASK}
     * portion.
     */
    public static int getActionMasked(android.view.MotionEvent event) {
        return event.getAction() & android.support.v4.view.MotionEventCompat.ACTION_MASK;
    }

    /**
     * Call {@link MotionEvent#getAction}, returning only the pointer index
     * portion
     */
    public static int getActionIndex(android.view.MotionEvent event) {
        return (event.getAction() & android.support.v4.view.MotionEventCompat.ACTION_POINTER_INDEX_MASK) >> android.support.v4.view.MotionEventCompat.ACTION_POINTER_INDEX_SHIFT;
    }

    /**
     * Call {@link MotionEvent#findPointerIndex(int)}.
     *
     * @deprecated Call {@link MotionEvent#findPointerIndex(int)} directly. This method will be
    removed in a future release.
     */
    @java.lang.Deprecated
    public static int findPointerIndex(android.view.MotionEvent event, int pointerId) {
        return event.findPointerIndex(pointerId);
    }

    /**
     * Call {@link MotionEvent#getPointerId(int)}.
     *
     * @deprecated Call {@link MotionEvent#getPointerId(int)} directly. This method will be
    removed in a future release.
     */
    @java.lang.Deprecated
    public static int getPointerId(android.view.MotionEvent event, int pointerIndex) {
        return event.getPointerId(pointerIndex);
    }

    /**
     * Call {@link MotionEvent#getX(int)}.
     *
     * @deprecated Call {@link MotionEvent#getX()} directly. This method will be
    removed in a future release.
     */
    @java.lang.Deprecated
    public static float getX(android.view.MotionEvent event, int pointerIndex) {
        return event.getX(pointerIndex);
    }

    /**
     * Call {@link MotionEvent#getY(int)}.
     *
     * @deprecated Call {@link MotionEvent#getY()} directly. This method will be
    removed in a future release.
     */
    @java.lang.Deprecated
    public static float getY(android.view.MotionEvent event, int pointerIndex) {
        return event.getY(pointerIndex);
    }

    /**
     * The number of pointers of data contained in this event.  Always
     *
     * @deprecated Call {@link MotionEvent#getPointerCount()} directly. This method will be
    removed in a future release.
     */
    @java.lang.Deprecated
    public static int getPointerCount(android.view.MotionEvent event) {
        return event.getPointerCount();
    }

    /**
     * Gets the source of the event.
     *
     * @return The event source or {@link InputDeviceCompat#SOURCE_UNKNOWN} if unknown.
     * @deprecated Call {@link MotionEvent#getSource()} directly. This method will be
    removed in a future release.
     */
    @java.lang.Deprecated
    public static int getSource(android.view.MotionEvent event) {
        return event.getSource();
    }

    /**
     * Determines whether the event is from the given source.
     *
     * @param source
     * 		The input source to check against.
     * @return Whether the event is from the given source.
     */
    public static boolean isFromSource(android.view.MotionEvent event, int source) {
        return (event.getSource() & source) == source;
    }

    /**
     * Get axis value for the first pointer index (may be an
     * arbitrary pointer identifier).
     *
     * @param axis
     * 		The axis identifier for the axis value to retrieve.
     * @see #AXIS_X
     * @see #AXIS_Y
     */
    public static float getAxisValue(android.view.MotionEvent event, int axis) {
        return android.support.v4.view.MotionEventCompat.IMPL.getAxisValue(event, axis);
    }

    /**
     * Returns the value of the requested axis for the given pointer <em>index</em>
     * (use {@link #getPointerId(MotionEvent, int)} to find the pointer identifier for this index).
     *
     * @param axis
     * 		The axis identifier for the axis value to retrieve.
     * @param pointerIndex
     * 		Raw index of pointer to retrieve.  Value may be from 0
     * 		(the first pointer that is down) to {@link #getPointerCount(MotionEvent)}-1.
     * @return The value of the axis, or 0 if the axis is not available.
     * @see #AXIS_X
     * @see #AXIS_Y
     */
    public static float getAxisValue(android.view.MotionEvent event, int axis, int pointerIndex) {
        return android.support.v4.view.MotionEventCompat.IMPL.getAxisValue(event, axis, pointerIndex);
    }

    /**
     *
     *
     * @param event
     * 		
     * @return 
     */
    public static int getButtonState(android.view.MotionEvent event) {
        return android.support.v4.view.MotionEventCompat.IMPL.getButtonState(event);
    }

    private MotionEventCompat() {
    }
}

