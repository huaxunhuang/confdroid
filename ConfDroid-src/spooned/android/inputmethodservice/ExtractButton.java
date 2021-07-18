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
package android.inputmethodservice;


/**
 * Specialization of {@link Button} that ignores the window not being focused.
 */
class ExtractButton extends android.widget.Button {
    public ExtractButton(android.content.Context context) {
        super(context, null);
    }

    public ExtractButton(android.content.Context context, android.util.AttributeSet attrs) {
        super(context, attrs, com.android.internal.R.attr.buttonStyle);
    }

    public ExtractButton(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public ExtractButton(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    /**
     * Pretend like the window this view is in always has focus, so it will
     * highlight when selected.
     */
    @java.lang.Override
    public boolean hasWindowFocus() {
        return isEnabled() && (getVisibility() == android.view.View.VISIBLE) ? true : false;
    }
}

