/**
 * Copyright (C) 2013 The Android Open Source Project
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
package android.support.v4.text;


class ICUCompatIcs {
    private static final java.lang.String TAG = "ICUCompatIcs";

    private static java.lang.reflect.Method sGetScriptMethod;

    private static java.lang.reflect.Method sAddLikelySubtagsMethod;

    static {
        try {
            final java.lang.Class<?> clazz = java.lang.Class.forName("libcore.icu.ICU");
            if (clazz != null) {
                android.support.v4.text.ICUCompatIcs.sGetScriptMethod = clazz.getMethod("getScript", new java.lang.Class[]{ java.lang.String.class });
                android.support.v4.text.ICUCompatIcs.sAddLikelySubtagsMethod = clazz.getMethod("addLikelySubtags", new java.lang.Class[]{ java.lang.String.class });
            }
        } catch (java.lang.Exception e) {
            android.support.v4.text.ICUCompatIcs.sGetScriptMethod = null;
            android.support.v4.text.ICUCompatIcs.sAddLikelySubtagsMethod = null;
            // Nothing we can do here, we just log the exception
            android.util.Log.w(android.support.v4.text.ICUCompatIcs.TAG, e);
        }
    }

    public static java.lang.String maximizeAndGetScript(java.util.Locale locale) {
        final java.lang.String localeWithSubtags = android.support.v4.text.ICUCompatIcs.addLikelySubtags(locale);
        if (localeWithSubtags != null) {
            return android.support.v4.text.ICUCompatIcs.getScript(localeWithSubtags);
        }
        return null;
    }

    private static java.lang.String getScript(java.lang.String localeStr) {
        try {
            if (android.support.v4.text.ICUCompatIcs.sGetScriptMethod != null) {
                final java.lang.Object[] args = new java.lang.Object[]{ localeStr };
                return ((java.lang.String) (android.support.v4.text.ICUCompatIcs.sGetScriptMethod.invoke(null, args)));
            }
        } catch (java.lang.IllegalAccessException e) {
            // Nothing we can do here, we just log the exception
            android.util.Log.w(android.support.v4.text.ICUCompatIcs.TAG, e);
        } catch (java.lang.reflect.InvocationTargetException e) {
            // Nothing we can do here, we just log the exception
            android.util.Log.w(android.support.v4.text.ICUCompatIcs.TAG, e);
        }
        return null;
    }

    private static java.lang.String addLikelySubtags(java.util.Locale locale) {
        final java.lang.String localeStr = locale.toString();
        try {
            if (android.support.v4.text.ICUCompatIcs.sAddLikelySubtagsMethod != null) {
                final java.lang.Object[] args = new java.lang.Object[]{ localeStr };
                return ((java.lang.String) (android.support.v4.text.ICUCompatIcs.sAddLikelySubtagsMethod.invoke(null, args)));
            }
        } catch (java.lang.IllegalAccessException e) {
            // Nothing we can do here, we just log the exception
            android.util.Log.w(android.support.v4.text.ICUCompatIcs.TAG, e);
        } catch (java.lang.reflect.InvocationTargetException e) {
            // Nothing we can do here, we just log the exception
            android.util.Log.w(android.support.v4.text.ICUCompatIcs.TAG, e);
        }
        return localeStr;
    }
}

