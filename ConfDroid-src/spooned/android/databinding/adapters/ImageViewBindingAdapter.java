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


@android.databinding.BindingMethods({ @android.databinding.BindingMethod(type = android.widget.ImageView.class, attribute = "android:tint", method = "setImageTintList"), @android.databinding.BindingMethod(type = android.widget.ImageView.class, attribute = "android:tintMode", method = "setImageTintMode") })
public class ImageViewBindingAdapter {
    @android.databinding.BindingAdapter("android:src")
    public static void setImageUri(android.widget.ImageView view, java.lang.String imageUri) {
        if (imageUri == null) {
            view.setImageURI(null);
        } else {
            view.setImageURI(android.net.Uri.parse(imageUri));
        }
    }

    @android.databinding.BindingAdapter("android:src")
    public static void setImageUri(android.widget.ImageView view, android.net.Uri imageUri) {
        view.setImageURI(imageUri);
    }

    @android.databinding.BindingAdapter("android:src")
    public static void setImageDrawable(android.widget.ImageView view, android.graphics.drawable.Drawable drawable) {
        view.setImageDrawable(drawable);
    }
}

