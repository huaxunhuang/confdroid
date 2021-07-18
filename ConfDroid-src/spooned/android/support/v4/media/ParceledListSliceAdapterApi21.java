/**
 * Copyright (C) 2016 The Android Open Source Project
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
package android.support.v4.media;


/**
 * An adapter class for accessing the hidden framework classes, ParceledListSlice using reflection.
 */
class ParceledListSliceAdapterApi21 {
    private static java.lang.reflect.Constructor sConstructor;

    static {
        try {
            java.lang.Class theClass = java.lang.Class.forName("android.content.pm.ParceledListSlice");
            android.support.v4.media.ParceledListSliceAdapterApi21.sConstructor = theClass.getConstructor(new java.lang.Class[]{ java.util.List.class });
        } catch (java.lang.ClassNotFoundException | java.lang.NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    static java.lang.Object newInstance(java.util.List<android.media.browse.MediaBrowser.MediaItem> itemList) {
        java.lang.Object result = null;
        try {
            result = android.support.v4.media.ParceledListSliceAdapterApi21.sConstructor.newInstance(itemList);
        } catch (java.lang.InstantiationException | java.lang.IllegalAccessException | java.lang.reflect.InvocationTargetException e) {
            e.printStackTrace();
        }
        return result;
    }
}

