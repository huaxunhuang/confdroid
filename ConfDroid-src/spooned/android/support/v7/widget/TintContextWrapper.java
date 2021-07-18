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
 * A {@link android.content.ContextWrapper} which returns a tint-aware
 * {@link android.content.res.Resources} instance from {@link #getResources()}.
 *
 * @unknown 
 */
@android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
public class TintContextWrapper extends android.content.ContextWrapper {
    private static final java.lang.Object CACHE_LOCK = new java.lang.Object();

    private static java.util.ArrayList<java.lang.ref.WeakReference<android.support.v7.widget.TintContextWrapper>> sCache;

    public static android.content.Context wrap(@android.support.annotation.NonNull
    final android.content.Context context) {
        if (android.support.v7.widget.TintContextWrapper.shouldWrap(context)) {
            synchronized(android.support.v7.widget.TintContextWrapper.CACHE_LOCK) {
                if (android.support.v7.widget.TintContextWrapper.sCache == null) {
                    android.support.v7.widget.TintContextWrapper.sCache = new java.util.ArrayList<>();
                } else {
                    // This is a convenient place to prune any dead reference entries
                    for (int i = android.support.v7.widget.TintContextWrapper.sCache.size() - 1; i >= 0; i--) {
                        final java.lang.ref.WeakReference<android.support.v7.widget.TintContextWrapper> ref = android.support.v7.widget.TintContextWrapper.sCache.get(i);
                        if ((ref == null) || (ref.get() == null)) {
                            android.support.v7.widget.TintContextWrapper.sCache.remove(i);
                        }
                    }
                    // Now check our instance cache
                    for (int i = android.support.v7.widget.TintContextWrapper.sCache.size() - 1; i >= 0; i--) {
                        final java.lang.ref.WeakReference<android.support.v7.widget.TintContextWrapper> ref = android.support.v7.widget.TintContextWrapper.sCache.get(i);
                        final android.support.v7.widget.TintContextWrapper wrapper = (ref != null) ? ref.get() : null;
                        if ((wrapper != null) && (wrapper.getBaseContext() == context)) {
                            return wrapper;
                        }
                    }
                }
                // If we reach here then the cache didn't have a hit, so create a new instance
                // and add it to the cache
                final android.support.v7.widget.TintContextWrapper wrapper = new android.support.v7.widget.TintContextWrapper(context);
                android.support.v7.widget.TintContextWrapper.sCache.add(new java.lang.ref.WeakReference<>(wrapper));
                return wrapper;
            }
        }
        return context;
    }

    private static boolean shouldWrap(@android.support.annotation.NonNull
    final android.content.Context context) {
        if (((context instanceof android.support.v7.widget.TintContextWrapper) || (context.getResources() instanceof android.support.v7.widget.TintResources)) || (context.getResources() instanceof android.support.v7.widget.VectorEnabledTintResources)) {
            // If the Context already has a TintResources[Experimental] impl, no need to wrap again
            // If the Context is already a TintContextWrapper, no need to wrap again
            return false;
        }
        return (android.os.Build.VERSION.SDK_INT < 21) || android.support.v7.widget.VectorEnabledTintResources.shouldBeUsed();
    }

    private final android.content.res.Resources mResources;

    private final android.content.res.Resources.Theme mTheme;

    private TintContextWrapper(@android.support.annotation.NonNull
    final android.content.Context base) {
        super(base);
        if (android.support.v7.widget.VectorEnabledTintResources.shouldBeUsed()) {
            // We need to create a copy of the Theme so that the Theme references our
            // new Resources instance
            mResources = new android.support.v7.widget.VectorEnabledTintResources(this, base.getResources());
            mTheme = mResources.newTheme();
            mTheme.setTo(base.getTheme());
        } else {
            mResources = new android.support.v7.widget.TintResources(this, base.getResources());
            mTheme = null;
        }
    }

    @java.lang.Override
    public android.content.res.Resources.Theme getTheme() {
        return mTheme == null ? super.getTheme() : mTheme;
    }

    @java.lang.Override
    public void setTheme(int resid) {
        if (mTheme == null) {
            super.setTheme(resid);
        } else {
            mTheme.applyStyle(resid, true);
        }
    }

    @java.lang.Override
    public android.content.res.Resources getResources() {
        return mResources;
    }
}

