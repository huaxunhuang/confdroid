/**
 * Copyright (C) 2011 The Android Open Source Project
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
package android.media.effect.effects;


/**
 *
 *
 * @unknown 
 */
public class IdentityEffect extends android.media.effect.FilterEffect {
    public IdentityEffect(android.media.effect.EffectContext context, java.lang.String name) {
        super(context, name);
    }

    @java.lang.Override
    public void apply(int inputTexId, int width, int height, int outputTexId) {
        beginGLEffect();
        android.filterfw.core.Frame inputFrame = frameFromTexture(inputTexId, width, height);
        android.filterfw.core.Frame outputFrame = frameFromTexture(outputTexId, width, height);
        outputFrame.setDataFromFrame(inputFrame);
        inputFrame.release();
        outputFrame.release();
        endGLEffect();
    }

    @java.lang.Override
    public void setParameter(java.lang.String parameterKey, java.lang.Object value) {
        throw new java.lang.IllegalArgumentException(("Unknown parameter " + parameterKey) + " for IdentityEffect!");
    }

    @java.lang.Override
    public void release() {
    }
}

