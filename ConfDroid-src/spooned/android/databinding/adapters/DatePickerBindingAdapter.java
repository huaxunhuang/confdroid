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
package android.databinding.adapters;


@android.databinding.InverseBindingMethods({ @android.databinding.InverseBindingMethod(type = android.widget.DatePicker.class, attribute = "android:year"), @android.databinding.InverseBindingMethod(type = android.widget.DatePicker.class, attribute = "android:month"), @android.databinding.InverseBindingMethod(type = android.widget.DatePicker.class, attribute = "android:day", method = "getDayOfMonth") })
public class DatePickerBindingAdapter {
    @android.databinding.BindingAdapter(value = { "android:year", "android:month", "android:day", "android:onDateChanged", "android:yearAttrChanged", "android:monthAttrChanged", "android:dayAttrChanged" }, requireAll = false)
    public static void setListeners(android.widget.DatePicker view, int year, int month, int day, final android.widget.DatePicker.OnDateChangedListener listener, final android.databinding.InverseBindingListener yearChanged, final android.databinding.InverseBindingListener monthChanged, final android.databinding.InverseBindingListener dayChanged) {
        if (year == 0) {
            year = view.getYear();
        }
        if (day == 0) {
            day = view.getDayOfMonth();
        }
        if (((yearChanged == null) && (monthChanged == null)) && (dayChanged == null)) {
            view.init(year, month, day, listener);
        } else {
            android.databinding.adapters.DatePickerBindingAdapter.DateChangedListener oldListener = android.databinding.adapters.ListenerUtil.getListener(view, R.id.onDateChanged);
            if (oldListener == null) {
                oldListener = new android.databinding.adapters.DatePickerBindingAdapter.DateChangedListener();
                android.databinding.adapters.ListenerUtil.trackListener(view, oldListener, R.id.onDateChanged);
            }
            oldListener.setListeners(listener, yearChanged, monthChanged, dayChanged);
            view.init(year, month, day, oldListener);
        }
    }

    private static class DateChangedListener implements android.widget.DatePicker.OnDateChangedListener {
        android.widget.DatePicker.OnDateChangedListener mListener;

        android.databinding.InverseBindingListener mYearChanged;

        android.databinding.InverseBindingListener mMonthChanged;

        android.databinding.InverseBindingListener mDayChanged;

        public void setListeners(android.widget.DatePicker.OnDateChangedListener listener, android.databinding.InverseBindingListener yearChanged, android.databinding.InverseBindingListener monthChanged, android.databinding.InverseBindingListener dayChanged) {
            mListener = listener;
            mYearChanged = yearChanged;
            mMonthChanged = monthChanged;
            mDayChanged = dayChanged;
        }

        @java.lang.Override
        public void onDateChanged(android.widget.DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            if (mListener != null) {
                mListener.onDateChanged(view, year, monthOfYear, dayOfMonth);
            }
            if (mYearChanged != null) {
                mYearChanged.onChange();
            }
            if (mMonthChanged != null) {
                mMonthChanged.onChange();
            }
            if (mDayChanged != null) {
                mDayChanged.onChange();
            }
        }
    }
}

