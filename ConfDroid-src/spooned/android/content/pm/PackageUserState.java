/**
 * Copyright (C) 2012 The Android Open Source Project
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
package android.content.pm;


/**
 * Per-user state information about a package.
 *
 * @unknown 
 */
public class PackageUserState {
    private static final boolean DEBUG = false;

    private static final java.lang.String LOG_TAG = "PackageUserState";

    public long ceDataInode;

    public boolean installed;

    public boolean stopped;

    public boolean notLaunched;

    public boolean hidden;// Is the app restricted by owner / admin


    public int distractionFlags;

    public boolean suspended;

    public java.lang.String suspendingPackage;

    public android.content.pm.SuspendDialogInfo dialogInfo;

    public android.os.PersistableBundle suspendedAppExtras;

    public android.os.PersistableBundle suspendedLauncherExtras;

    public boolean instantApp;

    public boolean virtualPreload;

    public int enabled;

    public java.lang.String lastDisableAppCaller;

    public int domainVerificationStatus;

    public int appLinkGeneration;

    public int categoryHint = android.content.pm.ApplicationInfo.CATEGORY_UNDEFINED;

    public int installReason;

    public java.lang.String harmfulAppWarning;

    public android.util.ArraySet<java.lang.String> disabledComponents;

    public android.util.ArraySet<java.lang.String> enabledComponents;

    public java.lang.String[] overlayPaths;

    @android.annotation.UnsupportedAppUsage
    public PackageUserState() {
        installed = true;
        hidden = false;
        suspended = false;
        enabled = android.content.pm.PackageManager.COMPONENT_ENABLED_STATE_DEFAULT;
        domainVerificationStatus = android.content.pm.PackageManager.INTENT_FILTER_DOMAIN_VERIFICATION_STATUS_UNDEFINED;
        installReason = android.content.pm.PackageManager.INSTALL_REASON_UNKNOWN;
    }

    @com.android.internal.annotations.VisibleForTesting
    public PackageUserState(android.content.pm.PackageUserState o) {
        ceDataInode = o.ceDataInode;
        installed = o.installed;
        stopped = o.stopped;
        notLaunched = o.notLaunched;
        hidden = o.hidden;
        distractionFlags = o.distractionFlags;
        suspended = o.suspended;
        suspendingPackage = o.suspendingPackage;
        dialogInfo = o.dialogInfo;
        suspendedAppExtras = o.suspendedAppExtras;
        suspendedLauncherExtras = o.suspendedLauncherExtras;
        instantApp = o.instantApp;
        virtualPreload = o.virtualPreload;
        enabled = o.enabled;
        lastDisableAppCaller = o.lastDisableAppCaller;
        domainVerificationStatus = o.domainVerificationStatus;
        appLinkGeneration = o.appLinkGeneration;
        categoryHint = o.categoryHint;
        installReason = o.installReason;
        disabledComponents = com.android.internal.util.ArrayUtils.cloneOrNull(o.disabledComponents);
        enabledComponents = com.android.internal.util.ArrayUtils.cloneOrNull(o.enabledComponents);
        overlayPaths = (o.overlayPaths == null) ? null : java.util.Arrays.copyOf(o.overlayPaths, o.overlayPaths.length);
        harmfulAppWarning = o.harmfulAppWarning;
    }

    /**
     * Test if this package is installed.
     */
    public boolean isAvailable(int flags) {
        // True if it is installed for this user and it is not hidden. If it is hidden,
        // still return true if the caller requested MATCH_UNINSTALLED_PACKAGES
        final boolean matchAnyUser = (flags & android.content.pm.PackageManager.MATCH_ANY_USER) != 0;
        final boolean matchUninstalled = (flags & android.content.pm.PackageManager.MATCH_UNINSTALLED_PACKAGES) != 0;
        return matchAnyUser || (this.installed && ((!this.hidden) || matchUninstalled));
    }

    /**
     * Test if the given component is considered installed, enabled and a match
     * for the given flags.
     *
     * <p>
     * Expects at least one of {@link PackageManager#MATCH_DIRECT_BOOT_AWARE} and
     * {@link PackageManager#MATCH_DIRECT_BOOT_UNAWARE} are specified in {@code flags}.
     * </p>
     */
    public boolean isMatch(android.content.pm.ComponentInfo componentInfo, int flags) {
        final boolean isSystemApp = componentInfo.applicationInfo.isSystemApp();
        final boolean matchUninstalled = (flags & android.content.pm.PackageManager.MATCH_KNOWN_PACKAGES) != 0;
        if ((!isAvailable(flags)) && (!(isSystemApp && matchUninstalled)))
            return reportIfDebug(false, flags);

        if (!isEnabled(componentInfo, flags))
            return reportIfDebug(false, flags);

        if ((flags & android.content.pm.PackageManager.MATCH_SYSTEM_ONLY) != 0) {
            if (!isSystemApp) {
                return reportIfDebug(false, flags);
            }
        }
        final boolean matchesUnaware = ((flags & android.content.pm.PackageManager.MATCH_DIRECT_BOOT_UNAWARE) != 0) && (!componentInfo.directBootAware);
        final boolean matchesAware = ((flags & android.content.pm.PackageManager.MATCH_DIRECT_BOOT_AWARE) != 0) && componentInfo.directBootAware;
        return reportIfDebug(matchesUnaware || matchesAware, flags);
    }

    private boolean reportIfDebug(boolean result, int flags) {
        if (android.content.pm.PackageUserState.DEBUG && (!result)) {
            android.util.Slog.i(android.content.pm.PackageUserState.LOG_TAG, (("No match!; flags: " + android.util.DebugUtils.flagsToString(android.content.pm.PackageManager.class, "MATCH_", flags)) + " ") + android.os.Debug.getCaller());
        }
        return result;
    }

    /**
     * Test if the given component is considered enabled.
     */
    public boolean isEnabled(android.content.pm.ComponentInfo componentInfo, int flags) {
        if ((flags & android.content.pm.PackageManager.MATCH_DISABLED_COMPONENTS) != 0) {
            return true;
        }
        // First check if the overall package is disabled; if the package is
        // enabled then fall through to check specific component
        switch (this.enabled) {
            case android.content.pm.PackageManager.COMPONENT_ENABLED_STATE_DISABLED :
            case android.content.pm.PackageManager.COMPONENT_ENABLED_STATE_DISABLED_USER :
                return false;
            case android.content.pm.PackageManager.COMPONENT_ENABLED_STATE_DISABLED_UNTIL_USED :
                if ((flags & android.content.pm.PackageManager.MATCH_DISABLED_UNTIL_USED_COMPONENTS) == 0) {
                    return false;
                }
            case android.content.pm.PackageManager.COMPONENT_ENABLED_STATE_DEFAULT :
                if (!componentInfo.applicationInfo.enabled) {
                    return false;
                }
            case android.content.pm.PackageManager.COMPONENT_ENABLED_STATE_ENABLED :
                break;
        }
        // Check if component has explicit state before falling through to
        // the manifest default
        if (com.android.internal.util.ArrayUtils.contains(this.enabledComponents, componentInfo.name)) {
            return true;
        }
        if (com.android.internal.util.ArrayUtils.contains(this.disabledComponents, componentInfo.name)) {
            return false;
        }
        return componentInfo.enabled;
    }

    @java.lang.Override
    public final boolean equals(java.lang.Object obj) {
        if (!(obj instanceof android.content.pm.PackageUserState)) {
            return false;
        }
        final android.content.pm.PackageUserState oldState = ((android.content.pm.PackageUserState) (obj));
        if (ceDataInode != oldState.ceDataInode) {
            return false;
        }
        if (installed != oldState.installed) {
            return false;
        }
        if (stopped != oldState.stopped) {
            return false;
        }
        if (notLaunched != oldState.notLaunched) {
            return false;
        }
        if (hidden != oldState.hidden) {
            return false;
        }
        if (distractionFlags != oldState.distractionFlags) {
            return false;
        }
        if (suspended != oldState.suspended) {
            return false;
        }
        if (suspended) {
            if ((suspendingPackage == null) || (!suspendingPackage.equals(oldState.suspendingPackage))) {
                return false;
            }
            if (!java.util.Objects.equals(dialogInfo, oldState.dialogInfo)) {
                return false;
            }
            if (!android.os.BaseBundle.kindofEquals(suspendedAppExtras, oldState.suspendedAppExtras)) {
                return false;
            }
            if (!android.os.BaseBundle.kindofEquals(suspendedLauncherExtras, oldState.suspendedLauncherExtras)) {
                return false;
            }
        }
        if (instantApp != oldState.instantApp) {
            return false;
        }
        if (virtualPreload != oldState.virtualPreload) {
            return false;
        }
        if (enabled != oldState.enabled) {
            return false;
        }
        if (((lastDisableAppCaller == null) && (oldState.lastDisableAppCaller != null)) || ((lastDisableAppCaller != null) && (!lastDisableAppCaller.equals(oldState.lastDisableAppCaller)))) {
            return false;
        }
        if (domainVerificationStatus != oldState.domainVerificationStatus) {
            return false;
        }
        if (appLinkGeneration != oldState.appLinkGeneration) {
            return false;
        }
        if (categoryHint != oldState.categoryHint) {
            return false;
        }
        if (installReason != oldState.installReason) {
            return false;
        }
        if (((disabledComponents == null) && (oldState.disabledComponents != null)) || ((disabledComponents != null) && (oldState.disabledComponents == null))) {
            return false;
        }
        if (disabledComponents != null) {
            if (disabledComponents.size() != oldState.disabledComponents.size()) {
                return false;
            }
            for (int i = disabledComponents.size() - 1; i >= 0; --i) {
                if (!oldState.disabledComponents.contains(disabledComponents.valueAt(i))) {
                    return false;
                }
            }
        }
        if (((enabledComponents == null) && (oldState.enabledComponents != null)) || ((enabledComponents != null) && (oldState.enabledComponents == null))) {
            return false;
        }
        if (enabledComponents != null) {
            if (enabledComponents.size() != oldState.enabledComponents.size()) {
                return false;
            }
            for (int i = enabledComponents.size() - 1; i >= 0; --i) {
                if (!oldState.enabledComponents.contains(enabledComponents.valueAt(i))) {
                    return false;
                }
            }
        }
        if (((harmfulAppWarning == null) && (oldState.harmfulAppWarning != null)) || ((harmfulAppWarning != null) && (!harmfulAppWarning.equals(oldState.harmfulAppWarning)))) {
            return false;
        }
        return true;
    }
}

