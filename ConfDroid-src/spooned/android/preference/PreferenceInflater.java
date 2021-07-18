/**
 * Copyright (C) 2007 The Android Open Source Project
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
package android.preference;


/**
 * The {@link PreferenceInflater} is used to inflate preference hierarchies from
 * XML files.
 * <p>
 * Do not construct this directly, instead use
 * {@link Context#getSystemService(String)} with
 * {@link Context#PREFERENCE_INFLATER_SERVICE}.
 *
 * @deprecated Use the <a href="{@docRoot }jetpack/androidx.html">AndroidX</a>
<a href="{@docRoot }reference/androidx/preference/package-summary.html">
Preference Library</a> for consistent behavior across all devices. For more information on
using the AndroidX Preference Library see
<a href="{@docRoot }guide/topics/ui/settings.html">Settings</a>.
 */
@java.lang.Deprecated
class PreferenceInflater extends android.preference.GenericInflater<android.preference.Preference, android.preference.PreferenceGroup> {
    private static final java.lang.String TAG = "PreferenceInflater";

    private static final java.lang.String INTENT_TAG_NAME = "intent";

    private static final java.lang.String EXTRA_TAG_NAME = "extra";

    private android.preference.PreferenceManager mPreferenceManager;

    public PreferenceInflater(android.content.Context context, android.preference.PreferenceManager preferenceManager) {
        super(context);
        init(preferenceManager);
    }

    PreferenceInflater(android.preference.GenericInflater<android.preference.Preference, android.preference.PreferenceGroup> original, android.preference.PreferenceManager preferenceManager, android.content.Context newContext) {
        super(original, newContext);
        init(preferenceManager);
    }

    @java.lang.Override
    public android.preference.GenericInflater<android.preference.Preference, android.preference.PreferenceGroup> cloneInContext(android.content.Context newContext) {
        return new android.preference.PreferenceInflater(this, mPreferenceManager, newContext);
    }

    private void init(android.preference.PreferenceManager preferenceManager) {
        mPreferenceManager = preferenceManager;
        setDefaultPackage("android.preference.");
    }

    @java.lang.Override
    protected boolean onCreateCustomFromTag(org.xmlpull.v1.XmlPullParser parser, android.preference.Preference parentPreference, android.util.AttributeSet attrs) throws org.xmlpull.v1.XmlPullParserException {
        final java.lang.String tag = parser.getName();
        if (tag.equals(android.preference.PreferenceInflater.INTENT_TAG_NAME)) {
            android.content.Intent intent = null;
            try {
                intent = android.content.Intent.parseIntent(getContext().getResources(), parser, attrs);
            } catch (java.io.IOException e) {
                org.xmlpull.v1.XmlPullParserException ex = new org.xmlpull.v1.XmlPullParserException("Error parsing preference");
                ex.initCause(e);
                throw ex;
            }
            if (intent != null) {
                parentPreference.setIntent(intent);
            }
            return true;
        } else
            if (tag.equals(android.preference.PreferenceInflater.EXTRA_TAG_NAME)) {
                getContext().getResources().parseBundleExtra(android.preference.PreferenceInflater.EXTRA_TAG_NAME, attrs, parentPreference.getExtras());
                try {
                    com.android.internal.util.XmlUtils.skipCurrentTag(parser);
                } catch (java.io.IOException e) {
                    org.xmlpull.v1.XmlPullParserException ex = new org.xmlpull.v1.XmlPullParserException("Error parsing preference");
                    ex.initCause(e);
                    throw ex;
                }
                return true;
            }

        return false;
    }

    @java.lang.Override
    protected android.preference.PreferenceGroup onMergeRoots(android.preference.PreferenceGroup givenRoot, boolean attachToGivenRoot, android.preference.PreferenceGroup xmlRoot) {
        // If we were given a Preferences, use it as the root (ignoring the root
        // Preferences from the XML file).
        if (givenRoot == null) {
            xmlRoot.onAttachedToHierarchy(mPreferenceManager);
            return xmlRoot;
        } else {
            return givenRoot;
        }
    }
}

