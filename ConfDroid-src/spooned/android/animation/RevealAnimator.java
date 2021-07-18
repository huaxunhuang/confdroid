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
package android.animation;


/**
 * Reveals a View with an animated clipping circle.
 * The clipping is implemented efficiently by talking to a private reveal API on View.
 * This hidden class currently only accessed by the {@link android.view.View}.
 *
 * @unknown 
 */
public class RevealAnimator extends android.view.RenderNodeAnimator {
    private android.view.View mClipView;

    public RevealAnimator(android.view.View clipView, int x, int y, float startRadius, float endRadius) {
        super(x, y, startRadius, endRadius);
        mClipView = clipView;
        setTarget(mClipView);
    }

    @java.lang.Override
    protected void onFinished() {
        mClipView.setRevealClip(false, 0, 0, 0);
        super.onFinished();
    }
}

