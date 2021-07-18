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
 * limitations under the License
 */
package android.support.v4.text;


class ICUCompatApi23 {
    private static final java.lang.String TAG = "ICUCompatIcs";

    private static java.lang.reflect.Method sAddLikelySubtagsMethod;

    static {
        try {
            // This class should always exist on API-23 since it's CTS tested.
            final java.lang.Class<?> clazz = java.lang.Class.forName("libcore.icu.ICU");
            android.support.v4.text.ICUCompatApi23.sAddLikelySubtagsMethod = clazz.getMethod("addLikelySubtags", new java.lang.Class[]{ java.util.Locale.class });
        } catch (java.lang.Exception e) {
            throw new java.lang.IllegalStateException(e);
        }
    }

    public static java.lang.String maximizeAndGetScript(java.util.Locale locale) {
        try {
            final java.lang.Object[] args = new java.lang.Object[]{ locale };
            return ((java.util.Locale) (android.support.v4.text.ICUCompatApi23.sAddLikelySubtagsMethod.invoke(null, args))).getScript();
        } catch (java.lang.reflect.InvocationTargetException e) {
            android.util.Log.w(android.support.v4.text.ICUCompatApi23.TAG, e);
        } catch (java.lang.IllegalAccessException e) {
            android.util.Log.w(android.support.v4.text.ICUCompatApi23.TAG, e);
        }
        return locale.getScript();
    }
}

