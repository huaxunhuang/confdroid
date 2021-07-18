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
package android.system;


public class UnixSocketAddressTest extends junit.framework.TestCase {
    public void testFilesystemSunPath() throws java.lang.Exception {
        java.lang.String path = "/foo/bar";
        android.system.UnixSocketAddress sa = android.system.UnixSocketAddress.createFileSystem(path);
        byte[] abstractNameBytes = path.getBytes(java.nio.charset.StandardCharsets.UTF_8);
        byte[] expected = new byte[abstractNameBytes.length + 1];
        // See unix(7)
        java.lang.System.arraycopy(abstractNameBytes, 0, expected, 0, abstractNameBytes.length);
        junit.framework.TestCase.assertTrue(java.util.Arrays.equals(expected, sa.getSunPath()));
    }

    public void testUnnamedSunPath() throws java.lang.Exception {
        android.system.UnixSocketAddress sa = android.system.UnixSocketAddress.createUnnamed();
        junit.framework.TestCase.assertEquals(0, sa.getSunPath().length);
    }

    public void testAbstractSunPath() throws java.lang.Exception {
        java.lang.String abstractName = "abstract";
        android.system.UnixSocketAddress sa = android.system.UnixSocketAddress.createAbstract(abstractName);
        byte[] abstractNameBytes = abstractName.getBytes(java.nio.charset.StandardCharsets.UTF_8);
        byte[] expected = new byte[abstractNameBytes.length + 1];
        // See unix(7)
        java.lang.System.arraycopy(abstractNameBytes, 0, expected, 1, abstractNameBytes.length);
        junit.framework.TestCase.assertTrue(java.util.Arrays.equals(expected, sa.getSunPath()));
    }
}

