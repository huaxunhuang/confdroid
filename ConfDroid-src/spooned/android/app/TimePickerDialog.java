/**
 * Copyright (C) 2007 The Android Open Source Project
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
package android.app;


/**
 * A dialog that prompts the user for the time of day using a
 * {@link TimePicker}.
 *
 * <p>
 * See the <a href="{@docRoot }guide/topics/ui/controls/pickers.html">Pickers</a>
 * guide.
 */
public class TimePickerDialog extends android.app.AlertDialog implements android.content.DialogInterface.OnClickListener , android.widget.TimePicker.OnTimeChangedListener {
    private static final java.lang.String HOUR = "hour";

    private static final java.lang.String MINUTE = "minute";

    private static final java.lang.String IS_24_HOUR = "is24hour";

    private final android.widget.TimePicker mTimePicker;

    private final android.app.TimePickerDialog.OnTimeSetListener mTimeSetListener;

    private final int mInitialHourOfDay;

    private final int mInitialMinute;

    private final boolean mIs24HourView;

    /**
     * The callback interface used to indicate the user is done filling in
     * the time (e.g. they clicked on the 'OK' button).
     */
    public interface OnTimeSetListener {
        /**
         * Called when the user is done setting a new time and the dialog has
         * closed.
         *
         * @param view
         * 		the view associated with this listener
         * @param hourOfDay
         * 		the hour that was set
         * @param minute
         * 		the minute that was set
         */
        void onTimeSet(android.widget.TimePicker view, int hourOfDay, int minute);
    }

    /**
     * Creates a new time picker dialog.
     *
     * @param context
     * 		the parent context
     * @param listener
     * 		the listener to call when the time is set
     * @param hourOfDay
     * 		the initial hour
     * @param minute
     * 		the initial minute
     * @param is24HourView
     * 		whether this is a 24 hour view or AM/PM
     */
    public TimePickerDialog(android.content.Context context, android.app.TimePickerDialog.OnTimeSetListener listener, int hourOfDay, int minute, boolean is24HourView) {
        this(context, 0, listener, hourOfDay, minute, is24HourView);
    }

    static int resolveDialogTheme(android.content.Context context, int resId) {
        if (resId == 0) {
            final android.util.TypedValue outValue = new android.util.TypedValue();
            context.getTheme().resolveAttribute(R.attr.timePickerDialogTheme, outValue, true);
            return outValue.resourceId;
        } else {
            return resId;
        }
    }

    /**
     * Creates a new time picker dialog with the specified theme.
     * <p>
     * The theme is overlaid on top of the theme of the parent {@code context}.
     * If {@code themeResId} is 0, the dialog will be inflated using the theme
     * specified by the
     * {@link android.R.attr#timePickerDialogTheme android:timePickerDialogTheme}
     * attribute on the parent {@code context}'s theme.
     *
     * @param context
     * 		the parent context
     * @param themeResId
     * 		the resource ID of the theme to apply to this dialog
     * @param listener
     * 		the listener to call when the time is set
     * @param hourOfDay
     * 		the initial hour
     * @param minute
     * 		the initial minute
     * @param is24HourView
     * 		Whether this is a 24 hour view, or AM/PM.
     */
    public TimePickerDialog(android.content.Context context, int themeResId, android.app.TimePickerDialog.OnTimeSetListener listener, int hourOfDay, int minute, boolean is24HourView) {
        super(context, android.app.TimePickerDialog.resolveDialogTheme(context, themeResId));
        mTimeSetListener = listener;
        mInitialHourOfDay = hourOfDay;
        mInitialMinute = minute;
        mIs24HourView = is24HourView;
        final android.content.Context themeContext = getContext();
        final android.view.LayoutInflater inflater = android.view.LayoutInflater.from(themeContext);
        final android.view.View view = inflater.inflate(R.layout.time_picker_dialog, null);
        setView(view);
        setButton(android.content.DialogInterface.BUTTON_POSITIVE, themeContext.getString(R.string.ok), this);
        setButton(android.content.DialogInterface.BUTTON_NEGATIVE, themeContext.getString(R.string.cancel), this);
        setButtonPanelLayoutHint(android.app.AlertDialog.LAYOUT_HINT_SIDE);
        mTimePicker = ((android.widget.TimePicker) (view.findViewById(R.id.timePicker)));
        mTimePicker.setIs24HourView(mIs24HourView);
        mTimePicker.setCurrentHour(mInitialHourOfDay);
        mTimePicker.setCurrentMinute(mInitialMinute);
        mTimePicker.setOnTimeChangedListener(this);
    }

    /**
     *
     *
     * @return the time picker displayed in the dialog
     * @unknown For testing only.
     */
    @android.annotation.TestApi
    public android.widget.TimePicker getTimePicker() {
        return mTimePicker;
    }

    @java.lang.Override
    public void onTimeChanged(android.widget.TimePicker view, int hourOfDay, int minute) {
        /* do nothing */
    }

    @java.lang.Override
    public void onClick(android.content.DialogInterface dialog, int which) {
        switch (which) {
            case android.content.DialogInterface.BUTTON_POSITIVE :
                if (mTimeSetListener != null) {
                    mTimeSetListener.onTimeSet(mTimePicker, mTimePicker.getCurrentHour(), mTimePicker.getCurrentMinute());
                }
                break;
            case android.content.DialogInterface.BUTTON_NEGATIVE :
                cancel();
                break;
        }
    }

    /**
     * Sets the current time.
     *
     * @param hourOfDay
     * 		The current hour within the day.
     * @param minuteOfHour
     * 		The current minute within the hour.
     */
    public void updateTime(int hourOfDay, int minuteOfHour) {
        mTimePicker.setCurrentHour(hourOfDay);
        mTimePicker.setCurrentMinute(minuteOfHour);
    }

    @java.lang.Override
    public android.os.Bundle onSaveInstanceState() {
        final android.os.Bundle state = super.onSaveInstanceState();
        state.putInt(android.app.TimePickerDialog.HOUR, mTimePicker.getCurrentHour());
        state.putInt(android.app.TimePickerDialog.MINUTE, mTimePicker.getCurrentMinute());
        state.putBoolean(android.app.TimePickerDialog.IS_24_HOUR, mTimePicker.is24HourView());
        return state;
    }

    @java.lang.Override
    public void onRestoreInstanceState(android.os.Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        final int hour = savedInstanceState.getInt(android.app.TimePickerDialog.HOUR);
        final int minute = savedInstanceState.getInt(android.app.TimePickerDialog.MINUTE);
        mTimePicker.setIs24HourView(savedInstanceState.getBoolean(android.app.TimePickerDialog.IS_24_HOUR));
        mTimePicker.setCurrentHour(hour);
        mTimePicker.setCurrentMinute(minute);
    }
}

