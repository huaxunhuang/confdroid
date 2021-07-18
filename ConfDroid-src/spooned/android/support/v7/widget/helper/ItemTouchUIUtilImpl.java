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
package android.support.v7.widget.helper;


/**
 * Package private class to keep implementations. Putting them inside ItemTouchUIUtil makes them
 * public API, which is not desired in this case.
 */
class ItemTouchUIUtilImpl {
    static class Lollipop extends android.support.v7.widget.helper.ItemTouchUIUtilImpl.Honeycomb {
        @java.lang.Override
        public void onDraw(android.graphics.Canvas c, android.support.v7.widget.RecyclerView recyclerView, android.view.View view, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            if (isCurrentlyActive) {
                java.lang.Object originalElevation = view.getTag(R.id.item_touch_helper_previous_elevation);
                if (originalElevation == null) {
                    originalElevation = android.support.v4.view.ViewCompat.getElevation(view);
                    float newElevation = 1.0F + findMaxElevation(recyclerView, view);
                    android.support.v4.view.ViewCompat.setElevation(view, newElevation);
                    view.setTag(R.id.item_touch_helper_previous_elevation, originalElevation);
                }
            }
            super.onDraw(c, recyclerView, view, dX, dY, actionState, isCurrentlyActive);
        }

        private float findMaxElevation(android.support.v7.widget.RecyclerView recyclerView, android.view.View itemView) {
            final int childCount = recyclerView.getChildCount();
            float max = 0;
            for (int i = 0; i < childCount; i++) {
                final android.view.View child = recyclerView.getChildAt(i);
                if (child == itemView) {
                    continue;
                }
                final float elevation = android.support.v4.view.ViewCompat.getElevation(child);
                if (elevation > max) {
                    max = elevation;
                }
            }
            return max;
        }

        @java.lang.Override
        public void clearView(android.view.View view) {
            final java.lang.Object tag = view.getTag(R.id.item_touch_helper_previous_elevation);
            if ((tag != null) && (tag instanceof java.lang.Float)) {
                android.support.v4.view.ViewCompat.setElevation(view, ((java.lang.Float) (tag)));
            }
            view.setTag(R.id.item_touch_helper_previous_elevation, null);
            super.clearView(view);
        }
    }

    static class Honeycomb implements android.support.v7.widget.helper.ItemTouchUIUtil {
        @java.lang.Override
        public void clearView(android.view.View view) {
            android.support.v4.view.ViewCompat.setTranslationX(view, 0.0F);
            android.support.v4.view.ViewCompat.setTranslationY(view, 0.0F);
        }

        @java.lang.Override
        public void onSelected(android.view.View view) {
        }

        @java.lang.Override
        public void onDraw(android.graphics.Canvas c, android.support.v7.widget.RecyclerView recyclerView, android.view.View view, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            android.support.v4.view.ViewCompat.setTranslationX(view, dX);
            android.support.v4.view.ViewCompat.setTranslationY(view, dY);
        }

        @java.lang.Override
        public void onDrawOver(android.graphics.Canvas c, android.support.v7.widget.RecyclerView recyclerView, android.view.View view, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        }
    }

    static class Gingerbread implements android.support.v7.widget.helper.ItemTouchUIUtil {
        private void draw(android.graphics.Canvas c, android.support.v7.widget.RecyclerView parent, android.view.View view, float dX, float dY) {
            c.save();
            c.translate(dX, dY);
            parent.drawChild(c, view, 0);
            c.restore();
        }

        @java.lang.Override
        public void clearView(android.view.View view) {
            view.setVisibility(android.view.View.VISIBLE);
        }

        @java.lang.Override
        public void onSelected(android.view.View view) {
            view.setVisibility(android.view.View.INVISIBLE);
        }

        @java.lang.Override
        public void onDraw(android.graphics.Canvas c, android.support.v7.widget.RecyclerView recyclerView, android.view.View view, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            if (actionState != android.support.v7.widget.helper.ItemTouchHelper.ACTION_STATE_DRAG) {
                draw(c, recyclerView, view, dX, dY);
            }
        }

        @java.lang.Override
        public void onDrawOver(android.graphics.Canvas c, android.support.v7.widget.RecyclerView recyclerView, android.view.View view, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            if (actionState == android.support.v7.widget.helper.ItemTouchHelper.ACTION_STATE_DRAG) {
                draw(c, recyclerView, view, dX, dY);
            }
        }
    }
}

