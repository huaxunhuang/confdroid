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
package android.support.v4.app;


class ActivityCompatApi23 {
    public interface OnSharedElementsReadyListenerBridge {
        void onSharedElementsReady();
    }

    public interface RequestPermissionsRequestCodeValidator {
        void validateRequestPermissionsRequestCode(int requestCode);
    }

    public static void requestPermissions(android.app.Activity activity, java.lang.String[] permissions, int requestCode) {
        if (activity instanceof android.support.v4.app.ActivityCompatApi23.RequestPermissionsRequestCodeValidator) {
            ((android.support.v4.app.ActivityCompatApi23.RequestPermissionsRequestCodeValidator) (activity)).validateRequestPermissionsRequestCode(requestCode);
        }
        activity.requestPermissions(permissions, requestCode);
    }

    public static boolean shouldShowRequestPermissionRationale(android.app.Activity activity, java.lang.String permission) {
        return activity.shouldShowRequestPermissionRationale(permission);
    }

    public static void setEnterSharedElementCallback(android.app.Activity activity, android.support.v4.app.ActivityCompatApi23.SharedElementCallback23 callback) {
        activity.setEnterSharedElementCallback(android.support.v4.app.ActivityCompatApi23.createCallback(callback));
    }

    public static void setExitSharedElementCallback(android.app.Activity activity, android.support.v4.app.ActivityCompatApi23.SharedElementCallback23 callback) {
        activity.setExitSharedElementCallback(android.support.v4.app.ActivityCompatApi23.createCallback(callback));
    }

    private static android.app.SharedElementCallback createCallback(android.support.v4.app.ActivityCompatApi23.SharedElementCallback23 callback) {
        android.app.SharedElementCallback newListener = null;
        if (callback != null) {
            newListener = new android.support.v4.app.ActivityCompatApi23.SharedElementCallbackImpl(callback);
        }
        return newListener;
    }

    public static abstract class SharedElementCallback23 extends android.support.v4.app.ActivityCompatApi21.SharedElementCallback21 {
        public abstract void onSharedElementsArrived(java.util.List<java.lang.String> sharedElementNames, java.util.List<android.view.View> sharedElements, android.support.v4.app.ActivityCompatApi23.OnSharedElementsReadyListenerBridge listener);
    }

    private static class SharedElementCallbackImpl extends android.app.SharedElementCallback {
        private android.support.v4.app.ActivityCompatApi23.SharedElementCallback23 mCallback;

        public SharedElementCallbackImpl(android.support.v4.app.ActivityCompatApi23.SharedElementCallback23 callback) {
            mCallback = callback;
        }

        @java.lang.Override
        public void onSharedElementStart(java.util.List<java.lang.String> sharedElementNames, java.util.List<android.view.View> sharedElements, java.util.List<android.view.View> sharedElementSnapshots) {
            mCallback.onSharedElementStart(sharedElementNames, sharedElements, sharedElementSnapshots);
        }

        @java.lang.Override
        public void onSharedElementEnd(java.util.List<java.lang.String> sharedElementNames, java.util.List<android.view.View> sharedElements, java.util.List<android.view.View> sharedElementSnapshots) {
            mCallback.onSharedElementEnd(sharedElementNames, sharedElements, sharedElementSnapshots);
        }

        @java.lang.Override
        public void onRejectSharedElements(java.util.List<android.view.View> rejectedSharedElements) {
            mCallback.onRejectSharedElements(rejectedSharedElements);
        }

        @java.lang.Override
        public void onMapSharedElements(java.util.List<java.lang.String> names, java.util.Map<java.lang.String, android.view.View> sharedElements) {
            mCallback.onMapSharedElements(names, sharedElements);
        }

        @java.lang.Override
        public android.os.Parcelable onCaptureSharedElementSnapshot(android.view.View sharedElement, android.graphics.Matrix viewToGlobalMatrix, android.graphics.RectF screenBounds) {
            return mCallback.onCaptureSharedElementSnapshot(sharedElement, viewToGlobalMatrix, screenBounds);
        }

        @java.lang.Override
        public android.view.View onCreateSnapshotView(android.content.Context context, android.os.Parcelable snapshot) {
            return mCallback.onCreateSnapshotView(context, snapshot);
        }

        @java.lang.Override
        public void onSharedElementsArrived(java.util.List<java.lang.String> sharedElementNames, java.util.List<android.view.View> sharedElements, final android.app.SharedElementCallback.OnSharedElementsReadyListener listener) {
            mCallback.onSharedElementsArrived(sharedElementNames, sharedElements, new android.support.v4.app.ActivityCompatApi23.OnSharedElementsReadyListenerBridge() {
                @java.lang.Override
                public void onSharedElementsReady() {
                    listener.onSharedElementsReady();
                }
            });
        }
    }
}

