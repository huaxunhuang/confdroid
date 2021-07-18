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
 * Exercises cases where elements of the UI are removed (and
 * focus should go somewhere).
 */
public class FocusAfterRemoval extends android.app.Activity {
    protected void onCreate(android.os.Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.focus_after_removal);
        final android.widget.LinearLayout left = ((android.widget.LinearLayout) (findViewById(R.id.leftLayout)));
        // top left makes parent layout GONE
        android.widget.Button topLeftButton = ((android.widget.Button) (findViewById(R.id.topLeftButton)));
        topLeftButton.setOnClickListener(new android.view.View.OnClickListener() {
            public void onClick(android.view.View v) {
                left.setVisibility(android.view.View.GONE);
            }
        });
        // bottom left makes parent layout INVISIBLE
        // top left makes parent layout GONE
        android.widget.Button bottomLeftButton = ((android.widget.Button) (findViewById(R.id.bottomLeftButton)));
        bottomLeftButton.setOnClickListener(new android.view.View.OnClickListener() {
            public void onClick(android.view.View v) {
                left.setVisibility(android.view.View.INVISIBLE);
            }
        });
        // top right button makes top right button GONE
        final android.widget.Button topRightButton = ((android.widget.Button) (findViewById(R.id.topRightButton)));
        topRightButton.setOnClickListener(new android.view.View.OnClickListener() {
            public void onClick(android.view.View v) {
                topRightButton.setVisibility(android.view.View.GONE);
            }
        });
        // bottom right button makes bottom right button INVISIBLE
        final android.widget.Button bottomRightButton = ((android.widget.Button) (findViewById(R.id.bottomRightButton)));
        bottomRightButton.setOnClickListener(new android.view.View.OnClickListener() {
            public void onClick(android.view.View v) {
                bottomRightButton.setVisibility(android.view.View.INVISIBLE);
            }
        });
    }
}

