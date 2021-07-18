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


class ActivityOptionsCompat21 {
    private final android.app.ActivityOptions mActivityOptions;

    public static android.support.v4.app.ActivityOptionsCompat21 makeCustomAnimation(android.content.Context context, int enterResId, int exitResId) {
        return new android.support.v4.app.ActivityOptionsCompat21(android.app.ActivityOptions.makeCustomAnimation(context, enterResId, exitResId));
    }

    public static android.support.v4.app.ActivityOptionsCompat21 makeScaleUpAnimation(android.view.View source, int startX, int startY, int startWidth, int startHeight) {
        return new android.support.v4.app.ActivityOptionsCompat21(android.app.ActivityOptions.makeScaleUpAnimation(source, startX, startY, startWidth, startHeight));
    }

    public static android.support.v4.app.ActivityOptionsCompat21 makeThumbnailScaleUpAnimation(android.view.View source, android.graphics.Bitmap thumbnail, int startX, int startY) {
        return new android.support.v4.app.ActivityOptionsCompat21(android.app.ActivityOptions.makeThumbnailScaleUpAnimation(source, thumbnail, startX, startY));
    }

    public static android.support.v4.app.ActivityOptionsCompat21 makeSceneTransitionAnimation(android.app.Activity activity, android.view.View sharedElement, java.lang.String sharedElementName) {
        return new android.support.v4.app.ActivityOptionsCompat21(android.app.ActivityOptions.makeSceneTransitionAnimation(activity, sharedElement, sharedElementName));
    }

    public static android.support.v4.app.ActivityOptionsCompat21 makeSceneTransitionAnimation(android.app.Activity activity, android.view.View[] sharedElements, java.lang.String[] sharedElementNames) {
        android.util.Pair[] pairs = null;
        if (sharedElements != null) {
            pairs = new android.util.Pair[sharedElements.length];
            for (int i = 0; i < pairs.length; i++) {
                pairs[i] = android.util.Pair.create(sharedElements[i], sharedElementNames[i]);
            }
        }
        return new android.support.v4.app.ActivityOptionsCompat21(android.app.ActivityOptions.makeSceneTransitionAnimation(activity, pairs));
    }

    public static android.support.v4.app.ActivityOptionsCompat21 makeTaskLaunchBehind() {
        return new android.support.v4.app.ActivityOptionsCompat21(android.app.ActivityOptions.makeTaskLaunchBehind());
    }

    private ActivityOptionsCompat21(android.app.ActivityOptions activityOptions) {
        mActivityOptions = activityOptions;
    }

    public android.os.Bundle toBundle() {
        return mActivityOptions.toBundle();
    }

    public void update(android.support.v4.app.ActivityOptionsCompat21 otherOptions) {
        mActivityOptions.update(otherOptions.mActivityOptions);
    }
}

