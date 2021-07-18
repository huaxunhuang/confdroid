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
package android.text.method;


/**
 * Transforms source text into an ALL CAPS string, locale-aware.
 *
 * @unknown 
 */
public class AllCapsTransformationMethod implements android.text.method.TransformationMethod2 {
    private static final java.lang.String TAG = "AllCapsTransformationMethod";

    private boolean mEnabled;

    private java.util.Locale mLocale;

    public AllCapsTransformationMethod(android.content.Context context) {
        mLocale = context.getResources().getConfiguration().locale;
    }

    @java.lang.Override
    public java.lang.CharSequence getTransformation(java.lang.CharSequence source, android.view.View view) {
        if (!mEnabled) {
            android.util.Log.w(android.text.method.AllCapsTransformationMethod.TAG, "Caller did not enable length changes; not transforming text");
            return source;
        }
        if (source == null) {
            return null;
        }
        java.util.Locale locale = null;
        if (view instanceof android.widget.TextView) {
            locale = ((android.widget.TextView) (view)).getTextLocale();
        }
        if (locale == null) {
            locale = mLocale;
        }
        return source.toString().toUpperCase(locale);
    }

    @java.lang.Override
    public void onFocusChanged(android.view.View view, java.lang.CharSequence sourceText, boolean focused, int direction, android.graphics.Rect previouslyFocusedRect) {
    }

    @java.lang.Override
    public void setLengthChangesAllowed(boolean allowLengthChanges) {
        mEnabled = allowLengthChanges;
    }
}

