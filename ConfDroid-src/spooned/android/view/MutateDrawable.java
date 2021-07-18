/**
 * Copyright (C) 2009 The Android Open Source Project
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


public class MutateDrawable extends android.app.Activity {
    @java.lang.Override
    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        android.widget.LinearLayout layout = new android.widget.LinearLayout(this);
        android.widget.Button ok = new android.widget.Button(this);
        ok.setId(R.id.a);
        ok.setBackgroundDrawable(getResources().getDrawable(R.drawable.sym_now_playing_skip_forward_1));
        android.widget.Button cancel = new android.widget.Button(this);
        cancel.setId(R.id.b);
        cancel.setBackgroundDrawable(getResources().getDrawable(R.drawable.sym_now_playing_skip_forward_1));
        layout.addView(ok);
        layout.addView(cancel);
        ok.getBackground().mutate().setAlpha(127);
        setContentView(layout);
    }
}

