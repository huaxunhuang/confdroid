/**
 * Copyright (C) 2010 The Android Open Source Project
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
package android.widget;


/**
 * A Switch is a two-state toggle switch widget that can select between two
 * options. The user may drag the "thumb" back and forth to choose the selected option,
 * or simply tap to toggle as if it were a checkbox. The {@link #setText(CharSequence) text}
 * property controls the text displayed in the label for the switch, whereas the
 * {@link #setTextOff(CharSequence) off} and {@link #setTextOn(CharSequence) on} text
 * controls the text on the thumb. Similarly, the
 * {@link #setTextAppearance(android.content.Context, int) textAppearance} and the related
 * setTypeface() methods control the typeface and style of label text, whereas the
 * {@link #setSwitchTextAppearance(android.content.Context, int) switchTextAppearance} and
 * the related setSwitchTypeface() methods control that of the thumb.
 *
 * <p>{@link android.support.v7.widget.SwitchCompat} is a version of
 * the Switch widget which runs on devices back to API 7.</p>
 *
 * <p>See the <a href="{@docRoot }guide/topics/ui/controls/togglebutton.html">Toggle Buttons</a>
 * guide.</p>
 *
 * @unknown ref android.R.styleable#Switch_textOn
 * @unknown ref android.R.styleable#Switch_textOff
 * @unknown ref android.R.styleable#Switch_switchMinWidth
 * @unknown ref android.R.styleable#Switch_switchPadding
 * @unknown ref android.R.styleable#Switch_switchTextAppearance
 * @unknown ref android.R.styleable#Switch_thumb
 * @unknown ref android.R.styleable#Switch_thumbTextPadding
 * @unknown ref android.R.styleable#Switch_track
 */
public class Switch extends android.widget.CompoundButton {
    private static final int THUMB_ANIMATION_DURATION = 250;

    private static final int TOUCH_MODE_IDLE = 0;

    private static final int TOUCH_MODE_DOWN = 1;

    private static final int TOUCH_MODE_DRAGGING = 2;

    // Enum for the "typeface" XML parameter.
    private static final int SANS = 1;

    private static final int SERIF = 2;

    private static final int MONOSPACE = 3;

    @android.annotation.UnsupportedAppUsage
    private android.graphics.drawable.Drawable mThumbDrawable;

    private android.content.res.ColorStateList mThumbTintList = null;

    private android.graphics.BlendMode mThumbBlendMode = null;

    private boolean mHasThumbTint = false;

    private boolean mHasThumbTintMode = false;

    @android.annotation.UnsupportedAppUsage
    private android.graphics.drawable.Drawable mTrackDrawable;

    private android.content.res.ColorStateList mTrackTintList = null;

    private android.graphics.BlendMode mTrackBlendMode = null;

    private boolean mHasTrackTint = false;

    private boolean mHasTrackTintMode = false;

    private int mThumbTextPadding;

    @android.annotation.UnsupportedAppUsage
    private int mSwitchMinWidth;

    private int mSwitchPadding;

    private boolean mSplitTrack;

    private java.lang.CharSequence mTextOn;

    private java.lang.CharSequence mTextOff;

    private boolean mShowText;

    private boolean mUseFallbackLineSpacing;

    private int mTouchMode;

    private int mTouchSlop;

    private float mTouchX;

    private float mTouchY;

    private android.view.VelocityTracker mVelocityTracker = android.view.VelocityTracker.obtain();

    private int mMinFlingVelocity;

    private float mThumbPosition;

    /**
     * Width required to draw the switch track and thumb. Includes padding and
     * optical bounds for both the track and thumb.
     */
    @android.annotation.UnsupportedAppUsage
    private int mSwitchWidth;

    /**
     * Height required to draw the switch track and thumb. Includes padding and
     * optical bounds for both the track and thumb.
     */
    @android.annotation.UnsupportedAppUsage
    private int mSwitchHeight;

    /**
     * Width of the thumb's content region. Does not include padding or
     * optical bounds.
     */
    @android.annotation.UnsupportedAppUsage
    private int mThumbWidth;

    /**
     * Left bound for drawing the switch track and thumb.
     */
    private int mSwitchLeft;

    /**
     * Top bound for drawing the switch track and thumb.
     */
    private int mSwitchTop;

    /**
     * Right bound for drawing the switch track and thumb.
     */
    private int mSwitchRight;

    /**
     * Bottom bound for drawing the switch track and thumb.
     */
    private int mSwitchBottom;

    private android.text.TextPaint mTextPaint;

    private android.content.res.ColorStateList mTextColors;

    @android.annotation.UnsupportedAppUsage
    private android.text.Layout mOnLayout;

    @android.annotation.UnsupportedAppUsage
    private android.text.Layout mOffLayout;

    private android.text.method.TransformationMethod2 mSwitchTransformationMethod;

    private android.animation.ObjectAnimator mPositionAnimator;

    @java.lang.SuppressWarnings("hiding")
    private final android.graphics.Rect mTempRect = new android.graphics.Rect();

    private static final int[] CHECKED_STATE_SET = new int[]{ R.attr.state_checked };

    /**
     * Construct a new Switch with default styling.
     *
     * @param context
     * 		The Context that will determine this widget's theming.
     */
    public Switch(android.content.Context context) {
        this(context, null);
    }

    /**
     * Construct a new Switch with default styling, overriding specific style
     * attributes as requested.
     *
     * @param context
     * 		The Context that will determine this widget's theming.
     * @param attrs
     * 		Specification of attributes that should deviate from default styling.
     */
    public Switch(android.content.Context context, android.util.AttributeSet attrs) {
        this(context, attrs, com.android.internal.R.attr.switchStyle);
    }

    /**
     * Construct a new Switch with a default style determined by the given theme attribute,
     * overriding specific style attributes as requested.
     *
     * @param context
     * 		The Context that will determine this widget's theming.
     * @param attrs
     * 		Specification of attributes that should deviate from the default styling.
     * @param defStyleAttr
     * 		An attribute in the current theme that contains a
     * 		reference to a style resource that supplies default values for
     * 		the view. Can be 0 to not look for defaults.
     */
    public Switch(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    /**
     * Construct a new Switch with a default style determined by the given theme
     * attribute or style resource, overriding specific style attributes as
     * requested.
     *
     * @param context
     * 		The Context that will determine this widget's theming.
     * @param attrs
     * 		Specification of attributes that should deviate from the
     * 		default styling.
     * @param defStyleAttr
     * 		An attribute in the current theme that contains a
     * 		reference to a style resource that supplies default values for
     * 		the view. Can be 0 to not look for defaults.
     * @param defStyleRes
     * 		A resource identifier of a style resource that
     * 		supplies default values for the view, used only if
     * 		defStyleAttr is 0 or can not be found in the theme. Can be 0
     * 		to not look for defaults.
     */
    public Switch(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mTextPaint = new android.text.TextPaint(android.graphics.Paint.ANTI_ALIAS_FLAG);
        final android.content.res.Resources res = getResources();
        mTextPaint.density = res.getDisplayMetrics().density;
        mTextPaint.setCompatibilityScaling(res.getCompatibilityInfo().applicationScale);
        final android.content.res.TypedArray a = context.obtainStyledAttributes(attrs, com.android.internal.R.styleable.Switch, defStyleAttr, defStyleRes);
        saveAttributeDataForStyleable(context, com.android.internal.R.styleable.Switch, attrs, a, defStyleAttr, defStyleRes);
        mThumbDrawable = a.getDrawable(com.android.internal.R.styleable.Switch_thumb);
        if (mThumbDrawable != null) {
            mThumbDrawable.setCallback(this);
        }
        mTrackDrawable = a.getDrawable(com.android.internal.R.styleable.Switch_track);
        if (mTrackDrawable != null) {
            mTrackDrawable.setCallback(this);
        }
        mTextOn = a.getText(com.android.internal.R.styleable.Switch_textOn);
        mTextOff = a.getText(com.android.internal.R.styleable.Switch_textOff);
        mShowText = a.getBoolean(com.android.internal.R.styleable.Switch_showText, true);
        mThumbTextPadding = a.getDimensionPixelSize(com.android.internal.R.styleable.Switch_thumbTextPadding, 0);
        mSwitchMinWidth = a.getDimensionPixelSize(com.android.internal.R.styleable.Switch_switchMinWidth, 0);
        mSwitchPadding = a.getDimensionPixelSize(com.android.internal.R.styleable.Switch_switchPadding, 0);
        mSplitTrack = a.getBoolean(com.android.internal.R.styleable.Switch_splitTrack, false);
        mUseFallbackLineSpacing = context.getApplicationInfo().targetSdkVersion >= android.os.Build.VERSION_CODES.P;
        android.content.res.ColorStateList thumbTintList = a.getColorStateList(com.android.internal.R.styleable.Switch_thumbTint);
        if (thumbTintList != null) {
            mThumbTintList = thumbTintList;
            mHasThumbTint = true;
        }
        android.graphics.BlendMode thumbTintMode = android.graphics.drawable.Drawable.parseBlendMode(a.getInt(com.android.internal.R.styleable.Switch_thumbTintMode, -1), null);
        if (mThumbBlendMode != thumbTintMode) {
            mThumbBlendMode = thumbTintMode;
            mHasThumbTintMode = true;
        }
        if (mHasThumbTint || mHasThumbTintMode) {
            applyThumbTint();
        }
        android.content.res.ColorStateList trackTintList = a.getColorStateList(com.android.internal.R.styleable.Switch_trackTint);
        if (trackTintList != null) {
            mTrackTintList = trackTintList;
            mHasTrackTint = true;
        }
        android.graphics.BlendMode trackTintMode = android.graphics.drawable.Drawable.parseBlendMode(a.getInt(com.android.internal.R.styleable.Switch_trackTintMode, -1), null);
        if (mTrackBlendMode != trackTintMode) {
            mTrackBlendMode = trackTintMode;
            mHasTrackTintMode = true;
        }
        if (mHasTrackTint || mHasTrackTintMode) {
            applyTrackTint();
        }
        final int appearance = a.getResourceId(com.android.internal.R.styleable.Switch_switchTextAppearance, 0);
        if (appearance != 0) {
            setSwitchTextAppearance(context, appearance);
        }
        a.recycle();
        final android.view.ViewConfiguration config = android.view.ViewConfiguration.get(context);
        mTouchSlop = config.getScaledTouchSlop();
        mMinFlingVelocity = config.getScaledMinimumFlingVelocity();
        // Refresh display with current params
        refreshDrawableState();
        setChecked(isChecked());
    }

    /**
     * Sets the switch text color, size, style, hint color, and highlight color
     * from the specified TextAppearance resource.
     *
     * @unknown ref android.R.styleable#Switch_switchTextAppearance
     */
    public void setSwitchTextAppearance(android.content.Context context, @android.annotation.StyleRes
    int resid) {
        android.content.res.TypedArray appearance = context.obtainStyledAttributes(resid, com.android.internal.R.styleable.TextAppearance);
        android.content.res.ColorStateList colors;
        int ts;
        colors = appearance.getColorStateList(com.android.internal.R.styleable.TextAppearance_textColor);
        if (colors != null) {
            mTextColors = colors;
        } else {
            // If no color set in TextAppearance, default to the view's textColor
            mTextColors = getTextColors();
        }
        ts = appearance.getDimensionPixelSize(com.android.internal.R.styleable.TextAppearance_textSize, 0);
        if (ts != 0) {
            if (ts != mTextPaint.getTextSize()) {
                mTextPaint.setTextSize(ts);
                requestLayout();
            }
        }
        int typefaceIndex;
        int styleIndex;
        typefaceIndex = appearance.getInt(com.android.internal.R.styleable.TextAppearance_typeface, -1);
        styleIndex = appearance.getInt(com.android.internal.R.styleable.TextAppearance_textStyle, -1);
        setSwitchTypefaceByIndex(typefaceIndex, styleIndex);
        boolean allCaps = appearance.getBoolean(com.android.internal.R.styleable.TextAppearance_textAllCaps, false);
        if (allCaps) {
            mSwitchTransformationMethod = new android.text.method.AllCapsTransformationMethod(getContext());
            mSwitchTransformationMethod.setLengthChangesAllowed(true);
        } else {
            mSwitchTransformationMethod = null;
        }
        appearance.recycle();
    }

    private void setSwitchTypefaceByIndex(int typefaceIndex, int styleIndex) {
        android.graphics.Typeface tf = null;
        switch (typefaceIndex) {
            case android.widget.Switch.SANS :
                tf = android.graphics.Typeface.SANS_SERIF;
                break;
            case android.widget.Switch.SERIF :
                tf = android.graphics.Typeface.SERIF;
                break;
            case android.widget.Switch.MONOSPACE :
                tf = android.graphics.Typeface.MONOSPACE;
                break;
        }
        setSwitchTypeface(tf, styleIndex);
    }

    /**
     * Sets the typeface and style in which the text should be displayed on the
     * switch, and turns on the fake bold and italic bits in the Paint if the
     * Typeface that you provided does not have all the bits in the
     * style that you specified.
     */
    public void setSwitchTypeface(android.graphics.Typeface tf, int style) {
        if (style > 0) {
            if (tf == null) {
                tf = android.graphics.Typeface.defaultFromStyle(style);
            } else {
                tf = android.graphics.Typeface.create(tf, style);
            }
            setSwitchTypeface(tf);
            // now compute what (if any) algorithmic styling is needed
            int typefaceStyle = (tf != null) ? tf.getStyle() : 0;
            int need = style & (~typefaceStyle);
            mTextPaint.setFakeBoldText((need & android.graphics.Typeface.BOLD) != 0);
            mTextPaint.setTextSkewX((need & android.graphics.Typeface.ITALIC) != 0 ? -0.25F : 0);
        } else {
            mTextPaint.setFakeBoldText(false);
            mTextPaint.setTextSkewX(0);
            setSwitchTypeface(tf);
        }
    }

    /**
     * Sets the typeface in which the text should be displayed on the switch.
     * Note that not all Typeface families actually have bold and italic
     * variants, so you may need to use
     * {@link #setSwitchTypeface(Typeface, int)} to get the appearance
     * that you actually want.
     *
     * @unknown ref android.R.styleable#TextView_typeface
     * @unknown ref android.R.styleable#TextView_textStyle
     */
    public void setSwitchTypeface(android.graphics.Typeface tf) {
        if (mTextPaint.getTypeface() != tf) {
            mTextPaint.setTypeface(tf);
            requestLayout();
            invalidate();
        }
    }

    /**
     * Set the amount of horizontal padding between the switch and the associated text.
     *
     * @param pixels
     * 		Amount of padding in pixels
     * @unknown ref android.R.styleable#Switch_switchPadding
     */
    public void setSwitchPadding(int pixels) {
        mSwitchPadding = pixels;
        requestLayout();
    }

    /**
     * Get the amount of horizontal padding between the switch and the associated text.
     *
     * @return Amount of padding in pixels
     * @unknown ref android.R.styleable#Switch_switchPadding
     */
    @android.view.inspector.InspectableProperty
    public int getSwitchPadding() {
        return mSwitchPadding;
    }

    /**
     * Set the minimum width of the switch in pixels. The switch's width will be the maximum
     * of this value and its measured width as determined by the switch drawables and text used.
     *
     * @param pixels
     * 		Minimum width of the switch in pixels
     * @unknown ref android.R.styleable#Switch_switchMinWidth
     */
    public void setSwitchMinWidth(int pixels) {
        mSwitchMinWidth = pixels;
        requestLayout();
    }

    /**
     * Get the minimum width of the switch in pixels. The switch's width will be the maximum
     * of this value and its measured width as determined by the switch drawables and text used.
     *
     * @return Minimum width of the switch in pixels
     * @unknown ref android.R.styleable#Switch_switchMinWidth
     */
    @android.view.inspector.InspectableProperty
    public int getSwitchMinWidth() {
        return mSwitchMinWidth;
    }

    /**
     * Set the horizontal padding around the text drawn on the switch itself.
     *
     * @param pixels
     * 		Horizontal padding for switch thumb text in pixels
     * @unknown ref android.R.styleable#Switch_thumbTextPadding
     */
    public void setThumbTextPadding(int pixels) {
        mThumbTextPadding = pixels;
        requestLayout();
    }

    /**
     * Get the horizontal padding around the text drawn on the switch itself.
     *
     * @return Horizontal padding for switch thumb text in pixels
     * @unknown ref android.R.styleable#Switch_thumbTextPadding
     */
    @android.view.inspector.InspectableProperty
    public int getThumbTextPadding() {
        return mThumbTextPadding;
    }

    /**
     * Set the drawable used for the track that the switch slides within.
     *
     * @param track
     * 		Track drawable
     * @unknown ref android.R.styleable#Switch_track
     */
    public void setTrackDrawable(android.graphics.drawable.Drawable track) {
        if (mTrackDrawable != null) {
            mTrackDrawable.setCallback(null);
        }
        mTrackDrawable = track;
        if (track != null) {
            track.setCallback(this);
        }
        requestLayout();
    }

    /**
     * Set the drawable used for the track that the switch slides within.
     *
     * @param resId
     * 		Resource ID of a track drawable
     * @unknown ref android.R.styleable#Switch_track
     */
    public void setTrackResource(@android.annotation.DrawableRes
    int resId) {
        setTrackDrawable(getContext().getDrawable(resId));
    }

    /**
     * Get the drawable used for the track that the switch slides within.
     *
     * @return Track drawable
     * @unknown ref android.R.styleable#Switch_track
     */
    @android.view.inspector.InspectableProperty(name = "track")
    public android.graphics.drawable.Drawable getTrackDrawable() {
        return mTrackDrawable;
    }

    /**
     * Applies a tint to the track drawable. Does not modify the current
     * tint mode, which is {@link PorterDuff.Mode#SRC_IN} by default.
     * <p>
     * Subsequent calls to {@link #setTrackDrawable(Drawable)} will
     * automatically mutate the drawable and apply the specified tint and tint
     * mode using {@link Drawable#setTintList(ColorStateList)}.
     *
     * @param tint
     * 		the tint to apply, may be {@code null} to clear tint
     * @unknown ref android.R.styleable#Switch_trackTint
     * @see #getTrackTintList()
     * @see Drawable#setTintList(ColorStateList)
     */
    public void setTrackTintList(@android.annotation.Nullable
    android.content.res.ColorStateList tint) {
        mTrackTintList = tint;
        mHasTrackTint = true;
        applyTrackTint();
    }

    /**
     *
     *
     * @return the tint applied to the track drawable
     * @unknown ref android.R.styleable#Switch_trackTint
     * @see #setTrackTintList(ColorStateList)
     */
    @android.view.inspector.InspectableProperty(name = "trackTint")
    @android.annotation.Nullable
    public android.content.res.ColorStateList getTrackTintList() {
        return mTrackTintList;
    }

    /**
     * Specifies the blending mode used to apply the tint specified by
     * {@link #setTrackTintList(ColorStateList)}} to the track drawable.
     * The default mode is {@link PorterDuff.Mode#SRC_IN}.
     *
     * @param tintMode
     * 		the blending mode used to apply the tint, may be
     * 		{@code null} to clear tint
     * @unknown ref android.R.styleable#Switch_trackTintMode
     * @see #getTrackTintMode()
     * @see Drawable#setTintMode(PorterDuff.Mode)
     */
    public void setTrackTintMode(@android.annotation.Nullable
    android.graphics.PorterDuff.Mode tintMode) {
        setTrackTintBlendMode(tintMode != null ? android.graphics.BlendMode.fromValue(tintMode.nativeInt) : null);
    }

    /**
     * Specifies the blending mode used to apply the tint specified by
     * {@link #setTrackTintList(ColorStateList)}} to the track drawable.
     * The default mode is {@link BlendMode#SRC_IN}.
     *
     * @param blendMode
     * 		the blending mode used to apply the tint, may be
     * 		{@code null} to clear tint
     * @unknown ref android.R.styleable#Switch_trackTintMode
     * @see #getTrackTintMode()
     * @see Drawable#setTintBlendMode(BlendMode)
     */
    public void setTrackTintBlendMode(@android.annotation.Nullable
    android.graphics.BlendMode blendMode) {
        mTrackBlendMode = blendMode;
        mHasTrackTintMode = true;
        applyTrackTint();
    }

    /**
     *
     *
     * @return the blending mode used to apply the tint to the track
    drawable
     * @unknown ref android.R.styleable#Switch_trackTintMode
     * @see #setTrackTintMode(PorterDuff.Mode)
     */
    @android.view.inspector.InspectableProperty
    @android.annotation.Nullable
    public android.graphics.PorterDuff.Mode getTrackTintMode() {
        android.graphics.BlendMode mode = getTrackTintBlendMode();
        return mode != null ? android.graphics.BlendMode.blendModeToPorterDuffMode(mode) : null;
    }

    /**
     *
     *
     * @return the blending mode used to apply the tint to the track
    drawable
     * @unknown ref android.R.styleable#Switch_trackTintMode
     * @see #setTrackTintBlendMode(BlendMode)
     */
    @android.view.inspector.InspectableProperty(attributeId = com.android.internal.R.styleable.Switch_trackTintMode)
    @android.annotation.Nullable
    public android.graphics.BlendMode getTrackTintBlendMode() {
        return mTrackBlendMode;
    }

    private void applyTrackTint() {
        if ((mTrackDrawable != null) && (mHasTrackTint || mHasTrackTintMode)) {
            mTrackDrawable = mTrackDrawable.mutate();
            if (mHasTrackTint) {
                mTrackDrawable.setTintList(mTrackTintList);
            }
            if (mHasTrackTintMode) {
                mTrackDrawable.setTintBlendMode(mTrackBlendMode);
            }
            // The drawable (or one of its children) may not have been
            // stateful before applying the tint, so let's try again.
            if (mTrackDrawable.isStateful()) {
                mTrackDrawable.setState(getDrawableState());
            }
        }
    }

    /**
     * Set the drawable used for the switch "thumb" - the piece that the user
     * can physically touch and drag along the track.
     *
     * @param thumb
     * 		Thumb drawable
     * @unknown ref android.R.styleable#Switch_thumb
     */
    public void setThumbDrawable(android.graphics.drawable.Drawable thumb) {
        if (mThumbDrawable != null) {
            mThumbDrawable.setCallback(null);
        }
        mThumbDrawable = thumb;
        if (thumb != null) {
            thumb.setCallback(this);
        }
        requestLayout();
    }

    /**
     * Set the drawable used for the switch "thumb" - the piece that the user
     * can physically touch and drag along the track.
     *
     * @param resId
     * 		Resource ID of a thumb drawable
     * @unknown ref android.R.styleable#Switch_thumb
     */
    public void setThumbResource(@android.annotation.DrawableRes
    int resId) {
        setThumbDrawable(getContext().getDrawable(resId));
    }

    /**
     * Get the drawable used for the switch "thumb" - the piece that the user
     * can physically touch and drag along the track.
     *
     * @return Thumb drawable
     * @unknown ref android.R.styleable#Switch_thumb
     */
    @android.view.inspector.InspectableProperty(name = "thumb")
    public android.graphics.drawable.Drawable getThumbDrawable() {
        return mThumbDrawable;
    }

    /**
     * Applies a tint to the thumb drawable. Does not modify the current
     * tint mode, which is {@link PorterDuff.Mode#SRC_IN} by default.
     * <p>
     * Subsequent calls to {@link #setThumbDrawable(Drawable)} will
     * automatically mutate the drawable and apply the specified tint and tint
     * mode using {@link Drawable#setTintList(ColorStateList)}.
     *
     * @param tint
     * 		the tint to apply, may be {@code null} to clear tint
     * @unknown ref android.R.styleable#Switch_thumbTint
     * @see #getThumbTintList()
     * @see Drawable#setTintList(ColorStateList)
     */
    public void setThumbTintList(@android.annotation.Nullable
    android.content.res.ColorStateList tint) {
        mThumbTintList = tint;
        mHasThumbTint = true;
        applyThumbTint();
    }

    /**
     *
     *
     * @return the tint applied to the thumb drawable
     * @unknown ref android.R.styleable#Switch_thumbTint
     * @see #setThumbTintList(ColorStateList)
     */
    @android.view.inspector.InspectableProperty(name = "thumbTint")
    @android.annotation.Nullable
    public android.content.res.ColorStateList getThumbTintList() {
        return mThumbTintList;
    }

    /**
     * Specifies the blending mode used to apply the tint specified by
     * {@link #setThumbTintList(ColorStateList)}} to the thumb drawable.
     * The default mode is {@link PorterDuff.Mode#SRC_IN}.
     *
     * @param tintMode
     * 		the blending mode used to apply the tint, may be
     * 		{@code null} to clear tint
     * @unknown ref android.R.styleable#Switch_thumbTintMode
     * @see #getThumbTintMode()
     * @see Drawable#setTintMode(PorterDuff.Mode)
     */
    public void setThumbTintMode(@android.annotation.Nullable
    android.graphics.PorterDuff.Mode tintMode) {
        setThumbTintBlendMode(tintMode != null ? android.graphics.BlendMode.fromValue(tintMode.nativeInt) : null);
    }

    /**
     * Specifies the blending mode used to apply the tint specified by
     * {@link #setThumbTintList(ColorStateList)}} to the thumb drawable.
     * The default mode is {@link PorterDuff.Mode#SRC_IN}.
     *
     * @param blendMode
     * 		the blending mode used to apply the tint, may be
     * 		{@code null} to clear tint
     * @unknown ref android.R.styleable#Switch_thumbTintMode
     * @see #getThumbTintMode()
     * @see Drawable#setTintBlendMode(BlendMode)
     */
    public void setThumbTintBlendMode(@android.annotation.Nullable
    android.graphics.BlendMode blendMode) {
        mThumbBlendMode = blendMode;
        mHasThumbTintMode = true;
        applyThumbTint();
    }

    /**
     *
     *
     * @return the blending mode used to apply the tint to the thumb
    drawable
     * @unknown ref android.R.styleable#Switch_thumbTintMode
     * @see #setThumbTintMode(PorterDuff.Mode)
     */
    @android.view.inspector.InspectableProperty
    @android.annotation.Nullable
    public android.graphics.PorterDuff.Mode getThumbTintMode() {
        android.graphics.BlendMode mode = getThumbTintBlendMode();
        return mode != null ? android.graphics.BlendMode.blendModeToPorterDuffMode(mode) : null;
    }

    /**
     *
     *
     * @return the blending mode used to apply the tint to the thumb
    drawable
     * @unknown ref android.R.styleable#Switch_thumbTintMode
     * @see #setThumbTintBlendMode(BlendMode)
     */
    @android.view.inspector.InspectableProperty(attributeId = com.android.internal.R.styleable.Switch_thumbTintMode)
    @android.annotation.Nullable
    public android.graphics.BlendMode getThumbTintBlendMode() {
        return mThumbBlendMode;
    }

    private void applyThumbTint() {
        if ((mThumbDrawable != null) && (mHasThumbTint || mHasThumbTintMode)) {
            mThumbDrawable = mThumbDrawable.mutate();
            if (mHasThumbTint) {
                mThumbDrawable.setTintList(mThumbTintList);
            }
            if (mHasThumbTintMode) {
                mThumbDrawable.setTintBlendMode(mThumbBlendMode);
            }
            // The drawable (or one of its children) may not have been
            // stateful before applying the tint, so let's try again.
            if (mThumbDrawable.isStateful()) {
                mThumbDrawable.setState(getDrawableState());
            }
        }
    }

    /**
     * Specifies whether the track should be split by the thumb. When true,
     * the thumb's optical bounds will be clipped out of the track drawable,
     * then the thumb will be drawn into the resulting gap.
     *
     * @param splitTrack
     * 		Whether the track should be split by the thumb
     * @unknown ref android.R.styleable#Switch_splitTrack
     */
    public void setSplitTrack(boolean splitTrack) {
        mSplitTrack = splitTrack;
        invalidate();
    }

    /**
     * Returns whether the track should be split by the thumb.
     *
     * @unknown ref android.R.styleable#Switch_splitTrack
     */
    @android.view.inspector.InspectableProperty
    public boolean getSplitTrack() {
        return mSplitTrack;
    }

    /**
     * Returns the text displayed when the button is in the checked state.
     *
     * @unknown ref android.R.styleable#Switch_textOn
     */
    @android.view.inspector.InspectableProperty
    public java.lang.CharSequence getTextOn() {
        return mTextOn;
    }

    /**
     * Sets the text displayed when the button is in the checked state.
     *
     * @unknown ref android.R.styleable#Switch_textOn
     */
    public void setTextOn(java.lang.CharSequence textOn) {
        mTextOn = textOn;
        requestLayout();
    }

    /**
     * Returns the text displayed when the button is not in the checked state.
     *
     * @unknown ref android.R.styleable#Switch_textOff
     */
    @android.view.inspector.InspectableProperty
    public java.lang.CharSequence getTextOff() {
        return mTextOff;
    }

    /**
     * Sets the text displayed when the button is not in the checked state.
     *
     * @unknown ref android.R.styleable#Switch_textOff
     */
    public void setTextOff(java.lang.CharSequence textOff) {
        mTextOff = textOff;
        requestLayout();
    }

    /**
     * Sets whether the on/off text should be displayed.
     *
     * @param showText
     * 		{@code true} to display on/off text
     * @unknown ref android.R.styleable#Switch_showText
     */
    public void setShowText(boolean showText) {
        if (mShowText != showText) {
            mShowText = showText;
            requestLayout();
        }
    }

    /**
     *
     *
     * @return whether the on/off text should be displayed
     * @unknown ref android.R.styleable#Switch_showText
     */
    @android.view.inspector.InspectableProperty
    public boolean getShowText() {
        return mShowText;
    }

    @java.lang.Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mShowText) {
            if (mOnLayout == null) {
                mOnLayout = makeLayout(mTextOn);
            }
            if (mOffLayout == null) {
                mOffLayout = makeLayout(mTextOff);
            }
        }
        final android.graphics.Rect padding = mTempRect;
        final int thumbWidth;
        final int thumbHeight;
        if (mThumbDrawable != null) {
            // Cached thumb width does not include padding.
            mThumbDrawable.getPadding(padding);
            thumbWidth = (mThumbDrawable.getIntrinsicWidth() - padding.left) - padding.right;
            thumbHeight = mThumbDrawable.getIntrinsicHeight();
        } else {
            thumbWidth = 0;
            thumbHeight = 0;
        }
        final int maxTextWidth;
        if (mShowText) {
            maxTextWidth = java.lang.Math.max(mOnLayout.getWidth(), mOffLayout.getWidth()) + (mThumbTextPadding * 2);
        } else {
            maxTextWidth = 0;
        }
        mThumbWidth = java.lang.Math.max(maxTextWidth, thumbWidth);
        final int trackHeight;
        if (mTrackDrawable != null) {
            mTrackDrawable.getPadding(padding);
            trackHeight = mTrackDrawable.getIntrinsicHeight();
        } else {
            padding.setEmpty();
            trackHeight = 0;
        }
        // Adjust left and right padding to ensure there's enough room for the
        // thumb's padding (when present).
        int paddingLeft = padding.left;
        int paddingRight = padding.right;
        if (mThumbDrawable != null) {
            final android.graphics.Insets inset = mThumbDrawable.getOpticalInsets();
            paddingLeft = java.lang.Math.max(paddingLeft, inset.left);
            paddingRight = java.lang.Math.max(paddingRight, inset.right);
        }
        final int switchWidth = java.lang.Math.max(mSwitchMinWidth, ((2 * mThumbWidth) + paddingLeft) + paddingRight);
        final int switchHeight = java.lang.Math.max(trackHeight, thumbHeight);
        mSwitchWidth = switchWidth;
        mSwitchHeight = switchHeight;
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        final int measuredHeight = getMeasuredHeight();
        if (measuredHeight < switchHeight) {
            setMeasuredDimension(getMeasuredWidthAndState(), switchHeight);
        }
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public void onPopulateAccessibilityEventInternal(android.view.accessibility.AccessibilityEvent event) {
        super.onPopulateAccessibilityEventInternal(event);
        final java.lang.CharSequence text = (isChecked()) ? mTextOn : mTextOff;
        if (text != null) {
            event.getText().add(text);
        }
    }

    private android.text.Layout makeLayout(java.lang.CharSequence text) {
        final java.lang.CharSequence transformed = (mSwitchTransformationMethod != null) ? mSwitchTransformationMethod.getTransformation(text, this) : text;
        int width = ((int) (java.lang.Math.ceil(android.text.Layout.getDesiredWidth(transformed, 0, transformed.length(), mTextPaint, getTextDirectionHeuristic()))));
        return StaticLayout.Builder.obtain(transformed, 0, transformed.length(), mTextPaint, width).setUseLineSpacingFromFallbacks(mUseFallbackLineSpacing).build();
    }

    /**
     *
     *
     * @return true if (x, y) is within the target area of the switch thumb
     */
    private boolean hitThumb(float x, float y) {
        if (mThumbDrawable == null) {
            return false;
        }
        // Relies on mTempRect, MUST be called first!
        final int thumbOffset = getThumbOffset();
        mThumbDrawable.getPadding(mTempRect);
        final int thumbTop = mSwitchTop - mTouchSlop;
        final int thumbLeft = (mSwitchLeft + thumbOffset) - mTouchSlop;
        final int thumbRight = (((thumbLeft + mThumbWidth) + mTempRect.left) + mTempRect.right) + mTouchSlop;
        final int thumbBottom = mSwitchBottom + mTouchSlop;
        return (((x > thumbLeft) && (x < thumbRight)) && (y > thumbTop)) && (y < thumbBottom);
    }

    @java.lang.Override
    public boolean onTouchEvent(android.view.MotionEvent ev) {
        mVelocityTracker.addMovement(ev);
        final int action = ev.getActionMasked();
        switch (action) {
            case android.view.MotionEvent.ACTION_DOWN :
                {
                    final float x = ev.getX();
                    final float y = ev.getY();
                    if (isEnabled() && hitThumb(x, y)) {
                        mTouchMode = android.widget.Switch.TOUCH_MODE_DOWN;
                        mTouchX = x;
                        mTouchY = y;
                    }
                    break;
                }
            case android.view.MotionEvent.ACTION_MOVE :
                {
                    switch (mTouchMode) {
                        case android.widget.Switch.TOUCH_MODE_IDLE :
                            // Didn't target the thumb, treat normally.
                            break;
                        case android.widget.Switch.TOUCH_MODE_DOWN :
                            {
                                final float x = ev.getX();
                                final float y = ev.getY();
                                if ((java.lang.Math.abs(x - mTouchX) > mTouchSlop) || (java.lang.Math.abs(y - mTouchY) > mTouchSlop)) {
                                    mTouchMode = android.widget.Switch.TOUCH_MODE_DRAGGING;
                                    getParent().requestDisallowInterceptTouchEvent(true);
                                    mTouchX = x;
                                    mTouchY = y;
                                    return true;
                                }
                                break;
                            }
                        case android.widget.Switch.TOUCH_MODE_DRAGGING :
                            {
                                final float x = ev.getX();
                                final int thumbScrollRange = getThumbScrollRange();
                                final float thumbScrollOffset = x - mTouchX;
                                float dPos;
                                if (thumbScrollRange != 0) {
                                    dPos = thumbScrollOffset / thumbScrollRange;
                                } else {
                                    // If the thumb scroll range is empty, just use the
                                    // movement direction to snap on or off.
                                    dPos = (thumbScrollOffset > 0) ? 1 : -1;
                                }
                                if (isLayoutRtl()) {
                                    dPos = -dPos;
                                }
                                final float newPos = android.util.MathUtils.constrain(mThumbPosition + dPos, 0, 1);
                                if (newPos != mThumbPosition) {
                                    mTouchX = x;
                                    setThumbPosition(newPos);
                                }
                                return true;
                            }
                    }
                    break;
                }
            case android.view.MotionEvent.ACTION_UP :
            case android.view.MotionEvent.ACTION_CANCEL :
                {
                    if (mTouchMode == android.widget.Switch.TOUCH_MODE_DRAGGING) {
                        stopDrag(ev);
                        // Allow super class to handle pressed state, etc.
                        super.onTouchEvent(ev);
                        return true;
                    }
                    mTouchMode = android.widget.Switch.TOUCH_MODE_IDLE;
                    mVelocityTracker.clear();
                    break;
                }
        }
        return super.onTouchEvent(ev);
    }

    private void cancelSuperTouch(android.view.MotionEvent ev) {
        android.view.MotionEvent cancel = android.view.MotionEvent.obtain(ev);
        cancel.setAction(android.view.MotionEvent.ACTION_CANCEL);
        super.onTouchEvent(cancel);
        cancel.recycle();
    }

    /**
     * Called from onTouchEvent to end a drag operation.
     *
     * @param ev
     * 		Event that triggered the end of drag mode - ACTION_UP or ACTION_CANCEL
     */
    private void stopDrag(android.view.MotionEvent ev) {
        mTouchMode = android.widget.Switch.TOUCH_MODE_IDLE;
        // Commit the change if the event is up and not canceled and the switch
        // has not been disabled during the drag.
        final boolean commitChange = (ev.getAction() == android.view.MotionEvent.ACTION_UP) && isEnabled();
        final boolean oldState = isChecked();
        final boolean newState;
        if (commitChange) {
            mVelocityTracker.computeCurrentVelocity(1000);
            final float xvel = mVelocityTracker.getXVelocity();
            if (java.lang.Math.abs(xvel) > mMinFlingVelocity) {
                newState = (isLayoutRtl()) ? xvel < 0 : xvel > 0;
            } else {
                newState = getTargetCheckedState();
            }
        } else {
            newState = oldState;
        }
        if (newState != oldState) {
            playSoundEffect(android.view.SoundEffectConstants.CLICK);
        }
        // Always call setChecked so that the thumb is moved back to the correct edge
        setChecked(newState);
        cancelSuperTouch(ev);
    }

    private void animateThumbToCheckedState(boolean newCheckedState) {
        final float targetPosition = (newCheckedState) ? 1 : 0;
        mPositionAnimator = android.animation.ObjectAnimator.ofFloat(this, android.widget.Switch.THUMB_POS, targetPosition);
        mPositionAnimator.setDuration(android.widget.Switch.THUMB_ANIMATION_DURATION);
        mPositionAnimator.setAutoCancel(true);
        mPositionAnimator.start();
    }

    @android.annotation.UnsupportedAppUsage
    private void cancelPositionAnimator() {
        if (mPositionAnimator != null) {
            mPositionAnimator.cancel();
        }
    }

    private boolean getTargetCheckedState() {
        return mThumbPosition > 0.5F;
    }

    /**
     * Sets the thumb position as a decimal value between 0 (off) and 1 (on).
     *
     * @param position
     * 		new position between [0,1]
     */
    @android.annotation.UnsupportedAppUsage
    private void setThumbPosition(float position) {
        mThumbPosition = position;
        invalidate();
    }

    @java.lang.Override
    public void toggle() {
        setChecked(!isChecked());
    }

    @java.lang.Override
    public void setChecked(boolean checked) {
        super.setChecked(checked);
        // Calling the super method may result in setChecked() getting called
        // recursively with a different value, so load the REAL value...
        checked = isChecked();
        if (isAttachedToWindow() && isLaidOut()) {
            animateThumbToCheckedState(checked);
        } else {
            // Immediately move the thumb to the new position.
            cancelPositionAnimator();
            setThumbPosition(checked ? 1 : 0);
        }
    }

    @java.lang.Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        int opticalInsetLeft = 0;
        int opticalInsetRight = 0;
        if (mThumbDrawable != null) {
            final android.graphics.Rect trackPadding = mTempRect;
            if (mTrackDrawable != null) {
                mTrackDrawable.getPadding(trackPadding);
            } else {
                trackPadding.setEmpty();
            }
            final android.graphics.Insets insets = mThumbDrawable.getOpticalInsets();
            opticalInsetLeft = java.lang.Math.max(0, insets.left - trackPadding.left);
            opticalInsetRight = java.lang.Math.max(0, insets.right - trackPadding.right);
        }
        final int switchRight;
        final int switchLeft;
        if (isLayoutRtl()) {
            switchLeft = getPaddingLeft() + opticalInsetLeft;
            switchRight = ((switchLeft + mSwitchWidth) - opticalInsetLeft) - opticalInsetRight;
        } else {
            switchRight = (getWidth() - getPaddingRight()) - opticalInsetRight;
            switchLeft = ((switchRight - mSwitchWidth) + opticalInsetLeft) + opticalInsetRight;
        }
        final int switchTop;
        final int switchBottom;
        switch (getGravity() & android.view.Gravity.VERTICAL_GRAVITY_MASK) {
            default :
            case android.view.Gravity.TOP :
                switchTop = getPaddingTop();
                switchBottom = switchTop + mSwitchHeight;
                break;
            case android.view.Gravity.CENTER_VERTICAL :
                switchTop = (((getPaddingTop() + getHeight()) - getPaddingBottom()) / 2) - (mSwitchHeight / 2);
                switchBottom = switchTop + mSwitchHeight;
                break;
            case android.view.Gravity.BOTTOM :
                switchBottom = getHeight() - getPaddingBottom();
                switchTop = switchBottom - mSwitchHeight;
                break;
        }
        mSwitchLeft = switchLeft;
        mSwitchTop = switchTop;
        mSwitchBottom = switchBottom;
        mSwitchRight = switchRight;
    }

    @java.lang.Override
    public void draw(android.graphics.Canvas c) {
        final android.graphics.Rect padding = mTempRect;
        final int switchLeft = mSwitchLeft;
        final int switchTop = mSwitchTop;
        final int switchRight = mSwitchRight;
        final int switchBottom = mSwitchBottom;
        int thumbInitialLeft = switchLeft + getThumbOffset();
        final android.graphics.Insets thumbInsets;
        if (mThumbDrawable != null) {
            thumbInsets = mThumbDrawable.getOpticalInsets();
        } else {
            thumbInsets = android.graphics.Insets.NONE;
        }
        // Layout the track.
        if (mTrackDrawable != null) {
            mTrackDrawable.getPadding(padding);
            // Adjust thumb position for track padding.
            thumbInitialLeft += padding.left;
            // If necessary, offset by the optical insets of the thumb asset.
            int trackLeft = switchLeft;
            int trackTop = switchTop;
            int trackRight = switchRight;
            int trackBottom = switchBottom;
            if (thumbInsets != android.graphics.Insets.NONE) {
                if (thumbInsets.left > padding.left) {
                    trackLeft += thumbInsets.left - padding.left;
                }
                if (thumbInsets.top > padding.top) {
                    trackTop += thumbInsets.top - padding.top;
                }
                if (thumbInsets.right > padding.right) {
                    trackRight -= thumbInsets.right - padding.right;
                }
                if (thumbInsets.bottom > padding.bottom) {
                    trackBottom -= thumbInsets.bottom - padding.bottom;
                }
            }
            mTrackDrawable.setBounds(trackLeft, trackTop, trackRight, trackBottom);
        }
        // Layout the thumb.
        if (mThumbDrawable != null) {
            mThumbDrawable.getPadding(padding);
            final int thumbLeft = thumbInitialLeft - padding.left;
            final int thumbRight = (thumbInitialLeft + mThumbWidth) + padding.right;
            mThumbDrawable.setBounds(thumbLeft, switchTop, thumbRight, switchBottom);
            final android.graphics.drawable.Drawable background = getBackground();
            if (background != null) {
                background.setHotspotBounds(thumbLeft, switchTop, thumbRight, switchBottom);
            }
        }
        // Draw the background.
        super.draw(c);
    }

    @java.lang.Override
    protected void onDraw(android.graphics.Canvas canvas) {
        super.onDraw(canvas);
        final android.graphics.Rect padding = mTempRect;
        final android.graphics.drawable.Drawable trackDrawable = mTrackDrawable;
        if (trackDrawable != null) {
            trackDrawable.getPadding(padding);
        } else {
            padding.setEmpty();
        }
        final int switchTop = mSwitchTop;
        final int switchBottom = mSwitchBottom;
        final int switchInnerTop = switchTop + padding.top;
        final int switchInnerBottom = switchBottom - padding.bottom;
        final android.graphics.drawable.Drawable thumbDrawable = mThumbDrawable;
        if (trackDrawable != null) {
            if (mSplitTrack && (thumbDrawable != null)) {
                final android.graphics.Insets insets = thumbDrawable.getOpticalInsets();
                thumbDrawable.copyBounds(padding);
                padding.left += insets.left;
                padding.right -= insets.right;
                final int saveCount = canvas.save();
                canvas.clipRect(padding, android.graphics.Region.Op.DIFFERENCE);
                trackDrawable.draw(canvas);
                canvas.restoreToCount(saveCount);
            } else {
                trackDrawable.draw(canvas);
            }
        }
        final int saveCount = canvas.save();
        if (thumbDrawable != null) {
            thumbDrawable.draw(canvas);
        }
        final android.text.Layout switchText = (getTargetCheckedState()) ? mOnLayout : mOffLayout;
        if (switchText != null) {
            final int[] drawableState = getDrawableState();
            if (mTextColors != null) {
                mTextPaint.setColor(mTextColors.getColorForState(drawableState, 0));
            }
            mTextPaint.drawableState = drawableState;
            final int cX;
            if (thumbDrawable != null) {
                final android.graphics.Rect bounds = thumbDrawable.getBounds();
                cX = bounds.left + bounds.right;
            } else {
                cX = getWidth();
            }
            final int left = (cX / 2) - (switchText.getWidth() / 2);
            final int top = ((switchInnerTop + switchInnerBottom) / 2) - (switchText.getHeight() / 2);
            canvas.translate(left, top);
            switchText.draw(canvas);
        }
        canvas.restoreToCount(saveCount);
    }

    @java.lang.Override
    public int getCompoundPaddingLeft() {
        if (!isLayoutRtl()) {
            return super.getCompoundPaddingLeft();
        }
        int padding = super.getCompoundPaddingLeft() + mSwitchWidth;
        if (!android.text.TextUtils.isEmpty(getText())) {
            padding += mSwitchPadding;
        }
        return padding;
    }

    @java.lang.Override
    public int getCompoundPaddingRight() {
        if (isLayoutRtl()) {
            return super.getCompoundPaddingRight();
        }
        int padding = super.getCompoundPaddingRight() + mSwitchWidth;
        if (!android.text.TextUtils.isEmpty(getText())) {
            padding += mSwitchPadding;
        }
        return padding;
    }

    /**
     * Translates thumb position to offset according to current RTL setting and
     * thumb scroll range. Accounts for both track and thumb padding.
     *
     * @return thumb offset
     */
    private int getThumbOffset() {
        final float thumbPosition;
        if (isLayoutRtl()) {
            thumbPosition = 1 - mThumbPosition;
        } else {
            thumbPosition = mThumbPosition;
        }
        return ((int) ((thumbPosition * getThumbScrollRange()) + 0.5F));
    }

    private int getThumbScrollRange() {
        if (mTrackDrawable != null) {
            final android.graphics.Rect padding = mTempRect;
            mTrackDrawable.getPadding(padding);
            final android.graphics.Insets insets;
            if (mThumbDrawable != null) {
                insets = mThumbDrawable.getOpticalInsets();
            } else {
                insets = android.graphics.Insets.NONE;
            }
            return ((((mSwitchWidth - mThumbWidth) - padding.left) - padding.right) - insets.left) - insets.right;
        } else {
            return 0;
        }
    }

    @java.lang.Override
    protected int[] onCreateDrawableState(int extraSpace) {
        final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
        if (isChecked()) {
            android.view.View.mergeDrawableStates(drawableState, android.widget.Switch.CHECKED_STATE_SET);
        }
        return drawableState;
    }

    @java.lang.Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        final int[] state = getDrawableState();
        boolean changed = false;
        final android.graphics.drawable.Drawable thumbDrawable = mThumbDrawable;
        if ((thumbDrawable != null) && thumbDrawable.isStateful()) {
            changed |= thumbDrawable.setState(state);
        }
        final android.graphics.drawable.Drawable trackDrawable = mTrackDrawable;
        if ((trackDrawable != null) && trackDrawable.isStateful()) {
            changed |= trackDrawable.setState(state);
        }
        if (changed) {
            invalidate();
        }
    }

    @java.lang.Override
    public void drawableHotspotChanged(float x, float y) {
        super.drawableHotspotChanged(x, y);
        if (mThumbDrawable != null) {
            mThumbDrawable.setHotspot(x, y);
        }
        if (mTrackDrawable != null) {
            mTrackDrawable.setHotspot(x, y);
        }
    }

    @java.lang.Override
    protected boolean verifyDrawable(@android.annotation.NonNull
    android.graphics.drawable.Drawable who) {
        return (super.verifyDrawable(who) || (who == mThumbDrawable)) || (who == mTrackDrawable);
    }

    @java.lang.Override
    public void jumpDrawablesToCurrentState() {
        super.jumpDrawablesToCurrentState();
        if (mThumbDrawable != null) {
            mThumbDrawable.jumpToCurrentState();
        }
        if (mTrackDrawable != null) {
            mTrackDrawable.jumpToCurrentState();
        }
        if ((mPositionAnimator != null) && mPositionAnimator.isStarted()) {
            mPositionAnimator.end();
            mPositionAnimator = null;
        }
    }

    @java.lang.Override
    public java.lang.CharSequence getAccessibilityClassName() {
        return android.widget.Switch.class.getName();
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    protected void onProvideStructure(@android.annotation.NonNull
    android.view.ViewStructure structure, @android.view.View.ViewStructureType
    int viewFor, int flags) {
        java.lang.CharSequence switchText = (isChecked()) ? mTextOn : mTextOff;
        if (!android.text.TextUtils.isEmpty(switchText)) {
            java.lang.CharSequence oldText = structure.getText();
            if (android.text.TextUtils.isEmpty(oldText)) {
                structure.setText(switchText);
            } else {
                java.lang.StringBuilder newText = new java.lang.StringBuilder();
                newText.append(oldText).append(' ').append(switchText);
                structure.setText(newText);
            }
            // The style of the label text is provided via the base TextView class. This is more
            // relevant than the style of the (optional) on/off text on the switch button itself,
            // so ignore the size/color/style stored this.mTextPaint.
        }
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public void onInitializeAccessibilityNodeInfoInternal(android.view.accessibility.AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfoInternal(info);
        java.lang.CharSequence switchText = (isChecked()) ? mTextOn : mTextOff;
        if (!android.text.TextUtils.isEmpty(switchText)) {
            java.lang.CharSequence oldText = info.getText();
            if (android.text.TextUtils.isEmpty(oldText)) {
                info.setText(switchText);
            } else {
                java.lang.StringBuilder newText = new java.lang.StringBuilder();
                newText.append(oldText).append(' ').append(switchText);
                info.setText(newText);
            }
        }
    }

    private static final android.util.FloatProperty<android.widget.Switch> THUMB_POS = new android.util.FloatProperty<android.widget.Switch>("thumbPos") {
        @java.lang.Override
        public java.lang.Float get(android.widget.Switch object) {
            return object.mThumbPosition;
        }

        @java.lang.Override
        public void setValue(android.widget.Switch object, float value) {
            object.setThumbPosition(value);
        }
    };
}

