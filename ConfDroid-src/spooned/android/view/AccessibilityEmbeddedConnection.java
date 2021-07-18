/**
 * Copyright (C) 2019 The Android Open Source Project
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
package android.view;


/**
 * This class is an interface this ViewRootImpl provides to the host view to the latter
 * can interact with the view hierarchy in SurfaceControlViewHost.
 *
 * @unknown 
 */
final class AccessibilityEmbeddedConnection extends android.view.accessibility.IAccessibilityEmbeddedConnection.Stub {
    private final java.lang.ref.WeakReference<android.view.ViewRootImpl> mViewRootImpl;

    private final android.graphics.Matrix mTmpScreenMatrix = new android.graphics.Matrix();

    AccessibilityEmbeddedConnection(android.view.ViewRootImpl viewRootImpl) {
        mViewRootImpl = new java.lang.ref.WeakReference<>(viewRootImpl);
    }

    @java.lang.Override
    @android.annotation.Nullable
    public android.os.IBinder associateEmbeddedHierarchy(@android.annotation.NonNull
    android.os.IBinder host, int hostViewId) {
        final android.view.ViewRootImpl viewRootImpl = mViewRootImpl.get();
        if (viewRootImpl != null) {
            final android.view.accessibility.AccessibilityManager accessibilityManager = android.view.accessibility.AccessibilityManager.getInstance(viewRootImpl.mContext);
            viewRootImpl.mAttachInfo.mLeashedParentToken = host;
            viewRootImpl.mAttachInfo.mLeashedParentAccessibilityViewId = hostViewId;
            if (accessibilityManager.isEnabled()) {
                accessibilityManager.associateEmbeddedHierarchy(host, viewRootImpl.mLeashToken);
            }
            return viewRootImpl.mLeashToken;
        }
        return null;
    }

    @java.lang.Override
    public void disassociateEmbeddedHierarchy() {
        final android.view.ViewRootImpl viewRootImpl = mViewRootImpl.get();
        if (viewRootImpl != null) {
            final android.view.accessibility.AccessibilityManager accessibilityManager = android.view.accessibility.AccessibilityManager.getInstance(viewRootImpl.mContext);
            viewRootImpl.mAttachInfo.mLeashedParentToken = null;
            viewRootImpl.mAttachInfo.mLeashedParentAccessibilityViewId = android.view.View.NO_ID;
            viewRootImpl.mAttachInfo.mLocationInParentDisplay.set(0, 0);
            if (accessibilityManager.isEnabled()) {
                accessibilityManager.disassociateEmbeddedHierarchy(viewRootImpl.mLeashToken);
            }
        }
    }

    @java.lang.Override
    public void setScreenMatrix(float[] matrixValues) {
        final android.view.ViewRootImpl viewRootImpl = mViewRootImpl.get();
        if (viewRootImpl != null) {
            mTmpScreenMatrix.setValues(matrixValues);
            if (viewRootImpl.mAttachInfo.mScreenMatrixInEmbeddedHierarchy == null) {
                viewRootImpl.mAttachInfo.mScreenMatrixInEmbeddedHierarchy = new android.graphics.Matrix();
            }
            viewRootImpl.mAttachInfo.mScreenMatrixInEmbeddedHierarchy.set(mTmpScreenMatrix);
        }
    }
}

