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
package android.service.dreams;


/**
 * Internal helper for launching dreams to ensure consistency between the
 * <code>UiModeManagerService</code> system service and the <code>Somnambulator</code> activity.
 *
 * @unknown 
 */
public final class Sandman {
    private static final java.lang.String TAG = "Sandman";

    // The component name of a special dock app that merely launches a dream.
    // We don't want to launch this app when docked because it causes an unnecessary
    // activity transition.  We just want to start the dream.
    private static final android.content.ComponentName SOMNAMBULATOR_COMPONENT = new android.content.ComponentName("com.android.systemui", "com.android.systemui.Somnambulator");

    // The sandman is eternal.  No one instantiates him.
    private Sandman() {
    }

    /**
     * Returns true if the specified dock app intent should be started.
     * False if we should dream instead, if appropriate.
     */
    public static boolean shouldStartDockApp(android.content.Context context, android.content.Intent intent) {
        android.content.ComponentName name = intent.resolveActivity(context.getPackageManager());
        return (name != null) && (!name.equals(android.service.dreams.Sandman.SOMNAMBULATOR_COMPONENT));
    }

    /**
     * Starts a dream manually.
     */
    public static void startDreamByUserRequest(android.content.Context context) {
        android.service.dreams.Sandman.startDream(context, false);
    }

    /**
     * Starts a dream when docked if the system has been configured to do so,
     * otherwise does nothing.
     */
    public static void startDreamWhenDockedIfAppropriate(android.content.Context context) {
        if ((!android.service.dreams.Sandman.isScreenSaverEnabled(context)) || (!android.service.dreams.Sandman.isScreenSaverActivatedOnDock(context))) {
            android.util.Slog.i(android.service.dreams.Sandman.TAG, "Dreams currently disabled for docks.");
            return;
        }
        android.service.dreams.Sandman.startDream(context, true);
    }

    private static void startDream(android.content.Context context, boolean docked) {
        try {
            android.service.dreams.IDreamManager dreamManagerService = IDreamManager.Stub.asInterface(android.os.ServiceManager.getService(android.service.dreams.DreamService.DREAM_SERVICE));
            if ((dreamManagerService != null) && (!dreamManagerService.isDreaming())) {
                if (docked) {
                    android.util.Slog.i(android.service.dreams.Sandman.TAG, "Activating dream while docked.");
                    // Wake up.
                    // The power manager will wake up the system automatically when it starts
                    // receiving power from a dock but there is a race between that happening
                    // and the UI mode manager starting a dream.  We want the system to already
                    // be awake by the time this happens.  Otherwise the dream may not start.
                    android.os.PowerManager powerManager = ((android.os.PowerManager) (context.getSystemService(android.content.Context.POWER_SERVICE)));
                    powerManager.wakeUp(android.os.SystemClock.uptimeMillis(), "android.service.dreams:DREAM");
                } else {
                    android.util.Slog.i(android.service.dreams.Sandman.TAG, "Activating dream by user request.");
                }
                // Dream.
                dreamManagerService.dream();
            }
        } catch (android.os.RemoteException ex) {
            android.util.Slog.e(android.service.dreams.Sandman.TAG, "Could not start dream when docked.", ex);
        }
    }

    private static boolean isScreenSaverEnabled(android.content.Context context) {
        int def = (context.getResources().getBoolean(com.android.internal.R.bool.config_dreamsEnabledByDefault)) ? 1 : 0;
        return android.provider.Settings.Secure.getIntForUser(context.getContentResolver(), android.provider.Settings.Secure.SCREENSAVER_ENABLED, def, android.os.UserHandle.USER_CURRENT) != 0;
    }

    private static boolean isScreenSaverActivatedOnDock(android.content.Context context) {
        int def = (context.getResources().getBoolean(com.android.internal.R.bool.config_dreamsActivatedOnDockByDefault)) ? 1 : 0;
        return android.provider.Settings.Secure.getIntForUser(context.getContentResolver(), android.provider.Settings.Secure.SCREENSAVER_ACTIVATE_ON_DOCK, def, android.os.UserHandle.USER_CURRENT) != 0;
    }
}

