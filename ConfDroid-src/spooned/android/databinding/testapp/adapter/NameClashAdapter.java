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
package android.databinding.testapp.adapter;


public class NameClashAdapter {
    @android.databinding.BindingAdapter("gabble-babble")
    public void setGabbleBabble(android.view.View view, java.lang.String value) {
    }

    public static class MyAdapter {
        @android.databinding.BindingAdapter({ "gabble-babble-flabble", "booble-beeble-bee" })
        public void setGabbleBabbleFlabble(android.view.View view, java.lang.String value, java.lang.String value2) {
        }
    }
}

