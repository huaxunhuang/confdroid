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
package android.support.doclava;


/**
 * This class is used to hold complex argument(s) to doclava
 */
public class DoclavaJavadocOptionFileOption implements org.gradle.external.javadoc.JavadocOptionFileOption<java.lang.Iterable<java.lang.String>> {
    private final java.lang.String option;

    private java.lang.Iterable<java.lang.String> args;

    public DoclavaJavadocOptionFileOption(java.lang.String option) {
        this.option = option;
    }

    public java.lang.Iterable<java.lang.String> getValue() {
        return args;
    }

    public void setValue(java.lang.Iterable<java.lang.String> args) {
        this.args = args;
    }

    public java.lang.String getOption() {
        return option;
    }

    public void write(org.gradle.external.javadoc.internal.JavadocOptionFileWriterContext writerContext) throws java.io.IOException {
        writerContext.writeOptionHeader(getOption());
        if (args != null) {
            java.util.Iterator<java.lang.String> iter = args.iterator();
            while (true) {
                writerContext.writeValue(iter.next());
                if (!iter.hasNext()) {
                    break;
                }
                writerContext.write(" ");
            } 
        }
        writerContext.newLine();
    }
}

