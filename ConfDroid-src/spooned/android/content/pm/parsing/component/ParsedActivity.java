/**
 * Copyright (C) 2020 The Android Open Source Project
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
package android.content.pm.parsing.component;


/**
 *
 *
 * @unknown *
 */
public class ParsedActivity extends android.content.pm.parsing.component.ParsedMainComponent {
    int theme;

    int uiOptions;

    @android.annotation.Nullable
    @com.android.internal.util.DataClass.ParcelWith(com.android.internal.util.Parcelling.BuiltIn.ForInternedString.class)
    private java.lang.String targetActivity;

    @android.annotation.Nullable
    @com.android.internal.util.DataClass.ParcelWith(com.android.internal.util.Parcelling.BuiltIn.ForInternedString.class)
    private java.lang.String parentActivityName;

    @android.annotation.Nullable
    java.lang.String taskAffinity;

    int privateFlags;

    @android.annotation.Nullable
    @com.android.internal.util.DataClass.ParcelWith(com.android.internal.util.Parcelling.BuiltIn.ForInternedString.class)
    private java.lang.String permission;

    int launchMode;

    int documentLaunchMode;

    int maxRecents;

    int configChanges;

    int softInputMode;

    int persistableMode;

    int lockTaskLaunchMode;

    int screenOrientation = android.content.pm.ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED;

    int resizeMode = android.content.pm.ActivityInfo.RESIZE_MODE_RESIZEABLE;

    @android.annotation.Nullable
    private java.lang.Float maxAspectRatio;

    @android.annotation.Nullable
    private java.lang.Float minAspectRatio;

    private boolean supportsSizeChanges;

    @android.annotation.Nullable
    java.lang.String requestedVrComponent;

    int rotationAnimation = -1;

    int colorMode;

    @android.annotation.Nullable
    android.content.pm.ActivityInfo.WindowLayout windowLayout;

    public ParsedActivity(android.content.pm.parsing.component.ParsedActivity other) {
        super(other);
        this.theme = other.theme;
        this.uiOptions = other.uiOptions;
        this.targetActivity = other.targetActivity;
        this.parentActivityName = other.parentActivityName;
        this.taskAffinity = other.taskAffinity;
        this.privateFlags = other.privateFlags;
        this.permission = other.permission;
        this.launchMode = other.launchMode;
        this.documentLaunchMode = other.documentLaunchMode;
        this.maxRecents = other.maxRecents;
        this.configChanges = other.configChanges;
        this.softInputMode = other.softInputMode;
        this.persistableMode = other.persistableMode;
        this.lockTaskLaunchMode = other.lockTaskLaunchMode;
        this.screenOrientation = other.screenOrientation;
        this.resizeMode = other.resizeMode;
        this.maxAspectRatio = other.maxAspectRatio;
        this.minAspectRatio = other.minAspectRatio;
        this.supportsSizeChanges = other.supportsSizeChanges;
        this.requestedVrComponent = other.requestedVrComponent;
        this.rotationAnimation = other.rotationAnimation;
        this.colorMode = other.colorMode;
        this.windowLayout = other.windowLayout;
    }

    /**
     * Generate activity object that forwards user to App Details page automatically.
     * This activity should be invisible to user and user should not know or see it.
     */
    public static android.content.pm.parsing.component.ParsedActivity makeAppDetailsActivity(java.lang.String packageName, java.lang.String processName, int uiOptions, java.lang.String taskAffinity, boolean hardwareAccelerated) {
        android.content.pm.parsing.component.ParsedActivity activity = new android.content.pm.parsing.component.ParsedActivity();
        activity.setPackageName(packageName);
        activity.theme = android.content.pm.parsing.component.android.R.style;
        activity.exported = true;
        activity.setName(android.content.pm.PackageManager.APP_DETAILS_ACTIVITY_CLASS_NAME);
        activity.setProcessName(processName);
        activity.uiOptions = uiOptions;
        activity.taskAffinity = taskAffinity;
        activity.launchMode = android.content.pm.ActivityInfo.LAUNCH_MULTIPLE;
        activity.documentLaunchMode = android.content.pm.ActivityInfo.DOCUMENT_LAUNCH_NONE;
        activity.maxRecents = android.app.ActivityTaskManager.getDefaultAppRecentsLimitStatic();
        activity.configChanges = android.content.pm.PackageParser.getActivityConfigChanges(0, 0);
        activity.softInputMode = 0;
        activity.persistableMode = android.content.pm.ActivityInfo.PERSIST_NEVER;
        activity.screenOrientation = android.content.pm.ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED;
        activity.resizeMode = android.content.pm.ActivityInfo.RESIZE_MODE_FORCE_RESIZEABLE;
        activity.lockTaskLaunchMode = 0;
        activity.setDirectBootAware(false);
        activity.rotationAnimation = android.view.WindowManager.LayoutParams.ROTATION_ANIMATION_UNSPECIFIED;
        activity.colorMode = android.content.pm.ActivityInfo.COLOR_MODE_DEFAULT;
        if (hardwareAccelerated) {
            activity.setFlags(activity.getFlags() | android.content.pm.ActivityInfo.FLAG_HARDWARE_ACCELERATED);
        }
        return activity;
    }

    static android.content.pm.parsing.component.ParsedActivity makeAlias(java.lang.String targetActivityName, android.content.pm.parsing.component.ParsedActivity target) {
        android.content.pm.parsing.component.ParsedActivity alias = new android.content.pm.parsing.component.ParsedActivity();
        alias.setPackageName(target.getPackageName());
        alias.setTargetActivity(targetActivityName);
        alias.configChanges = target.configChanges;
        alias.flags = target.flags;
        alias.privateFlags = target.privateFlags;
        alias.icon = target.icon;
        alias.logo = target.logo;
        alias.banner = target.banner;
        alias.labelRes = target.labelRes;
        alias.nonLocalizedLabel = target.nonLocalizedLabel;
        alias.launchMode = target.launchMode;
        alias.lockTaskLaunchMode = target.lockTaskLaunchMode;
        alias.descriptionRes = target.descriptionRes;
        alias.screenOrientation = target.screenOrientation;
        alias.taskAffinity = target.taskAffinity;
        alias.theme = target.theme;
        alias.softInputMode = target.softInputMode;
        alias.uiOptions = target.uiOptions;
        alias.parentActivityName = target.parentActivityName;
        alias.maxRecents = target.maxRecents;
        alias.windowLayout = target.windowLayout;
        alias.resizeMode = target.resizeMode;
        alias.maxAspectRatio = target.maxAspectRatio;
        alias.minAspectRatio = target.minAspectRatio;
        alias.supportsSizeChanges = target.supportsSizeChanges;
        alias.requestedVrComponent = target.requestedVrComponent;
        alias.directBootAware = target.directBootAware;
        alias.setProcessName(target.getProcessName());
        return alias;
        // Not all attributes from the target ParsedActivity are copied to the alias.
        // Careful when adding an attribute and determine whether or not it should be copied.
        // alias.enabled = target.enabled;
        // alias.exported = target.exported;
        // alias.permission = target.permission;
        // alias.splitName = target.splitName;
        // alias.documentLaunchMode = target.documentLaunchMode;
        // alias.persistableMode = target.persistableMode;
        // alias.rotationAnimation = target.rotationAnimation;
        // alias.colorMode = target.colorMode;
        // alias.intents.addAll(target.intents);
        // alias.order = target.order;
        // alias.metaData = target.metaData;
    }

    public android.content.pm.parsing.component.ParsedActivity setMaxAspectRatio(int resizeMode, float maxAspectRatio) {
        if ((resizeMode == android.content.pm.ActivityInfo.RESIZE_MODE_RESIZEABLE) || (resizeMode == android.content.pm.ActivityInfo.RESIZE_MODE_RESIZEABLE_VIA_SDK_VERSION)) {
            // Resizeable activities can be put in any aspect ratio.
            return this;
        }
        if ((maxAspectRatio < 1.0F) && (maxAspectRatio != 0)) {
            // Ignore any value lesser than 1.0.
            return this;
        }
        this.maxAspectRatio = maxAspectRatio;
        return this;
    }

    public android.content.pm.parsing.component.ParsedActivity setMinAspectRatio(int resizeMode, float minAspectRatio) {
        if ((resizeMode == android.content.pm.ActivityInfo.RESIZE_MODE_RESIZEABLE) || (resizeMode == android.content.pm.ActivityInfo.RESIZE_MODE_RESIZEABLE_VIA_SDK_VERSION)) {
            // Resizeable activities can be put in any aspect ratio.
            return this;
        }
        if ((minAspectRatio < 1.0F) && (minAspectRatio != 0)) {
            // Ignore any value lesser than 1.0.
            return this;
        }
        this.minAspectRatio = minAspectRatio;
        return this;
    }

    public android.content.pm.parsing.component.ParsedActivity setSupportsSizeChanges(boolean supportsSizeChanges) {
        this.supportsSizeChanges = supportsSizeChanges;
        return this;
    }

    public android.content.pm.parsing.component.ParsedActivity setFlags(int flags) {
        this.flags = flags;
        return this;
    }

    public android.content.pm.parsing.component.ParsedActivity setResizeMode(int resizeMode) {
        this.resizeMode = resizeMode;
        return this;
    }

    public android.content.pm.parsing.component.ParsedActivity setTargetActivity(java.lang.String targetActivity) {
        this.targetActivity = android.text.TextUtils.safeIntern(targetActivity);
        return this;
    }

    public android.content.pm.parsing.component.ParsedActivity setParentActivity(java.lang.String parentActivity) {
        this.parentActivityName = android.text.TextUtils.safeIntern(parentActivity);
        return this;
    }

    public android.content.pm.parsing.component.ParsedActivity setPermission(java.lang.String permission) {
        // Empty string must be converted to null
        this.permission = (android.text.TextUtils.isEmpty(permission)) ? null : permission.intern();
        return this;
    }

    public java.lang.String toString() {
        java.lang.StringBuilder sb = new java.lang.StringBuilder(128);
        sb.append("Activity{");
        sb.append(java.lang.Integer.toHexString(java.lang.System.identityHashCode(this)));
        sb.append(' ');
        android.content.ComponentName.appendShortString(sb, getPackageName(), getName());
        sb.append('}');
        return sb.toString();
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(this.theme);
        dest.writeInt(this.uiOptions);
        dest.writeString(this.targetActivity);
        dest.writeString(this.parentActivityName);
        dest.writeString(this.taskAffinity);
        dest.writeInt(this.privateFlags);
        android.content.pm.parsing.ParsingPackageImpl.sForInternedString.parcel(this.permission, dest, flags);
        dest.writeInt(this.launchMode);
        dest.writeInt(this.documentLaunchMode);
        dest.writeInt(this.maxRecents);
        dest.writeInt(this.configChanges);
        dest.writeInt(this.softInputMode);
        dest.writeInt(this.persistableMode);
        dest.writeInt(this.lockTaskLaunchMode);
        dest.writeInt(this.screenOrientation);
        dest.writeInt(this.resizeMode);
        dest.writeValue(this.maxAspectRatio);
        dest.writeValue(this.minAspectRatio);
        dest.writeBoolean(this.supportsSizeChanges);
        dest.writeString(this.requestedVrComponent);
        dest.writeInt(this.rotationAnimation);
        dest.writeInt(this.colorMode);
        dest.writeBundle(this.metaData);
        if (windowLayout != null) {
            dest.writeInt(1);
            windowLayout.writeToParcel(dest);
        } else {
            dest.writeBoolean(false);
        }
    }

    public ParsedActivity() {
    }

    protected ParsedActivity(android.os.Parcel in) {
        super(in);
        this.theme = in.readInt();
        this.uiOptions = in.readInt();
        this.targetActivity = in.readString();
        this.parentActivityName = in.readString();
        this.taskAffinity = in.readString();
        this.privateFlags = in.readInt();
        this.permission = android.content.pm.parsing.ParsingPackageImpl.sForInternedString.unparcel(in);
        this.launchMode = in.readInt();
        this.documentLaunchMode = in.readInt();
        this.maxRecents = in.readInt();
        this.configChanges = in.readInt();
        this.softInputMode = in.readInt();
        this.persistableMode = in.readInt();
        this.lockTaskLaunchMode = in.readInt();
        this.screenOrientation = in.readInt();
        this.resizeMode = in.readInt();
        this.maxAspectRatio = ((java.lang.Float) (in.readValue(java.lang.Float.class.getClassLoader())));
        this.minAspectRatio = ((java.lang.Float) (in.readValue(java.lang.Float.class.getClassLoader())));
        this.supportsSizeChanges = in.readBoolean();
        this.requestedVrComponent = in.readString();
        this.rotationAnimation = in.readInt();
        this.colorMode = in.readInt();
        this.metaData = in.readBundle();
        if (in.readBoolean()) {
            windowLayout = new android.content.pm.ActivityInfo.WindowLayout(in);
        }
    }

    public static final android.os.Parcelable.Creator<android.content.pm.parsing.component.ParsedActivity> CREATOR = new android.content.pm.parsing.component.Creator<android.content.pm.parsing.component.ParsedActivity>() {
        @java.lang.Override
        public android.content.pm.parsing.component.ParsedActivity createFromParcel(android.os.Parcel source) {
            return new android.content.pm.parsing.component.ParsedActivity(source);
        }

        @java.lang.Override
        public android.content.pm.parsing.component.ParsedActivity[] newArray(int size) {
            return new android.content.pm.parsing.component.ParsedActivity[size];
        }
    };

    public int getTheme() {
        return theme;
    }

    public int getUiOptions() {
        return uiOptions;
    }

    @android.annotation.Nullable
    public java.lang.String getTargetActivity() {
        return targetActivity;
    }

    @android.annotation.Nullable
    public java.lang.String getParentActivityName() {
        return parentActivityName;
    }

    @android.annotation.Nullable
    public java.lang.String getTaskAffinity() {
        return taskAffinity;
    }

    public int getPrivateFlags() {
        return privateFlags;
    }

    @android.annotation.Nullable
    public java.lang.String getPermission() {
        return permission;
    }

    public int getLaunchMode() {
        return launchMode;
    }

    public int getDocumentLaunchMode() {
        return documentLaunchMode;
    }

    public int getMaxRecents() {
        return maxRecents;
    }

    public int getConfigChanges() {
        return configChanges;
    }

    public int getSoftInputMode() {
        return softInputMode;
    }

    public int getPersistableMode() {
        return persistableMode;
    }

    public int getLockTaskLaunchMode() {
        return lockTaskLaunchMode;
    }

    public int getScreenOrientation() {
        return screenOrientation;
    }

    public int getResizeMode() {
        return resizeMode;
    }

    @android.annotation.Nullable
    public java.lang.Float getMaxAspectRatio() {
        return maxAspectRatio;
    }

    @android.annotation.Nullable
    public java.lang.Float getMinAspectRatio() {
        return minAspectRatio;
    }

    public boolean getSupportsSizeChanges() {
        return supportsSizeChanges;
    }

    @android.annotation.Nullable
    public java.lang.String getRequestedVrComponent() {
        return requestedVrComponent;
    }

    public int getRotationAnimation() {
        return rotationAnimation;
    }

    public int getColorMode() {
        return colorMode;
    }

    @android.annotation.Nullable
    public android.content.pm.ActivityInfo.WindowLayout getWindowLayout() {
        return windowLayout;
    }
}

