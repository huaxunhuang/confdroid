/**
 * Copyright 2019 The Android Open Source Project
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
package android.graphics;


/**
 * Shader that calculates pixel output with a program (fragment shader) running on a GPU.
 *
 * @unknown 
 */
public class RuntimeShader extends android.graphics.Shader {
    private static class NoImagePreloadHolder {
        public static final libcore.util.NativeAllocationRegistry sRegistry = libcore.util.NativeAllocationRegistry.createMalloced(android.graphics.RuntimeShader.class.getClassLoader(), android.graphics.RuntimeShader.nativeGetFinalizer());
    }

    private byte[] mUniforms;

    private boolean mIsOpaque;

    /**
     * Current native shader factory instance.
     */
    private long mNativeInstanceRuntimeShaderFactory;

    /**
     * Creates a new RuntimeShader.
     *
     * @param sksl
     * 		The text of SKSL program to run on the GPU.
     * @param uniforms
     * 		Array of parameters passed by the SKSL shader. Array size depends
     * 		on number of uniforms declared by sksl.
     * @param isOpaque
     * 		True if all pixels have alpha 1.0f.
     */
    public RuntimeShader(@android.annotation.NonNull
    java.lang.String sksl, @android.annotation.Nullable
    byte[] uniforms, boolean isOpaque) {
        this(sksl, uniforms, isOpaque, android.graphics.ColorSpace.get(android.graphics.ColorSpace.Named.SRGB));
    }

    private RuntimeShader(@android.annotation.NonNull
    java.lang.String sksl, @android.annotation.Nullable
    byte[] uniforms, boolean isOpaque, android.graphics.ColorSpace colorSpace) {
        super(colorSpace);
        mUniforms = uniforms;
        mIsOpaque = isOpaque;
        mNativeInstanceRuntimeShaderFactory = android.graphics.RuntimeShader.nativeCreateShaderFactory(sksl);
        android.graphics.RuntimeShader.NoImagePreloadHolder.sRegistry.registerNativeAllocation(this, mNativeInstanceRuntimeShaderFactory);
    }

    /**
     * Sets new value for shader parameters.
     *
     * @param uniforms
     * 		Array of parameters passed by the SKSL shader. Array size depends
     * 		on number of uniforms declared by mSksl.
     */
    public void updateUniforms(@android.annotation.Nullable
    byte[] uniforms) {
        mUniforms = uniforms;
        discardNativeInstance();
    }

    @java.lang.Override
    long createNativeInstance(long nativeMatrix) {
        return android.graphics.RuntimeShader.nativeCreate(mNativeInstanceRuntimeShaderFactory, nativeMatrix, mUniforms, colorSpace().getNativeInstance(), mIsOpaque);
    }

    private static native long nativeCreate(long shaderFactory, long matrix, byte[] inputs, long colorSpaceHandle, boolean isOpaque);

    private static native long nativeCreateShaderFactory(java.lang.String sksl);

    private static native long nativeGetFinalizer();
}

