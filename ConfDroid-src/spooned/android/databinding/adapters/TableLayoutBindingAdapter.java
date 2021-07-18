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
package android.databinding.adapters;


public class TableLayoutBindingAdapter {
    private static java.util.regex.Pattern sColumnPattern = java.util.regex.Pattern.compile("\\s*,\\s*");

    private static final int MAX_COLUMNS = 20;

    @android.databinding.BindingAdapter({ "android:collapseColumns" })
    public static void setCollapseColumns(android.widget.TableLayout view, java.lang.CharSequence columnsStr) {
        android.util.SparseBooleanArray columns = android.databinding.adapters.TableLayoutBindingAdapter.parseColumns(columnsStr);
        for (int i = 0; i < android.databinding.adapters.TableLayoutBindingAdapter.MAX_COLUMNS; i++) {
            boolean isCollapsed = columns.get(i, false);
            if (isCollapsed != view.isColumnCollapsed(i)) {
                view.setColumnCollapsed(i, isCollapsed);
            }
        }
    }

    @android.databinding.BindingAdapter({ "android:shrinkColumns" })
    public static void setShrinkColumns(android.widget.TableLayout view, java.lang.CharSequence columnsStr) {
        if (((columnsStr != null) && (columnsStr.length() > 0)) && (columnsStr.charAt(0) == '*')) {
            view.setShrinkAllColumns(true);
        } else {
            view.setShrinkAllColumns(false);
            android.util.SparseBooleanArray columns = android.databinding.adapters.TableLayoutBindingAdapter.parseColumns(columnsStr);
            int columnCount = columns.size();
            for (int i = 0; i < columnCount; i++) {
                int column = columns.keyAt(i);
                boolean shrinkable = columns.valueAt(i);
                if (shrinkable) {
                    view.setColumnShrinkable(column, shrinkable);
                }
            }
        }
    }

    @android.databinding.BindingAdapter({ "android:stretchColumns" })
    public static void setStretchColumns(android.widget.TableLayout view, java.lang.CharSequence columnsStr) {
        if (((columnsStr != null) && (columnsStr.length() > 0)) && (columnsStr.charAt(0) == '*')) {
            view.setStretchAllColumns(true);
        } else {
            view.setStretchAllColumns(false);
            android.util.SparseBooleanArray columns = android.databinding.adapters.TableLayoutBindingAdapter.parseColumns(columnsStr);
            int columnCount = columns.size();
            for (int i = 0; i < columnCount; i++) {
                int column = columns.keyAt(i);
                boolean stretchable = columns.valueAt(i);
                if (stretchable) {
                    view.setColumnStretchable(column, stretchable);
                }
            }
        }
    }

    private static android.util.SparseBooleanArray parseColumns(java.lang.CharSequence sequence) {
        android.util.SparseBooleanArray columns = new android.util.SparseBooleanArray();
        if (sequence == null) {
            return columns;
        }
        java.lang.String[] columnDefs = android.databinding.adapters.TableLayoutBindingAdapter.sColumnPattern.split(sequence);
        for (java.lang.String columnIdentifier : columnDefs) {
            try {
                int columnIndex = java.lang.Integer.parseInt(columnIdentifier);
                // only valid, i.e. positive, columns indexes are handled
                if (columnIndex >= 0) {
                    // putting true in this sparse array indicates that the
                    // column index was defined in the XML file
                    columns.put(columnIndex, true);
                }
            } catch (java.lang.NumberFormatException e) {
                // we just ignore columns that don't exist
            }
        }
        return columns;
    }
}

