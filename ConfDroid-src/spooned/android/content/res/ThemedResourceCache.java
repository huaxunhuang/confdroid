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
package android.content.res;


/**
 * Data structure used for caching data against themes.
 *
 * @param <T>
 * 		type of data to cache
 */
abstract class ThemedResourceCache<T> {
    @android.annotation.UnsupportedAppUsage
    private android.util.ArrayMap<android.content.res.Resources.ThemeKey, android.util.LongSparseArray<java.lang.ref.WeakReference<T>>> mThemedEntries;

    private android.util.LongSparseArray<java.lang.ref.WeakReference<T>> mUnthemedEntries;

    private android.util.LongSparseArray<java.lang.ref.WeakReference<T>> mNullThemedEntries;

    /**
     * Adds a new theme-dependent entry to the cache.
     *
     * @param key
     * 		a key that uniquely identifies the entry
     * @param theme
     * 		the theme against which this entry was inflated, or
     * 		{@code null} if the entry has no theme applied
     * @param entry
     * 		the entry to cache
     */
    public void put(long key, @android.annotation.Nullable
    android.content.res.Resources.Theme theme, @android.annotation.NonNull
    T entry) {
        put(key, theme, entry, true);
    }

    /**
     * Adds a new entry to the cache.
     *
     * @param key
     * 		a key that uniquely identifies the entry
     * @param theme
     * 		the theme against which this entry was inflated, or
     * 		{@code null} if the entry has no theme applied
     * @param entry
     * 		the entry to cache
     * @param usesTheme
     * 		{@code true} if the entry is affected theme changes,
     * 		{@code false} otherwise
     */
    public void put(long key, @android.annotation.Nullable
    android.content.res.Resources.Theme theme, @android.annotation.NonNull
    T entry, boolean usesTheme) {
        if (entry == null) {
            return;
        }
        synchronized(this) {
            final android.util.LongSparseArray<java.lang.ref.WeakReference<T>> entries;
            if (!usesTheme) {
                entries = getUnthemedLocked(true);
            } else {
                entries = getThemedLocked(theme, true);
            }
            if (entries != null) {
                entries.put(key, new java.lang.ref.WeakReference(entry));
            }
        }
    }

    /**
     * Returns an entry from the cache.
     *
     * @param key
     * 		a key that uniquely identifies the entry
     * @param theme
     * 		the theme where the entry will be used
     * @return a cached entry, or {@code null} if not in the cache
     */
    @android.annotation.Nullable
    public T get(long key, @android.annotation.Nullable
    android.content.res.Resources.Theme theme) {
        // The themed (includes null-themed) and unthemed caches are mutually
        // exclusive, so we'll give priority to whichever one we think we'll
        // hit first. Since most of the framework drawables are themed, that's
        // probably going to be the themed cache.
        synchronized(this) {
            final android.util.LongSparseArray<java.lang.ref.WeakReference<T>> themedEntries = getThemedLocked(theme, false);
            if (themedEntries != null) {
                final java.lang.ref.WeakReference<T> themedEntry = themedEntries.get(key);
                if (themedEntry != null) {
                    return themedEntry.get();
                }
            }
            final android.util.LongSparseArray<java.lang.ref.WeakReference<T>> unthemedEntries = getUnthemedLocked(false);
            if (unthemedEntries != null) {
                final java.lang.ref.WeakReference<T> unthemedEntry = unthemedEntries.get(key);
                if (unthemedEntry != null) {
                    return unthemedEntry.get();
                }
            }
        }
        return null;
    }

    /**
     * Prunes cache entries that have been invalidated by a configuration
     * change.
     *
     * @param configChanges
     * 		a bitmask of configuration changes
     */
    @android.annotation.UnsupportedAppUsage
    public void onConfigurationChange(@android.content.pm.ActivityInfo.Config
    int configChanges) {
        prune(configChanges);
    }

    /**
     * Returns whether a cached entry has been invalidated by a configuration
     * change.
     *
     * @param entry
     * 		a cached entry
     * @param configChanges
     * 		a non-zero bitmask of configuration changes
     * @return {@code true} if the entry is invalid, {@code false} otherwise
     */
    protected abstract boolean shouldInvalidateEntry(@android.annotation.NonNull
    T entry, int configChanges);

    /**
     * Returns the cached data for the specified theme, optionally creating a
     * new entry if one does not already exist.
     *
     * @param t
     * 		the theme for which to return cached data
     * @param create
     * 		{@code true} to create an entry if one does not already
     * 		exist, {@code false} otherwise
     * @return the cached data for the theme, or {@code null} if the cache is
    empty and {@code create} was {@code false}
     */
    @android.annotation.Nullable
    private android.util.LongSparseArray<java.lang.ref.WeakReference<T>> getThemedLocked(@android.annotation.Nullable
    android.content.res.Resources.Theme t, boolean create) {
        if (t == null) {
            if ((mNullThemedEntries == null) && create) {
                mNullThemedEntries = new android.util.LongSparseArray(1);
            }
            return mNullThemedEntries;
        }
        if (mThemedEntries == null) {
            if (create) {
                mThemedEntries = new android.util.ArrayMap(1);
            } else {
                return null;
            }
        }
        final android.content.res.Resources.ThemeKey key = t.getKey();
        android.util.LongSparseArray<java.lang.ref.WeakReference<T>> cache = mThemedEntries.get(key);
        if ((cache == null) && create) {
            cache = new android.util.LongSparseArray(1);
            final android.content.res.Resources.ThemeKey keyClone = key.clone();
            mThemedEntries.put(keyClone, cache);
        }
        return cache;
    }

    /**
     * Returns the theme-agnostic cached data.
     *
     * @param create
     * 		{@code true} to create an entry if one does not already
     * 		exist, {@code false} otherwise
     * @return the theme-agnostic cached data, or {@code null} if the cache is
    empty and {@code create} was {@code false}
     */
    @android.annotation.Nullable
    private android.util.LongSparseArray<java.lang.ref.WeakReference<T>> getUnthemedLocked(boolean create) {
        if ((mUnthemedEntries == null) && create) {
            mUnthemedEntries = new android.util.LongSparseArray(1);
        }
        return mUnthemedEntries;
    }

    /**
     * Prunes cache entries affected by configuration changes or where weak
     * references have expired.
     *
     * @param configChanges
     * 		a bitmask of configuration changes, or {@code 0} to
     * 		simply prune missing weak references
     * @return {@code true} if the cache is completely empty after pruning
     */
    private boolean prune(@android.content.pm.ActivityInfo.Config
    int configChanges) {
        synchronized(this) {
            if (mThemedEntries != null) {
                for (int i = mThemedEntries.size() - 1; i >= 0; i--) {
                    if (pruneEntriesLocked(mThemedEntries.valueAt(i), configChanges)) {
                        mThemedEntries.removeAt(i);
                    }
                }
            }
            pruneEntriesLocked(mNullThemedEntries, configChanges);
            pruneEntriesLocked(mUnthemedEntries, configChanges);
            return ((mThemedEntries == null) && (mNullThemedEntries == null)) && (mUnthemedEntries == null);
        }
    }

    private boolean pruneEntriesLocked(@android.annotation.Nullable
    android.util.LongSparseArray<java.lang.ref.WeakReference<T>> entries, @android.content.pm.ActivityInfo.Config
    int configChanges) {
        if (entries == null) {
            return true;
        }
        for (int i = entries.size() - 1; i >= 0; i--) {
            final java.lang.ref.WeakReference<T> ref = entries.valueAt(i);
            if ((ref == null) || pruneEntryLocked(ref.get(), configChanges)) {
                entries.removeAt(i);
            }
        }
        return entries.size() == 0;
    }

    private boolean pruneEntryLocked(@android.annotation.Nullable
    T entry, @android.content.pm.ActivityInfo.Config
    int configChanges) {
        return (entry == null) || ((configChanges != 0) && shouldInvalidateEntry(entry, configChanges));
    }
}

