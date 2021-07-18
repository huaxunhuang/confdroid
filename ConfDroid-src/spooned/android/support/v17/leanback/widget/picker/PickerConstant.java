/**
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package android.support.v17.leanback.widget.picker;


/**
 * Date/Time Picker related constants
 */
class PickerConstant {
    public final java.lang.String[] months;

    public final java.lang.String[] days;

    public final java.lang.String[] hours12;

    public final java.lang.String[] hours24;

    public final java.lang.String[] minutes;

    public final java.lang.String[] ampm;

    public final java.lang.String dateSeparator;

    public final java.lang.String timeSeparator;

    public final java.util.Locale locale;

    public PickerConstant(java.util.Locale locale, android.content.res.Resources resources) {
        this.locale = locale;
        java.text.DateFormatSymbols symbols = java.text.DateFormatSymbols.getInstance(locale);
        months = symbols.getShortMonths();
        java.util.Calendar calendar = java.util.Calendar.getInstance(locale);
        days = android.support.v17.leanback.widget.picker.PickerConstant.createStringIntArrays(calendar.getMinimum(java.util.Calendar.DAY_OF_MONTH), calendar.getMaximum(java.util.Calendar.DAY_OF_MONTH), "%02d");
        hours12 = android.support.v17.leanback.widget.picker.PickerConstant.createStringIntArrays(1, 12, "%02d");
        hours24 = android.support.v17.leanback.widget.picker.PickerConstant.createStringIntArrays(0, 23, "%02d");
        minutes = android.support.v17.leanback.widget.picker.PickerConstant.createStringIntArrays(0, 59, "%02d");
        ampm = symbols.getAmPmStrings();
        dateSeparator = resources.getString(R.string.lb_date_separator);
        timeSeparator = resources.getString(R.string.lb_time_separator);
    }

    public static java.lang.String[] createStringIntArrays(int firstNumber, int lastNumber, java.lang.String format) {
        java.lang.String[] array = new java.lang.String[(lastNumber - firstNumber) + 1];
        for (int i = firstNumber; i <= lastNumber; i++) {
            if (format != null) {
                array[i - firstNumber] = java.lang.String.format(format, i);
            } else {
                array[i - firstNumber] = java.lang.String.valueOf(i);
            }
        }
        return array;
    }
}

