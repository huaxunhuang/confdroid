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
 * A fairly simple ExpandableListAdapter that creates views defined in an XML
 * file. You can specify the XML file that defines the appearance of the views.
 */
public abstract class ResourceCursorTreeAdapter extends android.widget.CursorTreeAdapter {
    private int mCollapsedGroupLayout;

    private int mExpandedGroupLayout;

    private int mChildLayout;

    private int mLastChildLayout;

    private android.view.LayoutInflater mInflater;

    /**
     * Constructor.
     *
     * @param context
     * 		The context where the ListView associated with this
     * 		SimpleListItemFactory is running
     * @param cursor
     * 		The database cursor
     * @param collapsedGroupLayout
     * 		resource identifier of a layout file that
     * 		defines the views for collapsed groups.
     * @param expandedGroupLayout
     * 		resource identifier of a layout file that
     * 		defines the views for expanded groups.
     * @param childLayout
     * 		resource identifier of a layout file that defines the
     * 		views for all children but the last..
     * @param lastChildLayout
     * 		resource identifier of a layout file that defines
     * 		the views for the last child of a group.
     */
    public ResourceCursorTreeAdapter(android.content.Context context, android.database.Cursor cursor, int collapsedGroupLayout, int expandedGroupLayout, int childLayout, int lastChildLayout) {
        super(cursor, context);
        mCollapsedGroupLayout = collapsedGroupLayout;
        mExpandedGroupLayout = expandedGroupLayout;
        mChildLayout = childLayout;
        mLastChildLayout = lastChildLayout;
        mInflater = ((android.view.LayoutInflater) (context.getSystemService(android.content.Context.LAYOUT_INFLATER_SERVICE)));
    }

    /**
     * Constructor.
     *
     * @param context
     * 		The context where the ListView associated with this
     * 		SimpleListItemFactory is running
     * @param cursor
     * 		The database cursor
     * @param collapsedGroupLayout
     * 		resource identifier of a layout file that
     * 		defines the views for collapsed groups.
     * @param expandedGroupLayout
     * 		resource identifier of a layout file that
     * 		defines the views for expanded groups.
     * @param childLayout
     * 		resource identifier of a layout file that defines the
     * 		views for all children.
     */
    public ResourceCursorTreeAdapter(android.content.Context context, android.database.Cursor cursor, int collapsedGroupLayout, int expandedGroupLayout, int childLayout) {
        this(context, cursor, collapsedGroupLayout, expandedGroupLayout, childLayout, childLayout);
    }

    /**
     * Constructor.
     *
     * @param context
     * 		The context where the ListView associated with this
     * 		SimpleListItemFactory is running
     * @param cursor
     * 		The database cursor
     * @param groupLayout
     * 		resource identifier of a layout file that defines the
     * 		views for all groups.
     * @param childLayout
     * 		resource identifier of a layout file that defines the
     * 		views for all children.
     */
    public ResourceCursorTreeAdapter(android.content.Context context, android.database.Cursor cursor, int groupLayout, int childLayout) {
        this(context, cursor, groupLayout, groupLayout, childLayout, childLayout);
    }

    @java.lang.Override
    public android.view.View newChildView(android.content.Context context, android.database.Cursor cursor, boolean isLastChild, android.view.ViewGroup parent) {
        return mInflater.inflate(isLastChild ? mLastChildLayout : mChildLayout, parent, false);
    }

    @java.lang.Override
    public android.view.View newGroupView(android.content.Context context, android.database.Cursor cursor, boolean isExpanded, android.view.ViewGroup parent) {
        return mInflater.inflate(isExpanded ? mExpandedGroupLayout : mCollapsedGroupLayout, parent, false);
    }
}

