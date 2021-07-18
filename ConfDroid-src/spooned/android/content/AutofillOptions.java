/**
 * Copyright (C) 2018 The Android Open Source Project
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
package android.content;


/**
 * Autofill options for a given package.
 *
 * <p>This object is created by the Autofill System Service and passed back to the app when the
 * application is created.
 *
 * @unknown 
 */
@android.annotation.TestApi
public final class AutofillOptions implements android.os.Parcelable {
    private static final java.lang.String TAG = android.content.AutofillOptions.class.getSimpleName();

    /**
     * Logging level for {@code logcat} statements.
     */
    public final int loggingLevel;

    /**
     * Whether compatibility mode is enabled for the package.
     */
    public final boolean compatModeEnabled;

    /**
     * Whether package is whitelisted for augmented autofill.
     */
    public boolean augmentedAutofillEnabled;

    /**
     * List of whitelisted activities.
     */
    @android.annotation.Nullable
    public android.util.ArraySet<android.content.ComponentName> whitelistedActivitiesForAugmentedAutofill;

    public AutofillOptions(int loggingLevel, boolean compatModeEnabled) {
        this.loggingLevel = loggingLevel;
        this.compatModeEnabled = compatModeEnabled;
    }

    /**
     * Returns whether activity is whitelisted for augmented autofill.
     */
    public boolean isAugmentedAutofillEnabled(@android.annotation.NonNull
    android.content.Context context) {
        if (!augmentedAutofillEnabled)
            return false;

        final android.view.autofill.AutofillManager.AutofillClient autofillClient = context.getAutofillClient();
        if (autofillClient == null)
            return false;

        final android.content.ComponentName component = autofillClient.autofillClientGetComponentName();
        return (whitelistedActivitiesForAugmentedAutofill == null) || whitelistedActivitiesForAugmentedAutofill.contains(component);
    }

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.TestApi
    public static android.content.AutofillOptions forWhitelistingItself() {
        final android.app.ActivityThread at = android.app.ActivityThread.currentActivityThread();
        if (at == null) {
            throw new java.lang.IllegalStateException("No ActivityThread");
        }
        final java.lang.String packageName = at.getApplication().getPackageName();
        if (!"android.autofillservice.cts".equals(packageName)) {
            android.util.Log.e(android.content.AutofillOptions.TAG, "forWhitelistingItself(): called by " + packageName);
            throw new java.lang.SecurityException("Thou shall not pass!");
        }
        final android.content.AutofillOptions options = /* compatModeAllowed= */
        new android.content.AutofillOptions(android.view.autofill.AutofillManager.FLAG_ADD_CLIENT_VERBOSE, true);
        options.augmentedAutofillEnabled = true;
        // Always log, as it's used by test only
        android.util.Log.i(android.content.AutofillOptions.TAG, (("forWhitelistingItself(" + packageName) + "): ") + options);
        return options;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return ((((("AutofillOptions [loggingLevel=" + loggingLevel) + ", compatMode=") + compatModeEnabled) + ", augmentedAutofillEnabled=") + augmentedAutofillEnabled) + "]";
    }

    /**
     *
     *
     * @unknown 
     */
    public void dumpShort(@android.annotation.NonNull
    java.io.PrintWriter pw) {
        pw.print("logLvl=");
        pw.print(loggingLevel);
        pw.print(", compatMode=");
        pw.print(compatModeEnabled);
        pw.print(", augmented=");
        pw.print(augmentedAutofillEnabled);
        if (whitelistedActivitiesForAugmentedAutofill != null) {
            pw.print(", whitelistedActivitiesForAugmentedAutofill=");
            pw.print(whitelistedActivitiesForAugmentedAutofill);
        }
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel parcel, int flags) {
        parcel.writeInt(loggingLevel);
        parcel.writeBoolean(compatModeEnabled);
        parcel.writeBoolean(augmentedAutofillEnabled);
        parcel.writeArraySet(whitelistedActivitiesForAugmentedAutofill);
    }

    @android.annotation.NonNull
    public static final android.os.Parcelable.Creator<android.content.AutofillOptions> CREATOR = new android.os.Parcelable.Creator<android.content.AutofillOptions>() {
        @java.lang.Override
        public android.content.AutofillOptions createFromParcel(android.os.Parcel parcel) {
            final int loggingLevel = parcel.readInt();
            final boolean compatMode = parcel.readBoolean();
            final android.content.AutofillOptions options = new android.content.AutofillOptions(loggingLevel, compatMode);
            options.augmentedAutofillEnabled = parcel.readBoolean();
            options.whitelistedActivitiesForAugmentedAutofill = ((ArraySet<android.content.ComponentName>) (parcel.readArraySet(null)));
            return options;
        }

        @java.lang.Override
        public android.content.AutofillOptions[] newArray(int size) {
            return new android.content.AutofillOptions[size];
        }
    };
}

