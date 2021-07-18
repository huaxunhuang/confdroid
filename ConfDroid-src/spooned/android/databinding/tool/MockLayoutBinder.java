/**
 * Copyright (C) 2015 The Android Open Source Project
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *      http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package android.databinding.tool;


public class MockLayoutBinder extends android.databinding.tool.LayoutBinder {
    public MockLayoutBinder() {
        super(new android.databinding.tool.store.ResourceBundle.LayoutFileBundle(new java.io.File("./blah.xml"), "blah.xml", "layout", "com.test.submodule", false));
    }

    public android.databinding.tool.expr.IdentifierExpr addVariable(java.lang.String name, java.lang.String type, android.databinding.tool.store.Location location) {
        return super.addVariable(name, type, location, true);
    }
}

