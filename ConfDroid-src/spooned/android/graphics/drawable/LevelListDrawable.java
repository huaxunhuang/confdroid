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
 * A resource that manages a number of alternate Drawables, each assigned a maximum numerical value.
 * Setting the level value of the object with {@link #setLevel(int)} will load the image with the next
 * greater or equal value assigned to its max attribute.
 * A good example use of
 * a LevelListDrawable would be a battery level indicator icon, with different images to indicate the current
 * battery level.
 * <p>
 * It can be defined in an XML file with the <code>&lt;level-list></code> element.
 * Each Drawable level is defined in a nested <code>&lt;item></code>. For example:
 * </p>
 * <pre>
 * &lt;level-list xmlns:android="http://schemas.android.com/apk/res/android">
 *  &lt;item android:maxLevel="0" android:drawable="@drawable/ic_wifi_signal_1" />
 *  &lt;item android:maxLevel="1" android:drawable="@drawable/ic_wifi_signal_2" />
 *  &lt;item android:maxLevel="2" android:drawable="@drawable/ic_wifi_signal_3" />
 *  &lt;item android:maxLevel="3" android:drawable="@drawable/ic_wifi_signal_4" />
 * &lt;/level-list>
 * </pre>
 * <p>With this XML saved into the res/drawable/ folder of the project, it can be referenced as
 * the drawable for an {@link android.widget.ImageView}. The default image is the first in the list.
 * It can then be changed to one of the other levels with
 * {@link android.widget.ImageView#setImageLevel(int)}. For more
 * information, see the guide to <a
 * href="{@docRoot }guide/topics/resources/drawable-resource.html">Drawable Resources</a>.</p>
 *
 * @unknown ref android.R.styleable#LevelListDrawableItem_minLevel
 * @unknown ref android.R.styleable#LevelListDrawableItem_maxLevel
 * @unknown ref android.R.styleable#LevelListDrawableItem_drawable
 */
public class LevelListDrawable extends android.graphics.drawable.DrawableContainer {
    private android.graphics.drawable.LevelListDrawable.LevelListState mLevelListState;

    private boolean mMutated;

    public LevelListDrawable() {
        this(null, null);
    }

    public void addLevel(int low, int high, android.graphics.drawable.Drawable drawable) {
        if (drawable != null) {
            mLevelListState.addLevel(low, high, drawable);
            // in case the new state matches our current state...
            onLevelChange(getLevel());
        }
    }

    // overrides from Drawable
    @java.lang.Override
    protected boolean onLevelChange(int level) {
        int idx = mLevelListState.indexOfLevel(level);
        if (selectDrawable(idx)) {
            return true;
        }
        return super.onLevelChange(level);
    }

    @java.lang.Override
    public void inflate(android.content.res.Resources r, org.xmlpull.v1.XmlPullParser parser, android.util.AttributeSet attrs, android.content.res.Resources.Theme theme) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        super.inflate(r, parser, attrs, theme);
        updateDensity(r);
        inflateChildElements(r, parser, attrs, theme);
    }

    private void inflateChildElements(android.content.res.Resources r, org.xmlpull.v1.XmlPullParser parser, android.util.AttributeSet attrs, android.content.res.Resources.Theme theme) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        int type;
        int low = 0;
        final int innerDepth = parser.getDepth() + 1;
        int depth;
        while (((type = parser.next()) != org.xmlpull.v1.XmlPullParser.END_DOCUMENT) && (((depth = parser.getDepth()) >= innerDepth) || (type != org.xmlpull.v1.XmlPullParser.END_TAG))) {
            if (type != org.xmlpull.v1.XmlPullParser.START_TAG) {
                continue;
            }
            if ((depth > innerDepth) || (!parser.getName().equals("item"))) {
                continue;
            }
            android.content.res.TypedArray a = android.graphics.drawable.Drawable.obtainAttributes(r, theme, attrs, com.android.internal.R.styleable.LevelListDrawableItem);
            low = a.getInt(com.android.internal.R.styleable.LevelListDrawableItem_minLevel, 0);
            int high = a.getInt(com.android.internal.R.styleable.LevelListDrawableItem_maxLevel, 0);
            int drawableRes = a.getResourceId(com.android.internal.R.styleable.LevelListDrawableItem_drawable, 0);
            a.recycle();
            if (high < 0) {
                throw new org.xmlpull.v1.XmlPullParserException(parser.getPositionDescription() + ": <item> tag requires a 'maxLevel' attribute");
            }
            android.graphics.drawable.Drawable dr;
            if (drawableRes != 0) {
                dr = r.getDrawable(drawableRes, theme);
            } else {
                while ((type = parser.next()) == org.xmlpull.v1.XmlPullParser.TEXT) {
                } 
                if (type != org.xmlpull.v1.XmlPullParser.START_TAG) {
                    throw new org.xmlpull.v1.XmlPullParserException((parser.getPositionDescription() + ": <item> tag requires a 'drawable' attribute or ") + "child tag defining a drawable");
                }
                dr = android.graphics.drawable.Drawable.createFromXmlInner(r, parser, attrs, theme);
            }
            mLevelListState.addLevel(low, high, dr);
        } 
        onLevelChange(getLevel());
    }

    @java.lang.Override
    public android.graphics.drawable.Drawable mutate() {
        if ((!mMutated) && (super.mutate() == this)) {
            mLevelListState.mutate();
            mMutated = true;
        }
        return this;
    }

    @java.lang.Override
    android.graphics.drawable.LevelListDrawable.LevelListState cloneConstantState() {
        return new android.graphics.drawable.LevelListDrawable.LevelListState(mLevelListState, this, null);
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

    private static final class LevelListState extends android.graphics.drawable.DrawableContainer.DrawableContainerState {
        private int[] mLows;

        private int[] mHighs;

        LevelListState(android.graphics.drawable.LevelListDrawable.LevelListState orig, android.graphics.drawable.LevelListDrawable owner, android.content.res.Resources res) {
            super(orig, owner, res);
            if (orig != null) {
                // Perform a shallow copy and rely on mutate() to deep-copy.
                mLows = orig.mLows;
                mHighs = orig.mHighs;
            } else {
                mLows = new int[getCapacity()];
                mHighs = new int[getCapacity()];
            }
        }

        private void mutate() {
            mLows = mLows.clone();
            mHighs = mHighs.clone();
        }

        public void addLevel(int low, int high, android.graphics.drawable.Drawable drawable) {
            int pos = addChild(drawable);
            mLows[pos] = low;
            mHighs[pos] = high;
        }

        public int indexOfLevel(int level) {
            final int[] lows = mLows;
            final int[] highs = mHighs;
            final int N = getChildCount();
            for (int i = 0; i < N; i++) {
                if ((level >= lows[i]) && (level <= highs[i])) {
                    return i;
                }
            }
            return -1;
        }

        @java.lang.Override
        public android.graphics.drawable.Drawable newDrawable() {
            return new android.graphics.drawable.LevelListDrawable(this, null);
        }

        @java.lang.Override
        public android.graphics.drawable.Drawable newDrawable(android.content.res.Resources res) {
            return new android.graphics.drawable.LevelListDrawable(this, res);
        }

        @java.lang.Override
        public void growArray(int oldSize, int newSize) {
            super.growArray(oldSize, newSize);
            int[] newInts = new int[newSize];
            java.lang.System.arraycopy(mLows, 0, newInts, 0, oldSize);
            mLows = newInts;
            newInts = new int[newSize];
            java.lang.System.arraycopy(mHighs, 0, newInts, 0, oldSize);
            mHighs = newInts;
        }
    }

    @java.lang.Override
    protected void setConstantState(@android.annotation.NonNull
    android.graphics.drawable.DrawableContainer.DrawableContainerState state) {
        super.setConstantState(state);
        if (state instanceof android.graphics.drawable.LevelListDrawable.LevelListState) {
            mLevelListState = ((android.graphics.drawable.LevelListDrawable.LevelListState) (state));
        }
    }

    private LevelListDrawable(android.graphics.drawable.LevelListDrawable.LevelListState state, android.content.res.Resources res) {
        final android.graphics.drawable.LevelListDrawable.LevelListState as = new android.graphics.drawable.LevelListDrawable.LevelListState(state, this, res);
        setConstantState(as);
        onLevelChange(getLevel());
    }
}

