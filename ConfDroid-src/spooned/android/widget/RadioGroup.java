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
package android.widget;


/**
 * <p>This class is used to create a multiple-exclusion scope for a set of radio
 * buttons. Checking one radio button that belongs to a radio group unchecks
 * any previously checked radio button within the same group.</p>
 *
 * <p>Intially, all of the radio buttons are unchecked. While it is not possible
 * to uncheck a particular radio button, the radio group can be cleared to
 * remove the checked state.</p>
 *
 * <p>The selection is identified by the unique id of the radio button as defined
 * in the XML layout file.</p>
 *
 * <p><strong>XML Attributes</strong></p>
 * <p>See {@link android.R.styleable#RadioGroup RadioGroup Attributes},
 * {@link android.R.styleable#LinearLayout LinearLayout Attributes},
 * {@link android.R.styleable#ViewGroup ViewGroup Attributes},
 * {@link android.R.styleable#View View Attributes}</p>
 * <p>Also see
 * {@link android.widget.LinearLayout.LayoutParams LinearLayout.LayoutParams}
 * for layout attributes.</p>
 *
 * @see RadioButton
 */
public class RadioGroup extends android.widget.LinearLayout {
    private static final java.lang.String LOG_TAG = android.widget.RadioGroup.class.getSimpleName();

    // holds the checked id; the selection is empty by default
    private int mCheckedId = -1;

    // tracks children radio buttons checked state
    @android.annotation.UnsupportedAppUsage
    private android.widget.CompoundButton.OnCheckedChangeListener mChildOnCheckedChangeListener;

    // when true, mOnCheckedChangeListener discards events
    private boolean mProtectFromCheckedChange = false;

    @android.annotation.UnsupportedAppUsage
    private android.widget.RadioGroup.OnCheckedChangeListener mOnCheckedChangeListener;

    private android.widget.RadioGroup.PassThroughHierarchyChangeListener mPassThroughListener;

    // Indicates whether the child was set from resources or dynamically, so it can be used
    // to sanitize autofill requests.
    private int mInitialCheckedId = android.view.View.NO_ID;

    /**
     * {@inheritDoc }
     */
    public RadioGroup(android.content.Context context) {
        super(context);
        setOrientation(android.widget.LinearLayout.VERTICAL);
        init();
    }

    /**
     * {@inheritDoc }
     */
    public RadioGroup(android.content.Context context, android.util.AttributeSet attrs) {
        super(context, attrs);
        // RadioGroup is important by default, unless app developer overrode attribute.
        if (getImportantForAutofill() == android.view.View.IMPORTANT_FOR_AUTOFILL_AUTO) {
            setImportantForAutofill(android.view.View.IMPORTANT_FOR_AUTOFILL_YES);
        }
        // retrieve selected radio button as requested by the user in the
        // XML layout file
        android.content.res.TypedArray attributes = context.obtainStyledAttributes(attrs, com.android.internal.R.styleable.RadioGroup, com.android.internal.R.attr.radioButtonStyle, 0);
        saveAttributeDataForStyleable(context, com.android.internal.R.styleable.RadioGroup, attrs, attributes, com.android.internal.R.attr.radioButtonStyle, 0);
        int value = attributes.getResourceId(R.styleable.RadioGroup_checkedButton, android.view.View.NO_ID);
        if (value != android.view.View.NO_ID) {
            mCheckedId = value;
            mInitialCheckedId = value;
        }
        final int index = attributes.getInt(com.android.internal.R.styleable.RadioGroup_orientation, android.widget.LinearLayout.VERTICAL);
        setOrientation(index);
        attributes.recycle();
        init();
    }

    private void init() {
        mChildOnCheckedChangeListener = new android.widget.RadioGroup.CheckedStateTracker();
        mPassThroughListener = new android.widget.RadioGroup.PassThroughHierarchyChangeListener();
        super.setOnHierarchyChangeListener(mPassThroughListener);
    }

    /**
     * {@inheritDoc }
     */
    @java.lang.Override
    public void setOnHierarchyChangeListener(android.view.ViewGroup.OnHierarchyChangeListener listener) {
        // the user listener is delegated to our pass-through listener
        mPassThroughListener.mOnHierarchyChangeListener = listener;
    }

    /**
     * {@inheritDoc }
     */
    @java.lang.Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        // checks the appropriate radio button as requested in the XML file
        if (mCheckedId != (-1)) {
            mProtectFromCheckedChange = true;
            setCheckedStateForView(mCheckedId, true);
            mProtectFromCheckedChange = false;
            setCheckedId(mCheckedId);
        }
    }

    @java.lang.Override
    public void addView(android.view.View child, int index, android.view.ViewGroup.LayoutParams params) {
        if (child instanceof android.widget.RadioButton) {
            final android.widget.RadioButton button = ((android.widget.RadioButton) (child));
            if (button.isChecked()) {
                mProtectFromCheckedChange = true;
                if (mCheckedId != (-1)) {
                    setCheckedStateForView(mCheckedId, false);
                }
                mProtectFromCheckedChange = false;
                setCheckedId(button.getId());
            }
        }
        super.addView(child, index, params);
    }

    /**
     * <p>Sets the selection to the radio button whose identifier is passed in
     * parameter. Using -1 as the selection identifier clears the selection;
     * such an operation is equivalent to invoking {@link #clearCheck()}.</p>
     *
     * @param id
     * 		the unique id of the radio button to select in this group
     * @see #getCheckedRadioButtonId()
     * @see #clearCheck()
     */
    public void check(@android.annotation.IdRes
    int id) {
        // don't even bother
        if ((id != (-1)) && (id == mCheckedId)) {
            return;
        }
        if (mCheckedId != (-1)) {
            setCheckedStateForView(mCheckedId, false);
        }
        if (id != (-1)) {
            setCheckedStateForView(id, true);
        }
        setCheckedId(id);
    }

    private void setCheckedId(@android.annotation.IdRes
    int id) {
        boolean changed = id != mCheckedId;
        mCheckedId = id;
        if (mOnCheckedChangeListener != null) {
            mOnCheckedChangeListener.onCheckedChanged(this, mCheckedId);
        }
        if (changed) {
            final android.view.autofill.AutofillManager afm = mContext.getSystemService(android.view.autofill.AutofillManager.class);
            if (afm != null) {
                afm.notifyValueChanged(this);
            }
        }
    }

    private void setCheckedStateForView(int viewId, boolean checked) {
        android.view.View checkedView = findViewById(viewId);
        if ((checkedView != null) && (checkedView instanceof android.widget.RadioButton)) {
            ((android.widget.RadioButton) (checkedView)).setChecked(checked);
        }
    }

    /**
     * <p>Returns the identifier of the selected radio button in this group.
     * Upon empty selection, the returned value is -1.</p>
     *
     * @return the unique id of the selected radio button in this group
     * @see #check(int)
     * @see #clearCheck()
     * @unknown ref android.R.styleable#RadioGroup_checkedButton
     */
    @android.annotation.IdRes
    public int getCheckedRadioButtonId() {
        return mCheckedId;
    }

    /**
     * <p>Clears the selection. When the selection is cleared, no radio button
     * in this group is selected and {@link #getCheckedRadioButtonId()} returns
     * null.</p>
     *
     * @see #check(int)
     * @see #getCheckedRadioButtonId()
     */
    public void clearCheck() {
        check(-1);
    }

    /**
     * <p>Register a callback to be invoked when the checked radio button
     * changes in this group.</p>
     *
     * @param listener
     * 		the callback to call on checked state change
     */
    public void setOnCheckedChangeListener(android.widget.RadioGroup.OnCheckedChangeListener listener) {
        mOnCheckedChangeListener = listener;
    }

    /**
     * {@inheritDoc }
     */
    @java.lang.Override
    public android.widget.RadioGroup.LayoutParams generateLayoutParams(android.util.AttributeSet attrs) {
        return new android.widget.RadioGroup.LayoutParams(getContext(), attrs);
    }

    /**
     * {@inheritDoc }
     */
    @java.lang.Override
    protected boolean checkLayoutParams(android.view.ViewGroup.LayoutParams p) {
        return p instanceof android.widget.RadioGroup.LayoutParams;
    }

    @java.lang.Override
    protected android.widget.LinearLayout.LayoutParams generateDefaultLayoutParams() {
        return new android.widget.RadioGroup.LayoutParams(android.widget.RadioGroup.LayoutParams.WRAP_CONTENT, android.widget.RadioGroup.LayoutParams.WRAP_CONTENT);
    }

    @java.lang.Override
    public java.lang.CharSequence getAccessibilityClassName() {
        return android.widget.RadioGroup.class.getName();
    }

    /**
     * <p>This set of layout parameters defaults the width and the height of
     * the children to {@link #WRAP_CONTENT} when they are not specified in the
     * XML file. Otherwise, this class ussed the value read from the XML file.</p>
     *
     * <p>See
     * {@link android.R.styleable#LinearLayout_Layout LinearLayout Attributes}
     * for a list of all child view attributes that this class supports.</p>
     */
    public static class LayoutParams extends android.widget.LinearLayout.LayoutParams {
        /**
         * {@inheritDoc }
         */
        public LayoutParams(android.content.Context c, android.util.AttributeSet attrs) {
            super(c, attrs);
        }

        /**
         * {@inheritDoc }
         */
        public LayoutParams(int w, int h) {
            super(w, h);
        }

        /**
         * {@inheritDoc }
         */
        public LayoutParams(int w, int h, float initWeight) {
            super(w, h, initWeight);
        }

        /**
         * {@inheritDoc }
         */
        public LayoutParams(android.view.ViewGroup.LayoutParams p) {
            super(p);
        }

        /**
         * {@inheritDoc }
         */
        public LayoutParams(android.view.ViewGroup.MarginLayoutParams source) {
            super(source);
        }

        /**
         * <p>Fixes the child's width to
         * {@link android.view.ViewGroup.LayoutParams#WRAP_CONTENT} and the child's
         * height to  {@link android.view.ViewGroup.LayoutParams#WRAP_CONTENT}
         * when not specified in the XML file.</p>
         *
         * @param a
         * 		the styled attributes set
         * @param widthAttr
         * 		the width attribute to fetch
         * @param heightAttr
         * 		the height attribute to fetch
         */
        @java.lang.Override
        protected void setBaseAttributes(android.content.res.TypedArray a, int widthAttr, int heightAttr) {
            if (a.hasValue(widthAttr)) {
                width = a.getLayoutDimension(widthAttr, "layout_width");
            } else {
                width = android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
            }
            if (a.hasValue(heightAttr)) {
                height = a.getLayoutDimension(heightAttr, "layout_height");
            } else {
                height = android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
            }
        }
    }

    /**
     * <p>Interface definition for a callback to be invoked when the checked
     * radio button changed in this group.</p>
     */
    public interface OnCheckedChangeListener {
        /**
         * <p>Called when the checked radio button has changed. When the
         * selection is cleared, checkedId is -1.</p>
         *
         * @param group
         * 		the group in which the checked radio button has changed
         * @param checkedId
         * 		the unique identifier of the newly checked radio button
         */
        public void onCheckedChanged(android.widget.RadioGroup group, @android.annotation.IdRes
        int checkedId);
    }

    private class CheckedStateTracker implements android.widget.CompoundButton.OnCheckedChangeListener {
        @java.lang.Override
        public void onCheckedChanged(android.widget.CompoundButton buttonView, boolean isChecked) {
            // prevents from infinite recursion
            if (mProtectFromCheckedChange) {
                return;
            }
            mProtectFromCheckedChange = true;
            if (mCheckedId != (-1)) {
                setCheckedStateForView(mCheckedId, false);
            }
            mProtectFromCheckedChange = false;
            int id = buttonView.getId();
            setCheckedId(id);
        }
    }

    /**
     * <p>A pass-through listener acts upon the events and dispatches them
     * to another listener. This allows the table layout to set its own internal
     * hierarchy change listener without preventing the user to setup his.</p>
     */
    private class PassThroughHierarchyChangeListener implements android.view.ViewGroup.OnHierarchyChangeListener {
        private android.view.ViewGroup.OnHierarchyChangeListener mOnHierarchyChangeListener;

        /**
         * {@inheritDoc }
         */
        @java.lang.Override
        public void onChildViewAdded(android.view.View parent, android.view.View child) {
            if ((parent == android.widget.RadioGroup.this) && (child instanceof android.widget.RadioButton)) {
                int id = child.getId();
                // generates an id if it's missing
                if (id == android.view.View.NO_ID) {
                    id = android.view.View.generateViewId();
                    child.setId(id);
                }
                ((android.widget.RadioButton) (child)).setOnCheckedChangeWidgetListener(mChildOnCheckedChangeListener);
            }
            if (mOnHierarchyChangeListener != null) {
                mOnHierarchyChangeListener.onChildViewAdded(parent, child);
            }
        }

        /**
         * {@inheritDoc }
         */
        @java.lang.Override
        public void onChildViewRemoved(android.view.View parent, android.view.View child) {
            if ((parent == android.widget.RadioGroup.this) && (child instanceof android.widget.RadioButton)) {
                ((android.widget.RadioButton) (child)).setOnCheckedChangeWidgetListener(null);
            }
            if (mOnHierarchyChangeListener != null) {
                mOnHierarchyChangeListener.onChildViewRemoved(parent, child);
            }
        }
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
        super.onProvideStructure(structure, viewFor, flags);
        if (viewFor == android.view.View.VIEW_STRUCTURE_FOR_AUTOFILL) {
            structure.setDataIsSensitive(mCheckedId != mInitialCheckedId);
        }
    }

    @java.lang.Override
    public void autofill(android.view.autofill.AutofillValue value) {
        if (!isEnabled())
            return;

        if (!value.isList()) {
            android.util.Log.w(android.widget.RadioGroup.LOG_TAG, (value + " could not be autofilled into ") + this);
            return;
        }
        final int index = value.getListValue();
        final android.view.View child = getChildAt(index);
        if (child == null) {
            android.util.Log.w(android.view.View.VIEW_LOG_TAG, "RadioGroup.autoFill(): no child with index " + index);
            return;
        }
        check(child.getId());
    }

    @java.lang.Override
    @android.view.View.AutofillType
    public int getAutofillType() {
        return isEnabled() ? android.view.View.AUTOFILL_TYPE_LIST : android.view.View.AUTOFILL_TYPE_NONE;
    }

    @java.lang.Override
    public android.view.autofill.AutofillValue getAutofillValue() {
        if (!isEnabled())
            return null;

        final int count = getChildCount();
        for (int i = 0; i < count; i++) {
            final android.view.View child = getChildAt(i);
            if (child.getId() == mCheckedId) {
                return android.view.autofill.AutofillValue.forList(i);
            }
        }
        return null;
    }
}

