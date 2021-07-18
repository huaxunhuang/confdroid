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
package android.databinding;


/**
 * Utility class for managing Observable callbacks.
 */
public class PropertyChangeRegistry extends android.databinding.CallbackRegistry<android.databinding.Observable.OnPropertyChangedCallback, android.databinding.Observable, java.lang.Void> {
    private static final android.databinding.CallbackRegistry.NotifierCallback<android.databinding.Observable.OnPropertyChangedCallback, android.databinding.Observable, java.lang.Void> NOTIFIER_CALLBACK = new android.databinding.CallbackRegistry.NotifierCallback<android.databinding.Observable.OnPropertyChangedCallback, android.databinding.Observable, java.lang.Void>() {
        @java.lang.Override
        public void onNotifyCallback(android.databinding.Observable.OnPropertyChangedCallback callback, android.databinding.Observable sender, int arg, java.lang.Void notUsed) {
            callback.onPropertyChanged(sender, arg);
        }
    };

    public PropertyChangeRegistry() {
        super(android.databinding.PropertyChangeRegistry.NOTIFIER_CALLBACK);
    }

    /**
     * Notifies registered callbacks that a specific property has changed.
     *
     * @param observable
     * 		The Observable that has changed.
     * @param propertyId
     * 		The BR id of the property that has changed or BR._all if the entire
     * 		Observable has changed.
     */
    public void notifyChange(android.databinding.Observable observable, int propertyId) {
        notifyCallbacks(observable, propertyId, null);
    }
}

