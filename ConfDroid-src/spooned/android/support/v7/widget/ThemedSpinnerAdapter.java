/**
 * Copyright (C) 2015 The Android Open Source Project
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
package android.support.v7.widget;


/**
 * An extension of SpinnerAdapter that is capable of inflating drop-down views
 * against a different theme than normal views.
 * <p>
 * Classes that implement this interface should use the theme provided to
 * {@link #setDropDownViewTheme(Theme)} when creating views in
 * {@link SpinnerAdapter#getDropDownView(int, View, ViewGroup)}.
 *
 * <p>The {@link Helper} class is provided to aide implementation in a backwards compatible way.
 * </p>
 */
public interface ThemedSpinnerAdapter extends android.widget.SpinnerAdapter {
    /**
     * Sets the {@link Resources.Theme} against which drop-down views are
     * inflated.
     *
     * @param theme
     * 		the context against which to inflate drop-down views, or
     * 		{@code null} to use the default theme
     * @see SpinnerAdapter#getDropDownView(int, View, ViewGroup)
     */
    void setDropDownViewTheme(@android.support.annotation.Nullable
    android.content.res.Resources.Theme theme);

    /**
     * Returns the value previously set by a call to
     * {@link #setDropDownViewTheme(Theme)}.
     *
     * @return the {@link Resources.Theme} against which drop-down views are
    inflated, or {@code null} if one has not been explicitly set
     */
    @android.support.annotation.Nullable
    android.content.res.Resources.Theme getDropDownViewTheme();

    /**
     * A helper class which allows easy integration of {@link ThemedSpinnerAdapter} into existing
     * {@link SpinnerAdapter}s in a backwards compatible way.
     *
     * <p>An example {@link android.widget.BaseAdapter BaseAdapter} implementation would be:</p>
     *
     * <pre>
     * public class MyAdapter extends BaseAdapter implements ThemedSpinnerAdapter {
     *     private final ThemedSpinnerAdapter.Helper mDropDownHelper;
     *
     *     public CheeseAdapter(Context context) {
     *         mDropDownHelper = new ThemedSpinnerAdapter.Helper(context);
     *         // ...
     *     }
     *
     *     &#064;Override
     *     public View getDropDownView(int position, View convertView, ViewGroup parent) {
     *         View view;
     *
     *         if (convertView == null) {
     *             // Inflate the drop down using the helper's LayoutInflater
     *             LayoutInflater inflater = mDropDownHelper.getDropDownViewInflater();
     *             view = inflater.inflate(R.layout.my_dropdown, parent, false);
     *         }
     *
     *         // ...
     *     }
     *
     *     &#064;Override
     *     public void setDropDownViewTheme(@Nullable Resources.Theme theme) {
     *         // Pass the new theme to the helper
     *         mDropDownHelper.setDropDownViewTheme(theme);
     *     }
     *
     *     &#064;Override
     *     public Resources.Theme getDropDownViewTheme() {
     *         // Return the helper's value
     *         return mDropDownHelper.getDropDownViewTheme();
     *     }
     * }
     * </pre>
     */
    public static final class Helper {
        private final android.content.Context mContext;

        private final android.view.LayoutInflater mInflater;

        private android.view.LayoutInflater mDropDownInflater;

        public Helper(@android.support.annotation.NonNull
        android.content.Context context) {
            mContext = context;
            mInflater = android.view.LayoutInflater.from(context);
        }

        /**
         * Should be called from your adapter's
         * {@link ThemedSpinnerAdapter#setDropDownViewTheme(Theme)}
         *
         * @param theme
         * 		the theme passed in to
         * 		{@link ThemedSpinnerAdapter#setDropDownViewTheme(Theme)}
         */
        public void setDropDownViewTheme(@android.support.annotation.Nullable
        android.content.res.Resources.Theme theme) {
            if (theme == null) {
                mDropDownInflater = null;
            } else
                if (theme == mContext.getTheme()) {
                    mDropDownInflater = mInflater;
                } else {
                    final android.content.Context context = new android.support.v7.view.ContextThemeWrapper(mContext, theme);
                    mDropDownInflater = android.view.LayoutInflater.from(context);
                }

        }

        /**
         * Should be called from your adapter's {@link ThemedSpinnerAdapter#getDropDownViewTheme()},
         * returning the value returned from this method.
         */
        @android.support.annotation.Nullable
        public android.content.res.Resources.Theme getDropDownViewTheme() {
            return mDropDownInflater == null ? null : mDropDownInflater.getContext().getTheme();
        }

        /**
         * Returns the {@link LayoutInflater} which should be used when inflating any layouts
         * from your {@link SpinnerAdapter#getDropDownView(int, View, ViewGroup)}.
         *
         * <p>The instance returned will have a correct theme, meaning that any inflated views
         * will be created with the same theme.</p>
         */
        @android.support.annotation.NonNull
        public android.view.LayoutInflater getDropDownViewInflater() {
            return mDropDownInflater != null ? mDropDownInflater : mInflater;
        }
    }
}

