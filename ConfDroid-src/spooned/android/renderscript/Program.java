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
 * @unknown Program is a base class for all the objects that modify
various stages of the graphics pipeline
 */
public class Program extends android.renderscript.BaseObj {
    static final int MAX_INPUT = 8;

    static final int MAX_OUTPUT = 8;

    static final int MAX_CONSTANT = 8;

    static final int MAX_TEXTURE = 8;

    /**
     * TextureType specifies what textures are attached to Program
     * objects
     */
    public enum TextureType {

        TEXTURE_2D(0),
        TEXTURE_CUBE(1);
        int mID;

        TextureType(int id) {
            mID = id;
        }
    }

    enum ProgramParam {

        INPUT(0),
        OUTPUT(1),
        CONSTANT(2),
        TEXTURE_TYPE(3);
        int mID;

        ProgramParam(int id) {
            mID = id;
        }
    }

    android.renderscript.Element[] mInputs;

    android.renderscript.Element[] mOutputs;

    android.renderscript.Type[] mConstants;

    android.renderscript.Program.TextureType[] mTextures;

    java.lang.String[] mTextureNames;

    int mTextureCount;

    java.lang.String mShader;

    Program(long id, android.renderscript.RenderScript rs) {
        super(id, rs);
        guard.open("destroy");
    }

    /**
     * Program object can have zero or more constant allocations
     * associated with it. This method returns the total count.
     *
     * @return number of constant input types
     */
    public int getConstantCount() {
        return mConstants != null ? mConstants.length : 0;
    }

    /**
     * Returns the type of the constant buffer used in the program
     * object. It could be used to query internal elements or create
     * an allocation to store constant data.
     *
     * @param slot
     * 		index of the constant input type to return
     * @return constant input type
     */
    public android.renderscript.Type getConstant(int slot) {
        if ((slot < 0) || (slot >= mConstants.length)) {
            throw new java.lang.IllegalArgumentException("Slot ID out of range.");
        }
        return mConstants[slot];
    }

    /**
     * Returns the number of textures used in this program object
     *
     * @return number of texture inputs
     */
    public int getTextureCount() {
        return mTextureCount;
    }

    /**
     * Returns the type of texture at a given slot. e.g. 2D or Cube
     *
     * @param slot
     * 		index of the texture input
     * @return texture input type
     */
    public android.renderscript.Program.TextureType getTextureType(int slot) {
        if ((slot < 0) || (slot >= mTextureCount)) {
            throw new java.lang.IllegalArgumentException("Slot ID out of range.");
        }
        return mTextures[slot];
    }

    /**
     * Returns the name of the texture input at a given slot. e.g.
     * tex0, diffuse, spec
     *
     * @param slot
     * 		index of the texture input
     * @return texture input name
     */
    public java.lang.String getTextureName(int slot) {
        if ((slot < 0) || (slot >= mTextureCount)) {
            throw new java.lang.IllegalArgumentException("Slot ID out of range.");
        }
        return mTextureNames[slot];
    }

    /**
     * Binds a constant buffer to be used as uniform inputs to the
     * program
     *
     * @param a
     * 		allocation containing uniform data
     * @param slot
     * 		index within the program's list of constant
     * 		buffer allocations
     */
    public void bindConstants(android.renderscript.Allocation a, int slot) {
        if ((slot < 0) || (slot >= mConstants.length)) {
            throw new java.lang.IllegalArgumentException("Slot ID out of range.");
        }
        if ((a != null) && (a.getType().getID(mRS) != mConstants[slot].getID(mRS))) {
            throw new java.lang.IllegalArgumentException("Allocation type does not match slot type.");
        }
        long id = (a != null) ? a.getID(mRS) : 0;
        mRS.nProgramBindConstants(getID(mRS), slot, id);
    }

    /**
     * Binds a texture to be used in the program
     *
     * @param va
     * 		allocation containing texture data
     * @param slot
     * 		index within the program's list of textures
     */
    public void bindTexture(android.renderscript.Allocation va, int slot) throws java.lang.IllegalArgumentException {
        mRS.validate();
        if ((slot < 0) || (slot >= mTextureCount)) {
            throw new java.lang.IllegalArgumentException("Slot ID out of range.");
        }
        if (((va != null) && va.getType().hasFaces()) && (mTextures[slot] != android.renderscript.Program.TextureType.TEXTURE_CUBE)) {
            throw new java.lang.IllegalArgumentException("Cannot bind cubemap to 2d texture slot");
        }
        long id = (va != null) ? va.getID(mRS) : 0;
        mRS.nProgramBindTexture(getID(mRS), slot, id);
    }

    /**
     * Binds an object that describes how a texture at the
     * corresponding location is sampled
     *
     * @param vs
     * 		sampler for a corresponding texture
     * @param slot
     * 		index within the program's list of textures to
     * 		use the sampler on
     */
    public void bindSampler(android.renderscript.Sampler vs, int slot) throws java.lang.IllegalArgumentException {
        mRS.validate();
        if ((slot < 0) || (slot >= mTextureCount)) {
            throw new java.lang.IllegalArgumentException("Slot ID out of range.");
        }
        long id = (vs != null) ? vs.getID(mRS) : 0;
        mRS.nProgramBindSampler(getID(mRS), slot, id);
    }

    public static class BaseProgramBuilder {
        android.renderscript.RenderScript mRS;

        android.renderscript.Element[] mInputs;

        android.renderscript.Element[] mOutputs;

        android.renderscript.Type[] mConstants;

        android.renderscript.Type[] mTextures;

        android.renderscript.Program.TextureType[] mTextureTypes;

        java.lang.String[] mTextureNames;

        int mInputCount;

        int mOutputCount;

        int mConstantCount;

        int mTextureCount;

        java.lang.String mShader;

        protected BaseProgramBuilder(android.renderscript.RenderScript rs) {
            mRS = rs;
            mInputs = new android.renderscript.Element[android.renderscript.Program.MAX_INPUT];
            mOutputs = new android.renderscript.Element[android.renderscript.Program.MAX_OUTPUT];
            mConstants = new android.renderscript.Type[android.renderscript.Program.MAX_CONSTANT];
            mInputCount = 0;
            mOutputCount = 0;
            mConstantCount = 0;
            mTextureCount = 0;
            mTextureTypes = new android.renderscript.Program.TextureType[android.renderscript.Program.MAX_TEXTURE];
            mTextureNames = new java.lang.String[android.renderscript.Program.MAX_TEXTURE];
        }

        /**
         * Sets the GLSL shader code to be used in the program
         *
         * @param s
         * 		GLSL shader string
         * @return self
         */
        public android.renderscript.Program.BaseProgramBuilder setShader(java.lang.String s) {
            mShader = s;
            return this;
        }

        /**
         * Sets the GLSL shader code to be used in the program
         *
         * @param resources
         * 		application resources
         * @param resourceID
         * 		id of the file containing GLSL shader code
         * @return self
         */
        public android.renderscript.Program.BaseProgramBuilder setShader(android.content.res.Resources resources, int resourceID) {
            byte[] str;
            int strLength;
            java.io.InputStream is = resources.openRawResource(resourceID);
            try {
                try {
                    str = new byte[1024];
                    strLength = 0;
                    while (true) {
                        int bytesLeft = str.length - strLength;
                        if (bytesLeft == 0) {
                            byte[] buf2 = new byte[str.length * 2];
                            java.lang.System.arraycopy(str, 0, buf2, 0, str.length);
                            str = buf2;
                            bytesLeft = str.length - strLength;
                        }
                        int bytesRead = is.read(str, strLength, bytesLeft);
                        if (bytesRead <= 0) {
                            break;
                        }
                        strLength += bytesRead;
                    } 
                } finally {
                    is.close();
                }
            } catch (java.io.IOException e) {
                throw new android.content.res.Resources.NotFoundException();
            }
            try {
                mShader = new java.lang.String(str, 0, strLength, "UTF-8");
            } catch (java.io.UnsupportedEncodingException e) {
                android.util.Log.e("RenderScript shader creation", "Could not decode shader string");
            }
            return this;
        }

        /**
         * Queries the index of the last added constant buffer type
         */
        public int getCurrentConstantIndex() {
            return mConstantCount - 1;
        }

        /**
         * Queries the index of the last added texture type
         */
        public int getCurrentTextureIndex() {
            return mTextureCount - 1;
        }

        /**
         * Adds constant (uniform) inputs to the program
         *
         * @param t
         * 		Type that describes the layout of the Allocation
         * 		object to be used as constant inputs to the Program
         * @return self
         */
        public android.renderscript.Program.BaseProgramBuilder addConstant(android.renderscript.Type t) throws java.lang.IllegalStateException {
            // Should check for consistant and non-conflicting names...
            if (mConstantCount >= android.renderscript.Program.MAX_CONSTANT) {
                throw new android.renderscript.RSIllegalArgumentException("Max input count exceeded.");
            }
            if (t.getElement().isComplex()) {
                throw new android.renderscript.RSIllegalArgumentException("Complex elements not allowed.");
            }
            mConstants[mConstantCount] = t;
            mConstantCount++;
            return this;
        }

        /**
         * Adds a texture input to the Program
         *
         * @param texType
         * 		describes that the texture to append it (2D,
         * 		Cubemap, etc.)
         * @return self
         */
        public android.renderscript.Program.BaseProgramBuilder addTexture(android.renderscript.Program.TextureType texType) throws java.lang.IllegalArgumentException {
            addTexture(texType, "Tex" + mTextureCount);
            return this;
        }

        /**
         * Adds a texture input to the Program
         *
         * @param texType
         * 		describes that the texture to append it (2D,
         * 		Cubemap, etc.)
         * @param texName
         * 		what the texture should be called in the
         * 		shader
         * @return self
         */
        public android.renderscript.Program.BaseProgramBuilder addTexture(android.renderscript.Program.TextureType texType, java.lang.String texName) throws java.lang.IllegalArgumentException {
            if (mTextureCount >= android.renderscript.Program.MAX_TEXTURE) {
                throw new java.lang.IllegalArgumentException("Max texture count exceeded.");
            }
            mTextureTypes[mTextureCount] = texType;
            mTextureNames[mTextureCount] = texName;
            mTextureCount++;
            return this;
        }

        protected void initProgram(android.renderscript.Program p) {
            p.mInputs = new android.renderscript.Element[mInputCount];
            java.lang.System.arraycopy(mInputs, 0, p.mInputs, 0, mInputCount);
            p.mOutputs = new android.renderscript.Element[mOutputCount];
            java.lang.System.arraycopy(mOutputs, 0, p.mOutputs, 0, mOutputCount);
            p.mConstants = new android.renderscript.Type[mConstantCount];
            java.lang.System.arraycopy(mConstants, 0, p.mConstants, 0, mConstantCount);
            p.mTextureCount = mTextureCount;
            p.mTextures = new android.renderscript.Program.TextureType[mTextureCount];
            java.lang.System.arraycopy(mTextureTypes, 0, p.mTextures, 0, mTextureCount);
            p.mTextureNames = new java.lang.String[mTextureCount];
            java.lang.System.arraycopy(mTextureNames, 0, p.mTextureNames, 0, mTextureCount);
        }
    }
}

