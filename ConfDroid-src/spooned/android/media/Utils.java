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
package android.media;


// package private
class Utils {
    private static final java.lang.String TAG = "Utils";

    /**
     * Sorts distinct (non-intersecting) range array in ascending order.
     *
     * @throws java.lang.IllegalArgumentException
     * 		if ranges are not distinct
     */
    public static <T extends java.lang.Comparable<? super T>> void sortDistinctRanges(android.util.Range<T>[] ranges) {
        java.util.Arrays.sort(ranges, new java.util.Comparator<android.util.Range<T>>() {
            @java.lang.Override
            public int compare(android.util.Range<T> lhs, android.util.Range<T> rhs) {
                if (lhs.getUpper().compareTo(rhs.getLower()) < 0) {
                    return -1;
                } else
                    if (lhs.getLower().compareTo(rhs.getUpper()) > 0) {
                        return 1;
                    }

                throw new java.lang.IllegalArgumentException(((("sample rate ranges must be distinct (" + lhs) + " and ") + rhs) + ")");
            }
        });
    }

    /**
     * Returns the intersection of two sets of non-intersecting ranges
     *
     * @param one
     * 		a sorted set of non-intersecting ranges in ascending order
     * @param another
     * 		another sorted set of non-intersecting ranges in ascending order
     * @return the intersection of the two sets, sorted in ascending order
     */
    public static <T extends java.lang.Comparable<? super T>> android.util.Range<T>[] intersectSortedDistinctRanges(android.util.Range<T>[] one, android.util.Range<T>[] another) {
        int ix = 0;
        java.util.Vector<android.util.Range<T>> result = new java.util.Vector<android.util.Range<T>>();
        for (android.util.Range<T> range : another) {
            while ((ix < one.length) && (one[ix].getUpper().compareTo(range.getLower()) < 0)) {
                ++ix;
            } 
            while ((ix < one.length) && (one[ix].getUpper().compareTo(range.getUpper()) < 0)) {
                result.add(range.intersect(one[ix]));
                ++ix;
            } 
            if (ix == one.length) {
                break;
            }
            if (one[ix].getLower().compareTo(range.getUpper()) <= 0) {
                result.add(range.intersect(one[ix]));
            }
        }
        return result.toArray(new android.util.Range[result.size()]);
    }

    /**
     * Returns the index of the range that contains a value in a sorted array of distinct ranges.
     *
     * @param ranges
     * 		a sorted array of non-intersecting ranges in ascending order
     * @param value
     * 		the value to search for
     * @return if the value is in one of the ranges, it returns the index of that range.  Otherwise,
    the return value is {@code (-1-index)} for the {@code index} of the range that is
    immediately following {@code value}.
     */
    public static <T extends java.lang.Comparable<? super T>> int binarySearchDistinctRanges(android.util.Range<T>[] ranges, T value) {
        return java.util.Arrays.binarySearch(ranges, android.util.Range.create(value, value), new java.util.Comparator<android.util.Range<T>>() {
            @java.lang.Override
            public int compare(android.util.Range<T> lhs, android.util.Range<T> rhs) {
                if (lhs.getUpper().compareTo(rhs.getLower()) < 0) {
                    return -1;
                } else
                    if (lhs.getLower().compareTo(rhs.getUpper()) > 0) {
                        return 1;
                    }

                return 0;
            }
        });
    }

    /**
     * Returns greatest common divisor
     */
    static int gcd(int a, int b) {
        if ((a == 0) && (b == 0)) {
            return 1;
        }
        if (b < 0) {
            b = -b;
        }
        if (a < 0) {
            a = -a;
        }
        while (a != 0) {
            int c = b % a;
            b = a;
            a = c;
        } 
        return b;
    }

    /**
     * Returns the equivalent factored range {@code newrange}, where for every
     * {@code e}: {@code newrange.contains(e)} implies that {@code range.contains(e * factor)},
     * and {@code !newrange.contains(e)} implies that {@code !range.contains(e * factor)}.
     */
    static android.util.Range<java.lang.Integer> factorRange(android.util.Range<java.lang.Integer> range, int factor) {
        if (factor == 1) {
            return range;
        }
        return android.util.Range.create(android.media.Utils.divUp(range.getLower(), factor), range.getUpper() / factor);
    }

    /**
     * Returns the equivalent factored range {@code newrange}, where for every
     * {@code e}: {@code newrange.contains(e)} implies that {@code range.contains(e * factor)},
     * and {@code !newrange.contains(e)} implies that {@code !range.contains(e * factor)}.
     */
    static android.util.Range<java.lang.Long> factorRange(android.util.Range<java.lang.Long> range, long factor) {
        if (factor == 1) {
            return range;
        }
        return android.util.Range.create(android.media.Utils.divUp(range.getLower(), factor), range.getUpper() / factor);
    }

    private static android.util.Rational scaleRatio(android.util.Rational ratio, int num, int den) {
        int common = android.media.Utils.gcd(num, den);
        num /= common;
        den /= common;
        return // saturate to int
        new android.util.Rational(((int) (ratio.getNumerator() * ((double) (num)))), ((int) (ratio.getDenominator() * ((double) (den)))));// saturate to int

    }

    static android.util.Range<android.util.Rational> scaleRange(android.util.Range<android.util.Rational> range, int num, int den) {
        if (num == den) {
            return range;
        }
        return android.util.Range.create(android.media.Utils.scaleRatio(range.getLower(), num, den), android.media.Utils.scaleRatio(range.getUpper(), num, den));
    }

    static android.util.Range<java.lang.Integer> alignRange(android.util.Range<java.lang.Integer> range, int align) {
        return range.intersect(android.media.Utils.divUp(range.getLower(), align) * align, (range.getUpper() / align) * align);
    }

    static int divUp(int num, int den) {
        return ((num + den) - 1) / den;
    }

    static long divUp(long num, long den) {
        return ((num + den) - 1) / den;
    }

    /**
     * Returns least common multiple
     */
    private static long lcm(int a, int b) {
        if ((a == 0) || (b == 0)) {
            throw new java.lang.IllegalArgumentException("lce is not defined for zero arguments");
        }
        return (((long) (a)) * b) / android.media.Utils.gcd(a, b);
    }

    static android.util.Range<java.lang.Integer> intRangeFor(double v) {
        return android.util.Range.create(((int) (v)), ((int) (java.lang.Math.ceil(v))));
    }

    static android.util.Range<java.lang.Long> longRangeFor(double v) {
        return android.util.Range.create(((long) (v)), ((long) (java.lang.Math.ceil(v))));
    }

    static android.util.Size parseSize(java.lang.Object o, android.util.Size fallback) {
        try {
            return android.util.Size.parseSize(((java.lang.String) (o)));
        } catch (java.lang.ClassCastException e) {
        } catch (java.lang.NumberFormatException e) {
        } catch (java.lang.NullPointerException e) {
            return fallback;
        }
        android.util.Log.w(android.media.Utils.TAG, ("could not parse size '" + o) + "'");
        return fallback;
    }

    static int parseIntSafely(java.lang.Object o, int fallback) {
        if (o == null) {
            return fallback;
        }
        try {
            java.lang.String s = ((java.lang.String) (o));
            return java.lang.Integer.parseInt(s);
        } catch (java.lang.ClassCastException e) {
        } catch (java.lang.NumberFormatException e) {
        } catch (java.lang.NullPointerException e) {
            return fallback;
        }
        android.util.Log.w(android.media.Utils.TAG, ("could not parse integer '" + o) + "'");
        return fallback;
    }

    static android.util.Range<java.lang.Integer> parseIntRange(java.lang.Object o, android.util.Range<java.lang.Integer> fallback) {
        try {
            java.lang.String s = ((java.lang.String) (o));
            int ix = s.indexOf('-');
            if (ix >= 0) {
                return android.util.Range.create(java.lang.Integer.parseInt(s.substring(0, ix), 10), java.lang.Integer.parseInt(s.substring(ix + 1), 10));
            }
            int value = java.lang.Integer.parseInt(s);
            return android.util.Range.create(value, value);
        } catch (java.lang.ClassCastException e) {
        } catch (java.lang.NumberFormatException e) {
        } catch (java.lang.NullPointerException e) {
            return fallback;
        } catch (java.lang.IllegalArgumentException e) {
        }
        android.util.Log.w(android.media.Utils.TAG, ("could not parse integer range '" + o) + "'");
        return fallback;
    }

    static android.util.Range<java.lang.Long> parseLongRange(java.lang.Object o, android.util.Range<java.lang.Long> fallback) {
        try {
            java.lang.String s = ((java.lang.String) (o));
            int ix = s.indexOf('-');
            if (ix >= 0) {
                return android.util.Range.create(java.lang.Long.parseLong(s.substring(0, ix), 10), java.lang.Long.parseLong(s.substring(ix + 1), 10));
            }
            long value = java.lang.Long.parseLong(s);
            return android.util.Range.create(value, value);
        } catch (java.lang.ClassCastException e) {
        } catch (java.lang.NumberFormatException e) {
        } catch (java.lang.NullPointerException e) {
            return fallback;
        } catch (java.lang.IllegalArgumentException e) {
        }
        android.util.Log.w(android.media.Utils.TAG, ("could not parse long range '" + o) + "'");
        return fallback;
    }

    static android.util.Range<android.util.Rational> parseRationalRange(java.lang.Object o, android.util.Range<android.util.Rational> fallback) {
        try {
            java.lang.String s = ((java.lang.String) (o));
            int ix = s.indexOf('-');
            if (ix >= 0) {
                return android.util.Range.create(android.util.Rational.parseRational(s.substring(0, ix)), android.util.Rational.parseRational(s.substring(ix + 1)));
            }
            android.util.Rational value = android.util.Rational.parseRational(s);
            return android.util.Range.create(value, value);
        } catch (java.lang.ClassCastException e) {
        } catch (java.lang.NumberFormatException e) {
        } catch (java.lang.NullPointerException e) {
            return fallback;
        } catch (java.lang.IllegalArgumentException e) {
        }
        android.util.Log.w(android.media.Utils.TAG, ("could not parse rational range '" + o) + "'");
        return fallback;
    }

    static android.util.Pair<android.util.Size, android.util.Size> parseSizeRange(java.lang.Object o) {
        try {
            java.lang.String s = ((java.lang.String) (o));
            int ix = s.indexOf('-');
            if (ix >= 0) {
                return android.util.Pair.create(android.util.Size.parseSize(s.substring(0, ix)), android.util.Size.parseSize(s.substring(ix + 1)));
            }
            android.util.Size value = android.util.Size.parseSize(s);
            return android.util.Pair.create(value, value);
        } catch (java.lang.ClassCastException e) {
        } catch (java.lang.NumberFormatException e) {
        } catch (java.lang.NullPointerException e) {
            return null;
        } catch (java.lang.IllegalArgumentException e) {
        }
        android.util.Log.w(android.media.Utils.TAG, ("could not parse size range '" + o) + "'");
        return null;
    }
}

