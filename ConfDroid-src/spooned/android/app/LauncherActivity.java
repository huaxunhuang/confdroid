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
package android.app;


/**
 * Displays a list of all activities which can be performed
 * for a given intent. Launches when clicked.
 */
public abstract class LauncherActivity extends android.app.ListActivity {
    android.content.Intent mIntent;

    android.content.pm.PackageManager mPackageManager;

    android.app.LauncherActivity.IconResizer mIconResizer;

    /**
     * An item in the list
     */
    public static class ListItem {
        public android.content.pm.ResolveInfo resolveInfo;

        public java.lang.CharSequence label;

        public android.graphics.drawable.Drawable icon;

        public java.lang.String packageName;

        public java.lang.String className;

        public android.os.Bundle extras;

        ListItem(android.content.pm.PackageManager pm, android.content.pm.ResolveInfo resolveInfo, android.app.LauncherActivity.IconResizer resizer) {
            this.resolveInfo = resolveInfo;
            label = resolveInfo.loadLabel(pm);
            android.content.pm.ComponentInfo ci = resolveInfo.activityInfo;
            if (ci == null)
                ci = resolveInfo.serviceInfo;

            if ((label == null) && (ci != null)) {
                label = resolveInfo.activityInfo.name;
            }
            if (resizer != null) {
                icon = resizer.createIconThumbnail(resolveInfo.loadIcon(pm));
            }
            packageName = ci.applicationInfo.packageName;
            className = ci.name;
        }

        public ListItem() {
        }
    }

    /**
     * Adapter which shows the set of activities that can be performed for a given intent.
     */
    private class ActivityAdapter extends android.widget.BaseAdapter implements android.widget.Filterable {
        private final java.lang.Object lock = new java.lang.Object();

        private java.util.ArrayList<android.app.LauncherActivity.ListItem> mOriginalValues;

        protected final android.app.LauncherActivity.IconResizer mIconResizer;

        protected final android.view.LayoutInflater mInflater;

        protected java.util.List<android.app.LauncherActivity.ListItem> mActivitiesList;

        private android.widget.Filter mFilter;

        private final boolean mShowIcons;

        public ActivityAdapter(android.app.LauncherActivity.IconResizer resizer) {
            mIconResizer = resizer;
            mInflater = ((android.view.LayoutInflater) (android.app.LauncherActivity.this.getSystemService(android.content.Context.LAYOUT_INFLATER_SERVICE)));
            mShowIcons = onEvaluateShowIcons();
            mActivitiesList = makeListItems();
        }

        public android.content.Intent intentForPosition(int position) {
            if (mActivitiesList == null) {
                return null;
            }
            android.content.Intent intent = new android.content.Intent(mIntent);
            android.app.LauncherActivity.ListItem item = mActivitiesList.get(position);
            intent.setClassName(item.packageName, item.className);
            if (item.extras != null) {
                intent.putExtras(item.extras);
            }
            return intent;
        }

        public android.app.LauncherActivity.ListItem itemForPosition(int position) {
            if (mActivitiesList == null) {
                return null;
            }
            return mActivitiesList.get(position);
        }

        public int getCount() {
            return mActivitiesList != null ? mActivitiesList.size() : 0;
        }

        public java.lang.Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public android.view.View getView(int position, android.view.View convertView, android.view.ViewGroup parent) {
            android.view.View view;
            if (convertView == null) {
                view = mInflater.inflate(com.android.internal.R.layout.activity_list_item_2, parent, false);
            } else {
                view = convertView;
            }
            bindView(view, mActivitiesList.get(position));
            return view;
        }

        private void bindView(android.view.View view, android.app.LauncherActivity.ListItem item) {
            android.widget.TextView text = ((android.widget.TextView) (view));
            text.setText(item.label);
            if (mShowIcons) {
                if (item.icon == null) {
                    item.icon = mIconResizer.createIconThumbnail(item.resolveInfo.loadIcon(getPackageManager()));
                }
                text.setCompoundDrawablesWithIntrinsicBounds(item.icon, null, null, null);
            }
        }

        public android.widget.Filter getFilter() {
            if (mFilter == null) {
                mFilter = new android.app.LauncherActivity.ActivityAdapter.ArrayFilter();
            }
            return mFilter;
        }

        /**
         * An array filters constrains the content of the array adapter with a prefix. Each
         * item that does not start with the supplied prefix is removed from the list.
         */
        private class ArrayFilter extends android.widget.Filter {
            @java.lang.Override
            protected android.widget.Filter.FilterResults performFiltering(java.lang.CharSequence prefix) {
                android.widget.Filter.FilterResults results = new android.widget.Filter.FilterResults();
                if (mOriginalValues == null) {
                    synchronized(lock) {
                        mOriginalValues = new java.util.ArrayList<android.app.LauncherActivity.ListItem>(mActivitiesList);
                    }
                }
                if ((prefix == null) || (prefix.length() == 0)) {
                    synchronized(lock) {
                        java.util.ArrayList<android.app.LauncherActivity.ListItem> list = new java.util.ArrayList<android.app.LauncherActivity.ListItem>(mOriginalValues);
                        results.values = list;
                        results.count = list.size();
                    }
                } else {
                    final java.lang.String prefixString = prefix.toString().toLowerCase();
                    java.util.ArrayList<android.app.LauncherActivity.ListItem> values = mOriginalValues;
                    int count = values.size();
                    java.util.ArrayList<android.app.LauncherActivity.ListItem> newValues = new java.util.ArrayList<android.app.LauncherActivity.ListItem>(count);
                    for (int i = 0; i < count; i++) {
                        android.app.LauncherActivity.ListItem item = values.get(i);
                        java.lang.String[] words = item.label.toString().toLowerCase().split(" ");
                        int wordCount = words.length;
                        for (int k = 0; k < wordCount; k++) {
                            final java.lang.String word = words[k];
                            if (word.startsWith(prefixString)) {
                                newValues.add(item);
                                break;
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
                mActivitiesList = ((java.util.List<android.app.LauncherActivity.ListItem>) (results.values));
                if (results.count > 0) {
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }
        }
    }

    /**
     * Utility class to resize icons to match default icon size.
     */
    public class IconResizer {
        // Code is borrowed from com.android.launcher.Utilities.
        private int mIconWidth = -1;

        private int mIconHeight = -1;

        private final android.graphics.Rect mOldBounds = new android.graphics.Rect();

        private android.graphics.Canvas mCanvas = new android.graphics.Canvas();

        public IconResizer() {
            mCanvas.setDrawFilter(new android.graphics.PaintFlagsDrawFilter(android.graphics.Paint.DITHER_FLAG, android.graphics.Paint.FILTER_BITMAP_FLAG));
            final android.content.res.Resources resources = android.app.LauncherActivity.this.getResources();
            mIconWidth = mIconHeight = ((int) (resources.getDimension(android.app.android.R.dimen)));
        }

        /**
         * Returns a Drawable representing the thumbnail of the specified Drawable.
         * The size of the thumbnail is defined by the dimension
         * android.R.dimen.launcher_application_icon_size.
         *
         * This method is not thread-safe and should be invoked on the UI thread only.
         *
         * @param icon
         * 		The icon to get a thumbnail of.
         * @return A thumbnail for the specified icon or the icon itself if the
        thumbnail could not be created.
         */
        public android.graphics.drawable.Drawable createIconThumbnail(android.graphics.drawable.Drawable icon) {
            int width = mIconWidth;
            int height = mIconHeight;
            final int iconWidth = icon.getIntrinsicWidth();
            final int iconHeight = icon.getIntrinsicHeight();
            if (icon instanceof android.graphics.drawable.PaintDrawable) {
                android.graphics.drawable.PaintDrawable painter = ((android.graphics.drawable.PaintDrawable) (icon));
                painter.setIntrinsicWidth(width);
                painter.setIntrinsicHeight(height);
            }
            if ((width > 0) && (height > 0)) {
                if ((width < iconWidth) || (height < iconHeight)) {
                    final float ratio = ((float) (iconWidth)) / iconHeight;
                    if (iconWidth > iconHeight) {
                        height = ((int) (width / ratio));
                    } else
                        if (iconHeight > iconWidth) {
                            width = ((int) (height * ratio));
                        }

                    final android.graphics.Bitmap.Config c = (icon.getOpacity() != android.graphics.PixelFormat.OPAQUE) ? android.graphics.Bitmap.Config.ARGB_8888 : android.graphics.Bitmap.Config.RGB_565;
                    final android.graphics.Bitmap thumb = android.graphics.Bitmap.createBitmap(mIconWidth, mIconHeight, c);
                    final android.graphics.Canvas canvas = mCanvas;
                    canvas.setBitmap(thumb);
                    // Copy the old bounds to restore them later
                    // If we were to do oldBounds = icon.getBounds(),
                    // the call to setBounds() that follows would
                    // change the same instance and we would lose the
                    // old bounds
                    mOldBounds.set(icon.getBounds());
                    final int x = (mIconWidth - width) / 2;
                    final int y = (mIconHeight - height) / 2;
                    icon.setBounds(x, y, x + width, y + height);
                    icon.draw(canvas);
                    icon.setBounds(mOldBounds);
                    icon = new android.graphics.drawable.BitmapDrawable(getResources(), thumb);
                    canvas.setBitmap(null);
                } else
                    if ((iconWidth < width) && (iconHeight < height)) {
                        final android.graphics.Bitmap.Config c = android.graphics.Bitmap.Config.ARGB_8888;
                        final android.graphics.Bitmap thumb = android.graphics.Bitmap.createBitmap(mIconWidth, mIconHeight, c);
                        final android.graphics.Canvas canvas = mCanvas;
                        canvas.setBitmap(thumb);
                        mOldBounds.set(icon.getBounds());
                        final int x = (width - iconWidth) / 2;
                        final int y = (height - iconHeight) / 2;
                        icon.setBounds(x, y, x + iconWidth, y + iconHeight);
                        icon.draw(canvas);
                        icon.setBounds(mOldBounds);
                        icon = new android.graphics.drawable.BitmapDrawable(getResources(), thumb);
                        canvas.setBitmap(null);
                    }

            }
            return icon;
        }
    }

    @java.lang.Override
    protected void onCreate(android.os.Bundle icicle) {
        super.onCreate(icicle);
        mPackageManager = getPackageManager();
        if (!mPackageManager.hasSystemFeature(android.content.pm.PackageManager.FEATURE_WATCH)) {
            requestWindowFeature(android.view.Window.FEATURE_INDETERMINATE_PROGRESS);
            setProgressBarIndeterminateVisibility(true);
        }
        onSetContentView();
        mIconResizer = new android.app.LauncherActivity.IconResizer();
        mIntent = new android.content.Intent(getTargetIntent());
        mIntent.setComponent(null);
        mAdapter = new android.app.LauncherActivity.ActivityAdapter(mIconResizer);
        setListAdapter(mAdapter);
        getListView().setTextFilterEnabled(true);
        updateAlertTitle();
        updateButtonText();
        if (!mPackageManager.hasSystemFeature(android.content.pm.PackageManager.FEATURE_WATCH)) {
            setProgressBarIndeterminateVisibility(false);
        }
    }

    private void updateAlertTitle() {
        android.widget.TextView alertTitle = ((android.widget.TextView) (findViewById(com.android.internal.R.id.alertTitle)));
        if (alertTitle != null) {
            alertTitle.setText(getTitle());
        }
    }

    private void updateButtonText() {
        android.widget.Button cancelButton = ((android.widget.Button) (findViewById(com.android.internal.R.id.button1)));
        if (cancelButton != null) {
            cancelButton.setOnClickListener(new android.view.View.OnClickListener() {
                public void onClick(android.view.View v) {
                    finish();
                }
            });
        }
    }

    @java.lang.Override
    public void setTitle(java.lang.CharSequence title) {
        super.setTitle(title);
        updateAlertTitle();
    }

    @java.lang.Override
    public void setTitle(int titleId) {
        super.setTitle(titleId);
        updateAlertTitle();
    }

    /**
     * Override to call setContentView() with your own content view to
     * customize the list layout.
     */
    protected void onSetContentView() {
        setContentView(com.android.internal.R.layout.activity_list);
    }

    @java.lang.Override
    protected void onListItemClick(android.widget.ListView l, android.view.View v, int position, long id) {
        android.content.Intent intent = intentForPosition(position);
        startActivity(intent);
    }

    /**
     * Return the actual Intent for a specific position in our
     * {@link android.widget.ListView}.
     *
     * @param position
     * 		The item whose Intent to return
     */
    protected android.content.Intent intentForPosition(int position) {
        android.app.LauncherActivity.ActivityAdapter adapter = ((android.app.LauncherActivity.ActivityAdapter) (mAdapter));
        return adapter.intentForPosition(position);
    }

    /**
     * Return the {@link ListItem} for a specific position in our
     * {@link android.widget.ListView}.
     *
     * @param position
     * 		The item to return
     */
    protected android.app.LauncherActivity.ListItem itemForPosition(int position) {
        android.app.LauncherActivity.ActivityAdapter adapter = ((android.app.LauncherActivity.ActivityAdapter) (mAdapter));
        return adapter.itemForPosition(position);
    }

    /**
     * Get the base intent to use when running
     * {@link PackageManager#queryIntentActivities(Intent, int)}.
     */
    protected android.content.Intent getTargetIntent() {
        return new android.content.Intent();
    }

    /**
     * Perform query on package manager for list items.  The default
     * implementation queries for activities.
     */
    protected java.util.List<android.content.pm.ResolveInfo> onQueryPackageManager(android.content.Intent queryIntent) {
        return /* no flags */
        mPackageManager.queryIntentActivities(queryIntent, 0);
    }

    /**
     *
     *
     * @unknown 
     */
    protected void onSortResultList(java.util.List<android.content.pm.ResolveInfo> results) {
        java.util.Collections.sort(results, new android.content.pm.ResolveInfo.DisplayNameComparator(mPackageManager));
    }

    /**
     * Perform the query to determine which results to show and return a list of them.
     */
    public java.util.List<android.app.LauncherActivity.ListItem> makeListItems() {
        // Load all matching activities and sort correctly
        java.util.List<android.content.pm.ResolveInfo> list = onQueryPackageManager(mIntent);
        onSortResultList(list);
        java.util.ArrayList<android.app.LauncherActivity.ListItem> result = new java.util.ArrayList<android.app.LauncherActivity.ListItem>(list.size());
        int listSize = list.size();
        for (int i = 0; i < listSize; i++) {
            android.content.pm.ResolveInfo resolveInfo = list.get(i);
            result.add(new android.app.LauncherActivity.ListItem(mPackageManager, resolveInfo, null));
        }
        return result;
    }

    /**
     * Whether or not to show icons in the list
     *
     * @unknown keeping this private for now, since only Settings needs it
     * @return true to show icons beside the activity names, false otherwise
     */
    protected boolean onEvaluateShowIcons() {
        return true;
    }
}

