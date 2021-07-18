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
package android.view;


/**
 * Exercise <merge /> tag in XML files.
 */
public class Merge extends android.app.Activity {
    private android.widget.LinearLayout mLayout;

    @java.lang.Override
    protected void onCreate(android.os.Bundle icicle) {
        super.onCreate(icicle);
        mLayout = new android.widget.LinearLayout(this);
        mLayout.setOrientation(android.widget.LinearLayout.VERTICAL);
        android.view.LayoutInflater.from(this).inflate(R.layout.merge_tag, mLayout);
        setContentView(mLayout);
    }

    public android.view.ViewGroup getLayout() {
        return mLayout;
    }
}

