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
package android.app;


/**
 * Manages all of the system services that can be returned by {@link Context#getSystemService}.
 * Used by {@link ContextImpl}.
 */
final class SystemServiceRegistry {
    private static final java.lang.String TAG = "SystemServiceRegistry";

    // Service registry information.
    // This information is never changed once static initialization has completed.
    private static final java.util.HashMap<java.lang.Class<?>, java.lang.String> SYSTEM_SERVICE_NAMES = new java.util.HashMap<java.lang.Class<?>, java.lang.String>();

    private static final java.util.HashMap<java.lang.String, android.app.SystemServiceRegistry.ServiceFetcher<?>> SYSTEM_SERVICE_FETCHERS = new java.util.HashMap<java.lang.String, android.app.SystemServiceRegistry.ServiceFetcher<?>>();

    private static int sServiceCacheSize;

    // Not instantiable.
    private SystemServiceRegistry() {
    }

    static {
        android.app.SystemServiceRegistry.registerService(android.content.Context.ACCESSIBILITY_SERVICE, android.view.accessibility.AccessibilityManager.class, new android.app.SystemServiceRegistry.CachedServiceFetcher<android.view.accessibility.AccessibilityManager>() {
            @java.lang.Override
            public android.view.accessibility.AccessibilityManager createService(android.app.ContextImpl ctx) {
                return android.view.accessibility.AccessibilityManager.getInstance(ctx);
            }
        });
        android.app.SystemServiceRegistry.registerService(android.content.Context.CAPTIONING_SERVICE, android.view.accessibility.CaptioningManager.class, new android.app.SystemServiceRegistry.CachedServiceFetcher<android.view.accessibility.CaptioningManager>() {
            @java.lang.Override
            public android.view.accessibility.CaptioningManager createService(android.app.ContextImpl ctx) {
                return new android.view.accessibility.CaptioningManager(ctx);
            }
        });
        android.app.SystemServiceRegistry.registerService(android.content.Context.ACCOUNT_SERVICE, android.accounts.AccountManager.class, new android.app.SystemServiceRegistry.CachedServiceFetcher<android.accounts.AccountManager>() {
            @java.lang.Override
            public android.accounts.AccountManager createService(android.app.ContextImpl ctx) {
                android.os.IBinder b = android.os.ServiceManager.getService(android.content.Context.ACCOUNT_SERVICE);
                android.accounts.IAccountManager service = IAccountManager.Stub.asInterface(b);
                return new android.accounts.AccountManager(ctx, service);
            }
        });
        android.app.SystemServiceRegistry.registerService(android.content.Context.ACTIVITY_SERVICE, android.app.ActivityManager.class, new android.app.SystemServiceRegistry.CachedServiceFetcher<android.app.ActivityManager>() {
            @java.lang.Override
            public android.app.ActivityManager createService(android.app.ContextImpl ctx) {
                return new android.app.ActivityManager(ctx.getOuterContext(), ctx.mMainThread.getHandler());
            }
        });
        android.app.SystemServiceRegistry.registerService(android.content.Context.ALARM_SERVICE, android.app.AlarmManager.class, new android.app.SystemServiceRegistry.CachedServiceFetcher<android.app.AlarmManager>() {
            @java.lang.Override
            public android.app.AlarmManager createService(android.app.ContextImpl ctx) {
                android.os.IBinder b = android.os.ServiceManager.getService(android.content.Context.ALARM_SERVICE);
                android.app.IAlarmManager service = IAlarmManager.Stub.asInterface(b);
                return new android.app.AlarmManager(service, ctx);
            }
        });
        android.app.SystemServiceRegistry.registerService(android.content.Context.AUDIO_SERVICE, android.media.AudioManager.class, new android.app.SystemServiceRegistry.CachedServiceFetcher<android.media.AudioManager>() {
            @java.lang.Override
            public android.media.AudioManager createService(android.app.ContextImpl ctx) {
                return new android.media.AudioManager(ctx);
            }
        });
        android.app.SystemServiceRegistry.registerService(android.content.Context.MEDIA_ROUTER_SERVICE, android.media.MediaRouter.class, new android.app.SystemServiceRegistry.CachedServiceFetcher<android.media.MediaRouter>() {
            @java.lang.Override
            public android.media.MediaRouter createService(android.app.ContextImpl ctx) {
                return new android.media.MediaRouter(ctx);
            }
        });
        android.app.SystemServiceRegistry.registerService(android.content.Context.BLUETOOTH_SERVICE, android.bluetooth.BluetoothManager.class, new android.app.SystemServiceRegistry.CachedServiceFetcher<android.bluetooth.BluetoothManager>() {
            @java.lang.Override
            public android.bluetooth.BluetoothManager createService(android.app.ContextImpl ctx) {
                return new android.bluetooth.BluetoothManager(ctx);
            }
        });
        android.app.SystemServiceRegistry.registerService(android.content.Context.HDMI_CONTROL_SERVICE, android.hardware.hdmi.HdmiControlManager.class, new android.app.SystemServiceRegistry.StaticServiceFetcher<android.hardware.hdmi.HdmiControlManager>() {
            @java.lang.Override
            public android.hardware.hdmi.HdmiControlManager createService() {
                android.os.IBinder b = android.os.ServiceManager.getService(android.content.Context.HDMI_CONTROL_SERVICE);
                return new android.hardware.hdmi.HdmiControlManager(IHdmiControlService.Stub.asInterface(b));
            }
        });
        android.app.SystemServiceRegistry.registerService(android.content.Context.CLIPBOARD_SERVICE, android.content.ClipboardManager.class, new android.app.SystemServiceRegistry.CachedServiceFetcher<android.content.ClipboardManager>() {
            @java.lang.Override
            public android.content.ClipboardManager createService(android.app.ContextImpl ctx) {
                return new android.content.ClipboardManager(ctx.getOuterContext(), ctx.mMainThread.getHandler());
            }
        });
        // The clipboard service moved to a new package.  If someone asks for the old
        // interface by class then we want to redirect over to the new interface instead
        // (which extends it).
        android.app.SystemServiceRegistry.SYSTEM_SERVICE_NAMES.put(android.text.ClipboardManager.class, android.content.Context.CLIPBOARD_SERVICE);
        android.app.SystemServiceRegistry.registerService(android.content.Context.CONNECTIVITY_SERVICE, android.net.ConnectivityManager.class, new android.app.SystemServiceRegistry.StaticApplicationContextServiceFetcher<android.net.ConnectivityManager>() {
            @java.lang.Override
            public android.net.ConnectivityManager createService(android.content.Context context) {
                android.os.IBinder b = android.os.ServiceManager.getService(android.content.Context.CONNECTIVITY_SERVICE);
                android.net.IConnectivityManager service = IConnectivityManager.Stub.asInterface(b);
                return new android.net.ConnectivityManager(context, service);
            }
        });
        android.app.SystemServiceRegistry.registerService(android.content.Context.COUNTRY_DETECTOR, android.location.CountryDetector.class, new android.app.SystemServiceRegistry.StaticServiceFetcher<android.location.CountryDetector>() {
            @java.lang.Override
            public android.location.CountryDetector createService() {
                android.os.IBinder b = android.os.ServiceManager.getService(android.content.Context.COUNTRY_DETECTOR);
                return new android.location.CountryDetector(ICountryDetector.Stub.asInterface(b));
            }
        });
        android.app.SystemServiceRegistry.registerService(android.content.Context.DEVICE_POLICY_SERVICE, android.app.admin.DevicePolicyManager.class, new android.app.SystemServiceRegistry.CachedServiceFetcher<android.app.admin.DevicePolicyManager>() {
            @java.lang.Override
            public android.app.admin.DevicePolicyManager createService(android.app.ContextImpl ctx) {
                return android.app.admin.DevicePolicyManager.create(ctx);
            }
        });
        android.app.SystemServiceRegistry.registerService(android.content.Context.DOWNLOAD_SERVICE, android.app.DownloadManager.class, new android.app.SystemServiceRegistry.CachedServiceFetcher<android.app.DownloadManager>() {
            @java.lang.Override
            public android.app.DownloadManager createService(android.app.ContextImpl ctx) {
                return new android.app.DownloadManager(ctx);
            }
        });
        android.app.SystemServiceRegistry.registerService(android.content.Context.BATTERY_SERVICE, android.os.BatteryManager.class, new android.app.SystemServiceRegistry.StaticServiceFetcher<android.os.BatteryManager>() {
            @java.lang.Override
            public android.os.BatteryManager createService() {
                return new android.os.BatteryManager();
            }
        });
        android.app.SystemServiceRegistry.registerService(android.content.Context.NFC_SERVICE, android.nfc.NfcManager.class, new android.app.SystemServiceRegistry.CachedServiceFetcher<android.nfc.NfcManager>() {
            @java.lang.Override
            public android.nfc.NfcManager createService(android.app.ContextImpl ctx) {
                return new android.nfc.NfcManager(ctx);
            }
        });
        android.app.SystemServiceRegistry.registerService(android.content.Context.DROPBOX_SERVICE, android.os.DropBoxManager.class, new android.app.SystemServiceRegistry.CachedServiceFetcher<android.os.DropBoxManager>() {
            @java.lang.Override
            public android.os.DropBoxManager createService(android.app.ContextImpl ctx) {
                android.os.IBinder b = android.os.ServiceManager.getService(android.content.Context.DROPBOX_SERVICE);
                com.android.internal.os.IDropBoxManagerService service = IDropBoxManagerService.Stub.asInterface(b);
                if (service == null) {
                    // Don't return a DropBoxManager that will NPE upon use.
                    // This also avoids caching a broken DropBoxManager in
                    // getDropBoxManager during early boot, before the
                    // DROPBOX_SERVICE is registered.
                    return null;
                }
                return new android.os.DropBoxManager(ctx, service);
            }
        });
        android.app.SystemServiceRegistry.registerService(android.content.Context.INPUT_SERVICE, android.hardware.input.InputManager.class, new android.app.SystemServiceRegistry.StaticServiceFetcher<android.hardware.input.InputManager>() {
            @java.lang.Override
            public android.hardware.input.InputManager createService() {
                return android.hardware.input.InputManager.getInstance();
            }
        });
        android.app.SystemServiceRegistry.registerService(android.content.Context.DISPLAY_SERVICE, android.hardware.display.DisplayManager.class, new android.app.SystemServiceRegistry.CachedServiceFetcher<android.hardware.display.DisplayManager>() {
            @java.lang.Override
            public android.hardware.display.DisplayManager createService(android.app.ContextImpl ctx) {
                return new android.hardware.display.DisplayManager(ctx.getOuterContext());
            }
        });
        android.app.SystemServiceRegistry.registerService(android.content.Context.INPUT_METHOD_SERVICE, android.view.inputmethod.InputMethodManager.class, new android.app.SystemServiceRegistry.StaticServiceFetcher<android.view.inputmethod.InputMethodManager>() {
            @java.lang.Override
            public android.view.inputmethod.InputMethodManager createService() {
                return android.view.inputmethod.InputMethodManager.getInstance();
            }
        });
        android.app.SystemServiceRegistry.registerService(android.content.Context.TEXT_SERVICES_MANAGER_SERVICE, android.view.textservice.TextServicesManager.class, new android.app.SystemServiceRegistry.StaticServiceFetcher<android.view.textservice.TextServicesManager>() {
            @java.lang.Override
            public android.view.textservice.TextServicesManager createService() {
                return android.view.textservice.TextServicesManager.getInstance();
            }
        });
        android.app.SystemServiceRegistry.registerService(android.content.Context.KEYGUARD_SERVICE, android.app.KeyguardManager.class, new android.app.SystemServiceRegistry.StaticServiceFetcher<android.app.KeyguardManager>() {
            @java.lang.Override
            public android.app.KeyguardManager createService() {
                return new android.app.KeyguardManager();
            }
        });
        android.app.SystemServiceRegistry.registerService(android.content.Context.LAYOUT_INFLATER_SERVICE, android.view.LayoutInflater.class, new android.app.SystemServiceRegistry.CachedServiceFetcher<android.view.LayoutInflater>() {
            @java.lang.Override
            public android.view.LayoutInflater createService(android.app.ContextImpl ctx) {
                return new com.android.internal.policy.PhoneLayoutInflater(ctx.getOuterContext());
            }
        });
        android.app.SystemServiceRegistry.registerService(android.content.Context.LOCATION_SERVICE, android.location.LocationManager.class, new android.app.SystemServiceRegistry.CachedServiceFetcher<android.location.LocationManager>() {
            @java.lang.Override
            public android.location.LocationManager createService(android.app.ContextImpl ctx) {
                android.os.IBinder b = android.os.ServiceManager.getService(android.content.Context.LOCATION_SERVICE);
                return new android.location.LocationManager(ctx, ILocationManager.Stub.asInterface(b));
            }
        });
        android.app.SystemServiceRegistry.registerService(android.content.Context.NETWORK_POLICY_SERVICE, android.net.NetworkPolicyManager.class, new android.app.SystemServiceRegistry.CachedServiceFetcher<android.net.NetworkPolicyManager>() {
            @java.lang.Override
            public android.net.NetworkPolicyManager createService(android.app.ContextImpl ctx) {
                return new android.net.NetworkPolicyManager(ctx, INetworkPolicyManager.Stub.asInterface(android.os.ServiceManager.getService(android.content.Context.NETWORK_POLICY_SERVICE)));
            }
        });
        android.app.SystemServiceRegistry.registerService(android.content.Context.NOTIFICATION_SERVICE, android.app.NotificationManager.class, new android.app.SystemServiceRegistry.CachedServiceFetcher<android.app.NotificationManager>() {
            @java.lang.Override
            public android.app.NotificationManager createService(android.app.ContextImpl ctx) {
                final android.content.Context outerContext = ctx.getOuterContext();
                return new android.app.NotificationManager(new android.view.ContextThemeWrapper(outerContext, android.content.res.Resources.selectSystemTheme(0, outerContext.getApplicationInfo().targetSdkVersion, com.android.internal.R.style.Theme_Dialog, com.android.internal.R.style.Theme_Holo_Dialog, com.android.internal.R.style.Theme_DeviceDefault_Dialog, com.android.internal.R.style.Theme_DeviceDefault_Light_Dialog)), ctx.mMainThread.getHandler());
            }
        });
        android.app.SystemServiceRegistry.registerService(android.content.Context.NSD_SERVICE, android.net.nsd.NsdManager.class, new android.app.SystemServiceRegistry.CachedServiceFetcher<android.net.nsd.NsdManager>() {
            @java.lang.Override
            public android.net.nsd.NsdManager createService(android.app.ContextImpl ctx) {
                android.os.IBinder b = android.os.ServiceManager.getService(android.content.Context.NSD_SERVICE);
                android.net.nsd.INsdManager service = INsdManager.Stub.asInterface(b);
                return new android.net.nsd.NsdManager(ctx.getOuterContext(), service);
            }
        });
        android.app.SystemServiceRegistry.registerService(android.content.Context.POWER_SERVICE, android.os.PowerManager.class, new android.app.SystemServiceRegistry.CachedServiceFetcher<android.os.PowerManager>() {
            @java.lang.Override
            public android.os.PowerManager createService(android.app.ContextImpl ctx) {
                android.os.IBinder b = android.os.ServiceManager.getService(android.content.Context.POWER_SERVICE);
                android.os.IPowerManager service = IPowerManager.Stub.asInterface(b);
                if (service == null) {
                    android.util.Log.wtf(android.app.SystemServiceRegistry.TAG, "Failed to get power manager service.");
                }
                return new android.os.PowerManager(ctx.getOuterContext(), service, ctx.mMainThread.getHandler());
            }
        });
        android.app.SystemServiceRegistry.registerService(android.content.Context.RECOVERY_SERVICE, android.os.RecoverySystem.class, new android.app.SystemServiceRegistry.CachedServiceFetcher<android.os.RecoverySystem>() {
            @java.lang.Override
            public android.os.RecoverySystem createService(android.app.ContextImpl ctx) {
                android.os.IBinder b = android.os.ServiceManager.getService(android.content.Context.RECOVERY_SERVICE);
                android.os.IRecoverySystem service = IRecoverySystem.Stub.asInterface(b);
                if (service == null) {
                    android.util.Log.wtf(android.app.SystemServiceRegistry.TAG, "Failed to get recovery service.");
                }
                return new android.os.RecoverySystem(service);
            }
        });
        android.app.SystemServiceRegistry.registerService(android.content.Context.SEARCH_SERVICE, android.app.SearchManager.class, new android.app.SystemServiceRegistry.CachedServiceFetcher<android.app.SearchManager>() {
            @java.lang.Override
            public android.app.SearchManager createService(android.app.ContextImpl ctx) {
                return new android.app.SearchManager(ctx.getOuterContext(), ctx.mMainThread.getHandler());
            }
        });
        android.app.SystemServiceRegistry.registerService(android.content.Context.SENSOR_SERVICE, android.hardware.SensorManager.class, new android.app.SystemServiceRegistry.CachedServiceFetcher<android.hardware.SensorManager>() {
            @java.lang.Override
            public android.hardware.SensorManager createService(android.app.ContextImpl ctx) {
                return new android.hardware.SystemSensorManager(ctx.getOuterContext(), ctx.mMainThread.getHandler().getLooper());
            }
        });
        android.app.SystemServiceRegistry.registerService(android.content.Context.STATUS_BAR_SERVICE, android.app.StatusBarManager.class, new android.app.SystemServiceRegistry.CachedServiceFetcher<android.app.StatusBarManager>() {
            @java.lang.Override
            public android.app.StatusBarManager createService(android.app.ContextImpl ctx) {
                return new android.app.StatusBarManager(ctx.getOuterContext());
            }
        });
        android.app.SystemServiceRegistry.registerService(android.content.Context.STORAGE_SERVICE, android.os.storage.StorageManager.class, new android.app.SystemServiceRegistry.CachedServiceFetcher<android.os.storage.StorageManager>() {
            @java.lang.Override
            public android.os.storage.StorageManager createService(android.app.ContextImpl ctx) {
                return new android.os.storage.StorageManager(ctx, ctx.mMainThread.getHandler().getLooper());
            }
        });
        android.app.SystemServiceRegistry.registerService(android.content.Context.TELEPHONY_SERVICE, android.telephony.TelephonyManager.class, new android.app.SystemServiceRegistry.CachedServiceFetcher<android.telephony.TelephonyManager>() {
            @java.lang.Override
            public android.telephony.TelephonyManager createService(android.app.ContextImpl ctx) {
                return new android.telephony.TelephonyManager(ctx.getOuterContext());
            }
        });
        android.app.SystemServiceRegistry.registerService(android.content.Context.TELEPHONY_SUBSCRIPTION_SERVICE, android.telephony.SubscriptionManager.class, new android.app.SystemServiceRegistry.CachedServiceFetcher<android.telephony.SubscriptionManager>() {
            @java.lang.Override
            public android.telephony.SubscriptionManager createService(android.app.ContextImpl ctx) {
                return new android.telephony.SubscriptionManager(ctx.getOuterContext());
            }
        });
        android.app.SystemServiceRegistry.registerService(android.content.Context.CARRIER_CONFIG_SERVICE, android.telephony.CarrierConfigManager.class, new android.app.SystemServiceRegistry.CachedServiceFetcher<android.telephony.CarrierConfigManager>() {
            @java.lang.Override
            public android.telephony.CarrierConfigManager createService(android.app.ContextImpl ctx) {
                return new android.telephony.CarrierConfigManager();
            }
        });
        android.app.SystemServiceRegistry.registerService(android.content.Context.TELECOM_SERVICE, android.telecom.TelecomManager.class, new android.app.SystemServiceRegistry.CachedServiceFetcher<android.telecom.TelecomManager>() {
            @java.lang.Override
            public android.telecom.TelecomManager createService(android.app.ContextImpl ctx) {
                return new android.telecom.TelecomManager(ctx.getOuterContext());
            }
        });
        android.app.SystemServiceRegistry.registerService(android.content.Context.UI_MODE_SERVICE, android.app.UiModeManager.class, new android.app.SystemServiceRegistry.CachedServiceFetcher<android.app.UiModeManager>() {
            @java.lang.Override
            public android.app.UiModeManager createService(android.app.ContextImpl ctx) {
                return new android.app.UiModeManager();
            }
        });
        android.app.SystemServiceRegistry.registerService(android.content.Context.USB_SERVICE, android.hardware.usb.UsbManager.class, new android.app.SystemServiceRegistry.CachedServiceFetcher<android.hardware.usb.UsbManager>() {
            @java.lang.Override
            public android.hardware.usb.UsbManager createService(android.app.ContextImpl ctx) {
                android.os.IBinder b = android.os.ServiceManager.getService(android.content.Context.USB_SERVICE);
                return new android.hardware.usb.UsbManager(ctx, IUsbManager.Stub.asInterface(b));
            }
        });
        android.app.SystemServiceRegistry.registerService(android.content.Context.SERIAL_SERVICE, android.hardware.SerialManager.class, new android.app.SystemServiceRegistry.CachedServiceFetcher<android.hardware.SerialManager>() {
            @java.lang.Override
            public android.hardware.SerialManager createService(android.app.ContextImpl ctx) {
                android.os.IBinder b = android.os.ServiceManager.getService(android.content.Context.SERIAL_SERVICE);
                return new android.hardware.SerialManager(ctx, ISerialManager.Stub.asInterface(b));
            }
        });
        android.app.SystemServiceRegistry.registerService(android.content.Context.VIBRATOR_SERVICE, android.os.Vibrator.class, new android.app.SystemServiceRegistry.CachedServiceFetcher<android.os.Vibrator>() {
            @java.lang.Override
            public android.os.Vibrator createService(android.app.ContextImpl ctx) {
                return new android.os.SystemVibrator(ctx);
            }
        });
        android.app.SystemServiceRegistry.registerService(android.content.Context.WALLPAPER_SERVICE, android.app.WallpaperManager.class, new android.app.SystemServiceRegistry.CachedServiceFetcher<android.app.WallpaperManager>() {
            @java.lang.Override
            public android.app.WallpaperManager createService(android.app.ContextImpl ctx) {
                return new android.app.WallpaperManager(ctx.getOuterContext(), ctx.mMainThread.getHandler());
            }
        });
        android.app.SystemServiceRegistry.registerService(android.content.Context.WIFI_SERVICE, android.net.wifi.WifiManager.class, new android.app.SystemServiceRegistry.CachedServiceFetcher<android.net.wifi.WifiManager>() {
            @java.lang.Override
            public android.net.wifi.WifiManager createService(android.app.ContextImpl ctx) {
                android.os.IBinder b = android.os.ServiceManager.getService(android.content.Context.WIFI_SERVICE);
                android.net.wifi.IWifiManager service = IWifiManager.Stub.asInterface(b);
                return new android.net.wifi.WifiManager(ctx.getOuterContext(), service, android.net.ConnectivityThread.getInstanceLooper());
            }
        });
        android.app.SystemServiceRegistry.registerService(android.content.Context.WIFI_P2P_SERVICE, android.net.wifi.p2p.WifiP2pManager.class, new android.app.SystemServiceRegistry.StaticServiceFetcher<android.net.wifi.p2p.WifiP2pManager>() {
            @java.lang.Override
            public android.net.wifi.p2p.WifiP2pManager createService() {
                android.os.IBinder b = android.os.ServiceManager.getService(android.content.Context.WIFI_P2P_SERVICE);
                android.net.wifi.p2p.IWifiP2pManager service = IWifiP2pManager.Stub.asInterface(b);
                return new android.net.wifi.p2p.WifiP2pManager(service);
            }
        });
        android.app.SystemServiceRegistry.registerService(android.content.Context.WIFI_NAN_SERVICE, android.net.wifi.nan.WifiNanManager.class, new android.app.SystemServiceRegistry.StaticServiceFetcher<android.net.wifi.nan.WifiNanManager>() {
            @java.lang.Override
            public android.net.wifi.nan.WifiNanManager createService() {
                android.os.IBinder b = android.os.ServiceManager.getService(android.content.Context.WIFI_NAN_SERVICE);
                android.net.wifi.nan.IWifiNanManager service = IWifiNanManager.Stub.asInterface(b);
                if (service == null) {
                    return null;
                }
                return new android.net.wifi.nan.WifiNanManager(service);
            }
        });
        android.app.SystemServiceRegistry.registerService(android.content.Context.WIFI_SCANNING_SERVICE, android.net.wifi.WifiScanner.class, new android.app.SystemServiceRegistry.CachedServiceFetcher<android.net.wifi.WifiScanner>() {
            @java.lang.Override
            public android.net.wifi.WifiScanner createService(android.app.ContextImpl ctx) {
                android.os.IBinder b = android.os.ServiceManager.getService(android.content.Context.WIFI_SCANNING_SERVICE);
                android.net.wifi.IWifiScanner service = IWifiScanner.Stub.asInterface(b);
                return new android.net.wifi.WifiScanner(ctx.getOuterContext(), service, android.net.ConnectivityThread.getInstanceLooper());
            }
        });
        android.app.SystemServiceRegistry.registerService(android.content.Context.WIFI_RTT_SERVICE, android.net.wifi.RttManager.class, new android.app.SystemServiceRegistry.CachedServiceFetcher<android.net.wifi.RttManager>() {
            @java.lang.Override
            public android.net.wifi.RttManager createService(android.app.ContextImpl ctx) {
                android.os.IBinder b = android.os.ServiceManager.getService(android.content.Context.WIFI_RTT_SERVICE);
                android.net.wifi.IRttManager service = IRttManager.Stub.asInterface(b);
                return new android.net.wifi.RttManager(ctx.getOuterContext(), service, android.net.ConnectivityThread.getInstanceLooper());
            }
        });
        android.app.SystemServiceRegistry.registerService(android.content.Context.ETHERNET_SERVICE, android.net.EthernetManager.class, new android.app.SystemServiceRegistry.CachedServiceFetcher<android.net.EthernetManager>() {
            @java.lang.Override
            public android.net.EthernetManager createService(android.app.ContextImpl ctx) {
                android.os.IBinder b = android.os.ServiceManager.getService(android.content.Context.ETHERNET_SERVICE);
                android.net.IEthernetManager service = IEthernetManager.Stub.asInterface(b);
                return new android.net.EthernetManager(ctx.getOuterContext(), service);
            }
        });
        android.app.SystemServiceRegistry.registerService(android.content.Context.WINDOW_SERVICE, android.view.WindowManager.class, new android.app.SystemServiceRegistry.CachedServiceFetcher<android.view.WindowManager>() {
            @java.lang.Override
            public android.view.WindowManager createService(android.app.ContextImpl ctx) {
                return new android.view.WindowManagerImpl(ctx);
            }
        });
        android.app.SystemServiceRegistry.registerService(android.content.Context.USER_SERVICE, android.os.UserManager.class, new android.app.SystemServiceRegistry.CachedServiceFetcher<android.os.UserManager>() {
            @java.lang.Override
            public android.os.UserManager createService(android.app.ContextImpl ctx) {
                android.os.IBinder b = android.os.ServiceManager.getService(android.content.Context.USER_SERVICE);
                android.os.IUserManager service = IUserManager.Stub.asInterface(b);
                return new android.os.UserManager(ctx, service);
            }
        });
        android.app.SystemServiceRegistry.registerService(android.content.Context.APP_OPS_SERVICE, android.app.AppOpsManager.class, new android.app.SystemServiceRegistry.CachedServiceFetcher<android.app.AppOpsManager>() {
            @java.lang.Override
            public android.app.AppOpsManager createService(android.app.ContextImpl ctx) {
                android.os.IBinder b = android.os.ServiceManager.getService(android.content.Context.APP_OPS_SERVICE);
                com.android.internal.app.IAppOpsService service = IAppOpsService.Stub.asInterface(b);
                return new android.app.AppOpsManager(ctx, service);
            }
        });
        android.app.SystemServiceRegistry.registerService(android.content.Context.CAMERA_SERVICE, android.hardware.camera2.CameraManager.class, new android.app.SystemServiceRegistry.CachedServiceFetcher<android.hardware.camera2.CameraManager>() {
            @java.lang.Override
            public android.hardware.camera2.CameraManager createService(android.app.ContextImpl ctx) {
                return new android.hardware.camera2.CameraManager(ctx);
            }
        });
        android.app.SystemServiceRegistry.registerService(android.content.Context.LAUNCHER_APPS_SERVICE, android.content.pm.LauncherApps.class, new android.app.SystemServiceRegistry.CachedServiceFetcher<android.content.pm.LauncherApps>() {
            @java.lang.Override
            public android.content.pm.LauncherApps createService(android.app.ContextImpl ctx) {
                return new android.content.pm.LauncherApps(ctx);
            }
        });
        android.app.SystemServiceRegistry.registerService(android.content.Context.RESTRICTIONS_SERVICE, android.content.RestrictionsManager.class, new android.app.SystemServiceRegistry.CachedServiceFetcher<android.content.RestrictionsManager>() {
            @java.lang.Override
            public android.content.RestrictionsManager createService(android.app.ContextImpl ctx) {
                android.os.IBinder b = android.os.ServiceManager.getService(android.content.Context.RESTRICTIONS_SERVICE);
                android.content.IRestrictionsManager service = IRestrictionsManager.Stub.asInterface(b);
                return new android.content.RestrictionsManager(ctx, service);
            }
        });
        android.app.SystemServiceRegistry.registerService(android.content.Context.PRINT_SERVICE, android.print.PrintManager.class, new android.app.SystemServiceRegistry.CachedServiceFetcher<android.print.PrintManager>() {
            @java.lang.Override
            public android.print.PrintManager createService(android.app.ContextImpl ctx) {
                android.os.IBinder iBinder = android.os.ServiceManager.getService(android.content.Context.PRINT_SERVICE);
                android.print.IPrintManager service = IPrintManager.Stub.asInterface(iBinder);
                return new android.print.PrintManager(ctx.getOuterContext(), service, android.os.UserHandle.myUserId(), android.os.UserHandle.getAppId(android.os.Process.myUid()));
            }
        });
        android.app.SystemServiceRegistry.registerService(android.content.Context.CONSUMER_IR_SERVICE, android.hardware.ConsumerIrManager.class, new android.app.SystemServiceRegistry.CachedServiceFetcher<android.hardware.ConsumerIrManager>() {
            @java.lang.Override
            public android.hardware.ConsumerIrManager createService(android.app.ContextImpl ctx) {
                return new android.hardware.ConsumerIrManager(ctx);
            }
        });
        android.app.SystemServiceRegistry.registerService(android.content.Context.MEDIA_SESSION_SERVICE, android.media.session.MediaSessionManager.class, new android.app.SystemServiceRegistry.CachedServiceFetcher<android.media.session.MediaSessionManager>() {
            @java.lang.Override
            public android.media.session.MediaSessionManager createService(android.app.ContextImpl ctx) {
                return new android.media.session.MediaSessionManager(ctx);
            }
        });
        android.app.SystemServiceRegistry.registerService(android.content.Context.TRUST_SERVICE, android.app.trust.TrustManager.class, new android.app.SystemServiceRegistry.StaticServiceFetcher<android.app.trust.TrustManager>() {
            @java.lang.Override
            public android.app.trust.TrustManager createService() {
                android.os.IBinder b = android.os.ServiceManager.getService(android.content.Context.TRUST_SERVICE);
                return new android.app.trust.TrustManager(b);
            }
        });
        android.app.SystemServiceRegistry.registerService(android.content.Context.FINGERPRINT_SERVICE, android.hardware.fingerprint.FingerprintManager.class, new android.app.SystemServiceRegistry.CachedServiceFetcher<android.hardware.fingerprint.FingerprintManager>() {
            @java.lang.Override
            public android.hardware.fingerprint.FingerprintManager createService(android.app.ContextImpl ctx) {
                android.os.IBinder binder = android.os.ServiceManager.getService(android.content.Context.FINGERPRINT_SERVICE);
                android.hardware.fingerprint.IFingerprintService service = IFingerprintService.Stub.asInterface(binder);
                return new android.hardware.fingerprint.FingerprintManager(ctx.getOuterContext(), service);
            }
        });
        android.app.SystemServiceRegistry.registerService(android.content.Context.TV_INPUT_SERVICE, android.media.tv.TvInputManager.class, new android.app.SystemServiceRegistry.StaticServiceFetcher<android.media.tv.TvInputManager>() {
            @java.lang.Override
            public android.media.tv.TvInputManager createService() {
                android.os.IBinder iBinder = android.os.ServiceManager.getService(android.content.Context.TV_INPUT_SERVICE);
                android.media.tv.ITvInputManager service = ITvInputManager.Stub.asInterface(iBinder);
                return new android.media.tv.TvInputManager(service, android.os.UserHandle.myUserId());
            }
        });
        android.app.SystemServiceRegistry.registerService(android.content.Context.NETWORK_SCORE_SERVICE, android.net.NetworkScoreManager.class, new android.app.SystemServiceRegistry.CachedServiceFetcher<android.net.NetworkScoreManager>() {
            @java.lang.Override
            public android.net.NetworkScoreManager createService(android.app.ContextImpl ctx) {
                return new android.net.NetworkScoreManager(ctx);
            }
        });
        android.app.SystemServiceRegistry.registerService(android.content.Context.USAGE_STATS_SERVICE, android.app.usage.UsageStatsManager.class, new android.app.SystemServiceRegistry.CachedServiceFetcher<android.app.usage.UsageStatsManager>() {
            @java.lang.Override
            public android.app.usage.UsageStatsManager createService(android.app.ContextImpl ctx) {
                android.os.IBinder iBinder = android.os.ServiceManager.getService(android.content.Context.USAGE_STATS_SERVICE);
                android.app.usage.IUsageStatsManager service = IUsageStatsManager.Stub.asInterface(iBinder);
                return new android.app.usage.UsageStatsManager(ctx.getOuterContext(), service);
            }
        });
        android.app.SystemServiceRegistry.registerService(android.content.Context.NETWORK_STATS_SERVICE, android.app.usage.NetworkStatsManager.class, new android.app.SystemServiceRegistry.CachedServiceFetcher<android.app.usage.NetworkStatsManager>() {
            @java.lang.Override
            public android.app.usage.NetworkStatsManager createService(android.app.ContextImpl ctx) {
                return new android.app.usage.NetworkStatsManager(ctx.getOuterContext());
            }
        });
        android.app.SystemServiceRegistry.registerService(android.content.Context.JOB_SCHEDULER_SERVICE, android.app.job.JobScheduler.class, new android.app.SystemServiceRegistry.StaticServiceFetcher<android.app.job.JobScheduler>() {
            @java.lang.Override
            public android.app.job.JobScheduler createService() {
                android.os.IBinder b = android.os.ServiceManager.getService(android.content.Context.JOB_SCHEDULER_SERVICE);
                return new android.app.JobSchedulerImpl(IJobScheduler.Stub.asInterface(b));
            }
        });
        android.app.SystemServiceRegistry.registerService(android.content.Context.PERSISTENT_DATA_BLOCK_SERVICE, android.service.persistentdata.PersistentDataBlockManager.class, new android.app.SystemServiceRegistry.StaticServiceFetcher<android.service.persistentdata.PersistentDataBlockManager>() {
            @java.lang.Override
            public android.service.persistentdata.PersistentDataBlockManager createService() {
                android.os.IBinder b = android.os.ServiceManager.getService(android.content.Context.PERSISTENT_DATA_BLOCK_SERVICE);
                android.service.persistentdata.IPersistentDataBlockService persistentDataBlockService = IPersistentDataBlockService.Stub.asInterface(b);
                if (persistentDataBlockService != null) {
                    return new android.service.persistentdata.PersistentDataBlockManager(persistentDataBlockService);
                } else {
                    // not supported
                    return null;
                }
            }
        });
        android.app.SystemServiceRegistry.registerService(android.content.Context.MEDIA_PROJECTION_SERVICE, android.media.projection.MediaProjectionManager.class, new android.app.SystemServiceRegistry.CachedServiceFetcher<android.media.projection.MediaProjectionManager>() {
            @java.lang.Override
            public android.media.projection.MediaProjectionManager createService(android.app.ContextImpl ctx) {
                return new android.media.projection.MediaProjectionManager(ctx);
            }
        });
        android.app.SystemServiceRegistry.registerService(android.content.Context.APPWIDGET_SERVICE, android.appwidget.AppWidgetManager.class, new android.app.SystemServiceRegistry.CachedServiceFetcher<android.appwidget.AppWidgetManager>() {
            @java.lang.Override
            public android.appwidget.AppWidgetManager createService(android.app.ContextImpl ctx) {
                android.os.IBinder b = android.os.ServiceManager.getService(android.content.Context.APPWIDGET_SERVICE);
                return new android.appwidget.AppWidgetManager(ctx, IAppWidgetService.Stub.asInterface(b));
            }
        });
        android.app.SystemServiceRegistry.registerService(android.content.Context.MIDI_SERVICE, android.media.midi.MidiManager.class, new android.app.SystemServiceRegistry.CachedServiceFetcher<android.media.midi.MidiManager>() {
            @java.lang.Override
            public android.media.midi.MidiManager createService(android.app.ContextImpl ctx) {
                android.os.IBinder b = android.os.ServiceManager.getService(android.content.Context.MIDI_SERVICE);
                if (b == null) {
                    return null;
                }
                return new android.media.midi.MidiManager(IMidiManager.Stub.asInterface(b));
            }
        });
        android.app.SystemServiceRegistry.registerService(android.content.Context.RADIO_SERVICE, android.hardware.radio.RadioManager.class, new android.app.SystemServiceRegistry.CachedServiceFetcher<android.hardware.radio.RadioManager>() {
            @java.lang.Override
            public android.hardware.radio.RadioManager createService(android.app.ContextImpl ctx) {
                return new android.hardware.radio.RadioManager(ctx);
            }
        });
        android.app.SystemServiceRegistry.registerService(android.content.Context.HARDWARE_PROPERTIES_SERVICE, android.os.HardwarePropertiesManager.class, new android.app.SystemServiceRegistry.CachedServiceFetcher<android.os.HardwarePropertiesManager>() {
            @java.lang.Override
            public android.os.HardwarePropertiesManager createService(android.app.ContextImpl ctx) {
                android.os.IBinder b = android.os.ServiceManager.getService(android.content.Context.HARDWARE_PROPERTIES_SERVICE);
                android.os.IHardwarePropertiesManager service = IHardwarePropertiesManager.Stub.asInterface(b);
                if (service == null) {
                    android.util.Log.wtf(android.app.SystemServiceRegistry.TAG, "Failed to get hardwareproperties service.");
                    return null;
                }
                return new android.os.HardwarePropertiesManager(ctx, service);
            }
        });
        android.app.SystemServiceRegistry.registerService(android.content.Context.SOUND_TRIGGER_SERVICE, android.media.soundtrigger.SoundTriggerManager.class, new android.app.SystemServiceRegistry.CachedServiceFetcher<android.media.soundtrigger.SoundTriggerManager>() {
            @java.lang.Override
            public android.media.soundtrigger.SoundTriggerManager createService(android.app.ContextImpl ctx) {
                android.os.IBinder b = android.os.ServiceManager.getService(android.content.Context.SOUND_TRIGGER_SERVICE);
                return new android.media.soundtrigger.SoundTriggerManager(ctx, ISoundTriggerService.Stub.asInterface(b));
            }
        });
        android.app.SystemServiceRegistry.registerService(android.content.Context.SHORTCUT_SERVICE, android.content.pm.ShortcutManager.class, new android.app.SystemServiceRegistry.CachedServiceFetcher<android.content.pm.ShortcutManager>() {
            @java.lang.Override
            public android.content.pm.ShortcutManager createService(android.app.ContextImpl ctx) {
                return new android.content.pm.ShortcutManager(ctx);
            }
        });
        android.app.SystemServiceRegistry.registerService(android.content.Context.SYSTEM_HEALTH_SERVICE, android.os.health.SystemHealthManager.class, new android.app.SystemServiceRegistry.CachedServiceFetcher<android.os.health.SystemHealthManager>() {
            @java.lang.Override
            public android.os.health.SystemHealthManager createService(android.app.ContextImpl ctx) {
                return new android.os.health.SystemHealthManager();
            }
        });
        android.app.SystemServiceRegistry.registerService(android.content.Context.CONTEXTHUB_SERVICE, android.hardware.location.ContextHubManager.class, new android.app.SystemServiceRegistry.CachedServiceFetcher<android.hardware.location.ContextHubManager>() {
            @java.lang.Override
            public android.hardware.location.ContextHubManager createService(android.app.ContextImpl ctx) {
                return new android.hardware.location.ContextHubManager(ctx.getOuterContext(), ctx.mMainThread.getHandler().getLooper());
            }
        });
    }

    /**
     * Creates an array which is used to cache per-Context service instances.
     */
    public static java.lang.Object[] createServiceCache() {
        return new java.lang.Object[android.app.SystemServiceRegistry.sServiceCacheSize];
    }

    /**
     * Gets a system service from a given context.
     */
    public static java.lang.Object getSystemService(android.app.ContextImpl ctx, java.lang.String name) {
        android.app.SystemServiceRegistry.ServiceFetcher<?> fetcher = android.app.SystemServiceRegistry.SYSTEM_SERVICE_FETCHERS.get(name);
        return fetcher != null ? fetcher.getService(ctx) : null;
    }

    /**
     * Gets the name of the system-level service that is represented by the specified class.
     */
    public static java.lang.String getSystemServiceName(java.lang.Class<?> serviceClass) {
        return android.app.SystemServiceRegistry.SYSTEM_SERVICE_NAMES.get(serviceClass);
    }

    /**
     * Statically registers a system service with the context.
     * This method must be called during static initialization only.
     */
    private static <T> void registerService(java.lang.String serviceName, java.lang.Class<T> serviceClass, android.app.SystemServiceRegistry.ServiceFetcher<T> serviceFetcher) {
        android.app.SystemServiceRegistry.SYSTEM_SERVICE_NAMES.put(serviceClass, serviceName);
        android.app.SystemServiceRegistry.SYSTEM_SERVICE_FETCHERS.put(serviceName, serviceFetcher);
    }

    /**
     * Base interface for classes that fetch services.
     * These objects must only be created during static initialization.
     */
    static abstract interface ServiceFetcher<T> {
        T getService(android.app.ContextImpl ctx);
    }

    /**
     * Override this class when the system service constructor needs a
     * ContextImpl and should be cached and retained by that context.
     */
    static abstract class CachedServiceFetcher<T> implements android.app.SystemServiceRegistry.ServiceFetcher<T> {
        private final int mCacheIndex;

        public CachedServiceFetcher() {
            mCacheIndex = android.app.SystemServiceRegistry.sServiceCacheSize++;
        }

        @java.lang.Override
        @java.lang.SuppressWarnings("unchecked")
        public final T getService(android.app.ContextImpl ctx) {
            final java.lang.Object[] cache = ctx.mServiceCache;
            synchronized(cache) {
                // Fetch or create the service.
                java.lang.Object service = cache[mCacheIndex];
                if (service == null) {
                    service = createService(ctx);
                    cache[mCacheIndex] = service;
                }
                return ((T) (service));
            }
        }

        public abstract T createService(android.app.ContextImpl ctx);
    }

    /**
     * Override this class when the system service does not need a ContextImpl
     * and should be cached and retained process-wide.
     */
    static abstract class StaticServiceFetcher<T> implements android.app.SystemServiceRegistry.ServiceFetcher<T> {
        private T mCachedInstance;

        @java.lang.Override
        public final T getService(android.app.ContextImpl unused) {
            synchronized(this) {
                if (mCachedInstance == null) {
                    mCachedInstance = createService();
                }
                return mCachedInstance;
            }
        }

        public abstract T createService();
    }

    /**
     * Like StaticServiceFetcher, creates only one instance of the service per application, but when
     * creating the service for the first time, passes it the application context of the creating
     * application.
     *
     * TODO: Delete this once its only user (ConnectivityManager) is known to work well in the
     * case where multiple application components each have their own ConnectivityManager object.
     */
    static abstract class StaticApplicationContextServiceFetcher<T> implements android.app.SystemServiceRegistry.ServiceFetcher<T> {
        private T mCachedInstance;

        @java.lang.Override
        public final T getService(android.app.ContextImpl ctx) {
            synchronized(this) {
                if (mCachedInstance == null) {
                    android.content.Context appContext = ctx.getApplicationContext();
                    // If the application context is null, we're either in the system process or
                    // it's the application context very early in app initialization. In both these
                    // cases, the passed-in ContextImpl will not be freed, so it's safe to pass it
                    // to the service. http://b/27532714 .
                    mCachedInstance = createService(appContext != null ? appContext : ctx);
                }
                return mCachedInstance;
            }
        }

        public abstract T createService(android.content.Context applicationContext);
    }
}

