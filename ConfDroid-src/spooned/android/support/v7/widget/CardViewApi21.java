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


class CardViewApi21 implements android.support.v7.widget.CardViewImpl {
    @java.lang.Override
    public void initialize(android.support.v7.widget.CardViewDelegate cardView, android.content.Context context, android.content.res.ColorStateList backgroundColor, float radius, float elevation, float maxElevation) {
        final android.support.v7.widget.RoundRectDrawable background = new android.support.v7.widget.RoundRectDrawable(backgroundColor, radius);
        cardView.setCardBackground(background);
        android.view.View view = cardView.getCardView();
        view.setClipToOutline(true);
        view.setElevation(elevation);
        setMaxElevation(cardView, maxElevation);
    }

    @java.lang.Override
    public void setRadius(android.support.v7.widget.CardViewDelegate cardView, float radius) {
        getCardBackground(cardView).setRadius(radius);
    }

    @java.lang.Override
    public void initStatic() {
    }

    @java.lang.Override
    public void setMaxElevation(android.support.v7.widget.CardViewDelegate cardView, float maxElevation) {
        getCardBackground(cardView).setPadding(maxElevation, cardView.getUseCompatPadding(), cardView.getPreventCornerOverlap());
        updatePadding(cardView);
    }

    @java.lang.Override
    public float getMaxElevation(android.support.v7.widget.CardViewDelegate cardView) {
        return getCardBackground(cardView).getPadding();
    }

    @java.lang.Override
    public float getMinWidth(android.support.v7.widget.CardViewDelegate cardView) {
        return getRadius(cardView) * 2;
    }

    @java.lang.Override
    public float getMinHeight(android.support.v7.widget.CardViewDelegate cardView) {
        return getRadius(cardView) * 2;
    }

    @java.lang.Override
    public float getRadius(android.support.v7.widget.CardViewDelegate cardView) {
        return getCardBackground(cardView).getRadius();
    }

    @java.lang.Override
    public void setElevation(android.support.v7.widget.CardViewDelegate cardView, float elevation) {
        cardView.getCardView().setElevation(elevation);
    }

    @java.lang.Override
    public float getElevation(android.support.v7.widget.CardViewDelegate cardView) {
        return cardView.getCardView().getElevation();
    }

    @java.lang.Override
    public void updatePadding(android.support.v7.widget.CardViewDelegate cardView) {
        if (!cardView.getUseCompatPadding()) {
            cardView.setShadowPadding(0, 0, 0, 0);
            return;
        }
        float elevation = getMaxElevation(cardView);
        final float radius = getRadius(cardView);
        int hPadding = ((int) (java.lang.Math.ceil(android.support.v7.widget.RoundRectDrawableWithShadow.calculateHorizontalPadding(elevation, radius, cardView.getPreventCornerOverlap()))));
        int vPadding = ((int) (java.lang.Math.ceil(android.support.v7.widget.RoundRectDrawableWithShadow.calculateVerticalPadding(elevation, radius, cardView.getPreventCornerOverlap()))));
        cardView.setShadowPadding(hPadding, vPadding, hPadding, vPadding);
    }

    @java.lang.Override
    public void onCompatPaddingChanged(android.support.v7.widget.CardViewDelegate cardView) {
        setMaxElevation(cardView, getMaxElevation(cardView));
    }

    @java.lang.Override
    public void onPreventCornerOverlapChanged(android.support.v7.widget.CardViewDelegate cardView) {
        setMaxElevation(cardView, getMaxElevation(cardView));
    }

    @java.lang.Override
    public void setBackgroundColor(android.support.v7.widget.CardViewDelegate cardView, @android.support.annotation.Nullable
    android.content.res.ColorStateList color) {
        getCardBackground(cardView).setColor(color);
    }

    @java.lang.Override
    public android.content.res.ColorStateList getBackgroundColor(android.support.v7.widget.CardViewDelegate cardView) {
        return getCardBackground(cardView).getColor();
    }

    private android.support.v7.widget.RoundRectDrawable getCardBackground(android.support.v7.widget.CardViewDelegate cardView) {
        return ((android.support.v7.widget.RoundRectDrawable) (cardView.getCardBackground()));
    }
}

