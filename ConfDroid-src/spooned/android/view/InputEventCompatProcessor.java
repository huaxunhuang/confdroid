/**
 * Copyright (C) 2018 The Android Open Source Project
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
 * Compatibility processor for InputEvents that allows events to be adjusted before and
 * after it is sent to the application.
 *
 * {@hide }
 */
public class InputEventCompatProcessor {
    protected android.content.Context mContext;

    protected int mTargetSdkVersion;

    /**
     * List of events to be used to return the processed events
     */
    private java.util.List<android.view.InputEvent> mProcessedEvents;

    public InputEventCompatProcessor(android.content.Context context) {
        mContext = context;
        mTargetSdkVersion = context.getApplicationInfo().targetSdkVersion;
        mProcessedEvents = new java.util.ArrayList<>();
    }

    /**
     * Processes the InputEvent for compatibility before it is sent to the app, allowing for the
     * generation of more than one event if necessary.
     *
     * @param e
     * 		The InputEvent to process
     * @return The list of adjusted events, or null if no adjustments are needed. Do not keep a
    reference to the output as the list is reused.
     */
    public java.util.List<android.view.InputEvent> processInputEventForCompatibility(android.view.InputEvent e) {
        if ((mTargetSdkVersion < Build.VERSION_CODES.M) && (e instanceof android.view.MotionEvent)) {
            mProcessedEvents.clear();
            android.view.MotionEvent motion = ((android.view.MotionEvent) (e));
            final int mask = android.view.MotionEvent.BUTTON_STYLUS_PRIMARY | android.view.MotionEvent.BUTTON_STYLUS_SECONDARY;
            final int buttonState = motion.getButtonState();
            final int compatButtonState = (buttonState & mask) >> 4;
            if (compatButtonState != 0) {
                motion.setButtonState(buttonState | compatButtonState);
            }
            mProcessedEvents.add(motion);
            return mProcessedEvents;
        }
        return null;
    }

    /**
     * Processes the InputEvent for compatibility before it is finished by calling
     * InputEventReceiver#finishInputEvent().
     *
     * @param e
     * 		The InputEvent to process
     * @return The InputEvent to finish, or null if it should not be finished
     */
    public android.view.InputEvent processInputEventBeforeFinish(android.view.InputEvent e) {
        // No changes needed
        return e;
    }
}

