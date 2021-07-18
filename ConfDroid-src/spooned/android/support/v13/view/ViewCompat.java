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
package android.support.v13.view;


/**
 * Helper for accessing features in {@link View} introduced after API
 * level 13 in a backwards compatible fashion.
 */
public class ViewCompat extends android.support.v4.view.ViewCompat {
    interface ViewCompatImpl {
        boolean startDragAndDrop(android.view.View v, android.content.ClipData data, android.view.View.DragShadowBuilder shadowBuilder, java.lang.Object localState, int flags);

        void cancelDragAndDrop(android.view.View v);

        void updateDragShadow(android.view.View v, android.view.View.DragShadowBuilder shadowBuilder);
    }

    private static class BaseViewCompatImpl implements android.support.v13.view.ViewCompat.ViewCompatImpl {
        BaseViewCompatImpl() {
        }

        @java.lang.Override
        public boolean startDragAndDrop(android.view.View v, android.content.ClipData data, android.view.View.DragShadowBuilder shadowBuilder, java.lang.Object localState, int flags) {
            return v.startDrag(data, shadowBuilder, localState, flags);
        }

        @java.lang.Override
        public void cancelDragAndDrop(android.view.View v) {
            // no-op
        }

        @java.lang.Override
        public void updateDragShadow(android.view.View v, android.view.View.DragShadowBuilder shadowBuilder) {
            // no-op
        }
    }

    private static class Api24ViewCompatImpl implements android.support.v13.view.ViewCompat.ViewCompatImpl {
        Api24ViewCompatImpl() {
        }

        @java.lang.Override
        public boolean startDragAndDrop(android.view.View v, android.content.ClipData data, android.view.View.DragShadowBuilder shadowBuilder, java.lang.Object localState, int flags) {
            return android.support.v13.view.ViewCompatApi24.startDragAndDrop(v, data, shadowBuilder, localState, flags);
        }

        @java.lang.Override
        public void cancelDragAndDrop(android.view.View v) {
            android.support.v13.view.ViewCompatApi24.cancelDragAndDrop(v);
        }

        @java.lang.Override
        public void updateDragShadow(android.view.View v, android.view.View.DragShadowBuilder shadowBuilder) {
            android.support.v13.view.ViewCompatApi24.updateDragShadow(v, shadowBuilder);
        }
    }

    static android.support.v13.view.ViewCompat.ViewCompatImpl IMPL;

    static {
        if (android.support.v4.os.BuildCompat.isAtLeastN()) {
            android.support.v13.view.ViewCompat.IMPL = new android.support.v13.view.ViewCompat.Api24ViewCompatImpl();
        } else {
            android.support.v13.view.ViewCompat.IMPL = new android.support.v13.view.ViewCompat.BaseViewCompatImpl();
        }
    }

    /**
     * Start the drag and drop operation.
     */
    public static boolean startDragAndDrop(android.view.View v, android.content.ClipData data, android.view.View.DragShadowBuilder shadowBuilder, java.lang.Object localState, int flags) {
        return android.support.v13.view.ViewCompat.IMPL.startDragAndDrop(v, data, shadowBuilder, localState, flags);
    }

    /**
     * Cancel the drag and drop operation.
     */
    public static void cancelDragAndDrop(android.view.View v) {
        android.support.v13.view.ViewCompat.IMPL.cancelDragAndDrop(v);
    }

    /**
     * Update the drag shadow while drag and drop is in progress.
     */
    public static void updateDragShadow(android.view.View v, android.view.View.DragShadowBuilder shadowBuilder) {
        android.support.v13.view.ViewCompat.IMPL.updateDragShadow(v, shadowBuilder);
    }

    private ViewCompat() {
    }
}

