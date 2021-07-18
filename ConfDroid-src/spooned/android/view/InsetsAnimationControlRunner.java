/**
 * Copyright (C) 2020 The Android Open Source Project
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
package android.view;


/**
 * Interface representing a runner for an insets animation.
 *
 * @unknown 
 */
public interface InsetsAnimationControlRunner {
    /**
     *
     *
     * @return The {@link InsetsType} the animation of this runner is controlling.
     */
    @android.view.WindowInsets.Type.InsetsType
    int getTypes();

    /**
     * Cancels the animation.
     */
    void cancel();

    /**
     *
     *
     * @return The animation this runner is running.
     */
    android.view.WindowInsetsAnimation getAnimation();

    /**
     *
     *
     * @return Whether {@link #getTypes()} maps to a specific {@link InternalInsetsType}.
     */
    default boolean controlsInternalType(@android.view.InsetsState.InternalInsetsType
    int type) {
        return android.view.InsetsState.toInternalType(getTypes()).contains(type);
    }

    /**
     *
     *
     * @return The animation type this runner is running.
     */
    @android.view.InsetsController.AnimationType
    int getAnimationType();
}

