/**
 * Copyright (C) 2013 The Android Open Source Project
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
package android.content;


/**
 * Applications can expose restrictions for a restricted user on a
 * multiuser device. The administrator can configure these restrictions that will then be
 * applied to the restricted user. Each RestrictionsEntry is one configurable restriction.
 * <p/>
 * Any application that chooses to expose such restrictions does so by implementing a
 * receiver that handles the {@link Intent#ACTION_GET_RESTRICTION_ENTRIES} action.
 * The receiver then returns a result bundle that contains an entry called "restrictions", whose
 * value is an ArrayList<RestrictionsEntry>.
 */
public class RestrictionEntry implements android.os.Parcelable {
    /**
     * Hidden restriction type. Use this type for information that needs to be transferred
     * across but shouldn't be presented to the user in the UI. Stores a single String value.
     */
    public static final int TYPE_NULL = 0;

    /**
     * Restriction of type "bool". Use this for storing a boolean value, typically presented as
     * a checkbox in the UI.
     */
    public static final int TYPE_BOOLEAN = 1;

    /**
     * Restriction of type "choice". Use this for storing a string value, typically presented as
     * a single-select list. Call {@link #setChoiceEntries(String[])} and
     * {@link #setChoiceValues(String[])} to set the localized list entries to present to the user
     * and the corresponding values, respectively.
     */
    public static final int TYPE_CHOICE = 2;

    /**
     * Internal restriction type. Use this for storing a string value, typically presented as
     * a single-select list. Call {@link #setChoiceEntries(String[])} and
     * {@link #setChoiceValues(String[])} to set the localized list entries to present to the user
     * and the corresponding values, respectively.
     * The presentation could imply that values in lower array indices are included when a
     * particular value is chosen.
     *
     * @unknown 
     */
    public static final int TYPE_CHOICE_LEVEL = 3;

    /**
     * Restriction of type "multi-select". Use this for presenting a multi-select list where more
     * than one entry can be selected, such as for choosing specific titles to white-list.
     * Call {@link #setChoiceEntries(String[])} and
     * {@link #setChoiceValues(String[])} to set the localized list entries to present to the user
     * and the corresponding values, respectively.
     * Use {@link #getAllSelectedStrings()} and {@link #setAllSelectedStrings(String[])} to
     * manipulate the selections.
     */
    public static final int TYPE_MULTI_SELECT = 4;

    /**
     * Restriction of type "integer". Use this for storing an integer value. The range of values
     * is from {@link Integer#MIN_VALUE} to {@link Integer#MAX_VALUE}.
     */
    public static final int TYPE_INTEGER = 5;

    /**
     * Restriction of type "string". Use this for storing a string value.
     *
     * @see #setSelectedString
     * @see #getSelectedString
     */
    public static final int TYPE_STRING = 6;

    /**
     * Restriction of type "bundle". Use this for storing {@link android.os.Bundle bundles} of
     * restrictions
     */
    public static final int TYPE_BUNDLE = 7;

    /**
     * Restriction of type "bundle_array". Use this for storing arrays of
     * {@link android.os.Bundle bundles} of restrictions
     */
    public static final int TYPE_BUNDLE_ARRAY = 8;

    /**
     * The type of restriction.
     */
    private int mType;

    /**
     * The unique key that identifies the restriction.
     */
    private java.lang.String mKey;

    /**
     * The user-visible title of the restriction.
     */
    private java.lang.String mTitle;

    /**
     * The user-visible secondary description of the restriction.
     */
    private java.lang.String mDescription;

    /**
     * The user-visible set of choices used for single-select and multi-select lists.
     */
    private java.lang.String[] mChoiceEntries;

    /**
     * The values corresponding to the user-visible choices. The value(s) of this entry will
     * one or more of these, returned by {@link #getAllSelectedStrings()} and
     * {@link #getSelectedString()}.
     */
    private java.lang.String[] mChoiceValues;

    /* The chosen value, whose content depends on the type of the restriction. */
    private java.lang.String mCurrentValue;

    /* List of selected choices in the multi-select case. */
    private java.lang.String[] mCurrentValues;

    /**
     * List of nested restrictions. Used by {@link #TYPE_BUNDLE bundle} and
     * {@link #TYPE_BUNDLE_ARRAY bundle_array} restrictions.
     */
    private android.content.RestrictionEntry[] mRestrictions;

    /**
     * Constructor for specifying the type and key, with no initial value;
     *
     * @param type
     * 		the restriction type.
     * @param key
     * 		the unique key for this restriction
     */
    public RestrictionEntry(int type, java.lang.String key) {
        mType = type;
        mKey = key;
    }

    /**
     * Constructor for {@link #TYPE_CHOICE} type.
     *
     * @param key
     * 		the unique key for this restriction
     * @param selectedString
     * 		the current value
     */
    public RestrictionEntry(java.lang.String key, java.lang.String selectedString) {
        this.mKey = key;
        this.mType = android.content.RestrictionEntry.TYPE_CHOICE;
        this.mCurrentValue = selectedString;
    }

    /**
     * Constructor for {@link #TYPE_BOOLEAN} type.
     *
     * @param key
     * 		the unique key for this restriction
     * @param selectedState
     * 		whether this restriction is selected or not
     */
    public RestrictionEntry(java.lang.String key, boolean selectedState) {
        this.mKey = key;
        this.mType = android.content.RestrictionEntry.TYPE_BOOLEAN;
        setSelectedState(selectedState);
    }

    /**
     * Constructor for {@link #TYPE_MULTI_SELECT} type.
     *
     * @param key
     * 		the unique key for this restriction
     * @param selectedStrings
     * 		the list of values that are currently selected
     */
    public RestrictionEntry(java.lang.String key, java.lang.String[] selectedStrings) {
        this.mKey = key;
        this.mType = android.content.RestrictionEntry.TYPE_MULTI_SELECT;
        this.mCurrentValues = selectedStrings;
    }

    /**
     * Constructor for {@link #TYPE_INTEGER} type.
     *
     * @param key
     * 		the unique key for this restriction
     * @param selectedInt
     * 		the integer value of the restriction
     */
    public RestrictionEntry(java.lang.String key, int selectedInt) {
        mKey = key;
        mType = android.content.RestrictionEntry.TYPE_INTEGER;
        setIntValue(selectedInt);
    }

    /**
     * Constructor for {@link #TYPE_BUNDLE}/{@link #TYPE_BUNDLE_ARRAY} type.
     *
     * @param key
     * 		the unique key for this restriction
     * @param restrictionEntries
     * 		array of nested restriction entries. If the entry, being created
     * 		represents a {@link #TYPE_BUNDLE_ARRAY bundle-array}, {@code restrictionEntries} array may
     * 		only contain elements of type {@link #TYPE_BUNDLE bundle}.
     * @param isBundleArray
     * 		true if this restriction represents
     * 		{@link #TYPE_BUNDLE_ARRAY bundle-array} type, otherwise the type will be set to
     * 		{@link #TYPE_BUNDLE bundle}.
     */
    private RestrictionEntry(java.lang.String key, android.content.RestrictionEntry[] restrictionEntries, boolean isBundleArray) {
        mKey = key;
        if (isBundleArray) {
            mType = android.content.RestrictionEntry.TYPE_BUNDLE_ARRAY;
            if (restrictionEntries != null) {
                for (android.content.RestrictionEntry restriction : restrictionEntries) {
                    if (restriction.getType() != android.content.RestrictionEntry.TYPE_BUNDLE) {
                        throw new java.lang.IllegalArgumentException("bundle_array restriction can only have " + "nested restriction entries of type bundle");
                    }
                }
            }
        } else {
            mType = android.content.RestrictionEntry.TYPE_BUNDLE;
        }
        setRestrictions(restrictionEntries);
    }

    /**
     * Creates an entry of type {@link #TYPE_BUNDLE}.
     *
     * @param key
     * 		the unique key for this restriction
     * @param restrictionEntries
     * 		array of nested restriction entries.
     * @return the newly created restriction
     */
    public static android.content.RestrictionEntry createBundleEntry(java.lang.String key, android.content.RestrictionEntry[] restrictionEntries) {
        return new android.content.RestrictionEntry(key, restrictionEntries, false);
    }

    /**
     * Creates an entry of type {@link #TYPE_BUNDLE_ARRAY}.
     *
     * @param key
     * 		the unique key for this restriction
     * @param restrictionEntries
     * 		array of nested restriction entries. The array may only contain
     * 		elements of type {@link #TYPE_BUNDLE bundle}.
     * @return the newly created restriction
     */
    public static android.content.RestrictionEntry createBundleArrayEntry(java.lang.String key, android.content.RestrictionEntry[] restrictionEntries) {
        return new android.content.RestrictionEntry(key, restrictionEntries, true);
    }

    /**
     * Sets the type for this restriction.
     *
     * @param type
     * 		the type for this restriction.
     */
    public void setType(int type) {
        this.mType = type;
    }

    /**
     * Returns the type for this restriction.
     *
     * @return the type for this restriction
     */
    public int getType() {
        return mType;
    }

    /**
     * Returns the currently selected string value.
     *
     * @return the currently selected value, which can be null for types that aren't for holding
    single string values.
     */
    public java.lang.String getSelectedString() {
        return mCurrentValue;
    }

    /**
     * Returns the list of currently selected values.
     *
     * @return the list of current selections, if type is {@link #TYPE_MULTI_SELECT},
    null otherwise.
     */
    public java.lang.String[] getAllSelectedStrings() {
        return mCurrentValues;
    }

    /**
     * Returns the current selected state for an entry of type {@link #TYPE_BOOLEAN}.
     *
     * @return the current selected state of the entry.
     */
    public boolean getSelectedState() {
        return java.lang.Boolean.parseBoolean(mCurrentValue);
    }

    /**
     * Returns the value of the entry as an integer when the type is {@link #TYPE_INTEGER}.
     *
     * @return the integer value of the entry.
     */
    public int getIntValue() {
        return java.lang.Integer.parseInt(mCurrentValue);
    }

    /**
     * Sets the integer value of the entry when the type is {@link #TYPE_INTEGER}.
     *
     * @param value
     * 		the integer value to set.
     */
    public void setIntValue(int value) {
        mCurrentValue = java.lang.Integer.toString(value);
    }

    /**
     * Sets the string value to use as the selected value for this restriction. This value will
     * be persisted by the system for later use by the application.
     *
     * @param selectedString
     * 		the string value to select.
     */
    public void setSelectedString(java.lang.String selectedString) {
        mCurrentValue = selectedString;
    }

    /**
     * Sets the current selected state for an entry of type {@link #TYPE_BOOLEAN}. This value will
     * be persisted by the system for later use by the application.
     *
     * @param state
     * 		the current selected state
     */
    public void setSelectedState(boolean state) {
        mCurrentValue = java.lang.Boolean.toString(state);
    }

    /**
     * Sets the current list of selected values for an entry of type {@link #TYPE_MULTI_SELECT}.
     * These values will be persisted by the system for later use by the application.
     *
     * @param allSelectedStrings
     * 		the current list of selected values.
     */
    public void setAllSelectedStrings(java.lang.String[] allSelectedStrings) {
        mCurrentValues = allSelectedStrings;
    }

    /**
     * Sets a list of string values that can be selected by the user. If no user-visible entries
     * are set by a call to {@link #setChoiceEntries(String[])}, these values will be the ones
     * shown to the user. Values will be chosen from this list as the user's selection and the
     * selected values can be retrieved by a call to {@link #getAllSelectedStrings()}, or
     * {@link #getSelectedString()}, depending on whether it is a multi-select type or choice type.
     * This method is not relevant for types other than
     * {@link #TYPE_CHOICE}, and {@link #TYPE_MULTI_SELECT}.
     *
     * @param choiceValues
     * 		an array of Strings which will be the selected values for the user's
     * 		selections.
     * @see #getChoiceValues()
     * @see #getAllSelectedStrings()
     */
    public void setChoiceValues(java.lang.String[] choiceValues) {
        mChoiceValues = choiceValues;
    }

    /**
     * Sets a list of string values that can be selected by the user, similar to
     * {@link #setChoiceValues(String[])}.
     *
     * @param context
     * 		the application context for retrieving the resources.
     * @param stringArrayResId
     * 		the resource id for a string array containing the possible values.
     * @see #setChoiceValues(String[])
     */
    public void setChoiceValues(android.content.Context context, @android.annotation.ArrayRes
    int stringArrayResId) {
        mChoiceValues = context.getResources().getStringArray(stringArrayResId);
    }

    /**
     * Returns array of possible restriction entries that this entry may contain.
     */
    public android.content.RestrictionEntry[] getRestrictions() {
        return mRestrictions;
    }

    /**
     * Sets an array of possible restriction entries, that this entry may contain.
     * <p>This method is only relevant for types {@link #TYPE_BUNDLE} and
     * {@link #TYPE_BUNDLE_ARRAY}
     */
    public void setRestrictions(android.content.RestrictionEntry[] restrictions) {
        mRestrictions = restrictions;
    }

    /**
     * Returns the list of possible string values set earlier.
     *
     * @return the list of possible values.
     */
    public java.lang.String[] getChoiceValues() {
        return mChoiceValues;
    }

    /**
     * Sets a list of strings that will be presented as choices to the user. When the
     * user selects one or more of these choices, the corresponding value from the possible values
     * are stored as the selected strings. The size of this array must match the size of the array
     * set in {@link #setChoiceValues(String[])}. This method is not relevant for types other
     * than {@link #TYPE_CHOICE}, and {@link #TYPE_MULTI_SELECT}.
     *
     * @param choiceEntries
     * 		the list of user-visible choices.
     * @see #setChoiceValues(String[])
     */
    public void setChoiceEntries(java.lang.String[] choiceEntries) {
        mChoiceEntries = choiceEntries;
    }

    /**
     * Sets a list of strings that will be presented as choices to the user. This is similar to
     * {@link #setChoiceEntries(String[])}.
     *
     * @param context
     * 		the application context, used for retrieving the resources.
     * @param stringArrayResId
     * 		the resource id of a string array containing the possible entries.
     */
    public void setChoiceEntries(android.content.Context context, @android.annotation.ArrayRes
    int stringArrayResId) {
        mChoiceEntries = context.getResources().getStringArray(stringArrayResId);
    }

    /**
     * Returns the list of strings, set earlier, that will be presented as choices to the user.
     *
     * @return the list of choices presented to the user.
     */
    public java.lang.String[] getChoiceEntries() {
        return mChoiceEntries;
    }

    /**
     * Returns the provided user-visible description of the entry, if any.
     *
     * @return the user-visible description, null if none was set earlier.
     */
    public java.lang.String getDescription() {
        return mDescription;
    }

    /**
     * Sets the user-visible description of the entry, as a possible sub-text for the title.
     * You can use this to describe the entry in more detail or to display the current state of
     * the restriction.
     *
     * @param description
     * 		the user-visible description string.
     */
    public void setDescription(java.lang.String description) {
        this.mDescription = description;
    }

    /**
     * This is the unique key for the restriction entry.
     *
     * @return the key for the restriction.
     */
    public java.lang.String getKey() {
        return mKey;
    }

    /**
     * Returns the user-visible title for the entry, if any.
     *
     * @return the user-visible title for the entry, null if none was set earlier.
     */
    public java.lang.String getTitle() {
        return mTitle;
    }

    /**
     * Sets the user-visible title for the entry.
     *
     * @param title
     * 		the user-visible title for the entry.
     */
    public void setTitle(java.lang.String title) {
        this.mTitle = title;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (o == this)
            return true;

        if (!(o instanceof android.content.RestrictionEntry))
            return false;

        final android.content.RestrictionEntry other = ((android.content.RestrictionEntry) (o));
        if ((mType != other.mType) || (!mKey.equals(other.mKey))) {
            return false;
        }
        if (((((mCurrentValues == null) && (other.mCurrentValues == null)) && (mRestrictions == null)) && (other.mRestrictions == null)) && java.util.Objects.equals(mCurrentValue, other.mCurrentValue)) {
            return true;
        }
        if (((((mCurrentValue == null) && (other.mCurrentValue == null)) && (mRestrictions == null)) && (other.mRestrictions == null)) && java.util.Arrays.equals(mCurrentValues, other.mCurrentValues)) {
            return true;
        }
        if (((((mCurrentValue == null) && (other.mCurrentValue == null)) && (mCurrentValue == null)) && (other.mCurrentValue == null)) && java.util.Arrays.equals(mRestrictions, other.mRestrictions)) {
            return true;
        }
        return false;
    }

    @java.lang.Override
    public int hashCode() {
        int result = 17;
        result = (31 * result) + mKey.hashCode();
        if (mCurrentValue != null) {
            result = (31 * result) + mCurrentValue.hashCode();
        } else
            if (mCurrentValues != null) {
                for (java.lang.String value : mCurrentValues) {
                    if (value != null) {
                        result = (31 * result) + value.hashCode();
                    }
                }
            } else
                if (mRestrictions != null) {
                    result = (31 * result) + java.util.Arrays.hashCode(mRestrictions);
                }


        return result;
    }

    public RestrictionEntry(android.os.Parcel in) {
        mType = in.readInt();
        mKey = in.readString();
        mTitle = in.readString();
        mDescription = in.readString();
        mChoiceEntries = in.readStringArray();
        mChoiceValues = in.readStringArray();
        mCurrentValue = in.readString();
        mCurrentValues = in.readStringArray();
        android.os.Parcelable[] parcelables = in.readParcelableArray(null);
        if (parcelables != null) {
            mRestrictions = new android.content.RestrictionEntry[parcelables.length];
            for (int i = 0; i < parcelables.length; i++) {
                mRestrictions[i] = ((android.content.RestrictionEntry) (parcelables[i]));
            }
        }
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeInt(mType);
        dest.writeString(mKey);
        dest.writeString(mTitle);
        dest.writeString(mDescription);
        dest.writeStringArray(mChoiceEntries);
        dest.writeStringArray(mChoiceValues);
        dest.writeString(mCurrentValue);
        dest.writeStringArray(mCurrentValues);
        dest.writeParcelableArray(mRestrictions, 0);
    }

    @android.annotation.NonNull
    public static final android.content.Creator<android.content.RestrictionEntry> CREATOR = new android.content.Creator<android.content.RestrictionEntry>() {
        public android.content.RestrictionEntry createFromParcel(android.os.Parcel source) {
            return new android.content.RestrictionEntry(source);
        }

        public android.content.RestrictionEntry[] newArray(int size) {
            return new android.content.RestrictionEntry[size];
        }
    };

    @java.lang.Override
    public java.lang.String toString() {
        return (((((((((((((((((((((("RestrictionEntry{" + "mType=") + mType) + ", mKey='") + mKey) + '\'') + ", mTitle='") + mTitle) + '\'') + ", mDescription='") + mDescription) + '\'') + ", mChoiceEntries=") + java.util.Arrays.toString(mChoiceEntries)) + ", mChoiceValues=") + java.util.Arrays.toString(mChoiceValues)) + ", mCurrentValue='") + mCurrentValue) + '\'') + ", mCurrentValues=") + java.util.Arrays.toString(mCurrentValues)) + ", mRestrictions=") + java.util.Arrays.toString(mRestrictions)) + '}';
    }
}

