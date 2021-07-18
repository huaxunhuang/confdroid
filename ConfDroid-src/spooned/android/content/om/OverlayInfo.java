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
 * limitations under the License.
 */
package android.content.om;


/**
 * Immutable overlay information about a package. All PackageInfos that
 * represent an overlay package will have a corresponding OverlayInfo.
 *
 * @unknown 
 */
@android.annotation.SystemApi
public final class OverlayInfo implements android.os.Parcelable {
    /**
     *
     *
     * @unknown 
     */
    /**
     *
     *
     * @unknown 
     */
    @android.annotation.IntDef(prefix = "STATE_", value = { android.content.om.OverlayInfo.STATE_UNKNOWN, android.content.om.OverlayInfo.STATE_MISSING_TARGET, android.content.om.OverlayInfo.STATE_NO_IDMAP, android.content.om.OverlayInfo.STATE_DISABLED, android.content.om.OverlayInfo.STATE_ENABLED, android.content.om.OverlayInfo.STATE_ENABLED_STATIC, // @Deprecated STATE_TARGET_IS_BEING_REPLACED,
    android.content.om.OverlayInfo.STATE_OVERLAY_IS_BEING_REPLACED })
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface State {}

    /**
     * An internal state used as the initial state of an overlay. OverlayInfo
     * objects exposed outside the {@link com.android.server.om.OverlayManagerService} should never have this
     * state.
     *
     * @unknown 
     */
    public static final int STATE_UNKNOWN = -1;

    /**
     * The target package of the overlay is not installed. The overlay cannot be enabled.
     *
     * @unknown 
     */
    public static final int STATE_MISSING_TARGET = 0;

    /**
     * Creation of idmap file failed (e.g. no matching resources). The overlay
     * cannot be enabled.
     *
     * @unknown 
     */
    public static final int STATE_NO_IDMAP = 1;

    /**
     * The overlay is currently disabled. It can be enabled.
     *
     * @see IOverlayManager#setEnabled
     * @unknown 
     */
    public static final int STATE_DISABLED = 2;

    /**
     * The overlay is currently enabled. It can be disabled.
     *
     * @see IOverlayManager#setEnabled
     * @unknown 
     */
    public static final int STATE_ENABLED = 3;

    /**
     * The target package is currently being upgraded or downgraded; the state
     * will change once the package installation has finished.
     *
     * @unknown 
     * @deprecated No longer used. Caused invalid transitions from enabled -> upgrading -> enabled,
    where an update is propagated when nothing has changed. Can occur during --dont-kill
    installs when code and resources are hot swapped and the Activity should not be relaunched.
    In all other cases, the process and therefore Activity is killed, so the state loop is
    irrelevant.
     */
    @java.lang.Deprecated
    public static final int STATE_TARGET_IS_BEING_REPLACED = 4;

    /**
     * The overlay package is currently being upgraded or downgraded; the state
     * will change once the package installation has finished.
     *
     * @unknown 
     */
    public static final int STATE_OVERLAY_IS_BEING_REPLACED = 5;

    /**
     * The overlay package is currently enabled because it is marked as
     * 'static'. It cannot be disabled but will change state if for instance
     * its target is uninstalled.
     *
     * @unknown 
     */
    public static final int STATE_ENABLED_STATIC = 6;

    /**
     * Overlay category: theme.
     * <p>
     * Change how Android (including the status bar, dialogs, ...) looks.
     *
     * @unknown 
     */
    public static final java.lang.String CATEGORY_THEME = "android.theme";

    /**
     * Package name of the overlay package
     *
     * @unknown 
     */
    public final java.lang.String packageName;

    /**
     * Package name of the target package
     *
     * @unknown 
     */
    public final java.lang.String targetPackageName;

    /**
     * Name of the target overlayable declaration.
     *
     * @unknown 
     */
    public final java.lang.String targetOverlayableName;

    /**
     * Category of the overlay package
     *
     * @unknown 
     */
    public final java.lang.String category;

    /**
     * Full path to the base APK for this overlay package
     *
     * @unknown 
     */
    public final java.lang.String baseCodePath;

    /**
     * The state of this OverlayInfo as defined by the STATE_* constants in this class.
     *
     * @unknown 
     */
    @android.annotation.UnsupportedAppUsage
    @android.content.om.OverlayInfo.State
    public final int state;

    /**
     * User handle for which this overlay applies
     *
     * @unknown 
     */
    public final int userId;

    /**
     * Priority as read from the manifest. Used if isStatic is true. Not
     * intended to be exposed to 3rd party.
     *
     * @unknown 
     */
    public final int priority;

    /**
     * isStatic as read from the manifest. If true, the overlay is
     * unconditionally loaded and cannot be unloaded. Not intended to be
     * exposed to 3rd party.
     *
     * @unknown 
     */
    public final boolean isStatic;

    /**
     * Create a new OverlayInfo based on source with an updated state.
     *
     * @param source
     * 		the source OverlayInfo to base the new instance on
     * @param state
     * 		the new state for the source OverlayInfo
     * @unknown 
     */
    public OverlayInfo(@android.annotation.NonNull
    android.content.om.OverlayInfo source, @android.content.om.OverlayInfo.State
    int state) {
        this(source.packageName, source.targetPackageName, source.targetOverlayableName, source.category, source.baseCodePath, state, source.userId, source.priority, source.isStatic);
    }

    /**
     *
     *
     * @unknown 
     */
    public OverlayInfo(@android.annotation.NonNull
    java.lang.String packageName, @android.annotation.NonNull
    java.lang.String targetPackageName, @android.annotation.Nullable
    java.lang.String targetOverlayableName, @android.annotation.Nullable
    java.lang.String category, @android.annotation.NonNull
    java.lang.String baseCodePath, int state, int userId, int priority, boolean isStatic) {
        this.packageName = packageName;
        this.targetPackageName = targetPackageName;
        this.targetOverlayableName = targetOverlayableName;
        this.category = category;
        this.baseCodePath = baseCodePath;
        this.state = state;
        this.userId = userId;
        this.priority = priority;
        this.isStatic = isStatic;
        ensureValidState();
    }

    /**
     *
     *
     * @unknown 
     */
    public OverlayInfo(android.os.Parcel source) {
        packageName = source.readString();
        targetPackageName = source.readString();
        targetOverlayableName = source.readString();
        category = source.readString();
        baseCodePath = source.readString();
        state = source.readInt();
        userId = source.readInt();
        priority = source.readInt();
        isStatic = source.readBoolean();
        ensureValidState();
    }

    /**
     * Returns package name of the current overlay.
     *
     * @unknown 
     */
    @android.annotation.SystemApi
    @android.annotation.NonNull
    public java.lang.String getPackageName() {
        return packageName;
    }

    /**
     * Returns the target package name of the current overlay.
     *
     * @unknown 
     */
    @android.annotation.SystemApi
    @android.annotation.NonNull
    public java.lang.String getTargetPackageName() {
        return targetPackageName;
    }

    /**
     * Returns the category of the current overlay.
     *
     * @unknown 
     */
    @android.annotation.SystemApi
    @android.annotation.Nullable
    public java.lang.String getCategory() {
        return category;
    }

    /**
     * Returns user handle for which this overlay applies to.
     *
     * @unknown 
     */
    @android.annotation.SystemApi
    @android.annotation.UserIdInt
    public int getUserId() {
        return userId;
    }

    /**
     * Returns name of the target overlayable declaration.
     *
     * @unknown 
     */
    @android.annotation.SystemApi
    @android.annotation.Nullable
    public java.lang.String getTargetOverlayableName() {
        return targetOverlayableName;
    }

    private void ensureValidState() {
        if (packageName == null) {
            throw new java.lang.IllegalArgumentException("packageName must not be null");
        }
        if (targetPackageName == null) {
            throw new java.lang.IllegalArgumentException("targetPackageName must not be null");
        }
        if (baseCodePath == null) {
            throw new java.lang.IllegalArgumentException("baseCodePath must not be null");
        }
        switch (state) {
            case android.content.om.OverlayInfo.STATE_UNKNOWN :
            case android.content.om.OverlayInfo.STATE_MISSING_TARGET :
            case android.content.om.OverlayInfo.STATE_NO_IDMAP :
            case android.content.om.OverlayInfo.STATE_DISABLED :
            case android.content.om.OverlayInfo.STATE_ENABLED :
            case android.content.om.OverlayInfo.STATE_ENABLED_STATIC :
            case android.content.om.OverlayInfo.STATE_TARGET_IS_BEING_REPLACED :
            case android.content.om.OverlayInfo.STATE_OVERLAY_IS_BEING_REPLACED :
                break;
            default :
                throw new java.lang.IllegalArgumentException(("State " + state) + " is not a valid state");
        }
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeString(packageName);
        dest.writeString(targetPackageName);
        dest.writeString(targetOverlayableName);
        dest.writeString(category);
        dest.writeString(baseCodePath);
        dest.writeInt(state);
        dest.writeInt(userId);
        dest.writeInt(priority);
        dest.writeBoolean(isStatic);
    }

    @android.annotation.NonNull
    public static final android.os.Parcelable.Creator<android.content.om.OverlayInfo> CREATOR = new android.os.Parcelable.Creator<android.content.om.OverlayInfo>() {
        @java.lang.Override
        public android.content.om.OverlayInfo createFromParcel(android.os.Parcel source) {
            return new android.content.om.OverlayInfo(source);
        }

        @java.lang.Override
        public android.content.om.OverlayInfo[] newArray(int size) {
            return new android.content.om.OverlayInfo[size];
        }
    };

    /**
     * Return true if this overlay is enabled, i.e. should be used to overlay
     * the resources in the target package.
     *
     * Disabled overlay packages are installed but are currently not in use.
     *
     * @return true if the overlay is enabled, else false.
     * @unknown 
     */
    @android.annotation.SystemApi
    public boolean isEnabled() {
        switch (state) {
            case android.content.om.OverlayInfo.STATE_ENABLED :
            case android.content.om.OverlayInfo.STATE_ENABLED_STATIC :
                return true;
            default :
                return false;
        }
    }

    /**
     * Translate a state to a human readable string. Only intended for
     * debugging purposes.
     *
     * @return a human readable String representing the state.
     * @unknown 
     */
    public static java.lang.String stateToString(@android.content.om.OverlayInfo.State
    int state) {
        switch (state) {
            case android.content.om.OverlayInfo.STATE_UNKNOWN :
                return "STATE_UNKNOWN";
            case android.content.om.OverlayInfo.STATE_MISSING_TARGET :
                return "STATE_MISSING_TARGET";
            case android.content.om.OverlayInfo.STATE_NO_IDMAP :
                return "STATE_NO_IDMAP";
            case android.content.om.OverlayInfo.STATE_DISABLED :
                return "STATE_DISABLED";
            case android.content.om.OverlayInfo.STATE_ENABLED :
                return "STATE_ENABLED";
            case android.content.om.OverlayInfo.STATE_ENABLED_STATIC :
                return "STATE_ENABLED_STATIC";
            case android.content.om.OverlayInfo.STATE_TARGET_IS_BEING_REPLACED :
                return "STATE_TARGET_IS_BEING_REPLACED";
            case android.content.om.OverlayInfo.STATE_OVERLAY_IS_BEING_REPLACED :
                return "STATE_OVERLAY_IS_BEING_REPLACED";
            default :
                return "<unknown state>";
        }
    }

    @java.lang.Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + userId;
        result = (prime * result) + state;
        result = (prime * result) + (packageName == null ? 0 : packageName.hashCode());
        result = (prime * result) + (targetPackageName == null ? 0 : targetPackageName.hashCode());
        result = (prime * result) + (targetOverlayableName == null ? 0 : targetOverlayableName.hashCode());
        result = (prime * result) + (category == null ? 0 : category.hashCode());
        result = (prime * result) + (baseCodePath == null ? 0 : baseCodePath.hashCode());
        return result;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        android.content.om.OverlayInfo other = ((android.content.om.OverlayInfo) (obj));
        if (userId != other.userId) {
            return false;
        }
        if (state != other.state) {
            return false;
        }
        if (!packageName.equals(other.packageName)) {
            return false;
        }
        if (!targetPackageName.equals(other.targetPackageName)) {
            return false;
        }
        if (!java.util.Objects.equals(targetOverlayableName, other.targetOverlayableName)) {
            return false;
        }
        if (!java.util.Objects.equals(category, other.category)) {
            return false;
        }
        if (!baseCodePath.equals(other.baseCodePath)) {
            return false;
        }
        return true;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return (((((((((("OverlayInfo { overlay=" + packageName) + ", targetPackage=") + targetPackageName) + (targetOverlayableName == null ? "" : ", targetOverlayable=" + targetOverlayableName)) + ", state=") + state) + " (") + android.content.om.OverlayInfo.stateToString(state)) + "), userId=") + userId) + " }";
    }
}

