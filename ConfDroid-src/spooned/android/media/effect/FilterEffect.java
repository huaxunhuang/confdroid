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
package android.media.effect;


/**
 * The FilterEffect class is the base class for all Effects based on Filters from the Mobile
 * Filter Framework (MFF).
 *
 * @unknown 
 */
public abstract class FilterEffect extends android.media.effect.Effect {
    protected android.media.effect.EffectContext mEffectContext;

    private java.lang.String mName;

    /**
     * Protected constructor as FilterEffects should be created by Factory.
     */
    protected FilterEffect(android.media.effect.EffectContext context, java.lang.String name) {
        mEffectContext = context;
        mName = name;
    }

    /**
     * Get the effect name.
     *
     * Returns the unique name of the effect, which matches the name used for instantiating this
     * effect by the EffectFactory.
     *
     * @return The name of the effect.
     */
    @java.lang.Override
    public java.lang.String getName() {
        return mName;
    }

    // Helper Methods for subclasses ///////////////////////////////////////////////////////////////
    /**
     * Call this before manipulating the GL context. Will assert that the GL environment is in a
     * valid state, and save it.
     */
    protected void beginGLEffect() {
        mEffectContext.assertValidGLState();
        mEffectContext.saveGLState();
    }

    /**
     * Call this after manipulating the GL context. Restores the previous GL state.
     */
    protected void endGLEffect() {
        mEffectContext.restoreGLState();
    }

    /**
     * Returns the active filter context for this effect.
     */
    protected android.filterfw.core.FilterContext getFilterContext() {
        return mEffectContext.mFilterContext;
    }

    /**
     * Converts a texture into a Frame.
     */
    protected android.filterfw.core.Frame frameFromTexture(int texId, int width, int height) {
        android.filterfw.core.FrameManager manager = getFilterContext().getFrameManager();
        android.filterfw.core.FrameFormat format = android.filterfw.format.ImageFormat.create(width, height, android.filterfw.format.ImageFormat.COLORSPACE_RGBA, android.filterfw.core.FrameFormat.TARGET_GPU);
        android.filterfw.core.Frame frame = manager.newBoundFrame(format, android.filterfw.core.GLFrame.EXISTING_TEXTURE_BINDING, texId);
        frame.setTimestamp(android.filterfw.core.Frame.TIMESTAMP_UNKNOWN);
        return frame;
    }
}

