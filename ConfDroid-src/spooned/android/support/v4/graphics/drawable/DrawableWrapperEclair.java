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
package android.support.v4.graphics.drawable;


class DrawableWrapperEclair extends android.support.v4.graphics.drawable.DrawableWrapperDonut {
    DrawableWrapperEclair(android.graphics.drawable.Drawable drawable) {
        super(drawable);
    }

    DrawableWrapperEclair(android.support.v4.graphics.drawable.DrawableWrapperDonut.DrawableWrapperState state, android.content.res.Resources resources) {
        super(state, resources);
    }

    @java.lang.Override
    android.support.v4.graphics.drawable.DrawableWrapperDonut.DrawableWrapperState mutateConstantState() {
        return new android.support.v4.graphics.drawable.DrawableWrapperEclair.DrawableWrapperStateEclair(mState, null);
    }

    @java.lang.Override
    protected android.graphics.drawable.Drawable newDrawableFromState(android.graphics.drawable.Drawable.ConstantState state, android.content.res.Resources res) {
        return state.newDrawable(res);
    }

    private static class DrawableWrapperStateEclair extends android.support.v4.graphics.drawable.DrawableWrapperDonut.DrawableWrapperState {
        DrawableWrapperStateEclair(@android.support.annotation.Nullable
        android.support.v4.graphics.drawable.DrawableWrapperDonut.DrawableWrapperState orig, @android.support.annotation.Nullable
        android.content.res.Resources res) {
            super(orig, res);
        }

        @java.lang.Override
        public android.graphics.drawable.Drawable newDrawable(@android.support.annotation.Nullable
        android.content.res.Resources res) {
            return new android.support.v4.graphics.drawable.DrawableWrapperEclair(this, res);
        }
    }
}

