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
package android.support.v4.app;


/**
 * Pre-Honeycomb versions of the platform don't have {@link View#setSaveFromParentEnabled(boolean)},
 * so instead we insert this between the view and its parent.
 */
class NoSaveStateFrameLayout extends android.widget.FrameLayout {
    static android.view.ViewGroup wrap(android.view.View child) {
        android.support.v4.app.NoSaveStateFrameLayout wrapper = new android.support.v4.app.NoSaveStateFrameLayout(child.getContext());
        android.view.ViewGroup.LayoutParams childParams = child.getLayoutParams();
        if (childParams != null) {
            wrapper.setLayoutParams(childParams);
        }
        android.widget.FrameLayout.LayoutParams lp = new android.widget.FrameLayout.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.MATCH_PARENT);
        child.setLayoutParams(lp);
        wrapper.addView(child);
        return wrapper;
    }

    public NoSaveStateFrameLayout(android.content.Context context) {
        super(context);
    }

    /**
     * Override to prevent freezing of any child views.
     */
    @java.lang.Override
    protected void dispatchSaveInstanceState(android.util.SparseArray<android.os.Parcelable> container) {
        dispatchFreezeSelfOnly(container);
    }

    /**
     * Override to prevent thawing of any child views.
     */
    @java.lang.Override
    protected void dispatchRestoreInstanceState(android.util.SparseArray<android.os.Parcelable> container) {
        dispatchThawSelfOnly(container);
    }
}

