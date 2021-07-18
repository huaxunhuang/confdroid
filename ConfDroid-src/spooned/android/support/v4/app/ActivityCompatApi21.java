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
package android.support.v4.app;


class ActivityCompatApi21 {
    public static void setMediaController(android.app.Activity activity, java.lang.Object mediaControllerObj) {
        activity.setMediaController(((android.media.session.MediaController) (mediaControllerObj)));
    }

    public static void finishAfterTransition(android.app.Activity activity) {
        activity.finishAfterTransition();
    }

    public static void setEnterSharedElementCallback(android.app.Activity activity, android.support.v4.app.ActivityCompatApi21.SharedElementCallback21 callback) {
        activity.setEnterSharedElementCallback(android.support.v4.app.ActivityCompatApi21.createCallback(callback));
    }

    public static void setExitSharedElementCallback(android.app.Activity activity, android.support.v4.app.ActivityCompatApi21.SharedElementCallback21 callback) {
        activity.setExitSharedElementCallback(android.support.v4.app.ActivityCompatApi21.createCallback(callback));
    }

    public static void postponeEnterTransition(android.app.Activity activity) {
        activity.postponeEnterTransition();
    }

    public static void startPostponedEnterTransition(android.app.Activity activity) {
        activity.startPostponedEnterTransition();
    }

    public static abstract class SharedElementCallback21 {
        public abstract void onSharedElementStart(java.util.List<java.lang.String> sharedElementNames, java.util.List<android.view.View> sharedElements, java.util.List<android.view.View> sharedElementSnapshots);

        public abstract void onSharedElementEnd(java.util.List<java.lang.String> sharedElementNames, java.util.List<android.view.View> sharedElements, java.util.List<android.view.View> sharedElementSnapshots);

        public abstract void onRejectSharedElements(java.util.List<android.view.View> rejectedSharedElements);

        public abstract void onMapSharedElements(java.util.List<java.lang.String> names, java.util.Map<java.lang.String, android.view.View> sharedElements);

        public abstract android.os.Parcelable onCaptureSharedElementSnapshot(android.view.View sharedElement, android.graphics.Matrix viewToGlobalMatrix, android.graphics.RectF screenBounds);

        public abstract android.view.View onCreateSnapshotView(android.content.Context context, android.os.Parcelable snapshot);
    }

    private static android.app.SharedElementCallback createCallback(android.support.v4.app.ActivityCompatApi21.SharedElementCallback21 callback) {
        android.app.SharedElementCallback newListener = null;
        if (callback != null) {
            newListener = new android.support.v4.app.ActivityCompatApi21.SharedElementCallbackImpl(callback);
        }
        return newListener;
    }

    private static class SharedElementCallbackImpl extends android.app.SharedElementCallback {
        private android.support.v4.app.ActivityCompatApi21.SharedElementCallback21 mCallback;

        public SharedElementCallbackImpl(android.support.v4.app.ActivityCompatApi21.SharedElementCallback21 callback) {
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
    }
}

