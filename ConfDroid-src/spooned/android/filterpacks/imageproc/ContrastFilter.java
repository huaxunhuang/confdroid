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
public class ContrastFilter extends android.filterpacks.imageproc.SimpleImageFilter {
    private static final java.lang.String mContrastShader = "precision mediump float;\n" + ((((((((("uniform sampler2D tex_sampler_0;\n" + "uniform float contrast;\n") + "varying vec2 v_texcoord;\n") + "void main() {\n") + "  vec4 color = texture2D(tex_sampler_0, v_texcoord);\n") + "  color -= 0.5;\n") + "  color *= contrast;\n") + "  color += 0.5;\n") + "  gl_FragColor = color;\n")// this will clamp
     + "}\n");

    public ContrastFilter(java.lang.String name) {
        super(name, "contrast");
    }

    @java.lang.Override
    protected android.filterfw.core.Program getNativeProgram(android.filterfw.core.FilterContext context) {
        return new android.filterfw.core.NativeProgram("filterpack_imageproc", "contrast");
    }

    @java.lang.Override
    protected android.filterfw.core.Program getShaderProgram(android.filterfw.core.FilterContext context) {
        return new android.filterfw.core.ShaderProgram(context, android.filterpacks.imageproc.ContrastFilter.mContrastShader);
    }
}

