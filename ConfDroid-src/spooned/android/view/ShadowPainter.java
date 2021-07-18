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
package android.view;


public class ShadowPainter {
    /**
     * Adds a drop shadow to a semi-transparent image (of an arbitrary shape) and returns it as a
     * new image. This method attempts to mimic the same visual characteristics as the rectangular
     * shadow painting methods in this class, {@link #createRectangularDropShadow(java.awt.image.BufferedImage)}
     * and {@link #createSmallRectangularDropShadow(java.awt.image.BufferedImage)}.
     * <p/>
     * If shadowSize is less or equals to 1, no shadow will be painted and the source image will be
     * returned instead.
     *
     * @param source
     * 		the source image
     * @param shadowSize
     * 		the size of the shadow, normally {@link #SHADOW_SIZE or {@link
     * 		#SMALL_SHADOW_SIZE}}
     * @param alpha
     * 		alpha value to apply to the shadow
     * @return an image with the shadow painted in or the source image if shadowSize <= 1
     */
    @android.annotation.NonNull
    public static java.awt.image.BufferedImage createDropShadow(java.awt.image.BufferedImage source, int shadowSize, float alpha) {
        shadowSize /= 2;// make shadow size have the same meaning as in the other shadow paint methods in this class

        return android.view.ShadowPainter.createDropShadow(source, shadowSize, 0.7F * alpha, 0);
    }

    /**
     * Creates a drop shadow of a given image and returns a new image which shows the input image on
     * top of its drop shadow.
     * <p/>
     * <b>NOTE: If the shape is rectangular and opaque, consider using {@link #drawRectangleShadow(Graphics2D, int, int, int, int)} instead.</b>
     *
     * @param source
     * 		the source image to be shadowed
     * @param shadowSize
     * 		the size of the shadow in pixels
     * @param shadowOpacity
     * 		the opacity of the shadow, with 0=transparent and 1=opaque
     * @param shadowRgb
     * 		the RGB int to use for the shadow color
     * @return a new image with the source image on top of its shadow when shadowSize > 0 or the
    source image otherwise
     */
    // Imported code
    @java.lang.SuppressWarnings({ "SuspiciousNameCombination", "UnnecessaryLocalVariable" })
    public static java.awt.image.BufferedImage createDropShadow(java.awt.image.BufferedImage source, int shadowSize, float shadowOpacity, int shadowRgb) {
        if (shadowSize <= 0) {
            return source;
        }
        // This code is based on
        // http://www.jroller.com/gfx/entry/non_rectangular_shadow
        java.awt.image.BufferedImage image;
        int width = source.getWidth();
        int height = source.getHeight();
        image = new java.awt.image.BufferedImage(width + android.view.ShadowPainter.SHADOW_SIZE, height + android.view.ShadowPainter.SHADOW_SIZE, java.awt.image.BufferedImage.TYPE_INT_ARGB);
        java.awt.Graphics2D g2 = image.createGraphics();
        g2.drawImage(image, shadowSize, shadowSize, null);
        int dstWidth = image.getWidth();
        int dstHeight = image.getHeight();
        int left = (shadowSize - 1) >> 1;
        int right = shadowSize - left;
        int xStart = left;
        int xStop = dstWidth - right;
        int yStart = left;
        int yStop = dstHeight - right;
        shadowRgb &= 0xffffff;
        int[] aHistory = new int[shadowSize];
        int historyIdx;
        int aSum;
        int[] dataBuffer = ((java.awt.image.DataBufferInt) (image.getRaster().getDataBuffer())).getData();
        int lastPixelOffset = right * dstWidth;
        float sumDivider = shadowOpacity / shadowSize;
        // horizontal pass
        for (int y = 0, bufferOffset = 0; y < dstHeight; y++ , bufferOffset = y * dstWidth) {
            aSum = 0;
            historyIdx = 0;
            for (int x = 0; x < shadowSize; x++ , bufferOffset++) {
                int a = dataBuffer[bufferOffset] >>> 24;
                aHistory[x] = a;
                aSum += a;
            }
            bufferOffset -= right;
            for (int x = xStart; x < xStop; x++ , bufferOffset++) {
                int a = ((int) (aSum * sumDivider));
                dataBuffer[bufferOffset] = (a << 24) | shadowRgb;
                // subtract the oldest pixel from the sum
                aSum -= aHistory[historyIdx];
                // get the latest pixel
                a = dataBuffer[bufferOffset + right] >>> 24;
                aHistory[historyIdx] = a;
                aSum += a;
                if ((++historyIdx) >= shadowSize) {
                    historyIdx -= shadowSize;
                }
            }
        }
        // vertical pass
        for (int x = 0, bufferOffset = 0; x < dstWidth; x++ , bufferOffset = x) {
            aSum = 0;
            historyIdx = 0;
            for (int y = 0; y < shadowSize; y++ , bufferOffset += dstWidth) {
                int a = dataBuffer[bufferOffset] >>> 24;
                aHistory[y] = a;
                aSum += a;
            }
            bufferOffset -= lastPixelOffset;
            for (int y = yStart; y < yStop; y++ , bufferOffset += dstWidth) {
                int a = ((int) (aSum * sumDivider));
                dataBuffer[bufferOffset] = (a << 24) | shadowRgb;
                // subtract the oldest pixel from the sum
                aSum -= aHistory[historyIdx];
                // get the latest pixel
                a = dataBuffer[bufferOffset + lastPixelOffset] >>> 24;
                aHistory[historyIdx] = a;
                aSum += a;
                if ((++historyIdx) >= shadowSize) {
                    historyIdx -= shadowSize;
                }
            }
        }
        g2.drawImage(source, null, 0, 0);
        g2.dispose();
        return image;
    }

    /**
     * Draws a rectangular drop shadow (of size {@link #SHADOW_SIZE} by {@link #SHADOW_SIZE} around
     * the given source and returns a new image with both combined
     *
     * @param source
     * 		the source image
     * @return the source image with a drop shadow on the bottom and right
     */
    @java.lang.SuppressWarnings("UnusedDeclaration")
    public static java.awt.image.BufferedImage createRectangularDropShadow(java.awt.image.BufferedImage source) {
        int type = source.getType();
        if (type == java.awt.image.BufferedImage.TYPE_CUSTOM) {
            type = java.awt.image.BufferedImage.TYPE_INT_ARGB;
        }
        int width = source.getWidth();
        int height = source.getHeight();
        java.awt.image.BufferedImage image;
        image = new java.awt.image.BufferedImage(width + android.view.ShadowPainter.SHADOW_SIZE, height + android.view.ShadowPainter.SHADOW_SIZE, type);
        java.awt.Graphics2D g = image.createGraphics();
        g.drawImage(source, 0, 0, null);
        android.view.ShadowPainter.drawRectangleShadow(image, 0, 0, width, height);
        g.dispose();
        return image;
    }

    /**
     * Draws a small rectangular drop shadow (of size {@link #SMALL_SHADOW_SIZE} by {@link #SMALL_SHADOW_SIZE} around the given source and returns a new image with both combined
     *
     * @param source
     * 		the source image
     * @return the source image with a drop shadow on the bottom and right
     */
    @java.lang.SuppressWarnings("UnusedDeclaration")
    public static java.awt.image.BufferedImage createSmallRectangularDropShadow(java.awt.image.BufferedImage source) {
        int type = source.getType();
        if (type == java.awt.image.BufferedImage.TYPE_CUSTOM) {
            type = java.awt.image.BufferedImage.TYPE_INT_ARGB;
        }
        int width = source.getWidth();
        int height = source.getHeight();
        java.awt.image.BufferedImage image;
        image = new java.awt.image.BufferedImage(width + android.view.ShadowPainter.SMALL_SHADOW_SIZE, height + android.view.ShadowPainter.SMALL_SHADOW_SIZE, type);
        java.awt.Graphics2D g = image.createGraphics();
        g.drawImage(source, 0, 0, null);
        android.view.ShadowPainter.drawSmallRectangleShadow(image, 0, 0, width, height);
        g.dispose();
        return image;
    }

    /**
     * Draws a drop shadow for the given rectangle into the given context. It will not draw anything
     * if the rectangle is smaller than a minimum determined by the assets used to draw the shadow
     * graphics. The size of the shadow is {@link #SHADOW_SIZE}.
     *
     * @param image
     * 		the image to draw the shadow into
     * @param x
     * 		the left coordinate of the left hand side of the rectangle
     * @param y
     * 		the top coordinate of the top of the rectangle
     * @param width
     * 		the width of the rectangle
     * @param height
     * 		the height of the rectangle
     */
    public static void drawRectangleShadow(java.awt.image.BufferedImage image, int x, int y, int width, int height) {
        java.awt.Graphics2D gc = image.createGraphics();
        try {
            android.view.ShadowPainter.drawRectangleShadow(gc, x, y, width, height);
        } finally {
            gc.dispose();
        }
    }

    /**
     * Draws a small drop shadow for the given rectangle into the given context. It will not draw
     * anything if the rectangle is smaller than a minimum determined by the assets used to draw the
     * shadow graphics. The size of the shadow is {@link #SMALL_SHADOW_SIZE}.
     *
     * @param image
     * 		the image to draw the shadow into
     * @param x
     * 		the left coordinate of the left hand side of the rectangle
     * @param y
     * 		the top coordinate of the top of the rectangle
     * @param width
     * 		the width of the rectangle
     * @param height
     * 		the height of the rectangle
     */
    public static void drawSmallRectangleShadow(java.awt.image.BufferedImage image, int x, int y, int width, int height) {
        java.awt.Graphics2D gc = image.createGraphics();
        try {
            android.view.ShadowPainter.drawSmallRectangleShadow(gc, x, y, width, height);
        } finally {
            gc.dispose();
        }
    }

    /**
     * The width and height of the drop shadow painted by
     * {@link #drawRectangleShadow(Graphics2D, int, int, int, int)}
     */
    public static final int SHADOW_SIZE = 20;// DO NOT EDIT. This corresponds to bitmap graphics


    /**
     * The width and height of the drop shadow painted by
     * {@link #drawSmallRectangleShadow(Graphics2D, int, int, int, int)}
     */
    public static final int SMALL_SHADOW_SIZE = 10;// DO NOT EDIT. Corresponds to bitmap graphics


    /**
     * Draws a drop shadow for the given rectangle into the given context. It will not draw anything
     * if the rectangle is smaller than a minimum determined by the assets used to draw the shadow
     * graphics.
     *
     * @param gc
     * 		the graphics context to draw into
     * @param x
     * 		the left coordinate of the left hand side of the rectangle
     * @param y
     * 		the top coordinate of the top of the rectangle
     * @param width
     * 		the width of the rectangle
     * @param height
     * 		the height of the rectangle
     */
    public static void drawRectangleShadow(java.awt.Graphics2D gc, int x, int y, int width, int height) {
        assert android.view.ShadowPainter.ShadowBottomLeft != null;
        assert android.view.ShadowPainter.ShadowBottomRight.getWidth(null) == android.view.ShadowPainter.SHADOW_SIZE;
        assert android.view.ShadowPainter.ShadowBottomRight.getHeight(null) == android.view.ShadowPainter.SHADOW_SIZE;
        int blWidth = android.view.ShadowPainter.ShadowBottomLeft.getWidth(null);
        int trHeight = android.view.ShadowPainter.ShadowTopRight.getHeight(null);
        if (width < blWidth) {
            return;
        }
        if (height < trHeight) {
            return;
        }
        gc.drawImage(android.view.ShadowPainter.ShadowBottomLeft, x - android.view.ShadowPainter.ShadowBottomLeft.getWidth(null), y + height, null);
        gc.drawImage(android.view.ShadowPainter.ShadowBottomRight, x + width, y + height, null);
        gc.drawImage(android.view.ShadowPainter.ShadowTopRight, x + width, y, null);
        gc.drawImage(android.view.ShadowPainter.ShadowTopLeft, x - android.view.ShadowPainter.ShadowTopLeft.getWidth(null), y, null);
        gc.drawImage(android.view.ShadowPainter.ShadowBottom, x, y + height, x + width, (y + height) + android.view.ShadowPainter.ShadowBottom.getHeight(null), 0, 0, android.view.ShadowPainter.ShadowBottom.getWidth(null), android.view.ShadowPainter.ShadowBottom.getHeight(null), null);
        gc.drawImage(android.view.ShadowPainter.ShadowRight, x + width, y + android.view.ShadowPainter.ShadowTopRight.getHeight(null), (x + width) + android.view.ShadowPainter.ShadowRight.getWidth(null), y + height, 0, 0, android.view.ShadowPainter.ShadowRight.getWidth(null), android.view.ShadowPainter.ShadowRight.getHeight(null), null);
        gc.drawImage(android.view.ShadowPainter.ShadowLeft, x - android.view.ShadowPainter.ShadowLeft.getWidth(null), y + android.view.ShadowPainter.ShadowTopLeft.getHeight(null), x, y + height, 0, 0, android.view.ShadowPainter.ShadowLeft.getWidth(null), android.view.ShadowPainter.ShadowLeft.getHeight(null), null);
    }

    /**
     * Draws a small drop shadow for the given rectangle into the given context. It will not draw
     * anything if the rectangle is smaller than a minimum determined by the assets used to draw the
     * shadow graphics.
     * <p/>
     *
     * @param gc
     * 		the graphics context to draw into
     * @param x
     * 		the left coordinate of the left hand side of the rectangle
     * @param y
     * 		the top coordinate of the top of the rectangle
     * @param width
     * 		the width of the rectangle
     * @param height
     * 		the height of the rectangle
     */
    public static void drawSmallRectangleShadow(java.awt.Graphics2D gc, int x, int y, int width, int height) {
        assert android.view.ShadowPainter.Shadow2BottomLeft != null;
        assert android.view.ShadowPainter.Shadow2TopRight != null;
        assert android.view.ShadowPainter.Shadow2BottomRight.getWidth(null) == android.view.ShadowPainter.SMALL_SHADOW_SIZE;
        assert android.view.ShadowPainter.Shadow2BottomRight.getHeight(null) == android.view.ShadowPainter.SMALL_SHADOW_SIZE;
        int blWidth = android.view.ShadowPainter.Shadow2BottomLeft.getWidth(null);
        int trHeight = android.view.ShadowPainter.Shadow2TopRight.getHeight(null);
        if (width < blWidth) {
            return;
        }
        if (height < trHeight) {
            return;
        }
        gc.drawImage(android.view.ShadowPainter.Shadow2BottomLeft, x - android.view.ShadowPainter.Shadow2BottomLeft.getWidth(null), y + height, null);
        gc.drawImage(android.view.ShadowPainter.Shadow2BottomRight, x + width, y + height, null);
        gc.drawImage(android.view.ShadowPainter.Shadow2TopRight, x + width, y, null);
        gc.drawImage(android.view.ShadowPainter.Shadow2TopLeft, x - android.view.ShadowPainter.Shadow2TopLeft.getWidth(null), y, null);
        gc.drawImage(android.view.ShadowPainter.Shadow2Bottom, x, y + height, x + width, (y + height) + android.view.ShadowPainter.Shadow2Bottom.getHeight(null), 0, 0, android.view.ShadowPainter.Shadow2Bottom.getWidth(null), android.view.ShadowPainter.Shadow2Bottom.getHeight(null), null);
        gc.drawImage(android.view.ShadowPainter.Shadow2Right, x + width, y + android.view.ShadowPainter.Shadow2TopRight.getHeight(null), (x + width) + android.view.ShadowPainter.Shadow2Right.getWidth(null), y + height, 0, 0, android.view.ShadowPainter.Shadow2Right.getWidth(null), android.view.ShadowPainter.Shadow2Right.getHeight(null), null);
        gc.drawImage(android.view.ShadowPainter.Shadow2Left, x - android.view.ShadowPainter.Shadow2Left.getWidth(null), y + android.view.ShadowPainter.Shadow2TopLeft.getHeight(null), x, y + height, 0, 0, android.view.ShadowPainter.Shadow2Left.getWidth(null), android.view.ShadowPainter.Shadow2Left.getHeight(null), null);
    }

    private static java.awt.Image loadIcon(java.lang.String name) {
        java.io.InputStream inputStream = android.view.ShadowPainter.class.getResourceAsStream(name);
        if (inputStream == null) {
            throw new java.lang.RuntimeException("Unable to load image for shadow: " + name);
        }
        try {
            return javax.imageio.ImageIO.read(inputStream);
        } catch (java.io.IOException e) {
            throw new java.lang.RuntimeException("Unable to load image for shadow:" + name, e);
        } finally {
            try {
                inputStream.close();
            } catch (java.io.IOException e) {
                // ignore.
            }
        }
    }

    // Shadow graphics. This was generated by creating a drop shadow in
    // Gimp, using the parameters x offset=10, y offset=10, blur radius=10,
    // (for the small drop shadows x offset=10, y offset=10, blur radius=10)
    // color=black, and opacity=51. These values attempt to make a shadow
    // that is legible both for dark and light themes, on top of the
    // canvas background (rgb(150,150,150). Darker shadows would tend to
    // blend into the foreground for a dark holo screen, and lighter shadows
    // would be hard to spot on the canvas background. If you make adjustments,
    // make sure to check the shadow with both dark and light themes.
    // 
    // After making the graphics, I cut out the top right, bottom left
    // and bottom right corners as 20x20 images, and these are reproduced by
    // painting them in the corresponding places in the target graphics context.
    // I then grabbed a single horizontal gradient line from the middle of the
    // right edge,and a single vertical gradient line from the bottom. These
    // are then painted scaled/stretched in the target to fill the gaps between
    // the three corner images.
    // 
    // Filenames: bl=bottom left, b=bottom, br=bottom right, r=right, tr=top right
    // Normal Drop Shadow
    private static final java.awt.Image ShadowBottom = android.view.ShadowPainter.loadIcon("/icons/shadow-b.png");

    private static final java.awt.Image ShadowBottomLeft = android.view.ShadowPainter.loadIcon("/icons/shadow-bl.png");

    private static final java.awt.Image ShadowBottomRight = android.view.ShadowPainter.loadIcon("/icons/shadow-br.png");

    private static final java.awt.Image ShadowRight = android.view.ShadowPainter.loadIcon("/icons/shadow-r.png");

    private static final java.awt.Image ShadowTopRight = android.view.ShadowPainter.loadIcon("/icons/shadow-tr.png");

    private static final java.awt.Image ShadowTopLeft = android.view.ShadowPainter.loadIcon("/icons/shadow-tl.png");

    private static final java.awt.Image ShadowLeft = android.view.ShadowPainter.loadIcon("/icons/shadow-l.png");

    // Small Drop Shadow
    private static final java.awt.Image Shadow2Bottom = android.view.ShadowPainter.loadIcon("/icons/shadow2-b.png");

    private static final java.awt.Image Shadow2BottomLeft = android.view.ShadowPainter.loadIcon("/icons/shadow2-bl.png");

    private static final java.awt.Image Shadow2BottomRight = android.view.ShadowPainter.loadIcon("/icons/shadow2-br.png");

    private static final java.awt.Image Shadow2Right = android.view.ShadowPainter.loadIcon("/icons/shadow2-r.png");

    private static final java.awt.Image Shadow2TopRight = android.view.ShadowPainter.loadIcon("/icons/shadow2-tr.png");

    private static final java.awt.Image Shadow2TopLeft = android.view.ShadowPainter.loadIcon("/icons/shadow2-tl.png");

    private static final java.awt.Image Shadow2Left = android.view.ShadowPainter.loadIcon("/icons/shadow2-l.png");
}

