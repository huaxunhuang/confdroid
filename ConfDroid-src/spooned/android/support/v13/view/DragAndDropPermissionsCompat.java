/**
 * Copyright (C) 2016 The Android Open Source Project
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
package android.support.v13.view;


/**
 * Helper for accessing features in {@link android.view.DragAndDropPermissions}
 * introduced after API level 13 in a backwards compatible fashion.
 */
public final class DragAndDropPermissionsCompat {
    interface DragAndDropPermissionsCompatImpl {
        java.lang.Object request(android.app.Activity activity, android.view.DragEvent dragEvent);

        void release(java.lang.Object dragAndDropPermissions);
    }

    static class BaseDragAndDropPermissionsCompatImpl implements android.support.v13.view.DragAndDropPermissionsCompat.DragAndDropPermissionsCompatImpl {
        @java.lang.Override
        public java.lang.Object request(android.app.Activity activity, android.view.DragEvent dragEvent) {
            return null;
        }

        @java.lang.Override
        public void release(java.lang.Object dragAndDropPermissions) {
            // no-op
        }
    }

    static class Api24DragAndDropPermissionsCompatImpl extends android.support.v13.view.DragAndDropPermissionsCompat.BaseDragAndDropPermissionsCompatImpl {
        @java.lang.Override
        public java.lang.Object request(android.app.Activity activity, android.view.DragEvent dragEvent) {
            return android.support.v13.view.DragAndDropPermissionsCompatApi24.request(activity, dragEvent);
        }

        @java.lang.Override
        public void release(java.lang.Object dragAndDropPermissions) {
            android.support.v13.view.DragAndDropPermissionsCompatApi24.release(dragAndDropPermissions);
        }
    }

    private static android.support.v13.view.DragAndDropPermissionsCompat.DragAndDropPermissionsCompatImpl IMPL;

    static {
        if (android.support.v4.os.BuildCompat.isAtLeastN()) {
            android.support.v13.view.DragAndDropPermissionsCompat.IMPL = new android.support.v13.view.DragAndDropPermissionsCompat.Api24DragAndDropPermissionsCompatImpl();
        } else {
            android.support.v13.view.DragAndDropPermissionsCompat.IMPL = new android.support.v13.view.DragAndDropPermissionsCompat.BaseDragAndDropPermissionsCompatImpl();
        }
    }

    private java.lang.Object mDragAndDropPermissions;

    private DragAndDropPermissionsCompat(java.lang.Object dragAndDropPermissions) {
        mDragAndDropPermissions = dragAndDropPermissions;
    }

    /**
     *
     *
     * @unknown 
     */
    @android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
    public static android.support.v13.view.DragAndDropPermissionsCompat request(android.app.Activity activity, android.view.DragEvent dragEvent) {
        java.lang.Object dragAndDropPermissions = android.support.v13.view.DragAndDropPermissionsCompat.IMPL.request(activity, dragEvent);
        if (dragAndDropPermissions != null) {
            return new android.support.v13.view.DragAndDropPermissionsCompat(dragAndDropPermissions);
        }
        return null;
    }

    /* Revoke the permission grant explicitly. */
    public void release() {
        android.support.v13.view.DragAndDropPermissionsCompat.IMPL.release(mDragAndDropPermissions);
    }
}

