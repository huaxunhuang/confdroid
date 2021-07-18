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
package android.view;


/**
 * Tests views using post*() and getViewTreeObserver() before onAttachedToWindow().
 */
public class RunQueue extends android.app.Activity implements android.view.ViewTreeObserver.OnGlobalLayoutListener {
    public boolean runnableRan = false;

    public boolean runnableCancelled = true;

    public boolean globalLayout = false;

    public android.view.ViewTreeObserver viewTreeObserver;

    @java.lang.Override
    protected void onCreate(android.os.Bundle icicle) {
        super.onCreate(icicle);
        android.widget.TextView textView = new android.widget.TextView(this);
        textView.setText("RunQueue");
        textView.setId(R.id.simple_view);
        setContentView(textView);
        final android.view.View view = findViewById(R.id.simple_view);
        view.post(new java.lang.Runnable() {
            public void run() {
                runnableRan = true;
            }
        });
        final java.lang.Runnable runnable = new java.lang.Runnable() {
            public void run() {
                runnableCancelled = false;
            }
        };
        view.post(runnable);
        view.post(runnable);
        view.post(runnable);
        view.post(runnable);
        view.removeCallbacks(runnable);
        viewTreeObserver = view.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(this);
    }

    public void onGlobalLayout() {
        globalLayout = true;
    }
}

