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
package android.view.textservice;


/**
 * A stub class of TextServicesManager for Layout-Lib.
 */
public final class TextServicesManager {
    private static final android.view.textservice.TextServicesManager sInstance = new android.view.textservice.TextServicesManager();

    private static final android.view.textservice.SpellCheckerInfo[] EMPTY_SPELL_CHECKER_INFO = new android.view.textservice.SpellCheckerInfo[0];

    /**
     * Retrieve the global TextServicesManager instance, creating it if it doesn't already exist.
     *
     * @unknown 
     */
    public static android.view.textservice.TextServicesManager getInstance() {
        return android.view.textservice.TextServicesManager.sInstance;
    }

    public android.view.textservice.SpellCheckerSession newSpellCheckerSession(android.os.Bundle bundle, java.util.Locale locale, android.view.textservice.SpellCheckerSession.SpellCheckerSessionListener listener, boolean referToSpellCheckerLanguageSettings) {
        return null;
    }

    /**
     *
     *
     * @unknown 
     */
    public android.view.textservice.SpellCheckerInfo[] getEnabledSpellCheckers() {
        return android.view.textservice.TextServicesManager.EMPTY_SPELL_CHECKER_INFO;
    }

    /**
     *
     *
     * @unknown 
     */
    public android.view.textservice.SpellCheckerInfo getCurrentSpellChecker() {
        return null;
    }

    /**
     *
     *
     * @unknown 
     */
    public android.view.textservice.SpellCheckerSubtype getCurrentSpellCheckerSubtype(boolean allowImplicitlySelectedSubtype) {
        return null;
    }

    /**
     *
     *
     * @unknown 
     */
    public boolean isSpellCheckerEnabled() {
        return false;
    }
}

