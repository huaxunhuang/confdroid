/**
 * Copyright (C) 2014 The Android Open Source Project
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
package android.service.voice;


/**
 *
 *
 * @unknown 
 */
public class VoiceInteractionServiceInfo {
    static final java.lang.String TAG = "VoiceInteractionServiceInfo";

    private java.lang.String mParseError;

    private android.content.pm.ServiceInfo mServiceInfo;

    private java.lang.String mSessionService;

    private java.lang.String mRecognitionService;

    private java.lang.String mSettingsActivity;

    private boolean mSupportsAssist;

    private boolean mSupportsLaunchFromKeyguard;

    private boolean mSupportsLocalInteraction;

    public VoiceInteractionServiceInfo(android.content.pm.PackageManager pm, android.content.ComponentName comp) throws android.content.pm.PackageManager.NameNotFoundException {
        this(pm, pm.getServiceInfo(comp, android.content.pm.PackageManager.GET_META_DATA));
    }

    public VoiceInteractionServiceInfo(android.content.pm.PackageManager pm, android.content.ComponentName comp, int userHandle) throws android.content.pm.PackageManager.NameNotFoundException {
        this(pm, android.service.voice.VoiceInteractionServiceInfo.getServiceInfoOrThrow(comp, userHandle));
    }

    static android.content.pm.ServiceInfo getServiceInfoOrThrow(android.content.ComponentName comp, int userHandle) throws android.content.pm.PackageManager.NameNotFoundException {
        try {
            android.content.pm.ServiceInfo si = android.app.AppGlobals.getPackageManager().getServiceInfo(comp, ((android.content.pm.PackageManager.GET_META_DATA | android.content.pm.PackageManager.MATCH_DIRECT_BOOT_AWARE) | android.content.pm.PackageManager.MATCH_DIRECT_BOOT_UNAWARE) | android.content.pm.PackageManager.MATCH_DEBUG_TRIAGED_MISSING, userHandle);
            if (si != null) {
                return si;
            }
        } catch (android.os.RemoteException e) {
        }
        throw new android.content.pm.PackageManager.NameNotFoundException(comp.toString());
    }

    public VoiceInteractionServiceInfo(android.content.pm.PackageManager pm, android.content.pm.ServiceInfo si) {
        if (si == null) {
            mParseError = "Service not available";
            return;
        }
        if (!Manifest.permission.BIND_VOICE_INTERACTION.equals(si.permission)) {
            mParseError = "Service does not require permission " + Manifest.permission.BIND_VOICE_INTERACTION;
            return;
        }
        android.content.res.XmlResourceParser parser = null;
        try {
            parser = si.loadXmlMetaData(pm, android.service.voice.VoiceInteractionService.SERVICE_META_DATA);
            if (parser == null) {
                mParseError = (("No " + android.service.voice.VoiceInteractionService.SERVICE_META_DATA) + " meta-data for ") + si.packageName;
                return;
            }
            android.content.res.Resources res = pm.getResourcesForApplication(si.applicationInfo);
            android.util.AttributeSet attrs = android.util.Xml.asAttributeSet(parser);
            int type;
            while (((type = parser.next()) != org.xmlpull.v1.XmlPullParser.END_DOCUMENT) && (type != org.xmlpull.v1.XmlPullParser.START_TAG)) {
            } 
            java.lang.String nodeName = parser.getName();
            if (!"voice-interaction-service".equals(nodeName)) {
                mParseError = "Meta-data does not start with voice-interaction-service tag";
                return;
            }
            android.content.res.TypedArray array = res.obtainAttributes(attrs, com.android.internal.R.styleable.VoiceInteractionService);
            mSessionService = array.getString(com.android.internal.R.styleable.VoiceInteractionService_sessionService);
            mRecognitionService = array.getString(com.android.internal.R.styleable.VoiceInteractionService_recognitionService);
            mSettingsActivity = array.getString(com.android.internal.R.styleable.VoiceInteractionService_settingsActivity);
            mSupportsAssist = array.getBoolean(com.android.internal.R.styleable.VoiceInteractionService_supportsAssist, false);
            mSupportsLaunchFromKeyguard = array.getBoolean(com.android.internal.R.styleable.VoiceInteractionService_supportsLaunchVoiceAssistFromKeyguard, false);
            mSupportsLocalInteraction = array.getBoolean(com.android.internal.R.styleable.VoiceInteractionService_supportsLocalInteraction, false);
            array.recycle();
            if (mSessionService == null) {
                mParseError = "No sessionService specified";
                return;
            }
            if (mRecognitionService == null) {
                mParseError = "No recognitionService specified";
                return;
            }
        } catch (org.xmlpull.v1.XmlPullParserException e) {
            mParseError = "Error parsing voice interation service meta-data: " + e;
            android.util.Log.w(android.service.voice.VoiceInteractionServiceInfo.TAG, "error parsing voice interaction service meta-data", e);
            return;
        } catch (java.io.IOException e) {
            mParseError = "Error parsing voice interation service meta-data: " + e;
            android.util.Log.w(android.service.voice.VoiceInteractionServiceInfo.TAG, "error parsing voice interaction service meta-data", e);
            return;
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            mParseError = "Error parsing voice interation service meta-data: " + e;
            android.util.Log.w(android.service.voice.VoiceInteractionServiceInfo.TAG, "error parsing voice interaction service meta-data", e);
            return;
        } finally {
            if (parser != null)
                parser.close();

        }
        mServiceInfo = si;
    }

    public java.lang.String getParseError() {
        return mParseError;
    }

    public android.content.pm.ServiceInfo getServiceInfo() {
        return mServiceInfo;
    }

    public java.lang.String getSessionService() {
        return mSessionService;
    }

    public java.lang.String getRecognitionService() {
        return mRecognitionService;
    }

    public java.lang.String getSettingsActivity() {
        return mSettingsActivity;
    }

    public boolean getSupportsAssist() {
        return mSupportsAssist;
    }

    public boolean getSupportsLaunchFromKeyguard() {
        return mSupportsLaunchFromKeyguard;
    }

    public boolean getSupportsLocalInteraction() {
        return mSupportsLocalInteraction;
    }
}

