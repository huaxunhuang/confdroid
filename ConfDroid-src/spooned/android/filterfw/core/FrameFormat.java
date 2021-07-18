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
package android.filterfw.core;


/**
 *
 *
 * @unknown 
 */
public class FrameFormat {
    public static final int TYPE_UNSPECIFIED = 0;

    public static final int TYPE_BIT = 1;

    public static final int TYPE_BYTE = 2;

    public static final int TYPE_INT16 = 3;

    public static final int TYPE_INT32 = 4;

    public static final int TYPE_FLOAT = 5;

    public static final int TYPE_DOUBLE = 6;

    public static final int TYPE_POINTER = 7;

    public static final int TYPE_OBJECT = 8;

    public static final int TARGET_UNSPECIFIED = 0;

    public static final int TARGET_SIMPLE = 1;

    public static final int TARGET_NATIVE = 2;

    public static final int TARGET_GPU = 3;

    public static final int TARGET_VERTEXBUFFER = 4;

    public static final int TARGET_RS = 5;

    public static final int SIZE_UNSPECIFIED = 0;

    // TODO: When convenience formats are used, consider changing this to 0 and have the convenience
    // intializers use a proper BPS.
    public static final int BYTES_PER_SAMPLE_UNSPECIFIED = 1;

    protected static final int SIZE_UNKNOWN = -1;

    protected int mBaseType = android.filterfw.core.FrameFormat.TYPE_UNSPECIFIED;

    protected int mBytesPerSample = 1;

    protected int mSize = android.filterfw.core.FrameFormat.SIZE_UNKNOWN;

    protected int mTarget = android.filterfw.core.FrameFormat.TARGET_UNSPECIFIED;

    protected int[] mDimensions;

    protected android.filterfw.core.KeyValueMap mMetaData;

    protected java.lang.Class mObjectClass;

    protected FrameFormat() {
    }

    public FrameFormat(int baseType, int target) {
        mBaseType = baseType;
        mTarget = target;
        initDefaults();
    }

    public static android.filterfw.core.FrameFormat unspecified() {
        return new android.filterfw.core.FrameFormat(android.filterfw.core.FrameFormat.TYPE_UNSPECIFIED, android.filterfw.core.FrameFormat.TARGET_UNSPECIFIED);
    }

    public int getBaseType() {
        return mBaseType;
    }

    public boolean isBinaryDataType() {
        return (mBaseType >= android.filterfw.core.FrameFormat.TYPE_BIT) && (mBaseType <= android.filterfw.core.FrameFormat.TYPE_DOUBLE);
    }

    public int getBytesPerSample() {
        return mBytesPerSample;
    }

    public int getValuesPerSample() {
        return mBytesPerSample / android.filterfw.core.FrameFormat.bytesPerSampleOf(mBaseType);
    }

    public int getTarget() {
        return mTarget;
    }

    public int[] getDimensions() {
        return mDimensions;
    }

    public int getDimension(int i) {
        return mDimensions[i];
    }

    public int getDimensionCount() {
        return mDimensions == null ? 0 : mDimensions.length;
    }

    public boolean hasMetaKey(java.lang.String key) {
        return mMetaData != null ? mMetaData.containsKey(key) : false;
    }

    public boolean hasMetaKey(java.lang.String key, java.lang.Class expectedClass) {
        if ((mMetaData != null) && mMetaData.containsKey(key)) {
            if (!expectedClass.isAssignableFrom(mMetaData.get(key).getClass())) {
                throw new java.lang.RuntimeException(((((("FrameFormat meta-key '" + key) + "' is of type ") + mMetaData.get(key).getClass()) + " but expected to be of type ") + expectedClass) + "!");
            }
            return true;
        }
        return false;
    }

    public java.lang.Object getMetaValue(java.lang.String key) {
        return mMetaData != null ? mMetaData.get(key) : null;
    }

    public int getNumberOfDimensions() {
        return mDimensions != null ? mDimensions.length : 0;
    }

    public int getLength() {
        return (mDimensions != null) && (mDimensions.length >= 1) ? mDimensions[0] : -1;
    }

    public int getWidth() {
        return getLength();
    }

    public int getHeight() {
        return (mDimensions != null) && (mDimensions.length >= 2) ? mDimensions[1] : -1;
    }

    public int getDepth() {
        return (mDimensions != null) && (mDimensions.length >= 3) ? mDimensions[2] : -1;
    }

    public int getSize() {
        if (mSize == android.filterfw.core.FrameFormat.SIZE_UNKNOWN)
            mSize = calcSize(mDimensions);

        return mSize;
    }

    public java.lang.Class getObjectClass() {
        return mObjectClass;
    }

    public android.filterfw.core.MutableFrameFormat mutableCopy() {
        android.filterfw.core.MutableFrameFormat result = new android.filterfw.core.MutableFrameFormat();
        result.setBaseType(getBaseType());
        result.setTarget(getTarget());
        result.setBytesPerSample(getBytesPerSample());
        result.setDimensions(getDimensions());
        result.setObjectClass(getObjectClass());
        result.mMetaData = (mMetaData == null) ? null : ((android.filterfw.core.KeyValueMap) (mMetaData.clone()));
        return result;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof android.filterfw.core.FrameFormat)) {
            return false;
        }
        android.filterfw.core.FrameFormat format = ((android.filterfw.core.FrameFormat) (object));
        return ((((format.mBaseType == mBaseType) && (format.mTarget == mTarget)) && (format.mBytesPerSample == mBytesPerSample)) && java.util.Arrays.equals(format.mDimensions, mDimensions)) && format.mMetaData.equals(mMetaData);
    }

    @java.lang.Override
    public int hashCode() {
        return ((4211 ^ mBaseType) ^ mBytesPerSample) ^ getSize();
    }

    public boolean isCompatibleWith(android.filterfw.core.FrameFormat specification) {
        // Check base type
        if ((specification.getBaseType() != android.filterfw.core.FrameFormat.TYPE_UNSPECIFIED) && (getBaseType() != specification.getBaseType())) {
            return false;
        }
        // Check target
        if ((specification.getTarget() != android.filterfw.core.FrameFormat.TARGET_UNSPECIFIED) && (getTarget() != specification.getTarget())) {
            return false;
        }
        // Check bytes per sample
        if ((specification.getBytesPerSample() != android.filterfw.core.FrameFormat.BYTES_PER_SAMPLE_UNSPECIFIED) && (getBytesPerSample() != specification.getBytesPerSample())) {
            return false;
        }
        // Check number of dimensions
        if ((specification.getDimensionCount() > 0) && (getDimensionCount() != specification.getDimensionCount())) {
            return false;
        }
        // Check dimensions
        for (int i = 0; i < specification.getDimensionCount(); ++i) {
            int specDim = specification.getDimension(i);
            if ((specDim != android.filterfw.core.FrameFormat.SIZE_UNSPECIFIED) && (getDimension(i) != specDim)) {
                return false;
            }
        }
        // Check class
        if (specification.getObjectClass() != null) {
            if ((getObjectClass() == null) || (!specification.getObjectClass().isAssignableFrom(getObjectClass()))) {
                return false;
            }
        }
        // Check meta-data
        if (specification.mMetaData != null) {
            for (java.lang.String specKey : specification.mMetaData.keySet()) {
                if (((mMetaData == null) || (!mMetaData.containsKey(specKey))) || (!mMetaData.get(specKey).equals(specification.mMetaData.get(specKey)))) {
                    return false;
                }
            }
        }
        // Passed all the tests
        return true;
    }

    public boolean mayBeCompatibleWith(android.filterfw.core.FrameFormat specification) {
        // Check base type
        if (((specification.getBaseType() != android.filterfw.core.FrameFormat.TYPE_UNSPECIFIED) && (getBaseType() != android.filterfw.core.FrameFormat.TYPE_UNSPECIFIED)) && (getBaseType() != specification.getBaseType())) {
            return false;
        }
        // Check target
        if (((specification.getTarget() != android.filterfw.core.FrameFormat.TARGET_UNSPECIFIED) && (getTarget() != android.filterfw.core.FrameFormat.TARGET_UNSPECIFIED)) && (getTarget() != specification.getTarget())) {
            return false;
        }
        // Check bytes per sample
        if (((specification.getBytesPerSample() != android.filterfw.core.FrameFormat.BYTES_PER_SAMPLE_UNSPECIFIED) && (getBytesPerSample() != android.filterfw.core.FrameFormat.BYTES_PER_SAMPLE_UNSPECIFIED)) && (getBytesPerSample() != specification.getBytesPerSample())) {
            return false;
        }
        // Check number of dimensions
        if (((specification.getDimensionCount() > 0) && (getDimensionCount() > 0)) && (getDimensionCount() != specification.getDimensionCount())) {
            return false;
        }
        // Check dimensions
        for (int i = 0; i < specification.getDimensionCount(); ++i) {
            int specDim = specification.getDimension(i);
            if (((specDim != android.filterfw.core.FrameFormat.SIZE_UNSPECIFIED) && (getDimension(i) != android.filterfw.core.FrameFormat.SIZE_UNSPECIFIED)) && (getDimension(i) != specDim)) {
                return false;
            }
        }
        // Check class
        if ((specification.getObjectClass() != null) && (getObjectClass() != null)) {
            if (!specification.getObjectClass().isAssignableFrom(getObjectClass())) {
                return false;
            }
        }
        // Check meta-data
        if ((specification.mMetaData != null) && (mMetaData != null)) {
            for (java.lang.String specKey : specification.mMetaData.keySet()) {
                if (mMetaData.containsKey(specKey) && (!mMetaData.get(specKey).equals(specification.mMetaData.get(specKey)))) {
                    return false;
                }
            }
        }
        // Passed all the tests
        return true;
    }

    public static int bytesPerSampleOf(int baseType) {
        // Defaults based on base-type
        switch (baseType) {
            case android.filterfw.core.FrameFormat.TYPE_BIT :
            case android.filterfw.core.FrameFormat.TYPE_BYTE :
                return 1;
            case android.filterfw.core.FrameFormat.TYPE_INT16 :
                return 2;
            case android.filterfw.core.FrameFormat.TYPE_INT32 :
            case android.filterfw.core.FrameFormat.TYPE_FLOAT :
            case android.filterfw.core.FrameFormat.TYPE_POINTER :
                return 4;
            case android.filterfw.core.FrameFormat.TYPE_DOUBLE :
                return 8;
            default :
                return 1;
        }
    }

    public static java.lang.String dimensionsToString(int[] dimensions) {
        java.lang.StringBuffer buffer = new java.lang.StringBuffer();
        if (dimensions != null) {
            int n = dimensions.length;
            for (int i = 0; i < n; ++i) {
                if (dimensions[i] == android.filterfw.core.FrameFormat.SIZE_UNSPECIFIED) {
                    buffer.append("[]");
                } else {
                    buffer.append(("[" + java.lang.String.valueOf(dimensions[i])) + "]");
                }
            }
        }
        return buffer.toString();
    }

    public static java.lang.String baseTypeToString(int baseType) {
        switch (baseType) {
            case android.filterfw.core.FrameFormat.TYPE_UNSPECIFIED :
                return "unspecified";
            case android.filterfw.core.FrameFormat.TYPE_BIT :
                return "bit";
            case android.filterfw.core.FrameFormat.TYPE_BYTE :
                return "byte";
            case android.filterfw.core.FrameFormat.TYPE_INT16 :
                return "int";
            case android.filterfw.core.FrameFormat.TYPE_INT32 :
                return "int";
            case android.filterfw.core.FrameFormat.TYPE_FLOAT :
                return "float";
            case android.filterfw.core.FrameFormat.TYPE_DOUBLE :
                return "double";
            case android.filterfw.core.FrameFormat.TYPE_POINTER :
                return "pointer";
            case android.filterfw.core.FrameFormat.TYPE_OBJECT :
                return "object";
            default :
                return "unknown";
        }
    }

    public static java.lang.String targetToString(int target) {
        switch (target) {
            case android.filterfw.core.FrameFormat.TARGET_UNSPECIFIED :
                return "unspecified";
            case android.filterfw.core.FrameFormat.TARGET_SIMPLE :
                return "simple";
            case android.filterfw.core.FrameFormat.TARGET_NATIVE :
                return "native";
            case android.filterfw.core.FrameFormat.TARGET_GPU :
                return "gpu";
            case android.filterfw.core.FrameFormat.TARGET_VERTEXBUFFER :
                return "vbo";
            case android.filterfw.core.FrameFormat.TARGET_RS :
                return "renderscript";
            default :
                return "unknown";
        }
    }

    public static java.lang.String metaDataToString(android.filterfw.core.KeyValueMap metaData) {
        if (metaData == null) {
            return "";
        } else {
            java.lang.StringBuffer buffer = new java.lang.StringBuffer();
            buffer.append("{ ");
            for (java.util.Map.Entry<java.lang.String, java.lang.Object> entry : metaData.entrySet()) {
                buffer.append(((entry.getKey() + ": ") + entry.getValue()) + " ");
            }
            buffer.append("}");
            return buffer.toString();
        }
    }

    public static int readTargetString(java.lang.String targetString) {
        if (targetString.equalsIgnoreCase("CPU") || targetString.equalsIgnoreCase("NATIVE")) {
            return android.filterfw.core.FrameFormat.TARGET_NATIVE;
        } else
            if (targetString.equalsIgnoreCase("GPU")) {
                return android.filterfw.core.FrameFormat.TARGET_GPU;
            } else
                if (targetString.equalsIgnoreCase("SIMPLE")) {
                    return android.filterfw.core.FrameFormat.TARGET_SIMPLE;
                } else
                    if (targetString.equalsIgnoreCase("VERTEXBUFFER")) {
                        return android.filterfw.core.FrameFormat.TARGET_VERTEXBUFFER;
                    } else
                        if (targetString.equalsIgnoreCase("UNSPECIFIED")) {
                            return android.filterfw.core.FrameFormat.TARGET_UNSPECIFIED;
                        } else {
                            throw new java.lang.RuntimeException(("Unknown target type '" + targetString) + "'!");
                        }




    }

    // TODO: FromString
    public java.lang.String toString() {
        int valuesPerSample = getValuesPerSample();
        java.lang.String sampleCountString = (valuesPerSample == 1) ? "" : java.lang.String.valueOf(valuesPerSample);
        java.lang.String targetString = (mTarget == android.filterfw.core.FrameFormat.TARGET_UNSPECIFIED) ? "" : android.filterfw.core.FrameFormat.targetToString(mTarget) + " ";
        java.lang.String classString = (mObjectClass == null) ? "" : (" class(" + mObjectClass.getSimpleName()) + ") ";
        return ((((targetString + android.filterfw.core.FrameFormat.baseTypeToString(mBaseType)) + sampleCountString) + android.filterfw.core.FrameFormat.dimensionsToString(mDimensions)) + classString) + android.filterfw.core.FrameFormat.metaDataToString(mMetaData);
    }

    private void initDefaults() {
        mBytesPerSample = android.filterfw.core.FrameFormat.bytesPerSampleOf(mBaseType);
    }

    // Core internal methods ///////////////////////////////////////////////////////////////////////
    int calcSize(int[] dimensions) {
        if ((dimensions != null) && (dimensions.length > 0)) {
            int size = getBytesPerSample();
            for (int dim : dimensions) {
                size *= dim;
            }
            return size;
        }
        return 0;
    }

    boolean isReplaceableBy(android.filterfw.core.FrameFormat format) {
        return ((mTarget == format.mTarget) && (getSize() == format.getSize())) && java.util.Arrays.equals(format.mDimensions, mDimensions);
    }
}

