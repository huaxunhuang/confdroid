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
package android.opengl;


/**
 * The abstract base class for a GL wrapper. Provides
 * some convenient instance variables and default implementations.
 */
abstract class GLWrapperBase implements javax.microedition.khronos.opengles.GL , javax.microedition.khronos.opengles.GL10 , javax.microedition.khronos.opengles.GL10Ext , javax.microedition.khronos.opengles.GL11 , javax.microedition.khronos.opengles.GL11Ext , javax.microedition.khronos.opengles.GL11ExtensionPack {
    public GLWrapperBase(javax.microedition.khronos.opengles.GL gl) {
        mgl = ((javax.microedition.khronos.opengles.GL10) (gl));
        if (gl instanceof javax.microedition.khronos.opengles.GL10Ext) {
            mgl10Ext = ((javax.microedition.khronos.opengles.GL10Ext) (gl));
        }
        if (gl instanceof javax.microedition.khronos.opengles.GL11) {
            mgl11 = ((javax.microedition.khronos.opengles.GL11) (gl));
        }
        if (gl instanceof javax.microedition.khronos.opengles.GL11Ext) {
            mgl11Ext = ((javax.microedition.khronos.opengles.GL11Ext) (gl));
        }
        if (gl instanceof javax.microedition.khronos.opengles.GL11ExtensionPack) {
            mgl11ExtensionPack = ((javax.microedition.khronos.opengles.GL11ExtensionPack) (gl));
        }
    }

    protected javax.microedition.khronos.opengles.GL10 mgl;

    protected javax.microedition.khronos.opengles.GL10Ext mgl10Ext;

    protected javax.microedition.khronos.opengles.GL11 mgl11;

    protected javax.microedition.khronos.opengles.GL11Ext mgl11Ext;

    protected javax.microedition.khronos.opengles.GL11ExtensionPack mgl11ExtensionPack;
}

