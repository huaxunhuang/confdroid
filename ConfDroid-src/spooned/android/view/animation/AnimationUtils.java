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
package android.view.animation;


/**
 * Defines common utilities for working with animations.
 */
public class AnimationUtils {
    /**
     * These flags are used when parsing AnimatorSet objects
     */
    private static final int TOGETHER = 0;

    private static final int SEQUENTIALLY = 1;

    private static class AnimationState {
        boolean animationClockLocked;

        long currentVsyncTimeMillis;

        long lastReportedTimeMillis;
    }

    private static java.lang.ThreadLocal<android.view.animation.AnimationUtils.AnimationState> sAnimationState = new java.lang.ThreadLocal<android.view.animation.AnimationUtils.AnimationState>() {
        @java.lang.Override
        protected android.view.animation.AnimationUtils.AnimationState initialValue() {
            return new android.view.animation.AnimationUtils.AnimationState();
        }
    };

    /**
     * Locks AnimationUtils{@link #currentAnimationTimeMillis()} to a fixed value for the current
     * thread. This is used by {@link android.view.Choreographer} to ensure that all accesses
     * during a vsync update are synchronized to the timestamp of the vsync.
     *
     * It is also exposed to tests to allow for rapid, flake-free headless testing.
     *
     * Must be followed by a call to {@link #unlockAnimationClock()} to allow time to
     * progress. Failing to do this will result in stuck animations, scrolls, and flings.
     *
     * Note that time is not allowed to "rewind" and must perpetually flow forward. So the
     * lock may fail if the time is in the past from a previously returned value, however
     * time will be frozen for the duration of the lock. The clock is a thread-local, so
     * ensure that {@link #lockAnimationClock(long)}, {@link #unlockAnimationClock()}, and
     * {@link #currentAnimationTimeMillis()} are all called on the same thread.
     *
     * This is also not reference counted in any way. Any call to {@link #unlockAnimationClock()}
     * will unlock the clock for everyone on the same thread. It is therefore recommended
     * for tests to use their own thread to ensure that there is no collision with any existing
     * {@link android.view.Choreographer} instance.
     *
     * @unknown 
     */
    @android.annotation.TestApi
    public static void lockAnimationClock(long vsyncMillis) {
        android.view.animation.AnimationUtils.AnimationState state = android.view.animation.AnimationUtils.sAnimationState.get();
        state.animationClockLocked = true;
        state.currentVsyncTimeMillis = vsyncMillis;
    }

    /**
     * Frees the time lock set in place by {@link #lockAnimationClock(long)}. Must be called
     * to allow the animation clock to self-update.
     *
     * @unknown 
     */
    @android.annotation.TestApi
    public static void unlockAnimationClock() {
        android.view.animation.AnimationUtils.sAnimationState.get().animationClockLocked = false;
    }

    /**
     * Returns the current animation time in milliseconds. This time should be used when invoking
     * {@link Animation#setStartTime(long)}. Refer to {@link android.os.SystemClock} for more
     * information about the different available clocks. The clock used by this method is
     * <em>not</em> the "wall" clock (it is not {@link System#currentTimeMillis}).
     *
     * @return the current animation time in milliseconds
     * @see android.os.SystemClock
     */
    public static long currentAnimationTimeMillis() {
        android.view.animation.AnimationUtils.AnimationState state = android.view.animation.AnimationUtils.sAnimationState.get();
        if (state.animationClockLocked) {
            // It's important that time never rewinds
            return java.lang.Math.max(state.currentVsyncTimeMillis, state.lastReportedTimeMillis);
        }
        state.lastReportedTimeMillis = android.os.SystemClock.uptimeMillis();
        return state.lastReportedTimeMillis;
    }

    /**
     * Loads an {@link Animation} object from a resource
     *
     * @param context
     * 		Application context used to access resources
     * @param id
     * 		The resource id of the animation to load
     * @return The animation object reference by the specified id
     * @throws NotFoundException
     * 		when the animation cannot be loaded
     */
    public static android.view.animation.Animation loadAnimation(android.content.Context context, @android.annotation.AnimRes
    int id) throws android.content.res.Resources.NotFoundException {
        android.content.res.XmlResourceParser parser = null;
        try {
            parser = context.getResources().getAnimation(id);
            return android.view.animation.AnimationUtils.createAnimationFromXml(context, parser);
        } catch (org.xmlpull.v1.XmlPullParserException ex) {
            android.content.res.Resources.NotFoundException rnf = new android.content.res.Resources.NotFoundException("Can't load animation resource ID #0x" + java.lang.Integer.toHexString(id));
            rnf.initCause(ex);
            throw rnf;
        } catch (java.io.IOException ex) {
            android.content.res.Resources.NotFoundException rnf = new android.content.res.Resources.NotFoundException("Can't load animation resource ID #0x" + java.lang.Integer.toHexString(id));
            rnf.initCause(ex);
            throw rnf;
        } finally {
            if (parser != null)
                parser.close();

        }
    }

    private static android.view.animation.Animation createAnimationFromXml(android.content.Context c, org.xmlpull.v1.XmlPullParser parser) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        return android.view.animation.AnimationUtils.createAnimationFromXml(c, parser, null, android.util.Xml.asAttributeSet(parser));
    }

    @android.annotation.UnsupportedAppUsage
    private static android.view.animation.Animation createAnimationFromXml(android.content.Context c, org.xmlpull.v1.XmlPullParser parser, android.view.animation.AnimationSet parent, android.util.AttributeSet attrs) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        android.view.animation.Animation anim = null;
        // Make sure we are on a start tag.
        int type;
        int depth = parser.getDepth();
        while ((((type = parser.next()) != org.xmlpull.v1.XmlPullParser.END_TAG) || (parser.getDepth() > depth)) && (type != org.xmlpull.v1.XmlPullParser.END_DOCUMENT)) {
            if (type != org.xmlpull.v1.XmlPullParser.START_TAG) {
                continue;
            }
            java.lang.String name = parser.getName();
            if (name.equals("set")) {
                anim = new android.view.animation.AnimationSet(c, attrs);
                android.view.animation.AnimationUtils.createAnimationFromXml(c, parser, ((android.view.animation.AnimationSet) (anim)), attrs);
            } else
                if (name.equals("alpha")) {
                    anim = new android.view.animation.AlphaAnimation(c, attrs);
                } else
                    if (name.equals("scale")) {
                        anim = new android.view.animation.ScaleAnimation(c, attrs);
                    } else
                        if (name.equals("rotate")) {
                            anim = new android.view.animation.RotateAnimation(c, attrs);
                        } else
                            if (name.equals("translate")) {
                                anim = new android.view.animation.TranslateAnimation(c, attrs);
                            } else
                                if (name.equals("cliprect")) {
                                    anim = new android.view.animation.ClipRectAnimation(c, attrs);
                                } else {
                                    throw new java.lang.RuntimeException("Unknown animation name: " + parser.getName());
                                }





            if (parent != null) {
                parent.addAnimation(anim);
            }
        } 
        return anim;
    }

    /**
     * Loads a {@link LayoutAnimationController} object from a resource
     *
     * @param context
     * 		Application context used to access resources
     * @param id
     * 		The resource id of the animation to load
     * @return The animation object reference by the specified id
     * @throws NotFoundException
     * 		when the layout animation controller cannot be loaded
     */
    public static android.view.animation.LayoutAnimationController loadLayoutAnimation(android.content.Context context, @android.annotation.AnimRes
    int id) throws android.content.res.Resources.NotFoundException {
        android.content.res.XmlResourceParser parser = null;
        try {
            parser = context.getResources().getAnimation(id);
            return android.view.animation.AnimationUtils.createLayoutAnimationFromXml(context, parser);
        } catch (org.xmlpull.v1.XmlPullParserException ex) {
            android.content.res.Resources.NotFoundException rnf = new android.content.res.Resources.NotFoundException("Can't load animation resource ID #0x" + java.lang.Integer.toHexString(id));
            rnf.initCause(ex);
            throw rnf;
        } catch (java.io.IOException ex) {
            android.content.res.Resources.NotFoundException rnf = new android.content.res.Resources.NotFoundException("Can't load animation resource ID #0x" + java.lang.Integer.toHexString(id));
            rnf.initCause(ex);
            throw rnf;
        } finally {
            if (parser != null)
                parser.close();

        }
    }

    private static android.view.animation.LayoutAnimationController createLayoutAnimationFromXml(android.content.Context c, org.xmlpull.v1.XmlPullParser parser) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        return android.view.animation.AnimationUtils.createLayoutAnimationFromXml(c, parser, android.util.Xml.asAttributeSet(parser));
    }

    private static android.view.animation.LayoutAnimationController createLayoutAnimationFromXml(android.content.Context c, org.xmlpull.v1.XmlPullParser parser, android.util.AttributeSet attrs) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        android.view.animation.LayoutAnimationController controller = null;
        int type;
        int depth = parser.getDepth();
        while ((((type = parser.next()) != org.xmlpull.v1.XmlPullParser.END_TAG) || (parser.getDepth() > depth)) && (type != org.xmlpull.v1.XmlPullParser.END_DOCUMENT)) {
            if (type != org.xmlpull.v1.XmlPullParser.START_TAG) {
                continue;
            }
            java.lang.String name = parser.getName();
            if ("layoutAnimation".equals(name)) {
                controller = new android.view.animation.LayoutAnimationController(c, attrs);
            } else
                if ("gridLayoutAnimation".equals(name)) {
                    controller = new android.view.animation.GridLayoutAnimationController(c, attrs);
                } else {
                    throw new java.lang.RuntimeException("Unknown layout animation name: " + name);
                }

        } 
        return controller;
    }

    /**
     * Make an animation for objects becoming visible. Uses a slide and fade
     * effect.
     *
     * @param c
     * 		Context for loading resources
     * @param fromLeft
     * 		is the object to be animated coming from the left
     * @return The new animation
     */
    public static android.view.animation.Animation makeInAnimation(android.content.Context c, boolean fromLeft) {
        android.view.animation.Animation a;
        if (fromLeft) {
            a = android.view.animation.AnimationUtils.loadAnimation(c, com.android.internal.R.anim.slide_in_left);
        } else {
            a = android.view.animation.AnimationUtils.loadAnimation(c, com.android.internal.R.anim.slide_in_right);
        }
        a.setInterpolator(new android.view.animation.DecelerateInterpolator());
        a.setStartTime(android.view.animation.AnimationUtils.currentAnimationTimeMillis());
        return a;
    }

    /**
     * Make an animation for objects becoming invisible. Uses a slide and fade
     * effect.
     *
     * @param c
     * 		Context for loading resources
     * @param toRight
     * 		is the object to be animated exiting to the right
     * @return The new animation
     */
    public static android.view.animation.Animation makeOutAnimation(android.content.Context c, boolean toRight) {
        android.view.animation.Animation a;
        if (toRight) {
            a = android.view.animation.AnimationUtils.loadAnimation(c, com.android.internal.R.anim.slide_out_right);
        } else {
            a = android.view.animation.AnimationUtils.loadAnimation(c, com.android.internal.R.anim.slide_out_left);
        }
        a.setInterpolator(new android.view.animation.AccelerateInterpolator());
        a.setStartTime(android.view.animation.AnimationUtils.currentAnimationTimeMillis());
        return a;
    }

    /**
     * Make an animation for objects becoming visible. Uses a slide up and fade
     * effect.
     *
     * @param c
     * 		Context for loading resources
     * @return The new animation
     */
    public static android.view.animation.Animation makeInChildBottomAnimation(android.content.Context c) {
        android.view.animation.Animation a;
        a = android.view.animation.AnimationUtils.loadAnimation(c, com.android.internal.R.anim.slide_in_child_bottom);
        a.setInterpolator(new android.view.animation.AccelerateInterpolator());
        a.setStartTime(android.view.animation.AnimationUtils.currentAnimationTimeMillis());
        return a;
    }

    /**
     * Loads an {@link Interpolator} object from a resource
     *
     * @param context
     * 		Application context used to access resources
     * @param id
     * 		The resource id of the animation to load
     * @return The animation object reference by the specified id
     * @throws NotFoundException
     * 		
     */
    public static android.view.animation.Interpolator loadInterpolator(android.content.Context context, @android.annotation.AnimRes
    @android.annotation.InterpolatorRes
    int id) throws android.content.res.Resources.NotFoundException {
        android.content.res.XmlResourceParser parser = null;
        try {
            parser = context.getResources().getAnimation(id);
            return android.view.animation.AnimationUtils.createInterpolatorFromXml(context.getResources(), context.getTheme(), parser);
        } catch (org.xmlpull.v1.XmlPullParserException ex) {
            android.content.res.Resources.NotFoundException rnf = new android.content.res.Resources.NotFoundException("Can't load animation resource ID #0x" + java.lang.Integer.toHexString(id));
            rnf.initCause(ex);
            throw rnf;
        } catch (java.io.IOException ex) {
            android.content.res.Resources.NotFoundException rnf = new android.content.res.Resources.NotFoundException("Can't load animation resource ID #0x" + java.lang.Integer.toHexString(id));
            rnf.initCause(ex);
            throw rnf;
        } finally {
            if (parser != null)
                parser.close();

        }
    }

    /**
     * Loads an {@link Interpolator} object from a resource
     *
     * @param res
     * 		The resources
     * @param id
     * 		The resource id of the animation to load
     * @return The interpolator object reference by the specified id
     * @throws NotFoundException
     * 		
     * @unknown 
     */
    public static android.view.animation.Interpolator loadInterpolator(android.content.res.Resources res, android.content.res.Resources.Theme theme, int id) throws android.content.res.Resources.NotFoundException {
        android.content.res.XmlResourceParser parser = null;
        try {
            parser = res.getAnimation(id);
            return android.view.animation.AnimationUtils.createInterpolatorFromXml(res, theme, parser);
        } catch (org.xmlpull.v1.XmlPullParserException ex) {
            android.content.res.Resources.NotFoundException rnf = new android.content.res.Resources.NotFoundException("Can't load animation resource ID #0x" + java.lang.Integer.toHexString(id));
            rnf.initCause(ex);
            throw rnf;
        } catch (java.io.IOException ex) {
            android.content.res.Resources.NotFoundException rnf = new android.content.res.Resources.NotFoundException("Can't load animation resource ID #0x" + java.lang.Integer.toHexString(id));
            rnf.initCause(ex);
            throw rnf;
        } finally {
            if (parser != null)
                parser.close();

        }
    }

    private static android.view.animation.Interpolator createInterpolatorFromXml(android.content.res.Resources res, android.content.res.Resources.Theme theme, org.xmlpull.v1.XmlPullParser parser) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        android.view.animation.BaseInterpolator interpolator = null;
        // Make sure we are on a start tag.
        int type;
        int depth = parser.getDepth();
        while ((((type = parser.next()) != org.xmlpull.v1.XmlPullParser.END_TAG) || (parser.getDepth() > depth)) && (type != org.xmlpull.v1.XmlPullParser.END_DOCUMENT)) {
            if (type != org.xmlpull.v1.XmlPullParser.START_TAG) {
                continue;
            }
            android.util.AttributeSet attrs = android.util.Xml.asAttributeSet(parser);
            java.lang.String name = parser.getName();
            if (name.equals("linearInterpolator")) {
                interpolator = new android.view.animation.LinearInterpolator();
            } else
                if (name.equals("accelerateInterpolator")) {
                    interpolator = new android.view.animation.AccelerateInterpolator(res, theme, attrs);
                } else
                    if (name.equals("decelerateInterpolator")) {
                        interpolator = new android.view.animation.DecelerateInterpolator(res, theme, attrs);
                    } else
                        if (name.equals("accelerateDecelerateInterpolator")) {
                            interpolator = new android.view.animation.AccelerateDecelerateInterpolator();
                        } else
                            if (name.equals("cycleInterpolator")) {
                                interpolator = new android.view.animation.CycleInterpolator(res, theme, attrs);
                            } else
                                if (name.equals("anticipateInterpolator")) {
                                    interpolator = new android.view.animation.AnticipateInterpolator(res, theme, attrs);
                                } else
                                    if (name.equals("overshootInterpolator")) {
                                        interpolator = new android.view.animation.OvershootInterpolator(res, theme, attrs);
                                    } else
                                        if (name.equals("anticipateOvershootInterpolator")) {
                                            interpolator = new android.view.animation.AnticipateOvershootInterpolator(res, theme, attrs);
                                        } else
                                            if (name.equals("bounceInterpolator")) {
                                                interpolator = new android.view.animation.BounceInterpolator();
                                            } else
                                                if (name.equals("pathInterpolator")) {
                                                    interpolator = new android.view.animation.PathInterpolator(res, theme, attrs);
                                                } else {
                                                    throw new java.lang.RuntimeException("Unknown interpolator name: " + parser.getName());
                                                }









        } 
        return interpolator;
    }
}

