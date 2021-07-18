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
 * An easy adapter to map static data to views defined in an XML file. You can specify the data
 * backing the list as an ArrayList of Maps. Each entry in the ArrayList corresponds to one row
 * in the list. The Maps contain the data for each row. You also specify an XML file that
 * defines the views used to display the row, and a mapping from keys in the Map to specific
 * views.
 *
 * Binding data to views occurs in two phases. First, if a
 * {@link android.widget.SimpleAdapter.ViewBinder} is available,
 * {@link ViewBinder#setViewValue(android.view.View, Object, String)}
 * is invoked. If the returned value is true, binding has occurred.
 * If the returned value is false, the following views are then tried in order:
 * <ul>
 * <li> A view that implements Checkable (e.g. CheckBox).  The expected bind value is a boolean.
 * <li> TextView.  The expected bind value is a string and {@link #setViewText(TextView, String)}
 * is invoked.
 * <li> ImageView. The expected bind value is a resource id or a string and
 * {@link #setViewImage(ImageView, int)} or {@link #setViewImage(ImageView, String)} is invoked.
 * </ul>
 * If no appropriate binding can be found, an {@link IllegalStateException} is thrown.
 */
public class SimpleAdapter extends android.widget.BaseAdapter implements android.widget.Filterable , android.widget.ThemedSpinnerAdapter {
    private final android.view.LayoutInflater mInflater;

    private int[] mTo;

    private java.lang.String[] mFrom;

    private android.widget.SimpleAdapter.ViewBinder mViewBinder;

    @android.annotation.UnsupportedAppUsage(maxTargetSdk = Build.VERSION_CODES.P, trackingBug = 115609023)
    private java.util.List<? extends java.util.Map<java.lang.String, ?>> mData;

    private int mResource;

    private int mDropDownResource;

    /**
     * Layout inflater used for {@link #getDropDownView(int, View, ViewGroup)}.
     */
    private android.view.LayoutInflater mDropDownInflater;

    private android.widget.SimpleAdapter.SimpleFilter mFilter;

    private java.util.ArrayList<java.util.Map<java.lang.String, ?>> mUnfilteredData;

    /**
     * Constructor
     *
     * @param context
     * 		The context where the View associated with this SimpleAdapter is running
     * @param data
     * 		A List of Maps. Each entry in the List corresponds to one row in the list. The
     * 		Maps contain the data for each row, and should include all the entries specified in
     * 		"from"
     * @param resource
     * 		Resource identifier of a view layout that defines the views for this list
     * 		item. The layout file should include at least those named views defined in "to"
     * @param from
     * 		A list of column names that will be added to the Map associated with each
     * 		item.
     * @param to
     * 		The views that should display column in the "from" parameter. These should all be
     * 		TextViews. The first N views in this list are given the values of the first N columns
     * 		in the from parameter.
     */
    public SimpleAdapter(android.content.Context context, java.util.List<? extends java.util.Map<java.lang.String, ?>> data, @android.annotation.LayoutRes
    int resource, java.lang.String[] from, @android.annotation.IdRes
    int[] to) {
        mData = data;
        mResource = mDropDownResource = resource;
        mFrom = from;
        mTo = to;
        mInflater = ((android.view.LayoutInflater) (context.getSystemService(android.content.Context.LAYOUT_INFLATER_SERVICE)));
    }

    /**
     *
     *
     * @see android.widget.Adapter#getCount()
     */
    public int getCount() {
        return mData.size();
    }

    /**
     *
     *
     * @see android.widget.Adapter#getItem(int)
     */
    public java.lang.Object getItem(int position) {
        return mData.get(position);
    }

    /**
     *
     *
     * @see android.widget.Adapter#getItemId(int)
     */
    public long getItemId(int position) {
        return position;
    }

    /**
     *
     *
     * @see android.widget.Adapter#getView(int, View, ViewGroup)
     */
    public android.view.View getView(int position, android.view.View convertView, android.view.ViewGroup parent) {
        return createViewFromResource(mInflater, position, convertView, parent, mResource);
    }

    private android.view.View createViewFromResource(android.view.LayoutInflater inflater, int position, android.view.View convertView, android.view.ViewGroup parent, int resource) {
        android.view.View v;
        if (convertView == null) {
            v = inflater.inflate(resource, parent, false);
        } else {
            v = convertView;
        }
        bindView(position, v);
        return v;
    }

    /**
     * <p>Sets the layout resource to create the drop down views.</p>
     *
     * @param resource
     * 		the layout resource defining the drop down views
     * @see #getDropDownView(int, android.view.View, android.view.ViewGroup)
     */
    public void setDropDownViewResource(int resource) {
        mDropDownResource = resource;
    }

    /**
     * Sets the {@link android.content.res.Resources.Theme} against which drop-down views are
     * inflated.
     * <p>
     * By default, drop-down views are inflated against the theme of the
     * {@link Context} passed to the adapter's constructor.
     *
     * @param theme
     * 		the theme against which to inflate drop-down views or
     * 		{@code null} to use the theme from the adapter's context
     * @see #getDropDownView(int, View, ViewGroup)
     */
    @java.lang.Override
    public void setDropDownViewTheme(android.content.res.Resources.Theme theme) {
        if (theme == null) {
            mDropDownInflater = null;
        } else
            if (theme == mInflater.getContext().getTheme()) {
                mDropDownInflater = mInflater;
            } else {
                final android.content.Context context = new android.view.ContextThemeWrapper(mInflater.getContext(), theme);
                mDropDownInflater = android.view.LayoutInflater.from(context);
            }

    }

    @java.lang.Override
    public android.content.res.Resources.Theme getDropDownViewTheme() {
        return mDropDownInflater == null ? null : mDropDownInflater.getContext().getTheme();
    }

    @java.lang.Override
    public android.view.View getDropDownView(int position, android.view.View convertView, android.view.ViewGroup parent) {
        final android.view.LayoutInflater inflater = (mDropDownInflater == null) ? mInflater : mDropDownInflater;
        return createViewFromResource(inflater, position, convertView, parent, mDropDownResource);
    }

    private void bindView(int position, android.view.View view) {
        final java.util.Map dataSet = mData.get(position);
        if (dataSet == null) {
            return;
        }
        final android.widget.SimpleAdapter.ViewBinder binder = mViewBinder;
        final java.lang.String[] from = mFrom;
        final int[] to = mTo;
        final int count = to.length;
        for (int i = 0; i < count; i++) {
            final android.view.View v = view.findViewById(to[i]);
            if (v != null) {
                final java.lang.Object data = dataSet.get(from[i]);
                java.lang.String text = (data == null) ? "" : data.toString();
                if (text == null) {
                    text = "";
                }
                boolean bound = false;
                if (binder != null) {
                    bound = binder.setViewValue(v, data, text);
                }
                if (!bound) {
                    if (v instanceof android.widget.Checkable) {
                        if (data instanceof java.lang.Boolean) {
                            ((android.widget.Checkable) (v)).setChecked(((java.lang.Boolean) (data)));
                        } else
                            if (v instanceof android.widget.TextView) {
                                // Note: keep the instanceof TextView check at the bottom of these
                                // ifs since a lot of views are TextViews (e.g. CheckBoxes).
                                setViewText(((android.widget.TextView) (v)), text);
                            } else {
                                throw new java.lang.IllegalStateException((v.getClass().getName() + " should be bound to a Boolean, not a ") + (data == null ? "<unknown type>" : data.getClass()));
                            }

                    } else
                        if (v instanceof android.widget.TextView) {
                            // Note: keep the instanceof TextView check at the bottom of these
                            // ifs since a lot of views are TextViews (e.g. CheckBoxes).
                            setViewText(((android.widget.TextView) (v)), text);
                        } else
                            if (v instanceof android.widget.ImageView) {
                                if (data instanceof java.lang.Integer) {
                                    setViewImage(((android.widget.ImageView) (v)), ((java.lang.Integer) (data)));
                                } else {
                                    setViewImage(((android.widget.ImageView) (v)), text);
                                }
                            } else {
                                throw new java.lang.IllegalStateException((v.getClass().getName() + " is not a ") + " view that can be bounds by this SimpleAdapter");
                            }


                }
            }
        }
    }

    /**
     * Returns the {@link ViewBinder} used to bind data to views.
     *
     * @return a ViewBinder or null if the binder does not exist
     * @see #setViewBinder(android.widget.SimpleAdapter.ViewBinder)
     */
    public android.widget.SimpleAdapter.ViewBinder getViewBinder() {
        return mViewBinder;
    }

    /**
     * Sets the binder used to bind data to views.
     *
     * @param viewBinder
     * 		the binder used to bind data to views, can be null to
     * 		remove the existing binder
     * @see #getViewBinder()
     */
    public void setViewBinder(android.widget.SimpleAdapter.ViewBinder viewBinder) {
        mViewBinder = viewBinder;
    }

    /**
     * Called by bindView() to set the image for an ImageView but only if
     * there is no existing ViewBinder or if the existing ViewBinder cannot
     * handle binding to an ImageView.
     *
     * This method is called instead of {@link #setViewImage(ImageView, String)}
     * if the supplied data is an int or Integer.
     *
     * @param v
     * 		ImageView to receive an image
     * @param value
     * 		the value retrieved from the data set
     * @see #setViewImage(ImageView, String)
     */
    public void setViewImage(android.widget.ImageView v, int value) {
        v.setImageResource(value);
    }

    /**
     * Called by bindView() to set the image for an ImageView but only if
     * there is no existing ViewBinder or if the existing ViewBinder cannot
     * handle binding to an ImageView.
     *
     * By default, the value will be treated as an image resource. If the
     * value cannot be used as an image resource, the value is used as an
     * image Uri.
     *
     * This method is called instead of {@link #setViewImage(ImageView, int)}
     * if the supplied data is not an int or Integer.
     *
     * @param v
     * 		ImageView to receive an image
     * @param value
     * 		the value retrieved from the data set
     * @see #setViewImage(ImageView, int)
     */
    public void setViewImage(android.widget.ImageView v, java.lang.String value) {
        try {
            v.setImageResource(java.lang.Integer.parseInt(value));
        } catch (java.lang.NumberFormatException nfe) {
            v.setImageURI(android.net.Uri.parse(value));
        }
    }

    /**
     * Called by bindView() to set the text for a TextView but only if
     * there is no existing ViewBinder or if the existing ViewBinder cannot
     * handle binding to a TextView.
     *
     * @param v
     * 		TextView to receive text
     * @param text
     * 		the text to be set for the TextView
     */
    public void setViewText(android.widget.TextView v, java.lang.String text) {
        v.setText(text);
    }

    public android.widget.Filter getFilter() {
        if (mFilter == null) {
            mFilter = new android.widget.SimpleAdapter.SimpleFilter();
        }
        return mFilter;
    }

    /**
     * This class can be used by external clients of SimpleAdapter to bind
     * values to views.
     *
     * You should use this class to bind values to views that are not
     * directly supported by SimpleAdapter or to change the way binding
     * occurs for views supported by SimpleAdapter.
     *
     * @see SimpleAdapter#setViewImage(ImageView, int)
     * @see SimpleAdapter#setViewImage(ImageView, String)
     * @see SimpleAdapter#setViewText(TextView, String)
     */
    public static interface ViewBinder {
        /**
         * Binds the specified data to the specified view.
         *
         * When binding is handled by this ViewBinder, this method must return true.
         * If this method returns false, SimpleAdapter will attempts to handle
         * the binding on its own.
         *
         * @param view
         * 		the view to bind the data to
         * @param data
         * 		the data to bind to the view
         * @param textRepresentation
         * 		a safe String representation of the supplied data:
         * 		it is either the result of data.toString() or an empty String but it
         * 		is never null
         * @return true if the data was bound to the view, false otherwise
         */
        boolean setViewValue(android.view.View view, java.lang.Object data, java.lang.String textRepresentation);
    }

    /**
     * <p>An array filters constrains the content of the array adapter with
     * a prefix. Each item that does not start with the supplied prefix
     * is removed from the list.</p>
     */
    private class SimpleFilter extends android.widget.Filter {
        @java.lang.Override
        protected android.widget.Filter.FilterResults performFiltering(java.lang.CharSequence prefix) {
            android.widget.Filter.FilterResults results = new android.widget.Filter.FilterResults();
            if (mUnfilteredData == null) {
                mUnfilteredData = new java.util.ArrayList<java.util.Map<java.lang.String, ?>>(mData);
            }
            if ((prefix == null) || (prefix.length() == 0)) {
                java.util.ArrayList<java.util.Map<java.lang.String, ?>> list = mUnfilteredData;
                results.values = list;
                results.count = list.size();
            } else {
                java.lang.String prefixString = prefix.toString().toLowerCase();
                java.util.ArrayList<java.util.Map<java.lang.String, ?>> unfilteredValues = mUnfilteredData;
                int count = unfilteredValues.size();
                java.util.ArrayList<java.util.Map<java.lang.String, ?>> newValues = new java.util.ArrayList<java.util.Map<java.lang.String, ?>>(count);
                for (int i = 0; i < count; i++) {
                    java.util.Map<java.lang.String, ?> h = unfilteredValues.get(i);
                    if (h != null) {
                        int len = mTo.length;
                        for (int j = 0; j < len; j++) {
                            java.lang.String str = ((java.lang.String) (h.get(mFrom[j])));
                            java.lang.String[] words = str.split(" ");
                            int wordCount = words.length;
                            for (int k = 0; k < wordCount; k++) {
                                java.lang.String word = words[k];
                                if (word.toLowerCase().startsWith(prefixString)) {
                                    newValues.add(h);
                                    break;
                                }
                            }
                        }
                    }
                }
                results.values = newValues;
                results.count = newValues.size();
            }
            return results;
        }

        @java.lang.Override
        protected void publishResults(java.lang.CharSequence constraint, android.widget.Filter.FilterResults results) {
            // noinspection unchecked
            mData = ((java.util.List<java.util.Map<java.lang.String, ?>>) (results.values));
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }
    }
}

