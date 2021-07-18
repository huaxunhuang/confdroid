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


/**
 * A Cache class which can be used to cache resource objects that are easy to clone but more
 * expensive to inflate.
 *
 * @unknown For internal use only.
 */
public class ConfigurationBoundResourceCache<T> extends android.content.res.ThemedResourceCache<android.content.res.ConstantState<T>> {
    /**
     * If the resource is cached, creates and returns a new instance of it.
     *
     * @param key
     * 		a key that uniquely identifies the drawable resource
     * @param resources
     * 		a Resources object from which to create new instances.
     * @param theme
     * 		the theme where the resource will be used
     * @return a new instance of the resource, or {@code null} if not in
    the cache
     */
    public T getInstance(long key, android.content.res.Resources resources, android.content.res.Resources.Theme theme) {
        final android.content.res.ConstantState<T> entry = get(key, theme);
        if (entry != null) {
            return entry.newInstance(resources, theme);
        }
        return null;
    }

    @java.lang.Override
    public boolean shouldInvalidateEntry(android.content.res.ConstantState<T> entry, @android.content.pm.ActivityInfo.Config
    int configChanges) {
        return android.content.res.Configuration.needNewResources(configChanges, entry.getChangingConfigurations());
    }
}

