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
package android.support.v4.view;


public final class ViewPropertyAnimatorCompat {
    private static final java.lang.String TAG = "ViewAnimatorCompat";

    private java.lang.ref.WeakReference<android.view.View> mView;

    java.lang.Runnable mStartAction = null;

    java.lang.Runnable mEndAction = null;

    int mOldLayerType = -1;

    // HACK ALERT! Choosing this id knowing that the framework does not use it anywhere
    // internally and apps should use ids higher than it
    static final int LISTENER_TAG_ID = 0x7e000000;

    ViewPropertyAnimatorCompat(android.view.View view) {
        mView = new java.lang.ref.WeakReference<android.view.View>(view);
    }

    interface ViewPropertyAnimatorCompatImpl {
        public void setDuration(android.support.v4.view.ViewPropertyAnimatorCompat vpa, android.view.View view, long value);

        public long getDuration(android.support.v4.view.ViewPropertyAnimatorCompat vpa, android.view.View view);

        public void setInterpolator(android.support.v4.view.ViewPropertyAnimatorCompat vpa, android.view.View view, android.view.animation.Interpolator value);

        public android.view.animation.Interpolator getInterpolator(android.support.v4.view.ViewPropertyAnimatorCompat vpa, android.view.View view);

        public void setStartDelay(android.support.v4.view.ViewPropertyAnimatorCompat vpa, android.view.View view, long value);

        public long getStartDelay(android.support.v4.view.ViewPropertyAnimatorCompat vpa, android.view.View view);

        public void alpha(android.support.v4.view.ViewPropertyAnimatorCompat vpa, android.view.View view, float value);

        public void alphaBy(android.support.v4.view.ViewPropertyAnimatorCompat vpa, android.view.View view, float value);

        public void rotation(android.support.v4.view.ViewPropertyAnimatorCompat vpa, android.view.View view, float value);

        public void rotationBy(android.support.v4.view.ViewPropertyAnimatorCompat vpa, android.view.View view, float value);

        public void rotationX(android.support.v4.view.ViewPropertyAnimatorCompat vpa, android.view.View view, float value);

        public void rotationXBy(android.support.v4.view.ViewPropertyAnimatorCompat vpa, android.view.View view, float value);

        public void rotationY(android.support.v4.view.ViewPropertyAnimatorCompat vpa, android.view.View view, float value);

        public void rotationYBy(android.support.v4.view.ViewPropertyAnimatorCompat vpa, android.view.View view, float value);

        public void scaleX(android.support.v4.view.ViewPropertyAnimatorCompat vpa, android.view.View view, float value);

        public void scaleXBy(android.support.v4.view.ViewPropertyAnimatorCompat vpa, android.view.View view, float value);

        public void scaleY(android.support.v4.view.ViewPropertyAnimatorCompat vpa, android.view.View view, float value);

        public void scaleYBy(android.support.v4.view.ViewPropertyAnimatorCompat vpa, android.view.View view, float value);

        public void cancel(android.support.v4.view.ViewPropertyAnimatorCompat vpa, android.view.View view);

        public void x(android.support.v4.view.ViewPropertyAnimatorCompat vpa, android.view.View view, float value);

        public void xBy(android.support.v4.view.ViewPropertyAnimatorCompat vpa, android.view.View view, float value);

        public void y(android.support.v4.view.ViewPropertyAnimatorCompat vpa, android.view.View view, float value);

        public void yBy(android.support.v4.view.ViewPropertyAnimatorCompat vpa, android.view.View view, float value);

        public void z(android.support.v4.view.ViewPropertyAnimatorCompat vpa, android.view.View view, float value);

        public void zBy(android.support.v4.view.ViewPropertyAnimatorCompat vpa, android.view.View view, float value);

        public void translationX(android.support.v4.view.ViewPropertyAnimatorCompat vpa, android.view.View view, float value);

        public void translationXBy(android.support.v4.view.ViewPropertyAnimatorCompat vpa, android.view.View view, float value);

        public void translationY(android.support.v4.view.ViewPropertyAnimatorCompat vpa, android.view.View view, float value);

        public void translationYBy(android.support.v4.view.ViewPropertyAnimatorCompat vpa, android.view.View view, float value);

        public void translationZ(android.support.v4.view.ViewPropertyAnimatorCompat vpa, android.view.View view, float value);

        public void translationZBy(android.support.v4.view.ViewPropertyAnimatorCompat vpa, android.view.View view, float value);

        public void start(android.support.v4.view.ViewPropertyAnimatorCompat vpa, android.view.View view);

        public void withLayer(android.support.v4.view.ViewPropertyAnimatorCompat vpa, android.view.View view);

        public void withStartAction(android.support.v4.view.ViewPropertyAnimatorCompat vpa, android.view.View view, java.lang.Runnable runnable);

        public void withEndAction(android.support.v4.view.ViewPropertyAnimatorCompat vpa, android.view.View view, java.lang.Runnable runnable);

        public void setListener(android.support.v4.view.ViewPropertyAnimatorCompat vpa, android.view.View view, android.support.v4.view.ViewPropertyAnimatorListener listener);

        public void setUpdateListener(android.support.v4.view.ViewPropertyAnimatorCompat vpa, android.view.View view, android.support.v4.view.ViewPropertyAnimatorUpdateListener listener);
    }

    static class BaseViewPropertyAnimatorCompatImpl implements android.support.v4.view.ViewPropertyAnimatorCompat.ViewPropertyAnimatorCompatImpl {
        java.util.WeakHashMap<android.view.View, java.lang.Runnable> mStarterMap = null;

        @java.lang.Override
        public void setDuration(android.support.v4.view.ViewPropertyAnimatorCompat vpa, android.view.View view, long value) {
            // noop on versions prior to ICS
        }

        @java.lang.Override
        public void alpha(android.support.v4.view.ViewPropertyAnimatorCompat vpa, android.view.View view, float value) {
            // noop on versions prior to ICS
            postStartMessage(vpa, view);
        }

        @java.lang.Override
        public void translationX(android.support.v4.view.ViewPropertyAnimatorCompat vpa, android.view.View view, float value) {
            // noop on versions prior to ICS
            postStartMessage(vpa, view);
        }

        @java.lang.Override
        public void translationY(android.support.v4.view.ViewPropertyAnimatorCompat vpa, android.view.View view, float value) {
            // noop on versions prior to ICS
            postStartMessage(vpa, view);
        }

        @java.lang.Override
        public void withEndAction(android.support.v4.view.ViewPropertyAnimatorCompat vpa, android.view.View view, java.lang.Runnable runnable) {
            vpa.mEndAction = runnable;
            postStartMessage(vpa, view);
        }

        @java.lang.Override
        public long getDuration(android.support.v4.view.ViewPropertyAnimatorCompat vpa, android.view.View view) {
            return 0;
        }

        @java.lang.Override
        public void setInterpolator(android.support.v4.view.ViewPropertyAnimatorCompat vpa, android.view.View view, android.view.animation.Interpolator value) {
            // noop on versions prior to ICS
        }

        @java.lang.Override
        public android.view.animation.Interpolator getInterpolator(android.support.v4.view.ViewPropertyAnimatorCompat vpa, android.view.View view) {
            return null;
        }

        @java.lang.Override
        public void setStartDelay(android.support.v4.view.ViewPropertyAnimatorCompat vpa, android.view.View view, long value) {
            // noop on versions prior to ICS
        }

        @java.lang.Override
        public long getStartDelay(android.support.v4.view.ViewPropertyAnimatorCompat vpa, android.view.View view) {
            return 0;
        }

        @java.lang.Override
        public void alphaBy(android.support.v4.view.ViewPropertyAnimatorCompat vpa, android.view.View view, float value) {
            // noop on versions prior to ICS
            postStartMessage(vpa, view);
        }

        @java.lang.Override
        public void rotation(android.support.v4.view.ViewPropertyAnimatorCompat vpa, android.view.View view, float value) {
            // noop on versions prior to ICS
            postStartMessage(vpa, view);
        }

        @java.lang.Override
        public void rotationBy(android.support.v4.view.ViewPropertyAnimatorCompat vpa, android.view.View view, float value) {
            // noop on versions prior to ICS
            postStartMessage(vpa, view);
        }

        @java.lang.Override
        public void rotationX(android.support.v4.view.ViewPropertyAnimatorCompat vpa, android.view.View view, float value) {
            // noop on versions prior to ICS
            postStartMessage(vpa, view);
        }

        @java.lang.Override
        public void rotationXBy(android.support.v4.view.ViewPropertyAnimatorCompat vpa, android.view.View view, float value) {
            // noop on versions prior to ICS
            postStartMessage(vpa, view);
        }

        @java.lang.Override
        public void rotationY(android.support.v4.view.ViewPropertyAnimatorCompat vpa, android.view.View view, float value) {
            // noop on versions prior to ICS
            postStartMessage(vpa, view);
        }

        @java.lang.Override
        public void rotationYBy(android.support.v4.view.ViewPropertyAnimatorCompat vpa, android.view.View view, float value) {
            // noop on versions prior to ICS
            postStartMessage(vpa, view);
        }

        @java.lang.Override
        public void scaleX(android.support.v4.view.ViewPropertyAnimatorCompat vpa, android.view.View view, float value) {
            // noop on versions prior to ICS
            postStartMessage(vpa, view);
        }

        @java.lang.Override
        public void scaleXBy(android.support.v4.view.ViewPropertyAnimatorCompat vpa, android.view.View view, float value) {
            // noop on versions prior to ICS
            postStartMessage(vpa, view);
        }

        @java.lang.Override
        public void scaleY(android.support.v4.view.ViewPropertyAnimatorCompat vpa, android.view.View view, float value) {
            // noop on versions prior to ICS
            postStartMessage(vpa, view);
        }

        @java.lang.Override
        public void scaleYBy(android.support.v4.view.ViewPropertyAnimatorCompat vpa, android.view.View view, float value) {
            // noop on versions prior to ICS
            postStartMessage(vpa, view);
        }

        @java.lang.Override
        public void cancel(android.support.v4.view.ViewPropertyAnimatorCompat vpa, android.view.View view) {
            // noop on versions prior to ICS
            postStartMessage(vpa, view);
        }

        @java.lang.Override
        public void x(android.support.v4.view.ViewPropertyAnimatorCompat vpa, android.view.View view, float value) {
            // noop on versions prior to ICS
            postStartMessage(vpa, view);
        }

        @java.lang.Override
        public void xBy(android.support.v4.view.ViewPropertyAnimatorCompat vpa, android.view.View view, float value) {
            // noop on versions prior to ICS
            postStartMessage(vpa, view);
        }

        @java.lang.Override
        public void y(android.support.v4.view.ViewPropertyAnimatorCompat vpa, android.view.View view, float value) {
            // noop on versions prior to ICS
            postStartMessage(vpa, view);
        }

        @java.lang.Override
        public void yBy(android.support.v4.view.ViewPropertyAnimatorCompat vpa, android.view.View view, float value) {
            // noop on versions prior to ICS
            postStartMessage(vpa, view);
        }

        @java.lang.Override
        public void z(android.support.v4.view.ViewPropertyAnimatorCompat vpa, android.view.View view, float value) {
            // noop on versions prior to Lollipop
        }

        @java.lang.Override
        public void zBy(android.support.v4.view.ViewPropertyAnimatorCompat vpa, android.view.View view, float value) {
            // noop on versions prior to Lollipop
        }

        @java.lang.Override
        public void translationXBy(android.support.v4.view.ViewPropertyAnimatorCompat vpa, android.view.View view, float value) {
            // noop on versions prior to ICS
            postStartMessage(vpa, view);
        }

        @java.lang.Override
        public void translationYBy(android.support.v4.view.ViewPropertyAnimatorCompat vpa, android.view.View view, float value) {
            // noop on versions prior to ICS
            postStartMessage(vpa, view);
        }

        @java.lang.Override
        public void translationZ(android.support.v4.view.ViewPropertyAnimatorCompat vpa, android.view.View view, float value) {
            // noop on versions prior to Lollipop
        }

        @java.lang.Override
        public void translationZBy(android.support.v4.view.ViewPropertyAnimatorCompat vpa, android.view.View view, float value) {
            // noop on versions prior to Lollipop
        }

        @java.lang.Override
        public void start(android.support.v4.view.ViewPropertyAnimatorCompat vpa, android.view.View view) {
            removeStartMessage(view);
            startAnimation(vpa, view);
        }

        @java.lang.Override
        public void withLayer(android.support.v4.view.ViewPropertyAnimatorCompat vpa, android.view.View view) {
            // noop on versions prior to ICS
        }

        @java.lang.Override
        public void withStartAction(android.support.v4.view.ViewPropertyAnimatorCompat vpa, android.view.View view, java.lang.Runnable runnable) {
            vpa.mStartAction = runnable;
            postStartMessage(vpa, view);
        }

        @java.lang.Override
        public void setListener(android.support.v4.view.ViewPropertyAnimatorCompat vpa, android.view.View view, android.support.v4.view.ViewPropertyAnimatorListener listener) {
            view.setTag(android.support.v4.view.ViewPropertyAnimatorCompat.LISTENER_TAG_ID, listener);
        }

        @java.lang.Override
        public void setUpdateListener(android.support.v4.view.ViewPropertyAnimatorCompat vpa, android.view.View view, android.support.v4.view.ViewPropertyAnimatorUpdateListener listener) {
            // noop
        }

        void startAnimation(android.support.v4.view.ViewPropertyAnimatorCompat vpa, android.view.View view) {
            java.lang.Object listenerTag = view.getTag(android.support.v4.view.ViewPropertyAnimatorCompat.LISTENER_TAG_ID);
            android.support.v4.view.ViewPropertyAnimatorListener listener = null;
            if (listenerTag instanceof android.support.v4.view.ViewPropertyAnimatorListener) {
                listener = ((android.support.v4.view.ViewPropertyAnimatorListener) (listenerTag));
            }
            java.lang.Runnable startAction = vpa.mStartAction;
            java.lang.Runnable endAction = vpa.mEndAction;
            vpa.mStartAction = null;
            vpa.mEndAction = null;
            if (startAction != null) {
                startAction.run();
            }
            if (listener != null) {
                listener.onAnimationStart(view);
                listener.onAnimationEnd(view);
            }
            if (endAction != null) {
                endAction.run();
            }
            if (mStarterMap != null) {
                mStarterMap.remove(view);
            }
        }

        class Starter implements java.lang.Runnable {
            java.lang.ref.WeakReference<android.view.View> mViewRef;

            android.support.v4.view.ViewPropertyAnimatorCompat mVpa;

            Starter(android.support.v4.view.ViewPropertyAnimatorCompat vpa, android.view.View view) {
                mViewRef = new java.lang.ref.WeakReference<android.view.View>(view);
                mVpa = vpa;
            }

            @java.lang.Override
            public void run() {
                final android.view.View view = mViewRef.get();
                if (view != null) {
                    startAnimation(mVpa, view);
                }
            }
        }

        private void removeStartMessage(android.view.View view) {
            java.lang.Runnable starter = null;
            if (mStarterMap != null) {
                starter = mStarterMap.get(view);
                if (starter != null) {
                    view.removeCallbacks(starter);
                }
            }
        }

        private void postStartMessage(android.support.v4.view.ViewPropertyAnimatorCompat vpa, android.view.View view) {
            java.lang.Runnable starter = null;
            if (mStarterMap != null) {
                starter = mStarterMap.get(view);
            }
            if (starter == null) {
                starter = new android.support.v4.view.ViewPropertyAnimatorCompat.BaseViewPropertyAnimatorCompatImpl.Starter(vpa, view);
                if (mStarterMap == null) {
                    mStarterMap = new java.util.WeakHashMap<android.view.View, java.lang.Runnable>();
                }
                mStarterMap.put(view, starter);
            }
            view.removeCallbacks(starter);
            view.post(starter);
        }
    }

    static class ICSViewPropertyAnimatorCompatImpl extends android.support.v4.view.ViewPropertyAnimatorCompat.BaseViewPropertyAnimatorCompatImpl {
        java.util.WeakHashMap<android.view.View, java.lang.Integer> mLayerMap = null;

        @java.lang.Override
        public void setDuration(android.support.v4.view.ViewPropertyAnimatorCompat vpa, android.view.View view, long value) {
            android.support.v4.view.ViewPropertyAnimatorCompatICS.setDuration(view, value);
        }

        @java.lang.Override
        public void alpha(android.support.v4.view.ViewPropertyAnimatorCompat vpa, android.view.View view, float value) {
            android.support.v4.view.ViewPropertyAnimatorCompatICS.alpha(view, value);
        }

        @java.lang.Override
        public void translationX(android.support.v4.view.ViewPropertyAnimatorCompat vpa, android.view.View view, float value) {
            android.support.v4.view.ViewPropertyAnimatorCompatICS.translationX(view, value);
        }

        @java.lang.Override
        public void translationY(android.support.v4.view.ViewPropertyAnimatorCompat vpa, android.view.View view, float value) {
            android.support.v4.view.ViewPropertyAnimatorCompatICS.translationY(view, value);
        }

        @java.lang.Override
        public long getDuration(android.support.v4.view.ViewPropertyAnimatorCompat vpa, android.view.View view) {
            return android.support.v4.view.ViewPropertyAnimatorCompatICS.getDuration(view);
        }

        @java.lang.Override
        public void setInterpolator(android.support.v4.view.ViewPropertyAnimatorCompat vpa, android.view.View view, android.view.animation.Interpolator value) {
            android.support.v4.view.ViewPropertyAnimatorCompatICS.setInterpolator(view, value);
        }

        @java.lang.Override
        public void setStartDelay(android.support.v4.view.ViewPropertyAnimatorCompat vpa, android.view.View view, long value) {
            android.support.v4.view.ViewPropertyAnimatorCompatICS.setStartDelay(view, value);
        }

        @java.lang.Override
        public long getStartDelay(android.support.v4.view.ViewPropertyAnimatorCompat vpa, android.view.View view) {
            return android.support.v4.view.ViewPropertyAnimatorCompatICS.getStartDelay(view);
        }

        @java.lang.Override
        public void alphaBy(android.support.v4.view.ViewPropertyAnimatorCompat vpa, android.view.View view, float value) {
            android.support.v4.view.ViewPropertyAnimatorCompatICS.alphaBy(view, value);
        }

        @java.lang.Override
        public void rotation(android.support.v4.view.ViewPropertyAnimatorCompat vpa, android.view.View view, float value) {
            android.support.v4.view.ViewPropertyAnimatorCompatICS.rotation(view, value);
        }

        @java.lang.Override
        public void rotationBy(android.support.v4.view.ViewPropertyAnimatorCompat vpa, android.view.View view, float value) {
            android.support.v4.view.ViewPropertyAnimatorCompatICS.rotationBy(view, value);
        }

        @java.lang.Override
        public void rotationX(android.support.v4.view.ViewPropertyAnimatorCompat vpa, android.view.View view, float value) {
            android.support.v4.view.ViewPropertyAnimatorCompatICS.rotationX(view, value);
        }

        @java.lang.Override
        public void rotationXBy(android.support.v4.view.ViewPropertyAnimatorCompat vpa, android.view.View view, float value) {
            android.support.v4.view.ViewPropertyAnimatorCompatICS.rotationXBy(view, value);
        }

        @java.lang.Override
        public void rotationY(android.support.v4.view.ViewPropertyAnimatorCompat vpa, android.view.View view, float value) {
            android.support.v4.view.ViewPropertyAnimatorCompatICS.rotationY(view, value);
        }

        @java.lang.Override
        public void rotationYBy(android.support.v4.view.ViewPropertyAnimatorCompat vpa, android.view.View view, float value) {
            android.support.v4.view.ViewPropertyAnimatorCompatICS.rotationYBy(view, value);
        }

        @java.lang.Override
        public void scaleX(android.support.v4.view.ViewPropertyAnimatorCompat vpa, android.view.View view, float value) {
            android.support.v4.view.ViewPropertyAnimatorCompatICS.scaleX(view, value);
        }

        @java.lang.Override
        public void scaleXBy(android.support.v4.view.ViewPropertyAnimatorCompat vpa, android.view.View view, float value) {
            android.support.v4.view.ViewPropertyAnimatorCompatICS.scaleXBy(view, value);
        }

        @java.lang.Override
        public void scaleY(android.support.v4.view.ViewPropertyAnimatorCompat vpa, android.view.View view, float value) {
            android.support.v4.view.ViewPropertyAnimatorCompatICS.scaleY(view, value);
        }

        @java.lang.Override
        public void scaleYBy(android.support.v4.view.ViewPropertyAnimatorCompat vpa, android.view.View view, float value) {
            android.support.v4.view.ViewPropertyAnimatorCompatICS.scaleYBy(view, value);
        }

        @java.lang.Override
        public void cancel(android.support.v4.view.ViewPropertyAnimatorCompat vpa, android.view.View view) {
            android.support.v4.view.ViewPropertyAnimatorCompatICS.cancel(view);
        }

        @java.lang.Override
        public void x(android.support.v4.view.ViewPropertyAnimatorCompat vpa, android.view.View view, float value) {
            android.support.v4.view.ViewPropertyAnimatorCompatICS.x(view, value);
        }

        @java.lang.Override
        public void xBy(android.support.v4.view.ViewPropertyAnimatorCompat vpa, android.view.View view, float value) {
            android.support.v4.view.ViewPropertyAnimatorCompatICS.xBy(view, value);
        }

        @java.lang.Override
        public void y(android.support.v4.view.ViewPropertyAnimatorCompat vpa, android.view.View view, float value) {
            android.support.v4.view.ViewPropertyAnimatorCompatICS.y(view, value);
        }

        @java.lang.Override
        public void yBy(android.support.v4.view.ViewPropertyAnimatorCompat vpa, android.view.View view, float value) {
            android.support.v4.view.ViewPropertyAnimatorCompatICS.yBy(view, value);
        }

        @java.lang.Override
        public void translationXBy(android.support.v4.view.ViewPropertyAnimatorCompat vpa, android.view.View view, float value) {
            android.support.v4.view.ViewPropertyAnimatorCompatICS.translationXBy(view, value);
        }

        @java.lang.Override
        public void translationYBy(android.support.v4.view.ViewPropertyAnimatorCompat vpa, android.view.View view, float value) {
            android.support.v4.view.ViewPropertyAnimatorCompatICS.translationYBy(view, value);
        }

        @java.lang.Override
        public void start(android.support.v4.view.ViewPropertyAnimatorCompat vpa, android.view.View view) {
            android.support.v4.view.ViewPropertyAnimatorCompatICS.start(view);
        }

        @java.lang.Override
        public void setListener(android.support.v4.view.ViewPropertyAnimatorCompat vpa, android.view.View view, android.support.v4.view.ViewPropertyAnimatorListener listener) {
            view.setTag(android.support.v4.view.ViewPropertyAnimatorCompat.LISTENER_TAG_ID, listener);
            android.support.v4.view.ViewPropertyAnimatorCompatICS.setListener(view, new android.support.v4.view.ViewPropertyAnimatorCompat.ICSViewPropertyAnimatorCompatImpl.MyVpaListener(vpa));
        }

        @java.lang.Override
        public void withEndAction(android.support.v4.view.ViewPropertyAnimatorCompat vpa, android.view.View view, final java.lang.Runnable runnable) {
            android.support.v4.view.ViewPropertyAnimatorCompatICS.setListener(view, new android.support.v4.view.ViewPropertyAnimatorCompat.ICSViewPropertyAnimatorCompatImpl.MyVpaListener(vpa));
            vpa.mEndAction = runnable;
        }

        @java.lang.Override
        public void withStartAction(android.support.v4.view.ViewPropertyAnimatorCompat vpa, android.view.View view, final java.lang.Runnable runnable) {
            android.support.v4.view.ViewPropertyAnimatorCompatICS.setListener(view, new android.support.v4.view.ViewPropertyAnimatorCompat.ICSViewPropertyAnimatorCompatImpl.MyVpaListener(vpa));
            vpa.mStartAction = runnable;
        }

        @java.lang.Override
        public void withLayer(android.support.v4.view.ViewPropertyAnimatorCompat vpa, android.view.View view) {
            vpa.mOldLayerType = android.support.v4.view.ViewCompat.getLayerType(view);
            android.support.v4.view.ViewPropertyAnimatorCompatICS.setListener(view, new android.support.v4.view.ViewPropertyAnimatorCompat.ICSViewPropertyAnimatorCompatImpl.MyVpaListener(vpa));
        }

        static class MyVpaListener implements android.support.v4.view.ViewPropertyAnimatorListener {
            android.support.v4.view.ViewPropertyAnimatorCompat mVpa;

            boolean mAnimEndCalled;

            MyVpaListener(android.support.v4.view.ViewPropertyAnimatorCompat vpa) {
                mVpa = vpa;
            }

            @java.lang.Override
            public void onAnimationStart(android.view.View view) {
                // Reset our end called flag, since this is a new animation...
                mAnimEndCalled = false;
                if (mVpa.mOldLayerType >= 0) {
                    android.support.v4.view.ViewCompat.setLayerType(view, android.support.v4.view.ViewCompat.LAYER_TYPE_HARDWARE, null);
                }
                if (mVpa.mStartAction != null) {
                    java.lang.Runnable startAction = mVpa.mStartAction;
                    mVpa.mStartAction = null;
                    startAction.run();
                }
                java.lang.Object listenerTag = view.getTag(android.support.v4.view.ViewPropertyAnimatorCompat.LISTENER_TAG_ID);
                android.support.v4.view.ViewPropertyAnimatorListener listener = null;
                if (listenerTag instanceof android.support.v4.view.ViewPropertyAnimatorListener) {
                    listener = ((android.support.v4.view.ViewPropertyAnimatorListener) (listenerTag));
                }
                if (listener != null) {
                    listener.onAnimationStart(view);
                }
            }

            @java.lang.Override
            public void onAnimationEnd(android.view.View view) {
                if (mVpa.mOldLayerType >= 0) {
                    android.support.v4.view.ViewCompat.setLayerType(view, mVpa.mOldLayerType, null);
                    mVpa.mOldLayerType = -1;
                }
                if ((android.os.Build.VERSION.SDK_INT >= 16) || (!mAnimEndCalled)) {
                    // Pre-v16 seems to have a bug where onAnimationEnd is called
                    // twice, therefore we only dispatch on the first call
                    if (mVpa.mEndAction != null) {
                        java.lang.Runnable endAction = mVpa.mEndAction;
                        mVpa.mEndAction = null;
                        endAction.run();
                    }
                    java.lang.Object listenerTag = view.getTag(android.support.v4.view.ViewPropertyAnimatorCompat.LISTENER_TAG_ID);
                    android.support.v4.view.ViewPropertyAnimatorListener listener = null;
                    if (listenerTag instanceof android.support.v4.view.ViewPropertyAnimatorListener) {
                        listener = ((android.support.v4.view.ViewPropertyAnimatorListener) (listenerTag));
                    }
                    if (listener != null) {
                        listener.onAnimationEnd(view);
                    }
                    mAnimEndCalled = true;
                }
            }

            @java.lang.Override
            public void onAnimationCancel(android.view.View view) {
                java.lang.Object listenerTag = view.getTag(android.support.v4.view.ViewPropertyAnimatorCompat.LISTENER_TAG_ID);
                android.support.v4.view.ViewPropertyAnimatorListener listener = null;
                if (listenerTag instanceof android.support.v4.view.ViewPropertyAnimatorListener) {
                    listener = ((android.support.v4.view.ViewPropertyAnimatorListener) (listenerTag));
                }
                if (listener != null) {
                    listener.onAnimationCancel(view);
                }
            }
        }
    }

    static class JBViewPropertyAnimatorCompatImpl extends android.support.v4.view.ViewPropertyAnimatorCompat.ICSViewPropertyAnimatorCompatImpl {
        @java.lang.Override
        public void setListener(android.support.v4.view.ViewPropertyAnimatorCompat vpa, android.view.View view, android.support.v4.view.ViewPropertyAnimatorListener listener) {
            android.support.v4.view.ViewPropertyAnimatorCompatJB.setListener(view, listener);
        }

        @java.lang.Override
        public void withStartAction(android.support.v4.view.ViewPropertyAnimatorCompat vpa, android.view.View view, java.lang.Runnable runnable) {
            android.support.v4.view.ViewPropertyAnimatorCompatJB.withStartAction(view, runnable);
        }

        @java.lang.Override
        public void withEndAction(android.support.v4.view.ViewPropertyAnimatorCompat vpa, android.view.View view, java.lang.Runnable runnable) {
            android.support.v4.view.ViewPropertyAnimatorCompatJB.withEndAction(view, runnable);
        }

        @java.lang.Override
        public void withLayer(android.support.v4.view.ViewPropertyAnimatorCompat vpa, android.view.View view) {
            android.support.v4.view.ViewPropertyAnimatorCompatJB.withLayer(view);
        }
    }

    static class JBMr2ViewPropertyAnimatorCompatImpl extends android.support.v4.view.ViewPropertyAnimatorCompat.JBViewPropertyAnimatorCompatImpl {
        @java.lang.Override
        public android.view.animation.Interpolator getInterpolator(android.support.v4.view.ViewPropertyAnimatorCompat vpa, android.view.View view) {
            return ((android.view.animation.Interpolator) (android.support.v4.view.ViewPropertyAnimatorCompatJellybeanMr2.getInterpolator(view)));
        }
    }

    static class KitKatViewPropertyAnimatorCompatImpl extends android.support.v4.view.ViewPropertyAnimatorCompat.JBMr2ViewPropertyAnimatorCompatImpl {
        @java.lang.Override
        public void setUpdateListener(android.support.v4.view.ViewPropertyAnimatorCompat vpa, android.view.View view, android.support.v4.view.ViewPropertyAnimatorUpdateListener listener) {
            android.support.v4.view.ViewPropertyAnimatorCompatKK.setUpdateListener(view, listener);
        }
    }

    static class LollipopViewPropertyAnimatorCompatImpl extends android.support.v4.view.ViewPropertyAnimatorCompat.KitKatViewPropertyAnimatorCompatImpl {
        @java.lang.Override
        public void translationZ(android.support.v4.view.ViewPropertyAnimatorCompat vpa, android.view.View view, float value) {
            android.support.v4.view.ViewPropertyAnimatorCompatLollipop.translationZ(view, value);
        }

        @java.lang.Override
        public void translationZBy(android.support.v4.view.ViewPropertyAnimatorCompat vpa, android.view.View view, float value) {
            android.support.v4.view.ViewPropertyAnimatorCompatLollipop.translationZBy(view, value);
        }

        @java.lang.Override
        public void z(android.support.v4.view.ViewPropertyAnimatorCompat vpa, android.view.View view, float value) {
            android.support.v4.view.ViewPropertyAnimatorCompatLollipop.z(view, value);
        }

        @java.lang.Override
        public void zBy(android.support.v4.view.ViewPropertyAnimatorCompat vpa, android.view.View view, float value) {
            android.support.v4.view.ViewPropertyAnimatorCompatLollipop.zBy(view, value);
        }
    }

    static final android.support.v4.view.ViewPropertyAnimatorCompat.ViewPropertyAnimatorCompatImpl IMPL;

    static {
        final int version = android.os.Build.VERSION.SDK_INT;
        if (version >= 21) {
            IMPL = new android.support.v4.view.ViewPropertyAnimatorCompat.LollipopViewPropertyAnimatorCompatImpl();
        } else
            if (version >= 19) {
                IMPL = new android.support.v4.view.ViewPropertyAnimatorCompat.KitKatViewPropertyAnimatorCompatImpl();
            } else
                if (version >= 18) {
                    IMPL = new android.support.v4.view.ViewPropertyAnimatorCompat.JBMr2ViewPropertyAnimatorCompatImpl();
                } else
                    if (version >= 16) {
                        IMPL = new android.support.v4.view.ViewPropertyAnimatorCompat.JBViewPropertyAnimatorCompatImpl();
                    } else
                        if (version >= 14) {
                            IMPL = new android.support.v4.view.ViewPropertyAnimatorCompat.ICSViewPropertyAnimatorCompatImpl();
                        } else {
                            IMPL = new android.support.v4.view.ViewPropertyAnimatorCompat.BaseViewPropertyAnimatorCompatImpl();
                        }




    }

    /**
     * Sets the duration for the underlying animator that animates the requested properties.
     * By default, the animator uses the default value for ValueAnimator. Calling this method
     * will cause the declared value to be used instead.
     *
     * <p>Prior to API 14, this method will do nothing.</p>
     *
     * @param value
     * 		The length of ensuing property animations, in milliseconds. The value
     * 		cannot be negative.
     * @return This object, allowing calls to methods in this class to be chained.
     */
    public android.support.v4.view.ViewPropertyAnimatorCompat setDuration(long value) {
        android.view.View view;
        if ((view = mView.get()) != null) {
            android.support.v4.view.ViewPropertyAnimatorCompat.IMPL.setDuration(this, view, value);
        }
        return this;
    }

    /**
     * This method will cause the View's <code>alpha</code> property to be animated to the
     * specified value. Animations already running on the property will be canceled.
     *
     * <p>Prior to API 14, this method will do nothing.</p>
     *
     * @param value
     * 		The value to be animated to.
     * @return This object, allowing calls to methods in this class to be chained.
     */
    public android.support.v4.view.ViewPropertyAnimatorCompat alpha(float value) {
        android.view.View view;
        if ((view = mView.get()) != null) {
            android.support.v4.view.ViewPropertyAnimatorCompat.IMPL.alpha(this, view, value);
        }
        return this;
    }

    /**
     * This method will cause the View's <code>alpha</code> property to be animated by the
     * specified value. Animations already running on the property will be canceled.
     *
     * <p>Prior to API 14, this method will do nothing.</p>
     *
     * @param value
     * 		The amount to be animated by, as an offset from the current value.
     * @return This object, allowing calls to methods in this class to be chained.
     */
    public android.support.v4.view.ViewPropertyAnimatorCompat alphaBy(float value) {
        android.view.View view;
        if ((view = mView.get()) != null) {
            android.support.v4.view.ViewPropertyAnimatorCompat.IMPL.alphaBy(this, view, value);
        }
        return this;
    }

    /**
     * This method will cause the View's <code>translationX</code> property to be animated to the
     * specified value. Animations already running on the property will be canceled.
     *
     * <p>Prior to API 14, this method will do nothing.</p>
     *
     * @param value
     * 		The value to be animated to.
     * @return This object, allowing calls to methods in this class to be chained.
     */
    public android.support.v4.view.ViewPropertyAnimatorCompat translationX(float value) {
        android.view.View view;
        if ((view = mView.get()) != null) {
            android.support.v4.view.ViewPropertyAnimatorCompat.IMPL.translationX(this, view, value);
        }
        return this;
    }

    /**
     * This method will cause the View's <code>translationY</code> property to be animated to the
     * specified value. Animations already running on the property will be canceled.
     *
     * <p>Prior to API 14, this method will do nothing.</p>
     *
     * @param value
     * 		The value to be animated to.
     * @return This object, allowing calls to methods in this class to be chained.
     */
    public android.support.v4.view.ViewPropertyAnimatorCompat translationY(float value) {
        android.view.View view;
        if ((view = mView.get()) != null) {
            android.support.v4.view.ViewPropertyAnimatorCompat.IMPL.translationY(this, view, value);
        }
        return this;
    }

    /**
     * Specifies an action to take place when the next animation ends. The action is only
     * run if the animation ends normally; if the ViewPropertyAnimator is canceled during
     * that animation, the runnable will not run.
     * This method, along with {@link #withStartAction(Runnable)}, is intended to help facilitate
     * choreographing ViewPropertyAnimator animations with other animations or actions
     * in the application.
     *
     * <p>For example, the following code animates a view to x=200 and then back to 0:</p>
     * <pre>
     *     Runnable endAction = new Runnable() {
     *         public void run() {
     *             view.animate().x(0);
     *         }
     *     };
     *     view.animate().x(200).withEndAction(endAction);
     * </pre>
     *
     * <p>Prior to API 14, this method will run the action immediately.</p>
     *
     * <p>For API 14 and 15, this method will run by setting
     * a listener on the ViewPropertyAnimatorCompat object and running the action
     * in that listener's {@link ViewPropertyAnimatorListener#onAnimationEnd(View)} method.</p>
     *
     * @param runnable
     * 		The action to run when the next animation ends.
     * @return This object, allowing calls to methods in this class to be chained.
     */
    public android.support.v4.view.ViewPropertyAnimatorCompat withEndAction(java.lang.Runnable runnable) {
        android.view.View view;
        if ((view = mView.get()) != null) {
            android.support.v4.view.ViewPropertyAnimatorCompat.IMPL.withEndAction(this, view, runnable);
        }
        return this;
    }

    /**
     * Returns the current duration of property animations. If the duration was set on this
     * object, that value is returned. Otherwise, the default value of the underlying Animator
     * is returned.
     *
     * <p>Prior to API 14, this method will return 0.</p>
     *
     * @see #setDuration(long)
     * @return The duration of animations, in milliseconds.
     */
    public long getDuration() {
        android.view.View view;
        if ((view = mView.get()) != null) {
            return android.support.v4.view.ViewPropertyAnimatorCompat.IMPL.getDuration(this, view);
        } else {
            return 0;
        }
    }

    /**
     * Sets the interpolator for the underlying animator that animates the requested properties.
     * By default, the animator uses the default interpolator for ValueAnimator. Calling this method
     * will cause the declared object to be used instead.
     *
     * <p>Prior to API 14, this method will do nothing.</p>
     *
     * @param value
     * 		The TimeInterpolator to be used for ensuing property animations.
     * @return This object, allowing calls to methods in this class to be chained.
     */
    public android.support.v4.view.ViewPropertyAnimatorCompat setInterpolator(android.view.animation.Interpolator value) {
        android.view.View view;
        if ((view = mView.get()) != null) {
            android.support.v4.view.ViewPropertyAnimatorCompat.IMPL.setInterpolator(this, view, value);
        }
        return this;
    }

    /**
     * Returns the timing interpolator that this animation uses.
     *
     * <p>Prior to API 14, this method will return null.</p>
     *
     * @return The timing interpolator for this animation.
     */
    public android.view.animation.Interpolator getInterpolator() {
        android.view.View view;
        if ((view = mView.get()) != null) {
            return android.support.v4.view.ViewPropertyAnimatorCompat.IMPL.getInterpolator(this, view);
        } else
            return null;

    }

    /**
     * Sets the startDelay for the underlying animator that animates the requested properties.
     * By default, the animator uses the default value for ValueAnimator. Calling this method
     * will cause the declared value to be used instead.
     *
     * <p>Prior to API 14, this method will do nothing.</p>
     *
     * @param value
     * 		The delay of ensuing property animations, in milliseconds. The value
     * 		cannot be negative.
     * @return This object, allowing calls to methods in this class to be chained.
     */
    public android.support.v4.view.ViewPropertyAnimatorCompat setStartDelay(long value) {
        android.view.View view;
        if ((view = mView.get()) != null) {
            android.support.v4.view.ViewPropertyAnimatorCompat.IMPL.setStartDelay(this, view, value);
        }
        return this;
    }

    /**
     * Returns the current startDelay of property animations. If the startDelay was set on this
     * object, that value is returned. Otherwise, the default value of the underlying Animator
     * is returned.
     *
     * <p>Prior to API 14, this method will return 0.</p>
     *
     * @see #setStartDelay(long)
     * @return The startDelay of animations, in milliseconds.
     */
    public long getStartDelay() {
        android.view.View view;
        if ((view = mView.get()) != null) {
            return android.support.v4.view.ViewPropertyAnimatorCompat.IMPL.getStartDelay(this, view);
        } else {
            return 0;
        }
    }

    /**
     * This method will cause the View's <code>rotation</code> property to be animated to the
     * specified value. Animations already running on the property will be canceled.
     *
     * <p>Prior to API 14, this method will do nothing.</p>
     *
     * @param value
     * 		The value to be animated to.
     * @return This object, allowing calls to methods in this class to be chained.
     */
    public android.support.v4.view.ViewPropertyAnimatorCompat rotation(float value) {
        android.view.View view;
        if ((view = mView.get()) != null) {
            android.support.v4.view.ViewPropertyAnimatorCompat.IMPL.rotation(this, view, value);
        }
        return this;
    }

    /**
     * This method will cause the View's <code>rotation</code> property to be animated by the
     * specified value. Animations already running on the property will be canceled.
     *
     * <p>Prior to API 14, this method will do nothing.</p>
     *
     * @param value
     * 		The amount to be animated by, as an offset from the current value.
     * @return This object, allowing calls to methods in this class to be chained.
     */
    public android.support.v4.view.ViewPropertyAnimatorCompat rotationBy(float value) {
        android.view.View view;
        if ((view = mView.get()) != null) {
            android.support.v4.view.ViewPropertyAnimatorCompat.IMPL.rotationBy(this, view, value);
        }
        return this;
    }

    /**
     * This method will cause the View's <code>rotationX</code> property to be animated to the
     * specified value. Animations already running on the property will be canceled.
     *
     * <p>Prior to API 14, this method will do nothing.</p>
     *
     * @param value
     * 		The value to be animated to.
     * @return This object, allowing calls to methods in this class to be chained.
     */
    public android.support.v4.view.ViewPropertyAnimatorCompat rotationX(float value) {
        android.view.View view;
        if ((view = mView.get()) != null) {
            android.support.v4.view.ViewPropertyAnimatorCompat.IMPL.rotationX(this, view, value);
        }
        return this;
    }

    /**
     * This method will cause the View's <code>rotationX</code> property to be animated by the
     * specified value. Animations already running on the property will be canceled.
     *
     * <p>Prior to API 14, this method will do nothing.</p>
     *
     * @param value
     * 		The amount to be animated by, as an offset from the current value.
     * @return This object, allowing calls to methods in this class to be chained.
     */
    public android.support.v4.view.ViewPropertyAnimatorCompat rotationXBy(float value) {
        android.view.View view;
        if ((view = mView.get()) != null) {
            android.support.v4.view.ViewPropertyAnimatorCompat.IMPL.rotationXBy(this, view, value);
        }
        return this;
    }

    /**
     * This method will cause the View's <code>rotationY</code> property to be animated to the
     * specified value. Animations already running on the property will be canceled.
     *
     * <p>Prior to API 14, this method will do nothing.</p>
     *
     * @param value
     * 		The value to be animated to.
     * @return This object, allowing calls to methods in this class to be chained.
     */
    public android.support.v4.view.ViewPropertyAnimatorCompat rotationY(float value) {
        android.view.View view;
        if ((view = mView.get()) != null) {
            android.support.v4.view.ViewPropertyAnimatorCompat.IMPL.rotationY(this, view, value);
        }
        return this;
    }

    /**
     * This method will cause the View's <code>rotationY</code> property to be animated by the
     * specified value. Animations already running on the property will be canceled.
     *
     * <p>Prior to API 14, this method will do nothing.</p>
     *
     * @param value
     * 		The amount to be animated by, as an offset from the current value.
     * @return This object, allowing calls to methods in this class to be chained.
     */
    public android.support.v4.view.ViewPropertyAnimatorCompat rotationYBy(float value) {
        android.view.View view;
        if ((view = mView.get()) != null) {
            android.support.v4.view.ViewPropertyAnimatorCompat.IMPL.rotationYBy(this, view, value);
        }
        return this;
    }

    /**
     * This method will cause the View's <code>scaleX</code> property to be animated to the
     * specified value. Animations already running on the property will be canceled.
     *
     * <p>Prior to API 14, this method will do nothing.</p>
     *
     * @param value
     * 		The value to be animated to.
     * @return This object, allowing calls to methods in this class to be chained.
     */
    public android.support.v4.view.ViewPropertyAnimatorCompat scaleX(float value) {
        android.view.View view;
        if ((view = mView.get()) != null) {
            android.support.v4.view.ViewPropertyAnimatorCompat.IMPL.scaleX(this, view, value);
        }
        return this;
    }

    /**
     * This method will cause the View's <code>scaleX</code> property to be animated by the
     * specified value. Animations already running on the property will be canceled.
     *
     * <p>Prior to API 14, this method will do nothing.</p>
     *
     * @param value
     * 		The amount to be animated by, as an offset from the current value.
     * @return This object, allowing calls to methods in this class to be chained.
     */
    public android.support.v4.view.ViewPropertyAnimatorCompat scaleXBy(float value) {
        android.view.View view;
        if ((view = mView.get()) != null) {
            android.support.v4.view.ViewPropertyAnimatorCompat.IMPL.scaleXBy(this, view, value);
        }
        return this;
    }

    /**
     * This method will cause the View's <code>scaleY</code> property to be animated to the
     * specified value. Animations already running on the property will be canceled.
     *
     * <p>Prior to API 14, this method will do nothing.</p>
     *
     * @param value
     * 		The value to be animated to.
     * @return This object, allowing calls to methods in this class to be chained.
     */
    public android.support.v4.view.ViewPropertyAnimatorCompat scaleY(float value) {
        android.view.View view;
        if ((view = mView.get()) != null) {
            android.support.v4.view.ViewPropertyAnimatorCompat.IMPL.scaleY(this, view, value);
        }
        return this;
    }

    /**
     * This method will cause the View's <code>scaleY</code> property to be animated by the
     * specified value. Animations already running on the property will be canceled.
     *
     * <p>Prior to API 14, this method will do nothing.</p>
     *
     * @param value
     * 		The amount to be animated by, as an offset from the current value.
     * @return This object, allowing calls to methods in this class to be chained.
     */
    public android.support.v4.view.ViewPropertyAnimatorCompat scaleYBy(float value) {
        android.view.View view;
        if ((view = mView.get()) != null) {
            android.support.v4.view.ViewPropertyAnimatorCompat.IMPL.scaleYBy(this, view, value);
        }
        return this;
    }

    /**
     * Cancels all property animations that are currently running or pending.
     */
    public void cancel() {
        android.view.View view;
        if ((view = mView.get()) != null) {
            android.support.v4.view.ViewPropertyAnimatorCompat.IMPL.cancel(this, view);
        }
    }

    /**
     * This method will cause the View's <code>x</code> property to be animated to the
     * specified value. Animations already running on the property will be canceled.
     *
     * <p>Prior to API 14, this method will do nothing.</p>
     *
     * @param value
     * 		The value to be animated to.
     * @return This object, allowing calls to methods in this class to be chained.
     */
    public android.support.v4.view.ViewPropertyAnimatorCompat x(float value) {
        android.view.View view;
        if ((view = mView.get()) != null) {
            android.support.v4.view.ViewPropertyAnimatorCompat.IMPL.x(this, view, value);
        }
        return this;
    }

    /**
     * This method will cause the View's <code>x</code> property to be animated by the
     * specified value. Animations already running on the property will be canceled.
     *
     * <p>Prior to API 14, this method will do nothing.</p>
     *
     * @param value
     * 		The amount to be animated by, as an offset from the current value.
     * @return This object, allowing calls to methods in this class to be chained.
     */
    public android.support.v4.view.ViewPropertyAnimatorCompat xBy(float value) {
        android.view.View view;
        if ((view = mView.get()) != null) {
            android.support.v4.view.ViewPropertyAnimatorCompat.IMPL.xBy(this, view, value);
        }
        return this;
    }

    /**
     * This method will cause the View's <code>y</code> property to be animated to the
     * specified value. Animations already running on the property will be canceled.
     *
     * <p>Prior to API 14, this method will do nothing.</p>
     *
     * @param value
     * 		The value to be animated to.
     * @return This object, allowing calls to methods in this class to be chained.
     */
    public android.support.v4.view.ViewPropertyAnimatorCompat y(float value) {
        android.view.View view;
        if ((view = mView.get()) != null) {
            android.support.v4.view.ViewPropertyAnimatorCompat.IMPL.y(this, view, value);
        }
        return this;
    }

    /**
     * This method will cause the View's <code>y</code> property to be animated by the
     * specified value. Animations already running on the property will be canceled.
     *
     * <p>Prior to API 14, this method will do nothing.</p>
     *
     * @param value
     * 		The amount to be animated by, as an offset from the current value.
     * @return This object, allowing calls to methods in this class to be chained.
     */
    public android.support.v4.view.ViewPropertyAnimatorCompat yBy(float value) {
        android.view.View view;
        if ((view = mView.get()) != null) {
            android.support.v4.view.ViewPropertyAnimatorCompat.IMPL.yBy(this, view, value);
        }
        return this;
    }

    /**
     * This method will cause the View's <code>translationX</code> property to be animated by the
     * specified value. Animations already running on the property will be canceled.
     *
     * <p>Prior to API 14, this method will do nothing.</p>
     *
     * @param value
     * 		The amount to be animated by, as an offset from the current value.
     * @return This object, allowing calls to methods in this class to be chained.
     */
    public android.support.v4.view.ViewPropertyAnimatorCompat translationXBy(float value) {
        android.view.View view;
        if ((view = mView.get()) != null) {
            android.support.v4.view.ViewPropertyAnimatorCompat.IMPL.translationXBy(this, view, value);
        }
        return this;
    }

    /**
     * This method will cause the View's <code>translationY</code> property to be animated by the
     * specified value. Animations already running on the property will be canceled.
     *
     * <p>Prior to API 14, this method will do nothing.</p>
     *
     * @param value
     * 		The amount to be animated by, as an offset from the current value.
     * @return This object, allowing calls to methods in this class to be chained.
     */
    public android.support.v4.view.ViewPropertyAnimatorCompat translationYBy(float value) {
        android.view.View view;
        if ((view = mView.get()) != null) {
            android.support.v4.view.ViewPropertyAnimatorCompat.IMPL.translationYBy(this, view, value);
        }
        return this;
    }

    /**
     * This method will cause the View's <code>translationZ</code> property to be animated by the
     * specified value. Animations already running on the property will be canceled.
     *
     * <p>Prior to API 21, this method will do nothing.</p>
     *
     * @param value
     * 		The amount to be animated by, as an offset from the current value.
     * @return This object, allowing calls to methods in this class to be chained.
     */
    public android.support.v4.view.ViewPropertyAnimatorCompat translationZBy(float value) {
        android.view.View view;
        if ((view = mView.get()) != null) {
            android.support.v4.view.ViewPropertyAnimatorCompat.IMPL.translationZBy(this, view, value);
        }
        return this;
    }

    /**
     * This method will cause the View's <code>translationZ</code> property to be animated to the
     * specified value. Animations already running on the property will be canceled.
     *
     * <p>Prior to API 21, this method will do nothing.</p>
     *
     * @param value
     * 		The amount to be animated by, as an offset from the current value.
     * @return This object, allowing calls to methods in this class to be chained.
     */
    public android.support.v4.view.ViewPropertyAnimatorCompat translationZ(float value) {
        android.view.View view;
        if ((view = mView.get()) != null) {
            android.support.v4.view.ViewPropertyAnimatorCompat.IMPL.translationZ(this, view, value);
        }
        return this;
    }

    /**
     * This method will cause the View's <code>z</code> property to be animated to the
     * specified value. Animations already running on the property will be canceled.
     *
     * <p>Prior to API 21, this method will do nothing.</p>
     *
     * @param value
     * 		The amount to be animated by, as an offset from the current value.
     * @return This object, allowing calls to methods in this class to be chained.
     */
    public android.support.v4.view.ViewPropertyAnimatorCompat z(float value) {
        android.view.View view;
        if ((view = mView.get()) != null) {
            android.support.v4.view.ViewPropertyAnimatorCompat.IMPL.z(this, view, value);
        }
        return this;
    }

    /**
     * This method will cause the View's <code>z</code> property to be animated by the
     * specified value. Animations already running on the property will be canceled.
     *
     * <p>Prior to API 21, this method will do nothing.</p>
     *
     * @param value
     * 		The amount to be animated by, as an offset from the current value.
     * @return This object, allowing calls to methods in this class to be chained.
     */
    public android.support.v4.view.ViewPropertyAnimatorCompat zBy(float value) {
        android.view.View view;
        if ((view = mView.get()) != null) {
            android.support.v4.view.ViewPropertyAnimatorCompat.IMPL.zBy(this, view, value);
        }
        return this;
    }

    /**
     * Starts the currently pending property animations immediately. Calling <code>start()</code>
     * is optional because all animations start automatically at the next opportunity. However,
     * if the animations are needed to start immediately and synchronously (not at the time when
     * the next event is processed by the hierarchy, which is when the animations would begin
     * otherwise), then this method can be used.
     *
     * <p>Prior to API 14, this method will do nothing.</p>
     */
    public void start() {
        android.view.View view;
        if ((view = mView.get()) != null) {
            android.support.v4.view.ViewPropertyAnimatorCompat.IMPL.start(this, view);
        }
    }

    /**
     * The View associated with this ViewPropertyAnimator will have its
     * {@link ViewCompat#setLayerType(View, int, android.graphics.Paint) layer type} set to
     * {@link ViewCompat#LAYER_TYPE_HARDWARE} for the duration of the next animation.
     * As stated in the documentation for {@link ViewCompat#LAYER_TYPE_HARDWARE},
     * the actual type of layer used internally depends on the runtime situation of the
     * view. If the activity and this view are hardware-accelerated, then the layer will be
     * accelerated as well. If the activity or the view is not accelerated, then the layer will
     * effectively be the same as {@link ViewCompat#LAYER_TYPE_SOFTWARE}.
     *
     * <p>This state is not persistent, either on the View or on this ViewPropertyAnimator: the
     * layer type of the View will be restored when the animation ends to what it was when this
     * method was called, and this setting on ViewPropertyAnimator is only valid for the next
     * animation. Note that calling this method and then independently setting the layer type of
     * the View (by a direct call to
     * {@link ViewCompat#setLayerType(View, int, android.graphics.Paint)}) will result in some
     * inconsistency, including having the layer type restored to its pre-withLayer()
     * value when the animation ends.</p>
     *
     * <p>Prior to API 14, this method will do nothing.</p>
     *
     * <p>For API 14 and 15, this method will run by setting
     * a listener on the ViewPropertyAnimatorCompat object, setting a hardware layer in
     * the listener's {@link ViewPropertyAnimatorListener#onAnimationStart(View)} method,
     * and then restoring the orignal layer type in the listener's
     * {@link ViewPropertyAnimatorListener#onAnimationEnd(View)} method.</p>
     *
     * @see View#setLayerType(int, android.graphics.Paint)
     * @return This object, allowing calls to methods in this class to be chained.
     */
    public android.support.v4.view.ViewPropertyAnimatorCompat withLayer() {
        android.view.View view;
        if ((view = mView.get()) != null) {
            android.support.v4.view.ViewPropertyAnimatorCompat.IMPL.withLayer(this, view);
        }
        return this;
    }

    /**
     * Specifies an action to take place when the next animation runs. If there is a
     * {@link #setStartDelay(long) startDelay} set on this ViewPropertyAnimator, then the
     * action will run after that startDelay expires, when the actual animation begins.
     * This method, along with {@link #withEndAction(Runnable)}, is intended to help facilitate
     * choreographing ViewPropertyAnimator animations with other animations or actions
     * in the application.
     *
     * <p>Prior to API 14, this method will run the action immediately.</p>
     *
     * <p>For API 14 and 15, this method will run by setting
     * a listener on the ViewPropertyAnimatorCompat object and running the action
     * in that listener's {@link ViewPropertyAnimatorListener#onAnimationStart(View)} method.</p>
     *
     * @param runnable
     * 		The action to run when the next animation starts.
     * @return This object, allowing calls to methods in this class to be chained.
     */
    public android.support.v4.view.ViewPropertyAnimatorCompat withStartAction(java.lang.Runnable runnable) {
        android.view.View view;
        if ((view = mView.get()) != null) {
            android.support.v4.view.ViewPropertyAnimatorCompat.IMPL.withStartAction(this, view, runnable);
        }
        return this;
    }

    /**
     * Sets a listener for events in the underlying Animators that run the property
     * animations.
     *
     * <p>Prior to API 14, this method will do nothing.</p>
     *
     * @param listener
     * 		The listener to be called with AnimatorListener events. A value of
     * 		<code>null</code> removes any existing listener.
     * @return This object, allowing calls to methods in this class to be chained.
     */
    public android.support.v4.view.ViewPropertyAnimatorCompat setListener(android.support.v4.view.ViewPropertyAnimatorListener listener) {
        android.view.View view;
        if ((view = mView.get()) != null) {
            android.support.v4.view.ViewPropertyAnimatorCompat.IMPL.setListener(this, view, listener);
        }
        return this;
    }

    /**
     * Sets a listener for update events in the underlying Animator that runs
     * the property animations.
     *
     * <p>Prior to API 19, this method will do nothing.</p>
     *
     * @param listener
     * 		The listener to be called with update events. A value of
     * 		<code>null</code> removes any existing listener.
     * @return This object, allowing calls to methods in this class to be chained.
     */
    public android.support.v4.view.ViewPropertyAnimatorCompat setUpdateListener(android.support.v4.view.ViewPropertyAnimatorUpdateListener listener) {
        android.view.View view;
        if ((view = mView.get()) != null) {
            android.support.v4.view.ViewPropertyAnimatorCompat.IMPL.setUpdateListener(this, view, listener);
        }
        return this;
    }
}

