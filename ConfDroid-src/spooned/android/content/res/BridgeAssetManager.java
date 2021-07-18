/**
 * Copyright (C) 2008 The Android Open Source Project
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
package android.content.res;


public class BridgeAssetManager extends android.content.res.AssetManager {
    @android.annotation.Nullable
    private com.android.ide.common.rendering.api.AssetRepository mAssetRepository;

    /**
     * This initializes the static field {@link AssetManager#sSystem} which is used
     * by methods who get a global asset manager using {@link AssetManager#getSystem()}.
     * <p/>
     * They will end up using our bridge asset manager.
     * <p/>
     * {@link Bridge} calls this method after setting up a new bridge.
     */
    public static android.content.res.AssetManager initSystem() {
        if (!(android.content.res.AssetManager.sSystem instanceof android.content.res.BridgeAssetManager)) {
            // Note that AssetManager() creates a system AssetManager and we override it
            // with our BridgeAssetManager.
            android.content.res.AssetManager.sSystem = new android.content.res.BridgeAssetManager();
        }
        return android.content.res.AssetManager.sSystem;
    }

    /**
     * Clears the static {@link AssetManager#sSystem} to make sure we don't leave objects
     * around that would prevent us from unloading the library.
     */
    public static void clearSystem() {
        android.content.res.AssetManager.sSystem = null;
    }

    public void setAssetRepository(@android.annotation.NonNull
    com.android.ide.common.rendering.api.AssetRepository assetRepository) {
        mAssetRepository = assetRepository;
    }

    /**
     * Clears the AssetRepository reference.
     */
    public void releaseAssetRepository() {
        mAssetRepository = null;
    }

    @android.annotation.NonNull
    public com.android.ide.common.rendering.api.AssetRepository getAssetRepository() {
        if (mAssetRepository == null) {
            throw new java.lang.IllegalStateException("Asset repository is not set");
        }
        return mAssetRepository;
    }

    public BridgeAssetManager() {
    }
}

