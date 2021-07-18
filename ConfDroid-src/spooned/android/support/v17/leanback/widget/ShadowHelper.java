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
 * Helper for shadow.
 */
final class ShadowHelper {
    static final android.support.v17.leanback.widget.ShadowHelper sInstance = new android.support.v17.leanback.widget.ShadowHelper();

    boolean mSupportsDynamicShadow;

    android.support.v17.leanback.widget.ShadowHelper.ShadowHelperVersionImpl mImpl;

    /**
     * Interface implemented by classes that support Shadow.
     */
    static interface ShadowHelperVersionImpl {
        public java.lang.Object addDynamicShadow(android.view.View shadowContainer, float unfocusedZ, float focusedZ, int roundedCornerRadius);

        public void setZ(android.view.View view, float z);

        public void setShadowFocusLevel(java.lang.Object impl, float level);
    }

    /**
     * Interface used when we do not support Shadow animations.
     */
    private static final class ShadowHelperStubImpl implements android.support.v17.leanback.widget.ShadowHelper.ShadowHelperVersionImpl {
        ShadowHelperStubImpl() {
        }

        @java.lang.Override
        public java.lang.Object addDynamicShadow(android.view.View shadowContainer, float focusedZ, float unfocusedZ, int roundedCornerRadius) {
            // do nothing
            return null;
        }

        @java.lang.Override
        public void setShadowFocusLevel(java.lang.Object impl, float level) {
            // do nothing
        }

        @java.lang.Override
        public void setZ(android.view.View view, float z) {
            // do nothing
        }
    }

    /**
     * Implementation used on api 21 (and above).
     */
    private static final class ShadowHelperApi21Impl implements android.support.v17.leanback.widget.ShadowHelper.ShadowHelperVersionImpl {
        ShadowHelperApi21Impl() {
        }

        @java.lang.Override
        public java.lang.Object addDynamicShadow(android.view.View shadowContainer, float unfocusedZ, float focusedZ, int roundedCornerRadius) {
            return android.support.v17.leanback.widget.ShadowHelperApi21.addDynamicShadow(shadowContainer, unfocusedZ, focusedZ, roundedCornerRadius);
        }

        @java.lang.Override
        public void setShadowFocusLevel(java.lang.Object impl, float level) {
            android.support.v17.leanback.widget.ShadowHelperApi21.setShadowFocusLevel(impl, level);
        }

        @java.lang.Override
        public void setZ(android.view.View view, float z) {
            android.support.v17.leanback.widget.ShadowHelperApi21.setZ(view, z);
        }
    }

    /**
     * Returns the ShadowHelper.
     */
    private ShadowHelper() {
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            mSupportsDynamicShadow = true;
            mImpl = new android.support.v17.leanback.widget.ShadowHelper.ShadowHelperApi21Impl();
        } else {
            mImpl = new android.support.v17.leanback.widget.ShadowHelper.ShadowHelperStubImpl();
        }
    }

    public static android.support.v17.leanback.widget.ShadowHelper getInstance() {
        return android.support.v17.leanback.widget.ShadowHelper.sInstance;
    }

    public boolean supportsDynamicShadow() {
        return mSupportsDynamicShadow;
    }

    public java.lang.Object addDynamicShadow(android.view.View shadowContainer, float unfocusedZ, float focusedZ, int roundedCornerRadius) {
        return mImpl.addDynamicShadow(shadowContainer, unfocusedZ, focusedZ, roundedCornerRadius);
    }

    public void setShadowFocusLevel(java.lang.Object impl, float level) {
        mImpl.setShadowFocusLevel(impl, level);
    }

    /**
     * Set the view z coordinate.
     */
    public void setZ(android.view.View view, float z) {
        mImpl.setZ(view, z);
    }
}

