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
package android.accessibilityservice;


/**
 * Accessibility services with the
 * {@link android.R.styleable#AccessibilityService_canPerformGestures} property can dispatch
 * gestures. This class describes those gestures. Gestures are made up of one or more strokes.
 * Gestures are immutable once built.
 * <p>
 * Spatial dimensions throughout are in screen pixels. Time is measured in milliseconds.
 */
public final class GestureDescription {
    /**
     * Gestures may contain no more than this many strokes
     */
    private static final int MAX_STROKE_COUNT = 10;

    /**
     * Upper bound on total gesture duration. Nearly all gestures will be much shorter.
     */
    private static final long MAX_GESTURE_DURATION_MS = 60 * 1000;

    private final java.util.List<android.accessibilityservice.GestureDescription.StrokeDescription> mStrokes = new java.util.ArrayList<>();

    private final float[] mTempPos = new float[2];

    /**
     * Get the upper limit for the number of strokes a gesture may contain.
     *
     * @return The maximum number of strokes.
     */
    public static int getMaxStrokeCount() {
        return android.accessibilityservice.GestureDescription.MAX_STROKE_COUNT;
    }

    /**
     * Get the upper limit on a gesture's duration.
     *
     * @return The maximum duration in milliseconds.
     */
    public static long getMaxGestureDuration() {
        return android.accessibilityservice.GestureDescription.MAX_GESTURE_DURATION_MS;
    }

    private GestureDescription() {
    }

    private GestureDescription(java.util.List<android.accessibilityservice.GestureDescription.StrokeDescription> strokes) {
        mStrokes.addAll(strokes);
    }

    /**
     * Get the number of stroke in the gesture.
     *
     * @return the number of strokes in this gesture
     */
    public int getStrokeCount() {
        return mStrokes.size();
    }

    /**
     * Read a stroke from the gesture
     *
     * @param index
     * 		the index of the stroke
     * @return A description of the stroke.
     */
    public android.accessibilityservice.GestureDescription.StrokeDescription getStroke(@android.annotation.IntRange(from = 0)
    int index) {
        return mStrokes.get(index);
    }

    /**
     * Return the smallest key point (where a path starts or ends) that is at least a specified
     * offset
     *
     * @param offset
     * 		the minimum start time
     * @return The next key time that is at least the offset or -1 if one can't be found
     */
    private long getNextKeyPointAtLeast(long offset) {
        long nextKeyPoint = java.lang.Long.MAX_VALUE;
        for (int i = 0; i < mStrokes.size(); i++) {
            long thisStartTime = mStrokes.get(i).mStartTime;
            if ((thisStartTime < nextKeyPoint) && (thisStartTime >= offset)) {
                nextKeyPoint = thisStartTime;
            }
            long thisEndTime = mStrokes.get(i).mEndTime;
            if ((thisEndTime < nextKeyPoint) && (thisEndTime >= offset)) {
                nextKeyPoint = thisEndTime;
            }
        }
        return nextKeyPoint == java.lang.Long.MAX_VALUE ? -1L : nextKeyPoint;
    }

    /**
     * Get the points that correspond to a particular moment in time.
     *
     * @param time
     * 		The time of interest
     * @param touchPoints
     * 		An array to hold the current touch points. Must be preallocated to at
     * 		least the number of paths in the gesture to prevent going out of bounds
     * @return The number of points found, and thus the number of elements set in each array
     */
    private int getPointsForTime(long time, android.accessibilityservice.GestureDescription.TouchPoint[] touchPoints) {
        int numPointsFound = 0;
        for (int i = 0; i < mStrokes.size(); i++) {
            android.accessibilityservice.GestureDescription.StrokeDescription strokeDescription = mStrokes.get(i);
            if (strokeDescription.hasPointForTime(time)) {
                touchPoints[numPointsFound].mPathIndex = i;
                touchPoints[numPointsFound].mIsStartOfPath = time == strokeDescription.mStartTime;
                touchPoints[numPointsFound].mIsEndOfPath = time == strokeDescription.mEndTime;
                strokeDescription.getPosForTime(time, mTempPos);
                touchPoints[numPointsFound].mX = java.lang.Math.round(mTempPos[0]);
                touchPoints[numPointsFound].mY = java.lang.Math.round(mTempPos[1]);
                numPointsFound++;
            }
        }
        return numPointsFound;
    }

    // Total duration assumes that the gesture starts at 0; waiting around to start a gesture
    // counts against total duration
    private static long getTotalDuration(java.util.List<android.accessibilityservice.GestureDescription.StrokeDescription> paths) {
        long latestEnd = java.lang.Long.MIN_VALUE;
        for (int i = 0; i < paths.size(); i++) {
            android.accessibilityservice.GestureDescription.StrokeDescription path = paths.get(i);
            latestEnd = java.lang.Math.max(latestEnd, path.mEndTime);
        }
        return java.lang.Math.max(latestEnd, 0);
    }

    /**
     * Builder for a {@code GestureDescription}
     */
    public static class Builder {
        private final java.util.List<android.accessibilityservice.GestureDescription.StrokeDescription> mStrokes = new java.util.ArrayList<>();

        /**
         * Add a stroke to the gesture description. Up to
         * {@link GestureDescription#getMaxStrokeCount()} paths may be
         * added to a gesture, and the total gesture duration (earliest path start time to latest
         * path end time) may not exceed {@link GestureDescription#getMaxGestureDuration()}.
         *
         * @param strokeDescription
         * 		the stroke to add.
         * @return this
         */
        public android.accessibilityservice.GestureDescription.Builder addStroke(@android.annotation.NonNull
        android.accessibilityservice.GestureDescription.StrokeDescription strokeDescription) {
            if (mStrokes.size() >= android.accessibilityservice.GestureDescription.MAX_STROKE_COUNT) {
                throw new java.lang.IllegalStateException("Attempting to add too many strokes to a gesture");
            }
            mStrokes.add(strokeDescription);
            if (android.accessibilityservice.GestureDescription.getTotalDuration(mStrokes) > android.accessibilityservice.GestureDescription.MAX_GESTURE_DURATION_MS) {
                mStrokes.remove(strokeDescription);
                throw new java.lang.IllegalStateException("Gesture would exceed maximum duration with new stroke");
            }
            return this;
        }

        public android.accessibilityservice.GestureDescription build() {
            if (mStrokes.size() == 0) {
                throw new java.lang.IllegalStateException("Gestures must have at least one stroke");
            }
            return new android.accessibilityservice.GestureDescription(mStrokes);
        }
    }

    /**
     * Immutable description of stroke that can be part of a gesture.
     */
    public static class StrokeDescription {
        android.graphics.Path mPath;

        long mStartTime;

        long mEndTime;

        private float mTimeToLengthConversion;

        private android.graphics.PathMeasure mPathMeasure;

        // The tap location is only set for zero-length paths
        float[] mTapLocation;

        /**
         *
         *
         * @param path
         * 		The path to follow. Must have exactly one contour. The bounds of the path
         * 		must not be negative. The path must not be empty. If the path has zero length
         * 		(for example, a single {@code moveTo()}), the stroke is a touch that doesn't move.
         * @param startTime
         * 		The time, in milliseconds, from the time the gesture starts to the
         * 		time the stroke should start. Must not be negative.
         * @param duration
         * 		The duration, in milliseconds, the stroke takes to traverse the path.
         * 		Must not be negative.
         */
        public StrokeDescription(@android.annotation.NonNull
        android.graphics.Path path, @android.annotation.IntRange(from = 0)
        long startTime, @android.annotation.IntRange(from = 0)
        long duration) {
            if (duration <= 0) {
                throw new java.lang.IllegalArgumentException("Duration must be positive");
            }
            if (startTime < 0) {
                throw new java.lang.IllegalArgumentException("Start time must not be negative");
            }
            android.graphics.RectF bounds = new android.graphics.RectF();
            /* unused */
            path.computeBounds(bounds, false);
            if ((((bounds.bottom < 0) || (bounds.top < 0)) || (bounds.right < 0)) || (bounds.left < 0)) {
                throw new java.lang.IllegalArgumentException("Path bounds must not be negative");
            }
            if (path.isEmpty()) {
                throw new java.lang.IllegalArgumentException("Path is empty");
            }
            mPath = new android.graphics.Path(path);
            mPathMeasure = new android.graphics.PathMeasure(path, false);
            if (mPathMeasure.getLength() == 0) {
                // Treat zero-length paths as taps
                android.graphics.Path tempPath = new android.graphics.Path(path);
                tempPath.lineTo(-1, -1);
                mTapLocation = new float[2];
                android.graphics.PathMeasure pathMeasure = new android.graphics.PathMeasure(tempPath, false);
                pathMeasure.getPosTan(0, mTapLocation, null);
            }
            if (mPathMeasure.nextContour()) {
                throw new java.lang.IllegalArgumentException("Path has more than one contour");
            }
            /* Calling nextContour has moved mPathMeasure off the first contour, which is the only
            one we care about. Set the path again to go back to the first contour.
             */
            mPathMeasure.setPath(mPath, false);
            mStartTime = startTime;
            mEndTime = startTime + duration;
            mTimeToLengthConversion = getLength() / duration;
        }

        /**
         * Retrieve a copy of the path for this stroke
         *
         * @return A copy of the path
         */
        public android.graphics.Path getPath() {
            return new android.graphics.Path(mPath);
        }

        /**
         * Get the stroke's start time
         *
         * @return the start time for this stroke.
         */
        public long getStartTime() {
            return mStartTime;
        }

        /**
         * Get the stroke's duration
         *
         * @return the duration for this stroke
         */
        public long getDuration() {
            return mEndTime - mStartTime;
        }

        float getLength() {
            return mPathMeasure.getLength();
        }

        /* Assumes hasPointForTime returns true */
        boolean getPosForTime(long time, float[] pos) {
            if (mTapLocation != null) {
                pos[0] = mTapLocation[0];
                pos[1] = mTapLocation[1];
                return true;
            }
            if (time == mEndTime) {
                // Close to the end time, roundoff can be a problem
                return mPathMeasure.getPosTan(getLength(), pos, null);
            }
            float length = mTimeToLengthConversion * ((float) (time - mStartTime));
            return mPathMeasure.getPosTan(length, pos, null);
        }

        boolean hasPointForTime(long time) {
            return (time >= mStartTime) && (time <= mEndTime);
        }
    }

    /**
     * The location of a finger for gesture dispatch
     *
     * @unknown 
     */
    public static class TouchPoint implements android.os.Parcelable {
        private static final int FLAG_IS_START_OF_PATH = 0x1;

        private static final int FLAG_IS_END_OF_PATH = 0x2;

        int mPathIndex;

        boolean mIsStartOfPath;

        boolean mIsEndOfPath;

        float mX;

        float mY;

        public TouchPoint() {
        }

        public TouchPoint(android.accessibilityservice.GestureDescription.TouchPoint pointToCopy) {
            copyFrom(pointToCopy);
        }

        public TouchPoint(android.os.Parcel parcel) {
            mPathIndex = parcel.readInt();
            int startEnd = parcel.readInt();
            mIsStartOfPath = (startEnd & android.accessibilityservice.GestureDescription.TouchPoint.FLAG_IS_START_OF_PATH) != 0;
            mIsEndOfPath = (startEnd & android.accessibilityservice.GestureDescription.TouchPoint.FLAG_IS_END_OF_PATH) != 0;
            mX = parcel.readFloat();
            mY = parcel.readFloat();
        }

        void copyFrom(android.accessibilityservice.GestureDescription.TouchPoint other) {
            mPathIndex = other.mPathIndex;
            mIsStartOfPath = other.mIsStartOfPath;
            mIsEndOfPath = other.mIsEndOfPath;
            mX = other.mX;
            mY = other.mY;
        }

        @java.lang.Override
        public int describeContents() {
            return 0;
        }

        @java.lang.Override
        public void writeToParcel(android.os.Parcel dest, int flags) {
            dest.writeInt(mPathIndex);
            int startEnd = (mIsStartOfPath) ? android.accessibilityservice.GestureDescription.TouchPoint.FLAG_IS_START_OF_PATH : 0;
            startEnd |= (mIsEndOfPath) ? android.accessibilityservice.GestureDescription.TouchPoint.FLAG_IS_END_OF_PATH : 0;
            dest.writeInt(startEnd);
            dest.writeFloat(mX);
            dest.writeFloat(mY);
        }

        public static final android.os.Parcelable.Creator<android.accessibilityservice.GestureDescription.TouchPoint> CREATOR = new android.os.Parcelable.Creator<android.accessibilityservice.GestureDescription.TouchPoint>() {
            public android.accessibilityservice.GestureDescription.TouchPoint createFromParcel(android.os.Parcel in) {
                return new android.accessibilityservice.GestureDescription.TouchPoint(in);
            }

            public android.accessibilityservice.GestureDescription.TouchPoint[] newArray(int size) {
                return new android.accessibilityservice.GestureDescription.TouchPoint[size];
            }
        };
    }

    /**
     * A step along a gesture. Contains all of the touch points at a particular time
     *
     * @unknown 
     */
    public static class GestureStep implements android.os.Parcelable {
        public long timeSinceGestureStart;

        public int numTouchPoints;

        public android.accessibilityservice.GestureDescription.TouchPoint[] touchPoints;

        public GestureStep(long timeSinceGestureStart, int numTouchPoints, android.accessibilityservice.GestureDescription.TouchPoint[] touchPointsToCopy) {
            this.timeSinceGestureStart = timeSinceGestureStart;
            this.numTouchPoints = numTouchPoints;
            this.touchPoints = new android.accessibilityservice.GestureDescription.TouchPoint[numTouchPoints];
            for (int i = 0; i < numTouchPoints; i++) {
                this.touchPoints[i] = new android.accessibilityservice.GestureDescription.TouchPoint(touchPointsToCopy[i]);
            }
        }

        public GestureStep(android.os.Parcel parcel) {
            timeSinceGestureStart = parcel.readLong();
            android.os.Parcelable[] parcelables = parcel.readParcelableArray(android.accessibilityservice.GestureDescription.TouchPoint.class.getClassLoader());
            numTouchPoints = (parcelables == null) ? 0 : parcelables.length;
            touchPoints = new android.accessibilityservice.GestureDescription.TouchPoint[numTouchPoints];
            for (int i = 0; i < numTouchPoints; i++) {
                touchPoints[i] = ((android.accessibilityservice.GestureDescription.TouchPoint) (parcelables[i]));
            }
        }

        @java.lang.Override
        public int describeContents() {
            return 0;
        }

        @java.lang.Override
        public void writeToParcel(android.os.Parcel dest, int flags) {
            dest.writeLong(timeSinceGestureStart);
            dest.writeParcelableArray(touchPoints, flags);
        }

        public static final android.os.Parcelable.Creator<android.accessibilityservice.GestureDescription.GestureStep> CREATOR = new android.os.Parcelable.Creator<android.accessibilityservice.GestureDescription.GestureStep>() {
            public android.accessibilityservice.GestureDescription.GestureStep createFromParcel(android.os.Parcel in) {
                return new android.accessibilityservice.GestureDescription.GestureStep(in);
            }

            public android.accessibilityservice.GestureDescription.GestureStep[] newArray(int size) {
                return new android.accessibilityservice.GestureDescription.GestureStep[size];
            }
        };
    }

    /**
     * Class to convert a GestureDescription to a series of MotionEvents.
     *
     * @unknown 
     */
    public static class MotionEventGenerator {
        /**
         * Constants used to initialize all MotionEvents
         */
        private static final int EVENT_META_STATE = 0;

        private static final int EVENT_BUTTON_STATE = 0;

        private static final int EVENT_DEVICE_ID = 0;

        private static final int EVENT_EDGE_FLAGS = 0;

        private static final int EVENT_SOURCE = android.view.InputDevice.SOURCE_TOUCHSCREEN;

        private static final int EVENT_FLAGS = 0;

        private static final float EVENT_X_PRECISION = 1;

        private static final float EVENT_Y_PRECISION = 1;

        /* Lazily-created scratch memory for processing touches */
        private static android.accessibilityservice.GestureDescription.TouchPoint[] sCurrentTouchPoints;

        private static android.accessibilityservice.GestureDescription.TouchPoint[] sLastTouchPoints;

        private static android.view.MotionEvent.PointerCoords[] sPointerCoords;

        private static android.view.MotionEvent.PointerProperties[] sPointerProps;

        static java.util.List<android.accessibilityservice.GestureDescription.GestureStep> getGestureStepsFromGestureDescription(android.accessibilityservice.GestureDescription description, int sampleTimeMs) {
            final java.util.List<android.accessibilityservice.GestureDescription.GestureStep> gestureSteps = new java.util.ArrayList<>();
            // Point data at each time we generate an event for
            final android.accessibilityservice.GestureDescription.TouchPoint[] currentTouchPoints = android.accessibilityservice.GestureDescription.MotionEventGenerator.getCurrentTouchPoints(description.getStrokeCount());
            int currentTouchPointSize = 0;
            /* Loop through each time slice where there are touch points */
            long timeSinceGestureStart = 0;
            long nextKeyPointTime = description.getNextKeyPointAtLeast(timeSinceGestureStart);
            while (nextKeyPointTime >= 0) {
                timeSinceGestureStart = (currentTouchPointSize == 0) ? nextKeyPointTime : java.lang.Math.min(nextKeyPointTime, timeSinceGestureStart + sampleTimeMs);
                currentTouchPointSize = description.getPointsForTime(timeSinceGestureStart, currentTouchPoints);
                gestureSteps.add(new android.accessibilityservice.GestureDescription.GestureStep(timeSinceGestureStart, currentTouchPointSize, currentTouchPoints));
                /* Move to next time slice */
                nextKeyPointTime = description.getNextKeyPointAtLeast(timeSinceGestureStart + 1);
            } 
            return gestureSteps;
        }

        public static java.util.List<android.view.MotionEvent> getMotionEventsFromGestureSteps(java.util.List<android.accessibilityservice.GestureDescription.GestureStep> steps) {
            final java.util.List<android.view.MotionEvent> motionEvents = new java.util.ArrayList<>();
            // Number of points in last touch event
            int lastTouchPointSize = 0;
            android.accessibilityservice.GestureDescription.TouchPoint[] lastTouchPoints;
            for (int i = 0; i < steps.size(); i++) {
                android.accessibilityservice.GestureDescription.GestureStep step = steps.get(i);
                int currentTouchPointSize = step.numTouchPoints;
                lastTouchPoints = android.accessibilityservice.GestureDescription.MotionEventGenerator.getLastTouchPoints(java.lang.Math.max(lastTouchPointSize, currentTouchPointSize));
                android.accessibilityservice.GestureDescription.MotionEventGenerator.appendMoveEventIfNeeded(motionEvents, lastTouchPoints, lastTouchPointSize, step.touchPoints, currentTouchPointSize, step.timeSinceGestureStart);
                lastTouchPointSize = android.accessibilityservice.GestureDescription.MotionEventGenerator.appendUpEvents(motionEvents, lastTouchPoints, lastTouchPointSize, step.touchPoints, currentTouchPointSize, step.timeSinceGestureStart);
                lastTouchPointSize = android.accessibilityservice.GestureDescription.MotionEventGenerator.appendDownEvents(motionEvents, lastTouchPoints, lastTouchPointSize, step.touchPoints, currentTouchPointSize, step.timeSinceGestureStart);
            }
            return motionEvents;
        }

        private static android.accessibilityservice.GestureDescription.TouchPoint[] getCurrentTouchPoints(int requiredCapacity) {
            if ((android.accessibilityservice.GestureDescription.MotionEventGenerator.sCurrentTouchPoints == null) || (android.accessibilityservice.GestureDescription.MotionEventGenerator.sCurrentTouchPoints.length < requiredCapacity)) {
                android.accessibilityservice.GestureDescription.MotionEventGenerator.sCurrentTouchPoints = new android.accessibilityservice.GestureDescription.TouchPoint[requiredCapacity];
                for (int i = 0; i < requiredCapacity; i++) {
                    android.accessibilityservice.GestureDescription.MotionEventGenerator.sCurrentTouchPoints[i] = new android.accessibilityservice.GestureDescription.TouchPoint();
                }
            }
            return android.accessibilityservice.GestureDescription.MotionEventGenerator.sCurrentTouchPoints;
        }

        private static android.accessibilityservice.GestureDescription.TouchPoint[] getLastTouchPoints(int requiredCapacity) {
            if ((android.accessibilityservice.GestureDescription.MotionEventGenerator.sLastTouchPoints == null) || (android.accessibilityservice.GestureDescription.MotionEventGenerator.sLastTouchPoints.length < requiredCapacity)) {
                android.accessibilityservice.GestureDescription.MotionEventGenerator.sLastTouchPoints = new android.accessibilityservice.GestureDescription.TouchPoint[requiredCapacity];
                for (int i = 0; i < requiredCapacity; i++) {
                    android.accessibilityservice.GestureDescription.MotionEventGenerator.sLastTouchPoints[i] = new android.accessibilityservice.GestureDescription.TouchPoint();
                }
            }
            return android.accessibilityservice.GestureDescription.MotionEventGenerator.sLastTouchPoints;
        }

        private static android.view.MotionEvent.PointerCoords[] getPointerCoords(int requiredCapacity) {
            if ((android.accessibilityservice.GestureDescription.MotionEventGenerator.sPointerCoords == null) || (android.accessibilityservice.GestureDescription.MotionEventGenerator.sPointerCoords.length < requiredCapacity)) {
                android.accessibilityservice.GestureDescription.MotionEventGenerator.sPointerCoords = new android.view.MotionEvent.PointerCoords[requiredCapacity];
                for (int i = 0; i < requiredCapacity; i++) {
                    android.accessibilityservice.GestureDescription.MotionEventGenerator.sPointerCoords[i] = new android.view.MotionEvent.PointerCoords();
                }
            }
            return android.accessibilityservice.GestureDescription.MotionEventGenerator.sPointerCoords;
        }

        private static android.view.MotionEvent.PointerProperties[] getPointerProps(int requiredCapacity) {
            if ((android.accessibilityservice.GestureDescription.MotionEventGenerator.sPointerProps == null) || (android.accessibilityservice.GestureDescription.MotionEventGenerator.sPointerProps.length < requiredCapacity)) {
                android.accessibilityservice.GestureDescription.MotionEventGenerator.sPointerProps = new android.view.MotionEvent.PointerProperties[requiredCapacity];
                for (int i = 0; i < requiredCapacity; i++) {
                    android.accessibilityservice.GestureDescription.MotionEventGenerator.sPointerProps[i] = new android.view.MotionEvent.PointerProperties();
                }
            }
            return android.accessibilityservice.GestureDescription.MotionEventGenerator.sPointerProps;
        }

        private static void appendMoveEventIfNeeded(java.util.List<android.view.MotionEvent> motionEvents, android.accessibilityservice.GestureDescription.TouchPoint[] lastTouchPoints, int lastTouchPointsSize, android.accessibilityservice.GestureDescription.TouchPoint[] currentTouchPoints, int currentTouchPointsSize, long currentTime) {
            /* Look for pointers that have moved */
            boolean moveFound = false;
            for (int i = 0; i < currentTouchPointsSize; i++) {
                int lastPointsIndex = android.accessibilityservice.GestureDescription.MotionEventGenerator.findPointByPathIndex(lastTouchPoints, lastTouchPointsSize, currentTouchPoints[i].mPathIndex);
                if (lastPointsIndex >= 0) {
                    moveFound |= (lastTouchPoints[lastPointsIndex].mX != currentTouchPoints[i].mX) || (lastTouchPoints[lastPointsIndex].mY != currentTouchPoints[i].mY);
                    lastTouchPoints[lastPointsIndex].copyFrom(currentTouchPoints[i]);
                }
            }
            if (moveFound) {
                long downTime = motionEvents.get(motionEvents.size() - 1).getDownTime();
                motionEvents.add(android.accessibilityservice.GestureDescription.MotionEventGenerator.obtainMotionEvent(downTime, currentTime, android.view.MotionEvent.ACTION_MOVE, lastTouchPoints, lastTouchPointsSize));
            }
        }

        private static int appendUpEvents(java.util.List<android.view.MotionEvent> motionEvents, android.accessibilityservice.GestureDescription.TouchPoint[] lastTouchPoints, int lastTouchPointsSize, android.accessibilityservice.GestureDescription.TouchPoint[] currentTouchPoints, int currentTouchPointsSize, long currentTime) {
            /* Look for a pointer at the end of its path */
            for (int i = 0; i < currentTouchPointsSize; i++) {
                if (currentTouchPoints[i].mIsEndOfPath) {
                    int indexOfUpEvent = android.accessibilityservice.GestureDescription.MotionEventGenerator.findPointByPathIndex(lastTouchPoints, lastTouchPointsSize, currentTouchPoints[i].mPathIndex);
                    if (indexOfUpEvent < 0) {
                        continue;// Should not happen

                    }
                    long downTime = motionEvents.get(motionEvents.size() - 1).getDownTime();
                    int action = (lastTouchPointsSize == 1) ? android.view.MotionEvent.ACTION_UP : android.view.MotionEvent.ACTION_POINTER_UP;
                    action |= indexOfUpEvent << android.view.MotionEvent.ACTION_POINTER_INDEX_SHIFT;
                    motionEvents.add(android.accessibilityservice.GestureDescription.MotionEventGenerator.obtainMotionEvent(downTime, currentTime, action, lastTouchPoints, lastTouchPointsSize));
                    /* Remove this point from lastTouchPoints */
                    for (int j = indexOfUpEvent; j < (lastTouchPointsSize - 1); j++) {
                        lastTouchPoints[j].copyFrom(lastTouchPoints[j + 1]);
                    }
                    lastTouchPointsSize--;
                }
            }
            return lastTouchPointsSize;
        }

        private static int appendDownEvents(java.util.List<android.view.MotionEvent> motionEvents, android.accessibilityservice.GestureDescription.TouchPoint[] lastTouchPoints, int lastTouchPointsSize, android.accessibilityservice.GestureDescription.TouchPoint[] currentTouchPoints, int currentTouchPointsSize, long currentTime) {
            /* Look for a pointer that is just starting */
            for (int i = 0; i < currentTouchPointsSize; i++) {
                if (currentTouchPoints[i].mIsStartOfPath) {
                    /* Add the point to last coords and use the new array to generate the event */
                    lastTouchPoints[lastTouchPointsSize++].copyFrom(currentTouchPoints[i]);
                    int action = (lastTouchPointsSize == 1) ? android.view.MotionEvent.ACTION_DOWN : android.view.MotionEvent.ACTION_POINTER_DOWN;
                    long downTime = (action == android.view.MotionEvent.ACTION_DOWN) ? currentTime : motionEvents.get(motionEvents.size() - 1).getDownTime();
                    action |= i << android.view.MotionEvent.ACTION_POINTER_INDEX_SHIFT;
                    motionEvents.add(android.accessibilityservice.GestureDescription.MotionEventGenerator.obtainMotionEvent(downTime, currentTime, action, lastTouchPoints, lastTouchPointsSize));
                }
            }
            return lastTouchPointsSize;
        }

        private static android.view.MotionEvent obtainMotionEvent(long downTime, long eventTime, int action, android.accessibilityservice.GestureDescription.TouchPoint[] touchPoints, int touchPointsSize) {
            android.view.MotionEvent.PointerCoords[] pointerCoords = android.accessibilityservice.GestureDescription.MotionEventGenerator.getPointerCoords(touchPointsSize);
            android.view.MotionEvent.PointerProperties[] pointerProperties = android.accessibilityservice.GestureDescription.MotionEventGenerator.getPointerProps(touchPointsSize);
            for (int i = 0; i < touchPointsSize; i++) {
                pointerProperties[i].id = touchPoints[i].mPathIndex;
                pointerProperties[i].toolType = android.view.MotionEvent.TOOL_TYPE_UNKNOWN;
                pointerCoords[i].clear();
                pointerCoords[i].pressure = 1.0F;
                pointerCoords[i].size = 1.0F;
                pointerCoords[i].x = touchPoints[i].mX;
                pointerCoords[i].y = touchPoints[i].mY;
            }
            return android.view.MotionEvent.obtain(downTime, eventTime, action, touchPointsSize, pointerProperties, pointerCoords, android.accessibilityservice.GestureDescription.MotionEventGenerator.EVENT_META_STATE, android.accessibilityservice.GestureDescription.MotionEventGenerator.EVENT_BUTTON_STATE, android.accessibilityservice.GestureDescription.MotionEventGenerator.EVENT_X_PRECISION, android.accessibilityservice.GestureDescription.MotionEventGenerator.EVENT_Y_PRECISION, android.accessibilityservice.GestureDescription.MotionEventGenerator.EVENT_DEVICE_ID, android.accessibilityservice.GestureDescription.MotionEventGenerator.EVENT_EDGE_FLAGS, android.accessibilityservice.GestureDescription.MotionEventGenerator.EVENT_SOURCE, android.accessibilityservice.GestureDescription.MotionEventGenerator.EVENT_FLAGS);
        }

        private static int findPointByPathIndex(android.accessibilityservice.GestureDescription.TouchPoint[] touchPoints, int touchPointsSize, int pathIndex) {
            for (int i = 0; i < touchPointsSize; i++) {
                if (touchPoints[i].mPathIndex == pathIndex) {
                    return i;
                }
            }
            return -1;
        }
    }
}

