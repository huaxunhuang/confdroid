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
package android.widget.layout.linear;


public class LLOfTwoFocusableInTouchMode extends android.app.Activity {
    private android.view.View mButton1;

    private android.view.View mButton2;

    private android.view.View mButton3;

    private boolean mB1Fired = false;

    private boolean mB2Fired = false;

    private boolean mB3Fired = false;

    @java.lang.Override
    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.linear_layout_buttons);
        mButton1 = findViewById(R.id.button1);
        mButton2 = findViewById(R.id.button2);
        mButton3 = findViewById(R.id.button3);
        mButton1.setFocusableInTouchMode(true);
        mButton2.setFocusableInTouchMode(true);
        mButton3.setFocusableInTouchMode(true);
        mButton1.setOnClickListener(new android.view.View.OnClickListener() {
            public void onClick(android.view.View v) {
                mB1Fired = true;
            }
        });
        mButton2.setOnClickListener(new android.view.View.OnClickListener() {
            public void onClick(android.view.View v) {
                mB2Fired = true;
            }
        });
        mButton3.setOnClickListener(new android.view.View.OnClickListener() {
            public void onClick(android.view.View v) {
                mB3Fired = true;
            }
        });
    }

    public android.view.View getButton1() {
        return mButton1;
    }

    public android.view.View getButton2() {
        return mButton2;
    }

    public android.view.View getButton3() {
        return mButton3;
    }

    public boolean isB1Fired() {
        return mB1Fired;
    }

    public boolean isB2Fired() {
        return mB2Fired;
    }

    public boolean isB3Fired() {
        return mB3Fired;
    }
}

