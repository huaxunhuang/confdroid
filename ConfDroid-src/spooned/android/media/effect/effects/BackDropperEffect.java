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
 * Background replacement Effect.
 *
 * Replaces the background of the input video stream with a selected video
 * Learns the background when it first starts up;
 * needs unobstructed view of background when this happens.
 *
 * Effect parameters:
 *   source: A URI for the background video
 * Listener: Called when learning period is complete
 *
 * @unknown 
 */
public class BackDropperEffect extends android.media.effect.FilterGraphEffect {
    private static final java.lang.String mGraphDefinition = "@import android.filterpacks.base;\n" + (((((((((((((((((((((((((("@import android.filterpacks.videoproc;\n" + "@import android.filterpacks.videosrc;\n") + "\n") + "@filter GLTextureSource foreground {\n") + "  texId = 0;\n")// Will be set by base class
     + "  width = 0;\n") + "  height = 0;\n") + "  repeatFrame = true;\n") + "}\n") + "\n") + "@filter MediaSource background {\n") + "  sourceUrl = \"no_file_specified\";\n") + "  waitForNewFrame = false;\n") + "  sourceIsUrl = true;\n") + "}\n") + "\n") + "@filter BackDropperFilter replacer {\n") + "  autowbToggle = 1;\n") + "}\n") + "\n") + "@filter GLTextureTarget output {\n") + "  texId = 0;\n") + "}\n") + "\n") + "@connect foreground[frame]  => replacer[video];\n") + "@connect background[video]  => replacer[background];\n") + "@connect replacer[video]    => output[frame];\n");

    private android.media.effect.EffectUpdateListener mEffectListener = null;

    private android.filterpacks.videoproc.BackDropperFilter.LearningDoneListener mLearningListener = new android.filterpacks.videoproc.BackDropperFilter.LearningDoneListener() {
        public void onLearningDone(android.filterpacks.videoproc.BackDropperFilter filter) {
            if (mEffectListener != null) {
                mEffectListener.onEffectUpdated(android.media.effect.effects.BackDropperEffect.this, null);
            }
        }
    };

    public BackDropperEffect(android.media.effect.EffectContext context, java.lang.String name) {
        super(context, name, android.media.effect.effects.BackDropperEffect.mGraphDefinition, "foreground", "output", android.filterfw.core.OneShotScheduler.class);
        android.filterfw.core.Filter replacer = mGraph.getFilter("replacer");
        replacer.setInputValue("learningDoneListener", mLearningListener);
    }

    @java.lang.Override
    public void setParameter(java.lang.String parameterKey, java.lang.Object value) {
        if (parameterKey.equals("source")) {
            android.filterfw.core.Filter background = mGraph.getFilter("background");
            background.setInputValue("sourceUrl", value);
        } else
            if (parameterKey.equals("context")) {
                android.filterfw.core.Filter background = mGraph.getFilter("background");
                background.setInputValue("context", value);
            }

    }

    @java.lang.Override
    public void setUpdateListener(android.media.effect.EffectUpdateListener listener) {
        mEffectListener = listener;
    }
}

