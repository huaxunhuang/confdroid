/**
 * Copyright (C) 2011 The Android Open Source Project
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
package android.filterfw.geometry;


/**
 *
 *
 * @unknown 
 */
// public Rectangle rotated(float radians) {
// TODO: Implement this.
// }
public class Rectangle extends android.filterfw.geometry.Quad {
    public Rectangle() {
    }

    public Rectangle(float x, float y, float width, float height) {
        super(new android.filterfw.geometry.Point(x, y), new android.filterfw.geometry.Point(x + width, y), new android.filterfw.geometry.Point(x, y + height), new android.filterfw.geometry.Point(x + width, y + height));
    }

    public Rectangle(android.filterfw.geometry.Point origin, android.filterfw.geometry.Point size) {
        super(origin, origin.plus(size.x, 0.0F), origin.plus(0.0F, size.y), origin.plus(size.x, size.y));
    }

    public static android.filterfw.geometry.Rectangle fromRotatedRect(android.filterfw.geometry.Point center, android.filterfw.geometry.Point size, float rotation) {
        android.filterfw.geometry.Point p0 = new android.filterfw.geometry.Point(center.x - (size.x / 2.0F), center.y - (size.y / 2.0F));
        android.filterfw.geometry.Point p1 = new android.filterfw.geometry.Point(center.x + (size.x / 2.0F), center.y - (size.y / 2.0F));
        android.filterfw.geometry.Point p2 = new android.filterfw.geometry.Point(center.x - (size.x / 2.0F), center.y + (size.y / 2.0F));
        android.filterfw.geometry.Point p3 = new android.filterfw.geometry.Point(center.x + (size.x / 2.0F), center.y + (size.y / 2.0F));
        return new android.filterfw.geometry.Rectangle(p0.rotatedAround(center, rotation), p1.rotatedAround(center, rotation), p2.rotatedAround(center, rotation), p3.rotatedAround(center, rotation));
    }

    private Rectangle(android.filterfw.geometry.Point p0, android.filterfw.geometry.Point p1, android.filterfw.geometry.Point p2, android.filterfw.geometry.Point p3) {
        super(p0, p1, p2, p3);
    }

    public static android.filterfw.geometry.Rectangle fromCenterVerticalAxis(android.filterfw.geometry.Point center, android.filterfw.geometry.Point vAxis, android.filterfw.geometry.Point size) {
        android.filterfw.geometry.Point dy = vAxis.scaledTo(size.y / 2.0F);
        android.filterfw.geometry.Point dx = vAxis.rotated90(1).scaledTo(size.x / 2.0F);
        return new android.filterfw.geometry.Rectangle(center.minus(dx).minus(dy), center.plus(dx).minus(dy), center.minus(dx).plus(dy), center.plus(dx).plus(dy));
    }

    public float getWidth() {
        return p1.minus(p0).length();
    }

    public float getHeight() {
        return p2.minus(p0).length();
    }

    public android.filterfw.geometry.Point center() {
        return p0.plus(p1).plus(p2).plus(p3).times(0.25F);
    }

    @java.lang.Override
    public android.filterfw.geometry.Rectangle scaled(float s) {
        return new android.filterfw.geometry.Rectangle(p0.times(s), p1.times(s), p2.times(s), p3.times(s));
    }

    @java.lang.Override
    public android.filterfw.geometry.Rectangle scaled(float x, float y) {
        return new android.filterfw.geometry.Rectangle(p0.mult(x, y), p1.mult(x, y), p2.mult(x, y), p3.mult(x, y));
    }
}

