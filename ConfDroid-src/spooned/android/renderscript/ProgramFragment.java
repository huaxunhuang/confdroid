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
package android.renderscript;


/**
 *
 *
 * @unknown 
 * @deprecated in API 16
<p>The RenderScript fragment program, also known as fragment shader is responsible
for manipulating pixel data in a user defined way. It's constructed from a GLSL
shader string containing the program body, textures inputs, and a Type object
that describes the constants used by the program. Similar to the vertex programs,
when an allocation with constant input values is bound to the shader, its values
are sent to the graphics program automatically.</p>
<p> The values inside the allocation are not explicitly tracked. If they change between two draw
calls using the same program object, the runtime needs to be notified of that
change by calling rsgAllocationSyncAll so it could send the new values to hardware.
Communication between the vertex and fragment programs is handled internally in the
GLSL code. For example, if the fragment program is expecting a varying input called
varTex0, the GLSL code inside the program vertex must provide it.
</p>
 */
public class ProgramFragment extends android.renderscript.Program {
    ProgramFragment(long id, android.renderscript.RenderScript rs) {
        super(id, rs);
    }

    /**
     *
     *
     * @deprecated in API 16
     */
    public static class Builder extends android.renderscript.Program.BaseProgramBuilder {
        /**
         *
         *
         * @deprecated in API 16
        Create a builder object.
         * @param rs
         * 		Context to which the program will belong.
         */
        public Builder(android.renderscript.RenderScript rs) {
            super(rs);
        }

        /**
         *
         *
         * @deprecated in API 16
        Creates ProgramFragment from the current state of the builder
         * @return ProgramFragment
         */
        public android.renderscript.ProgramFragment create() {
            mRS.validate();
            long[] tmp = new long[(((mInputCount + mOutputCount) + mConstantCount) + mTextureCount) * 2];
            java.lang.String[] texNames = new java.lang.String[mTextureCount];
            int idx = 0;
            for (int i = 0; i < mInputCount; i++) {
                tmp[idx++] = android.renderscript.Program.ProgramParam.INPUT.mID;
                tmp[idx++] = mInputs[i].getID(mRS);
            }
            for (int i = 0; i < mOutputCount; i++) {
                tmp[idx++] = android.renderscript.Program.ProgramParam.OUTPUT.mID;
                tmp[idx++] = mOutputs[i].getID(mRS);
            }
            for (int i = 0; i < mConstantCount; i++) {
                tmp[idx++] = android.renderscript.Program.ProgramParam.CONSTANT.mID;
                tmp[idx++] = mConstants[i].getID(mRS);
            }
            for (int i = 0; i < mTextureCount; i++) {
                tmp[idx++] = android.renderscript.Program.ProgramParam.TEXTURE_TYPE.mID;
                tmp[idx++] = mTextureTypes[i].mID;
                texNames[i] = mTextureNames[i];
            }
            long id = mRS.nProgramFragmentCreate(mShader, texNames, tmp);
            android.renderscript.ProgramFragment pf = new android.renderscript.ProgramFragment(id, mRS);
            initProgram(pf);
            return pf;
        }
    }
}

