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
 * Forward all interface calls into a handler by posting it as a {@code Runnable}.
 *
 * <p>All calls will return immediately; functions with return values will return a default
 * value of {@code null}, {@code 0}, or {@code false} where that value is legal.</p>
 *
 * <p>Any exceptions thrown on the handler while trying to invoke a method
 * will be re-thrown. Throwing checked exceptions on a handler which doesn't expect any
 * checked exceptions to be thrown will result in "undefined" behavior
 * (although in practice it is usually thrown as normal).</p>
 */
public class HandlerDispatcher<T> implements android.hardware.camera2.dispatch.Dispatchable<T> {
    private static final java.lang.String TAG = "HandlerDispatcher";

    private final android.hardware.camera2.dispatch.Dispatchable<T> mDispatchTarget;

    private final android.os.Handler mHandler;

    /**
     * Create a dispatcher that forwards it's dispatch calls by posting
     * them onto the {@code handler} as a {@code Runnable}.
     *
     * @param dispatchTarget
     * 		the destination whose method calls will be redirected into the handler
     * @param handler
     * 		all calls into {@code dispatchTarget} will be posted onto this handler
     * @param <T>
     * 		the type of the element you want to wrap.
     * @return a dispatcher that will forward it's dispatch calls to a handler
     */
    public HandlerDispatcher(android.hardware.camera2.dispatch.Dispatchable<T> dispatchTarget, android.os.Handler handler) {
        mDispatchTarget = android.hardware.camera2.dispatch.HandlerDispatcher.checkNotNull(dispatchTarget, "dispatchTarget must not be null");
        mHandler = android.hardware.camera2.dispatch.HandlerDispatcher.checkNotNull(handler, "handler must not be null");
    }

    @java.lang.Override
    public java.lang.Object dispatch(final java.lang.reflect.Method method, final java.lang.Object[] args) throws java.lang.Throwable {
        mHandler.post(new java.lang.Runnable() {
            @java.lang.Override
            public void run() {
                try {
                    mDispatchTarget.dispatch(method, args);
                } catch (java.lang.reflect.InvocationTargetException e) {
                    java.lang.Throwable t = e.getTargetException();
                    // Potential UB. Hopefully 't' is a runtime exception.
                    android.hardware.camera2.utils.UncheckedThrow.throwAnyException(t);
                } catch (java.lang.IllegalAccessException e) {
                    // Impossible
                    android.util.Log.wtf(android.hardware.camera2.dispatch.HandlerDispatcher.TAG, "IllegalAccessException while invoking " + method, e);
                } catch (java.lang.IllegalArgumentException e) {
                    // Impossible
                    android.util.Log.wtf(android.hardware.camera2.dispatch.HandlerDispatcher.TAG, "IllegalArgumentException while invoking " + method, e);
                } catch (java.lang.Throwable e) {
                    android.hardware.camera2.utils.UncheckedThrow.throwAnyException(e);
                }
            }
        });
        // TODO handle primitive return values that would avoid NPE if unboxed
        return null;
    }
}

