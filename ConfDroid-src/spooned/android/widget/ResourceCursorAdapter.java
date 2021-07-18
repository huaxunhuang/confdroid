/**
 * Copyright (C) 2007 The Android Open Source Project
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
 * An easy adapter that creates views defined in an XML file. You can specify
 * the XML file that defines the appearance of the views.
 */
public abstract class ResourceCursorAdapter extends android.widget.CursorAdapter {
    private int mLayout;

    private int mDropDownLayout;

    private android.view.LayoutInflater mInflater;

    private android.view.LayoutInflater mDropDownInflater;

    /**
     * Constructor the enables auto-requery.
     *
     * @deprecated This option is discouraged, as it results in Cursor queries
    being performed on the application's UI thread and thus can cause poor
    responsiveness or even Application Not Responding errors.  As an alternative,
    use {@link android.app.LoaderManager} with a {@link android.content.CursorLoader}.
     * @param context
     * 		The context where the ListView associated with this adapter is running
     * @param layout
     * 		resource identifier of a layout file that defines the views
     * 		for this list item.  Unless you override them later, this will
     * 		define both the item views and the drop down views.
     */
    @java.lang.Deprecated
    public ResourceCursorAdapter(android.content.Context context, int layout, android.database.Cursor c) {
        super(context, c);
        mLayout = mDropDownLayout = layout;
        mInflater = ((android.view.LayoutInflater) (context.getSystemService(android.content.Context.LAYOUT_INFLATER_SERVICE)));
        mDropDownInflater = mInflater;
    }

    /**
     * Constructor with default behavior as per
     * {@link CursorAdapter#CursorAdapter(Context, Cursor, boolean)}; it is recommended
     * you not use this, but instead {@link #ResourceCursorAdapter(Context, int, Cursor, int)}.
     * When using this constructor, {@link #FLAG_REGISTER_CONTENT_OBSERVER}
     * will always be set.
     *
     * @param context
     * 		The context where the ListView associated with this adapter is running
     * @param layout
     * 		resource identifier of a layout file that defines the views
     * 		for this list item.  Unless you override them later, this will
     * 		define both the item views and the drop down views.
     * @param c
     * 		The cursor from which to get the data.
     * @param autoRequery
     * 		If true the adapter will call requery() on the
     * 		cursor whenever it changes so the most recent
     * 		data is always displayed.  Using true here is discouraged.
     */
    public ResourceCursorAdapter(android.content.Context context, int layout, android.database.Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
        mLayout = mDropDownLayout = layout;
        mInflater = ((android.view.LayoutInflater) (context.getSystemService(android.content.Context.LAYOUT_INFLATER_SERVICE)));
        mDropDownInflater = mInflater;
    }

    /**
     * Standard constructor.
     *
     * @param context
     * 		The context where the ListView associated with this adapter is running
     * @param layout
     * 		Resource identifier of a layout file that defines the views
     * 		for this list item.  Unless you override them later, this will
     * 		define both the item views and the drop down views.
     * @param c
     * 		The cursor from which to get the data.
     * @param flags
     * 		Flags used to determine the behavior of the adapter,
     * 		as per {@link CursorAdapter#CursorAdapter(Context, Cursor, int)}.
     */
    public ResourceCursorAdapter(android.content.Context context, int layout, android.database.Cursor c, int flags) {
        super(context, c, flags);
        mLayout = mDropDownLayout = layout;
        mInflater = ((android.view.LayoutInflater) (context.getSystemService(android.content.Context.LAYOUT_INFLATER_SERVICE)));
        mDropDownInflater = mInflater;
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
     * @see #newDropDownView(Context, Cursor, ViewGroup)
     */
    @java.lang.Override
    public void setDropDownViewTheme(android.content.res.Resources.Theme theme) {
        super.setDropDownViewTheme(theme);
        if (theme == null) {
            mDropDownInflater = null;
        } else
            if (theme == mInflater.getContext().getTheme()) {
                mDropDownInflater = mInflater;
            } else {
                final android.content.Context context = new android.view.ContextThemeWrapper(mContext, theme);
                mDropDownInflater = android.view.LayoutInflater.from(context);
            }

    }

    /**
     * Inflates view(s) from the specified XML file.
     *
     * @see android.widget.CursorAdapter#newView(android.content.Context,
    android.database.Cursor, ViewGroup)
     */
    @java.lang.Override
    public android.view.View newView(android.content.Context context, android.database.Cursor cursor, android.view.ViewGroup parent) {
        return mInflater.inflate(mLayout, parent, false);
    }

    @java.lang.Override
    public android.view.View newDropDownView(android.content.Context context, android.database.Cursor cursor, android.view.ViewGroup parent) {
        return mDropDownInflater.inflate(mDropDownLayout, parent, false);
    }

    /**
     * <p>Sets the layout resource of the item views.</p>
     *
     * @param layout
     * 		the layout resources used to create item views
     */
    public void setViewResource(int layout) {
        mLayout = layout;
    }

    /**
     * <p>Sets the layout resource of the drop down views.</p>
     *
     * @param dropDownLayout
     * 		the layout resources used to create drop down views
     */
    public void setDropDownViewResource(int dropDownLayout) {
        mDropDownLayout = dropDownLayout;
    }
}

