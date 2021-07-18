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


class ActivityOptionsCompat24 {
    public static android.support.v4.app.ActivityOptionsCompat24 makeCustomAnimation(android.content.Context context, int enterResId, int exitResId) {
        return new android.support.v4.app.ActivityOptionsCompat24(android.app.ActivityOptions.makeCustomAnimation(context, enterResId, exitResId));
    }

    public static android.support.v4.app.ActivityOptionsCompat24 makeScaleUpAnimation(android.view.View source, int startX, int startY, int startWidth, int startHeight) {
        return new android.support.v4.app.ActivityOptionsCompat24(android.app.ActivityOptions.makeScaleUpAnimation(source, startX, startY, startWidth, startHeight));
    }

    public static android.support.v4.app.ActivityOptionsCompat24 makeThumbnailScaleUpAnimation(android.view.View source, android.graphics.Bitmap thumbnail, int startX, int startY) {
        return new android.support.v4.app.ActivityOptionsCompat24(android.app.ActivityOptions.makeThumbnailScaleUpAnimation(source, thumbnail, startX, startY));
    }

    public static android.support.v4.app.ActivityOptionsCompat24 makeSceneTransitionAnimation(android.app.Activity activity, android.view.View sharedElement, java.lang.String sharedElementName) {
        return new android.support.v4.app.ActivityOptionsCompat24(android.app.ActivityOptions.makeSceneTransitionAnimation(activity, sharedElement, sharedElementName));
    }

    public static android.support.v4.app.ActivityOptionsCompat24 makeSceneTransitionAnimation(android.app.Activity activity, android.view.View[] sharedElements, java.lang.String[] sharedElementNames) {
        android.util.Pair[] pairs = null;
        if (sharedElements != null) {
            pairs = new android.util.Pair[sharedElements.length];
            for (int i = 0; i < pairs.length; i++) {
                pairs[i] = android.util.Pair.create(sharedElements[i], sharedElementNames[i]);
            }
        }
        return new android.support.v4.app.ActivityOptionsCompat24(android.app.ActivityOptions.makeSceneTransitionAnimation(activity, pairs));
    }

    public static android.support.v4.app.ActivityOptionsCompat24 makeClipRevealAnimation(android.view.View source, int startX, int startY, int width, int height) {
        return new android.support.v4.app.ActivityOptionsCompat24(android.app.ActivityOptions.makeClipRevealAnimation(source, startX, startY, width, height));
    }

    public static android.support.v4.app.ActivityOptionsCompat24 makeTaskLaunchBehind() {
        return new android.support.v4.app.ActivityOptionsCompat24(android.app.ActivityOptions.makeTaskLaunchBehind());
    }

    public static android.support.v4.app.ActivityOptionsCompat24 makeBasic() {
        return new android.support.v4.app.ActivityOptionsCompat24(android.app.ActivityOptions.makeBasic());
    }

    private final android.app.ActivityOptions mActivityOptions;

    private ActivityOptionsCompat24(android.app.ActivityOptions activityOptions) {
        mActivityOptions = activityOptions;
    }

    public android.support.v4.app.ActivityOptionsCompat24 setLaunchBounds(@android.support.annotation.Nullable
    android.graphics.Rect screenSpacePixelRect) {
        return new android.support.v4.app.ActivityOptionsCompat24(mActivityOptions.setLaunchBounds(screenSpacePixelRect));
    }

    public android.graphics.Rect getLaunchBounds() {
        return mActivityOptions.getLaunchBounds();
    }

    public android.os.Bundle toBundle() {
        return mActivityOptions.toBundle();
    }

    public void update(android.support.v4.app.ActivityOptionsCompat24 otherOptions) {
        mActivityOptions.update(otherOptions.mActivityOptions);
    }

    public void requestUsageTimeReport(android.app.PendingIntent receiver) {
        mActivityOptions.requestUsageTimeReport(receiver);
    }
}

