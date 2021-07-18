/**
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package android.support.v17.leanback.widget;


/**
 *
 *
 * @unknown 
 */
@android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
public class InvisibleRowPresenter extends android.support.v17.leanback.widget.RowPresenter {
    public InvisibleRowPresenter() {
        setHeaderPresenter(null);
    }

    @java.lang.Override
    protected android.support.v17.leanback.widget.RowPresenter.ViewHolder createRowViewHolder(android.view.ViewGroup parent) {
        android.widget.RelativeLayout root = new android.widget.RelativeLayout(parent.getContext());
        root.setLayoutParams(new android.widget.RelativeLayout.LayoutParams(0, 0));
        return new android.support.v17.leanback.widget.RowPresenter.ViewHolder(root);
    }
}

