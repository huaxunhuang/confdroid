/**
 * Copyright (C) 2012 The Android Open Source Project
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
package android.support.v8.renderscript;


/**
 * The superclass for all user-defined scripts. This is only
 * intended to be used by the generated derived classes.
 */
public class ScriptC extends android.support.v8.renderscript.Script {
    private static final java.lang.String TAG = "ScriptC";

    /**
     * Only intended for use by the generated derived classes.
     *
     * @param id
     * 		
     * @param rs
     * 		
     */
    protected ScriptC(long id, android.support.v8.renderscript.RenderScript rs) {
        super(id, rs);
    }

    /**
     * Only intended for use by the generated derived classes.
     *
     * @param rs
     * 		
     * @param resources
     * 		
     * @param resourceID
     * 		
     */
    protected ScriptC(android.support.v8.renderscript.RenderScript rs, android.content.res.Resources resources, int resourceID) {
        super(0, rs);
        long id = android.support.v8.renderscript.ScriptC.internalCreate(rs, resources, resourceID);
        if (id == 0) {
            throw new android.support.v8.renderscript.RSRuntimeException("Loading of ScriptC script failed.");
        }
        setID(id);
    }

    /**
     * Only intended for use by the generated derived classes.
     *
     * @param rs
     * 		
     * @param resName
     * 		
     * @param bitcode32
     * 		
     * @param bitcode64
     * 		
     */
    protected ScriptC(android.support.v8.renderscript.RenderScript rs, java.lang.String resName, byte[] bitcode32, byte[] bitcode64) {
        super(0, rs);
        long id = 0;
        if (android.support.v8.renderscript.RenderScript.sPointerSize == 4) {
            id = android.support.v8.renderscript.ScriptC.internalStringCreate(rs, resName, bitcode32);
        } else {
            id = android.support.v8.renderscript.ScriptC.internalStringCreate(rs, resName, bitcode64);
        }
        if (id == 0) {
            throw new android.support.v8.renderscript.RSRuntimeException("Loading of ScriptC script failed.");
        }
        setID(id);
    }

    private static synchronized long internalCreate(android.support.v8.renderscript.RenderScript rs, android.content.res.Resources resources, int resourceID) {
        byte[] pgm;
        int pgmLength;
        java.io.InputStream is = resources.openRawResource(resourceID);
        try {
            try {
                pgm = new byte[1024];
                pgmLength = 0;
                while (true) {
                    int bytesLeft = pgm.length - pgmLength;
                    if (bytesLeft == 0) {
                        byte[] buf2 = new byte[pgm.length * 2];
                        java.lang.System.arraycopy(pgm, 0, buf2, 0, pgm.length);
                        pgm = buf2;
                        bytesLeft = pgm.length - pgmLength;
                    }
                    int bytesRead = is.read(pgm, pgmLength, bytesLeft);
                    if (bytesRead <= 0) {
                        break;
                    }
                    pgmLength += bytesRead;
                } 
            } finally {
                is.close();
            }
        } catch (java.io.IOException e) {
            throw new android.content.res.Resources.NotFoundException();
        }
        java.lang.String resName = resources.getResourceEntryName(resourceID);
        java.lang.String cachePath = rs.getApplicationContext().getCacheDir().toString();
        // Log.v(TAG, "Create script for resource = " + resName + ", " + pgmLength + ", " + pgm);
        // Log.v(TAG, " path = " + cachePath);
        return rs.nScriptCCreate(resName, cachePath, pgm, pgmLength);
    }

    private static synchronized long internalStringCreate(android.support.v8.renderscript.RenderScript rs, java.lang.String resName, byte[] bitcode) {
        // Log.v(TAG, "Create script for resource = " + resName);
        java.lang.String cachePath = rs.getApplicationContext().getCacheDir().toString();
        return rs.nScriptCCreate(resName, cachePath, bitcode, bitcode.length);
    }
}

