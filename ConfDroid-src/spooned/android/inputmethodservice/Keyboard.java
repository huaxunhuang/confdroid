/**
 * Copyright (C) 2008-2009 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package android.inputmethodservice;


/**
 * Loads an XML description of a keyboard and stores the attributes of the keys. A keyboard
 * consists of rows of keys.
 * <p>The layout file for a keyboard contains XML that looks like the following snippet:</p>
 * <pre>
 * &lt;Keyboard
 *         android:keyWidth="%10p"
 *         android:keyHeight="50px"
 *         android:horizontalGap="2px"
 *         android:verticalGap="2px" &gt;
 *     &lt;Row android:keyWidth="32px" &gt;
 *         &lt;Key android:keyLabel="A" /&gt;
 *         ...
 *     &lt;/Row&gt;
 *     ...
 * &lt;/Keyboard&gt;
 * </pre>
 *
 * @unknown ref android.R.styleable#Keyboard_keyWidth
 * @unknown ref android.R.styleable#Keyboard_keyHeight
 * @unknown ref android.R.styleable#Keyboard_horizontalGap
 * @unknown ref android.R.styleable#Keyboard_verticalGap
 */
public class Keyboard {
    static final java.lang.String TAG = "Keyboard";

    // Keyboard XML Tags
    private static final java.lang.String TAG_KEYBOARD = "Keyboard";

    private static final java.lang.String TAG_ROW = "Row";

    private static final java.lang.String TAG_KEY = "Key";

    public static final int EDGE_LEFT = 0x1;

    public static final int EDGE_RIGHT = 0x2;

    public static final int EDGE_TOP = 0x4;

    public static final int EDGE_BOTTOM = 0x8;

    public static final int KEYCODE_SHIFT = -1;

    public static final int KEYCODE_MODE_CHANGE = -2;

    public static final int KEYCODE_CANCEL = -3;

    public static final int KEYCODE_DONE = -4;

    public static final int KEYCODE_DELETE = -5;

    public static final int KEYCODE_ALT = -6;

    /**
     * Keyboard label *
     */
    private java.lang.CharSequence mLabel;

    /**
     * Horizontal gap default for all rows
     */
    private int mDefaultHorizontalGap;

    /**
     * Default key width
     */
    private int mDefaultWidth;

    /**
     * Default key height
     */
    private int mDefaultHeight;

    /**
     * Default gap between rows
     */
    private int mDefaultVerticalGap;

    /**
     * Is the keyboard in the shifted state
     */
    private boolean mShifted;

    /**
     * Key instance for the shift key, if present
     */
    private android.inputmethodservice.Keyboard.Key[] mShiftKeys = new android.inputmethodservice.Keyboard.Key[]{ null, null };

    /**
     * Key index for the shift key, if present
     */
    private int[] mShiftKeyIndices = new int[]{ -1, -1 };

    /**
     * Current key width, while loading the keyboard
     */
    private int mKeyWidth;

    /**
     * Current key height, while loading the keyboard
     */
    private int mKeyHeight;

    /**
     * Total height of the keyboard, including the padding and keys
     */
    private int mTotalHeight;

    /**
     * Total width of the keyboard, including left side gaps and keys, but not any gaps on the
     * right side.
     */
    private int mTotalWidth;

    /**
     * List of keys in this keyboard
     */
    private java.util.List<android.inputmethodservice.Keyboard.Key> mKeys;

    /**
     * List of modifier keys such as Shift & Alt, if any
     */
    private java.util.List<android.inputmethodservice.Keyboard.Key> mModifierKeys;

    /**
     * Width of the screen available to fit the keyboard
     */
    private int mDisplayWidth;

    /**
     * Height of the screen
     */
    private int mDisplayHeight;

    /**
     * Keyboard mode, or zero, if none.
     */
    private int mKeyboardMode;

    // Variables for pre-computing nearest keys.
    private static final int GRID_WIDTH = 10;

    private static final int GRID_HEIGHT = 5;

    private static final int GRID_SIZE = android.inputmethodservice.Keyboard.GRID_WIDTH * android.inputmethodservice.Keyboard.GRID_HEIGHT;

    private int mCellWidth;

    private int mCellHeight;

    private int[][] mGridNeighbors;

    private int mProximityThreshold;

    /**
     * Number of key widths from current touch point to search for nearest keys.
     */
    private static float SEARCH_DISTANCE = 1.8F;

    private java.util.ArrayList<android.inputmethodservice.Keyboard.Row> rows = new java.util.ArrayList<android.inputmethodservice.Keyboard.Row>();

    /**
     * Container for keys in the keyboard. All keys in a row are at the same Y-coordinate.
     * Some of the key size defaults can be overridden per row from what the {@link Keyboard}
     * defines.
     *
     * @unknown ref android.R.styleable#Keyboard_keyWidth
     * @unknown ref android.R.styleable#Keyboard_keyHeight
     * @unknown ref android.R.styleable#Keyboard_horizontalGap
     * @unknown ref android.R.styleable#Keyboard_verticalGap
     * @unknown ref android.R.styleable#Keyboard_Row_rowEdgeFlags
     * @unknown ref android.R.styleable#Keyboard_Row_keyboardMode
     */
    public static class Row {
        /**
         * Default width of a key in this row.
         */
        public int defaultWidth;

        /**
         * Default height of a key in this row.
         */
        public int defaultHeight;

        /**
         * Default horizontal gap between keys in this row.
         */
        public int defaultHorizontalGap;

        /**
         * Vertical gap following this row.
         */
        public int verticalGap;

        java.util.ArrayList<android.inputmethodservice.Keyboard.Key> mKeys = new java.util.ArrayList<android.inputmethodservice.Keyboard.Key>();

        /**
         * Edge flags for this row of keys. Possible values that can be assigned are
         * {@link Keyboard#EDGE_TOP EDGE_TOP} and {@link Keyboard#EDGE_BOTTOM EDGE_BOTTOM}
         */
        public int rowEdgeFlags;

        /**
         * The keyboard mode for this row
         */
        public int mode;

        private android.inputmethodservice.Keyboard parent;

        public Row(android.inputmethodservice.Keyboard parent) {
            this.parent = parent;
        }

        public Row(android.content.res.Resources res, android.inputmethodservice.Keyboard parent, android.content.res.XmlResourceParser parser) {
            this.parent = parent;
            android.content.res.TypedArray a = res.obtainAttributes(android.util.Xml.asAttributeSet(parser), com.android.internal.R.styleable.Keyboard);
            defaultWidth = android.inputmethodservice.Keyboard.getDimensionOrFraction(a, com.android.internal.R.styleable.Keyboard_keyWidth, parent.mDisplayWidth, parent.mDefaultWidth);
            defaultHeight = android.inputmethodservice.Keyboard.getDimensionOrFraction(a, com.android.internal.R.styleable.Keyboard_keyHeight, parent.mDisplayHeight, parent.mDefaultHeight);
            defaultHorizontalGap = android.inputmethodservice.Keyboard.getDimensionOrFraction(a, com.android.internal.R.styleable.Keyboard_horizontalGap, parent.mDisplayWidth, parent.mDefaultHorizontalGap);
            verticalGap = android.inputmethodservice.Keyboard.getDimensionOrFraction(a, com.android.internal.R.styleable.Keyboard_verticalGap, parent.mDisplayHeight, parent.mDefaultVerticalGap);
            a.recycle();
            a = res.obtainAttributes(android.util.Xml.asAttributeSet(parser), com.android.internal.R.styleable.Keyboard_Row);
            rowEdgeFlags = a.getInt(com.android.internal.R.styleable.Keyboard_Row_rowEdgeFlags, 0);
            mode = a.getResourceId(com.android.internal.R.styleable.Keyboard_Row_keyboardMode, 0);
        }
    }

    /**
     * Class for describing the position and characteristics of a single key in the keyboard.
     *
     * @unknown ref android.R.styleable#Keyboard_keyWidth
     * @unknown ref android.R.styleable#Keyboard_keyHeight
     * @unknown ref android.R.styleable#Keyboard_horizontalGap
     * @unknown ref android.R.styleable#Keyboard_Key_codes
     * @unknown ref android.R.styleable#Keyboard_Key_keyIcon
     * @unknown ref android.R.styleable#Keyboard_Key_keyLabel
     * @unknown ref android.R.styleable#Keyboard_Key_iconPreview
     * @unknown ref android.R.styleable#Keyboard_Key_isSticky
     * @unknown ref android.R.styleable#Keyboard_Key_isRepeatable
     * @unknown ref android.R.styleable#Keyboard_Key_isModifier
     * @unknown ref android.R.styleable#Keyboard_Key_popupKeyboard
     * @unknown ref android.R.styleable#Keyboard_Key_popupCharacters
     * @unknown ref android.R.styleable#Keyboard_Key_keyOutputText
     * @unknown ref android.R.styleable#Keyboard_Key_keyEdgeFlags
     */
    public static class Key {
        /**
         * All the key codes (unicode or custom code) that this key could generate, zero'th
         * being the most important.
         */
        public int[] codes;

        /**
         * Label to display
         */
        public java.lang.CharSequence label;

        /**
         * Icon to display instead of a label. Icon takes precedence over a label
         */
        public android.graphics.drawable.Drawable icon;

        /**
         * Preview version of the icon, for the preview popup
         */
        public android.graphics.drawable.Drawable iconPreview;

        /**
         * Width of the key, not including the gap
         */
        public int width;

        /**
         * Height of the key, not including the gap
         */
        public int height;

        /**
         * The horizontal gap before this key
         */
        public int gap;

        /**
         * Whether this key is sticky, i.e., a toggle key
         */
        public boolean sticky;

        /**
         * X coordinate of the key in the keyboard layout
         */
        public int x;

        /**
         * Y coordinate of the key in the keyboard layout
         */
        public int y;

        /**
         * The current pressed state of this key
         */
        public boolean pressed;

        /**
         * If this is a sticky key, is it on?
         */
        public boolean on;

        /**
         * Text to output when pressed. This can be multiple characters, like ".com"
         */
        public java.lang.CharSequence text;

        /**
         * Popup characters
         */
        public java.lang.CharSequence popupCharacters;

        /**
         * Flags that specify the anchoring to edges of the keyboard for detecting touch events
         * that are just out of the boundary of the key. This is a bit mask of
         * {@link Keyboard#EDGE_LEFT}, {@link Keyboard#EDGE_RIGHT}, {@link Keyboard#EDGE_TOP} and
         * {@link Keyboard#EDGE_BOTTOM}.
         */
        public int edgeFlags;

        /**
         * Whether this is a modifier key, such as Shift or Alt
         */
        public boolean modifier;

        /**
         * The keyboard that this key belongs to
         */
        private android.inputmethodservice.Keyboard keyboard;

        /**
         * If this key pops up a mini keyboard, this is the resource id for the XML layout for that
         * keyboard.
         */
        public int popupResId;

        /**
         * Whether this key repeats itself when held down
         */
        public boolean repeatable;

        private static final int[] KEY_STATE_NORMAL_ON = new int[]{ android.R.attr.state_checkable, android.R.attr.state_checked };

        private static final int[] KEY_STATE_PRESSED_ON = new int[]{ android.R.attr.state_pressed, android.R.attr.state_checkable, android.R.attr.state_checked };

        private static final int[] KEY_STATE_NORMAL_OFF = new int[]{ android.R.attr.state_checkable };

        private static final int[] KEY_STATE_PRESSED_OFF = new int[]{ android.R.attr.state_pressed, android.R.attr.state_checkable };

        private static final int[] KEY_STATE_NORMAL = new int[]{  };

        private static final int[] KEY_STATE_PRESSED = new int[]{ android.R.attr.state_pressed };

        /**
         * Create an empty key with no attributes.
         */
        public Key(android.inputmethodservice.Keyboard.Row parent) {
            keyboard = parent.parent;
            height = parent.defaultHeight;
            width = parent.defaultWidth;
            gap = parent.defaultHorizontalGap;
            edgeFlags = parent.rowEdgeFlags;
        }

        /**
         * Create a key with the given top-left coordinate and extract its attributes from
         * the XML parser.
         *
         * @param res
         * 		resources associated with the caller's context
         * @param parent
         * 		the row that this key belongs to. The row must already be attached to
         * 		a {@link Keyboard}.
         * @param x
         * 		the x coordinate of the top-left
         * @param y
         * 		the y coordinate of the top-left
         * @param parser
         * 		the XML parser containing the attributes for this key
         */
        public Key(android.content.res.Resources res, android.inputmethodservice.Keyboard.Row parent, int x, int y, android.content.res.XmlResourceParser parser) {
            this(parent);
            this.x = x;
            this.y = y;
            android.content.res.TypedArray a = res.obtainAttributes(android.util.Xml.asAttributeSet(parser), com.android.internal.R.styleable.Keyboard);
            width = android.inputmethodservice.Keyboard.getDimensionOrFraction(a, com.android.internal.R.styleable.Keyboard_keyWidth, keyboard.mDisplayWidth, parent.defaultWidth);
            height = android.inputmethodservice.Keyboard.getDimensionOrFraction(a, com.android.internal.R.styleable.Keyboard_keyHeight, keyboard.mDisplayHeight, parent.defaultHeight);
            gap = android.inputmethodservice.Keyboard.getDimensionOrFraction(a, com.android.internal.R.styleable.Keyboard_horizontalGap, keyboard.mDisplayWidth, parent.defaultHorizontalGap);
            a.recycle();
            a = res.obtainAttributes(android.util.Xml.asAttributeSet(parser), com.android.internal.R.styleable.Keyboard_Key);
            this.x += gap;
            android.util.TypedValue codesValue = new android.util.TypedValue();
            a.getValue(com.android.internal.R.styleable.Keyboard_Key_codes, codesValue);
            if ((codesValue.type == android.util.TypedValue.TYPE_INT_DEC) || (codesValue.type == android.util.TypedValue.TYPE_INT_HEX)) {
                codes = new int[]{ codesValue.data };
            } else
                if (codesValue.type == android.util.TypedValue.TYPE_STRING) {
                    codes = parseCSV(codesValue.string.toString());
                }

            iconPreview = a.getDrawable(com.android.internal.R.styleable.Keyboard_Key_iconPreview);
            if (iconPreview != null) {
                iconPreview.setBounds(0, 0, iconPreview.getIntrinsicWidth(), iconPreview.getIntrinsicHeight());
            }
            popupCharacters = a.getText(com.android.internal.R.styleable.Keyboard_Key_popupCharacters);
            popupResId = a.getResourceId(com.android.internal.R.styleable.Keyboard_Key_popupKeyboard, 0);
            repeatable = a.getBoolean(com.android.internal.R.styleable.Keyboard_Key_isRepeatable, false);
            modifier = a.getBoolean(com.android.internal.R.styleable.Keyboard_Key_isModifier, false);
            sticky = a.getBoolean(com.android.internal.R.styleable.Keyboard_Key_isSticky, false);
            edgeFlags = a.getInt(com.android.internal.R.styleable.Keyboard_Key_keyEdgeFlags, 0);
            edgeFlags |= parent.rowEdgeFlags;
            icon = a.getDrawable(com.android.internal.R.styleable.Keyboard_Key_keyIcon);
            if (icon != null) {
                icon.setBounds(0, 0, icon.getIntrinsicWidth(), icon.getIntrinsicHeight());
            }
            label = a.getText(com.android.internal.R.styleable.Keyboard_Key_keyLabel);
            text = a.getText(com.android.internal.R.styleable.Keyboard_Key_keyOutputText);
            if ((codes == null) && (!android.text.TextUtils.isEmpty(label))) {
                codes = new int[]{ label.charAt(0) };
            }
            a.recycle();
        }

        /**
         * Informs the key that it has been pressed, in case it needs to change its appearance or
         * state.
         *
         * @see #onReleased(boolean)
         */
        public void onPressed() {
            pressed = !pressed;
        }

        /**
         * Changes the pressed state of the key.
         *
         * <p>Toggled state of the key will be flipped when all the following conditions are
         * fulfilled:</p>
         *
         * <ul>
         *     <li>This is a sticky key, that is, {@link #sticky} is {@code true}.
         *     <li>The parameter {@code inside} is {@code true}.
         *     <li>{@link android.os.Build.VERSION#SDK_INT} is greater than
         *         {@link android.os.Build.VERSION_CODES#LOLLIPOP_MR1}.
         * </ul>
         *
         * @param inside
         * 		whether the finger was released inside the key. Works only on Android M and
         * 		later. See the method document for details.
         * @see #onPressed()
         */
        public void onReleased(boolean inside) {
            pressed = !pressed;
            if (sticky && inside) {
                on = !on;
            }
        }

        int[] parseCSV(java.lang.String value) {
            int count = 0;
            int lastIndex = 0;
            if (value.length() > 0) {
                count++;
                while ((lastIndex = value.indexOf(",", lastIndex + 1)) > 0) {
                    count++;
                } 
            }
            int[] values = new int[count];
            count = 0;
            java.util.StringTokenizer st = new java.util.StringTokenizer(value, ",");
            while (st.hasMoreTokens()) {
                try {
                    values[count++] = java.lang.Integer.parseInt(st.nextToken());
                } catch (java.lang.NumberFormatException nfe) {
                    android.util.Log.e(android.inputmethodservice.Keyboard.TAG, "Error parsing keycodes " + value);
                }
            } 
            return values;
        }

        /**
         * Detects if a point falls inside this key.
         *
         * @param x
         * 		the x-coordinate of the point
         * @param y
         * 		the y-coordinate of the point
         * @return whether or not the point falls inside the key. If the key is attached to an edge,
        it will assume that all points between the key and the edge are considered to be inside
        the key.
         */
        public boolean isInside(int x, int y) {
            boolean leftEdge = (edgeFlags & android.inputmethodservice.Keyboard.EDGE_LEFT) > 0;
            boolean rightEdge = (edgeFlags & android.inputmethodservice.Keyboard.EDGE_RIGHT) > 0;
            boolean topEdge = (edgeFlags & android.inputmethodservice.Keyboard.EDGE_TOP) > 0;
            boolean bottomEdge = (edgeFlags & android.inputmethodservice.Keyboard.EDGE_BOTTOM) > 0;
            if (((((x >= this.x) || (leftEdge && (x <= (this.x + this.width)))) && ((x < (this.x + this.width)) || (rightEdge && (x >= this.x)))) && ((y >= this.y) || (topEdge && (y <= (this.y + this.height))))) && ((y < (this.y + this.height)) || (bottomEdge && (y >= this.y)))) {
                return true;
            } else {
                return false;
            }
        }

        /**
         * Returns the square of the distance between the center of the key and the given point.
         *
         * @param x
         * 		the x-coordinate of the point
         * @param y
         * 		the y-coordinate of the point
         * @return the square of the distance of the point from the center of the key
         */
        public int squaredDistanceFrom(int x, int y) {
            int xDist = (this.x + (width / 2)) - x;
            int yDist = (this.y + (height / 2)) - y;
            return (xDist * xDist) + (yDist * yDist);
        }

        /**
         * Returns the drawable state for the key, based on the current state and type of the key.
         *
         * @return the drawable state of the key.
         * @see android.graphics.drawable.StateListDrawable#setState(int[])
         */
        public int[] getCurrentDrawableState() {
            int[] states = android.inputmethodservice.Keyboard.Key.KEY_STATE_NORMAL;
            if (on) {
                if (pressed) {
                    states = android.inputmethodservice.Keyboard.Key.KEY_STATE_PRESSED_ON;
                } else {
                    states = android.inputmethodservice.Keyboard.Key.KEY_STATE_NORMAL_ON;
                }
            } else {
                if (sticky) {
                    if (pressed) {
                        states = android.inputmethodservice.Keyboard.Key.KEY_STATE_PRESSED_OFF;
                    } else {
                        states = android.inputmethodservice.Keyboard.Key.KEY_STATE_NORMAL_OFF;
                    }
                } else {
                    if (pressed) {
                        states = android.inputmethodservice.Keyboard.Key.KEY_STATE_PRESSED;
                    }
                }
            }
            return states;
        }
    }

    /**
     * Creates a keyboard from the given xml key layout file.
     *
     * @param context
     * 		the application or service context
     * @param xmlLayoutResId
     * 		the resource file that contains the keyboard layout and keys.
     */
    public Keyboard(android.content.Context context, int xmlLayoutResId) {
        this(context, xmlLayoutResId, 0);
    }

    /**
     * Creates a keyboard from the given xml key layout file. Weeds out rows
     * that have a keyboard mode defined but don't match the specified mode.
     *
     * @param context
     * 		the application or service context
     * @param xmlLayoutResId
     * 		the resource file that contains the keyboard layout and keys.
     * @param modeId
     * 		keyboard mode identifier
     * @param width
     * 		sets width of keyboard
     * @param height
     * 		sets height of keyboard
     */
    public Keyboard(android.content.Context context, @android.annotation.XmlRes
    int xmlLayoutResId, int modeId, int width, int height) {
        mDisplayWidth = width;
        mDisplayHeight = height;
        mDefaultHorizontalGap = 0;
        mDefaultWidth = mDisplayWidth / 10;
        mDefaultVerticalGap = 0;
        mDefaultHeight = mDefaultWidth;
        mKeys = new java.util.ArrayList<android.inputmethodservice.Keyboard.Key>();
        mModifierKeys = new java.util.ArrayList<android.inputmethodservice.Keyboard.Key>();
        mKeyboardMode = modeId;
        loadKeyboard(context, context.getResources().getXml(xmlLayoutResId));
    }

    /**
     * Creates a keyboard from the given xml key layout file. Weeds out rows
     * that have a keyboard mode defined but don't match the specified mode.
     *
     * @param context
     * 		the application or service context
     * @param xmlLayoutResId
     * 		the resource file that contains the keyboard layout and keys.
     * @param modeId
     * 		keyboard mode identifier
     */
    public Keyboard(android.content.Context context, @android.annotation.XmlRes
    int xmlLayoutResId, int modeId) {
        android.util.DisplayMetrics dm = context.getResources().getDisplayMetrics();
        mDisplayWidth = dm.widthPixels;
        mDisplayHeight = dm.heightPixels;
        // Log.v(TAG, "keyboard's display metrics:" + dm);
        mDefaultHorizontalGap = 0;
        mDefaultWidth = mDisplayWidth / 10;
        mDefaultVerticalGap = 0;
        mDefaultHeight = mDefaultWidth;
        mKeys = new java.util.ArrayList<android.inputmethodservice.Keyboard.Key>();
        mModifierKeys = new java.util.ArrayList<android.inputmethodservice.Keyboard.Key>();
        mKeyboardMode = modeId;
        loadKeyboard(context, context.getResources().getXml(xmlLayoutResId));
    }

    /**
     * <p>Creates a blank keyboard from the given resource file and populates it with the specified
     * characters in left-to-right, top-to-bottom fashion, using the specified number of columns.
     * </p>
     * <p>If the specified number of columns is -1, then the keyboard will fit as many keys as
     * possible in each row.</p>
     *
     * @param context
     * 		the application or service context
     * @param layoutTemplateResId
     * 		the layout template file, containing no keys.
     * @param characters
     * 		the list of characters to display on the keyboard. One key will be created
     * 		for each character.
     * @param columns
     * 		the number of columns of keys to display. If this number is greater than the
     * 		number of keys that can fit in a row, it will be ignored. If this number is -1, the
     * 		keyboard will fit as many keys as possible in each row.
     */
    public Keyboard(android.content.Context context, int layoutTemplateResId, java.lang.CharSequence characters, int columns, int horizontalPadding) {
        this(context, layoutTemplateResId);
        int x = 0;
        int y = 0;
        int column = 0;
        mTotalWidth = 0;
        android.inputmethodservice.Keyboard.Row row = new android.inputmethodservice.Keyboard.Row(this);
        row.defaultHeight = mDefaultHeight;
        row.defaultWidth = mDefaultWidth;
        row.defaultHorizontalGap = mDefaultHorizontalGap;
        row.verticalGap = mDefaultVerticalGap;
        row.rowEdgeFlags = android.inputmethodservice.Keyboard.EDGE_TOP | android.inputmethodservice.Keyboard.EDGE_BOTTOM;
        final int maxColumns = (columns == (-1)) ? java.lang.Integer.MAX_VALUE : columns;
        for (int i = 0; i < characters.length(); i++) {
            char c = characters.charAt(i);
            if ((column >= maxColumns) || (((x + mDefaultWidth) + horizontalPadding) > mDisplayWidth)) {
                x = 0;
                y += mDefaultVerticalGap + mDefaultHeight;
                column = 0;
            }
            final android.inputmethodservice.Keyboard.Key key = new android.inputmethodservice.Keyboard.Key(row);
            key.x = x;
            key.y = y;
            key.label = java.lang.String.valueOf(c);
            key.codes = new int[]{ c };
            column++;
            x += key.width + key.gap;
            mKeys.add(key);
            row.mKeys.add(key);
            if (x > mTotalWidth) {
                mTotalWidth = x;
            }
        }
        mTotalHeight = y + mDefaultHeight;
        rows.add(row);
    }

    final void resize(int newWidth, int newHeight) {
        int numRows = rows.size();
        for (int rowIndex = 0; rowIndex < numRows; ++rowIndex) {
            android.inputmethodservice.Keyboard.Row row = rows.get(rowIndex);
            int numKeys = row.mKeys.size();
            int totalGap = 0;
            int totalWidth = 0;
            for (int keyIndex = 0; keyIndex < numKeys; ++keyIndex) {
                android.inputmethodservice.Keyboard.Key key = row.mKeys.get(keyIndex);
                if (keyIndex > 0) {
                    totalGap += key.gap;
                }
                totalWidth += key.width;
            }
            if ((totalGap + totalWidth) > newWidth) {
                int x = 0;
                float scaleFactor = ((float) (newWidth - totalGap)) / totalWidth;
                for (int keyIndex = 0; keyIndex < numKeys; ++keyIndex) {
                    android.inputmethodservice.Keyboard.Key key = row.mKeys.get(keyIndex);
                    key.width *= scaleFactor;
                    key.x = x;
                    x += key.width + key.gap;
                }
            }
        }
        mTotalWidth = newWidth;
        // TODO: This does not adjust the vertical placement according to the new size.
        // The main problem in the previous code was horizontal placement/size, but we should
        // also recalculate the vertical sizes/positions when we get this resize call.
    }

    public java.util.List<android.inputmethodservice.Keyboard.Key> getKeys() {
        return mKeys;
    }

    public java.util.List<android.inputmethodservice.Keyboard.Key> getModifierKeys() {
        return mModifierKeys;
    }

    protected int getHorizontalGap() {
        return mDefaultHorizontalGap;
    }

    protected void setHorizontalGap(int gap) {
        mDefaultHorizontalGap = gap;
    }

    protected int getVerticalGap() {
        return mDefaultVerticalGap;
    }

    protected void setVerticalGap(int gap) {
        mDefaultVerticalGap = gap;
    }

    protected int getKeyHeight() {
        return mDefaultHeight;
    }

    protected void setKeyHeight(int height) {
        mDefaultHeight = height;
    }

    protected int getKeyWidth() {
        return mDefaultWidth;
    }

    protected void setKeyWidth(int width) {
        mDefaultWidth = width;
    }

    /**
     * Returns the total height of the keyboard
     *
     * @return the total height of the keyboard
     */
    public int getHeight() {
        return mTotalHeight;
    }

    public int getMinWidth() {
        return mTotalWidth;
    }

    public boolean setShifted(boolean shiftState) {
        for (android.inputmethodservice.Keyboard.Key shiftKey : mShiftKeys) {
            if (shiftKey != null) {
                shiftKey.on = shiftState;
            }
        }
        if (mShifted != shiftState) {
            mShifted = shiftState;
            return true;
        }
        return false;
    }

    public boolean isShifted() {
        return mShifted;
    }

    /**
     *
     *
     * @unknown 
     */
    public int[] getShiftKeyIndices() {
        return mShiftKeyIndices;
    }

    public int getShiftKeyIndex() {
        return mShiftKeyIndices[0];
    }

    private void computeNearestNeighbors() {
        // Round-up so we don't have any pixels outside the grid
        mCellWidth = ((getMinWidth() + android.inputmethodservice.Keyboard.GRID_WIDTH) - 1) / android.inputmethodservice.Keyboard.GRID_WIDTH;
        mCellHeight = ((getHeight() + android.inputmethodservice.Keyboard.GRID_HEIGHT) - 1) / android.inputmethodservice.Keyboard.GRID_HEIGHT;
        mGridNeighbors = new int[android.inputmethodservice.Keyboard.GRID_SIZE][];
        int[] indices = new int[mKeys.size()];
        final int gridWidth = android.inputmethodservice.Keyboard.GRID_WIDTH * mCellWidth;
        final int gridHeight = android.inputmethodservice.Keyboard.GRID_HEIGHT * mCellHeight;
        for (int x = 0; x < gridWidth; x += mCellWidth) {
            for (int y = 0; y < gridHeight; y += mCellHeight) {
                int count = 0;
                for (int i = 0; i < mKeys.size(); i++) {
                    final android.inputmethodservice.Keyboard.Key key = mKeys.get(i);
                    if ((((key.squaredDistanceFrom(x, y) < mProximityThreshold) || (key.squaredDistanceFrom((x + mCellWidth) - 1, y) < mProximityThreshold)) || (key.squaredDistanceFrom((x + mCellWidth) - 1, (y + mCellHeight) - 1) < mProximityThreshold)) || (key.squaredDistanceFrom(x, (y + mCellHeight) - 1) < mProximityThreshold)) {
                        indices[count++] = i;
                    }
                }
                int[] cell = new int[count];
                java.lang.System.arraycopy(indices, 0, cell, 0, count);
                mGridNeighbors[((y / mCellHeight) * android.inputmethodservice.Keyboard.GRID_WIDTH) + (x / mCellWidth)] = cell;
            }
        }
    }

    /**
     * Returns the indices of the keys that are closest to the given point.
     *
     * @param x
     * 		the x-coordinate of the point
     * @param y
     * 		the y-coordinate of the point
     * @return the array of integer indices for the nearest keys to the given point. If the given
    point is out of range, then an array of size zero is returned.
     */
    public int[] getNearestKeys(int x, int y) {
        if (mGridNeighbors == null)
            computeNearestNeighbors();

        if ((((x >= 0) && (x < getMinWidth())) && (y >= 0)) && (y < getHeight())) {
            int index = ((y / mCellHeight) * android.inputmethodservice.Keyboard.GRID_WIDTH) + (x / mCellWidth);
            if (index < android.inputmethodservice.Keyboard.GRID_SIZE) {
                return mGridNeighbors[index];
            }
        }
        return new int[0];
    }

    protected android.inputmethodservice.Keyboard.Row createRowFromXml(android.content.res.Resources res, android.content.res.XmlResourceParser parser) {
        return new android.inputmethodservice.Keyboard.Row(res, this, parser);
    }

    protected android.inputmethodservice.Keyboard.Key createKeyFromXml(android.content.res.Resources res, android.inputmethodservice.Keyboard.Row parent, int x, int y, android.content.res.XmlResourceParser parser) {
        return new android.inputmethodservice.Keyboard.Key(res, parent, x, y, parser);
    }

    private void loadKeyboard(android.content.Context context, android.content.res.XmlResourceParser parser) {
        boolean inKey = false;
        boolean inRow = false;
        boolean leftMostKey = false;
        int row = 0;
        int x = 0;
        int y = 0;
        android.inputmethodservice.Keyboard.Key key = null;
        android.inputmethodservice.Keyboard.Row currentRow = null;
        android.content.res.Resources res = context.getResources();
        boolean skipRow = false;
        try {
            int event;
            while ((event = parser.next()) != android.content.res.XmlResourceParser.END_DOCUMENT) {
                if (event == android.content.res.XmlResourceParser.START_TAG) {
                    java.lang.String tag = parser.getName();
                    if (android.inputmethodservice.Keyboard.TAG_ROW.equals(tag)) {
                        inRow = true;
                        x = 0;
                        currentRow = createRowFromXml(res, parser);
                        rows.add(currentRow);
                        skipRow = (currentRow.mode != 0) && (currentRow.mode != mKeyboardMode);
                        if (skipRow) {
                            skipToEndOfRow(parser);
                            inRow = false;
                        }
                    } else
                        if (android.inputmethodservice.Keyboard.TAG_KEY.equals(tag)) {
                            inKey = true;
                            key = createKeyFromXml(res, currentRow, x, y, parser);
                            mKeys.add(key);
                            if (key.codes[0] == android.inputmethodservice.Keyboard.KEYCODE_SHIFT) {
                                // Find available shift key slot and put this shift key in it
                                for (int i = 0; i < mShiftKeys.length; i++) {
                                    if (mShiftKeys[i] == null) {
                                        mShiftKeys[i] = key;
                                        mShiftKeyIndices[i] = mKeys.size() - 1;
                                        break;
                                    }
                                }
                                mModifierKeys.add(key);
                            } else
                                if (key.codes[0] == android.inputmethodservice.Keyboard.KEYCODE_ALT) {
                                    mModifierKeys.add(key);
                                }

                            currentRow.mKeys.add(key);
                        } else
                            if (android.inputmethodservice.Keyboard.TAG_KEYBOARD.equals(tag)) {
                                parseKeyboardAttributes(res, parser);
                            }


                } else
                    if (event == android.content.res.XmlResourceParser.END_TAG) {
                        if (inKey) {
                            inKey = false;
                            x += key.gap + key.width;
                            if (x > mTotalWidth) {
                                mTotalWidth = x;
                            }
                        } else
                            if (inRow) {
                                inRow = false;
                                y += currentRow.verticalGap;
                                y += currentRow.defaultHeight;
                                row++;
                            } else {
                                // TODO: error or extend?
                            }

                    }

            } 
        } catch (java.lang.Exception e) {
            android.util.Log.e(android.inputmethodservice.Keyboard.TAG, "Parse error:" + e);
            e.printStackTrace();
        }
        mTotalHeight = y - mDefaultVerticalGap;
    }

    private void skipToEndOfRow(android.content.res.XmlResourceParser parser) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        int event;
        while ((event = parser.next()) != android.content.res.XmlResourceParser.END_DOCUMENT) {
            if ((event == android.content.res.XmlResourceParser.END_TAG) && parser.getName().equals(android.inputmethodservice.Keyboard.TAG_ROW)) {
                break;
            }
        } 
    }

    private void parseKeyboardAttributes(android.content.res.Resources res, android.content.res.XmlResourceParser parser) {
        android.content.res.TypedArray a = res.obtainAttributes(android.util.Xml.asAttributeSet(parser), com.android.internal.R.styleable.Keyboard);
        mDefaultWidth = android.inputmethodservice.Keyboard.getDimensionOrFraction(a, com.android.internal.R.styleable.Keyboard_keyWidth, mDisplayWidth, mDisplayWidth / 10);
        mDefaultHeight = android.inputmethodservice.Keyboard.getDimensionOrFraction(a, com.android.internal.R.styleable.Keyboard_keyHeight, mDisplayHeight, 50);
        mDefaultHorizontalGap = android.inputmethodservice.Keyboard.getDimensionOrFraction(a, com.android.internal.R.styleable.Keyboard_horizontalGap, mDisplayWidth, 0);
        mDefaultVerticalGap = android.inputmethodservice.Keyboard.getDimensionOrFraction(a, com.android.internal.R.styleable.Keyboard_verticalGap, mDisplayHeight, 0);
        mProximityThreshold = ((int) (mDefaultWidth * android.inputmethodservice.Keyboard.SEARCH_DISTANCE));
        mProximityThreshold = mProximityThreshold * mProximityThreshold;// Square it for comparison

        a.recycle();
    }

    static int getDimensionOrFraction(android.content.res.TypedArray a, int index, int base, int defValue) {
        android.util.TypedValue value = a.peekValue(index);
        if (value == null)
            return defValue;

        if (value.type == android.util.TypedValue.TYPE_DIMENSION) {
            return a.getDimensionPixelOffset(index, defValue);
        } else
            if (value.type == android.util.TypedValue.TYPE_FRACTION) {
                // Round it to avoid values like 47.9999 from getting truncated
                return java.lang.Math.round(a.getFraction(index, base, base, defValue));
            }

        return defValue;
    }
}

