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
package android.content.res;


public class ResourceCacheActivity extends android.app.Activity {
    static java.lang.ref.WeakReference<android.content.res.ResourceCacheActivity> lastCreatedInstance;

    @java.lang.Override
    protected void onCreate(@android.annotation.Nullable
    android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        android.content.res.ResourceCacheActivity.lastCreatedInstance = new java.lang.ref.WeakReference<android.content.res.ResourceCacheActivity>(this);
    }

    public static android.content.res.ResourceCacheActivity getLastCreatedInstance() {
        return android.content.res.ResourceCacheActivity.lastCreatedInstance == null ? null : android.content.res.ResourceCacheActivity.lastCreatedInstance.get();
    }
}

