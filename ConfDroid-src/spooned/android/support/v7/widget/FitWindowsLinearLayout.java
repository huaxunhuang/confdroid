/**
 * Copyright (C) 2014 The Android Open Source Project
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
package android.support.v7.widget;


/**
 *
 *
 * @unknown 
 */
@android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
public class FitWindowsLinearLayout extends android.widget.LinearLayout implements android.support.v7.widget.FitWindowsViewGroup {
    private android.support.v7.widget.FitWindowsViewGroup.OnFitSystemWindowsListener mListener;

    public FitWindowsLinearLayout(android.content.Context context) {
        super(context);
    }

    public FitWindowsLinearLayout(android.content.Context context, android.util.AttributeSet attrs) {
        super(context, attrs);
    }

    @java.lang.Override
    public void setOnFitSystemWindowsListener(android.support.v7.widget.FitWindowsViewGroup.OnFitSystemWindowsListener listener) {
        mListener = listener;
    }

    @java.lang.Override
    protected boolean fitSystemWindows(android.graphics.Rect insets) {
        if (mListener != null) {
            mListener.onFitSystemWindows(insets);
        }
        return super.fitSystemWindows(insets);
    }
}

