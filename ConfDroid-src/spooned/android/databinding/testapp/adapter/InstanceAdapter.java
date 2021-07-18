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


public class InstanceAdapter {
    private final java.lang.String format;

    public InstanceAdapter(java.lang.String format) {
        this.format = format;
    }

    @android.databinding.BindingAdapter("instanceAttr0")
    public void setInstanceAttr0(android.widget.TextView view, java.lang.String text) {
        view.setText(java.lang.String.format(format, text, "foo", "bar", "baz"));
    }

    @android.databinding.BindingAdapter({ "instanceAttr1", "instanceAttr2" })
    public void setInstanceAttr1(android.widget.TextView view, java.lang.String text, java.lang.String text2) {
        view.setText(java.lang.String.format(format, text, text2, "foo", "bar"));
    }

    @android.databinding.BindingAdapter("instanceAttr3")
    public void setInstanceAttr3(android.widget.TextView view, java.lang.String oldText, java.lang.String text) {
        view.setText(java.lang.String.format(format, oldText, text, "foo", "bar"));
    }

    @android.databinding.BindingAdapter({ "instanceAttr4", "instanceAttr5" })
    public void setInstanceAttr4(android.widget.TextView view, java.lang.String oldText1, java.lang.String oldText2, java.lang.String text1, java.lang.String text2) {
        view.setText(java.lang.String.format(format, oldText1, oldText2, text1, text2));
    }

    @android.databinding.BindingAdapter("instanceAttr6")
    public static void setInstanceAttr6(android.databinding.DataBindingComponent component, android.widget.TextView view, java.lang.String text) {
        view.setText(java.lang.String.format("%s %s", text, component == null ? "null" : "component"));
    }

    @android.databinding.BindingAdapter("instanceAttr7")
    public void setInstanceAttr7(android.databinding.DataBindingComponent component, android.widget.TextView view, java.lang.String text) {
        view.setText(java.lang.String.format(format, text, component == null ? "null" : "component", "bar", "baz"));
    }

    @android.databinding.BindingAdapter({ "instanceAttr8", "instanceAttr9" })
    public void setInstanceAttr8(android.databinding.testapp.TestComponent component, android.widget.TextView view, java.lang.String text, java.lang.String text2) {
        view.setText(java.lang.String.format(format, text, text2, component == null ? "null" : "component", "bar"));
    }
}

