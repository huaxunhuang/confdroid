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
package android.content.pm;


/**
 * Entry points used by {@link LauncherApps}.
 *
 * <p>No permission / argument checks will be performed inside.
 * Callers must check the calling app permission and the calling package name.
 *
 * @unknown 
 */
public abstract class ShortcutServiceInternal {
    public interface ShortcutChangeListener {
        void onShortcutChanged(@android.annotation.NonNull
        java.lang.String packageName, @android.annotation.UserIdInt
        int userId);
    }

    public abstract java.util.List<android.content.pm.ShortcutInfo> getShortcuts(int launcherUserId, @android.annotation.NonNull
    java.lang.String callingPackage, long changedSince, @android.annotation.Nullable
    java.lang.String packageName, @android.annotation.Nullable
    java.util.List<java.lang.String> shortcutIds, @android.annotation.Nullable
    android.content.ComponentName componentName, @android.content.pm.LauncherApps.ShortcutQuery.QueryFlags
    int flags, int userId, int callingPid, int callingUid);

    public abstract boolean isPinnedByCaller(int launcherUserId, @android.annotation.NonNull
    java.lang.String callingPackage, @android.annotation.NonNull
    java.lang.String packageName, @android.annotation.NonNull
    java.lang.String id, int userId);

    public abstract void pinShortcuts(int launcherUserId, @android.annotation.NonNull
    java.lang.String callingPackage, @android.annotation.NonNull
    java.lang.String packageName, @android.annotation.NonNull
    java.util.List<java.lang.String> shortcutIds, int userId);

    public abstract android.content.Intent[] createShortcutIntents(int launcherUserId, @android.annotation.NonNull
    java.lang.String callingPackage, @android.annotation.NonNull
    java.lang.String packageName, @android.annotation.NonNull
    java.lang.String shortcutId, int userId, int callingPid, int callingUid);

    public abstract void addListener(@android.annotation.NonNull
    android.content.pm.ShortcutServiceInternal.ShortcutChangeListener listener);

    public abstract int getShortcutIconResId(int launcherUserId, @android.annotation.NonNull
    java.lang.String callingPackage, @android.annotation.NonNull
    java.lang.String packageName, @android.annotation.NonNull
    java.lang.String shortcutId, int userId);

    public abstract android.os.ParcelFileDescriptor getShortcutIconFd(int launcherUserId, @android.annotation.NonNull
    java.lang.String callingPackage, @android.annotation.NonNull
    java.lang.String packageName, @android.annotation.NonNull
    java.lang.String shortcutId, int userId);

    public abstract boolean hasShortcutHostPermission(int launcherUserId, @android.annotation.NonNull
    java.lang.String callingPackage, int callingPid, int callingUid);

    public abstract void setShortcutHostPackage(@android.annotation.NonNull
    java.lang.String type, @android.annotation.Nullable
    java.lang.String packageName, int userId);

    public abstract boolean requestPinAppWidget(@android.annotation.NonNull
    java.lang.String callingPackage, @android.annotation.NonNull
    android.appwidget.AppWidgetProviderInfo appWidget, @android.annotation.Nullable
    android.os.Bundle extras, @android.annotation.Nullable
    android.content.IntentSender resultIntent, int userId);

    public abstract boolean isRequestPinItemSupported(int callingUserId, int requestType);

    public abstract boolean isForegroundDefaultLauncher(@android.annotation.NonNull
    java.lang.String callingPackage, int callingUid);
}

