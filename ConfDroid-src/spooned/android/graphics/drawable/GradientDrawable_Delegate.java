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
package android.graphics.drawable;


/**
 * Delegate implementing the native methods of {@link GradientDrawable}
 *
 * Through the layoutlib_create tool, the original native methods of GradientDrawable have been
 * replaced by calls to methods of the same name in this delegate class.
 */
public class GradientDrawable_Delegate {
    /**
     * The ring can be built either by drawing full circles, or by drawing arcs in case the
     * circle isn't complete. LayoutLib cannot handle drawing full circles (requires path
     * subtraction). So, if we need to draw full circles, we switch to drawing 99% circle.
     */
    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static android.graphics.Path buildRing(android.graphics.drawable.GradientDrawable thisDrawable, android.graphics.drawable.GradientDrawable.GradientState st) {
        boolean useLevel = st.mUseLevelForShape;
        int level = thisDrawable.getLevel();
        // 10000 is the max level. See android.graphics.drawable.Drawable#getLevel()
        float sweep = (useLevel) ? (360.0F * level) / 10000.0F : 360.0F;
        java.lang.reflect.Field mLevel = null;
        if ((sweep >= 360) || (sweep <= (-360))) {
            st.mUseLevelForShape = true;
            // Use reflection to set the value of the field to prevent setting the drawable to
            // dirty again.
            try {
                mLevel = android.graphics.drawable.Drawable.class.getDeclaredField("mLevel");
                mLevel.setAccessible(true);
                mLevel.setInt(thisDrawable, 9999);// set to one less than max.

            } catch (java.lang.NoSuchFieldException e) {
                // The field has been removed in a recent framework change. Fall back to old
                // buggy behaviour.
            } catch (java.lang.IllegalAccessException e) {
                // We've already set the field to be accessible.
                assert false;
            }
        }
        android.graphics.Path path = thisDrawable.buildRing_Original(st);
        st.mUseLevelForShape = useLevel;
        if (mLevel != null) {
            try {
                mLevel.setInt(thisDrawable, level);
            } catch (java.lang.IllegalAccessException e) {
                assert false;
            }
        }
        return path;
    }
}

