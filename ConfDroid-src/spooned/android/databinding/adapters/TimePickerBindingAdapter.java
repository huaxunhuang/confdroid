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


public class TimePickerBindingAdapter {
    @java.lang.SuppressWarnings("deprecation")
    @android.databinding.BindingAdapter("android:hour")
    public static void setHour(android.widget.TimePicker view, int hour) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            if (view.getHour() != hour) {
                view.setHour(hour);
            }
        } else {
            if (view.getCurrentHour() != hour) {
                view.setCurrentHour(hour);
            }
        }
    }

    @java.lang.SuppressWarnings("deprecation")
    @android.databinding.BindingAdapter("android:minute")
    public static void setMinute(android.widget.TimePicker view, int minute) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            if (view.getMinute() != minute) {
                view.setMinute(minute);
            }
        } else {
            if (view.getCurrentMinute() != minute) {
                view.setCurrentHour(minute);
            }
        }
    }

    @android.databinding.InverseBindingAdapter(attribute = "android:hour")
    public static int getHour(android.widget.TimePicker view) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            return view.getHour();
        } else {
            @java.lang.SuppressWarnings("deprecation")
            java.lang.Integer hour = view.getCurrentHour();
            if (hour == null) {
                return 0;
            } else {
                return hour;
            }
        }
    }

    @android.databinding.InverseBindingAdapter(attribute = "android:minute")
    public static int getMinute(android.widget.TimePicker view) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            return view.getMinute();
        } else {
            @java.lang.SuppressWarnings("deprecation")
            java.lang.Integer minute = view.getCurrentMinute();
            if (minute == null) {
                return 0;
            } else {
                return minute;
            }
        }
    }

    @android.databinding.BindingAdapter(value = { "android:onTimeChanged", "android:hourAttrChanged", "android:minuteAttrChanged" }, requireAll = false)
    public static void setListeners(android.widget.TimePicker view, final android.widget.TimePicker.OnTimeChangedListener listener, final android.databinding.InverseBindingListener hourChange, final android.databinding.InverseBindingListener minuteChange) {
        if ((hourChange == null) && (minuteChange == null)) {
            view.setOnTimeChangedListener(listener);
        } else {
            view.setOnTimeChangedListener(new android.widget.TimePicker.OnTimeChangedListener() {
                @java.lang.Override
                public void onTimeChanged(android.widget.TimePicker view, int hourOfDay, int minute) {
                    if (listener != null) {
                        listener.onTimeChanged(view, hourOfDay, minute);
                    }
                    if (hourChange != null) {
                        hourChange.onChange();
                    }
                    if (minuteChange != null) {
                        minuteChange.onChange();
                    }
                }
            });
        }
    }
}

