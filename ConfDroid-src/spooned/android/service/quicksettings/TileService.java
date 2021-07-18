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
package android.service.quicksettings;


/**
 * A TileService provides the user a tile that can be added to Quick Settings.
 * Quick Settings is a space provided that allows the user to change settings and
 * take quick actions without leaving the context of their current app.
 *
 * <p>The lifecycle of a TileService is different from some other services in
 * that it may be unbound during parts of its lifecycle.  Any of the following
 * lifecycle events can happen indepently in a separate binding/creation of the
 * service.</p>
 *
 * <ul>
 * <li>When a tile is added by the user its TileService will be bound to and
 * {@link #onTileAdded()} will be called.</li>
 *
 * <li>When a tile should be up to date and listing will be indicated by
 * {@link #onStartListening()} and {@link #onStopListening()}.</li>
 *
 * <li>When the user removes a tile from Quick Settings {@link #onTileRemoved()}
 * will be called.</li>
 * </ul>
 * <p>TileService will be detected by tiles that match the {@value #ACTION_QS_TILE}
 * and require the permission "android.permission.BIND_QUICK_SETTINGS_TILE".
 * The label and icon for the service will be used as the default label and
 * icon for the tile. Here is an example TileService declaration.</p>
 * <pre class="prettyprint">
 * {@literal <service
 *     android:name=".MyQSTileService"
 *     android:label="@string/my_default_tile_label"
 *     android:icon="@drawable/my_default_icon_label"
 *     android:permission="android.permission.BIND_QUICK_SETTINGS_TILE">
 *     <intent-filter>
 *         <action android:name="android.service.quicksettings.action.QS_TILE" />
 *     </intent-filter>
 * </service>}
 * </pre>
 *
 * @see Tile Tile for details about the UI of a Quick Settings Tile.
 */
public class TileService extends android.app.Service {
    /**
     * An activity that provides a user interface for adjusting TileService preferences.
     * Optional but recommended for apps that implement a TileService.
     */
    @android.annotation.SdkConstant(android.annotation.SdkConstant.SdkConstantType.INTENT_CATEGORY)
    public static final java.lang.String ACTION_QS_TILE_PREFERENCES = "android.service.quicksettings.action.QS_TILE_PREFERENCES";

    /**
     * Action that identifies a Service as being a TileService.
     */
    public static final java.lang.String ACTION_QS_TILE = "android.service.quicksettings.action.QS_TILE";

    /**
     * Meta-data for tile definition to set a tile into active mode.
     * <p>
     * Active mode is for tiles which already listen and keep track of their state in their
     * own process.  These tiles may request to send an update to the System while their process
     * is alive using {@link #requestListeningState}.  The System will only bind these tiles
     * on its own when a click needs to occur.
     *
     * To make a TileService an active tile, set this meta-data to true on the TileService's
     * manifest declaration.
     * <pre class="prettyprint">
     * {@literal <meta-data android:name="android.service.quicksettings.ACTIVE_TILE"
     *      android:value="true" />}
     * </pre>
     */
    public static final java.lang.String META_DATA_ACTIVE_TILE = "android.service.quicksettings.ACTIVE_TILE";

    /**
     * Used to notify SysUI that Listening has be requested.
     *
     * @unknown 
     */
    public static final java.lang.String ACTION_REQUEST_LISTENING = "android.service.quicksettings.action.REQUEST_LISTENING";

    /**
     *
     *
     * @unknown 
     */
    public static final java.lang.String EXTRA_SERVICE = "service";

    /**
     *
     *
     * @unknown 
     */
    public static final java.lang.String EXTRA_TOKEN = "token";

    /**
     *
     *
     * @unknown 
     */
    public static final java.lang.String EXTRA_COMPONENT = "android.service.quicksettings.extra.COMPONENT";

    private final android.service.quicksettings.TileService.H mHandler = new android.service.quicksettings.TileService.H(android.os.Looper.getMainLooper());

    private boolean mListening = false;

    private android.service.quicksettings.Tile mTile;

    private android.os.IBinder mToken;

    private android.service.quicksettings.IQSService mService;

    private java.lang.Runnable mUnlockRunnable;

    private android.os.IBinder mTileToken;

    @java.lang.Override
    public void onDestroy() {
        if (mListening) {
            onStopListening();
            mListening = false;
        }
        super.onDestroy();
    }

    /**
     * Called when the user adds this tile to Quick Settings.
     * <p/>
     * Note that this is not guaranteed to be called between {@link #onCreate()}
     * and {@link #onStartListening()}, it will only be called when the tile is added
     * and not on subsequent binds.
     */
    public void onTileAdded() {
    }

    /**
     * Called when the user removes this tile from Quick Settings.
     */
    public void onTileRemoved() {
    }

    /**
     * Called when this tile moves into a listening state.
     * <p/>
     * When this tile is in a listening state it is expected to keep the
     * UI up to date.  Any listeners or callbacks needed to keep this tile
     * up to date should be registered here and unregistered in {@link #onStopListening()}.
     *
     * @see #getQsTile()
     * @see Tile#updateTile()
     */
    public void onStartListening() {
    }

    /**
     * Called when this tile moves out of the listening state.
     */
    public void onStopListening() {
    }

    /**
     * Called when the user clicks on this tile.
     */
    public void onClick() {
    }

    /**
     * Sets an icon to be shown in the status bar.
     * <p>
     * The icon will be displayed before all other icons.  Can only be called between
     * {@link #onStartListening} and {@link #onStopListening}.  Can only be called by system apps.
     *
     * @param icon
     * 		The icon to be displayed, null to hide
     * @param contentDescription
     * 		Content description of the icon to be displayed
     * @unknown 
     */
    @android.annotation.SystemApi
    public final void setStatusIcon(android.graphics.drawable.Icon icon, java.lang.String contentDescription) {
        if (mService != null) {
            try {
                mService.updateStatusIcon(mTileToken, icon, contentDescription);
            } catch (android.os.RemoteException e) {
            }
        }
    }

    /**
     * Used to show a dialog.
     *
     * This will collapse the Quick Settings panel and show the dialog.
     *
     * @param dialog
     * 		Dialog to show.
     * @see #isLocked()
     */
    public final void showDialog(android.app.Dialog dialog) {
        dialog.getWindow().getAttributes().token = mToken;
        dialog.getWindow().setType(android.view.WindowManager.LayoutParams.TYPE_QS_DIALOG);
        dialog.getWindow().getDecorView().addOnAttachStateChangeListener(new android.view.View.OnAttachStateChangeListener() {
            @java.lang.Override
            public void onViewAttachedToWindow(android.view.View v) {
            }

            @java.lang.Override
            public void onViewDetachedFromWindow(android.view.View v) {
                try {
                    mService.onDialogHidden(mTileToken);
                } catch (android.os.RemoteException e) {
                }
            }
        });
        dialog.show();
        try {
            mService.onShowDialog(mTileToken);
        } catch (android.os.RemoteException e) {
        }
    }

    /**
     * Prompts the user to unlock the device before executing the Runnable.
     * <p>
     * The user will be prompted for their current security method if applicable
     * and if successful, runnable will be executed.  The Runnable will not be
     * executed if the user fails to unlock the device or cancels the operation.
     */
    public final void unlockAndRun(java.lang.Runnable runnable) {
        mUnlockRunnable = runnable;
        try {
            mService.startUnlockAndRun(mTileToken);
        } catch (android.os.RemoteException e) {
        }
    }

    /**
     * Checks if the device is in a secure state.
     *
     * TileServices should detect when the device is secure and change their behavior
     * accordingly.
     *
     * @return true if the device is secure.
     */
    public final boolean isSecure() {
        try {
            return mService.isSecure();
        } catch (android.os.RemoteException e) {
            return true;
        }
    }

    /**
     * Checks if the lock screen is showing.
     *
     * When a device is locked, then {@link #showDialog} will not present a dialog, as it will
     * be under the lock screen. If the behavior of the Tile is safe to do while locked,
     * then the user should use {@link #startActivity} to launch an activity on top of the lock
     * screen, otherwise the tile should use {@link #unlockAndRun(Runnable)} to give the
     * user their security challenge.
     *
     * @return true if the device is locked.
     */
    public final boolean isLocked() {
        try {
            return mService.isLocked();
        } catch (android.os.RemoteException e) {
            return true;
        }
    }

    /**
     * Start an activity while collapsing the panel.
     */
    public final void startActivityAndCollapse(android.content.Intent intent) {
        startActivity(intent);
        try {
            mService.onStartActivity(mTileToken);
        } catch (android.os.RemoteException e) {
        }
    }

    /**
     * Gets the {@link Tile} for this service.
     * <p/>
     * This tile may be used to get or set the current state for this
     * tile. This tile is only valid for updates between {@link #onStartListening()}
     * and {@link #onStopListening()}.
     */
    public final android.service.quicksettings.Tile getQsTile() {
        return mTile;
    }

    @java.lang.Override
    public android.os.IBinder onBind(android.content.Intent intent) {
        mService = IQSService.Stub.asInterface(intent.getIBinderExtra(android.service.quicksettings.TileService.EXTRA_SERVICE));
        mTileToken = intent.getIBinderExtra(android.service.quicksettings.TileService.EXTRA_TOKEN);
        try {
            mTile = mService.getTile(mTileToken);
        } catch (android.os.RemoteException e) {
            throw new java.lang.RuntimeException("Unable to reach IQSService", e);
        }
        if (mTile != null) {
            mTile.setService(mService, mTileToken);
            mHandler.sendEmptyMessage(android.service.quicksettings.TileService.H.MSG_START_SUCCESS);
        }
        return new android.service.quicksettings.IQSTileService.Stub() {
            @java.lang.Override
            public void onTileRemoved() throws android.os.RemoteException {
                mHandler.sendEmptyMessage(android.service.quicksettings.TileService.H.MSG_TILE_REMOVED);
            }

            @java.lang.Override
            public void onTileAdded() throws android.os.RemoteException {
                mHandler.sendEmptyMessage(android.service.quicksettings.TileService.H.MSG_TILE_ADDED);
            }

            @java.lang.Override
            public void onStopListening() throws android.os.RemoteException {
                mHandler.sendEmptyMessage(android.service.quicksettings.TileService.H.MSG_STOP_LISTENING);
            }

            @java.lang.Override
            public void onStartListening() throws android.os.RemoteException {
                mHandler.sendEmptyMessage(android.service.quicksettings.TileService.H.MSG_START_LISTENING);
            }

            @java.lang.Override
            public void onClick(android.os.IBinder wtoken) throws android.os.RemoteException {
                mHandler.obtainMessage(android.service.quicksettings.TileService.H.MSG_TILE_CLICKED, wtoken).sendToTarget();
            }

            @java.lang.Override
            public void onUnlockComplete() throws android.os.RemoteException {
                mHandler.sendEmptyMessage(android.service.quicksettings.TileService.H.MSG_UNLOCK_COMPLETE);
            }
        };
    }

    private class H extends android.os.Handler {
        private static final int MSG_START_LISTENING = 1;

        private static final int MSG_STOP_LISTENING = 2;

        private static final int MSG_TILE_ADDED = 3;

        private static final int MSG_TILE_REMOVED = 4;

        private static final int MSG_TILE_CLICKED = 5;

        private static final int MSG_UNLOCK_COMPLETE = 6;

        private static final int MSG_START_SUCCESS = 7;

        public H(android.os.Looper looper) {
            super(looper);
        }

        @java.lang.Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case android.service.quicksettings.TileService.H.MSG_TILE_ADDED :
                    android.service.quicksettings.TileService.this.onTileAdded();
                    break;
                case android.service.quicksettings.TileService.H.MSG_TILE_REMOVED :
                    if (mListening) {
                        mListening = false;
                        android.service.quicksettings.TileService.this.onStopListening();
                    }
                    android.service.quicksettings.TileService.this.onTileRemoved();
                    break;
                case android.service.quicksettings.TileService.H.MSG_STOP_LISTENING :
                    if (mListening) {
                        mListening = false;
                        android.service.quicksettings.TileService.this.onStopListening();
                    }
                    break;
                case android.service.quicksettings.TileService.H.MSG_START_LISTENING :
                    if (!mListening) {
                        mListening = true;
                        android.service.quicksettings.TileService.this.onStartListening();
                    }
                    break;
                case android.service.quicksettings.TileService.H.MSG_TILE_CLICKED :
                    mToken = ((android.os.IBinder) (msg.obj));
                    android.service.quicksettings.TileService.this.onClick();
                    break;
                case android.service.quicksettings.TileService.H.MSG_UNLOCK_COMPLETE :
                    if (mUnlockRunnable != null) {
                        mUnlockRunnable.run();
                    }
                    break;
                case android.service.quicksettings.TileService.H.MSG_START_SUCCESS :
                    try {
                        mService.onStartSuccessful(mTileToken);
                    } catch (android.os.RemoteException e) {
                    }
                    break;
            }
        }
    }

    /**
     * Requests that a tile be put in the listening state so it can send an update.
     *
     * This method is only applicable to tiles that have {@link #META_DATA_ACTIVE_TILE} defined
     * as true on their TileService Manifest declaration, and will do nothing otherwise.
     */
    public static final void requestListeningState(android.content.Context context, android.content.ComponentName component) {
        android.content.Intent intent = new android.content.Intent(android.service.quicksettings.TileService.ACTION_REQUEST_LISTENING);
        intent.putExtra(android.service.quicksettings.TileService.EXTRA_COMPONENT, component);
        context.sendBroadcast(intent, Manifest.permission.BIND_QUICK_SETTINGS_TILE);
    }
}

