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
package android.view.shadow;


/**
 * Generates ambient shadow bitmap
 */
class AmbientShadowTriangulator {
    private final android.view.shadow.AmbientShadowConfig mShadowConfig;

    private final android.view.shadow.AmbientShadowVertexCalculator mCalculator;

    private boolean mValid;

    public AmbientShadowTriangulator(android.view.shadow.AmbientShadowConfig shadowConfig) {
        mShadowConfig = shadowConfig;
        mCalculator = new android.view.shadow.AmbientShadowVertexCalculator(mShadowConfig);
    }

    /**
     * Populate vertices and fill the triangle buffers.
     */
    public void triangulate() {
        try {
            mCalculator.generateVertex();
            mValid = true;
        } catch (java.lang.IndexOutOfBoundsException | java.lang.ArithmeticException mathError) {
            com.android.layoutlib.bridge.Bridge.getLog().warning(LayoutLog.TAG_INFO, "Arithmetic error while drawing " + "ambient shadow", null, mathError);
        } catch (java.lang.Exception ex) {
            com.android.layoutlib.bridge.Bridge.getLog().warning(LayoutLog.TAG_INFO, "Error while drawing shadow", null, ex);
        }
    }

    public boolean isValid() {
        return mValid;
    }

    public float[] getVertices() {
        return mCalculator.getVertex();
    }

    public int[] getIndices() {
        return mCalculator.getIndex();
    }

    public float[] getColors() {
        return mCalculator.getColor();
    }
}

