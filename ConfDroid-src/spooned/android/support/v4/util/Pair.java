/**
 * Copyright (C) 2009 The Android Open Source Project
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
package android.support.v4.util;


/**
 * Container to ease passing around a tuple of two objects. This object provides a sensible
 * implementation of equals(), returning true if equals() is true on each of the contained
 * objects.
 */
public class Pair<F, S> {
    public final F first;

    public final S second;

    /**
     * Constructor for a Pair.
     *
     * @param first
     * 		the first object in the Pair
     * @param second
     * 		the second object in the pair
     */
    public Pair(F first, S second) {
        this.first = first;
        this.second = second;
    }

    /**
     * Checks the two objects for equality by delegating to their respective
     * {@link Object#equals(Object)} methods.
     *
     * @param o
     * 		the {@link Pair} to which this one is to be checked for equality
     * @return true if the underlying objects of the Pair are both considered
    equal
     */
    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (!(o instanceof android.support.v4.util.Pair)) {
            return false;
        }
        android.support.v4.util.Pair<?, ?> p = ((android.support.v4.util.Pair<?, ?>) (o));
        return android.support.v4.util.Pair.objectsEqual(p.first, first) && android.support.v4.util.Pair.objectsEqual(p.second, second);
    }

    private static boolean objectsEqual(java.lang.Object a, java.lang.Object b) {
        return (a == b) || ((a != null) && a.equals(b));
    }

    /**
     * Compute a hash code using the hash codes of the underlying objects
     *
     * @return a hashcode of the Pair
     */
    @java.lang.Override
    public int hashCode() {
        return (first == null ? 0 : first.hashCode()) ^ (second == null ? 0 : second.hashCode());
    }

    @java.lang.Override
    public java.lang.String toString() {
        return ((("Pair{" + java.lang.String.valueOf(first)) + " ") + java.lang.String.valueOf(second)) + "}";
    }

    /**
     * Convenience method for creating an appropriately typed pair.
     *
     * @param a
     * 		the first object in the Pair
     * @param b
     * 		the second object in the pair
     * @return a Pair that is templatized with the types of a and b
     */
    public static <A, B> android.support.v4.util.Pair<A, B> create(A a, B b) {
        return new android.support.v4.util.Pair<A, B>(a, b);
    }
}

