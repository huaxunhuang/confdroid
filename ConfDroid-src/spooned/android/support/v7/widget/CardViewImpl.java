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
package android.support.v7.widget;


/**
 * Interface for platform specific CardView implementations.
 */
interface CardViewImpl {
    void initialize(android.support.v7.widget.CardViewDelegate cardView, android.content.Context context, android.content.res.ColorStateList backgroundColor, float radius, float elevation, float maxElevation);

    void setRadius(android.support.v7.widget.CardViewDelegate cardView, float radius);

    float getRadius(android.support.v7.widget.CardViewDelegate cardView);

    void setElevation(android.support.v7.widget.CardViewDelegate cardView, float elevation);

    float getElevation(android.support.v7.widget.CardViewDelegate cardView);

    void initStatic();

    void setMaxElevation(android.support.v7.widget.CardViewDelegate cardView, float maxElevation);

    float getMaxElevation(android.support.v7.widget.CardViewDelegate cardView);

    float getMinWidth(android.support.v7.widget.CardViewDelegate cardView);

    float getMinHeight(android.support.v7.widget.CardViewDelegate cardView);

    void updatePadding(android.support.v7.widget.CardViewDelegate cardView);

    void onCompatPaddingChanged(android.support.v7.widget.CardViewDelegate cardView);

    void onPreventCornerOverlapChanged(android.support.v7.widget.CardViewDelegate cardView);

    void setBackgroundColor(android.support.v7.widget.CardViewDelegate cardView, @android.support.annotation.Nullable
    android.content.res.ColorStateList color);

    android.content.res.ColorStateList getBackgroundColor(android.support.v7.widget.CardViewDelegate cardView);
}

