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
package android.support.v4.content.res;


class ResourcesCompatApi21 {
    public static android.graphics.drawable.Drawable getDrawable(android.content.res.Resources res, int id, android.content.res.Resources.Theme theme) throws android.content.res.Resources.NotFoundException {
        return res.getDrawable(id, theme);
    }

    public static android.graphics.drawable.Drawable getDrawableForDensity(android.content.res.Resources res, int id, int density, android.content.res.Resources.Theme theme) throws android.content.res.Resources.NotFoundException {
        return res.getDrawableForDensity(id, density, theme);
    }
}

