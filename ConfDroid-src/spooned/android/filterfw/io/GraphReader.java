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
package android.filterfw.io;


/**
 *
 *
 * @unknown 
 */
public abstract class GraphReader {
    protected android.filterfw.core.KeyValueMap mReferences = new android.filterfw.core.KeyValueMap();

    public abstract android.filterfw.core.FilterGraph readGraphString(java.lang.String graphString) throws android.filterfw.io.GraphIOException;

    public abstract android.filterfw.core.KeyValueMap readKeyValueAssignments(java.lang.String assignments) throws android.filterfw.io.GraphIOException;

    public android.filterfw.core.FilterGraph readGraphResource(android.content.Context context, int resourceId) throws android.filterfw.io.GraphIOException {
        java.io.InputStream inputStream = context.getResources().openRawResource(resourceId);
        java.io.InputStreamReader reader = new java.io.InputStreamReader(inputStream);
        java.io.StringWriter writer = new java.io.StringWriter();
        char[] buffer = new char[1024];
        try {
            int bytesRead;
            while ((bytesRead = reader.read(buffer, 0, 1024)) > 0) {
                writer.write(buffer, 0, bytesRead);
            } 
        } catch (java.io.IOException e) {
            throw new java.lang.RuntimeException("Could not read specified resource file!");
        }
        return readGraphString(writer.toString());
    }

    public void addReference(java.lang.String name, java.lang.Object object) {
        mReferences.put(name, object);
    }

    public void addReferencesByMap(android.filterfw.core.KeyValueMap refs) {
        mReferences.putAll(refs);
    }

    public void addReferencesByKeysAndValues(java.lang.Object... references) {
        mReferences.setKeyValues(references);
    }
}

