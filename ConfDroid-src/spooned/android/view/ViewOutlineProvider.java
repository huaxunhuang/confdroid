/**
 * Copyright (C) 2014 The Android Open Source Project
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
package android.view;


/**
 * Interface by which a View builds its {@link Outline}, used for shadow casting and clipping.
 */
public abstract class ViewOutlineProvider {
    /**
     * Default outline provider for Views, which queries the Outline from the View's background,
     * or generates a 0 alpha, rectangular Outline the size of the View if a background
     * isn't present.
     *
     * @see Drawable#getOutline(Outline)
     */
    public static final android.view.ViewOutlineProvider BACKGROUND = new android.view.ViewOutlineProvider() {
        @java.lang.Override
        public void getOutline(android.view.View view, android.graphics.Outline outline) {
            android.graphics.drawable.Drawable background = view.getBackground();
            if (background != null) {
                background.getOutline(outline);
            } else {
                outline.setRect(0, 0, view.getWidth(), view.getHeight());
                outline.setAlpha(0.0F);
            }
        }
    };

    /**
     * Maintains the outline of the View to match its rectangular bounds,
     * at <code>1.0f</code> alpha.
     *
     * This can be used to enable Views that are opaque but lacking a background cast a shadow.
     */
    public static final android.view.ViewOutlineProvider BOUNDS = new android.view.ViewOutlineProvider() {
        @java.lang.Override
        public void getOutline(android.view.View view, android.graphics.Outline outline) {
            outline.setRect(0, 0, view.getWidth(), view.getHeight());
        }
    };

    /**
     * Maintains the outline of the View to match its rectangular padded bounds,
     * at <code>1.0f</code> alpha.
     *
     * This can be used to enable Views that are opaque but lacking a background cast a shadow.
     */
    public static final android.view.ViewOutlineProvider PADDED_BOUNDS = new android.view.ViewOutlineProvider() {
        @java.lang.Override
        public void getOutline(android.view.View view, android.graphics.Outline outline) {
            outline.setRect(view.getPaddingLeft(), view.getPaddingTop(), view.getWidth() - view.getPaddingRight(), view.getHeight() - view.getPaddingBottom());
        }
    };

    /**
     * Called to get the provider to populate the Outline.
     *
     * This method will be called by a View when its owned Drawables are invalidated, when the
     * View's size changes, or if {@link View#invalidateOutline()} is called
     * explicitly.
     *
     * The input outline is empty and has an alpha of <code>1.0f</code>.
     *
     * @param view
     * 		The view building the outline.
     * @param outline
     * 		The empty outline to be populated.
     */
    public abstract void getOutline(android.view.View view, android.graphics.Outline outline);
}

