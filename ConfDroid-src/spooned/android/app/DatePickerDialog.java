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
 * A simple dialog containing an {@link android.widget.DatePicker}.
 * <p>
 * See the <a href="{@docRoot }guide/topics/ui/controls/pickers.html">Pickers</a>
 * guide.
 */
public class DatePickerDialog extends android.app.AlertDialog implements android.content.DialogInterface.OnClickListener , android.widget.DatePicker.OnDateChangedListener {
    private static final java.lang.String YEAR = "year";

    private static final java.lang.String MONTH = "month";

    private static final java.lang.String DAY = "day";

    private final android.widget.DatePicker mDatePicker;

    private android.app.DatePickerDialog.OnDateSetListener mDateSetListener;

    /**
     * Creates a new date picker dialog for the current date using the parent
     * context's default date picker dialog theme.
     *
     * @param context
     * 		the parent context
     */
    public DatePickerDialog(@android.annotation.NonNull
    android.content.Context context) {
        this(context, 0, null, java.util.Calendar.getInstance(), -1, -1, -1);
    }

    /**
     * Creates a new date picker dialog for the current date.
     *
     * @param context
     * 		the parent context
     * @param themeResId
     * 		the resource ID of the theme against which to inflate
     * 		this dialog, or {@code 0} to use the parent
     * 		{@code context}'s default alert dialog theme
     */
    public DatePickerDialog(@android.annotation.NonNull
    android.content.Context context, @android.annotation.StyleRes
    int themeResId) {
        this(context, themeResId, null, java.util.Calendar.getInstance(), -1, -1, -1);
    }

    /**
     * Creates a new date picker dialog for the specified date using the parent
     * context's default date picker dialog theme.
     *
     * @param context
     * 		the parent context
     * @param listener
     * 		the listener to call when the user sets the date
     * @param year
     * 		the initially selected year
     * @param month
     * 		the initially selected month (0-11 for compatibility with
     * 		{@link Calendar#MONTH})
     * @param dayOfMonth
     * 		the initially selected day of month (1-31, depending
     * 		on month)
     */
    public DatePickerDialog(@android.annotation.NonNull
    android.content.Context context, @android.annotation.Nullable
    android.app.DatePickerDialog.OnDateSetListener listener, int year, int month, int dayOfMonth) {
        this(context, 0, listener, null, year, month, dayOfMonth);
    }

    /**
     * Creates a new date picker dialog for the specified date.
     *
     * @param context
     * 		the parent context
     * @param themeResId
     * 		the resource ID of the theme against which to inflate
     * 		this dialog, or {@code 0} to use the parent
     * 		{@code context}'s default alert dialog theme
     * @param listener
     * 		the listener to call when the user sets the date
     * @param year
     * 		the initially selected year
     * @param monthOfYear
     * 		the initially selected month of the year (0-11 for
     * 		compatibility with {@link Calendar#MONTH})
     * @param dayOfMonth
     * 		the initially selected day of month (1-31, depending
     * 		on month)
     */
    public DatePickerDialog(@android.annotation.NonNull
    android.content.Context context, @android.annotation.StyleRes
    int themeResId, @android.annotation.Nullable
    android.app.DatePickerDialog.OnDateSetListener listener, int year, int monthOfYear, int dayOfMonth) {
        this(context, themeResId, listener, null, year, monthOfYear, dayOfMonth);
    }

    private DatePickerDialog(@android.annotation.NonNull
    android.content.Context context, @android.annotation.StyleRes
    int themeResId, @android.annotation.Nullable
    android.app.DatePickerDialog.OnDateSetListener listener, @android.annotation.Nullable
    java.util.Calendar calendar, int year, int monthOfYear, int dayOfMonth) {
        super(context, android.app.DatePickerDialog.resolveDialogTheme(context, themeResId));
        final android.content.Context themeContext = getContext();
        final android.view.LayoutInflater inflater = android.view.LayoutInflater.from(themeContext);
        final android.view.View view = inflater.inflate(R.layout.date_picker_dialog, null);
        setView(view);
        setButton(android.content.DialogInterface.BUTTON_POSITIVE, themeContext.getString(R.string.ok), this);
        setButton(android.content.DialogInterface.BUTTON_NEGATIVE, themeContext.getString(R.string.cancel), this);
        setButtonPanelLayoutHint(android.app.AlertDialog.LAYOUT_HINT_SIDE);
        if (calendar != null) {
            year = calendar.get(java.util.Calendar.YEAR);
            monthOfYear = calendar.get(java.util.Calendar.MONTH);
            dayOfMonth = calendar.get(java.util.Calendar.DAY_OF_MONTH);
        }
        mDatePicker = ((android.widget.DatePicker) (view.findViewById(R.id.datePicker)));
        mDatePicker.init(year, monthOfYear, dayOfMonth, this);
        mDatePicker.setValidationCallback(mValidationCallback);
        mDateSetListener = listener;
    }

    @android.annotation.StyleRes
    static int resolveDialogTheme(@android.annotation.NonNull
    android.content.Context context, @android.annotation.StyleRes
    int themeResId) {
        if (themeResId == 0) {
            final android.util.TypedValue outValue = new android.util.TypedValue();
            context.getTheme().resolveAttribute(R.attr.datePickerDialogTheme, outValue, true);
            return outValue.resourceId;
        } else {
            return themeResId;
        }
    }

    @java.lang.Override
    public void onDateChanged(@android.annotation.NonNull
    android.widget.DatePicker view, int year, int month, int dayOfMonth) {
        mDatePicker.init(year, month, dayOfMonth, this);
    }

    /**
     * Sets the listener to call when the user sets the date.
     *
     * @param listener
     * 		the listener to call when the user sets the date
     */
    public void setOnDateSetListener(@android.annotation.Nullable
    android.app.DatePickerDialog.OnDateSetListener listener) {
        mDateSetListener = listener;
    }

    @java.lang.Override
    public void onClick(@android.annotation.NonNull
    android.content.DialogInterface dialog, int which) {
        switch (which) {
            case android.content.DialogInterface.BUTTON_POSITIVE :
                if (mDateSetListener != null) {
                    // Clearing focus forces the dialog to commit any pending
                    // changes, e.g. typed text in a NumberPicker.
                    mDatePicker.clearFocus();
                    mDateSetListener.onDateSet(mDatePicker, mDatePicker.getYear(), mDatePicker.getMonth(), mDatePicker.getDayOfMonth());
                }
                break;
            case android.content.DialogInterface.BUTTON_NEGATIVE :
                cancel();
                break;
        }
    }

    /**
     * Returns the {@link DatePicker} contained in this dialog.
     *
     * @return the date picker
     */
    @android.annotation.NonNull
    public android.widget.DatePicker getDatePicker() {
        return mDatePicker;
    }

    /**
     * Sets the current date.
     *
     * @param year
     * 		the year
     * @param month
     * 		the month (0-11 for compatibility with
     * 		{@link Calendar#MONTH})
     * @param dayOfMonth
     * 		the day of month (1-31, depending on month)
     */
    public void updateDate(int year, int month, int dayOfMonth) {
        mDatePicker.updateDate(year, month, dayOfMonth);
    }

    @java.lang.Override
    public android.os.Bundle onSaveInstanceState() {
        final android.os.Bundle state = super.onSaveInstanceState();
        state.putInt(android.app.DatePickerDialog.YEAR, mDatePicker.getYear());
        state.putInt(android.app.DatePickerDialog.MONTH, mDatePicker.getMonth());
        state.putInt(android.app.DatePickerDialog.DAY, mDatePicker.getDayOfMonth());
        return state;
    }

    @java.lang.Override
    public void onRestoreInstanceState(android.os.Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        final int year = savedInstanceState.getInt(android.app.DatePickerDialog.YEAR);
        final int month = savedInstanceState.getInt(android.app.DatePickerDialog.MONTH);
        final int day = savedInstanceState.getInt(android.app.DatePickerDialog.DAY);
        mDatePicker.init(year, month, day, this);
    }

    private final android.widget.DatePicker.ValidationCallback mValidationCallback = new android.widget.DatePicker.ValidationCallback() {
        @java.lang.Override
        public void onValidationChanged(boolean valid) {
            final android.widget.Button positive = getButton(android.content.DialogInterface.BUTTON_POSITIVE);
            if (positive != null) {
                positive.setEnabled(valid);
            }
        }
    };

    /**
     * The listener used to indicate the user has finished selecting a date.
     */
    public interface OnDateSetListener {
        /**
         *
         *
         * @param view
         * 		the picker associated with the dialog
         * @param year
         * 		the selected year
         * @param month
         * 		the selected month (0-11 for compatibility with
         * 		{@link Calendar#MONTH})
         * @param dayOfMonth
         * 		th selected day of the month (1-31, depending on
         * 		month)
         */
        void onDateSet(android.widget.DatePicker view, int year, int month, int dayOfMonth);
    }
}

