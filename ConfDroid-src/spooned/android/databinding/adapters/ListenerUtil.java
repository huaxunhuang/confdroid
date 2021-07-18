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
package android.databinding.adapters;


public class ListenerUtil {
    private static android.util.SparseArray<java.util.WeakHashMap<android.view.View, java.lang.ref.WeakReference<?>>> sListeners = new android.util.SparseArray<java.util.WeakHashMap<android.view.View, java.lang.ref.WeakReference<?>>>();

    /**
     * This method tracks listeners for a View. Only one listener per listenerResourceId
     * can be tracked at a time. This is useful for add*Listener and remove*Listener methods
     * when used with BindingAdapters. This guarantees not to leak the listener or the View,
     * so will not keep a strong reference to either.
     *
     * Example usage:
     * <pre><code>@BindingAdapter("onFoo")
     * public static void addFooListener(MyView view, OnFooListener listener) {
     *     OnFooListener oldValue = ListenerUtil.trackListener(view, listener, R.id.fooListener);
     *     if (oldValue != null) {
     *         view.removeOnFooListener(oldValue);
     *     }
     *     if (listener != null) {
     *         view.addOnFooListener(listener);
     *     }
     * }</code></pre>
     *
     * @param view
     * 		The View that will have this listener
     * @param listener
     * 		The listener to keep track of. May be null if the listener is being removed.
     * @param listenerResourceId
     * 		A unique resource ID associated with the listener type.
     * @return The previously tracked listener. This will be null if the View did not have
    a previously-tracked listener.
     */
    @java.lang.SuppressWarnings("unchecked")
    public static <T> T trackListener(android.view.View view, T listener, int listenerResourceId) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            final T oldValue = ((T) (view.getTag(listenerResourceId)));
            view.setTag(listenerResourceId, listener);
            return oldValue;
        } else {
            synchronized(android.databinding.adapters.ListenerUtil.sListeners) {
                java.util.WeakHashMap<android.view.View, java.lang.ref.WeakReference<?>> listeners = android.databinding.adapters.ListenerUtil.sListeners.get(listenerResourceId);
                if (listeners == null) {
                    listeners = new java.util.WeakHashMap<android.view.View, java.lang.ref.WeakReference<?>>();
                    android.databinding.adapters.ListenerUtil.sListeners.put(listenerResourceId, listeners);
                }
                final java.lang.ref.WeakReference<T> oldValue;
                if (listener == null) {
                    oldValue = ((java.lang.ref.WeakReference<T>) (listeners.remove(view)));
                } else {
                    oldValue = ((java.lang.ref.WeakReference<T>) (listeners.put(view, new java.lang.ref.WeakReference(listener))));
                }
                if (oldValue == null) {
                    return null;
                } else {
                    return oldValue.get();
                }
            }
        }
    }

    /**
     * Returns the previous value for a listener if one was stored previously with
     * {@link #trackListener(View, Object, int)}
     *
     * @param view
     * 		The View to check for a listener previously stored with
     * 		{@link #trackListener(View, Object, int)}
     * @param listenerResourceId
     * 		A unique resource ID associated with the listener type.
     * @return The previously tracked listener. This will be null if the View did not have
    a previously-tracked listener.
     */
    public static <T> T getListener(android.view.View view, int listenerResourceId) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            return ((T) (view.getTag(listenerResourceId)));
        } else {
            synchronized(android.databinding.adapters.ListenerUtil.sListeners) {
                java.util.WeakHashMap<android.view.View, java.lang.ref.WeakReference<?>> listeners = android.databinding.adapters.ListenerUtil.sListeners.get(listenerResourceId);
                if (listeners == null) {
                    return null;
                }
                final java.lang.ref.WeakReference<T> oldValue = ((java.lang.ref.WeakReference<T>) (listeners.get(view)));
                if (oldValue == null) {
                    return null;
                } else {
                    return oldValue.get();
                }
            }
        }
    }
}

