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
 * A ClassPresenterSelector selects a {@link Presenter} based on the item's
 * Java class.
 */
public final class ClassPresenterSelector extends android.support.v17.leanback.widget.PresenterSelector {
    private final java.util.ArrayList<android.support.v17.leanback.widget.Presenter> mPresenters = new java.util.ArrayList<android.support.v17.leanback.widget.Presenter>();

    private final java.util.HashMap<java.lang.Class<?>, java.lang.Object> mClassMap = new java.util.HashMap<java.lang.Class<?>, java.lang.Object>();

    /**
     * Sets a presenter to be used for the given class.
     *
     * @param cls
     * 		The data model class to be rendered.
     * @param presenter
     * 		The presenter that renders the objects of the given class.
     * @return This ClassPresenterSelector object.
     */
    public android.support.v17.leanback.widget.ClassPresenterSelector addClassPresenter(java.lang.Class<?> cls, android.support.v17.leanback.widget.Presenter presenter) {
        mClassMap.put(cls, presenter);
        if (!mPresenters.contains(presenter)) {
            mPresenters.add(presenter);
        }
        return this;
    }

    /**
     * Sets a presenter selector to be used for the given class.
     *
     * @param cls
     * 		The data model class to be rendered.
     * @param presenterSelector
     * 		The presenter selector that finds the right presenter for a given
     * 		class.
     * @return This ClassPresenterSelector object.
     */
    public android.support.v17.leanback.widget.ClassPresenterSelector addClassPresenterSelector(java.lang.Class<?> cls, android.support.v17.leanback.widget.PresenterSelector presenterSelector) {
        mClassMap.put(cls, presenterSelector);
        android.support.v17.leanback.widget.Presenter[] innerPresenters = presenterSelector.getPresenters();
        for (int i = 0; i < innerPresenters.length; i++)
            if (!mPresenters.contains(innerPresenters[i])) {
                mPresenters.add(innerPresenters[i]);
            }

        return this;
    }

    @java.lang.Override
    public android.support.v17.leanback.widget.Presenter getPresenter(java.lang.Object item) {
        java.lang.Class<?> cls = item.getClass();
        java.lang.Object presenter = null;
        do {
            presenter = mClassMap.get(cls);
            if (presenter instanceof android.support.v17.leanback.widget.PresenterSelector) {
                android.support.v17.leanback.widget.Presenter innerPresenter = ((android.support.v17.leanback.widget.PresenterSelector) (presenter)).getPresenter(item);
                if (innerPresenter != null) {
                    return innerPresenter;
                }
            }
            cls = cls.getSuperclass();
        } while ((presenter == null) && (cls != null) );
        return ((android.support.v17.leanback.widget.Presenter) (presenter));
    }

    @java.lang.Override
    public android.support.v17.leanback.widget.Presenter[] getPresenters() {
        return mPresenters.toArray(new android.support.v17.leanback.widget.Presenter[mPresenters.size()]);
    }
}

