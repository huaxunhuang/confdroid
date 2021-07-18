/**
 * Copyright (C) 2006 The Android Open Source Project
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
package android.graphics.drawable;


/**
 * Lets you assign a number of graphic images to a single Drawable and swap out the visible item by a string
 * ID value.
 * <p/>
 * <p>It can be defined in an XML file with the <code>&lt;selector></code> element.
 * Each state Drawable is defined in a nested <code>&lt;item></code> element. For more
 * information, see the guide to <a
 * href="{@docRoot }guide/topics/resources/drawable-resource.html">Drawable Resources</a>.</p>
 *
 * @unknown ref android.R.styleable#StateListDrawable_visible
 * @unknown ref android.R.styleable#StateListDrawable_variablePadding
 * @unknown ref android.R.styleable#StateListDrawable_constantSize
 * @unknown ref android.R.styleable#DrawableStates_state_focused
 * @unknown ref android.R.styleable#DrawableStates_state_window_focused
 * @unknown ref android.R.styleable#DrawableStates_state_enabled
 * @unknown ref android.R.styleable#DrawableStates_state_checkable
 * @unknown ref android.R.styleable#DrawableStates_state_checked
 * @unknown ref android.R.styleable#DrawableStates_state_selected
 * @unknown ref android.R.styleable#DrawableStates_state_activated
 * @unknown ref android.R.styleable#DrawableStates_state_active
 * @unknown ref android.R.styleable#DrawableStates_state_single
 * @unknown ref android.R.styleable#DrawableStates_state_first
 * @unknown ref android.R.styleable#DrawableStates_state_middle
 * @unknown ref android.R.styleable#DrawableStates_state_last
 * @unknown ref android.R.styleable#DrawableStates_state_pressed
 */
public class StateListDrawable extends android.graphics.drawable.DrawableContainer {
    private static final java.lang.String TAG = "StateListDrawable";

    private static final boolean DEBUG = false;

    @android.annotation.UnsupportedAppUsage
    private android.graphics.drawable.StateListDrawable.StateListState mStateListState;

    private boolean mMutated;

    public StateListDrawable() {
        this(null, null);
    }

    /**
     * Add a new image/string ID to the set of images.
     *
     * @param stateSet
     * 		An array of resource Ids to associate with the image.
     * 		Switch to this image by calling setState().
     * @param drawable
     * 		The image to show. Note this must be a unique Drawable that is not shared
     * 		between any other View or Drawable otherwise the results are
     * 		undefined and can lead to unexpected rendering behavior
     */
    public void addState(int[] stateSet, android.graphics.drawable.Drawable drawable) {
        if (drawable != null) {
            mStateListState.addStateSet(stateSet, drawable);
            // in case the new state matches our current state...
            onStateChange(getState());
        }
    }

    @java.lang.Override
    public boolean isStateful() {
        return true;
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public boolean hasFocusStateSpecified() {
        return mStateListState.hasFocusStateSpecified();
    }

    @java.lang.Override
    protected boolean onStateChange(int[] stateSet) {
        final boolean changed = super.onStateChange(stateSet);
        int idx = mStateListState.indexOfStateSet(stateSet);
        if (android.graphics.drawable.StateListDrawable.DEBUG)
            android.graphics.drawable.android.util.Log.i(android.graphics.drawable.StateListDrawable.TAG, (((("onStateChange " + this) + " states ") + java.util.Arrays.toString(stateSet)) + " found ") + idx);

        if (idx < 0) {
            idx = mStateListState.indexOfStateSet(StateSet.WILD_CARD);
        }
        return selectDrawable(idx) || changed;
    }

    @java.lang.Override
    public void inflate(android.content.res.Resources r, org.xmlpull.v1.XmlPullParser parser, android.util.AttributeSet attrs, android.content.res.Resources.Theme theme) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        final android.content.res.TypedArray a = android.graphics.drawable.Drawable.obtainAttributes(r, theme, attrs, R.styleable.StateListDrawable);
        super.inflateWithAttributes(r, parser, a, R.styleable.StateListDrawable_visible);
        updateStateFromTypedArray(a);
        updateDensity(r);
        a.recycle();
        inflateChildElements(r, parser, attrs, theme);
        onStateChange(getState());
    }

    /**
     * Updates the constant state from the values in the typed array.
     */
    @android.annotation.UnsupportedAppUsage
    private void updateStateFromTypedArray(android.content.res.TypedArray a) {
        final android.graphics.drawable.StateListDrawable.StateListState state = mStateListState;
        // Account for any configuration changes.
        state.mChangingConfigurations |= a.getChangingConfigurations();
        // Extract the theme attributes, if any.
        state.mThemeAttrs = a.extractThemeAttrs();
        state.mVariablePadding = a.getBoolean(R.styleable.StateListDrawable_variablePadding, state.mVariablePadding);
        state.mConstantSize = a.getBoolean(R.styleable.StateListDrawable_constantSize, state.mConstantSize);
        state.mEnterFadeDuration = a.getInt(R.styleable.StateListDrawable_enterFadeDuration, state.mEnterFadeDuration);
        state.mExitFadeDuration = a.getInt(R.styleable.StateListDrawable_exitFadeDuration, state.mExitFadeDuration);
        state.mDither = a.getBoolean(R.styleable.StateListDrawable_dither, state.mDither);
        state.mAutoMirrored = a.getBoolean(R.styleable.StateListDrawable_autoMirrored, state.mAutoMirrored);
    }

    /**
     * Inflates child elements from XML.
     */
    private void inflateChildElements(android.content.res.Resources r, org.xmlpull.v1.XmlPullParser parser, android.util.AttributeSet attrs, android.content.res.Resources.Theme theme) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        final android.graphics.drawable.StateListDrawable.StateListState state = mStateListState;
        final int innerDepth = parser.getDepth() + 1;
        int type;
        int depth;
        while (((type = parser.next()) != org.xmlpull.v1.XmlPullParser.END_DOCUMENT) && (((depth = parser.getDepth()) >= innerDepth) || (type != org.xmlpull.v1.XmlPullParser.END_TAG))) {
            if (type != org.xmlpull.v1.XmlPullParser.START_TAG) {
                continue;
            }
            if ((depth > innerDepth) || (!parser.getName().equals("item"))) {
                continue;
            }
            // This allows state list drawable item elements to be themed at
            // inflation time but does NOT make them work for Zygote preload.
            final android.content.res.TypedArray a = android.graphics.drawable.Drawable.obtainAttributes(r, theme, attrs, R.styleable.StateListDrawableItem);
            android.graphics.drawable.Drawable dr = a.getDrawable(R.styleable.StateListDrawableItem_drawable);
            a.recycle();
            final int[] states = extractStateSet(attrs);
            // Loading child elements modifies the state of the AttributeSet's
            // underlying parser, so it needs to happen after obtaining
            // attributes and extracting states.
            if (dr == null) {
                while ((type = parser.next()) == org.xmlpull.v1.XmlPullParser.TEXT) {
                } 
                if (type != org.xmlpull.v1.XmlPullParser.START_TAG) {
                    throw new org.xmlpull.v1.XmlPullParserException((parser.getPositionDescription() + ": <item> tag requires a 'drawable' attribute or ") + "child tag defining a drawable");
                }
                dr = android.graphics.drawable.Drawable.createFromXmlInner(r, parser, attrs, theme);
            }
            state.addStateSet(states, dr);
        } 
    }

    /**
     * Extracts state_ attributes from an attribute set.
     *
     * @param attrs
     * 		The attribute set.
     * @return An array of state_ attributes.
     */
    @android.annotation.UnsupportedAppUsage
    int[] extractStateSet(android.util.AttributeSet attrs) {
        int j = 0;
        final int numAttrs = attrs.getAttributeCount();
        int[] states = new int[numAttrs];
        for (int i = 0; i < numAttrs; i++) {
            final int stateResId = attrs.getAttributeNameResource(i);
            switch (stateResId) {
                case 0 :
                    break;
                case R.attr.drawable :
                case R.attr.id :
                    // Ignore attributes from StateListDrawableItem and
                    // AnimatedStateListDrawableItem.
                    continue;
                default :
                    states[j++] = (attrs.getAttributeBooleanValue(i, false)) ? stateResId : -stateResId;
            }
        }
        states = android.util.StateSet.trimStateSet(states, j);
        return states;
    }

    android.graphics.drawable.StateListDrawable.StateListState getStateListState() {
        return mStateListState;
    }

    /**
     * Gets the number of states contained in this drawable.
     *
     * @return The number of states contained in this drawable.
     * @see #getStateSet(int)
     * @see #getStateDrawable(int)
     */
    public int getStateCount() {
        return mStateListState.getChildCount();
    }

    /**
     * Gets the state set at an index.
     *
     * @param index
     * 		The index of the state set.
     * @return The state set at the index.
     * @see #getStateCount()
     * @see #getStateDrawable(int)
     */
    @android.annotation.NonNull
    public int[] getStateSet(int index) {
        return mStateListState.mStateSets[index];
    }

    /**
     * Gets the drawable at an index.
     *
     * @param index
     * 		The index of the drawable.
     * @return The drawable at the index.
     * @see #getStateCount()
     * @see #getStateSet(int)
     */
    @android.annotation.Nullable
    public android.graphics.drawable.Drawable getStateDrawable(int index) {
        return mStateListState.getChild(index);
    }

    /**
     * Gets the index of the drawable with the provided state set.
     *
     * @param stateSet
     * 		the state set to look up
     * @return the index of the provided state set, or -1 if not found
     * @see #getStateDrawable(int)
     * @see #getStateSet(int)
     */
    public int findStateDrawableIndex(@android.annotation.NonNull
    int[] stateSet) {
        return mStateListState.indexOfStateSet(stateSet);
    }

    @java.lang.Override
    public android.graphics.drawable.Drawable mutate() {
        if ((!mMutated) && (super.mutate() == this)) {
            mStateListState.mutate();
            mMutated = true;
        }
        return this;
    }

    @java.lang.Override
    android.graphics.drawable.StateListDrawable.StateListState cloneConstantState() {
        return new android.graphics.drawable.StateListDrawable.StateListState(mStateListState, this, null);
    }

    /**
     *
     *
     * @unknown 
     */
    public void clearMutated() {
        super.clearMutated();
        mMutated = false;
    }

    static class StateListState extends android.graphics.drawable.DrawableContainer.DrawableContainerState {
        int[] mThemeAttrs;

        int[][] mStateSets;

        StateListState(android.graphics.drawable.StateListDrawable.StateListState orig, android.graphics.drawable.StateListDrawable owner, android.content.res.Resources res) {
            super(orig, owner, res);
            if (orig != null) {
                // Perform a shallow copy and rely on mutate() to deep-copy.
                mThemeAttrs = orig.mThemeAttrs;
                mStateSets = orig.mStateSets;
            } else {
                mThemeAttrs = null;
                mStateSets = new int[getCapacity()][];
            }
        }

        void mutate() {
            mThemeAttrs = (mThemeAttrs != null) ? mThemeAttrs.clone() : null;
            final int[][] stateSets = new int[mStateSets.length][];
            for (int i = mStateSets.length - 1; i >= 0; i--) {
                stateSets[i] = (mStateSets[i] != null) ? mStateSets[i].clone() : null;
            }
            mStateSets = stateSets;
        }

        @android.annotation.UnsupportedAppUsage
        int addStateSet(int[] stateSet, android.graphics.drawable.Drawable drawable) {
            final int pos = addChild(drawable);
            mStateSets[pos] = stateSet;
            return pos;
        }

        int indexOfStateSet(int[] stateSet) {
            final int[][] stateSets = mStateSets;
            final int N = getChildCount();
            for (int i = 0; i < N; i++) {
                if (android.util.StateSet.stateSetMatches(stateSets[i], stateSet)) {
                    return i;
                }
            }
            return -1;
        }

        boolean hasFocusStateSpecified() {
            return android.util.StateSet.containsAttribute(mStateSets, R.attr.state_focused);
        }

        @java.lang.Override
        public android.graphics.drawable.Drawable newDrawable() {
            return new android.graphics.drawable.StateListDrawable(this, null);
        }

        @java.lang.Override
        public android.graphics.drawable.Drawable newDrawable(android.content.res.Resources res) {
            return new android.graphics.drawable.StateListDrawable(this, res);
        }

        @java.lang.Override
        public boolean canApplyTheme() {
            return (mThemeAttrs != null) || super.canApplyTheme();
        }

        @java.lang.Override
        public void growArray(int oldSize, int newSize) {
            super.growArray(oldSize, newSize);
            final int[][] newStateSets = new int[newSize][];
            java.lang.System.arraycopy(mStateSets, 0, newStateSets, 0, oldSize);
            mStateSets = newStateSets;
        }
    }

    @java.lang.Override
    public void applyTheme(android.content.res.Resources.Theme theme) {
        super.applyTheme(theme);
        onStateChange(getState());
    }

    protected void setConstantState(@android.annotation.NonNull
    android.graphics.drawable.DrawableContainer.DrawableContainerState state) {
        super.setConstantState(state);
        if (state instanceof android.graphics.drawable.StateListDrawable.StateListState) {
            mStateListState = ((android.graphics.drawable.StateListDrawable.StateListState) (state));
        }
    }

    private StateListDrawable(android.graphics.drawable.StateListDrawable.StateListState state, android.content.res.Resources res) {
        // Every state list drawable has its own constant state.
        final android.graphics.drawable.StateListDrawable.StateListState newState = new android.graphics.drawable.StateListDrawable.StateListState(state, this, res);
        setConstantState(newState);
        onStateChange(getState());
    }

    /**
     * This constructor exists so subclasses can avoid calling the default
     * constructor and setting up a StateListDrawable-specific constant state.
     */
    StateListDrawable(@android.annotation.Nullable
    android.graphics.drawable.StateListDrawable.StateListState state) {
        if (state != null) {
            setConstantState(state);
        }
    }
}

