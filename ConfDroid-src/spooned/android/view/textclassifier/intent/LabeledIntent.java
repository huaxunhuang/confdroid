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
package android.view.textclassifier.intent;


/**
 * Helper class to store the information from which RemoteActions are built.
 *
 * @unknown 
 */
@com.android.internal.annotations.VisibleForTesting(visibility = VisibleForTesting.Visibility.PACKAGE)
public final class LabeledIntent {
    private static final java.lang.String TAG = "LabeledIntent";

    public static final int DEFAULT_REQUEST_CODE = 0;

    private static final android.view.textclassifier.intent.LabeledIntent.TitleChooser DEFAULT_TITLE_CHOOSER = ( labeledIntent, resolveInfo) -> {
        if (!android.text.TextUtils.isEmpty(labeledIntent.titleWithEntity)) {
            return labeledIntent.titleWithEntity;
        }
        return labeledIntent.titleWithoutEntity;
    };

    @android.annotation.Nullable
    public final java.lang.String titleWithoutEntity;

    @android.annotation.Nullable
    public final java.lang.String titleWithEntity;

    public final java.lang.String description;

    @android.annotation.Nullable
    public final java.lang.String descriptionWithAppName;

    // Do not update this intent.
    public final android.content.Intent intent;

    public final int requestCode;

    /**
     * Initializes a LabeledIntent.
     *
     * <p>NOTE: {@code requestCode} is required to not be {@link #DEFAULT_REQUEST_CODE}
     * if distinguishing info (e.g. the classified text) is represented in intent extras only.
     * In such circumstances, the request code should represent the distinguishing info
     * (e.g. by generating a hashcode) so that the generated PendingIntent is (somewhat)
     * unique. To be correct, the PendingIntent should be definitely unique but we try a
     * best effort approach that avoids spamming the system with PendingIntents.
     */
    // TODO: Fix the issue mentioned above so the behaviour is correct.
    public LabeledIntent(@android.annotation.Nullable
    java.lang.String titleWithoutEntity, @android.annotation.Nullable
    java.lang.String titleWithEntity, java.lang.String description, @android.annotation.Nullable
    java.lang.String descriptionWithAppName, android.content.Intent intent, int requestCode) {
        if (android.text.TextUtils.isEmpty(titleWithEntity) && android.text.TextUtils.isEmpty(titleWithoutEntity)) {
            throw new java.lang.IllegalArgumentException("titleWithEntity and titleWithoutEntity should not be both null");
        }
        this.titleWithoutEntity = titleWithoutEntity;
        this.titleWithEntity = titleWithEntity;
        this.description = com.android.internal.util.Preconditions.checkNotNull(description);
        this.descriptionWithAppName = descriptionWithAppName;
        this.intent = com.android.internal.util.Preconditions.checkNotNull(intent);
        this.requestCode = requestCode;
    }

    /**
     * Return the resolved result.
     *
     * @param context
     * 		the context to resolve the result's intent and action
     * @param titleChooser
     * 		for choosing an action title
     * @param textLanguagesBundle
     * 		containing language detection information
     */
    @android.annotation.Nullable
    public android.view.textclassifier.intent.LabeledIntent.Result resolve(android.content.Context context, @android.annotation.Nullable
    android.view.textclassifier.intent.LabeledIntent.TitleChooser titleChooser, @android.annotation.Nullable
    android.os.Bundle textLanguagesBundle) {
        final android.content.pm.PackageManager pm = context.getPackageManager();
        final android.content.pm.ResolveInfo resolveInfo = pm.resolveActivity(intent, 0);
        if ((resolveInfo == null) || (resolveInfo.activityInfo == null)) {
            android.view.textclassifier.Log.w(android.view.textclassifier.intent.LabeledIntent.TAG, "resolveInfo or activityInfo is null");
            return null;
        }
        final java.lang.String packageName = resolveInfo.activityInfo.packageName;
        final java.lang.String className = resolveInfo.activityInfo.name;
        if ((packageName == null) || (className == null)) {
            android.view.textclassifier.Log.w(android.view.textclassifier.intent.LabeledIntent.TAG, "packageName or className is null");
            return null;
        }
        android.content.Intent resolvedIntent = new android.content.Intent(intent);
        resolvedIntent.putExtra(android.view.textclassifier.TextClassifier.EXTRA_FROM_TEXT_CLASSIFIER, getFromTextClassifierExtra(textLanguagesBundle));
        boolean shouldShowIcon = false;
        android.graphics.drawable.Icon icon = null;
        if (!"android".equals(packageName)) {
            // We only set the component name when the package name is not resolved to "android"
            // to workaround a bug that explicit intent with component name == ResolverActivity
            // can't be launched on keyguard.
            resolvedIntent.setComponent(new android.content.ComponentName(packageName, className));
            if (resolveInfo.activityInfo.getIconResource() != 0) {
                icon = android.graphics.drawable.Icon.createWithResource(packageName, resolveInfo.activityInfo.getIconResource());
                shouldShowIcon = true;
            }
        }
        if (icon == null) {
            // RemoteAction requires that there be an icon.
            icon = android.graphics.drawable.Icon.createWithResource("android", com.android.internal.R.drawable.ic_more_items);
        }
        final android.app.PendingIntent pendingIntent = android.view.textclassifier.TextClassification.createPendingIntent(context, resolvedIntent, requestCode);
        titleChooser = (titleChooser == null) ? android.view.textclassifier.intent.LabeledIntent.DEFAULT_TITLE_CHOOSER : titleChooser;
        java.lang.CharSequence title = titleChooser.chooseTitle(this, resolveInfo);
        if (android.text.TextUtils.isEmpty(title)) {
            android.view.textclassifier.Log.w(android.view.textclassifier.intent.LabeledIntent.TAG, "Custom titleChooser return null, fallback to the default titleChooser");
            title = android.view.textclassifier.intent.LabeledIntent.DEFAULT_TITLE_CHOOSER.chooseTitle(this, resolveInfo);
        }
        final android.app.RemoteAction action = new android.app.RemoteAction(icon, title, resolveDescription(resolveInfo, pm), pendingIntent);
        action.setShouldShowIcon(shouldShowIcon);
        return new android.view.textclassifier.intent.LabeledIntent.Result(resolvedIntent, action);
    }

    private java.lang.String resolveDescription(android.content.pm.ResolveInfo resolveInfo, android.content.pm.PackageManager packageManager) {
        if (!android.text.TextUtils.isEmpty(descriptionWithAppName)) {
            // Example string format of descriptionWithAppName: "Use %1$s to open map".
            java.lang.String applicationName = getApplicationName(resolveInfo, packageManager);
            if (!android.text.TextUtils.isEmpty(applicationName)) {
                return java.lang.String.format(descriptionWithAppName, applicationName);
            }
        }
        return description;
    }

    @android.annotation.Nullable
    private java.lang.String getApplicationName(android.content.pm.ResolveInfo resolveInfo, android.content.pm.PackageManager packageManager) {
        if (resolveInfo.activityInfo == null) {
            return null;
        }
        if ("android".equals(resolveInfo.activityInfo.packageName)) {
            return null;
        }
        if (resolveInfo.activityInfo.applicationInfo == null) {
            return null;
        }
        return ((java.lang.String) (packageManager.getApplicationLabel(resolveInfo.activityInfo.applicationInfo)));
    }

    private android.os.Bundle getFromTextClassifierExtra(@android.annotation.Nullable
    android.os.Bundle textLanguagesBundle) {
        if (textLanguagesBundle != null) {
            final android.os.Bundle bundle = new android.os.Bundle();
            android.view.textclassifier.ExtrasUtils.putTextLanguagesExtra(bundle, textLanguagesBundle);
            return bundle;
        } else {
            return android.os.Bundle.EMPTY;
        }
    }

    /**
     * Data class that holds the result.
     */
    public static final class Result {
        public final android.content.Intent resolvedIntent;

        public final android.app.RemoteAction remoteAction;

        public Result(android.content.Intent resolvedIntent, android.app.RemoteAction remoteAction) {
            this.resolvedIntent = com.android.internal.util.Preconditions.checkNotNull(resolvedIntent);
            this.remoteAction = com.android.internal.util.Preconditions.checkNotNull(remoteAction);
        }
    }

    /**
     * An object to choose a title from resolved info.  If {@code null} is returned,
     * {@link #titleWithEntity} will be used if it exists, {@link #titleWithoutEntity} otherwise.
     */
    public interface TitleChooser {
        /**
         * Picks a title from a {@link LabeledIntent} by looking into resolved info.
         * {@code resolveInfo} is guaranteed to have a non-null {@code activityInfo}.
         */
        @android.annotation.Nullable
        java.lang.CharSequence chooseTitle(android.view.textclassifier.intent.LabeledIntent labeledIntent, android.content.pm.ResolveInfo resolveInfo);
    }
}

