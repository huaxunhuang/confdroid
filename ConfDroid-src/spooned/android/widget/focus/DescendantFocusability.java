/**
 * Copyright (C) 2008 The Android Open Source Project
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
package android.widget.focus;


public class DescendantFocusability extends android.app.Activity {
    public android.view.ViewGroup beforeDescendants;

    public android.widget.Button beforeDescendantsChild;

    public android.view.ViewGroup afterDescendants;

    public android.widget.Button afterDescendantsChild;

    public android.view.ViewGroup blocksDescendants;

    public android.widget.Button blocksDescendantsChild;

    @java.lang.Override
    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.descendant_focusability);
        beforeDescendants = ((android.view.ViewGroup) (findViewById(R.id.beforeDescendants)));
        beforeDescendantsChild = ((android.widget.Button) (beforeDescendants.getChildAt(0)));
        afterDescendants = ((android.view.ViewGroup) (findViewById(R.id.afterDescendants)));
        afterDescendantsChild = ((android.widget.Button) (afterDescendants.getChildAt(0)));
        blocksDescendants = ((android.view.ViewGroup) (findViewById(R.id.blocksDescendants)));
        blocksDescendantsChild = ((android.widget.Button) (blocksDescendants.getChildAt(0)));
    }
}

