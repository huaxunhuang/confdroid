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
 * Exercise View's disabled state.
 */
public class Disabled extends android.app.Activity implements android.view.View.OnClickListener {
    @java.lang.Override
    protected void onCreate(android.os.Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.disabled);
        // Find our buttons
        android.widget.Button disabledButton = ((android.widget.Button) (findViewById(R.id.disabledButton)));
        disabledButton.setEnabled(false);
        // Find our buttons
        android.widget.Button disabledButtonA = ((android.widget.Button) (findViewById(R.id.disabledButtonA)));
        disabledButtonA.setOnClickListener(this);
    }

    public void onClick(android.view.View v) {
        android.widget.Button disabledButtonB = ((android.widget.Button) (findViewById(R.id.disabledButtonB)));
        disabledButtonB.setEnabled(!disabledButtonB.isEnabled());
    }
}

