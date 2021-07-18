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
package android.support.v4.graphics.drawable;


class RoundedBitmapDrawable21 extends android.support.v4.graphics.drawable.RoundedBitmapDrawable {
    protected RoundedBitmapDrawable21(android.content.res.Resources res, android.graphics.Bitmap bitmap) {
        super(res, bitmap);
    }

    @java.lang.Override
    public void getOutline(android.graphics.Outline outline) {
        updateDstRect();
        outline.setRoundRect(mDstRect, getCornerRadius());
    }

    @java.lang.Override
    public void setMipMap(boolean mipMap) {
        if (mBitmap != null) {
            mBitmap.setHasMipMap(mipMap);
            invalidateSelf();
        }
    }

    @java.lang.Override
    public boolean hasMipMap() {
        return (mBitmap != null) && mBitmap.hasMipMap();
    }

    @java.lang.Override
    void gravityCompatApply(int gravity, int bitmapWidth, int bitmapHeight, android.graphics.Rect bounds, android.graphics.Rect outRect) {
        android.view.Gravity.apply(gravity, bitmapWidth, bitmapHeight, bounds, outRect, android.view.View.LAYOUT_DIRECTION_LTR);
    }
}

