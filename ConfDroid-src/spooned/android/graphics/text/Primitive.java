/**
 * Copyright (C) 2018 The Android Open Source Project
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
package android.graphics.text;


// Based on the native implementation of Primitive in
// frameworks/base/core/jni/android_text_StaticLayout.cpp revision b808260
public class Primitive {
    @android.annotation.NonNull
    public final android.graphics.text.Primitive.PrimitiveType type;

    public final int location;

    // The following fields don't make sense for all types.
    // Box and Glue have width only.
    // Penalty has both width and penalty.
    // Word_break has penalty only.
    public final float width;

    public final float penalty;

    /**
     * Use {@code PrimitiveType#getNewPrimitive()}
     */
    private Primitive(@android.annotation.NonNull
    android.graphics.text.Primitive.PrimitiveType type, int location, float width, float penalty) {
        this.type = type;
        this.location = location;
        this.width = width;
        this.penalty = penalty;
    }

    public static enum PrimitiveType {

        /**
         * Something with a constant width that is to be typeset - like a character.
         */
        BOX,
        /**
         * Blank space with fixed width.
         */
        GLUE,
        /**
         * Aesthetic cost indicating how desirable breaking at this point will be. A penalty of
         * {@link #PENALTY_INFINITY} means a forced non-break, whereas a penalty of negative
         * {@code #PENALTY_INFINITY} means a forced break.
         * <p/>
         * Currently, it only stores penalty with values 0 or -infinity.
         */
        PENALTY,
        /**
         * For tabs - variable width space.
         */
        VARIABLE,
        /**
         * Possible breakpoints within a word. Think of this as a high cost {@link #PENALTY}.
         */
        WORD_BREAK;
        public android.graphics.text.Primitive getNewPrimitive(int location) {
            assert this == android.graphics.text.Primitive.PrimitiveType.VARIABLE;
            return new android.graphics.text.Primitive(this, location, 0.0F, 0.0F);
        }

        public android.graphics.text.Primitive getNewPrimitive(int location, float value) {
            assert ((this == android.graphics.text.Primitive.PrimitiveType.BOX) || (this == android.graphics.text.Primitive.PrimitiveType.GLUE)) || (this == android.graphics.text.Primitive.PrimitiveType.WORD_BREAK);
            if ((this == android.graphics.text.Primitive.PrimitiveType.BOX) || (this == android.graphics.text.Primitive.PrimitiveType.GLUE)) {
                return new android.graphics.text.Primitive(this, location, value, 0.0F);
            } else {
                return new android.graphics.text.Primitive(this, location, 0.0F, value);
            }
        }

        public android.graphics.text.Primitive getNewPrimitive(int location, float width, float penalty) {
            assert this == android.graphics.text.Primitive.PrimitiveType.PENALTY;
            return new android.graphics.text.Primitive(this, location, width, penalty);
        }

        // forced non-break, negative infinity is forced break.
        public static final float PENALTY_INFINITY = 1.0E7F;
    }
}

