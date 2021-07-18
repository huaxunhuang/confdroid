/**
 * Copyright (C) 2008 The Android Open Source Project
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
package android.view;


public class FocusFinderTest extends android.test.AndroidTestCase {
    private android.view.FocusFinderHelper mFocusFinder;

    @java.lang.Override
    protected void setUp() throws java.lang.Exception {
        setUp();
        mFocusFinder = new android.view.FocusFinderHelper(android.view.FocusFinder.getInstance());
    }

    @android.test.suitebuilder.annotation.SmallTest
    public void testPreconditions() {
        assertNotNull("focus finder instance", mFocusFinder);
    }

    @android.test.suitebuilder.annotation.SmallTest
    public void testBelowNotCandidateForDirectionUp() {
        // src  (left, top, right, bottom)
        assertIsNotCandidate(android.view.View.FOCUS_UP, new android.graphics.Rect(0, 30, 10, 40), new android.graphics.Rect(0, 50, 10, 60));// dest (left, top, right, bottom)

    }

    @android.test.suitebuilder.annotation.SmallTest
    public void testAboveShareEdgeEdgeOkForDirectionUp() {
        final android.graphics.Rect src = new android.graphics.Rect(0, 30, 10, 40);
        final android.graphics.Rect dest = new android.graphics.Rect(src);
        dest.offset(0, -src.height());
        assertEquals(src.top, dest.bottom);
        assertDirectionIsCandidate(android.view.View.FOCUS_UP, src, dest);
    }

    @android.test.suitebuilder.annotation.SmallTest
    public void testCompletelyContainedNotCandidate() {
        // L  T   R   B
        assertIsNotCandidate(android.view.View.FOCUS_DOWN, new android.graphics.Rect(0, 0, 50, 50), new android.graphics.Rect(0, 1, 50, 49));
    }

    @android.test.suitebuilder.annotation.SmallTest
    public void testContinaedWithCommonBottomNotCandidate() {
        // L  T   R   B
        assertIsNotCandidate(android.view.View.FOCUS_DOWN, new android.graphics.Rect(0, 0, 50, 50), new android.graphics.Rect(0, 1, 50, 50));
    }

    @android.test.suitebuilder.annotation.SmallTest
    public void testOverlappingIsCandidateWhenBothEdgesAreInDirection() {
        // L  T   R   B
        assertDirectionIsCandidate(android.view.View.FOCUS_DOWN, new android.graphics.Rect(0, 0, 50, 50), new android.graphics.Rect(0, 1, 50, 51));
    }

    @android.test.suitebuilder.annotation.SmallTest
    public void testTopEdgeOfDestAtOrAboveTopOfSrcNotCandidateForDown() {
        // L  T   R   B
        assertIsNotCandidate(android.view.View.FOCUS_DOWN, new android.graphics.Rect(0, 0, 50, 50), new android.graphics.Rect(0, 0, 50, 51));
        // L  T   R   B
        assertIsNotCandidate(android.view.View.FOCUS_DOWN, new android.graphics.Rect(0, 0, 50, 50), new android.graphics.Rect(0, -1, 50, 51));
    }

    @android.test.suitebuilder.annotation.SmallTest
    public void testSameRectBeamsOverlap() {
        final android.graphics.Rect rect = new android.graphics.Rect(0, 0, 20, 20);
        assertBeamsOverlap(android.view.View.FOCUS_LEFT, rect, rect);
        assertBeamsOverlap(android.view.View.FOCUS_RIGHT, rect, rect);
        assertBeamsOverlap(android.view.View.FOCUS_UP, rect, rect);
        assertBeamsOverlap(android.view.View.FOCUS_DOWN, rect, rect);
    }

    @android.test.suitebuilder.annotation.SmallTest
    public void testOverlapBeamsRightLeftUpToEdge() {
        final android.graphics.Rect rect1 = new android.graphics.Rect(0, 0, 20, 20);
        final android.graphics.Rect rect2 = new android.graphics.Rect(rect1);
        // just below bottom edge
        rect2.offset(0, rect1.height() - 1);
        assertBeamsOverlap(android.view.View.FOCUS_LEFT, rect1, rect2);
        assertBeamsOverlap(android.view.View.FOCUS_RIGHT, rect1, rect2);
        // at edge
        rect2.offset(0, 1);
        assertBeamsOverlap(android.view.View.FOCUS_LEFT, rect1, rect2);
        assertBeamsOverlap(android.view.View.FOCUS_RIGHT, rect1, rect2);
        // just beyond
        rect2.offset(0, 1);
        assertBeamsDontOverlap(android.view.View.FOCUS_LEFT, rect1, rect2);
        assertBeamsDontOverlap(android.view.View.FOCUS_RIGHT, rect1, rect2);
        // just below top edge
        rect2.set(rect1);
        rect2.offset(0, -(rect1.height() - 1));
        assertBeamsOverlap(android.view.View.FOCUS_LEFT, rect1, rect2);
        assertBeamsOverlap(android.view.View.FOCUS_RIGHT, rect1, rect2);
        // at top edge
        rect2.offset(0, -1);
        assertBeamsOverlap(android.view.View.FOCUS_LEFT, rect1, rect2);
        assertBeamsOverlap(android.view.View.FOCUS_RIGHT, rect1, rect2);
        // just beyond top edge
        rect2.offset(0, -1);
        assertBeamsDontOverlap(android.view.View.FOCUS_LEFT, rect1, rect2);
        assertBeamsDontOverlap(android.view.View.FOCUS_RIGHT, rect1, rect2);
    }

    @android.test.suitebuilder.annotation.SmallTest
    public void testOverlapBeamsUpDownUpToEdge() {
        final android.graphics.Rect rect1 = new android.graphics.Rect(0, 0, 20, 20);
        final android.graphics.Rect rect2 = new android.graphics.Rect(rect1);
        // just short of right edge
        rect2.offset(rect1.width() - 1, 0);
        assertBeamsOverlap(android.view.View.FOCUS_UP, rect1, rect2);
        assertBeamsOverlap(android.view.View.FOCUS_DOWN, rect1, rect2);
        // at edge
        rect2.offset(1, 0);
        assertBeamsOverlap(android.view.View.FOCUS_UP, rect1, rect2);
        assertBeamsOverlap(android.view.View.FOCUS_DOWN, rect1, rect2);
        // just beyond
        rect2.offset(1, 0);
        assertBeamsDontOverlap(android.view.View.FOCUS_UP, rect1, rect2);
        assertBeamsDontOverlap(android.view.View.FOCUS_DOWN, rect1, rect2);
        // just short of left edge
        rect2.set(rect1);
        rect2.offset(-(rect1.width() - 1), 0);
        assertBeamsOverlap(android.view.View.FOCUS_UP, rect1, rect2);
        assertBeamsOverlap(android.view.View.FOCUS_DOWN, rect1, rect2);
        // at edge
        rect2.offset(-1, 0);
        assertBeamsOverlap(android.view.View.FOCUS_UP, rect1, rect2);
        assertBeamsOverlap(android.view.View.FOCUS_DOWN, rect1, rect2);
        // just beyond edge
        rect2.offset(-1, 0);
        assertBeamsDontOverlap(android.view.View.FOCUS_UP, rect1, rect2);
        assertBeamsDontOverlap(android.view.View.FOCUS_DOWN, rect1, rect2);
    }

    @android.test.suitebuilder.annotation.SmallTest
    public void testDirectlyAboveTrumpsAboveLeft() {
        android.graphics.Rect src = new android.graphics.Rect(0, 50, 20, 70);// src (left, top, right, bottom)

        android.graphics.Rect directlyAbove = new android.graphics.Rect(src);
        directlyAbove.offset(0, -(1 + src.height()));
        android.graphics.Rect aboveLeft = new android.graphics.Rect(src);
        aboveLeft.offset(-(1 + src.width()), -(1 + src.height()));
        assertBetterCandidate(android.view.View.FOCUS_UP, src, directlyAbove, aboveLeft);
    }

    @android.test.suitebuilder.annotation.SmallTest
    public void testAboveInBeamTrumpsSlightlyCloserOutOfBeam() {
        android.graphics.Rect src = new android.graphics.Rect(0, 50, 20, 70);// src (left, top, right, bottom)

        android.graphics.Rect directlyAbove = new android.graphics.Rect(src);
        directlyAbove.offset(0, -(1 + src.height()));
        android.graphics.Rect aboveLeft = new android.graphics.Rect(src);
        aboveLeft.offset(-(1 + src.width()), -(1 + src.height()));
        // offset directly above a little further up
        directlyAbove.offset(0, -5);
        assertBetterCandidate(android.view.View.FOCUS_UP, src, directlyAbove, aboveLeft);
    }

    @android.test.suitebuilder.annotation.SmallTest
    public void testOutOfBeamBeatsInBeamUp() {
        android.graphics.Rect src = new android.graphics.Rect(0, 0, 50, 50);// (left, top, right, bottom)

        android.graphics.Rect aboveLeftOfBeam = new android.graphics.Rect(src);
        aboveLeftOfBeam.offset(-(src.width() + 1), -src.height());
        assertBeamsDontOverlap(android.view.View.FOCUS_UP, src, aboveLeftOfBeam);
        android.graphics.Rect aboveInBeam = new android.graphics.Rect(src);
        aboveInBeam.offset(0, -src.height());
        assertBeamsOverlap(android.view.View.FOCUS_UP, src, aboveInBeam);
        // in beam wins
        assertBetterCandidate(android.view.View.FOCUS_UP, src, aboveInBeam, aboveLeftOfBeam);
        // still wins while aboveInBeam's bottom edge is < out of beams' top
        aboveInBeam.offset(0, -(aboveLeftOfBeam.height() - 1));
        assertTrue("aboveInBeam.bottom > aboveLeftOfBeam.top", aboveInBeam.bottom > aboveLeftOfBeam.top);
        assertBetterCandidate(android.view.View.FOCUS_UP, src, aboveInBeam, aboveLeftOfBeam);
        // cross the threshold: the out of beam prevails
        aboveInBeam.offset(0, -1);
        assertEquals(aboveInBeam.bottom, aboveLeftOfBeam.top);
        assertBetterCandidate(android.view.View.FOCUS_UP, src, aboveLeftOfBeam, aboveInBeam);
    }

    /**
     * A non-candidate (even a much closer one) is always a worse choice
     * than a real candidate.
     */
    @android.test.suitebuilder.annotation.SmallTest
    public void testSomeCandidateBetterThanNonCandidate() {
        android.graphics.Rect src = new android.graphics.Rect(0, 0, 50, 50);// (left, top, right, bottom)

        android.graphics.Rect nonCandidate = new android.graphics.Rect(src);
        nonCandidate.offset(src.width() + 1, 0);
        assertIsNotCandidate(android.view.View.FOCUS_LEFT, src, nonCandidate);
        android.graphics.Rect candidate = new android.graphics.Rect(src);
        candidate.offset(-(4 * src.width()), 0);
        assertDirectionIsCandidate(android.view.View.FOCUS_LEFT, src, candidate);
        assertBetterCandidate(android.view.View.FOCUS_LEFT, src, candidate, nonCandidate);
    }

    /**
     * Grabbed from {@link android.widget.focus.VerticalFocusSearchTest#testSearchFromMidLeft()}
     */
    @android.test.suitebuilder.annotation.SmallTest
    public void testVerticalFocusSearchScenario() {
        // L    T    R    B
        // src
        // expectedbetter
        assertBetterCandidate(android.view.View.FOCUS_DOWN, new android.graphics.Rect(0, 109, 153, 169), new android.graphics.Rect(166, 169, 319, 229), new android.graphics.Rect(0, 229, 320, 289));// expectedworse

        // failing test 4/10/2008, the values were tweaked somehow in functional
        // test...
        // L    T    R    B
        // src
        // expectedbetter
        assertBetterCandidate(android.view.View.FOCUS_DOWN, new android.graphics.Rect(0, 91, 153, 133), new android.graphics.Rect(166, 133, 319, 175), new android.graphics.Rect(0, 175, 320, 217));// expectedworse

    }

    /**
     * Example: going down from a thin button all the way to the left of a
     * screen where, just below, is a very wide button, and just below that,
     * is an equally skinny button all the way to the left.  want to make
     * sure any minor axis factor doesn't override the fact that the one below
     * in vertical beam should be next focus
     */
    @android.test.suitebuilder.annotation.SmallTest
    public void testBeamsOverlapMajorAxisCloserMinorAxisFurther() {
        // L   T    R    B
        // src
        // expectedbetter
        assertBetterCandidate(android.view.View.FOCUS_DOWN, new android.graphics.Rect(0, 0, 100, 100), new android.graphics.Rect(0, 100, 480, 200), new android.graphics.Rect(0, 200, 100, 300));// expectedworse

    }

    /**
     * Real scenario grabbed from song playback screen.
     */
    @android.test.suitebuilder.annotation.SmallTest
    public void testMusicPlaybackScenario() {
        // L    T    R    B
        // src
        // expectedbetter
        assertBetterCandidate(android.view.View.FOCUS_LEFT, new android.graphics.Rect(227, 185, 312, 231), new android.graphics.Rect(195, 386, 266, 438), new android.graphics.Rect(124, 386, 195, 438));// expectedworse

    }

    /**
     * more generalized version of {@link #testMusicPlaybackScenario()}
     */
    @android.test.suitebuilder.annotation.SmallTest
    public void testOutOfBeamOverlapBeatsOutOfBeamFurtherOnMajorAxis() {
        // L    T    R    B
        // src
        // expectedbetter
        assertBetterCandidate(android.view.View.FOCUS_DOWN, new android.graphics.Rect(0, 0, 50, 50), new android.graphics.Rect(60, 40, 110, 90), new android.graphics.Rect(60, 70, 110, 120));// expectedworse

    }

    /**
     * Make sure that going down prefers views that are actually
     * down (and not those next to but still a candidate because
     * they are overlapping on the major axis)
     */
    @android.test.suitebuilder.annotation.SmallTest
    public void testInBeamTrumpsOutOfBeamOverlapping() {
        // L    T    R    B
        // src
        // expectedbetter
        assertBetterCandidate(android.view.View.FOCUS_DOWN, new android.graphics.Rect(0, 0, 50, 50), new android.graphics.Rect(0, 60, 50, 110), new android.graphics.Rect(51, 1, 101, 51));// expectedworse

    }

    @android.test.suitebuilder.annotation.SmallTest
    public void testOverlappingBeatsNonOverlapping() {
        // L    T    R    B
        // src
        // expectedbetter
        assertBetterCandidate(android.view.View.FOCUS_DOWN, new android.graphics.Rect(0, 0, 50, 50), new android.graphics.Rect(0, 40, 50, 90), new android.graphics.Rect(0, 75, 50, 125));// expectedworse

    }

    @android.test.suitebuilder.annotation.SmallTest
    public void testEditContactScenarioLeftFromDiscardChangesGoesToSaveContactInLandscape() {
        // L    T    R    B
        // src
        // better
        assertBetterCandidate(android.view.View.FOCUS_LEFT, new android.graphics.Rect(357, 258, 478, 318), new android.graphics.Rect(2, 258, 100, 318), new android.graphics.Rect(106, 120, 424, 184));// worse

    }

    /**
     * A dial pad with 9 squares arranged in a grid.  no padding, so
     * the edges are equal.  see {@link android.widget.focus.LinearLayoutGrid}
     */
    @android.test.suitebuilder.annotation.SmallTest
    public void testGridWithTouchingEdges() {
        // L    T    R    B
        // src
        // better
        assertBetterCandidate(android.view.View.FOCUS_DOWN, new android.graphics.Rect(106, 49, 212, 192), new android.graphics.Rect(106, 192, 212, 335), new android.graphics.Rect(0, 192, 106, 335));// worse

        // L    T    R    B
        // src
        // better
        assertBetterCandidate(android.view.View.FOCUS_DOWN, new android.graphics.Rect(106, 49, 212, 192), new android.graphics.Rect(106, 192, 212, 335), new android.graphics.Rect(212, 192, 318, 335));// worse

    }

    @android.test.suitebuilder.annotation.SmallTest
    public void testSearchFromEmptyRect() {
        // L   T    R    B
        // src
        // better
        assertBetterCandidate(android.view.View.FOCUS_DOWN, new android.graphics.Rect(0, 0, 0, 0), new android.graphics.Rect(0, 0, 320, 45), new android.graphics.Rect(0, 45, 320, 545));// worse

    }

    /**
     * Reproduce bug 1124559, drilling down to actual bug
     * (majorAxisDistance was wrong for direction left)
     */
    @android.test.suitebuilder.annotation.SmallTest
    public void testGmailReplyButtonsScenario() {
        // L    T    R    B
        // src
        // better
        assertBetterCandidate(android.view.View.FOCUS_LEFT, new android.graphics.Rect(223, 380, 312, 417), new android.graphics.Rect(102, 380, 210, 417), new android.graphics.Rect(111, 443, 206, 480));// worse

        // L    T    R    B
        // src
        // better
        assertBeamBeats(android.view.View.FOCUS_LEFT, new android.graphics.Rect(223, 380, 312, 417), new android.graphics.Rect(102, 380, 210, 417), new android.graphics.Rect(111, 443, 206, 480));// worse

        // L    T    R    B
        assertBeamsOverlap(android.view.View.FOCUS_LEFT, new android.graphics.Rect(223, 380, 312, 417), new android.graphics.Rect(102, 380, 210, 417));
        // L    T    R    B
        assertBeamsDontOverlap(android.view.View.FOCUS_LEFT, new android.graphics.Rect(223, 380, 312, 417), new android.graphics.Rect(111, 443, 206, 480));
        assertTrue("major axis distance less than major axis distance to " + "far edge", // L    T    R    B
        android.view.FocusFinderHelper.majorAxisDistance(android.view.View.FOCUS_LEFT, new android.graphics.Rect(223, 380, 312, 417), new android.graphics.Rect(102, 380, 210, 417)) < // L    T    R    B
        android.view.FocusFinderHelper.majorAxisDistanceToFarEdge(android.view.View.FOCUS_LEFT, new android.graphics.Rect(223, 380, 312, 417), new android.graphics.Rect(111, 443, 206, 480)));
    }

    @android.test.suitebuilder.annotation.SmallTest
    public void testGmailScenarioBug1203288() {
        // L    T    R    B
        // src
        // better
        assertBetterCandidate(android.view.View.FOCUS_DOWN, new android.graphics.Rect(0, 2, 480, 82), new android.graphics.Rect(344, 87, 475, 124), new android.graphics.Rect(0, 130, 480, 203));// worse

    }

    @android.test.suitebuilder.annotation.SmallTest
    public void testHomeShortcutScenarioBug1295354() {
        // L    T    R    B
        // src
        // better
        assertBetterCandidate(android.view.View.FOCUS_RIGHT, new android.graphics.Rect(3, 338, 77, 413), new android.graphics.Rect(163, 338, 237, 413), new android.graphics.Rect(83, 38, 157, 113));// worse

    }

    @android.test.suitebuilder.annotation.SmallTest
    public void testBeamAlwaysBeatsHoriz() {
        // L    T    R    B
        // src
        // better, (way further, but in beam)
        assertBetterCandidate(android.view.View.FOCUS_RIGHT, new android.graphics.Rect(0, 0, 50, 50), new android.graphics.Rect(150, 0, 200, 50), new android.graphics.Rect(60, 51, 110, 101));// worse, even though it is closer

        // L    T    R    B
        // src
        // better, (way further, but in beam)
        assertBetterCandidate(android.view.View.FOCUS_LEFT, new android.graphics.Rect(150, 0, 200, 50), new android.graphics.Rect(0, 50, 50, 50), new android.graphics.Rect(49, 99, 149, 101));// worse, even though it is closer

    }

    @android.test.suitebuilder.annotation.SmallTest
    public void testIsCandidateOverlappingEdgeFromEmptyRect() {
        // L   T    R    B
        // src
        assertDirectionIsCandidate(android.view.View.FOCUS_DOWN, new android.graphics.Rect(0, 0, 0, 0), new android.graphics.Rect(0, 0, 20, 1));// candidate

        // L   T    R    B
        // src
        assertDirectionIsCandidate(android.view.View.FOCUS_UP, new android.graphics.Rect(0, 0, 0, 0), new android.graphics.Rect(0, -1, 20, 0));// candidate

        // L   T    R    B
        // src
        assertDirectionIsCandidate(android.view.View.FOCUS_LEFT, new android.graphics.Rect(0, 0, 0, 0), new android.graphics.Rect(-1, 0, 0, 20));// candidate

        // L   T    R    B
        // src
        assertDirectionIsCandidate(android.view.View.FOCUS_RIGHT, new android.graphics.Rect(0, 0, 0, 0), new android.graphics.Rect(0, 0, 1, 20));// candidate

    }

    private void assertBeamsOverlap(int direction, android.graphics.Rect rect1, android.graphics.Rect rect2) {
        java.lang.String directionStr = validateAndGetStringFor(direction);
        java.lang.String assertMsg = java.lang.String.format("Expected beams to overlap in direction %s " + "for rectangles %s and %s", directionStr, rect1, rect2);
        assertTrue(assertMsg, mFocusFinder.beamsOverlap(direction, rect1, rect2));
    }

    private void assertBeamsDontOverlap(int direction, android.graphics.Rect rect1, android.graphics.Rect rect2) {
        java.lang.String directionStr = validateAndGetStringFor(direction);
        java.lang.String assertMsg = java.lang.String.format("Expected beams not to overlap in direction %s " + "for rectangles %s and %s", directionStr, rect1, rect2);
        assertFalse(assertMsg, mFocusFinder.beamsOverlap(direction, rect1, rect2));
    }

    /**
     * Assert that particular rect is a better focus search candidate from a
     * source rect than another.
     *
     * @param direction
     * 		The direction of focus search.
     * @param srcRect
     * 		The src rectangle.
     * @param expectedBetter
     * 		The candidate that should be better.
     * @param expectedWorse
     * 		The candidate that should be worse.
     */
    private void assertBetterCandidate(int direction, android.graphics.Rect srcRect, android.graphics.Rect expectedBetter, android.graphics.Rect expectedWorse) {
        java.lang.String directionStr = validateAndGetStringFor(direction);
        java.lang.String assertMsg = java.lang.String.format("expected %s to be a better focus search candidate than " + ("%s when searching " + "from %s in direction %s"), expectedBetter, expectedWorse, srcRect, directionStr);
        assertTrue(assertMsg, mFocusFinder.isBetterCandidate(direction, srcRect, expectedBetter, expectedWorse));
        assertMsg = java.lang.String.format("expected %s to not be a better focus search candidate than " + ("%s when searching " + "from %s in direction %s"), expectedWorse, expectedBetter, srcRect, directionStr);
        assertFalse(assertMsg, mFocusFinder.isBetterCandidate(direction, srcRect, expectedWorse, expectedBetter));
    }

    private void assertIsNotCandidate(int direction, android.graphics.Rect src, android.graphics.Rect dest) {
        java.lang.String directionStr = validateAndGetStringFor(direction);
        final java.lang.String assertMsg = java.lang.String.format("expected going from %s to %s in direction %s to be an invalid " + "focus search candidate", src, dest, directionStr);
        assertFalse(assertMsg, mFocusFinder.isCandidate(src, dest, direction));
    }

    private void assertBeamBeats(int direction, android.graphics.Rect srcRect, android.graphics.Rect rect1, android.graphics.Rect rect2) {
        java.lang.String directionStr = validateAndGetStringFor(direction);
        java.lang.String assertMsg = java.lang.String.format("expecting %s to beam beat %s w.r.t %s in direction %s", rect1, rect2, srcRect, directionStr);
        assertTrue(assertMsg, mFocusFinder.beamBeats(direction, srcRect, rect1, rect2));
    }

    private void assertDirectionIsCandidate(int direction, android.graphics.Rect src, android.graphics.Rect dest) {
        java.lang.String directionStr = validateAndGetStringFor(direction);
        final java.lang.String assertMsg = java.lang.String.format("expected going from %s to %s in direction %s to be a valid " + "focus search candidate", src, dest, directionStr);
        assertTrue(assertMsg, mFocusFinder.isCandidate(src, dest, direction));
    }

    private java.lang.String validateAndGetStringFor(int direction) {
        java.lang.String directionStr = "??";
        switch (direction) {
            case android.view.View.FOCUS_UP :
                directionStr = "FOCUS_UP";
                break;
            case android.view.View.FOCUS_DOWN :
                directionStr = "FOCUS_DOWN";
                break;
            case android.view.View.FOCUS_LEFT :
                directionStr = "FOCUS_LEFT";
                break;
            case android.view.View.FOCUS_RIGHT :
                directionStr = "FOCUS_RIGHT";
                break;
            default :
                fail("passed in unknown direction, ya blewit!");
        }
        return directionStr;
    }
}

