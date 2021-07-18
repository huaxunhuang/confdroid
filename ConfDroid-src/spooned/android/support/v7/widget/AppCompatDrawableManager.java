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
package android.support.v7.widget;


/**
 *
 *
 * @unknown 
 */
@android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
public final class AppCompatDrawableManager {
    private interface InflateDelegate {
        android.graphics.drawable.Drawable createFromXmlInner(@android.support.annotation.NonNull
        android.content.Context context, @android.support.annotation.NonNull
        org.xmlpull.v1.XmlPullParser parser, @android.support.annotation.NonNull
        android.util.AttributeSet attrs, @android.support.annotation.Nullable
        android.content.res.Resources.Theme theme);
    }

    private static final java.lang.String TAG = "AppCompatDrawableManager";

    private static final boolean DEBUG = false;

    private static final android.graphics.PorterDuff.Mode DEFAULT_MODE = android.graphics.PorterDuff.Mode.SRC_IN;

    private static final java.lang.String SKIP_DRAWABLE_TAG = "appcompat_skip_skip";

    private static final java.lang.String PLATFORM_VD_CLAZZ = "android.graphics.drawable.VectorDrawable";

    private static android.support.v7.widget.AppCompatDrawableManager INSTANCE;

    public static android.support.v7.widget.AppCompatDrawableManager get() {
        if (android.support.v7.widget.AppCompatDrawableManager.INSTANCE == null) {
            android.support.v7.widget.AppCompatDrawableManager.INSTANCE = new android.support.v7.widget.AppCompatDrawableManager();
            android.support.v7.widget.AppCompatDrawableManager.installDefaultInflateDelegates(android.support.v7.widget.AppCompatDrawableManager.INSTANCE);
        }
        return android.support.v7.widget.AppCompatDrawableManager.INSTANCE;
    }

    private static void installDefaultInflateDelegates(@android.support.annotation.NonNull
    android.support.v7.widget.AppCompatDrawableManager manager) {
        final int sdk = android.os.Build.VERSION.SDK_INT;
        // This sdk version check will affect src:appCompat code path.
        // Although VectorDrawable exists in Android framework from Lollipop, AppCompat will use the
        // VectorDrawableCompat before Nougat to utilize the bug fixes in VectorDrawableCompat.
        if (sdk < 24) {
            manager.addDelegate("vector", new android.support.v7.widget.AppCompatDrawableManager.VdcInflateDelegate());
            if (sdk >= 11) {
                // AnimatedVectorDrawableCompat only works on API v11+
                manager.addDelegate("animated-vector", new android.support.v7.widget.AppCompatDrawableManager.AvdcInflateDelegate());
            }
        }
    }

    private static final android.support.v7.widget.AppCompatDrawableManager.ColorFilterLruCache COLOR_FILTER_CACHE = new android.support.v7.widget.AppCompatDrawableManager.ColorFilterLruCache(6);

    /**
     * Drawables which should be tinted with the value of {@code R.attr.colorControlNormal},
     * using the default mode using a raw color filter.
     */
    private static final int[] COLORFILTER_TINT_COLOR_CONTROL_NORMAL = new int[]{ R.drawable.abc_textfield_search_default_mtrl_alpha, R.drawable.abc_textfield_default_mtrl_alpha, R.drawable.abc_ab_share_pack_mtrl_alpha };

    /**
     * Drawables which should be tinted with the value of {@code R.attr.colorControlNormal}, using
     * {@link DrawableCompat}'s tinting functionality.
     */
    private static final int[] TINT_COLOR_CONTROL_NORMAL = new int[]{ R.drawable.abc_ic_commit_search_api_mtrl_alpha, R.drawable.abc_seekbar_tick_mark_material, R.drawable.abc_ic_menu_share_mtrl_alpha, R.drawable.abc_ic_menu_copy_mtrl_am_alpha, R.drawable.abc_ic_menu_cut_mtrl_alpha, R.drawable.abc_ic_menu_selectall_mtrl_alpha, R.drawable.abc_ic_menu_paste_mtrl_am_alpha };

    /**
     * Drawables which should be tinted with the value of {@code R.attr.colorControlActivated},
     * using a color filter.
     */
    private static final int[] COLORFILTER_COLOR_CONTROL_ACTIVATED = new int[]{ R.drawable.abc_textfield_activated_mtrl_alpha, R.drawable.abc_textfield_search_activated_mtrl_alpha, R.drawable.abc_cab_background_top_mtrl_alpha, R.drawable.abc_text_cursor_material, R.drawable.abc_text_select_handle_left_mtrl_dark, R.drawable.abc_text_select_handle_middle_mtrl_dark, R.drawable.abc_text_select_handle_right_mtrl_dark, R.drawable.abc_text_select_handle_left_mtrl_light, R.drawable.abc_text_select_handle_middle_mtrl_light, R.drawable.abc_text_select_handle_right_mtrl_light };

    /**
     * Drawables which should be tinted with the value of {@code android.R.attr.colorBackground},
     * using the {@link android.graphics.PorterDuff.Mode#MULTIPLY} mode and a color filter.
     */
    private static final int[] COLORFILTER_COLOR_BACKGROUND_MULTIPLY = new int[]{ R.drawable.abc_popup_background_mtrl_mult, R.drawable.abc_cab_background_internal_bg, R.drawable.abc_menu_hardkey_panel_mtrl_mult };

    /**
     * Drawables which should be tinted using a state list containing values of
     * {@code R.attr.colorControlNormal} and {@code R.attr.colorControlActivated}
     */
    private static final int[] TINT_COLOR_CONTROL_STATE_LIST = new int[]{ R.drawable.abc_tab_indicator_material, R.drawable.abc_textfield_search_material };

    /**
     * Drawables which should be tinted using a state list containing values of
     * {@code R.attr.colorControlNormal} and {@code R.attr.colorControlActivated} for the checked
     * state.
     */
    private static final int[] TINT_CHECKABLE_BUTTON_LIST = new int[]{ R.drawable.abc_btn_check_material, R.drawable.abc_btn_radio_material };

    private java.util.WeakHashMap<android.content.Context, android.util.SparseArray<android.content.res.ColorStateList>> mTintLists;

    private android.support.v4.util.ArrayMap<java.lang.String, android.support.v7.widget.AppCompatDrawableManager.InflateDelegate> mDelegates;

    private android.util.SparseArray<java.lang.String> mKnownDrawableIdTags;

    private final java.lang.Object mDrawableCacheLock = new java.lang.Object();

    private final java.util.WeakHashMap<android.content.Context, android.support.v4.util.LongSparseArray<java.lang.ref.WeakReference<android.graphics.drawable.Drawable.ConstantState>>> mDrawableCaches = new java.util.WeakHashMap<>(0);

    private android.util.TypedValue mTypedValue;

    private boolean mHasCheckedVectorDrawableSetup;

    public android.graphics.drawable.Drawable getDrawable(@android.support.annotation.NonNull
    android.content.Context context, @android.support.annotation.DrawableRes
    int resId) {
        return getDrawable(context, resId, false);
    }

    android.graphics.drawable.Drawable getDrawable(@android.support.annotation.NonNull
    android.content.Context context, @android.support.annotation.DrawableRes
    int resId, boolean failIfNotKnown) {
        checkVectorDrawableSetup(context);
        android.graphics.drawable.Drawable drawable = loadDrawableFromDelegates(context, resId);
        if (drawable == null) {
            drawable = createDrawableIfNeeded(context, resId);
        }
        if (drawable == null) {
            drawable = android.support.v4.content.ContextCompat.getDrawable(context, resId);
        }
        if (drawable != null) {
            // Tint it if needed
            drawable = tintDrawable(context, resId, failIfNotKnown, drawable);
        }
        if (drawable != null) {
            // See if we need to 'fix' the drawable
            android.support.v7.widget.DrawableUtils.fixDrawable(drawable);
        }
        return drawable;
    }

    public void onConfigurationChanged(@android.support.annotation.NonNull
    android.content.Context context) {
        synchronized(mDrawableCacheLock) {
            android.support.v4.util.LongSparseArray<java.lang.ref.WeakReference<android.graphics.drawable.Drawable.ConstantState>> cache = mDrawableCaches.get(context);
            if (cache != null) {
                // Crude, but we'll just clear the cache when the configuration changes
                cache.clear();
            }
        }
    }

    private static long createCacheKey(android.util.TypedValue tv) {
        return (((long) (tv.assetCookie)) << 32) | tv.data;
    }

    private android.graphics.drawable.Drawable createDrawableIfNeeded(@android.support.annotation.NonNull
    android.content.Context context, @android.support.annotation.DrawableRes
    final int resId) {
        if (mTypedValue == null) {
            mTypedValue = new android.util.TypedValue();
        }
        final android.util.TypedValue tv = mTypedValue;
        context.getResources().getValue(resId, tv, true);
        final long key = android.support.v7.widget.AppCompatDrawableManager.createCacheKey(tv);
        android.graphics.drawable.Drawable dr = getCachedDrawable(context, key);
        if (dr != null) {
            // If we got a cached drawable, return it
            return dr;
        }
        // Else we need to try and create one...
        if (resId == R.drawable.abc_cab_background_top_material) {
            dr = new android.graphics.drawable.LayerDrawable(new android.graphics.drawable.Drawable[]{ getDrawable(context, R.drawable.abc_cab_background_internal_bg), getDrawable(context, R.drawable.abc_cab_background_top_mtrl_alpha) });
        }
        if (dr != null) {
            dr.setChangingConfigurations(tv.changingConfigurations);
            // If we reached here then we created a new drawable, add it to the cache
            addDrawableToCache(context, key, dr);
        }
        return dr;
    }

    private android.graphics.drawable.Drawable tintDrawable(@android.support.annotation.NonNull
    android.content.Context context, @android.support.annotation.DrawableRes
    int resId, boolean failIfNotKnown, @android.support.annotation.NonNull
    android.graphics.drawable.Drawable drawable) {
        final android.content.res.ColorStateList tintList = getTintList(context, resId);
        if (tintList != null) {
            // First mutate the Drawable, then wrap it and set the tint list
            if (android.support.v7.widget.DrawableUtils.canSafelyMutateDrawable(drawable)) {
                drawable = drawable.mutate();
            }
            drawable = android.support.v4.graphics.drawable.DrawableCompat.wrap(drawable);
            android.support.v4.graphics.drawable.DrawableCompat.setTintList(drawable, tintList);
            // If there is a blending mode specified for the drawable, use it
            final android.graphics.PorterDuff.Mode tintMode = android.support.v7.widget.AppCompatDrawableManager.getTintMode(resId);
            if (tintMode != null) {
                android.support.v4.graphics.drawable.DrawableCompat.setTintMode(drawable, tintMode);
            }
        } else
            if (resId == R.drawable.abc_seekbar_track_material) {
                android.graphics.drawable.LayerDrawable ld = ((android.graphics.drawable.LayerDrawable) (drawable));
                android.support.v7.widget.AppCompatDrawableManager.setPorterDuffColorFilter(ld.findDrawableByLayerId(android.R.id.background), android.support.v7.widget.ThemeUtils.getThemeAttrColor(context, R.attr.colorControlNormal), android.support.v7.widget.AppCompatDrawableManager.DEFAULT_MODE);
                android.support.v7.widget.AppCompatDrawableManager.setPorterDuffColorFilter(ld.findDrawableByLayerId(android.R.id.secondaryProgress), android.support.v7.widget.ThemeUtils.getThemeAttrColor(context, R.attr.colorControlNormal), android.support.v7.widget.AppCompatDrawableManager.DEFAULT_MODE);
                android.support.v7.widget.AppCompatDrawableManager.setPorterDuffColorFilter(ld.findDrawableByLayerId(android.R.id.progress), android.support.v7.widget.ThemeUtils.getThemeAttrColor(context, R.attr.colorControlActivated), android.support.v7.widget.AppCompatDrawableManager.DEFAULT_MODE);
            } else
                if (((resId == R.drawable.abc_ratingbar_material) || (resId == R.drawable.abc_ratingbar_indicator_material)) || (resId == R.drawable.abc_ratingbar_small_material)) {
                    android.graphics.drawable.LayerDrawable ld = ((android.graphics.drawable.LayerDrawable) (drawable));
                    android.support.v7.widget.AppCompatDrawableManager.setPorterDuffColorFilter(ld.findDrawableByLayerId(android.R.id.background), android.support.v7.widget.ThemeUtils.getDisabledThemeAttrColor(context, R.attr.colorControlNormal), android.support.v7.widget.AppCompatDrawableManager.DEFAULT_MODE);
                    android.support.v7.widget.AppCompatDrawableManager.setPorterDuffColorFilter(ld.findDrawableByLayerId(android.R.id.secondaryProgress), android.support.v7.widget.ThemeUtils.getThemeAttrColor(context, R.attr.colorControlActivated), android.support.v7.widget.AppCompatDrawableManager.DEFAULT_MODE);
                    android.support.v7.widget.AppCompatDrawableManager.setPorterDuffColorFilter(ld.findDrawableByLayerId(android.R.id.progress), android.support.v7.widget.ThemeUtils.getThemeAttrColor(context, R.attr.colorControlActivated), android.support.v7.widget.AppCompatDrawableManager.DEFAULT_MODE);
                } else {
                    final boolean tinted = android.support.v7.widget.AppCompatDrawableManager.tintDrawableUsingColorFilter(context, resId, drawable);
                    if ((!tinted) && failIfNotKnown) {
                        // If we didn't tint using a ColorFilter, and we're set to fail if we don't
                        // know the id, return null
                        drawable = null;
                    }
                }


        return drawable;
    }

    private android.graphics.drawable.Drawable loadDrawableFromDelegates(@android.support.annotation.NonNull
    android.content.Context context, @android.support.annotation.DrawableRes
    int resId) {
        if ((mDelegates != null) && (!mDelegates.isEmpty())) {
            if (mKnownDrawableIdTags != null) {
                final java.lang.String cachedTagName = mKnownDrawableIdTags.get(resId);
                if (android.support.v7.widget.AppCompatDrawableManager.SKIP_DRAWABLE_TAG.equals(cachedTagName) || ((cachedTagName != null) && (mDelegates.get(cachedTagName) == null))) {
                    // If we don't have a delegate for the drawable tag, or we've been set to
                    // skip it, fail fast and return null
                    if (android.support.v7.widget.AppCompatDrawableManager.DEBUG) {
                        android.util.Log.d(android.support.v7.widget.AppCompatDrawableManager.TAG, "[loadDrawableFromDelegates] Skipping drawable: " + context.getResources().getResourceName(resId));
                    }
                    return null;
                }
            } else {
                // Create an id cache as we'll need one later
                mKnownDrawableIdTags = new android.util.SparseArray<>();
            }
            if (mTypedValue == null) {
                mTypedValue = new android.util.TypedValue();
            }
            final android.util.TypedValue tv = mTypedValue;
            final android.content.res.Resources res = context.getResources();
            res.getValue(resId, tv, true);
            final long key = android.support.v7.widget.AppCompatDrawableManager.createCacheKey(tv);
            android.graphics.drawable.Drawable dr = getCachedDrawable(context, key);
            if (dr != null) {
                if (android.support.v7.widget.AppCompatDrawableManager.DEBUG) {
                    android.util.Log.i(android.support.v7.widget.AppCompatDrawableManager.TAG, "[loadDrawableFromDelegates] Returning cached drawable: " + context.getResources().getResourceName(resId));
                }
                // We have a cached drawable, return it!
                return dr;
            }
            if ((tv.string != null) && tv.string.toString().endsWith(".xml")) {
                // If the resource is an XML file, let's try and parse it
                try {
                    final org.xmlpull.v1.XmlPullParser parser = res.getXml(resId);
                    final android.util.AttributeSet attrs = android.util.Xml.asAttributeSet(parser);
                    int type;
                    while (((type = parser.next()) != org.xmlpull.v1.XmlPullParser.START_TAG) && (type != org.xmlpull.v1.XmlPullParser.END_DOCUMENT)) {
                        // Empty loop
                    } 
                    if (type != org.xmlpull.v1.XmlPullParser.START_TAG) {
                        throw new org.xmlpull.v1.XmlPullParserException("No start tag found");
                    }
                    final java.lang.String tagName = parser.getName();
                    // Add the tag name to the cache
                    mKnownDrawableIdTags.append(resId, tagName);
                    // Now try and find a delegate for the tag name and inflate if found
                    final android.support.v7.widget.AppCompatDrawableManager.InflateDelegate delegate = mDelegates.get(tagName);
                    if (delegate != null) {
                        dr = delegate.createFromXmlInner(context, parser, attrs, context.getTheme());
                    }
                    if (dr != null) {
                        // Add it to the drawable cache
                        dr.setChangingConfigurations(tv.changingConfigurations);
                        if (addDrawableToCache(context, key, dr) && android.support.v7.widget.AppCompatDrawableManager.DEBUG) {
                            android.util.Log.i(android.support.v7.widget.AppCompatDrawableManager.TAG, "[loadDrawableFromDelegates] Saved drawable to cache: " + context.getResources().getResourceName(resId));
                        }
                    }
                } catch (java.lang.Exception e) {
                    android.util.Log.e(android.support.v7.widget.AppCompatDrawableManager.TAG, "Exception while inflating drawable", e);
                }
            }
            if (dr == null) {
                // If we reach here then the delegate inflation of the resource failed. Mark it as
                // bad so we skip the id next time
                mKnownDrawableIdTags.append(resId, android.support.v7.widget.AppCompatDrawableManager.SKIP_DRAWABLE_TAG);
            }
            return dr;
        }
        return null;
    }

    private android.graphics.drawable.Drawable getCachedDrawable(@android.support.annotation.NonNull
    final android.content.Context context, final long key) {
        synchronized(mDrawableCacheLock) {
            final android.support.v4.util.LongSparseArray<java.lang.ref.WeakReference<android.graphics.drawable.Drawable.ConstantState>> cache = mDrawableCaches.get(context);
            if (cache == null) {
                return null;
            }
            final java.lang.ref.WeakReference<android.graphics.drawable.Drawable.ConstantState> wr = cache.get(key);
            if (wr != null) {
                // We have the key, and the secret
                android.graphics.drawable.Drawable.ConstantState entry = wr.get();
                if (entry != null) {
                    return entry.newDrawable(context.getResources());
                } else {
                    // Our entry has been purged
                    cache.delete(key);
                }
            }
        }
        return null;
    }

    private boolean addDrawableToCache(@android.support.annotation.NonNull
    final android.content.Context context, final long key, @android.support.annotation.NonNull
    final android.graphics.drawable.Drawable drawable) {
        final android.graphics.drawable.Drawable.ConstantState cs = drawable.getConstantState();
        if (cs != null) {
            synchronized(mDrawableCacheLock) {
                android.support.v4.util.LongSparseArray<java.lang.ref.WeakReference<android.graphics.drawable.Drawable.ConstantState>> cache = mDrawableCaches.get(context);
                if (cache == null) {
                    cache = new android.support.v4.util.LongSparseArray<>();
                    mDrawableCaches.put(context, cache);
                }
                cache.put(key, new java.lang.ref.WeakReference<android.graphics.drawable.Drawable.ConstantState>(cs));
            }
            return true;
        }
        return false;
    }

    android.graphics.drawable.Drawable onDrawableLoadedFromResources(@android.support.annotation.NonNull
    android.content.Context context, @android.support.annotation.NonNull
    android.support.v7.widget.VectorEnabledTintResources resources, @android.support.annotation.DrawableRes
    final int resId) {
        android.graphics.drawable.Drawable drawable = loadDrawableFromDelegates(context, resId);
        if (drawable == null) {
            drawable = resources.superGetDrawable(resId);
        }
        if (drawable != null) {
            return tintDrawable(context, resId, false, drawable);
        }
        return null;
    }

    static boolean tintDrawableUsingColorFilter(@android.support.annotation.NonNull
    android.content.Context context, @android.support.annotation.DrawableRes
    final int resId, @android.support.annotation.NonNull
    android.graphics.drawable.Drawable drawable) {
        android.graphics.PorterDuff.Mode tintMode = android.support.v7.widget.AppCompatDrawableManager.DEFAULT_MODE;
        boolean colorAttrSet = false;
        int colorAttr = 0;
        int alpha = -1;
        if (android.support.v7.widget.AppCompatDrawableManager.arrayContains(android.support.v7.widget.AppCompatDrawableManager.COLORFILTER_TINT_COLOR_CONTROL_NORMAL, resId)) {
            colorAttr = R.attr.colorControlNormal;
            colorAttrSet = true;
        } else
            if (android.support.v7.widget.AppCompatDrawableManager.arrayContains(android.support.v7.widget.AppCompatDrawableManager.COLORFILTER_COLOR_CONTROL_ACTIVATED, resId)) {
                colorAttr = R.attr.colorControlActivated;
                colorAttrSet = true;
            } else
                if (android.support.v7.widget.AppCompatDrawableManager.arrayContains(android.support.v7.widget.AppCompatDrawableManager.COLORFILTER_COLOR_BACKGROUND_MULTIPLY, resId)) {
                    colorAttr = android.R.attr.colorBackground;
                    colorAttrSet = true;
                    tintMode = android.graphics.PorterDuff.Mode.MULTIPLY;
                } else
                    if (resId == R.drawable.abc_list_divider_mtrl_alpha) {
                        colorAttr = android.R.attr.colorForeground;
                        colorAttrSet = true;
                        alpha = java.lang.Math.round(0.16F * 255);
                    } else
                        if (resId == R.drawable.abc_dialog_material_background) {
                            colorAttr = android.R.attr.colorBackground;
                            colorAttrSet = true;
                        }




        if (colorAttrSet) {
            if (android.support.v7.widget.DrawableUtils.canSafelyMutateDrawable(drawable)) {
                drawable = drawable.mutate();
            }
            final int color = android.support.v7.widget.ThemeUtils.getThemeAttrColor(context, colorAttr);
            drawable.setColorFilter(android.support.v7.widget.AppCompatDrawableManager.getPorterDuffColorFilter(color, tintMode));
            if (alpha != (-1)) {
                drawable.setAlpha(alpha);
            }
            if (android.support.v7.widget.AppCompatDrawableManager.DEBUG) {
                android.util.Log.d(android.support.v7.widget.AppCompatDrawableManager.TAG, (("[tintDrawableUsingColorFilter] Tinted " + context.getResources().getResourceName(resId)) + " with color: #") + java.lang.Integer.toHexString(color));
            }
            return true;
        }
        return false;
    }

    private void addDelegate(@android.support.annotation.NonNull
    java.lang.String tagName, @android.support.annotation.NonNull
    android.support.v7.widget.AppCompatDrawableManager.InflateDelegate delegate) {
        if (mDelegates == null) {
            mDelegates = new android.support.v4.util.ArrayMap<>();
        }
        mDelegates.put(tagName, delegate);
    }

    private void removeDelegate(@android.support.annotation.NonNull
    java.lang.String tagName, @android.support.annotation.NonNull
    android.support.v7.widget.AppCompatDrawableManager.InflateDelegate delegate) {
        if ((mDelegates != null) && (mDelegates.get(tagName) == delegate)) {
            mDelegates.remove(tagName);
        }
    }

    private static boolean arrayContains(int[] array, int value) {
        for (int id : array) {
            if (id == value) {
                return true;
            }
        }
        return false;
    }

    static android.graphics.PorterDuff.Mode getTintMode(final int resId) {
        android.graphics.PorterDuff.Mode mode = null;
        if (resId == R.drawable.abc_switch_thumb_material) {
            mode = android.graphics.PorterDuff.Mode.MULTIPLY;
        }
        return mode;
    }

    android.content.res.ColorStateList getTintList(@android.support.annotation.NonNull
    android.content.Context context, @android.support.annotation.DrawableRes
    int resId) {
        return getTintList(context, resId, null);
    }

    android.content.res.ColorStateList getTintList(@android.support.annotation.NonNull
    android.content.Context context, @android.support.annotation.DrawableRes
    int resId, @android.support.annotation.Nullable
    android.content.res.ColorStateList customTint) {
        // We only want to use the cache for the standard tints, not ones created using custom
        // tints
        final boolean useCache = customTint == null;
        // Try the cache first (if it exists)
        android.content.res.ColorStateList tint = (useCache) ? getTintListFromCache(context, resId) : null;
        if (tint == null) {
            // ...if the cache did not contain a color state list, try and create one
            if (resId == R.drawable.abc_edit_text_material) {
                tint = android.support.v7.content.res.AppCompatResources.getColorStateList(context, R.color.abc_tint_edittext);
            } else
                if (resId == R.drawable.abc_switch_track_mtrl_alpha) {
                    tint = android.support.v7.content.res.AppCompatResources.getColorStateList(context, R.color.abc_tint_switch_track);
                } else
                    if (resId == R.drawable.abc_switch_thumb_material) {
                        tint = android.support.v7.content.res.AppCompatResources.getColorStateList(context, R.color.abc_tint_switch_thumb);
                    } else
                        if (resId == R.drawable.abc_btn_default_mtrl_shape) {
                            tint = createDefaultButtonColorStateList(context, customTint);
                        } else
                            if (resId == R.drawable.abc_btn_borderless_material) {
                                tint = createBorderlessButtonColorStateList(context, customTint);
                            } else
                                if (resId == R.drawable.abc_btn_colored_material) {
                                    tint = createColoredButtonColorStateList(context, customTint);
                                } else
                                    if ((resId == R.drawable.abc_spinner_mtrl_am_alpha) || (resId == R.drawable.abc_spinner_textfield_background_material)) {
                                        tint = android.support.v7.content.res.AppCompatResources.getColorStateList(context, R.color.abc_tint_spinner);
                                    } else
                                        if (android.support.v7.widget.AppCompatDrawableManager.arrayContains(android.support.v7.widget.AppCompatDrawableManager.TINT_COLOR_CONTROL_NORMAL, resId)) {
                                            tint = android.support.v7.widget.ThemeUtils.getThemeAttrColorStateList(context, R.attr.colorControlNormal);
                                        } else
                                            if (android.support.v7.widget.AppCompatDrawableManager.arrayContains(android.support.v7.widget.AppCompatDrawableManager.TINT_COLOR_CONTROL_STATE_LIST, resId)) {
                                                tint = android.support.v7.content.res.AppCompatResources.getColorStateList(context, R.color.abc_tint_default);
                                            } else
                                                if (android.support.v7.widget.AppCompatDrawableManager.arrayContains(android.support.v7.widget.AppCompatDrawableManager.TINT_CHECKABLE_BUTTON_LIST, resId)) {
                                                    tint = android.support.v7.content.res.AppCompatResources.getColorStateList(context, R.color.abc_tint_btn_checkable);
                                                } else
                                                    if (resId == R.drawable.abc_seekbar_thumb_material) {
                                                        tint = android.support.v7.content.res.AppCompatResources.getColorStateList(context, R.color.abc_tint_seek_thumb);
                                                    }










            if (useCache && (tint != null)) {
                addTintListToCache(context, resId, tint);
            }
        }
        return tint;
    }

    private android.content.res.ColorStateList getTintListFromCache(@android.support.annotation.NonNull
    android.content.Context context, @android.support.annotation.DrawableRes
    int resId) {
        if (mTintLists != null) {
            final android.util.SparseArray<android.content.res.ColorStateList> tints = mTintLists.get(context);
            return tints != null ? tints.get(resId) : null;
        }
        return null;
    }

    private void addTintListToCache(@android.support.annotation.NonNull
    android.content.Context context, @android.support.annotation.DrawableRes
    int resId, @android.support.annotation.NonNull
    android.content.res.ColorStateList tintList) {
        if (mTintLists == null) {
            mTintLists = new java.util.WeakHashMap<>();
        }
        android.util.SparseArray<android.content.res.ColorStateList> themeTints = mTintLists.get(context);
        if (themeTints == null) {
            themeTints = new android.util.SparseArray<>();
            mTintLists.put(context, themeTints);
        }
        themeTints.append(resId, tintList);
    }

    private android.content.res.ColorStateList createDefaultButtonColorStateList(@android.support.annotation.NonNull
    android.content.Context context, @android.support.annotation.Nullable
    android.content.res.ColorStateList customTint) {
        return createButtonColorStateList(context, android.support.v7.widget.ThemeUtils.getThemeAttrColor(context, R.attr.colorButtonNormal), customTint);
    }

    private android.content.res.ColorStateList createBorderlessButtonColorStateList(@android.support.annotation.NonNull
    android.content.Context context, @android.support.annotation.Nullable
    android.content.res.ColorStateList customTint) {
        // We ignore the custom tint for borderless buttons
        return createButtonColorStateList(context, android.graphics.Color.TRANSPARENT, null);
    }

    private android.content.res.ColorStateList createColoredButtonColorStateList(@android.support.annotation.NonNull
    android.content.Context context, @android.support.annotation.Nullable
    android.content.res.ColorStateList customTint) {
        return createButtonColorStateList(context, android.support.v7.widget.ThemeUtils.getThemeAttrColor(context, R.attr.colorAccent), customTint);
    }

    private android.content.res.ColorStateList createButtonColorStateList(@android.support.annotation.NonNull
    final android.content.Context context, @android.support.annotation.ColorInt
    final int baseColor, @android.support.annotation.Nullable
    final android.content.res.ColorStateList tint) {
        final int[][] states = new int[4][];
        final int[] colors = new int[4];
        int i = 0;
        final int colorControlHighlight = android.support.v7.widget.ThemeUtils.getThemeAttrColor(context, R.attr.colorControlHighlight);
        final int disabledColor = android.support.v7.widget.ThemeUtils.getDisabledThemeAttrColor(context, R.attr.colorButtonNormal);
        // Disabled state
        states[i] = android.support.v7.widget.ThemeUtils.DISABLED_STATE_SET;
        colors[i] = (tint == null) ? disabledColor : tint.getColorForState(states[i], 0);
        i++;
        states[i] = android.support.v7.widget.ThemeUtils.PRESSED_STATE_SET;
        colors[i] = android.support.v4.graphics.ColorUtils.compositeColors(colorControlHighlight, tint == null ? baseColor : tint.getColorForState(states[i], 0));
        i++;
        states[i] = android.support.v7.widget.ThemeUtils.FOCUSED_STATE_SET;
        colors[i] = android.support.v4.graphics.ColorUtils.compositeColors(colorControlHighlight, tint == null ? baseColor : tint.getColorForState(states[i], 0));
        i++;
        // Default enabled state
        states[i] = android.support.v7.widget.ThemeUtils.EMPTY_STATE_SET;
        colors[i] = (tint == null) ? baseColor : tint.getColorForState(states[i], 0);
        i++;
        return new android.content.res.ColorStateList(states, colors);
    }

    private static class ColorFilterLruCache extends android.support.v4.util.LruCache<java.lang.Integer, android.graphics.PorterDuffColorFilter> {
        public ColorFilterLruCache(int maxSize) {
            super(maxSize);
        }

        android.graphics.PorterDuffColorFilter get(int color, android.graphics.PorterDuff.Mode mode) {
            return get(android.support.v7.widget.AppCompatDrawableManager.ColorFilterLruCache.generateCacheKey(color, mode));
        }

        android.graphics.PorterDuffColorFilter put(int color, android.graphics.PorterDuff.Mode mode, android.graphics.PorterDuffColorFilter filter) {
            return put(android.support.v7.widget.AppCompatDrawableManager.ColorFilterLruCache.generateCacheKey(color, mode), filter);
        }

        private static int generateCacheKey(int color, android.graphics.PorterDuff.Mode mode) {
            int hashCode = 1;
            hashCode = (31 * hashCode) + color;
            hashCode = (31 * hashCode) + mode.hashCode();
            return hashCode;
        }
    }

    static void tintDrawable(android.graphics.drawable.Drawable drawable, android.support.v7.widget.TintInfo tint, int[] state) {
        if (android.support.v7.widget.DrawableUtils.canSafelyMutateDrawable(drawable) && (drawable.mutate() != drawable)) {
            android.util.Log.d(android.support.v7.widget.AppCompatDrawableManager.TAG, "Mutated drawable is not the same instance as the input.");
            return;
        }
        if (tint.mHasTintList || tint.mHasTintMode) {
            drawable.setColorFilter(android.support.v7.widget.AppCompatDrawableManager.createTintFilter(tint.mHasTintList ? tint.mTintList : null, tint.mHasTintMode ? tint.mTintMode : android.support.v7.widget.AppCompatDrawableManager.DEFAULT_MODE, state));
        } else {
            drawable.clearColorFilter();
        }
        if (android.os.Build.VERSION.SDK_INT <= 23) {
            // Pre-v23 there is no guarantee that a state change will invoke an invalidation,
            // so we force it ourselves
            drawable.invalidateSelf();
        }
    }

    private static android.graphics.PorterDuffColorFilter createTintFilter(android.content.res.ColorStateList tint, android.graphics.PorterDuff.Mode tintMode, final int[] state) {
        if ((tint == null) || (tintMode == null)) {
            return null;
        }
        final int color = tint.getColorForState(state, android.graphics.Color.TRANSPARENT);
        return android.support.v7.widget.AppCompatDrawableManager.getPorterDuffColorFilter(color, tintMode);
    }

    public static android.graphics.PorterDuffColorFilter getPorterDuffColorFilter(int color, android.graphics.PorterDuff.Mode mode) {
        // First, lets see if the cache already contains the color filter
        android.graphics.PorterDuffColorFilter filter = android.support.v7.widget.AppCompatDrawableManager.COLOR_FILTER_CACHE.get(color, mode);
        if (filter == null) {
            // Cache miss, so create a color filter and add it to the cache
            filter = new android.graphics.PorterDuffColorFilter(color, mode);
            android.support.v7.widget.AppCompatDrawableManager.COLOR_FILTER_CACHE.put(color, mode, filter);
        }
        return filter;
    }

    private static void setPorterDuffColorFilter(android.graphics.drawable.Drawable d, int color, android.graphics.PorterDuff.Mode mode) {
        if (android.support.v7.widget.DrawableUtils.canSafelyMutateDrawable(d)) {
            d = d.mutate();
        }
        d.setColorFilter(android.support.v7.widget.AppCompatDrawableManager.getPorterDuffColorFilter(color, mode == null ? android.support.v7.widget.AppCompatDrawableManager.DEFAULT_MODE : mode));
    }

    private void checkVectorDrawableSetup(@android.support.annotation.NonNull
    android.content.Context context) {
        if (mHasCheckedVectorDrawableSetup) {
            // We've already checked so return now...
            return;
        }
        // Here we will check that a known Vector drawable resource inside AppCompat can be
        // correctly decoded
        mHasCheckedVectorDrawableSetup = true;
        final android.graphics.drawable.Drawable d = getDrawable(context, R.drawable.abc_vector_test);
        if ((d == null) || (!android.support.v7.widget.AppCompatDrawableManager.isVectorDrawable(d))) {
            mHasCheckedVectorDrawableSetup = false;
            throw new java.lang.IllegalStateException("This app has been built with an incorrect " + "configuration. Please configure your build for VectorDrawableCompat.");
        }
    }

    private static boolean isVectorDrawable(@android.support.annotation.NonNull
    android.graphics.drawable.Drawable d) {
        return (d instanceof android.support.graphics.drawable.VectorDrawableCompat) || android.support.v7.widget.AppCompatDrawableManager.PLATFORM_VD_CLAZZ.equals(d.getClass().getName());
    }

    private static class VdcInflateDelegate implements android.support.v7.widget.AppCompatDrawableManager.InflateDelegate {
        VdcInflateDelegate() {
        }

        @java.lang.Override
        public android.graphics.drawable.Drawable createFromXmlInner(@android.support.annotation.NonNull
        android.content.Context context, @android.support.annotation.NonNull
        org.xmlpull.v1.XmlPullParser parser, @android.support.annotation.NonNull
        android.util.AttributeSet attrs, @android.support.annotation.Nullable
        android.content.res.Resources.Theme theme) {
            try {
                return android.support.graphics.drawable.VectorDrawableCompat.createFromXmlInner(context.getResources(), parser, attrs, theme);
            } catch (java.lang.Exception e) {
                android.util.Log.e("VdcInflateDelegate", "Exception while inflating <vector>", e);
                return null;
            }
        }
    }

    private static class AvdcInflateDelegate implements android.support.v7.widget.AppCompatDrawableManager.InflateDelegate {
        AvdcInflateDelegate() {
        }

        @java.lang.Override
        public android.graphics.drawable.Drawable createFromXmlInner(@android.support.annotation.NonNull
        android.content.Context context, @android.support.annotation.NonNull
        org.xmlpull.v1.XmlPullParser parser, @android.support.annotation.NonNull
        android.util.AttributeSet attrs, @android.support.annotation.Nullable
        android.content.res.Resources.Theme theme) {
            try {
                return android.support.graphics.drawable.AnimatedVectorDrawableCompat.createFromXmlInner(context, context.getResources(), parser, attrs, theme);
            } catch (java.lang.Exception e) {
                android.util.Log.e("AvdcInflateDelegate", "Exception while inflating <animated-vector>", e);
                return null;
            }
        }
    }
}

