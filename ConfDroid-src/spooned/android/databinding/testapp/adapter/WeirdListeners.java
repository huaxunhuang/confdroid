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


public class WeirdListeners {
    @android.databinding.BindingAdapter("android:onFoo")
    public static void setListener(android.view.View view, android.databinding.testapp.adapter.WeirdListeners.OnFoo onFoo) {
    }

    @android.databinding.BindingAdapter("android:onFoo2")
    public static void setListener(android.view.View view, android.databinding.testapp.adapter.WeirdListeners.OnFoo2 onFoo) {
    }

    @android.databinding.BindingAdapter("android:onBar1")
    public static void setListener(android.view.View view, android.databinding.testapp.adapter.WeirdListeners.OnBar1 onBar) {
    }

    @android.databinding.BindingAdapter("android:onBar2")
    public static void setListener(android.view.View view, android.databinding.testapp.adapter.WeirdListeners.OnBar2 onBar) {
    }

    @android.databinding.BindingAdapter({ "runnable", "fooId", "barId" })
    public static void setRunnable(android.view.View view, java.lang.Runnable runnable, int foo, int bar) {
        runnable.run();
    }

    @android.annotation.TargetApi(android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public static abstract class OnFoo {
        public abstract void onFoo();

        public void onBar() {
        }
    }

    public interface OnFoo2 {
        void onFoo();
    }

    @android.annotation.TargetApi(android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public interface OnBar1 {
        void onBar();
    }

    @android.annotation.TargetApi(android.os.Build.VERSION_CODES.GINGERBREAD)
    public interface OnBar2 {
        boolean onBar(android.view.View view);
    }
}

