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
 * An active voice interaction session, initiated by a {@link VoiceInteractionService}.
 */
public abstract class VoiceInteractionSessionService extends android.app.Service {
    static final int MSG_NEW_SESSION = 1;

    com.android.internal.app.IVoiceInteractionManagerService mSystemService;

    android.service.voice.VoiceInteractionSession mSession;

    android.service.voice.IVoiceInteractionSessionService mInterface = new android.service.voice.IVoiceInteractionSessionService.Stub() {
        public void newSession(android.os.IBinder token, android.os.Bundle args, int startFlags) {
            mHandlerCaller.sendMessage(mHandlerCaller.obtainMessageIOO(android.service.voice.VoiceInteractionSessionService.MSG_NEW_SESSION, startFlags, token, args));
        }
    };

    com.android.internal.os.HandlerCaller mHandlerCaller;

    final HandlerCaller.Callback mHandlerCallerCallback = new com.android.internal.os.HandlerCaller.Callback() {
        @java.lang.Override
        public void executeMessage(android.os.Message msg) {
            com.android.internal.os.SomeArgs args = ((com.android.internal.os.SomeArgs) (msg.obj));
            switch (msg.what) {
                case android.service.voice.VoiceInteractionSessionService.MSG_NEW_SESSION :
                    doNewSession(((android.os.IBinder) (args.arg1)), ((android.os.Bundle) (args.arg2)), args.argi1);
                    break;
            }
        }
    };

    @java.lang.Override
    public void onCreate() {
        super.onCreate();
        mSystemService = IVoiceInteractionManagerService.Stub.asInterface(android.os.ServiceManager.getService(android.content.Context.VOICE_INTERACTION_MANAGER_SERVICE));
        mHandlerCaller = new com.android.internal.os.HandlerCaller(this, android.os.Looper.myLooper(), mHandlerCallerCallback, true);
    }

    public abstract android.service.voice.VoiceInteractionSession onNewSession(android.os.Bundle args);

    @java.lang.Override
    public android.os.IBinder onBind(android.content.Intent intent) {
        return mInterface.asBinder();
    }

    @java.lang.Override
    public void onConfigurationChanged(android.content.res.Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (mSession != null) {
            mSession.onConfigurationChanged(newConfig);
        }
    }

    @java.lang.Override
    public void onLowMemory() {
        super.onLowMemory();
        if (mSession != null) {
            mSession.onLowMemory();
        }
    }

    @java.lang.Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        if (mSession != null) {
            mSession.onTrimMemory(level);
        }
    }

    @java.lang.Override
    protected void dump(java.io.FileDescriptor fd, java.io.PrintWriter writer, java.lang.String[] args) {
        if (mSession == null) {
            writer.println("(no active session)");
        } else {
            writer.println("VoiceInteractionSession:");
            mSession.dump("  ", fd, writer, args);
        }
    }

    void doNewSession(android.os.IBinder token, android.os.Bundle args, int startFlags) {
        if (mSession != null) {
            mSession.doDestroy();
            mSession = null;
        }
        mSession = onNewSession(args);
        try {
            mSystemService.deliverNewSession(token, mSession.mSession, mSession.mInteractor);
            mSession.doCreate(mSystemService, token);
        } catch (android.os.RemoteException e) {
        }
    }
}

