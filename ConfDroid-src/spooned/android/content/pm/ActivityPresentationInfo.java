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
package android.content.pm;


/**
 * Holds basic information about an activity.
 *
 * @unknown 
 */
public final class ActivityPresentationInfo {
    public final int taskId;

    public final int displayId;

    @android.annotation.NonNull
    public final android.content.ComponentName componentName;

    public ActivityPresentationInfo(int taskId, int displayId, @android.annotation.NonNull
    android.content.ComponentName componentName) {
        this.taskId = taskId;
        this.displayId = displayId;
        this.componentName = componentName;
    }
}

