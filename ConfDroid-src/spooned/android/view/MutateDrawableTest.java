/**
 * Copyright (C) 2009 The Android Open Source Project
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


public class MutateDrawableTest extends android.test.ActivityInstrumentationTestCase2<android.view.MutateDrawable> {
    private android.view.View mFirstButton;

    private android.view.View mSecondButton;

    public MutateDrawableTest() {
        super("com.android.frameworks.coretests", android.view.MutateDrawable.class);
    }

    @java.lang.Override
    protected void setUp() throws java.lang.Exception {
        super.setUp();
        mFirstButton = getActivity().findViewById(com.android.frameworks.coretests.R.id.a);
        mSecondButton = getActivity().findViewById(com.android.frameworks.coretests.R.id.b);
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testSetUpConditions() throws java.lang.Exception {
        assertNotNull(mFirstButton);
        assertNotNull(mSecondButton);
        assertNotSame(mFirstButton.getBackground(), mSecondButton.getBackground());
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testDrawableCanMutate() throws java.lang.Exception {
        assertNotSame(mFirstButton.getBackground().getConstantState(), mSecondButton.getBackground().getConstantState());
    }
}

