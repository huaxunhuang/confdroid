/**
 * Copyright (C) 2015 The Android Open Source Project
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
package android.databinding.tool.reflection;


public abstract class ModelMethod {
    public abstract android.databinding.tool.reflection.ModelClass getDeclaringClass();

    public abstract android.databinding.tool.reflection.ModelClass[] getParameterTypes();

    public abstract java.lang.String getName();

    public abstract android.databinding.tool.reflection.ModelClass getReturnType(java.util.List<android.databinding.tool.reflection.ModelClass> args);

    public abstract boolean isVoid();

    public abstract boolean isPublic();

    public abstract boolean isStatic();

    public abstract boolean isAbstract();

    /**
     *
     *
     * @return whether or not this method has been given the {@link Bindable} annotation.
     */
    public abstract boolean isBindable();

    /**
     * Since when this method is available. Important for Binding expressions so that we don't
     * call non-existing APIs when setting UI.
     *
     * @return The SDK_INT where this method was added. If it is not a framework method, should
    return 1.
     */
    public abstract int getMinApi();

    /**
     * Returns the JNI description of the method which can be used to lookup it in SDK.
     *
     * @see TypeUtil
     */
    public abstract java.lang.String getJniDescription();

    /**
     *
     *
     * @return true if the final parameter is a varargs parameter.
     */
    public abstract boolean isVarArgs();

    /**
     *
     *
     * @param args
     * 		The arguments to the method
     * @return Whether the arguments would be accepted as parameters to this method.
     */
    public boolean acceptsArguments(java.util.List<android.databinding.tool.reflection.ModelClass> args) {
        boolean isVarArgs = isVarArgs();
        android.databinding.tool.reflection.ModelClass[] parameterTypes = getParameterTypes();
        if (((!isVarArgs) && (args.size() != parameterTypes.length)) || (isVarArgs && (args.size() < (parameterTypes.length - 1)))) {
            return false;// The wrong number of parameters

        }
        boolean parametersMatch = true;
        for (int i = 0; i < args.size(); i++) {
            android.databinding.tool.reflection.ModelClass parameterType = getParameter(i, parameterTypes);
            android.databinding.tool.reflection.ModelClass arg = args.get(i);
            if ((!parameterType.isAssignableFrom(arg)) && (!android.databinding.tool.reflection.ModelMethod.isImplicitConversion(arg, parameterType))) {
                parametersMatch = false;
                break;
            }
        }
        return parametersMatch;
    }

    public boolean isBetterArgMatchThan(android.databinding.tool.reflection.ModelMethod other, java.util.List<android.databinding.tool.reflection.ModelClass> args) {
        final android.databinding.tool.reflection.ModelClass[] parameterTypes = getParameterTypes();
        final android.databinding.tool.reflection.ModelClass[] otherParameterTypes = other.getParameterTypes();
        for (int i = 0; i < args.size(); i++) {
            final android.databinding.tool.reflection.ModelClass arg = args.get(i);
            final android.databinding.tool.reflection.ModelClass thisParameter = getParameter(i, parameterTypes);
            final android.databinding.tool.reflection.ModelClass thatParameter = other.getParameter(i, otherParameterTypes);
            final int diff = android.databinding.tool.reflection.ModelMethod.compareParameter(arg, thisParameter, thatParameter);
            if (diff != 0) {
                return diff < 0;
            }
        }
        return false;
    }

    private android.databinding.tool.reflection.ModelClass getParameter(int index, android.databinding.tool.reflection.ModelClass[] parameterTypes) {
        int normalParamCount = (isVarArgs()) ? parameterTypes.length - 1 : parameterTypes.length;
        if (index < normalParamCount) {
            return parameterTypes[index];
        } else {
            return parameterTypes[parameterTypes.length - 1].getComponentType();
        }
    }

    private static int compareParameter(android.databinding.tool.reflection.ModelClass arg, android.databinding.tool.reflection.ModelClass thisParameter, android.databinding.tool.reflection.ModelClass thatParameter) {
        if (thatParameter.equals(arg)) {
            return 1;
        } else
            if (thisParameter.equals(arg)) {
                return -1;
            } else
                if (android.databinding.tool.reflection.ModelMethod.isBoxingConversion(thatParameter, arg)) {
                    return 1;
                } else
                    if (android.databinding.tool.reflection.ModelMethod.isBoxingConversion(thisParameter, arg)) {
                        // Boxing/unboxing is second best
                        return -1;
                    } else {
                        int argConversionLevel = android.databinding.tool.reflection.ModelMethod.getImplicitConversionLevel(arg);
                        if (argConversionLevel != (-1)) {
                            int oldConversionLevel = android.databinding.tool.reflection.ModelMethod.getImplicitConversionLevel(thatParameter);
                            int newConversionLevel = android.databinding.tool.reflection.ModelMethod.getImplicitConversionLevel(thisParameter);
                            if ((newConversionLevel != (-1)) && ((oldConversionLevel == (-1)) || (newConversionLevel < oldConversionLevel))) {
                                return -1;
                            } else
                                if (oldConversionLevel != (-1)) {
                                    return 1;
                                }

                        }
                        // Look for more exact match
                        if (thatParameter.isAssignableFrom(thisParameter)) {
                            return -1;
                        }
                    }



        return 0;// no difference

    }

    public static boolean isBoxingConversion(android.databinding.tool.reflection.ModelClass class1, android.databinding.tool.reflection.ModelClass class2) {
        if (class1.isPrimitive() != class2.isPrimitive()) {
            return class1.box().equals(class2.box());
        } else {
            return false;
        }
    }

    public static int getImplicitConversionLevel(android.databinding.tool.reflection.ModelClass primitive) {
        if (primitive == null) {
            return -1;
        } else
            if (primitive.isByte()) {
                return 0;
            } else
                if (primitive.isChar()) {
                    return 1;
                } else
                    if (primitive.isShort()) {
                        return 2;
                    } else
                        if (primitive.isInt()) {
                            return 3;
                        } else
                            if (primitive.isLong()) {
                                return 4;
                            } else
                                if (primitive.isFloat()) {
                                    return 5;
                                } else
                                    if (primitive.isDouble()) {
                                        return 6;
                                    } else {
                                        return -1;
                                    }







    }

    public static boolean isImplicitConversion(android.databinding.tool.reflection.ModelClass from, android.databinding.tool.reflection.ModelClass to) {
        if ((((from != null) && (to != null)) && from.isPrimitive()) && to.isPrimitive()) {
            if ((from.isBoolean() || to.isBoolean()) || to.isChar()) {
                return false;
            }
            int fromConversionLevel = android.databinding.tool.reflection.ModelMethod.getImplicitConversionLevel(from);
            int toConversionLevel = android.databinding.tool.reflection.ModelMethod.getImplicitConversionLevel(to);
            return fromConversionLevel < toConversionLevel;
        } else {
            return false;
        }
    }
}

