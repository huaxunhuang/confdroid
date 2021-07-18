/**
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License") {}
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
package android.graphics.drawable;


/**
 * Delegate used to provide new implementation of a select few methods of {@link AnimatedVectorDrawable}
 * <p>
 * Through the layoutlib_create tool, the original  methods of AnimatedVectorDrawable have been
 * replaced by calls to methods of the same name in this delegate class.
 */
@java.lang.SuppressWarnings("unused")
public class AnimatedVectorDrawable_Delegate {
    private static com.android.layoutlib.bridge.impl.DelegateManager<android.graphics.drawable.AnimatedVectorDrawable_Delegate.AnimatorSetHolder> sAnimatorSets = new com.android.layoutlib.bridge.impl.DelegateManager(android.graphics.drawable.AnimatedVectorDrawable_Delegate.AnimatorSetHolder.class);

    private static com.android.layoutlib.bridge.impl.DelegateManager<android.graphics.drawable.AnimatedVectorDrawable_Delegate.PropertySetter> sHolders = new com.android.layoutlib.bridge.impl.DelegateManager(android.graphics.drawable.AnimatedVectorDrawable_Delegate.PropertySetter.class);

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static long nCreateAnimatorSet() {
        return android.graphics.drawable.AnimatedVectorDrawable_Delegate.sAnimatorSets.addNewDelegate(new android.graphics.drawable.AnimatedVectorDrawable_Delegate.AnimatorSetHolder());
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nSetVectorDrawableTarget(long animatorPtr, long vectorDrawablePtr) {
        // TODO: implement
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nAddAnimator(long setPtr, long propertyValuesHolder, long nativeInterpolator, long startDelay, long duration, int repeatCount, int repeatMode) {
        android.graphics.drawable.AnimatedVectorDrawable_Delegate.PropertySetter holder = android.graphics.drawable.AnimatedVectorDrawable_Delegate.sHolders.getDelegate(propertyValuesHolder);
        if ((holder == null) || (holder.getValues() == null)) {
            return;
        }
        android.animation.ObjectAnimator animator = new android.animation.ObjectAnimator();
        animator.setValues(holder.getValues());
        animator.setInterpolator(com.android.internal.view.animation.NativeInterpolatorFactoryHelper_Delegate.getDelegate(nativeInterpolator));
        animator.setStartDelay(startDelay);
        animator.setDuration(duration);
        animator.setRepeatCount(repeatCount);
        animator.setRepeatMode(repeatMode);
        animator.setTarget(holder);
        animator.setPropertyName(holder.getValues().getPropertyName());
        android.graphics.drawable.AnimatedVectorDrawable_Delegate.AnimatorSetHolder set = android.graphics.drawable.AnimatedVectorDrawable_Delegate.sAnimatorSets.getDelegate(setPtr);
        assert set != null;
        set.addAnimator(animator);
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static long nCreateGroupPropertyHolder(long nativePtr, int propertyId, float startValue, float endValue) {
        android.graphics.drawable.VectorDrawable_Delegate.VGroup_Delegate group = android.graphics.drawable.VectorDrawable_Delegate.VNativeObject.getDelegate(nativePtr);
        java.util.function.Consumer<java.lang.Float> setter = group.getPropertySetter(propertyId);
        return android.graphics.drawable.AnimatedVectorDrawable_Delegate.sHolders.addNewDelegate(android.graphics.drawable.AnimatedVectorDrawable_Delegate.FloatPropertySetter.of(setter, startValue, endValue));
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static long nCreatePathDataPropertyHolder(long nativePtr, long startValuePtr, long endValuePtr) {
        com.android.layoutlib.bridge.Bridge.getLog().fidelityWarning(LayoutLog.TAG_UNSUPPORTED, "AnimatedVectorDrawable path " + "animations are not supported.", null, null);
        return 0;
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static long nCreatePathColorPropertyHolder(long nativePtr, int propertyId, int startValue, int endValue) {
        android.graphics.drawable.VectorDrawable_Delegate.VFullPath_Delegate path = android.graphics.drawable.VectorDrawable_Delegate.VNativeObject.getDelegate(nativePtr);
        java.util.function.Consumer<java.lang.Integer> setter = path.getIntPropertySetter(propertyId);
        return android.graphics.drawable.AnimatedVectorDrawable_Delegate.sHolders.addNewDelegate(android.graphics.drawable.AnimatedVectorDrawable_Delegate.IntPropertySetter.of(setter, startValue, endValue));
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static long nCreatePathPropertyHolder(long nativePtr, int propertyId, float startValue, float endValue) {
        android.graphics.drawable.VectorDrawable_Delegate.VFullPath_Delegate path = android.graphics.drawable.VectorDrawable_Delegate.VNativeObject.getDelegate(nativePtr);
        java.util.function.Consumer<java.lang.Float> setter = path.getFloatPropertySetter(propertyId);
        return android.graphics.drawable.AnimatedVectorDrawable_Delegate.sHolders.addNewDelegate(android.graphics.drawable.AnimatedVectorDrawable_Delegate.FloatPropertySetter.of(setter, startValue, endValue));
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static long nCreateRootAlphaPropertyHolder(long nativePtr, float startValue, float endValue) {
        android.graphics.drawable.VectorDrawable_Delegate.VPathRenderer_Delegate renderer = android.graphics.drawable.VectorDrawable_Delegate.VNativeObject.getDelegate(nativePtr);
        return android.graphics.drawable.AnimatedVectorDrawable_Delegate.sHolders.addNewDelegate(android.graphics.drawable.AnimatedVectorDrawable_Delegate.FloatPropertySetter.of(renderer::setRootAlpha, startValue, endValue));
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nSetPropertyHolderData(long nativePtr, float[] data, int length) {
        android.graphics.drawable.AnimatedVectorDrawable_Delegate.PropertySetter setter = android.graphics.drawable.AnimatedVectorDrawable_Delegate.sHolders.getDelegate(nativePtr);
        assert setter != null;
        setter.setValues(data);
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nSetPropertyHolderData(long nativePtr, int[] data, int length) {
        android.graphics.drawable.AnimatedVectorDrawable_Delegate.PropertySetter setter = android.graphics.drawable.AnimatedVectorDrawable_Delegate.sHolders.getDelegate(nativePtr);
        assert setter != null;
        setter.setValues(data);
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nStart(long animatorSetPtr, android.graphics.drawable.AnimatedVectorDrawable.VectorDrawableAnimatorRT set, int id) {
        android.graphics.drawable.AnimatedVectorDrawable_Delegate.AnimatorSetHolder animatorSet = android.graphics.drawable.AnimatedVectorDrawable_Delegate.sAnimatorSets.getDelegate(animatorSetPtr);
        assert animatorSet != null;
        animatorSet.start();
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nReverse(long animatorSetPtr, android.graphics.drawable.AnimatedVectorDrawable.VectorDrawableAnimatorRT set, int id) {
        android.graphics.drawable.AnimatedVectorDrawable_Delegate.AnimatorSetHolder animatorSet = android.graphics.drawable.AnimatedVectorDrawable_Delegate.sAnimatorSets.getDelegate(animatorSetPtr);
        assert animatorSet != null;
        animatorSet.reverse();
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nEnd(long animatorSetPtr) {
        android.graphics.drawable.AnimatedVectorDrawable_Delegate.AnimatorSetHolder animatorSet = android.graphics.drawable.AnimatedVectorDrawable_Delegate.sAnimatorSets.getDelegate(animatorSetPtr);
        assert animatorSet != null;
        animatorSet.end();
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nReset(long animatorSetPtr) {
        android.graphics.drawable.AnimatedVectorDrawable_Delegate.AnimatorSetHolder animatorSet = android.graphics.drawable.AnimatedVectorDrawable_Delegate.sAnimatorSets.getDelegate(animatorSetPtr);
        assert animatorSet != null;
        animatorSet.end();
        animatorSet.start();
    }

    private static class AnimatorSetHolder {
        private java.util.ArrayList<android.animation.Animator> mAnimators = new java.util.ArrayList<>();

        private android.animation.AnimatorSet mAnimatorSet = null;

        private void addAnimator(@android.annotation.NonNull
        android.animation.Animator animator) {
            mAnimators.add(animator);
        }

        private void ensureAnimatorSet() {
            if (mAnimatorSet == null) {
                mAnimatorSet = new android.animation.AnimatorSet();
                mAnimatorSet.playTogether(mAnimators);
            }
        }

        private void start() {
            ensureAnimatorSet();
            mAnimatorSet.start();
        }

        private void end() {
            mAnimatorSet.end();
        }

        private void reset() {
            end();
            start();
        }

        private void reverse() {
            mAnimatorSet.reverse();
        }
    }

    /**
     * Class that allows setting a value and holds the range of values for the given property.
     *
     * @param <T>
     * 		the type of the property
     */
    private static class PropertySetter<T> {
        final java.util.function.Consumer<T> mValueSetter;

        private android.animation.PropertyValuesHolder mValues;

        private PropertySetter(@android.annotation.NonNull
        java.util.function.Consumer<T> valueSetter) {
            mValueSetter = valueSetter;
        }

        /**
         * Method to set an {@link Integer} value for this property. The default implementation of
         * this method doesn't do anything. This method is accessed via reflection by the
         * PropertyValuesHolder.
         */
        public void setIntValue(java.lang.Integer value) {
        }

        /**
         * Method to set an {@link Integer} value for this property. The default implementation of
         * this method doesn't do anything. This method is accessed via reflection by the
         * PropertyValuesHolder.
         */
        public void setFloatValue(java.lang.Float value) {
        }

        void setValues(float... values) {
            mValues = android.animation.PropertyValuesHolder.ofFloat("floatValue", values);
        }

        @android.annotation.Nullable
        android.animation.PropertyValuesHolder getValues() {
            return mValues;
        }

        void setValues(int... values) {
            mValues = android.animation.PropertyValuesHolder.ofInt("intValue", values);
        }
    }

    private static class IntPropertySetter extends android.graphics.drawable.AnimatedVectorDrawable_Delegate.PropertySetter<java.lang.Integer> {
        private IntPropertySetter(java.util.function.Consumer<java.lang.Integer> valueSetter) {
            super(valueSetter);
        }

        private static android.graphics.drawable.AnimatedVectorDrawable_Delegate.PropertySetter of(java.util.function.Consumer<java.lang.Integer> valueSetter, int... values) {
            android.graphics.drawable.AnimatedVectorDrawable_Delegate.PropertySetter setter = new android.graphics.drawable.AnimatedVectorDrawable_Delegate.IntPropertySetter(valueSetter);
            setter.setValues(values);
            return setter;
        }

        public void setIntValue(java.lang.Integer value) {
            mValueSetter.accept(value);
        }
    }

    private static class FloatPropertySetter extends android.graphics.drawable.AnimatedVectorDrawable_Delegate.PropertySetter<java.lang.Float> {
        private FloatPropertySetter(java.util.function.Consumer<java.lang.Float> valueSetter) {
            super(valueSetter);
        }

        private static android.graphics.drawable.AnimatedVectorDrawable_Delegate.PropertySetter of(java.util.function.Consumer<java.lang.Float> valueSetter, float... values) {
            android.graphics.drawable.AnimatedVectorDrawable_Delegate.PropertySetter setter = new android.graphics.drawable.AnimatedVectorDrawable_Delegate.FloatPropertySetter(valueSetter);
            setter.setValues(values);
            return setter;
        }

        public void setFloatValue(java.lang.Float value) {
            mValueSetter.accept(value);
        }
    }
}

