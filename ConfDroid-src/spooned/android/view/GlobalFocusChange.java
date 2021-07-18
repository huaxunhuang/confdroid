/**
 * Copyright (C) 2008 The Android Open Source Project
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


public class GlobalFocusChange extends android.app.Activity implements android.view.ViewTreeObserver.OnGlobalFocusChangeListener {
    public android.view.View mOldFocus;

    public android.view.View mNewFocus;

    @java.lang.Override
    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.focus_listener);
        findViewById(R.id.left).getViewTreeObserver().addOnGlobalFocusChangeListener(this);
    }

    public void reset() {
        mOldFocus = mNewFocus = null;
    }

    public void onGlobalFocusChanged(android.view.View oldFocus, android.view.View newFocus) {
        mOldFocus = oldFocus;
        mNewFocus = newFocus;
    }
}

