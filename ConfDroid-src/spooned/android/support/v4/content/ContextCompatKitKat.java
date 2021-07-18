/**
 * Copyright (C) 2013 The Android Open Source Project
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
package android.support.v4.content;


class ContextCompatKitKat {
    public static java.io.File[] getExternalCacheDirs(android.content.Context context) {
        return context.getExternalCacheDirs();
    }

    public static java.io.File[] getExternalFilesDirs(android.content.Context context, java.lang.String type) {
        return context.getExternalFilesDirs(type);
    }

    public static java.io.File[] getObbDirs(android.content.Context context) {
        return context.getObbDirs();
    }
}

