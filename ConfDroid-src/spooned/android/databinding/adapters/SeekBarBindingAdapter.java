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


@android.databinding.InverseBindingMethods({ @android.databinding.InverseBindingMethod(type = android.widget.SeekBar.class, attribute = "android:progress") })
public class SeekBarBindingAdapter {
    @android.databinding.BindingAdapter("android:progress")
    public static void setProgress(android.widget.SeekBar view, int progress) {
        if (progress != view.getProgress()) {
            view.setProgress(progress);
        }
    }

    @android.databinding.BindingAdapter(value = { "android:onStartTrackingTouch", "android:onStopTrackingTouch", "android:onProgressChanged", "android:progressAttrChanged" }, requireAll = false)
    public static void setOnSeekBarChangeListener(android.widget.SeekBar view, final android.databinding.adapters.SeekBarBindingAdapter.OnStartTrackingTouch start, final android.databinding.adapters.SeekBarBindingAdapter.OnStopTrackingTouch stop, final android.databinding.adapters.SeekBarBindingAdapter.OnProgressChanged progressChanged, final android.databinding.InverseBindingListener attrChanged) {
        if ((((start == null) && (stop == null)) && (progressChanged == null)) && (attrChanged == null)) {
            view.setOnSeekBarChangeListener(null);
        } else {
            view.setOnSeekBarChangeListener(new android.widget.SeekBar.OnSeekBarChangeListener() {
                @java.lang.Override
                public void onProgressChanged(android.widget.SeekBar seekBar, int progress, boolean fromUser) {
                    if (progressChanged != null) {
                        progressChanged.onProgressChanged(seekBar, progress, fromUser);
                    }
                    if (attrChanged != null) {
                        attrChanged.onChange();
                    }
                }

                @java.lang.Override
                public void onStartTrackingTouch(android.widget.SeekBar seekBar) {
                    if (start != null) {
                        start.onStartTrackingTouch(seekBar);
                    }
                }

                @java.lang.Override
                public void onStopTrackingTouch(android.widget.SeekBar seekBar) {
                    if (stop != null) {
                        stop.onStopTrackingTouch(seekBar);
                    }
                }
            });
        }
    }

    public interface OnStartTrackingTouch {
        void onStartTrackingTouch(android.widget.SeekBar seekBar);
    }

    public interface OnStopTrackingTouch {
        void onStopTrackingTouch(android.widget.SeekBar seekBar);
    }

    public interface OnProgressChanged {
        void onProgressChanged(android.widget.SeekBar seekBar, int progress, boolean fromUser);
    }
}

