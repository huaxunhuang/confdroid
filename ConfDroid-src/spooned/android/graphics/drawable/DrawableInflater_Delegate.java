/**
 * Copyright (C) 2019 The Android Open Source Project
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
package android.graphics.drawable;


public class DrawableInflater_Delegate {
    private static final android.util.LruCache<java.lang.String, java.lang.reflect.Constructor<? extends android.graphics.drawable.Drawable>> CONSTRUCTOR_MAP = new android.util.LruCache(20);

    /**
     * This is identical to the original method except that it uses LayoutlibCallback to
     * load the drawable class, which enables loading custom drawables from the project.
     */
    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static android.graphics.drawable.Drawable inflateFromClass(android.graphics.drawable.DrawableInflater thisInflater, java.lang.String className) {
        try {
            java.lang.reflect.Constructor<? extends android.graphics.drawable.Drawable> constructor;
            synchronized(android.graphics.drawable.DrawableInflater_Delegate.CONSTRUCTOR_MAP) {
                constructor = android.graphics.drawable.DrawableInflater_Delegate.CONSTRUCTOR_MAP.get(className);
                if (constructor == null) {
                    final java.lang.Class<? extends android.graphics.drawable.Drawable> clazz = android.content.res.Resources_Delegate.getLayoutlibCallback(thisInflater.mRes).findClass(className).asSubclass(android.graphics.drawable.Drawable.class);
                    constructor = clazz.getConstructor();
                    android.graphics.drawable.DrawableInflater_Delegate.CONSTRUCTOR_MAP.put(className, constructor);
                }
            }
            return constructor.newInstance();
        } catch (java.lang.NoSuchMethodException e) {
            final android.view.InflateException ie = new android.view.InflateException("Error inflating class " + className);
            ie.initCause(e);
            throw ie;
        } catch (java.lang.ClassCastException e) {
            // If loaded class is not a Drawable subclass.
            final android.view.InflateException ie = new android.view.InflateException("Class is not a Drawable " + className);
            ie.initCause(e);
            throw ie;
        } catch (java.lang.ClassNotFoundException e) {
            // If loadClass fails, we should propagate the exception.
            final android.view.InflateException ie = new android.view.InflateException("Class not found " + className);
            ie.initCause(e);
            throw ie;
        } catch (java.lang.Exception e) {
            final android.view.InflateException ie = new android.view.InflateException("Error inflating class " + className);
            ie.initCause(e);
            throw ie;
        }
    }

    public static void clearConstructorCache() {
        android.graphics.drawable.DrawableInflater_Delegate.CONSTRUCTOR_MAP.evictAll();
    }
}

