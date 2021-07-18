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
package android.hardware.soundtrigger;


/**
 * Enrollment information about the different available keyphrases.
 *
 * @unknown 
 */
public class KeyphraseEnrollmentInfo {
    private static final java.lang.String TAG = "KeyphraseEnrollmentInfo";

    /**
     * Name under which a Hotword enrollment component publishes information about itself.
     * This meta-data should reference an XML resource containing a
     * <code>&lt;{@link android.R.styleable#VoiceEnrollmentApplication
     * voice-enrollment-application}&gt;</code> tag.
     */
    private static final java.lang.String VOICE_KEYPHRASE_META_DATA = "android.voice_enrollment";

    /**
     * Activity Action: Show activity for managing the keyphrases for hotword detection.
     * This needs to be defined by an activity that supports enrolling users for hotword/keyphrase
     * detection.
     */
    public static final java.lang.String ACTION_MANAGE_VOICE_KEYPHRASES = "com.android.intent.action.MANAGE_VOICE_KEYPHRASES";

    /**
     * Intent extra: The intent extra for the specific manage action that needs to be performed.
     * Possible values are {@link AlwaysOnHotwordDetector#MANAGE_ACTION_ENROLL},
     * {@link AlwaysOnHotwordDetector#MANAGE_ACTION_RE_ENROLL}
     * or {@link AlwaysOnHotwordDetector#MANAGE_ACTION_UN_ENROLL}.
     */
    public static final java.lang.String EXTRA_VOICE_KEYPHRASE_ACTION = "com.android.intent.extra.VOICE_KEYPHRASE_ACTION";

    /**
     * Intent extra: The hint text to be shown on the voice keyphrase management UI.
     */
    public static final java.lang.String EXTRA_VOICE_KEYPHRASE_HINT_TEXT = "com.android.intent.extra.VOICE_KEYPHRASE_HINT_TEXT";

    /**
     * Intent extra: The voice locale to use while managing the keyphrase.
     * This is a BCP-47 language tag.
     */
    public static final java.lang.String EXTRA_VOICE_KEYPHRASE_LOCALE = "com.android.intent.extra.VOICE_KEYPHRASE_LOCALE";

    /**
     * List of available keyphrases.
     */
    private final android.hardware.soundtrigger.KeyphraseMetadata[] mKeyphrases;

    /**
     * Map between KeyphraseMetadata and the package name of the enrollment app that provides it.
     */
    private final java.util.Map<android.hardware.soundtrigger.KeyphraseMetadata, java.lang.String> mKeyphrasePackageMap;

    private java.lang.String mParseError;

    public KeyphraseEnrollmentInfo(android.content.pm.PackageManager pm) {
        // Find the apps that supports enrollment for hotword keyhphrases,
        // Pick a privileged app and obtain the information about the supported keyphrases
        // from its metadata.
        java.util.List<android.content.pm.ResolveInfo> ris = pm.queryIntentActivities(new android.content.Intent(android.hardware.soundtrigger.KeyphraseEnrollmentInfo.ACTION_MANAGE_VOICE_KEYPHRASES), android.content.pm.PackageManager.MATCH_DEFAULT_ONLY);
        if ((ris == null) || ris.isEmpty()) {
            // No application capable of enrolling for voice keyphrases is present.
            mParseError = "No enrollment applications found";
            mKeyphrasePackageMap = java.util.Collections.<android.hardware.soundtrigger.KeyphraseMetadata, java.lang.String>emptyMap();
            mKeyphrases = null;
            return;
        }
        java.util.List<java.lang.String> parseErrors = new java.util.LinkedList<java.lang.String>();
        mKeyphrasePackageMap = new java.util.HashMap<android.hardware.soundtrigger.KeyphraseMetadata, java.lang.String>();
        for (android.content.pm.ResolveInfo ri : ris) {
            try {
                android.content.pm.ApplicationInfo ai = pm.getApplicationInfo(ri.activityInfo.packageName, android.content.pm.PackageManager.GET_META_DATA);
                if ((ai.privateFlags & android.content.pm.ApplicationInfo.PRIVATE_FLAG_PRIVILEGED) == 0) {
                    // The application isn't privileged (/system/priv-app).
                    // The enrollment application needs to be a privileged system app.
                    android.util.Slog.w(android.hardware.soundtrigger.KeyphraseEnrollmentInfo.TAG, ai.packageName + "is not a privileged system app");
                    continue;
                }
                if (!Manifest.permission.MANAGE_VOICE_KEYPHRASES.equals(ai.permission)) {
                    // The application trying to manage keyphrases doesn't
                    // require the MANAGE_VOICE_KEYPHRASES permission.
                    android.util.Slog.w(android.hardware.soundtrigger.KeyphraseEnrollmentInfo.TAG, ai.packageName + " does not require MANAGE_VOICE_KEYPHRASES");
                    continue;
                }
                mKeyphrasePackageMap.put(getKeyphraseMetadataFromApplicationInfo(pm, ai, parseErrors), ai.packageName);
            } catch (android.content.pm.PackageManager.NameNotFoundException e) {
                java.lang.String error = "error parsing voice enrollment meta-data for " + ri.activityInfo.packageName;
                parseErrors.add((error + ": ") + e);
                android.util.Slog.w(android.hardware.soundtrigger.KeyphraseEnrollmentInfo.TAG, error, e);
            }
        }
        if (mKeyphrasePackageMap.isEmpty()) {
            java.lang.String error = "No suitable enrollment application found";
            parseErrors.add(error);
            android.util.Slog.w(android.hardware.soundtrigger.KeyphraseEnrollmentInfo.TAG, error);
            mKeyphrases = null;
        } else {
            mKeyphrases = mKeyphrasePackageMap.keySet().toArray(new android.hardware.soundtrigger.KeyphraseMetadata[mKeyphrasePackageMap.size()]);
        }
        if (!parseErrors.isEmpty()) {
            mParseError = android.text.TextUtils.join("\n", parseErrors);
        }
    }

    private android.hardware.soundtrigger.KeyphraseMetadata getKeyphraseMetadataFromApplicationInfo(android.content.pm.PackageManager pm, android.content.pm.ApplicationInfo ai, java.util.List<java.lang.String> parseErrors) {
        android.content.res.XmlResourceParser parser = null;
        java.lang.String packageName = ai.packageName;
        android.hardware.soundtrigger.KeyphraseMetadata keyphraseMetadata = null;
        try {
            parser = ai.loadXmlMetaData(pm, android.hardware.soundtrigger.KeyphraseEnrollmentInfo.VOICE_KEYPHRASE_META_DATA);
            if (parser == null) {
                java.lang.String error = (("No " + android.hardware.soundtrigger.KeyphraseEnrollmentInfo.VOICE_KEYPHRASE_META_DATA) + " meta-data for ") + packageName;
                parseErrors.add(error);
                android.util.Slog.w(android.hardware.soundtrigger.KeyphraseEnrollmentInfo.TAG, error);
                return null;
            }
            android.content.res.Resources res = pm.getResourcesForApplication(ai);
            android.util.AttributeSet attrs = android.util.Xml.asAttributeSet(parser);
            int type;
            while (((type = parser.next()) != org.xmlpull.v1.XmlPullParser.END_DOCUMENT) && (type != org.xmlpull.v1.XmlPullParser.START_TAG)) {
            } 
            java.lang.String nodeName = parser.getName();
            if (!"voice-enrollment-application".equals(nodeName)) {
                java.lang.String error = "Meta-data does not start with voice-enrollment-application tag for " + packageName;
                parseErrors.add(error);
                android.util.Slog.w(android.hardware.soundtrigger.KeyphraseEnrollmentInfo.TAG, error);
                return null;
            }
            android.content.res.TypedArray array = res.obtainAttributes(attrs, com.android.internal.R.styleable.VoiceEnrollmentApplication);
            keyphraseMetadata = getKeyphraseFromTypedArray(array, packageName, parseErrors);
            array.recycle();
        } catch (org.xmlpull.v1.XmlPullParserException e) {
            java.lang.String error = "Error parsing keyphrase enrollment meta-data for " + packageName;
            parseErrors.add((error + ": ") + e);
            android.util.Slog.w(android.hardware.soundtrigger.KeyphraseEnrollmentInfo.TAG, error, e);
        } catch (java.io.IOException e) {
            java.lang.String error = "Error parsing keyphrase enrollment meta-data for " + packageName;
            parseErrors.add((error + ": ") + e);
            android.util.Slog.w(android.hardware.soundtrigger.KeyphraseEnrollmentInfo.TAG, error, e);
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            java.lang.String error = "Error parsing keyphrase enrollment meta-data for " + packageName;
            parseErrors.add((error + ": ") + e);
            android.util.Slog.w(android.hardware.soundtrigger.KeyphraseEnrollmentInfo.TAG, error, e);
        } finally {
            if (parser != null)
                parser.close();

        }
        return keyphraseMetadata;
    }

    private android.hardware.soundtrigger.KeyphraseMetadata getKeyphraseFromTypedArray(android.content.res.TypedArray array, java.lang.String packageName, java.util.List<java.lang.String> parseErrors) {
        // Get the keyphrase ID.
        int searchKeyphraseId = array.getInt(com.android.internal.R.styleable.VoiceEnrollmentApplication_searchKeyphraseId, -1);
        if (searchKeyphraseId <= 0) {
            java.lang.String error = "No valid searchKeyphraseId specified in meta-data for " + packageName;
            parseErrors.add(error);
            android.util.Slog.w(android.hardware.soundtrigger.KeyphraseEnrollmentInfo.TAG, error);
            return null;
        }
        // Get the keyphrase text.
        java.lang.String searchKeyphrase = array.getString(com.android.internal.R.styleable.VoiceEnrollmentApplication_searchKeyphrase);
        if (searchKeyphrase == null) {
            java.lang.String error = "No valid searchKeyphrase specified in meta-data for " + packageName;
            parseErrors.add(error);
            android.util.Slog.w(android.hardware.soundtrigger.KeyphraseEnrollmentInfo.TAG, error);
            return null;
        }
        // Get the supported locales.
        java.lang.String searchKeyphraseSupportedLocales = array.getString(com.android.internal.R.styleable.VoiceEnrollmentApplication_searchKeyphraseSupportedLocales);
        if (searchKeyphraseSupportedLocales == null) {
            java.lang.String error = "No valid searchKeyphraseSupportedLocales specified in meta-data for " + packageName;
            parseErrors.add(error);
            android.util.Slog.w(android.hardware.soundtrigger.KeyphraseEnrollmentInfo.TAG, error);
            return null;
        }
        android.util.ArraySet<java.util.Locale> locales = new android.util.ArraySet<>();
        // Try adding locales if the locale string is non-empty.
        if (!android.text.TextUtils.isEmpty(searchKeyphraseSupportedLocales)) {
            try {
                java.lang.String[] supportedLocalesDelimited = searchKeyphraseSupportedLocales.split(",");
                for (int i = 0; i < supportedLocalesDelimited.length; i++) {
                    locales.add(java.util.Locale.forLanguageTag(supportedLocalesDelimited[i]));
                }
            } catch (java.lang.Exception ex) {
                // We catch a generic exception here because we don't want the system service
                // to be affected by a malformed metadata because invalid locales were specified
                // by the system application.
                java.lang.String error = "Error reading searchKeyphraseSupportedLocales from meta-data for " + packageName;
                parseErrors.add(error);
                android.util.Slog.w(android.hardware.soundtrigger.KeyphraseEnrollmentInfo.TAG, error);
                return null;
            }
        }
        // Get the supported recognition modes.
        int recognitionModes = array.getInt(com.android.internal.R.styleable.VoiceEnrollmentApplication_searchKeyphraseRecognitionFlags, -1);
        if (recognitionModes < 0) {
            java.lang.String error = "No valid searchKeyphraseRecognitionFlags specified in meta-data for " + packageName;
            parseErrors.add(error);
            android.util.Slog.w(android.hardware.soundtrigger.KeyphraseEnrollmentInfo.TAG, error);
            return null;
        }
        return new android.hardware.soundtrigger.KeyphraseMetadata(searchKeyphraseId, searchKeyphrase, locales, recognitionModes);
    }

    public java.lang.String getParseError() {
        return mParseError;
    }

    /**
     *
     *
     * @return An array of available keyphrases that can be enrolled on the system.
    It may be null if no keyphrases can be enrolled.
     */
    public android.hardware.soundtrigger.KeyphraseMetadata[] listKeyphraseMetadata() {
        return mKeyphrases;
    }

    /**
     * Returns an intent to launch an activity that manages the given keyphrase
     * for the locale.
     *
     * @param action
     * 		The enrollment related action that this intent is supposed to perform.
     * 		This can be one of {@link AlwaysOnHotwordDetector#MANAGE_ACTION_ENROLL},
     * 		{@link AlwaysOnHotwordDetector#MANAGE_ACTION_RE_ENROLL}
     * 		or {@link AlwaysOnHotwordDetector#MANAGE_ACTION_UN_ENROLL}
     * @param keyphrase
     * 		The keyphrase that the user needs to be enrolled to.
     * @param locale
     * 		The locale for which the enrollment needs to be performed.
     * @return An {@link Intent} to manage the keyphrase. This can be null if managing the
    given keyphrase/locale combination isn't possible.
     */
    public android.content.Intent getManageKeyphraseIntent(int action, java.lang.String keyphrase, java.util.Locale locale) {
        if ((mKeyphrasePackageMap == null) || mKeyphrasePackageMap.isEmpty()) {
            android.util.Slog.w(android.hardware.soundtrigger.KeyphraseEnrollmentInfo.TAG, "No enrollment application exists");
            return null;
        }
        android.hardware.soundtrigger.KeyphraseMetadata keyphraseMetadata = getKeyphraseMetadata(keyphrase, locale);
        if (keyphraseMetadata != null) {
            android.content.Intent intent = new android.content.Intent(android.hardware.soundtrigger.KeyphraseEnrollmentInfo.ACTION_MANAGE_VOICE_KEYPHRASES).setPackage(mKeyphrasePackageMap.get(keyphraseMetadata)).putExtra(android.hardware.soundtrigger.KeyphraseEnrollmentInfo.EXTRA_VOICE_KEYPHRASE_HINT_TEXT, keyphrase).putExtra(android.hardware.soundtrigger.KeyphraseEnrollmentInfo.EXTRA_VOICE_KEYPHRASE_LOCALE, locale.toLanguageTag()).putExtra(android.hardware.soundtrigger.KeyphraseEnrollmentInfo.EXTRA_VOICE_KEYPHRASE_ACTION, action);
            return intent;
        }
        return null;
    }

    /**
     * Gets the {@link KeyphraseMetadata} for the given keyphrase and locale, null if any metadata
     * isn't available for the given combination.
     *
     * @param keyphrase
     * 		The keyphrase that the user needs to be enrolled to.
     * @param locale
     * 		The locale for which the enrollment needs to be performed.
     * 		This is a Java locale, for example "en_US".
     * @return The metadata, if the enrollment client supports the given keyphrase
    and locale, null otherwise.
     */
    public android.hardware.soundtrigger.KeyphraseMetadata getKeyphraseMetadata(java.lang.String keyphrase, java.util.Locale locale) {
        if ((mKeyphrases != null) && (mKeyphrases.length > 0)) {
            for (android.hardware.soundtrigger.KeyphraseMetadata keyphraseMetadata : mKeyphrases) {
                // Check if the given keyphrase is supported in the locale provided by
                // the enrollment application.
                if (keyphraseMetadata.supportsPhrase(keyphrase) && keyphraseMetadata.supportsLocale(locale)) {
                    return keyphraseMetadata;
                }
            }
        }
        android.util.Slog.w(android.hardware.soundtrigger.KeyphraseEnrollmentInfo.TAG, "No Enrollment application supports the given keyphrase/locale");
        return null;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return ((("KeyphraseEnrollmentInfo [Keyphrases=" + mKeyphrasePackageMap.toString()) + ", ParseError=") + mParseError) + "]";
    }
}

