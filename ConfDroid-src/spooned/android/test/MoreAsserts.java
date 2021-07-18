/**
 * Copyright (C) 2007 The Android Open Source Project
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
package android.test;


/**
 * Contains additional assertion methods not found in JUnit.
 *
 * @deprecated Use
<a href="https://github.com/hamcrest">Hamcrest matchers</a> instead.
 */
@java.lang.Deprecated
public final class MoreAsserts {
    private MoreAsserts() {
    }

    /**
     * Asserts that the class  {@code expected} is assignable from the object
     * {@code actual}. This verifies {@code expected} is a parent class or a
     * interface that {@code actual} implements.
     */
    public static void assertAssignableFrom(java.lang.Class<?> expected, java.lang.Object actual) {
        android.test.MoreAsserts.assertAssignableFrom(expected, actual.getClass());
    }

    /**
     * Asserts that class {@code expected} is assignable from the class
     * {@code actual}. This verifies {@code expected} is a parent class or a
     * interface that {@code actual} implements.
     */
    public static void assertAssignableFrom(java.lang.Class<?> expected, java.lang.Class<?> actual) {
        junit.framework.Assert.assertTrue((("Expected " + expected.getCanonicalName()) + " to be assignable from actual class ") + actual.getCanonicalName(), expected.isAssignableFrom(actual));
    }

    /**
     * Asserts that {@code actual} is not equal {@code unexpected}, according
     * to both {@code ==} and {@link Object#equals}.
     */
    public static void assertNotEqual(java.lang.String message, java.lang.Object unexpected, java.lang.Object actual) {
        if (android.test.MoreAsserts.equal(unexpected, actual)) {
            android.test.MoreAsserts.failEqual(message, unexpected);
        }
    }

    /**
     * Variant of {@link #assertNotEqual(String,Object,Object)} using a
     * generic message.
     */
    public static void assertNotEqual(java.lang.Object unexpected, java.lang.Object actual) {
        android.test.MoreAsserts.assertNotEqual(null, unexpected, actual);
    }

    /**
     * Asserts that array {@code actual} is the same size and every element equals
     * those in array {@code expected}. On failure, message indicates specific
     * element mismatch.
     */
    public static void assertEquals(java.lang.String message, byte[] expected, byte[] actual) {
        if (expected.length != actual.length) {
            android.test.MoreAsserts.failWrongLength(message, expected.length, actual.length);
        }
        for (int i = 0; i < expected.length; i++) {
            if (expected[i] != actual[i]) {
                android.test.MoreAsserts.failWrongElement(message, i, expected[i], actual[i]);
            }
        }
    }

    /**
     * Asserts that array {@code actual} is the same size and every element equals
     * those in array {@code expected}. On failure, message indicates specific
     * element mismatch.
     */
    public static void assertEquals(byte[] expected, byte[] actual) {
        android.test.MoreAsserts.assertEquals(null, expected, actual);
    }

    /**
     * Asserts that array {@code actual} is the same size and every element equals
     * those in array {@code expected}. On failure, message indicates first
     * specific element mismatch.
     */
    public static void assertEquals(java.lang.String message, int[] expected, int[] actual) {
        if (expected.length != actual.length) {
            android.test.MoreAsserts.failWrongLength(message, expected.length, actual.length);
        }
        for (int i = 0; i < expected.length; i++) {
            if (expected[i] != actual[i]) {
                android.test.MoreAsserts.failWrongElement(message, i, expected[i], actual[i]);
            }
        }
    }

    /**
     * Asserts that array {@code actual} is the same size and every element equals
     * those in array {@code expected}. On failure, message indicates first
     * specific element mismatch.
     */
    public static void assertEquals(int[] expected, int[] actual) {
        android.test.MoreAsserts.assertEquals(null, expected, actual);
    }

    /**
     *
     *
     * @unknown Asserts that array {@code actual} is the same size and every element equals
    those in array {@code expected}. On failure, message indicates first
    specific element mismatch.
     */
    public static void assertEquals(java.lang.String message, long[] expected, long[] actual) {
        if (expected.length != actual.length) {
            android.test.MoreAsserts.failWrongLength(message, expected.length, actual.length);
        }
        for (int i = 0; i < expected.length; i++) {
            if (expected[i] != actual[i]) {
                android.test.MoreAsserts.failWrongElement(message, i, expected[i], actual[i]);
            }
        }
    }

    /**
     *
     *
     * @unknown Asserts that array {@code actual} is the same size and every element equals
    those in array {@code expected}. On failure, message indicates first
    specific element mismatch.
     */
    public static void assertEquals(long[] expected, long[] actual) {
        android.test.MoreAsserts.assertEquals(null, expected, actual);
    }

    /**
     * Asserts that array {@code actual} is the same size and every element equals
     * those in array {@code expected}. On failure, message indicates first
     * specific element mismatch.
     */
    public static void assertEquals(java.lang.String message, double[] expected, double[] actual) {
        if (expected.length != actual.length) {
            android.test.MoreAsserts.failWrongLength(message, expected.length, actual.length);
        }
        for (int i = 0; i < expected.length; i++) {
            if (expected[i] != actual[i]) {
                android.test.MoreAsserts.failWrongElement(message, i, expected[i], actual[i]);
            }
        }
    }

    /**
     * Asserts that array {@code actual} is the same size and every element equals
     * those in array {@code expected}. On failure, message indicates first
     * specific element mismatch.
     */
    public static void assertEquals(double[] expected, double[] actual) {
        android.test.MoreAsserts.assertEquals(null, expected, actual);
    }

    /**
     * Asserts that array {@code actual} is the same size and every element
     * is the same as those in array {@code expected}. Note that this uses
     * {@code equals()} instead of {@code ==} to compare the objects.
     * {@code null} will be considered equal to {@code null} (unlike SQL).
     * On failure, message indicates first specific element mismatch.
     */
    public static void assertEquals(java.lang.String message, java.lang.Object[] expected, java.lang.Object[] actual) {
        if (expected.length != actual.length) {
            android.test.MoreAsserts.failWrongLength(message, expected.length, actual.length);
        }
        for (int i = 0; i < expected.length; i++) {
            java.lang.Object exp = expected[i];
            java.lang.Object act = actual[i];
            // The following borrowed from java.util.equals(Object[], Object[]).
            if (!(exp == null ? act == null : exp.equals(act))) {
                android.test.MoreAsserts.failWrongElement(message, i, exp, act);
            }
        }
    }

    /**
     * Asserts that array {@code actual} is the same size and every element
     * is the same as those in array {@code expected}. Note that this uses
     * {@code ==} instead of {@code equals()} to compare the objects.
     * On failure, message indicates first specific element mismatch.
     */
    public static void assertEquals(java.lang.Object[] expected, java.lang.Object[] actual) {
        android.test.MoreAsserts.assertEquals(null, expected, actual);
    }

    /**
     * Asserts that two sets contain the same elements.
     */
    public static void assertEquals(java.lang.String message, java.util.Set<? extends java.lang.Object> expected, java.util.Set<? extends java.lang.Object> actual) {
        java.util.Set<java.lang.Object> onlyInExpected = new java.util.HashSet<java.lang.Object>(expected);
        onlyInExpected.removeAll(actual);
        java.util.Set<java.lang.Object> onlyInActual = new java.util.HashSet<java.lang.Object>(actual);
        onlyInActual.removeAll(expected);
        if ((onlyInExpected.size() != 0) || (onlyInActual.size() != 0)) {
            java.util.Set<java.lang.Object> intersection = new java.util.HashSet<java.lang.Object>(expected);
            intersection.retainAll(actual);
            android.test.MoreAsserts.failWithMessage(message, (((("Sets do not match.\nOnly in expected: " + onlyInExpected) + "\nOnly in actual: ") + onlyInActual) + "\nIntersection: ") + intersection);
        }
    }

    /**
     * Asserts that two sets contain the same elements.
     */
    public static void assertEquals(java.util.Set<? extends java.lang.Object> expected, java.util.Set<? extends java.lang.Object> actual) {
        android.test.MoreAsserts.assertEquals(null, expected, actual);
    }

    /**
     * Asserts that {@code expectedRegex} exactly matches {@code actual} and
     * fails with {@code message} if it does not.  The MatchResult is returned
     * in case the test needs access to any captured groups.  Note that you can
     * also use this for a literal string, by wrapping your expected string in
     * {@link Pattern#quote}.
     */
    public static java.util.regex.MatchResult assertMatchesRegex(java.lang.String message, java.lang.String expectedRegex, java.lang.String actual) {
        if (actual == null) {
            android.test.MoreAsserts.failNotMatches(message, expectedRegex, actual);
        }
        java.util.regex.Matcher matcher = android.test.MoreAsserts.getMatcher(expectedRegex, actual);
        if (!matcher.matches()) {
            android.test.MoreAsserts.failNotMatches(message, expectedRegex, actual);
        }
        return matcher;
    }

    /**
     * Variant of {@link #assertMatchesRegex(String,String,String)} using a
     * generic message.
     */
    public static java.util.regex.MatchResult assertMatchesRegex(java.lang.String expectedRegex, java.lang.String actual) {
        return android.test.MoreAsserts.assertMatchesRegex(null, expectedRegex, actual);
    }

    /**
     * Asserts that {@code expectedRegex} matches any substring of {@code actual}
     * and fails with {@code message} if it does not.  The Matcher is returned in
     * case the test needs access to any captured groups.  Note that you can also
     * use this for a literal string, by wrapping your expected string in
     * {@link Pattern#quote}.
     */
    public static java.util.regex.MatchResult assertContainsRegex(java.lang.String message, java.lang.String expectedRegex, java.lang.String actual) {
        if (actual == null) {
            android.test.MoreAsserts.failNotContains(message, expectedRegex, actual);
        }
        java.util.regex.Matcher matcher = android.test.MoreAsserts.getMatcher(expectedRegex, actual);
        if (!matcher.find()) {
            android.test.MoreAsserts.failNotContains(message, expectedRegex, actual);
        }
        return matcher;
    }

    /**
     * Variant of {@link #assertContainsRegex(String,String,String)} using a
     * generic message.
     */
    public static java.util.regex.MatchResult assertContainsRegex(java.lang.String expectedRegex, java.lang.String actual) {
        return android.test.MoreAsserts.assertContainsRegex(null, expectedRegex, actual);
    }

    /**
     * Asserts that {@code expectedRegex} does not exactly match {@code actual},
     * and fails with {@code message} if it does. Note that you can also use
     * this for a literal string, by wrapping your expected string in
     * {@link Pattern#quote}.
     */
    public static void assertNotMatchesRegex(java.lang.String message, java.lang.String expectedRegex, java.lang.String actual) {
        java.util.regex.Matcher matcher = android.test.MoreAsserts.getMatcher(expectedRegex, actual);
        if (matcher.matches()) {
            android.test.MoreAsserts.failMatch(message, expectedRegex, actual);
        }
    }

    /**
     * Variant of {@link #assertNotMatchesRegex(String,String,String)} using a
     * generic message.
     */
    public static void assertNotMatchesRegex(java.lang.String expectedRegex, java.lang.String actual) {
        android.test.MoreAsserts.assertNotMatchesRegex(null, expectedRegex, actual);
    }

    /**
     * Asserts that {@code expectedRegex} does not match any substring of
     * {@code actual}, and fails with {@code message} if it does.  Note that you
     * can also use this for a literal string, by wrapping your expected string
     * in {@link Pattern#quote}.
     */
    public static void assertNotContainsRegex(java.lang.String message, java.lang.String expectedRegex, java.lang.String actual) {
        java.util.regex.Matcher matcher = android.test.MoreAsserts.getMatcher(expectedRegex, actual);
        if (matcher.find()) {
            android.test.MoreAsserts.failContains(message, expectedRegex, actual);
        }
    }

    /**
     * Variant of {@link #assertNotContainsRegex(String,String,String)} using a
     * generic message.
     */
    public static void assertNotContainsRegex(java.lang.String expectedRegex, java.lang.String actual) {
        android.test.MoreAsserts.assertNotContainsRegex(null, expectedRegex, actual);
    }

    /**
     * Asserts that {@code actual} contains precisely the elements
     * {@code expected}, and in the same order.
     */
    public static void assertContentsInOrder(java.lang.String message, java.lang.Iterable<?> actual, java.lang.Object... expected) {
        java.util.ArrayList actualList = new java.util.ArrayList();
        for (java.lang.Object o : actual) {
            actualList.add(o);
        }
        junit.framework.Assert.assertEquals(message, java.util.Arrays.asList(expected), actualList);
    }

    /**
     * Variant of assertContentsInOrder(String, Iterable<?>, Object...)
     * using a generic message.
     */
    public static void assertContentsInOrder(java.lang.Iterable<?> actual, java.lang.Object... expected) {
        android.test.MoreAsserts.assertContentsInOrder(((java.lang.String) (null)), actual, expected);
    }

    /**
     * Asserts that {@code actual} contains precisely the elements
     * {@code expected}, but in any order.
     */
    public static void assertContentsInAnyOrder(java.lang.String message, java.lang.Iterable<?> actual, java.lang.Object... expected) {
        java.util.HashMap<java.lang.Object, java.lang.Object> expectedMap = new java.util.HashMap<java.lang.Object, java.lang.Object>(expected.length);
        for (java.lang.Object expectedObj : expected) {
            expectedMap.put(expectedObj, expectedObj);
        }
        for (java.lang.Object actualObj : actual) {
            if (expectedMap.remove(actualObj) == null) {
                android.test.MoreAsserts.failWithMessage(message, ("Extra object in actual: (" + actualObj.toString()) + ")");
            }
        }
        if (expectedMap.size() > 0) {
            android.test.MoreAsserts.failWithMessage(message, "Extra objects in expected.");
        }
    }

    /**
     * Variant of assertContentsInAnyOrder(String, Iterable<?>, Object...)
     * using a generic message.
     */
    public static void assertContentsInAnyOrder(java.lang.Iterable<?> actual, java.lang.Object... expected) {
        android.test.MoreAsserts.assertContentsInAnyOrder(((java.lang.String) (null)), actual, expected);
    }

    /**
     * Asserts that {@code iterable} is empty.
     */
    public static void assertEmpty(java.lang.String message, java.lang.Iterable<?> iterable) {
        if (iterable.iterator().hasNext()) {
            android.test.MoreAsserts.failNotEmpty(message, iterable.toString());
        }
    }

    /**
     * Variant of {@link #assertEmpty(String, Iterable)} using a
     * generic message.
     */
    public static void assertEmpty(java.lang.Iterable<?> iterable) {
        android.test.MoreAsserts.assertEmpty(null, iterable);
    }

    /**
     * Asserts that {@code map} is empty.
     */
    public static void assertEmpty(java.lang.String message, java.util.Map<?, ?> map) {
        if (!map.isEmpty()) {
            android.test.MoreAsserts.failNotEmpty(message, map.toString());
        }
    }

    /**
     * Variant of {@link #assertEmpty(String, Map)} using a generic
     * message.
     */
    public static void assertEmpty(java.util.Map<?, ?> map) {
        android.test.MoreAsserts.assertEmpty(null, map);
    }

    /**
     * Asserts that {@code iterable} is not empty.
     */
    public static void assertNotEmpty(java.lang.String message, java.lang.Iterable<?> iterable) {
        if (!iterable.iterator().hasNext()) {
            android.test.MoreAsserts.failEmpty(message);
        }
    }

    /**
     * Variant of assertNotEmpty(String, Iterable<?>)
     * using a generic message.
     */
    public static void assertNotEmpty(java.lang.Iterable<?> iterable) {
        android.test.MoreAsserts.assertNotEmpty(null, iterable);
    }

    /**
     * Asserts that {@code map} is not empty.
     */
    public static void assertNotEmpty(java.lang.String message, java.util.Map<?, ?> map) {
        if (map.isEmpty()) {
            android.test.MoreAsserts.failEmpty(message);
        }
    }

    /**
     * Variant of {@link #assertNotEmpty(String, Map)} using a generic
     * message.
     */
    public static void assertNotEmpty(java.util.Map<?, ?> map) {
        android.test.MoreAsserts.assertNotEmpty(null, map);
    }

    /**
     * Utility for testing equals() and hashCode() results at once.
     * Tests that lhs.equals(rhs) matches expectedResult, as well as
     * rhs.equals(lhs).  Also tests that hashCode() return values are
     * equal if expectedResult is true.  (hashCode() is not tested if
     * expectedResult is false, as unequal objects can have equal hashCodes.)
     *
     * @param lhs
     * 		An Object for which equals() and hashCode() are to be tested.
     * @param rhs
     * 		As lhs.
     * @param expectedResult
     * 		True if the objects should compare equal,
     * 		false if not.
     */
    public static void checkEqualsAndHashCodeMethods(java.lang.String message, java.lang.Object lhs, java.lang.Object rhs, boolean expectedResult) {
        if ((lhs == null) && (rhs == null)) {
            junit.framework.Assert.assertTrue("Your check is dubious...why would you expect null != null?", expectedResult);
            return;
        }
        if ((lhs == null) || (rhs == null)) {
            junit.framework.Assert.assertFalse("Your check is dubious...why would you expect an object " + "to be equal to null?", expectedResult);
        }
        if (lhs != null) {
            junit.framework.Assert.assertEquals(message, expectedResult, lhs.equals(rhs));
        }
        if (rhs != null) {
            junit.framework.Assert.assertEquals(message, expectedResult, rhs.equals(lhs));
        }
        if (expectedResult) {
            java.lang.String hashMessage = "hashCode() values for equal objects should be the same";
            if (message != null) {
                hashMessage += ": " + message;
            }
            junit.framework.Assert.assertTrue(hashMessage, lhs.hashCode() == rhs.hashCode());
        }
    }

    /**
     * Variant of
     * checkEqualsAndHashCodeMethods(String,Object,Object,boolean...)}
     * using a generic message.
     */
    public static void checkEqualsAndHashCodeMethods(java.lang.Object lhs, java.lang.Object rhs, boolean expectedResult) {
        android.test.MoreAsserts.checkEqualsAndHashCodeMethods(((java.lang.String) (null)), lhs, rhs, expectedResult);
    }

    private static java.util.regex.Matcher getMatcher(java.lang.String expectedRegex, java.lang.String actual) {
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(expectedRegex);
        return pattern.matcher(actual);
    }

    private static void failEqual(java.lang.String message, java.lang.Object unexpected) {
        android.test.MoreAsserts.failWithMessage(message, ("expected not to be:<" + unexpected) + ">");
    }

    private static void failWrongLength(java.lang.String message, int expected, int actual) {
        android.test.MoreAsserts.failWithMessage(message, ((("expected array length:<" + expected) + "> but was:<") + actual) + '>');
    }

    private static void failWrongElement(java.lang.String message, int index, java.lang.Object expected, java.lang.Object actual) {
        android.test.MoreAsserts.failWithMessage(message, ((((("expected array element[" + index) + "]:<") + expected) + "> but was:<") + actual) + '>');
    }

    private static void failNotMatches(java.lang.String message, java.lang.String expectedRegex, java.lang.String actual) {
        java.lang.String actualDesc = (actual == null) ? "null" : ('<' + actual) + '>';
        android.test.MoreAsserts.failWithMessage(message, (("expected to match regex:<" + expectedRegex) + "> but was:") + actualDesc);
    }

    private static void failNotContains(java.lang.String message, java.lang.String expectedRegex, java.lang.String actual) {
        java.lang.String actualDesc = (actual == null) ? "null" : ('<' + actual) + '>';
        android.test.MoreAsserts.failWithMessage(message, (("expected to contain regex:<" + expectedRegex) + "> but was:") + actualDesc);
    }

    private static void failMatch(java.lang.String message, java.lang.String expectedRegex, java.lang.String actual) {
        android.test.MoreAsserts.failWithMessage(message, ((("expected not to match regex:<" + expectedRegex) + "> but was:<") + actual) + '>');
    }

    private static void failContains(java.lang.String message, java.lang.String expectedRegex, java.lang.String actual) {
        android.test.MoreAsserts.failWithMessage(message, ((("expected not to contain regex:<" + expectedRegex) + "> but was:<") + actual) + '>');
    }

    private static void failNotEmpty(java.lang.String message, java.lang.String actual) {
        android.test.MoreAsserts.failWithMessage(message, ("expected to be empty, but contained: <" + actual) + ">");
    }

    private static void failEmpty(java.lang.String message) {
        android.test.MoreAsserts.failWithMessage(message, "expected not to be empty, but was");
    }

    private static void failWithMessage(java.lang.String userMessage, java.lang.String ourMessage) {
        junit.framework.Assert.fail(userMessage == null ? ourMessage : (userMessage + ' ') + ourMessage);
    }

    private static boolean equal(java.lang.Object a, java.lang.Object b) {
        return (a == b) || ((a != null) && a.equals(b));
    }
}

