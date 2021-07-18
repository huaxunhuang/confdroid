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
package android.hardware.camera2.utils;


/**
 * Comparator for {@link Size} objects by the area.
 *
 * <p>This comparator totally orders by rectangle area. Tie-breaks on width.</p>
 */
public class SizeAreaComparator implements java.util.Comparator<android.util.Size> {
    /**
     * {@inheritDoc }
     */
    @java.lang.Override
    public int compare(android.util.Size size, android.util.Size size2) {
        checkNotNull(size, "size must not be null");
        checkNotNull(size2, "size2 must not be null");
        if (size.equals(size2)) {
            return 0;
        }
        long width = size.getWidth();
        long width2 = size2.getWidth();
        long area = width * size.getHeight();
        long area2 = width2 * size2.getHeight();
        if (area == area2) {
            return width > width2 ? 1 : -1;
        }
        return area > area2 ? 1 : -1;
    }

    /**
     * Get the largest {@code Size} from the list by comparing each size's area
     * by each other using {@link SizeAreaComparator}.
     *
     * @param sizes
     * 		a non-{@code null} list of non-{@code null} sizes
     * @return a non-{@code null} size
     * @throws NullPointerException
     * 		if {@code sizes} or any elements in it were {@code null}
     */
    public static android.util.Size findLargestByArea(java.util.List<android.util.Size> sizes) {
        checkNotNull(sizes, "sizes must not be null");
        return java.util.Collections.max(sizes, new android.hardware.camera2.utils.SizeAreaComparator());
    }
}

