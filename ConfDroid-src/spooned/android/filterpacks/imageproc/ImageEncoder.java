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
package android.filterpacks.imageproc;


/**
 *
 *
 * @unknown 
 */
public class ImageEncoder extends android.filterfw.core.Filter {
    @android.filterfw.core.GenerateFieldPort(name = "stream")
    private java.io.OutputStream mOutputStream;

    @android.filterfw.core.GenerateFieldPort(name = "quality", hasDefault = true)
    private int mQuality = 80;

    public ImageEncoder(java.lang.String name) {
        super(name);
    }

    @java.lang.Override
    public void setupPorts() {
        addMaskedInputPort("image", android.filterfw.format.ImageFormat.create(android.filterfw.format.ImageFormat.COLORSPACE_RGBA, android.filterfw.core.FrameFormat.TARGET_UNSPECIFIED));
    }

    @java.lang.Override
    public void process(android.filterfw.core.FilterContext env) {
        android.filterfw.core.Frame input = pullInput("image");
        android.graphics.Bitmap bitmap = input.getBitmap();
        bitmap.compress(android.graphics.Bitmap.CompressFormat.JPEG, mQuality, mOutputStream);
    }
}

