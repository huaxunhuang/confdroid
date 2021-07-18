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
public class Quad {
    public android.filterfw.geometry.Point p0;

    public android.filterfw.geometry.Point p1;

    public android.filterfw.geometry.Point p2;

    public android.filterfw.geometry.Point p3;

    public Quad() {
    }

    public Quad(android.filterfw.geometry.Point p0, android.filterfw.geometry.Point p1, android.filterfw.geometry.Point p2, android.filterfw.geometry.Point p3) {
        this.p0 = p0;
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
    }

    public boolean IsInUnitRange() {
        return ((p0.IsInUnitRange() && p1.IsInUnitRange()) && p2.IsInUnitRange()) && p3.IsInUnitRange();
    }

    public android.filterfw.geometry.Quad translated(android.filterfw.geometry.Point t) {
        return new android.filterfw.geometry.Quad(p0.plus(t), p1.plus(t), p2.plus(t), p3.plus(t));
    }

    public android.filterfw.geometry.Quad translated(float x, float y) {
        return new android.filterfw.geometry.Quad(p0.plus(x, y), p1.plus(x, y), p2.plus(x, y), p3.plus(x, y));
    }

    public android.filterfw.geometry.Quad scaled(float s) {
        return new android.filterfw.geometry.Quad(p0.times(s), p1.times(s), p2.times(s), p3.times(s));
    }

    public android.filterfw.geometry.Quad scaled(float x, float y) {
        return new android.filterfw.geometry.Quad(p0.mult(x, y), p1.mult(x, y), p2.mult(x, y), p3.mult(x, y));
    }

    public android.filterfw.geometry.Rectangle boundingBox() {
        java.util.List<java.lang.Float> xs = java.util.Arrays.asList(p0.x, p1.x, p2.x, p3.x);
        java.util.List<java.lang.Float> ys = java.util.Arrays.asList(p0.y, p1.y, p2.y, p3.y);
        float x0 = java.util.Collections.min(xs);
        float y0 = java.util.Collections.min(ys);
        float x1 = java.util.Collections.max(xs);
        float y1 = java.util.Collections.max(ys);
        return new android.filterfw.geometry.Rectangle(x0, y0, x1 - x0, y1 - y0);
    }

    public float getBoundingWidth() {
        java.util.List<java.lang.Float> xs = java.util.Arrays.asList(p0.x, p1.x, p2.x, p3.x);
        return java.util.Collections.max(xs) - java.util.Collections.min(xs);
    }

    public float getBoundingHeight() {
        java.util.List<java.lang.Float> ys = java.util.Arrays.asList(p0.y, p1.y, p2.y, p3.y);
        return java.util.Collections.max(ys) - java.util.Collections.min(ys);
    }

    @java.lang.Override
    public java.lang.String toString() {
        return ((((((("{" + p0) + ", ") + p1) + ", ") + p2) + ", ") + p3) + "}";
    }
}

