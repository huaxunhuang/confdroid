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
 * Sampler object that defines how Allocations can be read as textures within a
 * kernel. Samplers are used in conjunction with the {@code rsSample} runtime
 * function to return values from normalized coordinates.
 *
 * Any Allocation used with a Sampler must have been created with {@link android.renderscript.Allocation#USAGE_GRAPHICS_TEXTURE}; using a Sampler on
 * an {@link android.renderscript.Allocation} that was not created with {@link android.renderscript.Allocation#USAGE_GRAPHICS_TEXTURE} is undefined.
 */
public class Sampler extends android.renderscript.BaseObj {
    public enum Value {

        NEAREST(0),
        LINEAR(1),
        LINEAR_MIP_LINEAR(2),
        LINEAR_MIP_NEAREST(5),
        WRAP(3),
        CLAMP(4),
        MIRRORED_REPEAT(6);
        int mID;

        Value(int id) {
            mID = id;
        }
    }

    android.renderscript.Sampler.Value mMin;

    android.renderscript.Sampler.Value mMag;

    android.renderscript.Sampler.Value mWrapS;

    android.renderscript.Sampler.Value mWrapT;

    android.renderscript.Sampler.Value mWrapR;

    float mAniso;

    Sampler(long id, android.renderscript.RenderScript rs) {
        super(id, rs);
        guard.open("destroy");
    }

    /**
     *
     *
     * @return minification setting for the sampler
     */
    public android.renderscript.Sampler.Value getMinification() {
        return mMin;
    }

    /**
     *
     *
     * @return magnification setting for the sampler
     */
    public android.renderscript.Sampler.Value getMagnification() {
        return mMag;
    }

    /**
     *
     *
     * @return S wrapping mode for the sampler
     */
    public android.renderscript.Sampler.Value getWrapS() {
        return mWrapS;
    }

    /**
     *
     *
     * @return T wrapping mode for the sampler
     */
    public android.renderscript.Sampler.Value getWrapT() {
        return mWrapT;
    }

    /**
     *
     *
     * @return anisotropy setting for the sampler
     */
    public float getAnisotropy() {
        return mAniso;
    }

    /**
     * Retrieve a sampler with min and mag set to nearest and wrap modes set to
     * clamp.
     *
     * @param rs
     * 		Context to which the sampler will belong.
     * @return Sampler
     */
    public static android.renderscript.Sampler CLAMP_NEAREST(android.renderscript.RenderScript rs) {
        if (rs.mSampler_CLAMP_NEAREST == null) {
            synchronized(rs) {
                if (rs.mSampler_CLAMP_NEAREST == null) {
                    android.renderscript.Sampler.Builder b = new android.renderscript.Sampler.Builder(rs);
                    b.setMinification(android.renderscript.Sampler.Value.NEAREST);
                    b.setMagnification(android.renderscript.Sampler.Value.NEAREST);
                    b.setWrapS(android.renderscript.Sampler.Value.CLAMP);
                    b.setWrapT(android.renderscript.Sampler.Value.CLAMP);
                    rs.mSampler_CLAMP_NEAREST = b.create();
                }
            }
        }
        return rs.mSampler_CLAMP_NEAREST;
    }

    /**
     * Retrieve a sampler with min and mag set to linear and wrap modes set to
     * clamp.
     *
     * @param rs
     * 		Context to which the sampler will belong.
     * @return Sampler
     */
    public static android.renderscript.Sampler CLAMP_LINEAR(android.renderscript.RenderScript rs) {
        if (rs.mSampler_CLAMP_LINEAR == null) {
            synchronized(rs) {
                if (rs.mSampler_CLAMP_LINEAR == null) {
                    android.renderscript.Sampler.Builder b = new android.renderscript.Sampler.Builder(rs);
                    b.setMinification(android.renderscript.Sampler.Value.LINEAR);
                    b.setMagnification(android.renderscript.Sampler.Value.LINEAR);
                    b.setWrapS(android.renderscript.Sampler.Value.CLAMP);
                    b.setWrapT(android.renderscript.Sampler.Value.CLAMP);
                    rs.mSampler_CLAMP_LINEAR = b.create();
                }
            }
        }
        return rs.mSampler_CLAMP_LINEAR;
    }

    /**
     * Retrieve a sampler with mag set to linear, min linear mipmap linear, and
     * wrap modes set to clamp.
     *
     * @param rs
     * 		Context to which the sampler will belong.
     * @return Sampler
     */
    public static android.renderscript.Sampler CLAMP_LINEAR_MIP_LINEAR(android.renderscript.RenderScript rs) {
        if (rs.mSampler_CLAMP_LINEAR_MIP_LINEAR == null) {
            synchronized(rs) {
                if (rs.mSampler_CLAMP_LINEAR_MIP_LINEAR == null) {
                    android.renderscript.Sampler.Builder b = new android.renderscript.Sampler.Builder(rs);
                    b.setMinification(android.renderscript.Sampler.Value.LINEAR_MIP_LINEAR);
                    b.setMagnification(android.renderscript.Sampler.Value.LINEAR);
                    b.setWrapS(android.renderscript.Sampler.Value.CLAMP);
                    b.setWrapT(android.renderscript.Sampler.Value.CLAMP);
                    rs.mSampler_CLAMP_LINEAR_MIP_LINEAR = b.create();
                }
            }
        }
        return rs.mSampler_CLAMP_LINEAR_MIP_LINEAR;
    }

    /**
     * Retrieve a sampler with min and mag set to nearest and wrap modes set to
     * wrap.
     *
     * @param rs
     * 		Context to which the sampler will belong.
     * @return Sampler
     */
    public static android.renderscript.Sampler WRAP_NEAREST(android.renderscript.RenderScript rs) {
        if (rs.mSampler_WRAP_NEAREST == null) {
            synchronized(rs) {
                if (rs.mSampler_WRAP_NEAREST == null) {
                    android.renderscript.Sampler.Builder b = new android.renderscript.Sampler.Builder(rs);
                    b.setMinification(android.renderscript.Sampler.Value.NEAREST);
                    b.setMagnification(android.renderscript.Sampler.Value.NEAREST);
                    b.setWrapS(android.renderscript.Sampler.Value.WRAP);
                    b.setWrapT(android.renderscript.Sampler.Value.WRAP);
                    rs.mSampler_WRAP_NEAREST = b.create();
                }
            }
        }
        return rs.mSampler_WRAP_NEAREST;
    }

    /**
     * Retrieve a sampler with min and mag set to linear and wrap modes set to
     * wrap.
     *
     * @param rs
     * 		Context to which the sampler will belong.
     * @return Sampler
     */
    public static android.renderscript.Sampler WRAP_LINEAR(android.renderscript.RenderScript rs) {
        if (rs.mSampler_WRAP_LINEAR == null) {
            synchronized(rs) {
                if (rs.mSampler_WRAP_LINEAR == null) {
                    android.renderscript.Sampler.Builder b = new android.renderscript.Sampler.Builder(rs);
                    b.setMinification(android.renderscript.Sampler.Value.LINEAR);
                    b.setMagnification(android.renderscript.Sampler.Value.LINEAR);
                    b.setWrapS(android.renderscript.Sampler.Value.WRAP);
                    b.setWrapT(android.renderscript.Sampler.Value.WRAP);
                    rs.mSampler_WRAP_LINEAR = b.create();
                }
            }
        }
        return rs.mSampler_WRAP_LINEAR;
    }

    /**
     * Retrieve a sampler with mag set to linear, min linear mipmap linear, and
     * wrap modes set to wrap.
     *
     * @param rs
     * 		Context to which the sampler will belong.
     * @return Sampler
     */
    public static android.renderscript.Sampler WRAP_LINEAR_MIP_LINEAR(android.renderscript.RenderScript rs) {
        if (rs.mSampler_WRAP_LINEAR_MIP_LINEAR == null) {
            synchronized(rs) {
                if (rs.mSampler_WRAP_LINEAR_MIP_LINEAR == null) {
                    android.renderscript.Sampler.Builder b = new android.renderscript.Sampler.Builder(rs);
                    b.setMinification(android.renderscript.Sampler.Value.LINEAR_MIP_LINEAR);
                    b.setMagnification(android.renderscript.Sampler.Value.LINEAR);
                    b.setWrapS(android.renderscript.Sampler.Value.WRAP);
                    b.setWrapT(android.renderscript.Sampler.Value.WRAP);
                    rs.mSampler_WRAP_LINEAR_MIP_LINEAR = b.create();
                }
            }
        }
        return rs.mSampler_WRAP_LINEAR_MIP_LINEAR;
    }

    /**
     * Retrieve a sampler with min and mag set to nearest and wrap modes set to
     * mirrored repeat.
     *
     * @param rs
     * 		Context to which the sampler will belong.
     * @return Sampler
     */
    public static android.renderscript.Sampler MIRRORED_REPEAT_NEAREST(android.renderscript.RenderScript rs) {
        if (rs.mSampler_MIRRORED_REPEAT_NEAREST == null) {
            synchronized(rs) {
                if (rs.mSampler_MIRRORED_REPEAT_NEAREST == null) {
                    android.renderscript.Sampler.Builder b = new android.renderscript.Sampler.Builder(rs);
                    b.setMinification(android.renderscript.Sampler.Value.NEAREST);
                    b.setMagnification(android.renderscript.Sampler.Value.NEAREST);
                    b.setWrapS(android.renderscript.Sampler.Value.MIRRORED_REPEAT);
                    b.setWrapT(android.renderscript.Sampler.Value.MIRRORED_REPEAT);
                    rs.mSampler_MIRRORED_REPEAT_NEAREST = b.create();
                }
            }
        }
        return rs.mSampler_MIRRORED_REPEAT_NEAREST;
    }

    /**
     * Retrieve a sampler with min and mag set to linear and wrap modes set to
     * mirrored repeat.
     *
     * @param rs
     * 		Context to which the sampler will belong.
     * @return Sampler
     */
    public static android.renderscript.Sampler MIRRORED_REPEAT_LINEAR(android.renderscript.RenderScript rs) {
        if (rs.mSampler_MIRRORED_REPEAT_LINEAR == null) {
            synchronized(rs) {
                if (rs.mSampler_MIRRORED_REPEAT_LINEAR == null) {
                    android.renderscript.Sampler.Builder b = new android.renderscript.Sampler.Builder(rs);
                    b.setMinification(android.renderscript.Sampler.Value.LINEAR);
                    b.setMagnification(android.renderscript.Sampler.Value.LINEAR);
                    b.setWrapS(android.renderscript.Sampler.Value.MIRRORED_REPEAT);
                    b.setWrapT(android.renderscript.Sampler.Value.MIRRORED_REPEAT);
                    rs.mSampler_MIRRORED_REPEAT_LINEAR = b.create();
                }
            }
        }
        return rs.mSampler_MIRRORED_REPEAT_LINEAR;
    }

    /**
     * Retrieve a sampler with min and mag set to linear and wrap modes set to
     * mirrored repeat.
     *
     * @param rs
     * 		Context to which the sampler will belong.
     * @return Sampler
     */
    public static android.renderscript.Sampler MIRRORED_REPEAT_LINEAR_MIP_LINEAR(android.renderscript.RenderScript rs) {
        if (rs.mSampler_MIRRORED_REPEAT_LINEAR_MIP_LINEAR == null) {
            synchronized(rs) {
                if (rs.mSampler_MIRRORED_REPEAT_LINEAR_MIP_LINEAR == null) {
                    android.renderscript.Sampler.Builder b = new android.renderscript.Sampler.Builder(rs);
                    b.setMinification(android.renderscript.Sampler.Value.LINEAR_MIP_LINEAR);
                    b.setMagnification(android.renderscript.Sampler.Value.LINEAR);
                    b.setWrapS(android.renderscript.Sampler.Value.MIRRORED_REPEAT);
                    b.setWrapT(android.renderscript.Sampler.Value.MIRRORED_REPEAT);
                    rs.mSampler_MIRRORED_REPEAT_LINEAR_MIP_LINEAR = b.create();
                }
            }
        }
        return rs.mSampler_MIRRORED_REPEAT_LINEAR_MIP_LINEAR;
    }

    /**
     * Builder for creating non-standard samplers.  This is only necessary if
     * a Sampler with different min and mag modes is desired.
     */
    public static class Builder {
        android.renderscript.RenderScript mRS;

        android.renderscript.Sampler.Value mMin;

        android.renderscript.Sampler.Value mMag;

        android.renderscript.Sampler.Value mWrapS;

        android.renderscript.Sampler.Value mWrapT;

        android.renderscript.Sampler.Value mWrapR;

        float mAniso;

        public Builder(android.renderscript.RenderScript rs) {
            mRS = rs;
            mMin = android.renderscript.Sampler.Value.NEAREST;
            mMag = android.renderscript.Sampler.Value.NEAREST;
            mWrapS = android.renderscript.Sampler.Value.WRAP;
            mWrapT = android.renderscript.Sampler.Value.WRAP;
            mWrapR = android.renderscript.Sampler.Value.WRAP;
            mAniso = 1.0F;
        }

        public void setMinification(android.renderscript.Sampler.Value v) {
            if ((((v == android.renderscript.Sampler.Value.NEAREST) || (v == android.renderscript.Sampler.Value.LINEAR)) || (v == android.renderscript.Sampler.Value.LINEAR_MIP_LINEAR)) || (v == android.renderscript.Sampler.Value.LINEAR_MIP_NEAREST)) {
                mMin = v;
            } else {
                throw new java.lang.IllegalArgumentException("Invalid value");
            }
        }

        public void setMagnification(android.renderscript.Sampler.Value v) {
            if ((v == android.renderscript.Sampler.Value.NEAREST) || (v == android.renderscript.Sampler.Value.LINEAR)) {
                mMag = v;
            } else {
                throw new java.lang.IllegalArgumentException("Invalid value");
            }
        }

        public void setWrapS(android.renderscript.Sampler.Value v) {
            if (((v == android.renderscript.Sampler.Value.WRAP) || (v == android.renderscript.Sampler.Value.CLAMP)) || (v == android.renderscript.Sampler.Value.MIRRORED_REPEAT)) {
                mWrapS = v;
            } else {
                throw new java.lang.IllegalArgumentException("Invalid value");
            }
        }

        public void setWrapT(android.renderscript.Sampler.Value v) {
            if (((v == android.renderscript.Sampler.Value.WRAP) || (v == android.renderscript.Sampler.Value.CLAMP)) || (v == android.renderscript.Sampler.Value.MIRRORED_REPEAT)) {
                mWrapT = v;
            } else {
                throw new java.lang.IllegalArgumentException("Invalid value");
            }
        }

        public void setAnisotropy(float v) {
            if (v >= 0.0F) {
                mAniso = v;
            } else {
                throw new java.lang.IllegalArgumentException("Invalid value");
            }
        }

        public android.renderscript.Sampler create() {
            mRS.validate();
            long id = mRS.nSamplerCreate(mMag.mID, mMin.mID, mWrapS.mID, mWrapT.mID, mWrapR.mID, mAniso);
            android.renderscript.Sampler sampler = new android.renderscript.Sampler(id, mRS);
            sampler.mMin = mMin;
            sampler.mMag = mMag;
            sampler.mWrapS = mWrapS;
            sampler.mWrapT = mWrapT;
            sampler.mWrapR = mWrapR;
            sampler.mAniso = mAniso;
            return sampler;
        }
    }
}

