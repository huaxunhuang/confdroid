/**
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package android.graphics.drawable;


/**
 * This class animates properties of a {@link android.graphics.drawable.VectorDrawable} with
 * animations defined using {@link android.animation.ObjectAnimator} or
 * {@link android.animation.AnimatorSet}.
 * <p>
 * Starting from API 25, AnimatedVectorDrawable runs on RenderThread (as opposed to on UI thread for
 * earlier APIs). This means animations in AnimatedVectorDrawable can remain smooth even when there
 * is heavy workload on the UI thread. Note: If the UI thread is unresponsive, RenderThread may
 * continue animating until the UI thread is capable of pushing another frame. Therefore, it is not
 * possible to precisely coordinate a RenderThread-enabled AnimatedVectorDrawable with UI thread
 * animations. Additionally,
 * {@link android.graphics.drawable.Animatable2.AnimationCallback#onAnimationEnd(Drawable)} will be
 * called the frame after the AnimatedVectorDrawable finishes on the RenderThread.
 * </p>
 * <p>
 * AnimatedVectorDrawable can be defined in either <a href="#ThreeXML">three separate XML files</a>,
 * or <a href="#OneXML">one XML</a>.
 * </p>
 * <a name="ThreeXML"></a>
 * <h3>Define an AnimatedVectorDrawable in three separate XML files</h3>
 * <ul>
 * <a name="VDExample"></a>
 * <li><h4>XML for the VectorDrawable containing properties to be animated</h4>
 * <p>
 * Animations can be performed on the animatable attributes in
 * {@link android.graphics.drawable.VectorDrawable}. These attributes will be animated by
 * {@link android.animation.ObjectAnimator}. The ObjectAnimator's target can be the root element,
 * a group element or a path element. The targeted elements need to be named uniquely within
 * the same VectorDrawable. Elements without animation do not need to be named.
 * </p>
 * <p>
 * Here are all the animatable attributes in {@link android.graphics.drawable.VectorDrawable}:
 * <table border="2" align="center" cellpadding="5">
 *     <thead>
 *         <tr>
 *             <th>Element Name</th>
 *             <th>Animatable attribute name</th>
 *         </tr>
 *     </thead>
 *     <tr>
 *         <td>&lt;vector&gt;</td>
 *         <td>alpha</td>
 *     </tr>
 *     <tr>
 *         <td rowspan="7">&lt;group&gt;</td>
 *         <td>rotation</td>
 *     </tr>
 *     <tr>
 *         <td>pivotX</td>
 *     </tr>
 *     <tr>
 *         <td>pivotY</td>
 *     </tr>
 *     <tr>
 *         <td>scaleX</td>
 *     </tr>
 *     <tr>
 *         <td>scaleY</td>
 *     </tr>
 *     <tr>
 *         <td>translateX</td>
 *     </tr>
 *     <tr>
 *         <td>translateY</td>
 *     </tr>
 *     <tr>
 *         <td rowspan="9">&lt;path&gt;</td>
 *         <td>pathData</td>
 *     </tr>
 *     <tr>
 *         <td>fillColor</td>
 *     </tr>
 *     <tr>
 *         <td>strokeColor</td>
 *     </tr>
 *     <tr>
 *         <td>strokeWidth</td>
 *     </tr>
 *     <tr>
 *         <td>strokeAlpha</td>
 *     </tr>
 *     <tr>
 *         <td>fillAlpha</td>
 *     </tr>
 *     <tr>
 *         <td>trimPathStart</td>
 *     </tr>
 *     <tr>
 *         <td>trimPathEnd</td>
 *     </tr>
 *     <tr>
 *         <td>trimPathOffset</td>
 *     </tr>
 *     <tr>
 *         <td>&lt;clip-path&gt;</td>
 *         <td>pathData</td>
 *     </tr>
 * </table>
 * </p>
 * Below is an example of a VectorDrawable defined in vectordrawable.xml. This VectorDrawable is
 * referred to by its file name (not including file suffix) in the
 * <a href="#AVDExample">AnimatedVectorDrawable XML example</a>.
 * <pre>
 * &lt;vector xmlns:android=&quot;http://schemas.android.com/apk/res/android&quot;
 *     android:height=&quot;64dp&quot;
 *     android:width=&quot;64dp&quot;
 *     android:viewportHeight=&quot;600&quot;
 *     android:viewportWidth=&quot;600&quot; &gt;
 *     &lt;group
 *         android:name=&quot;rotationGroup&quot;
 *         android:pivotX=&quot;300.0&quot;
 *         android:pivotY=&quot;300.0&quot;
 *         android:rotation=&quot;45.0&quot; &gt;
 *         &lt;path
 *             android:name=&quot;v&quot;
 *             android:fillColor=&quot;#000000&quot;
 *             android:pathData=&quot;M300,70 l 0,-70 70,70 0,0 -70,70z&quot; /&gt;
 *     &lt;/group&gt;
 * &lt;/vector&gt;
 * </pre></li>
 *
 * <a name="AVDExample"></a>
 * <li><h4>XML for AnimatedVectorDrawable</h4>
 * <p>
 * An AnimatedVectorDrawable element has a VectorDrawable attribute, and one or more target
 * element(s). The target element can specify its target by android:name attribute, and link the
 * target with the proper ObjectAnimator or AnimatorSet by android:animation attribute.
 * </p>
 * The following code sample defines an AnimatedVectorDrawable. Note that the names refer to the
 * groups and paths in the <a href="#VDExample">VectorDrawable XML above</a>.
 * <pre>
 * &lt;animated-vector xmlns:android=&quot;http://schemas.android.com/apk/res/android&quot;
 *     android:drawable=&quot;@drawable/vectordrawable&quot; &gt;
 *     &lt;target
 *         android:name=&quot;rotationGroup&quot;
 *         android:animation=&quot;@animator/rotation&quot; /&gt;
 *     &lt;target
 *         android:name=&quot;v&quot;
 *         android:animation=&quot;@animator/path_morph&quot; /&gt;
 * &lt;/animated-vector&gt;
 * </pre>
 * </li>
 *
 * <li><h4>XML for Animations defined using ObjectAnimator or AnimatorSet</h4>
 * <p>
 * From the previous <a href="#AVDExample">example of AnimatedVectorDrawable</a>, two animations
 * were used: rotation.xml and path_morph.xml.
 * </p>
 * rotation.xml rotates the target group from 0 degree to 360 degrees over 6000ms:
 * <pre>
 * &lt;objectAnimator
 *     android:duration=&quot;6000&quot;
 *     android:propertyName=&quot;rotation&quot;
 *     android:valueFrom=&quot;0&quot;
 *     android:valueTo=&quot;360&quot; /&gt;
 * </pre>
 *
 * path_morph.xml morphs the path from one shape into the other. Note that the paths must be
 * compatible for morphing. Specifically, the paths must have the same commands, in the same order,
 * and must have the same number of parameters for each command. It is recommended to store path
 * strings as string resources for reuse.
 * <pre>
 * &lt;set xmlns:android=&quot;http://schemas.android.com/apk/res/android&quot;&gt;
 *     &lt;objectAnimator
 *         android:duration=&quot;3000&quot;
 *         android:propertyName=&quot;pathData&quot;
 *         android:valueFrom=&quot;M300,70 l 0,-70 70,70 0,0 -70,70z&quot;
 *         android:valueTo=&quot;M300,70 l 0,-70 70,0  0,140 -70,0 z&quot;
 *         android:valueType=&quot;pathType&quot;/&gt;
 * &lt;/set&gt;
 * </pre>
 * </ul>
 * <a name="OneXML"></a>
 * <h3>Define an AnimatedVectorDrawable all in one XML file</h3>
 * <p>
 * Since the AAPT tool supports a new format that bundles several related XML files together, we can
 * merge the XML files from the previous examples into one XML file:
 * </p>
 * <pre>
 * &lt;animated-vector xmlns:android=&quot;http://schemas.android.com/apk/res/android&quot;
 *                  xmlns:aapt=&quothttp://schemas.android.com/aapt&quot; &gt;
 *     &lt;aapt:attr name="android:drawable"&gt;
 *         &lt;vector
 *             android:height=&quot;64dp&quot;
 *             android:width=&quot;64dp&quot;
 *             android:viewportHeight=&quot;600&quot;
 *             android:viewportWidth=&quot;600&quot; &gt;
 *             &lt;group
 *                 android:name=&quot;rotationGroup&quot;
 *                 android:pivotX=&quot;300.0&quot;
 *                 android:pivotY=&quot;300.0&quot;
 *                 android:rotation=&quot;45.0&quot; &gt;
 *                 &lt;path
 *                     android:name=&quot;v&quot;
 *                     android:fillColor=&quot;#000000&quot;
 *                     android:pathData=&quot;M300,70 l 0,-70 70,70 0,0 -70,70z&quot; /&gt;
 *             &lt;/group&gt;
 *         &lt;/vector&gt;
 *     &lt;/aapt:attr&gt;
 *
 *     &lt;target android:name=&quot;rotationGroup&quot;&gt; *
 *         &lt;aapt:attr name="android:animation"&gt;
 *             &lt;objectAnimator
 *             android:duration=&quot;6000&quot;
 *             android:propertyName=&quot;rotation&quot;
 *             android:valueFrom=&quot;0&quot;
 *             android:valueTo=&quot;360&quot; /&gt;
 *         &lt;/aapt:attr&gt;
 *     &lt;/target&gt;
 *
 *     &lt;target android:name=&quot;v&quot; &gt;
 *         &lt;aapt:attr name="android:animation"&gt;
 *             &lt;set&gt;
 *                 &lt;objectAnimator
 *                     android:duration=&quot;3000&quot;
 *                     android:propertyName=&quot;pathData&quot;
 *                     android:valueFrom=&quot;M300,70 l 0,-70 70,70 0,0 -70,70z&quot;
 *                     android:valueTo=&quot;M300,70 l 0,-70 70,0  0,140 -70,0 z&quot;
 *                     android:valueType=&quot;pathType&quot;/&gt;
 *             &lt;/set&gt;
 *         &lt;/aapt:attr&gt;
 *      &lt;/target&gt;
 * &lt;/animated-vector&gt;
 * </pre>
 *
 * @unknown ref android.R.styleable#AnimatedVectorDrawable_drawable
 * @unknown ref android.R.styleable#AnimatedVectorDrawableTarget_name
 * @unknown ref android.R.styleable#AnimatedVectorDrawableTarget_animation
 */
public class AnimatedVectorDrawable extends android.graphics.drawable.Drawable implements android.graphics.drawable.Animatable2 {
    private static final java.lang.String LOGTAG = "AnimatedVectorDrawable";

    private static final java.lang.String ANIMATED_VECTOR = "animated-vector";

    private static final java.lang.String TARGET = "target";

    private static final boolean DBG_ANIMATION_VECTOR_DRAWABLE = false;

    /**
     * Local, mutable animator set.
     */
    @android.annotation.UnsupportedAppUsage
    private android.graphics.drawable.AnimatedVectorDrawable.VectorDrawableAnimator mAnimatorSet;

    /**
     * The resources against which this drawable was created. Used to attempt
     * to inflate animators if applyTheme() doesn't get called.
     */
    private android.content.res.Resources mRes;

    @android.annotation.UnsupportedAppUsage
    private android.graphics.drawable.AnimatedVectorDrawable.AnimatedVectorDrawableState mAnimatedVectorState;

    /**
     * The animator set that is parsed from the xml.
     */
    private android.animation.AnimatorSet mAnimatorSetFromXml = null;

    private boolean mMutated;

    /**
     * Use a internal AnimatorListener to support callbacks during animation events.
     */
    private java.util.ArrayList<android.graphics.drawable.Animatable2.AnimationCallback> mAnimationCallbacks = null;

    private android.animation.Animator.AnimatorListener mAnimatorListener = null;

    public AnimatedVectorDrawable() {
        this(null, null);
    }

    private AnimatedVectorDrawable(android.graphics.drawable.AnimatedVectorDrawable.AnimatedVectorDrawableState state, android.content.res.Resources res) {
        mAnimatedVectorState = new android.graphics.drawable.AnimatedVectorDrawable.AnimatedVectorDrawableState(state, mCallback, res);
        mAnimatorSet = new android.graphics.drawable.AnimatedVectorDrawable.VectorDrawableAnimatorRT(this);
        mRes = res;
    }

    @java.lang.Override
    public android.graphics.drawable.Drawable mutate() {
        if ((!mMutated) && (super.mutate() == this)) {
            mAnimatedVectorState = new android.graphics.drawable.AnimatedVectorDrawable.AnimatedVectorDrawableState(mAnimatedVectorState, mCallback, mRes);
            mMutated = true;
        }
        return this;
    }

    /**
     *
     *
     * @unknown 
     */
    public void clearMutated() {
        super.clearMutated();
        if (mAnimatedVectorState.mVectorDrawable != null) {
            mAnimatedVectorState.mVectorDrawable.clearMutated();
        }
        mMutated = false;
    }

    /**
     * In order to avoid breaking old apps, we only throw exception on invalid VectorDrawable
     * animations for apps targeting N and later. For older apps, we ignore (i.e. quietly skip)
     * these animations.
     *
     * @return whether invalid animations for vector drawable should be ignored.
     */
    private static boolean shouldIgnoreInvalidAnimation() {
        android.app.Application app = android.app.ActivityThread.currentApplication();
        if ((app == null) || (app.getApplicationInfo() == null)) {
            return true;
        }
        if (app.getApplicationInfo().targetSdkVersion < Build.VERSION_CODES.N) {
            return true;
        }
        return false;
    }

    @java.lang.Override
    public android.graphics.drawable.Drawable.ConstantState getConstantState() {
        mAnimatedVectorState.mChangingConfigurations = getChangingConfigurations();
        return mAnimatedVectorState;
    }

    @java.lang.Override
    @android.content.pm.ActivityInfo.Config
    public int getChangingConfigurations() {
        return super.getChangingConfigurations() | mAnimatedVectorState.getChangingConfigurations();
    }

    /**
     * Draws the AnimatedVectorDrawable into the given canvas.
     * <p>
     * <strong>Note:</strong> Calling this method with a software canvas when the
     * AnimatedVectorDrawable is being animated on RenderThread (for API 25 and later) may yield
     * outdated result, as the UI thread is not guaranteed to be in sync with RenderThread on
     * VectorDrawable's property changes during RenderThread animations.
     * </p>
     *
     * @param canvas
     * 		The canvas to draw into
     */
    @java.lang.Override
    public void draw(android.graphics.Canvas canvas) {
        if ((!canvas.isHardwareAccelerated()) && (mAnimatorSet instanceof android.graphics.drawable.AnimatedVectorDrawable.VectorDrawableAnimatorRT)) {
            // If we have SW canvas and the RT animation is waiting to start, We need to fallback
            // to UI thread animation for AVD.
            if ((!mAnimatorSet.isRunning()) && (((android.graphics.drawable.AnimatedVectorDrawable.VectorDrawableAnimatorRT) (mAnimatorSet)).mPendingAnimationActions.size() > 0)) {
                fallbackOntoUI();
            }
        }
        mAnimatorSet.onDraw(canvas);
        mAnimatedVectorState.mVectorDrawable.draw(canvas);
    }

    @java.lang.Override
    protected void onBoundsChange(android.graphics.Rect bounds) {
        mAnimatedVectorState.mVectorDrawable.setBounds(bounds);
    }

    @java.lang.Override
    protected boolean onStateChange(int[] state) {
        return mAnimatedVectorState.mVectorDrawable.setState(state);
    }

    @java.lang.Override
    protected boolean onLevelChange(int level) {
        return mAnimatedVectorState.mVectorDrawable.setLevel(level);
    }

    @java.lang.Override
    public boolean onLayoutDirectionChanged(@android.view.View.ResolvedLayoutDir
    int layoutDirection) {
        return mAnimatedVectorState.mVectorDrawable.setLayoutDirection(layoutDirection);
    }

    /**
     * For API 25 and later, AnimatedVectorDrawable runs on RenderThread. Therefore, when the
     * root alpha is being animated, this getter does not guarantee to return an up-to-date alpha
     * value.
     *
     * @return the containing vector drawable's root alpha value.
     */
    @java.lang.Override
    public int getAlpha() {
        return mAnimatedVectorState.mVectorDrawable.getAlpha();
    }

    @java.lang.Override
    public void setAlpha(int alpha) {
        mAnimatedVectorState.mVectorDrawable.setAlpha(alpha);
    }

    @java.lang.Override
    public void setColorFilter(android.graphics.ColorFilter colorFilter) {
        mAnimatedVectorState.mVectorDrawable.setColorFilter(colorFilter);
    }

    @java.lang.Override
    public android.graphics.ColorFilter getColorFilter() {
        return mAnimatedVectorState.mVectorDrawable.getColorFilter();
    }

    @java.lang.Override
    public void setTintList(android.content.res.ColorStateList tint) {
        mAnimatedVectorState.mVectorDrawable.setTintList(tint);
    }

    @java.lang.Override
    public void setHotspot(float x, float y) {
        mAnimatedVectorState.mVectorDrawable.setHotspot(x, y);
    }

    @java.lang.Override
    public void setHotspotBounds(int left, int top, int right, int bottom) {
        mAnimatedVectorState.mVectorDrawable.setHotspotBounds(left, top, right, bottom);
    }

    @java.lang.Override
    public void setTintBlendMode(@android.annotation.NonNull
    android.graphics.BlendMode blendMode) {
        mAnimatedVectorState.mVectorDrawable.setTintBlendMode(blendMode);
    }

    @java.lang.Override
    public boolean setVisible(boolean visible, boolean restart) {
        if (mAnimatorSet.isInfinite() && mAnimatorSet.isStarted()) {
            if (visible) {
                // Resume the infinite animation when the drawable becomes visible again.
                mAnimatorSet.resume();
            } else {
                // Pause the infinite animation once the drawable is no longer visible.
                mAnimatorSet.pause();
            }
        }
        mAnimatedVectorState.mVectorDrawable.setVisible(visible, restart);
        return super.setVisible(visible, restart);
    }

    @java.lang.Override
    public boolean isStateful() {
        return mAnimatedVectorState.mVectorDrawable.isStateful();
    }

    @java.lang.Override
    public int getOpacity() {
        return android.graphics.PixelFormat.TRANSLUCENT;
    }

    @java.lang.Override
    public int getIntrinsicWidth() {
        return mAnimatedVectorState.mVectorDrawable.getIntrinsicWidth();
    }

    @java.lang.Override
    public int getIntrinsicHeight() {
        return mAnimatedVectorState.mVectorDrawable.getIntrinsicHeight();
    }

    @java.lang.Override
    public void getOutline(@android.annotation.NonNull
    android.graphics.Outline outline) {
        mAnimatedVectorState.mVectorDrawable.getOutline(outline);
    }

    @java.lang.Override
    public android.graphics.Insets getOpticalInsets() {
        return mAnimatedVectorState.mVectorDrawable.getOpticalInsets();
    }

    @java.lang.Override
    public void inflate(android.content.res.Resources res, org.xmlpull.v1.XmlPullParser parser, android.util.AttributeSet attrs, android.content.res.Resources.Theme theme) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        final android.graphics.drawable.AnimatedVectorDrawable.AnimatedVectorDrawableState state = mAnimatedVectorState;
        int eventType = parser.getEventType();
        float pathErrorScale = 1;
        final int innerDepth = parser.getDepth() + 1;
        // Parse everything until the end of the animated-vector element.
        while ((eventType != org.xmlpull.v1.XmlPullParser.END_DOCUMENT) && ((parser.getDepth() >= innerDepth) || (eventType != org.xmlpull.v1.XmlPullParser.END_TAG))) {
            if (eventType == org.xmlpull.v1.XmlPullParser.START_TAG) {
                final java.lang.String tagName = parser.getName();
                if (android.graphics.drawable.AnimatedVectorDrawable.ANIMATED_VECTOR.equals(tagName)) {
                    final android.content.res.TypedArray a = android.graphics.drawable.Drawable.obtainAttributes(res, theme, attrs, R.styleable.AnimatedVectorDrawable);
                    int drawableRes = a.getResourceId(R.styleable.AnimatedVectorDrawable_drawable, 0);
                    if (drawableRes != 0) {
                        android.graphics.drawable.VectorDrawable vectorDrawable = ((android.graphics.drawable.VectorDrawable) (res.getDrawable(drawableRes, theme).mutate()));
                        vectorDrawable.setAllowCaching(false);
                        vectorDrawable.setCallback(mCallback);
                        pathErrorScale = vectorDrawable.getPixelSize();
                        if (state.mVectorDrawable != null) {
                            state.mVectorDrawable.setCallback(null);
                        }
                        state.mVectorDrawable = vectorDrawable;
                    }
                    a.recycle();
                } else
                    if (android.graphics.drawable.AnimatedVectorDrawable.TARGET.equals(tagName)) {
                        final android.content.res.TypedArray a = android.graphics.drawable.Drawable.obtainAttributes(res, theme, attrs, R.styleable.AnimatedVectorDrawableTarget);
                        final java.lang.String target = a.getString(R.styleable.AnimatedVectorDrawableTarget_name);
                        final int animResId = a.getResourceId(R.styleable.AnimatedVectorDrawableTarget_animation, 0);
                        if (animResId != 0) {
                            if (theme != null) {
                                // The animator here could be ObjectAnimator or AnimatorSet.
                                final android.animation.Animator animator = android.animation.AnimatorInflater.loadAnimator(res, theme, animResId, pathErrorScale);
                                android.graphics.drawable.AnimatedVectorDrawable.updateAnimatorProperty(animator, target, state.mVectorDrawable, state.mShouldIgnoreInvalidAnim);
                                state.addTargetAnimator(target, animator);
                            } else {
                                // The animation may be theme-dependent. As a
                                // workaround until Animator has full support for
                                // applyTheme(), postpone loading the animator
                                // until we have a theme in applyTheme().
                                state.addPendingAnimator(animResId, pathErrorScale, target);
                            }
                        }
                        a.recycle();
                    }

            }
            eventType = parser.next();
        } 
        // If we don't have any pending animations, we don't need to hold a
        // reference to the resources.
        mRes = (state.mPendingAnims == null) ? null : res;
    }

    private static void updateAnimatorProperty(android.animation.Animator animator, java.lang.String targetName, android.graphics.drawable.VectorDrawable vectorDrawable, boolean ignoreInvalidAnim) {
        if (animator instanceof android.animation.ObjectAnimator) {
            // Change the property of the Animator from using reflection based on the property
            // name to a Property object that wraps the setter and getter for modifying that
            // specific property for a given object. By replacing the reflection with a direct call,
            // we can largely reduce the time it takes for a animator to modify a VD property.
            android.animation.PropertyValuesHolder[] holders = ((android.animation.ObjectAnimator) (animator)).getValues();
            for (int i = 0; i < holders.length; i++) {
                android.animation.PropertyValuesHolder pvh = holders[i];
                java.lang.String propertyName = pvh.getPropertyName();
                java.lang.Object targetNameObj = vectorDrawable.getTargetByName(targetName);
                android.util.Property property = null;
                if (targetNameObj instanceof android.graphics.drawable.VectorDrawable.VObject) {
                    property = ((android.graphics.drawable.VectorDrawable.VObject) (targetNameObj)).getProperty(propertyName);
                } else
                    if (targetNameObj instanceof android.graphics.drawable.VectorDrawable.VectorDrawableState) {
                        property = ((android.graphics.drawable.VectorDrawable.VectorDrawableState) (targetNameObj)).getProperty(propertyName);
                    }

                if (property != null) {
                    if (android.graphics.drawable.AnimatedVectorDrawable.containsSameValueType(pvh, property)) {
                        pvh.setProperty(property);
                    } else
                        if (!ignoreInvalidAnim) {
                            throw new java.lang.RuntimeException(((((("Wrong valueType for Property: " + propertyName) + ".  Expected type: ") + property.getType().toString()) + ". Actual ") + "type defined in resources: ") + pvh.getValueType().toString());
                        }

                }
            }
        } else
            if (animator instanceof android.animation.AnimatorSet) {
                for (android.animation.Animator anim : ((android.animation.AnimatorSet) (animator)).getChildAnimations()) {
                    android.graphics.drawable.AnimatedVectorDrawable.updateAnimatorProperty(anim, targetName, vectorDrawable, ignoreInvalidAnim);
                }
            }

    }

    private static boolean containsSameValueType(android.animation.PropertyValuesHolder holder, android.util.Property property) {
        java.lang.Class type1 = holder.getValueType();
        java.lang.Class type2 = property.getType();
        if ((type1 == float.class) || (type1 == java.lang.Float.class)) {
            return (type2 == float.class) || (type2 == java.lang.Float.class);
        } else
            if ((type1 == int.class) || (type1 == java.lang.Integer.class)) {
                return (type2 == int.class) || (type2 == java.lang.Integer.class);
            } else {
                return type1 == type2;
            }

    }

    /**
     * Force to animate on UI thread.
     *
     * @unknown 
     */
    @android.annotation.UnsupportedAppUsage
    public void forceAnimationOnUI() {
        if (mAnimatorSet instanceof android.graphics.drawable.AnimatedVectorDrawable.VectorDrawableAnimatorRT) {
            android.graphics.drawable.AnimatedVectorDrawable.VectorDrawableAnimatorRT animator = ((android.graphics.drawable.AnimatedVectorDrawable.VectorDrawableAnimatorRT) (mAnimatorSet));
            if (animator.isRunning()) {
                throw new java.lang.UnsupportedOperationException("Cannot force Animated Vector Drawable to" + " run on UI thread when the animation has started on RenderThread.");
            }
            fallbackOntoUI();
        }
    }

    private void fallbackOntoUI() {
        if (mAnimatorSet instanceof android.graphics.drawable.AnimatedVectorDrawable.VectorDrawableAnimatorRT) {
            android.graphics.drawable.AnimatedVectorDrawable.VectorDrawableAnimatorRT oldAnim = ((android.graphics.drawable.AnimatedVectorDrawable.VectorDrawableAnimatorRT) (mAnimatorSet));
            mAnimatorSet = new android.graphics.drawable.AnimatedVectorDrawable.VectorDrawableAnimatorUI(this);
            if (mAnimatorSetFromXml != null) {
                mAnimatorSet.init(mAnimatorSetFromXml);
            }
            // Transfer the listener from RT animator to UI animator
            if (oldAnim.mListener != null) {
                mAnimatorSet.setListener(oldAnim.mListener);
            }
            oldAnim.transferPendingActions(mAnimatorSet);
        }
    }

    @java.lang.Override
    public boolean canApplyTheme() {
        return ((mAnimatedVectorState != null) && mAnimatedVectorState.canApplyTheme()) || super.canApplyTheme();
    }

    @java.lang.Override
    public void applyTheme(android.content.res.Resources.Theme t) {
        super.applyTheme(t);
        final android.graphics.drawable.VectorDrawable vectorDrawable = mAnimatedVectorState.mVectorDrawable;
        if ((vectorDrawable != null) && vectorDrawable.canApplyTheme()) {
            vectorDrawable.applyTheme(t);
        }
        if (t != null) {
            mAnimatedVectorState.inflatePendingAnimators(t.getResources(), t);
        }
        // If we don't have any pending animations, we don't need to hold a
        // reference to the resources.
        if (mAnimatedVectorState.mPendingAnims == null) {
            mRes = null;
        }
    }

    private static class AnimatedVectorDrawableState extends android.graphics.drawable.Drawable.ConstantState {
        @android.content.pm.ActivityInfo.Config
        int mChangingConfigurations;

        android.graphics.drawable.VectorDrawable mVectorDrawable;

        private final boolean mShouldIgnoreInvalidAnim;

        /**
         * Animators that require a theme before inflation.
         */
        java.util.ArrayList<android.graphics.drawable.AnimatedVectorDrawable.AnimatedVectorDrawableState.PendingAnimator> mPendingAnims;

        /**
         * Fully inflated animators awaiting cloning into an AnimatorSet.
         */
        java.util.ArrayList<android.animation.Animator> mAnimators;

        /**
         * Map of animators to their target object names
         */
        android.util.ArrayMap<android.animation.Animator, java.lang.String> mTargetNameMap;

        public AnimatedVectorDrawableState(android.graphics.drawable.AnimatedVectorDrawable.AnimatedVectorDrawableState copy, android.graphics.drawable.Drawable.Callback owner, android.content.res.Resources res) {
            mShouldIgnoreInvalidAnim = android.graphics.drawable.AnimatedVectorDrawable.shouldIgnoreInvalidAnimation();
            if (copy != null) {
                mChangingConfigurations = copy.mChangingConfigurations;
                if (copy.mVectorDrawable != null) {
                    final android.graphics.drawable.Drawable.ConstantState cs = copy.mVectorDrawable.getConstantState();
                    if (res != null) {
                        mVectorDrawable = ((android.graphics.drawable.VectorDrawable) (cs.newDrawable(res)));
                    } else {
                        mVectorDrawable = ((android.graphics.drawable.VectorDrawable) (cs.newDrawable()));
                    }
                    mVectorDrawable = ((android.graphics.drawable.VectorDrawable) (mVectorDrawable.mutate()));
                    mVectorDrawable.setCallback(owner);
                    mVectorDrawable.setLayoutDirection(copy.mVectorDrawable.getLayoutDirection());
                    mVectorDrawable.setBounds(copy.mVectorDrawable.getBounds());
                    mVectorDrawable.setAllowCaching(false);
                }
                if (copy.mAnimators != null) {
                    mAnimators = new java.util.ArrayList(copy.mAnimators);
                }
                if (copy.mTargetNameMap != null) {
                    mTargetNameMap = new android.util.ArrayMap(copy.mTargetNameMap);
                }
                if (copy.mPendingAnims != null) {
                    mPendingAnims = new java.util.ArrayList<>(copy.mPendingAnims);
                }
            } else {
                mVectorDrawable = new android.graphics.drawable.VectorDrawable();
            }
        }

        @java.lang.Override
        public boolean canApplyTheme() {
            return (((mVectorDrawable != null) && mVectorDrawable.canApplyTheme()) || (mPendingAnims != null)) || super.canApplyTheme();
        }

        @java.lang.Override
        public android.graphics.drawable.Drawable newDrawable() {
            return new android.graphics.drawable.AnimatedVectorDrawable(this, null);
        }

        @java.lang.Override
        public android.graphics.drawable.Drawable newDrawable(android.content.res.Resources res) {
            return new android.graphics.drawable.AnimatedVectorDrawable(this, res);
        }

        @java.lang.Override
        @android.content.pm.ActivityInfo.Config
        public int getChangingConfigurations() {
            return mChangingConfigurations;
        }

        public void addPendingAnimator(int resId, float pathErrorScale, java.lang.String target) {
            if (mPendingAnims == null) {
                mPendingAnims = new java.util.ArrayList<>(1);
            }
            mPendingAnims.add(new android.graphics.drawable.AnimatedVectorDrawable.AnimatedVectorDrawableState.PendingAnimator(resId, pathErrorScale, target));
        }

        public void addTargetAnimator(java.lang.String targetName, android.animation.Animator animator) {
            if (mAnimators == null) {
                mAnimators = new java.util.ArrayList(1);
                mTargetNameMap = new android.util.ArrayMap(1);
            }
            mAnimators.add(animator);
            mTargetNameMap.put(animator, targetName);
            if (android.graphics.drawable.AnimatedVectorDrawable.DBG_ANIMATION_VECTOR_DRAWABLE) {
                android.util.Log.v(android.graphics.drawable.AnimatedVectorDrawable.LOGTAG, (("add animator  for target " + targetName) + " ") + animator);
            }
        }

        /**
         * Prepares a local set of mutable animators based on the constant
         * state.
         * <p>
         * If there are any pending uninflated animators, attempts to inflate
         * them immediately against the provided resources object.
         *
         * @param animatorSet
         * 		the animator set to which the animators should
         * 		be added
         * @param res
         * 		the resources against which to inflate any pending
         * 		animators, or {@code null} if not available
         */
        public void prepareLocalAnimators(@android.annotation.NonNull
        android.animation.AnimatorSet animatorSet, @android.annotation.Nullable
        android.content.res.Resources res) {
            // Check for uninflated animators. We can remove this after we add
            // support for Animator.applyTheme(). See comments in inflate().
            if (mPendingAnims != null) {
                // Attempt to load animators without applying a theme.
                if (res != null) {
                    inflatePendingAnimators(res, null);
                } else {
                    android.util.Log.e(android.graphics.drawable.AnimatedVectorDrawable.LOGTAG, "Failed to load animators. Either the AnimatedVectorDrawable" + (" must be created using a Resources object or applyTheme() must be" + " called with a non-null Theme object."));
                }
                mPendingAnims = null;
            }
            // Perform a deep copy of the constant state's animators.
            final int count = (mAnimators == null) ? 0 : mAnimators.size();
            if (count > 0) {
                final android.animation.Animator firstAnim = prepareLocalAnimator(0);
                final android.animation.AnimatorSet.Builder builder = animatorSet.play(firstAnim);
                for (int i = 1; i < count; ++i) {
                    final android.animation.Animator nextAnim = prepareLocalAnimator(i);
                    builder.with(nextAnim);
                }
            }
        }

        /**
         * Prepares a local animator for the given index within the constant
         * state's list of animators.
         *
         * @param index
         * 		the index of the animator within the constant state
         */
        private android.animation.Animator prepareLocalAnimator(int index) {
            final android.animation.Animator animator = mAnimators.get(index);
            final android.animation.Animator localAnimator = animator.clone();
            final java.lang.String targetName = mTargetNameMap.get(animator);
            final java.lang.Object target = mVectorDrawable.getTargetByName(targetName);
            if (!mShouldIgnoreInvalidAnim) {
                if (target == null) {
                    throw new java.lang.IllegalStateException(("Target with the name \"" + targetName) + "\" cannot be found in the VectorDrawable to be animated.");
                } else
                    if ((!(target instanceof android.graphics.drawable.VectorDrawable.VectorDrawableState)) && (!(target instanceof android.graphics.drawable.VectorDrawable.VObject))) {
                        throw new java.lang.UnsupportedOperationException((("Target should be either VGroup, VPath," + " or ConstantState, ") + target.getClass()) + " is not supported");
                    }

            }
            localAnimator.setTarget(target);
            return localAnimator;
        }

        /**
         * Inflates pending animators, if any, against a theme. Clears the list of
         * pending animators.
         *
         * @param t
         * 		the theme against which to inflate the animators
         */
        public void inflatePendingAnimators(@android.annotation.NonNull
        android.content.res.Resources res, @android.annotation.Nullable
        android.content.res.Resources.Theme t) {
            final java.util.ArrayList<android.graphics.drawable.AnimatedVectorDrawable.AnimatedVectorDrawableState.PendingAnimator> pendingAnims = mPendingAnims;
            if (pendingAnims != null) {
                mPendingAnims = null;
                for (int i = 0, count = pendingAnims.size(); i < count; i++) {
                    final android.graphics.drawable.AnimatedVectorDrawable.AnimatedVectorDrawableState.PendingAnimator pendingAnimator = pendingAnims.get(i);
                    final android.animation.Animator animator = pendingAnimator.newInstance(res, t);
                    android.graphics.drawable.AnimatedVectorDrawable.updateAnimatorProperty(animator, pendingAnimator.target, mVectorDrawable, mShouldIgnoreInvalidAnim);
                    addTargetAnimator(pendingAnimator.target, animator);
                }
            }
        }

        /**
         * Basically a constant state for Animators until we actually implement
         * constant states for Animators.
         */
        private static class PendingAnimator {
            public final int animResId;

            public final float pathErrorScale;

            public final java.lang.String target;

            public PendingAnimator(int animResId, float pathErrorScale, java.lang.String target) {
                this.animResId = animResId;
                this.pathErrorScale = pathErrorScale;
                this.target = target;
            }

            public android.animation.Animator newInstance(android.content.res.Resources res, android.content.res.Resources.Theme theme) {
                return android.animation.AnimatorInflater.loadAnimator(res, theme, animResId, pathErrorScale);
            }
        }
    }

    @java.lang.Override
    public boolean isRunning() {
        return mAnimatorSet.isRunning();
    }

    /**
     * Resets the AnimatedVectorDrawable to the start state as specified in the animators.
     */
    public void reset() {
        ensureAnimatorSet();
        if (android.graphics.drawable.AnimatedVectorDrawable.DBG_ANIMATION_VECTOR_DRAWABLE) {
            android.util.Log.w(android.graphics.drawable.AnimatedVectorDrawable.LOGTAG, (("calling reset on AVD: " + ((android.graphics.drawable.VectorDrawable.VectorDrawableState) (((android.graphics.drawable.AnimatedVectorDrawable.AnimatedVectorDrawableState) (getConstantState())).mVectorDrawable.getConstantState())).mRootName) + ", at: ") + this);
        }
        mAnimatorSet.reset();
    }

    @java.lang.Override
    public void start() {
        ensureAnimatorSet();
        if (android.graphics.drawable.AnimatedVectorDrawable.DBG_ANIMATION_VECTOR_DRAWABLE) {
            android.util.Log.w(android.graphics.drawable.AnimatedVectorDrawable.LOGTAG, (("calling start on AVD: " + ((android.graphics.drawable.VectorDrawable.VectorDrawableState) (((android.graphics.drawable.AnimatedVectorDrawable.AnimatedVectorDrawableState) (getConstantState())).mVectorDrawable.getConstantState())).mRootName) + ", at: ") + this);
        }
        mAnimatorSet.start();
    }

    @android.annotation.NonNull
    private void ensureAnimatorSet() {
        if (mAnimatorSetFromXml == null) {
            // TODO: Skip the AnimatorSet creation and init the VectorDrawableAnimator directly
            // with a list of LocalAnimators.
            mAnimatorSetFromXml = new android.animation.AnimatorSet();
            mAnimatedVectorState.prepareLocalAnimators(mAnimatorSetFromXml, mRes);
            mAnimatorSet.init(mAnimatorSetFromXml);
            mRes = null;
        }
    }

    @java.lang.Override
    public void stop() {
        if (android.graphics.drawable.AnimatedVectorDrawable.DBG_ANIMATION_VECTOR_DRAWABLE) {
            android.util.Log.w(android.graphics.drawable.AnimatedVectorDrawable.LOGTAG, (("calling stop on AVD: " + ((android.graphics.drawable.VectorDrawable.VectorDrawableState) (((android.graphics.drawable.AnimatedVectorDrawable.AnimatedVectorDrawableState) (getConstantState())).mVectorDrawable.getConstantState())).mRootName) + ", at: ") + this);
        }
        mAnimatorSet.end();
    }

    /**
     * Reverses ongoing animations or starts pending animations in reverse.
     * <p>
     * NOTE: Only works if all animations support reverse. Otherwise, this will
     * do nothing.
     *
     * @unknown 
     */
    public void reverse() {
        ensureAnimatorSet();
        // Only reverse when all the animators can be reversed.
        if (!canReverse()) {
            android.util.Log.w(android.graphics.drawable.AnimatedVectorDrawable.LOGTAG, "AnimatedVectorDrawable can't reverse()");
            return;
        }
        mAnimatorSet.reverse();
    }

    /**
     *
     *
     * @unknown 
     */
    public boolean canReverse() {
        return mAnimatorSet.canReverse();
    }

    private final android.graphics.drawable.Drawable.Callback mCallback = new android.graphics.drawable.Drawable.Callback() {
        @java.lang.Override
        public void invalidateDrawable(@android.annotation.NonNull
        android.graphics.drawable.Drawable who) {
            invalidateSelf();
        }

        @java.lang.Override
        public void scheduleDrawable(@android.annotation.NonNull
        android.graphics.drawable.Drawable who, @android.annotation.NonNull
        java.lang.Runnable what, long when) {
            scheduleSelf(what, when);
        }

        @java.lang.Override
        public void unscheduleDrawable(@android.annotation.NonNull
        android.graphics.drawable.Drawable who, @android.annotation.NonNull
        java.lang.Runnable what) {
            unscheduleSelf(what);
        }
    };

    @java.lang.Override
    public void registerAnimationCallback(@android.annotation.NonNull
    android.graphics.drawable.Animatable2.AnimationCallback callback) {
        if (callback == null) {
            return;
        }
        // Add listener accordingly.
        if (mAnimationCallbacks == null) {
            mAnimationCallbacks = new java.util.ArrayList<>();
        }
        mAnimationCallbacks.add(callback);
        if (mAnimatorListener == null) {
            // Create a animator listener and trigger the callback events when listener is
            // triggered.
            mAnimatorListener = new android.animation.AnimatorListenerAdapter() {
                @java.lang.Override
                public void onAnimationStart(android.animation.Animator animation) {
                    java.util.ArrayList<android.graphics.drawable.Animatable2.AnimationCallback> tmpCallbacks = new java.util.ArrayList<>(mAnimationCallbacks);
                    int size = tmpCallbacks.size();
                    for (int i = 0; i < size; i++) {
                        tmpCallbacks.get(i).onAnimationStart(android.graphics.drawable.AnimatedVectorDrawable.this);
                    }
                }

                @java.lang.Override
                public void onAnimationEnd(android.animation.Animator animation) {
                    java.util.ArrayList<android.graphics.drawable.Animatable2.AnimationCallback> tmpCallbacks = new java.util.ArrayList<>(mAnimationCallbacks);
                    int size = tmpCallbacks.size();
                    for (int i = 0; i < size; i++) {
                        tmpCallbacks.get(i).onAnimationEnd(android.graphics.drawable.AnimatedVectorDrawable.this);
                    }
                }
            };
        }
        mAnimatorSet.setListener(mAnimatorListener);
    }

    // A helper function to clean up the animator listener in the mAnimatorSet.
    private void removeAnimatorSetListener() {
        if (mAnimatorListener != null) {
            mAnimatorSet.removeListener(mAnimatorListener);
            mAnimatorListener = null;
        }
    }

    @java.lang.Override
    public boolean unregisterAnimationCallback(@android.annotation.NonNull
    android.graphics.drawable.Animatable2.AnimationCallback callback) {
        if ((mAnimationCallbacks == null) || (callback == null)) {
            // Nothing to be removed.
            return false;
        }
        boolean removed = mAnimationCallbacks.remove(callback);
        // When the last call back unregistered, remove the listener accordingly.
        if (mAnimationCallbacks.size() == 0) {
            removeAnimatorSetListener();
        }
        return removed;
    }

    @java.lang.Override
    public void clearAnimationCallbacks() {
        removeAnimatorSetListener();
        if (mAnimationCallbacks == null) {
            return;
        }
        mAnimationCallbacks.clear();
    }

    private interface VectorDrawableAnimator {
        void init(@android.annotation.NonNull
        android.animation.AnimatorSet set);

        void start();

        void end();

        void reset();

        void reverse();

        boolean canReverse();

        void setListener(android.animation.Animator.AnimatorListener listener);

        void removeListener(android.animation.Animator.AnimatorListener listener);

        void onDraw(android.graphics.Canvas canvas);

        boolean isStarted();

        boolean isRunning();

        boolean isInfinite();

        void pause();

        void resume();
    }

    private static class VectorDrawableAnimatorUI implements android.graphics.drawable.AnimatedVectorDrawable.VectorDrawableAnimator {
        // mSet is only initialized in init(). So we need to check whether it is null before any
        // operation.
        private android.animation.AnimatorSet mSet = null;

        private final android.graphics.drawable.Drawable mDrawable;

        // Caching the listener in the case when listener operation is called before the mSet is
        // setup by init().
        private java.util.ArrayList<android.animation.Animator.AnimatorListener> mListenerArray = null;

        private boolean mIsInfinite = false;

        VectorDrawableAnimatorUI(@android.annotation.NonNull
        android.graphics.drawable.AnimatedVectorDrawable drawable) {
            mDrawable = drawable;
        }

        @java.lang.Override
        public void init(@android.annotation.NonNull
        android.animation.AnimatorSet set) {
            if (mSet != null) {
                // Already initialized
                throw new java.lang.UnsupportedOperationException("VectorDrawableAnimator cannot be " + "re-initialized");
            }
            // Keep a deep copy of the set, such that set can be still be constantly representing
            // the static content from XML file.
            mSet = set.clone();
            mIsInfinite = mSet.getTotalDuration() == android.animation.Animator.DURATION_INFINITE;
            // If there are listeners added before calling init(), now they should be setup.
            if ((mListenerArray != null) && (!mListenerArray.isEmpty())) {
                for (int i = 0; i < mListenerArray.size(); i++) {
                    mSet.addListener(mListenerArray.get(i));
                }
                mListenerArray.clear();
                mListenerArray = null;
            }
        }

        // Although start(), reset() and reverse() should call init() already, it is better to
        // protect these functions from NPE in any situation.
        @java.lang.Override
        public void start() {
            if ((mSet == null) || mSet.isStarted()) {
                return;
            }
            mSet.start();
            invalidateOwningView();
        }

        @java.lang.Override
        public void end() {
            if (mSet == null) {
                return;
            }
            mSet.end();
        }

        @java.lang.Override
        public void reset() {
            if (mSet == null) {
                return;
            }
            start();
            mSet.cancel();
        }

        @java.lang.Override
        public void reverse() {
            if (mSet == null) {
                return;
            }
            mSet.reverse();
            invalidateOwningView();
        }

        @java.lang.Override
        public boolean canReverse() {
            return (mSet != null) && mSet.canReverse();
        }

        @java.lang.Override
        public void setListener(android.animation.Animator.AnimatorListener listener) {
            if (mSet == null) {
                if (mListenerArray == null) {
                    mListenerArray = new java.util.ArrayList<android.animation.Animator.AnimatorListener>();
                }
                mListenerArray.add(listener);
            } else {
                mSet.addListener(listener);
            }
        }

        @java.lang.Override
        public void removeListener(android.animation.Animator.AnimatorListener listener) {
            if (mSet == null) {
                if (mListenerArray == null) {
                    return;
                }
                mListenerArray.remove(listener);
            } else {
                mSet.removeListener(listener);
            }
        }

        @java.lang.Override
        public void onDraw(android.graphics.Canvas canvas) {
            if ((mSet != null) && mSet.isStarted()) {
                invalidateOwningView();
            }
        }

        @java.lang.Override
        public boolean isStarted() {
            return (mSet != null) && mSet.isStarted();
        }

        @java.lang.Override
        public boolean isRunning() {
            return (mSet != null) && mSet.isRunning();
        }

        @java.lang.Override
        public boolean isInfinite() {
            return mIsInfinite;
        }

        @java.lang.Override
        public void pause() {
            if (mSet == null) {
                return;
            }
            mSet.pause();
        }

        @java.lang.Override
        public void resume() {
            if (mSet == null) {
                return;
            }
            mSet.resume();
        }

        private void invalidateOwningView() {
            mDrawable.invalidateSelf();
        }
    }

    /**
     *
     *
     * @unknown 
     */
    public static class VectorDrawableAnimatorRT implements android.graphics.drawable.AnimatedVectorDrawable.VectorDrawableAnimator , android.view.NativeVectorDrawableAnimator {
        private static final int START_ANIMATION = 1;

        private static final int REVERSE_ANIMATION = 2;

        private static final int RESET_ANIMATION = 3;

        private static final int END_ANIMATION = 4;

        // If the duration of an animation is more than 300 frames, we cap the sample size to 300.
        private static final int MAX_SAMPLE_POINTS = 300;

        private android.os.Handler mHandler;

        private android.animation.Animator.AnimatorListener mListener = null;

        private final android.util.LongArray mStartDelays = new android.util.LongArray();

        private PropertyValuesHolder.PropertyValues mTmpValues = new android.animation.PropertyValuesHolder.PropertyValues();

        private long mSetPtr = 0;

        private boolean mContainsSequentialAnimators = false;

        private boolean mStarted = false;

        private boolean mInitialized = false;

        private boolean mIsReversible = false;

        private boolean mIsInfinite = false;

        private final com.android.internal.util.VirtualRefBasePtr mSetRefBasePtr;

        private java.lang.ref.WeakReference<android.graphics.RenderNode> mLastSeenTarget = null;

        private int mLastListenerId = 0;

        private final android.util.IntArray mPendingAnimationActions = new android.util.IntArray();

        private final android.graphics.drawable.AnimatedVectorDrawable mDrawable;

        VectorDrawableAnimatorRT(android.graphics.drawable.AnimatedVectorDrawable drawable) {
            mDrawable = drawable;
            mSetPtr = android.graphics.drawable.AnimatedVectorDrawable.nCreateAnimatorSet();
            // Increment ref count on native AnimatorSet, so it doesn't get released before Java
            // side is done using it.
            mSetRefBasePtr = new com.android.internal.util.VirtualRefBasePtr(mSetPtr);
        }

        @java.lang.Override
        public void init(@android.annotation.NonNull
        android.animation.AnimatorSet set) {
            if (mInitialized) {
                // Already initialized
                throw new java.lang.UnsupportedOperationException("VectorDrawableAnimator cannot be " + "re-initialized");
            }
            parseAnimatorSet(set, 0);
            long vectorDrawableTreePtr = mDrawable.mAnimatedVectorState.mVectorDrawable.getNativeTree();
            android.graphics.drawable.AnimatedVectorDrawable.nSetVectorDrawableTarget(mSetPtr, vectorDrawableTreePtr);
            mInitialized = true;
            mIsInfinite = set.getTotalDuration() == android.animation.Animator.DURATION_INFINITE;
            // Check reversible.
            mIsReversible = true;
            if (mContainsSequentialAnimators) {
                mIsReversible = false;
            } else {
                // Check if there's any start delay set on child
                for (int i = 0; i < mStartDelays.size(); i++) {
                    if (mStartDelays.get(i) > 0) {
                        mIsReversible = false;
                        return;
                    }
                }
            }
        }

        private void parseAnimatorSet(android.animation.AnimatorSet set, long startTime) {
            java.util.ArrayList<android.animation.Animator> animators = set.getChildAnimations();
            boolean playTogether = set.shouldPlayTogether();
            // Convert AnimatorSet to VectorDrawableAnimatorRT
            for (int i = 0; i < animators.size(); i++) {
                android.animation.Animator animator = animators.get(i);
                // Here we only support ObjectAnimator
                if (animator instanceof android.animation.AnimatorSet) {
                    parseAnimatorSet(((android.animation.AnimatorSet) (animator)), startTime);
                }// ignore ValueAnimators and others because they don't directly modify VD
                 else
                    if (animator instanceof android.animation.ObjectAnimator) {
                        createRTAnimator(((android.animation.ObjectAnimator) (animator)), startTime);
                    }
                // ignore ValueAnimators and others because they don't directly modify VD

                // therefore will be useless to AVD.
                if (!playTogether) {
                    // Assume not play together means play sequentially
                    startTime += animator.getTotalDuration();
                    mContainsSequentialAnimators = true;
                }
            }
        }

        // TODO: This method reads animation data from already parsed Animators. We need to move
        // this step further up the chain in the parser to avoid the detour.
        private void createRTAnimator(android.animation.ObjectAnimator animator, long startTime) {
            android.animation.PropertyValuesHolder[] values = animator.getValues();
            java.lang.Object target = animator.getTarget();
            if (target instanceof android.graphics.drawable.VectorDrawable.VGroup) {
                createRTAnimatorForGroup(values, animator, ((android.graphics.drawable.VectorDrawable.VGroup) (target)), startTime);
            } else
                if (target instanceof android.graphics.drawable.VectorDrawable.VPath) {
                    for (int i = 0; i < values.length; i++) {
                        values[i].getPropertyValues(mTmpValues);
                        if ((mTmpValues.endValue instanceof android.util.PathParser.PathData) && mTmpValues.propertyName.equals("pathData")) {
                            createRTAnimatorForPath(animator, ((android.graphics.drawable.VectorDrawable.VPath) (target)), startTime);
                        } else
                            if (target instanceof android.graphics.drawable.VectorDrawable.VFullPath) {
                                createRTAnimatorForFullPath(animator, ((android.graphics.drawable.VectorDrawable.VFullPath) (target)), startTime);
                            } else
                                if (!mDrawable.mAnimatedVectorState.mShouldIgnoreInvalidAnim) {
                                    throw new java.lang.IllegalArgumentException("ClipPath only supports PathData " + "property");
                                }


                    }
                } else
                    if (target instanceof android.graphics.drawable.VectorDrawable.VectorDrawableState) {
                        createRTAnimatorForRootGroup(values, animator, ((android.graphics.drawable.VectorDrawable.VectorDrawableState) (target)), startTime);
                    }


        }

        private void createRTAnimatorForGroup(android.animation.PropertyValuesHolder[] values, android.animation.ObjectAnimator animator, android.graphics.drawable.VectorDrawable.VGroup target, long startTime) {
            long nativePtr = target.getNativePtr();
            int propertyId;
            for (int i = 0; i < values.length; i++) {
                // TODO: We need to support the rare case in AVD where no start value is provided
                values[i].getPropertyValues(mTmpValues);
                propertyId = android.graphics.drawable.VectorDrawable.VGroup.getPropertyIndex(mTmpValues.propertyName);
                if ((mTmpValues.type != java.lang.Float.class) && (mTmpValues.type != float.class)) {
                    if (android.graphics.drawable.AnimatedVectorDrawable.DBG_ANIMATION_VECTOR_DRAWABLE) {
                        android.util.Log.e(android.graphics.drawable.AnimatedVectorDrawable.LOGTAG, ("Unsupported type: " + mTmpValues.type) + ". Only float value is supported for Groups.");
                    }
                    continue;
                }
                if (propertyId < 0) {
                    if (android.graphics.drawable.AnimatedVectorDrawable.DBG_ANIMATION_VECTOR_DRAWABLE) {
                        android.util.Log.e(android.graphics.drawable.AnimatedVectorDrawable.LOGTAG, ("Unsupported property: " + mTmpValues.propertyName) + " for Vector Drawable Group");
                    }
                    continue;
                }
                long propertyPtr = android.graphics.drawable.AnimatedVectorDrawable.nCreateGroupPropertyHolder(nativePtr, propertyId, ((java.lang.Float) (mTmpValues.startValue)), ((java.lang.Float) (mTmpValues.endValue)));
                if (mTmpValues.dataSource != null) {
                    float[] dataPoints = android.graphics.drawable.AnimatedVectorDrawable.VectorDrawableAnimatorRT.createFloatDataPoints(mTmpValues.dataSource, animator.getDuration());
                    android.graphics.drawable.AnimatedVectorDrawable.nSetPropertyHolderData(propertyPtr, dataPoints, dataPoints.length);
                }
                createNativeChildAnimator(propertyPtr, startTime, animator);
            }
        }

        private void createRTAnimatorForPath(android.animation.ObjectAnimator animator, android.graphics.drawable.VectorDrawable.VPath target, long startTime) {
            long nativePtr = target.getNativePtr();
            long startPathDataPtr = ((android.util.PathParser.PathData) (mTmpValues.startValue)).getNativePtr();
            long endPathDataPtr = ((android.util.PathParser.PathData) (mTmpValues.endValue)).getNativePtr();
            long propertyPtr = android.graphics.drawable.AnimatedVectorDrawable.nCreatePathDataPropertyHolder(nativePtr, startPathDataPtr, endPathDataPtr);
            createNativeChildAnimator(propertyPtr, startTime, animator);
        }

        private void createRTAnimatorForFullPath(android.animation.ObjectAnimator animator, android.graphics.drawable.VectorDrawable.VFullPath target, long startTime) {
            int propertyId = target.getPropertyIndex(mTmpValues.propertyName);
            long propertyPtr;
            long nativePtr = target.getNativePtr();
            if ((mTmpValues.type == java.lang.Float.class) || (mTmpValues.type == float.class)) {
                if (propertyId < 0) {
                    if (mDrawable.mAnimatedVectorState.mShouldIgnoreInvalidAnim) {
                        return;
                    } else {
                        throw new java.lang.IllegalArgumentException(("Property: " + mTmpValues.propertyName) + " is not supported for FullPath");
                    }
                }
                propertyPtr = android.graphics.drawable.AnimatedVectorDrawable.nCreatePathPropertyHolder(nativePtr, propertyId, ((java.lang.Float) (mTmpValues.startValue)), ((java.lang.Float) (mTmpValues.endValue)));
                if (mTmpValues.dataSource != null) {
                    // Pass keyframe data to native, if any.
                    float[] dataPoints = android.graphics.drawable.AnimatedVectorDrawable.VectorDrawableAnimatorRT.createFloatDataPoints(mTmpValues.dataSource, animator.getDuration());
                    android.graphics.drawable.AnimatedVectorDrawable.nSetPropertyHolderData(propertyPtr, dataPoints, dataPoints.length);
                }
            } else
                if ((mTmpValues.type == java.lang.Integer.class) || (mTmpValues.type == int.class)) {
                    propertyPtr = android.graphics.drawable.AnimatedVectorDrawable.nCreatePathColorPropertyHolder(nativePtr, propertyId, ((java.lang.Integer) (mTmpValues.startValue)), ((java.lang.Integer) (mTmpValues.endValue)));
                    if (mTmpValues.dataSource != null) {
                        // Pass keyframe data to native, if any.
                        int[] dataPoints = android.graphics.drawable.AnimatedVectorDrawable.VectorDrawableAnimatorRT.createIntDataPoints(mTmpValues.dataSource, animator.getDuration());
                        android.graphics.drawable.AnimatedVectorDrawable.nSetPropertyHolderData(propertyPtr, dataPoints, dataPoints.length);
                    }
                } else {
                    if (mDrawable.mAnimatedVectorState.mShouldIgnoreInvalidAnim) {
                        return;
                    } else {
                        throw new java.lang.UnsupportedOperationException((("Unsupported type: " + mTmpValues.type) + ". Only float, int or PathData value is ") + "supported for Paths.");
                    }
                }

            createNativeChildAnimator(propertyPtr, startTime, animator);
        }

        private void createRTAnimatorForRootGroup(android.animation.PropertyValuesHolder[] values, android.animation.ObjectAnimator animator, android.graphics.drawable.VectorDrawable.VectorDrawableState target, long startTime) {
            long nativePtr = target.getNativeRenderer();
            if (!animator.getPropertyName().equals("alpha")) {
                if (mDrawable.mAnimatedVectorState.mShouldIgnoreInvalidAnim) {
                    return;
                } else {
                    throw new java.lang.UnsupportedOperationException("Only alpha is supported for root " + "group");
                }
            }
            java.lang.Float startValue = null;
            java.lang.Float endValue = null;
            for (int i = 0; i < values.length; i++) {
                values[i].getPropertyValues(mTmpValues);
                if (mTmpValues.propertyName.equals("alpha")) {
                    startValue = ((java.lang.Float) (mTmpValues.startValue));
                    endValue = ((java.lang.Float) (mTmpValues.endValue));
                    break;
                }
            }
            if ((startValue == null) && (endValue == null)) {
                if (mDrawable.mAnimatedVectorState.mShouldIgnoreInvalidAnim) {
                    return;
                } else {
                    throw new java.lang.UnsupportedOperationException("No alpha values are specified");
                }
            }
            long propertyPtr = android.graphics.drawable.AnimatedVectorDrawable.nCreateRootAlphaPropertyHolder(nativePtr, startValue, endValue);
            if (mTmpValues.dataSource != null) {
                // Pass keyframe data to native, if any.
                float[] dataPoints = android.graphics.drawable.AnimatedVectorDrawable.VectorDrawableAnimatorRT.createFloatDataPoints(mTmpValues.dataSource, animator.getDuration());
                android.graphics.drawable.AnimatedVectorDrawable.nSetPropertyHolderData(propertyPtr, dataPoints, dataPoints.length);
            }
            createNativeChildAnimator(propertyPtr, startTime, animator);
        }

        /**
         * Calculate the amount of frames an animation will run based on duration.
         */
        private static int getFrameCount(long duration) {
            long frameIntervalNanos = android.view.Choreographer.getInstance().getFrameIntervalNanos();
            int animIntervalMs = ((int) (frameIntervalNanos / android.util.TimeUtils.NANOS_PER_MS));
            int numAnimFrames = ((int) (java.lang.Math.ceil(((double) (duration)) / animIntervalMs)));
            // We need 2 frames of data minimum.
            numAnimFrames = java.lang.Math.max(2, numAnimFrames);
            if (numAnimFrames > android.graphics.drawable.AnimatedVectorDrawable.VectorDrawableAnimatorRT.MAX_SAMPLE_POINTS) {
                android.util.Log.w("AnimatedVectorDrawable", ("Duration for the animation is too long :" + duration) + ", the animation will subsample the keyframe or path data.");
                numAnimFrames = android.graphics.drawable.AnimatedVectorDrawable.VectorDrawableAnimatorRT.MAX_SAMPLE_POINTS;
            }
            return numAnimFrames;
        }

        // These are the data points that define the value of the animating properties.
        // e.g. translateX and translateY can animate along a Path, at any fraction in [0, 1]
        // a point on the path corresponds to the values of translateX and translateY.
        // TODO: (Optimization) We should pass the path down in native and chop it into segments
        // in native.
        private static float[] createFloatDataPoints(android.animation.PropertyValuesHolder.PropertyValues.DataSource dataSource, long duration) {
            int numAnimFrames = android.graphics.drawable.AnimatedVectorDrawable.VectorDrawableAnimatorRT.getFrameCount(duration);
            float[] values = new float[numAnimFrames];
            float lastFrame = numAnimFrames - 1;
            for (int i = 0; i < numAnimFrames; i++) {
                float fraction = i / lastFrame;
                values[i] = ((java.lang.Float) (dataSource.getValueAtFraction(fraction)));
            }
            return values;
        }

        private static int[] createIntDataPoints(android.animation.PropertyValuesHolder.PropertyValues.DataSource dataSource, long duration) {
            int numAnimFrames = android.graphics.drawable.AnimatedVectorDrawable.VectorDrawableAnimatorRT.getFrameCount(duration);
            int[] values = new int[numAnimFrames];
            float lastFrame = numAnimFrames - 1;
            for (int i = 0; i < numAnimFrames; i++) {
                float fraction = i / lastFrame;
                values[i] = ((java.lang.Integer) (dataSource.getValueAtFraction(fraction)));
            }
            return values;
        }

        private void createNativeChildAnimator(long propertyPtr, long extraDelay, android.animation.ObjectAnimator animator) {
            long duration = animator.getDuration();
            int repeatCount = animator.getRepeatCount();
            long startDelay = extraDelay + animator.getStartDelay();
            android.animation.TimeInterpolator interpolator = animator.getInterpolator();
            long nativeInterpolator = android.view.RenderNodeAnimatorSetHelper.createNativeInterpolator(interpolator, duration);
            startDelay *= android.animation.ValueAnimator.getDurationScale();
            duration *= android.animation.ValueAnimator.getDurationScale();
            mStartDelays.add(startDelay);
            android.graphics.drawable.AnimatedVectorDrawable.nAddAnimator(mSetPtr, propertyPtr, nativeInterpolator, startDelay, duration, repeatCount, animator.getRepeatMode());
        }

        /**
         * Holds a weak reference to the target that was last seen (through the RecordingCanvas
         * in the last draw call), so that when animator set needs to start, we can add the animator
         * to the last seen RenderNode target and start right away.
         */
        protected void recordLastSeenTarget(android.graphics.RecordingCanvas canvas) {
            final android.graphics.RenderNode node = android.view.RenderNodeAnimatorSetHelper.getTarget(canvas);
            mLastSeenTarget = new java.lang.ref.WeakReference<android.graphics.RenderNode>(node);
            // Add the animator to the list of animators on every draw
            if (mInitialized || (mPendingAnimationActions.size() > 0)) {
                if (useTarget(node)) {
                    if (android.graphics.drawable.AnimatedVectorDrawable.DBG_ANIMATION_VECTOR_DRAWABLE) {
                        android.util.Log.d(android.graphics.drawable.AnimatedVectorDrawable.LOGTAG, "Target is set in the next frame");
                    }
                    for (int i = 0; i < mPendingAnimationActions.size(); i++) {
                        handlePendingAction(mPendingAnimationActions.get(i));
                    }
                    mPendingAnimationActions.clear();
                }
            }
        }

        private void handlePendingAction(int pendingAnimationAction) {
            if (pendingAnimationAction == android.graphics.drawable.AnimatedVectorDrawable.VectorDrawableAnimatorRT.START_ANIMATION) {
                startAnimation();
            } else
                if (pendingAnimationAction == android.graphics.drawable.AnimatedVectorDrawable.VectorDrawableAnimatorRT.REVERSE_ANIMATION) {
                    reverseAnimation();
                } else
                    if (pendingAnimationAction == android.graphics.drawable.AnimatedVectorDrawable.VectorDrawableAnimatorRT.RESET_ANIMATION) {
                        resetAnimation();
                    } else
                        if (pendingAnimationAction == android.graphics.drawable.AnimatedVectorDrawable.VectorDrawableAnimatorRT.END_ANIMATION) {
                            endAnimation();
                        } else {
                            throw new java.lang.UnsupportedOperationException(("Animation action " + pendingAnimationAction) + "is not supported");
                        }



        }

        private boolean useLastSeenTarget() {
            if (mLastSeenTarget != null) {
                final android.graphics.RenderNode target = mLastSeenTarget.get();
                return useTarget(target);
            }
            return false;
        }

        private boolean useTarget(android.graphics.RenderNode target) {
            if ((target != null) && target.isAttached()) {
                target.registerVectorDrawableAnimator(this);
                return true;
            }
            return false;
        }

        private void invalidateOwningView() {
            mDrawable.invalidateSelf();
        }

        private void addPendingAction(int pendingAnimationAction) {
            invalidateOwningView();
            mPendingAnimationActions.add(pendingAnimationAction);
        }

        @java.lang.Override
        public void start() {
            if (!mInitialized) {
                return;
            }
            if (useLastSeenTarget()) {
                if (android.graphics.drawable.AnimatedVectorDrawable.DBG_ANIMATION_VECTOR_DRAWABLE) {
                    android.util.Log.d(android.graphics.drawable.AnimatedVectorDrawable.LOGTAG, "Target is set. Starting VDAnimatorSet from java");
                }
                startAnimation();
            } else {
                addPendingAction(android.graphics.drawable.AnimatedVectorDrawable.VectorDrawableAnimatorRT.START_ANIMATION);
            }
        }

        @java.lang.Override
        public void end() {
            if (!mInitialized) {
                return;
            }
            if (useLastSeenTarget()) {
                endAnimation();
            } else {
                addPendingAction(android.graphics.drawable.AnimatedVectorDrawable.VectorDrawableAnimatorRT.END_ANIMATION);
            }
        }

        @java.lang.Override
        public void reset() {
            if (!mInitialized) {
                return;
            }
            if (useLastSeenTarget()) {
                resetAnimation();
            } else {
                addPendingAction(android.graphics.drawable.AnimatedVectorDrawable.VectorDrawableAnimatorRT.RESET_ANIMATION);
            }
        }

        // Current (imperfect) Java AnimatorSet cannot be reversed when the set contains sequential
        // animators or when the animator set has a start delay
        @java.lang.Override
        public void reverse() {
            if ((!mIsReversible) || (!mInitialized)) {
                return;
            }
            if (useLastSeenTarget()) {
                if (android.graphics.drawable.AnimatedVectorDrawable.DBG_ANIMATION_VECTOR_DRAWABLE) {
                    android.util.Log.d(android.graphics.drawable.AnimatedVectorDrawable.LOGTAG, "Target is set. Reversing VDAnimatorSet from java");
                }
                reverseAnimation();
            } else {
                addPendingAction(android.graphics.drawable.AnimatedVectorDrawable.VectorDrawableAnimatorRT.REVERSE_ANIMATION);
            }
        }

        // This should only be called after animator has been added to the RenderNode target.
        private void startAnimation() {
            if (android.graphics.drawable.AnimatedVectorDrawable.DBG_ANIMATION_VECTOR_DRAWABLE) {
                android.util.Log.w(android.graphics.drawable.AnimatedVectorDrawable.LOGTAG, "starting animation on VD: " + ((android.graphics.drawable.VectorDrawable.VectorDrawableState) (((android.graphics.drawable.AnimatedVectorDrawable.AnimatedVectorDrawableState) (mDrawable.getConstantState())).mVectorDrawable.getConstantState())).mRootName);
            }
            mStarted = true;
            if (mHandler == null) {
                mHandler = new android.os.Handler();
            }
            android.graphics.drawable.AnimatedVectorDrawable.nStart(mSetPtr, this, ++mLastListenerId);
            invalidateOwningView();
            if (mListener != null) {
                mListener.onAnimationStart(null);
            }
        }

        // This should only be called after animator has been added to the RenderNode target.
        private void endAnimation() {
            if (android.graphics.drawable.AnimatedVectorDrawable.DBG_ANIMATION_VECTOR_DRAWABLE) {
                android.util.Log.w(android.graphics.drawable.AnimatedVectorDrawable.LOGTAG, "ending animation on VD: " + ((android.graphics.drawable.VectorDrawable.VectorDrawableState) (((android.graphics.drawable.AnimatedVectorDrawable.AnimatedVectorDrawableState) (mDrawable.getConstantState())).mVectorDrawable.getConstantState())).mRootName);
            }
            android.graphics.drawable.AnimatedVectorDrawable.nEnd(mSetPtr);
            invalidateOwningView();
        }

        // This should only be called after animator has been added to the RenderNode target.
        private void resetAnimation() {
            android.graphics.drawable.AnimatedVectorDrawable.nReset(mSetPtr);
            invalidateOwningView();
        }

        // This should only be called after animator has been added to the RenderNode target.
        private void reverseAnimation() {
            mStarted = true;
            android.graphics.drawable.AnimatedVectorDrawable.nReverse(mSetPtr, this, ++mLastListenerId);
            invalidateOwningView();
            if (mListener != null) {
                mListener.onAnimationStart(null);
            }
        }

        @java.lang.Override
        public long getAnimatorNativePtr() {
            return mSetPtr;
        }

        @java.lang.Override
        public boolean canReverse() {
            return mIsReversible;
        }

        @java.lang.Override
        public boolean isStarted() {
            return mStarted;
        }

        @java.lang.Override
        public boolean isRunning() {
            if (!mInitialized) {
                return false;
            }
            return mStarted;
        }

        @java.lang.Override
        public void setListener(android.animation.Animator.AnimatorListener listener) {
            mListener = listener;
        }

        @java.lang.Override
        public void removeListener(android.animation.Animator.AnimatorListener listener) {
            mListener = null;
        }

        @java.lang.Override
        public void onDraw(android.graphics.Canvas canvas) {
            if (canvas.isHardwareAccelerated()) {
                recordLastSeenTarget(((android.graphics.RecordingCanvas) (canvas)));
            }
        }

        @java.lang.Override
        public boolean isInfinite() {
            return mIsInfinite;
        }

        @java.lang.Override
        public void pause() {
            // TODO: Implement pause for Animator On RT.
        }

        @java.lang.Override
        public void resume() {
            // TODO: Implement resume for Animator On RT.
        }

        private void onAnimationEnd(int listenerId) {
            if (listenerId != mLastListenerId) {
                return;
            }
            if (android.graphics.drawable.AnimatedVectorDrawable.DBG_ANIMATION_VECTOR_DRAWABLE) {
                android.util.Log.d(android.graphics.drawable.AnimatedVectorDrawable.LOGTAG, "on finished called from native");
            }
            mStarted = false;
            // Invalidate in the end of the animation to make sure the data in
            // RT thread is synced back to UI thread.
            invalidateOwningView();
            if (mListener != null) {
                mListener.onAnimationEnd(null);
            }
        }

        // onFinished: should be called from native
        @android.annotation.UnsupportedAppUsage
        private static void callOnFinished(android.graphics.drawable.AnimatedVectorDrawable.VectorDrawableAnimatorRT set, int id) {
            set.mHandler.post(() -> set.onAnimationEnd(id));
        }

        private void transferPendingActions(android.graphics.drawable.AnimatedVectorDrawable.VectorDrawableAnimator animatorSet) {
            for (int i = 0; i < mPendingAnimationActions.size(); i++) {
                int pendingAction = mPendingAnimationActions.get(i);
                if (pendingAction == android.graphics.drawable.AnimatedVectorDrawable.VectorDrawableAnimatorRT.START_ANIMATION) {
                    animatorSet.start();
                } else
                    if (pendingAction == android.graphics.drawable.AnimatedVectorDrawable.VectorDrawableAnimatorRT.END_ANIMATION) {
                        animatorSet.end();
                    } else
                        if (pendingAction == android.graphics.drawable.AnimatedVectorDrawable.VectorDrawableAnimatorRT.REVERSE_ANIMATION) {
                            animatorSet.reverse();
                        } else
                            if (pendingAction == android.graphics.drawable.AnimatedVectorDrawable.VectorDrawableAnimatorRT.RESET_ANIMATION) {
                                animatorSet.reset();
                            } else {
                                throw new java.lang.UnsupportedOperationException(("Animation action " + pendingAction) + "is not supported");
                            }



            }
            mPendingAnimationActions.clear();
        }
    }

    private static native long nCreateAnimatorSet();

    private static native void nSetVectorDrawableTarget(long animatorPtr, long vectorDrawablePtr);

    private static native void nAddAnimator(long setPtr, long propertyValuesHolder, long nativeInterpolator, long startDelay, long duration, int repeatCount, int repeatMode);

    private static native void nSetPropertyHolderData(long nativePtr, float[] data, int length);

    private static native void nSetPropertyHolderData(long nativePtr, int[] data, int length);

    private static native void nStart(long animatorSetPtr, android.graphics.drawable.AnimatedVectorDrawable.VectorDrawableAnimatorRT set, int id);

    private static native void nReverse(long animatorSetPtr, android.graphics.drawable.AnimatedVectorDrawable.VectorDrawableAnimatorRT set, int id);

    // ------------- @FastNative -------------------
    @dalvik.annotation.optimization.FastNative
    private static native long nCreateGroupPropertyHolder(long nativePtr, int propertyId, float startValue, float endValue);

    @dalvik.annotation.optimization.FastNative
    private static native long nCreatePathDataPropertyHolder(long nativePtr, long startValuePtr, long endValuePtr);

    @dalvik.annotation.optimization.FastNative
    private static native long nCreatePathColorPropertyHolder(long nativePtr, int propertyId, int startValue, int endValue);

    @dalvik.annotation.optimization.FastNative
    private static native long nCreatePathPropertyHolder(long nativePtr, int propertyId, float startValue, float endValue);

    @dalvik.annotation.optimization.FastNative
    private static native long nCreateRootAlphaPropertyHolder(long nativePtr, float startValue, float endValue);

    @dalvik.annotation.optimization.FastNative
    private static native void nEnd(long animatorSetPtr);

    @dalvik.annotation.optimization.FastNative
    private static native void nReset(long animatorSetPtr);
}

