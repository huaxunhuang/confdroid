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
public class Point {
    public float x;

    public float y;

    public Point() {
    }

    public Point(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void set(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public boolean IsInUnitRange() {
        return (((x >= 0.0F) && (x <= 1.0F)) && (y >= 0.0F)) && (y <= 1.0F);
    }

    public android.filterfw.geometry.Point plus(float x, float y) {
        return new android.filterfw.geometry.Point(this.x + x, this.y + y);
    }

    public android.filterfw.geometry.Point plus(android.filterfw.geometry.Point point) {
        return this.plus(point.x, point.y);
    }

    public android.filterfw.geometry.Point minus(float x, float y) {
        return new android.filterfw.geometry.Point(this.x - x, this.y - y);
    }

    public android.filterfw.geometry.Point minus(android.filterfw.geometry.Point point) {
        return this.minus(point.x, point.y);
    }

    public android.filterfw.geometry.Point times(float s) {
        return new android.filterfw.geometry.Point(this.x * s, this.y * s);
    }

    public android.filterfw.geometry.Point mult(float x, float y) {
        return new android.filterfw.geometry.Point(this.x * x, this.y * y);
    }

    public float length() {
        return ((float) (java.lang.Math.hypot(x, y)));
    }

    public float distanceTo(android.filterfw.geometry.Point p) {
        return p.minus(this).length();
    }

    public android.filterfw.geometry.Point scaledTo(float length) {
        return this.times(length / this.length());
    }

    public android.filterfw.geometry.Point normalize() {
        return this.scaledTo(1.0F);
    }

    public android.filterfw.geometry.Point rotated90(int count) {
        float nx = this.x;
        float ny = this.y;
        for (int i = 0; i < count; ++i) {
            float ox = nx;
            nx = ny;
            ny = -ox;
        }
        return new android.filterfw.geometry.Point(nx, ny);
    }

    public android.filterfw.geometry.Point rotated(float radians) {
        // TODO(renn): Optimize: Keep cache of cos/sin values
        return new android.filterfw.geometry.Point(((float) ((java.lang.Math.cos(radians) * x) - (java.lang.Math.sin(radians) * y))), ((float) ((java.lang.Math.sin(radians) * x) + (java.lang.Math.cos(radians) * y))));
    }

    public android.filterfw.geometry.Point rotatedAround(android.filterfw.geometry.Point center, float radians) {
        return this.minus(center).rotated(radians).plus(center);
    }

    @java.lang.Override
    public java.lang.String toString() {
        return ((("(" + x) + ", ") + y) + ")";
    }
}

