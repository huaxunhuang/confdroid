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
package android.filterfw.format;


/**
 *
 *
 * @unknown 
 */
public class ObjectFormat {
    public static android.filterfw.core.MutableFrameFormat fromClass(java.lang.Class clazz, int count, int target) {
        // Create frame format
        android.filterfw.core.MutableFrameFormat result = new android.filterfw.core.MutableFrameFormat(android.filterfw.core.FrameFormat.TYPE_OBJECT, target);
        result.setObjectClass(android.filterfw.format.ObjectFormat.getBoxedClass(clazz));
        if (count != android.filterfw.core.FrameFormat.SIZE_UNSPECIFIED) {
            result.setDimensions(count);
        }
        result.setBytesPerSample(android.filterfw.format.ObjectFormat.bytesPerSampleForClass(clazz, target));
        return result;
    }

    public static android.filterfw.core.MutableFrameFormat fromClass(java.lang.Class clazz, int target) {
        return android.filterfw.format.ObjectFormat.fromClass(clazz, android.filterfw.core.FrameFormat.SIZE_UNSPECIFIED, target);
    }

    public static android.filterfw.core.MutableFrameFormat fromObject(java.lang.Object object, int target) {
        return object == null ? new android.filterfw.core.MutableFrameFormat(android.filterfw.core.FrameFormat.TYPE_OBJECT, target) : android.filterfw.format.ObjectFormat.fromClass(object.getClass(), android.filterfw.core.FrameFormat.SIZE_UNSPECIFIED, target);
    }

    public static android.filterfw.core.MutableFrameFormat fromObject(java.lang.Object object, int count, int target) {
        return object == null ? new android.filterfw.core.MutableFrameFormat(android.filterfw.core.FrameFormat.TYPE_OBJECT, target) : android.filterfw.format.ObjectFormat.fromClass(object.getClass(), count, target);
    }

    private static int bytesPerSampleForClass(java.lang.Class clazz, int target) {
        // Native targets have objects manifested in a byte buffer. Thus it is important to
        // correctly determine the size of single element here.
        if (target == android.filterfw.core.FrameFormat.TARGET_NATIVE) {
            if (!android.filterfw.core.NativeBuffer.class.isAssignableFrom(clazz)) {
                throw new java.lang.IllegalArgumentException((("Native object-based formats must be of a " + "NativeBuffer subclass! (Received class: ") + clazz) + ").");
            }
            try {
                return ((android.filterfw.core.NativeBuffer) (clazz.newInstance())).getElementSize();
            } catch (java.lang.Exception e) {
                throw new java.lang.RuntimeException(((("Could not determine the size of an element in a " + "native object-based frame of type ") + clazz) + "! Perhaps it is missing a ") + "default constructor?");
            }
        } else {
            return android.filterfw.core.FrameFormat.BYTES_PER_SAMPLE_UNSPECIFIED;
        }
    }

    private static java.lang.Class getBoxedClass(java.lang.Class type) {
        // Check if type is primitive
        if (type.isPrimitive()) {
            // Yes -> box it
            if (type == boolean.class) {
                return java.lang.Boolean.class;
            } else
                if (type == byte.class) {
                    return java.lang.Byte.class;
                } else
                    if (type == char.class) {
                        return java.lang.Character.class;
                    } else
                        if (type == short.class) {
                            return java.lang.Short.class;
                        } else
                            if (type == int.class) {
                                return java.lang.Integer.class;
                            } else
                                if (type == long.class) {
                                    return java.lang.Long.class;
                                } else
                                    if (type == float.class) {
                                        return java.lang.Float.class;
                                    } else
                                        if (type == double.class) {
                                            return java.lang.Double.class;
                                        } else {
                                            throw new java.lang.IllegalArgumentException(("Unknown primitive type: " + type.getSimpleName()) + "!");
                                        }







        } else {
            // No -> return it
            return type;
        }
    }
}

