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
package android.app;


/**
 * Allows an app to control the status bar.
 *
 * @unknown 
 */
public class StatusBarManager {
    public static final int DISABLE_EXPAND = android.view.View.STATUS_BAR_DISABLE_EXPAND;

    public static final int DISABLE_NOTIFICATION_ICONS = android.view.View.STATUS_BAR_DISABLE_NOTIFICATION_ICONS;

    public static final int DISABLE_NOTIFICATION_ALERTS = android.view.View.STATUS_BAR_DISABLE_NOTIFICATION_ALERTS;

    @java.lang.Deprecated
    public static final int DISABLE_NOTIFICATION_TICKER = android.view.View.STATUS_BAR_DISABLE_NOTIFICATION_TICKER;

    public static final int DISABLE_SYSTEM_INFO = android.view.View.STATUS_BAR_DISABLE_SYSTEM_INFO;

    public static final int DISABLE_HOME = android.view.View.STATUS_BAR_DISABLE_HOME;

    public static final int DISABLE_RECENT = android.view.View.STATUS_BAR_DISABLE_RECENT;

    public static final int DISABLE_BACK = android.view.View.STATUS_BAR_DISABLE_BACK;

    public static final int DISABLE_CLOCK = android.view.View.STATUS_BAR_DISABLE_CLOCK;

    public static final int DISABLE_SEARCH = android.view.View.STATUS_BAR_DISABLE_SEARCH;

    @java.lang.Deprecated
    public static final int DISABLE_NAVIGATION = android.view.View.STATUS_BAR_DISABLE_HOME | android.view.View.STATUS_BAR_DISABLE_RECENT;

    public static final int DISABLE_NONE = 0x0;

    public static final int DISABLE_MASK = ((((((((android.app.StatusBarManager.DISABLE_EXPAND | android.app.StatusBarManager.DISABLE_NOTIFICATION_ICONS) | android.app.StatusBarManager.DISABLE_NOTIFICATION_ALERTS) | android.app.StatusBarManager.DISABLE_NOTIFICATION_TICKER) | android.app.StatusBarManager.DISABLE_SYSTEM_INFO) | android.app.StatusBarManager.DISABLE_RECENT) | android.app.StatusBarManager.DISABLE_HOME) | android.app.StatusBarManager.DISABLE_BACK) | android.app.StatusBarManager.DISABLE_CLOCK) | android.app.StatusBarManager.DISABLE_SEARCH;

    /**
     * Flag to disable quick settings.
     *
     * Setting this flag disables quick settings completely, but does not disable expanding the
     * notification shade.
     */
    public static final int DISABLE2_QUICK_SETTINGS = 0x1;

    public static final int DISABLE2_NONE = 0x0;

    public static final int DISABLE2_MASK = android.app.StatusBarManager.DISABLE2_QUICK_SETTINGS;

    @android.annotation.IntDef(flag = true, value = { android.app.StatusBarManager.DISABLE2_NONE, android.app.StatusBarManager.DISABLE2_MASK, android.app.StatusBarManager.DISABLE2_QUICK_SETTINGS })
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface Disable2Flags {}

    public static final int NAVIGATION_HINT_BACK_ALT = 1 << 0;

    public static final int NAVIGATION_HINT_IME_SHOWN = 1 << 1;

    public static final int WINDOW_STATUS_BAR = 1;

    public static final int WINDOW_NAVIGATION_BAR = 2;

    public static final int WINDOW_STATE_SHOWING = 0;

    public static final int WINDOW_STATE_HIDING = 1;

    public static final int WINDOW_STATE_HIDDEN = 2;

    public static final int CAMERA_LAUNCH_SOURCE_WIGGLE = 0;

    public static final int CAMERA_LAUNCH_SOURCE_POWER_DOUBLE_TAP = 1;

    private android.content.Context mContext;

    private com.android.internal.statusbar.IStatusBarService mService;

    private android.os.IBinder mToken = new android.os.Binder();

    StatusBarManager(android.content.Context context) {
        mContext = context;
    }

    private synchronized com.android.internal.statusbar.IStatusBarService getService() {
        if (mService == null) {
            mService = IStatusBarService.Stub.asInterface(android.os.ServiceManager.getService(android.content.Context.STATUS_BAR_SERVICE));
            if (mService == null) {
                android.util.Slog.w("StatusBarManager", "warning: no STATUS_BAR_SERVICE");
            }
        }
        return mService;
    }

    /**
     * Disable some features in the status bar.  Pass the bitwise-or of the DISABLE_* flags.
     * To re-enable everything, pass {@link #DISABLE_NONE}.
     */
    public void disable(int what) {
        try {
            final com.android.internal.statusbar.IStatusBarService svc = getService();
            if (svc != null) {
                svc.disable(what, mToken, mContext.getPackageName());
            }
        } catch (android.os.RemoteException ex) {
            throw ex.rethrowFromSystemServer();
        }
    }

    /**
     * Disable additional status bar features. Pass the bitwise-or of the DISABLE2_* flags.
     * To re-enable everything, pass {@link #DISABLE_NONE}.
     *
     * Warning: Only pass DISABLE2_* flags into this function, do not use DISABLE_* flags.
     */
    public void disable2(@android.app.StatusBarManager.Disable2Flags
    int what) {
        try {
            final com.android.internal.statusbar.IStatusBarService svc = getService();
            if (svc != null) {
                svc.disable2(what, mToken, mContext.getPackageName());
            }
        } catch (android.os.RemoteException ex) {
            throw ex.rethrowFromSystemServer();
        }
    }

    /**
     * Expand the notifications panel.
     */
    public void expandNotificationsPanel() {
        try {
            final com.android.internal.statusbar.IStatusBarService svc = getService();
            if (svc != null) {
                svc.expandNotificationsPanel();
            }
        } catch (android.os.RemoteException ex) {
            throw ex.rethrowFromSystemServer();
        }
    }

    /**
     * Collapse the notifications and settings panels.
     */
    public void collapsePanels() {
        try {
            final com.android.internal.statusbar.IStatusBarService svc = getService();
            if (svc != null) {
                svc.collapsePanels();
            }
        } catch (android.os.RemoteException ex) {
            throw ex.rethrowFromSystemServer();
        }
    }

    /**
     * Expand the settings panel.
     */
    public void expandSettingsPanel() {
        expandSettingsPanel(null);
    }

    /**
     * Expand the settings panel and open a subPanel, pass null to just open the settings panel.
     */
    public void expandSettingsPanel(java.lang.String subPanel) {
        try {
            final com.android.internal.statusbar.IStatusBarService svc = getService();
            if (svc != null) {
                svc.expandSettingsPanel(subPanel);
            }
        } catch (android.os.RemoteException ex) {
            throw ex.rethrowFromSystemServer();
        }
    }

    public void setIcon(java.lang.String slot, int iconId, int iconLevel, java.lang.String contentDescription) {
        try {
            final com.android.internal.statusbar.IStatusBarService svc = getService();
            if (svc != null) {
                svc.setIcon(slot, mContext.getPackageName(), iconId, iconLevel, contentDescription);
            }
        } catch (android.os.RemoteException ex) {
            throw ex.rethrowFromSystemServer();
        }
    }

    public void removeIcon(java.lang.String slot) {
        try {
            final com.android.internal.statusbar.IStatusBarService svc = getService();
            if (svc != null) {
                svc.removeIcon(slot);
            }
        } catch (android.os.RemoteException ex) {
            throw ex.rethrowFromSystemServer();
        }
    }

    public void setIconVisibility(java.lang.String slot, boolean visible) {
        try {
            final com.android.internal.statusbar.IStatusBarService svc = getService();
            if (svc != null) {
                svc.setIconVisibility(slot, visible);
            }
        } catch (android.os.RemoteException ex) {
            throw ex.rethrowFromSystemServer();
        }
    }

    /**
     *
     *
     * @unknown 
     */
    public static java.lang.String windowStateToString(int state) {
        if (state == android.app.StatusBarManager.WINDOW_STATE_HIDING)
            return "WINDOW_STATE_HIDING";

        if (state == android.app.StatusBarManager.WINDOW_STATE_HIDDEN)
            return "WINDOW_STATE_HIDDEN";

        if (state == android.app.StatusBarManager.WINDOW_STATE_SHOWING)
            return "WINDOW_STATE_SHOWING";

        return "WINDOW_STATE_UNKNOWN";
    }
}

