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
 * Effect subclass for effects based on a single Filter. Subclasses need only invoke the
 * constructor with the correct arguments to obtain an Effect implementation.
 *
 * @unknown 
 */
public class SingleFilterEffect extends android.media.effect.FilterEffect {
    protected android.filterfw.core.FilterFunction mFunction;

    protected java.lang.String mInputName;

    protected java.lang.String mOutputName;

    /**
     * Constructs a new FilterFunctionEffect.
     *
     * @param name
     * 		The name of this effect (used to create it in the EffectFactory).
     * @param filterClass
     * 		The class of the filter to wrap.
     * @param inputName
     * 		The name of the input image port.
     * @param outputName
     * 		The name of the output image port.
     * @param finalParameters
     * 		Key-value pairs of final input port assignments.
     */
    public SingleFilterEffect(android.media.effect.EffectContext context, java.lang.String name, java.lang.Class filterClass, java.lang.String inputName, java.lang.String outputName, java.lang.Object... finalParameters) {
        super(context, name);
        mInputName = inputName;
        mOutputName = outputName;
        java.lang.String filterName = filterClass.getSimpleName();
        android.filterfw.core.FilterFactory factory = android.filterfw.core.FilterFactory.sharedFactory();
        android.filterfw.core.Filter filter = factory.createFilterByClass(filterClass, filterName);
        filter.initWithAssignmentList(finalParameters);
        mFunction = new android.filterfw.core.FilterFunction(getFilterContext(), filter);
    }

    @java.lang.Override
    public void apply(int inputTexId, int width, int height, int outputTexId) {
        beginGLEffect();
        android.filterfw.core.Frame inputFrame = frameFromTexture(inputTexId, width, height);
        android.filterfw.core.Frame outputFrame = frameFromTexture(outputTexId, width, height);
        android.filterfw.core.Frame resultFrame = mFunction.executeWithArgList(mInputName, inputFrame);
        outputFrame.setDataFromFrame(resultFrame);
        inputFrame.release();
        outputFrame.release();
        resultFrame.release();
        endGLEffect();
    }

    @java.lang.Override
    public void setParameter(java.lang.String parameterKey, java.lang.Object value) {
        mFunction.setInputValue(parameterKey, value);
    }

    @java.lang.Override
    public void release() {
        mFunction.tearDown();
        mFunction = null;
    }
}

