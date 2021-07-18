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
package android.support.v17.leanback.widget;


/**
 * Subclass of GuidedAction that can choose a date.  The Action is editable by default; to make it
 * read only, call hasEditableActivatorView(false) on the Builder.
 */
public class GuidedDatePickerAction extends android.support.v17.leanback.widget.GuidedAction {
    /**
     * Base Builder class to build GuidedDatePickerAction.  Subclass this BuilderBase when app needs
     * to subclass GuidedDatePickerAction, implement your build() which should call
     * {@link #applyDatePickerValues(GuidedDatePickerAction)}.  When using GuidedDatePickerAction
     * directly, use {@link Builder}.
     */
    public static abstract class BuilderBase<B extends android.support.v17.leanback.widget.GuidedDatePickerAction.BuilderBase> extends android.support.v17.leanback.widget.GuidedAction.BuilderBase<B> {
        private java.lang.String mDatePickerFormat;

        private long mDate;

        private long mMinDate = java.lang.Long.MIN_VALUE;

        private long mMaxDate = java.lang.Long.MAX_VALUE;

        public BuilderBase(android.content.Context context) {
            super(context);
            java.util.Calendar c = java.util.Calendar.getInstance();
            mDate = c.getTimeInMillis();
            hasEditableActivatorView(true);
        }

        /**
         * Sets format of date Picker or null for default.  The format is a case insensitive String
         * containing the day ('d'), month ('m'), and year ('y').  When the format is not specified,
         * a default format of current locale will be used.
         *
         * @param format
         * 		Format of showing Date, e.g. "YMD".
         * @return This Builder object.
         */
        public B datePickerFormat(java.lang.String format) {
            mDatePickerFormat = format;
            return ((B) (this));
        }

        /**
         * Sets a Date for date picker in milliseconds since January 1, 1970 00:00:00 in
         * {@link TimeZone#getDefault()} time zone.
         *
         * @return This Builder Object.
         */
        public B date(long date) {
            mDate = date;
            return ((B) (this));
        }

        /**
         * Sets minimal Date for date picker in milliseconds since January 1, 1970 00:00:00 in
         * {@link TimeZone#getDefault()} time zone.
         *
         * @return This Builder Object.
         */
        public B minDate(long minDate) {
            mMinDate = minDate;
            return ((B) (this));
        }

        /**
         * Sets maximum Date for date picker in milliseconds since January 1, 1970 00:00:00 in
         * {@link TimeZone#getDefault()} time zone.
         *
         * @return This Builder Object.
         */
        public B maxDate(long maxDate) {
            mMaxDate = maxDate;
            return ((B) (this));
        }

        /**
         * Apply values to GuidedDatePickerAction.
         *
         * @param action
         * 		GuidedDatePickerAction to apply values.
         */
        protected final void applyDatePickerValues(android.support.v17.leanback.widget.GuidedDatePickerAction action) {
            super.applyValues(action);
            action.mDatePickerFormat = mDatePickerFormat;
            action.mDate = mDate;
            if (mMinDate > mMaxDate) {
                throw new java.lang.IllegalArgumentException("MinDate cannot be larger than MaxDate");
            }
            action.mMinDate = mMinDate;
            action.mMaxDate = mMaxDate;
        }
    }

    /**
     * Builder class to build a GuidedDatePickerAction.
     */
    public static final class Builder extends android.support.v17.leanback.widget.GuidedDatePickerAction.BuilderBase<android.support.v17.leanback.widget.GuidedDatePickerAction.Builder> {
        public Builder(android.content.Context context) {
            super(context);
        }

        /**
         * Builds the GuidedDatePickerAction corresponding to this Builder.
         *
         * @return The GuidedDatePickerAction as configured through this Builder.
         */
        public android.support.v17.leanback.widget.GuidedDatePickerAction build() {
            android.support.v17.leanback.widget.GuidedDatePickerAction action = new android.support.v17.leanback.widget.GuidedDatePickerAction();
            applyDatePickerValues(action);
            return action;
        }
    }

    java.lang.String mDatePickerFormat;

    long mDate;

    long mMinDate = java.lang.Long.MIN_VALUE;

    long mMaxDate = java.lang.Long.MAX_VALUE;

    /**
     * Returns format of date Picker or null if not specified.  The format is a case insensitive
     * String containing the * day ('d'), month ('m'), and year ('y'). When the format is not
     * specified, a default format of current locale will
     * be used.
     *
     * @return Format of showing Date, e.g. "YMD".  Returns null if using current locale's default.
     */
    public java.lang.String getDatePickerFormat() {
        return mDatePickerFormat;
    }

    /**
     * Get current value of DatePicker in milliseconds since January 1, 1970 00:00:00 in
     * {@link TimeZone#getDefault()} time zone.
     *
     * @return Current value of DatePicker Action.
     */
    public long getDate() {
        return mDate;
    }

    /**
     * Sets current value of DatePicker in milliseconds since January 1, 1970 00:00:00 in
     * {@link TimeZone#getDefault()} time zone.
     *
     * @param date
     * 		New value to update current value of DatePicker Action.
     */
    public void setDate(long date) {
        mDate = date;
    }

    /**
     * Get minimal value of DatePicker in milliseconds since January 1, 1970 00:00:00 in
     * {@link TimeZone#getDefault()} time zone.  -1 if not set.
     *
     * @return Minimal value of DatePicker Action or Long.MIN_VALUE if not set.
     */
    public long getMinDate() {
        return mMinDate;
    }

    /**
     * Get maximum value of DatePicker in milliseconds since January 1, 1970 00:00:00 in
     * {@link TimeZone#getDefault()} time zone.
     *
     * @return Maximum value of DatePicker Action or Long.MAX_VALUE if not set.
     */
    public long getMaxDate() {
        return mMaxDate;
    }

    @java.lang.Override
    public void onSaveInstanceState(android.os.Bundle bundle, java.lang.String key) {
        bundle.putLong(key, getDate());
    }

    @java.lang.Override
    public void onRestoreInstanceState(android.os.Bundle bundle, java.lang.String key) {
        setDate(bundle.getLong(key, getDate()));
    }
}

