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
package android.databinding.tool.store;


public class LocationTest {
    @org.junit.Test
    public void testInvalid() {
        org.junit.Assert.assertFalse(new android.databinding.tool.store.Location().isValid());
    }

    @org.junit.Test
    public void testValid() {
        android.databinding.tool.store.Location location = new android.databinding.tool.store.Location(0, 0, 1, 1);
        org.junit.Assert.assertTrue(location.isValid());
    }

    @org.junit.Test
    public void testContains() {
        android.databinding.tool.store.Location location1 = new android.databinding.tool.store.Location(0, 0, 10, 1);
        android.databinding.tool.store.Location location2 = new android.databinding.tool.store.Location(0, 0, 9, 1);
        org.junit.Assert.assertTrue(location1.contains(location2));
        location2.endLine = 10;
        org.junit.Assert.assertTrue(location1.contains(location2));
        location2.endOffset = 2;
        org.junit.Assert.assertFalse(location1.contains(location2));
    }

    @org.junit.Test
    public void testAbsolute() {
        android.databinding.tool.store.Location loc = new android.databinding.tool.store.Location(1, 2, 3, 4);
        org.junit.Assert.assertEquals(loc, loc.toAbsoluteLocation());
    }

    @org.junit.Test
    public void testAbsoluteWithInvalidParent() {
        android.databinding.tool.store.Location loc = new android.databinding.tool.store.Location(1, 2, 3, 4);
        loc.setParentLocation(new android.databinding.tool.store.Location());
        org.junit.Assert.assertEquals(loc, loc.toAbsoluteLocation());
    }

    @org.junit.Test
    public void testAbsoluteWithParent() {
        android.databinding.tool.store.Location loc = new android.databinding.tool.store.Location(1, 2, 3, 4);
        loc.setParentLocation(new android.databinding.tool.store.Location(10, 0, 20, 0));
        org.junit.Assert.assertEquals(new android.databinding.tool.store.Location(11, 2, 13, 4), loc.toAbsoluteLocation());
    }

    @org.junit.Test
    public void testAbsoluteWith2Parents() {
        android.databinding.tool.store.Location loc = new android.databinding.tool.store.Location(1, 2, 3, 4);
        android.databinding.tool.store.Location parent1 = new android.databinding.tool.store.Location(5, 6, 10, 11);
        parent1.setParentLocation(new android.databinding.tool.store.Location(5, 6, 17, 8));
        loc.setParentLocation(parent1);
        org.junit.Assert.assertEquals(new android.databinding.tool.store.Location(10, 6, 15, 11), parent1.toAbsoluteLocation());
        org.junit.Assert.assertEquals(new android.databinding.tool.store.Location(11, 2, 13, 4), loc.toAbsoluteLocation());
    }

    @org.junit.Test
    public void testAbsoluteWithSameLine() {
        android.databinding.tool.store.Location loc = new android.databinding.tool.store.Location(0, 2, 0, 4);
        loc.setParentLocation(new android.databinding.tool.store.Location(7, 2, 12, 46));
        org.junit.Assert.assertEquals(new android.databinding.tool.store.Location(7, 4, 7, 6), loc.toAbsoluteLocation());
    }
}

