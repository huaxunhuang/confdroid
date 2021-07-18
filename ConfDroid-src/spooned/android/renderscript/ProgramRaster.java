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
Program raster is primarily used to specify whether point sprites are enabled and to control
the culling mode. By default, back faces are culled.
 */
public class ProgramRaster extends android.renderscript.BaseObj {
    /**
     *
     *
     * @deprecated in API 16
     */
    public enum CullMode {

        /**
         *
         *
         * @deprecated in API 16
         */
        BACK(0),
        /**
         *
         *
         * @deprecated in API 16
         */
        FRONT(1),
        /**
         *
         *
         * @deprecated in API 16
         */
        NONE(2);
        int mID;

        CullMode(int id) {
            mID = id;
        }
    }

    boolean mPointSprite;

    android.renderscript.ProgramRaster.CullMode mCullMode;

    ProgramRaster(long id, android.renderscript.RenderScript rs) {
        super(id, rs);
        mPointSprite = false;
        mCullMode = android.renderscript.ProgramRaster.CullMode.BACK;
    }

    /**
     *
     *
     * @deprecated in API 16
    Specifies whether vertices are rendered as screen aligned
    elements of a specified size
     * @return whether point sprites are enabled
     */
    public boolean isPointSpriteEnabled() {
        return mPointSprite;
    }

    /**
     *
     *
     * @deprecated in API 16
    Specifies how triangles are culled based on their orientation
     * @return cull mode
     */
    public android.renderscript.ProgramRaster.CullMode getCullMode() {
        return mCullMode;
    }

    /**
     *
     *
     * @deprecated in API 16
     */
    public static android.renderscript.ProgramRaster CULL_BACK(android.renderscript.RenderScript rs) {
        if (rs.mProgramRaster_CULL_BACK == null) {
            android.renderscript.ProgramRaster.Builder builder = new android.renderscript.ProgramRaster.Builder(rs);
            builder.setCullMode(android.renderscript.ProgramRaster.CullMode.BACK);
            rs.mProgramRaster_CULL_BACK = builder.create();
        }
        return rs.mProgramRaster_CULL_BACK;
    }

    /**
     *
     *
     * @deprecated in API 16
     */
    public static android.renderscript.ProgramRaster CULL_FRONT(android.renderscript.RenderScript rs) {
        if (rs.mProgramRaster_CULL_FRONT == null) {
            android.renderscript.ProgramRaster.Builder builder = new android.renderscript.ProgramRaster.Builder(rs);
            builder.setCullMode(android.renderscript.ProgramRaster.CullMode.FRONT);
            rs.mProgramRaster_CULL_FRONT = builder.create();
        }
        return rs.mProgramRaster_CULL_FRONT;
    }

    /**
     *
     *
     * @deprecated in API 16
     */
    public static android.renderscript.ProgramRaster CULL_NONE(android.renderscript.RenderScript rs) {
        if (rs.mProgramRaster_CULL_NONE == null) {
            android.renderscript.ProgramRaster.Builder builder = new android.renderscript.ProgramRaster.Builder(rs);
            builder.setCullMode(android.renderscript.ProgramRaster.CullMode.NONE);
            rs.mProgramRaster_CULL_NONE = builder.create();
        }
        return rs.mProgramRaster_CULL_NONE;
    }

    /**
     *
     *
     * @deprecated in API 16
     */
    public static class Builder {
        android.renderscript.RenderScript mRS;

        boolean mPointSprite;

        android.renderscript.ProgramRaster.CullMode mCullMode;

        /**
         *
         *
         * @deprecated in API 16
         */
        public Builder(android.renderscript.RenderScript rs) {
            mRS = rs;
            mPointSprite = false;
            mCullMode = android.renderscript.ProgramRaster.CullMode.BACK;
        }

        /**
         *
         *
         * @deprecated in API 16
         */
        public android.renderscript.ProgramRaster.Builder setPointSpriteEnabled(boolean enable) {
            mPointSprite = enable;
            return this;
        }

        /**
         *
         *
         * @deprecated in API 16
         */
        public android.renderscript.ProgramRaster.Builder setCullMode(android.renderscript.ProgramRaster.CullMode m) {
            mCullMode = m;
            return this;
        }

        /**
         *
         *
         * @deprecated in API 16
         */
        public android.renderscript.ProgramRaster create() {
            mRS.validate();
            long id = mRS.nProgramRasterCreate(mPointSprite, mCullMode.mID);
            android.renderscript.ProgramRaster programRaster = new android.renderscript.ProgramRaster(id, mRS);
            programRaster.mPointSprite = mPointSprite;
            programRaster.mCullMode = mCullMode;
            return programRaster;
        }
    }
}

