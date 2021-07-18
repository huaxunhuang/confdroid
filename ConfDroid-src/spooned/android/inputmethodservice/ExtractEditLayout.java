/**
 * Copyright (C) 2011 The Android Open Source Project
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
package android.inputmethodservice;


/**
 * ExtractEditLayout provides an ActionMode presentation for the limited screen real estate in
 * extract mode.
 *
 * @unknown 
 */
public class ExtractEditLayout extends android.widget.LinearLayout {
    android.widget.Button mExtractActionButton;

    public ExtractEditLayout(android.content.Context context) {
        super(context);
    }

    public ExtractEditLayout(android.content.Context context, android.util.AttributeSet attrs) {
        super(context, attrs);
    }

    @java.lang.Override
    public void onFinishInflate() {
        super.onFinishInflate();
        mExtractActionButton = ((android.widget.Button) (findViewById(com.android.internal.R.id.inputExtractAction)));
    }
}

