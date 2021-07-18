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
package android.support.v4.content;


public final class SharedPreferencesCompat {
    public static final class EditorCompat {
        private static android.support.v4.content.SharedPreferencesCompat.EditorCompat sInstance;

        private static class Helper {
            Helper() {
            }

            public void apply(@android.support.annotation.NonNull
            android.content.SharedPreferences.Editor editor) {
                try {
                    editor.apply();
                } catch (java.lang.AbstractMethodError unused) {
                    // The app injected its own pre-Gingerbread
                    // SharedPreferences.Editor implementation without
                    // an apply method.
                    editor.commit();
                }
            }
        }

        private final android.support.v4.content.SharedPreferencesCompat.EditorCompat.Helper mHelper;

        private EditorCompat() {
            mHelper = new android.support.v4.content.SharedPreferencesCompat.EditorCompat.Helper();
        }

        public static android.support.v4.content.SharedPreferencesCompat.EditorCompat getInstance() {
            if (android.support.v4.content.SharedPreferencesCompat.EditorCompat.sInstance == null) {
                android.support.v4.content.SharedPreferencesCompat.EditorCompat.sInstance = new android.support.v4.content.SharedPreferencesCompat.EditorCompat();
            }
            return android.support.v4.content.SharedPreferencesCompat.EditorCompat.sInstance;
        }

        public void apply(@android.support.annotation.NonNull
        android.content.SharedPreferences.Editor editor) {
            // Note that this redirection is needed to not break the public API chain
            // of getInstance().apply() calls. Otherwise this method could (and should)
            // be static.
            mHelper.apply(editor);
        }
    }

    private SharedPreferencesCompat() {
    }
}

