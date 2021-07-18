/**
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */
package android.content.res;


/**
 * Version of resources generated for apps targeting <26.
 *
 * @unknown 
 */
public class CompatResources extends android.content.res.Resources {
    private java.lang.ref.WeakReference<android.content.Context> mContext;

    public CompatResources(java.lang.ClassLoader cls) {
        super(cls);
        mContext = new java.lang.ref.WeakReference<>(null);
    }

    /**
     *
     *
     * @unknown 
     */
    public void setContext(android.content.Context context) {
        mContext = new java.lang.ref.WeakReference<>(context);
    }

    @java.lang.Override
    public android.graphics.drawable.Drawable getDrawable(@android.annotation.DrawableRes
    int id) throws android.content.res.Resources.NotFoundException {
        return getDrawable(id, getTheme());
    }

    @java.lang.Override
    public android.graphics.drawable.Drawable getDrawableForDensity(@android.annotation.DrawableRes
    int id, int density) throws android.content.res.Resources.NotFoundException {
        return getDrawableForDensity(id, density, getTheme());
    }

    @java.lang.Override
    public int getColor(@android.annotation.ColorRes
    int id) throws android.content.res.Resources.NotFoundException {
        return getColor(id, getTheme());
    }

    @java.lang.Override
    public android.content.res.ColorStateList getColorStateList(@android.annotation.ColorRes
    int id) throws android.content.res.Resources.NotFoundException {
        return getColorStateList(id, getTheme());
    }

    private android.content.res.Resources.Theme getTheme() {
        android.content.Context c = mContext.get();
        return c != null ? c.getTheme() : null;
    }
}

