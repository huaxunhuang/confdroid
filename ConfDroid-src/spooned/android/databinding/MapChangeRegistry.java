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
package android.databinding;


/**
 * Utility class for managing ObservableMap callbacks.
 */
public class MapChangeRegistry extends android.databinding.CallbackRegistry<android.databinding.ObservableMap.OnMapChangedCallback, android.databinding.ObservableMap, java.lang.Object> {
    private static android.databinding.CallbackRegistry.NotifierCallback<android.databinding.ObservableMap.OnMapChangedCallback, android.databinding.ObservableMap, java.lang.Object> NOTIFIER_CALLBACK = new android.databinding.CallbackRegistry.NotifierCallback<android.databinding.ObservableMap.OnMapChangedCallback, android.databinding.ObservableMap, java.lang.Object>() {
        @java.lang.Override
        public void onNotifyCallback(android.databinding.ObservableMap.OnMapChangedCallback callback, android.databinding.ObservableMap sender, int arg, java.lang.Object arg2) {
            callback.onMapChanged(sender, arg2);
        }
    };

    public MapChangeRegistry() {
        super(android.databinding.MapChangeRegistry.NOTIFIER_CALLBACK);
    }

    /**
     * Notifies registered callbacks that an element has been added, removed, or changed.
     *
     * @param sender
     * 		The map that has changed.
     * @param key
     * 		The key of the element that changed.
     */
    public void notifyChange(android.databinding.ObservableMap sender, java.lang.Object key) {
        notifyCallbacks(sender, 0, key);
    }
}

