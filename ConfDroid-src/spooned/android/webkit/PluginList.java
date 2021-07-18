/**
 * Copyright (C) 2007 The Android Open Source Project
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
package android.webkit;


/**
 * A simple list of initialized plugins. This list gets
 * populated when the plugins are initialized (at
 * browser startup, at the moment).
 *
 * @unknown 
 * @deprecated This interface was intended to be used by Gears. Since Gears was
deprecated, so is this class.
 */
@java.lang.Deprecated
public class PluginList {
    private java.util.ArrayList<android.webkit.Plugin> mPlugins;

    /**
     * Public constructor. Initializes the list of plugins.
     *
     * @unknown 
     * @deprecated This interface was intended to be used by Gears. Since Gears was
    deprecated, so is this class.
     */
    @java.lang.Deprecated
    public PluginList() {
        mPlugins = new java.util.ArrayList<android.webkit.Plugin>();
    }

    /**
     * Returns the list of plugins as a java.util.List.
     *
     * @unknown 
     * @deprecated This interface was intended to be used by Gears. Since Gears was
    deprecated, so is this class.
     */
    @java.lang.Deprecated
    public synchronized java.util.List getList() {
        return mPlugins;
    }

    /**
     * Adds a plugin to the list.
     *
     * @unknown 
     * @deprecated This interface was intended to be used by Gears. Since Gears was
    deprecated, so is this class.
     */
    @java.lang.Deprecated
    public synchronized void addPlugin(android.webkit.Plugin plugin) {
        if (!mPlugins.contains(plugin)) {
            mPlugins.add(plugin);
        }
    }

    /**
     * Removes a plugin from the list.
     *
     * @unknown 
     * @deprecated This interface was intended to be used by Gears. Since Gears was
    deprecated, so is this class.
     */
    @java.lang.Deprecated
    public synchronized void removePlugin(android.webkit.Plugin plugin) {
        int location = mPlugins.indexOf(plugin);
        if (location != (-1)) {
            mPlugins.remove(location);
        }
    }

    /**
     * Clears the plugin list.
     *
     * @unknown 
     * @deprecated This interface was intended to be used by Gears. Since Gears was
    deprecated, so is this class.
     */
    @java.lang.Deprecated
    public synchronized void clear() {
        mPlugins.clear();
    }

    /**
     * Dispatches the click event to the appropriate plugin.
     *
     * @unknown 
     * @deprecated This interface was intended to be used by Gears. Since Gears was
    deprecated, so is this class.
     */
    @java.lang.Deprecated
    public synchronized void pluginClicked(android.content.Context context, int position) {
        try {
            android.webkit.Plugin plugin = mPlugins.get(position);
            plugin.dispatchClickEvent(context);
        } catch (java.lang.IndexOutOfBoundsException e) {
            // This can happen if the list of plugins
            // gets changed while the pref menu is up.
        }
    }
}

