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
 * A {@link Preference} that allows the user to choose a ringtone from those on the device.
 * The chosen ringtone's URI will be persisted as a string.
 * <p>
 * If the user chooses the "Default" item, the saved string will be one of
 * {@link System#DEFAULT_RINGTONE_URI},
 * {@link System#DEFAULT_NOTIFICATION_URI}, or
 * {@link System#DEFAULT_ALARM_ALERT_URI}. If the user chooses the "Silent"
 * item, the saved string will be an empty string.
 *
 * @unknown ref android.R.styleable#RingtonePreference_ringtoneType
 * @unknown ref android.R.styleable#RingtonePreference_showDefault
 * @unknown ref android.R.styleable#RingtonePreference_showSilent
 * @deprecated Use the <a href="{@docRoot }jetpack/androidx.html">AndroidX</a>
<a href="{@docRoot }reference/androidx/preference/package-summary.html">
Preference Library</a> for consistent behavior across all devices. For more information on
using the AndroidX Preference Library see
<a href="{@docRoot }guide/topics/ui/settings.html">Settings</a>.
 */
@java.lang.Deprecated
public class RingtonePreference extends android.preference.Preference implements android.preference.PreferenceManager.OnActivityResultListener {
    private static final java.lang.String TAG = "RingtonePreference";

    private int mRingtoneType;

    private boolean mShowDefault;

    private boolean mShowSilent;

    @android.annotation.UnsupportedAppUsage
    private int mRequestCode;

    public RingtonePreference(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        final android.content.res.TypedArray a = context.obtainStyledAttributes(attrs, com.android.internal.R.styleable.RingtonePreference, defStyleAttr, defStyleRes);
        mRingtoneType = a.getInt(com.android.internal.R.styleable.RingtonePreference_ringtoneType, RingtoneManager.TYPE_RINGTONE);
        mShowDefault = a.getBoolean(com.android.internal.R.styleable.RingtonePreference_showDefault, true);
        mShowSilent = a.getBoolean(com.android.internal.R.styleable.RingtonePreference_showSilent, true);
        a.recycle();
    }

    public RingtonePreference(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public RingtonePreference(android.content.Context context, android.util.AttributeSet attrs) {
        this(context, attrs, com.android.internal.R.attr.ringtonePreferenceStyle);
    }

    public RingtonePreference(android.content.Context context) {
        this(context, null);
    }

    /**
     * Returns the sound type(s) that are shown in the picker.
     *
     * @return The sound type(s) that are shown in the picker.
     * @see #setRingtoneType(int)
     */
    public int getRingtoneType() {
        return mRingtoneType;
    }

    /**
     * Sets the sound type(s) that are shown in the picker.
     *
     * @param type
     * 		The sound type(s) that are shown in the picker.
     * @see RingtoneManager#EXTRA_RINGTONE_TYPE
     */
    public void setRingtoneType(int type) {
        mRingtoneType = type;
    }

    /**
     * Returns whether to a show an item for the default sound/ringtone.
     *
     * @return Whether to show an item for the default sound/ringtone.
     */
    public boolean getShowDefault() {
        return mShowDefault;
    }

    /**
     * Sets whether to show an item for the default sound/ringtone. The default
     * to use will be deduced from the sound type(s) being shown.
     *
     * @param showDefault
     * 		Whether to show the default or not.
     * @see RingtoneManager#EXTRA_RINGTONE_SHOW_DEFAULT
     */
    public void setShowDefault(boolean showDefault) {
        mShowDefault = showDefault;
    }

    /**
     * Returns whether to a show an item for 'Silent'.
     *
     * @return Whether to show an item for 'Silent'.
     */
    public boolean getShowSilent() {
        return mShowSilent;
    }

    /**
     * Sets whether to show an item for 'Silent'.
     *
     * @param showSilent
     * 		Whether to show 'Silent'.
     * @see RingtoneManager#EXTRA_RINGTONE_SHOW_SILENT
     */
    public void setShowSilent(boolean showSilent) {
        mShowSilent = showSilent;
    }

    @java.lang.Override
    protected void onClick() {
        // Launch the ringtone picker
        android.content.Intent intent = new android.content.Intent(android.media.RingtoneManager.ACTION_RINGTONE_PICKER);
        onPrepareRingtonePickerIntent(intent);
        android.preference.PreferenceFragment owningFragment = getPreferenceManager().getFragment();
        if (owningFragment != null) {
            owningFragment.startActivityForResult(intent, mRequestCode);
        } else {
            getPreferenceManager().getActivity().startActivityForResult(intent, mRequestCode);
        }
    }

    /**
     * Prepares the intent to launch the ringtone picker. This can be modified
     * to adjust the parameters of the ringtone picker.
     *
     * @param ringtonePickerIntent
     * 		The ringtone picker intent that can be
     * 		modified by putting extras.
     */
    protected void onPrepareRingtonePickerIntent(android.content.Intent ringtonePickerIntent) {
        ringtonePickerIntent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, onRestoreRingtone());
        ringtonePickerIntent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, mShowDefault);
        if (mShowDefault) {
            ringtonePickerIntent.putExtra(RingtoneManager.EXTRA_RINGTONE_DEFAULT_URI, android.media.RingtoneManager.getDefaultUri(getRingtoneType()));
        }
        ringtonePickerIntent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT, mShowSilent);
        ringtonePickerIntent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, mRingtoneType);
        ringtonePickerIntent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, getTitle());
        ringtonePickerIntent.putExtra(RingtoneManager.EXTRA_RINGTONE_AUDIO_ATTRIBUTES_FLAGS, AudioAttributes.FLAG_BYPASS_INTERRUPTION_POLICY);
    }

    /**
     * Called when a ringtone is chosen.
     * <p>
     * By default, this saves the ringtone URI to the persistent storage as a
     * string.
     *
     * @param ringtoneUri
     * 		The chosen ringtone's {@link Uri}. Can be null.
     */
    protected void onSaveRingtone(android.net.Uri ringtoneUri) {
        persistString(ringtoneUri != null ? ringtoneUri.toString() : "");
    }

    /**
     * Called when the chooser is about to be shown and the current ringtone
     * should be marked. Can return null to not mark any ringtone.
     * <p>
     * By default, this restores the previous ringtone URI from the persistent
     * storage.
     *
     * @return The ringtone to be marked as the current ringtone.
     */
    protected android.net.Uri onRestoreRingtone() {
        final java.lang.String uriString = getPersistedString(null);
        return !android.text.TextUtils.isEmpty(uriString) ? android.net.Uri.parse(uriString) : null;
    }

    @java.lang.Override
    protected java.lang.Object onGetDefaultValue(android.content.res.TypedArray a, int index) {
        return a.getString(index);
    }

    @java.lang.Override
    protected void onSetInitialValue(boolean restorePersistedValue, java.lang.Object defaultValueObj) {
        java.lang.String defaultValue = ((java.lang.String) (defaultValueObj));
        /* This method is normally to make sure the internal state and UI
        matches either the persisted value or the default value. Since we
        don't show the current value in the UI (until the dialog is opened)
        and we don't keep local state, if we are restoring the persisted
        value we don't need to do anything.
         */
        if (restorePersistedValue) {
            return;
        }
        // If we are setting to the default value, we should persist it.
        if (!android.text.TextUtils.isEmpty(defaultValue)) {
            onSaveRingtone(android.net.Uri.parse(defaultValue));
        }
    }

    @java.lang.Override
    protected void onAttachedToHierarchy(android.preference.PreferenceManager preferenceManager) {
        super.onAttachedToHierarchy(preferenceManager);
        preferenceManager.registerOnActivityResultListener(this);
        mRequestCode = preferenceManager.getNextRequestCode();
    }

    public boolean onActivityResult(int requestCode, int resultCode, android.content.Intent data) {
        if (requestCode == mRequestCode) {
            if (data != null) {
                android.net.Uri uri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
                if (callChangeListener(uri != null ? uri.toString() : "")) {
                    onSaveRingtone(uri);
                }
            }
            return true;
        }
        return false;
    }
}

