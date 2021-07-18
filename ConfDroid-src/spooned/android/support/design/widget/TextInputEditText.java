/**
 * Copyright (C) 2016 The Android Open Source Project
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
package android.support.design.widget;


/**
 * A special sub-class of {@link android.widget.EditText} designed for use as a child of
 * {@link TextInputLayout}.
 *
 * <p>Using this class allows us to display a hint in the IME when in 'extract' mode.</p>
 */
public class TextInputEditText extends android.support.v7.widget.AppCompatEditText {
    public TextInputEditText(android.content.Context context) {
        super(context);
    }

    public TextInputEditText(android.content.Context context, android.util.AttributeSet attrs) {
        super(context, attrs);
    }

    public TextInputEditText(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @java.lang.Override
    public android.view.inputmethod.InputConnection onCreateInputConnection(android.view.inputmethod.EditorInfo outAttrs) {
        final android.view.inputmethod.InputConnection ic = super.onCreateInputConnection(outAttrs);
        if ((ic != null) && (outAttrs.hintText == null)) {
            // If we don't have a hint and our parent is a TextInputLayout, use it's hint for the
            // EditorInfo. This allows us to display a hint in 'extract mode'.
            android.view.ViewParent parent = getParent();
            while (parent instanceof android.view.View) {
                if (parent instanceof android.support.design.widget.TextInputLayout) {
                    outAttrs.hintText = ((android.support.design.widget.TextInputLayout) (parent)).getHint();
                    break;
                }
                parent = parent.getParent();
            } 
        }
        return ic;
    }
}

