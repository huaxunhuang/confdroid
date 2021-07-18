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
package android.media.soundtrigger;


/**
 * This class provides management of non-voice (general sound trigger) based sound recognition
 * models. Usage of this class is restricted to system or signature applications only. This allows
 * OEMs to write apps that can manage non-voice based sound trigger models.
 *
 * @unknown 
 */
@android.annotation.SystemApi
public final class SoundTriggerManager {
    private static final boolean DBG = false;

    private static final java.lang.String TAG = "SoundTriggerManager";

    private final android.content.Context mContext;

    private final com.android.internal.app.ISoundTriggerService mSoundTriggerService;

    // Stores a mapping from the sound model UUID to the SoundTriggerInstance created by
    // the createSoundTriggerDetector() call.
    private final java.util.HashMap<java.util.UUID, android.media.soundtrigger.SoundTriggerDetector> mReceiverInstanceMap;

    /**
     *
     *
     * @unknown 
     */
    public SoundTriggerManager(android.content.Context context, com.android.internal.app.ISoundTriggerService soundTriggerService) {
        if (android.media.soundtrigger.SoundTriggerManager.DBG) {
            android.util.Slog.i(android.media.soundtrigger.SoundTriggerManager.TAG, "SoundTriggerManager created.");
        }
        mSoundTriggerService = soundTriggerService;
        mContext = context;
        mReceiverInstanceMap = new java.util.HashMap<java.util.UUID, android.media.soundtrigger.SoundTriggerDetector>();
    }

    /**
     * Updates the given sound trigger model.
     */
    public void updateModel(android.media.soundtrigger.SoundTriggerManager.Model model) {
        try {
            mSoundTriggerService.updateSoundModel(model.getGenericSoundModel());
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * Returns the sound trigger model represented by the given UUID. An instance of {@link Model}
     * is returned.
     */
    public android.media.soundtrigger.SoundTriggerManager.Model getModel(java.util.UUID soundModelId) {
        try {
            return new android.media.soundtrigger.SoundTriggerManager.Model(mSoundTriggerService.getSoundModel(new android.os.ParcelUuid(soundModelId)));
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * Deletes the sound model represented by the provided UUID.
     */
    public void deleteModel(java.util.UUID soundModelId) {
        try {
            mSoundTriggerService.deleteSoundModel(new android.os.ParcelUuid(soundModelId));
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * Creates an instance of {@link SoundTriggerDetector} which can be used to start/stop
     * recognition on the model and register for triggers from the model. Note that this call
     * invalidates any previously returned instances for the same sound model Uuid.
     *
     * @param soundModelId
     * 		UUID of the sound model to create the receiver object for.
     * @param callback
     * 		Instance of the {@link SoundTriggerDetector#Callback} object for the
     * 		callbacks for the given sound model.
     * @param handler
     * 		The Handler to use for the callback operations. A null value will use the
     * 		current thread's Looper.
     * @return Instance of {@link SoundTriggerDetector} or null on error.
     */
    @android.annotation.Nullable
    public android.media.soundtrigger.SoundTriggerDetector createSoundTriggerDetector(java.util.UUID soundModelId, @android.annotation.NonNull
    android.media.soundtrigger.SoundTriggerDetector.Callback callback, @android.annotation.Nullable
    android.os.Handler handler) {
        if (soundModelId == null) {
            return null;
        }
        android.media.soundtrigger.SoundTriggerDetector oldInstance = mReceiverInstanceMap.get(soundModelId);
        if (oldInstance != null) {
            // Shutdown old instance.
        }
        android.media.soundtrigger.SoundTriggerDetector newInstance = new android.media.soundtrigger.SoundTriggerDetector(mSoundTriggerService, soundModelId, callback, handler);
        mReceiverInstanceMap.put(soundModelId, newInstance);
        return newInstance;
    }

    /**
     * Class captures the data and fields that represent a non-keyphrase sound model. Use the
     * factory constructor {@link Model#create()} to create an instance.
     */
    // We use encapsulation to expose the SoundTrigger.GenericSoundModel as a SystemApi. This
    // prevents us from exposing SoundTrigger.GenericSoundModel as an Api.
    public static class Model {
        private android.hardware.soundtrigger.SoundTrigger.GenericSoundModel mGenericSoundModel;

        /**
         *
         *
         * @unknown 
         */
        Model(android.hardware.soundtrigger.SoundTrigger.GenericSoundModel soundTriggerModel) {
            mGenericSoundModel = soundTriggerModel;
        }

        /**
         * Factory constructor to create a SoundModel instance for use with methods in this
         * class.
         */
        public static android.media.soundtrigger.SoundTriggerManager.Model create(java.util.UUID modelUuid, java.util.UUID vendorUuid, byte[] data) {
            return new android.media.soundtrigger.SoundTriggerManager.Model(new android.hardware.soundtrigger.SoundTrigger.GenericSoundModel(modelUuid, vendorUuid, data));
        }

        public java.util.UUID getModelUuid() {
            return mGenericSoundModel.uuid;
        }

        public java.util.UUID getVendorUuid() {
            return mGenericSoundModel.vendorUuid;
        }

        public byte[] getModelData() {
            return mGenericSoundModel.data;
        }

        /**
         *
         *
         * @unknown 
         */
        android.hardware.soundtrigger.SoundTrigger.GenericSoundModel getGenericSoundModel() {
            return mGenericSoundModel;
        }
    }
}

