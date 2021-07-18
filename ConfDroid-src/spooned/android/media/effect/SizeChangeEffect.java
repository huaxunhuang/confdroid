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
 * Effect subclass for effects based on a single Filter with output size differnet
 * from input.  Subclasses need only invoke the constructor with the correct arguments
 * to obtain an Effect implementation.
 *
 * @unknown 
 */
public class SizeChangeEffect extends android.media.effect.SingleFilterEffect {
    public SizeChangeEffect(android.media.effect.EffectContext context, java.lang.String name, java.lang.Class filterClass, java.lang.String inputName, java.lang.String outputName, java.lang.Object... finalParameters) {
        super(context, name, filterClass, inputName, outputName, finalParameters);
    }

    @java.lang.Override
    public void apply(int inputTexId, int width, int height, int outputTexId) {
        beginGLEffect();
        android.filterfw.core.Frame inputFrame = frameFromTexture(inputTexId, width, height);
        android.filterfw.core.Frame resultFrame = mFunction.executeWithArgList(mInputName, inputFrame);
        int outputWidth = resultFrame.getFormat().getWidth();
        int outputHeight = resultFrame.getFormat().getHeight();
        android.filterfw.core.Frame outputFrame = frameFromTexture(outputTexId, outputWidth, outputHeight);
        outputFrame.setDataFromFrame(resultFrame);
        inputFrame.release();
        outputFrame.release();
        resultFrame.release();
        endGLEffect();
    }
}

