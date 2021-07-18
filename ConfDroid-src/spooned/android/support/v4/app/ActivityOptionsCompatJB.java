/**
 * Copyright (C) 2012 The Android Open Source Project
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


class ActivityOptionsCompatJB {
    public static android.support.v4.app.ActivityOptionsCompatJB makeCustomAnimation(android.content.Context context, int enterResId, int exitResId) {
        return new android.support.v4.app.ActivityOptionsCompatJB(android.app.ActivityOptions.makeCustomAnimation(context, enterResId, exitResId));
    }

    public static android.support.v4.app.ActivityOptionsCompatJB makeScaleUpAnimation(android.view.View source, int startX, int startY, int startWidth, int startHeight) {
        return new android.support.v4.app.ActivityOptionsCompatJB(android.app.ActivityOptions.makeScaleUpAnimation(source, startX, startY, startWidth, startHeight));
    }

    public static android.support.v4.app.ActivityOptionsCompatJB makeThumbnailScaleUpAnimation(android.view.View source, android.graphics.Bitmap thumbnail, int startX, int startY) {
        return new android.support.v4.app.ActivityOptionsCompatJB(android.app.ActivityOptions.makeThumbnailScaleUpAnimation(source, thumbnail, startX, startY));
    }

    private final android.app.ActivityOptions mActivityOptions;

    private ActivityOptionsCompatJB(android.app.ActivityOptions activityOptions) {
        mActivityOptions = activityOptions;
    }

    public android.os.Bundle toBundle() {
        return mActivityOptions.toBundle();
    }

    public void update(android.support.v4.app.ActivityOptionsCompatJB otherOptions) {
        mActivityOptions.update(otherOptions.mActivityOptions);
    }
}

