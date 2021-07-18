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
package android.support.v4.content;


class ContextCompatApi21 {
    public static android.graphics.drawable.Drawable getDrawable(android.content.Context context, int id) {
        return context.getDrawable(id);
    }

    public static java.io.File getNoBackupFilesDir(android.content.Context context) {
        return context.getNoBackupFilesDir();
    }

    public static java.io.File getCodeCacheDir(android.content.Context context) {
        return context.getCodeCacheDir();
    }
}

