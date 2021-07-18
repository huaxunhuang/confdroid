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
package android.support.v4.graphics.drawable;


class DrawableWrapperHoneycomb extends android.support.v4.graphics.drawable.DrawableWrapperGingerbread {
    DrawableWrapperHoneycomb(android.graphics.drawable.Drawable drawable) {
        super(drawable);
    }

    DrawableWrapperHoneycomb(android.support.v4.graphics.drawable.DrawableWrapperGingerbread.DrawableWrapperState state, android.content.res.Resources resources) {
        super(state, resources);
    }

    @java.lang.Override
    public void jumpToCurrentState() {
        mDrawable.jumpToCurrentState();
    }

    @android.support.annotation.NonNull
    @java.lang.Override
    android.support.v4.graphics.drawable.DrawableWrapperGingerbread.DrawableWrapperState mutateConstantState() {
        return new android.support.v4.graphics.drawable.DrawableWrapperHoneycomb.DrawableWrapperStateHoneycomb(mState, null);
    }

    private static class DrawableWrapperStateHoneycomb extends android.support.v4.graphics.drawable.DrawableWrapperGingerbread.DrawableWrapperState {
        DrawableWrapperStateHoneycomb(@android.support.annotation.Nullable
        android.support.v4.graphics.drawable.DrawableWrapperGingerbread.DrawableWrapperState orig, @android.support.annotation.Nullable
        android.content.res.Resources res) {
            super(orig, res);
        }

        @java.lang.Override
        public android.graphics.drawable.Drawable newDrawable(@android.support.annotation.Nullable
        android.content.res.Resources res) {
            return new android.support.v4.graphics.drawable.DrawableWrapperHoneycomb(this, res);
        }
    }
}

