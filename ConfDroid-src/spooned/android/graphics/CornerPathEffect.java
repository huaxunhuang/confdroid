/**
 * Copyright (C) 2007 The Android Open Source Project
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
package android.graphics;


public class CornerPathEffect extends android.graphics.PathEffect {
    /**
     * Transforms geometries that are drawn (either STROKE or FILL styles) by
     * replacing any sharp angles between line segments into rounded angles of
     * the specified radius.
     *
     * @param radius
     * 		Amount to round sharp angles between line segments.
     */
    public CornerPathEffect(float radius) {
        native_instance = android.graphics.CornerPathEffect.nativeCreate(radius);
    }

    private static native long nativeCreate(float radius);
}

