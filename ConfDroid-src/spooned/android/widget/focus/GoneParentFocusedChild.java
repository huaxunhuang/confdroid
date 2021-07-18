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
package android.widget.focus;


/**
 * An activity that helps test the scenario where a parent is
 * GONE and one of its children has focus; the activity should get
 * the key event.  see bug 945150.
 */
public class GoneParentFocusedChild extends android.app.Activity {
    private android.widget.LinearLayout mGoneGroup;

    private android.widget.Button mButton;

    private boolean mUnhandledKeyEvent = false;

    private android.widget.LinearLayout mLayout;

    public boolean isUnhandledKeyEvent() {
        return mUnhandledKeyEvent;
    }

    public android.widget.LinearLayout getLayout() {
        return mLayout;
    }

    public android.widget.LinearLayout getGoneGroup() {
        return mGoneGroup;
    }

    public android.widget.Button getButton() {
        return mButton;
    }

    protected void onCreate(android.os.Bundle icicle) {
        super.onCreate(icicle);
        mLayout = new android.widget.LinearLayout(this);
        mLayout.setOrientation(android.widget.LinearLayout.HORIZONTAL);
        mLayout.setLayoutParams(new android.view.ViewGroup.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.MATCH_PARENT));
        mGoneGroup = new android.widget.LinearLayout(this);
        mGoneGroup.setOrientation(android.widget.LinearLayout.HORIZONTAL);
        mGoneGroup.setLayoutParams(new android.view.ViewGroup.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.MATCH_PARENT));
        mButton = new android.widget.Button(this);
        mButton.setLayoutParams(new android.widget.LinearLayout.LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
        mGoneGroup.addView(mButton);
        setContentView(mLayout);
        mGoneGroup.setVisibility(android.view.View.GONE);
        mButton.requestFocus();
    }

    public boolean onKeyUp(int keyCode, android.view.KeyEvent event) {
        mUnhandledKeyEvent = true;
        return true;
    }
}

