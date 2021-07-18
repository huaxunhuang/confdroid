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
package android.support.v7.app;


final class MediaRouteDialogHelper {
    /**
     * The framework should set the dialog width properly, but somehow it doesn't work, hence
     * duplicating a similar logic here to determine the appropriate dialog width.
     */
    public static int getDialogWidth(android.content.Context context) {
        android.util.DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        boolean isPortrait = metrics.widthPixels < metrics.heightPixels;
        android.util.TypedValue value = new android.util.TypedValue();
        context.getResources().getValue(isPortrait ? R.dimen.mr_dialog_fixed_width_minor : R.dimen.mr_dialog_fixed_width_major, value, true);
        if (value.type == android.util.TypedValue.TYPE_DIMENSION) {
            return ((int) (value.getDimension(metrics)));
        } else
            if (value.type == android.util.TypedValue.TYPE_FRACTION) {
                return ((int) (value.getFraction(metrics.widthPixels, metrics.widthPixels)));
            }

        return android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
    }

    /**
     * Compares two lists regardless of order.
     *
     * @param list1
     * 		A list
     * @param list2
     * 		A list to be compared with {@code list1}
     * @return True if two lists have exactly same items regardless of order, false otherwise.
     */
    public static <E> boolean listUnorderedEquals(java.util.List<E> list1, java.util.List<E> list2) {
        java.util.HashSet<E> set1 = new java.util.HashSet<>(list1);
        java.util.HashSet<E> set2 = new java.util.HashSet<>(list2);
        return set1.equals(set2);
    }

    /**
     * Compares two lists and returns a set of items which exist
     * after-list but before-list, which means newly added items.
     *
     * @param before
     * 		A list
     * @param after
     * 		A list to be compared with {@code before}
     * @return A set of items which contains newly added items while
    comparing {@code after} to {@code before}.
     */
    public static <E> java.util.Set<E> getItemsAdded(java.util.List<E> before, java.util.List<E> after) {
        java.util.HashSet<E> set = new java.util.HashSet<>(after);
        set.removeAll(before);
        return set;
    }

    /**
     * Compares two lists and returns a set of items which exist
     * before-list but after-list, which means removed items.
     *
     * @param before
     * 		A list
     * @param after
     * 		A list to be compared with {@code before}
     * @return A set of items which contains removed items while
    comparing {@code after} to {@code before}.
     */
    public static <E> java.util.Set<E> getItemsRemoved(java.util.List<E> before, java.util.List<E> after) {
        java.util.HashSet<E> set = new java.util.HashSet<>(before);
        set.removeAll(after);
        return set;
    }

    /**
     * Generates an item-Rect map which indicates where member
     * items are located in the given ListView.
     *
     * @param listView
     * 		A list view
     * @param adapter
     * 		An array adapter which contains an array of items.
     * @return A map of items and bounds of their views located in the given list view.
     */
    public static <E> java.util.HashMap<E, android.graphics.Rect> getItemBoundMap(android.widget.ListView listView, android.widget.ArrayAdapter<E> adapter) {
        java.util.HashMap<E, android.graphics.Rect> itemBoundMap = new java.util.HashMap<>();
        int firstVisiblePosition = listView.getFirstVisiblePosition();
        for (int i = 0; i < listView.getChildCount(); ++i) {
            int position = firstVisiblePosition + i;
            E item = adapter.getItem(position);
            android.view.View view = listView.getChildAt(i);
            itemBoundMap.put(item, new android.graphics.Rect(view.getLeft(), view.getTop(), view.getRight(), view.getBottom()));
        }
        return itemBoundMap;
    }

    /**
     * Generates an item-BitmapDrawable map which stores snapshots
     * of member items in the given ListView.
     *
     * @param context
     * 		A context
     * @param listView
     * 		A list view
     * @param adapter
     * 		An array adapter which contains an array of items.
     * @return A map of items and snapshots of their views in the given list view.
     */
    public static <E> java.util.HashMap<E, android.graphics.drawable.BitmapDrawable> getItemBitmapMap(android.content.Context context, android.widget.ListView listView, android.widget.ArrayAdapter<E> adapter) {
        java.util.HashMap<E, android.graphics.drawable.BitmapDrawable> itemBitmapMap = new java.util.HashMap<>();
        int firstVisiblePosition = listView.getFirstVisiblePosition();
        for (int i = 0; i < listView.getChildCount(); ++i) {
            int position = firstVisiblePosition + i;
            E item = adapter.getItem(position);
            android.view.View view = listView.getChildAt(i);
            itemBitmapMap.put(item, android.support.v7.app.MediaRouteDialogHelper.getViewBitmap(context, view));
        }
        return itemBitmapMap;
    }

    private static android.graphics.drawable.BitmapDrawable getViewBitmap(android.content.Context context, android.view.View view) {
        android.graphics.Bitmap bitmap = android.graphics.Bitmap.createBitmap(view.getWidth(), view.getHeight(), android.graphics.Bitmap.Config.ARGB_8888);
        android.graphics.Canvas canvas = new android.graphics.Canvas(bitmap);
        view.draw(canvas);
        return new android.graphics.drawable.BitmapDrawable(context.getResources(), bitmap);
    }
}

