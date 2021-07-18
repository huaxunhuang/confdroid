/**
 * Copyright (C) 2015 The Android Open Source Project
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
package android.support.graphics.drawable;


/**
 * For API 24 and above, this class is delegating to the framework's {@link AnimatedVectorDrawable}.
 * For older API version, this class uses {@link android.animation.ObjectAnimator} and
 * {@link android.animation.AnimatorSet} to animate the properties of a
 * {@link VectorDrawableCompat} to create an animated drawable.
 * <p>
 * AnimatedVectorDrawableCompat are defined in the same XML format as {@link AnimatedVectorDrawable}.
 * </p>
 * You can always create a AnimatedVectorDrawableCompat object and use it as a Drawable by the Java
 * API. In order to refer to AnimatedVectorDrawableCompat inside a XML file, you can use
 * app:srcCompat attribute in AppCompat library's ImageButton or ImageView.
 */
@android.annotation.TargetApi(android.os.Build.VERSION_CODES.LOLLIPOP)
public class AnimatedVectorDrawableCompat extends android.support.graphics.drawable.VectorDrawableCommon implements android.graphics.drawable.Animatable {
    private static final java.lang.String LOGTAG = "AnimatedVDCompat";

    private static final java.lang.String ANIMATED_VECTOR = "animated-vector";

    private static final java.lang.String TARGET = "target";

    private static final boolean DBG_ANIMATION_VECTOR_DRAWABLE = false;

    private android.support.graphics.drawable.AnimatedVectorDrawableCompat.AnimatedVectorDrawableCompatState mAnimatedVectorState;

    private android.content.Context mContext;

    private android.animation.ArgbEvaluator mArgbEvaluator = null;

    android.support.graphics.drawable.AnimatedVectorDrawableCompat.AnimatedVectorDrawableDelegateState mCachedConstantStateDelegate;

    AnimatedVectorDrawableCompat() {
        this(null, null, null);
    }

    private AnimatedVectorDrawableCompat(@android.support.annotation.Nullable
    android.content.Context context) {
        this(context, null, null);
    }

    private AnimatedVectorDrawableCompat(@android.support.annotation.Nullable
    android.content.Context context, @android.support.annotation.Nullable
    android.support.graphics.drawable.AnimatedVectorDrawableCompat.AnimatedVectorDrawableCompatState state, @android.support.annotation.Nullable
    android.content.res.Resources res) {
        mContext = context;
        if (state != null) {
            mAnimatedVectorState = state;
        } else {
            mAnimatedVectorState = new android.support.graphics.drawable.AnimatedVectorDrawableCompat.AnimatedVectorDrawableCompatState(context, state, mCallback, res);
        }
    }

    /**
     * mutate() will be effective only if the getConstantState() is returning non-null.
     * Otherwise, it just return the current object without modification.
     */
    @java.lang.Override
    public android.graphics.drawable.Drawable mutate() {
        if (mDelegateDrawable != null) {
            mDelegateDrawable.mutate();
        }
        // For older platforms that there is no delegated drawable, we just return this without
        // any modification here, and the getConstantState() will return null in this case.
        return this;
    }

    /**
     * Create a AnimatedVectorDrawableCompat object.
     *
     * @param context
     * 		the context for creating the animators.
     * @param resId
     * 		the resource ID for AnimatedVectorDrawableCompat object.
     * @return a new AnimatedVectorDrawableCompat or null if parsing error is found.
     */
    @android.support.annotation.Nullable
    public static android.support.graphics.drawable.AnimatedVectorDrawableCompat create(@android.support.annotation.NonNull
    android.content.Context context, @android.support.annotation.DrawableRes
    int resId) {
        if (android.os.Build.VERSION.SDK_INT >= 24) {
            final android.support.graphics.drawable.AnimatedVectorDrawableCompat drawable = new android.support.graphics.drawable.AnimatedVectorDrawableCompat(context);
            drawable.mDelegateDrawable = android.support.v4.content.res.ResourcesCompat.getDrawable(context.getResources(), resId, context.getTheme());
            drawable.mDelegateDrawable.setCallback(drawable.mCallback);
            drawable.mCachedConstantStateDelegate = new android.support.graphics.drawable.AnimatedVectorDrawableCompat.AnimatedVectorDrawableDelegateState(drawable.mDelegateDrawable.getConstantState());
            return drawable;
        }
        android.content.res.Resources resources = context.getResources();
        try {
            final org.xmlpull.v1.XmlPullParser parser = resources.getXml(resId);
            final android.util.AttributeSet attrs = android.util.Xml.asAttributeSet(parser);
            int type;
            while (((type = parser.next()) != org.xmlpull.v1.XmlPullParser.START_TAG) && (type != org.xmlpull.v1.XmlPullParser.END_DOCUMENT)) {
                // Empty loop
            } 
            if (type != org.xmlpull.v1.XmlPullParser.START_TAG) {
                throw new org.xmlpull.v1.XmlPullParserException("No start tag found");
            }
            return android.support.graphics.drawable.AnimatedVectorDrawableCompat.createFromXmlInner(context, context.getResources(), parser, attrs, context.getTheme());
        } catch (org.xmlpull.v1.XmlPullParserException e) {
            android.util.Log.e(android.support.graphics.drawable.AnimatedVectorDrawableCompat.LOGTAG, "parser error", e);
        } catch (java.io.IOException e) {
            android.util.Log.e(android.support.graphics.drawable.AnimatedVectorDrawableCompat.LOGTAG, "parser error", e);
        }
        return null;
    }

    /**
     * Create a AnimatedVectorDrawableCompat from inside an XML document using an optional
     * {@link Theme}. Called on a parser positioned at a tag in an XML
     * document, tries to create a Drawable from that tag. Returns {@code null}
     * if the tag is not a valid drawable.
     */
    public static android.support.graphics.drawable.AnimatedVectorDrawableCompat createFromXmlInner(android.content.Context context, android.content.res.Resources r, org.xmlpull.v1.XmlPullParser parser, android.util.AttributeSet attrs, android.content.res.Resources.Theme theme) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        final android.support.graphics.drawable.AnimatedVectorDrawableCompat drawable = new android.support.graphics.drawable.AnimatedVectorDrawableCompat(context);
        drawable.inflate(r, parser, attrs, theme);
        return drawable;
    }

    /**
     * {@inheritDoc }
     * <strong>Note</strong> that we don't support constant state when SDK < 24.
     * Make sure you check the return value before using it.
     */
    @java.lang.Override
    public android.graphics.drawable.Drawable.ConstantState getConstantState() {
        if (mDelegateDrawable != null) {
            return new android.support.graphics.drawable.AnimatedVectorDrawableCompat.AnimatedVectorDrawableDelegateState(mDelegateDrawable.getConstantState());
        }
        // We can't support constant state in older platform.
        // We need Context to create the animator, and we can't save the context in the constant
        // state.
        return null;
    }

    @java.lang.Override
    public int getChangingConfigurations() {
        if (mDelegateDrawable != null) {
            return mDelegateDrawable.getChangingConfigurations();
        }
        return super.getChangingConfigurations() | mAnimatedVectorState.mChangingConfigurations;
    }

    @java.lang.Override
    public void draw(android.graphics.Canvas canvas) {
        if (mDelegateDrawable != null) {
            mDelegateDrawable.draw(canvas);
            return;
        }
        mAnimatedVectorState.mVectorDrawable.draw(canvas);
        if (isStarted()) {
            invalidateSelf();
        }
    }

    @java.lang.Override
    protected void onBoundsChange(android.graphics.Rect bounds) {
        if (mDelegateDrawable != null) {
            mDelegateDrawable.setBounds(bounds);
            return;
        }
        mAnimatedVectorState.mVectorDrawable.setBounds(bounds);
    }

    @java.lang.Override
    protected boolean onStateChange(int[] state) {
        if (mDelegateDrawable != null) {
            return mDelegateDrawable.setState(state);
        }
        return mAnimatedVectorState.mVectorDrawable.setState(state);
    }

    @java.lang.Override
    protected boolean onLevelChange(int level) {
        if (mDelegateDrawable != null) {
            return mDelegateDrawable.setLevel(level);
        }
        return mAnimatedVectorState.mVectorDrawable.setLevel(level);
    }

    @java.lang.Override
    public int getAlpha() {
        if (mDelegateDrawable != null) {
            return android.support.v4.graphics.drawable.DrawableCompat.getAlpha(mDelegateDrawable);
        }
        return mAnimatedVectorState.mVectorDrawable.getAlpha();
    }

    @java.lang.Override
    public void setAlpha(int alpha) {
        if (mDelegateDrawable != null) {
            mDelegateDrawable.setAlpha(alpha);
            return;
        }
        mAnimatedVectorState.mVectorDrawable.setAlpha(alpha);
    }

    @java.lang.Override
    public void setColorFilter(android.graphics.ColorFilter colorFilter) {
        if (mDelegateDrawable != null) {
            mDelegateDrawable.setColorFilter(colorFilter);
            return;
        }
        mAnimatedVectorState.mVectorDrawable.setColorFilter(colorFilter);
    }

    @java.lang.Override
    public void setTint(int tint) {
        if (mDelegateDrawable != null) {
            android.support.v4.graphics.drawable.DrawableCompat.setTint(mDelegateDrawable, tint);
            return;
        }
        mAnimatedVectorState.mVectorDrawable.setTint(tint);
    }

    @java.lang.Override
    public void setTintList(android.content.res.ColorStateList tint) {
        if (mDelegateDrawable != null) {
            android.support.v4.graphics.drawable.DrawableCompat.setTintList(mDelegateDrawable, tint);
            return;
        }
        mAnimatedVectorState.mVectorDrawable.setTintList(tint);
    }

    @java.lang.Override
    public void setTintMode(android.graphics.PorterDuff.Mode tintMode) {
        if (mDelegateDrawable != null) {
            android.support.v4.graphics.drawable.DrawableCompat.setTintMode(mDelegateDrawable, tintMode);
            return;
        }
        mAnimatedVectorState.mVectorDrawable.setTintMode(tintMode);
    }

    @java.lang.Override
    public boolean setVisible(boolean visible, boolean restart) {
        if (mDelegateDrawable != null) {
            return mDelegateDrawable.setVisible(visible, restart);
        }
        mAnimatedVectorState.mVectorDrawable.setVisible(visible, restart);
        return super.setVisible(visible, restart);
    }

    @java.lang.Override
    public boolean isStateful() {
        if (mDelegateDrawable != null) {
            return mDelegateDrawable.isStateful();
        }
        return mAnimatedVectorState.mVectorDrawable.isStateful();
    }

    @java.lang.Override
    public int getOpacity() {
        if (mDelegateDrawable != null) {
            return mDelegateDrawable.getOpacity();
        }
        return mAnimatedVectorState.mVectorDrawable.getOpacity();
    }

    @java.lang.Override
    public int getIntrinsicWidth() {
        if (mDelegateDrawable != null) {
            return mDelegateDrawable.getIntrinsicWidth();
        }
        return mAnimatedVectorState.mVectorDrawable.getIntrinsicWidth();
    }

    @java.lang.Override
    public int getIntrinsicHeight() {
        if (mDelegateDrawable != null) {
            return mDelegateDrawable.getIntrinsicHeight();
        }
        return mAnimatedVectorState.mVectorDrawable.getIntrinsicHeight();
    }

    @java.lang.Override
    public boolean isAutoMirrored() {
        if (mDelegateDrawable != null) {
            return android.support.v4.graphics.drawable.DrawableCompat.isAutoMirrored(mDelegateDrawable);
        }
        return mAnimatedVectorState.mVectorDrawable.isAutoMirrored();
    }

    @java.lang.Override
    public void setAutoMirrored(boolean mirrored) {
        if (mDelegateDrawable != null) {
            mDelegateDrawable.setAutoMirrored(mirrored);
            return;
        }
        mAnimatedVectorState.mVectorDrawable.setAutoMirrored(mirrored);
    }

    /**
     * Obtains styled attributes from the theme, if available, or unstyled
     * resources if the theme is null.
     */
    static android.content.res.TypedArray obtainAttributes(android.content.res.Resources res, android.content.res.Resources.Theme theme, android.util.AttributeSet set, int[] attrs) {
        if (theme == null) {
            return res.obtainAttributes(set, attrs);
        }
        return theme.obtainStyledAttributes(set, attrs, 0, 0);
    }

    @java.lang.Override
    public void inflate(android.content.res.Resources res, org.xmlpull.v1.XmlPullParser parser, android.util.AttributeSet attrs, android.content.res.Resources.Theme theme) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        if (mDelegateDrawable != null) {
            android.support.v4.graphics.drawable.DrawableCompat.inflate(mDelegateDrawable, res, parser, attrs, theme);
            return;
        }
        int eventType = parser.getEventType();
        final int innerDepth = parser.getDepth() + 1;
        // Parse everything until the end of the animated-vector element.
        while ((eventType != org.xmlpull.v1.XmlPullParser.END_DOCUMENT) && ((parser.getDepth() >= innerDepth) || (eventType != org.xmlpull.v1.XmlPullParser.END_TAG))) {
            if (eventType == org.xmlpull.v1.XmlPullParser.START_TAG) {
                final java.lang.String tagName = parser.getName();
                if (android.support.graphics.drawable.AnimatedVectorDrawableCompat.DBG_ANIMATION_VECTOR_DRAWABLE) {
                    android.util.Log.v(android.support.graphics.drawable.AnimatedVectorDrawableCompat.LOGTAG, "tagName is " + tagName);
                }
                if (android.support.graphics.drawable.AnimatedVectorDrawableCompat.ANIMATED_VECTOR.equals(tagName)) {
                    final android.content.res.TypedArray a = android.support.graphics.drawable.AnimatedVectorDrawableCompat.obtainAttributes(res, theme, attrs, android.support.graphics.drawable.AndroidResources.styleable_AnimatedVectorDrawable);
                    int drawableRes = a.getResourceId(android.support.graphics.drawable.AndroidResources.styleable_AnimatedVectorDrawable_drawable, 0);
                    if (android.support.graphics.drawable.AnimatedVectorDrawableCompat.DBG_ANIMATION_VECTOR_DRAWABLE) {
                        android.util.Log.v(android.support.graphics.drawable.AnimatedVectorDrawableCompat.LOGTAG, "drawableRes is " + drawableRes);
                    }
                    if (drawableRes != 0) {
                        android.support.graphics.drawable.VectorDrawableCompat vectorDrawable = android.support.graphics.drawable.VectorDrawableCompat.create(res, drawableRes, theme);
                        vectorDrawable.setAllowCaching(false);
                        vectorDrawable.setCallback(mCallback);
                        if (mAnimatedVectorState.mVectorDrawable != null) {
                            mAnimatedVectorState.mVectorDrawable.setCallback(null);
                        }
                        mAnimatedVectorState.mVectorDrawable = vectorDrawable;
                    }
                    a.recycle();
                } else
                    if (android.support.graphics.drawable.AnimatedVectorDrawableCompat.TARGET.equals(tagName)) {
                        final android.content.res.TypedArray a = res.obtainAttributes(attrs, android.support.graphics.drawable.AndroidResources.styleable_AnimatedVectorDrawableTarget);
                        final java.lang.String target = a.getString(android.support.graphics.drawable.AndroidResources.styleable_AnimatedVectorDrawableTarget_name);
                        int id = a.getResourceId(android.support.graphics.drawable.AndroidResources.styleable_AnimatedVectorDrawableTarget_animation, 0);
                        if (id != 0) {
                            if (mContext != null) {
                                android.animation.Animator objectAnimator = android.animation.AnimatorInflater.loadAnimator(mContext, id);
                                setupAnimatorsForTarget(target, objectAnimator);
                            } else {
                                throw new java.lang.IllegalStateException("Context can't be null when inflating" + " animators");
                            }
                        }
                        a.recycle();
                    }

            }
            eventType = parser.next();
        } 
    }

    @java.lang.Override
    public void inflate(android.content.res.Resources res, org.xmlpull.v1.XmlPullParser parser, android.util.AttributeSet attrs) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        inflate(res, parser, attrs, null);
    }

    @java.lang.Override
    public void applyTheme(android.content.res.Resources.Theme t) {
        if (mDelegateDrawable != null) {
            android.support.v4.graphics.drawable.DrawableCompat.applyTheme(mDelegateDrawable, t);
            return;
        }
        // TODO: support theming in older platform.
        return;
    }

    @java.lang.Override
    public boolean canApplyTheme() {
        if (mDelegateDrawable != null) {
            return android.support.v4.graphics.drawable.DrawableCompat.canApplyTheme(mDelegateDrawable);
        }
        // TODO: support theming in older platform.
        return false;
    }

    /**
     * Constant state for delegating the creating drawable job.
     * Instead of creating a VectorDrawable, create a VectorDrawableCompat instance which contains
     * a delegated VectorDrawable instance.
     */
    private static class AnimatedVectorDrawableDelegateState extends android.graphics.drawable.Drawable.ConstantState {
        private final android.graphics.drawable.Drawable.ConstantState mDelegateState;

        public AnimatedVectorDrawableDelegateState(android.graphics.drawable.Drawable.ConstantState state) {
            mDelegateState = state;
        }

        @java.lang.Override
        public android.graphics.drawable.Drawable newDrawable() {
            android.support.graphics.drawable.AnimatedVectorDrawableCompat drawableCompat = new android.support.graphics.drawable.AnimatedVectorDrawableCompat();
            drawableCompat.mDelegateDrawable = mDelegateState.newDrawable();
            drawableCompat.mDelegateDrawable.setCallback(drawableCompat.mCallback);
            return drawableCompat;
        }

        @java.lang.Override
        public android.graphics.drawable.Drawable newDrawable(android.content.res.Resources res) {
            android.support.graphics.drawable.AnimatedVectorDrawableCompat drawableCompat = new android.support.graphics.drawable.AnimatedVectorDrawableCompat();
            drawableCompat.mDelegateDrawable = mDelegateState.newDrawable(res);
            drawableCompat.mDelegateDrawable.setCallback(drawableCompat.mCallback);
            return drawableCompat;
        }

        @java.lang.Override
        public android.graphics.drawable.Drawable newDrawable(android.content.res.Resources res, android.content.res.Resources.Theme theme) {
            android.support.graphics.drawable.AnimatedVectorDrawableCompat drawableCompat = new android.support.graphics.drawable.AnimatedVectorDrawableCompat();
            drawableCompat.mDelegateDrawable = mDelegateState.newDrawable(res, theme);
            drawableCompat.mDelegateDrawable.setCallback(drawableCompat.mCallback);
            return drawableCompat;
        }

        @java.lang.Override
        public boolean canApplyTheme() {
            return mDelegateState.canApplyTheme();
        }

        @java.lang.Override
        public int getChangingConfigurations() {
            return mDelegateState.getChangingConfigurations();
        }
    }

    private static class AnimatedVectorDrawableCompatState extends android.graphics.drawable.Drawable.ConstantState {
        int mChangingConfigurations;

        android.support.graphics.drawable.VectorDrawableCompat mVectorDrawable;

        java.util.ArrayList<android.animation.Animator> mAnimators;

        android.support.v4.util.ArrayMap<android.animation.Animator, java.lang.String> mTargetNameMap;

        public AnimatedVectorDrawableCompatState(android.content.Context context, android.support.graphics.drawable.AnimatedVectorDrawableCompat.AnimatedVectorDrawableCompatState copy, android.graphics.drawable.Drawable.Callback owner, android.content.res.Resources res) {
            if (copy != null) {
                mChangingConfigurations = copy.mChangingConfigurations;
                if (copy.mVectorDrawable != null) {
                    final android.graphics.drawable.Drawable.ConstantState cs = copy.mVectorDrawable.getConstantState();
                    if (res != null) {
                        mVectorDrawable = ((android.support.graphics.drawable.VectorDrawableCompat) (cs.newDrawable(res)));
                    } else {
                        mVectorDrawable = ((android.support.graphics.drawable.VectorDrawableCompat) (cs.newDrawable()));
                    }
                    mVectorDrawable = ((android.support.graphics.drawable.VectorDrawableCompat) (mVectorDrawable.mutate()));
                    mVectorDrawable.setCallback(owner);
                    mVectorDrawable.setBounds(copy.mVectorDrawable.getBounds());
                    mVectorDrawable.setAllowCaching(false);
                }
                if (copy.mAnimators != null) {
                    final int numAnimators = copy.mAnimators.size();
                    mAnimators = new java.util.ArrayList<android.animation.Animator>(numAnimators);
                    mTargetNameMap = new android.support.v4.util.ArrayMap<android.animation.Animator, java.lang.String>(numAnimators);
                    for (int i = 0; i < numAnimators; ++i) {
                        android.animation.Animator anim = copy.mAnimators.get(i);
                        android.animation.Animator animClone = anim.clone();
                        java.lang.String targetName = copy.mTargetNameMap.get(anim);
                        java.lang.Object targetObject = mVectorDrawable.getTargetByName(targetName);
                        animClone.setTarget(targetObject);
                        mAnimators.add(animClone);
                        mTargetNameMap.put(animClone, targetName);
                    }
                }
            }
        }

        @java.lang.Override
        public android.graphics.drawable.Drawable newDrawable() {
            throw new java.lang.IllegalStateException("No constant state support for SDK < 24.");
        }

        @java.lang.Override
        public android.graphics.drawable.Drawable newDrawable(android.content.res.Resources res) {
            throw new java.lang.IllegalStateException("No constant state support for SDK < 24.");
        }

        @java.lang.Override
        public int getChangingConfigurations() {
            return mChangingConfigurations;
        }
    }

    /**
     * Utility function to fix color interpolation prior to Lollipop. Without this fix, colors
     * are evaluated as raw integers instead of as colors, which leads to artifacts during
     * fillColor animations.
     */
    private void setupColorAnimator(android.animation.Animator animator) {
        if (animator instanceof android.animation.AnimatorSet) {
            java.util.List<android.animation.Animator> childAnimators = ((android.animation.AnimatorSet) (animator)).getChildAnimations();
            if (childAnimators != null) {
                for (int i = 0; i < childAnimators.size(); ++i) {
                    setupColorAnimator(childAnimators.get(i));
                }
            }
        }
        if (animator instanceof android.animation.ObjectAnimator) {
            android.animation.ObjectAnimator objectAnim = ((android.animation.ObjectAnimator) (animator));
            final java.lang.String propertyName = objectAnim.getPropertyName();
            if ("fillColor".equals(propertyName) || "strokeColor".equals(propertyName)) {
                if (mArgbEvaluator == null) {
                    mArgbEvaluator = new android.animation.ArgbEvaluator();
                }
                objectAnim.setEvaluator(mArgbEvaluator);
            }
        }
    }

    private void setupAnimatorsForTarget(java.lang.String name, android.animation.Animator animator) {
        java.lang.Object target = mAnimatedVectorState.mVectorDrawable.getTargetByName(name);
        animator.setTarget(target);
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP) {
            setupColorAnimator(animator);
        }
        if (mAnimatedVectorState.mAnimators == null) {
            mAnimatedVectorState.mAnimators = new java.util.ArrayList<android.animation.Animator>();
            mAnimatedVectorState.mTargetNameMap = new android.support.v4.util.ArrayMap<android.animation.Animator, java.lang.String>();
        }
        mAnimatedVectorState.mAnimators.add(animator);
        mAnimatedVectorState.mTargetNameMap.put(animator, name);
        if (android.support.graphics.drawable.AnimatedVectorDrawableCompat.DBG_ANIMATION_VECTOR_DRAWABLE) {
            android.util.Log.v(android.support.graphics.drawable.AnimatedVectorDrawableCompat.LOGTAG, (("add animator  for target " + name) + " ") + animator);
        }
    }

    @java.lang.Override
    public boolean isRunning() {
        if (mDelegateDrawable != null) {
            return ((android.graphics.drawable.AnimatedVectorDrawable) (mDelegateDrawable)).isRunning();
        }
        final java.util.ArrayList<android.animation.Animator> animators = mAnimatedVectorState.mAnimators;
        final int size = animators.size();
        for (int i = 0; i < size; i++) {
            final android.animation.Animator animator = animators.get(i);
            if (animator.isRunning()) {
                return true;
            }
        }
        return false;
    }

    private boolean isStarted() {
        final java.util.ArrayList<android.animation.Animator> animators = mAnimatedVectorState.mAnimators;
        if (animators == null) {
            return false;
        }
        final int size = animators.size();
        for (int i = 0; i < size; i++) {
            final android.animation.Animator animator = animators.get(i);
            if (animator.isRunning()) {
                return true;
            }
        }
        return false;
    }

    @java.lang.Override
    public void start() {
        if (mDelegateDrawable != null) {
            ((android.graphics.drawable.AnimatedVectorDrawable) (mDelegateDrawable)).start();
            return;
        }
        // If any one of the animator has not ended, do nothing.
        if (isStarted()) {
            return;
        }
        // Otherwise, kick off every animator.
        final java.util.ArrayList<android.animation.Animator> animators = mAnimatedVectorState.mAnimators;
        final int size = animators.size();
        for (int i = 0; i < size; i++) {
            final android.animation.Animator animator = animators.get(i);
            animator.start();
        }
        invalidateSelf();
    }

    @java.lang.Override
    public void stop() {
        if (mDelegateDrawable != null) {
            ((android.graphics.drawable.AnimatedVectorDrawable) (mDelegateDrawable)).stop();
            return;
        }
        final java.util.ArrayList<android.animation.Animator> animators = mAnimatedVectorState.mAnimators;
        final int size = animators.size();
        for (int i = 0; i < size; i++) {
            final android.animation.Animator animator = animators.get(i);
            animator.end();
        }
    }

    final android.graphics.drawable.Drawable.Callback mCallback = new android.graphics.drawable.Drawable.Callback() {
        @java.lang.Override
        public void invalidateDrawable(android.graphics.drawable.Drawable who) {
            invalidateSelf();
        }

        @java.lang.Override
        public void scheduleDrawable(android.graphics.drawable.Drawable who, java.lang.Runnable what, long when) {
            scheduleSelf(what, when);
        }

        @java.lang.Override
        public void unscheduleDrawable(android.graphics.drawable.Drawable who, java.lang.Runnable what) {
            unscheduleSelf(what);
        }
    };
}

