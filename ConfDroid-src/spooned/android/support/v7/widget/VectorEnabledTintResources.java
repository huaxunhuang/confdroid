/**
 * Copyright (C) 2015 The Android Open Source Project
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
package android.support.v7.widget;


/**
 * This class allows us to intercept calls so that we can tint resources (if applicable), and
 * inflate vector resources from within drawable containers pre-L.
 *
 * @unknown 
 */
@android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
public class VectorEnabledTintResources extends android.content.res.Resources {
    public static boolean shouldBeUsed() {
        return android.support.v7.app.AppCompatDelegate.isCompatVectorFromResourcesEnabled() && (android.os.Build.VERSION.SDK_INT <= android.support.v7.widget.VectorEnabledTintResources.MAX_SDK_WHERE_REQUIRED);
    }

    /**
     * The maximum API level where this class is needed.
     */
    public static final int MAX_SDK_WHERE_REQUIRED = 20;

    private final java.lang.ref.WeakReference<android.content.Context> mContextRef;

    public VectorEnabledTintResources(@android.support.annotation.NonNull
    final android.content.Context context, @android.support.annotation.NonNull
    final android.content.res.Resources res) {
        super(res.getAssets(), res.getDisplayMetrics(), res.getConfiguration());
        mContextRef = new java.lang.ref.WeakReference<>(context);
    }

    /**
     * We intercept this call so that we tint the result (if applicable). This is needed for
     * things like {@link android.graphics.drawable.DrawableContainer}s which can retrieve
     * their children via this method.
     */
    @java.lang.Override
    public android.graphics.drawable.Drawable getDrawable(int id) throws android.content.res.Resources.NotFoundException {
        final android.content.Context context = mContextRef.get();
        if (context != null) {
            return android.support.v7.widget.AppCompatDrawableManager.get().onDrawableLoadedFromResources(context, this, id);
        } else {
            return super.getDrawable(id);
        }
    }

    final android.graphics.drawable.Drawable superGetDrawable(int id) {
        return super.getDrawable(id);
    }
}

