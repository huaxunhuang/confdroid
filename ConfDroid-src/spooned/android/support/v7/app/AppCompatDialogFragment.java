/**
 * Copyright (C) 2015 The Android Open Source Project
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
package android.support.v7.app;


/**
 * A special version of {@link DialogFragment} which uses an {@link AppCompatDialog} in place of a
 * platform-styled dialog.
 *
 * @see DialogFragment
 */
public class AppCompatDialogFragment extends android.support.v4.app.DialogFragment {
    @java.lang.Override
    public android.app.Dialog onCreateDialog(android.os.Bundle savedInstanceState) {
        return new android.support.v7.app.AppCompatDialog(getContext(), getTheme());
    }

    /**
     *
     *
     * @unknown 
     */
    @android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
    @java.lang.Override
    public void setupDialog(android.app.Dialog dialog, int style) {
        if (dialog instanceof android.support.v7.app.AppCompatDialog) {
            // If the dialog is an AppCompatDialog, we'll handle it
            android.support.v7.app.AppCompatDialog acd = ((android.support.v7.app.AppCompatDialog) (dialog));
            switch (style) {
                case android.support.v4.app.DialogFragment.STYLE_NO_INPUT :
                    dialog.getWindow().addFlags(android.view.WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | android.view.WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    // fall through...
                case android.support.v4.app.DialogFragment.STYLE_NO_FRAME :
                case android.support.v4.app.DialogFragment.STYLE_NO_TITLE :
                    acd.supportRequestWindowFeature(android.view.Window.FEATURE_NO_TITLE);
            }
        } else {
            // Else, just let super handle it
            super.setupDialog(dialog, style);
        }
    }
}

