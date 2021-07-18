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
package android.media;


/**
 * Class to encapsulate a number of common player operations:
 *   - AppOps for OP_PLAY_AUDIO
 *   - more to come (routing, transport control)
 *
 * @unknown 
 */
public abstract class PlayerBase {
    // parameters of the player that affect AppOps
    protected android.media.AudioAttributes mAttributes;

    protected float mLeftVolume = 1.0F;

    protected float mRightVolume = 1.0F;

    protected float mAuxEffectSendLevel = 0.0F;

    // for AppOps
    private final com.android.internal.app.IAppOpsService mAppOps;

    private final com.android.internal.app.IAppOpsCallback mAppOpsCallback;

    private boolean mHasAppOpsPlayAudio = true;

    private final java.lang.Object mAppOpsLock = new java.lang.Object();

    /**
     * Constructor. Must be given audio attributes, as they are required for AppOps.
     *
     * @param attr
     * 		non-null audio attributes
     */
    PlayerBase(@android.annotation.NonNull
    android.media.AudioAttributes attr) {
        if (attr == null) {
            throw new java.lang.IllegalArgumentException("Illegal null AudioAttributes");
        }
        mAttributes = attr;
        android.os.IBinder b = android.os.ServiceManager.getService(android.content.Context.APP_OPS_SERVICE);
        mAppOps = IAppOpsService.Stub.asInterface(b);
        // initialize mHasAppOpsPlayAudio
        updateAppOpsPlayAudio_sync();
        // register a callback to monitor whether the OP_PLAY_AUDIO is still allowed
        mAppOpsCallback = new com.android.internal.app.IAppOpsCallback.Stub() {
            public void opChanged(int op, int uid, java.lang.String packageName) {
                synchronized(mAppOpsLock) {
                    if (op == android.app.AppOpsManager.OP_PLAY_AUDIO) {
                        updateAppOpsPlayAudio_sync();
                    }
                }
            }
        };
        try {
            mAppOps.startWatchingMode(android.app.AppOpsManager.OP_PLAY_AUDIO, android.app.ActivityThread.currentPackageName(), mAppOpsCallback);
        } catch (android.os.RemoteException e) {
            mHasAppOpsPlayAudio = false;
        }
    }

    /**
     * To be called whenever the audio attributes of the player change
     *
     * @param attr
     * 		non-null audio attributes
     */
    void baseUpdateAudioAttributes(@android.annotation.NonNull
    android.media.AudioAttributes attr) {
        if (attr == null) {
            throw new java.lang.IllegalArgumentException("Illegal null AudioAttributes");
        }
        synchronized(mAppOpsLock) {
            mAttributes = attr;
            updateAppOpsPlayAudio_sync();
        }
    }

    void baseStart() {
        synchronized(mAppOpsLock) {
            if (isRestricted_sync()) {
                playerSetVolume(0, 0);
            }
        }
    }

    void baseSetVolume(float leftVolume, float rightVolume) {
        synchronized(mAppOpsLock) {
            mLeftVolume = leftVolume;
            mRightVolume = rightVolume;
            if (isRestricted_sync()) {
                return;
            }
        }
        playerSetVolume(leftVolume, rightVolume);
    }

    int baseSetAuxEffectSendLevel(float level) {
        synchronized(mAppOpsLock) {
            mAuxEffectSendLevel = level;
            if (isRestricted_sync()) {
                return android.media.AudioSystem.SUCCESS;
            }
        }
        return playerSetAuxEffectSendLevel(level);
    }

    /**
     * To be called from a subclass release or finalize method.
     * Releases AppOps related resources.
     */
    void baseRelease() {
        try {
            mAppOps.stopWatchingMode(mAppOpsCallback);
        } catch (android.os.RemoteException e) {
            // nothing to do here, the object is supposed to be released anyway
        }
    }

    /**
     * To be called whenever a condition that might affect audibility of this player is updated.
     * Must be called synchronized on mAppOpsLock.
     */
    void updateAppOpsPlayAudio_sync() {
        boolean oldHasAppOpsPlayAudio = mHasAppOpsPlayAudio;
        try {
            final int mode = mAppOps.checkAudioOperation(android.app.AppOpsManager.OP_PLAY_AUDIO, mAttributes.getUsage(), android.os.Process.myUid(), android.app.ActivityThread.currentPackageName());
            mHasAppOpsPlayAudio = mode == android.app.AppOpsManager.MODE_ALLOWED;
        } catch (android.os.RemoteException e) {
            mHasAppOpsPlayAudio = false;
        }
        // AppsOps alters a player's volume; when the restriction changes, reflect it on the actual
        // volume used by the player
        try {
            if (oldHasAppOpsPlayAudio != mHasAppOpsPlayAudio) {
                if (mHasAppOpsPlayAudio) {
                    playerSetVolume(mLeftVolume, mRightVolume);
                    playerSetAuxEffectSendLevel(mAuxEffectSendLevel);
                } else {
                    playerSetVolume(0.0F, 0.0F);
                    playerSetAuxEffectSendLevel(0.0F);
                }
            }
        } catch (java.lang.Exception e) {
            // failing silently, player might not be in right state
        }
    }

    /**
     * To be called by the subclass whenever an operation is potentially restricted.
     * As the media player-common behavior are incorporated into this class, the subclass's need
     * to call this method should be removed, and this method could become private.
     * FIXME can this method be private so subclasses don't have to worry about when to check
     *    the restrictions.
     *
     * @return 
     */
    boolean isRestricted_sync() {
        // check app ops
        if (mHasAppOpsPlayAudio) {
            return false;
        }
        // check bypass flag
        if ((mAttributes.getAllFlags() & android.media.AudioAttributes.FLAG_BYPASS_INTERRUPTION_POLICY) != 0) {
            return false;
        }
        return true;
    }

    // Abstract methods a subclass needs to implement
    abstract void playerSetVolume(float leftVolume, float rightVolume);

    abstract int playerSetAuxEffectSendLevel(float level);
}

