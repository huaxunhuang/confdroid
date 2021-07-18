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


public class InvokeDispatcher<T> implements android.hardware.camera2.dispatch.Dispatchable<T> {
    private static final java.lang.String TAG = "InvocationSink";

    private final T mTarget;

    public InvokeDispatcher(T target) {
        mTarget = android.hardware.camera2.dispatch.InvokeDispatcher.checkNotNull(target, "target must not be null");
    }

    @java.lang.Override
    public java.lang.Object dispatch(java.lang.reflect.Method method, java.lang.Object[] args) {
        try {
            return method.invoke(mTarget, args);
        } catch (java.lang.reflect.InvocationTargetException e) {
            java.lang.Throwable t = e.getTargetException();
            // Potential UB. Hopefully 't' is a runtime exception.
            android.hardware.camera2.utils.UncheckedThrow.throwAnyException(t);
        } catch (java.lang.IllegalAccessException e) {
            // Impossible
            android.util.Log.wtf(android.hardware.camera2.dispatch.InvokeDispatcher.TAG, "IllegalAccessException while invoking " + method, e);
        } catch (java.lang.IllegalArgumentException e) {
            // Impossible
            android.util.Log.wtf(android.hardware.camera2.dispatch.InvokeDispatcher.TAG, "IllegalArgumentException while invoking " + method, e);
        }
        // unreachable
        return null;
    }
}

