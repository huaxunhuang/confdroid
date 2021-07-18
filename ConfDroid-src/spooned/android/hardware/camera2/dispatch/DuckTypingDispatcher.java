/**
 * Copyright (C) 2014 The Android Open Source Project
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
package android.hardware.camera2.dispatch;


/**
 * Duck typing dispatcher; converts dispatch methods calls from one class to another by
 * looking up equivalently methods at runtime by name.
 *
 * <p>For example, if two types have identical method names and arguments, but
 * are not subclasses/subinterfaces of each other, this dispatcher will allow calls to be
 * made from one type to the other.</p>
 *
 * @param <TFrom>
 * 		source dispatch type, whose methods with {@link #dispatch} will be called
 * @param <T>
 * 		destination dispatch type, methods will be converted to the class of {@code T}
 */
public class DuckTypingDispatcher<TFrom, T> implements android.hardware.camera2.dispatch.Dispatchable<TFrom> {
    private final android.hardware.camera2.dispatch.MethodNameInvoker<T> mDuck;

    /**
     * Create a new duck typing dispatcher.
     *
     * @param target
     * 		destination dispatch type, methods will be redirected to this dispatcher
     * @param targetClass
     * 		destination dispatch class, methods will be converted to this class's
     */
    public DuckTypingDispatcher(android.hardware.camera2.dispatch.Dispatchable<T> target, java.lang.Class<T> targetClass) {
        android.hardware.camera2.dispatch.DuckTypingDispatcher.checkNotNull(targetClass, "targetClass must not be null");
        android.hardware.camera2.dispatch.DuckTypingDispatcher.checkNotNull(target, "target must not be null");
        mDuck = new android.hardware.camera2.dispatch.MethodNameInvoker<T>(target, targetClass);
    }

    @java.lang.Override
    public java.lang.Object dispatch(java.lang.reflect.Method method, java.lang.Object[] args) {
        return mDuck.invoke(method.getName(), args);
    }
}

