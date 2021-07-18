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
package android.animation;


/**
 * Abstract base class used convert type T to another type V and back again. This
 * is necessary when the value types of in animation are different from the property
 * type. BidirectionalTypeConverter is needed when only the final value for the
 * animation is supplied to animators.
 *
 * @see PropertyValuesHolder#setConverter(TypeConverter)
 */
public abstract class BidirectionalTypeConverter<T, V> extends android.animation.TypeConverter<T, V> {
    private android.animation.BidirectionalTypeConverter mInvertedConverter;

    public BidirectionalTypeConverter(java.lang.Class<T> fromClass, java.lang.Class<V> toClass) {
        super(fromClass, toClass);
    }

    /**
     * Does a conversion from the target type back to the source type. The subclass
     * must implement this when a TypeConverter is used in animations and current
     * values will need to be read for an animation.
     *
     * @param value
     * 		The Object to convert.
     * @return A value of type T, converted from <code>value</code>.
     */
    public abstract T convertBack(V value);

    /**
     * Returns the inverse of this converter, where the from and to classes are reversed.
     * The inverted converter uses this convert to call {@link #convertBack(Object)} for
     * {@link #convert(Object)} calls and {@link #convert(Object)} for
     * {@link #convertBack(Object)} calls.
     *
     * @return The inverse of this converter, where the from and to classes are reversed.
     */
    public android.animation.BidirectionalTypeConverter<V, T> invert() {
        if (mInvertedConverter == null) {
            mInvertedConverter = new android.animation.BidirectionalTypeConverter.InvertedConverter(this);
        }
        return mInvertedConverter;
    }

    private static class InvertedConverter<From, To> extends android.animation.BidirectionalTypeConverter<From, To> {
        private android.animation.BidirectionalTypeConverter<To, From> mConverter;

        public InvertedConverter(android.animation.BidirectionalTypeConverter<To, From> converter) {
            super(converter.getTargetType(), converter.getSourceType());
            mConverter = converter;
        }

        @java.lang.Override
        public From convertBack(To value) {
            return mConverter.convert(value);
        }

        @java.lang.Override
        public To convert(From value) {
            return mConverter.convertBack(value);
        }
    }
}

