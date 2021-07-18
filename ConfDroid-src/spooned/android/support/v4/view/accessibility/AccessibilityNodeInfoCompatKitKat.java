/**
 * Copyright (C) 2012 The Android Open Source Project
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
package android.support.v4.view.accessibility;


/**
 * KitKat-specific AccessibilityNodeInfo API implementation.
 */
class AccessibilityNodeInfoCompatKitKat {
    private static final byte TRAIT_UNSET = -1;

    private static final java.lang.String TRAITS_KEY = "android.view.accessibility.AccessibilityNodeInfo.traits";

    private static final long TRAIT_HAS_IMAGE = 0x1;

    private static final java.lang.String ROLE_DESCRIPTION_KEY = "AccessibilityNodeInfo.roleDescription";

    static int getLiveRegion(java.lang.Object info) {
        return ((android.view.accessibility.AccessibilityNodeInfo) (info)).getLiveRegion();
    }

    static void setLiveRegion(java.lang.Object info, int mode) {
        ((android.view.accessibility.AccessibilityNodeInfo) (info)).setLiveRegion(mode);
    }

    static java.lang.Object getCollectionInfo(java.lang.Object info) {
        return ((android.view.accessibility.AccessibilityNodeInfo) (info)).getCollectionInfo();
    }

    static java.lang.Object getCollectionItemInfo(java.lang.Object info) {
        return ((android.view.accessibility.AccessibilityNodeInfo) (info)).getCollectionItemInfo();
    }

    public static void setCollectionInfo(java.lang.Object info, java.lang.Object collectionInfo) {
        ((android.view.accessibility.AccessibilityNodeInfo) (info)).setCollectionInfo(((android.view.accessibility.AccessibilityNodeInfo.CollectionInfo) (collectionInfo)));
    }

    public static void setCollectionItemInfo(java.lang.Object info, java.lang.Object collectionItemInfo) {
        ((android.view.accessibility.AccessibilityNodeInfo) (info)).setCollectionItemInfo(((android.view.accessibility.AccessibilityNodeInfo.CollectionItemInfo) (collectionItemInfo)));
    }

    static java.lang.Object getRangeInfo(java.lang.Object info) {
        return ((android.view.accessibility.AccessibilityNodeInfo) (info)).getRangeInfo();
    }

    public static void setRangeInfo(java.lang.Object info, java.lang.Object rangeInfo) {
        ((android.view.accessibility.AccessibilityNodeInfo) (info)).setRangeInfo(((android.view.accessibility.AccessibilityNodeInfo.RangeInfo) (rangeInfo)));
    }

    public static java.lang.Object obtainCollectionInfo(int rowCount, int columnCount, boolean hierarchical, int selectionMode) {
        return android.view.accessibility.AccessibilityNodeInfo.CollectionInfo.obtain(rowCount, columnCount, hierarchical);
    }

    public static java.lang.Object obtainCollectionInfo(int rowCount, int columnCount, boolean hierarchical) {
        return android.view.accessibility.AccessibilityNodeInfo.CollectionInfo.obtain(rowCount, columnCount, hierarchical);
    }

    public static java.lang.Object obtainCollectionItemInfo(int rowIndex, int rowSpan, int columnIndex, int columnSpan, boolean heading) {
        return android.view.accessibility.AccessibilityNodeInfo.CollectionItemInfo.obtain(rowIndex, rowSpan, columnIndex, columnSpan, heading);
    }

    public static void setContentInvalid(java.lang.Object info, boolean contentInvalid) {
        ((android.view.accessibility.AccessibilityNodeInfo) (info)).setContentInvalid(contentInvalid);
    }

    public static boolean isContentInvalid(java.lang.Object info) {
        return ((android.view.accessibility.AccessibilityNodeInfo) (info)).isContentInvalid();
    }

    public static boolean canOpenPopup(java.lang.Object info) {
        return ((android.view.accessibility.AccessibilityNodeInfo) (info)).canOpenPopup();
    }

    public static void setCanOpenPopup(java.lang.Object info, boolean opensPopup) {
        ((android.view.accessibility.AccessibilityNodeInfo) (info)).setCanOpenPopup(opensPopup);
    }

    public static android.os.Bundle getExtras(java.lang.Object info) {
        return ((android.view.accessibility.AccessibilityNodeInfo) (info)).getExtras();
    }

    private static long getTraits(java.lang.Object info) {
        return android.support.v4.view.accessibility.AccessibilityNodeInfoCompatKitKat.getExtras(info).getLong(android.support.v4.view.accessibility.AccessibilityNodeInfoCompatKitKat.TRAITS_KEY, android.support.v4.view.accessibility.AccessibilityNodeInfoCompatKitKat.TRAIT_UNSET);
    }

    private static void setTrait(java.lang.Object info, long trait) {
        android.os.Bundle extras = android.support.v4.view.accessibility.AccessibilityNodeInfoCompatKitKat.getExtras(info);
        long traits = extras.getLong(android.support.v4.view.accessibility.AccessibilityNodeInfoCompatKitKat.TRAITS_KEY, 0);
        extras.putLong(android.support.v4.view.accessibility.AccessibilityNodeInfoCompatKitKat.TRAITS_KEY, traits | trait);
    }

    public static int getInputType(java.lang.Object info) {
        return ((android.view.accessibility.AccessibilityNodeInfo) (info)).getInputType();
    }

    public static void setInputType(java.lang.Object info, int inputType) {
        ((android.view.accessibility.AccessibilityNodeInfo) (info)).setInputType(inputType);
    }

    public static boolean isDismissable(java.lang.Object info) {
        return ((android.view.accessibility.AccessibilityNodeInfo) (info)).isDismissable();
    }

    public static void setDismissable(java.lang.Object info, boolean dismissable) {
        ((android.view.accessibility.AccessibilityNodeInfo) (info)).setDismissable(dismissable);
    }

    public static boolean isMultiLine(java.lang.Object info) {
        return ((android.view.accessibility.AccessibilityNodeInfo) (info)).isMultiLine();
    }

    public static void setMultiLine(java.lang.Object info, boolean multiLine) {
        ((android.view.accessibility.AccessibilityNodeInfo) (info)).setMultiLine(multiLine);
    }

    public static java.lang.CharSequence getRoleDescription(java.lang.Object info) {
        android.os.Bundle extras = android.support.v4.view.accessibility.AccessibilityNodeInfoCompatKitKat.getExtras(info);
        return extras.getCharSequence(android.support.v4.view.accessibility.AccessibilityNodeInfoCompatKitKat.ROLE_DESCRIPTION_KEY);
    }

    public static void setRoleDescription(java.lang.Object info, java.lang.CharSequence roleDescription) {
        android.os.Bundle extras = android.support.v4.view.accessibility.AccessibilityNodeInfoCompatKitKat.getExtras(info);
        extras.putCharSequence(android.support.v4.view.accessibility.AccessibilityNodeInfoCompatKitKat.ROLE_DESCRIPTION_KEY, roleDescription);
    }

    public static java.lang.Object obtainRangeInfo(int type, float min, float max, float current) {
        return android.view.accessibility.AccessibilityNodeInfo.RangeInfo.obtain(type, min, max, current);
    }

    static class CollectionInfo {
        static int getColumnCount(java.lang.Object info) {
            return ((android.view.accessibility.AccessibilityNodeInfo.CollectionInfo) (info)).getColumnCount();
        }

        static int getRowCount(java.lang.Object info) {
            return ((android.view.accessibility.AccessibilityNodeInfo.CollectionInfo) (info)).getRowCount();
        }

        static boolean isHierarchical(java.lang.Object info) {
            return ((android.view.accessibility.AccessibilityNodeInfo.CollectionInfo) (info)).isHierarchical();
        }
    }

    static class CollectionItemInfo {
        static int getColumnIndex(java.lang.Object info) {
            return ((android.view.accessibility.AccessibilityNodeInfo.CollectionItemInfo) (info)).getColumnIndex();
        }

        static int getColumnSpan(java.lang.Object info) {
            return ((android.view.accessibility.AccessibilityNodeInfo.CollectionItemInfo) (info)).getColumnSpan();
        }

        static int getRowIndex(java.lang.Object info) {
            return ((android.view.accessibility.AccessibilityNodeInfo.CollectionItemInfo) (info)).getRowIndex();
        }

        static int getRowSpan(java.lang.Object info) {
            return ((android.view.accessibility.AccessibilityNodeInfo.CollectionItemInfo) (info)).getRowSpan();
        }

        static boolean isHeading(java.lang.Object info) {
            return ((android.view.accessibility.AccessibilityNodeInfo.CollectionItemInfo) (info)).isHeading();
        }
    }

    static class RangeInfo {
        static float getCurrent(java.lang.Object info) {
            return ((android.view.accessibility.AccessibilityNodeInfo.RangeInfo) (info)).getCurrent();
        }

        static float getMax(java.lang.Object info) {
            return ((android.view.accessibility.AccessibilityNodeInfo.RangeInfo) (info)).getMax();
        }

        static float getMin(java.lang.Object info) {
            return ((android.view.accessibility.AccessibilityNodeInfo.RangeInfo) (info)).getMin();
        }

        static int getType(java.lang.Object info) {
            return ((android.view.accessibility.AccessibilityNodeInfo.RangeInfo) (info)).getType();
        }
    }
}

