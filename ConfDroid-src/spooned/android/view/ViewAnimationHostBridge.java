/**
 * Copyright (C) 2018 The Android Open Source Project
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
 * Maps a View to a RenderNode's AnimationHost
 *
 * @unknown 
 */
public class ViewAnimationHostBridge implements android.graphics.RenderNode.AnimationHost {
    private final android.view.View mView;

    /**
     *
     *
     * @param view
     * 		the View to bridge to an AnimationHost
     */
    public ViewAnimationHostBridge(android.view.View view) {
        mView = view;
    }

    @java.lang.Override
    public void registerAnimatingRenderNode(android.graphics.RenderNode animator) {
        mView.mAttachInfo.mViewRootImpl.registerAnimatingRenderNode(animator);
    }

    @java.lang.Override
    public void registerVectorDrawableAnimator(android.view.NativeVectorDrawableAnimator animator) {
        mView.mAttachInfo.mViewRootImpl.registerVectorDrawableAnimator(animator);
    }

    @java.lang.Override
    public boolean isAttached() {
        return mView.mAttachInfo != null;
    }
}

