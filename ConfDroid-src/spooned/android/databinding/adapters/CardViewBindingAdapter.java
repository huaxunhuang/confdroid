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
package android.databinding.adapters;


@android.databinding.BindingMethods({ @android.databinding.BindingMethod(type = android.support.v7.widget.CardView.class, attribute = "cardCornerRadius", method = "setRadius"), @android.databinding.BindingMethod(type = android.support.v7.widget.CardView.class, attribute = "cardMaxElevation", method = "setMaxCardElevation"), @android.databinding.BindingMethod(type = android.support.v7.widget.CardView.class, attribute = "cardPreventCornerOverlap", method = "setPreventCornerOverlap"), @android.databinding.BindingMethod(type = android.support.v7.widget.CardView.class, attribute = "cardUseCompatPadding", method = "setUseCompatPadding") })
public class CardViewBindingAdapter {
    @android.databinding.BindingAdapter("contentPadding")
    public static void setContentPadding(android.support.v7.widget.CardView view, int padding) {
        view.setContentPadding(padding, padding, padding, padding);
    }

    @android.databinding.BindingAdapter("contentPaddingLeft")
    public static void setContentPaddingLeft(android.support.v7.widget.CardView view, int left) {
        int top = view.getContentPaddingTop();
        int right = view.getContentPaddingRight();
        int bottom = view.getContentPaddingBottom();
        view.setContentPadding(left, top, right, bottom);
    }

    @android.databinding.BindingAdapter("contentPaddingTop")
    public static void setContentPaddingTop(android.support.v7.widget.CardView view, int top) {
        int left = view.getContentPaddingLeft();
        int right = view.getContentPaddingRight();
        int bottom = view.getContentPaddingBottom();
        view.setContentPadding(left, top, right, bottom);
    }

    @android.databinding.BindingAdapter("contentPaddingRight")
    public static void setContentPaddingRight(android.support.v7.widget.CardView view, int right) {
        int left = view.getContentPaddingLeft();
        int top = view.getContentPaddingTop();
        int bottom = view.getContentPaddingBottom();
        view.setContentPadding(left, top, right, bottom);
    }

    @android.databinding.BindingAdapter("contentPaddingBottom")
    public static void setContentPaddingBottom(android.support.v7.widget.CardView view, int bottom) {
        int left = view.getContentPaddingLeft();
        int top = view.getContentPaddingTop();
        int right = view.getContentPaddingRight();
        view.setContentPadding(left, top, right, bottom);
    }
}

