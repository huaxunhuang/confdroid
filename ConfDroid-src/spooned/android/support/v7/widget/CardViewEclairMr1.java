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


class CardViewEclairMr1 implements android.support.v7.widget.CardViewImpl {
    final android.graphics.RectF sCornerRect = new android.graphics.RectF();

    @java.lang.Override
    public void initStatic() {
        // Draws a round rect using 7 draw operations. This is faster than using
        // canvas.drawRoundRect before JBMR1 because API 11-16 used alpha mask textures to draw
        // shapes.
        android.support.v7.widget.RoundRectDrawableWithShadow.sRoundRectHelper = new android.support.v7.widget.RoundRectDrawableWithShadow.RoundRectHelper() {
            @java.lang.Override
            public void drawRoundRect(android.graphics.Canvas canvas, android.graphics.RectF bounds, float cornerRadius, android.graphics.Paint paint) {
                final float twoRadius = cornerRadius * 2;
                final float innerWidth = (bounds.width() - twoRadius) - 1;
                final float innerHeight = (bounds.height() - twoRadius) - 1;
                if (cornerRadius >= 1.0F) {
                    // increment corner radius to account for half pixels.
                    float roundedCornerRadius = cornerRadius + 0.5F;
                    sCornerRect.set(-roundedCornerRadius, -roundedCornerRadius, roundedCornerRadius, roundedCornerRadius);
                    int saved = canvas.save();
                    canvas.translate(bounds.left + roundedCornerRadius, bounds.top + roundedCornerRadius);
                    canvas.drawArc(sCornerRect, 180, 90, true, paint);
                    canvas.translate(innerWidth, 0);
                    canvas.rotate(90);
                    canvas.drawArc(sCornerRect, 180, 90, true, paint);
                    canvas.translate(innerHeight, 0);
                    canvas.rotate(90);
                    canvas.drawArc(sCornerRect, 180, 90, true, paint);
                    canvas.translate(innerWidth, 0);
                    canvas.rotate(90);
                    canvas.drawArc(sCornerRect, 180, 90, true, paint);
                    canvas.restoreToCount(saved);
                    // draw top and bottom pieces
                    canvas.drawRect((bounds.left + roundedCornerRadius) - 1.0F, bounds.top, (bounds.right - roundedCornerRadius) + 1.0F, bounds.top + roundedCornerRadius, paint);
                    canvas.drawRect((bounds.left + roundedCornerRadius) - 1.0F, bounds.bottom - roundedCornerRadius, (bounds.right - roundedCornerRadius) + 1.0F, bounds.bottom, paint);
                }
                // center
                canvas.drawRect(bounds.left, bounds.top + cornerRadius, bounds.right, bounds.bottom - cornerRadius, paint);
            }
        };
    }

    @java.lang.Override
    public void initialize(android.support.v7.widget.CardViewDelegate cardView, android.content.Context context, android.content.res.ColorStateList backgroundColor, float radius, float elevation, float maxElevation) {
        android.support.v7.widget.RoundRectDrawableWithShadow background = createBackground(context, backgroundColor, radius, elevation, maxElevation);
        background.setAddPaddingForCorners(cardView.getPreventCornerOverlap());
        cardView.setCardBackground(background);
        updatePadding(cardView);
    }

    private android.support.v7.widget.RoundRectDrawableWithShadow createBackground(android.content.Context context, android.content.res.ColorStateList backgroundColor, float radius, float elevation, float maxElevation) {
        return new android.support.v7.widget.RoundRectDrawableWithShadow(context.getResources(), backgroundColor, radius, elevation, maxElevation);
    }

    @java.lang.Override
    public void updatePadding(android.support.v7.widget.CardViewDelegate cardView) {
        android.graphics.Rect shadowPadding = new android.graphics.Rect();
        getShadowBackground(cardView).getMaxShadowAndCornerPadding(shadowPadding);
        cardView.setMinWidthHeightInternal(((int) (java.lang.Math.ceil(getMinWidth(cardView)))), ((int) (java.lang.Math.ceil(getMinHeight(cardView)))));
        cardView.setShadowPadding(shadowPadding.left, shadowPadding.top, shadowPadding.right, shadowPadding.bottom);
    }

    @java.lang.Override
    public void onCompatPaddingChanged(android.support.v7.widget.CardViewDelegate cardView) {
        // NO OP
    }

    @java.lang.Override
    public void onPreventCornerOverlapChanged(android.support.v7.widget.CardViewDelegate cardView) {
        getShadowBackground(cardView).setAddPaddingForCorners(cardView.getPreventCornerOverlap());
        updatePadding(cardView);
    }

    @java.lang.Override
    public void setBackgroundColor(android.support.v7.widget.CardViewDelegate cardView, @android.support.annotation.Nullable
    android.content.res.ColorStateList color) {
        getShadowBackground(cardView).setColor(color);
    }

    public android.content.res.ColorStateList getBackgroundColor(android.support.v7.widget.CardViewDelegate cardView) {
        return getShadowBackground(cardView).getColor();
    }

    @java.lang.Override
    public void setRadius(android.support.v7.widget.CardViewDelegate cardView, float radius) {
        getShadowBackground(cardView).setCornerRadius(radius);
        updatePadding(cardView);
    }

    @java.lang.Override
    public float getRadius(android.support.v7.widget.CardViewDelegate cardView) {
        return getShadowBackground(cardView).getCornerRadius();
    }

    @java.lang.Override
    public void setElevation(android.support.v7.widget.CardViewDelegate cardView, float elevation) {
        getShadowBackground(cardView).setShadowSize(elevation);
    }

    @java.lang.Override
    public float getElevation(android.support.v7.widget.CardViewDelegate cardView) {
        return getShadowBackground(cardView).getShadowSize();
    }

    @java.lang.Override
    public void setMaxElevation(android.support.v7.widget.CardViewDelegate cardView, float maxElevation) {
        getShadowBackground(cardView).setMaxShadowSize(maxElevation);
        updatePadding(cardView);
    }

    @java.lang.Override
    public float getMaxElevation(android.support.v7.widget.CardViewDelegate cardView) {
        return getShadowBackground(cardView).getMaxShadowSize();
    }

    @java.lang.Override
    public float getMinWidth(android.support.v7.widget.CardViewDelegate cardView) {
        return getShadowBackground(cardView).getMinWidth();
    }

    @java.lang.Override
    public float getMinHeight(android.support.v7.widget.CardViewDelegate cardView) {
        return getShadowBackground(cardView).getMinHeight();
    }

    private android.support.v7.widget.RoundRectDrawableWithShadow getShadowBackground(android.support.v7.widget.CardViewDelegate cardView) {
        return ((android.support.v7.widget.RoundRectDrawableWithShadow) (cardView.getCardBackground()));
    }
}

