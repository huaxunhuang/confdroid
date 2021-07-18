/**
 * Copyright (C) 2010 The Android Open Source Project
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
package android.graphics;


/**
 * Delegate implementing the native methods of android.graphics.PorterDuffColorFilter
 *
 * Through the layoutlib_create tool, the original native methods of PorterDuffColorFilter have
 * been replaced by calls to methods of the same name in this delegate class.
 *
 * This class behaves like the original native implementation, but in Java, keeping previously
 * native data into its own objects and mapping them to int that are sent back and forth between
 * it and the original PorterDuffColorFilter class.
 *
 * Because this extends {@link ColorFilter_Delegate}, there's no need to use a
 * {@link DelegateManager}, as all the Shader classes will be added to the manager
 * owned by {@link ColorFilter_Delegate}.
 *
 * @see ColorFilter_Delegate
 */
public class PorterDuffColorFilter_Delegate extends android.graphics.ColorFilter_Delegate {
    // ---- delegate data ----
    private final java.awt.Color mSrcColor;

    private final android.graphics.PorterDuff.Mode mMode;

    // ---- Public Helper methods ----
    @java.lang.Override
    public boolean isSupported() {
        return true;
    }

    @java.lang.Override
    public java.lang.String getSupportMessage() {
        return ("PorterDuff Color Filter is not supported for mode: " + mMode.name()) + ".";
    }

    @java.lang.Override
    public void applyFilter(java.awt.Graphics2D g, int width, int height) {
        g.setComposite(getComposite(mMode, 0xff));
        g.setColor(mSrcColor);
        g.fillRect(0, 0, width, height);
    }

    // ---- native methods ----
    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static long native_CreateBlendModeFilter(int srcColor, int porterDuffMode) {
        android.graphics.PorterDuffColorFilter_Delegate newDelegate = new android.graphics.PorterDuffColorFilter_Delegate(srcColor, porterDuffMode);
        return android.graphics.ColorFilter_Delegate.sManager.addNewDelegate(newDelegate);
    }

    // ---- Private delegate/helper methods ----
    private PorterDuffColorFilter_Delegate(int srcColor, int mode) {
        mSrcColor = /* hasAlpha */
        new java.awt.Color(srcColor, true);
        mMode = getCompatibleMode(getPorterDuffMode(mode));
    }

    // For filtering the colors, the src image should contain the "color" only for pixel values
    // which are not transparent in the target image. But, we are using a simple rectangular image
    // completely filled with color. Hence some Composite rules do not apply as intended. However,
    // in such cases, they can usually be mapped to some other mode, which produces an approximately
    // equivalent result.
    private android.graphics.PorterDuff.Mode getCompatibleMode(android.graphics.PorterDuff.Mode mode) {
        android.graphics.PorterDuff.Mode m = mode;
        // Modes that are directly supported:
        // CLEAR, DST, SRC_IN, DST_IN, DST_OUT, SRC_ATOP, DARKEN, LIGHTEN, MULTIPLY, SCREEN,
        // ADD, OVERLAY
        switch (mode) {
            // Modes that can be mapped to one of the supported modes.
            case SRC :
                m = android.graphics.PorterDuff.Mode.SRC_IN;
                break;
            case SRC_OVER :
                m = android.graphics.PorterDuff.Mode.SRC_ATOP;
                break;
            case DST_OVER :
                m = android.graphics.PorterDuff.Mode.DST;
                break;
            case SRC_OUT :
                m = android.graphics.PorterDuff.Mode.CLEAR;
                break;
            case DST_ATOP :
                m = android.graphics.PorterDuff.Mode.DST_IN;
                break;
            case XOR :
                m = android.graphics.PorterDuff.Mode.DST_OUT;
                break;
        }
        return m;
    }
}

