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
 * ExpandableListPosition can refer to either a group's position or a child's
 * position. Referring to a child's position requires both a group position (the
 * group containing the child) and a child position (the child's position within
 * that group). To create objects, use {@link #obtainChildPosition(int, int)} or
 * {@link #obtainGroupPosition(int)}.
 */
class ExpandableListPosition {
    private static final int MAX_POOL_SIZE = 5;

    private static java.util.ArrayList<android.widget.ExpandableListPosition> sPool = new java.util.ArrayList<android.widget.ExpandableListPosition>(android.widget.ExpandableListPosition.MAX_POOL_SIZE);

    /**
     * This data type represents a child position
     */
    public static final int CHILD = 1;

    /**
     * This data type represents a group position
     */
    public static final int GROUP = 2;

    /**
     * The position of either the group being referred to, or the parent
     * group of the child being referred to
     */
    public int groupPos;

    /**
     * The position of the child within its parent group
     */
    public int childPos;

    /**
     * The position of the item in the flat list (optional, used internally when
     * the corresponding flat list position for the group or child is known)
     */
    int flatListPos;

    /**
     * What type of position this ExpandableListPosition represents
     */
    public int type;

    private void resetState() {
        groupPos = 0;
        childPos = 0;
        flatListPos = 0;
        type = 0;
    }

    private ExpandableListPosition() {
    }

    long getPackedPosition() {
        if (type == android.widget.ExpandableListPosition.CHILD)
            return android.widget.ExpandableListView.getPackedPositionForChild(groupPos, childPos);
        else
            return android.widget.ExpandableListView.getPackedPositionForGroup(groupPos);

    }

    static android.widget.ExpandableListPosition obtainGroupPosition(int groupPosition) {
        return android.widget.ExpandableListPosition.obtain(android.widget.ExpandableListPosition.GROUP, groupPosition, 0, 0);
    }

    static android.widget.ExpandableListPosition obtainChildPosition(int groupPosition, int childPosition) {
        return android.widget.ExpandableListPosition.obtain(android.widget.ExpandableListPosition.CHILD, groupPosition, childPosition, 0);
    }

    static android.widget.ExpandableListPosition obtainPosition(long packedPosition) {
        if (packedPosition == android.widget.ExpandableListView.PACKED_POSITION_VALUE_NULL) {
            return null;
        }
        android.widget.ExpandableListPosition elp = android.widget.ExpandableListPosition.getRecycledOrCreate();
        elp.groupPos = android.widget.ExpandableListView.getPackedPositionGroup(packedPosition);
        if (android.widget.ExpandableListView.getPackedPositionType(packedPosition) == android.widget.ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
            elp.type = android.widget.ExpandableListPosition.CHILD;
            elp.childPos = android.widget.ExpandableListView.getPackedPositionChild(packedPosition);
        } else {
            elp.type = android.widget.ExpandableListPosition.GROUP;
        }
        return elp;
    }

    static android.widget.ExpandableListPosition obtain(int type, int groupPos, int childPos, int flatListPos) {
        android.widget.ExpandableListPosition elp = android.widget.ExpandableListPosition.getRecycledOrCreate();
        elp.type = type;
        elp.groupPos = groupPos;
        elp.childPos = childPos;
        elp.flatListPos = flatListPos;
        return elp;
    }

    private static android.widget.ExpandableListPosition getRecycledOrCreate() {
        android.widget.ExpandableListPosition elp;
        synchronized(android.widget.ExpandableListPosition.sPool) {
            if (android.widget.ExpandableListPosition.sPool.size() > 0) {
                elp = android.widget.ExpandableListPosition.sPool.remove(0);
            } else {
                return new android.widget.ExpandableListPosition();
            }
        }
        elp.resetState();
        return elp;
    }

    /**
     * Do not call this unless you obtained this via ExpandableListPosition.obtain().
     * PositionMetadata will handle recycling its own children.
     */
    public void recycle() {
        synchronized(android.widget.ExpandableListPosition.sPool) {
            if (android.widget.ExpandableListPosition.sPool.size() < android.widget.ExpandableListPosition.MAX_POOL_SIZE) {
                android.widget.ExpandableListPosition.sPool.add(this);
            }
        }
    }
}

