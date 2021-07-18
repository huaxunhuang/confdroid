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
 * limitations under the License
 */
package android.support.v17.leanback.widget;


/**
 * Helper for static (nine patch) shadows.
 */
final class StaticShadowHelper {
    static final android.support.v17.leanback.widget.StaticShadowHelper sInstance = new android.support.v17.leanback.widget.StaticShadowHelper();

    boolean mSupportsShadow;

    android.support.v17.leanback.widget.StaticShadowHelper.ShadowHelperVersionImpl mImpl;

    /**
     * Interface implemented by classes that support Shadow.
     */
    static interface ShadowHelperVersionImpl {
        public void prepareParent(android.view.ViewGroup parent);

        public java.lang.Object addStaticShadow(android.view.ViewGroup shadowContainer);

        public void setShadowFocusLevel(java.lang.Object impl, float level);
    }

    /**
     * Interface used when we do not support Shadow animations.
     */
    private static final class ShadowHelperStubImpl implements android.support.v17.leanback.widget.StaticShadowHelper.ShadowHelperVersionImpl {
        ShadowHelperStubImpl() {
        }

        @java.lang.Override
        public void prepareParent(android.view.ViewGroup parent) {
            // do nothing
        }

        @java.lang.Override
        public java.lang.Object addStaticShadow(android.view.ViewGroup shadowContainer) {
            // do nothing
            return null;
        }

        @java.lang.Override
        public void setShadowFocusLevel(java.lang.Object impl, float level) {
            // do nothing
        }
    }

    /**
     * Implementation used on JBMR2 (and above).
     */
    private static final class ShadowHelperJbmr2Impl implements android.support.v17.leanback.widget.StaticShadowHelper.ShadowHelperVersionImpl {
        ShadowHelperJbmr2Impl() {
        }

        @java.lang.Override
        public void prepareParent(android.view.ViewGroup parent) {
            android.support.v17.leanback.widget.ShadowHelperJbmr2.prepareParent(parent);
        }

        @java.lang.Override
        public java.lang.Object addStaticShadow(android.view.ViewGroup shadowContainer) {
            return android.support.v17.leanback.widget.ShadowHelperJbmr2.addShadow(shadowContainer);
        }

        @java.lang.Override
        public void setShadowFocusLevel(java.lang.Object impl, float level) {
            android.support.v17.leanback.widget.ShadowHelperJbmr2.setShadowFocusLevel(impl, level);
        }
    }

    /**
     * Returns the StaticShadowHelper.
     */
    private StaticShadowHelper() {
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            mSupportsShadow = true;
            mImpl = new android.support.v17.leanback.widget.StaticShadowHelper.ShadowHelperJbmr2Impl();
        } else {
            mSupportsShadow = false;
            mImpl = new android.support.v17.leanback.widget.StaticShadowHelper.ShadowHelperStubImpl();
        }
    }

    public static android.support.v17.leanback.widget.StaticShadowHelper getInstance() {
        return android.support.v17.leanback.widget.StaticShadowHelper.sInstance;
    }

    public boolean supportsShadow() {
        return mSupportsShadow;
    }

    public void prepareParent(android.view.ViewGroup parent) {
        mImpl.prepareParent(parent);
    }

    public java.lang.Object addStaticShadow(android.view.ViewGroup shadowContainer) {
        return mImpl.addStaticShadow(shadowContainer);
    }

    public void setShadowFocusLevel(java.lang.Object impl, float level) {
        mImpl.setShadowFocusLevel(impl, level);
    }
}

