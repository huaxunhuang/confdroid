/**
 * Copyright (C) 2014 The Android Open Source Project
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
 * A {@link PresenterSelector} that always returns the same {@link Presenter}.
 * Useful for rows of items of the same type that are all rendered the same way.
 */
public final class SinglePresenterSelector extends android.support.v17.leanback.widget.PresenterSelector {
    private final android.support.v17.leanback.widget.Presenter mPresenter;

    /**
     *
     *
     * @param presenter
     * 		The Presenter to return for every item.
     */
    public SinglePresenterSelector(android.support.v17.leanback.widget.Presenter presenter) {
        mPresenter = presenter;
    }

    @java.lang.Override
    public android.support.v17.leanback.widget.Presenter getPresenter(java.lang.Object item) {
        return mPresenter;
    }

    @java.lang.Override
    public android.support.v17.leanback.widget.Presenter[] getPresenters() {
        return new android.support.v17.leanback.widget.Presenter[]{ mPresenter };
    }
}

