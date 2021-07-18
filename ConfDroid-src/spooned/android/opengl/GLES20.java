/**
 * *
 * * Copyright 2009, The Android Open Source Project
 * *
 * * Licensed under the Apache License, Version 2.0 (the "License");
 * * you may not use this file except in compliance with the License.
 * * You may obtain a copy of the License at
 * *
 * *     http://www.apache.org/licenses/LICENSE-2.0
 * *
 * * Unless required by applicable law or agreed to in writing, software
 * * distributed under the License is distributed on an "AS IS" BASIS,
 * * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * * See the License for the specific language governing permissions and
 * * limitations under the License.
 */
/**
 * This source file is automatically generated
 */
package android.opengl;


/**
 * OpenGL ES 2.0
 */
public class GLES20 {
    public static final int GL_ACTIVE_TEXTURE = 0x84e0;

    public static final int GL_DEPTH_BUFFER_BIT = 0x100;

    public static final int GL_STENCIL_BUFFER_BIT = 0x400;

    public static final int GL_COLOR_BUFFER_BIT = 0x4000;

    public static final int GL_FALSE = 0;

    public static final int GL_TRUE = 1;

    public static final int GL_POINTS = 0x0;

    public static final int GL_LINES = 0x1;

    public static final int GL_LINE_LOOP = 0x2;

    public static final int GL_LINE_STRIP = 0x3;

    public static final int GL_TRIANGLES = 0x4;

    public static final int GL_TRIANGLE_STRIP = 0x5;

    public static final int GL_TRIANGLE_FAN = 0x6;

    public static final int GL_ZERO = 0;

    public static final int GL_ONE = 1;

    public static final int GL_SRC_COLOR = 0x300;

    public static final int GL_ONE_MINUS_SRC_COLOR = 0x301;

    public static final int GL_SRC_ALPHA = 0x302;

    public static final int GL_ONE_MINUS_SRC_ALPHA = 0x303;

    public static final int GL_DST_ALPHA = 0x304;

    public static final int GL_ONE_MINUS_DST_ALPHA = 0x305;

    public static final int GL_DST_COLOR = 0x306;

    public static final int GL_ONE_MINUS_DST_COLOR = 0x307;

    public static final int GL_SRC_ALPHA_SATURATE = 0x308;

    public static final int GL_FUNC_ADD = 0x8006;

    public static final int GL_BLEND_EQUATION = 0x8009;

    public static final int GL_BLEND_EQUATION_RGB = 0x8009;/* same as BLEND_EQUATION */


    public static final int GL_BLEND_EQUATION_ALPHA = 0x883d;

    public static final int GL_FUNC_SUBTRACT = 0x800a;

    public static final int GL_FUNC_REVERSE_SUBTRACT = 0x800b;

    public static final int GL_BLEND_DST_RGB = 0x80c8;

    public static final int GL_BLEND_SRC_RGB = 0x80c9;

    public static final int GL_BLEND_DST_ALPHA = 0x80ca;

    public static final int GL_BLEND_SRC_ALPHA = 0x80cb;

    public static final int GL_CONSTANT_COLOR = 0x8001;

    public static final int GL_ONE_MINUS_CONSTANT_COLOR = 0x8002;

    public static final int GL_CONSTANT_ALPHA = 0x8003;

    public static final int GL_ONE_MINUS_CONSTANT_ALPHA = 0x8004;

    public static final int GL_BLEND_COLOR = 0x8005;

    public static final int GL_ARRAY_BUFFER = 0x8892;

    public static final int GL_ELEMENT_ARRAY_BUFFER = 0x8893;

    public static final int GL_ARRAY_BUFFER_BINDING = 0x8894;

    public static final int GL_ELEMENT_ARRAY_BUFFER_BINDING = 0x8895;

    public static final int GL_STREAM_DRAW = 0x88e0;

    public static final int GL_STATIC_DRAW = 0x88e4;

    public static final int GL_DYNAMIC_DRAW = 0x88e8;

    public static final int GL_BUFFER_SIZE = 0x8764;

    public static final int GL_BUFFER_USAGE = 0x8765;

    public static final int GL_CURRENT_VERTEX_ATTRIB = 0x8626;

    public static final int GL_FRONT = 0x404;

    public static final int GL_BACK = 0x405;

    public static final int GL_FRONT_AND_BACK = 0x408;

    public static final int GL_TEXTURE_2D = 0xde1;

    public static final int GL_CULL_FACE = 0xb44;

    public static final int GL_BLEND = 0xbe2;

    public static final int GL_DITHER = 0xbd0;

    public static final int GL_STENCIL_TEST = 0xb90;

    public static final int GL_DEPTH_TEST = 0xb71;

    public static final int GL_SCISSOR_TEST = 0xc11;

    public static final int GL_POLYGON_OFFSET_FILL = 0x8037;

    public static final int GL_SAMPLE_ALPHA_TO_COVERAGE = 0x809e;

    public static final int GL_SAMPLE_COVERAGE = 0x80a0;

    public static final int GL_NO_ERROR = 0;

    public static final int GL_INVALID_ENUM = 0x500;

    public static final int GL_INVALID_VALUE = 0x501;

    public static final int GL_INVALID_OPERATION = 0x502;

    public static final int GL_OUT_OF_MEMORY = 0x505;

    public static final int GL_CW = 0x900;

    public static final int GL_CCW = 0x901;

    public static final int GL_LINE_WIDTH = 0xb21;

    public static final int GL_ALIASED_POINT_SIZE_RANGE = 0x846d;

    public static final int GL_ALIASED_LINE_WIDTH_RANGE = 0x846e;

    public static final int GL_CULL_FACE_MODE = 0xb45;

    public static final int GL_FRONT_FACE = 0xb46;

    public static final int GL_DEPTH_RANGE = 0xb70;

    public static final int GL_DEPTH_WRITEMASK = 0xb72;

    public static final int GL_DEPTH_CLEAR_VALUE = 0xb73;

    public static final int GL_DEPTH_FUNC = 0xb74;

    public static final int GL_STENCIL_CLEAR_VALUE = 0xb91;

    public static final int GL_STENCIL_FUNC = 0xb92;

    public static final int GL_STENCIL_FAIL = 0xb94;

    public static final int GL_STENCIL_PASS_DEPTH_FAIL = 0xb95;

    public static final int GL_STENCIL_PASS_DEPTH_PASS = 0xb96;

    public static final int GL_STENCIL_REF = 0xb97;

    public static final int GL_STENCIL_VALUE_MASK = 0xb93;

    public static final int GL_STENCIL_WRITEMASK = 0xb98;

    public static final int GL_STENCIL_BACK_FUNC = 0x8800;

    public static final int GL_STENCIL_BACK_FAIL = 0x8801;

    public static final int GL_STENCIL_BACK_PASS_DEPTH_FAIL = 0x8802;

    public static final int GL_STENCIL_BACK_PASS_DEPTH_PASS = 0x8803;

    public static final int GL_STENCIL_BACK_REF = 0x8ca3;

    public static final int GL_STENCIL_BACK_VALUE_MASK = 0x8ca4;

    public static final int GL_STENCIL_BACK_WRITEMASK = 0x8ca5;

    public static final int GL_VIEWPORT = 0xba2;

    public static final int GL_SCISSOR_BOX = 0xc10;

    public static final int GL_COLOR_CLEAR_VALUE = 0xc22;

    public static final int GL_COLOR_WRITEMASK = 0xc23;

    public static final int GL_UNPACK_ALIGNMENT = 0xcf5;

    public static final int GL_PACK_ALIGNMENT = 0xd05;

    public static final int GL_MAX_TEXTURE_SIZE = 0xd33;

    public static final int GL_MAX_VIEWPORT_DIMS = 0xd3a;

    public static final int GL_SUBPIXEL_BITS = 0xd50;

    public static final int GL_RED_BITS = 0xd52;

    public static final int GL_GREEN_BITS = 0xd53;

    public static final int GL_BLUE_BITS = 0xd54;

    public static final int GL_ALPHA_BITS = 0xd55;

    public static final int GL_DEPTH_BITS = 0xd56;

    public static final int GL_STENCIL_BITS = 0xd57;

    public static final int GL_POLYGON_OFFSET_UNITS = 0x2a00;

    public static final int GL_POLYGON_OFFSET_FACTOR = 0x8038;

    public static final int GL_TEXTURE_BINDING_2D = 0x8069;

    public static final int GL_SAMPLE_BUFFERS = 0x80a8;

    public static final int GL_SAMPLES = 0x80a9;

    public static final int GL_SAMPLE_COVERAGE_VALUE = 0x80aa;

    public static final int GL_SAMPLE_COVERAGE_INVERT = 0x80ab;

    public static final int GL_NUM_COMPRESSED_TEXTURE_FORMATS = 0x86a2;

    public static final int GL_COMPRESSED_TEXTURE_FORMATS = 0x86a3;

    public static final int GL_DONT_CARE = 0x1100;

    public static final int GL_FASTEST = 0x1101;

    public static final int GL_NICEST = 0x1102;

    public static final int GL_GENERATE_MIPMAP_HINT = 0x8192;

    public static final int GL_BYTE = 0x1400;

    public static final int GL_UNSIGNED_BYTE = 0x1401;

    public static final int GL_SHORT = 0x1402;

    public static final int GL_UNSIGNED_SHORT = 0x1403;

    public static final int GL_INT = 0x1404;

    public static final int GL_UNSIGNED_INT = 0x1405;

    public static final int GL_FLOAT = 0x1406;

    public static final int GL_FIXED = 0x140c;

    public static final int GL_DEPTH_COMPONENT = 0x1902;

    public static final int GL_ALPHA = 0x1906;

    public static final int GL_RGB = 0x1907;

    public static final int GL_RGBA = 0x1908;

    public static final int GL_LUMINANCE = 0x1909;

    public static final int GL_LUMINANCE_ALPHA = 0x190a;

    public static final int GL_UNSIGNED_SHORT_4_4_4_4 = 0x8033;

    public static final int GL_UNSIGNED_SHORT_5_5_5_1 = 0x8034;

    public static final int GL_UNSIGNED_SHORT_5_6_5 = 0x8363;

    public static final int GL_FRAGMENT_SHADER = 0x8b30;

    public static final int GL_VERTEX_SHADER = 0x8b31;

    public static final int GL_MAX_VERTEX_ATTRIBS = 0x8869;

    public static final int GL_MAX_VERTEX_UNIFORM_VECTORS = 0x8dfb;

    public static final int GL_MAX_VARYING_VECTORS = 0x8dfc;

    public static final int GL_MAX_COMBINED_TEXTURE_IMAGE_UNITS = 0x8b4d;

    public static final int GL_MAX_VERTEX_TEXTURE_IMAGE_UNITS = 0x8b4c;

    public static final int GL_MAX_TEXTURE_IMAGE_UNITS = 0x8872;

    public static final int GL_MAX_FRAGMENT_UNIFORM_VECTORS = 0x8dfd;

    public static final int GL_SHADER_TYPE = 0x8b4f;

    public static final int GL_DELETE_STATUS = 0x8b80;

    public static final int GL_LINK_STATUS = 0x8b82;

    public static final int GL_VALIDATE_STATUS = 0x8b83;

    public static final int GL_ATTACHED_SHADERS = 0x8b85;

    public static final int GL_ACTIVE_UNIFORMS = 0x8b86;

    public static final int GL_ACTIVE_UNIFORM_MAX_LENGTH = 0x8b87;

    public static final int GL_ACTIVE_ATTRIBUTES = 0x8b89;

    public static final int GL_ACTIVE_ATTRIBUTE_MAX_LENGTH = 0x8b8a;

    public static final int GL_SHADING_LANGUAGE_VERSION = 0x8b8c;

    public static final int GL_CURRENT_PROGRAM = 0x8b8d;

    public static final int GL_NEVER = 0x200;

    public static final int GL_LESS = 0x201;

    public static final int GL_EQUAL = 0x202;

    public static final int GL_LEQUAL = 0x203;

    public static final int GL_GREATER = 0x204;

    public static final int GL_NOTEQUAL = 0x205;

    public static final int GL_GEQUAL = 0x206;

    public static final int GL_ALWAYS = 0x207;

    public static final int GL_KEEP = 0x1e00;

    public static final int GL_REPLACE = 0x1e01;

    public static final int GL_INCR = 0x1e02;

    public static final int GL_DECR = 0x1e03;

    public static final int GL_INVERT = 0x150a;

    public static final int GL_INCR_WRAP = 0x8507;

    public static final int GL_DECR_WRAP = 0x8508;

    public static final int GL_VENDOR = 0x1f00;

    public static final int GL_RENDERER = 0x1f01;

    public static final int GL_VERSION = 0x1f02;

    public static final int GL_EXTENSIONS = 0x1f03;

    public static final int GL_NEAREST = 0x2600;

    public static final int GL_LINEAR = 0x2601;

    public static final int GL_NEAREST_MIPMAP_NEAREST = 0x2700;

    public static final int GL_LINEAR_MIPMAP_NEAREST = 0x2701;

    public static final int GL_NEAREST_MIPMAP_LINEAR = 0x2702;

    public static final int GL_LINEAR_MIPMAP_LINEAR = 0x2703;

    public static final int GL_TEXTURE_MAG_FILTER = 0x2800;

    public static final int GL_TEXTURE_MIN_FILTER = 0x2801;

    public static final int GL_TEXTURE_WRAP_S = 0x2802;

    public static final int GL_TEXTURE_WRAP_T = 0x2803;

    public static final int GL_TEXTURE = 0x1702;

    public static final int GL_TEXTURE_CUBE_MAP = 0x8513;

    public static final int GL_TEXTURE_BINDING_CUBE_MAP = 0x8514;

    public static final int GL_TEXTURE_CUBE_MAP_POSITIVE_X = 0x8515;

    public static final int GL_TEXTURE_CUBE_MAP_NEGATIVE_X = 0x8516;

    public static final int GL_TEXTURE_CUBE_MAP_POSITIVE_Y = 0x8517;

    public static final int GL_TEXTURE_CUBE_MAP_NEGATIVE_Y = 0x8518;

    public static final int GL_TEXTURE_CUBE_MAP_POSITIVE_Z = 0x8519;

    public static final int GL_TEXTURE_CUBE_MAP_NEGATIVE_Z = 0x851a;

    public static final int GL_MAX_CUBE_MAP_TEXTURE_SIZE = 0x851c;

    public static final int GL_TEXTURE0 = 0x84c0;

    public static final int GL_TEXTURE1 = 0x84c1;

    public static final int GL_TEXTURE2 = 0x84c2;

    public static final int GL_TEXTURE3 = 0x84c3;

    public static final int GL_TEXTURE4 = 0x84c4;

    public static final int GL_TEXTURE5 = 0x84c5;

    public static final int GL_TEXTURE6 = 0x84c6;

    public static final int GL_TEXTURE7 = 0x84c7;

    public static final int GL_TEXTURE8 = 0x84c8;

    public static final int GL_TEXTURE9 = 0x84c9;

    public static final int GL_TEXTURE10 = 0x84ca;

    public static final int GL_TEXTURE11 = 0x84cb;

    public static final int GL_TEXTURE12 = 0x84cc;

    public static final int GL_TEXTURE13 = 0x84cd;

    public static final int GL_TEXTURE14 = 0x84ce;

    public static final int GL_TEXTURE15 = 0x84cf;

    public static final int GL_TEXTURE16 = 0x84d0;

    public static final int GL_TEXTURE17 = 0x84d1;

    public static final int GL_TEXTURE18 = 0x84d2;

    public static final int GL_TEXTURE19 = 0x84d3;

    public static final int GL_TEXTURE20 = 0x84d4;

    public static final int GL_TEXTURE21 = 0x84d5;

    public static final int GL_TEXTURE22 = 0x84d6;

    public static final int GL_TEXTURE23 = 0x84d7;

    public static final int GL_TEXTURE24 = 0x84d8;

    public static final int GL_TEXTURE25 = 0x84d9;

    public static final int GL_TEXTURE26 = 0x84da;

    public static final int GL_TEXTURE27 = 0x84db;

    public static final int GL_TEXTURE28 = 0x84dc;

    public static final int GL_TEXTURE29 = 0x84dd;

    public static final int GL_TEXTURE30 = 0x84de;

    public static final int GL_TEXTURE31 = 0x84df;

    public static final int GL_REPEAT = 0x2901;

    public static final int GL_CLAMP_TO_EDGE = 0x812f;

    public static final int GL_MIRRORED_REPEAT = 0x8370;

    public static final int GL_FLOAT_VEC2 = 0x8b50;

    public static final int GL_FLOAT_VEC3 = 0x8b51;

    public static final int GL_FLOAT_VEC4 = 0x8b52;

    public static final int GL_INT_VEC2 = 0x8b53;

    public static final int GL_INT_VEC3 = 0x8b54;

    public static final int GL_INT_VEC4 = 0x8b55;

    public static final int GL_BOOL = 0x8b56;

    public static final int GL_BOOL_VEC2 = 0x8b57;

    public static final int GL_BOOL_VEC3 = 0x8b58;

    public static final int GL_BOOL_VEC4 = 0x8b59;

    public static final int GL_FLOAT_MAT2 = 0x8b5a;

    public static final int GL_FLOAT_MAT3 = 0x8b5b;

    public static final int GL_FLOAT_MAT4 = 0x8b5c;

    public static final int GL_SAMPLER_2D = 0x8b5e;

    public static final int GL_SAMPLER_CUBE = 0x8b60;

    public static final int GL_VERTEX_ATTRIB_ARRAY_ENABLED = 0x8622;

    public static final int GL_VERTEX_ATTRIB_ARRAY_SIZE = 0x8623;

    public static final int GL_VERTEX_ATTRIB_ARRAY_STRIDE = 0x8624;

    public static final int GL_VERTEX_ATTRIB_ARRAY_TYPE = 0x8625;

    public static final int GL_VERTEX_ATTRIB_ARRAY_NORMALIZED = 0x886a;

    public static final int GL_VERTEX_ATTRIB_ARRAY_POINTER = 0x8645;

    public static final int GL_VERTEX_ATTRIB_ARRAY_BUFFER_BINDING = 0x889f;

    public static final int GL_IMPLEMENTATION_COLOR_READ_TYPE = 0x8b9a;

    public static final int GL_IMPLEMENTATION_COLOR_READ_FORMAT = 0x8b9b;

    public static final int GL_COMPILE_STATUS = 0x8b81;

    public static final int GL_INFO_LOG_LENGTH = 0x8b84;

    public static final int GL_SHADER_SOURCE_LENGTH = 0x8b88;

    public static final int GL_SHADER_COMPILER = 0x8dfa;

    public static final int GL_SHADER_BINARY_FORMATS = 0x8df8;

    public static final int GL_NUM_SHADER_BINARY_FORMATS = 0x8df9;

    public static final int GL_LOW_FLOAT = 0x8df0;

    public static final int GL_MEDIUM_FLOAT = 0x8df1;

    public static final int GL_HIGH_FLOAT = 0x8df2;

    public static final int GL_LOW_INT = 0x8df3;

    public static final int GL_MEDIUM_INT = 0x8df4;

    public static final int GL_HIGH_INT = 0x8df5;

    public static final int GL_FRAMEBUFFER = 0x8d40;

    public static final int GL_RENDERBUFFER = 0x8d41;

    public static final int GL_RGBA4 = 0x8056;

    public static final int GL_RGB5_A1 = 0x8057;

    public static final int GL_RGB565 = 0x8d62;

    public static final int GL_DEPTH_COMPONENT16 = 0x81a5;

    // GL_STENCIL_INDEX does not appear in gl2.h or gl2ext.h, and there is no
    // token with value 0x1901.
    // 
    @java.lang.Deprecated
    public static final int GL_STENCIL_INDEX = 0x1901;

    public static final int GL_STENCIL_INDEX8 = 0x8d48;

    public static final int GL_RENDERBUFFER_WIDTH = 0x8d42;

    public static final int GL_RENDERBUFFER_HEIGHT = 0x8d43;

    public static final int GL_RENDERBUFFER_INTERNAL_FORMAT = 0x8d44;

    public static final int GL_RENDERBUFFER_RED_SIZE = 0x8d50;

    public static final int GL_RENDERBUFFER_GREEN_SIZE = 0x8d51;

    public static final int GL_RENDERBUFFER_BLUE_SIZE = 0x8d52;

    public static final int GL_RENDERBUFFER_ALPHA_SIZE = 0x8d53;

    public static final int GL_RENDERBUFFER_DEPTH_SIZE = 0x8d54;

    public static final int GL_RENDERBUFFER_STENCIL_SIZE = 0x8d55;

    public static final int GL_FRAMEBUFFER_ATTACHMENT_OBJECT_TYPE = 0x8cd0;

    public static final int GL_FRAMEBUFFER_ATTACHMENT_OBJECT_NAME = 0x8cd1;

    public static final int GL_FRAMEBUFFER_ATTACHMENT_TEXTURE_LEVEL = 0x8cd2;

    public static final int GL_FRAMEBUFFER_ATTACHMENT_TEXTURE_CUBE_MAP_FACE = 0x8cd3;

    public static final int GL_COLOR_ATTACHMENT0 = 0x8ce0;

    public static final int GL_DEPTH_ATTACHMENT = 0x8d00;

    public static final int GL_STENCIL_ATTACHMENT = 0x8d20;

    public static final int GL_NONE = 0;

    public static final int GL_FRAMEBUFFER_COMPLETE = 0x8cd5;

    public static final int GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT = 0x8cd6;

    public static final int GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT = 0x8cd7;

    public static final int GL_FRAMEBUFFER_INCOMPLETE_DIMENSIONS = 0x8cd9;

    public static final int GL_FRAMEBUFFER_UNSUPPORTED = 0x8cdd;

    public static final int GL_FRAMEBUFFER_BINDING = 0x8ca6;

    public static final int GL_RENDERBUFFER_BINDING = 0x8ca7;

    public static final int GL_MAX_RENDERBUFFER_SIZE = 0x84e8;

    public static final int GL_INVALID_FRAMEBUFFER_OPERATION = 0x506;

    private static native void _nativeClassInit();

    static {
        android.opengl.GLES20._nativeClassInit();
    }

    // C function void glActiveTexture ( GLenum texture )
    public static native void glActiveTexture(int texture);

    // C function void glAttachShader ( GLuint program, GLuint shader )
    public static native void glAttachShader(int program, int shader);

    // C function void glBindAttribLocation ( GLuint program, GLuint index, const char *name )
    public static native void glBindAttribLocation(int program, int index, java.lang.String name);

    // C function void glBindBuffer ( GLenum target, GLuint buffer )
    public static native void glBindBuffer(int target, int buffer);

    // C function void glBindFramebuffer ( GLenum target, GLuint framebuffer )
    public static native void glBindFramebuffer(int target, int framebuffer);

    // C function void glBindRenderbuffer ( GLenum target, GLuint renderbuffer )
    public static native void glBindRenderbuffer(int target, int renderbuffer);

    // C function void glBindTexture ( GLenum target, GLuint texture )
    public static native void glBindTexture(int target, int texture);

    // C function void glBlendColor ( GLclampf red, GLclampf green, GLclampf blue, GLclampf alpha )
    public static native void glBlendColor(float red, float green, float blue, float alpha);

    // C function void glBlendEquation ( GLenum mode )
    public static native void glBlendEquation(int mode);

    // C function void glBlendEquationSeparate ( GLenum modeRGB, GLenum modeAlpha )
    public static native void glBlendEquationSeparate(int modeRGB, int modeAlpha);

    // C function void glBlendFunc ( GLenum sfactor, GLenum dfactor )
    public static native void glBlendFunc(int sfactor, int dfactor);

    // C function void glBlendFuncSeparate ( GLenum srcRGB, GLenum dstRGB, GLenum srcAlpha, GLenum dstAlpha )
    public static native void glBlendFuncSeparate(int srcRGB, int dstRGB, int srcAlpha, int dstAlpha);

    // C function void glBufferData ( GLenum target, GLsizeiptr size, const GLvoid *data, GLenum usage )
    public static native void glBufferData(int target, int size, java.nio.Buffer data, int usage);

    // C function void glBufferSubData ( GLenum target, GLintptr offset, GLsizeiptr size, const GLvoid *data )
    public static native void glBufferSubData(int target, int offset, int size, java.nio.Buffer data);

    // C function GLenum glCheckFramebufferStatus ( GLenum target )
    public static native int glCheckFramebufferStatus(int target);

    // C function void glClear ( GLbitfield mask )
    public static native void glClear(int mask);

    // C function void glClearColor ( GLclampf red, GLclampf green, GLclampf blue, GLclampf alpha )
    public static native void glClearColor(float red, float green, float blue, float alpha);

    // C function void glClearDepthf ( GLclampf depth )
    public static native void glClearDepthf(float depth);

    // C function void glClearStencil ( GLint s )
    public static native void glClearStencil(int s);

    // C function void glColorMask ( GLboolean red, GLboolean green, GLboolean blue, GLboolean alpha )
    public static native void glColorMask(boolean red, boolean green, boolean blue, boolean alpha);

    // C function void glCompileShader ( GLuint shader )
    public static native void glCompileShader(int shader);

    // C function void glCompressedTexImage2D ( GLenum target, GLint level, GLenum internalformat, GLsizei width, GLsizei height, GLint border, GLsizei imageSize, const GLvoid *data )
    public static native void glCompressedTexImage2D(int target, int level, int internalformat, int width, int height, int border, int imageSize, java.nio.Buffer data);

    // C function void glCompressedTexSubImage2D ( GLenum target, GLint level, GLint xoffset, GLint yoffset, GLsizei width, GLsizei height, GLenum format, GLsizei imageSize, const GLvoid *data )
    public static native void glCompressedTexSubImage2D(int target, int level, int xoffset, int yoffset, int width, int height, int format, int imageSize, java.nio.Buffer data);

    // C function void glCopyTexImage2D ( GLenum target, GLint level, GLenum internalformat, GLint x, GLint y, GLsizei width, GLsizei height, GLint border )
    public static native void glCopyTexImage2D(int target, int level, int internalformat, int x, int y, int width, int height, int border);

    // C function void glCopyTexSubImage2D ( GLenum target, GLint level, GLint xoffset, GLint yoffset, GLint x, GLint y, GLsizei width, GLsizei height )
    public static native void glCopyTexSubImage2D(int target, int level, int xoffset, int yoffset, int x, int y, int width, int height);

    // C function GLuint glCreateProgram ( void )
    public static native int glCreateProgram();

    // C function GLuint glCreateShader ( GLenum type )
    public static native int glCreateShader(int type);

    // C function void glCullFace ( GLenum mode )
    public static native void glCullFace(int mode);

    // C function void glDeleteBuffers ( GLsizei n, const GLuint *buffers )
    public static native void glDeleteBuffers(int n, int[] buffers, int offset);

    // C function void glDeleteBuffers ( GLsizei n, const GLuint *buffers )
    public static native void glDeleteBuffers(int n, java.nio.IntBuffer buffers);

    // C function void glDeleteFramebuffers ( GLsizei n, const GLuint *framebuffers )
    public static native void glDeleteFramebuffers(int n, int[] framebuffers, int offset);

    // C function void glDeleteFramebuffers ( GLsizei n, const GLuint *framebuffers )
    public static native void glDeleteFramebuffers(int n, java.nio.IntBuffer framebuffers);

    // C function void glDeleteProgram ( GLuint program )
    public static native void glDeleteProgram(int program);

    // C function void glDeleteRenderbuffers ( GLsizei n, const GLuint *renderbuffers )
    public static native void glDeleteRenderbuffers(int n, int[] renderbuffers, int offset);

    // C function void glDeleteRenderbuffers ( GLsizei n, const GLuint *renderbuffers )
    public static native void glDeleteRenderbuffers(int n, java.nio.IntBuffer renderbuffers);

    // C function void glDeleteShader ( GLuint shader )
    public static native void glDeleteShader(int shader);

    // C function void glDeleteTextures ( GLsizei n, const GLuint *textures )
    public static native void glDeleteTextures(int n, int[] textures, int offset);

    // C function void glDeleteTextures ( GLsizei n, const GLuint *textures )
    public static native void glDeleteTextures(int n, java.nio.IntBuffer textures);

    // C function void glDepthFunc ( GLenum func )
    public static native void glDepthFunc(int func);

    // C function void glDepthMask ( GLboolean flag )
    public static native void glDepthMask(boolean flag);

    // C function void glDepthRangef ( GLclampf zNear, GLclampf zFar )
    public static native void glDepthRangef(float zNear, float zFar);

    // C function void glDetachShader ( GLuint program, GLuint shader )
    public static native void glDetachShader(int program, int shader);

    // C function void glDisable ( GLenum cap )
    public static native void glDisable(int cap);

    // C function void glDisableVertexAttribArray ( GLuint index )
    public static native void glDisableVertexAttribArray(int index);

    // C function void glDrawArrays ( GLenum mode, GLint first, GLsizei count )
    public static native void glDrawArrays(int mode, int first, int count);

    // C function void glDrawElements ( GLenum mode, GLsizei count, GLenum type, GLint offset )
    public static native void glDrawElements(int mode, int count, int type, int offset);

    // C function void glDrawElements ( GLenum mode, GLsizei count, GLenum type, const GLvoid *indices )
    public static native void glDrawElements(int mode, int count, int type, java.nio.Buffer indices);

    // C function void glEnable ( GLenum cap )
    public static native void glEnable(int cap);

    // C function void glEnableVertexAttribArray ( GLuint index )
    public static native void glEnableVertexAttribArray(int index);

    // C function void glFinish ( void )
    public static native void glFinish();

    // C function void glFlush ( void )
    public static native void glFlush();

    // C function void glFramebufferRenderbuffer ( GLenum target, GLenum attachment, GLenum renderbuffertarget, GLuint renderbuffer )
    public static native void glFramebufferRenderbuffer(int target, int attachment, int renderbuffertarget, int renderbuffer);

    // C function void glFramebufferTexture2D ( GLenum target, GLenum attachment, GLenum textarget, GLuint texture, GLint level )
    public static native void glFramebufferTexture2D(int target, int attachment, int textarget, int texture, int level);

    // C function void glFrontFace ( GLenum mode )
    public static native void glFrontFace(int mode);

    // C function void glGenBuffers ( GLsizei n, GLuint *buffers )
    public static native void glGenBuffers(int n, int[] buffers, int offset);

    // C function void glGenBuffers ( GLsizei n, GLuint *buffers )
    public static native void glGenBuffers(int n, java.nio.IntBuffer buffers);

    // C function void glGenerateMipmap ( GLenum target )
    public static native void glGenerateMipmap(int target);

    // C function void glGenFramebuffers ( GLsizei n, GLuint *framebuffers )
    public static native void glGenFramebuffers(int n, int[] framebuffers, int offset);

    // C function void glGenFramebuffers ( GLsizei n, GLuint *framebuffers )
    public static native void glGenFramebuffers(int n, java.nio.IntBuffer framebuffers);

    // C function void glGenRenderbuffers ( GLsizei n, GLuint *renderbuffers )
    public static native void glGenRenderbuffers(int n, int[] renderbuffers, int offset);

    // C function void glGenRenderbuffers ( GLsizei n, GLuint *renderbuffers )
    public static native void glGenRenderbuffers(int n, java.nio.IntBuffer renderbuffers);

    // C function void glGenTextures ( GLsizei n, GLuint *textures )
    public static native void glGenTextures(int n, int[] textures, int offset);

    // C function void glGenTextures ( GLsizei n, GLuint *textures )
    public static native void glGenTextures(int n, java.nio.IntBuffer textures);

    // C function void glGetActiveAttrib ( GLuint program, GLuint index, GLsizei bufsize, GLsizei *length, GLint *size, GLenum *type, char *name )
    public static native void glGetActiveAttrib(int program, int index, int bufsize, int[] length, int lengthOffset, int[] size, int sizeOffset, int[] type, int typeOffset, byte[] name, int nameOffset);

    // C function void glGetActiveAttrib ( GLuint program, GLuint index, GLsizei bufsize, GLsizei *length, GLint *size, GLenum *type, char *name )
    /**
     *
     *
     * @unknown Method is broken, but used to be public (b/6006380)
     */
    public static native void glGetActiveAttrib(int program, int index, int bufsize, java.nio.IntBuffer length, java.nio.IntBuffer size, java.nio.IntBuffer type, byte name);

    // C function void glGetActiveAttrib ( GLuint program, GLuint index, GLsizei bufsize, GLsizei *length, GLint *size, GLenum *type, char *name )
    public static native java.lang.String glGetActiveAttrib(int program, int index, int[] size, int sizeOffset, int[] type, int typeOffset);

    // C function void glGetActiveAttrib ( GLuint program, GLuint index, GLsizei bufsize, GLsizei *length, GLint *size, GLenum *type, char *name )
    public static native java.lang.String glGetActiveAttrib(int program, int index, java.nio.IntBuffer size, java.nio.IntBuffer type);

    // C function void glGetActiveUniform ( GLuint program, GLuint index, GLsizei bufsize, GLsizei *length, GLint *size, GLenum *type, char *name )
    public static native void glGetActiveUniform(int program, int index, int bufsize, int[] length, int lengthOffset, int[] size, int sizeOffset, int[] type, int typeOffset, byte[] name, int nameOffset);

    // C function void glGetActiveUniform ( GLuint program, GLuint index, GLsizei bufsize, GLsizei *length, GLint *size, GLenum *type, char *name )
    /**
     *
     *
     * @unknown Method is broken, but used to be public (b/6006380)
     */
    public static native void glGetActiveUniform(int program, int index, int bufsize, java.nio.IntBuffer length, java.nio.IntBuffer size, java.nio.IntBuffer type, byte name);

    // C function void glGetActiveUniform ( GLuint program, GLuint index, GLsizei bufsize, GLsizei *length, GLint *size, GLenum *type, char *name )
    public static native java.lang.String glGetActiveUniform(int program, int index, int[] size, int sizeOffset, int[] type, int typeOffset);

    // C function void glGetActiveUniform ( GLuint program, GLuint index, GLsizei bufsize, GLsizei *length, GLint *size, GLenum *type, char *name )
    public static native java.lang.String glGetActiveUniform(int program, int index, java.nio.IntBuffer size, java.nio.IntBuffer type);

    // C function void glGetAttachedShaders ( GLuint program, GLsizei maxcount, GLsizei *count, GLuint *shaders )
    public static native void glGetAttachedShaders(int program, int maxcount, int[] count, int countOffset, int[] shaders, int shadersOffset);

    // C function void glGetAttachedShaders ( GLuint program, GLsizei maxcount, GLsizei *count, GLuint *shaders )
    public static native void glGetAttachedShaders(int program, int maxcount, java.nio.IntBuffer count, java.nio.IntBuffer shaders);

    // C function GLint glGetAttribLocation ( GLuint program, const char *name )
    public static native int glGetAttribLocation(int program, java.lang.String name);

    // C function void glGetBooleanv ( GLenum pname, GLboolean *params )
    public static native void glGetBooleanv(int pname, boolean[] params, int offset);

    // C function void glGetBooleanv ( GLenum pname, GLboolean *params )
    public static native void glGetBooleanv(int pname, java.nio.IntBuffer params);

    // C function void glGetBufferParameteriv ( GLenum target, GLenum pname, GLint *params )
    public static native void glGetBufferParameteriv(int target, int pname, int[] params, int offset);

    // C function void glGetBufferParameteriv ( GLenum target, GLenum pname, GLint *params )
    public static native void glGetBufferParameteriv(int target, int pname, java.nio.IntBuffer params);

    // C function GLenum glGetError ( void )
    public static native int glGetError();

    // C function void glGetFloatv ( GLenum pname, GLfloat *params )
    public static native void glGetFloatv(int pname, float[] params, int offset);

    // C function void glGetFloatv ( GLenum pname, GLfloat *params )
    public static native void glGetFloatv(int pname, java.nio.FloatBuffer params);

    // C function void glGetFramebufferAttachmentParameteriv ( GLenum target, GLenum attachment, GLenum pname, GLint *params )
    public static native void glGetFramebufferAttachmentParameteriv(int target, int attachment, int pname, int[] params, int offset);

    // C function void glGetFramebufferAttachmentParameteriv ( GLenum target, GLenum attachment, GLenum pname, GLint *params )
    public static native void glGetFramebufferAttachmentParameteriv(int target, int attachment, int pname, java.nio.IntBuffer params);

    // C function void glGetIntegerv ( GLenum pname, GLint *params )
    public static native void glGetIntegerv(int pname, int[] params, int offset);

    // C function void glGetIntegerv ( GLenum pname, GLint *params )
    public static native void glGetIntegerv(int pname, java.nio.IntBuffer params);

    // C function void glGetProgramiv ( GLuint program, GLenum pname, GLint *params )
    public static native void glGetProgramiv(int program, int pname, int[] params, int offset);

    // C function void glGetProgramiv ( GLuint program, GLenum pname, GLint *params )
    public static native void glGetProgramiv(int program, int pname, java.nio.IntBuffer params);

    // C function void glGetProgramInfoLog( GLuint program, GLsizei maxLength, GLsizei * length,
    // GLchar * infoLog);
    public static native java.lang.String glGetProgramInfoLog(int program);

    // C function void glGetRenderbufferParameteriv ( GLenum target, GLenum pname, GLint *params )
    public static native void glGetRenderbufferParameteriv(int target, int pname, int[] params, int offset);

    // C function void glGetRenderbufferParameteriv ( GLenum target, GLenum pname, GLint *params )
    public static native void glGetRenderbufferParameteriv(int target, int pname, java.nio.IntBuffer params);

    // C function void glGetShaderiv ( GLuint shader, GLenum pname, GLint *params )
    public static native void glGetShaderiv(int shader, int pname, int[] params, int offset);

    // C function void glGetShaderiv ( GLuint shader, GLenum pname, GLint *params )
    public static native void glGetShaderiv(int shader, int pname, java.nio.IntBuffer params);

    // C function void glGetShaderInfoLog( GLuint shader, GLsizei maxLength, GLsizei * length,
    // GLchar * infoLog);
    public static native java.lang.String glGetShaderInfoLog(int shader);

    // C function void glGetShaderPrecisionFormat ( GLenum shadertype, GLenum precisiontype, GLint *range, GLint *precision )
    public static native void glGetShaderPrecisionFormat(int shadertype, int precisiontype, int[] range, int rangeOffset, int[] precision, int precisionOffset);

    // C function void glGetShaderPrecisionFormat ( GLenum shadertype, GLenum precisiontype, GLint *range, GLint *precision )
    public static native void glGetShaderPrecisionFormat(int shadertype, int precisiontype, java.nio.IntBuffer range, java.nio.IntBuffer precision);

    // C function void glGetShaderSource ( GLuint shader, GLsizei bufsize, GLsizei *length, char *source )
    public static native void glGetShaderSource(int shader, int bufsize, int[] length, int lengthOffset, byte[] source, int sourceOffset);

    // C function void glGetShaderSource ( GLuint shader, GLsizei bufsize, GLsizei *length, char *source )
    /**
     *
     *
     * @unknown Method is broken, but used to be public (b/6006380)
     */
    public static native void glGetShaderSource(int shader, int bufsize, java.nio.IntBuffer length, byte source);

    // C function void glGetShaderSource ( GLuint shader, GLsizei bufsize, GLsizei *length, char *source )
    public static native java.lang.String glGetShaderSource(int shader);

    // C function const GLubyte * glGetString ( GLenum name )
    public static native java.lang.String glGetString(int name);

    // C function void glGetTexParameterfv ( GLenum target, GLenum pname, GLfloat *params )
    public static native void glGetTexParameterfv(int target, int pname, float[] params, int offset);

    // C function void glGetTexParameterfv ( GLenum target, GLenum pname, GLfloat *params )
    public static native void glGetTexParameterfv(int target, int pname, java.nio.FloatBuffer params);

    // C function void glGetTexParameteriv ( GLenum target, GLenum pname, GLint *params )
    public static native void glGetTexParameteriv(int target, int pname, int[] params, int offset);

    // C function void glGetTexParameteriv ( GLenum target, GLenum pname, GLint *params )
    public static native void glGetTexParameteriv(int target, int pname, java.nio.IntBuffer params);

    // C function void glGetUniformfv ( GLuint program, GLint location, GLfloat *params )
    public static native void glGetUniformfv(int program, int location, float[] params, int offset);

    // C function void glGetUniformfv ( GLuint program, GLint location, GLfloat *params )
    public static native void glGetUniformfv(int program, int location, java.nio.FloatBuffer params);

    // C function void glGetUniformiv ( GLuint program, GLint location, GLint *params )
    public static native void glGetUniformiv(int program, int location, int[] params, int offset);

    // C function void glGetUniformiv ( GLuint program, GLint location, GLint *params )
    public static native void glGetUniformiv(int program, int location, java.nio.IntBuffer params);

    // C function GLint glGetUniformLocation ( GLuint program, const char *name )
    public static native int glGetUniformLocation(int program, java.lang.String name);

    // C function void glGetVertexAttribfv ( GLuint index, GLenum pname, GLfloat *params )
    public static native void glGetVertexAttribfv(int index, int pname, float[] params, int offset);

    // C function void glGetVertexAttribfv ( GLuint index, GLenum pname, GLfloat *params )
    public static native void glGetVertexAttribfv(int index, int pname, java.nio.FloatBuffer params);

    // C function void glGetVertexAttribiv ( GLuint index, GLenum pname, GLint *params )
    public static native void glGetVertexAttribiv(int index, int pname, int[] params, int offset);

    // C function void glGetVertexAttribiv ( GLuint index, GLenum pname, GLint *params )
    public static native void glGetVertexAttribiv(int index, int pname, java.nio.IntBuffer params);

    // C function void glHint ( GLenum target, GLenum mode )
    public static native void glHint(int target, int mode);

    // C function GLboolean glIsBuffer ( GLuint buffer )
    public static native boolean glIsBuffer(int buffer);

    // C function GLboolean glIsEnabled ( GLenum cap )
    public static native boolean glIsEnabled(int cap);

    // C function GLboolean glIsFramebuffer ( GLuint framebuffer )
    public static native boolean glIsFramebuffer(int framebuffer);

    // C function GLboolean glIsProgram ( GLuint program )
    public static native boolean glIsProgram(int program);

    // C function GLboolean glIsRenderbuffer ( GLuint renderbuffer )
    public static native boolean glIsRenderbuffer(int renderbuffer);

    // C function GLboolean glIsShader ( GLuint shader )
    public static native boolean glIsShader(int shader);

    // C function GLboolean glIsTexture ( GLuint texture )
    public static native boolean glIsTexture(int texture);

    // C function void glLineWidth ( GLfloat width )
    public static native void glLineWidth(float width);

    // C function void glLinkProgram ( GLuint program )
    public static native void glLinkProgram(int program);

    // C function void glPixelStorei ( GLenum pname, GLint param )
    public static native void glPixelStorei(int pname, int param);

    // C function void glPolygonOffset ( GLfloat factor, GLfloat units )
    public static native void glPolygonOffset(float factor, float units);

    // C function void glReadPixels ( GLint x, GLint y, GLsizei width, GLsizei height, GLenum format, GLenum type, GLvoid *pixels )
    public static native void glReadPixels(int x, int y, int width, int height, int format, int type, java.nio.Buffer pixels);

    // C function void glReleaseShaderCompiler ( void )
    public static native void glReleaseShaderCompiler();

    // C function void glRenderbufferStorage ( GLenum target, GLenum internalformat, GLsizei width, GLsizei height )
    public static native void glRenderbufferStorage(int target, int internalformat, int width, int height);

    // C function void glSampleCoverage ( GLclampf value, GLboolean invert )
    public static native void glSampleCoverage(float value, boolean invert);

    // C function void glScissor ( GLint x, GLint y, GLsizei width, GLsizei height )
    public static native void glScissor(int x, int y, int width, int height);

    // C function void glShaderBinary ( GLsizei n, const GLuint *shaders, GLenum binaryformat, const GLvoid *binary, GLsizei length )
    public static native void glShaderBinary(int n, int[] shaders, int offset, int binaryformat, java.nio.Buffer binary, int length);

    // C function void glShaderBinary ( GLsizei n, const GLuint *shaders, GLenum binaryformat, const GLvoid *binary, GLsizei length )
    public static native void glShaderBinary(int n, java.nio.IntBuffer shaders, int binaryformat, java.nio.Buffer binary, int length);

    // C function void glShaderSource ( GLuint shader, GLsizei count, const GLchar ** string, const GLint* length )
    public static native void glShaderSource(int shader, java.lang.String string);

    // C function void glStencilFunc ( GLenum func, GLint ref, GLuint mask )
    public static native void glStencilFunc(int func, int ref, int mask);

    // C function void glStencilFuncSeparate ( GLenum face, GLenum func, GLint ref, GLuint mask )
    public static native void glStencilFuncSeparate(int face, int func, int ref, int mask);

    // C function void glStencilMask ( GLuint mask )
    public static native void glStencilMask(int mask);

    // C function void glStencilMaskSeparate ( GLenum face, GLuint mask )
    public static native void glStencilMaskSeparate(int face, int mask);

    // C function void glStencilOp ( GLenum fail, GLenum zfail, GLenum zpass )
    public static native void glStencilOp(int fail, int zfail, int zpass);

    // C function void glStencilOpSeparate ( GLenum face, GLenum fail, GLenum zfail, GLenum zpass )
    public static native void glStencilOpSeparate(int face, int fail, int zfail, int zpass);

    // C function void glTexImage2D ( GLenum target, GLint level, GLint internalformat, GLsizei width, GLsizei height, GLint border, GLenum format, GLenum type, const GLvoid *pixels )
    public static native void glTexImage2D(int target, int level, int internalformat, int width, int height, int border, int format, int type, java.nio.Buffer pixels);

    // C function void glTexParameterf ( GLenum target, GLenum pname, GLfloat param )
    public static native void glTexParameterf(int target, int pname, float param);

    // C function void glTexParameterfv ( GLenum target, GLenum pname, const GLfloat *params )
    public static native void glTexParameterfv(int target, int pname, float[] params, int offset);

    // C function void glTexParameterfv ( GLenum target, GLenum pname, const GLfloat *params )
    public static native void glTexParameterfv(int target, int pname, java.nio.FloatBuffer params);

    // C function void glTexParameteri ( GLenum target, GLenum pname, GLint param )
    public static native void glTexParameteri(int target, int pname, int param);

    // C function void glTexParameteriv ( GLenum target, GLenum pname, const GLint *params )
    public static native void glTexParameteriv(int target, int pname, int[] params, int offset);

    // C function void glTexParameteriv ( GLenum target, GLenum pname, const GLint *params )
    public static native void glTexParameteriv(int target, int pname, java.nio.IntBuffer params);

    // C function void glTexSubImage2D ( GLenum target, GLint level, GLint xoffset, GLint yoffset, GLsizei width, GLsizei height, GLenum format, GLenum type, const GLvoid *pixels )
    public static native void glTexSubImage2D(int target, int level, int xoffset, int yoffset, int width, int height, int format, int type, java.nio.Buffer pixels);

    // C function void glUniform1f ( GLint location, GLfloat x )
    public static native void glUniform1f(int location, float x);

    // C function void glUniform1fv ( GLint location, GLsizei count, const GLfloat *v )
    public static native void glUniform1fv(int location, int count, float[] v, int offset);

    // C function void glUniform1fv ( GLint location, GLsizei count, const GLfloat *v )
    public static native void glUniform1fv(int location, int count, java.nio.FloatBuffer v);

    // C function void glUniform1i ( GLint location, GLint x )
    public static native void glUniform1i(int location, int x);

    // C function void glUniform1iv ( GLint location, GLsizei count, const GLint *v )
    public static native void glUniform1iv(int location, int count, int[] v, int offset);

    // C function void glUniform1iv ( GLint location, GLsizei count, const GLint *v )
    public static native void glUniform1iv(int location, int count, java.nio.IntBuffer v);

    // C function void glUniform2f ( GLint location, GLfloat x, GLfloat y )
    public static native void glUniform2f(int location, float x, float y);

    // C function void glUniform2fv ( GLint location, GLsizei count, const GLfloat *v )
    public static native void glUniform2fv(int location, int count, float[] v, int offset);

    // C function void glUniform2fv ( GLint location, GLsizei count, const GLfloat *v )
    public static native void glUniform2fv(int location, int count, java.nio.FloatBuffer v);

    // C function void glUniform2i ( GLint location, GLint x, GLint y )
    public static native void glUniform2i(int location, int x, int y);

    // C function void glUniform2iv ( GLint location, GLsizei count, const GLint *v )
    public static native void glUniform2iv(int location, int count, int[] v, int offset);

    // C function void glUniform2iv ( GLint location, GLsizei count, const GLint *v )
    public static native void glUniform2iv(int location, int count, java.nio.IntBuffer v);

    // C function void glUniform3f ( GLint location, GLfloat x, GLfloat y, GLfloat z )
    public static native void glUniform3f(int location, float x, float y, float z);

    // C function void glUniform3fv ( GLint location, GLsizei count, const GLfloat *v )
    public static native void glUniform3fv(int location, int count, float[] v, int offset);

    // C function void glUniform3fv ( GLint location, GLsizei count, const GLfloat *v )
    public static native void glUniform3fv(int location, int count, java.nio.FloatBuffer v);

    // C function void glUniform3i ( GLint location, GLint x, GLint y, GLint z )
    public static native void glUniform3i(int location, int x, int y, int z);

    // C function void glUniform3iv ( GLint location, GLsizei count, const GLint *v )
    public static native void glUniform3iv(int location, int count, int[] v, int offset);

    // C function void glUniform3iv ( GLint location, GLsizei count, const GLint *v )
    public static native void glUniform3iv(int location, int count, java.nio.IntBuffer v);

    // C function void glUniform4f ( GLint location, GLfloat x, GLfloat y, GLfloat z, GLfloat w )
    public static native void glUniform4f(int location, float x, float y, float z, float w);

    // C function void glUniform4fv ( GLint location, GLsizei count, const GLfloat *v )
    public static native void glUniform4fv(int location, int count, float[] v, int offset);

    // C function void glUniform4fv ( GLint location, GLsizei count, const GLfloat *v )
    public static native void glUniform4fv(int location, int count, java.nio.FloatBuffer v);

    // C function void glUniform4i ( GLint location, GLint x, GLint y, GLint z, GLint w )
    public static native void glUniform4i(int location, int x, int y, int z, int w);

    // C function void glUniform4iv ( GLint location, GLsizei count, const GLint *v )
    public static native void glUniform4iv(int location, int count, int[] v, int offset);

    // C function void glUniform4iv ( GLint location, GLsizei count, const GLint *v )
    public static native void glUniform4iv(int location, int count, java.nio.IntBuffer v);

    // C function void glUniformMatrix2fv ( GLint location, GLsizei count, GLboolean transpose, const GLfloat *value )
    public static native void glUniformMatrix2fv(int location, int count, boolean transpose, float[] value, int offset);

    // C function void glUniformMatrix2fv ( GLint location, GLsizei count, GLboolean transpose, const GLfloat *value )
    public static native void glUniformMatrix2fv(int location, int count, boolean transpose, java.nio.FloatBuffer value);

    // C function void glUniformMatrix3fv ( GLint location, GLsizei count, GLboolean transpose, const GLfloat *value )
    public static native void glUniformMatrix3fv(int location, int count, boolean transpose, float[] value, int offset);

    // C function void glUniformMatrix3fv ( GLint location, GLsizei count, GLboolean transpose, const GLfloat *value )
    public static native void glUniformMatrix3fv(int location, int count, boolean transpose, java.nio.FloatBuffer value);

    // C function void glUniformMatrix4fv ( GLint location, GLsizei count, GLboolean transpose, const GLfloat *value )
    public static native void glUniformMatrix4fv(int location, int count, boolean transpose, float[] value, int offset);

    // C function void glUniformMatrix4fv ( GLint location, GLsizei count, GLboolean transpose, const GLfloat *value )
    public static native void glUniformMatrix4fv(int location, int count, boolean transpose, java.nio.FloatBuffer value);

    // C function void glUseProgram ( GLuint program )
    public static native void glUseProgram(int program);

    // C function void glValidateProgram ( GLuint program )
    public static native void glValidateProgram(int program);

    // C function void glVertexAttrib1f ( GLuint indx, GLfloat x )
    public static native void glVertexAttrib1f(int indx, float x);

    // C function void glVertexAttrib1fv ( GLuint indx, const GLfloat *values )
    public static native void glVertexAttrib1fv(int indx, float[] values, int offset);

    // C function void glVertexAttrib1fv ( GLuint indx, const GLfloat *values )
    public static native void glVertexAttrib1fv(int indx, java.nio.FloatBuffer values);

    // C function void glVertexAttrib2f ( GLuint indx, GLfloat x, GLfloat y )
    public static native void glVertexAttrib2f(int indx, float x, float y);

    // C function void glVertexAttrib2fv ( GLuint indx, const GLfloat *values )
    public static native void glVertexAttrib2fv(int indx, float[] values, int offset);

    // C function void glVertexAttrib2fv ( GLuint indx, const GLfloat *values )
    public static native void glVertexAttrib2fv(int indx, java.nio.FloatBuffer values);

    // C function void glVertexAttrib3f ( GLuint indx, GLfloat x, GLfloat y, GLfloat z )
    public static native void glVertexAttrib3f(int indx, float x, float y, float z);

    // C function void glVertexAttrib3fv ( GLuint indx, const GLfloat *values )
    public static native void glVertexAttrib3fv(int indx, float[] values, int offset);

    // C function void glVertexAttrib3fv ( GLuint indx, const GLfloat *values )
    public static native void glVertexAttrib3fv(int indx, java.nio.FloatBuffer values);

    // C function void glVertexAttrib4f ( GLuint indx, GLfloat x, GLfloat y, GLfloat z, GLfloat w )
    public static native void glVertexAttrib4f(int indx, float x, float y, float z, float w);

    // C function void glVertexAttrib4fv ( GLuint indx, const GLfloat *values )
    public static native void glVertexAttrib4fv(int indx, float[] values, int offset);

    // C function void glVertexAttrib4fv ( GLuint indx, const GLfloat *values )
    public static native void glVertexAttrib4fv(int indx, java.nio.FloatBuffer values);

    // C function void glVertexAttribPointer ( GLuint indx, GLint size, GLenum type, GLboolean normalized, GLsizei stride, GLint offset )
    public static native void glVertexAttribPointer(int indx, int size, int type, boolean normalized, int stride, int offset);

    // C function void glVertexAttribPointer ( GLuint indx, GLint size, GLenum type, GLboolean normalized, GLsizei stride, const GLvoid *ptr )
    private static native void glVertexAttribPointerBounds(int indx, int size, int type, boolean normalized, int stride, java.nio.Buffer ptr, int remaining);

    public static void glVertexAttribPointer(int indx, int size, int type, boolean normalized, int stride, java.nio.Buffer ptr) {
        android.opengl.GLES20.glVertexAttribPointerBounds(indx, size, type, normalized, stride, ptr, ptr.remaining());
    }

    // C function void glViewport ( GLint x, GLint y, GLsizei width, GLsizei height )
    public static native void glViewport(int x, int y, int width, int height);
}

